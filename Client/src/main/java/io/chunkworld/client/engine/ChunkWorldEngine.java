package io.chunkworld.client.engine;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.core.injection.Injector;
import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.assets.urn.ResourceUrn;
import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.context.CoreRegistry;
import io.chunkworld.api.core.context.internal.ContextImpl;
import io.chunkworld.api.core.ecs.entity.system.EntitySystemManager;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.injection.InjectionHelper;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.status.ChunkWorldEngineStatus;
import io.chunkworld.api.core.status.EngineStatus;
import io.chunkworld.api.core.status.EngineStatusUpdatedEvent;
import io.chunkworld.api.core.status.StandardGameStatus;
import io.chunkworld.api.core.systems.EngineSubsystem;
import io.chunkworld.api.core.time.EngineTime;
import lombok.Getter;

import java.util.Deque;
import java.util.Iterator;
import java.util.Map;

public class ChunkWorldEngine implements GameEngine {
    @Getter private GameState state;
    @Getter private GameState pendingState;
    @Getter private volatile boolean shutdownRequested;
    @Getter private volatile boolean running;
    @Getter private Deque<EngineSubsystem> allSubsystems;
    @Getter private EngineStatus status = StandardGameStatus.UNSTARTED;
    @Getter private Context rootContext;
    @In private EngineTime time;
    @In private EntitySystemManager systemManager;
    private final Map<ResourceUrn, EngineSubsystem> registeredSubsystems;

    /**
     * Contains objects that life for the duration of this engine.
     */

    public ChunkWorldEngine() {
        this.rootContext = new ContextImpl();
        CoreRegistry.setContext(rootContext);
        this.allSubsystems = Queues.newArrayDeque();
        this.registeredSubsystems = Maps.newConcurrentMap();
    }

    /**
     * Adds a sub system to the engine
     *
     * @param system the systems
     */
    public void addSubsystem(EngineSubsystem system) {
        var urn = new ResourceUrn("engine", "subsystems", system.getName().toLowerCase());
        if (!registeredSubsystems.containsKey(urn)) {
            this.allSubsystems.add(system);
            registeredSubsystems.put(urn, system);
        }
    }

    /**
     * Removes a sub system with the given urn
     *
     * @param urn the urn
     */
    @Override
    public EngineSubsystem removeSubsystem(ResourceUrn urn) {
        var system = registeredSubsystems.get(urn);
        if (system != null) {
            allSubsystems.remove(system);
            registeredSubsystems.remove(urn);
        }
        return system;
    }

    /**
     * The main run loop, will tick the game
     *
     * @param initialState the initial state
     */
    @Override
    public void run(GameState initialState) {
        changeStatus(StandardGameStatus.RUNNING);
        running = true;
        initialize(initialState);
        while (tick()) {
        }
    }

    /**
     * Initialize the engine
     */
    private void initialize(GameState initialState) {
        rootContext.put(GameEngine.class, this);

        preInitSubsystems();
        //Managers should be created before the subsystems are injected
        initManagers();

        //Init the loadingState before initializing the sub systems
        changeState(initialState);

        //Anything injectable should be initialized inside of the pre init so it can be injected and used in main init
        injectSubsystems();

        initSubsystems();


        postInitSubsystems();


    }


    /**
     * Step the engine a single time
     *
     * @return returns true if the engine is stepping
     */
    private boolean tick() {
        if (shutdownRequested)
            return false;
        processPendingState();
        if (state == null) {
            shutdown();
            return false;
        }
        update();
        return true;
    }

    /**
     * Updates the sub systems
     */
    private void update() {
        Iterator<Float> updateCycles = time.tick();

        for (EngineSubsystem subsystem : allSubsystems) {
            subsystem.preUpdate(state);
        }

        while (updateCycles.hasNext()) {
            float updateDelta = updateCycles.next(); // gameTime gets updated here!
            state.update(updateDelta);
        }

        //Process the entity systems
        if (systemManager != null)
            systemManager.process();

        for (EngineSubsystem subsystem : allSubsystems)
            subsystem.postUpdate(state);

    }

    /**
     * Process the pending state
     */
    private void processPendingState() {
        if (pendingState != null) {
            switchState(pendingState);
            pendingState = null;
        }
    }

    /**
     * Gives a chance to subsystems to do something BEFORE managers and Time are initialized.
     */
    private void preInitSubsystems() {
        changeStatus(ChunkWorldEngineStatus.PREPARING_SUBSYSTEMS);
        for (EngineSubsystem subsystem : allSubsystems) {
            changeStatus(() -> "Pre-initialising " + subsystem.getName() + " subsystem");
            subsystem.preInitialise(rootContext);
        }
    }


    /**
     * Initialize the managers
     */
    private void initManagers() {
    }

    /**
     * This class will inject all of the variables inside the root context
     */
    private void injectSubsystems() {
//        InjectionHelper.injectGenerics(this, rootContext);
        Injector.GENERICS.inject(this, true);
        for (EngineSubsystem subsystem : allSubsystems) {
            InjectionHelper.share(subsystem);
        }
        changeStatus(ChunkWorldEngineStatus.INJECTING_INSTANCES);
        for (EngineSubsystem subsystem : allSubsystems) {
            changeStatus(() -> "Scanning for shared variables in " + subsystem.getName() + " subsystem");
            Injector.GENERICS.inject(subsystem, true);
        }
    }

    /**
     * Initialize the sub systems AFTER the injections have taken place
     */
    private void initSubsystems() {
        changeStatus(ChunkWorldEngineStatus.INITIALIZING_SUBSYSTEMS);
        for (EngineSubsystem subsystem : allSubsystems) {
            changeStatus(() -> "Initialising " + subsystem.getName() + " subsystem");
            subsystem.initialise();
        }
    }

    /**
     * Initializes all of the entity systems
     */
    public void initEntitySystems() {
        Injector.GENERICS.inject(this);
        changeStatus(ChunkWorldEngineStatus.INITIALIZING_SUBSYSTEMS);
        systemManager.initialize();
    }


    /**
     * Post initialize the subsystems
     */
    private void postInitSubsystems() {
        for (EngineSubsystem subsystem : allSubsystems) {
            changeStatus(() -> "Post-Initialising " + subsystem.getName() + " subsystem");
            subsystem.postInitialise();
        }
    }


    /**
     * Changes the game state, i.e. to switch from the MainMenu to Ingame via Loading screen
     * (each is a GameState). The change can be immediate, if there is no current game
     * state set, or scheduled, when a current state exists and the new state is stored as
     * pending. That been said, scheduled changes occurs in the main loop through the call
     * processStateChanges(). As such, from a user perspective in normal circumstances,
     * scheduled changes are likely to be perceived as immediate.
     */
    @Override
    public void changeState(GameState newState) {
        if (state != null) {
            pendingState = newState;    // scheduled change
        } else {
            switchState(newState);      // immediate change
        }
    }

    /**
     * Updates the status and posts the event for the update
     *
     * @param newStatus the new status
     */
    private void changeStatus(EngineStatus newStatus) {
        Bus.Logic.post(new EngineStatusUpdatedEvent(this.status, newStatus));
        status = newStatus;
        //System.out.println("Updated status: " + newStatus.getDescription());
    }

    /**
     * Switch the game state
     *
     * @param newState the new state
     */
    private void switchState(GameState newState) {
        if (state != null) {
            state.dispose();
        }
        state = newState;
        Injector.GENERICS.inject(state, false);
        newState.init();
        //We want to re-inject the sub systems after the state has changed, as somethings may be updated now
        injectSubsystems();
    }

    @Override
    public boolean hasPendingState() {
        return pendingState != null;
    }

    @Override
    public Context createChildContext() {
        return new ContextImpl(rootContext);
    }

    @Override
    public void shutdown() {
        shutdownRequested = true;
    }

}

package io.chunkworld.client.engine;

import com.google.common.collect.Queues;
import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.core.annotations.In;
import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.context.CoreRegistry;
import io.chunkworld.api.core.context.internal.ContextImpl;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.injection.InjectionHelper;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.status.ChunkWorldEngineStatus;
import io.chunkworld.api.core.status.EngineStatus;
import io.chunkworld.api.core.status.EngineStatusUpdatedEvent;
import io.chunkworld.api.core.status.StandardGameStatus;
import io.chunkworld.api.core.systems.EngineSubsystem;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.api.core.time.Time;
import lombok.Getter;

import java.util.Deque;
import java.util.Iterator;

public class ChunkWorldEngine implements GameEngine {
    @Getter
    private GameState state;
    private GameState pendingState;
    @Getter
    private volatile boolean shutdownRequested;
    @Getter
    private volatile boolean running;
    @Getter
    private Deque<EngineSubsystem> allSubsystems;
    @Getter
    private EngineStatus status = StandardGameStatus.UNSTARTED;

    @In
    private EngineTime time;

    /**
     * Contains objects that life for the duration of this engine.
     */
    @Getter
    private Context rootContext;

    public ChunkWorldEngine() {
        this.rootContext = new ContextImpl();
        CoreRegistry.setContext(rootContext);
        this.allSubsystems = Queues.newArrayDeque();
    }

    /**
     * Adds a sub system to the engine
     *
     * @param systems the systems
     */
    public void addSubsystem(EngineSubsystem systems) {
        this.allSubsystems.add(systems);
    }


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
            subsystem.preUpdate(state, time.getRealDelta());
        }

        while (updateCycles.hasNext()) {
            float updateDelta = updateCycles.next(); // gameTime gets updated here!
            state.update(updateDelta);
        }

        for (EngineSubsystem subsystem : allSubsystems) {
            subsystem.postUpdate(state, time.getRealDelta());
        }
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
        InjectionHelper.inject(this, rootContext);
        for (EngineSubsystem subsystem : allSubsystems) {
            InjectionHelper.share(subsystem);
        }
        changeStatus(ChunkWorldEngineStatus.INJECTING_INSTANCES);
        for (EngineSubsystem subsystem : allSubsystems) {
            changeStatus(() -> "Scanning for shared variables in " + subsystem.getName() + " subsystem");
            subsystem.inject(rootContext);
        }
    }

    /**
     * Initialize the sub systems AFTER the injections have taken place
     */
    private void initSubsystems() {
        changeStatus(ChunkWorldEngineStatus.INITIALIZING_SUBSYSTEMS);
        for (EngineSubsystem subsystem : allSubsystems) {
            changeStatus(() -> "Initialising " + subsystem.getName() + " subsystem");
            subsystem.initialise(this, rootContext);
        }
    }


    /**
     * Post initialize the subsystems
     */
    private void postInitSubsystems() {
        for (EngineSubsystem subsystem : allSubsystems) {
            changeStatus(() -> "Post-Initialising " + subsystem.getName() + " subsystem");
            subsystem.postInitialise(rootContext);
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
        InjectionHelper.inject(state);
        newState.init();
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

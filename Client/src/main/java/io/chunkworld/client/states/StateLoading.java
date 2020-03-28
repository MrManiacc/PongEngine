package io.chunkworld.client.states;

import com.google.common.collect.Queues;
import io.chunkworld.api.core.injection.Injector;
import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.injection.InjectionHelper;
import io.chunkworld.api.core.modes.LoadProcess;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.engine.ChunkWorldEngine;
import io.chunkworld.client.engine.load.*;
import io.chunkworld.client.engine.subsystems.gui.GuiSubsystem;
import io.chunkworld.client.pong.load.LoadEntities;
import io.chunkworld.client.pong.load.LoadSystems;
import lombok.Getter;

import java.util.Queue;

/**
 * Used to load all of the assets and do other setup tasks
 */
public class StateLoading implements GameState {
    @Getter private int progress;
    @Getter private int maxProgress;
    @In private GameEngine engine;
    @In private EngineTime time;

    private Queue<LoadProcess> loadProcesses = Queues.newArrayDeque();
    private LoadProcess current;

    /**
     * Initialize the client/server
     */
    @Override
    public void init() {
        initClient();
        registerSubsystems();
        popStep();
    }

    /**
     * Initialize the client
     */
    private void initClient() {
        //Initialize the client here
        pushLoadProcess(new LoadConfiguration());
        pushLoadProcess(new LoadAssets());
        pushLoadProcess(new LoadNanovg());
        pushLoadProcess(new LoadClient());
        pushLoadProcess(new LoadEntitySystems());
        pushLoadProcess(new LoadSystems());
        pushLoadProcess(new LoadEntities());
    }

    /**
     * The subsystem to registers
     */
    private void registerSubsystems() {
        engine.addSubsystem(new GuiSubsystem());
    }

    /**
     * Initialize the server
     */
    private void initServer() {
        loadProcesses.add(new LoadServer());
    }

    /**
     * Pop the next load process, and pop the next step
     *
     * @param delta the delta time
     */
    @Override
    public void update(float delta) {
        long startTime = time.getRealTimeInMs();
        while (current != null && time.getRealTimeInMs() - startTime < 20 && !engine.hasPendingState()) {
            if (current.step())
                popStep();
        }
        if (current == null) {
            ((ChunkWorldEngine) engine).initEntitySystems();
            engine.changeState(new StateInGame());
            ((ChunkWorldEngine) engine).getAllSubsystems().forEach(Injector.GENERICS::inject);
        }
    }


    private void popStep() {
        if (current != null) {
            progress += current.getExpectedCost();
        }
        current = null;
        if (!loadProcesses.isEmpty()) {
            current = loadProcesses.remove();
            System.err.println("Load process: " + current.getMessage());
            Injector.GENERICS.inject(current);
            current.begin();
        }
    }

    /**
     * Pushes a load process, and injects the mapped values
     *
     * @param process the process to enqueue
     */
    private void pushLoadProcess(LoadProcess process) {
        loadProcesses.add(process);
        this.maxProgress += process.getExpectedCost();
        Injector.GENERICS.inject(process, true);
    }
}

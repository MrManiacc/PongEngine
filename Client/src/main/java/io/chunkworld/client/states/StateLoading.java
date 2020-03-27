package io.chunkworld.client.states;

import com.google.common.collect.Queues;
import io.chunkworld.api.core.annotations.In;
import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.injection.InjectionHelper;
import io.chunkworld.api.core.modes.LoadProcess;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.engine.load.*;
import lombok.Getter;

import java.util.Queue;

/**
 * Used to load all of the assets and do other setup tasks
 */
public class StateLoading implements GameState {

    private Queue<LoadProcess> loadProcesses = Queues.newArrayDeque();
    private LoadProcess current;
    @Getter
    private int progress;
    @Getter
    private int maxProgress;

    @In
    private GameEngine engine;

    @In

    private EngineTime time;

    /**
     * Initialize the client/server
     */
    @Override
    public void init() {
        initClient();
        popStep();
    }

    /**
     * Initialize the client
     */
    private void initClient() {
        //Initialize the client here
        pushLoadProcess(new RegisterConfig());
        pushLoadProcess(new RegisterAssets());
        pushLoadProcess(new RegisterClient());
    }

    /**
     * Initialize the server
     */
    private void initServer() {
        loadProcesses.add(new RegisterServer());
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
            if (current.step()) {
                popStep();
            }
        }
        if (current == null) {
            engine.changeState(new StateInGame());
        } else {
            float progressValue = (progress + current.getExpectedCost() * current.getProgress()) / maxProgress;
           // System.out.println(current.getMessage() + ": (" + progressValue + "/" + maxProgress + ")");
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
        InjectionHelper.inject(process);
    }
}

package io.chunkworld.client.engine.subsystems.glfw;

import io.chunkworld.api.core.annotations.In;
import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.systems.EngineSubsystem;
import io.chunkworld.api.core.window.IWindow;
import lombok.Getter;

public class GlfwWindowSubsystem implements EngineSubsystem {
    @Getter
    private final String name = "Graphics";
    private IWindow window;
    @In
    private GameEngine engine;

    @Override
    public void preInitialise(Context rootContext) {
        //TODO: create the window here, and upload it to the context
        this.window = new GlfwWindow("ChunkWorld", 1920, 1080, false, true, false);
        rootContext.put(IWindow.class, window);
        window.init();
    }

    /**
     * Check to see if we need to request a shutdown
     *
     * @param currentState The current state
     * @param delta        The total time this frame/update cycle
     */
    @Override
    public void preUpdate(GameState currentState, float delta) {
        if (window.isCloseRequested())
            engine.shutdown();
    }

    /**
     * We want to update the window here
     *
     * @param currentState The current state
     * @param delta        The total time this frame/update cycle
     */
    @Override
    public void postUpdate(GameState currentState, float delta) {
        window.process();
    }
}

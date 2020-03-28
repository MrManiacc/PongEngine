package io.chunkworld.client.engine.subsystems.glfw;

import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.systems.EngineSubsystem;
import io.chunkworld.api.core.window.IWindow;
import io.chunkworld.client.engine.utils.GLUtils;
import lombok.Getter;

public class GlfwWindowSubsystem implements EngineSubsystem {
    @Getter
    private final String name = "graphics";
    private IWindow window;
    @In private GameEngine engine;
    @In private GLUtils glUtils;

    @Override
    public void preInitialise(Context rootContext) {
        //TODO: create the window here, and upload it to the context
        this.window = new GlfwWindow("ChunkWorld", 1350, 900, false, true, false);
        rootContext.put(IWindow.class, window);
        window.init();
    }

    /**
     * Check to see if we need to request a shutdown
     *
     * @param currentState The current state
     */
    @Override
    public void preUpdate(GameState currentState) {
        if (window.isCloseRequested())
            engine.shutdown();
        if (glUtils != null) {
            glUtils.color(0.1960784314f, 0.3921568627f, 0.6588235294f, 1.0f);
            glUtils.clear(true, true);
        }
    }

    /**
     * We want to update the window here
     *
     * @param currentState The current state
     */
    @Override
    public void postUpdate(GameState currentState) {
        window.process();
    }
}

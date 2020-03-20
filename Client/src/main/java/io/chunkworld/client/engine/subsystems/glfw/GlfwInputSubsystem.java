package io.chunkworld.client.engine.subsystems.glfw;

import io.chunkworld.api.core.annotations.In;
import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.input.Input;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.systems.EngineSubsystem;
import io.chunkworld.api.core.window.IWindow;
import lombok.Getter;

public class GlfwInputSubsystem implements EngineSubsystem {
    @Getter
    private final String name = "Input";
    @In
    private IWindow window;
    @In
    private GameEngine engine;
    private GlfwInput input;

    @Override
    public void initialise(GameEngine engine, Context rootContext) {
        this.input = new GlfwInput();
        //TODO: do some kind of check to confirm the window is glfw window
        input.registerCallbacks((GlfwWindow) window);
        rootContext.put(Input.class, input);
    }

    /**
     * TODO: move this somewhere else, we shouldn't be checking for window shutdown here
     *
     * @param currentState The current state
     * @param delta        The total time this frame/update cycle
     */
    @Override
    public void preUpdate(GameState currentState, float delta) {
        if (input.keyPressed(Input.KEY_ESCAPE)) {
            engine.shutdown();
        }
    }

    /**
     * We want to reset the input after the update
     *
     * @param currentState The current state
     * @param delta        The total time this frame/update cycle
     */
    @Override
    public void postUpdate(GameState currentState, float delta) {
        input.reset();
    }
}

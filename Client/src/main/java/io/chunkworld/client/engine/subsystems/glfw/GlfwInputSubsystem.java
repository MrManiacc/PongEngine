package io.chunkworld.client.engine.subsystems.glfw;

import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.input.Input;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.systems.EngineSubsystem;
import io.chunkworld.api.core.window.IWindow;
import lombok.Getter;

public class GlfwInputSubsystem implements EngineSubsystem {
    @Getter
    private final String name = "input";
    @In
    private IWindow window;
    @In
    private GameEngine engine;
    private GlfwInput input;

    @Getter private boolean loaded = false;


    @Override
    public void initialise() {
        this.input = new GlfwInput();
        //TODO: do some kind of check to confirm the window is glfw window
        input.registerCallbacks((GlfwWindow) window);
        engine.getRootContext().put(Input.class, input);
        loaded = true;
    }

    /**
     * TODO: move this somewhere else, we shouldn't be checking for window shutdown here
     *
     * @param currentState The current state
     */
    @Override
    public void preUpdate(GameState currentState) {
        if (input.keyPressed(Input.KEY_ESCAPE)) {
            engine.shutdown();
        }
    }

    /**
     * We want to reset the input after the update
     *
     * @param currentState The current state
     */
    @Override
    public void postUpdate(GameState currentState) {
        input.reset();
    }
}

package io.chunkworld.client.engine.subsystems.gui;

import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.assets.type.AssetManager;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.input.Input;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.systems.EngineSubsystem;
import io.chunkworld.client.states.StateInGame;
import io.chunkworld.client.states.StateLoading;
import lombok.Getter;

/**
 * The overseer of all things related to gui
 */
public class GuiSubsystem implements EngineSubsystem {
    @Getter private final String name = "loading_gui";
    @In private Input input;
    @In private AssetManager assetManager;
    @In private GameEngine engine;
    @Getter private boolean loaded = false;

    @Override
    public void initialise() {
        loaded = true;
    }

    /**
     * Update the gui menu
     *
     * @param currentState The current state
     */
    @Override
    public void postUpdate(GameState currentState) {
        if (currentState instanceof StateLoading) {

        } else if (currentState instanceof StateInGame) {

        }
    }
}

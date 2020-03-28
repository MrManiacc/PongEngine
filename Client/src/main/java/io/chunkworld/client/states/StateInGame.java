package io.chunkworld.client.states;

import io.chunkworld.api.core.state.GameState;
import lombok.Getter;

/**
 * The main game state. This is where all of the logic for the game will be ran
 */
public class StateInGame implements GameState {
    @Getter private final int progress = 0;
    @Getter private final int maxProgress = 0;

    /**
     * Initialize the in game state
     */
    @Override
    public void init() {
    }


    /**
     * Update the main game state
     *
     * @param delta the delta time
     */
    @Override
    public void update(float delta) {

    }

}

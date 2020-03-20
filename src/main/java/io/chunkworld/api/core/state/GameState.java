package io.chunkworld.api.core.state;

import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.engine.GameEngine;

/**
 * @version 0.1
 * <p>
 * A GameState encapsulates a different set of systems and managers being initialized
 * on state change and updated every iteration of the main loop (every frame). Existing
 * GameState implementations do not necessarily represent a state of play.
 * I.e. interacting with the Main Menu is handled through a GameState.
 */
public interface GameState {

    default void init() {
    }

    default void dispose(boolean shuttingDown) {
    }

    default void dispose() {
        dispose(false);
    }

    default void handleInput(float delta) {
    }

    default void update(float delta) {
    }

    default void render() {
    }


    int getProgress();

    int getMaxProgress();


}

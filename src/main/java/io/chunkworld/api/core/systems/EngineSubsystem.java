package io.chunkworld.api.core.systems;

import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.injection.InjectionHelper;
import io.chunkworld.api.core.injection.Injector;
import io.chunkworld.api.core.state.GameState;

public interface EngineSubsystem {

    /**
     * @return The name of the subsystem
     */
    String getName();


    /**
     * Called on each system before initialisation. This is an opportunity to add anything into the root context that will carry across the entire rune
     * of the engine, and may be used by other systems
     */
    default void preInitialise(Context rootContext) {
    }

    /**
     * Called to initialise the system
     */
    default void initialise() {
    }


    /**
     * Called to do any final initialisation after asset types are registered.
     */
    default void postInitialise() {
    }

    /**
     * Called before the main game logic update, once a frame/full update cycle
     *
     * @param currentState The current state
     */
    default void preUpdate(GameState currentState) {
    }

    /**
     * Called after the main game logic update, once a frame/full update cycle
     *
     * @param currentState The current state
     */
    default void postUpdate(GameState currentState) {
    }

    /**
     * Called when the system is being shut down
     */
    default void shutdown() {
    }

    /**
     * Register the component systems
     *
     * @param componentSystemManager the component manager
     */
    default void registerComponentSystem(ComponentSystemManager componentSystemManager) {
    }
}

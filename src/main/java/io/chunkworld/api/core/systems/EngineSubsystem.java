package io.chunkworld.api.core.systems;

import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.injection.InjectionHelper;
import io.chunkworld.api.core.state.GameState;

public interface EngineSubsystem {

    /**
     * @return The name of the subsystem
     */
    String getName();

    /**
     * Injects all variables with the in value
     *
     * @param context the context to use for injection
     */
    default void inject(Context context) {
        InjectionHelper.inject(this, context);
    }

    /**
     * Called on each system before initialisation. This is an opportunity to add anything into the root context that will carry across the entire rune
     * of the engine, and may be used by other systems
     *
     * @param rootContext The root context, that will survive the entire run of the engine
     */
    default void preInitialise(Context rootContext) {
    }

    /**
     * Called to initialise the system
     *
     * @param engine      The game engine
     * @param rootContext The root context, that will survive the entire run of the engine
     */
    default void initialise(GameEngine engine, Context rootContext) {
    }


    /**
     * Called to do any final initialisation after asset types are registered.
     */
    default void postInitialise(Context context) {
    }

    /**
     * Called before the main game logic update, once a frame/full update cycle
     *
     * @param currentState The current state
     * @param delta        The total time this frame/update cycle
     */
    default void preUpdate(GameState currentState, float delta) {
    }

    /**
     * Called after the main game logic update, once a frame/full update cycle
     *
     * @param currentState The current state
     * @param delta        The total time this frame/update cycle
     */
    default void postUpdate(GameState currentState, float delta) {
    }

    /**
     * Called just prior to shutdown.
     */
    default void preShutdown() {
    }

    default void shutdown() {
    }

    default void registerSystems(ComponentSystemManager componentSystemManager) {
    }
}

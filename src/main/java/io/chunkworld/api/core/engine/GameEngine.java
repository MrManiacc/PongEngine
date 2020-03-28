package io.chunkworld.api.core.engine;

import io.chunkworld.api.core.assets.urn.ResourceUrn;
import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.status.EngineStatus;
import io.chunkworld.api.core.systems.EngineSubsystem;

import java.util.Collection;

public interface GameEngine {

    /**
     * @return The current, fine-grained status of the engine.
     */
    EngineStatus getStatus();

    /**
     * Adds all subsystems
     *
     * @param systems the systems
     */
    void addSubsystem(EngineSubsystem systems);

    /**
     * Simple method to add all of the passed sub systems
     *
     * @param systems the systems to add
     */
    default void addSubsystems(Collection<EngineSubsystem> systems) {
        systems.forEach(this::addSubsystem);
    }

    /**
     * Removes a system by the specified urn
     *
     * @param urn the urn
     */
    EngineSubsystem removeSubsystem(ResourceUrn urn);

    /**
     * Helper method to removes a sub system by the urn
     *
     * @param urn
     */
    default EngineSubsystem removeSubsystem(String urn) {
        return removeSubsystem(new ResourceUrn(urn));
    }


    /**
     * Runs the engine, which will block the thread.
     * Invalid for a disposed engine
     */
    void run(GameState initialState);

    /**
     * Request the engine to stop running
     */
    void shutdown();

    /**
     * @return Whether the engine is running - this is true from the point run() is called to the point shutdown is complete
     */
    boolean isRunning();

    /**
     * @return The current state of the engine
     */
    GameState getState();

    /**
     * Clears all states, replacing them with newState
     *
     * @param newState
     */
    void changeState(GameState newState);


    boolean hasPendingState();

    /**
     * Creates a context that provides read access to the objects of the engine context and can
     * be populated with it's own private objects.
     */
    Context createChildContext();

    /**
     * Gets the root engine context
     *
     * @return returns engine context
     */
    Context getRootContext();
}

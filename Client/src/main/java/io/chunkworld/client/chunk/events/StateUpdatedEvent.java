package io.chunkworld.client.chunk.events;

import io.chunkworld.api.chunk.IChunk;
import io.chunkworld.client.chunk.data.StateData;

/**
 * Called when a chunk's state is updated
 */
public class StateUpdatedEvent {
    //the chunk who had a state update
    public final IChunk chunk;
    //old state is state before the update, and newState is the newly set state
    public final StateData oldState, newState;

    public StateUpdatedEvent(IChunk chunk, StateData oldState, StateData newState) {
        this.chunk = chunk;
        this.oldState = oldState;
        this.newState = newState;
    }
}

package io.chunkworld.api.chunk.events;

import io.chunkworld.api.chunk.IChunk;
import io.chunkworld.api.utilities.Direction;

/**
 * Represents an event when a neighbor is added to a chunk
 */
public class NeighborAddedEvent {
    //Added is the new chunk that has been added to the "addedTo" chunk
    public final IChunk added, addedTo;
    public final Direction direction;

    public NeighborAddedEvent(IChunk added, IChunk addedTo, Direction direction) {
        this.added = added;
        this.addedTo = addedTo;
        this.direction = direction;
    }
}

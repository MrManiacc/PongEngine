package io.chunkworld.api.chunk.events;

import io.chunkworld.api.chunk.IChunk;
import io.chunkworld.api.core.utils.Direction;

/**
 * Represents an event when a neighbor is added to a chunk
 */
public class NeighborRemovedEvent {
    //Removed is the chunk that has been removed, removedFrom is the chunk that had the removed neighbor
    public final IChunk removed, removedFrom;
    //The direction that the chunk has been removed from
    public final Direction direction;

    public NeighborRemovedEvent(IChunk removed, IChunk removedFrom, Direction direction) {
        this.removed = removed;
        this.removedFrom = removedFrom;
        this.direction = direction;
    }
}

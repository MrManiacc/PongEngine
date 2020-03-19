package io.chunkworld.api.chunk.events;

import io.chunkworld.api.chunk.IChunk;

/**
 * Called when the given chunk has all of the neighbors
 */
public class AllNeighborsAddedEvent {
    public final IChunk chunk;

    public AllNeighborsAddedEvent(IChunk chunk) {
        this.chunk = chunk;
    }
}

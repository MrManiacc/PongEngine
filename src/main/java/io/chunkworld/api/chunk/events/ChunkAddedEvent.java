package io.chunkworld.api.chunk.events;

import io.chunkworld.api.chunk.IChunk;
import org.joml.Vector3i;

/**
 * Called when a chunk has been added to the chunk provider
 */
public class ChunkAddedEvent {
    //The chunk added
    public final IChunk added;
    public final Vector3i origin;

    public ChunkAddedEvent(IChunk added) {
        this.added = added;
        this.origin = added.origin;
    }
}

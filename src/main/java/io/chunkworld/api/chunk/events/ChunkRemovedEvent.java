package io.chunkworld.api.chunk.events;

import io.chunkworld.api.chunk.IChunk;
import org.joml.Vector3i;

/**
 * Called when a chunk has been removed from the chunk provider
 */
public class ChunkRemovedEvent {
    //The chunk removed
    public final IChunk removed;
    public final Vector3i origin;

    public ChunkRemovedEvent(IChunk removed) {
        this.removed = removed;
        this.origin = removed.origin;
    }
}

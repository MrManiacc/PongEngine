package io.chunkworld.api.chunk.events;

import io.chunkworld.api.chunk.IChunk;
import org.joml.Vector3i;

/**
 * Called when a block has been updated
 */
public class BlockUpdatedEvent {
    //The chunk that had a block update
    public final IChunk sender;
    //The location of the updated block
    public final Vector3i location;
    //oldBlock is what the block was before being set to the newBlock
    public final byte oldBlock, newBlock;

    public BlockUpdatedEvent(IChunk sender, Vector3i location, byte oldBlock, byte newBlock) {
        this.sender = sender;
        this.location = location;
        this.oldBlock = oldBlock;
        this.newBlock = newBlock;
    }
}

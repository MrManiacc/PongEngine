package io.chunkworld.api.chunk.data;

import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.chunk.IChunk;
import io.chunkworld.api.chunk.events.BlockUpdatedEvent;
import io.chunkworld.api.utilities.Constants;
import io.chunkworld.api.utilities.LocationUtils;
import lombok.Getter;
import org.joml.Vector3i;

import java.util.Arrays;

/**
 * Represents the ubiquitous data backing a chunk.
 */
public class BlockData {
    @Getter
    private final byte[] blocks;
    private final IChunk parent;

    public BlockData(IChunk parent) {
        this.parent = parent;
        this.blocks = new byte[Constants.CHUNK_SIZE_CUBED];
        Arrays.fill(blocks, (byte) 0);
    }

    /**
     * Creates a new chunk data with the inpputed blocks
     *
     * @param blocks the blocks to set to
     */
    public BlockData(IChunk parent, byte[] blocks) {
        this.parent = parent;
        assert blocks.length == Constants.CHUNK_SIZE_CUBED;
        this.blocks = blocks;
    }

    /**
     * Set's a block at the given position to the block type
     *
     * @param x     the relative block x
     * @param y     the relative block y
     * @param z     the relative block z
     * @param block the block to set to
     */
    public void setBlock(int x, int y, int z, byte block) {
        int index = LocationUtils.getIndexFromBlock(x, y, z);
        var oldBlock = blocks[index];
        blocks[index] = block;
        Bus.Chunk.post(new BlockUpdatedEvent(parent, new Vector3i(x, y, z), oldBlock, block));
    }

    /**
     * Set's a block at the given index to the given type
     *
     * @param block the block to set
     * @param type  the type of block
     */
    public void setBlock(Vector3i block, byte type) {
        setBlock(block.x, block.y, block.z, type);
    }


    /**
     * Get's a block at the given position by computing it's index
     *
     * @param x the block x
     * @param y the block y
     * @param z the block z
     * @return returns the block at the given position
     */
    public byte getBlock(int x, int y, int z) {
        int index = LocationUtils.getIndexFromBlock(x, y, z);
        return blocks[index];
    }


    /**
     * Get's a block at the given position, this is a wrapper around the chunk data
     *
     * @param block the block
     * @return returns the block at the given position
     */
    public byte getBlock(Vector3i block) {
        return getBlock(block.x, block.y, block.z);
    }

}

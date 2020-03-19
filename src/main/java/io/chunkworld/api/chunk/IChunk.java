package io.chunkworld.api.chunk;

import io.chunkworld.api.chunk.data.BlockData;
import io.chunkworld.api.chunk.data.NeighborData;
import org.joml.Vector3i;

/**
 * This a generic chunk type. This is used to generalize the storage of chunks between client and server.
 * It simply requires a location as the origin
 */
public abstract class IChunk {
    public final Vector3i origin;
    private final BlockData blockData;
    private final NeighborData neighborData;

    public IChunk(int x, int y, int z, byte[] blocks) {
        this.origin = new Vector3i(x, y, z);
        this.blockData = new BlockData(this, blocks);
        this.neighborData = new NeighborData(this);
    }

    public IChunk(int x, int y, int z) {
        this.origin = new Vector3i(x, y, z);
        this.blockData = new BlockData(this);
        this.neighborData = new NeighborData(this);
    }

    /**
     * A simple method to allow for block editing, better than going (getBlockData().setBlock(...))
     *
     * @return returns the block data, which is used to update the chunk
     */
    public BlockData edit() {
        return blockData;
    }

    /**
     * A simple method to allow for neighbor edditing. The naming is to keep it and in line with edit, and other data components
     *
     * @return returns the neighborDat
     */
    public NeighborData neighbors() {
        return neighborData;
    }

    /**
     * Simply outputs the location
     *
     * @return returns the location
     */
    public String toString() {
        return "[X: " + this.origin.x + ", Y: " + this.origin.y + ", Z: " + this.origin.z + "]";
    }
}

package io.chunkworld.api.core.utils;

import org.joml.Vector3i;

import static io.chunkworld.api.core.utils.Constants.CHUNK_SIZE;
import static io.chunkworld.api.core.utils.Constants.CHUNK_SIZE_SQUARED;

public class LocationUtils {
    /**
     * Computes the chunk position for the current location. This position will be an index, chunk root's
     * are stacked one after another, for example, the center chunk would be [0,0,0] while the chunk to the right
     * would be at location [1,0,0], and the chunk above would be [0,1,0]. This is in direct contrast, with the method of
     * using chunk origins as actual block relative origins, where as the center would be still be [0,0,0] and the chunk
     * to the right in this system would be [32, 0, 0]. The reason we're using this system, is because we're going to
     * write out own chunk management system that stores chunks inside a region as an array of chunk[8][8][8]
     *
     * @param in the input position, which should a be a block position in world space
     * @return returns the relatives chunk
     */
    public static Vector3i getRelativeChunk(Vector3i in) {
        var chunkLocation = getRelativeChunk(in.x, in.y, in.z);
        return new Vector3i(chunkLocation[0], chunkLocation[1], chunkLocation[2]);
    }

    /**
     * Computes the chunk position for the current location. This position will be an index, chunk root's
     * are stacked one after another, for example, the center chunk would be [0,0,0] while the chunk to the right
     * would be at location [1,0,0], and the chunk above would be [0,1,0]. This is in direct contrast, with the method of
     * using chunk origins as actual block relative origins, where as the center would be still be [0,0,0] and the chunk
     * to the right in this system would be [32, 0, 0]. The reason we're using this system, is because we're going to
     * write out own chunk management system that stores chunks inside a region as an array of chunk[8][8][8]
     *
     * @param x the x pos
     * @param y the y pos
     * @param z the z pos
     * @return returns the relatives chunk
     */
    private static int[] getRelativeChunk(int x, int y, int z) {
        return new int[]{(int) Math.floor((float) x / CHUNK_SIZE), (int) Math.floor((float) y / CHUNK_SIZE), (int) Math.floor((float) z / CHUNK_SIZE)};
    }

    /**
     * Computes the chunk position for the current location. This position will be an index, chunk root's
     * are stacked one after another, for example, the center chunk would be [0,0,0] while the chunk to the right
     * would be at location [1,0,0], and the chunk above would be [0,1,0]. This is in direct contrast, with the method of
     * using chunk origins as actual block relative origins, where as the center would be still be [0,0,0] and the chunk
     * to the right in this system would be [32, 0, 0]. The reason we're using this system, is because we're going to
     * write out own chunk management system that stores chunks inside a region as an array of chunk[8][8][8]
     *
     * @param in the input position, which should a be a block position in world space
     * @return returns the absolute chunk
     */
    public static Vector3i getAbsoluteChunk(Vector3i in) {
        return getRelativeChunk(in).mul(CHUNK_SIZE);
    }

    /**
     * Computes the chunk position for the current location. This position will be an index, chunk root's
     * are stacked one after another, for example, the center chunk would be [0,0,0] while the chunk to the right
     * would be at location [1,0,0], and the chunk above would be [0,1,0]. This is in direct contrast, with the method of
     * using chunk origins as actual block relative origins, where as the center would be still be [0,0,0] and the chunk
     * to the right in this system would be [32, 0, 0]. The reason we're using this system, is because we're going to
     * write out own chunk management system that stores chunks inside a region as an array of chunk[8][8][8]
     *
     * @param x the x pos
     * @param y the y pos
     * @param z the z pos
     * @return returns the absolute chunk
     */
    private static int[] getAbsoluteChunk(int x, int y, int z) {
        var relative = getRelativeChunk(x, y, z);
        relative[0] *= CHUNK_SIZE;
        relative[1] *= CHUNK_SIZE;
        relative[2] *= CHUNK_SIZE;
        return relative;
    }


    /**
     * Calculates the block position relative to the chunk. The value will be converted from a 3d space such as
     * [1685, 240, -402] to a block position like [21, 16, 14], with the chunk origin in
     * absolute being [1664,224, -416] and in relative being [52, 7, -13]
     * Formula = (blockPos - (floor(blockPos / chunk_size)) * chunk_size)
     *
     * @param in the input block position
     * @return returns the out position
     */
    public static Vector3i getRelativeBlock(Vector3i in) {
        var origin = getAbsoluteChunk(in);
        return new Vector3i(in.x - origin.x, in.y - origin.y, in.z - origin.z);
    }

    /**
     * Calculates the block position relative to the chunk. The value will be converted from a 3d space such as
     * [1685, 240, -402] to a block position like [21, 16, 14], with the chunk origin in
     * absolute being [1664,224, -416] and in relative being [52, 7, -13]
     * Formula = (blockPos - (floor(blockPos / chunk_size)) * chunk_size)
     *
     * @param x the x pos
     * @param y the y pos
     * @param z the z pos
     * @return returns an int[] of relative block position
     */
    private static int[] getRelativeBlock(int x, int y, int z) {
        var origin = getAbsoluteChunk(x, y, z);
        return new int[]{x - origin[0], y - origin[1], z - origin[2]};
    }

    /**
     * Get's a relative 3d position of the block from it's relative index.
     *
     * @param index the index relative to the chunk it's in
     * @return returns a vector3 in relative coordinates, so x, y and z will be in the range of 0 - 31
     */
    public static Vector3i getBlockFromIndex(int index) {
        final int z = index / (CHUNK_SIZE_SQUARED);
        index -= (z * CHUNK_SIZE_SQUARED);
        final int y = index / CHUNK_SIZE;
        final int x = index % CHUNK_SIZE;
        return new Vector3i(x, y, z);
    }


    /**
     * Get's a relative 3d index of a block relative to the chunk
     *
     * @param x        the block x
     * @param y        the block y
     * @param z        the block z
     * @param relative if relative, we won't use the chunk position to compute the block position. The input will be the actual block
     * @return returns the index of the block
     */
    public static int getIndexFromBlock(int x, int y, int z, boolean relative) {
        var xPos = x;
        var yPos = y;
        var zPos = z;
        if (relative) {
            var relLocation = getRelativeBlock(x, y, z);
            xPos = relLocation[x];
            yPos = relLocation[y];
            zPos = relLocation[z];
        }
        return (zPos * CHUNK_SIZE_SQUARED) + (yPos * CHUNK_SIZE) + xPos;
    }

    /**
     * Get's a relative 3d index of a block relative to the chunk
     *
     * @param x the block x
     * @param y the block y
     * @param z the block z
     * @return returns the index of the block
     */
    public static int getIndexFromBlock(int x, int y, int z) {
        return getIndexFromBlock(x, y, z, false);
    }

    /**
     * Get's a relative 3d index of a block relative to the chunk
     *
     * @param block the block position
     * @return returns the index of the block
     */
    public static int getIndexFromBlock(Vector3i block, boolean relative) {
        return getIndexFromBlock(block.x, block.y, block.z, relative);
    }

    /**
     * Get's a relative 3d index of a block relative to the chunk
     *
     * @param block the block position
     * @return returns the index of the block
     */
    public static int getIndexFromBlock(Vector3i block) {
        return getIndexFromBlock(block.x, block.y, block.z, false);
    }


}

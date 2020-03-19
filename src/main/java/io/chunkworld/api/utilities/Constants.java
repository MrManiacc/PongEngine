package io.chunkworld.api.utilities;

/**
 * This is a simple class that stores contstants like
 */
public class Constants {
    //Chunk locational data
    public static final int CHUNK_SIZE = 32; //32x,32x,32x voxels per chunk
    public static final int CHUNK_SIZE_SQUARED = 1024; //This is a simple method to save computation of 32 * 32
    public static final int CHUNK_SIZE_CUBED = 32768; //This is a simple method to save computation of 32 * 32 * 32
}

package io.chunkworld.client;

import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.chunk.ChunkProvider;
import io.chunkworld.api.engine.CoreEngine;
import io.chunkworld.api.utilities.Constants;
import io.chunkworld.client.chunk.Chunk;
import io.chunkworld.client.chunk.data.StateData;
import io.chunkworld.client.chunk.listener.ChunkListener;

public class Client extends CoreEngine {
    public static void main(String[] args) {
        Bus.Chunk.register(new ChunkListener());
        var provider = new ChunkProvider();
        var chunkTest = new Chunk(0, 0, 0, new byte[Constants.CHUNK_SIZE_CUBED]);

        chunkTest.neighbors().addNeighbor(new Chunk(32, 0, 0));
        chunkTest.neighbors().addNeighbor(new Chunk(-32, 0, 0));
        chunkTest.neighbors().addNeighbor(new Chunk(0, 0, 32));
        chunkTest.neighbors().addNeighbor(new Chunk(0, 0, -32));
        chunkTest.neighbors().addNeighbor(new Chunk(0, 32, 0));
        chunkTest.neighbors().addNeighbor(new Chunk(0, -32, 0));

        provider.addChunk(chunkTest);
    }
}

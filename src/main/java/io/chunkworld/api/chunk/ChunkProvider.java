package io.chunkworld.api.chunk;

import com.google.common.collect.Maps;
import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.chunk.events.ChunkAddedEvent;
import io.chunkworld.api.chunk.events.ChunkRemovedEvent;
import org.joml.Vector3i;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * This class will store and manage all the chunks
 */
public class ChunkProvider {
    private final Map<Vector3i, IChunk> chunks;

    public ChunkProvider() {
        chunks = Maps.newConcurrentMap();
    }

    /**
     * Gets a chunk at the given position
     *
     * @param x chunk x
     * @param y chunk y
     * @param z chunk z
     * @return returns the chunk at the given position
     */
    public IChunk chunkAt(int x, int y, int z) {
        return chunkAt(new Vector3i(x, y, z));
    }

    /**
     * Gets a chunk at the given position
     *
     * @param chunk the chunk to get
     * @return returns the chunk at the given position
     */
    public IChunk chunkAt(Vector3i chunk) {
        return chunks.get(chunk);
    }

    /**
     * Checks to see if the given chunk is present
     *
     * @param chunk the chunk position
     * @return returns true if present
     */
    public boolean hasChunk(Vector3i chunk) {
        return chunks.containsKey(chunk);
    }

    /**
     * Checks to see if the given chunk is present
     *
     * @param x chunk x
     * @param y chunk y
     * @param z chunk z
     * @return returns true if present
     */
    public boolean hasChunk(int x, int y, int z) {
        return hasChunk(new Vector3i(x, y, z));
    }

    /**
     * Adds a chunk to the provider and fires the onAddedEvent inside the IChunkEvents
     *
     * @param chunk the chunk to add
     * @return returns false if the chunk is already present
     */
    public boolean addChunk(IChunk chunk) {
        if (hasChunk(chunk.origin))
            return false;
        chunks.put(chunk.origin, chunk);
        Bus.Chunk.post(new ChunkAddedEvent(chunk));
        return true;
    }

    /**
     * Adds a chunk to the provider and fires the onAddedEvent inside the IChunkEvents
     *
     * @param chunks the chunks to add
     * @return returns the total number of added chunks
     */
    public int addChunks(IChunk... chunks) {
        var count = 0;
        for (var chunk : chunks) {
            if (addChunk(chunk))
                count++;
        }
        return count;
    }

    /**
     * Removes a chunk and calls the on removed event inside the IChunkEvents
     *
     * @param chunk the chunk to remove
     * @return returns true if removed
     */
    public boolean removeChunk(IChunk chunk) {
        if (!hasChunk(chunk.origin)) return false;
        chunks.remove(chunk.origin);
        Bus.Chunk.post(new ChunkRemovedEvent(chunk));
        return true;
    }

    /**
     * Removes a chunk and calls the on removed event inside the IChunkEvents
     *
     * @param chunk the chunk to remove
     * @return returns true if removed
     */
    public boolean removeChunk(Vector3i chunk) {
        if (!hasChunk(chunk))
            return false;
        var c = chunks.remove(chunk);
        Bus.Chunk.post(new ChunkRemovedEvent(c));
        return true;
    }

    /**
     * Removes a chunk and calls the on removed event inside the IChunkEvents
     *
     * @param x the chunk x
     * @param y the chunk y
     * @param z the chunk z
     * @return returns true if removed
     */
    public boolean removeChunk(int x, int y, int z) {
        return removeChunk(new Vector3i(x, y, z));
    }

    /**
     * Used to iterate through the chunks
     *
     * @return returns the chunks
     */
    public Collection<IChunk> chunks() {
        return this.chunks.values();
    }

    /**
     * Used to iterate the chunk origins
     *
     * @return returns chunk origins
     */
    public Set<Vector3i> origins() {
        return this.chunks.keySet();
    }

}

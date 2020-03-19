package io.chunkworld.api.chunk.data;

import com.google.common.collect.Maps;
import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.chunk.IChunk;
import io.chunkworld.api.chunk.events.AllNeighborsAddedEvent;
import io.chunkworld.api.chunk.events.NeighborAddedEvent;
import io.chunkworld.api.chunk.events.NeighborRemovedEvent;
import io.chunkworld.api.utilities.Direction;

import java.util.Map;

import static io.chunkworld.api.utilities.Constants.CHUNK_SIZE;

/**
 * This class will store a reference to the chunk's neighbors, or null
 * if the given chunk's neighbor doesn't exist yet
 */
public class NeighborData {
    //The parent chunk
    private final IChunk parent;
    private final Map<Direction, IChunk> neighbors;

    public NeighborData(IChunk parent) {
        this.neighbors = Maps.newConcurrentMap();
        this.parent = parent;
    }

    /**
     * Adds a neighbor, will return true if the chunk is in fact a neighbor
     *
     * @param chunk the chunk to add
     * @return returns true if added
     */
    public boolean addNeighbor(IChunk chunk) {
        var direction = getDirection(chunk);
        if (direction == Direction.INVALID) return false;
        return addNeighbor(direction, chunk);
    }

    /**
     * Adds a neighbor without computing the direction
     *
     * @param direction the direction to add for
     * @param chunk     the chunk to add
     * @return returns true if added
     */
    public boolean addNeighbor(Direction direction, IChunk chunk) {
        if (hasNeighbor(direction))
            return false;
        neighbors.put(direction, chunk);
        Bus.Chunk.post(new NeighborAddedEvent(chunk, parent, direction));
        if (hasAllNeighbors())
            Bus.Chunk.post(new AllNeighborsAddedEvent(parent));
        chunk.neighbors().addNeighbor(parent); //The inverse, this current chunk to the neighbor chunk as a neighbor
        return true;
    }

    /**
     * This will remove a neighbor by it's direction
     *
     * @param direction the direction of the neighbor
     * @return returns true if the neighbor was removed
     */
    public boolean removeNeighbor(Direction direction) {
        if (!neighbors.containsKey(direction)) return false;
        var removed = neighbors.remove(direction);
        Bus.Chunk.post(new NeighborRemovedEvent(removed, parent, direction));
        removed.neighbors().removeNeighbor(parent);//The inverse, if the chunk isn't being deleted, we need to notify it that this chunk has removed it as a neighbor
        return true;
    }

    /**
     * This will remove the neighbor by the chunk reference
     *
     * @param chunk the chunk to remove
     * @return returns true if the neighbor was removed
     */
    public boolean removeNeighbor(IChunk chunk) {
        var direction = getDirection(chunk);
        if (direction == Direction.INVALID)
            return false;
        return removeNeighbor(direction);
    }

    /**
     * Checks to see if the neighbor is present
     *
     * @param direction the direction of the neighbor
     * @return returns true if the neighbor is present
     */
    public boolean hasNeighbor(Direction direction) {
        return neighbors.containsKey(direction);
    }

    /**
     * Returns true if all of the neighbors are present
     *
     * @return true if neighbors present
     */
    public boolean hasAllNeighbors() {
        for (var dir : Direction.values()) {
            if (dir != Direction.INVALID)
                if (!hasNeighbor(dir)) return false;
        }
        return true;
    }


    /**
     * Checks to see if the given chunk is a neighbor to this chunk
     *
     * @param other the chunk to check
     * @return returns true if the chunk is a neighbor of this chunk
     */
    public boolean isNeighbor(IChunk other) {
        return getDirection(other) != Direction.INVALID;
    }

    /**
     * Gets the direction of the other chunk, or invalid if not a neighbor
     *
     * @param other the chunk to get the direction for
     * @return returns the direction or invalid
     */
    public Direction getDirection(IChunk other) {
        var i = parent.origin;
        var j = other.origin;
        if (i.x == j.x && i.y == j.y && i.z == j.z)
            return Direction.INVALID;
        //The chunks are in the same up/down and same forward/back
        if (i.y == j.y && i.z == j.z) {
            if (i.x + CHUNK_SIZE == j.x)
                return Direction.EAST;
            else if (i.x - CHUNK_SIZE == j.x)
                return Direction.WEST;
        } else if (i.y == j.y && i.x == j.x) {
            if (i.z + CHUNK_SIZE == j.z)
                return Direction.NORTH;
            else if (i.z - CHUNK_SIZE == j.z)
                return Direction.SOUTH;
        } else if (i.x == j.x && i.z == j.z)
            if (i.y + CHUNK_SIZE == j.y)
                return Direction.UP;
            else if (i.y - CHUNK_SIZE == j.y)
                return Direction.DOWN;
        return Direction.INVALID;
    }

    /**
     * Get's a neighbor in the given direction, or null if not present
     *
     * @param direction the direction of the neighbor
     * @return returns the neighbor or null
     */
    public IChunk neighbor(Direction direction) {
        return neighbors.get(direction);
    }


    @Override
    public String toString() {
        var builder = new StringBuilder();
        var added = false;
        for (var neighbor : neighbors.keySet()) {
            builder.append(neighbor.name().toLowerCase());
            builder.append(", ");
            added = true;
        }
        var output = builder.toString();
        if (added)
            output = output.substring(0, output.length() - 2);
        return "Neighbors: [" + output + "]";
    }
}

package io.chunkworld.client.chunk;

import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.chunk.IChunk;
import io.chunkworld.client.chunk.data.StateData;
import io.chunkworld.client.chunk.events.StateUpdatedEvent;


/**
 * The chunk should be initialized from the server
 */
public class Chunk extends IChunk {
    private StateData state;

    public Chunk(int x, int y, int z, byte[] blocks) {
        super(x, y, z, blocks);
        this.state = StateData.BLOCKS_LOADED;
    }

    public Chunk(int x, int y, int z) {
        super(x, y, z);
        this.state = StateData.CREATED;
    }

    /**
     * A simple getter for the state, to keep the format of edit()
     *
     * @return returns the state
     */
    public StateData state() {
        return state;
    }

    /**
     * Sets the state data to the input state
     *
     * @param state the state to set
     * @return returns this instance
     */
    public StateData state(StateData state) {
        Bus.Chunk.post(new StateUpdatedEvent(this, this.state, state));
        this.state = state;
        return state;
    }

    /**
     * Used for debugging
     *
     * @return returns the toString
     */
    public String toString() {
        return "Chunk{\n\tState: [" + this.state.name().toLowerCase().replace("_", "-") + "],\n\t" + neighbors() + "\n\tLocation: " + super.toString() + "\n}";
    }
}

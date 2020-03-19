package io.chunkworld.client.chunk.listener;

import com.google.common.eventbus.Subscribe;
import io.chunkworld.api.chunk.events.AllNeighborsAddedEvent;
import io.chunkworld.api.chunk.events.ChunkAddedEvent;
import io.chunkworld.api.chunk.events.NeighborAddedEvent;
import io.chunkworld.client.chunk.Chunk;
import io.chunkworld.client.chunk.data.StateData;
import io.chunkworld.client.chunk.events.StateUpdatedEvent;

import java.text.DecimalFormat;

public class ChunkListener {

    /**
     * Called when a chunk is added
     *
     * @param event the chunk added event
     */
    @Subscribe
    public void onChunkAdded(ChunkAddedEvent event) {
        System.out.println("Added: " + event.added.origin.toString(DecimalFormat.getInstance()));
    }

    /**
     * Called when a neighbor is added
     *
     * @param event the neighbor event
     */
    @Subscribe
    public void onNeighborAdded(NeighborAddedEvent event) {
//        System.out.println(event.added.origin.toString(DecimalFormat.getInstance()) + " added to " + event.addedTo.origin.toString(DecimalFormat.getInstance()) + " for direction: " + event.direction.name().toLowerCase());
    }

    /**
     * Called when all of the neighbors are present for the chunk
     *
     * @param event the neighbor event
     */
    @Subscribe
    public void onAllNeighborsAdded(AllNeighborsAddedEvent event) {
        var chunk = (Chunk) event.chunk;
        chunk.state(StateData.NEIGHBORS_LOADED);
    }


    /**
     * Called when a chunk's state changes
     *
     * @param event state change event
     */
    @Subscribe
    public void onStateChanged(StateUpdatedEvent event) {
        System.out.println(event.chunk.origin.toString(DecimalFormat.getInstance()) + " updated from " + event.oldState.name().toLowerCase() + ", to " + event.newState.name().toLowerCase());
    }
}

package io.chunkworld.api.bus;

import com.google.common.eventbus.EventBus;
import lombok.Getter;

/**
 * represents the different types of event buses
 */
public enum Bus {
    Logic(new EventBus("logic")), Chunk(new EventBus("chunk")), Network(new EventBus("network")), Entity(new EventBus("entity"));
    private EventBus bus;

    Bus(EventBus bus) {
        this.bus = bus;
    }

    /**
     * Posts an event to the given bus
     *
     * @param event the event to post
     */
    public void post(Object event) {
        bus.post(event);
    }

    /**
     * Registers an event listener to the given bus
     *
     * @param listener the listener to register
     */
    public void register(Object listener) {
        bus.register(listener);
    }

    /**
     * Unregisters a listener to the given bus
     *
     * @param listener the listener to unregister
     */
    public void unregister(Object listener) {
        bus.unregister(listener);
    }


}

package io.chunkworld.api.core.time;

import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.api.core.time.TimeBase;

import java.util.Iterator;

/**
 * A generic timer class
 */
public class GenericTime extends TimeBase {

    public GenericTime() {
        super(System.currentTimeMillis());
    }

    /**
     * Simple current time mills
     *
     * @return returns current time
     */
    protected long getRawTimeInMs() {
        return System.currentTimeMillis();
    }
}

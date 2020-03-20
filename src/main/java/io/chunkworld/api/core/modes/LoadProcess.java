package io.chunkworld.api.core.modes;

/**
 * A simple process that will be executed in a queue. This will be used mainly inside of the StateLoading
 */
public interface LoadProcess {
    /**
     * @return A message describing the state of the process
     */
    String getMessage();

    /**
     * Runs a single step.
     *
     * @return Whether the overall process is finished
     */
    boolean step();

    /**
     * Begins the loading
     */
    void begin();

    /**
     * @return The progress of the process, between 0f and 1f inclusive
     */
    float getProgress();

    /**
     * @return A relative cost for this process. A small process would have a cost of 1, a large process a bigger cost.
     */
    int getExpectedCost();
}

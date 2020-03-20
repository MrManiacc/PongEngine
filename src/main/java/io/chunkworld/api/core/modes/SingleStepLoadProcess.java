package io.chunkworld.api.core.modes;

import lombok.Getter;

public abstract class SingleStepLoadProcess implements LoadProcess {
    @Getter
    private final String message;
    @Getter
    private final int expectedCost;

    public SingleStepLoadProcess(String message, int expectedCost) {
        this.message = message;
        this.expectedCost = expectedCost;
    }

    //Ignored
    public void begin() {
    }

    public float getProgress() {
        return 0;
    }
}

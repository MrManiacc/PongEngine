package io.chunkworld.api.core.status;

import lombok.Getter;

public enum StandardGameStatus implements EngineStatus {
    UNSTARTED("Unstarted"),
    LOADING("Loading"),
    RUNNING("Running"),
    SHUTTING_DOWN("Shutting down..."),
    DISPOSED("Shut down");

    @Getter
    private boolean processing = false;
    @Getter
    private float progress = 0;
    @Getter
    private final String description;

    StandardGameStatus(String description) {
        this.description = description;
    }
}

package io.chunkworld.api.core.status;

import lombok.Getter;

public enum ChunkWorldEngineStatus implements EngineStatus {

    PREPARING_SUBSYSTEMS("Preparing Subsystems..."),
    INITIALIZING_ASSET_MANAGEMENT("Initializing Asset Management..."),
    INJECTING_INSTANCES("Injecting instances to Subsystesm..."),
    INITIALIZING_SUBSYSTEMS("Initializing Subsystems..."),
    INITIALIZING_MODULE_MANAGER("Initializing Module Management..."),
    INITIALIZING_LOWLEVEL_OBJECT_MANIPULATION("Initializing low-level object manipulators..."),
    INITIALIZING_ASSET_TYPES("Initializing asset types...");

    private final String defaultDescription;
    @Getter
    private final boolean progressing = false;
    @Getter
    private final int progress = 0;

    ChunkWorldEngineStatus(String defaultDescription) {
        this.defaultDescription = defaultDescription;
    }

    @Override
    public String getDescription() {
        return defaultDescription;
    }

    @Override
    public float getProgress() {
        return 0;
    }


}

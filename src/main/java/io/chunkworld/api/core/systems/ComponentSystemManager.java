package io.chunkworld.api.core.systems;

import io.chunkworld.api.core.context.Context;

/**
 * Simple manager for components.
 * This takes care of registering systems with the core registry
 */
public class ComponentSystemManager {
    private boolean initialised;
    private final Context context;

    public ComponentSystemManager(Context context) {
        this.context = context;
    }
}

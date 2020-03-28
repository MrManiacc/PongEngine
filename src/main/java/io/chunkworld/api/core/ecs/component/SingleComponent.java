package io.chunkworld.api.core.ecs.component;

import io.chunkworld.api.core.assets.urn.ResourceUrn;

/**
 * A single entity component marks an entity to be mapped to the given resource Urn
 */
public class SingleComponent implements Component {
    public final ResourceUrn urn;

    public SingleComponent(ResourceUrn urn) {
        this.urn = urn;
    }

    public SingleComponent(String urn) {
        this(new ResourceUrn(urn));
    }

}

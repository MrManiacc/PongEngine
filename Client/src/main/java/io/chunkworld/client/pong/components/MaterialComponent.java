package io.chunkworld.client.pong.components;

import io.chunkworld.api.core.assets.urn.ResourceUrn;
import io.chunkworld.api.core.ecs.component.Component;

public class MaterialComponent implements Component {
    public final ResourceUrn shapeUrn;
    public final ResourceUrn textureUrn;

    public MaterialComponent(ResourceUrn shapeUrn, ResourceUrn textureUrn) {
        this.shapeUrn = shapeUrn;
        this.textureUrn = textureUrn;
    }

    public MaterialComponent(String shapeUrn, String textureUrn) {
        this(new ResourceUrn(shapeUrn), new ResourceUrn(textureUrn));
    }

}

package io.chunkworld.client.engine.subsystems.gui;

import io.chunkworld.api.core.assets.urn.ResourceUrn;
import lombok.Getter;

/**
 * This represents a generic gui element that can be rendered
 */
public abstract class GuiElement {
    @Getter private ResourceUrn urn;
}

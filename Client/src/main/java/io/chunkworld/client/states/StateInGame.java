package io.chunkworld.client.states;

import io.chunkworld.api.core.annotations.In;
import io.chunkworld.api.core.assets.type.AssetManager;
import io.chunkworld.api.core.assets.urn.ResourceUrn;
import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.context.CoreRegistry;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.input.Input;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.client.engine.assets.texture.Texture;
import lombok.Getter;

public class StateInGame implements GameState {
    @Getter
    private final int progress = 0;
    @Getter
    private final int maxProgress = 0;

    @In
    private Input input;

    @In
    private AssetManager assetManager;

    /**
     * Initialize the in game state
     */
    @Override
    public void init() {
        var textureAsset = assetManager.getAsset(new ResourceUrn("engine:laughing"), Texture.class);
        if (textureAsset.isPresent()) {
            System.out.println("Found texture!");
            var texture = textureAsset.get();
        }
    }

    @Override
    public void update(float delta) {
    }
}

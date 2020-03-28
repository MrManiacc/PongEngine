package io.chunkworld.client.engine.load;

import com.google.common.collect.Queues;
import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.assets.files.AssetSourceResolver;
import io.chunkworld.api.core.assets.type.AssetManager;
import io.chunkworld.api.core.assets.type.AssetTypeManager;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.modes.SingleStepLoadProcess;
import io.chunkworld.client.engine.assets.shader.Shader;
import io.chunkworld.client.engine.assets.shader.ShaderFormat;
import io.chunkworld.client.engine.assets.texture.Texture;
import io.chunkworld.client.engine.assets.texture.TextureFormat;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

/**
 * Registers the asset type manager
 */
public class LoadAssets extends SingleStepLoadProcess {
    @In
    private GameEngine engine;
    private Queue<AssetSourceResolver> unresolved = Queues.newArrayDeque();
    private AssetTypeManager typeManager;

    public LoadAssets() {
        super("Registering asset types...", 1);
    }

    /**
     * Register the asset type registry
     *
     * @return returns true
     */
    @Override
    public boolean step() {
        if (typeManager == null)
            initialize();
        nextUnresolved();
        return unresolved.isEmpty();
    }

    /**
     * Registers the asset resolver things
     */
    private void initialize() {
        typeManager = new AssetTypeManager();
        registerAssetResolvers(typeManager);
        var assetManager = new AssetManager(typeManager);
        engine.getRootContext().put(AssetManager.class, assetManager);
        resolveAssets();
    }

    /**
     * Attempts to load the next source
     */
    private void nextUnresolved() {
        if (this.unresolved.isEmpty()) return;
        var unresolved = this.unresolved.poll();
        if (unresolved == null) return;
        if (!unresolved.resolveAssets()) {
            //If it's unresolved again we put it to the back of the queue
            this.unresolved.add(unresolved);
        }
    }

    /**
     * Register the asset type resolvers
     *
     * @param typeManager the type manager
     */
    private void registerAssetResolvers(AssetTypeManager typeManager) {
        typeManager.registerAssetType(Texture.class, Texture::new, new TextureFormat(), "textures");
        typeManager.registerAssetType(Shader.class, Shader::new, new ShaderFormat(), "shaders");
    }

    /**
     * Resolves the asset file paths for the registered types
     */
    private void resolveAssets() {
        var assetSourceResolver = new AssetSourceResolver(typeManager);
        if (!assetSourceResolver.resolveAssets()) {
            this.unresolved.add(assetSourceResolver);
        }
    }


}

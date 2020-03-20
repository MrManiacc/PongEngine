package io.chunkworld.client.engine.load;

import com.artemis.annotations.PrefabData;
import io.chunkworld.api.core.annotations.In;
import io.chunkworld.api.core.assets.data.AssetFactory;
import io.chunkworld.api.core.assets.files.AssetSourceResolver;
import io.chunkworld.api.core.assets.type.AssetManager;
import io.chunkworld.api.core.assets.type.AssetTypeManager;
import io.chunkworld.api.core.engine.GameEngine;
import io.chunkworld.api.core.modes.SingleStepLoadProcess;
import io.chunkworld.client.engine.assets.shader.Shader;
import io.chunkworld.client.engine.assets.shader.ShaderFormat;
import io.chunkworld.client.engine.assets.texture.Texture;
import io.chunkworld.client.engine.assets.texture.TextureData;
import io.chunkworld.client.engine.assets.texture.TextureFormat;
import org.w3c.dom.Text;

/**
 * Registers the asset type manager
 */
public class RegisterAssets extends SingleStepLoadProcess {
    @In
    private GameEngine engine;

    public RegisterAssets() {
        super("Register asset types", 1);
    }

    /**
     * Register the asset type registry
     *
     * @return returns true
     */
    @Override
    public boolean step() {
        //TODO: register game assets here
        var assetTypeManager = new AssetTypeManager();
        registerAssetResolvers(assetTypeManager);
        var assetManager = new AssetManager(assetTypeManager);
        engine.getRootContext().put(AssetManager.class, assetManager);
        resolveAssets(assetTypeManager);
        return true;
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
     *
     * @param typeManager the type manager
     */
    private void resolveAssets(AssetTypeManager typeManager) {
        var assetSourceResolver = new AssetSourceResolver(typeManager);
        assetSourceResolver.resolveAssets();
    }


}

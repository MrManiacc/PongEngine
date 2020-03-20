package io.chunkworld.client.engine.assets.shader;

import io.chunkworld.api.core.assets.Asset;
import io.chunkworld.api.core.assets.type.AssetType;
import io.chunkworld.api.core.assets.urn.ResourceUrn;

public class Shader extends Asset<ShaderData> {
    /**
     * The constructor for an asset. It is suggested that implementing classes provide a constructor taking both the urn, and an initial AssetData to load.
     *
     * @param urn       The urn identifying the asset.
     * @param assetType The asset type this asset belongs to.
     */
    public Shader(ResourceUrn urn, AssetType<?, ShaderData> assetType, ShaderData data) {
        super(urn, assetType);
        reload(data);
    }

    /**
     * Load the shader asset
     *
     * @param data The data to load.
     */
    public void reload(ShaderData data) {
//        System.out.println(data.getVertexSource() + " : " + data.getFragmentSource());
    }
}

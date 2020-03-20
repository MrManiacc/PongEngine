package io.chunkworld.api.core.assets.data;

import io.chunkworld.api.core.assets.Asset;
import io.chunkworld.api.core.assets.type.AssetType;
import io.chunkworld.api.core.assets.urn.ResourceUrn;
/**
 * AssetFactorys are used to load AssetData into new assets.
 * <p>For many assets, the assets just have one asset implementation so the factory would simply call the constructor for the implementation and pass the urn and data
 * straight through. However other assets may have multiple implementations (e.g. Texture may have an OpenGL and a DirectX implementation) so the factory installed
 * will determine that. Additionally the factory may pass through other information (OpenGL texture handle, or a reference to a central OpenGL context).</p>
 *
 * @author Immortius
 */
@FunctionalInterface
public interface AssetFactory<T extends Asset<U>, U extends AssetData> {

    /**
     * @param urn       The urn of the asset to construct
     * @param assetType The assetType the asset belongs to
     * @param data      The data for the asset
     * @return The built asset
     */
    T build(ResourceUrn urn, AssetType<T, U> assetType, U data);
}

package io.chunkworld.api.core.assets.type;

import io.chunkworld.api.core.assets.Asset;
import io.chunkworld.api.core.assets.data.AssetData;
import io.chunkworld.api.core.assets.urn.ResourceUrn;

import java.util.Optional;

/**
 * AssetManager provides an simplified interface for working with assets across multiple asset types.
 * <p>
 * To do this it uses an {@link AssetTypeManager} to obtain the AssetTypes relating to an Asset
 * class of interest, and delegates down to them for actions such as obtaining and reloading assets.
 * </p>
 *
 * @author Immortius
 */
public class AssetManager {
    private final AssetTypeManager assetTypeManager;

    public AssetManager(AssetTypeManager assetTypeManager) {
        this.assetTypeManager = assetTypeManager;
    }

//    /**
//     * @param urn  The urn of the asset to check. Must not be an instance urn
//     * @param type The Asset class of interest
//     * @return whether an asset is loaded with the given urn
//     */
//    public boolean isLoaded(ResourceUrn urn, Class<? extends Asset<?>> type) {
//        var assetType = assetTypeManager.getAssetType(type);
//        return assetType.map(value -> value.isLoaded(urn)).orElse(false);
//    }


    /**
     * Retrieves an asset with the given urn and type
     *
     * @param urn  The urn of the asset to retrieve
     * @param type The type of asset to retrieve
     * @param <T>  The class of Asset
     * @param <U>  The class of AssetData
     * @return An Optional containing the requested asset if successfully obtained
     */
    public <T extends Asset<U>, U extends AssetData> Optional<T> getAsset(ResourceUrn urn, Class<T> type) {
        var assetType = assetTypeManager.getAssetType(type);
        if (!assetType.isPresent())
            return Optional.empty();
        return assetType.get().getAsset(urn);
    }

    /**
     * Retrieves an asset with the given urn and type
     *
     * @param urn  The urn of the asset to retrieve
     * @param type The type of asset to retrieve
     * @param <T>  The class of Asset
     * @param <U>  The class of AssetData
     * @return An Optional containing the requested asset if successfully obtained
     */
    public <T extends Asset<U>, U extends AssetData> Optional<T> getAsset(String urn, Class<T> type) {
        return getAsset(new ResourceUrn(urn), type);
    }

    /**
     * Creates or reloads an asset with the given urn, data and type. The type must be the actual type of the asset, not a super type.
     *
     * @param urn  The urn of the asset
     * @param data The data to load the asset with
     * @param type The type of the asset
     * @param <T>  The class of Asset
     * @param <U>  The class of AssetData
     * @return The loaded asset
     * @throws java.lang.IllegalStateException if the asset type is not managed by this AssetManager.
     */
    public <T extends Asset<U>, U extends AssetData> T loadAsset(ResourceUrn urn, U data, Class<T> type) {
        Optional<AssetType<T, U>> assetType = assetTypeManager.getAssetType(type);
        if (assetType.isPresent()) {
            return assetType.get().loadAsset(urn, data);
        } else {
            throw new IllegalStateException(type + " is not a supported type of asset");
        }
    }


}

package io.chunkworld.api.core.assets.files;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.chunkworld.api.core.assets.Asset;
import io.chunkworld.api.core.assets.data.AssetData;
import io.chunkworld.api.core.assets.type.AssetType;
import io.chunkworld.api.core.assets.type.AssetTypeManager;
import io.chunkworld.api.core.assets.urn.ResourceUrn;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class will determine all of the files for a given asset type, and load the asset data by creating the asset file format
 */
public class AssetSourceResolver {
    private List<AssetDataFile> inputs = Lists.newArrayList();
    private AssetTypeManager typeManager;
    //TODO: change this!!!!
    private static final String ASSETS_FOLDER = "Client/src/main/resources/engine";
    private final Map<ResourceUrn, List<AssetDataFile>> unreadyAssets = Maps.newConcurrentMap();
    private final Map<ResourceUrn, AssetFileFormat<?>> unreadyFormats = Maps.newConcurrentMap();
    private final Map<ResourceUrn, AssetType> unreadyTypes = Maps.newConcurrentMap();

    public AssetSourceResolver(AssetTypeManager typeManager) {
        this.typeManager = typeManager;
    }

    /**
     * Resolves the assets for the given type
     */
    @SneakyThrows
    public void resolveAssets() {
        for (var assetClass : typeManager.getRegisteredTypes()) {
            Optional<String> assetFolder = typeManager.getAssetFolder(assetClass);
            Optional<AssetType<? extends Asset, ? extends AssetData>> assetType = typeManager.getAssetType(assetClass);
            Optional<AbstractAssetFileFormat<? extends AssetData>> assetFormat = typeManager.getFormat(assetClass);
            if (assetType.isPresent() && assetFormat.isPresent() && assetFolder.isPresent()) {
                var type = assetType.get();
                var format = assetFormat.get();
                var folder = assetFolder.get();
                var path = Paths.get(ASSETS_FOLDER, folder);
                var files = path.toFile().listFiles();
                if (files != null && files.length > 0)
                    for (var file : Objects.requireNonNull(files)) {
                        var filePath = Paths.get(file.toURI());
                        if (format.getFileMatcher().matches(filePath)) {
                            //TODO: remove hard coded engine!!!!!!
                            var urn = new ResourceUrn("engine", format.getAssetName(file.getName()).toLowerCase());
                            var assetData = Collections.singletonList(new AssetDataFile(filePath));
                            parseAsset(urn, assetData, format, type);
                        }
                    }
            } else {
                throw new IOException("Failed to create asset type or asset file format!");
            }
        }
    }

    /**
     * Parses the asset, or put's it into the unready map, which will attempt to load the asset every time
     * a different asset is loaded. This allows for us to make shaders dependent upon other shader
     *
     * @param urn        the urn to load
     * @param assetFiles the asset file
     * @param format     the asset format
     */
    @SneakyThrows
    private void parseAsset(ResourceUrn urn, List<AssetDataFile> assetFiles, AssetFileFormat<?> format, AssetType type) {
        var data = format.load(urn, assetFiles);
        if (data == null) {
            unreadyAssets.put(urn, assetFiles);
            unreadyFormats.put(urn, format);
            unreadyTypes.put(urn, type);
        } else {
            type.loadAsset(urn, data);
            for (var unreadyUrn : unreadyAssets.keySet()) {
                var nextFormat = unreadyFormats.get(unreadyUrn);
                data = nextFormat.load(unreadyUrn, unreadyAssets.get(unreadyUrn));
                if (data != null) {
                    var assetType = unreadyTypes.get(unreadyUrn);
                    assetType.loadAsset(unreadyUrn, data);
                    unreadyAssets.remove(unreadyUrn);
                    unreadyTypes.remove(unreadyUrn);
                    unreadyFormats.remove(unreadyUrn);
                }
            }
        }
    }

}

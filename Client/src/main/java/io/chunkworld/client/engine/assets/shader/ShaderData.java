package io.chunkworld.client.engine.assets.shader;

import io.chunkworld.api.core.assets.data.AssetData;
import lombok.Getter;

/**
 * The shader data, will store frag shader data, and vert shader data
 */
public class ShaderData implements AssetData {
    @Getter
    private final String vertexSource, fragmentSource;

    public ShaderData(String vertexSource, String fragmentSource) {
        this.vertexSource = vertexSource;
        this.fragmentSource = fragmentSource;
    }
}

package io.chunkworld.client.engine.assets.shader;

import com.google.common.collect.Maps;
import io.chunkworld.api.core.assets.data.AssetData;
import io.chunkworld.client.engine.assets.shader.parsing.Bind;
import io.chunkworld.client.engine.assets.shader.parsing.Custom;
import io.chunkworld.client.engine.assets.shader.parsing.Import;
import io.chunkworld.client.engine.assets.shader.parsing.Uniform;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The shader data, will store frag shader data, and vert shader data
 */
public class ShaderData implements AssetData {
    @Getter
    private final String[] vertexSource, fragmentSource, customLines;
    @Getter
    private Map<String, Uniform> uniforms = Maps.newHashMap();
    @Getter
    private Map<String, Bind> binds = Maps.newHashMap();
    @Getter
    private Map<String, List<Import>> imports = Maps.newHashMap();
    @Getter
    private Map<String, Custom> custom = Maps.newHashMap();

    public ShaderData(String[] vertexSource, String[] fragmentSource, String[] customLines) {
        this.vertexSource = vertexSource;
        this.fragmentSource = fragmentSource;
        this.customLines = customLines;
    }

    /**
     * Adds an import
     *
     * @param imp the import to add
     */
    public Import addImport(Import imp) {
        if (imports.containsKey(imp.getName()))
            imports.get(imp.getName()).add(imp);
        else
            imports.put(imp.getName(), new ArrayList<>(Collections.singletonList(imp)));
        return imp;
    }

    /**
     * adds a new custom type
     *
     * @param custom the custom type
     */
    public Custom addCustom(Custom custom) {
        this.custom.put(custom.getName(), custom);
        return custom;
    }

    /**
     * Adds a bind
     *
     * @param bind the bind to add
     */
    public Bind addBind(Bind bind) {
        binds.put(bind.getName(), bind);
        return bind;
    }


    /**
     * Adds a uniform
     *
     * @param uniform the uniform to add
     */
    public Uniform addUniform(Uniform uniform) {
        uniforms.put(uniform.getName(), uniform);
        return uniform;
    }


    /**
     * @return returns converted vertex code
     */
    public String getVertex() {
        var sb = new StringBuilder();
        for (var line : vertexSource)
            sb.append(line).append("\n");
        return sb.toString();
    }

    /**
     * @return returns converted fragment code
     */
    public String getFrag() {
        var sb = new StringBuilder();
        for (var line : fragmentSource)
            sb.append(line).append("\n");
        return sb.toString();
    }
}

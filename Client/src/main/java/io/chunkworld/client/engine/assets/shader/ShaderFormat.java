package io.chunkworld.client.engine.assets.shader;

import io.chunkworld.api.core.assets.files.AbstractAssetFileFormat;
import io.chunkworld.api.core.assets.files.AssetDataFile;
import io.chunkworld.api.core.assets.urn.ResourceUrn;
import io.chunkworld.client.engine.assets.shader.parsing.ShaderParser;
import org.apache.commons.io.IOUtils;
import org.joml.Vector2i;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

/**
 * Loads the shader data from the file
 */
public class ShaderFormat extends AbstractAssetFileFormat<ShaderData> {
    public ShaderFormat() {
        super("glsl");
    }

    /**
     * Loads the shader data
     *
     * @param urn    The urn identifying the asset being loaded.
     * @param inputs The inputs corresponding to this asset
     * @return returns the shader data
     * @throws IOException
     */
    public ShaderData load(ResourceUrn urn, List<AssetDataFile> inputs) throws IOException {
        var input = inputs.get(0);
        var stream = input.openStream();
        var lines = Files.readAllLines(input.getPath());
        stream.close();
//        return buildShader(lines);
        return ShaderParser.parseShader(urn, lines);
    }

    /**
     * Builds the shader data from the shader source inputs
     *
     * @param lines the input to build for
     * @return returns the shader data
     */
    private ShaderData buildShader(List<String> lines) {
        var vertexBuilder = new StringBuilder();
        var fragBuilder = new StringBuilder();
        var shaderLines = parseShaderLines(lines);
        for (int i = shaderLines[0]; i < shaderLines[1]; i++) {
            vertexBuilder.append(lines.get(i).trim()).append("\n");
        }
        for (int i = shaderLines[2]; i < shaderLines[3]; i++)
            fragBuilder.append(lines.get(i).trim()).append("\n");
        return new ShaderData(vertexBuilder.toString(), fragBuilder.toString());
    }

    /**
     * Finds the start and stop lines for the given shader,
     *
     * @param lines the shader source
     * @return returns the start and stop of both shaders
     */
    private int[] parseShaderLines(List<String> lines) {
        var shaderLines = new int[]{
                -1, -1, -1, -1
        };
        var inVertex = false;
        var inFrag = false;
        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i).trim();
            if (line.startsWith("#ifdef")) {
                var id = line.split(" ")[1];
                if (id.equalsIgnoreCase("VERTEX_SHADER")) {
                    shaderLines[0] = i + 1;
                    inVertex = true;
                }
                if (id.equalsIgnoreCase("FRAGMENT_SHADER")) {
                    shaderLines[2] = i + 1;
                    inFrag = true;
                }
            } else if (line.startsWith("#endif")) {
                if (inVertex) {
                    inVertex = false;
                    shaderLines[1] = i;
                }
                if (inFrag) {
                    shaderLines[3] = i;
                    inFrag = false;
                }
            }
        }
        return shaderLines;
    }
}

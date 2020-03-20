package io.chunkworld.client.engine.assets.shader.parsing;

import com.google.common.collect.Maps;
import io.chunkworld.api.core.assets.urn.ResourceUrn;
import io.chunkworld.client.engine.assets.shader.ShaderData;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class will parse the shader and generate the proper source code
 */
public class ShaderParser {
    private static final Map<ResourceUrn, ShaderData> cachedData = Maps.newHashMap();

    public ShaderParser() {
    }

    /**
     * This will parse the shader data
     *
     * @param resource the resource urn to parse
     * @return returns an optional shader data
     */
    public static ShaderData parseShader(ShaderData shaderData, ResourceUrn resource) {

        for (int i = 0; i < shaderData.getCustomLines().length; i++) {
            processLine(shaderData.getCustomLines(), i, shaderData, false, false);
        }
        for (int i = 0; i < shaderData.getVertexSource().length; i++) {
            processLine(shaderData.getVertexSource(), i, shaderData, true, false);
        }

        for (int i = 0; i < shaderData.getFragmentSource().length; i++) {
            processLine(shaderData.getFragmentSource(), i, shaderData, false, true);
        }
        var vertBinds = new StringBuilder();
        var fragBinds = new StringBuilder();
        shaderData.getBinds().forEach((name, bind) -> {
            var type = bind.getType();
            if (bind.isInVertex())
                vertBinds.append("in").append(" ").append(type).append(" ").append(name).append(";").append("\n");
            if (bind.isInFrag())
                fragBinds.append("in").append(" ").append(type).append(" ").append(name).append(";").append("\n");
        });
        shaderData.addCustom(new Custom("FRAG_BINDS", fragBinds.toString()));
        shaderData.addCustom(new Custom("VERT_BINDS", vertBinds.toString()));


        var vertUniforms = new StringBuilder();
        var fragUniforms = new StringBuilder();

        shaderData.getUniforms().forEach((name, uniform) -> {
            if (uniform.isInVertex())
                vertUniforms.append("uniform").append(" ").append(uniform.getType()).append(" ").append(name).append(";").append("\n");
            if (uniform.isInFrag())
                fragUniforms.append("uniform").append(" ").append(uniform.getType()).append(" ").append(name).append(";").append("\n");
        });

        shaderData.addCustom(new Custom("VERT_UNIFORMS", vertUniforms.toString()));
        shaderData.addCustom(new Custom("FRAG_UNIFORMS", fragUniforms.toString()));


        cachedData.put(resource, shaderData);
        var valid = new AtomicBoolean(true);
        shaderData.getImports().forEach((name, imp) -> {
            imp.forEach(anImport -> {
                var urn = new ResourceUrn(name);
                if (!cachedData.containsKey(urn)) {
                    valid.set(false);
                } else {
                    var importAsset = cachedData.get(urn);
                    if (importAsset.getCustom().containsKey(anImport.getDefinition())) {
                        var custom = importAsset.getCustom().get(anImport.getDefinition());
                        System.out.println(anImport.isInVertex());
                        anImport.source[anImport.line] = custom.getValue();
                    } else {
                        if (importAsset.getCustom().containsKey("VERT_" + anImport.getDefinition())) {
                            var custom = importAsset.getCustom().get("VERT_" + anImport.getDefinition());
                            if (!anImport.isInFrag())
                                anImport.source[anImport.line] = custom.getValue();
                            else if (anImport.source[anImport.line].contains("#define")) {
                                System.out.println(anImport.source[anImport.line]);
                                anImport.source[anImport.line] = "";
                            }
                        } else if (importAsset.getCustom().containsKey("FRAG_" + anImport.getDefinition())) {
                            var custom = importAsset.getCustom().get("FRAG_" + anImport.getDefinition());
                            if (!anImport.isInVertex())
                                anImport.source[anImport.line] = custom.getValue();
                            else if (anImport.source[anImport.line].contains("#define")) {
                                anImport.source[anImport.line] = "";
                            }
                        }
                    }
                }
            });
        });

        if (!valid.get())
            return null;
        return shaderData;
    }

    /**
     * Process a given line
     */
    private static void processLine(String[] input, int index, ShaderData data, boolean vertex, boolean frag) {
        var line = input[index];
        if (line.trim().startsWith("#define")) {
            var rawLine = line.replaceFirst("#define", "").trim();
            var type = rawLine.split("->")[0].replace("->", "").trim();
            switch (type) {
                case "UNIFORMS":
                    parseUniforms(input, index, data, vertex, frag);
                    break;
                case "BINDS":
                    parseBinds(input, index, data, vertex, frag);
                    break;
                default:
                    if (type.equals(type.toLowerCase()) && type.contains(":") && !type.contains("\"")) {
                        var impt = data.addImport(new Import(index, input));
                        impt.setInFrag(frag);
                        impt.setInVertex(vertex);
                    } else if (line.contains("\"")) {
                        var custom = data.addCustom(new Custom(index, input));
                        custom.setInFrag(frag);
                        custom.setInVertex(vertex);
                    }
                    break;
            }
        }
    }

    /**
     * Parses a collection of uniforms from the uniform line
     */
    private static void parseUniforms(String[] lines, int index, ShaderData shaderData, boolean vert, boolean frag) {
        var line = lines[index];
        if (line.contains(",")) {
            var elements = line.split(",");
            for (var element : elements) {
                shaderData.addUniform(new Uniform(index, lines, element));
            }
        } else {
            shaderData.addUniform(new Uniform(index, lines, line));
        }
    }

    /**
     * Parses a collection of binds from the binds line
     */
    private static void parseBinds(String[] lines, int index, ShaderData data, boolean vert, boolean frag) {
        var line = lines[index];
        if (line.contains(",")) {
            var start = 0;
            List<String> elements = new ArrayList<>();
            var inParenthesis = false;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '(')
                    inParenthesis = true;
                if (line.charAt(i) == ')')
                    inParenthesis = false;
                if (!inParenthesis && line.charAt(i) == ',') {
                    elements.add(line.substring(start, i).trim());
                    start = i;
                }
            }
            if (start != 0)
                elements.add(line.substring(start + 1).trim());
            for (var element : elements) {
                var bind = data.addBind(new Bind(index, lines, element));
                bind.setInFrag(frag);
                bind.setInVertex(vert);
            }

        } else {
            var bind = data.addBind(new Bind(index, lines, line));
            bind.setInFrag(frag);
            bind.setInVertex(vert);
        }
    }

}

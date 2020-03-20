package io.chunkworld.client.engine.assets.shader.parsing;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.chunkworld.api.core.assets.urn.ResourceUrn;
import io.chunkworld.client.engine.assets.shader.ShaderData;

import java.util.*;

/**
 * This class will parse the shader and generate the proper source code
 */
public class ShaderParser {
    private static final Map<ResourceUrn, ShaderData> cachedData = Maps.newHashMap();

    /**
     * This will parse the shader data
     *
     * @param resource the resource urn to parse
     * @param lines    the lines to process
     * @return returns an optional shader data
     */
    public static ShaderData parseShader(ResourceUrn resource, List<String> lines) {
        var uniforms = new ArrayList<Uniform>();
        var binds = new ArrayList<Bind>();
        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            if (line.trim().startsWith("#define")) {
                var rawLine = line.replaceFirst("#define", "").trim();
                var type = rawLine.split("->")[0].replace("->", "").trim();
                var value = rawLine.split("->")[1].trim();
                switch (type) {
                    case "UNIFORMS":
                        parseUniforms(value, uniforms);
                        break;
                    case "BINDS":
                        parseBinds(value, binds);
                        break;
                    default:
                        //Custom
                        break;
                }
            }
        }
        uniforms.forEach(System.out::println);
        return null;
    }

    /**
     * Parses a collection of uniforms from the uniform line
     *
     * @param line the uniform line
     */
    private static void parseUniforms(String line, List<Uniform> uniforms) {
        if (line.contains(",")) {
            var elements = line.split(",");
            for (var element : elements) {
                var uniform = new Uniform(element);
                uniforms.add(uniform);
            }
        } else
            uniforms.add(new Uniform(line));
    }

    /**
     * Parses a collection of binds from the binds line
     *
     * @param line the binds line
     */
    private static void parseBinds(String line, List<Bind> binds) {
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
            for (var element : elements) {
                binds.add(new Bind(element));
            }
        } else
            binds.add(new Bind(line));
    }

}

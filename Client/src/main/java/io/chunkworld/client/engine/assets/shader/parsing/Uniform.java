package io.chunkworld.client.engine.assets.shader.parsing;

import lombok.Getter;

/**
 * Represents a uniform, which is used to automatically map data in shader
 */
public class Uniform extends Define {
    @Getter
    private String target;
    @Getter
    private String type;

    public Uniform(int line, String[] source, String element) {
        this.line = line;
        this.source = source;
        this.name = parse(element);
    }

    /**
     * Parses the uniform
     *
     * @param input the input data
     * @return returns the name
     */
    @Override
    protected String parse(String input) {
        type = input.split(":")[1].trim();
        target = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));
        if (target.equalsIgnoreCase("vert")) {
            setInVertex(true);
            setInFrag(false);
        } else if (target.equalsIgnoreCase("frag")) {
            setInFrag(true);
            setInVertex(false);
        } else {
            setInVertex(true);
            setInFrag(true);
        }
        return input.substring(input.contains("->") ? input.indexOf("->") + 2 : 0, input.indexOf("(")).trim();
    }

    @Override
    public String toString() {
        return "Uniform{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

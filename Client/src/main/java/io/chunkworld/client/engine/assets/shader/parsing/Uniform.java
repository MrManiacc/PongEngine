package io.chunkworld.client.engine.assets.shader.parsing;

import lombok.Getter;

/**
 * Represents a uniform, which is used to automatically map data in shader
 */
public class Uniform {
    @Getter
    private final String target;
    @Getter
    private final String name;
    @Getter
    private final String type;

    public Uniform(String input) {
        type = input.split(":")[1].trim();
        name = input.substring(0, input.indexOf("(")).trim();
        target = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));
    }

    @Override
    public String toString() {
        return "Uniform{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

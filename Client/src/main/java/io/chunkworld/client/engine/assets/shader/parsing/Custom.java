package io.chunkworld.client.engine.assets.shader.parsing;

import lombok.Getter;

/**
 * Represents a custom piece of code that can be imported
 */
public class Custom {
    @Getter
    private final String name;
    @Getter
    private final String value;

    public Custom(String name, String value) {
        this.name = name;
        this.value = value.replace("##", "#").trim();
    }
}

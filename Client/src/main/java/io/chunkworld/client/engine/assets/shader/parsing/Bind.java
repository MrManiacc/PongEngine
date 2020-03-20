package io.chunkworld.client.engine.assets.shader.parsing;

import lombok.Getter;

/**
 * Represents some bind that can be appended to the shader dynamically
 */
public class Bind {
    @Getter
    private final int attribute;
    @Getter
    private final String name;
    @Getter
    private final String type;
    @Getter
    private final boolean passBind;

    public Bind(String input) {
        this.name = input.substring(0, input.indexOf("("));
        this.attribute = Integer.parseInt(input.substring(input.indexOf("(") + 1, input.indexOf(",")));
        this.passBind = Boolean.parseBoolean(input.substring(input.indexOf(","), input.indexOf(")")).trim());
        this.type = input.substring(input.indexOf(":"), input.length() - 1);
    }
}

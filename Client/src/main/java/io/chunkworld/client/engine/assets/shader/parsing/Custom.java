package io.chunkworld.client.engine.assets.shader.parsing;

import lombok.Getter;

/**
 * Represents a custom piece of code that can be imported
 */
public class Custom extends Define {
    @Getter
    private String value;

    public Custom(int line, String[] source) {
        super(line, source);
    }

    public Custom(String name, String value) {
        this.line = -1;
        this.name = name;
        this.value = value;
    }

    /**
     * Parses the custom type
     *
     * @param input input line
     * @return returns name
     */
    @Override
    protected String parse(String input) {
        this.value = input.substring(input.indexOf("\"") + 1, input.lastIndexOf("\""));
        return input.substring(input.indexOf(" ") + 1, input.indexOf("->")).trim();
    }

    @Override
    public String toString() {
        return "Custom{" +
                "value='" + value + '\'' +
                ", line=" + line +
                ", name='" + name + '\'' +
                '}';
    }
}

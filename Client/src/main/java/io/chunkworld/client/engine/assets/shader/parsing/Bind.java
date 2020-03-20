package io.chunkworld.client.engine.assets.shader.parsing;

import lombok.Getter;

/**
 * Represents some bind that can be appended to the shader dynamically
 */
public class Bind extends Define {
    @Getter
    private int attribute;
    @Getter
    private String type;
    @Getter
    private boolean passBind;

    public Bind(int line, String[] source) {
        super(line, source);
    }

    public Bind(int line, String[] source, String input) {
        this.line = line;
        this.source = source;
        this.name = parse(input);
    }

    /**
     * Parsed the asset
     *
     * @param input the input line
     * @return returns the name
     */
    @Override
    protected String parse(String input) {
        var name = input.substring(input.contains("->") ? input.indexOf("->") + 2 : 0, input.indexOf("(")).trim();
        this.attribute = Integer.parseInt(input.substring(input.indexOf("(") + 1, input.indexOf(",")));
        this.passBind = Boolean.parseBoolean(input.substring(input.indexOf(",") + 1, input.indexOf(")")).trim());
        this.type = input.substring(input.indexOf(":") + 1).trim();
        return name;
    }

    @Override
    public String toString() {
        return "Bind{" +
                "attribute=" + attribute +
                ", type='" + type + '\'' +
                ", passBind=" + passBind +
                '}';
    }
}

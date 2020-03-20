package io.chunkworld.client.engine.assets.shader.parsing;

import io.chunkworld.api.core.assets.urn.ResourceUrn;
import lombok.Getter;

/**
 * Represents some arbitrary code that will be imported from one file to another
 */
public class Import extends Define {
    @Getter
    private ResourceUrn urn;
    @Getter
    private String definition;

    private boolean vertex;

    public Import(int line, String[] source) {
        super(line, source);
    }

    public Import(ResourceUrn urn, String definition) {
        this.urn = urn;
        this.definition = definition;
    }

    @Override
    protected String parse(String input) {
        var name = input.substring(input.indexOf(" "), input.indexOf("->")).trim();
        urn = new ResourceUrn(name);
        definition = input.substring(input.indexOf("->") + 2).trim();
        return name;
    }

    @Override
    public String toString() {
        return "Import{" +
                "line='" + line + '\'' +
                "definition='" + definition + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

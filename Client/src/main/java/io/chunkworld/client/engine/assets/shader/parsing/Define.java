package io.chunkworld.client.engine.assets.shader.parsing;

import com.google.common.collect.Lists;
import io.chunkworld.api.core.annotations.Share;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * An abstract definition of a #define
 */
public abstract class Define {
    @Getter
    @Setter
    protected int line;
    @Getter
    protected String[] source;
    @Getter
    protected String name;
    @Getter
    @Setter
    private boolean inVertex;
    @Getter
    @Setter
    private boolean inFrag;

    public Define(int line, String[] source) {
        this.line = line;
        this.source = source;
        name = parse(source[line]);
    }

    protected Define() {
    }

    /**
     * Parse the definition
     *
     * @param line the line
     * @return should already return a name
     */
    protected abstract String parse(String line);

    /**
     * Serialize the data
     *
     * @return returns the serialized string
     */
    public abstract String serialize();
}

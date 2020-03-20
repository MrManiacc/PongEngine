package io.chunkworld.api.core.engine;

import com.google.common.collect.Lists;
import io.chunkworld.api.core.systems.EngineSubsystem;
import lombok.SneakyThrows;

import java.util.List;

/**
 * A simple helper class to create an engine
 */
public class EngineBuilder<T extends GameEngine> {
    private Class<T> engine;
    private List<EngineSubsystem> otherSubsystems = Lists.newArrayList();

    public EngineBuilder(Class<T> engine) {
        this.engine = engine;
    }

    public EngineBuilder<T> add(EngineSubsystem other) {
        otherSubsystems.add(other);
        return this;
    }

    /**
     * Builds the new game engine
     *
     * @return returns the new engine
     */
    @SneakyThrows
    public T build() {
        var gameEngine = engine.getDeclaredConstructor().newInstance();
        gameEngine.addSubsystems(otherSubsystems);
        return gameEngine;
    }


}

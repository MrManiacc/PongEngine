package io.chunkworld.client.engine.subsystems.glfw;

import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.systems.EngineSubsystem;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.api.core.time.GenericTime;
import lombok.Getter;

public class GlfwTimer implements EngineSubsystem {
    @Getter
    private final String name = "time";

    /**
     * A simple initializer for the timer
     *
     * @param rootContext The root context, that will survive the entire run of the engine
     */
    @Override
    public void preInitialise(Context rootContext) {
        rootContext.put(EngineTime.class, new GenericTime());
    }
}

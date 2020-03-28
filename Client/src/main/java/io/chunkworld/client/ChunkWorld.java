package io.chunkworld.client;

import io.chunkworld.api.core.engine.EngineBuilder;
import io.chunkworld.client.engine.ChunkWorldEngine;
import io.chunkworld.client.engine.subsystems.glfw.GlfwInputSubsystem;
import io.chunkworld.client.engine.subsystems.glfw.GlfwTimer;
import io.chunkworld.client.engine.subsystems.glfw.GlfwWindowSubsystem;
import io.chunkworld.client.pong.systems.PongPhysicsSubsystem;
import io.chunkworld.client.states.StateLoading;

public class ChunkWorld {
    public static void main(String[] args) {
        var builder = new EngineBuilder<>(ChunkWorldEngine.class);
        populateSubsystems(builder);
        var engine = builder.build();
        engine.run(new StateLoading());
    }

    /**
     * Adds the default sub systems, like lwjgl grahpics, and the timer, and the input
     *
     * @param builder the engine builder
     */
    private static void populateSubsystems(EngineBuilder<ChunkWorldEngine> builder) {
        builder.add(new GlfwInputSubsystem());
        builder.add(new GlfwWindowSubsystem());
        builder.add(new GlfwTimer());
        builder.add(new PongPhysicsSubsystem());
    }
}

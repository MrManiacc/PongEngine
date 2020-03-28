package io.chunkworld.client.pong.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.core.context.CoreRegistry;
import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.api.core.systems.EngineSubsystem;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.pong.events.ContactBeginEvent;
import io.chunkworld.client.pong.events.ContactEndEvent;
import io.chunkworld.client.pong.events.PostSolveContactEvent;
import io.chunkworld.client.pong.events.PreSolveContactEvent;
import lombok.Getter;


import java.util.Objects;

/**
 * This class will handle the physics for the pong entity,
 * which includes updating collisions, and handling velocity of objects
 */
public class PongPhysicsSubsystem implements ContactListener, EngineSubsystem {
    private World physicsWorld;
    @Getter private final String name = "Physics";
    @Getter private boolean loaded = false;
    @In private EngineTime time;


    /**
     * initialize this class as a contact listener
     */
    @Override
    public void initialise() {
        World.setVelocityThreshold(0f);
        physicsWorld = CoreRegistry.put(World.class, new World(new Vector2(0, 0), false));
        Objects.requireNonNull(physicsWorld).setContactListener(this);
        loaded = true;
    }

    /**
     * Update the physics world
     */
    @Override
    public void postUpdate(GameState currentState) {
        physicsWorld.step(1.0f / 60.0f, 6, 6);
    }

    /**
     * Called at the start of a contact between two entities
     *
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        Bus.Logic.post(new ContactBeginEvent(contact));
    }

    /**
     * Called at the end of a contact between two entities
     *
     * @param contact
     */
    @Override
    public void endContact(Contact contact) {
        Bus.Logic.post(new ContactEndEvent(contact));
    }

    /**
     * Called before the collision is resolved
     *
     * @param contact
     * @param oldManifold
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Bus.Logic.post(new PreSolveContactEvent(contact, oldManifold));
    }

    /**
     * Called after the collision is solved
     *
     * @param contact
     * @param impulse
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Bus.Logic.post(new PostSolveContactEvent(contact, impulse));
    }

}

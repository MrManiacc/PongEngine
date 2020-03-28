package io.chunkworld.client.pong.systems;

import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.pong.events.ContactBeginEvent;
import io.chunkworld.client.pong.events.ContactEndEvent;
import io.chunkworld.client.pong.events.PostSolveContactEvent;
import io.chunkworld.client.pong.events.PreSolveContactEvent;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * This class will handle the physics for the pong entity,
 * which includes updating collisions, and handling velocity of objects
 */
public class PongPhysicsSystem extends EntitySystem implements ContactListener {
    @In private World physicsWorld;

    /**
     * initialize this class as a contact listener
     */
    @Override
    public void initialize() {
        physicsWorld.setContactListener(this);
    }

    /**
     * Update the physics world
     *
     * @param time
     */
    @Override
    protected void process(EngineTime time) {
        physicsWorld.step(time.getGameDelta(), 8, 3);
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

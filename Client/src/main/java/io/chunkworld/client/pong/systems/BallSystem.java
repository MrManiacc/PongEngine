package io.chunkworld.client.pong.systems;

import com.google.common.eventbus.Subscribe;
import io.chunkworld.api.core.injection.anotations.EventSubscriber;
import io.chunkworld.api.core.injection.anotations.Single;
import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.pong.events.PreSolveContactEvent;

/**
 * Handles everything related to the ball
 */
@EventSubscriber
public class BallSystem extends EntitySystem {
    @Single("engine:entities#ball") private EntityRef ball;

    /**
     * Process the system
     *
     * @param time
     */
    @Override
    protected void process(EngineTime time) {

    }

    /**
     * Called when a contact is made between two entities
     *
     * @param e contact event
     */
    @Subscribe
    public void onContact(PreSolveContactEvent e) {
        //A contact has been made between the ball
        if (e.getEntityA().equals(ball) || e.getEntityB().equals(ball)) {
            System.out.println("contact normal: " + e.getOldManifold().localNormal);
        }
    }
}

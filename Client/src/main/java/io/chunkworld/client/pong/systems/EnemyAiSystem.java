package io.chunkworld.client.pong.systems;

import com.google.common.eventbus.Subscribe;
import io.chunkworld.api.core.injection.anotations.EventSubscriber;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.client.pong.components.PhysicsComponent;
import io.chunkworld.client.pong.events.ContactBeginEvent;

/**
 * This class will handle the enemy ai for the the pong
 */
@EventSubscriber
public class EnemyAiSystem extends EntitySystem {
    /**
     * Subscribe to the begining of a contact
     *
     * @param e contact event
     */
    @Subscribe
    public void onBeginContact(ContactBeginEvent e) {
        e.getEntityA().ifPresent(PhysicsComponent.class, physicsComponent -> {
            System.out.println(physicsComponent.body.getPosition());
        });
    }
}

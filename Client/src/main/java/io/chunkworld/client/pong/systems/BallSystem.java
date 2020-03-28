package io.chunkworld.client.pong.systems;

import com.badlogic.gdx.math.Vector2;
import com.google.common.eventbus.Subscribe;
import io.chunkworld.api.core.injection.anotations.EventSubscriber;
import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.injection.anotations.Single;
import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.pong.components.PhysicsComponent;
import io.chunkworld.client.pong.events.PostSolveContactEvent;
import io.chunkworld.client.pong.events.PreSolveContactEvent;

/**
 * Handles everything related to the ball
 */
@EventSubscriber
public class BallSystem extends EntitySystem {
    @Single("engine:entities#ball") private EntityRef ballEntity;
    private static final float MAX_VELOCITY = 30;
    @In private ScoreSystem scoreSystem;

    /**
     * Process the system
     *
     * @param time
     */
    @Override
    protected void process(EngineTime time) {
        ballEntity.ifPresent(PhysicsComponent.class, physics -> {
            var body = physics.body;
            if (!scoreSystem.isGameOver()) {
                if (body.getLinearVelocity().x >= MAX_VELOCITY)
                    body.setLinearVelocity(new Vector2(MAX_VELOCITY, body.getLinearVelocity().y));
                if (body.getLinearVelocity().x <= -MAX_VELOCITY)
                    body.setLinearVelocity(new Vector2(-MAX_VELOCITY, body.getLinearVelocity().y));
                if (body.getLinearVelocity().y >= MAX_VELOCITY)
                    body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, MAX_VELOCITY));
                if (body.getLinearVelocity().y <= -MAX_VELOCITY)
                    body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, -MAX_VELOCITY));
            } else {
                body.setLinearVelocity(new Vector2(0, 0));
            }
        });
    }

}

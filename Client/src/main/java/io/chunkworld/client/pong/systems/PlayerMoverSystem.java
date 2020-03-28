package io.chunkworld.client.pong.systems;

import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.injection.anotations.EventSubscriber;
import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.api.core.injection.anotations.Single;
import io.chunkworld.api.core.input.Input;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.pong.components.PhysicsComponent;
import org.jbox2d.common.Vec2;

/**
 * This class updates the player based upon the input
 */
@EventSubscriber
public class PlayerMoverSystem extends EntitySystem {
    @Single("engine:entities#local-player") private EntityRef player;
    @In private Input input;

    /**
     * Updates the player position
     */
    @Override
    protected void process(EngineTime time) {
        var physics = player.getComponent(PhysicsComponent.class);
        physics.body.setLinearVelocity(new Vec2(0, 0));
        if (input.keyDown(Input.KEY_W) || input.keyDown(Input.KEY_UP))
            physics.body.setLinearVelocity(new Vec2(0, 500 * time.getGameDelta()));
        if (input.keyDown(Input.KEY_S) || input.keyDown(Input.KEY_DOWN))
            physics.body.setLinearVelocity(new Vec2(0, -500 * time.getGameDelta()));
    }



}

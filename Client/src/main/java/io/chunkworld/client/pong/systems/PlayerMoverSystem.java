package io.chunkworld.client.pong.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.google.common.eventbus.Subscribe;
import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.injection.anotations.EventSubscriber;
import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.api.core.injection.anotations.Single;
import io.chunkworld.api.core.input.Input;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.pong.components.PhysicsComponent;
import io.chunkworld.client.pong.events.ContactEndEvent;
import io.chunkworld.client.pong.events.PostSolveContactEvent;
import io.chunkworld.client.pong.events.PreSolveContactEvent;

/**
 * This class updates the player based upon the input
 */
@EventSubscriber
public class PlayerMoverSystem extends EntitySystem {
    @Single("engine:entities#local-player") private EntityRef player;
    @In private Input input;
    private boolean resetBody;
    @In private ScoreSystem scoreSystem;

    /**
     * Updates the player position
     */
    @Override
    protected void process(EngineTime time) {
        var physics = player.getComponent(PhysicsComponent.class);
        if (!scoreSystem.isGameOver()) {
            physics.body.setLinearVelocity(new Vector2(0, physics.body.getLinearVelocity().y));
            if (input.keyDown(Input.KEY_W) || input.keyDown(Input.KEY_UP))
                physics.body.setLinearVelocity(new Vector2(0, 1500 * time.getRealDelta()));
            if (input.keyDown(Input.KEY_S) || input.keyDown(Input.KEY_DOWN))
                physics.body.setLinearVelocity(new Vector2(0, -1500 * time.getRealDelta()));
        } else {
            physics.body.setLinearVelocity(0, 0);
            physics.body.setTransform(23, 0, 0);
        }
    }


}

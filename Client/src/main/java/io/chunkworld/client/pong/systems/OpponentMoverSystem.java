package io.chunkworld.client.pong.systems;

import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import io.chunkworld.api.core.injection.anotations.EventSubscriber;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.injection.anotations.Single;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.pong.components.PhysicsComponent;
import org.joml.Vector2f;

/**
 * This class will handle the enemy ai for the the pong
 */
@EventSubscriber
public class OpponentMoverSystem extends EntitySystem {
    @Single("engine:entities#ball")
    private EntityRef ballRef;
    @Single("engine:entities#opponent-player")
    private EntityRef opponentRef;

    private float lerp = 10F;
    private float speed = 30;

    @In private ScoreSystem scoreSystem;

    /**
     * Moves the opponent paddle
     *
     * @param time
     */
    @Override
    protected void process(EngineTime time) {
        var paddlePhy = opponentRef.getComponent(PhysicsComponent.class);
        var ballPhys = ballRef.getComponent(PhysicsComponent.class);
        var paddlePos = new Vector2f(paddlePhy.body.getPosition().x, paddlePhy.body.getPosition().y);
        var paddleVel = new Vector2f(paddlePhy.body.getLinearVelocity().x, paddlePhy.body.getLinearVelocity().y);
        if (!scoreSystem.isGameOver()) {
            var lerp = paddleVel.lerp(new Vector2f(0, 0).mul(speed), this.lerp * time.getGameDelta());
            if (ballPhys.body.getPosition().x < 0)
                this.speed += Math.abs(ballPhys.body.getPosition().x) * 0.5;
            else
                this.speed = 30;

            if (this.speed >= 70)
                this.speed = 70;
            if (ballPhys.body.getPosition().y > paddlePos.y) {
                if (paddlePhy.body.getLinearVelocity().y < 0)
                    paddlePhy.body.setLinearVelocity(0, 0);
                lerp = paddleVel.lerp(new Vector2f(0, 1).mul(speed), this.lerp * time.getGameDelta());
            } else if (ballPhys.body.getPosition().y < paddlePos.y) {
                if (paddlePhy.body.getLinearVelocity().y > 0)
                    paddlePhy.body.setLinearVelocity(0, 0);
                lerp = paddleVel.lerp(new Vector2f(0, -1).mul(speed), this.lerp * time.getGameDelta());
            }
            paddlePhy.body.setLinearVelocity(lerp.x, lerp.y);
        } else {
            paddlePhy.body.setLinearVelocity(0, 0);
            paddlePhy.body.setTransform(-23, 0, 0);
        }
    }


}

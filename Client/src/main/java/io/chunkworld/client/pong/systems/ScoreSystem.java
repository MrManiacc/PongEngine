package io.chunkworld.client.pong.systems;

import com.badlogic.gdx.math.Vector2;
import com.google.common.eventbus.Subscribe;
import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.core.context.CoreRegistry;
import io.chunkworld.api.core.ecs.component.SingleComponent;
import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.api.core.injection.anotations.EventSubscriber;
import io.chunkworld.api.core.injection.anotations.Single;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.pong.components.PhysicsComponent;
import io.chunkworld.client.pong.events.ContactBeginEvent;
import io.chunkworld.client.pong.events.ContactEndEvent;
import io.chunkworld.client.pong.events.RestartGameEvent;
import io.chunkworld.client.pong.events.ScoreEvent;
import lombok.Getter;

import java.util.Optional;

/**
 * Keeps track of the score
 */
@EventSubscriber
public class ScoreSystem extends EntitySystem {
    @Single("engine:entities#player-wall") private EntityRef playerWallRef;
    @Single("engine:entities#opponent-wall") private EntityRef opponentWallRef;
    @Single("engine:entities#ball") private EntityRef ballRef;
    private boolean resetBall = false;
    @Getter private int playerScore = 0, opponentScore = 0;
    @Getter private boolean gameOver = false;

    /**
     * Process the collision
     *
     * @param time
     */
    @Override
    protected void process(EngineTime time) {
        if (!gameOver) {
            if (resetBall) {
                //TODO: reset the ball position
                ballRef.ifPresent(PhysicsComponent.class, ball -> {
                    ball.body.setTransform(0, 0, 0);
                    ball.body.setLinearVelocity(new Vector2((((float) Math.random() * 2) - 1) * 5000, (((float) Math.random() * 2) - 1) * 5000));
                });
                resetBall = false;
            }
        }
    }

    /**
     * Process the collision to see if one is a wall or not
     *
     * @param e
     */
    @Subscribe
    public void onContact(ContactBeginEvent e) {
        if (e.getEntityA().equals(playerWallRef) || e.getEntityB().equals(playerWallRef)) {
            //Player scored
            Bus.Logic.post(new ScoreEvent(ScoreEvent.PLAYER));
            System.out.println("Player scored");
            resetBall = true;
            playerScore++;

        } else if (e.getEntityA().equals(opponentWallRef) || e.getEntityB().equals(opponentWallRef)) {
            //Opponent scored
            Bus.Logic.post(new ScoreEvent(ScoreEvent.OPPONENT));
            System.out.println("Opponent scored");
            resetBall = true;
            opponentScore++;
        }
        if (opponentScore >= 7 || playerScore >= 7)
            gameOver = true;
    }

    /**
     * When the game has been reset
     *
     * @param e
     */
    @Subscribe
    public void onRestartGame(RestartGameEvent e) {
        gameOver = false;
        opponentScore = 0;
        playerScore = 0;
        resetBall = true;
    }
}

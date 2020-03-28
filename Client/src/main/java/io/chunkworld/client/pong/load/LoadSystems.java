package io.chunkworld.client.pong.load;

import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.ecs.entity.system.EntitySystemManager;
import io.chunkworld.api.core.modes.SingleStepLoadProcess;
import io.chunkworld.client.pong.systems.EnemyAiSystem;
import io.chunkworld.client.pong.systems.PlayerMoverSystem;
import io.chunkworld.client.pong.systems.PongRendererSystem;
import io.chunkworld.client.pong.systems.PongPhysicsSystem;

/**
 * Loads all of the pong related entity systems
 */
public class LoadSystems extends SingleStepLoadProcess {
    @In private EntitySystemManager systemManager;

    public LoadSystems() {
        super("Loading entity systems....", 1);
    }

    /**
     * Loads all of the related entity systems
     *
     * @return returns true
     */
    @Override
    public boolean step() {
        systemManager.add(new PongPhysicsSystem());
        systemManager.add(new PlayerMoverSystem());
        systemManager.add(new EnemyAiSystem());
        systemManager.add(new PongRendererSystem());
        return true;
    }
}

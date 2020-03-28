package io.chunkworld.client.pong.load;

import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.ecs.entity.system.EntitySystemManager;
import io.chunkworld.api.core.modes.SingleStepLoadProcess;
import io.chunkworld.client.pong.systems.*;

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
        systemManager.add(new ScoreSystem());
        systemManager.add(new PlayerMoverSystem());
        systemManager.add(new OpponentMoverSystem());
        systemManager.add(new PongRendererSystem());
        systemManager.add(new BallSystem());
        systemManager.add(new SimpleGuiSystem());
        return true;
    }
}

package io.chunkworld.client.engine.load;

import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.ecs.entity.system.EntitySystemManager;
import io.chunkworld.api.core.modes.SingleStepLoadProcess;
import io.chunkworld.client.engine.subsystems.gui.GuiRendererSystem;

/**
 * Registers the core entities to their respective systems
 */
public class LoadEntitySystems extends SingleStepLoadProcess {
    @In private EntitySystemManager systemManager;

    public LoadEntitySystems() {
        super("Registering core entities...", 1);
    }

    /**
     * Register the core entities
     *
     * @return returns true (single step)
     */
    @Override
    public boolean step() {
        systemManager.add(new GuiRendererSystem());
        return true;
    }


}

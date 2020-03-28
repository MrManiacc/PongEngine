package io.chunkworld.client.engine.load;

import io.chunkworld.api.core.assets.urn.ResourceUrn;
import io.chunkworld.api.core.context.CoreRegistry;
import io.chunkworld.api.core.ecs.entity.pool.EntityManager;
import io.chunkworld.api.core.ecs.entity.system.EntitySystemManager;
import io.chunkworld.api.core.modes.SingleStepLoadProcess;
import io.chunkworld.client.engine.assets.model.Vao;
import io.chunkworld.client.engine.utils.GLUtils;
import io.chunkworld.client.engine.utils.ShapeUtils;

import java.util.Objects;

public class LoadConfiguration extends SingleStepLoadProcess {
    public LoadConfiguration() {
        super("Registering config...", 1);
    }

    /**
     * Registers the config and the config manager
     *
     * @return returns true
     */
    public boolean step() {
        CoreRegistry.put(GLUtils.class, new GLUtils());
        addDefaultShapes(Objects.requireNonNull(CoreRegistry.put(ShapeUtils.class, new ShapeUtils())));
        CoreRegistry.put(EntityManager.class, new EntityManager());
        CoreRegistry.put(EntitySystemManager.class, new EntitySystemManager());
        return true;
    }

    /**
     * Create's the default shapes
     */
    private void addDefaultShapes(ShapeUtils shapeUtils) {
        var quadVao = Vao.create(2, Vao.ARRAYS_TRIANGLE_STRIPS);
        { //The quad
            quadVao.bind();
            quadVao.createAttribute(new float[]{-1, 1, -1, -1, 1, 1, 1, -1}, 2);
            quadVao.createAttribute(new float[]{0, 0, 1, 0, 0, 1, 1, 1}, 2);
            quadVao.unbind();
            shapeUtils.add(new ResourceUrn("engine", "shapes", "quad"), quadVao);
        }
    }

}

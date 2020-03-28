package io.chunkworld.client.pong.systems;

import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.assets.type.AssetManager;
import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.api.core.ecs.group.Group;
import io.chunkworld.api.core.injection.anotations.All;
import io.chunkworld.api.core.injection.anotations.Resource;
import io.chunkworld.api.core.injection.anotations.Single;
import io.chunkworld.api.core.time.EngineTime;
import io.chunkworld.client.engine.assets.shader.Shader;
import io.chunkworld.client.engine.assets.texture.Texture;
import io.chunkworld.client.engine.utils.GLUtils;
import io.chunkworld.client.engine.utils.ShapeUtils;
import io.chunkworld.client.pong.components.CameraComponent;
import io.chunkworld.client.pong.components.PhysicsComponent;
import io.chunkworld.client.pong.components.MaterialComponent;

/**
 * Renders a paddle
 */
public class PongRendererSystem extends EntitySystem {
    @In private ShapeUtils shapeUtils;
    @In private AssetManager assetManager;
    @All({PhysicsComponent.class, MaterialComponent.class}) private Group entities;
    @Single("engine:entities#local-player") private EntityRef player;
    @In private GLUtils glUtils;
    @Resource("engine:shaders#gui")
    private Shader shader;

    /**
     * initialize the assets
     */
    @Override
    public void initialize() {
//        shader = assetManager.getAsset("engine:shaders#gui", Shader.class).get();
    }

    /**
     * Renders the paddles
     *
     * @param time
     */
    @Override
    protected void process(EngineTime time) {
        shader.start();
        shader.loadMat4("projectionMatrix", player.getComponent(CameraComponent.class).projectionMatrix);
        glUtils.alphaBlending(true);
        entities.forEach(entity -> {
            var physics = entity.getComponent(PhysicsComponent.class);
            var materialRef = entity.getComponent(MaterialComponent.class);
            var shape = shapeUtils.get(materialRef.shapeUrn);
            var texture = assetManager.getAsset(materialRef.textureUrn, Texture.class).get();
            texture.bind();
            shader.loadMat4("modelMatrix", physics.getModelMatrix());
            shape.draw();
            texture.unbind();
        });
        glUtils.alphaBlending(false);
        shader.stop();
    }


}

package io.chunkworld.client.engine.subsystems.gui;

import io.chunkworld.api.core.ecs.entity.system.EntitySystem;

/**
 * Renders all guis with the specified components
 */
public class GuiRendererSystem extends EntitySystem {
//    @In private AssetManager assets;
//    @In private ShapeUtils shapes;
//    private Shader shader;
//    private Texture texture;
//    private Matrix4f testMatrix = new Matrix4f().translate(0, 0, 0).scale(0.05f);
//
//    //Single entity example
//    @Single("engine:entities#local_player") private EntityRef localPlayer;
//    //Excluding example
//    @Exclude(SingleComponent.class)
//    @All({ShapeComponent.class, TransformComponent.class})
//    private Group shapeEntities;
//
//    /**
//     * Initialize the render system here
//     */
//    public void initialize() {
//        texture = assets.getAsset("engine:yessir", Texture.class).get();
//        shader = assets.getAsset("engine:gui", Shader.class).get();
//    }
//
//    /**
//     * Render the gui entities here
//     *
//     * @param time the engine time
//     */
//    protected void process(EngineTime time) {
//        shader.start();
//        texture.bind();
//        shapeEntities.forEach(renderEntity);
//        texture.unbind();
//        shader.stop();
//    }
//
//    /**
//     * Renders the entity
//     */
//    private Consumer<EntityRef> renderEntity = (entity -> {
//        var shape = entity.getComponent(ShapeComponent.class);
//        var transform = entity.getComponent(TransformComponent.class);
//        transform.rotation.x += time.getGameDelta() * 50 * (Math.random() * 10);
//        transform.rotation.y += time.getGameDelta() * 50 * (Math.random() * 10);
//        var model = shapes.get(shape.shapeUrn);
//        testMatrix.identity().scale(transform.scale).translate(transform.position).rotateX((float) Math.toRadians(transform.rotation.x)).rotateY((float) Math.toRadians(transform.rotation.y));
//        shader.loadMat4("modelMatrix", testMatrix);
//        model.draw();
//    });

}

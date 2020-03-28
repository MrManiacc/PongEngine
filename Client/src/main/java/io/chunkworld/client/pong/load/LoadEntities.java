package io.chunkworld.client.pong.load;

import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.assets.urn.ResourceUrn;
import io.chunkworld.api.core.ecs.component.SingleComponent;
import io.chunkworld.api.core.ecs.entity.pool.EntityManager;
import io.chunkworld.api.core.ecs.entity.system.EntitySystemManager;
import io.chunkworld.api.core.modes.SingleStepLoadProcess;
import io.chunkworld.api.core.window.IWindow;
import io.chunkworld.client.pong.components.CameraComponent;
import io.chunkworld.client.pong.components.PhysicsComponent;
import io.chunkworld.client.pong.components.MaterialComponent;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * This class handles the registration of the player entity as well as the initialization
 */
public class LoadEntities extends SingleStepLoadProcess {
    @In private EntitySystemManager systemManager;
    @In private EntityManager manager;
    @In private World physicsWorld;
    @In private IWindow window;

    public LoadEntities() {
        super("Loading player...", 1);
    }

    /**
     * Load the player related entity systems
     *
     * @return returns true
     */
    @Override
    public boolean step() {
        createBall((float) ((Math.random() * 2) - 1) * 25f, 0);
        createArena();
        createControllers();
        return true;
    }

    /**
     * Creates the ball entity
     */
    private void createBall(float x, float y) {

        //Load the player entity
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyType.DYNAMIC;
        // Set our body's starting position in the world
        bodyDef.position.set(x, y);

        // Create our body in the world using our body definition
        Body body = physicsWorld.createBody(bodyDef);

        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(1);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 50f;
        fixtureDef.restitution = 10f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(manager.create(
                new SingleComponent("engine:entities#ball"),
                new PhysicsComponent(body, fixture, bodyDef, circle),
                new MaterialComponent(new ResourceUrn("engine", "shapes", "quad"), new ResourceUrn("engine:ball"))
        ));

    }

    /**
     * Creates the player controller and the ai controller
     */
    private void createControllers() {

        //Load the player entity
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(0.25f, 10);
        Body body = physicsWorld.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.density = 0;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(manager.create(
                new SingleComponent("engine:entities#local-player"),
                new CameraComponent((float) Math.toRadians(120), window.getWidth() / (float) window.getHeight(), 0.1f, 1000f),
                new PhysicsComponent(body, fixture, bodyDef, groundBox),
                new MaterialComponent(new ResourceUrn("engine", "shapes", "quad"), new ResourceUrn("engine:white"))
        ));
    }


    /**
     * Creates the area for the component
     */
    private void createArena() {
        createWall(0, -15, 25, 0.3f);
        createWall(0, 15, 25, 0.3f);
        createWall(25, 0, 0.3f, 15.3f);
        createWall(-25, 0, 0.3f, 15.3f);
    }

    /**
     * Creates a wall component
     *
     * @param x x pos
     * @param y y pos
     * @param w width
     * @param h height
     */
    private void createWall(float x, float y, float w, float h) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vec2(x, y));
        Body groundBody = physicsWorld.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(w, h);
        Fixture groundFixture = groundBody.createFixture(groundBox, 0.0f);

        groundFixture.setUserData(manager.create(
                new PhysicsComponent(groundBody, groundFixture, groundBodyDef, groundBox),
                new MaterialComponent("engine:shapes#quad", "engine:white")
        ));
    }

}

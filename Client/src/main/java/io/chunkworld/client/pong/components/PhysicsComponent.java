package io.chunkworld.client.pong.components;

import io.chunkworld.api.core.ecs.component.Component;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.joml.Matrix4f;
import org.joml.Vector2f;

/**
 * A generic physics component
 */
public class PhysicsComponent implements Component {
    public final Body body;
    public final Fixture fixture;
    public final BodyDef bodyDef;
    public final Shape shape;
    private final Matrix4f modelMatrix;
    private Vector2f scale;

    public PhysicsComponent(Body body, Fixture fixture, BodyDef bodyDef, Shape shape) {
        this.body = body;
        this.fixture = fixture;
        this.bodyDef = bodyDef;
        this.shape = shape;
        this.modelMatrix = new Matrix4f();
        if (shape instanceof CircleShape) {
            this.scale = new Vector2f(shape.getRadius());
        } else if (shape instanceof PolygonShape) {
            var poly = (PolygonShape) shape;
            var rawScale = poly.getVertex(2);
            this.scale = new Vector2f(rawScale.x, rawScale.y);
        }
    }

    public Matrix4f getModelMatrix() {
        modelMatrix.identity().translate(body.getPosition().x, body.getPosition().y, -10f).scale(scale.x, scale.y, 1);
        return modelMatrix;
    }
}

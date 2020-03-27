package io.chunkworld.client.states;

import io.chunkworld.api.core.annotations.In;
import io.chunkworld.api.core.assets.type.AssetManager;
import io.chunkworld.api.core.input.Input;
import io.chunkworld.api.core.state.GameState;
import io.chunkworld.client.engine.assets.model.Vao;
import io.chunkworld.client.engine.assets.shader.Shader;
import io.chunkworld.client.engine.assets.texture.Texture;
import lombok.Getter;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class StateInGame implements GameState {
    @Getter
    private final int progress = 0;
    @Getter
    private final int maxProgress = 0;

    @In
    private Input input;

    @In
    private AssetManager assetManager;

    private Shader shader;

    private Texture texture;

    private Vao vao;

    private Matrix4f testMatrix = new Matrix4f().translate(0, 0, 0);


    /**
     * Initialize the in game state
     */
    @Override
    public void init() {
        texture = assetManager.getAsset("engine:laughing", Texture.class).get();
        shader = assetManager.getAsset("engine:gui", Shader.class).get();
        vao = Vao.create(2);
        vao.bind();
        vao.createAttribute(new float[]{
                //left bottom triangle
                -0.5f, +0.5f, 0.0f, // v0 - x1, y1, z1
                -0.5f, -0.5f, 0.0f, // v1 - x2, y2, z2
                +0.5f, -0.5f, 0.0f, // v2 - x3, y3, z3
                +0.5f, +0.5f, 0.0f // v3 - x4, y4, z4
        }, 3);
        vao.createAttribute(new float[]{
                0, 0,
                1, 0,
                0, 1,
                1, 1
        }, 2);
        vao.createIndexBuffer(new int[]{
                0, 1, 3, // top left triangle (v0, v1, v2)
                3, 1, 2 // bottom right triangle (v3 v1 v2)
        });
        vao.unbind();

    }

    /**
     * Update the main game state
     *
     * @param delta the delta time
     */
    @Override
    public void update(float delta) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        glClearColor(0, 0, 0, 1);
        shader.start();
        shader.loadMat4("modelMatrix", testMatrix);
        texture.bind();
        vao.bind();
        glDrawElements(GL_TRIANGLES, vao.getIndexCount(), GL_UNSIGNED_INT, 0);
        vao.unbind();
        texture.unbind();
        shader.stop();
    }

}

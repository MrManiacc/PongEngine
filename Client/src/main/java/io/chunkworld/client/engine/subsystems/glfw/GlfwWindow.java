package io.chunkworld.client.engine.subsystems.glfw;

import io.chunkworld.api.core.window.IWindow;
import lombok.Getter;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Represents a generic glfw window
 */
public class GlfwWindow implements IWindow {
    @Getter
    private String title;
    @Getter
    private int width, height;
    @Getter
    private boolean fullscreen;
    @Getter
    private boolean vsync;
    @Getter
    private boolean resizable;
    @Getter
    private long handle;

    public GlfwWindow(String title, int width, int height, boolean fullscreen, boolean vsync, boolean resizable) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
        this.vsync = vsync;
        this.resizable = resizable;
    }

    /**
     * Initialize the window
     */
    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        setHints();
        centerWindow();
        finishWindow();
    }

    /**
     * Sets the hints
     */
    private void setHints() {
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        handle = glfwCreateWindow(width, height, title, 0, 0);
        if (handle == 0)
            throw new RuntimeException("Failed to create the GLFW window");
    }

    /**
     * Centers the window
     */
    private void centerWindow() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(handle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(handle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }
    }

    /**
     * Registers a key callback, which should be used by the input system
     *
     * @param callback the input callback
     */
    public void registerKeyCallback(GLFWKeyCallback callback) {
        glfwSetKeyCallback(handle, callback);
    }

    /**
     * Registers a mouse position callback
     *
     * @param callback the mouse position callback
     */
    public void registerMousePositionCallback(GLFWCursorPosCallback callback) {
        glfwSetCursorPosCallback(handle, callback);
    }

    /**
     * Registers a mouse button callback
     *
     * @param callback the mouse button callback
     */
    public void registerMouseButtonCallback(GLFWMouseButtonCallback callback) {
        glfwSetMouseButtonCallback(handle, callback);
    }

    /**
     * Registers a mouse scroll callback
     *
     * @param callback the mouse scroll callback
     */
    public void registerMouseScrollCallback(GLFWScrollCallback callback) {
        glfwSetScrollCallback(handle, callback);
    }

    /**
     * Registers a char callback
     *
     * @param callback the char callback
     */
    public void registerCharCallback(GLFWCharCallback callback) {
        glfwSetCharCallback(handle, callback);
    }

    /**
     * Finishes the window
     */
    private void finishWindow() {
        // Make the OpenGL context current
        glfwMakeContextCurrent(handle);
        // Enable v-sync
        glfwSwapInterval(vsync ? 1 : 0);

        // Make the window visible
        glfwShowWindow(handle);

        GL.createCapabilities();

    }

    /**
     * Checks to see if the glfw window should close or not
     *
     * @return returns true if close has been requested
     */
    @Override
    public boolean isCloseRequested() {
        return glfwWindowShouldClose(handle);
    }

    /**
     * Poll the window
     */
    @Override
    public void process() {
        glfwSwapBuffers(handle);
        glfwPollEvents();
    }


    /**
     * Dispose of the window
     */
    public void dispose() {
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}

package io.chunkworld.api.core.window;

/**
 * Represents some generic window. This allows for us to have different kinds of windows, for example
 * the client will likely always use a GLFW window, while the server will likely be window-less or
 * possible some kind of javafx/swing implementation
 */
public interface IWindow {
    String getTitle();

    int getWidth();

    int getHeight();

    boolean isFullscreen();

    boolean isVsync();

    boolean isResizable();

    void init();

    void process();

    void dispose();

    /**
     * Called when the window has requested close
     *
     * @return returns close request
     */
    boolean isCloseRequested();

}

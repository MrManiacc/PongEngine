package io.chunkworld.client.pong.systems;

import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.core.ecs.entity.system.EntitySystem;
import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.input.Input;
import io.chunkworld.api.core.window.IWindow;
import io.chunkworld.client.engine.load.LoadNanovg;
import io.chunkworld.client.pong.events.QuitGameEvent;
import io.chunkworld.client.pong.events.RestartGameEvent;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Renders the gui
 */
public class SimpleGuiSystem extends EntitySystem {
    private NVGColor color, color2, color3;
    @In private IWindow window;
    @In private ScoreSystem scoreSystem;
    @In private Input input;

    /**
     * Initialize the paint stuff
     */
    @Override
    public void initialize() {
        color = NVGColor.create();
        color2 = NVGColor.create();
        color3 = NVGColor.create();
    }

    /**
     * Render the scene
     */
    @Override
    protected void postProcess() {
        nvgBeginFrame(LoadNanovg.vg, window.getWidth(), window.getHeight(), window.getContentScale());
        render();
        nvgEndFrame(LoadNanovg.vg);
    }

    /**
     * Renders the gui
     */
    private void render() {
        if (!scoreSystem.isGameOver()) {
            drawText(100, 100, 32, scoreSystem.getOpponentScore() + "", rgba(255, 255, 255, 255));
            drawText(window.getWidth() - 100f, 100, 32, scoreSystem.getPlayerScore() + "", rgba(255, 255, 255, 255));
        } else {
            drawRestart();
        }
    }

    /**
     * Draws the restart menu
     */
    private void drawRestart() {
        //overlay
        drawRect(0, 0, window.getWidth(), window.getWidth(), rgba(0, 0, 0, 125));
        var title = "YOU LOST";
        if (scoreSystem.getPlayerScore() > scoreSystem.getOpponentScore())
            title = "YOU WON";
        drawText(window.getWidth() / 2.0f, 200, 64, title, rgba(255, 255, 255, 255));
        drawText(window.getWidth() / 2.0f, 240, 20, "(" + scoreSystem.getOpponentScore() + "-" + scoreSystem.getPlayerScore() + ")", rgba(255, 255, 255, 255));
        if (drawButton(window.getWidth() / 2, 275, 250, 60, "PLAY AGAIN", 32))
            Bus.Logic.post(new RestartGameEvent());
        if (drawButton(window.getWidth() / 2, 380 - 15, 250, 60, "QUIT", 32))
            Bus.Logic.post(new QuitGameEvent());
    }

    /**
     * Draws a button on the screen
     *
     * @return returns true if the button is clicked
     */
    private boolean drawButton(float x, float y, float w, float h, String text, float fontSize) {
        var cx = x - w / 2.0f;
        var mx = input.getMx();
        var my = input.getMy();

        var hovered = (mx >= cx && mx <= cx + w && my >= y && my <= y + h);

        var vg = LoadNanovg.vg;
        nvgBeginPath(vg);
        nvgRoundedRect(vg, cx, y, w, h, 8);
        nvgFillColor(vg, rgba(0, 0, 0, 255));
        nvgFill(vg);

        nvgBeginPath(vg);
        nvgRoundedRect(vg, cx, y, w, h, 8);
        nvgStrokeColor(vg, rgba(255, 255, 255, 255));
        nvgStrokeWidth(vg, 5);
        nvgStroke(vg);
        drawText(x, (y + h / 2.0f) + fontSize / 10.0f, fontSize, text, rgba(255, 255, 255, 255));
        return hovered && input.mousePressed(Input.MOUSE_BUTTON_LEFT);
    }

    /**
     * Draws a rect
     */
    private void drawRect(float x, float y, float w, float h, NVGColor color) {
        var vg = LoadNanovg.vg;
        // Window
        nvgBeginPath(vg);
        nvgRect(vg, x, y, w, h);
        nvgFillColor(vg, color);
        //nvgFillColor(vg, rgba(0,0,0,128, color));
        nvgFill(vg);
    }


    /**
     * Draws some text
     */
    private void drawText(float x, float y, float size, String text, NVGColor color) {
        var vg = LoadNanovg.vg;
        nvgFontSize(vg, size);
        nvgFontFace(vg, "default");
        nvgFillColor(vg, color);
        nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
        nvgText(vg, x, y, text);
    }

    /**
     * Creates an nvg color from the give color values
     */
    private NVGColor rgba(int r, int g, int b, int a) {
        return rgba(r, g, b, a, color);
    }

    /**
     * Creates an nvg color from the give color values
     */
    private NVGColor rgba(int r, int g, int b, int a, NVGColor color) {
        color.r(r / 255.0f);
        color.g(g / 255.0f);
        color.b(b / 255.0f);
        color.a(a / 255.0f);
        return color;
    }
}

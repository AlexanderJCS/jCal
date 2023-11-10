package selection;

import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.ColorShader;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.MouseEvent;
import jangl.shapes.Rect;
import jangl.shapes.Shape;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class Checkbox implements AutoCloseable {
    private final ColorShader colorShader;
    private final ShaderProgram colorShaderProgram;
    private final Rect rect;
    private boolean selected;
    private boolean toggledLastUpdate;

    public Checkbox(WorldCoords topLeft) {
        this.selected = false;
        this.toggledLastUpdate = false;
        this.colorShader = new ColorShader(ColorFactory.fromNormalized(1, 1, 1, 1));
        this.colorShaderProgram = new ShaderProgram(this.colorShader);
        this.resetColors();

        this.rect = new Rect(topLeft, 0.05f, 0.05f);
    }

    public boolean isSelected() {
        return this.selected;
    }

    public boolean wasToggledLastUpdate() {
        return this.toggledLastUpdate;
    }

    public void toggle() {
        this.toggledLastUpdate = true;
        this.selected = !this.selected;
        this.resetColors();
    }

    public WorldCoords getDimensions() {
        return new WorldCoords(this.rect.getWidth(), this.rect.getHeight());
    }

    public void draw() {
        this.rect.draw(this.colorShaderProgram);
    }

    public void update(List<MouseEvent> mouseEvents) {
        this.toggledLastUpdate = false;

        for (MouseEvent event : mouseEvents) {
            if (event.button != GLFW.GLFW_MOUSE_BUTTON_1 || event.action != GLFW.GLFW_PRESS) {
                continue;
            }

            if (Shape.collides(this.rect, Mouse.getMousePos())) {
                this.toggle();
            }
        }
    }

    private void resetColors() {
        if (this.selected) {
            this.colorShader.setColor(ColorFactory.fromNormalized(0.75f, 0, 0, 1));
        } else {
            this.colorShader.setColor(ColorFactory.fromNormalized(0, 0, 0, 1));
        }
    }

    @Override
    public void close() {
        this.colorShaderProgram.close();
        this.rect.close();
    }
}

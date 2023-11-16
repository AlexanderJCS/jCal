package uihelper.checkbox;

import jangl.color.Color;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.ColorShader;
import jangl.graphics.shaders.premade.TextureShaderVert;
import jangl.graphics.textures.Texture;
import jangl.graphics.textures.TextureBuilder;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.MouseEvent;
import jangl.shapes.Rect;
import jangl.shapes.Shape;
import org.lwjgl.glfw.GLFW;
import shaders.CheckboxShader;

import java.util.List;

public class Checkbox implements AutoCloseable {
    private final ColorShader colorShader;
    private final Rect rect;
    private boolean selected;
    private final ShaderProgram checkboxShader;

    private static final Texture EMPTY_CHECKBOX = new Texture(new TextureBuilder().setImagePath("resources/textures/empty_checkbox.png").setSmoothScaling());
    private static final Texture FULL_CHECKBOX = new Texture(new TextureBuilder().setImagePath("resources/textures/full_checkbox.png").setSmoothScaling());

    public Checkbox(WorldCoords topLeft, Color calendarColor) {
        this.selected = true;
        this.colorShader = new ColorShader(ColorFactory.fromNormalized(1, 1, 1, 1));
        this.resetColors();

        this.rect = new Rect(topLeft, 0.05f, 0.05f);

        this.checkboxShader = new ShaderProgram(
                new TextureShaderVert(),
                new CheckboxShader(ColorFactory.from255(0, 200, 0, 255), calendarColor)
        );
    }

    public static void initTextures() {
        EMPTY_CHECKBOX.useDefaultShader(false);
        FULL_CHECKBOX.useDefaultShader(false);
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void toggle() {
        this.selected = !this.selected;
        this.resetColors();
    }

    public void setState(boolean state) {
        if (state != this.selected) {
            this.toggle();
        }
    }

    public WorldCoords getDimensions() {
        return new WorldCoords(this.rect.getWidth(), this.rect.getHeight());
    }

    public void draw() {
        this.checkboxShader.bind();
        this.rect.draw(this.selected ? FULL_CHECKBOX : EMPTY_CHECKBOX);
        this.checkboxShader.unbind();
    }

    public void update(List<MouseEvent> mouseEvents) {
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
        this.checkboxShader.close();
        this.rect.close();
    }
}

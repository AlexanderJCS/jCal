package shaders;

import jangl.color.Color;
import jangl.graphics.shaders.FragmentShader;

import java.io.UncheckedIOException;
import java.util.Objects;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform3f;

public class CheckboxShader extends FragmentShader {
    private Color findColor;
    private Color replaceColor;

    public CheckboxShader(Color findColor, Color replaceColor) throws UncheckedIOException {
        super(
                Objects.requireNonNull(
                        CheckboxShader.class.getResourceAsStream("/shaders/checkboxShader.frag")
                )
        );

        this.findColor = findColor;
        this.replaceColor = replaceColor;
    }

    public Color getFindColor() {
        return this.findColor;
    }

    public void setFindColor(Color findColor) {
        this.findColor = findColor;
    }

    public Color getReplaceColor() {
        return this.replaceColor;
    }

    public void setReplaceColor(Color replaceColor) {
        this.replaceColor = replaceColor;
    }

    @Override
    public void setUniforms(int programID) {
        int findColorLocation = glGetUniformLocation(programID, "findColor");

        if (findColorLocation == -1) {
            throw new RuntimeException("Could not find uniform variable \"findColor\" in the checkbox shader.");
        }

        glUniform3f(
                findColorLocation,
                this.findColor.getNormRed(),
                this.findColor.getNormGreen(),
                this.findColor.getNormBlue()
        );

        int replaceColorLocation = glGetUniformLocation(programID, "replaceColor");

        if (replaceColorLocation == -1) {
            throw new RuntimeException("Could not find uniform variable \"replaceColor\" in the checkbox shader.");
        }

        glUniform3f(
                replaceColorLocation,
                this.replaceColor.getNormRed(),
                this.replaceColor.getNormGreen(),
                this.replaceColor.getNormBlue()
        );
    }
}

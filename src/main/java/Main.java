import jangl.JANGL;
import jangl.color.ColorFactory;
import jangl.graphics.textures.TextureBuilder;
import jangl.io.Window;
import selection.Checkbox;
import uihelper.Fonts;

public class Main {
    public static void main(String[] args) {
        JANGL.init(0.65f, 16f/9);
        Fonts.init();
        Checkbox.initTextures();
        Window.setVsync(true);
        Window.setIcon(new TextureBuilder().setImagePath("resources/icon.png"));
        Window.setTitle("jCal");
        Window.setClearColor(ColorFactory.fromNormalized(0.9f, 0.9f, 0.9f, 1));

        try (JCal jCal = new JCal()) {
            jCal.run();
        }

        Window.close();
    }
}
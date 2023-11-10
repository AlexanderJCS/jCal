package uihelper;


import jangl.color.ColorFactory;
import jangl.graphics.font.Font;

public class Fonts {
    public static final Font ARIAL_WHITE = new Font("resources/font/arial.fnt", "resources/font/arial.png");
    public static final Font ARIAL_BLACK = new Font("resources/font/arial.fnt", "resources/font/arial.png");

    public static void init() {
        ARIAL_BLACK.setFontColor(ColorFactory.fromNormalized(0, 0, 0, 1));
    }
}

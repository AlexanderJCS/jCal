import jangl.JANGL;
import jangl.color.ColorFactory;
import jangl.io.Window;

public class Main {
    public static void main(String[] args) {
        JANGL.init(0.65f, 16f/9);
        Window.setVsync(true);
        Window.setClearColor(ColorFactory.fromNormalized(0.7f, 0.7f, 0.7f, 1));

        try (JCal jCal = new JCal()) {
            jCal.run();
        }

        Window.close();
    }
}
import jangl.JANGL;
import jangl.io.Window;

public class Main {
    public static void main(String[] args) {
        JANGL.init(0.65f, 16f/9);
        Window.setVsync(true);

        try (JCal jCal = new JCal()) {
            jCal.run();
        }

        Window.close();
    }
}
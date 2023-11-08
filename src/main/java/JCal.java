import calendar.Calendar;
import jangl.JANGL;
import jangl.io.Window;

public class JCal implements AutoCloseable {
    private final Calendar calendar;

    public JCal() {
        this.calendar = new Calendar();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.calendar.draw();

            JANGL.update();
        }
    }

    @Override
    public void close() {
        this.calendar.close();
    }
}

import calendar.Calendar;
import calendar.CalendarSet;
import jangl.JANGL;
import jangl.io.Window;

public class JCal implements AutoCloseable {
    private final CalendarSet calendarSet;

    public JCal() {
        this.calendarSet = new CalendarSet();
    }

    public void run() {
        while (Window.shouldRun()) {
            Window.clear();
            this.calendarSet.draw();

            JANGL.update();
        }
    }

    @Override
    public void close() {
        this.calendarSet.close();
    }
}

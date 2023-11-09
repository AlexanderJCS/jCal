import calendar.CalendarSet;
import jangl.JANGL;
import jangl.io.Window;
import parser.JCalParser;

public class JCal implements AutoCloseable {
    private final CalendarSet calendarSet;

    public JCal() {
        this.calendarSet = new CalendarSet();
        this.calendarSet.addCalendar(new JCalParser().parse(this.calendarSet.getCanvas(), "src/main/resources/calendars/alex.jcal"));
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

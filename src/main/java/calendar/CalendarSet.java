package calendar;

import java.util.ArrayList;
import java.util.List;

public class CalendarSet implements AutoCloseable {
    private final List<Calendar> calendars;

    public CalendarSet() {
        this.calendars = new ArrayList<>();

        this.addCalendar(new Calendar());
        this.addCalendar(new Calendar());
    }

    public void addCalendar(Calendar calendar) {
        this.calendars.add(calendar);

        for (int i = 0; i < this.calendars.size(); i++) {
            this.calendars.get(i).setCalendarNumber(i + 1, this.calendars.size());
        }
    }

    public void draw() {
        for (Calendar calendar : this.calendars) {
            calendar.draw();
        }
    }

    @Override
    public void close() {
        for (Calendar calendar : this.calendars) {
            calendar.close();
        }
    }
}

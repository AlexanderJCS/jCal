package calendar;

import calendar.ui.DayMarkings;
import calendar.ui.TimeMarkings;
import jangl.coords.WorldCoords;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarSet implements AutoCloseable {
    private final List<Calendar> calendars;
    private final CalendarCanvas canvas;
    private final TimeMarkings timeMarkings;
    private final DayMarkings dayMarkings;

    public CalendarSet(CalendarCanvas canvas) {
        this.canvas = canvas;

        this.timeMarkings = new TimeMarkings(this.canvas);
        this.dayMarkings = new DayMarkings(this.canvas);

        this.calendars = new ArrayList<>();
    }

    public void addCalendar(Calendar calendar) {
        this.calendars.add(calendar);
        this.refreshCalendarNumbers();
    }

    public void removeCalendar(Calendar calendar) {
        this.calendars.remove(calendar);
        this.refreshCalendarNumbers();
    }

    private void refreshCalendarNumbers() {
        for (int i = 0; i < this.calendars.size(); i++) {
            this.calendars.get(i).setCalendarNumber(i + 1, this.calendars.size());
        }
    }

    public List<Calendar> getCalendars() {
        return new ArrayList<>(this.calendars);
    }

    public CalendarCanvas getCanvas() {
        return canvas;
    }

    public void draw() {
        this.timeMarkings.draw();
        this.dayMarkings.draw();

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

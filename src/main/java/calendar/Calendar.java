package calendar;

import jangl.coords.WorldCoords;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Calendar implements AutoCloseable {
    private final CalendarCanvas canvas;
    private final List<CalendarEvent> events;

    public Calendar() {
        this.canvas = new CalendarCanvas(
                new WorldCoords(0, WorldCoords.getTopRight().y),
                WorldCoords.getTopRight(),
                LocalTime.of(8, 0),
                LocalTime.of(20,0)
        );

        this.events = new ArrayList<>();
        this.events.add(new CalendarEvent("Test", this.canvas, WeekDay.FRI, LocalTime.of(9, 0), LocalTime.of(17, 0)));
    }

    public void setCalendarNumber(int calendarNumber, int numCalendars) {
        for (CalendarEvent event : this.events) {
            event.setCalendarNumber(calendarNumber, numCalendars);
        }
    }

    public void draw() {
        for (CalendarEvent event : this.events) {
            event.draw();
        }
    }

    @Override
    public void close() {
        for (CalendarEvent event : this.events) {
            event.close();
        }
    }
}

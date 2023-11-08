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

    public CalendarSet() {
        float canvasTopYPadding = 0.1f;

        this.canvas = new CalendarCanvas(
                new WorldCoords(0, WorldCoords.getTopRight().y - canvasTopYPadding),
                new WorldCoords(WorldCoords.getTopRight().x, WorldCoords.getTopRight().y - canvasTopYPadding),
                LocalTime.of(8, 0),
                LocalTime.of(20,0)
        );

        this.timeMarkings = new TimeMarkings(this.canvas);
        this.dayMarkings = new DayMarkings(this.canvas);

        this.calendars = new ArrayList<>();
        this.addCalendar(new Calendar(this.canvas));
        this.addCalendar(new Calendar(this.canvas));
    }

    public void addCalendar(Calendar calendar) {
        this.calendars.add(calendar);

        for (int i = 0; i < this.calendars.size(); i++) {
            this.calendars.get(i).setCalendarNumber(i + 1, this.calendars.size());
        }
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

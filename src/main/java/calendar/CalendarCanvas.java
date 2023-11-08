package calendar;

import jangl.coords.WorldCoords;
import uihelper.Canvas;

import java.time.LocalTime;

public class CalendarCanvas extends Canvas {
    private final LocalTime startTime, endTime;

    public CalendarCanvas(WorldCoords topLeft, WorldCoords widthHeight, LocalTime startTime, LocalTime endTime) {
        super(topLeft, widthHeight);

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime startTime() {
        return this.startTime;
    }

    public LocalTime endTime() {
        return this.endTime;
    }

    /**
     * The width, in WorldCoords, of one of the day columns in the CalendarCanvas. Equal to this.width() / WeekDay.numDays().
     *
     * @return The width, in WorldCoords, of one of the day columns in the CalendarCanvas.
     */
    public float columnWidth() {
        return this.width() / WeekDay.numDays();
    }
}

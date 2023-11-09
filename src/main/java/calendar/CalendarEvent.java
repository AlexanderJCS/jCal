package calendar;

import jangl.coords.WorldCoords;
import jangl.graphics.font.Justify;
import jangl.graphics.font.Text;
import jangl.shapes.Rect;
import uihelper.Fonts;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class CalendarEvent implements AutoCloseable {
    private final Text text;
    private final WeekDay day;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Rect rect;
    private final CalendarCanvas canvas;

    public CalendarEvent(String title, CalendarCanvas canvas, WeekDay day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.canvas = canvas;

        // Insert temporary values for the rectangle, since it will be set to non-temp values when the setCalendarNumber
        // method is run
        this.rect = new Rect(new WorldCoords(0, 0), 0, 0);
        this.regenerateRect(1, 1);

        this.text = new Text(this.rect.getTransform().getCenter(), Fonts.ARIAL_WHITE, 0.025f, title, Justify.CENTER);
        this.text.getTransform().rotate((float) -Math.PI / 2);  // Rotate the text -90 degrees
    }

    private void regenerateRect(int calendarNumber, int numCalendars) {
        /*
         * to anybody, including my future self, reading this code:
         * good luck...
         */

        int calendarDuration = (int) this.canvas.startTime().until(this.canvas.endTime(), ChronoUnit.SECONDS);
        int durationUntilEvent = (int) canvas.startTime().until(this.startTime, ChronoUnit.SECONDS);
        int eventDuration = (int) startTime.until(this.endTime, ChronoUnit.SECONDS);

        float widthScale = (float) 1 / numCalendars;
        float rectWidth = this.canvas.columnWidth();

        float rectHeight = (float) eventDuration / calendarDuration * this.canvas.height();

        float rectTop = this.canvas.height() - (float) durationUntilEvent / calendarDuration * this.canvas.height();

        float rectLeft = (float) this.day.ordinal() / WeekDay.numDays() * this.canvas.width() + this.canvas.topLeft().x;

        // Offset the top left coordinate so that the calendar event doesn't stay in the middle of the column when the
        // width of the event is not equal to the width of the column

        // The -0.5f * (numCalendars - 1) needs to be added since the calendar event is centered in the middle
        // of the column, instead of the top left of the column.
        rectLeft += this.canvas.columnWidth() * (calendarNumber - 1f - 0.5f * (numCalendars - 1)) / numCalendars;

        // Set the intended rect position. Add the top and the left values by width and height / 2 since setPos sets the
        // center of the object.
        this.rect.getTransform().setPos(new WorldCoords(rectLeft + rectWidth / 2, rectTop - rectHeight / 2));

        this.rect.setWidth(rectWidth * widthScale);

        // If the height is different, change the height
        if (Math.abs(this.rect.getHeight() - rectHeight) > 0.0001f) {
            this.rect.setHeight(rectHeight);
        }
    }

    public void setCalendarNumber(int calendarNumber, int numCalendars) {
        this.regenerateRect(calendarNumber, numCalendars);
        this.text.getTransform().setPos(this.rect.getTransform().getCenter());
    }

    public void draw() {
        this.rect.draw();
        this.text.draw();
    }

    @Override
    public void close() {
        this.rect.close();
    }
}

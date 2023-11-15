package calendar;

import calendar.ui.CalendarTitle;
import jangl.color.Color;
import jangl.color.ColorFactory;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.ColorShader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Calendar implements AutoCloseable {
    private final List<CalendarEvent> events;
    private Color color;
    private ShaderProgram colorShader;
    private final CalendarTitle title;

    public Calendar(CalendarCanvas canvas) {
        this.color = ColorFactory.fromNormalizedHSVA((float) Math.random(), 0.7f, 0.7f, 1);
        this.colorShader = new ShaderProgram(new ColorShader(this.color));
        this.title = new CalendarTitle(canvas, "");

        this.events = new ArrayList<>();
    }

    public void setCalendarTitle(String name) {
        this.title.setTitle(name);
    }

    public String getTitle() {
        return this.title.getTitle();
    }

    public void setCalendarNumber(int calendarNumber, int numCalendars) {
        for (CalendarEvent event : this.events) {
            event.setCalendarNumber(calendarNumber, numCalendars);
        }

        this.title.setCalendarNumber(calendarNumber, numCalendars);
    }

    public void addEvent(CalendarEvent event) {
        this.events.add(event);
    }

    public List<CalendarEvent> getEvents() {
        return new ArrayList<>(this.events);
    }

    public void draw() {
        for (CalendarEvent event : this.events) {
            this.colorShader.bind();
            event.draw();
            this.colorShader.unbind();
        }

        this.title.draw();
    }

    public void setColor(Color color) {
        this.color = color;
        this.colorShader.close();
        this.colorShader = new ShaderProgram(new ColorShader(color));
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public void close() {
        // TODO: code smell! only de-allocate resources that the object allocated
        for (CalendarEvent event : this.events) {
            event.close();
        }

        this.title.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calendar calendar = (Calendar) o;
        return Objects.equals(this.events, calendar.events) && Objects.equals(this.colorShader, calendar.colorShader) && Objects.equals(this.title, calendar.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.events, this.colorShader, this.title);
    }
}

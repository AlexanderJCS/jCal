package calendar;

import calendar.ui.CalendarTitle;
import jangl.color.Color;
import jangl.color.ColorFactory;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.ColorShader;

import java.util.ArrayList;
import java.util.List;

public class Calendar implements AutoCloseable {
    private final List<CalendarEvent> events;
    private ShaderProgram colorShader;
    private final CalendarTitle title;

    public Calendar(CalendarCanvas canvas) {
        this.colorShader = new ShaderProgram(new ColorShader(ColorFactory.fromNormalizedHSVA((float) Math.random(), 0.7f, 0.7f, 1)));
        this.title = new CalendarTitle(canvas, "");

        this.events = new ArrayList<>();
    }

    public void setCalendarTitle(String name) {
        this.title.setTitle(name);
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

    public void draw() {
        for (CalendarEvent event : this.events) {
            this.colorShader.bind();
            event.draw();
            this.colorShader.unbind();
        }

        this.title.draw();
    }

    public void setColor(Color color) {
        this.colorShader.close();
        this.colorShader = new ShaderProgram(new ColorShader(color));
    }

    @Override
    public void close() {
        // TODO: code smell! only de-allocate resources that the object allocated
        for (CalendarEvent event : this.events) {
            event.close();
        }

        this.title.close();
    }
}

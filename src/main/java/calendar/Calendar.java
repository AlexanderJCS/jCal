package calendar;

import jangl.color.Color;
import jangl.color.ColorFactory;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.ColorShader;

import java.util.ArrayList;
import java.util.List;

public class Calendar implements AutoCloseable {
    private final List<CalendarEvent> events;
    private ShaderProgram colorShader;
    private String calendarName;

    public Calendar() {
        this.colorShader = new ShaderProgram(new ColorShader(ColorFactory.fromNormalizedHSVA((float) Math.random(), 0.7f, 0.7f, 1)));
        this.calendarName = "";

        this.events = new ArrayList<>();
    }

    public void setCalendarNumber(int calendarNumber, int numCalendars) {
        for (CalendarEvent event : this.events) {
            event.setCalendarNumber(calendarNumber, numCalendars);
        }
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
    }

    public void setCalendarName(String name) {
        this.calendarName = name;
    }

    public void setColor(Color color) {
        this.colorShader.close();
        this.colorShader = new ShaderProgram(new ColorShader(color));
    }

    @Override
    public void close() {
        for (CalendarEvent event : this.events) {
            event.close();
        }
    }
}

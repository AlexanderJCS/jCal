package calendar;

import jangl.color.Color;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.ColorShader;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Calendar implements AutoCloseable {
    private final CalendarCanvas canvas;
    private final List<CalendarEvent> events;
    private final ShaderProgram colorShader;

    public Calendar(CalendarCanvas canvas) {
        this.canvas = canvas;
        this.colorShader = new ShaderProgram(new ColorShader(ColorFactory.fromNormalizedHSVA((float) Math.random(), 0.7f, 0.7f, 1)));

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

    @Override
    public void close() {
        for (CalendarEvent event : this.events) {
            event.close();
        }
    }
}

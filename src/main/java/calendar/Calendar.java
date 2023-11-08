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

    public Calendar() {
        this.canvas = new CalendarCanvas(
                new WorldCoords(0, WorldCoords.getTopRight().y),
                WorldCoords.getTopRight(),
                LocalTime.of(8, 0),
                LocalTime.of(20,0)
        );

        this.colorShader = new ShaderProgram(new ColorShader(ColorFactory.fromNormalizedHSVA((float) Math.random(), 0.7f, 0.7f, 1)));

        this.events = new ArrayList<>();
        this.events.add(new CalendarEvent("Test", this.canvas, WeekDay.MON, LocalTime.of(9, 0), LocalTime.of(17, 0)));
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

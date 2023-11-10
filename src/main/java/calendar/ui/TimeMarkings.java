package calendar.ui;

import calendar.CalendarCanvas;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.batching.Batch;
import jangl.graphics.batching.BatchBuilder;
import jangl.graphics.font.Font;
import jangl.graphics.font.Justify;
import jangl.graphics.font.Text;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.ColorShader;
import jangl.shapes.Rect;
import uihelper.Fonts;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TimeMarkings implements AutoCloseable {
    private final Font font;
    private final List<Text> timeText;
    private final Batch lines;
    private final ShaderProgram lineShader;

    public TimeMarkings(CalendarCanvas canvas) {
        this.font = Fonts.ARIAL_BLACK;

        this.timeText = this.generateTimeText(canvas);
        this.lines = this.generateLines(canvas);

        this.lineShader = new ShaderProgram(new ColorShader(ColorFactory.fromNormalized(0.7f, 0.7f, 0.7f, 1)));
    }

    private List<Text> generateTimeText(CalendarCanvas canvas) {
        // "Per minute" and "Per hour" refer to the representation of second and hours on the canvas, not actual time
        float worldCoordsPerMinute = canvas.height() / canvas.startTime().until(canvas.endTime(), ChronoUnit.MINUTES);

        List<Text> timeText = new ArrayList<>();

        // There's probably much faster ways to do exactly what I'm doing now but computers are fast, this operation is
        // inexpensive, and it only needs to be run once so whatever

        float textHeight = 0.03f;
        WorldCoords worldCoordLocation = canvas.topLeft();
        worldCoordLocation.y += textHeight / 2;
        worldCoordLocation.y += worldCoordsPerMinute;
        worldCoordLocation.x -= 0.01f;

        // Add 1 minute to the start time to skip the first hour, assuming that the canvas starts exactly at the top of
        // the hour. This makes the canvas look more aesthetically pleasing
        for (LocalTime time = canvas.startTime().plusMinutes(1); time.isBefore(canvas.endTime()); time = time.plusMinutes(1)) {
            worldCoordLocation.y -= worldCoordsPerMinute;

            if (time.getMinute() != 0) {
                continue;
            }

            timeText.add(new Text(worldCoordLocation, this.font, textHeight, time.getHour() + ":00", Justify.RIGHT));
        }

        return timeText;
    }

    private Batch generateLines(CalendarCanvas canvas) {
        BatchBuilder builder = new BatchBuilder();

        // "Per minute" and "Per hour" refer to the representation of second and hours on the canvas, not actual time
        float worldCoordsPerMinute = canvas.height() / canvas.startTime().until(canvas.endTime(), ChronoUnit.MINUTES);

        // There's probably much faster ways to do exactly what I'm doing now but computers are fast, this operation is
        // inexpensive, and it only needs to be run once so whatever

        float lineHeight = 0.002f;
        WorldCoords worldCoordLocation = canvas.topLeft();
        worldCoordLocation.y += worldCoordsPerMinute;

        // Add 1 minute to the start time to skip the first hour, assuming that the canvas starts exactly at the top of
        // the hour. This makes the canvas look more aesthetically pleasing
        for (LocalTime time = canvas.startTime(); time.isBefore(canvas.endTime()); time = time.plusMinutes(1)) {
            worldCoordLocation.y -= worldCoordsPerMinute;

            if (time.getMinute() != 0) {
                continue;
            }

            Rect rect = new Rect(worldCoordLocation, canvas.width(), lineHeight);
            builder.addObject(rect);
            rect.close();
        }

        return new Batch(builder);
    }

    public void draw() {
        this.lines.draw(this.lineShader);

        for (Text text : this.timeText) {
            text.draw();
        }
    }

    @Override
    public void close() {
        for (Text text : this.timeText) {
            text.close();
        }

        this.lines.close();
        this.font.close();
        this.lineShader.close();
    }
}

package calendar.ui;

import calendar.CalendarCanvas;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Justify;
import jangl.graphics.font.Text;
import uihelper.Fonts;

import java.util.ArrayList;
import java.util.List;

public class CalendarTitle implements AutoCloseable {
    private static final float FONT_HEIGHT = 0.02f;

    private String title;
    private final List<Text> labels;
    private final CalendarCanvas canvas;

    public CalendarTitle(CalendarCanvas canvas, String title) {
        this.labels = new ArrayList<>();
        this.canvas = canvas;
        this.title = title;

        this.generateText();
    }

    private void generateText() {
        WorldCoords cursor = new WorldCoords(
                this.canvas.topLeft().x + this.canvas.columnWidth() / 2,
                this.canvas.topLeft().y + FONT_HEIGHT
        );

        while (cursor.x < this.canvas.topLeft().x + this.canvas.width()) {
            this.labels.add(
                    new Text(cursor, Fonts.ARIAL_BLACK, FONT_HEIGHT, this.title, Justify.CENTER)
            );

            cursor.x += this.canvas.columnWidth();
        }
    }

    public void setTitle(String title) {
        this.title = title;

        for (Text label : this.labels) {
            label.setText(title);
        }
    }

    public void setCalendarNumber(int calendarNumber, int numCalendars) {
        WorldCoords cursor = new WorldCoords(
                this.canvas.topLeft().x + this.canvas.columnWidth() * calendarNumber / numCalendars,
                this.canvas.topLeft().y + FONT_HEIGHT
        );

        // Spacing changes
        cursor.x -= this.canvas.columnWidth() / numCalendars / 2;

        for (Text label : this.labels) {
            label.getTransform().setPos(cursor);
            cursor.x += this.canvas.columnWidth();
        }
    }

    public void draw() {
        for (Text label : this.labels) {
            label.draw();
        }
    }

    @Override
    public void close() {
        for (Text label : this.labels) {
            label.close();
        }
    }
}

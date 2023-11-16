package calendar.ui;

import calendar.CalendarCanvas;
import calendar.WeekDay;
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

import java.util.ArrayList;
import java.util.List;

public class DayMarkings implements AutoCloseable {
    private final Font font;
    private final List<Text> dayMarkings;
    private final Batch lines;
    private final ShaderProgram lineShader;

    public DayMarkings(CalendarCanvas canvas) {
        this.font = Fonts.ARIAL_BLACK;

        this.dayMarkings = this.generateDayMarkings(canvas);
        this.lines = this.generateLines(canvas);
        this.lineShader = new ShaderProgram(new ColorShader(ColorFactory.fromNormalized(0.7f, 0.7f, 0.7f, 1)));
    }

    private List<Text> generateDayMarkings(CalendarCanvas canvas) {
        List<Text> dayMarkings = new ArrayList<>();
        WorldCoords cursor = canvas.topLeft();
        float jumpDist = canvas.width() / WeekDay.numDays();
        cursor.x += jumpDist / 2;  // Center the cursor in the middle of the week day column

        float fontSize = 0.05f;

        cursor.y += fontSize * 2;

        for (WeekDay day : WeekDay.values()) {
            dayMarkings.add(new Text(cursor, this.font, fontSize, day.toString(), Justify.CENTER));

            cursor.x += jumpDist;
        }

        return dayMarkings;
    }

    private Batch generateLines(CalendarCanvas canvas) {
        BatchBuilder builder = new BatchBuilder();
        WorldCoords cursor = canvas.topLeft();
        float jumpDist = canvas.width() / WeekDay.numDays();

        float lineWidth = 0.002f;

        for (int i = 0; i < WeekDay.numDays() + 1; i++) {
            Rect rect = new Rect(cursor, lineWidth, canvas.height());

            builder.addObject(rect);
            rect.close();

            cursor.x += jumpDist;
        }

        return new Batch(builder);
    }

    public void draw() {
        this.lines.draw(this.lineShader);

        for (Text text : this.dayMarkings) {
            text.draw();
        }
    }

    @Override
    public void close() {
        for (Text text : this.dayMarkings) {
            text.close();
        }

        this.lines.close();
        this.lineShader.close();
    }
}

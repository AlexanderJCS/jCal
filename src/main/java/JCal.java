import calendar.Calendar;
import calendar.CalendarCanvas;
import calendar.CalendarSet;
import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.io.mouse.Mouse;
import parser.JCalParser;
import selection.SelectionCanvas;
import selection.SelectionField;

import java.time.LocalTime;

public class JCal implements AutoCloseable {
    private final CalendarSet calendarSet;
    private final SelectionField selectionField;

    public JCal() {
        float canvasTopPadding = 0.1f;
        float canvasLeftPadding = 0.1f;
        float canvasRightPadding = 0.3f;
        float canvasBottomPadding = 0f;

        CalendarCanvas calendarCanvas = new CalendarCanvas(
                new WorldCoords(canvasLeftPadding, WorldCoords.getTopRight().y - canvasTopPadding),
                new WorldCoords(WorldCoords.getTopRight().x - canvasLeftPadding - canvasRightPadding, WorldCoords.getTopRight().y - canvasTopPadding - canvasBottomPadding),
                LocalTime.of(8, 0),
                LocalTime.of(18,0)
        );

        this.calendarSet = new CalendarSet(calendarCanvas);

        this.calendarSet.addCalendar(new JCalParser().parse(this.calendarSet.getCanvas(), "src/main/resources/calendars/alex.jcal"));
        this.calendarSet.addCalendar(new JCalParser().parse(this.calendarSet.getCanvas(), "src/main/resources/calendars/ash.jcal"));
        this.calendarSet.addCalendar(new JCalParser().parse(this.calendarSet.getCanvas(), "src/main/resources/calendars/camila.jcal"));
        this.calendarSet.addCalendar(new JCalParser().parse(this.calendarSet.getCanvas(), "src/main/resources/calendars/eden.jcal"));

        WorldCoords selectionCanvasTopLeft = calendarCanvas.topLeft();
        selectionCanvasTopLeft.x += calendarCanvas.width() + 0.05f;

        WorldCoords selectionCanvasWidthHeight = new WorldCoords(
                WorldCoords.getTopRight().x - selectionCanvasTopLeft.x,
                calendarCanvas.widthHeight().y
        );

        this.selectionField = new SelectionField(
                new SelectionCanvas(selectionCanvasTopLeft, selectionCanvasWidthHeight),
                this.calendarSet
        );
    }

    private void draw() {
        Window.clear();
        this.calendarSet.draw();
        this.selectionField.draw();
    }

    private void update() {
        this.selectionField.update(Mouse.getEvents());
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();
            this.update();

            JANGL.update();
        }
    }

    @Override
    public void close() {
        this.calendarSet.close();
        this.selectionField.close();
    }
}

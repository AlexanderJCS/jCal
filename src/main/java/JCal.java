import calendar.CalendarCanvas;
import calendar.CalendarSet;
import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.io.mouse.Mouse;
import parser.JCalParser;
import selection.SelectionCanvas;
import selection.SelectionField;

import java.io.File;
import java.time.LocalTime;

public class JCal implements AutoCloseable {
    private final CalendarSet calendarSet;
    private final SelectionField selectionField;

    public JCal() {
        CalendarCanvas calendarCanvas = getCalendarCanvas();

        this.calendarSet = new CalendarSet(calendarCanvas);
        loadCalendars(this.calendarSet);

        this.selectionField = new SelectionField(
                getSelectionCanvas(calendarCanvas),
                this.calendarSet
        );
    }

    private static CalendarCanvas getCalendarCanvas() {
        float canvasTopPadding = 0.1f;
        float canvasLeftPadding = 0.1f;
        float canvasRightPadding = 0.3f;
        float canvasBottomPadding = 0f;

        return new CalendarCanvas(
                new WorldCoords(canvasLeftPadding, WorldCoords.getTopRight().y - canvasTopPadding),
                new WorldCoords(WorldCoords.getTopRight().x - canvasLeftPadding - canvasRightPadding, WorldCoords.getTopRight().y - canvasTopPadding - canvasBottomPadding),
                LocalTime.of(8, 0),
                LocalTime.of(18,0)
        );
    }

    private static SelectionCanvas getSelectionCanvas(CalendarCanvas calendarCanvas) {
        WorldCoords selectionCanvasTopLeft = calendarCanvas.topLeft();
        selectionCanvasTopLeft.x += calendarCanvas.width() + 0.05f;

        WorldCoords selectionCanvasWidthHeight = new WorldCoords(
                WorldCoords.getTopRight().x - selectionCanvasTopLeft.x,
                calendarCanvas.widthHeight().y
        );

        return new SelectionCanvas(selectionCanvasTopLeft, selectionCanvasWidthHeight);
    }

    private static void loadCalendars(CalendarSet calendarSet) {
        File calendarDir = new File("src/main/resources/calendars");
        File[] calendarFiles = calendarDir.listFiles();

        if (calendarFiles == null) {
            throw new RuntimeException("Could not find the directory " + calendarDir.getPath());
        }

        for (File calendarFile : calendarFiles) {
            // Skip non-calendar files
            if (calendarFile.isDirectory() || !calendarFile.getName().endsWith(".jcal")) {
                continue;
            }

            calendarSet.addCalendar(
                    new JCalParser().parse(calendarSet.getCanvas(), calendarFile.getPath())
            );
        }
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

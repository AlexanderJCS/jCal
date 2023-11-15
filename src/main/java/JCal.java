import calendar.CalendarCanvas;
import calendar.CalendarSet;
import calendarview.ViewSharedClasses;
import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.MouseEvent;
import parser.JCalParser;
import selection.SelectionField;

import java.io.File;
import java.time.LocalTime;
import java.util.List;

public class JCal implements AutoCloseable {
    private final CalendarSet calendarSet;
    private final SelectionField selectionField;
    private final ViewSharedClasses viewSharedClasses;

    public JCal() {
        CalendarCanvas calendarCanvas = getCalendarCanvas();

        this.calendarSet = new CalendarSet(calendarCanvas);
        loadCalendars(this.calendarSet);

        WorldCoords viewSharedClassesTopLeft = calendarCanvas.topLeft();
        viewSharedClassesTopLeft.x += calendarCanvas.width() + 0.05f;

        this.viewSharedClasses = new ViewSharedClasses(
                viewSharedClassesTopLeft,
                this.calendarSet
        );

        WorldCoords selectionFieldTopLeft = new WorldCoords(viewSharedClassesTopLeft.x, viewSharedClassesTopLeft.y);
        selectionFieldTopLeft.y -= this.viewSharedClasses.getDimensions().y + 0.05f;

        this.selectionField = new SelectionField(
                selectionFieldTopLeft,
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

    private static void loadCalendars(CalendarSet calendarSet) {
        File calendarDir = new File("resources/calendars");
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

        if (!this.viewSharedClasses.isSelected()) {
            this.calendarSet.draw();
        }

        this.selectionField.draw();
        this.viewSharedClasses.draw();
    }

    private void update() {
        List<MouseEvent> mouseEvents = Mouse.getEvents();

        this.selectionField.update(mouseEvents);
        this.viewSharedClasses.update(mouseEvents);
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
        this.viewSharedClasses.close();
    }
}

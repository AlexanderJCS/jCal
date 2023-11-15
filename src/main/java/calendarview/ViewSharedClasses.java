package calendarview;

import calendar.CalendarSet;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.io.mouse.MouseEvent;
import uihelper.checkbox.CheckboxWithText;

import java.util.List;

public class ViewSharedClasses implements AutoCloseable {
    private final CalendarSet set;
    private final CheckboxWithText checkbox;

    public ViewSharedClasses(WorldCoords topLeft, CalendarSet set) {
        this.set = set;
        this.checkbox = new CheckboxWithText(topLeft, "Shared", ColorFactory.from255(0, 200, 0, 255));
        this.checkbox.setState(false);
    }

    public void draw() {
        this.checkbox.draw();
    }

    public void update(List<MouseEvent> mouseEvents) {
        this.checkbox.update(mouseEvents);
    }

    @Override
    public void close() {
        this.checkbox.close();
    }
}

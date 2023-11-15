package sharedclasses;

import calendar.CalendarSet;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import uihelper.checkbox.CheckboxWithText;

public class ViewSharedClasses implements AutoCloseable {
    private final CalendarSet set;
    private final CheckboxWithText checkbox;

    public ViewSharedClasses(WorldCoords topLeft, CalendarSet set) {
        this.set = set;
        this.checkbox = new CheckboxWithText(topLeft, "View Shared Classes", ColorFactory.from255(0, 200, 0, 255));
    }

    @Override
    public void close() {
        this.checkbox.close();
    }
}

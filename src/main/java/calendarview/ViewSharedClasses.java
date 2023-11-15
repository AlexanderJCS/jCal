package calendarview;

import calendar.CalendarSet;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.io.mouse.MouseEvent;
import uihelper.checkbox.CheckboxWithText;

import java.util.List;

public class ViewSharedClasses implements AutoCloseable {
    private final CalendarSet set;
    private final CalendarSet sharedClassesCalSet;
    private final CheckboxWithText checkbox;

    public ViewSharedClasses(WorldCoords topLeft, CalendarSet set) {
        this.set = set;
        this.checkbox = new CheckboxWithText(topLeft, "Shared", ColorFactory.from255(0, 200, 0, 255));
        this.checkbox.setState(false);

        this.sharedClassesCalSet = new CalendarSet(set.getCanvas());
    }

    /**
     * @return The x and y dimensions of the checkbox. Does *not* include the dimensions of the text.
     */
    public WorldCoords getDimensions() {
        return this.checkbox.getDimensions();
    }

    public boolean isSelected() {
        return this.checkbox.isSelected();
    }

    public void draw() {
        this.checkbox.draw();

        if (this.checkbox.isSelected()) {
            this.sharedClassesCalSet.draw();
        }
    }

    public void update(List<MouseEvent> mouseEvents) {
        this.checkbox.update(mouseEvents);
    }

    @Override
    public void close() {
        this.checkbox.close();
    }
}

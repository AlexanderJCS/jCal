package selection;

import calendar.Calendar;
import calendar.CalendarSet;
import jangl.coords.WorldCoords;
import jangl.io.mouse.MouseEvent;

import java.util.List;

public class SelectionField implements AutoCloseable {
    /**
     * Key: the checkbox object<br>
     * Value: the corresponding Calendar object to enable/disable
     */
    private final CheckboxWithText[] checkboxes;
    private final Calendar[] calendars;
    private final CalendarSet calendarSet;

    public SelectionField(SelectionCanvas canvas, CalendarSet calendarSet) {
        this.calendarSet = calendarSet;

        this.calendars = calendarSet.getCalendars().toArray(new Calendar[0]);
        this.checkboxes = generateCheckboxes(canvas, this.calendars);
    }

    private static CheckboxWithText[] generateCheckboxes(SelectionCanvas canvas, Calendar[] selectionSet) {
        WorldCoords cursor = canvas.topLeft();
        CheckboxWithText[] checkboxes = new CheckboxWithText[selectionSet.length];

        for (int i = 0; i < checkboxes.length; i++) {
            checkboxes[i] = new CheckboxWithText(cursor, selectionSet[i].getTitle());

            cursor.y -= checkboxes[i].getDimensions().y * 1.2f;
        }

        return checkboxes;
    }

    public void draw() {
        for (CheckboxWithText checkbox : this.checkboxes) {
            checkbox.draw();
        }
    }

    public void update(List<MouseEvent> mouseEvents) {
        for (int i = 0; i < this.checkboxes.length; i++) {
            CheckboxWithText checkbox = this.checkboxes[i];
            checkbox.update(mouseEvents);

            if (checkbox.wasToggledLastUpdate()) {
                if (checkbox.isSelected()) {
                    this.calendarSet.addCalendar(this.calendars[i]);
                } else {
                    this.calendarSet.removeCalendar(this.calendars[i]);
                }
            }
        }

        this.refreshCalendarTitles();
    }

    private void refreshCalendarTitles() {
        // Update the calendar title in case it updated
        for (int i = 0; i < this.calendars.length; i++) {
            CheckboxWithText checkbox = this.checkboxes[i];
            Calendar calendar = this.calendars[i];

            if (!checkbox.getText().equals(calendar.getTitle())) {
                checkbox.setText(calendar.getTitle());
            }
        }
    }

    @Override
    public void close() {
        for (CheckboxWithText checkbox : this.checkboxes) {
            checkbox.close();
        }
    }
}

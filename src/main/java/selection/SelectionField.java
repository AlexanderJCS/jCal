package selection;

import calendar.Calendar;
import calendar.CalendarSet;
import jangl.coords.WorldCoords;
import jangl.io.mouse.MouseEvent;
import uihelper.checkbox.CheckboxWithText;

import java.util.List;

public class SelectionField implements AutoCloseable {
    /**
     * Key: the checkbox object<br>
     * Value: the corresponding Calendar object to enable/disable
     */
    private final CheckboxWithText[] checkboxes;
    private final Calendar[] calendars;
    private final CalendarSet calendarSet;

    public SelectionField(WorldCoords topLeft, CalendarSet calendarSet) {
        this.calendarSet = calendarSet;

        this.calendars = calendarSet.getCalendars().toArray(new Calendar[0]);
        this.checkboxes = generateCheckboxes(topLeft, this.calendars);
        this.syncCheckboxes();
    }

    private static CheckboxWithText[] generateCheckboxes(WorldCoords topLeft, Calendar[] selectionSet) {
        WorldCoords cursor = new WorldCoords(topLeft.x, topLeft.y);
        CheckboxWithText[] checkboxes = new CheckboxWithText[selectionSet.length];

        for (int i = 0; i < checkboxes.length; i++) {
            checkboxes[i] = new CheckboxWithText(cursor, selectionSet[i].getTitle(), selectionSet[i].getColor());

            // Only have the first 3 checkboxes on by default
            if (i >= 3) {
                checkboxes[i].setState(false);
            }

            cursor.y -= checkboxes[i].getDimensions().y * 1.2f;
        }

        return checkboxes;
    }

    private void syncCheckboxes() {
        for (int i = 0; i < this.checkboxes.length; i++) {
            CheckboxWithText checkbox = this.checkboxes[i];
            Calendar calendar = this.calendars[i];

            if (checkbox.isSelected() && !this.calendarSet.getCalendars().contains(calendar)) {
                this.calendarSet.addCalendar(calendar);
            }

            else if (!checkbox.isSelected()) {
                this.calendarSet.removeCalendar(calendar);
            }
        }
    }

    public void draw() {
        for (CheckboxWithText checkbox : this.checkboxes) {
            checkbox.draw();
        }
    }

    public void update(List<MouseEvent> mouseEvents) {
        for (CheckboxWithText checkbox : this.checkboxes) {
            checkbox.update(mouseEvents);
        }

        this.syncCheckboxes();
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

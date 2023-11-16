package calendarview;

import calendar.Calendar;
import calendar.CalendarEvent;
import calendar.CalendarSet;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.io.mouse.MouseEvent;
import uihelper.checkbox.CheckboxWithText;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewSharedEvents implements AutoCloseable {
    private final CalendarSet fullCalendarSet;
    private final CalendarSet sharedEventsCalSet;
    private final Calendar sharedEventsCal;
    private final CheckboxWithText checkbox;

    public ViewSharedEvents(WorldCoords topLeft, CalendarSet set) {
        this.fullCalendarSet = set;
        this.checkbox = new CheckboxWithText(topLeft, "Shared", ColorFactory.from255(0, 200, 0, 255));
        this.checkbox.setState(false);

        this.sharedEventsCalSet = new CalendarSet(set.getCanvas());

        this.sharedEventsCal = new Calendar(this.sharedEventsCalSet.getCanvas());
        this.sharedEventsCal.setColor(ColorFactory.from255(30, 180, 70, 255));
        this.sharedEventsCal.setCalendarTitle("Shared Events");

        this.sharedEventsCalSet.addCalendar(this.sharedEventsCal);
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

    private Set<CalendarEvent> getSharedEvents() {
        // Create a set with all the possible shared events
        Set<CalendarEvent> allCalendarEvents = new HashSet<>();

        for (Calendar calendar : this.fullCalendarSet.getCalendars()) {
            allCalendarEvents.addAll(calendar.getEvents());
        }

        // Create a set that only has events shared amongst all calendars
        Set<CalendarEvent> sharedCalendarEvents = new HashSet<>();
        for (CalendarEvent event : allCalendarEvents) {

            boolean sharedEvent = true;

            for (Calendar calendar : this.fullCalendarSet.getCalendars()) {
                if (!calendar.getEvents().contains(event)) {
                    sharedEvent = false;
                    break;
                }
            }

            if (sharedEvent) {
                sharedCalendarEvents.add(event);
            }
        }

        return sharedCalendarEvents;
    }

    private void updateSharedEventsCalendar() {
        Set<CalendarEvent> sharedEvents = this.getSharedEvents();

        List<CalendarEvent> oldSharedEvents = this.sharedEventsCal.getEvents();

        // Add events that may be missing
        for (CalendarEvent event : sharedEvents) {
            if (!oldSharedEvents.contains(event)) {
                this.sharedEventsCal.addEvent(event);
            }
        }

        // Remove events that are not shared
        for (CalendarEvent event : oldSharedEvents) {
            if (!sharedEvents.contains(event)) {
                this.sharedEventsCal.removeEvent(event);
            }
        }
    }

    public void draw() {
        this.checkbox.draw();

        if (this.checkbox.isSelected()) {
            for (Calendar calendar : this.fullCalendarSet.getCalendars()) {
                calendar.setCalendarNumber(1, 1);
            }

            this.sharedEventsCalSet.draw();
        }
    }

    public void update(List<MouseEvent> mouseEvents) {
        this.checkbox.update(mouseEvents);
        this.updateSharedEventsCalendar();
    }

    @Override
    public void close() {
        this.checkbox.close();
    }
}

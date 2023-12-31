package parser;

import calendar.Calendar;
import calendar.CalendarCanvas;
import calendar.CalendarEvent;
import calendar.WeekDay;
import jangl.color.ColorFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class JCalParser {
    public Calendar parse(CalendarCanvas canvas, String filepath) throws JCalParseException {
        Calendar calendar = new Calendar(canvas);
        HashMap<String, String> metadata = new HashMap<>();

        Scanner scanner;
        try {
            scanner = new Scanner(new File(filepath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        WeekDay currentDay = null;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().strip();

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("[") && line.endsWith("]")) {
                currentDay = this.parseDay(line);
            } else if (line.startsWith("#meta ")) {
                String[] keyValue = this.parseMeta(line);
                metadata.put(keyValue[0], keyValue[1]);

            } else if (currentDay == null) {
                throw new JCalParseException("Could not parse line: it is not associated with a week day\n" + line);
            } else {
                calendar.addEvent(this.parseEvent(line, currentDay, canvas));
            }
        }

        if (metadata.get("name") != null) {
            calendar.setCalendarTitle(metadata.get("name"));
        }

        if (metadata.get("color") != null) {
            String[] splitColor = metadata.get("color").split(",");

            // If the length is not 4 or 3
            if (!(splitColor.length == 4 || splitColor.length == 3)) {
                throw new JCalParseException("Could not parse the color:\n" + metadata.get("color"));
            }

            calendar.setColor(
                    ColorFactory.from255(
                            Integer.parseInt(splitColor[0]),
                            Integer.parseInt(splitColor[1]),
                            Integer.parseInt(splitColor[2]),
                            splitColor.length == 4 ? Integer.parseInt(splitColor[3]) : 255
                    )
            );
        }

        return calendar;
    }

    private WeekDay parseDay(String line) throws JCalParseException {
        line = line.substring(1, line.length() - 1);

        try {
            return WeekDay.valueOf(line);
        } catch (IllegalArgumentException e) {
            throw new JCalParseException("Could not find the value of day " + line + ". Is it a 3-letter abbreviation of a weekday?");
        }
    }

    private CalendarEvent parseEvent(String line, WeekDay day, CalendarCanvas canvas) throws JCalParseException {
        int eventNameStart = line.indexOf('"');
        int eventNameEnd = line.lastIndexOf('"');

        if (eventNameStart == eventNameEnd) {
            throw new JCalParseException("Could not find the end of the event name on line:\n" + line);
        }

        String eventName = line.substring(eventNameStart + 1, eventNameEnd);

        // Now that the event name is extracted, find the start and end times
        // This line should find isolate the times
        line = line.substring(0, eventNameStart).replace(" ", "");

        String[] times = line.split("-");

        if (times.length != 2) {
            throw new JCalParseException("Could not find the start and end time of an event");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

        LocalTime startTime = LocalTime.parse(times[0], formatter);
        LocalTime endTime = LocalTime.parse(times[1], formatter);

        return new CalendarEvent(eventName, canvas, day, startTime, endTime);
    }

    private String[] parseMeta(String line) throws JCalParseException {
        String originalLine = line;

        line = line.replace("#meta", "");
        line = line.strip();

        String[] keyValue = line.split("=");
        if (keyValue.length != 2) {
            throw new JCalParseException("Could not find key/value pair for meta line:\n" + originalLine);
        }

        keyValue[0] = keyValue[0].strip();
        keyValue[1] = keyValue[1].strip();

        return keyValue;
    }
}

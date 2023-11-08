package calendar;

public enum WeekDay {
    MON, TUES, WED, THURS, FRI;

    /**
     * @return The number of values in the enum
     */
    public static int numDays() {
        return WeekDay.values().length;
    }
}

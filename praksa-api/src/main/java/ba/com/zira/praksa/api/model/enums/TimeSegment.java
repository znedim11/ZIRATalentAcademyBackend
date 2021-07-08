package ba.com.zira.praksa.api.model.enums;

public enum TimeSegment {

    /** WEEK */
    WEEK("WEEK"),
    /** MONTH */
    MONTH("MONTH"),
    /** YEAR */
    YEAR("YEAR"),
    /** QUARTAL */
    QUARTAL("QUARTAL"),
    /** ALLTIME */
    ALLTIME("ALLTIME");

    private final String value;

    /**
     *
     * @param value
     */
    private TimeSegment(final String value) {
        this.value = value;
    }

    /**
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

}

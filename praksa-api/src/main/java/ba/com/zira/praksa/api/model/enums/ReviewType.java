package ba.com.zira.praksa.api.model.enums;

public enum ReviewType {
    /** INTERNAL */
    INTERNAL("INTERNAL"),
    /** EXTERNAL */
    EXTERNAL("EXTERNAL"),
    /** BOTH */
    BOTH("BOTH");

    private final String value;

    /**
     *
     * @param value
     */

    private ReviewType(final String value) {
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

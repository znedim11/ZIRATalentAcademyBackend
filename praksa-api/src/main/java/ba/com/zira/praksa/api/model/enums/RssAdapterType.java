package ba.com.zira.praksa.api.model.enums;

public enum RssAdapterType {
    /** GIANTBOMB */
    GIANTBOMB("GB"),
    /** POLYGON */
    POLYGON("POLY"),
    /** IGN */
    IGN("IGN"),
    /** PCGAMER */
    PCGAMER("PCG");

    private final String value;

    /**
     *
     * @param value
     */

    private RssAdapterType(final String value) {
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

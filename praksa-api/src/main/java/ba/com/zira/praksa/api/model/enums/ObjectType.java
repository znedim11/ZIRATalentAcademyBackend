package ba.com.zira.praksa.api.model.enums;

public enum ObjectType {
    /** GAME */
    GAME("GAME"),
    /** PERSON */
    PERSON("PERSON"),
    /** OBJECT */
    OBJECT("OBJECT"),
    /** LOCATION */
    LOCATION("LOCATION"),
    /** CHARACTER */
    CHARACTER("CHARACTER"),
    /** CONCEPT */
    CONCEPT("CONCEPT"),
    /** COMPANY */
    COMPANY("COMPANY"),
    /** PLATFORM */
    PLATFORM("PLATFORM"),
    /** REVIEW */
    REVIEW("REVIEW");

    private final String value;

    ObjectType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

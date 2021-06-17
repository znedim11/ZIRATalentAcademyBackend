package ba.com.zira.praksa.api.model.enums;

public enum ReleaseType
{
	/** GAME */
	Game("GAME"),
	/** PLATFORM */
	Platform("PLATFORM");

	private final String value;

	/**
	 *
	 * @param value
	 */

	private ReleaseType(final String value)
	{
		this.value = value;
	}

	/**
	 *
	 * @return the value
	 */

	public String getValue()
	{
		return value;
	}
}
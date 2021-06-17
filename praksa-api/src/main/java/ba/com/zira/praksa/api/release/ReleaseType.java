package ba.com.zira.praksa.api.release;

public enum ReleaseType
{
	Game("GAME"), Platform("PLATFORM");

	private String releaseType;

	private ReleaseType(String releaseType)
	{
		this.releaseType = releaseType;
	}

	@Override
	public String toString()
	{
		return releaseType;
	}
}

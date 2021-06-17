package ba.com.zira.praksa.api.release;

public enum ReleaseType
{
	Game("Game"), Platform("Platform");

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

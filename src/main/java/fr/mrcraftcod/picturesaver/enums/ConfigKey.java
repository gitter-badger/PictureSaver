package fr.mrcraftcod.picturesaver.enums;

public class ConfigKey<T>
{
	public static final ConfigKey<String> EMAIL_FETCH = new ConfigKey<>("EmailFetch");
	public static final ConfigKey<String> EMAIL_FETCH_PASSWORD = new ConfigKey<>("EmailFetchPassword");

	private final String ID;

	public ConfigKey(String ID)
	{
		this.ID = ID;
	}

	public String getID()
	{
		return this.ID;
	}
}

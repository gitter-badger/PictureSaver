package fr.mrcraftcod.picturesaver.enums;

import java.io.File;
import java.util.ArrayList;
public class ConfigKey<T>
{
	private static final ArrayList<ConfigKey> configKeys = new ArrayList<>();

	public static final ConfigKey<String> EMAIL_FETCH_MAIL = new ConfigKey<>("EmailFetch");
	public static final ConfigKey<String> EMAIL_FETCH_PASSWORD = new ConfigKey<>("EmailFetchPassword");
	public static final ConfigKey<Boolean> EMAIL_FETCH_STATUS = new ConfigKey<>("EmailFetchStatus", false);

	private final String ID;
	private final T defaultValue;

	public ConfigKey(String ID)
	{
		this(ID, null);
	}

	public ConfigKey(String ID, T defaultValue)
	{
		this.ID = ID;
		this.defaultValue = defaultValue;
		configKeys.add(this);
	}

	public String getID()
	{
		return this.ID;
	}

	public T getDefault()
	{
		return this.defaultValue;
	}

	public boolean hasDefault()
	{
		return defaultValue != null;
	}

	public static ArrayList<ConfigKey> getAll()
	{
		return configKeys;
	}

	public String getWritableValue(T value)
	{
		if(value instanceof File)
			return ((File) value).getAbsolutePath();
		return value.toString();
	}
}

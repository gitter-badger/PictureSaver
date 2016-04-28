package fr.mrcraftcod.picturesaver.enums;

import fr.mrcraftcod.picturesaver.objects.configkeys.BooleanConfigKey;
import fr.mrcraftcod.picturesaver.objects.configkeys.StringConfigKey;
import java.io.File;
import java.util.ArrayList;

public abstract class ConfigKey<T>
{
	private static final ArrayList<ConfigKey> configKeys = new ArrayList<>();

	public static final StringConfigKey EMAIL_FETCH_MAIL = new StringConfigKey("EmailFetch");
	public static final StringConfigKey EMAIL_FETCH_PASSWORD = new StringConfigKey("EmailFetchPassword");
	public static final BooleanConfigKey EMAIL_FETCH_STATUS = new BooleanConfigKey("EmailFetchStatus", false);

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

	public abstract T parseValue(String value);

	public static ConfigKey getWithID(String ID)
	{
		for(ConfigKey configKey : getAll())
			if(configKey.is(ID))
				return configKey;
		return null;
	}

	public boolean is(String id)
	{
		return this.getID().equals(id);
	}
}

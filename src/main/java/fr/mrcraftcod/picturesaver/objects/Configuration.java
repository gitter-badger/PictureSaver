package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.enums.ConfigKeys;
import fr.mrcraftcod.utils.config.SQLiteManager;
import org.jdeferred.Promise;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Consumer;

public class Configuration extends SQLiteManager
{
	private final ConfigList configValues;
	private final String TABLE_DB_FILE = "Config";
	private final String KEY_LABEL = "Name";
	public static final String VALUE_LABEL = "Content";

	public Configuration(File databaseURL, boolean log) throws ClassNotFoundException, InterruptedException
	{
		super(databaseURL, log);
		this.configValues = new ConfigList();
		this.sendUpdateRequest("CREATE TABLE IF NOT EXISTS " + TABLE_DB_FILE + "(" + KEY_LABEL + " varchar(75), " + VALUE_LABEL + " blob, PRIMARY KEY (" + KEY_LABEL + "));").waitSafely();
	}

	public void pullAllValues() throws InterruptedException
	{
		this.sendQueryRequest("SELECT * FROM " + TABLE_DB_FILE + ";").done(resultSet -> {
			try
			{
				while(resultSet.next())
				{
					ConfigKeys key = ConfigKeys.getWithID(resultSet.getString(KEY_LABEL));
					configValues.add(new ConfigValue(key, key.parseValue(resultSet.getString(VALUE_LABEL))));
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}).waitSafely();
	}

	public Promise<Integer, Throwable, Void> setValues(Collection<fr.mrcraftcod.picturesaver.objects.ConfigValue> values)
	{
		StringBuilder stringBuilder = new StringBuilder("INSERT OR REPLACE INTO ").append(TABLE_DB_FILE).append("(").append(KEY_LABEL).append(",").append(VALUE_LABEL).append(") VALUES");
		for(fr.mrcraftcod.picturesaver.objects.ConfigValue value : values)
			stringBuilder.append("(\"").append(value.getKey().getID()).append("\",\"").append(value.getWritableValue()).append("\"),");
		stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), ";");
		return this.sendUpdateRequest(stringBuilder.toString());
	}

	public void getValues(Collection<ConfigKeys> keys, Consumer<ConfigList> callback, Consumer<ConfigList> errorCallback)
	{
		ConfigList values = new ConfigList();
		for(ConfigKeys key : keys)
			values.add(getValue(key));
		if(keys.size() == values.size())
			callback.accept(values);
		else
			errorCallback.accept(values);
	}

	public <T> ConfigValue getValue(ConfigKeys<T> configKey)
	{
		for(fr.mrcraftcod.picturesaver.objects.ConfigValue value : this.configValues)
			if(value.getKey().is(configKey))
				return value;
		ConfigValue<T> configValue = new ConfigValue(configKey);
		configValues.add(configValue);
		return configValue;
	}

	@Override
	public void close()
	{
		try
		{
			setValues(this.configValues).waitSafely();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		super.close();
	}

	public <T> Promise<ResultSet, Throwable, Void> pullValue(ConfigKeys<T> key)
	{
		return this.sendQueryRequest("SELECT * FROM " + TABLE_DB_FILE + " WHERE " + KEY_LABEL + " = \"" + key.getID() + "\";");
	}
}

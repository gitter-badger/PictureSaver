package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import fr.mrcraftcod.utils.config.SQLiteManager;
import org.jdeferred.Promise;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class Configuration extends SQLiteManager
{
	private final ArrayList<ConfigValue> configValues;
	private final String TABLE_DB_FILE = "Config";
	private final String KEY_LABEL = "Name";
	private final String VALUE_LABEL = "Content";

	public Configuration(File databaseURL, boolean log) throws ClassNotFoundException, InterruptedException
	{
		super(databaseURL, log);
		this.configValues = new ArrayList<>();
		this.sendUpdateRequest("CREATE TABLE IF NOT EXISTS " + TABLE_DB_FILE + "(" + KEY_LABEL + " varchar(75), " + VALUE_LABEL + " blob, PRIMARY KEY (" + KEY_LABEL + "));").waitSafely();
		this.pullAllValues();
	}

	private void pullAllValues() throws InterruptedException
	{
		this.sendQueryRequest("SELECT * FROM " + TABLE_DB_FILE + ";").done(resultSet -> {
			try
			{
				while(resultSet.next())
				{
					ConfigKey key = ConfigKey.getWithID(resultSet.getString(KEY_LABEL));
					configValues.add(new ConfigValue(key, key.parseValue(resultSet.getString(VALUE_LABEL))));
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}).waitSafely();
	}

	public Promise<Integer, Throwable, Void> setValues(Collection<ConfigValue> values)
	{
		StringBuilder stringBuilder = new StringBuilder("INSERT OR REPLACE INTO ").append(TABLE_DB_FILE).append("(").append(KEY_LABEL).append(",").append(VALUE_LABEL).append(") VALUES");
		for(ConfigValue value : values)
			stringBuilder.append("(\"").append(value.getKey().getID()).append("\",\"").append(value.getWritableValue()).append("\"),");
		stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), ";");
		return this.sendUpdateRequest(stringBuilder.toString());
	}

	public void getValues(Collection<ConfigKey> keys, Consumer<ArrayList<ConfigValue>> callback, Consumer<Collection<ConfigValue>> errorCallback)
	{
		ArrayList<ConfigValue> values = new ArrayList<>();
		for(ConfigKey key : keys)
			values.add(getValue(key));
		if(keys.size() == values.size())
			callback.accept(values);
		else
			errorCallback.accept(values);
	}

	public <T> ConfigValue<T> getValue(ConfigKey<T> configKey)
	{
		for(ConfigValue value : this.configValues)
			if(value.getKey().is(configKey))
				return value;
		return null;
	}

	@Override
	public void close()
	{
		syncValues();
		super.close();
	}

	private void syncValues()
	{

	}
}

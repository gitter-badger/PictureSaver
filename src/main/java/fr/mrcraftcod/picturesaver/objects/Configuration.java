package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import fr.mrcraftcod.utils.config.SQLiteManager;
import org.jdeferred.Promise;
import java.io.File;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public class Configuration extends SQLiteManager
{
	private final String TABLE_DB_FILE = "Config";
	private final String KEY_LABEL = "Name";
	private final String VALUE_LABEL = "Content";

	public Configuration(File databaseURL, boolean log) throws ClassNotFoundException, InterruptedException
	{
		super(databaseURL, log);
		this.sendUpdateRequest("CREATE TABLE IF NOT EXISTS " + TABLE_DB_FILE + "(" + KEY_LABEL + " varchar(75), " + VALUE_LABEL + " blob, PRIMARY KEY (" + KEY_LABEL + "));").waitSafely();
	}

	public <T> Promise<Integer, Throwable, Void> setValue(ConfigKey<T> configKey, T value)
	{
		return this.sendUpdateRequest("INSERT OR REPLACE INTO " + TABLE_DB_FILE + "(" + KEY_LABEL + "," + VALUE_LABEL + ") VALUES(\"" + configKey.getID() + "\", \"" + value.toString() + "\");");
	}

	public <T> void getValue(ConfigKey<T> configKey, Function<Object, T> parser, Consumer<T> callback, Consumer<ConfigKey<T>> errorCallback)
	{
		try
		{
			this.sendQueryRequest("SELECT * FROM " + TABLE_DB_FILE + " WHERE " + KEY_LABEL + " = \"" + configKey.getID() + "\";").done(result -> {
				try
				{
					if(result.next())
						callback.accept(parser.apply(result.getObject(VALUE_LABEL)));
					else
						if(errorCallback != null)
							errorCallback.accept(configKey);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
			}).fail(err -> errorCallback.accept(configKey)).waitSafely();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void getIntegerValue(ConfigKey<Integer> configKey, Consumer<Integer> callback, Consumer<ConfigKey<Integer>> errorCallBack)
	{
		getValue(configKey, o -> Integer.parseInt(o.toString()), callback, errorCallBack);
	}

	public void getStringValue(ConfigKey<String> configKey, Consumer<String> callback, Consumer<ConfigKey<String>> errorCallback)
	{
		getValue(configKey, o -> new String((byte[])o), callback, errorCallback);
	}
}

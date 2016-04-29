package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.enums.ConfigKeys;
import javafx.beans.property.SimpleObjectProperty;
import java.io.File;
import java.sql.SQLException;

public class ConfigValue<T>
{
	private final ConfigKeys<T> key;
	private SimpleObjectProperty<T> value;
	private long lastUpdated;

	public ConfigValue(ConfigKeys<T> key)
	{
		this(key, key.getDefault());
		this.lastUpdated = System.currentTimeMillis();
	}

	public ConfigValue(ConfigKeys<T> key, T value)
	{
		this.key = key;
		this.value = new SimpleObjectProperty<>(value);
		this.lastUpdated = 0;
	}

	public void setValue(T value)
	{
		this.valueProperty().set(value);
		this.lastUpdated = System.currentTimeMillis();
	}

	private SimpleObjectProperty<T> valueProperty()
	{
		return this.value;
	}

	public ConfigKeys<T> getKey()
	{
		return this.key;
	}

	public String getWritableValue()
	{
		checkValue();
		return this.getKey().getWritableValue(this.valueProperty().get());
	}

	public boolean getBooleanValue()
	{
		checkValue();
		return (Boolean)this.valueProperty().get();
	}

	public String getStringValue()
	{
		checkValue();
		return this.valueProperty().get().toString();
	}

	public File getFileValue()
	{
		checkValue();
		return (File)this.valueProperty().get();
	}

	private void checkValue()
	{
		if(this.valueProperty().isNull().get())
		{
			try
			{
				Constants.configuration.pullValue(this.getKey()).done(resultSet -> {
					try
					{
						if(resultSet.next())
							this.valueProperty().set(this.getKey().parseValue(resultSet.getString(Configuration.VALUE_LABEL)));
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
				}).waitSafely();
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}

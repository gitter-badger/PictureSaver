package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.enums.ConfigKeys;
import javafx.beans.property.SimpleObjectProperty;
import java.io.File;

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
		return this.getKey().getWritableValue(this.valueProperty().get());
	}

	public boolean getBooleanValue()
	{
		return (Boolean)this.valueProperty().get();
	}

	public String getStringValue()
	{
		return this.valueProperty().get().toString();
	}

	public File getFileValue()
	{
		return (File)this.valueProperty().get();
	}
}

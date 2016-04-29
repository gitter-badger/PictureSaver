package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import javafx.beans.property.SimpleObjectProperty;

public class ConfigValue<T>
{
	private final ConfigKey<T> key;
	private SimpleObjectProperty<T> value;
	private long lastUpdated;

	public ConfigValue(ConfigKey<T> key)
	{
		this(key, key.getDefault());
		this.lastUpdated = System.currentTimeMillis();
	}

	public ConfigValue(ConfigKey<T> key, T value)
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

	public ConfigKey<T> getKey()
	{
		return this.key;
	}

	public String getWritableValue()
	{
		return this.getKey().getWritableValue(this.valueProperty().get());
	}
}

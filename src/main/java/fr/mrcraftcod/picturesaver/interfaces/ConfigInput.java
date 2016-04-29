package fr.mrcraftcod.picturesaver.interfaces;

import fr.mrcraftcod.picturesaver.objects.ConfigValue;

public interface ConfigInput<T>
{
	public ConfigValue<T> getValue();
}

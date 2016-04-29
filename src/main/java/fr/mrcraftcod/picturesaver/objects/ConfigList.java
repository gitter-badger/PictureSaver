package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.enums.ConfigKeys;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ConfigList extends ArrayList<fr.mrcraftcod.picturesaver.objects.ConfigValue>
{
	public fr.mrcraftcod.picturesaver.objects.ConfigValue get(ConfigKeys key)
	{
		for(fr.mrcraftcod.picturesaver.objects.ConfigValue value : this)
			if(value.getKey().is(key))
				return value;
		return null;
	}

	public boolean containsKeys(Collection<ConfigKeys> keys)
	{
		for(ConfigKeys key : keys)
			if(this.get(key) == null)
				return false;
		return true;
	}

	public void remove(ConfigKeys key)
	{
		Iterator<ConfigValue> it = this.iterator();
		while(it.hasNext())
		{
			ConfigValue value = it.next();
			if(value.getKey().is(key))
				it.remove();
		}
	}

	@Override
	public boolean add(ConfigValue configValue)
	{
		if(configValue == null)
			return false;
		if(this.contains(configValue.getKey()))
			this.remove(configValue.getKey());
		return super.add(configValue);
	}
}

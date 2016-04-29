package fr.mrcraftcod.picturesaver.objects.configkeys;

import fr.mrcraftcod.picturesaver.enums.ConfigKeys;

public class BooleanConfigKey extends ConfigKeys<Boolean>
{
	public BooleanConfigKey(String ID)
	{
		super(ID);
	}

	@Override
	public Boolean parseValue(String value)
	{
		return Boolean.valueOf(value);
	}

	public BooleanConfigKey(String ID, boolean defaultValue)
	{
		super(ID, defaultValue);
	}
}

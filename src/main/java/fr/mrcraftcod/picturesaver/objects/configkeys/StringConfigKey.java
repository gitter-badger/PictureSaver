package fr.mrcraftcod.picturesaver.objects.configkeys;

import fr.mrcraftcod.picturesaver.enums.ConfigKey;

public class StringConfigKey extends ConfigKey<String>
{
	public StringConfigKey(String ID)
	{
		super(ID);
	}

	@Override
	public String parseValue(String value)
	{
		return value;
	}
}

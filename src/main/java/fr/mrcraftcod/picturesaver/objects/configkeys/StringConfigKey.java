package fr.mrcraftcod.picturesaver.objects.configkeys;

import fr.mrcraftcod.picturesaver.enums.ConfigKeys;

public class StringConfigKey extends ConfigKeys<String>
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

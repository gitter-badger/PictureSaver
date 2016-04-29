package fr.mrcraftcod.picturesaver.objects.configkeys;

import fr.mrcraftcod.picturesaver.enums.ConfigKeys;
import java.io.File;

public class FileConfigKey extends ConfigKeys<File>
{
	public FileConfigKey(String ID)
	{
		super(ID);
	}

	@Override
	public File parseValue(String value)
	{
		return new File(value);
	}
}

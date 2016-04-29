package fr.mrcraftcod.picturesaver;

import fr.mrcraftcod.picturesaver.objects.Configuration;
import fr.mrcraftcod.utils.FileUtils;
import fr.mrcraftcod.utils.Log;
import fr.mrcraftcod.utils.resources.ResourcesBase;
import java.io.File;

public class Constants
{
	public static final String APP_NAME = "PictureSaver";
	public static final String VERSION = "0.0.1";
	public static final ResourcesBase resources = new ResourcesBase(Constants.class);
	public static Configuration configuration;

	static
	{
		Log.setAppName(APP_NAME);
		try
		{
			configuration = new Configuration(new File(FileUtils.getAppDataFolder() + File.separator + "PictureSaver", "config.db"), true);
		}
		catch(Exception e)
		{
			Log.error("Failed to create configuration object!", e);
		}
	}
}

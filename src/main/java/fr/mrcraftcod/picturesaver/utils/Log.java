package fr.mrcraftcod.picturesaver.utils;

import fr.mrcraftcod.picturesaver.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Log
{
	private static final Logger logger = Logger.getLogger(Constants.APP_NAME);

	public static void warning(String s, Throwable e)
	{
		logger.log(Level.WARNING, s, e);
	}

	public static void info(String s)
	{
		logger.log(Level.INFO, s);
	}
}

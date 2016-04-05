package fr.mrcraftcod.picturesaver.enums;

import fr.mrcraftcod.picturesaver.Constants;
import java.net.URL;

public enum Resources
{
	ICONS("icons");

	private final String root;

	Resources(String rootPath)
	{
		this.root = rootPath;
	}

	public URL getResourceURL(String path)
	{
		return Constants.class.getResource("/" + this.root + "/" + path);
	}
}

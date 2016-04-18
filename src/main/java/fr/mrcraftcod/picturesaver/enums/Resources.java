package fr.mrcraftcod.picturesaver.enums;

import fr.mrcraftcod.utils.resources.ResourceElement;

public enum Resources implements ResourceElement
{
	ICONS("icons");

	private final String root;

	Resources(String rootPath)
	{
		this.root = rootPath;
	}

	@Override
	public String getRootPath()
	{
		return this.root;
	}
}

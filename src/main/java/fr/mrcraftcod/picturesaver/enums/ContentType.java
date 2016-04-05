package fr.mrcraftcod.picturesaver.enums;

import java.util.regex.Pattern;
public enum ContentType
{
	NULL(null),
	JPG("jpg"),
	PNG("png");

	private String extension;

	ContentType(String extension)
	{
		this.extension = extension;
	}

	public static boolean isAllowed(String string)
	{
		return getContentType(string) != NULL;
	}

	private static ContentType getContentType(String string)
	{
		for(ContentType contentType : ContentType.values())
			if(contentType.match(string))
				return contentType;
		return NULL;
	}

	private boolean match(String string)
	{
		return string != null && !string.equals("") && Pattern.compile(".+\\." + extension + "(.+)?").matcher(string).matches();
	}
}

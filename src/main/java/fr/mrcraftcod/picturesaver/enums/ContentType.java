package fr.mrcraftcod.picturesaver.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public enum ContentType
{
	NULL(null),
	JPG("jpg"),
	PNG("png"),
	WEBM("webm");

	private final String extension;
	private final Pattern pattern;

	ContentType(String extension)
	{
		this.extension = extension;
		this.pattern = Pattern.compile(".+(\\." + extension + ")(.+)?");
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
		return string != null && !string.equals("") && pattern.matcher(string).matches();
	}

	public static int getExtensionIndex(String string)
	{
		return getContentType(string).findIndex(string);
	}

	public static int getExtensionEndIndex(String string)
	{
		return getContentType(string).findEndIndex(string);
	}

	public int findIndex(String string)
	{
		if(this == NULL)
			return -1;
		Matcher matcher = pattern.matcher(string);
		matcher.matches();
		return matcher.start(1);
	}

	public int findEndIndex(String string)
	{
		if(this == NULL)
			return -1;
		Matcher matcher = pattern.matcher(string);
		matcher.matches();
		return matcher.end(1);
	}
}

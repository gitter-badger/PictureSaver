package fr.mrcraftcod.picturesaver.enums;

import fr.mrcraftcod.picturesaver.interfaces.LinkFetcher;
import fr.mrcraftcod.picturesaver.objects.linkfetchers.NormalLinkFetcher;
import fr.mrcraftcod.utils.Log;
import java.net.URL;
import java.util.regex.Pattern;

public enum Origins
{
	NORMAL(".+", NormalLinkFetcher.class);

	private final Pattern pattern;
	private final LinkFetcher linkFetcher;

	Origins(String pattern, Class<? extends LinkFetcher> linkFetcher)
	{
		this.pattern = Pattern.compile(pattern);
		LinkFetcher fetcher = null;
		try
		{
			fetcher = linkFetcher.newInstance();
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			Log.error("Couldn't instantiate LinkFetcher for origin " + this, e);
		}
		this.linkFetcher = fetcher;
	}

	public static Origins getDefault()
	{
		return NORMAL;
	}

	public LinkFetcher getFetcher()
	{
		return this.linkFetcher;
	}

	public static Origins getOrigin(URL url)
	{
		for(Origins origins : Origins.values())
			if(origins != Origins.getDefault())
				if(origins.isOriginOf(url))
					return origins;
		return Origins.getDefault();
	}

	private boolean isOriginOf(URL url)
	{
		return this.getPattern().matcher(url.toString()).matches();
	}

	public Pattern getPattern()
	{
		return this.pattern;
	}
}

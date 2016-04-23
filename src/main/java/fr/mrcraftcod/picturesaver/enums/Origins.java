package fr.mrcraftcod.picturesaver.enums;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.interfaces.LinkFetcher;
import fr.mrcraftcod.picturesaver.objects.linkfetchers.DeviantartLinkFetcher;
import fr.mrcraftcod.picturesaver.objects.linkfetchers.NormalLinkFetcher;
import fr.mrcraftcod.utils.FileUtils;
import fr.mrcraftcod.utils.Log;
import javafx.beans.property.SimpleObjectProperty;
import java.io.File;
import java.net.URL;
import java.util.regex.Pattern;

public enum Origins
{
	NORMAL(".+", NormalLinkFetcher.class),
	DEVIANTART(".+deviantart\\.com/art/.+", DeviantartLinkFetcher.class);

	private final Pattern pattern;
	private final LinkFetcher linkFetcher;
	private final SimpleObjectProperty<File> outputFolder;
	private final ConfigKey<File> outputFileKey;

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
		this.outputFileKey = new ConfigKey<File>("OutputFolder" + this.toString());
		this.outputFolder = new SimpleObjectProperty<>(buildOutputFolder());
	}

	private File buildOutputFolder()
	{
		Constants.configuration.getFileValue(this.outputFileKey, file -> this.outputFolderProperty().set(file), null);
		return new File(FileUtils.getDesktopFolder(), "/" + this.toString() + "/");
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

	public File getFolder()
	{
		return this.outputFolderProperty().get();
	}

	public SimpleObjectProperty<File> outputFolderProperty()
	{
		return this.outputFolder;
	}
}

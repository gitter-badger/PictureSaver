package fr.mrcraftcod.picturesaver.enums;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.interfaces.LinkFetcher;
import fr.mrcraftcod.picturesaver.objects.configkeys.BooleanConfigKey;
import fr.mrcraftcod.picturesaver.objects.configkeys.FileConfigKey;
import fr.mrcraftcod.picturesaver.objects.linkfetchers.DeviantartLinkFetcher;
import fr.mrcraftcod.picturesaver.objects.linkfetchers.NormalLinkFetcher;
import fr.mrcraftcod.utils.FileUtils;
import fr.mrcraftcod.utils.Log;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

public enum Origins
{
	NORMAL(".+", NormalLinkFetcher.class),
	DEVIANTART(".+deviantart\\.com/art/.+", DeviantartLinkFetcher.class);

	private final Pattern pattern;
	private final LinkFetcher linkFetcher;
	private final SimpleObjectProperty<File> outputFolder;
	private final ConfigKeys<File> outputFileKey;
	private final SimpleBooleanProperty activated;
	private final BooleanConfigKey activatedKey;

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
		this.outputFileKey = new FileConfigKey("OutputFolder" + this.name());
		this.activatedKey = new BooleanConfigKey("OriginStatus" + this.name(), true);
		this.outputFolder = new SimpleObjectProperty<>(new File(FileUtils.getDesktopFolder(), "/" + this.name() + "/"));
		this.activated = new SimpleBooleanProperty(true);
		initPropertiesConfig();
	}

	private void initPropertiesConfig()
	{
		ArrayList<ConfigKeys> keys = new ArrayList<>();
		keys.add(getOutputFolderKey());
		keys.add(getActivatedKey());
		Constants.configuration.getValues(keys, results -> {
			this.outputFolderProperty().set(results.get(getOutputFolderKey()).getFileValue());
			this.activatedProperty().set(results.get(getActivatedKey()).getBooleanValue());
		}, null);
	}

	public SimpleBooleanProperty activatedProperty()
	{
		return this.activated;
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

	public ConfigKeys<File> getOutputFolderKey()
	{
		return this.outputFileKey;
	}

	public ConfigKeys<Boolean> getActivatedKey()
	{
		return this.activatedKey;
	}

	public fr.mrcraftcod.picturesaver.objects.ConfigValue getOutputFolderConfigValue()
	{
		return Constants.configuration.getValue(getOutputFolderKey());
	}

	public fr.mrcraftcod.picturesaver.objects.ConfigValue getActivatedConfigValue()
	{
		return Constants.configuration.getValue(getActivatedKey());
	}
}

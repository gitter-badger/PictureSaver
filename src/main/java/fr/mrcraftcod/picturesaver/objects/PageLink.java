package fr.mrcraftcod.picturesaver.objects;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.mrcraftcod.picturesaver.utils.Log;
import fr.mrcraftcod.utils.StringUtils;
import fr.mrcraftcod.utils.http.URLHandler;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.net.URISyntaxException;
import java.net.URL;
public class PageLink
{
	private final Page parentPage;
	private final SimpleObjectProperty<URL> url;
	private final SimpleLongProperty byteSize;

	public PageLink(Page parentPage, URL url)
	{
		this.parentPage = parentPage;
		this.url = new SimpleObjectProperty<>(url);
		this.byteSize = new SimpleLongProperty(-1);
	}

	@Override
	public String toString()
	{
		return this.getURL().toString() + " (" + getByteSize() + ")";
	}

	public URL getURL()
	{
		return this.url.get();
	}

	public void fetch()
	{
		try
		{
			this.byteSize.set(URLHandler.getConnectionLinkLength(getURL()));
		}
		catch(UnirestException | URISyntaxException e)
		{
			Log.warning("Error fetching link size!", e);
		}
	}

	public String getByteSizeString()
	{
		return StringUtils.getDownloadSizeText(getByteSize());
	}

	public long getByteSize()
	{
		return this.byteSize.get();
	}
}

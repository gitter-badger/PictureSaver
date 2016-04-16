package fr.mrcraftcod.picturesaver.objects;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.mrcraftcod.picturesaver.enums.ContentType;
import fr.mrcraftcod.picturesaver.jfx.components.table.ProgressBarMax;
import fr.mrcraftcod.picturesaver.utils.Log;
import fr.mrcraftcod.utils.FileUtils;
import fr.mrcraftcod.utils.StringUtils;
import fr.mrcraftcod.utils.http.URLHandler;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class PageLink
{
	private final Page parentPage;
	private final SimpleObjectProperty<URL> url;
	private final SimpleLongProperty byteSize;
	private final SimpleLongProperty downloadedBytes;
	private final SimpleObjectProperty<ProgressBarMax> downloadProgressBar;

	public PageLink(Page parentPage, URL url)
	{
		this.parentPage = parentPage;
		this.url = new SimpleObjectProperty<>(url);
		this.byteSize = new SimpleLongProperty(-1);
		this.downloadedBytes = new SimpleLongProperty(-1);
		this.downloadProgressBar = new SimpleObjectProperty<>(new ProgressBarMax());
	}

	public SimpleLongProperty byteSizeProperty()
	{
		return this.byteSize;
	}

	public SimpleObjectProperty<URL> urlProperty()
	{
		return this.url;
	}

	public SimpleObjectProperty<URL> sourceURLProperty()
	{
		return this.parentPage.urlProperty();
	}

	public SimpleObjectProperty<ProgressBarMax> downloadProgressBarProperty()
	{
		return this.downloadProgressBar;
	}

	public SimpleLongProperty downloadedBytesProperty()
	{
		return this.downloadedBytes;
	}

	@Override
	public String toString()
	{
		return this.getUrl().toString() + " (" + getByteSize() + ")";
	}

	public URL getUrl()
	{
		return this.urlProperty().get();
	}

	public boolean fetch()
	{
		try
		{
			this.setByteSize(URLHandler.getConnectionLinkLength(getUrl()));
			this.downloadProgressBarProperty().get().setMax(this.getByteSize());
			return true;
		}
		catch(UnirestException | URISyntaxException e)
		{
			Log.warning("Error fetching link size!", e);
		}
		return false;
	}

	private void setByteSize(long value)
	{
		this.byteSize.set(value);
	}

	public String getByteSizeString()
	{
		return StringUtils.getDownloadSizeText(getByteSize());
	}

	public long getByteSize()
	{
		return this.byteSizeProperty().get();
	}

	public void setDownloadedBytes(long downloadedBytes)
	{
		this.downloadedBytes.set(downloadedBytes);
		this.downloadProgressBarProperty().get().setValue(downloadedBytes);
	}

	public File getOutputFile()
	{
		File file = new File(this.parentPage.getOutputFile(), FileUtils.sanitizeFileName(this.getFileName()));
		FileUtils.createDirectories(file);
		return file;
	}

	public String getFileName()
	{
		String url = this.url.toString();
		return url.substring(url.lastIndexOf("/") + 1, ContentType.getExtensionEndIndex(url));
	}
}

package fr.mrcraftcod.picturesaver.objects;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.mrcraftcod.picturesaver.enums.ContentType;
import fr.mrcraftcod.picturesaver.enums.LinkStatus;
import fr.mrcraftcod.picturesaver.jfx.components.table.ProgressBarMax;
import fr.mrcraftcod.utils.FileUtils;
import fr.mrcraftcod.utils.Log;
import fr.mrcraftcod.utils.StringUtils;
import fr.mrcraftcod.utils.http.URLHandler;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import static fr.mrcraftcod.picturesaver.enums.LinkStatus.WAITING_DOWNLOAD;

public class PageLink
{
	private final Page parentPage;
	private final SimpleObjectProperty<URL> url;
	private final SimpleLongProperty byteSize;
	private final SimpleLongProperty downloadedBytes;
	private final SimpleObjectProperty<ProgressBarMax> downloadProgressBar;
	private final SimpleObjectProperty<LinkStatus> linkStatus;
	private final SimpleObjectProperty<File> outputFile;

	public PageLink(Page parentPage, URL url)
	{
		this.parentPage = parentPage;
		this.url = new SimpleObjectProperty<>(url);
		this.byteSize = new SimpleLongProperty(-1);
		this.downloadedBytes = new SimpleLongProperty(-1);
		this.linkStatus = new SimpleObjectProperty<>(WAITING_DOWNLOAD);
		this.outputFile = new SimpleObjectProperty<>(getOutputFile());
		this.downloadProgressBar = new SimpleObjectProperty<>(new ProgressBarMax(this.downloadedBytesProperty(), this.byteSizeProperty()));
		this.downloadProgressBarProperty().get().setMaxWidth(Double.MAX_VALUE);
	}

	public SimpleObjectProperty<File> outputFileProperty()
	{
		return this.outputFile;
	}

	public SimpleObjectProperty<LinkStatus> linkStatusProperty()
	{
		return this.linkStatus;
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
	}

	public File getOutputFile()
	{
		if(this.outputFileProperty() != null)
			return this.outputFileProperty().get();
		File file = new File(this.parentPage.getOutputFile(), FileUtils.sanitizeFileName(this.getFileName()));
		FileUtils.createDirectories(file);
		return file;
	}

	public String getFileName()
	{
		String url = this.url.toString();
		return url.substring(url.lastIndexOf("/") + 1, ContentType.getExtensionEndIndex(url));
	}

	public void setStatus(LinkStatus status)
	{
		this.linkStatus.set(status);
	}

	public LinkStatus getStatus()
	{
		return this.linkStatusProperty().get();
	}
}

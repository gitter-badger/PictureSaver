package fr.mrcraftcod.picturesaver.jfx.components.table;

import fr.mrcraftcod.picturesaver.enums.LinkStatus;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.objects.PageLink;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.io.File;
import java.net.URL;
public class PageLinkItem
{
	private final SimpleObjectProperty<URL> url = new SimpleObjectProperty<>(null);
	private final SimpleObjectProperty<File> output = new SimpleObjectProperty<>(null);
	private final SimpleObjectProperty<LinkStatus> status = new SimpleObjectProperty<>(null);
	private final SimpleLongProperty bytes = new SimpleLongProperty(-1);
	private final SimpleLongProperty downloadedBytes = new SimpleLongProperty(-1);
	private final SimpleObjectProperty<ProgressBarMax> downloadBar = new SimpleObjectProperty<>(null);

	public PageLinkItem(Page page)
	{
		this.urlProperty().bindBidirectional(page.urlProperty());
		this.outputProperty().bindBidirectional(page.outputFolderProperty());
		this.linkStatusProperty().bindBidirectional(page.statusProperty());
		this.byteSizeProperty().bindBidirectional(page.byteSizeProperty());
		this.downloadedBytesProperty().bindBidirectional(page.downloadedByteSizeProperty());
		this.downloadBarProperty().bindBidirectional(page.downloadBarProperty());
	}

	public PageLinkItem(PageLink pageLink)
	{
		this.urlProperty().bindBidirectional(pageLink.urlProperty());
		this.outputProperty().bindBidirectional(pageLink.outputFileProperty());
		this.linkStatusProperty().bindBidirectional(pageLink.linkStatusProperty());
		this.byteSizeProperty().bindBidirectional(pageLink.byteSizeProperty());
		this.downloadedBytesProperty().bindBidirectional(pageLink.downloadedBytesProperty());
		this.downloadBarProperty().bindBidirectional(pageLink.downloadProgressBarProperty());
	}

	public SimpleObjectProperty<ProgressBarMax> downloadBarProperty()
	{
		return this.downloadBar;
	}

	public SimpleLongProperty downloadedBytesProperty()
	{
		return this.downloadedBytes;
	}

	public SimpleLongProperty byteSizeProperty()
	{
		return this.bytes;
	}

	public SimpleObjectProperty<LinkStatus> linkStatusProperty()
	{
		return this.status;
	}

	public SimpleObjectProperty<URL> urlProperty()
	{
		return this.url;
	}

	public SimpleObjectProperty<File> outputProperty()
	{
		return this.output;
	}
}

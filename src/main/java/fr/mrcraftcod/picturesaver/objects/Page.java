package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.enums.ContentType;
import fr.mrcraftcod.picturesaver.enums.LinkStatus;
import fr.mrcraftcod.picturesaver.jfx.components.table.ProgressBarMax;
import fr.mrcraftcod.utils.FileUtils;
import fr.mrcraftcod.utils.http.URLUtils;
import javafx.application.Platform;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import static fr.mrcraftcod.picturesaver.enums.LinkStatus.*;

public class Page
{
	private static int NEXT_ID = 0;
	private final int ID;
	private final SimpleObjectProperty<URL> originURL;
	private final SimpleObjectProperty<File> outputFolder;
	private final SimpleObjectProperty<LinkStatus> status;
	private final SimpleLongProperty byteSize;
	private final SimpleLongProperty downloadedByteSize;
	private final SimpleObjectProperty<ProgressBarMax> downloadBar;
	private final ArrayList<PageLink> pageLinks;
	private ArrayList<Consumer<Page>> onLinkFetchedCallbacks;
	private ArrayList<Consumer<Page>> onStatusChangeCallbacks;

	public Page(String url) throws Exception
	{
		this(new URL(url));
	}

	public Page(URL url) throws Exception
	{
		this.ID = NEXT_ID++;
		this.originURL = new SimpleObjectProperty<>(url);
		this.outputFolder = new SimpleObjectProperty<>(getOutputFile());
		this.byteSize = new SimpleLongProperty(-1);
		this.downloadedByteSize = new SimpleLongProperty(-1);
		this.downloadBar = new SimpleObjectProperty<>(new ProgressBarMax(this.downloadedByteSize, this.byteSize));
		this.downloadBarProperty().get().setMaxWidth(Double.MAX_VALUE);
		this.pageLinks = new ArrayList<>();
		this.onLinkFetchedCallbacks = new ArrayList<>();
		this.onStatusChangeCallbacks = new ArrayList<>();
		this.status = new SimpleObjectProperty<>(INITIALIZING);
		if(ContentType.isAllowed(url.toString()))
			this.pageLinks.add(new PageLink(this, url));
		this.pageLinks.addAll(findLinks(url));
		if(this.getPageLinks().isEmpty())
			throw new Exception("No links into that page " + pageLinks);
		this.byteSizeProperty().bind(getPageBytesBinding());
		this.downloadedByteSizeProperty().bind(getPageDownloadedBytesBinding());
	}

	public SimpleObjectProperty<ProgressBarMax> downloadBarProperty()
	{
		return this.downloadBar;
	}

	public SimpleLongProperty downloadedByteSizeProperty()
	{
		return this.downloadedByteSize;
	}

	private List<PageLink> findLinks(URL findURL) throws Exception
	{
		return URLUtils.convertStringToURL(URLUtils.pullLinks(findURL).stream().filter(ContentType::isAllowed).collect(Collectors.toList())).stream().map(url -> new PageLink(this, url)).collect(Collectors.toList());
	}

	public void fetchLinks(Consumer<Page> errorCallback)
	{
		Platform.runLater(() -> {
			for(PageLink pageLink : this.getPageLinks())
				if(!pageLink.fetch())
				{
					errorCallback.accept(this);
					return;
				}
			Page.this.onLinkFetchedCallbacks.forEach(pageCallback -> pageCallback.accept(this));
		});
	}

	public void setStatus(LinkStatus status)
	{
		this.status.set(status);
		this.onStatusChangeCallbacks.forEach(callback -> callback.accept(this));
	}

	public LinkStatus getStatus()
	{
		return this.statusProperty().get();
	}

	public SimpleObjectProperty<LinkStatus> statusProperty()
	{
		return this.status;
	}

	public ArrayList<PageLink> getPageLinks()
	{
		return this.pageLinks;
	}

	public URL getOriginURL()
	{
		return this.urlProperty().get();
	}

	@Override
	public String toString()
	{
		return getOriginURL().toString() + " containing " + getPageLinks().size() + " links";
	}

	public void onLinkFetched(Consumer<Page> callback)
	{
		onLinkFetchedCallbacks.add(callback);
	}

	public void onStatusChange(Consumer<Page> callback)
	{
		onStatusChangeCallbacks.add(callback);
	}

	public SimpleObjectProperty<URL> urlProperty()
	{
		return this.originURL;
	}

	public File getOutputFile()
	{
		if(outputFolderProperty() != null)
			return outputFolderProperty().get();
		return new File(FileUtils.getDesktopFolder(), "/" + this.getID() + "/");
	}

	public int getID()
	{
		return this.ID;
	}

	public void updateStatus()
	{
		int waitingDownload = (int) this.getPageLinks().stream().filter(page  -> page.getStatus() == WAITING_DOWNLOAD).count();
		int downloaded = (int) this.getPageLinks().stream().filter(page  -> page.getStatus() == DOWNLOADED || page.getStatus() == ERROR).count();
		if(downloaded == this.getPageLinks().size())
			this.setStatus(DOWNLOADED);
		else
			this.setStatus(WAITING_DOWNLOAD);
	}

	public SimpleObjectProperty<File> outputFolderProperty()
	{
		return this.outputFolder;
	}

	public SimpleLongProperty byteSizeProperty()
	{
		return this.byteSize;
	}

	public NumberBinding getPageBytesBinding()
	{
		if(this.getPageLinks().isEmpty())
			return null;
		NumberBinding numberBinding = this.getPageLinks().get(0).byteSizeProperty().add(0);
		for(int i = 1; i < this.getPageLinks().size(); i++)
			numberBinding = numberBinding.add(this.getPageLinks().get(i).byteSizeProperty());
		return numberBinding;
	}

	public NumberBinding getPageDownloadedBytesBinding()
	{
		if(this.getPageLinks().isEmpty())
			return null;
		NumberBinding numberBinding = this.getPageLinks().get(0).downloadedBytesProperty().add(0);
		for(int i = 1; i < this.getPageLinks().size(); i++)
			numberBinding = numberBinding.add(this.getPageLinks().get(i).downloadedBytesProperty());
		return numberBinding;
	}
}

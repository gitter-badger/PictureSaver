package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.enums.ContentType;
import fr.mrcraftcod.picturesaver.enums.PageStatus;
import fr.mrcraftcod.utils.Callback;
import fr.mrcraftcod.utils.FileUtils;
import fr.mrcraftcod.utils.http.URLUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Page
{
	private static int NEXT_ID = 0;
	private final int ID;
	private final SimpleObjectProperty<URL> originURL;
	private final ArrayList<PageLink> pageLinks;
	private ArrayList<Callback<Page>> onLinkFetchedCallbacks;
	private ArrayList<Callback<Page>> onStatusChangeCallbacks;
	private PageStatus status;

	public Page(String url) throws Exception
	{
		this(new URL(url));
	}

	public Page(URL url) throws Exception
	{
		this.ID = NEXT_ID++;
		this.originURL = new SimpleObjectProperty<>(url);
		this.pageLinks = new ArrayList<>();
		this.onLinkFetchedCallbacks = new ArrayList<>();
		this.onStatusChangeCallbacks = new ArrayList<>();
		this.status = PageStatus.INITIALIZING;
		this.pageLinks.addAll(findLinks(url));
	}

	private List<PageLink> findLinks(URL findURL) throws Exception
	{
		return URLUtils.convertStringToURL(URLUtils.pullLinks(findURL).stream().filter(ContentType::isAllowed).collect(Collectors.toList())).stream().map(url -> new PageLink(this, url)).collect(Collectors.toList());
	}

	public void fetchLinks(Callback<Page> errorCallback)
	{
		Platform.runLater(() -> {
			for(PageLink pageLink : this.getPageLinks())
				if(!pageLink.fetch())
				{
					errorCallback.call(this);
					return;
				}
			Page.this.onLinkFetchedCallbacks.forEach(pageCallback -> pageCallback.call(this));
		});
	}

	public void setStatus(PageStatus status)
	{
		this.status = status;
		this.onStatusChangeCallbacks.forEach(callback -> callback.call(this));
	}

	public PageStatus getStatus()
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

	public void onLinkFetched(Callback<Page> callback)
	{
		onLinkFetchedCallbacks.add(callback);
	}

	public void onStatusChange(Callback<Page> callback)
	{
		onStatusChangeCallbacks.add(callback);
	}

	public SimpleObjectProperty<URL> urlProperty()
	{
		return this.originURL;
	}

	public File getOutputFile()
	{
		return new File(FileUtils.getDesktopFolder(), "/" + this.getID() + "/");
	}

	public int getID()
	{
		return this.ID;
	}
}

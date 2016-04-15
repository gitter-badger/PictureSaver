package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.enums.ContentType;
import fr.mrcraftcod.picturesaver.enums.PageStatus;
import fr.mrcraftcod.utils.Callback;
import fr.mrcraftcod.utils.http.URLUtils;
import javafx.application.Platform;
import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DefaultDeferredManager;
import org.jdeferred.multiple.MultipleResults;
import org.jdeferred.multiple.OneResult;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Page
{
	private final URL originURL;
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
		this.originURL = url;
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
			DefaultDeferredManager deferredManager = new DefaultDeferredManager();
			deferredManager.when(this.pageLinks.stream().map(pageLink -> pageLink.fetch(deferredManager)).collect(Collectors.toList()).toArray(new Promise[this.pageLinks.size()])).done(new DoneCallback<MultipleResults>()
			{
				@Override
				public void onDone(MultipleResults results)
				{
					for(OneResult result : results)
						if(!(boolean) result.getResult())
						{
							errorCallback.call(Page.this);
							return;
						}
					Page.this.onLinkFetchedCallbacks.forEach(pageCallback -> pageCallback.call(Page.this));
				}
			});
			deferredManager.shutdown();
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
		return this.originURL;
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
}

package fr.mrcraftcod.picturesaver.objects;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.enums.ContentType;
import fr.mrcraftcod.utils.http.URLUtils;
import org.jdeferred.Promise;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Page
{
	private final URL originURL;
	private final ArrayList<PageLink> pageLinks;

	public Page(String url) throws Exception
	{
		this(new URL(url));
	}

	public Page(URL url) throws Exception
	{
		this.originURL = url;
		this.pageLinks = new ArrayList<>();
		this.pageLinks.addAll(findLinks(url));
	}

	private List<PageLink> findLinks(URL findURL) throws Exception
	{
		return URLUtils.convertStringToURL(URLUtils.pullLinks(findURL).stream().filter(ContentType::isAllowed).collect(Collectors.toList())).stream().map(url -> new PageLink(this, url)).collect(Collectors.toList());
	}

	public Promise<ArrayList<PageLink>, Throwable, Void> fetchLinks()
	{
		return Constants.deferredManager.when(() -> {
			this.pageLinks.forEach(PageLink::fetch);
			return this.pageLinks;
		});
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
}

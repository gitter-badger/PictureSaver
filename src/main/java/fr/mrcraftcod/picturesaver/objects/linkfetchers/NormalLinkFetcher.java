package fr.mrcraftcod.picturesaver.objects.linkfetchers;

import fr.mrcraftcod.picturesaver.enums.ContentType;
import fr.mrcraftcod.picturesaver.interfaces.LinkFetcher;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.objects.PageLink;
import fr.mrcraftcod.utils.http.URLUtils;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class NormalLinkFetcher implements LinkFetcher
{
	@Override
	public List<PageLink> fetch(Page page, URL url) throws Exception
	{
		return URLUtils.convertStringToURL(URLUtils.pullLinks(url).stream().filter(ContentType::isAllowed).collect(Collectors.toList())).stream().map(urls -> new PageLink(page, urls)).collect(Collectors.toList());
	}
}

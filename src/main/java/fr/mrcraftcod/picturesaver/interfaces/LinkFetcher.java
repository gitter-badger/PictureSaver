package fr.mrcraftcod.picturesaver.interfaces;

import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.objects.PageLink;
import java.net.URL;
import java.util.List;
public interface LinkFetcher
{
	public List<PageLink> fetch(Page page, URL url) throws Exception;
}

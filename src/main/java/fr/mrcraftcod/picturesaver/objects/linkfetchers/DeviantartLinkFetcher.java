package fr.mrcraftcod.picturesaver.objects.linkfetchers;

import fr.mrcraftcod.picturesaver.interfaces.LinkFetcher;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.objects.PageLink;
import fr.mrcraftcod.utils.StringUtils;
import fr.mrcraftcod.utils.http.URLHandler;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviantartLinkFetcher implements LinkFetcher
{
	private static final Pattern userPattern = Pattern.compile(".+//(.+)\\.deviantart\\.com.*");

	@Override
	public List<PageLink> fetch(Page page, URL url) throws Exception
	{
		ArrayList<PageLink> links = new ArrayList<>();
		JSONObject obj = URLHandler.getAsJSON(new URL("http://backend.deviantart.com/oembed?url=" + url.toString()));
		Matcher m = userPattern.matcher(obj.getString("author_url"));
		m.matches();
		links.add(new PageLink(page, new URL(obj.getString("url")), StringUtils.getEnding(obj.getString("url"), '/', 0, ""), m.group(1)));
		return links;
	}
}

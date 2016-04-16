package fr.mrcraftcod.picturesaver.threads;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.objects.PageLink;
import fr.mrcraftcod.utils.Callback;
import fr.mrcraftcod.utils.http.URLHandler;
import javafx.application.Platform;
import javafx.concurrent.Task;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class DownloadTask extends Task<Page>
{
	private final Page page;
	private final Callback<Page> successCallback;
	private final Callback<Page> errorCallback;

	public DownloadTask(Page page, Callback<Page> successCallback, Callback<Page> errorCallback)
	{
		this.page = page;
		this.successCallback = successCallback;
		this.errorCallback = errorCallback;
	}

	@Override
	protected Page call()
	{
		for(PageLink pageLink : this.page.getPageLinks())
			try
			{
				if(this.isCancelled())
				{
					if(this.errorCallback != null)
						this.errorCallback.call(this.page);
					return this.page;
				}
				if(!downloadLink(pageLink))
				{
					if(this.errorCallback != null)
						this.errorCallback.call(this.page);
				}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
				if(this.errorCallback != null)
					this.errorCallback.call(this.page);
				return this.page;
			}
		if(this.successCallback != null)
			this.successCallback.call(this.page);
		return this.page;
	}

	private boolean downloadLink(PageLink pageLink) throws InterruptedException
	{
		try(InputStream is = URLHandler.getAsBinary(pageLink.getUrl()); FileOutputStream fos = new FileOutputStream(pageLink.getOutputFile()))
		{
			int read = 0;
			byte[] bytes = new byte[1024];
			int i = -1;
			while((i = is.read(bytes)) != -1)
			{
				fos.write(bytes);
				read += i;
				final int tRead = read;
				Platform.runLater(() -> pageLink.setDownloadedBytes(tRead));
			}
		}
		catch(IOException | UnirestException | URISyntaxException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

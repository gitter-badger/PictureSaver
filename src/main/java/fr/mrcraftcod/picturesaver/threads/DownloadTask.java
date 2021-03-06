package fr.mrcraftcod.picturesaver.threads;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.mrcraftcod.picturesaver.enums.LinkStatus;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.objects.PageLink;
import fr.mrcraftcod.utils.http.URLHandler;
import javafx.application.Platform;
import javafx.concurrent.Task;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import static fr.mrcraftcod.picturesaver.enums.LinkStatus.ERROR;
import static fr.mrcraftcod.picturesaver.enums.LinkStatus.WAITING_DOWNLOAD;

public class DownloadTask extends Task<Page>
{
	private final Page page;
	private final Consumer<Page> successCallback;
	private final Consumer<Page> errorCallback;

	public DownloadTask(Page page, Consumer<Page> successCallback, Consumer<Page> errorCallback)
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
						this.errorCallback.accept(this.page);
					return this.page;
				}
				if(pageLink.getStatus() == WAITING_DOWNLOAD && !downloadLink(pageLink))
				{
					if(this.errorCallback != null)
						this.errorCallback.accept(this.page);
				}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
				if(this.errorCallback != null)
					this.errorCallback.accept(this.page);
				return this.page;
			}
		if(this.successCallback != null)
			this.successCallback.accept(this.page);
		return this.page;
	}

	private boolean downloadLink(PageLink pageLink) throws InterruptedException
	{
		pageLink.setStatus(LinkStatus.DOWNLOADING);
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
		catch(UnirestException | URISyntaxException e)
		{
			e.printStackTrace();
			pageLink.setStatus(LinkStatus.WAITING_DOWNLOAD);
			return false;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			pageLink.setStatus(ERROR);
			return true;
		}
		pageLink.setStatus(LinkStatus.DOWNLOADED);
		return true;
	}
}

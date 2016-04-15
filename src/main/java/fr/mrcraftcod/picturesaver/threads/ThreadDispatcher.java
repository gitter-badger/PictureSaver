package fr.mrcraftcod.picturesaver.threads;

import fr.mrcraftcod.picturesaver.enums.PageStatus;
import fr.mrcraftcod.picturesaver.interfaces.ClipboardListener;
import fr.mrcraftcod.picturesaver.interfaces.ProgressListener;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.utils.Log;
import fr.mrcraftcod.utils.http.URLUtils;
import fr.mrcraftcod.utils.threads.ThreadLoop;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import static fr.mrcraftcod.picturesaver.enums.PageStatus.WAITING_DOWNLOAD;

public class ThreadDispatcher extends ThreadLoop implements ClipboardListener
{
	private static final long SLEEP_INTERVAL = 750;
	private final ThreadClipboard threadClipboard;
	private final ArrayList<ProgressListener> progressListeners;
	private final ArrayList<Page> waitingInit;
	private final LinkedBlockingQueue<Page> waitingDownload;
	private final ArrayList<Page> downloaded;
	private final ExecutorService executor;

	public ThreadDispatcher()
	{
		this.setName("PS-TD");
		this.waitingInit = new ArrayList<Page>();
		this.waitingDownload = new LinkedBlockingQueue<Page>();
		this.downloaded = new ArrayList<>();
		this.progressListeners = new ArrayList<>();
		this.threadClipboard = new ThreadClipboard();
		this.threadClipboard.addListener(this);
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.submit(threadClipboard);
	}

	public void addProgressListener(ProgressListener listener)
	{
		this.progressListeners.add(listener);
	}

	@Override
	public void onClosed()
	{
		this.threadClipboard.close();
		this.executor.shutdown();
	}

	@Override
	public void loop()
	{
		processInit();
		processDownloads();
		try
		{
			Thread.sleep(SLEEP_INTERVAL);
		}
		catch(InterruptedException e){}
	}

	private void processDownloads()
	{

	}

	private void processInit()
	{
		this.waitingInit.forEach(Page::fetchLinks);
	}

	@Override
	public void clipboardChangeEvent(String value)
	{
		for(String link : URLUtils.pullLinks(value))
			try
			{
				this.addPage(new Page(link));
			}
			catch(Exception e)
			{
				Log.warning("Error creating a page!", e);
			}
	}

	private void addPage(Page page)
	{
		Log.info("New page detected: " + page);
		page.onStatusChange(this::changeStatus);
		page.onLinkFetched(evt -> evt.setStatus(WAITING_DOWNLOAD));
		this.progressListeners.forEach(listener -> listener.pageAdded(page));
		page.setStatus(PageStatus.INITIALIZING);
	}

	private void changeStatus(Page page)
	{
		this.waitingInit.remove(page);
		this.downloaded.remove(page);
		switch(page.getStatus())
		{
			case INITIALIZING:
				this.waitingInit.add(page);
				break;
			case WAITING_DOWNLOAD:
				this.waitingDownload.offer(page);
				break;
			case DOWNLOADED:
				this.downloaded.add(page);
				break;
		}
	}
}

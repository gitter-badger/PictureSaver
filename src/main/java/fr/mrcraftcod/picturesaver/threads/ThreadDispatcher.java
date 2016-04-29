package fr.mrcraftcod.picturesaver.threads;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import fr.mrcraftcod.picturesaver.interfaces.ClipboardListener;
import fr.mrcraftcod.picturesaver.interfaces.ProgressListener;
import fr.mrcraftcod.picturesaver.objects.GMailHandler;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.utils.Log;
import fr.mrcraftcod.utils.http.URLUtils;
import fr.mrcraftcod.utils.mail.GMailFetcher;
import fr.mrcraftcod.utils.mail.GMailUtils;
import fr.mrcraftcod.utils.threads.ThreadLoop;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import static fr.mrcraftcod.picturesaver.enums.LinkStatus.*;

public class ThreadDispatcher extends ThreadLoop implements ClipboardListener
{
	private static final long SLEEP_INTERVAL = 750;
	private static final int MAX_SIMULTANEOUS_DOWNLOADS = 2;
	private static final int MAX_SIMULTANEOUS_FETCHING = 2;
	private final ThreadClipboard threadClipboard;
	private final ArrayList<ProgressListener> progressListeners;
	private final ArrayList<Page> waitingInit;
	private final LinkedBlockingQueue<Page> waitingDownload;
	private final ArrayList<Page> downloaded;
	private final ArrayList<Page> downloading;
	private final ExecutorService executor;
	private GMailFetcher threadMail;
	private int downloadingCount;
	private boolean first = true;

	public ThreadDispatcher()
	{
		this.setName("PS-TD");
		this.downloadingCount = 0;
		this.waitingInit = new ArrayList<Page>();
		this.waitingDownload = new LinkedBlockingQueue<Page>();
		this.downloaded = new ArrayList<>();
		this.downloading = new ArrayList<>();
		this.progressListeners = new ArrayList<>();
		this.executor = Executors.newFixedThreadPool(MAX_SIMULTANEOUS_FETCHING + MAX_SIMULTANEOUS_DOWNLOADS + 1);
		this.threadClipboard = new ThreadClipboard();
		this.threadClipboard.addListener(this);
		ArrayList<ConfigKey> mailKeys = new ArrayList<ConfigKey>();
		mailKeys.add(ConfigKey.EMAIL_FETCH_STATUS);
		mailKeys.add(ConfigKey.EMAIL_FETCH_MAIL);
		mailKeys.add(ConfigKey.EMAIL_FETCH_PASSWORD);
		Constants.configuration.getValues(mailKeys, result -> {
			if(result.keySet().containsAll(mailKeys) && ConfigKey.EMAIL_FETCH_STATUS.parseValue(result.get(ConfigKey.EMAIL_FETCH_STATUS)))
				try
				{
					this.threadMail = GMailUtils.fetchGMailFolder(ConfigKey.EMAIL_FETCH_MAIL.parseValue(result.get(ConfigKey.EMAIL_FETCH_MAIL)), ConfigKey.EMAIL_FETCH_PASSWORD.parseValue(result.get(ConfigKey.EMAIL_FETCH_PASSWORD)), "INBOX", new GMailHandler(this::clipboardChangeEvent));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				this.executor.submit(threadClipboard);
			}, null);
	}

	public void addProgressListener(ProgressListener listener)
	{
		this.progressListeners.add(listener);
	}

	@Override
	public void onClosed()
	{
		if(this.threadMail != null)
			this.threadMail.close();
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
		if(this.waitingDownload.size() < 1)
			return;
		if(this.getDownloadingCount() < MAX_SIMULTANEOUS_DOWNLOADS)
		{
			Page downloadPage = this.waitingDownload.remove();
			this.addDownloadCount();
			Thread thread = new Thread(new DownloadTask(downloadPage, page -> {
				page.updateStatus();
				this.removeDownloadCount();
			}, page -> {
				page.setStatus(WAITING_DOWNLOAD);
				this.removeDownloadCount();
			}));
			downloadPage.setStatus(DOWNLOADING);
			thread.setDaemon(true);
			this.getExecutor().submit(thread);
		}
	}

	private void removeDownloadCount()
	{
		this.downloadingCount--;
	}

	private void addDownloadCount()
	{
		this.downloadingCount++;
	}

	private void processInit()
	{
		Iterator<Page> iterator = this.waitingInit.iterator();
		while(iterator.hasNext())
		{
			Page page = iterator.next();
			iterator.remove();
			Thread thread = new Thread(() -> page.fetchLinks(pageError -> pageError.setStatus(INITIALIZING)));
			thread.setDaemon(true);
			this.getExecutor().submit(thread);
		}
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
		if(!page.getOrigin().activatedProperty().get())
		{
			Log.info("Origin deactivated, skipping");
			return;
		}
		page.onStatusChange(this::changeStatus);
		page.onLinkFetched(evt -> evt.setStatus(WAITING_DOWNLOAD));
		this.progressListeners.forEach(listener -> listener.pageAdded(page));
		page.setStatus(INITIALIZING);
	}

	private void changeStatus(Page page)
	{
		this.waitingInit.remove(page);
		this.downloading.remove(page);
		this.downloaded.remove(page);
		switch(page.getStatus())
		{
			case INITIALIZING:
				this.waitingInit.add(page);
				break;
			case WAITING_DOWNLOAD:
				this.waitingDownload.offer(page);
				break;
			case DOWNLOADING:
				this.downloading.add(page);
				break;
			case DOWNLOADED:
				this.downloaded.add(page);
				break;
		}
	}

	public int getDownloadingCount()
	{
		return this.downloadingCount;
	}

	public ExecutorService getExecutor()
	{
		return this.executor;
	}
}

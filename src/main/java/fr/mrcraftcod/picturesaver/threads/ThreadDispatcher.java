package fr.mrcraftcod.picturesaver.threads;

import fr.mrcraftcod.picturesaver.interfaces.ClipboardListener;
import fr.mrcraftcod.picturesaver.interfaces.ProgressListener;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.utils.Log;
import fr.mrcraftcod.utils.http.URLUtils;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
public class ThreadDispatcher extends Thread implements ClipboardListener
{
	private static final long SLEEP_INTERVAL = 750;
	private final ThreadClipboard threadClipboard;
	private final ArrayList<ProgressListener> progressListeners;
	private final LinkedBlockingQueue<Page> waitingDownload;
	private final ExecutorService executor;
	private boolean running;

	public ThreadDispatcher()
	{
		this.setName("PS-TD");
		this.running = true;
		this.waitingDownload = new LinkedBlockingQueue<Page>();
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
	public void run()
	{
		while(running && !this.isInterrupted())
		{
			try
			{
				Thread.sleep(SLEEP_INTERVAL);
			}
			catch(InterruptedException e){}
		}
	}

	public void close()
	{
		this.threadClipboard.close();
		this.interrupt();
		this.running = false;
		executor.shutdown();
	}

	@Override
	public void clipboardChangeEvent(String value)
	{
		for(String link : URLUtils.pullLinks(value))
			try
			{
				addPage(new Page(link));
			}
			catch(Exception e)
			{
				Log.warning("Error creating a page!", e);
			}
	}

	private void addPage(Page page)
	{
		Log.info("New page detected: " + page);
		waitingDownload.offer(page);
		progressListeners.forEach(listener -> listener.pageAdded(page));
		page.fetchLinks();
	}
}

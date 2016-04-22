package fr.mrcraftcod.picturesaver.threads;

import fr.mrcraftcod.picturesaver.interfaces.ClipboardListener;
import fr.mrcraftcod.utils.threads.ThreadLoop;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

public class ThreadClipboard extends ThreadLoop
{
	private static final long SLEEP_INTERVAL = 333;
	private String lastClipboard;
	private final ArrayList<ClipboardListener> listeners;

	public ThreadClipboard()
	{
		this(false);
	}

	public ThreadClipboard(boolean takeOnlyNew)
	{
		this.setName("PS-TC");
		this.setDaemon(true);
		listeners = new ArrayList<>();
		lastClipboard = takeOnlyNew ? getClipboardAsText() : null;
	}

	private String getClipboardAsText()
	{
		String clipText = "";
		final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try
		{
			final Transferable contents = clipboard.getContents(null);
			if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor))
				clipText = (String) contents.getTransferData(DataFlavor.stringFlavor);
		}
		catch(final Exception e){}
		return clipText;
	}

	public void addListener(ClipboardListener listener)
	{
		this.listeners.add(listener);
	}

	@Override
	public void loop()
	{
		String newClipboard = getClipboardAsText();
		if(newClipboard != null && !newClipboard.equals(lastClipboard))
		{
			lastClipboard = newClipboard;
			listeners.forEach(listener -> listener.clipboardChangeEvent(newClipboard));
		}
		try
		{
			Thread.sleep(SLEEP_INTERVAL);
		}
		catch(InterruptedException e){}
	}
}

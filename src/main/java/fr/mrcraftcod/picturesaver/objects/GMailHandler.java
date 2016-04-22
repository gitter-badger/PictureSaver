package fr.mrcraftcod.picturesaver.objects;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.event.MessageCountEvent;
import java.io.IOException;
import java.util.function.Consumer;

public class GMailHandler implements Consumer<MessageCountEvent>
{
	private final Consumer<String> bodyCallback;

	public GMailHandler(Consumer<String> bodyCallback)
	{
		this.bodyCallback = bodyCallback;
	}

	@Override
	public void accept(MessageCountEvent messageCountEvent)
	{
		if(messageCountEvent.getType() == MessageCountEvent.ADDED)
		{
			for(Message message : messageCountEvent.getMessages())
			{
				try
				{
					if(message.getContent() instanceof Multipart)
						bodyCallback.accept(((Multipart) message.getContent()).getBodyPart(0).getContent().toString());
				}
				catch(IOException | MessagingException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}

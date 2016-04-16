package fr.mrcraftcod.picturesaver.jfx;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.enums.Resources;
import fr.mrcraftcod.picturesaver.interfaces.ProgressListener;
import fr.mrcraftcod.picturesaver.jfx.components.table.DownloadsTable;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.threads.ThreadDispatcher;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFrame extends Application implements ProgressListener
{
	private ThreadDispatcher threadDispatcher;
	private DownloadsTable downloadsTable;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		Scene scene = new Scene(createContent());
		stage.setTitle(Constants.APP_NAME);
		stage.setScene(scene);
		stage.getIcons().add(new Image(Resources.ICONS.getResourceURL("icon64.png").toString()));
		stage.sizeToScene();
		stage.show();
		threadDispatcher = new ThreadDispatcher();
		threadDispatcher.addProgressListener(this);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(threadDispatcher);
		stage.setOnCloseRequest(event -> {
			threadDispatcher.close();
			executor.shutdown();
		});
	}

	private Parent createContent()
	{
		downloadsTable = new DownloadsTable();

		StackPane root = new StackPane();
		root.setPrefSize(800, 600);
		root.getChildren().addAll(downloadsTable);

		/*new Thread(() -> {
			try
			{
				downloadsTable.addPageLinks(new Page("https://instagram.com/p/7IFMstufTV/").getPageLinks());
				downloadsTable.addPageLinks(new Page("https://instagram.com/p/Piin6aufU3/").getPageLinks());
				downloadsTable.addPageLinks(new Page("https://instagram.com/p/JUmZ7-Ofc1/").getPageLinks());
				downloadsTable.addPageLinks(new Page("https://instagram.com/p/J2ksO/").getPageLinks());
				downloadsTable.addPageLinks(new Page("https://instagram.com/p/qwtU/").getPageLinks());
				downloadsTable.addPageLinks(new Page("https://instagram.com/p/GpVl/").getPageLinks());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}).run();*/

		return root;
	}

	@Override
	public void pageAdded(Page page)
	{
		downloadsTable.addPage(page);
	}
}

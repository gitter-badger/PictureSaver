package fr.mrcraftcod.picturesaver.jfx;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.enums.Resources;
import fr.mrcraftcod.picturesaver.interfaces.ProgressListener;
import fr.mrcraftcod.picturesaver.jfx.components.table.DownloadsTable;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.threads.ThreadDispatcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFrame extends Application implements ProgressListener
{
	private ThreadDispatcher threadDispatcher;
	private DownloadsTable downloadsTable;

	public static void main(String[] args) throws InterruptedException
	{
		Constants.configuration.pullAllValues();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		Scene scene = new Scene(createContent(stage));
		stage.setTitle(Constants.APP_NAME);
		stage.setScene(scene);
		stage.getIcons().add(new Image(Constants.resources.getResource(Resources.ICONS, "icon64.png").toString()));
		stage.sizeToScene();
		stage.show();
		threadDispatcher = new ThreadDispatcher();
		threadDispatcher.addProgressListener(this);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(threadDispatcher);
		stage.setOnCloseRequest(event -> {
			threadDispatcher.close();
			executor.shutdown();
			Constants.configuration.close();
		});
	}

	private Parent createContent(Stage stage)
	{
		VBox root = new VBox();

		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");
		MenuItem settingsItem = new MenuItem("Settings");
		MenuItem foldersItem = new MenuItem("Folder outputs");
		settingsItem.setOnAction(evt -> openSettings(stage));
		foldersItem.setOnAction(evt -> openFoldersOutputs(stage));
		menuFile.getItems().addAll(settingsItem, foldersItem);
		menuBar.getMenus().addAll(menuFile);


		downloadsTable = new DownloadsTable();

		StackPane stackPane = new StackPane();
		stackPane.setPrefSize(800, 600);
		stackPane.getChildren().addAll(downloadsTable);

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

		root.getChildren().addAll(menuBar, stackPane);

		return root;
	}

	private void openFoldersOutputs(Stage parentStage)
	{
		Stage foldersOutputs = new OutputFoldersStage(parentStage);
		foldersOutputs.show();
	}

	private void openSettings(Stage parentStage)
	{
		Stage settings = new SettingsStage(parentStage);
		settings.show();
	}

	@Override
	public void pageAdded(Page page)
	{
		Platform.runLater(() -> downloadsTable.addPage(page));
	}
}

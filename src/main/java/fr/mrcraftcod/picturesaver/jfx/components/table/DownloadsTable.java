package fr.mrcraftcod.picturesaver.jfx.components.table;

import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.objects.PageLink;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.Collection;

public class DownloadsTable extends TableView<PageLink>
{
	private final ObservableList<PageLink> datas;

	public DownloadsTable()
	{
		super();

		this.datas = FXCollections.observableArrayList();

		TableColumn<PageLink, URL> sourceColumn = new TableColumn<>("Source");
		sourceColumn.setCellValueFactory(new PropertyValueFactory<>("sourceURL"));
		TableColumn<PageLink, URL> linkColumn = new TableColumn<>("Link");
		linkColumn.setCellValueFactory(new PropertyValueFactory<PageLink, URL>("url"));
		TableColumn<PageLink, Long> byteSizeColumn = new TableColumn<>("Size");
		byteSizeColumn.setCellValueFactory(new PropertyValueFactory<PageLink, Long>("byteSize"));

		TableColumn infosColumn = new TableColumn("File infos");
		infosColumn.getColumns().addAll(sourceColumn, linkColumn, byteSizeColumn);

		TableColumn<PageLink, Long> downloadBytesColumn = new TableColumn<>("Downloaded");
		downloadBytesColumn.setCellValueFactory(new PropertyValueFactory<>("downloadedBytes"));
		TableColumn<PageLink, ProgressBarMax> downloadProgressColumn = new TableColumn<>("Progress");
		downloadProgressColumn.setCellValueFactory(new PropertyValueFactory<PageLink, ProgressBarMax>("downloadProgressBar"));

		TableColumn downloadingColumn = new TableColumn("Download");
		downloadingColumn.getColumns().addAll(downloadBytesColumn, downloadProgressColumn);

		this.getColumns().addAll(infosColumn, downloadingColumn);

		this.setItems(datas);
	}

	public void addPageLinks(Collection<? extends PageLink> pageLinks)
	{
		pageLinks.forEach(datas::add);
	}

	public void addPage(Page page)
	{
		addPageLinks(page.getPageLinks());
	}
}

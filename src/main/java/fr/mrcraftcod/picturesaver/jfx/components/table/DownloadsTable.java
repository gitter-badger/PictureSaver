package fr.mrcraftcod.picturesaver.jfx.components.table;

import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.objects.PageLink;
import fr.mrcraftcod.utils.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
		sourceColumn.setCellValueFactory(callData -> callData.getValue().sourceURLProperty());
		TableColumn<PageLink, URL> linkColumn = new TableColumn<>("Link");
		linkColumn.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
		TableColumn<PageLink, Number> byteSizeColumn = new TableColumn<>("Size");
		byteSizeColumn.setCellValueFactory(cellData -> cellData.getValue().byteSizeProperty());
		byteSizeColumn.setCellFactory(column -> {
			return new TableCell<PageLink, Number>(){
				@Override
				protected void updateItem(Number item, boolean empty)
				{
					super.updateItem(item, empty);
					if(item == null || empty)
						setText("");
					else
						setText(StringUtils.getDownloadSizeText(item.doubleValue()));
				}
			};
		});

		TableColumn infosColumn = new TableColumn("File infos");
		infosColumn.getColumns().addAll(sourceColumn, linkColumn, byteSizeColumn);

		TableColumn<PageLink, Number> downloadBytesColumn = new TableColumn<>("Downloaded");
		downloadBytesColumn.setCellValueFactory(cellData -> cellData.getValue().downloadedBytesProperty());
		downloadBytesColumn.setCellFactory(column -> {
			return new TableCell<PageLink, Number>(){
				@Override
				protected void updateItem(Number item, boolean empty)
				{
					super.updateItem(item, empty);
					if(item == null || empty)
						setText("");
					else
						setText(StringUtils.getDownloadSizeText(item.doubleValue()));
				}
			};
		});
		TableColumn<PageLink, ProgressBarMax> downloadProgressColumn = new TableColumn<>("Progress");
		downloadProgressColumn.setCellValueFactory(cellData -> cellData.getValue().downloadProgressBarProperty());

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

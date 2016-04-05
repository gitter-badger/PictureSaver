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

		TableColumn originalLinkColumn = new TableColumn("Original link");
		originalLinkColumn.setCellValueFactory(new PropertyValueFactory<PageLink, URL>("URL"));
		TableColumn byteSizeColumn = new TableColumn("Size");
		byteSizeColumn.setCellValueFactory(new PropertyValueFactory<PageLink, String>("byteSizeString"));

		TableColumn infosColumn = new TableColumn("File infos");
		infosColumn.getColumns().addAll(originalLinkColumn, byteSizeColumn);

		TableColumn downloadColumn = new TableColumn("Download");
		TableColumn uploadColumn = new TableColumn("Upload");
		TableColumn progressColumn = new TableColumn("Progress");
		progressColumn.getColumns().addAll(downloadColumn, uploadColumn);

		this.getColumns().addAll(infosColumn, progressColumn);

		this.setItems(datas);
	}

	public void addPageLinks(Collection<? extends PageLink> pageLinks)
	{
		datas.addAll(pageLinks);
	}

	public void addPage(Page page)
	{
		addPageLinks(page.getPageLinks());
	}
}

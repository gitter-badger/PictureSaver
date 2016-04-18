package fr.mrcraftcod.picturesaver.jfx.components.table;

import fr.mrcraftcod.picturesaver.enums.LinkStatus;
import fr.mrcraftcod.picturesaver.objects.Page;
import fr.mrcraftcod.picturesaver.objects.PageLink;
import fr.mrcraftcod.utils.StringUtils;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import java.io.File;
import java.net.URL;

public class DownloadsTable extends TreeTableView<PageLinkItem>
{
	private final TreeItem<PageLinkItem> root;

	public DownloadsTable()
	{
		super();

		this.root = new TreeItem<>();
		this.setRoot(this.root);
		this.setShowRoot(false);

		TreeTableColumn<PageLinkItem, URL> linkColumn = new TreeTableColumn<>("Link");
		linkColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().urlProperty());
		TreeTableColumn<PageLinkItem, File> outputFileColumn = new TreeTableColumn<>("Path");
		outputFileColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().outputProperty());
		TreeTableColumn<PageLinkItem, LinkStatus> statusColumn = new TreeTableColumn<>("Status");
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().linkStatusProperty());
		TreeTableColumn<PageLinkItem, Number> byteSizeColumn = new TreeTableColumn<>("Size");
		byteSizeColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().byteSizeProperty());
		byteSizeColumn.setCellFactory(column -> {
			return new TreeTableCell<PageLinkItem, Number>(){
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

		TreeTableColumn infosColumn = new TreeTableColumn("File infos");
		infosColumn.getColumns().addAll(linkColumn, outputFileColumn, statusColumn, byteSizeColumn);

		TreeTableColumn<PageLinkItem, Number> downloadBytesColumn = new TreeTableColumn<>("Downloaded");
		downloadBytesColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().downloadedBytesProperty());
		downloadBytesColumn.setCellFactory(column -> {
			return new TreeTableCell<PageLinkItem, Number>(){
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
		TreeTableColumn<PageLinkItem, ProgressBarMax> downloadProgressColumn = new TreeTableColumn<>("Progress");
		downloadProgressColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().downloadBarProperty());

		TreeTableColumn downloadingColumn = new TreeTableColumn("Download");
		downloadingColumn.getColumns().addAll(downloadBytesColumn, downloadProgressColumn);

		this.getColumns().addAll(infosColumn, downloadingColumn);
		this.getSelectionModel().setCellSelectionEnabled(true);

		Platform.runLater(this::resizeContent);
	}

	public void addPageLinks(TreeItem<PageLinkItem> item)
	{
		root.getChildren().add(item);
	}

	public void addPage(Page page)
	{
		addPageLinks(createTreeItem(page));
	}

	private TreeItem<PageLinkItem> createTreeItem(Page page)
	{
		TreeItem<PageLinkItem> item = new TreeItem<>(new PageLinkItem(page));
		item.setExpanded(true);
		for(PageLink pageLink : page.getPageLinks())
		{
			TreeItem<PageLinkItem> subItem = new TreeItem<>(new PageLinkItem(pageLink));
			item.getChildren().add(subItem);
		}
		return item;
	}

	private void resizeContent()
	{
		this.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		this.setColumnResizePolicy(TreeTableView.UNCONSTRAINED_RESIZE_POLICY);
	}
}

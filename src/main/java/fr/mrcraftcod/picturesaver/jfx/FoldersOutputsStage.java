package fr.mrcraftcod.picturesaver.jfx;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import fr.mrcraftcod.picturesaver.interfaces.ConfigInput;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.ArrayList;

public class FoldersOutputsStage extends Stage
{
	private SimpleBooleanProperty updated;
	private ArrayList<ConfigInput> saveFunctions;

	public FoldersOutputsStage(Stage parentStage)
	{
		super();
		this.updated = new SimpleBooleanProperty(false);
		this.saveFunctions = new ArrayList<>();
		this.initModality(Modality.WINDOW_MODAL);
		this.initOwner(parentStage);
		this.setScene(new Scene(createContent()));
		this.setOnCloseRequest(evt -> closeFrame());
	}

	private void closeFrame()
	{
		if(this.updated.get())
		{
			System.out.println("Warn user");//TODO
		}
		this.close();
	}

	private Parent createContent()
	{
		VBox root = new VBox();


		HBox controls = createControls();

		root.getChildren().addAll(controls);
		return root;
	}

	private HBox createControls()
	{
		HBox root = new HBox();
		Button save = new Button("Save");
		save.setMaxWidth(Double.MAX_VALUE);
		save.setOnAction(evt -> {
			save();
			close();
		});
		Button cancel = new Button("Cancel");
		cancel.setMaxWidth(Double.MAX_VALUE);
		cancel.setOnAction(evt -> closeFrame());
		root.getChildren().addAll(save, cancel);
		return root;
	}

	private void save()
	{
		ArrayList<Pair<ConfigKey, Object>> keys = new ArrayList<>();
		for(ConfigInput configInput : this.saveFunctions)
			keys.add(configInput.getValue());
		Constants.configuration.setValues(keys);
		this.close();
	}
}

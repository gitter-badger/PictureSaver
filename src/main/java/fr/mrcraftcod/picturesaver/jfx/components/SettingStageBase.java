package fr.mrcraftcod.picturesaver.jfx.components;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.interfaces.ConfigInput;
import fr.mrcraftcod.picturesaver.jfx.components.inputs.BooleanInput;
import fr.mrcraftcod.picturesaver.jfx.components.inputs.FileInput;
import fr.mrcraftcod.picturesaver.jfx.components.inputs.StringInput;
import fr.mrcraftcod.picturesaver.objects.ConfigValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public abstract class SettingStageBase extends Stage
{
	private final Stage parent;
	private final SimpleBooleanProperty updated;
	private final ArrayList<ConfigInput> saveFunctions;

	public SettingStageBase(Stage parentStage)
	{
		super();
		this.parent = parentStage;
		this.updated = new SimpleBooleanProperty(false);
		this.saveFunctions = new ArrayList<>();
		this.initModality(Modality.WINDOW_MODAL);
		this.initOwner(parentStage);
		this.setScene(new Scene(createContent()));
		this.setOnCloseRequest(evt -> closeFrame());
	}

	private VBox createContent()
	{
		VBox root = new VBox();

		HBox controls = createControls();

		root.getChildren().addAll(getComponents());
		root.getChildren().addAll(controls);
		return root;
	}

	protected abstract Collection<? extends Node> getComponents();

	private void closeFrame()
	{
		if(this.updated.get())
		{
			System.out.println("Warn user");//TODO
		}
		this.close();
	}

	public FileInput createFileInput(ConfigValue<File> configValue, String description)
	{
		FileInput root = new FileInput(this, configValue, description);
		this.saveFunctions.add(root);
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
		HBox.setHgrow(save, Priority.ALWAYS);
		HBox.setHgrow(cancel, Priority.ALWAYS);
		root.getChildren().addAll(save, cancel);
		return root;
	}

	private void save()
	{
		ArrayList<ConfigValue> keys = new ArrayList<>();
		for(ConfigInput configInput : this.saveFunctions)
			keys.add(configInput.getValue());
		Constants.configuration.setValues(keys);
		this.close();
	}

	public StringInput createTextInput(ConfigValue<String> configValue,  String description)
	{
		StringInput root = new StringInput(configValue, description);
		this.saveFunctions.add(root);
		return root;
	}

	public BooleanInput createBooleanInput(ConfigValue<Boolean> configValue, String description)
	{
		BooleanInput root = new BooleanInput(configValue, description);
		this.saveFunctions.add(root);
		return root;
	}

	public BooleanProperty updatedProperty()
	{
		return this.updated;
	}
}

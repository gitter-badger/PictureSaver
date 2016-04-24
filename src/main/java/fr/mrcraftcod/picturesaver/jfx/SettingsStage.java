package fr.mrcraftcod.picturesaver.jfx;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import fr.mrcraftcod.picturesaver.interfaces.ConfigInput;
import fr.mrcraftcod.picturesaver.jfx.components.StringInput;
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

public class SettingsStage extends Stage
{
	private SimpleBooleanProperty updated;
	private ArrayList<ConfigInput> saveFunctions;

	public SettingsStage(Stage parentStage)
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

		StringInput fetchEmailUser = createTextInput(ConfigKey.EMAIL_FETCH_MAIL, "Fetching email:");
		StringInput fetchEmailPassword = createTextInput(ConfigKey.EMAIL_FETCH_PASSWORD, "Email password:");

		this.updated.bind(fetchEmailUser.updatedProperty().or(fetchEmailPassword.updatedProperty()));

		HBox controls = createControls();

		root.getChildren().addAll(fetchEmailUser, fetchEmailPassword, controls);
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

	private StringInput createTextInput(ConfigKey<String> configKey,  String description)
	{
		StringInput root = new StringInput(configKey, description);
		this.saveFunctions.add(root);
		return root;
	}
}

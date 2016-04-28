package fr.mrcraftcod.picturesaver.jfx;

import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import fr.mrcraftcod.picturesaver.jfx.components.SettingStageBase;
import fr.mrcraftcod.picturesaver.jfx.components.inputs.BooleanInput;
import fr.mrcraftcod.picturesaver.jfx.components.inputs.StringInput;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.Collection;
import java.util.LinkedList;

public class SettingsStage extends SettingStageBase
{
	public SettingsStage(Stage parentStage)
	{
		super(parentStage);
	}

	@Override
	protected Collection<? extends Node> getComponents()
	{
		LinkedList<Node> nodes = new LinkedList<>();

		BooleanInput fetchEmailStatus = createBooleanInput(ConfigKey.EMAIL_FETCH_STATUS, "Fetch emails?");
		StringInput fetchEmailUser = createTextInput(ConfigKey.EMAIL_FETCH_MAIL, "Fetching email:");
		StringInput fetchEmailPassword = createTextInput(ConfigKey.EMAIL_FETCH_PASSWORD, "Email password:");
		Button folderOutputs = new Button("Folder outputs");
		folderOutputs.setMaxWidth(Double.MAX_VALUE);
		folderOutputs.setOnAction(evt -> new OutputFoldersStage(this).show());

		nodes.add(fetchEmailStatus);
		nodes.add(fetchEmailUser);
		nodes.add(fetchEmailPassword);
		nodes.add(folderOutputs);

		this.updatedProperty().bind(fetchEmailUser.updatedProperty().or(fetchEmailPassword.updatedProperty()).or(fetchEmailStatus.updatedProperty()));

		return nodes;
	}
}

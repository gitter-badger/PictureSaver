package fr.mrcraftcod.picturesaver.jfx;

import fr.mrcraftcod.picturesaver.enums.Origins;
import fr.mrcraftcod.picturesaver.jfx.components.SettingStageBase;
import fr.mrcraftcod.picturesaver.jfx.components.inputs.BooleanInput;
import fr.mrcraftcod.picturesaver.jfx.components.inputs.FileInput;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collection;

public class OutputFoldersStage extends SettingStageBase
{
	public OutputFoldersStage(Stage parentStage)
	{
		super(parentStage);
	}

	@Override
	protected Collection<? extends Node> getComponents()
	{
		ArrayList<Node> components = new ArrayList<>();

		BooleanBinding update = null;

		for(Origins origin : Origins.values())
		{
			HBox line = new HBox();
			BooleanInput activated = createBooleanInput(origin.getActivatedKey(), "");
			FileInput file = createFileInput(origin.getOutputFolderKey(), origin.name());

			if(update == null)
				update = file.updatedProperty().and(new SimpleBooleanProperty(true));
			else
				update = update.and(file.updatedProperty());
			update = update.and(activated.updatedProperty());

			line.getChildren().addAll(activated, file);
			components.add(line);
		}

		if(update != null)
			this.updatedProperty().bind(update);

		return components;
	}
}

package fr.mrcraftcod.picturesaver.jfx;

import fr.mrcraftcod.picturesaver.enums.Origins;
import fr.mrcraftcod.picturesaver.jfx.components.SettingStageBase;
import fr.mrcraftcod.picturesaver.jfx.components.inputs.FileInput;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
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
		ArrayList<FileInput> files = new ArrayList<>();

		for(Origins origin : Origins.values())
		{
			FileInput file = createFileInput(origin.getOutputFolderKey(), origin.name());
			files.add(file);
		}

		BooleanBinding update = null;

		for(FileInput file : files)
		{
			if(update == null)
				update = file.updatedProperty().and(new SimpleBooleanProperty(true));
			else
				update = update.and(file.updatedProperty());
		}

		if(update != null)
			this.updatedProperty().bind(update);

		return files;
	}
}

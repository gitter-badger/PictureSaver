package fr.mrcraftcod.picturesaver.jfx.components.inputs;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import fr.mrcraftcod.picturesaver.interfaces.ConfigInput;
import fr.mrcraftcod.utils.FileUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.File;

public class FileInput extends HBox implements ConfigInput<File>
{
	private final Button input;
	private final ConfigKey<File> configKey;
	private final Stage parent;
	private SimpleBooleanProperty updated;

	public FileInput(Stage parent, ConfigKey<File> configKey, String description)
	{
		super();
		this.parent = parent;
		this.configKey = configKey;
		this.updated = new SimpleBooleanProperty(false);
		Label label = new Label(description);
		this.input = new Button();
		Constants.configuration.getFileValue(configKey, file -> input.setText(file.getAbsolutePath()), null);
		this.input.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				updated.set(true);
			}
		});
		this.input.setOnAction(evt -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("Open output folder");
			directoryChooser.setInitialDirectory(getFile());
			File file = directoryChooser.showDialog(this.parent);
			if(file != null)
				this.input.setText(file.getAbsolutePath());
		});
		this.getChildren().addAll(label, this.input);
	}

	@Override
	public Pair<ConfigKey<File>, File> getValue()
	{
		return new Pair<>(this.configKey, getFile());
	}

	public SimpleBooleanProperty updatedProperty()
	{
		return this.updated;
	}

	public File getFile()
	{
		return this.input.getText().equals("") ? FileUtils.getHomeFolder() : new File(this.input.getText());
	}
}

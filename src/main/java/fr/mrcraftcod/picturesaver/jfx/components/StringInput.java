package fr.mrcraftcod.picturesaver.jfx.components;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import fr.mrcraftcod.picturesaver.interfaces.ConfigInput;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

public class StringInput extends HBox implements ConfigInput<String>
{
	private final TextArea input;
	private final ConfigKey<String> configKey;
	private SimpleBooleanProperty updated;

	public StringInput(ConfigKey<String> configKey, String description)
	{
		super();
		this.configKey = configKey;
		this.updated = new SimpleBooleanProperty(false);
		Label label = new Label(description);
		this.input = new TextArea();
		Constants.configuration.getStringValue(configKey, input::setText, null);
		this.input.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				updated.set(true);
			}
		});
		this.getChildren().addAll(label, this.input);
	}

	@Override
	public Pair<ConfigKey<String>, String> getValue()
	{
		return new Pair<>(this.configKey, this.input.getText());
	}

	public SimpleBooleanProperty updatedProperty()
	{
		return this.updated;
	}
}

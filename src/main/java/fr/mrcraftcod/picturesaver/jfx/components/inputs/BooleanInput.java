package fr.mrcraftcod.picturesaver.jfx.components.inputs;

import fr.mrcraftcod.picturesaver.Constants;
import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import fr.mrcraftcod.picturesaver.interfaces.ConfigInput;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

public class BooleanInput extends HBox implements ConfigInput<Boolean>
{
	private final CheckBox input;
	private final ConfigKey<Boolean> configKey;
	private SimpleBooleanProperty updated;

	public BooleanInput(ConfigKey<Boolean> configKey, String description)
	{
		super();
		this.configKey = configKey;
		this.updated = new SimpleBooleanProperty(false);
		Label label = new Label(description);
		this.input = new CheckBox();
		Constants.configuration.getBooleanValue(configKey, input::setSelected, null);
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
	public Pair<ConfigKey<Boolean>, Boolean> getValue()
	{
		return new Pair<>(this.configKey, this.input.isSelected());
	}

	public SimpleBooleanProperty updatedProperty()
	{
		return this.updated;
	}
}

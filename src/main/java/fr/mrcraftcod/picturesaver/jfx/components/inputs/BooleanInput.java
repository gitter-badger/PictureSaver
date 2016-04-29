package fr.mrcraftcod.picturesaver.jfx.components.inputs;

import fr.mrcraftcod.picturesaver.interfaces.ConfigInput;
import fr.mrcraftcod.picturesaver.objects.ConfigValue;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class BooleanInput extends HBox implements ConfigInput<Boolean>
{
	private final CheckBox input;
	private final ConfigValue<Boolean> configValue;
	private SimpleBooleanProperty updated;

	public BooleanInput(ConfigValue<Boolean> configValue, String description)
	{
		super();
		this.configValue = configValue;
		this.updated = new SimpleBooleanProperty(false);
		Label label = new Label(description);
		this.input = new CheckBox();
		input.setSelected(configValue.getBooleanValue());
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
	public ConfigValue<Boolean> getValue()
	{
		this.configValue.setValue(this.input.isSelected());
		return this.configValue;
	}

	public SimpleBooleanProperty updatedProperty()
	{
		return this.updated;
	}
}

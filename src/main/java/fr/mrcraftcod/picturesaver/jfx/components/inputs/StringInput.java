package fr.mrcraftcod.picturesaver.jfx.components.inputs;

import fr.mrcraftcod.picturesaver.interfaces.ConfigInput;
import fr.mrcraftcod.picturesaver.objects.ConfigValue;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class StringInput extends HBox implements ConfigInput<String>
{
	private final TextArea input;
	private final ConfigValue<String> configValue;
	private SimpleBooleanProperty updated;

	public StringInput(ConfigValue<String> configValue, String description)
	{
		super();
		this.configValue = configValue;
		this.updated = new SimpleBooleanProperty(false);
		Label label = new Label(description);
		this.input = new TextArea();
		input.setText(configValue.getStringValue());
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
	public ConfigValue<String> getValue()
	{
		configValue.setValue(this.input.getText());
		return this.configValue;
	}

	public SimpleBooleanProperty updatedProperty()
	{
		return this.updated;
	}
}

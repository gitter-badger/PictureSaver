package fr.mrcraftcod.picturesaver.jfx.components.table;

import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.ProgressBar;

public class ProgressBarMax extends ProgressBar
{
	public ProgressBarMax(SimpleLongProperty progress, SimpleLongProperty max)
	{
		super(progress.get());
		this.progressProperty().bind(progress.divide(max.add(0D)));
	}
}

package fr.mrcraftcod.picturesaver.jfx.components.table;

import javafx.scene.control.ProgressBar;

public class ProgressBarMax extends ProgressBar
{
	private double max;

	public ProgressBarMax()
	{
		super();
		this.max = 1;
	}

	public ProgressBarMax(double progress)
	{
		super(progress);
		this.max = 1;
	}

	public ProgressBarMax(double progress, double max)
	{
		super(progress);
		this.max = max;
	}

	public void setMax(double max)
	{
		this.max = max;
	}

	public void setValue(double progress)
	{
		this.setProgress(progress / this.getMax());
	}

	public double getMax()
	{
		return this.max;
	}
}

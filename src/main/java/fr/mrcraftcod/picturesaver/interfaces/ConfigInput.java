package fr.mrcraftcod.picturesaver.interfaces;

import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import javafx.util.Pair;

public interface ConfigInput<T>
{
	public Pair<ConfigKey<T>, T> getValue();
}

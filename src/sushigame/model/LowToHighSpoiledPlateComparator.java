package sushigame.model;

import java.util.Comparator;

public class LowToHighSpoiledPlateComparator implements Comparator<Chef>{

	@Override
	public int compare(Chef a, Chef b) {
		return a.getSpoiled() - b.getSpoiled();
	}

}

package sushigame.model;

import java.util.Comparator;

public class HighToLowSoldSushiConsumedComparator implements Comparator<Chef>{

	@Override
	public int compare(Chef a, Chef b) {
		return b.getAmountConsumed()-a.getAmountConsumed();
	}
	

}

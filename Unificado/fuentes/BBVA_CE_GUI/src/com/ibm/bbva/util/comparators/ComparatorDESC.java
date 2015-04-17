package com.ibm.bbva.util.comparators;

public class ComparatorDESC<T> extends ComparatorASC {

	@Override
	public int compare(Comparable val1, Comparable val2) {
		return super.compare(val2, val1);
	}

}

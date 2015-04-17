package com.ibm.bbva.util.comparators;

import java.util.Comparator;

public class ComparatorASC<T extends Comparable<T>> implements Comparator<T> {

	public int compare(T val1, T val2) {
		if (val1 == null && val2 == null) {
			return 0;
		}
		if (val1 == null) {
			return -1;
		}
		if (val2 == null) {
			return 1;
		}
		return val1.compareTo(val2);
	}

}

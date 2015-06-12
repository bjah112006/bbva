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
		if (val1 instanceof String && val2 instanceof String) {
			return ((String) val1).compareToIgnoreCase((String) val2);
		} else {
			return val1.compareTo(val2);
		}
	}

}

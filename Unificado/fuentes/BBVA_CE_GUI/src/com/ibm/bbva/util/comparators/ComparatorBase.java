package com.ibm.bbva.util.comparators;

import java.util.Comparator;

public abstract class ComparatorBase<T extends Comparable<T>, E> implements Comparator<E> {

	protected Comparator<T> comparator;
	public static String SORT_ORDER_ASC = "ASC";
	public static String SORT_ORDER_DESC = "DESC";
	
	public ComparatorBase (String orden) {
		if (SORT_ORDER_DESC.equals(orden)) {
			comparator = new ComparatorDESC ();
		} else {
			comparator = new ComparatorASC ();
		}
	}
	
}

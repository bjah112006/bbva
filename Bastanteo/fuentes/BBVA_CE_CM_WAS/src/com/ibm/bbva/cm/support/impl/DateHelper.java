package com.ibm.bbva.cm.support.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateHelper {
	
	
	private DateHelper() {
		// implementacion vacia
	}
	
	/**
	 * Transforma un Timestamp a Calendar
	 * 
	 * @param timestamp fecha/hora en Timestamp
	 * @return fecha/hora en Calendar
	 */
	public static Calendar toCalendar(Timestamp timestamp) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTimeInMillis(timestamp.getTime());
		
		return calendar;
	}
	
	/**
	 * Transforma un Calendar a Timestamp
	 * 
	 * @param calendar fecha/hora en Calendar
	 * @return fecha/hora en Timestamp
	 */
	public static Timestamp toTimestamp(Calendar calendar) {
		Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
		
		return timestamp;
	}	
}

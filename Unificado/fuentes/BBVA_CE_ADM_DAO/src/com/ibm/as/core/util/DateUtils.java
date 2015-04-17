package com.ibm.as.core.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	//format AAAAmmdd
	public static Date parseDate(String s){
		if (s == null)
			return null;
		
		try {
			return new SimpleDateFormat("yyyyMMdd").parse(s);
		}
		catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDate(String date, String format) {
		if (date == null)
			return null;
		
		try {
			return new SimpleDateFormat(format).parse(date);
		}
		catch (ParseException e) {
			return null;
		}
	}

	public static String formatDate(Date d, String format) {
		if ( d==null || format==null)
			return null;

		return new SimpleDateFormat(format).format(d); 
	}

	public static Date truncateDate(Date d) {

		if (d==null)
			return null;
		
		Calendar c = Calendar.getInstance();
		Calendar nc = Calendar.getInstance();
		nc.clear();

		c.setTime(d);

		nc.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));
		nc.set(Calendar.YEAR, c.get(Calendar.YEAR));

		return nc.getTime();
	}
	
	public static int compareDate(Timestamp t1, Timestamp t2) {

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date(t1.getTime()));
		cal1.set(Calendar.HOUR, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(new Date(t2.getTime()));
		cal2.set(Calendar.HOUR, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
  
		return cal1.compareTo(cal2);
	}
	
	public static Timestamp addDay(Timestamp t) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(t.getTime()));
		cal.add(cal.DATE, 1);
		
		Date d = cal.getTime();		
		Timestamp timestamp = new Timestamp(d.getTime());
		
		return timestamp;		
	}
	
	public static int dayOfWeek(Timestamp t) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(t.getTime());			
		return cal.get(Calendar.DAY_OF_WEEK);		
	}
}
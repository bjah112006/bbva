package com.ibm.bbva.ctacte.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class dddd {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date fechaIni =  new Date();
		
		
		Date truncatedDate = DateUtils.truncate(fechaIni, Calendar.DATE);
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(truncatedDate); 
		c.add(Calendar.DATE, 0);
		Date fechaFin = c.getTime();
		
		Date truncatedDateFinal = DateUtils.truncate(fechaFin, Calendar.DATE);
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(truncatedDate);
		
		System.out.println("Fecha inicial:" + df.format(truncatedDate));
		System.out.println("Fecha Final:" + df.format(truncatedDateFinal));
		while (cal.getTime().before(truncatedDateFinal)) {
			cal.add(Calendar.DATE, 1);
			System.out.println(df.format(cal.getTime()));
		}
		
		
		
//		String html= "<table> <tr> <td style=\"text-align\"> jose </td> </tr> </table>";
//		String output = html2text(html);
//		System.out.println(output);
		
	}
	
//	public static String html2text(String html) {
//	    return Jsoup.parse(html).text();
//	}

}

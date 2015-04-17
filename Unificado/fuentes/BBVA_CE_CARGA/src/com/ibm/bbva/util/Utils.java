package com.ibm.bbva.util;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;


public class Utils {
	
	public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar calendar){
        if(calendar == null) {
            return null;
        }
        return calendar.toGregorianCalendar().getTime();
    }
	

}

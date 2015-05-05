package com.ibm.bbva.cm.support.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(DateHelper.class);
	
	private DateHelper() {
	}
	
	/**
	 * Transforma un Timestamp a Calendar
	 * 
	 * @param timestamp fecha/hora en Timestamp
	 * @return fecha/hora en Calendar
	 */
	public static Calendar toCalendar(Timestamp timestamp) {
		Calendar calendar = null;
		if(timestamp != null) {
			calendar = GregorianCalendar.getInstance();
			calendar.setTimeInMillis(timestamp.getTime());
		}
		return calendar;
	}
	
	public static Calendar toCalendar(XMLGregorianCalendar timestamp) {
		Calendar calendar = null;
		if(timestamp != null) {
			calendar = timestamp.toGregorianCalendar();
		}
		return calendar;
	}
	
	public static XMLGregorianCalendar toXMLGregorianCalendar(Calendar calendar){
		GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(calendar.getTime());
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
        	logger.error("", ex);
        }
        return xmlCalendar;
    }
	
	public static XMLGregorianCalendar toXMLGregorianCalendar(Date calendar){
		GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(calendar);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
        	logger.error("", ex);
        }
        return xmlCalendar;
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

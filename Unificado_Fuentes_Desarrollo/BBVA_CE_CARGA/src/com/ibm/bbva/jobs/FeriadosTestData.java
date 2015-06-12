package com.ibm.bbva.jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.bbva.general.entities.Feriado;

public class FeriadosTestData {
	
	public static List<Feriado> getFeriadoListado(){
		
		List<Feriado> feriados = new ArrayList<Feriado>();
		
		/**
		 * @author Daniel Flores
		 * Se crea solo un feriado de ejemplo
		 * con el objetivo de que este sea copiado en la BD.
		 */
		Feriado feriado = new Feriado();
		
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date());
		XMLGregorianCalendar date = null;
		
		try {
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		System.out.println("FECHA ====> " + date.toXMLFormat());
		System.out.println("FECHA TO STRING ====> " + date.toString());
		
		// Feriado 1
		
		feriado.setFecha(date);
		feriado.setIndicador("N");
		feriado.setDescripcion("Feriado");
		feriado.setUbigeo("1307000");
		
		feriados.add(feriado);
		
		// Feriado 2
		
		Feriado feriado2 = new Feriado();
		
		Calendar fechaFeriadoCalendar = Calendar.getInstance();
		fechaFeriadoCalendar.setTime(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(
				fechaFeriadoCalendar.get(Calendar.YEAR), 
				fechaFeriadoCalendar.get(Calendar.MONTH), 
				fechaFeriadoCalendar.get(Calendar.DAY_OF_MONTH),
				0,
				0, 
				0
		);
		
		XMLGregorianCalendar date2 = null;
		GregorianCalendar c2 = new GregorianCalendar();
		c2.setTime(cal.getTime());
		
		try {
			date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c2);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		// Estableciendo fecha sin horas ni minutos
		feriado2.setFecha(date2);
		feriado2.setIndicador("N");
		feriado2.setDescripcion("Feriado");
		feriado2.setUbigeo(null);
		
		feriados.add(feriado2);
		
		return feriados;
		
	}
}

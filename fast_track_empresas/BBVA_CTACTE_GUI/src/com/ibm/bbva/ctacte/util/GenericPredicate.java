package com.ibm.bbva.ctacte.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericPredicate<T> implements Predicate{
	
	private static final Logger LOG = LoggerFactory.getLogger(GenericPredicate.class);
	
	private T criterio;

	public GenericPredicate(T criterio) {
		this.criterio = criterio;		
	}
	public boolean evaluate(Object obj) {
		boolean success = true;
		try {
			Map<String, Object> propiedades = PropertyUtils.describe(obj);
			
			Iterator<Entry<String,Object>> it = propiedades.entrySet().iterator();			
			while (it.hasNext()) {				
				Map.Entry<String,Object> e = it.next();			
				Object valorPropiedad = PropertyUtils.getProperty(criterio, e.getKey());
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("e.getKey() : "+e.getKey());
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("valorPropiedad : "+valorPropiedad);
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("e.getValue() : "+e.getValue());
				if(valorPropiedad != null){
					if(valorPropiedad instanceof String){
						if(!valorPropiedad.toString().equals("")){
							if (e.getValue() !=null) {									
							   success = success && e.getValue().toString().contains(valorPropiedad.toString());
							   //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("success : "+success);
							}else{
								success = false;
							}
							//success = valorPropiedad.toString().contains(e.getValue().toString());
						}else{
							//success = success || false;							
						}
					}else{
						//success = success &&(e.getValue().equals(valorPropiedad));
					}															
				}else{
					//success = success || false;
				} 
			}
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			LOG.error(e.getMessage(), e);
		}
		return success;
	}


}

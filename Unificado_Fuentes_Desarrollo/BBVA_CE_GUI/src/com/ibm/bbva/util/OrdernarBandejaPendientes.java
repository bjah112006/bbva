package com.ibm.bbva.util;

import java.sql.Timestamp;
import java.util.Comparator;
	
public class OrdernarBandejaPendientes implements Comparator<ExpedienteTCWrapper> {
		
	boolean order = false;
	boolean prioridad = true;

	/**
	 * @param Ordena una Lista true = Desc, false = Asc
	 */
	public OrdernarBandejaPendientes(boolean order, boolean prioridad) {		
		this.order = order;
		this.prioridad = prioridad;
	}
	
	public int compare(ExpedienteTCWrapper s1, ExpedienteTCWrapper s2) {
		if (prioridad)
			return ordenarPorTipoCliente(s1, s2);
		else
			return ordenarPorFechaHora(s1, s2);
		
	}
	
	/*
	public int ordenarBandejaAsignados(ExpedienteTCWrapper s1, ExpedienteTCWrapper s2) {
		//LOG.info("Ordenando Tabla Bandeja Asignados");
    	if (order) {
            if (s2.getOrden().compareTo(s1.getOrden())==0) {
            	if(s1.getActivado() == null && s2.getActivado() == null)
    				return 0;
    			if(s1.getActivado() == null)
    				return -1;
    			if(s2.getActivado() == null)
    				return 1;
            	return s2.getActivado().compareTo(s1.getActivado());
            }
            return s2.getOrden().compareTo(s1.getOrden());
    	}else{
    		if (s1.getOrden().compareTo(s2.getOrden())==0) {
    			if(s1.getActivado() == null && s2.getActivado() == null)
    				return 0;
    			if(s1.getActivado() == null)
    				return -1;
    			if(s2.getActivado() == null)
    				return 1;
            	return s1.getActivado().compareTo(s2.getActivado());
            }
    		return s1.getOrden().compareTo(s2.getOrden());
    	}
    }
    */
	
    public int ordenarPorTipoCliente(ExpedienteTCWrapper s1, ExpedienteTCWrapper s2) {
    	//LOG.info("Ordenando Tabla Bandeja Pendientes X Tipo Cliente");
    	Integer order1 = s1.getOrden(s1.getCliente().getCodigoTipoCliente());
		Integer order2 = s2.getOrden(s2.getCliente().getCodigoTipoCliente());
    	if (order) {
    		return order2.compareTo(order1);
    	}else{
    		return order1.compareTo(order2);
    	}
    }
    
    public int ordenarPorFechaHora(ExpedienteTCWrapper s1, ExpedienteTCWrapper s2) {
    	//LOG.info("Ordenando Tabla Bandeja Pendientes X Fecha y Hora");
    	Integer order1 = s1.getOrden(s1.getCliente().getCodigoTipoCliente());
		Integer order2 = s2.getOrden(s2.getCliente().getCodigoTipoCliente());
    	Timestamp fecha1 = s1.getFechaActivado();
    	Timestamp fecha2 = s2.getFechaActivado();
    	if (order) {
    		if (order1 == order2)
    			return fecha2.compareTo(fecha1);
    		else
    			return 0;
    	} else {
    		if (order1 == order2)
    			return fecha1.compareTo(fecha2);
    		else
    			return 0;
    	}
    }
    
}

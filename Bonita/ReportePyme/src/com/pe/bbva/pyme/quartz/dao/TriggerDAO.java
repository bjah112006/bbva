package com.pe.bbva.pyme.quartz.dao;

import java.util.List;

import com.pe.bbva.pyme.quartz.domain.Trigger;

/**
 * Interfaz para acceder a los datos de los trabajos configurados
 * 
 * @author jquedena
 * 
 */
public interface TriggerDAO {

    long obtainLastExecution(String jobName);
    
    /**
     * Lista los trabajos configurados
     * 
     * @return La lista de trabajos configurados
     */
    List<Trigger> listar();
}

package com.pe.bbva.pyme.batch.service;

import java.util.Date;
import java.util.List;

import com.pe.bbva.pyme.quartz.domain.Trigger;

/**
 * Servicio para el acceso y control de ejecuci\u00F3n de los trabajos
 * @author jquedena
 *
 */
public interface JobInstanceService {

    /**
     * Ejecuta un trabajo
     * @param jobName, nombre del trabajo
     */
    void execute(String jobName);
    
    /**
     * Ejecuta un trabajo
     * @param jobName, nombre del trabajo
     * @param date, fecha a procesar
     */
    void execute(String jobName, Date date);
    
    List<Trigger> listTrigger();
    
    long obtainLastExecution(String jobName);
}

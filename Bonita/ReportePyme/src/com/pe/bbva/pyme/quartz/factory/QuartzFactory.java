package com.pe.bbva.pyme.quartz.factory;

import org.quartz.Job;

import com.pe.bbva.pyme.quartz.domain.JobConfig;

/**
 * F\u00E1brica de trabajos
 * 
 * @author jquedena
 *
 */
public interface QuartzFactory {

    /**
     * Crea las instancias Quartz para la ejecuci\u00F3n del trabajo seg\u00FAn
     * la programaci\u00F3n
     * 
     * @param jobBatch, trabajo a crear
     */
    void createJob(JobConfig jobBatch,  Class<? extends Job> clazz) throws Exception;

    void rescheduler(JobConfig jobBatch, Class<? extends Job> clazz) throws Exception;
    
    void deleteJob(JobConfig jobBatch) throws Exception;
}
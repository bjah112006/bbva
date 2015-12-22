package com.pe.bbva.pyme.batch.monitor;

import javax.annotation.Resource;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import com.pe.bbva.pyme.batch.service.NotificacionService;

/**
 * Oyente de las peticiones de ejecuci\u00F3n de los trabajos
 * 
 * @author jquedena
 *
 */
@Component("monitoringExecutionListener")
public class MonitoringExecutionListener implements JobExecutionListener {

    /**
     * Identificador del trabajo que se ejecuta
     */
    private Long idJob;

    @Resource(name = "notificacionService")
    private NotificacionService notificacionService;
    
    /**
     * Asigna el identificador del trabajo a la variable idJob,
     * este m\u00E9todo se ejecuta antes de que se inicie el trabajo

     * @param jobExecution, trabajo que se va ha ejecutar
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        idJob = jobExecution.getJobId();
    }

    /**
     * Mediante el uso de la interfaz JobMonitoringNotifier
     * realiza la notificaci\u00F3n del termino del trabajo
     * 
     * @param jobExecution, trabajo que se ejecuto
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        notificacionService.notificar(jobExecution);
    }

    public Long getIdJob() {
        return idJob;
    }
}

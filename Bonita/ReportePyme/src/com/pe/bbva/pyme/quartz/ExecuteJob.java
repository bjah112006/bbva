package com.pe.bbva.pyme.quartz;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.pe.bbva.pyme.batch.service.JobInstanceService;
import com.pe.bbva.pyme.listener.WFFastPymeServletContextListener;
import com.pe.bbva.pyme.utils.DBUtil;

public class ExecuteJob extends QuartzJobBean {

    private static final Logger LOG = Logger.getLogger(ExecuteJob.class);
    private String idAplicacion;
    private String nombreAplicacion;
    private String jobName;

    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Ejecutando: [" + idAplicacion + "]-[" + nombreAplicacion + "]-[" + jobName + "]");
        String activarJob = DBUtil.obtenerParametroDetalle("18", "004");
        if("0".equalsIgnoreCase(activarJob)) {
            LOG.error("Proceso inactivado");
            return;
        }
        
        try {
            JobInstanceService jobInstanceService = WFFastPymeServletContextListener.getBean("jobInstanceService");
            jobInstanceService.execute(jobName);
        } catch (Exception e) {
            LOG.error("Error ejecutando tarea principal:", e);
        }
    }
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(String idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    public void setNombreAplicacion(String nombreAplicacion) {
        this.nombreAplicacion = nombreAplicacion;
    }
}

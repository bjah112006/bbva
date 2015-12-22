package com.pe.bbva.pyme.controller.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

import com.pe.bbva.pyme.quartz.domain.Trigger;

public class SchedulerModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<JobExecution> runningJobInstances;
    private List<Trigger> triggerInstances;
    private String cronTrigger;
    private JobInstance jobInstance;
    private String time;
    private Resultado tipoResultado;
    private String mensaje;

    public Resultado getTipoResultado() {
        return tipoResultado;
    }

    public void setTipoResultado(Resultado tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<JobExecution> getRunningJobInstances() {
        if (runningJobInstances == null) {
            runningJobInstances = new ArrayList<JobExecution>();
        }
        return runningJobInstances;
    }

    public void setRunningJobInstances(List<JobExecution> runningJobInstances) {
        this.runningJobInstances = runningJobInstances;
    }

    public List<Trigger> getTriggerInstances() {
        return triggerInstances;
    }

    public void setTriggerInstances(List<Trigger> triggerInstances) {
        this.triggerInstances = triggerInstances;
    }

    public String getCronTrigger() {
        return cronTrigger;
    }

    public void setCronTrigger(String cronTrigger) {
        this.cronTrigger = cronTrigger;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public JobInstance getJobInstance() {
        return jobInstance;
    }

    public void setJobInstance(JobInstance jobInstance) {
        this.jobInstance = jobInstance;
    }
}

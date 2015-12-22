package com.pe.bbva.pyme.quartz.domain;

import java.io.Serializable;

public class JobConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private String cronExpresion;
    private String groupName;
    private String jobName;
    private String codigoRegistro;

    public JobConfig() {
        super();
    }

    public String getCronExpresion() {
        return cronExpresion;
    }

    public void setCronExpresion(String cronExpresion) {
        this.cronExpresion = cronExpresion;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(String codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }
}

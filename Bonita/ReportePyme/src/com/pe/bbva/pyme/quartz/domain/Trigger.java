package com.pe.bbva.pyme.quartz.domain;

import java.io.Serializable;

public class Trigger implements Serializable {

    private static final long serialVersionUID = 1L;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private Long nextFireTime;
    private String triggerState;
    private String triggerType;
    private Long startTime;
    private Long endTime;
    private String exitCode;
    
    public String getTriggerName() {
        return triggerName;
    }

    public Trigger setTriggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public Trigger setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public Trigger setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public Trigger setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
        return this;
    }

    public Long getNextFireTime() {
        return nextFireTime;
    }

    public Trigger setNextFireTime(Long nextFireTime) {
        this.nextFireTime = nextFireTime;
        return this;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public Trigger setTriggerState(String triggerState) {
        this.triggerState = triggerState;
        return this;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public Trigger setTriggerType(String triggerType) {
        this.triggerType = triggerType;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Trigger setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Trigger setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getExitCode() {
        return exitCode;
    }

    public Trigger setExitCode(String exitCode) {
        this.exitCode = exitCode;
        return this;
    }

}

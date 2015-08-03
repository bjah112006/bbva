package com.ibm.bbva.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="TBL_CE_IBM_LOG_JOB_DET", schema = "CONELE")
@NamedQuery(name = "LogJobDet.findById", query = "SELECT d FROM LogJobDet d WHERE d.id = :id")
public class LogJobDet implements Serializable 
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TBL_CE_IBM_LOG_JOB_DET_ID_GENERATOR", sequenceName="SEQ_CE_IBM_LOG_JOB_DET", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_LOG_JOB_DET_ID_GENERATOR")	
	private long id;
	
	@ManyToOne
	@JoinColumn(name="ID_LOG_JOB")
	private LogJob logJob;
	
	@Column(name="NOMBRE")
	private String nombre;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_INICIO")
	private Date fechaInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_FIN")
	private Date fechaFin;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LogJob getLogJob() {
		return logJob;
	}

	public void setLogJob(LogJob logJob) {
		this.logJob = logJob;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
		
}

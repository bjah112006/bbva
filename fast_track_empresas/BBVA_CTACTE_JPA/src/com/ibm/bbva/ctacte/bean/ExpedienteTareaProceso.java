package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_EXPTAR_PROCESO_CC", schema = "CONELE")
public class ExpedienteTareaProceso implements java.io.Serializable {
	
	private static final long serialVersionUID = 1183649652213514188L;

	@Id
	@SequenceGenerator(name = "seqExTarProc", sequenceName = "SEQ_CE_IBM_EXPTAR_PROCESO_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqExTarProc")
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	private Integer id;
	
	@Column(name = "ID_EXPEDIENTE_FK", unique = false, nullable = false, precision = 10, scale = 0)
	private Integer idExpediente;
	
	@Column(name = "ID_EMPLEADO_FK", unique = false, nullable = false, precision = 10, scale = 0)
	private Integer idEmpleado;
	
	@Column(name = "ID_TAREA_FK", unique = false, nullable = false, precision = 5, scale = 0)
	private Integer idTarea;
	
	public ExpedienteTareaProceso() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdExpediente() {
		return idExpediente;
	}

	public void setIdExpediente(Integer idExpediente) {
		this.idExpediente = idExpediente;
	}

	public Integer getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Integer idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public Integer getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(Integer idTarea) {
		this.idTarea = idTarea;
	}
	
}

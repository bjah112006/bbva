package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_EXPEDIENTE_TAREA_CC", schema = "CONELE")
public class ExpedienteTarea implements Serializable {

	private static final long serialVersionUID = -9075920036016309852L;

	@Id
	@SequenceGenerator(name = "seqTareaExpediente", sequenceName = "SEQ_CE_IBM_EXPEDIENTE_TAREA_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqTareaExpediente")
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_EXPEDIENTE_FK")
	private Expediente expediente;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ESTADO_TAREA_FK")
	private EstadoTarea estadoTarea;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_TAREA_FK")
	private Tarea tarea;

	public ExpedienteTarea() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public EstadoTarea getEstadoTarea() {
		return estadoTarea;
	}

	public void setEstadoTarea(EstadoTarea estadoTarea) {
		this.estadoTarea = estadoTarea;
	}

	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}
	
}

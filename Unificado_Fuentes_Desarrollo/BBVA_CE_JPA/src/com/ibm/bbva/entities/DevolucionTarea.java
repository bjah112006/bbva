package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_DEVOLUCION_TAREA database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_DEVOLUCION_TAREA", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="DevolucionTarea.findAll", query="SELECT d FROM DevolucionTarea d"),
	@NamedQuery(name="DevolucionTarea.findbyIdExpedienteIdTarea", query="SELECT d FROM DevolucionTarea d WHERE d.expediente.id=:idExpediente AND d.tarea.id=:idTarea ")
})
public class DevolucionTarea implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_DEVOLUCION_TAREA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_DEVOLUCION_TAREA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_DEVOLUCION_TAREA_ID_GENERATOR")
	private long id;

	//bi-directional many-to-one association to Expediente
	@ManyToOne
	@JoinColumn(name="ID_EXPEDIENTE_FK")
	private Expediente expediente;

	//uni-directional many-to-one association to MotivoDevolucion
	@ManyToOne
	@JoinColumn(name="ID_MOTIVO_DEVOLUCION_FK")
	private MotivoDevolucion motivoDevolucion;

	//bi-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;

	public DevolucionTarea() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Expediente getExpediente() {
		return this.expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public MotivoDevolucion getMotivoDevolucion() {
		return this.motivoDevolucion;
	}

	public void setMotivoDevolucion(MotivoDevolucion motivoDevolucion) {
		this.motivoDevolucion = motivoDevolucion;
	}

	public Tarea getTarea() {
		return this.tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

}
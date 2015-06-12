package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_CE_IBM_LOG database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_LOG", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="Log.findAll", query="SELECT l FROM Log l"),
	@NamedQuery(name="Log.findByIdExpediente", query="SELECT l FROM Log l WHERE l.expediente.id = :idExpediente order by l.fecRegistro desc")
})
public class Log implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_LOG_ID_GENERATOR", sequenceName="SEQ_CE_IBM_LOG", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_LOG_ID_GENERATOR")
	private long id;

	private String descripcion;

	@Column(name="FEC_REGISTRO")
	private Timestamp fecRegistro;

	@Column(name="NUM_TERMINAL")
	private String numTerminal;

	//uni-directional many-to-one association to Empleado
	@ManyToOne
	@JoinColumn(name="ID_EMPLEADO_FK")
	private Empleado empleado;

	//uni-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO_FK")
	private Estado estado;

	//uni-directional many-to-one association to Expediente
	@ManyToOne
	@JoinColumn(name="EXPEDIENTE_FK")
	private Expediente expediente;

	public Log() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Timestamp getFecRegistro() {
		return this.fecRegistro;
	}

	public void setFecRegistro(Timestamp fecRegistro) {
		this.fecRegistro = fecRegistro;
	}

	public String getNumTerminal() {
		return this.numTerminal;
	}

	public void setNumTerminal(String numTerminal) {
		this.numTerminal = numTerminal;
	}

	public Empleado getEmpleado() {
		return this.empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Expediente getExpediente() {
		return this.expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

}
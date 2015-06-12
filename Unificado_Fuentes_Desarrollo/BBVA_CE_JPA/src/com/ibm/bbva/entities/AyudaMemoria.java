package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_CE_IBM_AYUDA_MEMORIA database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_AYUDA_MEMORIA", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="AyudaMemoria.findAll", query="SELECT a FROM AyudaMemoria a"),
	@NamedQuery(name="AyudaMemoria.findByIdExpediente", query="SELECT a FROM AyudaMemoria a WHERE a.expediente.id = :idExpediente order by a.id asc"),
	@NamedQuery(name="AyudaMemoria.findById", query="SELECT a FROM AyudaMemoria a WHERE a.id = :idAyudaMemoria")
})
public class AyudaMemoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_AYUDA_MEMORIA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_AYUDA_MEMORIA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_AYUDA_MEMORIA_ID_GENERATOR")
	private long id;

	private String comentario;
	@Transient
	private String estado;

	@Column(name="FEC_REGISTRO")
	private Timestamp fecRegistro;

	//bi-directional many-to-one association to Empleado
	@ManyToOne
	@JoinColumn(name="ID_EMPLEADO_FK")
	private Empleado empleado;

	//bi-directional many-to-one association to Expediente
	@ManyToOne
	@JoinColumn(name="ID_EXPEDIENTE_FK")
	private Expediente expediente;

	public AyudaMemoria() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Timestamp getFecRegistro() {
		return this.fecRegistro;
	}

	public void setFecRegistro(Timestamp fecRegistro) {
		this.fecRegistro = fecRegistro;
	}

	public Empleado getEmpleado() {
		return this.empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Expediente getExpediente() {
		return this.expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	
	
	
	
	

}
package com.ibm.bbva.ctacte.bean;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AyudaMemoria generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_AYUDA_MEMORIA_CC", schema = "CONELE")
public class AyudaMemoria implements java.io.Serializable {

	private static final long serialVersionUID = -4265205320130288469L;

	@Id
	@SequenceGenerator(name = "seqAyudaMemoria", sequenceName = "SEQ_CE_IBM_AYUDA_MEMORIA_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqAyudaMemoria")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_EXPEDIENTE_FK")
	private Expediente expediente;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PERFIL_FK")
	private Perfil perfil;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_EMPLEADO_FK")
	private Empleado empleado;
	
	@Column(name = "COMENTARIO", length = 250)
	private String comentario;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_REGISTRO")
	private Date fechaRegistro;

	public AyudaMemoria() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

}
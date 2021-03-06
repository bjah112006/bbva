package com.ibm.bbva.ctacte.bean;
// default package
// Generated 14/05/2012 10:37:52 AM by Hibernate Tools 3.4.0.CR1

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

/**
 * Perfil generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_PERFIL", schema = "CONELE")
public class Perfil implements java.io.Serializable {

	private static final long serialVersionUID = 426369564277608107L;

	@Id
	@SequenceGenerator(name = "seqPerfil", sequenceName = "SEQ_CE_IBM_PERFIL_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqPerfil")
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PERFIL_FK")
	private Perfil perfil;
	
	@Column(name = "CODIGO", nullable = false, length = 5)
	private String codigo;
	
	@Column(name = "DESCRIPCION", nullable = false, length = 60)
	private String descripcion;
	
	@Column(name = "LISTA_CORREO_JEFES", nullable = false, length = 150)
	private String listaCorreoJefes;
	
	@Column(name = "FLAG_REGISTRA_EXP", nullable = false, length = 1)
	private String flagRegistraExp;
	
	@Column(name = "FLAG_ASIGNACION", nullable = false, length = 1)
	private String flagAsignacion;
	
	@Column(name = "FLAG_ADMINISTRACION", nullable = false, length = 1)
	private String flagAdministracion;
	
	@Column(name = "FLAG_REGISTRA_AYU", nullable = false, length = 1)
	private String flagRegistraAyu;
	
	@Column(name = "FLAG_MOSTRAR_INICIALES", nullable = false, length = 1)
	private String flagMostrarIniciales;

	public Perfil() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getListaCorreoJefes() {
		return listaCorreoJefes;
	}

	public void setListaCorreoJefes(String listaCorreoJefes) {
		this.listaCorreoJefes = listaCorreoJefes;
	}

	public String getFlagRegistraExp() {
		return flagRegistraExp;
	}

	public void setFlagRegistraExp(String flagRegistraExp) {
		this.flagRegistraExp = flagRegistraExp;
	}

	public String getFlagAsignacion() {
		return flagAsignacion;
	}

	public void setFlagAsignacion(String flagAsignacion) {
		this.flagAsignacion = flagAsignacion;
	}

	public String getFlagAdministracion() {
		return flagAdministracion;
	}

	public void setFlagAdministracion(String flagAdministracion) {
		this.flagAdministracion = flagAdministracion;
	}

	public String getFlagRegistraAyu() {
		return flagRegistraAyu;
	}

	public void setFlagRegistraAyu(String flagRegistraAyu) {
		this.flagRegistraAyu = flagRegistraAyu;
	}

	public String getFlagMostrarIniciales() {
		return flagMostrarIniciales;
	}

	public void setFlagMostrarIniciales(String flagMostrarIniciales) {
		this.flagMostrarIniciales = flagMostrarIniciales;
	}
	
}

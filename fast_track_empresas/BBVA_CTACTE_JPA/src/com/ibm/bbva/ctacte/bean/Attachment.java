package com.ibm.bbva.ctacte.bean;
// default package
// Generated 17/05/2012 03:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name = "TBL_CE_IBM_ATTACHMENT_CC", schema = "CONELE")
public class Attachment implements java.io.Serializable {
	
	private static final long serialVersionUID = 1678278869359113033L;

	private Long id;
	private Long id_Requerimiento;
	private String nombre_archivo;
	private String ruta_archivo;
	private String usuario_registro;
	private String usuario_actualiza;
	private Date fecha_registro;
	private Date fecha_actualiza;
	private String estado_ref;
	
	@Id
	@SequenceGenerator(name = "seqAttachment", sequenceName = "SEQ_CE_IBM_ATTACHMEN_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqAttachment")
	@Column(name = "ID", unique = true, nullable = false, precision = 36, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "ID_REQUERIMIENTO",nullable = false, precision = 36, scale = 0)
	public Long getId_Requerimiento() {
		return id_Requerimiento;
	}
	public void setId_Requerimiento(Long id_Requerimiento) {
		this.id_Requerimiento = id_Requerimiento;
	}
	@Column(name="NOMBRE_ARCHIVO", length=100)
	public String getNombre_archivo() {
		return nombre_archivo;
	}
	public void setNombre_archivo(String nombre_archivo) {
		this.nombre_archivo = nombre_archivo;
	}
	@Column(name="RUTA_ARCHIVO", length=500)
	public String getRuta_archivo() {
		return ruta_archivo;
	}
	public void setRuta_archivo(String ruta_archivo) {
		this.ruta_archivo = ruta_archivo;
	}
	
	@Column(name="USUARIO_REGISTRO", length=8)
	public String getUsuario_registro() {
		return usuario_registro;
	}
	public void setUsuario_registro(String usuario_registro) {
		this.usuario_registro = usuario_registro;
	}
	@Column(name="USUARIO_ACTUALIZA", length=8)
	public String getUsuario_actualiza() {
		return usuario_actualiza;
	}
	public void setUsuario_actualiza(String usuario_actualiza) {
		this.usuario_actualiza = usuario_actualiza;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_REGISTRO", length = 10)
	public Date getFecha_registro() {
		return fecha_registro;
	}
	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ACTUALIZA", length = 10)
	public Date getFecha_actualiza() {
		return fecha_actualiza;
	}
	public void setFecha_actualiza(Date fecha_actualiza) {
		this.fecha_actualiza = fecha_actualiza;
	}
	
	@Column(name = "ESTADO_REF",length = 30)
	public String getEstado_ref() {
		return estado_ref;
	}
	public void setEstado_ref(String estado_ref) {
		this.estado_ref = estado_ref;
	}
	

	
	
	

}

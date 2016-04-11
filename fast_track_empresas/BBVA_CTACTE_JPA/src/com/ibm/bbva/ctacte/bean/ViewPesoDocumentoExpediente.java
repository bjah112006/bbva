package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VIEW_CE_IBM_PESO_EXP_EMP", schema = "CONELE")
public class ViewPesoDocumentoExpediente implements java.io.Serializable {
	
	private static final long serialVersionUID = 4562951212412812491L;

	@Column(name = "ID_PRODUCTO", precision = 22, scale = 0)
	private Integer idProducto;
	
	@Column(name = "ID_TERRITORIO", precision = 22, scale = 0)
	private Integer idTerritorio;
	
	@Column(name = "ID_TAREA", precision = 22, scale = 0)
	private Integer idTarea;
	
	@Id
	@Column(name = "ID_EMPLEADO", precision = 22, scale = 0)
	private Integer idEmpleado;
	
	@Column(name = "COD_EMPLEADO", precision = 22, scale = 0)
	private String codEmpleado;
	
	@Column(name = "NOM_EMPLEADO", precision = 22, scale = 0)
	private String nomEmpleado;
	
	@Column(name = "ID_PERFIL", precision = 22, scale = 0)
	private Integer idPerfil;
	
	@Column(name = "DES_PERFIL", length = 60)
	private String desPerfil;
	
	@Column(name = "ID_ESTUDIO", precision = 5, scale = 0)
	private Integer idEstudio;
	
	@Column(name = "SUM_PESO_DOC_EXPEDIENTE", precision = 22, scale = 0)
	private Integer sumPesoDocumentoExpediente;
	
	public ViewPesoDocumentoExpediente() {
	}

	public Integer getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}

	public Integer getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(Integer idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public Integer getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(Integer idTarea) {
		this.idTarea = idTarea;
	}

	public Integer getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Integer idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getCodEmpleado() {
		return codEmpleado;
	}

	public void setCodEmpleado(String codEmpleado) {
		this.codEmpleado = codEmpleado;
	}

	public String getNomEmpleado() {
		return nomEmpleado;
	}

	public void setNomEmpleado(String nomEmpleado) {
		this.nomEmpleado = nomEmpleado;
	}

	public Integer getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getDesPerfil() {
		return desPerfil;
	}

	public void setDesPerfil(String desPerfil) {
		this.desPerfil = desPerfil;
	}

	public Integer getIdEstudio() {
		return idEstudio;
	}

	public void setIdEstudio(Integer idEstudio) {
		this.idEstudio = idEstudio;
	}

	public Integer getSumPesoDocumentoExpediente() {
		return sumPesoDocumentoExpediente;
	}

	public void setSumPesoDocumentoExpediente(Integer sumPesoDocumentoExpediente) {
		this.sumPesoDocumentoExpediente = sumPesoDocumentoExpediente;
	}
	
}

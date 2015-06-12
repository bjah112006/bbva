package com.ibm.bbva.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the VIEW_CE_IBM_COMPL_EXPEDIENTES database table.
 * 
 */
@Entity
@Table(name="VIEW_CE_IBM_COMPL_EXPEDIENTES", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="VistaExpedienteComplejidad.findByIdProdIdTerrIdPer", query="SELECT v FROM VistaExpedienteComplejidad v WHERE v.idProduto = :idProducto and v.idTerritorio = :idTerritorio and v.idPerfil = :idPerfil")
})
public class VistaExpedienteComplejidad implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="ID_PRODUCTO")
	private long idProduto;
	@Column(name="ID_TERRITORIO")
	private long idTerritorio;
	@Column(name="ID_PERFIL")
	private long idPerfil;
	@Id
	@Column(name="ID_EMPLEADO")
	private long idEmpleado;
	@Column(name="COMPLEJIDAD")
	private long complejidad;
	
	public VistaExpedienteComplejidad(){
	}

	public long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(long idProduto) {
		this.idProduto = idProduto;
	}

	public long getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(long idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public long getComplejidad() {
		return complejidad;
	}

	public void setComplejidad(long complejidad) {
		this.complejidad = complejidad;
	}

	public long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(long idPerfil) {
		this.idPerfil = idPerfil;
	}
}

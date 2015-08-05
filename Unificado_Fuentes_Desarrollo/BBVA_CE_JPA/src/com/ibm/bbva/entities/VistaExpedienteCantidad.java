package com.ibm.bbva.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the VIEW_CE_IBM_CANT_EXPEDIENTES database table.
 * 
 */
@Entity
@Table(name="VIEW_CE_IBM_CANT_EXPEDIENTES", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="VistaExpedienteCantidad.findByIdProdIdTerrIdPer", query="SELECT v FROM VistaExpedienteCantidad v WHERE v.idProduto = :idProducto and v.idTerritorio = :idTerritorio and v.idPerfil = :idPerfil"),
	@NamedQuery(name="VistaExpedienteCantidad.findByIdProdIdPer", query="SELECT v FROM VistaExpedienteCantidad v WHERE v.idProduto = :idProducto and v.idPerfil = :idPerfil"),
	@NamedQuery(name="VistaExpedienteCantidad.findByIdProdIdTerrIdOfIdPer", query="SELECT v FROM VistaExpedienteCantidad v WHERE v.idProduto = :idProducto and v.idTerritorio = :idTerritorio and v.idOficina = :idOficina and v.idPerfil = :idPerfil"),
	@NamedQuery(name="VistaExpedienteCantidad.findByIdEmpleado", query="SELECT v FROM VistaExpedienteCantidad v WHERE v.idEmpleado = :idEmpleado"),
	@NamedQuery(name="VistaExpedienteCantidad.sumExpByIdEmpleado", query="SELECT v.idEmpleado, SUM(v.numExpedientes) FROM VistaExpedienteCantidad v WHERE v.idEmpleado = :idEmpleado GROUP BY v.idEmpleado"),
	@NamedQuery(name="VistaExpedienteCantidad.sumExpByAllEmpleado", query="SELECT v.idEmpleado, SUM(v.numExpedientes) FROM VistaExpedienteCantidad v GROUP BY v.idEmpleado")
})
public class VistaExpedienteCantidad implements Serializable {
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
	@Column(name="NUM_EXPEDIENTES")
	private long numExpedientes;
	@Column(name="ID_OFICINA")
	private long idOficina;
	
	public VistaExpedienteCantidad(){
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

	public long getNumExpedientes() {
		return numExpedientes;
	}

	public void setNumExpedientes(long numExpedientes) {
		this.numExpedientes = numExpedientes;
	}

	public long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(long idPerfil) {
		this.idPerfil = idPerfil;
	}

	public long getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(long idOficina) {
		this.idOficina = idOficina;
	}
	
	
}

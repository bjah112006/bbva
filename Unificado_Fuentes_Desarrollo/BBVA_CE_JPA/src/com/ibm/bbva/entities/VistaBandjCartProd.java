package com.ibm.bbva.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the VIEW_CE_IBM_COMPL_EXPEDIENTES database table.
 * 
 */
@Entity
@Table(name="VIEW_CE_IBM_BANDJ_CARTPROD", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="VistaBandjCartProd.findByAll", query="SELECT v FROM VistaBandjCartProd v")
})
public class VistaBandjCartProd implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	
	@Column(name="ID_EMPLEADO")
	private long idEmpleado;	
	
	@Column(name="CODIGO")
	private String codigo;	
	
	@Column(name="NOMBRES_COMPLETOS")
	private String nombresCompletos;
	
	
	@Column(name="ID_PERFIL")
	private long idPerfil;	
	
	
	@Column(name="ID_TERRITORIO")
	private long idTerritorio;	
	
	
	@Column(name="ID_OFICINA")
	private long idOficina;		
	
	@Id
	@Column(name="ID_PRODUCTO")
	private long idProducto;

	public VistaBandjCartProd(){
	}

	public long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombresCompletos() {
		return nombresCompletos;
	}

	public void setNombresCompletos(String nombresCompletos) {
		this.nombresCompletos = nombresCompletos;
	}

	public long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(long idPerfil) {
		this.idPerfil = idPerfil;
	}

	public long getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(long idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public long getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(long idOficina) {
		this.idOficina = idOficina;
	}

	public long getIdProduto() {
		return idProducto;
	}

	public void setIdProduto(long idProducto) {
		this.idProducto = idProducto;
	}

	
}

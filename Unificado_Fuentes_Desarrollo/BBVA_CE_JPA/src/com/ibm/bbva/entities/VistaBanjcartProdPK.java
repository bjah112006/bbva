package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the VistaBandjCartProd database table.
 * 
 */
@Embeddable
public class VistaBanjcartProdPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_EMPLEADO", insertable=false, updatable=false)
	private long idEmpleado;
	
	@Column(name="ID_PERFIL", insertable=false, updatable=false)
	private long idPerfil;	
	
	@Column(name="ID_TERRITORIO", insertable=false, updatable=false)
	private long idTerritorio;	
	
	@Column(name="ID_OFICINA", insertable=false, updatable=false)
	private long idOficina;	
	
	@Column(name="ID_PRODUCTO", insertable=false, updatable=false)
	private long idProducto;
	
	public VistaBanjcartProdPK() {
	}
	
	public long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(long idEmpleado) {
		this.idEmpleado = idEmpleado;
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

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProduto(long idProducto) {
		this.idProducto = idProducto;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VistaBanjcartProdPK)) {
			return false;
		}
		VistaBanjcartProdPK castOther = (VistaBanjcartProdPK)other;
		return 
			(this.idEmpleado == castOther.idEmpleado)
			&& (this.idPerfil == castOther.idPerfil) 
			&& (this.idTerritorio == castOther.idTerritorio)
			&& (this.idOficina == castOther.idOficina) 
			&& (this.idProducto == castOther.idProducto) ;
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idEmpleado ^ (this.idEmpleado >>> 32)));
		hash = hash * prime + ((int) (this.idPerfil ^ (this.idPerfil >>> 32)));
		hash = hash * prime + ((int) (this.idTerritorio ^ (this.idTerritorio >>> 32)));
		hash = hash * prime + ((int) (this.idOficina ^ (this.idOficina >>> 32)));
		hash = hash * prime + ((int) (this.idProducto ^ (this.idProducto >>> 32)));
		return hash;
	}
}
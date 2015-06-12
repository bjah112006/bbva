package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TBL_CE_IBM_CARTERIZACION database table.
 * 
 */
@Embeddable
public class CarterizacionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_PRODUCTO_FK", insertable=false, updatable=false)
	private long idProductoFk;

	@Column(name="ID_TERRITORIO_FK", insertable=false, updatable=false)
	private long idTerritorioFk;

	@Column(name="ID_EMPLEADO_FK", insertable=false, updatable=false)
	private long idEmpleadoFk;

	public CarterizacionPK() {
	}
	public long getIdProductoFk() {
		return this.idProductoFk;
	}
	public void setIdProductoFk(long idProductoFk) {
		this.idProductoFk = idProductoFk;
	}
	public long getIdTerritorioFk() {
		return this.idTerritorioFk;
	}
	public void setIdTerritorioFk(long idTerritorioFk) {
		this.idTerritorioFk = idTerritorioFk;
	}
	public long getIdEmpleadoFk() {
		return this.idEmpleadoFk;
	}
	public void setIdEmpleadoFk(long idEmpleadoFk) {
		this.idEmpleadoFk = idEmpleadoFk;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CarterizacionPK)) {
			return false;
		}
		CarterizacionPK castOther = (CarterizacionPK)other;
		return 
			(this.idProductoFk == castOther.idProductoFk)
			&& (this.idTerritorioFk == castOther.idTerritorioFk)
			&& (this.idEmpleadoFk == castOther.idEmpleadoFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idProductoFk ^ (this.idProductoFk >>> 32)));
		hash = hash * prime + ((int) (this.idTerritorioFk ^ (this.idTerritorioFk >>> 32)));
		hash = hash * prime + ((int) (this.idEmpleadoFk ^ (this.idEmpleadoFk >>> 32)));
		
		return hash;
	}
}
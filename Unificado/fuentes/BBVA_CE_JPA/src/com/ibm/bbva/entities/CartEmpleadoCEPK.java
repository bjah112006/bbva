package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TBL_CE_IBM_CARTERIZACION database table.
 * 
 */
@Embeddable
public class CartEmpleadoCEPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_EMPLEADO_FK", insertable=false, updatable=false)
	private long idTerritorioFk;

	@Column(name="ID_CARTERIZACION_FK", insertable=false, updatable=false)
	private long idCarterizacionFk;

	public CartEmpleadoCEPK() {
	}
	
	public long getIdTerritorioFk() {
		return this.idTerritorioFk;
	}
	public void setIdTerritorioFk(long idTerritorioFk) {
		this.idTerritorioFk = idTerritorioFk;
	}

	public long getIdCarterizacionFk() {
		return idCarterizacionFk;
	}
	public void setIdCarterizacionFk(long idCarterizacionFk) {
		this.idCarterizacionFk = idCarterizacionFk;
	}
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CartEmpleadoCEPK)) {
			return false;
		}
		CartEmpleadoCEPK castOther = (CartEmpleadoCEPK)other;
		return 
			(this.idTerritorioFk == castOther.idTerritorioFk)
			&& (this.idCarterizacionFk == castOther.idCarterizacionFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idTerritorioFk ^ (this.idTerritorioFk >>> 32)));
		hash = hash * prime + ((int) (this.idCarterizacionFk ^ (this.idCarterizacionFk >>> 32)));
		
		return hash;
	}
}
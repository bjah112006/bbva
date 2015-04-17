package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TBL_CE_IBM_PRODUCTO_TAREA database table.
 * 
 */
@Embeddable
public class ProductoTareaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_PRODUCTO_FK", insertable=false, updatable=false)
	private long idProductoFk;
	
	@Column(name="ID_TAREA_FK", insertable=false, updatable=false)
	private long idTareaFk;	
	
	public ProductoTareaPK() {
	}

	public long getIdProductoFk() {
		return idProductoFk;
	}
	public void setIdProductoFk(long idProductoFk) {
		this.idProductoFk = idProductoFk;
	}
	
	public long getIdTareaFk() {
		return idTareaFk;
	}

	public void setIdTareaFk(long idTareaFk) {
		this.idTareaFk = idTareaFk;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProductoTareaPK)) {
			return false;
		}
		ProductoTareaPK castOther = (ProductoTareaPK)other;
		return 
			(this.idProductoFk == castOther.idProductoFk) 
			&& (this.idTareaFk == castOther.idTareaFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idProductoFk ^ (this.idProductoFk >>> 32)));
		hash = hash * prime + ((int) (this.idTareaFk ^ (this.idTareaFk >>> 32)));
		return hash;
	}
}
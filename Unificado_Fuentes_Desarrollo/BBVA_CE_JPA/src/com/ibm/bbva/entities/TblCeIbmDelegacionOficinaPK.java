package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TBL_CE_IBM_DELEGACION_OFICINA database table.
 * 
 */
@Embeddable
public class TblCeIbmDelegacionOficinaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_NIVEL_FK", insertable=false, updatable=false)
	private long idNivelFk;

	@Column(name="ID_TIPO_MONEDA_FK", insertable=false, updatable=false)
	private long idTipoMonedaFk;
	
	@Column(name="ID_PRODUCTO_FK", insertable=false, updatable=false)
	private long idProductoFk;	

	public TblCeIbmDelegacionOficinaPK() {
	}
	
	public long getIdNivelFk() {
		return this.idNivelFk;
	}
	public void setIdNivelFk(long idNivelFk) {
		this.idNivelFk = idNivelFk;
	}
	public long getIdTipoMonedaFk() {
		return this.idTipoMonedaFk;
	}
	public void setIdTipoMonedaFk(long idTipoMonedaFk) {
		this.idTipoMonedaFk = idTipoMonedaFk;
	}

	public long getIdProductoFk() {
		return idProductoFk;
	}

	public void setIdProductoFk(long idProductoFk) {
		this.idProductoFk = idProductoFk;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblCeIbmDelegacionOficinaPK)) {
			return false;
		}
		TblCeIbmDelegacionOficinaPK castOther = (TblCeIbmDelegacionOficinaPK)other;
		return 
			(this.idNivelFk == castOther.idNivelFk)
			&& (this.idTipoMonedaFk == castOther.idTipoMonedaFk) 
			&& (this.idProductoFk == castOther.idProductoFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idNivelFk ^ (this.idNivelFk >>> 32)));
		hash = hash * prime + ((int) (this.idTipoMonedaFk ^ (this.idTipoMonedaFk >>> 32)));
		hash = hash * prime + ((int) (this.idProductoFk ^ (this.idProductoFk >>> 32)));
		return hash;
	}
}
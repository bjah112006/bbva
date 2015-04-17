package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TBL_CE_IBM_DELEGACION_RIESGOS database table.
 * 
 */
@Embeddable
public class DelegacionRiesgoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_TIPO_CATEGORIA_FK", insertable=false, updatable=false)
	private long idTipoCategoriaFk;

	@Column(name="ID_TIPO_MONEDA_FK", insertable=false, updatable=false)
	private long idTipoMonedaFk;
	
	@Column(name="ID_PRODUCTO_FK", insertable=false, updatable=false)
	private long idProductoFk;
	

	public DelegacionRiesgoPK() {
	}
	public long getIdTipoCategoriaFk() {
		return this.idTipoCategoriaFk;
	}
	public void setIdTipoCategoriaFk(long idTipoCategoriaFk) {
		this.idTipoCategoriaFk = idTipoCategoriaFk;
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
		if (!(other instanceof DelegacionRiesgoPK)) {
			return false;
		}
		DelegacionRiesgoPK castOther = (DelegacionRiesgoPK)other;
		return 
			(this.idTipoCategoriaFk == castOther.idTipoCategoriaFk)
			&& (this.idTipoMonedaFk == castOther.idTipoMonedaFk)
			&& (this.idProductoFk == castOther.idProductoFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idTipoCategoriaFk ^ (this.idTipoCategoriaFk >>> 32)));
		hash = hash * prime + ((int) (this.idTipoMonedaFk ^ (this.idTipoMonedaFk >>> 32)));
		hash = hash * prime + ((int) (this.idProductoFk ^ (this.idProductoFk >>> 32)));
		return hash;
	}
}
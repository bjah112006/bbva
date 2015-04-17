package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TBL_CE_IBM_ANS database table.
 * 
 */
@Embeddable
public class AnsPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_PRODUCTO_FK", insertable=false, updatable=false)
	private long idProductoFk;

	@Column(name="ID_TAREA_FK", insertable=false, updatable=false)
	private long idTareaFk;
	
	@Column(name="ID_TIPO_OFERTA_FK", insertable=false, updatable=false)
	private long idTipoOfertaFk;

	@Column(name="ID_GRUPO_SEGMENTO_FK", insertable=false, updatable=false)
	private long idGrupoSegmentoFk;	

	public AnsPK() {
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

	public long getIdTipoOfertaFk() {
		return idTipoOfertaFk;
	}

	public void setIdTipoOfertaFk(long idTipoOfertaFk) {
		this.idTipoOfertaFk = idTipoOfertaFk;
	}

	public long getIdGrupoSegmentoFk() {
		return idGrupoSegmentoFk;
	}

	public void setIdGrupoSegmentoFk(long idGrupoSegmentoFk) {
		this.idGrupoSegmentoFk = idGrupoSegmentoFk;
	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AnsPK)) {
			return false;
		}
		AnsPK castOther = (AnsPK)other;
		return 
			(this.idProductoFk == castOther.idProductoFk) 
			&& (this.idTareaFk == castOther.idTareaFk)
			&& (this.idTipoOfertaFk == castOther.idTipoOfertaFk)
			&& (this.idGrupoSegmentoFk == castOther.idGrupoSegmentoFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idProductoFk ^ (this.idProductoFk >>> 32)));
		hash = hash * prime + ((int) (this.idTareaFk ^ (this.idTareaFk >>> 32)));
		hash = hash * prime + ((int) (this.idTipoOfertaFk ^ (this.idTipoOfertaFk >>> 32)));
		hash = hash * prime + ((int) (this.idGrupoSegmentoFk ^ (this.idGrupoSegmentoFk >>> 32)));
		return hash;
	}
}
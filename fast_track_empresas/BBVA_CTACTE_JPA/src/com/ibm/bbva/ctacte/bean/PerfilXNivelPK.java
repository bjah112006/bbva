package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TBL_CE_IBM_PERFIL_X_NIVEL_CC database table.
 * 
 */
@Embeddable
public class PerfilXNivelPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="TIPO_NIVEL", unique=true, nullable=false, precision=10)
	private long tipoNivel;

	@Column(unique=true, nullable=false, length=20)
	private String valor;

    public PerfilXNivelPK() {
    }
	public long getTipoNivel() {
		return this.tipoNivel;
	}
	public void setTipoNivel(long tipoNivel) {
		this.tipoNivel = tipoNivel;
	}
	public String getValor() {
		return this.valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PerfilXNivelPK)) {
			return false;
		}
		PerfilXNivelPK castOther = (PerfilXNivelPK)other;
		return 
			(this.tipoNivel == castOther.tipoNivel)
			&& this.valor.equals(castOther.valor);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.tipoNivel ^ (this.tipoNivel >>> 32)));
		hash = hash * prime + this.valor.hashCode();
		
		return hash;
    }
}
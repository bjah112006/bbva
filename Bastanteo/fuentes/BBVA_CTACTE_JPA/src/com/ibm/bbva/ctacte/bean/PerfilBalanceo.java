package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_PERFIL_BALANCEO_CC", schema = "CONELE")
public class PerfilBalanceo implements java.io.Serializable {
	
	private static final long serialVersionUID = 1857624367794937747L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@Column(name = "ID_PERFIL_FK", length = 5)
	private Integer idPerfil;
	
	@Column(name = "ID_TIPO_BALANCEO_FK", length = 5)
	private Integer idTipoBalanceo;
	
	public PerfilBalanceo() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public Integer getIdTipoBalanceo() {
		return idTipoBalanceo;
	}

	public void setIdTipoBalanceo(Integer idTipoBalanceo) {
		this.idTipoBalanceo = idTipoBalanceo;
	}
	
}

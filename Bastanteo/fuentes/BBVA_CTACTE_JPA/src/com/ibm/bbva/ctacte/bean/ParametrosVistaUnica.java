package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_PARA_VISTA_UNICA_CC", schema = "CONELE")
public class ParametrosVistaUnica implements java.io.Serializable {

	private static final long serialVersionUID = -332265798015706117L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@Column(name = "ID_DOCUMENTO_CC_FK", nullable = false, precision = 5, scale = 0)
	private Integer id_documento_cc_fk;
	
	@Column(name = "FLAG_SOLO_ULTIMA_VERSION", nullable = false, length = 1)
	private String flag_solo_ultima_version;
	
	public ParametrosVistaUnica() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_documento_cc_fk() {
		return id_documento_cc_fk;
	}

	public void setId_documento_cc_fk(Integer id_documento_cc_fk) {
		this.id_documento_cc_fk = id_documento_cc_fk;
	}

	public String getFlag_solo_ultima_version() {
		return flag_solo_ultima_version;
	}

	public void setFlag_solo_ultima_version(String flag_solo_ultima_version) {
		this.flag_solo_ultima_version = flag_solo_ultima_version;
	}
	
}

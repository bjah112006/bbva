package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_AUDIT_CLIENTE_CC", schema = "CONELE")
public class AuditoriaCliente implements java.io.Serializable {
	
	private static final long serialVersionUID = -3312989122764036099L;
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@Column(name = "CODIGO_CENTRAL", nullable = false, length = 100)
	private String codigoCentral;
	
	public AuditoriaCliente() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigoCentral() {
		return codigoCentral;
	}

	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}
}

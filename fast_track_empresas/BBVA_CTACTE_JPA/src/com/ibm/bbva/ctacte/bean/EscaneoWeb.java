package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_ESCANEO_WEB_CC", schema = "CONELE")
public class EscaneoWeb implements Serializable {
	
	private static final long serialVersionUID = 7206688558249593029L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@Column(name = "ID_EMPRESA", nullable = true, length = 20)
	private String id_empresa;
	
	@Column(name = "ID_SISTEMA", nullable = true, length = 20)
	private String id_sistema;
	
	public EscaneoWeb() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getId_empresa() {
		return id_empresa;
	}

	public void setId_empresa(String id_empresa) {
		this.id_empresa = id_empresa;
	}

	public String getId_sistema() {
		return id_sistema;
	}

	public void setId_sistema(String id_sistema) {
		this.id_sistema = id_sistema;
	}

}

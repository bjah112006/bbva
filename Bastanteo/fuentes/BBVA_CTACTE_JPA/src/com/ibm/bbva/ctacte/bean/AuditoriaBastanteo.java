package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_AUDIT_BASTANTEO_CC", schema = "CONELE")
public class AuditoriaBastanteo implements java.io.Serializable {
	
	private static final long serialVersionUID = -6912948705267031344L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@Column(name = "RESULTADO_BASTANTEO", nullable = false, length = 100)
	private String resultadoBastanteo;
	
	public AuditoriaBastanteo() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getResultadoBastanteo() {
		return resultadoBastanteo;
	}

	public void setResultadoBastanteo(String resultadoBastanteo) {
		this.resultadoBastanteo = resultadoBastanteo;
	}

}

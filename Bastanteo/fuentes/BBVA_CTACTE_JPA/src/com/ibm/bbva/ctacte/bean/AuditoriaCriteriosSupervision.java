package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "TBL_CE_IBM_AUDIT_CRITERIOS_CC", schema = "CONELE")
public class AuditoriaCriteriosSupervision implements java.io.Serializable {
	
	private static final long serialVersionUID = -4318302223930282148L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@Column(name = "TIPO_CRITERIO", nullable = false, length = 100)
	private String tipoCriterio;
	
	@Column(name = "INDICADOR", length = 1)
	private Integer indicador;
	
	public AuditoriaCriteriosSupervision() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipoCriterio() {
		return tipoCriterio;
	}

	public void setTipoCriterio(String tipoCriterio) {
		this.tipoCriterio = tipoCriterio;
	}

	public Integer getIndicador() {
		return indicador;
	}

	public void setIndicador(Integer indicador) {
		this.indicador = indicador;
	}
}

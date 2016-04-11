package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_EXP_CUENTA_CC", schema = "CONELE")
public class ExpedienteCuenta implements java.io.Serializable {
	
	private static final long serialVersionUID = -8175866836704658342L;

	@Id
	@SequenceGenerator(name = "seqExpedienteCuenta", sequenceName = "SEQ_CE_IBM_EXP_CTA_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqExpedienteCuenta")
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	private Integer id;
	
	@Column(name = "ID_EXPEDIENTE_FK", length = 5)
	private Integer idExpediente;
	
	@Column(name = "ID_CUENTA_FK", length = 5)
	private Integer idCuenta;
	
	public ExpedienteCuenta() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdExpediente() {
		return idExpediente;
	}

	public void setIdExpediente(Integer idExpediente) {
		this.idExpediente = idExpediente;
	}

	public Integer getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}
	
}

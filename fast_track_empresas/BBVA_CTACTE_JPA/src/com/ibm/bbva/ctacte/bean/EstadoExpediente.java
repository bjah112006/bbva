package com.ibm.bbva.ctacte.bean;
// default package
// Generated 14/05/2012 10:37:52 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Estado generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_ESTADO_EXP_CC", schema = "CONELE")
public class EstadoExpediente implements java.io.Serializable {

	private static final long serialVersionUID = -8325216913809500632L;

	@Id
	@SequenceGenerator(name = "seqEstadoExpediente", sequenceName = "SEQ_CE_IBM_ESTADO_EXP_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqEstadoExpediente")
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@Column(name = "DESCRIPCION", nullable = false, length = 100)
	private String descripcion;

	public EstadoExpediente() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}

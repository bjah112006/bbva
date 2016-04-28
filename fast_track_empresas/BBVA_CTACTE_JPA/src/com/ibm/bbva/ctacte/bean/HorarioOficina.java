package com.ibm.bbva.ctacte.bean;

// default package
// Generated 09/08/2012 11:20:03 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * HorarioOficina generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_HORARIO_OFICINA", schema = "CONELE")
public class HorarioOficina implements java.io.Serializable {

	private static final long serialVersionUID = -5618935677545214830L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_HORARIO_FK", nullable = false)
	private Horario horario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_OFICINA_FK", nullable = false)
	private Oficina oficina;

	public HorarioOficina() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

}
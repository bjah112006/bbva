package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_HORARIO_OFICINA_CE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_HORARIO_OFICINA_CE", schema = "CONELE")

@NamedQueries({
	@NamedQuery(name="HorarioOficina.findAll", query="SELECT h FROM HorarioOficina h"),
	@NamedQuery(name="HorarioOficina.findById", query="SELECT h FROM HorarioOficina h WHERE h.id = :id"),
	@NamedQuery(name="HorarioOficina.findByIdHorario", query="SELECT h FROM HorarioOficina h WHERE h.horario.id = :idHorario"),
	@NamedQuery(name="HorarioOficina.findByIdOficina", query="SELECT h FROM HorarioOficina h WHERE h.oficina.id = :idOficina"),
	@NamedQuery(name="HorarioOficina.findByIdOficinaIdHorario", query="SELECT h FROM HorarioOficina h WHERE h.oficina.id = :idOficina AND h.horario.id = :idHorario")
})

public class HorarioOficina implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_HORARIO_OFICINA_CE_ID_GENERATOR", sequenceName="SEQ_CE_IBM_HORARIO_OFICINA_CE", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_HORARIO_OFICINA_CE_ID_GENERATOR")
	private long id;

	//bi-directional many-to-one association to Horario
	@ManyToOne
	@JoinColumn(name="ID_HORARIO_FK")
	private Horario horario;

	//bi-directional many-to-one association to Oficina
	@ManyToOne
	@JoinColumn(name="ID_OFICINA_FK")
	private Oficina oficina;

	public HorarioOficina() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Horario getHorario() {
		return this.horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public Oficina getOficina() {
		return this.oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

}
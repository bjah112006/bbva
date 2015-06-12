package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_TAREA_PERFIL database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TAREA_PERFIL", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name="TareaPerfil.findAll", query="SELECT t FROM TareaPerfil t"),
	@NamedQuery(name = "TareaPerfil.findByIdPerfil", query = "SELECT e FROM TareaPerfil e WHERE e.perfil.id = :idPerfil"),
	@NamedQuery(name = "TareaPerfil.findByIdTarea", query = "SELECT e FROM TareaPerfil e WHERE e.tarea.id = :idTarea")
})
public class TareaPerfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TAREA_PERFIL_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TAREA_PERFIL", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TAREA_PERFIL_ID_GENERATOR")
	private long id;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="ID_PERFIL_FK")
	private Perfil perfil;

	//bi-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;

	public TareaPerfil() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Tarea getTarea() {
		return this.tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

}
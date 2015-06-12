package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_TAREA_ACCION database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TAREA_ACCION", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name="TareaAccion.findAll", query="SELECT t FROM TareaAccion t"),
	@NamedQuery(name = "TareaAccion.findByIdAccion", query = "SELECT e FROM TareaAccion e WHERE e.accion.id = :idAccion"),
	@NamedQuery(name = "TareaAccion.findByIdTarea", query = "SELECT e FROM TareaAccion e WHERE e.tarea.id = :idTarea")
})
public class TareaAccion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TAREA_ACCION_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TAREA_ACCION", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TAREA_ACCION_ID_GENERATOR")
	private long id;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="ID_ACCION_FK")
	private Accion accion;

	//bi-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;

	public TareaAccion() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Tarea getTarea() {
		return this.tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}
	
	
	
	

}
package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_ESTADO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_ESTADO", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name = "Estado.findAll", query="SELECT e FROM Estado e"),
	@NamedQuery(name = "Estado.findById", query = "SELECT e FROM Estado e WHERE e.id = :id")	 
})

public class Estado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_ESTADO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_ESTADO", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_ESTADO_ID_GENERATOR")
	private long id;

	@Column(name="CODIGO")
	private String codigo;

	@Column(name="DESCRIPCION")
	private String descripcion;

	//uni-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;

	public Estado() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Tarea getTarea() {
		return this.tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

}
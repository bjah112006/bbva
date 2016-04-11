package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the TBL_CE_IBM_ACCION_CC database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_ACCION_CC", schema = "CONELE")
public class Accion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, precision=5)
	private Integer id;
	
	@Column(length=50)
	private String nombre;

	@Column(length=50)
	private String descripcion;

	//uni-directional many-to-one association to Tarea
    @ManyToOne
	@JoinColumn(name="ID_TAREA_FK", nullable=false)
	private Tarea tarea;

    public Accion() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
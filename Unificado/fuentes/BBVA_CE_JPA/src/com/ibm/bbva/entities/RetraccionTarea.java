package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: RetraccionTarea
 *
 */
@Entity
@Table(name="TBL_CE_IBM_RETRACCION_TAREA", schema = "CONELE")
@NamedQueries({	
	@NamedQuery(name = "RetraccionTarea.findAll", query = "SELECT p FROM RetraccionTarea p "),
	@NamedQuery(name = "RetraccionTarea.find", query = "SELECT p FROM RetraccionTarea p where p.accion.id=:idAccion and p.salida=:salida and p.llegada=:llegada"),
	@NamedQuery(name = "TareaPerfil.findByFlagRetraer", query = "SELECT p FROM RetraccionTarea p where p.llegada=:idTareaActual and p.salida=:idTareaAnterior"),
	@NamedQuery(name = "TareaPerfil.findByFlagRetraerAnt", query = "SELECT p FROM RetraccionTarea p where p.salida=:idTareaAnterior")
})
public class RetraccionTarea implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_RETRACCION_TAREA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_RETRACCION_TAREA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_RETRACCION_TAREA_ID_GENERATOR")
	private long id;
	
	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="ID_ACCION_FK")
	private Accion accion;
	
	@Column(name="CU_SALIDA")
	private Long  salida;
	
	@Column(name="CU_LLEGADA")
	private Long llegada;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}

	public Long getSalida() {
		return salida;
	}

	public void setSalida(Long salida) {
		this.salida = salida;
	}

	public Long getLlegada() {
		return llegada;
	}

	public void setLlegada(Long llegada) {
		this.llegada = llegada;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}

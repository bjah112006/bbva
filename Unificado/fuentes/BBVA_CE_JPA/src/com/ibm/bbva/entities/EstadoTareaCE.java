package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the TBL_CE_IBM_CART_EMPLEADO_CE database table.
 * 
 */
@Entity
@Table(name = "TBL_CE_IBM_ESTADO_TAREA_CE", schema = "CONELE")

@NamedQueries({
  @NamedQuery(name = "EstadoTareaCE.findAll", query = "SELECT e FROM EstadoTareaCE e order by e.tarea.id"),
  //@NamedQuery(name = "CartEmpleadoCE.findIdCaract", query = "SELECT DISTINCT e.empleado.perfil FROM CartEmpleadoCE e WHERE e.carterizacionCE.id =:idCaract AND e.empleado.perfil.id IN (2,3,5,8,10) AND e.flagActivo=:flagActivo"),
  @NamedQuery(name = "EstadoTareaCE.findIdEstado", query = "SELECT e FROM EstadoTareaCE e WHERE e.estadoCE.id =:idEstado"),
  @NamedQuery(name = "EstadoTareaCE.findIdTarea", query = "SELECT e FROM EstadoTareaCE e WHERE e.tarea.id =:idTarea")
})

public class EstadoTareaCE implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id 

    @ManyToOne    
    @JoinColumn(name = "ID_ESTADO_FK")
    private Estado estado;
    
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_CE_FK")
    private EstadoCE estadoCE;

    @ManyToOne
    @JoinColumn(name = "ID_TAREA_FK")
    private Tarea tarea;
    
    public EstadoTareaCE() {
    }

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public EstadoCE getEstadoCE() {
		return estadoCE;
	}

	public void setEstadoCE(EstadoCE estadoCE) {
		this.estadoCE = estadoCE;
	}

	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

}

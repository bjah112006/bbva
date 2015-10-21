package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the TBL_CE_IBM_CART_EMPLEADO_CE database table.
 * 
 */
@Entity
@Table(name = "TBL_CE_IBM_CART_EMPLEADO_CE", schema = "CONELE")

@NamedQueries({
  @NamedQuery(name = "CartEmpleadoCE.findAll", query = "SELECT e FROM CartEmpleadoCE e"),
  //@NamedQuery(name = "CartEmpleadoCE.findIdCaract", query = "SELECT DISTINCT e.empleado.perfil FROM CartEmpleadoCE e WHERE e.carterizacionCE.id =:idCaract AND e.empleado.perfil.id IN (2,3,5,8,10) AND e.flagActivo=:flagActivo"),
  @NamedQuery(name = "CartEmpleadoCE.findIdCaractIdPerfil", query = "SELECT e FROM CartEmpleadoCE e WHERE e.carterizacionCE.id =:idCaract AND e.empleado.perfil.id =:idPerfil AND e.flagActivo LIKE :flagActivo AND e.empleado.flagActivo LIKE :flagActivo"),
  @NamedQuery(name = "CartEmpleadoCE.findIdCaractIdEmpl", query = "SELECT e FROM CartEmpleadoCE e WHERE e.carterizacionCE.id =:idCaract AND e.empleado.id =:idEmpleado AND e.flagActivo LIKE :flagActivo"),
  @NamedQuery(name = "CartEmpleadoCE.findIdEmpl", query = "SELECT e FROM CartEmpleadoCE e WHERE e.empleado.id =:idEmpleado"),
  @NamedQuery(name = "CartEmpleadoCE.findIdEmplActivo", query = "SELECT e FROM CartEmpleadoCE e WHERE e.empleado.id =:idEmpleado AND e.flagActivo = '1'")
})

public class CartEmpleadoCE implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private CartEmpleadoCEPK id;
	
	@Column(name="FLAG_ACTIVO")
    private String flagActivo;
        
    @ManyToOne
    @JoinColumn(name = "ID_CARTERIZACION_FK")
    private CarterizacionCE carterizacionCE;
    
    @ManyToOne
    @JoinColumn(name = "ID_EMPLEADO_FK")
    private Empleado empleado;
    
    @ManyToOne
    @JoinColumn(name = "ID_OFICINA_FK")
    private Oficina oficina;

  	@ManyToOne
  	@JoinColumn(name="ID_PERFIL_FK")
  	private Perfil perfil;
    
    public CartEmpleadoCE() {
    }
    
    public CartEmpleadoCE(String flagActivo, CarterizacionCE carterizacionCE, Empleado empleado) {
		this.flagActivo = flagActivo;
		this.carterizacionCE = carterizacionCE;
		this.empleado = empleado;
    }
    
    public CartEmpleadoCEPK getId() {
		return id;
	}

	public void setId(CartEmpleadoCEPK id) {
		this.id = id;
	}
	
    public String getFlagActivo() {
        return flagActivo;
    }

    public void setFlagActivo(String flagActivo) {
        this.flagActivo = flagActivo;
    }

    public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public CarterizacionCE getCarterizacionCE() {
		return carterizacionCE;
	}

	public void setCarterizacionCE(CarterizacionCE carterizacionCE) {
		this.carterizacionCE = carterizacionCE;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	
}

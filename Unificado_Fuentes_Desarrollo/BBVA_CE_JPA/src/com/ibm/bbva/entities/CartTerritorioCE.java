package com.ibm.bbva.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the TBL_CE_IBM_CART_TERRITORIO_CE database table.
 * 
 */
@Entity
@Table(name = "TBL_CE_IBM_CART_TERRITORIO_CE", schema = "CONELE")

@NamedQueries({
  @NamedQuery(name = "CartTerritorioCE.findAll", query = "select t FROM CartTerritorioCE t"),
  @NamedQuery(name = "CartTerritorioCE.findIdTerr", query = "select t FROM CartTerritorioCE t WHERE t.territorio.id=:idTerritorio AND t.flagActivo=:flagActivo"),
  @NamedQuery(name = "CartTerritorioCE.findIdProdIdTerr", query = "select t FROM CartTerritorioCE t WHERE t.producto.id=:idProducto AND t.territorio.id=:idTerritorio AND t.flagActivo=:flagActivo")  
  
})
public class CartTerritorioCE implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private CarterizacionCEPK id;
	    
    private String descripcion;
    
    @Column(name="FLAG_ACTIVO")
    private String flagActivo;
    
    @ManyToOne
    @JoinColumn(name = "ID_CARTERIZACION_FK")
    private CarterizacionCE carterizacionCE;
    
    @ManyToOne
    @JoinColumn(name = "ID_TERRITORIO_FK")
    private Territorio territorio;
    
    @ManyToOne
    @JoinColumn(name = "ID_PRODUCTO_FK")
    private Producto producto;

    
    public CartTerritorioCE() {
    }
    
    public CartTerritorioCE(String descripcion, String flagActivo, CarterizacionCE carterizacionCE, Producto producto, Territorio territorio) {
		this.descripcion = descripcion;
		this.flagActivo = flagActivo;
		this.carterizacionCE = carterizacionCE;
		this.producto = producto;
		this.territorio = territorio;
	}
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFlagActivo() {
        return flagActivo;
    }

    public void setFlagActivo(String flagActivo) {
        this.flagActivo = flagActivo;
    }

    public Territorio getTerritorio() {
		return territorio;
	}

	public void setTerritorio(Territorio territorio) {
		this.territorio = territorio;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public CarterizacionCE getCarterizacionCE() {
		return carterizacionCE;
	}

	public void setCarterizacionCE(CarterizacionCE carterizacionCE) {
		this.carterizacionCE = carterizacionCE;
	}

	public CarterizacionCEPK getId() {
		return id;
	}

	public void setId(CarterizacionCEPK id) {
		this.id = id;
	}
    
}

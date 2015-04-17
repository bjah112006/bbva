package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_CE_IBM_TIPO_CAMBIO_CE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TIPO_CAMBIO_CE", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name = "TipoCambioCE.findAll", query="SELECT t FROM TipoCambioCE t ORDER BY t.fecha DESC"),
	@NamedQuery(name = "TipoCambioCE.findByFecha", query = "SELECT t FROM TipoCambioCE t WHERE t.fecha = :inFecha")	 
})

public class TipoCambioCE implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
    @Temporal( TemporalType.DATE)
	private Date fecha;

	private BigDecimal monto;

    public TipoCambioCE() {
    }

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

}
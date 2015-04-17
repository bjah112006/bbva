package com.ibm.bbva.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

/**
 * The persistent class for the TBL_CE_IBM_ANS database table.
 * 
 */

@Entity
@Table(name="TBL_CE_IBM_ANS", schema = "CONELE")
@NamedQueries({		
	@NamedQuery(name = "Ans.findAll", query = "SELECT t FROM Ans t"),
	@NamedQuery(name = "Ans.findById", query = "SELECT t FROM Ans t " +
			" WHERE t.producto.id = :idProducto and t.tarea.id = :idTarea " +
			" and t.tipoOferta.id = :idTipoOferta and t.grupoSegmento.id = :idGrupoSegmento "+
			" and t.flagActivo = : flagActivo")
})

public class Ans implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private AnsPK id;	
	
	@Column(name="AMARILLO_MINUTOS")
	private BigDecimal amarilloMinutos;

	@Column(name="VERDE_MINUTOS")
	private BigDecimal verdeMinutos;

	//uni-directional many-to-one association to Producto	
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;
	
	//uni-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;

	//uni-directional many-to-one association to Producto	
	@ManyToOne
	@JoinColumn(name="ID_TIPO_OFERTA_FK") 
	private TipoOferta tipoOferta;
	
	//uni-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_GRUPO_SEGMENTO_FK")
	private GrupoSegmento grupoSegmento;
	
	@Column(name="FLAG_ACTIVO")
    private String flagActivo;
	
	public Ans() {
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}
	
	public BigDecimal getAmarilloMinutos() {
		return this.amarilloMinutos;
	}

	public void setAmarilloMinutos(BigDecimal amarilloMinutos) {
		this.amarilloMinutos = amarilloMinutos;
	}

	public BigDecimal getVerdeMinutos() {
		return this.verdeMinutos;
	}

	public void setVerdeMinutos(BigDecimal verdeMinutos) {
		this.verdeMinutos = verdeMinutos;
	}

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public TipoOferta getTipoOferta() {
		return tipoOferta;
	}

	public void setTipoOferta(TipoOferta tipoOferta) {
		this.tipoOferta = tipoOferta;
	}

	public GrupoSegmento getGrupoSegmento() {
		return grupoSegmento;
	}

	public void setGrupoSegmento(GrupoSegmento grupoSegmento) {
		this.grupoSegmento = grupoSegmento;
	}

	public AnsPK getId() {
		return id;
	}

	public void setId(AnsPK id) {
		this.id = id;
	}
}

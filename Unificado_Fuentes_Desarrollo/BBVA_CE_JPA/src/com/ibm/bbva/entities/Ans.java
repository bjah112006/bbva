package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


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

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_ANS_ID_GENERATOR", sequenceName="SEQ_CE_IBM_ANS", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_ANS_ID_GENERATOR")
	private long id;

	@Column(name="AMARILLO_MINUTOS")
	private BigDecimal amarilloMinutos;

	@Column(name="FLAG_ACTIVO")
	private String flagActivo;

	@Column(name="VERDE_MINUTOS")
	private BigDecimal verdeMinutos;

	//bi-directional many-to-one association to EstadoCE
    @ManyToOne
	@JoinColumn(name="ID_ESTADO_CE_FK")
	private EstadoCE estadoCE;

	//bi-directional many-to-one association to GrupoSegmento
    @ManyToOne
	@JoinColumn(name="ID_GRUPO_SEGMENTO_FK")
	private GrupoSegmento grupoSegmento;

	//bi-directional many-to-one association to Producto
    @ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	//bi-directional many-to-one association to Tarea
    @ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;

	//bi-directional many-to-one association to TipoOferta
    @ManyToOne
	@JoinColumn(name="ID_TIPO_OFERTA_FK")
	private TipoOferta tipoOferta;
    
    @Transient
    private boolean seleccionado = false; // para el mantenimiento

    public Ans() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAmarilloMinutos() {
		return this.amarilloMinutos;
	}

	public void setAmarilloMinutos(BigDecimal amarilloMinutos) {
		this.amarilloMinutos = amarilloMinutos;
	}

	public String getFlagActivo() {
		return this.flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public BigDecimal getVerdeMinutos() {
		return this.verdeMinutos;
	}

	public void setVerdeMinutos(BigDecimal verdeMinutos) {
		this.verdeMinutos = verdeMinutos;
	}

	public EstadoCE getEstadoCE() {
		return this.estadoCE;
	}

	public void setEstadoCE(EstadoCE estadoCE) {
		this.estadoCE = estadoCE;
	}
	
	public GrupoSegmento getGrupoSegmento() {
		return this.grupoSegmento;
	}

	public void setGrupoSegmento(GrupoSegmento grupoSegmento) {
		this.grupoSegmento = grupoSegmento;
	}
	
	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	public Tarea getTarea() {
		return this.tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}
	
	public TipoOferta getTipoOferta() {
		return this.tipoOferta;
	}

	public void setTipoOferta(TipoOferta tipoOferta) {
		this.tipoOferta = tipoOferta;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
	
}
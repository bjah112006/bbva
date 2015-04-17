package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_CE_IBM_TOE_PERFIL_ESTADO database table.
 *
 */
@Entity
@Table(name="TBL_CE_IBM_TOE_PERFIL_ESTADO", schema = "CONELE")
@NamedQuery(name="ToePerfilEstado.findAll", query="SELECT t FROM ToePerfilEstado t")
public class ToePerfilEstado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TOE_PERFIL_ESTADO", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_GENERATOR")
	private long id;

	@Column(name="NOM_COLUMNA")
	private String nomColumna;

	@Column(name="TIEMPO_OBJ_TC")
	private BigDecimal tiempoObjTc;

	@Column(name="TIEMPO_OBJ_TE")
	private BigDecimal tiempoObjTe;

	@Column(name="TIEMPO_PRE_TC")
	private BigDecimal tiempoPreTc;

	@Column(name="TIEMPO_PRE_TE")
	private BigDecimal tiempoPreTe;

	@Column(name="TIPO_FLUJO")
	private String tipoFlujo;

	//uni-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO_FK")
	private Estado estado;

	//uni-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="ID_PERFIL_FK")
	private Perfil perfil;

	//uni-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	//uni-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;
	
	//uni-directional many-to-one association to TipoOferta
	@ManyToOne
	@JoinColumn(name="ID_TIPO_OFERTA_FK")
	private TipoOferta tipoOferta;

	public ToePerfilEstado() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNomColumna() {
		return this.nomColumna;
	}

	public void setNomColumna(String nomColumna) {
		this.nomColumna = nomColumna;
	}

	public BigDecimal getTiempoObjTc() {
		return this.tiempoObjTc;
	}

	public void setTiempoObjTc(BigDecimal tiempoObjTc) {
		this.tiempoObjTc = tiempoObjTc;
	}

	public BigDecimal getTiempoObjTe() {
		return this.tiempoObjTe;
	}

	public void setTiempoObjTe(BigDecimal tiempoObjTe) {
		this.tiempoObjTe = tiempoObjTe;
	}

	public BigDecimal getTiempoPreTc() {
		return this.tiempoPreTc;
	}

	public void setTiempoPreTc(BigDecimal tiempoPreTc) {
		this.tiempoPreTc = tiempoPreTc;
	}

	public BigDecimal getTiempoPreTe() {
		return this.tiempoPreTe;
	}

	public void setTiempoPreTe(BigDecimal tiempoPreTe) {
		this.tiempoPreTe = tiempoPreTe;
	}

	public String getTipoFlujo() {
		return this.tipoFlujo;
	}

	public void setTipoFlujo(String tipoFlujo) {
		this.tipoFlujo = tipoFlujo;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public TipoOferta getTipoOferta() {
		return this.tipoOferta;
	}

	public void setTipoOferta(TipoOferta tipoOferta) {
		this.tipoOferta = tipoOferta;
	}

	public Tarea getTarea() {
		return this.tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}   	
}
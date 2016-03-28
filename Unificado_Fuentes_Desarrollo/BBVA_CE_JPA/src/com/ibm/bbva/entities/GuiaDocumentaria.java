package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;

/**
 * The persistent class for the TBL_CE_IBM_GUIA_DOCUMENTARIA database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_GUIA_DOCUMENTARIA", schema = "CONELE")

@NamedQueries({
	@NamedQuery(name="GuiaDocumentaria.findAll", query="SELECT g FROM GuiaDocumentaria g ORDER BY g.id"),
	
	/*@NamedQuery(name="GuiaDocumentaria.findGuiaDoc", query="SELECT g FROM GuiaDocumentaria g " +
			"WHERE g.producto.id = :producto " +
			"and g.subproducto.id = :subproducto " +
			"and g.tipoOferta.id = :tipoOferta " +
			"and g.flagCliente = :flagCliente " +
			"and g.flagPagoHab = :flagPagoHab " +
			"and g.flagPep = :flagPep " +
			"and g.flagTasaEsp = :flagTasaEsp " +
			"and g.flagVerifDom = :flagVerifDom " +
			"and g.flagVerifLab = :flagVerifLab ")*/
	
	@NamedQuery(name="GuiaDocumentaria.findGuiaDoc", query="SELECT g FROM GuiaDocumentaria g " +
			"WHERE g.producto.id = :producto " +			
			"and g.tipoOferta.id = :tipoOferta " +
			"and g.flagCliente = :flagCliente " +
			"and g.flagPagoHab = :flagPagoHab " +
			"and g.flagPep = :flagPep " +
			"and g.flagTasaEsp = :flagTasaEsp " +
			"and g.flagVerifDom = :flagVerifDom " +
			"and g.flagVerifLab = :flagVerifLab ")
})

public class GuiaDocumentaria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_GUIA_DOCUMENTARIA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_GUIA_DOCUMENTARIA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_GUIA_DOCUMENTARIA_ID_GENERATOR")
	private long id;

	private String descripcion;

	@Column(name="FLAG_CLIENTE")
	private String flagCliente;

	@Column(name="FLAG_COMITE_RIESGO")
	private String flagComiteRiesgo;

	@Column(name="FLAG_PAGO_HAB")
	private String flagPagoHab;

	@Column(name="FLAG_PEP")
	private String flagPep;

	@Column(name="FLAG_TASA_ESP")
	private String flagTasaEsp;

	@Column(name="FLAG_VERIF_DOM")
	private String flagVerifDom;

	@Column(name="FLAG_VERIF_LAB")
	private String flagVerifLab;
	
	@Column(name="FLAG_DPS")
	private String flagDps;		

	@Column(name="FLAG_ACTIVO")
	private String flagActivo;	
	
	private String obligatorio;
	
	
	//uni-directional many-to-one association to CategoriaRenta
	@ManyToOne
	@JoinColumn(name="ID_CATEGORIA_RENTA_FK")
	private CategoriaRenta categoriaRenta;

	//uni-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="ID_PERSONA_FK")
	private Persona persona;

	//uni-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;
	
	@ManyToOne
	@JoinColumn(name="ID_SUBPRODUCTO_FK")
	private Subproducto subproducto;

	//uni-directional many-to-one association to Subproducto
	/*@ManyToOne
	@JoinColumn(name="ID_SUBPRODUCTO_FK")
	private Subproducto subproducto;*/

	//uni-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;

	//uni-directional many-to-one association to TipoDocumento
	@ManyToOne
	@JoinColumn(name="ID_TIPO_DOCUMENTO_FK")
	private TipoDocumento tipoDocumento;

	//uni-directional many-to-one association to TipoOferta
	@ManyToOne
	@JoinColumn(name="ID_TIPO_OFERTA_FK")
	private TipoOferta tipoOferta;
	
	@Transient
	private Timestamp feRegistro;
	
	@Transient
	private long idCm;
	
	@Transient
	private String flagEscaneado;

	public GuiaDocumentaria() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFlagCliente() {
		return this.flagCliente;
	}

	public void setFlagCliente(String flagCliente) {
		this.flagCliente = flagCliente;
	}

	public String getFlagComiteRiesgo() {
		return this.flagComiteRiesgo;
	}

	public void setFlagComiteRiesgo(String flagComiteRiesgo) {
		this.flagComiteRiesgo = flagComiteRiesgo;
	}

	public String getFlagPagoHab() {
		return this.flagPagoHab;
	}

	public void setFlagPagoHab(String flagPagoHab) {
		this.flagPagoHab = flagPagoHab;
	}

	public String getFlagPep() {
		return this.flagPep;
	}

	public void setFlagPep(String flagPep) {
		this.flagPep = flagPep;
	}

	public String getFlagTasaEsp() {
		return this.flagTasaEsp;
	}

	public void setFlagTasaEsp(String flagTasaEsp) {
		this.flagTasaEsp = flagTasaEsp;
	}

	public String getFlagVerifDom() {
		return this.flagVerifDom;
	}

	public void setFlagVerifDom(String flagVerifDom) {
		this.flagVerifDom = flagVerifDom;
	}

	public String getFlagVerifLab() {
		return this.flagVerifLab;
	}

	public void setFlagVerifLab(String flagVerifLab) {
		this.flagVerifLab = flagVerifLab;
	}

	public String getObligatorio() {
		return this.obligatorio;
	}

	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}

	public CategoriaRenta getCategoriaRenta() {
		return this.categoriaRenta;
	}

	public void setCategoriaRenta(CategoriaRenta categoriaRenta) {
		this.categoriaRenta = categoriaRenta;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	/*public Subproducto getSubproducto() {
		return this.subproducto;
	}

	public void setSubproducto(Subproducto subproducto) {
		this.subproducto = subproducto;
	}*/

	public Tarea getTarea() {
		return this.tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public TipoDocumento getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public TipoOferta getTipoOferta() {
		return this.tipoOferta;
	}

	public void setTipoOferta(TipoOferta tipoOferta) {
		this.tipoOferta = tipoOferta;
	}

	public String getFlagDps() {
		return flagDps;
	}

	public void setFlagDps(String flagDps) {
		this.flagDps = flagDps;
	}

	public Timestamp getFeRegistro() {
		return feRegistro;
	}

	public void setFeRegistro(Timestamp feRegistro) {
		this.feRegistro = feRegistro;
	}

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public long getIdCm() {
		return idCm;
	}

	public void setIdCm(long idCm) {
		this.idCm = idCm;
	}

	public String getFlagEscaneado() {
		return flagEscaneado;
	}

	public void setFlagEscaneado(String flagEscaneado) {
		this.flagEscaneado = flagEscaneado;
	}

	public Subproducto getSubproducto() {
		return subproducto;
	}

	public void setSubproducto(Subproducto subproducto) {
		this.subproducto = subproducto;
	}
	

}
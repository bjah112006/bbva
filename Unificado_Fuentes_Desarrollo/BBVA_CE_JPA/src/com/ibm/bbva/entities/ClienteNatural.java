package com.ibm.bbva.entities;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TBL_CE_IBM_CLIENTE_NATURAL database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_CLIENTE_NATURAL", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="ClienteNatural.findAll", query="SELECT c FROM ClienteNatural c"),
	@NamedQuery(name="ClienteNatural.findById", query="SELECT c FROM ClienteNatural c WHERE c.id = :id"),
	@NamedQuery(name="ClienteNatural.findByTipoNumDoi", query="SELECT c FROM ClienteNatural c WHERE c.tipoDoi.id = :idTipoDoi and c.numDoi = :numDoi")
	
})
public class ClienteNatural implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_CLIENTE_NATURAL_ID_GENERATOR", sequenceName="SEQ_CE_IBM_CLIENTE_NATURAL", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_CLIENTE_NATURAL_ID_GENERATOR")
	private long id;

	@Column(name="APE_MAT")
	private String apeMat;

	@Column(name="APE_PAT")
	private String apePat;

	private String aval;

	private String celular;

	@Column(name="COD_CLIENTE")
	private String codCliente;

	private String correo;

	@Temporal(TemporalType.DATE)
	@Column(name="FEC_VEN_DOI")
	private Date fecVenDoi;

	@Column(name="ING_NETO_MENSUAL")
	private double ingNetoMensual;

	private String nombre;

	@Column(name="NUM_DOI")
	private String numDoi;

	@Column(name="PAGO_HAB")
	private String pagoHab;

	@Column(name="PER_EXP_PUB")
	private String perExpPub;

	private String subrog;

	//bi-directional many-to-one association to ClienteCatrenta
	@ManyToMany(fetch = EAGER)
	@JoinTable(name = "TBL_CE_IBM_CLIENTE_CATRENTA", joinColumns = { @JoinColumn(name = "ID_CLIENTE_NATURAL_FK", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "ID_CATEGORIA_RENTA_FK", referencedColumnName = "ID") })
	private List<CategoriaRenta> categoriasRenta;

	//uni-directional many-to-one association to EstadoCivil
	@ManyToOne
	@JoinColumn(name="ID_ESTADO_CIVIL_FK")
	private EstadoCivil estadoCivil;

	//uni-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="ID_PERSONA_FK")
	private Persona persona;

	//uni-directional many-to-one association to Segmento
	@ManyToOne
	@JoinColumn(name="ID_SEGMENTO_FK")
	private Segmento segmento;

	//uni-directional many-to-one association to TipoCliente
	@ManyToOne
	@JoinColumn(name="ID_TIPO_CLIENTE_FK")
	private TipoCliente tipoCliente;

	//uni-directional many-to-one association to TipoDoi
	@ManyToOne
	@JoinColumn(name="ID_TIPO_DOI_FK")
	private TipoDoi tipoDoi;

	//bi-directional many-to-one association to Expediente
	@OneToMany(mappedBy="clienteNatural")
	private List<Expediente> expedientes;

	public ClienteNatural() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApeMat() {
		return this.apeMat;
	}

	public void setApeMat(String apeMat) {
		this.apeMat = apeMat;
	}

	public String getApePat() {
		return this.apePat;
	}

	public void setApePat(String apePat) {
		this.apePat = apePat;
	}

	public String getAval() {
		return this.aval;
	}

	public void setAval(String aval) {
		this.aval = aval;
	}

	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCodCliente() {
		return this.codCliente;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Date getFecVenDoi() {
		return this.fecVenDoi;
	}

	public void setFecVenDoi(Date fecVenDoi) {
		this.fecVenDoi = fecVenDoi;
	}

	public double getIngNetoMensual() {
		return this.ingNetoMensual;
	}

	public void setIngNetoMensual(double ingNetoMensual) {
		this.ingNetoMensual = ingNetoMensual;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNumDoi() {
		return this.numDoi;
	}

	public void setNumDoi(String numDoi) {
		this.numDoi = numDoi;
	}

	public String getPagoHab() {
		return this.pagoHab;
	}

	public void setPagoHab(String pagoHab) {
		this.pagoHab = pagoHab;
	}

	public String getPerExpPub() {
		return this.perExpPub;
	}

	public void setPerExpPub(String perExpPub) {
		this.perExpPub = perExpPub;
	}

	public String getSubrog() {
		return this.subrog;
	}

	public void setSubrog(String subrog) {
		this.subrog = subrog;
	}

	public List<CategoriaRenta> getCategoriasRenta() {
		return this.categoriasRenta;
	}

	public void setCategoriasRenta(List<CategoriaRenta> categoriasRenta) {
		this.categoriasRenta = categoriasRenta;
	}

	public EstadoCivil getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Segmento getSegmento() {
		return this.segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}

	public TipoCliente getTipoCliente() {
		return this.tipoCliente;
	}

	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public TipoDoi getTipoDoi() {
		return this.tipoDoi;
	}

	public void setTipoDoi(TipoDoi tipoDoi) {
		this.tipoDoi = tipoDoi;
	}

	public List<Expediente> getExpedientes() {
		return this.expedientes;
	}

	public void setExpedientes(List<Expediente> expedientes) {
		this.expedientes = expedientes;
	}

	public Expediente addExpediente(Expediente expediente) {
		getExpedientes().add(expediente);
		expediente.setClienteNatural(this);

		return expediente;
	}

	public Expediente removeExpediente(Expediente expediente) {
		getExpedientes().remove(expediente);
		expediente.setClienteNatural(null);

		return expediente;
	}

}
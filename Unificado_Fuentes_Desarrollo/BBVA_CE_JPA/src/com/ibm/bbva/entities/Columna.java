package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_CE_IBM_COLUMNA database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_COLUMNA", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name="Columna.findAll", query="SELECT c FROM Columna c"),
	@NamedQuery(name = "Columna.findByIdTabla", query = "SELECT c FROM Columna c WHERE c.tabla.id = :idTabla") 
})


public class Columna implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_COLUMNA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_COLUMNA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_COLUMNA_ID_GENERATOR")
	private long id;

	private String busqueda;

	@Column(name="ES_LLAVE_FORANEA")
	private String esLlaveForanea;

	@Column(name="ES_LLAVE_PRIMARIA")
	private String esLlavePrimaria;

	@Column(name="ES_REQUERIDO")
	private String esRequerido;

	@Column(name="LONGITUD_MAXIMA")
	private BigDecimal longitudMaxima;

	private String nombre;

	@Column(name="NOMBRE_MOSTRAR")
	private String nombreMostrar;

	@Column(name="ORDEN_COLUMNA")
	private BigDecimal ordenColumna;

	@Column(name="TIPO_DATO")
	private String tipoDato;

	//bi-directional many-to-one association to Tabla
	@ManyToOne
	@JoinColumn(name="ID_TABLA")
	private Tabla tabla;

	//bi-directional many-to-one association to PosibleValor
	@OneToMany(mappedBy="columna")
	private List<PosibleValor> posiblesValores;

	//bi-directional many-to-one association to ValoresForaneo
	@OneToMany(mappedBy="columna")
	private List<ValoresForaneo> valoresForaneos;

	public Columna() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBusqueda() {
		return this.busqueda;
	}

	public void setBusqueda(String busqueda) {
		this.busqueda = busqueda;
	}

	public String getEsLlaveForanea() {
		return this.esLlaveForanea;
	}

	public void setEsLlaveForanea(String esLlaveForanea) {
		this.esLlaveForanea = esLlaveForanea;
	}

	public String getEsLlavePrimaria() {
		return this.esLlavePrimaria;
	}

	public void setEsLlavePrimaria(String esLlavePrimaria) {
		this.esLlavePrimaria = esLlavePrimaria;
	}

	public String getEsRequerido() {
		return this.esRequerido;
	}

	public void setEsRequerido(String esRequerido) {
		this.esRequerido = esRequerido;
	}

	public BigDecimal getLongitudMaxima() {
		return this.longitudMaxima;
	}

	public void setLongitudMaxima(BigDecimal longitudMaxima) {
		this.longitudMaxima = longitudMaxima;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreMostrar() {
		return this.nombreMostrar;
	}

	public void setNombreMostrar(String nombreMostrar) {
		this.nombreMostrar = nombreMostrar;
	}

	public BigDecimal getOrdenColumna() {
		return this.ordenColumna;
	}

	public void setOrdenColumna(BigDecimal ordenColumna) {
		this.ordenColumna = ordenColumna;
	}

	public String getTipoDato() {
		return this.tipoDato;
	}

	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
	}

	public Tabla getTabla() {
		return this.tabla;
	}

	public void setTabla(Tabla tabla) {
		this.tabla = tabla;
	}

	public List<PosibleValor> getPosiblesValores() {
		return this.posiblesValores;
	}

	public void setPosiblesValores(List<PosibleValor> posiblesValores) {
		this.posiblesValores = posiblesValores;
	}

	public PosibleValor addPosiblesValore(PosibleValor posiblesValore) {
		getPosiblesValores().add(posiblesValore);
		posiblesValore.setColumna(this);

		return posiblesValore;
	}

	public PosibleValor removePosiblesValore(PosibleValor posiblesValore) {
		getPosiblesValores().remove(posiblesValore);
		posiblesValore.setColumna(null);

		return posiblesValore;
	}

	public List<ValoresForaneo> getValoresForaneos() {
		return this.valoresForaneos;
	}

	public void setValoresForaneos(List<ValoresForaneo> valoresForaneos) {
		this.valoresForaneos = valoresForaneos;
	}

	public ValoresForaneo addValoresForaneo(ValoresForaneo valoresForaneo) {
		getValoresForaneos().add(valoresForaneo);
		valoresForaneo.setColumna(this);

		return valoresForaneo;
	}

	public ValoresForaneo removeValoresForaneo(ValoresForaneo valoresForaneo) {
		getValoresForaneos().remove(valoresForaneo);
		valoresForaneo.setColumna(null);

		return valoresForaneo;
	}

}
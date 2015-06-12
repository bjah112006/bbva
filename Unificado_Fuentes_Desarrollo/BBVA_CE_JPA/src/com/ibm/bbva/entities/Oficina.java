package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_CE_IBM_OFICINA_CE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_OFICINA_CE", schema = "CONELE")

@NamedQueries({
	@NamedQuery(name="Oficina.findAll", query="SELECT o FROM Oficina o ORDER BY o.descripcion"),
	@NamedQuery(name="Oficina.findById", query="SELECT o FROM Oficina o WHERE o.id = :id  ORDER BY o.descripcion"),
	@NamedQuery(name="Oficina.findByCodigo", query="SELECT o FROM Oficina o WHERE o.codigo = :codigo  ORDER BY o.descripcion"),
	@NamedQuery(name="Oficina.findByIdTerritorio", query="SELECT o FROM Oficina o WHERE o.territorio.id = :idTerritorio ORDER BY o.descripcion")
})

public class Oficina implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_OFICINA_CE_ID_GENERATOR", sequenceName="SEQ_CE_IBM_OFICINA_CE", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_OFICINA_CE_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	@Column(name="FLAG_AREA_RIESGO")
	private String flagAreaRiesgo;

	@Column(name="FLAG_DESPLAZADA")
	private String flagDesplazada;

	@Column(name="FLAG_ESCANEO_WEB")
	private String flagEscaneoWeb;

	@Column(name="MONTO_TOPE")
	private double montoTope;

	@Column(name="TASA_TRANSF")
	private BigDecimal tasaTransf;

	private String ubicacion;
	
	@Column(name="FLAG_ACTIVO")
	private String flagActivo;

	//bi-directional many-to-one association to Empleado
	@OneToMany(mappedBy="oficina")
	private List<Empleado> empleados;

	//bi-directional many-to-one association to HorarioOficina
	@OneToMany(mappedBy="oficina")
	private List<HorarioOficina> horarioOficinas;

	//bi-directional many-to-one association to Oficina
	@ManyToOne
	@JoinColumn(name="ID_OFICINA_PRINCIPAL_FK")
	private Oficina oficinaPrincipal;

	//bi-directional many-to-one association to Oficina
	@OneToMany(mappedBy="oficinaPrincipal")
	private List<Oficina> oficinas;

	//bi-directional many-to-one association to Territorio
	@ManyToOne
	@JoinColumn(name="ID_TERRITORIO_FK")
	private Territorio territorio;

	public Oficina() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFlagAreaRiesgo() {
		return this.flagAreaRiesgo;
	}

	public void setFlagAreaRiesgo(String flagAreaRiesgo) {
		this.flagAreaRiesgo = flagAreaRiesgo;
	}

	public String getFlagDesplazada() {
		return this.flagDesplazada;
	}

	public void setFlagDesplazada(String flagDesplazada) {
		this.flagDesplazada = flagDesplazada;
	}

	public String getFlagEscaneoWeb() {
		return this.flagEscaneoWeb;
	}

	public void setFlagEscaneoWeb(String flagEscaneoWeb) {
		this.flagEscaneoWeb = flagEscaneoWeb;
	}

	public double getMontoTope() {
		return this.montoTope;
	}

	public void setMontoTope(double montoTope) {
		this.montoTope = montoTope;
	}

	public BigDecimal getTasaTransf() {
		return this.tasaTransf;
	}

	public void setTasaTransf(BigDecimal tasaTransf) {
		this.tasaTransf = tasaTransf;
	}

	public String getUbicacion() {
		return this.ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public List<Empleado> getEmpleados() {
		return this.empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}

	public Empleado addEmpleado(Empleado empleado) {
		getEmpleados().add(empleado);
		empleado.setOficina(this);

		return empleado;
	}

	public Empleado removeEmpleado(Empleado empleado) {
		getEmpleados().remove(empleado);
		empleado.setOficina(null);

		return empleado;
	}

	public List<HorarioOficina> getHorarioOficinas() {
		return this.horarioOficinas;
	}

	public void setHorarioOficinas(List<HorarioOficina> horarioOficinas) {
		this.horarioOficinas = horarioOficinas;
	}

	public HorarioOficina addHorarioOficina(HorarioOficina horarioOficina) {
		getHorarioOficinas().add(horarioOficina);
		horarioOficina.setOficina(this);

		return horarioOficina;
	}

	public HorarioOficina removeHorarioOficina(HorarioOficina horarioOficina) {
		getHorarioOficinas().remove(horarioOficina);
		horarioOficina.setOficina(null);

		return horarioOficina;
	}

	public Oficina getOficinaPrincipal() {
		return this.oficinaPrincipal;
	}

	public void setOficinaPrincipal(Oficina oficinaPrincipal) {
		this.oficinaPrincipal = oficinaPrincipal;
	}

	public List<Oficina> getOficinas() {
		return this.oficinas;
	}

	public void setOficinas(List<Oficina> oficinas) {
		this.oficinas = oficinas;
	}

	public Oficina addOficina(Oficina oficina) {
		getOficinas().add(oficina);
		oficina.setOficinaPrincipal(this);

		return oficina;
	}

	public Oficina removeOficina(Oficina oficina) {
		getOficinas().remove(oficina);
		oficina.setOficinaPrincipal(null);

		return oficina;
	}

	public Territorio getTerritorio() {
		return this.territorio;
	}

	public void setTerritorio(Territorio territorio) {
		this.territorio = territorio;
	}

}
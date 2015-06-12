package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TBL_CE_IBM_PERFIL database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_PERFIL_CE", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name = "Perfil.findAll", query = "SELECT p FROM Perfil p ORDER BY p.descripcion"),
	//@NamedQuery(name = "Perfil.obtenerExisteCarterizacion", query = "SELECT DISTINCT c.empleado.perfil FROM Carterizacion c WHERE c.empleado.perfil.id IN (2,3,5,8,10) AND c.territorio.id = :idTerritorio"),
	@NamedQuery(name = "Perfil.findIdCaract", query = "SELECT DISTINCT e.empleado.perfil FROM CartEmpleadoCE e WHERE e.carterizacionCE.id =:idCaract AND e.empleado.perfil.id IN (2,3,5,8,10) AND e.flagActivo=:flagActivo"),
	
	@NamedQuery(name = "Perfil.findById", query = "SELECT p FROM Perfil p WHERE p.id = :id"),
	@NamedQuery(name = "Perfil.findByIdJefe", query = "SELECT p FROM Perfil p WHERE p.perfilJefe.id = :idPerfilJefe"),
	@NamedQuery(name = "Perfil.findByProcess", query = "SELECT p FROM Perfil p WHERE p.flagProceso=:flagProceso"),
})
public class Perfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_PERFIL_ID_GENERATOR", sequenceName="SEQ_CE_IBM_PERFIL", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_PERFIL_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	@Column(name="FLAG_ADMINISTRACION")
	private String flagAdministracion;

	@Column(name="FLAG_ASIGNACION")
	private String flagAsignacion;

	@Column(name="FLAG_REGISTRA_AYU")
	private String flagRegistraAyu;

	@Column(name="FLAG_REGISTRA_EXP")
	private String flagRegistraExp;

	@Column(name="LISTA_CORREO_JEFES")
	private String listaCorreoJefes;
	
	@Column(name="FLAG_PENDIENTES")
	private String flagPendientes;
	
	@Column(name="FLAG_PROCESO")
	private String flagProceso;
	
	@Column(name="FLAG_MENU_REG_EXPEDIENTE")
	private String flagMenuRegistraExpediente;
	
	@Column(name="FLAG_MENU_BAND_PENDIENTES")
	private String flagMenuBandejaPendientes;
	
	@Column(name="FLAG_MENU_BUSQUEDA")
	private String flagMenuBusqueda;
	
	@Column(name="FLAG_MENU_BAND_HISTORICA")
	private String flagMenuBandejaHistorica;
	
	@Column(name="FLAG_MENU_BAND_ASIGNACION")
	private String flagMenuBandejaAsignacion;
	
	@Column(name="FLAG_MENU_BAND_MANTENIMIENTO")
	private String flagMenuBandejaMantenimiento;
	
	@Column(name="FLAG_MENU_REP_HISTORIAL")
	private String flagMenuReporteHistorial;
	
	@Column(name="FLAG_MENU_REP_CONSOLIDADO")
	private String flagMenuReporteConsolidado;
	
	@Column(name="FLAG_MENU_REP_TOE")
	private String flagMenuReporteTOE;
	
	@Column(name="FLAG_MENU_HORARIO")
	private String flagMenuHorario;
	
	

	//bi-directional many-to-one association to Empleado
	@OneToMany(mappedBy="perfil")
	private List<Empleado> empleados;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="ID_PERFIL_FK")
	private Perfil perfilJefe;

	//bi-directional many-to-one association to Perfil
	@OneToMany(mappedBy="perfilJefe")
	private List<Perfil> perfiles;

	//bi-directional many-to-one association to TareaPerfil
	@OneToMany(mappedBy="perfil")
	private List<TareaPerfil> tareaPerfiles;
	
	

	public Perfil() {
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

	public String getFlagAdministracion() {
		return this.flagAdministracion;
	}

	public void setFlagAdministracion(String flagAdministracion) {
		this.flagAdministracion = flagAdministracion;
	}

	public String getFlagAsignacion() {
		return this.flagAsignacion;
	}

	public void setFlagAsignacion(String flagAsignacion) {
		this.flagAsignacion = flagAsignacion;
	}

	public String getFlagRegistraAyu() {
		return this.flagRegistraAyu;
	}

	public void setFlagRegistraAyu(String flagRegistraAyu) {
		this.flagRegistraAyu = flagRegistraAyu;
	}

	public String getFlagRegistraExp() {
		return this.flagRegistraExp;
	}

	public void setFlagRegistraExp(String flagRegistraExp) {
		this.flagRegistraExp = flagRegistraExp;
	}

	public String getListaCorreoJefes() {
		return this.listaCorreoJefes;
	}

	public void setListaCorreoJefes(String listaCorreoJefes) {
		this.listaCorreoJefes = listaCorreoJefes;
	}

	public List<Empleado> getEmpleados() {
		return this.empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}

	public Empleado addEmpleado(Empleado empleado) {
		getEmpleados().add(empleado);
		empleado.setPerfil(this);

		return empleado;
	}

	public Empleado removeEmpleado(Empleado empleado) {
		getEmpleados().remove(empleado);
		empleado.setPerfil(null);

		return empleado;
	}

	public Perfil getPerfilJefe() {
		return this.perfilJefe;
	}

	public void setPerfilJefe(Perfil perfilJefe) {
		this.perfilJefe = perfilJefe;
	}

	public List<Perfil> getPerfiles() {
		return this.perfiles;
	}

	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}

	public Perfil addPerfile(Perfil perfile) {
		getPerfiles().add(perfile);
		perfile.setPerfilJefe(this);

		return perfile;
	}

	public Perfil removePerfile(Perfil perfile) {
		getPerfiles().remove(perfile);
		perfile.setPerfilJefe(null);

		return perfile;
	}

	public List<TareaPerfil> getTareaPerfiles() {
		return this.tareaPerfiles;
	}

	public void setTareaPerfiles(List<TareaPerfil> tareaPerfiles) {
		this.tareaPerfiles = tareaPerfiles;
	}

	public TareaPerfil addTareaPerfile(TareaPerfil tareaPerfile) {
		getTareaPerfiles().add(tareaPerfile);
		tareaPerfile.setPerfil(this);

		return tareaPerfile;
	}

	public TareaPerfil removeTareaPerfile(TareaPerfil tareaPerfile) {
		getTareaPerfiles().remove(tareaPerfile);
		tareaPerfile.setPerfil(null);

		return tareaPerfile;
	}
	
	public String getFlagPendientes() {
		return flagPendientes;
	}

	public void setFlagPendientes(String flagPendientes) {
		this.flagPendientes = flagPendientes;
	}

	public String getFlagProceso() {
		return flagProceso;
	}

	public void setFlagProceso(String flagProceso) {
		this.flagProceso = flagProceso;
	}

	public String getFlagMenuRegistraExpediente() {
		return flagMenuRegistraExpediente;
	}

	public void setFlagMenuRegistraExpediente(String flagMenuRegistraExpediente) {
		this.flagMenuRegistraExpediente = flagMenuRegistraExpediente;
	}

	public String getFlagMenuBandejaPendientes() {
		return flagMenuBandejaPendientes;
	}

	public void setFlagMenuBandejaPendientes(String flagMenuBandejaPendientes) {
		this.flagMenuBandejaPendientes = flagMenuBandejaPendientes;
	}

	public String getFlagMenuBusqueda() {
		return flagMenuBusqueda;
	}

	public void setFlagMenuBusqueda(String flagMenuBusqueda) {
		this.flagMenuBusqueda = flagMenuBusqueda;
	}

	public String getFlagMenuBandejaHistorica() {
		return flagMenuBandejaHistorica;
	}

	public void setFlagMenuBandejaHistorica(String flagMenuBandejaHistorica) {
		this.flagMenuBandejaHistorica = flagMenuBandejaHistorica;
	}

	public String getFlagMenuBandejaAsignacion() {
		return flagMenuBandejaAsignacion;
	}

	public void setFlagMenuBandejaAsignacion(String flagMenuBandejaAsignacion) {
		this.flagMenuBandejaAsignacion = flagMenuBandejaAsignacion;
	}

	public String getFlagMenuBandejaMantenimiento() {
		return flagMenuBandejaMantenimiento;
	}

	public void setFlagMenuBandejaMantenimiento(String flagMenuBandejaMantenimiento) {
		this.flagMenuBandejaMantenimiento = flagMenuBandejaMantenimiento;
	}

	public String getFlagMenuReporteHistorial() {
		return flagMenuReporteHistorial;
	}

	public void setFlagMenuReporteHistorial(String flagMenuReporteHistorial) {
		this.flagMenuReporteHistorial = flagMenuReporteHistorial;
	}

	public String getFlagMenuReporteConsolidado() {
		return flagMenuReporteConsolidado;
	}

	public void setFlagMenuReporteConsolidado(String flagMenuReporteConsolidado) {
		this.flagMenuReporteConsolidado = flagMenuReporteConsolidado;
	}

	public String getFlagMenuReporteTOE() {
		return flagMenuReporteTOE;
	}

	public void setFlagMenuReporteTOE(String flagMenuReporteTOE) {
		this.flagMenuReporteTOE = flagMenuReporteTOE;
	}

	public String getFlagMenuHorario() {
		return flagMenuHorario;
	}

	public void setFlagMenuHorario(String flagMenuHorario) {
		this.flagMenuHorario = flagMenuHorario;
	}

	
}
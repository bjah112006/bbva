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
	
	@Column(name="FLAG_MENU_DESCARGA_LDAP")
	private String flagMenuDescargaLDAP;
	
	@Column(name="FLAG_MENU_OFICINA_TEMP")
	private String flagMenuOficinaTemporal;
	
	@Column(name="FLAG_MENU_TIPO_DOI")
	private String flagMenuTIPO_DOI;
	
	@Column(name="FLAG_MENU_PRODUCTO_CE")
	private String flagMenuPRODUCTO_CE;
	
	@Column(name="FLAG_MENU_SUBPRODUCTO")
	private String flagMenuSUBPRODUCTO;
	
	@Column(name="FLAG_MENU_TERRITORIO_CE")
	private String flagMenuTERRITORIO_CE;
	
	@Column(name="FLAG_MENU_GUIA_DOCUMENTARIA")
	private String flagMenuGUIA_DOCUMENTARIA;
	
	@Column(name="FLAG_MENU_CARTERIZACION_CE")
	private String flagMenuCARTERIZACION_CE;
	
	@Column(name="FLAG_MENU_DELEGACION_OFICINA")
	private String flagMenuDELEGACION_OFICINA;
	
	@Column(name="FLAG_MENU_TIPO_MONEDA")
	private String flagMenuTIPO_MONEDA;
	
	@Column(name="FLAG_MENU_TIPO_SCORING")
	private String flagMenuTIPO_SCORING;
	
	@Column(name="FLAG_MENU_DELEGACION_RIESGOS")
	private String flagMenuDELEGACION_RIESGOS;
	
	@Column(name="FLAG_MENU_TIPO_BURO")
	private String flagMenuTIPO_BURO;
	
	@Column(name="FLAG_MENU_TIPO_OFERTA")
	private String flagMenuTIPO_OFERTA;
	
	@Column(name="FLAG_MENU_CATEGORIA_RENTA")
	private String flagMenuCATEGORIA_RENTA;
	
	@Column(name="FLAG_MENU_NIVEL")
	private String flagMenuNIVEL;
	
	@Column(name="FLAG_MENU_TIPO_CATEGORIA")
	private String flagMenuTIPO_CATEGORIA;
	
	@Column(name="FLAG_MENU_PERFIL_CE")
	private String flagMenuPERFIL_CE;
	
	@Column(name="FLAG_MENU_OFICINA_CE")
	private String flagMenuOFICINA_CE;
	
	@Column(name="FLAG_MENU_EMPLEADO_CE")
	private String flagMenuEMPLEADO_CE;
	
	@Column(name="FLAG_MENU_TAREA_PERFIL")
	private String flagMenuTAREA_PERFIL;
	
	@Column(name="FLAG_MENU_TIPO_ENVIO")
	private String flagMenuTIPO_ENVIO;
	
	@Column(name="FLAG_MENU_TIPO_DOCUMENTO")
	private String flagMenuTIPO_DOCUMENTO;
	
	@Column(name="FLAG_MENU_MOTIVO_DEVOLUCION")
	private String flagMenuMOTIVO_DEVOLUCION;
	
	@Column(name="FLAG_MENU_SEGMENTO")
	private String flagMenuSEGMENTO;
	
	@Column(name="FLAG_MENU_TOE_PERFIL_ESTADO")
	private String flagMenuTOE_PERFIL_ESTADO;
	
	@Column(name="FLAG_MENU_PARAMETROS_CONF")
	private String flagMenuPARAMETROS_CONF;
	
	@Column(name="FLAG_MENU_LINEA_MAXIMA")
	private String flagMenuLINEA_MAXIMA;
	
	@Column(name="FLAG_MENU_PAUTA_CLASIFICACION")
	private String flagMenuPAUTA_CLASIFICACION;
	
	@Column(name="FLAG_MENU_GARANTIAS")
	private String flagMenuGARANTIAS;
	
	@Column(name="FLAG_MENU_TAREA")
	private String flagMenuTAREA;
	
	@Column(name="FLAG_MENU_CLASIF_BANCO")
	private String flagMenuCLASIF_BANCO;
	
	@Column(name="FLAG_MENU_ESTADO")
	private String flagMenuESTADO;
	
	@Column(name="FLAG_MENU_MONTO_PESO")
	private String flagMenuMONTO_PESO;
	
	@Column(name="FLAG_MENU_CART_TERRITORIO_CE")
	private String flagMenuCART_TERRITORIO_CE;
	
	@Column(name="FLAG_MENU_CART_EMPLEADO_CE")
	private String flagMenuCART_EMPLEADO_CE;
	
	@Column(name="FLAG_MENU_RETRACCION_TAREA")
	private String flagMenuRETRACCION_TAREA;
	
	@Column(name="FLAG_MENU_GRUPO_SEGMENTO")
	private String flagMenuGRUPO_SEGMENTO;
	
	@Column(name="FLAG_MENU_ANS")
	private String flagMenuANS;
	
	@Column(name="FLAG_MENU_DATOS_CORREO")
	private String flagMenuDATOS_CORREO;
	
	@Column(name="FLAG_MENU_MENSAJE_CE")
	private String flagMenuMENSAJE_CE;
	
	@Column(name="FLAG_MENU_DELEG_RIESG_CLASBCO")
	private String flagMenuDELEG_RIESG_CLASBCO;
	
	@Column(name="FLAG_MENU_DELEG_RIESG_COND")
	private String flagMenuDELEG_RIESG_COND;
	
	@Column(name="CODIGO_IDM")
	private String codigoIDM;
	
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

	public String getFlagMenuDescargaLDAP() {
		return flagMenuDescargaLDAP;
	}

	public void setFlagMenuDescargaLDAP(String flagMenuDescargaLDAP) {
		this.flagMenuDescargaLDAP = flagMenuDescargaLDAP;
	}

	public String getFlagMenuTIPO_DOI() {
		return flagMenuTIPO_DOI;
	}

	public void setFlagMenuTIPO_DOI(String flagMenuTIPO_DOI) {
		this.flagMenuTIPO_DOI = flagMenuTIPO_DOI;
	}

	public String getFlagMenuPRODUCTO_CE() {
		return flagMenuPRODUCTO_CE;
	}

	public void setFlagMenuPRODUCTO_CE(String flagMenuPRODUCTO_CE) {
		this.flagMenuPRODUCTO_CE = flagMenuPRODUCTO_CE;
	}

	public String getFlagMenuSUBPRODUCTO() {
		return flagMenuSUBPRODUCTO;
	}

	public void setFlagMenuSUBPRODUCTO(String flagMenuSUBPRODUCTO) {
		this.flagMenuSUBPRODUCTO = flagMenuSUBPRODUCTO;
	}

	public String getFlagMenuTERRITORIO_CE() {
		return flagMenuTERRITORIO_CE;
	}

	public void setFlagMenuTERRITORIO_CE(String flagMenuTERRITORIO_CE) {
		this.flagMenuTERRITORIO_CE = flagMenuTERRITORIO_CE;
	}

	public String getFlagMenuGUIA_DOCUMENTARIA() {
		return flagMenuGUIA_DOCUMENTARIA;
	}

	public void setFlagMenuGUIA_DOCUMENTARIA(String flagMenuGUIA_DOCUMENTARIA) {
		this.flagMenuGUIA_DOCUMENTARIA = flagMenuGUIA_DOCUMENTARIA;
	}

	public String getFlagMenuCARTERIZACION_CE() {
		return flagMenuCARTERIZACION_CE;
	}

	public void setFlagMenuCARTERIZACION_CE(String flagMenuCARTERIZACION_CE) {
		this.flagMenuCARTERIZACION_CE = flagMenuCARTERIZACION_CE;
	}

	public String getFlagMenuDELEGACION_OFICINA() {
		return flagMenuDELEGACION_OFICINA;
	}

	public void setFlagMenuDELEGACION_OFICINA(String flagMenuDELEGACION_OFICINA) {
		this.flagMenuDELEGACION_OFICINA = flagMenuDELEGACION_OFICINA;
	}

	public String getFlagMenuTIPO_MONEDA() {
		return flagMenuTIPO_MONEDA;
	}

	public void setFlagMenuTIPO_MONEDA(String flagMenuTIPO_MONEDA) {
		this.flagMenuTIPO_MONEDA = flagMenuTIPO_MONEDA;
	}

	public String getFlagMenuTIPO_SCORING() {
		return flagMenuTIPO_SCORING;
	}

	public void setFlagMenuTIPO_SCORING(String flagMenuTIPO_SCORING) {
		this.flagMenuTIPO_SCORING = flagMenuTIPO_SCORING;
	}

	public String getFlagMenuDELEGACION_RIESGOS() {
		return flagMenuDELEGACION_RIESGOS;
	}

	public void setFlagMenuDELEGACION_RIESGOS(String flagMenuDELEGACION_RIESGOS) {
		this.flagMenuDELEGACION_RIESGOS = flagMenuDELEGACION_RIESGOS;
	}

	public String getFlagMenuTIPO_BURO() {
		return flagMenuTIPO_BURO;
	}

	public void setFlagMenuTIPO_BURO(String flagMenuTIPO_BURO) {
		this.flagMenuTIPO_BURO = flagMenuTIPO_BURO;
	}

	public String getFlagMenuTIPO_OFERTA() {
		return flagMenuTIPO_OFERTA;
	}

	public void setFlagMenuTIPO_OFERTA(String flagMenuTIPO_OFERTA) {
		this.flagMenuTIPO_OFERTA = flagMenuTIPO_OFERTA;
	}

	public String getFlagMenuCATEGORIA_RENTA() {
		return flagMenuCATEGORIA_RENTA;
	}

	public void setFlagMenuCATEGORIA_RENTA(String flagMenuCATEGORIA_RENTA) {
		this.flagMenuCATEGORIA_RENTA = flagMenuCATEGORIA_RENTA;
	}

	public String getFlagMenuNIVEL() {
		return flagMenuNIVEL;
	}

	public void setFlagMenuNIVEL(String flagMenuNIVEL) {
		this.flagMenuNIVEL = flagMenuNIVEL;
	}

	public String getFlagMenuTIPO_CATEGORIA() {
		return flagMenuTIPO_CATEGORIA;
	}

	public void setFlagMenuTIPO_CATEGORIA(String flagMenuTIPO_CATEGORIA) {
		this.flagMenuTIPO_CATEGORIA = flagMenuTIPO_CATEGORIA;
	}

	public String getFlagMenuPERFIL_CE() {
		return flagMenuPERFIL_CE;
	}

	public void setFlagMenuPERFIL_CE(String flagMenuPERFIL_CE) {
		this.flagMenuPERFIL_CE = flagMenuPERFIL_CE;
	}

	public String getFlagMenuOFICINA_CE() {
		return flagMenuOFICINA_CE;
	}

	public void setFlagMenuOFICINA_CE(String flagMenuOFICINA_CE) {
		this.flagMenuOFICINA_CE = flagMenuOFICINA_CE;
	}

	public String getFlagMenuEMPLEADO_CE() {
		return flagMenuEMPLEADO_CE;
	}

	public void setFlagMenuEMPLEADO_CE(String flagMenuEMPLEADO_CE) {
		this.flagMenuEMPLEADO_CE = flagMenuEMPLEADO_CE;
	}

	public String getFlagMenuTAREA_PERFIL() {
		return flagMenuTAREA_PERFIL;
	}

	public void setFlagMenuTAREA_PERFIL(String flagMenuTAREA_PERFIL) {
		this.flagMenuTAREA_PERFIL = flagMenuTAREA_PERFIL;
	}

	public String getFlagMenuTIPO_ENVIO() {
		return flagMenuTIPO_ENVIO;
	}

	public void setFlagMenuTIPO_ENVIO(String flagMenuTIPO_ENVIO) {
		this.flagMenuTIPO_ENVIO = flagMenuTIPO_ENVIO;
	}

	public String getFlagMenuTIPO_DOCUMENTO() {
		return flagMenuTIPO_DOCUMENTO;
	}

	public void setFlagMenuTIPO_DOCUMENTO(String flagMenuTIPO_DOCUMENTO) {
		this.flagMenuTIPO_DOCUMENTO = flagMenuTIPO_DOCUMENTO;
	}

	public String getFlagMenuMOTIVO_DEVOLUCION() {
		return flagMenuMOTIVO_DEVOLUCION;
	}

	public void setFlagMenuMOTIVO_DEVOLUCION(String flagMenuMOTIVO_DEVOLUCION) {
		this.flagMenuMOTIVO_DEVOLUCION = flagMenuMOTIVO_DEVOLUCION;
	}

	public String getFlagMenuSEGMENTO() {
		return flagMenuSEGMENTO;
	}

	public void setFlagMenuSEGMENTO(String flagMenuSEGMENTO) {
		this.flagMenuSEGMENTO = flagMenuSEGMENTO;
	}

	public String getFlagMenuTOE_PERFIL_ESTADO() {
		return flagMenuTOE_PERFIL_ESTADO;
	}

	public void setFlagMenuTOE_PERFIL_ESTADO(String flagMenuTOE_PERFIL_ESTADO) {
		this.flagMenuTOE_PERFIL_ESTADO = flagMenuTOE_PERFIL_ESTADO;
	}

	public String getFlagMenuPARAMETROS_CONF() {
		return flagMenuPARAMETROS_CONF;
	}

	public void setFlagMenuPARAMETROS_CONF(String flagMenuPARAMETROS_CONF) {
		this.flagMenuPARAMETROS_CONF = flagMenuPARAMETROS_CONF;
	}

	public String getFlagMenuLINEA_MAXIMA() {
		return flagMenuLINEA_MAXIMA;
	}

	public void setFlagMenuLINEA_MAXIMA(String flagMenuLINEA_MAXIMA) {
		this.flagMenuLINEA_MAXIMA = flagMenuLINEA_MAXIMA;
	}

	public String getFlagMenuPAUTA_CLASIFICACION() {
		return flagMenuPAUTA_CLASIFICACION;
	}

	public void setFlagMenuPAUTA_CLASIFICACION(String flagMenuPAUTA_CLASIFICACION) {
		this.flagMenuPAUTA_CLASIFICACION = flagMenuPAUTA_CLASIFICACION;
	}

	public String getFlagMenuGARANTIAS() {
		return flagMenuGARANTIAS;
	}

	public void setFlagMenuGARANTIAS(String flagMenuGARANTIAS) {
		this.flagMenuGARANTIAS = flagMenuGARANTIAS;
	}

	public String getFlagMenuTAREA() {
		return flagMenuTAREA;
	}

	public void setFlagMenuTAREA(String flagMenuTAREA) {
		this.flagMenuTAREA = flagMenuTAREA;
	}

	public String getFlagMenuCLASIF_BANCO() {
		return flagMenuCLASIF_BANCO;
	}

	public void setFlagMenuCLASIF_BANCO(String flagMenuCLASIF_BANCO) {
		this.flagMenuCLASIF_BANCO = flagMenuCLASIF_BANCO;
	}

	public String getFlagMenuESTADO() {
		return flagMenuESTADO;
	}

	public void setFlagMenuESTADO(String flagMenuESTADO) {
		this.flagMenuESTADO = flagMenuESTADO;
	}

	public String getFlagMenuMONTO_PESO() {
		return flagMenuMONTO_PESO;
	}

	public void setFlagMenuMONTO_PESO(String flagMenuMONTO_PESO) {
		this.flagMenuMONTO_PESO = flagMenuMONTO_PESO;
	}

	public String getFlagMenuCART_TERRITORIO_CE() {
		return flagMenuCART_TERRITORIO_CE;
	}

	public void setFlagMenuCART_TERRITORIO_CE(String flagMenuCART_TERRITORIO_CE) {
		this.flagMenuCART_TERRITORIO_CE = flagMenuCART_TERRITORIO_CE;
	}

	public String getFlagMenuCART_EMPLEADO_CE() {
		return flagMenuCART_EMPLEADO_CE;
	}

	public void setFlagMenuCART_EMPLEADO_CE(String flagMenuCART_EMPLEADO_CE) {
		this.flagMenuCART_EMPLEADO_CE = flagMenuCART_EMPLEADO_CE;
	}

	public String getFlagMenuRETRACCION_TAREA() {
		return flagMenuRETRACCION_TAREA;
	}

	public void setFlagMenuRETRACCION_TAREA(String flagMenuRETRACCION_TAREA) {
		this.flagMenuRETRACCION_TAREA = flagMenuRETRACCION_TAREA;
	}

	public String getFlagMenuGRUPO_SEGMENTO() {
		return flagMenuGRUPO_SEGMENTO;
	}

	public void setFlagMenuGRUPO_SEGMENTO(String flagMenuGRUPO_SEGMENTO) {
		this.flagMenuGRUPO_SEGMENTO = flagMenuGRUPO_SEGMENTO;
	}

	public String getFlagMenuANS() {
		return flagMenuANS;
	}

	public void setFlagMenuANS(String flagMenuANS) {
		this.flagMenuANS = flagMenuANS;
	}

	public String getFlagMenuDATOS_CORREO() {
		return flagMenuDATOS_CORREO;
	}

	public void setFlagMenuDATOS_CORREO(String flagMenuDATOS_CORREO) {
		this.flagMenuDATOS_CORREO = flagMenuDATOS_CORREO;
	}

	public String getFlagMenuMENSAJE_CE() {
		return flagMenuMENSAJE_CE;
	}

	public void setFlagMenuMENSAJE_CE(String flagMenuMENSAJE_CE) {
		this.flagMenuMENSAJE_CE = flagMenuMENSAJE_CE;
	}

	public String getFlagMenuOficinaTemporal() {
		return flagMenuOficinaTemporal;
	}

	public void setFlagMenuOficinaTemporal(String flagMenuOficinaTemporal) {
		this.flagMenuOficinaTemporal = flagMenuOficinaTemporal;
	}

	public String getFlagMenuDELEG_RIESG_CLASBCO() {
		return flagMenuDELEG_RIESG_CLASBCO;
	}

	public void setFlagMenuDELEG_RIESG_CLASBCO(String flagMenuDELEG_RIESG_CLASBCO) {
		this.flagMenuDELEG_RIESG_CLASBCO = flagMenuDELEG_RIESG_CLASBCO;
	}

	public String getFlagMenuDELEG_RIESG_COND() {
		return flagMenuDELEG_RIESG_COND;
	}

	public void setFlagMenuDELEG_RIESG_COND(String flagMenuDELEG_RIESG_COND) {
		this.flagMenuDELEG_RIESG_COND = flagMenuDELEG_RIESG_COND;
	}

	public String getCodigoIDM() {
		return codigoIDM;
	}

	public void setCodigoIDM(String codigoIDM) {
		this.codigoIDM = codigoIDM;
	}
	
}
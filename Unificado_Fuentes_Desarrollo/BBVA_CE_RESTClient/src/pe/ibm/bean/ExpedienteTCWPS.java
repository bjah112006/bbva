package pe.ibm.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ExpedienteTCWPS implements Serializable {

	private static final long serialVersionUID = 152885624336730349L;
	
	private String taskID;  //
	private Calendar activado; //
	private String nombreNavegacionWeb;  //
	private String idTarea;  //
	private String codigo; 
	private String estado;  
	private String accion;  
	private String verificacionDomiciliaria;  
	private String modificacionScoring;  
	private String scoringAprobado;  
	private String devueltoPor;  
	private String segmento; 
	private String tipoOferta; 
	private Double lineaCredito;  
	private String moneda;  
	private Cliente cliente;
	private Producto producto;
	private String codigoRVGL;
	private String codigoPreEvaluador;
	private String idOficina;
	private String idTerritorio;
	private String codigoEmpleadoResponsable;
	private String codigoUsuarioActual;
	private String idPerfilUsuarioActual;
	private String perfilUsuarioActual;
	private String nombreUsuarioAnterior;
	private String perfilUsuarioAnterior;
	private String codigoUsuarioAnterior;
	private String numeroContrato;
	
	private String flagEnvioContent = "0";
	
	private String flagProvincia; 
	private Integer numeroDevoluciones;  
	private String flagRetraer; 
	private String idPerfilUsuarioAnterior;
	private String flagSubrogacion;
	private String idTipoOferta;
	private String idGrupoSegmento;
	
	private String desTarea; //
	private Double montoAprobado; 
	private String desOficina;
	private String desTerritorio;
	private String observacion;
	private Date fechaDocumento;
	private String nombreUsuarioActual;
	private String flagEnProcesoTimer;
	
	private int idTareaAnterior;
	private String estadoAnterior;	
	private String nroReintentos; //  ** aplica para tarea: Gestion de servicios web
	private String descripcionError; // ** aplica para tarea: Gestion de servicios web
	private String descripcionErrorUsu; // ** aplica para tarea: Gestion de servicios web
	private String desTareaAnterior; // ** 
	private String cantDocumentos;
	
	private String fechaRestauracion;
	private String fechaCancelacion;
	private String tipoError;
	private String fechaIncidencia;
	
	public int getIdTareaAnterior() {
		return idTareaAnterior;
	}

	public void setIdTareaAnterior(int idTareaAnterior) {
		this.idTareaAnterior = idTareaAnterior;
	}

	public String getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	public String getNroReintentos() {
		return nroReintentos;
	}

	public void setNroReintentos(String nroReintentos) {
		this.nroReintentos = nroReintentos;
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}

	public String getDesTareaAnterior() {
		return desTareaAnterior;
	}

	public void setDesTareaAnterior(String desTareaAnterior) {
		this.desTareaAnterior = desTareaAnterior;
	}
	
	public String getFlagEnvioContent() {
		return flagEnvioContent;
	}

	public void setFlagEnvioContent(String flagEnvioContent) {
		this.flagEnvioContent = flagEnvioContent;
	}

	public String getFlagEnProcesoTimer() {
		return flagEnProcesoTimer;
	}

	public void setFlagEnProcesoTimer(String flagEnProcesoTimer) {
		this.flagEnProcesoTimer = flagEnProcesoTimer;
	}

	public String getNombreUsuarioActual() {
		return nombreUsuarioActual;
	}

	public void setNombreUsuarioActual(String nombreUsuarioActual) {
		this.nombreUsuarioActual = nombreUsuarioActual;
	}

	public String getIdTipoOferta() {
		return idTipoOferta;
	}

	public void setIdTipoOferta(String idTipoOferta) {
		this.idTipoOferta = idTipoOferta;
	}

	public String getIdGrupoSegmento() {
		return idGrupoSegmento;
	}

	public void setIdGrupoSegmento(String idGrupoSegmento) {
		this.idGrupoSegmento = idGrupoSegmento;
	}
	
	public String getPerfilUsuarioActual() {
		return perfilUsuarioActual;
	}

	public void setPerfilUsuarioActual(String perfilUsuarioActual) {
		this.perfilUsuarioActual = perfilUsuarioActual;
	}
	
	public String getFlagRetraer() {
		return flagRetraer;
	}

	public void setFlagRetraer(String flagRetraer) {
		this.flagRetraer = flagRetraer;
	}

	public Integer getNumeroDevoluciones() {
		return numeroDevoluciones;
	}

	public void setNumeroDevoluciones(Integer numeroDevoluciones) {
		this.numeroDevoluciones = numeroDevoluciones;
	}

	public String getFlagProvincia() {
		return flagProvincia;
	}

	public void setFlagProvincia(String flagProvincia) {
		this.flagProvincia = flagProvincia;
	}
	
	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public Calendar getActivado() {
		return activado;
	}

	public void setActivado(Calendar activado) {
		this.activado = activado;
	}

	public String getNombreNavegacionWeb() {
		return nombreNavegacionWeb;
	}

	/**
	 * nombre navegacion
	 */
	public void setNombreNavegacionWeb(String nombreNavegacionWeb) {
		this.nombreNavegacionWeb = nombreNavegacionWeb;
	}
	
	public String getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(String idTarea) {
		this.idTarea = idTarea;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getEstado() {
		return estado;
	}
	
	/**
	 * Asigna el proceso
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getVerificacionDomiciliaria() {
		return verificacionDomiciliaria;
	}

	/**
	 * CU 4
	 * @param verificacionDomiciliaria
	 */
	public void setVerificacionDomiciliaria(String verificacionDomiciliaria) {
		this.verificacionDomiciliaria = verificacionDomiciliaria;
	}

	public String getModificacionScoring() {
		return modificacionScoring;
	}

	public void setModificacionScoring(String modificacionScoring) {
		this.modificacionScoring = modificacionScoring;
	}

	public String getScoringAprobado() {
		return this.scoringAprobado;
	}

	/**
	 * CU 3
	 * @param scoringAprobado
	 */
	public void setScoringAprobado(String scoringAprobado) {
		this.scoringAprobado = scoringAprobado;
	}

	public String getDevueltoPor() {
		return devueltoPor;
	}

	/**
	 * CU 15
	 * @param devueltoPor
	 */
	public void setDevueltoPor(String devueltoPor) {
		this.devueltoPor = devueltoPor;
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public String getTipoOferta() {
		return tipoOferta;
	}

	public void setTipoOferta(String tipoOferta) {
		this.tipoOferta = tipoOferta;
	}

	public Double getLineaCredito() {
		return lineaCredito;
	}

	public void setLineaCredito(Double lineaCredito) {
		this.lineaCredito = lineaCredito;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public String getCodigoRVGL() {
		return codigoRVGL;
	}

	public void setCodigoRVGL(String codigoRVGL) {
		this.codigoRVGL = codigoRVGL;
	}

	public String getCodigoPreEvaluador() {
		return codigoPreEvaluador;
	}

	public void setCodigoPreEvaluador(String codigoPreEvaluador) {
		this.codigoPreEvaluador = codigoPreEvaluador;
	}

	public String getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(String idOficina) {
		this.idOficina = idOficina;
	}

	public String getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(String idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public String getCodigoEmpleadoResponsable() {
		return codigoEmpleadoResponsable;
	}

	public void setCodigoEmpleadoResponsable(String codigoEmpleadoResponsable) {
		this.codigoEmpleadoResponsable = codigoEmpleadoResponsable;
	}

	public String getCodigoUsuarioActual() {
		return codigoUsuarioActual;
	}

	public void setCodigoUsuarioActual(String codigoUsuarioActual) {
		this.codigoUsuarioActual = codigoUsuarioActual;
	}

	public String getIdPerfilUsuarioActual() {
		return idPerfilUsuarioActual;
	}

	public void setIdPerfilUsuarioActual(String idPerfilUsuarioActual) {
		this.idPerfilUsuarioActual = idPerfilUsuarioActual;
	}

	public String getNombreUsuarioAnterior() {
		return nombreUsuarioAnterior;
	}

	public void setNombreUsuarioAnterior(String nombreUsuarioAnterior) {
		this.nombreUsuarioAnterior = nombreUsuarioAnterior;
	}

	public String getPerfilUsuarioAnterior() {
		return perfilUsuarioAnterior;
	}

	public void setPerfilUsuarioAnterior(String perfilUsuarioAnterior) {
		this.perfilUsuarioAnterior = perfilUsuarioAnterior;
	}

	public String getCodigoUsuarioAnterior() {
		return codigoUsuarioAnterior;
	}

	public void setCodigoUsuarioAnterior(String codigoUsuarioAnterior) {
		this.codigoUsuarioAnterior = codigoUsuarioAnterior;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	/*public String getFlagEnvioContent() {
		return flagEnvioContent;
	}
	
	public void setFlagEnvioContent(String flagEnvioContent) {
			this.flagEnvioContent = flagEnvioContent;
			this.envioContent = flagEnvioContent;
	}*/

	public String getIdPerfilUsuarioAnterior() {
		return idPerfilUsuarioAnterior;
	}

	public void setIdPerfilUsuarioAnterior(String idPerfilUsuarioAnterior) {
		this.idPerfilUsuarioAnterior = idPerfilUsuarioAnterior;
	}

	public String getFlagSubrogacion() {
		return flagSubrogacion;
	}

	public void setFlagSubrogacion(String flagSubrogacion) {
		this.flagSubrogacion = flagSubrogacion;
	}

	public String getDesTarea() {
		return desTarea;
	}

	public void setDesTarea(String desTarea) {
		this.desTarea = desTarea;
	}

	public Double getMontoAprobado() {
		return montoAprobado;
	}

	public void setMontoAprobado(Double montoAprobado) {
		this.montoAprobado = montoAprobado;
	}

	public String getDesOficina() {
		return desOficina;
	}

	public void setDesOficina(String desOficina) {
		this.desOficina = desOficina;
	}

	public String getDesTerritorio() {
		return desTerritorio;
	}

	public void setDesTerritorio(String desTerritorio) {
		this.desTerritorio = desTerritorio;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(Date fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}
	
	public String getCantDocumentos() {
		return cantDocumentos;
	}

	public void setCantDocumentos(String cantDocumentos) {
		this.cantDocumentos = cantDocumentos;
	}

	public String getFechaRestauracion() {
		return fechaRestauracion;
	}

	public void setFechaRestauracion(String fechaRestauracion) {
		this.fechaRestauracion = fechaRestauracion;
	}

	public String getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(String fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public String getTipoError() {
		return tipoError;
	}

	public void setTipoError(String tipoError) {
		this.tipoError = tipoError;
	}

	public String getFechaIncidencia() {
		return fechaIncidencia;
	}

	public void setFechaIncidencia(String fechaIncidencia) {
		this.fechaIncidencia = fechaIncidencia;
	}

	public String getDescripcionErrorUsu() {
		return descripcionErrorUsu;
	}

	public void setDescripcionErrorUsu(String descripcionErrorUsu) {
		this.descripcionErrorUsu = descripcionErrorUsu;
	}


	
	/*	
	public String getDescTerritorio() {
		return descTerritorio;
	}

	public void setDescTerritorio(String descTerritorio) {
		this.descTerritorio = descTerritorio;
	}
	*/
	
}

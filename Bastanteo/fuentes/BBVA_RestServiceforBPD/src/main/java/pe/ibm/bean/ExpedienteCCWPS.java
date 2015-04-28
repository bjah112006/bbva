package pe.ibm.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ExpedienteCCWPS implements Serializable {

	private static final long serialVersionUID = 152885624336730349L;
	
	private String taskID;  
	private Calendar activado;  
	
	private String codigoExpediente;
	private String estadoExpediente;
	private String idEstadoExpediente;
	private String numeroTarea;
	private String nombreTarea;
	private String estadoTarea;
	private String idEstadoTarea;
	private String codUsuarioActual;
	private String idUsuarioActual;
	private String nomUsuarioActual;
	private String codOficina;
	private String desOficina;
	private String desTerritorio;
	private String codOperacion;
	private String idOperacion;
	private String desOperacion;
	private String codCentralCliente;
	private String numDOICliente;
	private String razonSocialCliente;
	private DatosFlujoCC datosFlujoCtaCte;
	private OperacionesCC operacionesCtaCte;
	private Date fechaRegistro;  //fecha en la q se registro el exp
	//private Date fechaAtencion; // fecha en la q se atiende la tarea
	private Date fechaUltimoBastanteo;
	
	private String codSemaforo;
	
	private String numeroContrato; //
	private String envioContent;  //
	private String nombreNavegacionWeb;  //
	
	private Date fechaServidorP;
	
	
	
	private String idTarea;
	
	
	public String getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(String idTarea) {
		this.idTarea = idTarea;
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

	public String getCodigoExpediente() {
		return codigoExpediente;
	}

	public void setCodigoExpediente(String codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
	}

	public String getEstadoExpediente() {
		return estadoExpediente;
	}

	public void setEstadoExpediente(String estadoExpediente) {
		this.estadoExpediente = estadoExpediente;
	}

	public String getIdEstadoExpediente() {
		return idEstadoExpediente;
	}

	public void setIdEstadoExpediente(String idEstadoExpediente) {
		this.idEstadoExpediente = idEstadoExpediente;
	}

	public String getNumeroTarea() {
		return numeroTarea;
	}

	public void setNumeroTarea(String numeroTarea) {
		this.numeroTarea = numeroTarea;
	}

	public String getNombreTarea() {
		return nombreTarea;
	}

	public void setNombreTarea(String nombreTarea) {
		this.nombreTarea = nombreTarea;
	}

	public String getEstadoTarea() {
		return estadoTarea;
	}

	public void setEstadoTarea(String estadoTarea) {
		this.estadoTarea = estadoTarea;
	}

	public String getIdEstadoTarea() {
		return idEstadoTarea;
	}

	public void setIdEstadoTarea(String idEstadoTarea) {
		this.idEstadoTarea = idEstadoTarea;
	}

	public String getCodUsuarioActual() {
		return codUsuarioActual;
	}

	public void setCodUsuarioActual(String codUsuarioActual) {
		this.codUsuarioActual = codUsuarioActual;
	}

	public String getIdUsuarioActual() {
		return idUsuarioActual;
	}

	public void setIdUsuarioActual(String idUsuarioActual) {
		this.idUsuarioActual = idUsuarioActual;
	}

	public String getNomUsuarioActual() {
		return nomUsuarioActual;
	}

	public void setNomUsuarioActual(String nomUsuarioActual) {
		this.nomUsuarioActual = nomUsuarioActual;
	}

	public String getCodOficina() {
		return codOficina;
	}

	public void setCodOficina(String codOficina) {
		this.codOficina = codOficina;
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

	public String getCodOperacion() {
		return codOperacion;
	}

	public void setCodOperacion(String codOperacion) {
		this.codOperacion = codOperacion;
	}

	public String getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getDesOperacion() {
		return desOperacion;
	}

	public void setDesOperacion(String desOperacion) {
		this.desOperacion = desOperacion;
	}

	public String getCodCentralCliente() {
		return codCentralCliente;
	}

	public void setCodCentralCliente(String codCentralCliente) {
		this.codCentralCliente = codCentralCliente;
	}

	public String getNumDOICliente() {
		return numDOICliente;
	}

	public void setNumDOICliente(String numDOICliente) {
		this.numDOICliente = numDOICliente;
	}

	public String getRazonSocialCliente() {
		return razonSocialCliente;
	}

	public void setRazonSocialCliente(String razonSocialCliente) {
		this.razonSocialCliente = razonSocialCliente;
	}

	public DatosFlujoCC getDatosFlujoCtaCte() {
		return datosFlujoCtaCte;
	}

	public void setDatosFlujoCtaCte(DatosFlujoCC datosFlujoCtaCte) {
		this.datosFlujoCtaCte = datosFlujoCtaCte;
	}

	public OperacionesCC getOperacionesCtaCte() {
		return operacionesCtaCte;
	}

	public void setOperacionesCtaCte(OperacionesCC operacionesCtaCte) {
		this.operacionesCtaCte = operacionesCtaCte;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaUltimoBastanteo() {
		return fechaUltimoBastanteo;
	}

	public void setFechaUltimoBastanteo(Date fechaUltimoBastanteo) {
		this.fechaUltimoBastanteo = fechaUltimoBastanteo;
	}

	public String getCodSemaforo() {
		return codSemaforo;
	}

	public void setCodSemaforo(String codSemaforo) {
		this.codSemaforo = codSemaforo;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public String getEnvioContent() {
		return envioContent;
	}

	public void setEnvioContent(String envioContent) {
		this.envioContent = envioContent;
	}

	public String getNombreNavegacionWeb() {
		return nombreNavegacionWeb;
	}

	public void setNombreNavegacionWeb(String nombreNavegacionWeb) {
		this.nombreNavegacionWeb = nombreNavegacionWeb;
	}

	public Date getFechaServidorP() {
		return fechaServidorP;
	}

	public void setFechaServidorP(Date fechaServidorP) {
		this.fechaServidorP = fechaServidorP;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}

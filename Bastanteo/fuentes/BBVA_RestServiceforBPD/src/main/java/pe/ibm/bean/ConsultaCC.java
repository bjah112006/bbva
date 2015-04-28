package pe.ibm.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsultaCC implements Serializable{

	private static final long serialVersionUID = -1970097965025569441L;
	 
	private Date fechaInicioInf;
	private Date fechaInicioSup;
	private Date FechaFin;
	
	private String codigoExpediente;
	private String numeroTarea;
	private String estadoTarea;
	private String nombreTarea;
	private String codCentralCliente;
	private String codOperacion;
	private String numDOICliente;
	private String razonSocialCliente;
	private String codUsuarioActual;
	private String idTerritorio;
	private String codOficina;
	private String estadoExpediente;
	private String nomUsuarioAbogado;
	private String codUsuarioResponsable;
	private Date fechaAsignacionInf;
	private Date fechaAsignacionSup;
	private String idEstadoExpediente;
	private String idEstadoTarea;
	private String idOperacion;
	private String idUsuarioAbogado;
	private String codUsuarioAbogado;
	private String idUsuarioActual;
	private String idUsuarioResponsable;
	private String codEstudioAbogado;
	private Date fechaAtencionInf;
	private Date fechaAtencionSup;
	private String idTerritorioTarea;
	private String codOficinaTarea;
	
	private boolean considerarUsuarios;
	
	private List<String> usuarios;
	
	public ConsultaCC() {
		usuarios = new ArrayList<String> (5);
		considerarUsuarios = true;
	}
	
	public Date getFechaAtencionInf() {
		return fechaAtencionInf;
	}

	public void setFechaAtencionInf(Date fechaAtencionInf) {
		this.fechaAtencionInf = fechaAtencionInf;
	}
	
	public Date getFechaAtencionSup() {
		return fechaAtencionSup;
	}

	public void setFechaAtencionSup(Date fechaAtencionSup) {
		this.fechaAtencionSup = fechaAtencionSup;
	}

	public String getCodUsuarioAbogado() {
		return codUsuarioAbogado;
	}

	public void setCodUsuarioAbogado(String codUsuarioAbogado) {
		this.codUsuarioAbogado = codUsuarioAbogado;
	}

//	public Date getFechaInicio() {
//		return fechaInicio;
//	}
//
//	public void setFechaInicio(Date fechaInicio) {
//		this.fechaInicio = fechaInicio;
//	}

	public Date getFechaInicioInf() {
		return fechaInicioInf;
	}

	public void setFechaInicioInf(Date fechaInicioInf) {
		this.fechaInicioInf = fechaInicioInf;
	}

	public Date getFechaInicioSup() {
		return fechaInicioSup;
	}

	public void setFechaInicioSup(Date fechaInicioSup) {
		this.fechaInicioSup = fechaInicioSup;
	}

	public Date getFechaFin() {
		return FechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		FechaFin = fechaFin;
	}

	public String getCodigoExpediente() {
		return codigoExpediente;
	}

	public void setCodigoExpediente(String codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
	}

	public List<String> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<String> usuarios) {
		this.usuarios = usuarios;
	}

	public boolean isConsiderarUsuarios() {
		return considerarUsuarios;
	}

	public void setConsiderarUsuarios(boolean considerarUsuarios) {
		this.considerarUsuarios = considerarUsuarios;
	}

	public String getNumeroTarea() {
		return numeroTarea;
	}

	public void setNumeroTarea(String numeroTarea) {
		this.numeroTarea = numeroTarea;
	}

	public String getEstadoTarea() {
		return estadoTarea;
	}

	public void setEstadoTarea(String estadoTarea) {
		this.estadoTarea = estadoTarea;
	}

	public String getNombreTarea() {
		return nombreTarea;
	}

	public void setNombreTarea(String nombreTarea) {
		this.nombreTarea = nombreTarea;
	}

	public String getCodCentralCliente() {
		return codCentralCliente;
	}

	public void setCodCentralCliente(String codCentralCliente) {
		this.codCentralCliente = codCentralCliente;
	}

	public String getCodOperacion() {
		return codOperacion;
	}

	public void setCodOperacion(String codOperacion) {
		this.codOperacion = codOperacion;
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

	public String getCodUsuarioActual() {
		return codUsuarioActual;
	}

	public void setCodUsuarioActual(String codUsuarioActual) {
		this.codUsuarioActual = codUsuarioActual;
	}
	
	public String getCodOficina() {
		return codOficina;
	}

	public void setCodOficina(String codOficina) {
		this.codOficina = codOficina;
	}

	public String getEstadoExpediente() {
		return estadoExpediente;
	}

	public void setEstadoExpediente(String estadoExpediente) {
		this.estadoExpediente = estadoExpediente;
	}

	public String getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(String idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public String getNomUsuarioAbogado() {
		return nomUsuarioAbogado;
	}

	public void setNomUsuarioAbogado(String nomUsuarioAbogado) {
		this.nomUsuarioAbogado = nomUsuarioAbogado;
	}

	public String getCodUsuarioResponsable() {
		return codUsuarioResponsable;
	}

	public void setCodUsuarioResponsable(String codUsuarioResponsable) {
		this.codUsuarioResponsable = codUsuarioResponsable;
	}

	public Date getFechaAsignacionInf() {
		return fechaAsignacionInf;
	}

	public void setFechaAsignacionInf(Date fechaAsignacionInf) {
		this.fechaAsignacionInf = fechaAsignacionInf;
	}

	public Date getFechaAsignacionSup() {
		return fechaAsignacionSup;
	}

	public void setFechaAsignacionSup(Date fechaAsignacionSup) {
		this.fechaAsignacionSup = fechaAsignacionSup;
	}

	public String getIdEstadoExpediente() {
		return idEstadoExpediente;
	}

	public void setIdEstadoExpediente(String idEstadoExpediente) {
		this.idEstadoExpediente = idEstadoExpediente;
	}

	public String getIdEstadoTarea() {
		return idEstadoTarea;
	}

	public void setIdEstadoTarea(String idEstadoTarea) {
		this.idEstadoTarea = idEstadoTarea;
	}

	public String getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getIdUsuarioAbogado() {
		return idUsuarioAbogado;
	}

	public void setIdUsuarioAbogado(String idUsuarioAbogado) {
		this.idUsuarioAbogado = idUsuarioAbogado;
	}

	public String getIdUsuarioActual() {
		return idUsuarioActual;
	}

	public void setIdUsuarioActual(String idUsuarioActual) {
		this.idUsuarioActual = idUsuarioActual;
	}

	public String getIdUsuarioResponsable() {
		return idUsuarioResponsable;
	}

	public void setIdUsuarioResponsable(String idUsuarioResponsable) {
		this.idUsuarioResponsable = idUsuarioResponsable;
	}

	public String getCodEstudioAbogado() {
		return codEstudioAbogado;
	}

	public void setCodEstudioAbogado(String codEstudioAbogado) {
		this.codEstudioAbogado = codEstudioAbogado;
	}
	public String getIdTerritorioTarea() {
		return idTerritorioTarea;
	}

	public void setIdTerritorioTarea(String idTerritorioTarea) {
		this.idTerritorioTarea = idTerritorioTarea;
	}

	public String getCodOficinaTarea() {
		return codOficinaTarea;
	}

	public void setCodOficinaTarea(String codOficinaTarea) {
		this.codOficinaTarea = codOficinaTarea;
	}	
}

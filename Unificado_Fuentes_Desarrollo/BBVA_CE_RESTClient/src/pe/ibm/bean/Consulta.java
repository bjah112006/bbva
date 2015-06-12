package pe.ibm.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Consulta implements Serializable{

	private static final long serialVersionUID = -1970097965025569441L;
	 
	private Date fechaInicio;
	private Date FechaFin;
	
	private String codigoExpediente;
	private String tipoDOI;
	private String numeroDOI;
	private String apPaterno;
	private String apMaterno;
	private String nombres;
	private String idProducto;
	private String subProducto;
	private String segmento;
	private String codRVGL;
	private String tipoOferta;
	private String idTerritorio;
	private String idOficina;
	private String estado;
	private String codEstado;
	private String numeroContrato;
	private String codPreEvaluador;
	private String codigoCliente;
	private String flagProvincia;
	private String idPerfilUsuarioActual; 
	private String codigoTipoCliente;  
	private String codUsuarioActual;
	private String codTipoFecha;
	private int tipoConsulta;
	private String idTarea;
	
	private String perfilUsuarioSession;
	private String oficinaUsuarioSession;
	private List<Long> listIdsProd;

	public int getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(int tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	
	public String getCodUsuarioActual() {
		return codUsuarioActual;
	}

	public void setCodUsuarioActual(String codUsuarioActual) {
		this.codUsuarioActual = codUsuarioActual;
	}

	public String getIdPerfilUsuarioActual() {
		return idPerfilUsuarioActual;
	}

	public void setIdPerfilUsuarioActual(String idPerfilUsuarioActual) {
		this.idPerfilUsuarioActual = idPerfilUsuarioActual;
	}

	public String getCodigoTipoCliente() {
		return codigoTipoCliente;
	}

	public void setCodigoTipoCliente(String codigoTipoCliente) {
		this.codigoTipoCliente = codigoTipoCliente;
	}

	public String getFlagProvincia() {
		return flagProvincia;
	}

	public void setFlagProvincia(String flagProvincia) {
		this.flagProvincia = flagProvincia;
	}
	
	private boolean considerarUsuarios;
	
	private List<String> usuarios;
	
	public Consulta() {
		usuarios = new ArrayList<String> (5);
		considerarUsuarios = true;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
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

	public String getTipoDOI() {
		return tipoDOI;
	}

	public void setTipoDOI(String tipoDOI) {
		this.tipoDOI = tipoDOI;
	}

	public String getNumeroDOI() {
		return numeroDOI;
	}

	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
	}

	public String getApPaterno() {
		return apPaterno;
	}

	public void setApPaterno(String apPaterno) {
		this.apPaterno = apPaterno;
	}

	public String getApMaterno() {
		return apMaterno;
	}

	public void setApMaterno(String apMaterno) {
		this.apMaterno = apMaterno;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getSubProducto() {
		return subProducto;
	}

	public void setSubProducto(String subProducto) {
		this.subProducto = subProducto;
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public String getCodRVGL() {
		return codRVGL;
	}

	public void setCodRVGL(String codRVGL) {
		this.codRVGL = codRVGL;
	}

	public String getTipoOferta() {
		return tipoOferta;
	}

	public void setTipoOferta(String tipoOferta) {
		this.tipoOferta = tipoOferta;
	}

	public String getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(String idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public String getCodPreEvaluador() {
		return codPreEvaluador;
	}

	public void setCodPreEvaluador(String codPreEvaluador) {
		this.codPreEvaluador = codPreEvaluador;
	}

	public String getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(String idOficina) {
		this.idOficina = idOficina;
	}

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}

	public List<String> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<String> usuarios) {
		this.usuarios = usuarios;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public boolean isConsiderarUsuarios() {
		return considerarUsuarios;
	}

	public void setConsiderarUsuarios(boolean considerarUsuarios) {
		this.considerarUsuarios = considerarUsuarios;
	}

	public String getCodTipoFecha() {
		return codTipoFecha;
	}

	public void setCodTipoFecha(String codTipoFecha) {
		this.codTipoFecha = codTipoFecha;
	}

	public String getCodEstado() {
		return codEstado;
	}

	public void setCodEstado(String codEstado) {
		this.codEstado = codEstado;
	}
	
	public String getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(String idTarea) {
		this.idTarea = idTarea;
	}

	public String getPerfilUsuarioSession() {
		return perfilUsuarioSession;
	}

	public void setPerfilUsuarioSession(String perfilUsuarioSession) {
		this.perfilUsuarioSession = perfilUsuarioSession;
	}

	public List<Long> getListIdsProd() {
		return listIdsProd;
	}

	public void setListIdsProd(List<Long> listIdsProd) {
		this.listIdsProd = listIdsProd;
	}

	public String getOficinaUsuarioSession() {
		return oficinaUsuarioSession;
	}

	public void setOficinaUsuarioSession(String oficinaUsuarioSession) {
		this.oficinaUsuarioSession = oficinaUsuarioSession;
	}
	
}

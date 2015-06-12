package pe.ibm.bean;

import java.io.Serializable;
import java.util.Date;

public class ConsultaServicio implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigo;
	private String tipoDOI;
	private String numeroDOI;
	private String idProducto;
	private String subProducto;
	private String segmento;
	private String tipoOferta;
	private String idTerritorio;
	private String idOficina;
	private String estado;
	private String numeroContrato;
	private String codigoRVGL;
	private String codigoPreEvaluador;
		
	private Date fechaActivacionIni;
	private Date fechaActivacionFin;
	private int tipoConsulta;

	private String idTarea;
	private String idPerfil;
	private String usuario;
	
	public ConsultaServicio() {
		this.codigo = "";
		this.tipoDOI = "";
		this.numeroDOI = "";
		this.idProducto = "";
		this.subProducto = "";
		this.segmento = "";
		this.tipoOferta = "";
		this.idTerritorio = "";
		this.idOficina = "";
		this.estado = "";
		this.numeroContrato = "";
		this.codigoRVGL = "";
		this.codigoPreEvaluador = "";
		this.fechaActivacionIni = null;
		this.fechaActivacionFin = null;
		this.tipoConsulta = 0;
		this.idTarea = "";
		this.idPerfil = "";
		this.usuario = "";
	}

	public String getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(String idTarea) {
		this.idTarea = idTarea;
	}

	public String getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(String idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	
	public String getIdProducto() {
		return idProducto;
	}
	
	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
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
	
	public String getIdOficina() {
		return idOficina;
	}
	
	public void setIdOficina(String idOficina) {
		this.idOficina = idOficina;
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
	
	public Date getFechaActivacionIni() {
		return fechaActivacionIni;
	}
	
	public void setFechaActivacionIni(Date fechaActivacionIni) {
		this.fechaActivacionIni = fechaActivacionIni;
	}
	
	public Date getFechaActivacionFin() {
		return fechaActivacionFin;
	}
	
	public void setFechaActivacionFin(Date fechaActivacionFin) {
		this.fechaActivacionFin = fechaActivacionFin;
	}
	
	public int getTipoConsulta() {
		return tipoConsulta;
	}
	
	public void setTipoConsulta(int tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	
	
}

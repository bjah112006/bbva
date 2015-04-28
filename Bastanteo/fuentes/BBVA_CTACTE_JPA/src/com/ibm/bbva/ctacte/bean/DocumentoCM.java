package com.ibm.bbva.ctacte.bean;

import java.math.BigDecimal;
import java.util.Date;

public class DocumentoCM  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8890242199011952973L;
	private String codCliente;
	private String nroDocumento;
	private String tipoDoi;
	private BigDecimal id;
	private Date fechaExpiracion;
	private Character mandatorio;
	

    private java.lang.String tipo;
    private byte[] contenido;
    private java.lang.String numDoi;
    private java.lang.String nombreArchivo;
    private java.lang.String origen;
    private java.util.Calendar fechaCreacion;
    private java.lang.String urlContent;
	private String tipoArchivo;
	
	private String codigoDocumento;
	private java.lang.String pid;
	private java.lang.String rutaCM;
	
	
	
	public String getCodCliente() {
		return codCliente;
	}
	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}
	public String getNroDocumento() {
		return nroDocumento;
	}
	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	public String getTipoDoi() {
		return tipoDoi;
	}
	public void setTipoDoi(String tipoDoi) {
		this.tipoDoi = tipoDoi;
	}
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public Date getFechaExpiracion() {
		return fechaExpiracion;
	}
	public void setFechaExpiracion(Date fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}
	public Character getMandatorio() {
		return mandatorio;
	}
	public void setMandatorio(Character mandatorio) {
		this.mandatorio = mandatorio;
	}
	public java.lang.String getTipo() {
		return tipo;
	}
	public void setTipo(java.lang.String tipo) {
		this.tipo = tipo;
	}
	public byte[] getContenido() {
		return contenido;
	}
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
	public java.lang.String getNumDoi() {
		return numDoi;
	}
	public void setNumDoi(java.lang.String numDoi) {
		this.numDoi = numDoi;
	}
	public java.lang.String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(java.lang.String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public java.lang.String getOrigen() {
		return origen;
	}
	public void setOrigen(java.lang.String origen) {
		this.origen = origen;
	}
	public java.util.Calendar getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(java.util.Calendar fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public java.lang.String getUrlContent() {
		return urlContent;
	}
	public void setUrlContent(java.lang.String urlContent) {
		this.urlContent = urlContent;
	}
	public String getTipoArchivo() {
		if(this.tipo !=null){
			if(this.tipo.contains(".")){				
				tipoArchivo = this.tipo.substring(this.tipo.indexOf(".")+1).toUpperCase();
			}else{
				tipoArchivo = "PDF";
			}
		}
		return tipoArchivo;
	}
	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}
	public String getCodigoDocumento() {
		if(this.tipo != null && !this.tipo.equals("")){
			if(this.tipo.contains(".")){
				this.codigoDocumento = this.tipo.substring(0, this.tipo.indexOf("."));
			}else{
				this.codigoDocumento = this.tipo;
			}
			
		}else{
			this.codigoDocumento = null;
		}
		
		return codigoDocumento;
	}
	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public java.lang.String getPid() {
		return pid;
	}
	public void setPid(java.lang.String pid) {
		this.pid = pid;
	}
	public java.lang.String getRutaCM() {		
		return rutaCM;
	}
	public void setRutaCM(java.lang.String rutaCM) {
		this.rutaCM = rutaCM;
	}

	

}

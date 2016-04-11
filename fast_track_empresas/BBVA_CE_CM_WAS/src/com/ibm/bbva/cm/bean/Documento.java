package com.ibm.bbva.cm.bean;

import java.util.Calendar;

public class Documento {

	private Integer 	id;
    private String 		tipo;
    private Boolean 	mandatorio;
    private Calendar 	fechaExpiracion;
    private byte[] 		contenido;
    
    //Propiedades agregadas a partir de los requerimientos funcionales
    
    private String 		tipoDoi;
    private String 		numDoi;
    private String 		codCliente;
    
    //Parametros adicionales por Cta Cte
    private Calendar 	fechaCreacion;
    private String 		nombreArchivo;
    private String 		origen;
    
    private String		urlContent;
    
    public Documento() {
		// TODO Apéndice de constructor generado automáticamente
	}
        
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Boolean getMandatorio() {
		return mandatorio;
	}
	public void setMandatorio(Boolean mandatorio) {
		this.mandatorio = mandatorio;
	}
	public byte[] getContenido() {
		return contenido;
	}
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
    public Calendar getFechaExpiracion() {
		return fechaExpiracion;
	}
    public void setFechaExpiracion(Calendar fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	public String getTipoDoi() {
		return tipoDoi;
	}

	public void setTipoDoi(String tipoDoi) {
		this.tipoDoi = tipoDoi;
	}

	public String getNumDoi() {
		return numDoi;
	}

	public void setNumDoi(String numDoi) {
		this.numDoi = numDoi;
	}

	public String getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}
    
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public Calendar getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Calendar fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getUrlContent() {
		return urlContent;
	}

	public void setUrlContent(String urlContent) {
		this.urlContent = urlContent;
	}

	
}

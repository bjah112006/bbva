package com.ibm.bbva.tabla.util.vo;

import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.Territorio;

public class HistorialDetalle {
	
	private Long id;
	private Long idExpediente;
	private String estado;
	private String tipoOferta;
	private String tipoMonedaSolicitada;
	private String tipoMonedaAprobada;
	private String numeroDOI;
	private String nombre;
	private String apPaterno;
	private String apMaterno;
	private String tipoDOI;
	private String segmento;
	private String producto;
	private String subProducto;
	private double lineaCreditoSolicitado;
	private double lineaCreditoAprobado;
	private String codigoRGVL;
 	private String oficina;
	private String territorio;
	private String numeroContrato;
	private String observacion;
	private String fechaRegistro;
    private String correo;
    private String celular;
    
    /**FIX ERIKA ABREGU 05/07/2015
	 */
    private String origen;

	
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdExpediente() {
		return idExpediente;
	}
	public void setIdExpediente(Long idExpediente) {
		this.idExpediente = idExpediente;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTipoOferta() {
		return tipoOferta;
	}
	public void setTipoOferta(String tipoOferta) {
		this.tipoOferta = tipoOferta;
	}
	public String getTipoMonedaSolicitada() {
		return tipoMonedaSolicitada;
	}
	public void setTipoMonedaSolicitada(String tipoMonedaSolicitada) {
		this.tipoMonedaSolicitada = tipoMonedaSolicitada;
	}
	public String getTipoMonedaAprobada() {
		return tipoMonedaAprobada;
	}
	public void setTipoMonedaAprobada(String tipoMonedaAprobada) {
		this.tipoMonedaAprobada = tipoMonedaAprobada;
	}
	public String getNumeroDOI() {
		return numeroDOI;
	}
	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	public String getTipoDOI() {
		return tipoDOI;
	}
	public void setTipoDOI(String tipoDOI) {
		this.tipoDOI = tipoDOI;
	}
	public String getSegmento() {
		return segmento;
	}
	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public String getSubProducto() {
		return subProducto;
	}
	public void setSubProducto(String subProducto) {
		this.subProducto = subProducto;
	}
	public double getLineaCreditoSolicitado() {
		return lineaCreditoSolicitado;
	}
	public void setLineaCreditoSolicitado(double lineaCreditoSolicitado) {
		this.lineaCreditoSolicitado = lineaCreditoSolicitado;
	}
	public double getLineaCreditoAprobado() {
		return lineaCreditoAprobado;
	}
	public void setLineaCreditoAprobado(double lineaCreditoAprobado) {
		this.lineaCreditoAprobado = lineaCreditoAprobado;
	}
	public String getCodigoRGVL() {
		return codigoRGVL;
	}
	public void setCodigoRGVL(String codigoRGVL) {
		this.codigoRGVL = codigoRGVL;
	}
	 
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	public String getTerritorio() {
		return territorio;
	}
	public void setTerritorio(String territorio) {
		this.territorio = territorio;
	}
	public String getNumeroContrato() {
		return numeroContrato;
	}
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
 
	
}

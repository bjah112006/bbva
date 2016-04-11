package com.ibm.bbva.ctacte.bean.servicio.sfp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatosClientePJSFP implements Serializable {

	private static final long serialVersionUID = 5845682667683893484L;
	
	private String codigoCentral;
	private String tipoDOI;
	private String numeroDOI;
	private String partida;
	private String fechaEscritura;
	private String zonaRegistral;
	private String oficinaRegistral;
	private String jurisdiccion;
	private String notario;
	private String objeto;
	private String articulo;
	private String estructura;
	private String nroExpediente;
	private String fechaVersionBastanteo;
	private String razonSocial;
	private String tipoPJ;
	private String estadoPJ; // activo:A, inactivo:I
	private String descEstadoPJ;
	private String nroVersion;
	private String estadoVersion; // pendiente:P, aprobado:A
	private List<RepresentanteSFP> representantes = new ArrayList<RepresentanteSFP>();
	private List<CuentaSFP> cuentasCorriente = new ArrayList<CuentaSFP>();
	//private List<Combinacion> combinaciones;
	//private String pderesEspeciales;
	
	public String getCodigoCentral() {
		return codigoCentral;
	}
	
	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}
	
	public String getRazonSocial() {
		return razonSocial;
	}
	
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	public String getTipoPJ() {
		return tipoPJ;
	}
	
	public void setTipoPJ(String tipoPJ) {
		this.tipoPJ = tipoPJ;
	}
	
	public String getEstadoPJ() {
		return estadoPJ;
	}
	
	public void setEstadoPJ(String estadoPJ) {
		this.estadoPJ = estadoPJ;
	}
	
	public String getNroVersion() {
		return nroVersion;
	}
	
	public void setNroVersion(String nroVersion) {
		this.nroVersion = nroVersion;
	}
	
	public String getEstadoVersion() {
		return estadoVersion;
	}
	
	public void setEstadoVersion(String estadoVersion) {
		this.estadoVersion = estadoVersion;
	}
	
	public List<RepresentanteSFP> getRepresentantes() {
		return representantes;
	}
	
	public void setRepresentantes(List<RepresentanteSFP> representantes) {
		this.representantes = representantes;
	}
	
	public List<CuentaSFP> getCuentasCorriente() {
		return cuentasCorriente;
	}
	
	public void setCuentasCorriente(List<CuentaSFP> cuentasCorriente) {
		this.cuentasCorriente = cuentasCorriente;
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

	public String getPartida() {
		return partida;
	}

	public void setPartida(String partida) {
		this.partida = partida;
	}

	public String getFechaEscritura() {
		return fechaEscritura;
	}

	public void setFechaEscritura(String fechaEscritura) {
		this.fechaEscritura = fechaEscritura;
	}

	public String getZonaRegistral() {
		return zonaRegistral;
	}

	public void setZonaRegistral(String zonaRegistral) {
		this.zonaRegistral = zonaRegistral;
	}

	public String getOficinaRegistral() {
		return oficinaRegistral;
	}

	public void setOficinaRegistral(String oficinaRegistral) {
		this.oficinaRegistral = oficinaRegistral;
	}

	public String getJurisdiccion() {
		return jurisdiccion;
	}

	public void setJurisdiccion(String jurisdiccion) {
		this.jurisdiccion = jurisdiccion;
	}

	public String getNotario() {
		return notario;
	}

	public void setNotario(String notario) {
		this.notario = notario;
	}

	public String getObjeto() {
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	public String getArticulo() {
		return articulo;
	}

	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	public String getEstructura() {
		return estructura;
	}

	public void setEstructura(String estructura) {
		this.estructura = estructura;
	}

	public String getNroExpediente() {
		return nroExpediente;
	}

	public void setNroExpediente(String nroExpediente) {
		this.nroExpediente = nroExpediente;
	}

	public String getFechaVersionBastanteo() {
		return fechaVersionBastanteo;
	}

	public void setFechaVersionBastanteo(String fechaVersionBastanteo) {
		this.fechaVersionBastanteo = fechaVersionBastanteo;
	}

	public void setDescEstadoPJ(String descEstadoPJ) {
		this.descEstadoPJ = descEstadoPJ;
	}

	public String getDescEstadoPJ() {
		return descEstadoPJ;
	}

}

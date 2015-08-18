package pe.ibm.bean;

import java.io.Serializable;

public class ClienteWeb implements Serializable  {

	private static final long serialVersionUID = 4480617796130936716L;

	private String tipoDOI;
	private String numeroDOI;
	private String apPaterno;
	private String apMaterno;
	private String nombre;
	private String codigoCliente;
	private String codigoTipoCliente;  
	private String descripcionTipoCliente;  
	private String nroCelular;  
	private String correo;  


	
	public ClienteWeb(String tipoDOI, String numeroDOI, String apPaterno,
			String apMaterno, String nombre, String codigoCliente,
			String codigoTipoCliente, String descripcionTipoCliente, String correo, String celular) {
		super();
		this.tipoDOI = tipoDOI;
		this.numeroDOI = numeroDOI;
		this.apPaterno = apPaterno;
		this.apMaterno = apMaterno;
		this.nombre = nombre;
		this.codigoCliente = codigoCliente;
		this.codigoTipoCliente = codigoTipoCliente;
		this.descripcionTipoCliente = descripcionTipoCliente;
		this.nroCelular = celular;
		this.correo = correo;
	}

	public ClienteWeb() {
		
		this.tipoDOI = "";
		this.numeroDOI = "";
		this.apPaterno = "";
		this.apMaterno = "";
		this.nombre = "";
		this.codigoCliente = "";
		this.codigoTipoCliente ="";
		this.descripcionTipoCliente = "";
		this.nroCelular="";
		this.correo="";
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getCodigoTipoCliente() {
		return codigoTipoCliente;
	}

	public void setCodigoTipoCliente(String codigoTipoCliente) {
		this.codigoTipoCliente = codigoTipoCliente;
	}

	public String getDescripcionTipoCliente() {
		return descripcionTipoCliente;
	}

	public void setDescripcionTipoCliente(String descripcionTipoCliente) {
		this.descripcionTipoCliente = descripcionTipoCliente;
	}

	public String getNroCelular() {
		return nroCelular;
	}

	public void setNroCelular(String nroCelular) {
		this.nroCelular = nroCelular;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

}

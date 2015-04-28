package com.ibm.bbva.ctacte.controller.comun;

import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ibm.bbva.ctacte.controller.AbstractMBean;

@ManagedBean (name="combinacionDocumento")
@ViewScoped
public class CombinacionDocumentosMB extends AbstractMBean {
	
	private String codigoDocumento;
//	private String codigoDocumento1CM;
	private String idExpediente;
	private String codigoDocumento2CM;
	private String origenMigracionHist;
	private String codigoOperacion;
	private String codigoCentralCliente;
	private String parametros;
	private String tramaDescargaArcHist;
	
	public String getIdExpediente() {
		return idExpediente;
	}

	public void setIdExpediente(String idExpediente) {
		this.idExpediente = idExpediente;
	}
	
	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	@PostConstruct
	public void iniciar () {
//		codigoDocumento = getRequestParameter("codDoc");
//		codigoDocumentoCM = getRequestParameter("codigoDocIDCM");
//		origenMigracionHist = getRequestParameter("migradoHist");
//		codigoOperacion = getRequestParameter("codOperacion");
//		codigoCentralCliente = getRequestParameter("codCentralCliente");
		parametros = getRequestParameter("parametros");
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("parametros -->"+parametros );
		
			String [] campos = parametros.split(";");

		StringTokenizer st = new StringTokenizer (parametros, ";");
		
		codigoDocumento = campos[0];
		idExpediente = campos[1];
//		codigoDocumento2CM = campos[2];
		origenMigracionHist = campos[2];
		if (origenMigracionHist !=null && origenMigracionHist.equalsIgnoreCase("X"))
			origenMigracionHist = "";
//		codigoOperacion = campos[4];
//		codigoCentralCliente = campos[5];
		try{
			tramaDescargaArcHist = campos[3]+";"+campos[4]+";"+campos[5];
		}catch(Exception ex){
			//Archivo Historico ha sido escaneado en esta pantalla
			tramaDescargaArcHist = null;
		}
		
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codigoDocumento-->"+codigoDocumento);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("idExpediente-->"+idExpediente);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codigoDocumentoCM 2-->"+codigoDocumento2CM);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("origenMigracionHist-->"+origenMigracionHist);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codigoOperacion-->"+codigoOperacion);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codigoCentralCliente-->"+codigoCentralCliente);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("tramaDescargaArcHist-->"+tramaDescargaArcHist);
	}


	public String getCodigoDocumento2CM() {
		return codigoDocumento2CM;
	}

	public void setCodigoDocumento2CM(String codigoDocumento2CM) {
		this.codigoDocumento2CM = codigoDocumento2CM;
	}

	public String getOrigenMigracionHist() {
		return origenMigracionHist;
	}

	public void setOrigenMigracionHist(String origenMigracionHist) {
		this.origenMigracionHist = origenMigracionHist;
	}

	public String getCodigoOperacion() {
		return codigoOperacion;
	}

	public void setCodigoOperacion(String codigoOperacion) {
		this.codigoOperacion = codigoOperacion;
	}

	public String getCodigoCentralCliente() {
		return codigoCentralCliente;
	}

	public void setCodigoCentralCliente(String codigoCentralCliente) {
		this.codigoCentralCliente = codigoCentralCliente;
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public String getTramaDescargaArcHist() {
		return tramaDescargaArcHist;
	}

	public void setTramaDescargaArcHist(String tramaDescargaArcHist) {
		this.tramaDescargaArcHist = tramaDescargaArcHist;
	}

}

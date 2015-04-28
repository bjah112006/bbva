package com.ibm.bbva.ctacte.servicio.cm.bean;

import java.io.Serializable;
import java.util.Date;

public class DocumentoExpediente implements Serializable {

	private Integer id;
	private Integer idMotivo;
	private Integer idExpediente;
	private Integer idDocumento;
	private String flagEscaneado;
	private Date fechaEscaneo;
	private Date fechaVigencia;
	private String flagRechazado;
	private String nombreArchivo;
	private String flagCm;
	private Integer idCm;
	private Integer idCmCopia;
	private String flagAlterno;
	private String flagObligatorio;
	private Integer docPesoPromKB;
	private String flagReqEscaneo;
	private String pidCM;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdMotivo() {
		return idMotivo;
	}
	public void setIdMotivo(Integer idMotivo) {
		this.idMotivo = idMotivo;
	}
	public Integer getIdExpediente() {
		return idExpediente;
	}
	public void setIdExpediente(Integer idExpediente) {
		this.idExpediente = idExpediente;
	}
	public Integer getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getFlagEscaneado() {
		return flagEscaneado;
	}
	public void setFlagEscaneado(String flagEscaneado) {
		this.flagEscaneado = flagEscaneado;
	}
	public Date getFechaEscaneo() {
		return fechaEscaneo;
	}
	public void setFechaEscaneo(Date fechaEscaneo) {
		this.fechaEscaneo = fechaEscaneo;
	}
	public Date getFechaVigencia() {
		return fechaVigencia;
	}
	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}
	public String getFlagRechazado() {
		return flagRechazado;
	}
	public void setFlagRechazado(String flagRechazado) {
		this.flagRechazado = flagRechazado;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getFlagCm() {
		return flagCm;
	}
	public void setFlagCm(String flagCm) {
		this.flagCm = flagCm;
	}
	public Integer getIdCm() {
		return idCm;
	}
	public void setIdCm(Integer idCm) {
		this.idCm = idCm;
	}
	public Integer getIdCmCopia() {
		return idCmCopia;
	}
	public void setIdCmCopia(Integer idCmCopia) {
		this.idCmCopia = idCmCopia;
	}
	public String getFlagAlterno() {
		return flagAlterno;
	}
	public void setFlagAlterno(String flagAlterno) {
		this.flagAlterno = flagAlterno;
	}
	public String getFlagObligatorio() {
		return flagObligatorio;
	}
	public void setFlagObligatorio(String flagObligatorio) {
		this.flagObligatorio = flagObligatorio;
	}
	public Integer getDocPesoPromKB() {
		return docPesoPromKB;
	}
	public void setDocPesoPromKB(Integer docPesoPromKB) {
		this.docPesoPromKB = docPesoPromKB;
	}
	public String getFlagReqEscaneo() {
		return flagReqEscaneo;
	}
	public void setFlagReqEscaneo(String flagReqEscaneo) {
		this.flagReqEscaneo = flagReqEscaneo;
	}
	public String getPidCM() {
		return pidCM;
	}
	public void setPidCM(String pidCM) {
		this.pidCM = pidCM;
	}
	
	
}

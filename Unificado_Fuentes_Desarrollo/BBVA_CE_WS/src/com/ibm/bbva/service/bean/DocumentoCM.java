package com.ibm.bbva.service.bean;

import java.math.BigDecimal;
import java.util.Calendar;

public class DocumentoCM {
	private long id; // de la tabla TBL_CE_IBM_DOCUMENTO_EXP_TC va a ser el ID en Content
	private String codCliente;
	private String numDocumento;
	private String tipoDoi;
	private long idExpediente;
	private Calendar fechaExpiracion;
	private Character mandatorio;
	private String pidCm;
	private BigDecimal idCm;
	private Calendar fechaRegistro;
	private String flagCm;
	private String nombreArchivo;
	private long idTipoDocumento;

	public DocumentoCM() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public String getTipoDoi() {
		return tipoDoi;
	}

	public void setTipoDoi(String tipoDoi) {
		this.tipoDoi = tipoDoi;
	}

	public long getIdExpediente() {
		return idExpediente;
	}

	public void setIdExpediente(long idExpediente) {
		this.idExpediente = idExpediente;
	}

	public Calendar getFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(Calendar fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	public Character getMandatorio() {
		return mandatorio;
	}

	public void setMandatorio(Character mandatorio) {
		this.mandatorio = mandatorio;
	}

	public String getPidCm() {
		return pidCm;
	}

	public void setPidCm(String pidCm) {
		this.pidCm = pidCm;
	}

	public BigDecimal getIdCm() {
		return idCm;
	}

	public void setIdCm(BigDecimal idCm) {
		this.idCm = idCm;
	}

	public Calendar getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Calendar fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getFlagCm() {
		return flagCm;
	}

	public void setFlagCm(String flagCm) {
		this.flagCm = flagCm;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public long getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(long idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

}

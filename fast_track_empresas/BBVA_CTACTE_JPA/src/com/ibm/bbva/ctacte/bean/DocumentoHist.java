package com.ibm.bbva.ctacte.bean;
// default package
// Generated 22/05/2012 04:38:22 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DocumentoHist generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_DOCUMENTO_HIST_CC", schema = "CONELE")
public class DocumentoHist implements java.io.Serializable {

	private static final long serialVersionUID = 3814135540352610608L;

	@Id
	@SequenceGenerator(name = "seqDocumentoHistorico", sequenceName = "SEQ_CE_IBM_DOCUMENTO_HIST_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqDocumentoHistorico")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_DOCUMENTO_FK", nullable = false)
	private Documento documento;
	
	@Column(name = "FLAG_ESCANEADO", length = 1)
	private String flagEscaneado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_ESCANEO", length = 7)
	private Date fechaEscaneo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_VIGENCIA", length = 7)
	private Date fechaVigencia;
	
	@Column(name = "NOMBRE_ARCHIVO", length = 14)
	private String nombreArchivo;
	
	@Column(name = "FLAG_CM", length = 1)
	private String flagCm;
	
	@Column(name = "ID_CM", precision = 22, scale = 0)
	private Integer idCm;
	
	@Column(name = "FLAG_ALTERNO", length = 1)
	private String flagAlterno;
	
	@Column(name = "FLAG_OBLIGATORIO", length = 1)
	private String flagObligatorio;
	
	@Column(name = "FLAG_MIGRACION", length = 1)
	private String flagMigracion;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CLIENTE_FK", nullable = false)
	private Cliente cliente;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_EXPEDIENTE_FK", nullable = false)
	private Expediente expediente;
	
	public DocumentoHist() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
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

	public String getFlagMigracion() {
		return flagMigracion;
	}

	public void setFlagMigracion(String flagMigracion) {
		this.flagMigracion = flagMigracion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

}

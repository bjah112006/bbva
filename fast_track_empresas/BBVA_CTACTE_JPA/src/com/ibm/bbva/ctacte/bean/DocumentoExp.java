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
 * DocumentoExp generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_DOCUMENTO_EXP_CC", schema = "CONELE")
public class DocumentoExp implements java.io.Serializable {

	private static final long serialVersionUID = 5208139367006343787L;

	@Id
	@SequenceGenerator(name = "seqDocumentoExp", sequenceName = "SEQ_CE_IBM_DOCUMENTO_EXP_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqDocumentoExp")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_MOTIVO_CC_FK")
	private Motivo motivo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_EXPEDIENTE_CC_FK", nullable = false)
	private Expediente expediente;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_DOCUMENTO_CC_FK", nullable = false)
	private Documento documento;
	
	@Column(name = "FLAG_ESCANEADO", length = 1)
	private String flagEscaneado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_ESCANEO", length = 7)
	private Date fechaEscaneo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_VIGENCIA", length = 7)
	private Date fechaVigencia;
	
	@Column(name = "FLAG_RECHAZADO", length = 1)
	private String flagRechazado;
	
	@Column(name = "NOMBRE_ARCHIVO", length = 14)
	private String nombreArchivo;
	
	@Column(name = "FLAG_CM", length = 1)
	private String flagCm;
	
	@Column(name = "ID_CM", precision = 22, scale = 0)
	private Integer idCm;
	
	@Column(name = "ID_CM_COPIA", precision = 22, scale = 0)
	private Integer idCmCopia;
	
	@Column(name = "FLAG_ALTERNO", length = 1)
	private String flagAlterno;
	
	@Column(name = "FLAG_OBLIGATORIO", length = 1)
	private String flagObligatorio;
	
	@Column(name = "DOC_PESO_PROM_KB", precision = 22, scale = 0)
	private Integer docPesoPromKB;
	
	@Column(name = "FLAG_REQ_ESCANEO", length = 1)
	private String flagReqEscaneo;
	
	@Column(name = "PID_CM", length = 100)
	private String pidCM;
	
	@Column(name = "NRO_PAGINA", precision = 5, scale = 0)
	private Integer nroPagina;
	
	// [Begin]-[15.07.23]-[Cambio valores de los documentos (casos de negocio)]
	@Column(name = "FECHA_REGISTRO", precision = 5, scale = 0)
	private Date fechaRegistro;
	// [End]-[15.07.23]
	
	public DocumentoExp() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
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

	public Integer getNroPagina() {
		return nroPagina;
	}

	public void setNroPagina(Integer nroPagina) {
		this.nroPagina = nroPagina;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}
	
	// [Begin]-[15.07.23]-[Cambio valores de los documentos (casos de negocio)]
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	// [End]-[15.07.23]

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentoExp other = (DocumentoExp) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	
}

package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_CE_IBM_DOCUMENTO_EXP_TC database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_DOCUMENTO_EXP_TC", schema = "CONELE")
@NamedQueries({	
	@NamedQuery(name = "DocumentoExpTc.findAll", query = "SELECT d FROM DocumentoExpTc d"),
	@NamedQuery(name = "DocumentoExpTc.findByExpediente", query = "SELECT d FROM DocumentoExpTc d WHERE d.expediente.id = :id and d.fecVen is null"),
	@NamedQuery(name = "DocumentoExpTc.findById", query = "SELECT d FROM DocumentoExpTc d WHERE d.id = :id"),
	@NamedQuery(name = "DocumentoExpTc.consultarDocumentoExpediente", query = "SELECT d FROM DocumentoExpTc d WHERE d.expediente.id = :idExpediente AND d.tipoDocumento.codigo = :codigoTipoDoc ORDER BY d.persona.id ASC, d.obligatorio DESC"),
	@NamedQuery(name = "DocumentoExpTc.consultarDocumentoExpedienteCM", query = "SELECT d FROM DocumentoExpTc d WHERE d.expediente.id = :idExpediente AND d.tipoDocumento.codigo = :codigoTipoDoc AND d.idCm IS NOT NULL ORDER BY d.id DESC"),
	@NamedQuery(name = "DocumentoExpTc.consultarDocumentoExpedienteByIdCm", query = "SELECT d FROM DocumentoExpTc d WHERE d.expediente.id = :idExpediente AND d.idCm = :idCm ORDER BY d.persona.id ASC, d.obligatorio DESC"),
	@NamedQuery(name = "DocumentoExpTc.findByExpedienteFlagCM", query = "SELECT d FROM DocumentoExpTc d WHERE d.expediente.id = :id AND d.flagCm = :flagCm"),
	@NamedQuery(name = "DocumentoExpTc.findByExpedienteFlagEscaneado", query = "SELECT d FROM DocumentoExpTc d WHERE d.expediente.id = :id AND d.flagEscaneado = :flagEscaneado"),
	@NamedQuery(name = "DocumentoExpTc.findByTipoDocisNullCM", query = "SELECT d FROM DocumentoExpTc d WHERE d.expediente.id = :id AND d.tipoDocumento.id = :idTipoDocumento AND d.idCm IS NULL"),
	@NamedQuery(name = "DocumentoExpTc.consultarUltimoDocumentoExpediente", query = "SELECT d FROM DocumentoExpTc d WHERE d.expediente.id = :idExpediente AND d.tipoDocumento.codigo = :codigoTipoDoc ORDER BY d.fecReg DESC")
})

public class DocumentoExpTc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_DOCUMENTO_EXP_TC_ID_GENERATOR", sequenceName="SEQ_CE_IBM_DOCUMENTO_EXP_TC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_DOCUMENTO_EXP_TC_ID_GENERATOR")
	private long id;

	@Column(name="COD_CLIENTE")
	private String codCliente;

	@Temporal(TemporalType.DATE)
	@Column(name="FEC_REG")
	private Timestamp fecReg;

	@Temporal(TemporalType.DATE)
	@Column(name="FEC_VEN")
	private Date fecVen;

	@Column(name="FLAG_CM")
	private String flagCm;

	@Column(name="FLAG_DOC_REUTILIZABLE")
	private String flagDocReutilizable;

	@Column(name="ID_CM")
	private BigDecimal idCm;

	private String nombre;

	@Column(name="NOMBRE_ARCHIVO")
	private String nombreArchivo;

	@Column(name="NRO_DOI")
	private String nroDoi;

	private String obligatorio;

	@Column(name="PID_CM")
	private String pidCm;
	
	@Column(name="FLAG_OBS")
	private String flagObs;
	
	@Column(name="FLAG_ESCANEADO")
	private String flagEscaneado;

	//bi-directional many-to-one association to Expediente
	@ManyToOne
	@JoinColumn(name="ID_EXPEDIENTE_FK")
	private Expediente expediente;

	//bi-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="ID_PERSONA_FK")
	private Persona persona;

	//bi-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;

	//bi-directional many-to-one association to TipoDocumento
	@ManyToOne
	@JoinColumn(name="ID_TIPO_DOCUMENTO_FK")
	private TipoDocumento tipoDocumento;

	//uni-directional many-to-one association to TipoDoi
	@ManyToOne
	@JoinColumn(name="ID_TIPO_DOI_FK")
	private TipoDoi tipoDoi;

	public DocumentoExpTc() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodCliente() {
		return this.codCliente;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}

	public Timestamp getFecReg() {
		return this.fecReg;
	}

	public void setFecReg(Timestamp fecReg) {
		this.fecReg = fecReg;
	}

	public Date getFecVen() {
		return this.fecVen;
	}

	public void setFecVen(Date fecVen) {
		this.fecVen = fecVen;
	}

	public String getFlagCm() {
		return this.flagCm;
	}

	public void setFlagCm(String flagCm) {
		this.flagCm = flagCm;
	}

	public String getFlagDocReutilizable() {
		return this.flagDocReutilizable;
	}

	public void setFlagDocReutilizable(String flagDocReutilizable) {
		this.flagDocReutilizable = flagDocReutilizable;
	}

	public BigDecimal getIdCm() {
		return this.idCm;
	}

	public void setIdCm(BigDecimal idCm) {
		this.idCm = idCm;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreArchivo() {
		return this.nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getNroDoi() {
		return this.nroDoi;
	}

	public void setNroDoi(String nroDoi) {
		this.nroDoi = nroDoi;
	}

	public String getObligatorio() {
		return this.obligatorio;
	}

	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}

	public String getPidCm() {
		return this.pidCm;
	}

	public void setPidCm(String pidCm) {
		this.pidCm = pidCm;
	}

	public Expediente getExpediente() {
		return this.expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Tarea getTarea() {
		return this.tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public TipoDocumento getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public TipoDoi getTipoDoi() {
		return this.tipoDoi;
	}

	public void setTipoDoi(TipoDoi tipoDoi) {
		this.tipoDoi = tipoDoi;
	}

	public String getFlagObs() {
		return flagObs;
	}

	public void setFlagObs(String flagObs) {
		this.flagObs = flagObs;
	}

	public String getFlagEscaneado() {
		return flagEscaneado;
	}

	public void setFlagEscaneado(String flagEscaneado) {
		this.flagEscaneado = flagEscaneado;
	}

}
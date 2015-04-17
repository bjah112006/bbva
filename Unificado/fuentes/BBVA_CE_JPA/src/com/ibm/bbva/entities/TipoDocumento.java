package com.ibm.bbva.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the TBL_CE_IBM_TIPO_DOCUMENTO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TIPO_DOCUMENTO", schema = "CONELE")


@NamedQueries({
	@NamedQuery(name="TipoDocumento.findAll", query="SELECT t FROM TipoDocumento t ORDER BY t.descripcion"),
	@NamedQuery(name="TipoDocumento.findById", query="SELECT t FROM TipoDocumento t WHERE t.id = :id"),
	@NamedQuery(name="TipoDocumento.findByCodigo", query="SELECT t FROM TipoDocumento t WHERE t.codigo = :codigo")
})

public class TipoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TIPO_DOCUMENTO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TIPO_DOCUMENTO", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TIPO_DOCUMENTO_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	@Column(name="FLAG_REUTILIZABLE")
	private String flagReutilizable;

	private BigDecimal validez;

	//bi-directional many-to-one association to DocumentoExpTc
	@OneToMany(mappedBy="tipoDocumento")
	private List<DocumentoExpTc> documentoExpTcs;

	public TipoDocumento() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFlagReutilizable() {
		return this.flagReutilizable;
	}

	public void setFlagReutilizable(String flagReutilizable) {
		this.flagReutilizable = flagReutilizable;
	}

	public BigDecimal getValidez() {
		return this.validez;
	}

	public void setValidez(BigDecimal validez) {
		this.validez = validez;
	}

	public List<DocumentoExpTc> getDocumentoExpTcs() {
		return this.documentoExpTcs;
	}

	public void setDocumentoExpTcs(List<DocumentoExpTc> documentoExpTcs) {
		this.documentoExpTcs = documentoExpTcs;
	}

	public DocumentoExpTc addDocumentoExpTc(DocumentoExpTc documentoExpTc) {
		getDocumentoExpTcs().add(documentoExpTc);
		documentoExpTc.setTipoDocumento(this);

		return documentoExpTc;
	}

	public DocumentoExpTc removeDocumentoExpTc(DocumentoExpTc documentoExpTc) {
		getDocumentoExpTcs().remove(documentoExpTc);
		documentoExpTc.setTipoDocumento(null);

		return documentoExpTc;
	}

}
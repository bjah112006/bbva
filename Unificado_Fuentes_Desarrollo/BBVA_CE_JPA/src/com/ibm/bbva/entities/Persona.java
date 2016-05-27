package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TBL_CE_IBM_PERSONA database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_PERSONA", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="Persona.findAll", query="SELECT p FROM Persona p ORDER BY p.descripcion"),
	@NamedQuery(name="Persona.findById",query="SELECT p FROM Persona p WHERE p.id = :id"),
	@NamedQuery(name="Persona.findByCodigo", query="SELECT p FROM Persona p WHERE p.codigo = :codigo")
	//@NamedQuery(name="Persona.findByCodigos", query="SELECT p FROM Persona p WHERE p.codigo in ( :codigos)")
})
public class Persona implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_PERSONA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_PERSONA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_PERSONA_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	//bi-directional many-to-one association to DocumentoExpTc
	@OneToMany(mappedBy="persona")
	private List<DocumentoExpTc> documentoExpTcs;

	//uni-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	public Persona() {
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

	public List<DocumentoExpTc> getDocumentoExpTcs() {
		return this.documentoExpTcs;
	}

	public void setDocumentoExpTcs(List<DocumentoExpTc> documentoExpTcs) {
		this.documentoExpTcs = documentoExpTcs;
	}

	public DocumentoExpTc addDocumentoExpTc(DocumentoExpTc documentoExpTc) {
		getDocumentoExpTcs().add(documentoExpTc);
		documentoExpTc.setPersona(this);

		return documentoExpTc;
	}

	public DocumentoExpTc removeDocumentoExpTc(DocumentoExpTc documentoExpTc) {
		getDocumentoExpTcs().remove(documentoExpTc);
		documentoExpTc.setPersona(null);

		return documentoExpTc;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
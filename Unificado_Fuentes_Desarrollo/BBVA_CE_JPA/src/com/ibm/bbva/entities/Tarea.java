package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import static javax.persistence.FetchType.EAGER;


/**
 * The persistent class for the TBL_CE_IBM_TAREA database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TAREA", schema = "CONELE")
@NamedQueries({
		
	@NamedQuery(name = "Tarea.findAll", query = "SELECT t FROM Tarea t"),
		
	@NamedQuery(name = "Tarea.findById", query = "SELECT t FROM Tarea t WHERE t.id = :id") 
})
public class Tarea implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TAREA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TAREA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TAREA_ID_GENERATOR")
	private long id;

	//@Column(name="AMARILLO_MINUTOS")
	//private BigDecimal amarilloMinutos;

	private String codigo;

	private String descripcion;

	private String pagina;

	//@Column(name="VERDE_MINUTOS")
	//private BigDecimal verdeMinutos;

	//bi-directional many-to-one association to DevolucionTarea
	@OneToMany(mappedBy="tarea")
	private List<DevolucionTarea> devolucionTareas;

	//bi-directional many-to-one association to DocumentoExpTc
	@OneToMany(mappedBy="tarea")
	private List<DocumentoExpTc> documentoExpTcs;

	//uni-directional many-to-one association to Producto
/*	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;*/

	//bi-directional many-to-one association to TareaPerfil
	@OneToMany(mappedBy="tarea", fetch = EAGER)
	private List<TareaPerfil> tareaPerfiles;

	public Tarea() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/*public BigDecimal getAmarilloMinutos() {
		return this.amarilloMinutos;
	}

	public void setAmarilloMinutos(BigDecimal amarilloMinutos) {
		this.amarilloMinutos = amarilloMinutos;
	}*/

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

	public String getPagina() {
		return this.pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}

	/*public BigDecimal getVerdeMinutos() {
		return this.verdeMinutos;
	}

	public void setVerdeMinutos(BigDecimal verdeMinutos) {
		this.verdeMinutos = verdeMinutos;
	}*/

	public List<DevolucionTarea> getDevolucionTareas() {
		return this.devolucionTareas;
	}

	public void setDevolucionTareas(List<DevolucionTarea> devolucionTareas) {
		this.devolucionTareas = devolucionTareas;
	}

	public DevolucionTarea addDevolucionTarea(DevolucionTarea devolucionTarea) {
		getDevolucionTareas().add(devolucionTarea);
		devolucionTarea.setTarea(this);

		return devolucionTarea;
	}

	public DevolucionTarea removeDevolucionTarea(DevolucionTarea devolucionTarea) {
		getDevolucionTareas().remove(devolucionTarea);
		devolucionTarea.setTarea(null);

		return devolucionTarea;
	}

	public List<DocumentoExpTc> getDocumentoExpTcs() {
		return this.documentoExpTcs;
	}

	public void setDocumentoExpTcs(List<DocumentoExpTc> documentoExpTcs) {
		this.documentoExpTcs = documentoExpTcs;
	}

	public DocumentoExpTc addDocumentoExpTc(DocumentoExpTc documentoExpTc) {
		getDocumentoExpTcs().add(documentoExpTc);
		documentoExpTc.setTarea(this);

		return documentoExpTc;
	}

	public DocumentoExpTc removeDocumentoExpTc(DocumentoExpTc documentoExpTc) {
		getDocumentoExpTcs().remove(documentoExpTc);
		documentoExpTc.setTarea(null);

		return documentoExpTc;
	}
/*
	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
*/
	public List<TareaPerfil> getTareaPerfiles() {
		return this.tareaPerfiles;
	}

	public void setTareaPerfiles(List<TareaPerfil> tareaPerfiles) {
		this.tareaPerfiles = tareaPerfiles;
	}

	public TareaPerfil addTareaPerfile(TareaPerfil tareaPerfile) {
		getTareaPerfiles().add(tareaPerfile);
		tareaPerfile.setTarea(this);

		return tareaPerfile;
	}

	public TareaPerfil removeTareaPerfile(TareaPerfil tareaPerfile) {
		getTareaPerfiles().remove(tareaPerfile);
		tareaPerfile.setTarea(null);

		return tareaPerfile;
	}

}
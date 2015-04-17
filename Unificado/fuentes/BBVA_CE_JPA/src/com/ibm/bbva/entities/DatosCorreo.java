package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_DATOS_CORREO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_DATOS_CORREO", schema = "CONELE")
@NamedQueries({	
	@NamedQuery(name="DatosCorreo.findAll", query="SELECT d FROM DatosCorreo d ORDER BY d.tarea.id"),
	@NamedQuery(name="DatosCorreo.findById", query="SELECT d FROM DatosCorreo d WHERE d.id = :id")
})
public class DatosCorreo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_DATOS_CORREO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_DATOS_CORREO", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_DATOS_CORREO_ID_GENERATOR")
	private long id;

	@Column(name="ASUNTO")
	private String asunto;
	
	@Lob()
	@Column(name = "CUERPO")
	private byte[] cuerpo;
	
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;
		
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;
	
	@ManyToOne
	@JoinColumn(name="ID_ACCION_FK")
	private Accion accion;
	
	@Column(name="FLAG_ACTIVO")
	private String flagActivo;
	
	@Column(name="AUX_CUERPO")
	private String auxCuerpo;
	
	public DatosCorreo() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public byte[] getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(byte[] cuerpo) {
		this.cuerpo = cuerpo;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public String getAuxCuerpo() {
		return auxCuerpo;
	}

	public void setAuxCuerpo(String auxCuerpo) {
		this.auxCuerpo = auxCuerpo;
	}

	
}
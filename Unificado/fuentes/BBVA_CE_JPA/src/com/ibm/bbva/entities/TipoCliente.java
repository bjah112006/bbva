package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_TIPO_CLIENTE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TIPO_CLIENTE", schema = "CONELE")
@NamedQueries({
	
	@NamedQuery(name = "TipoCliente.findAll", query="SELECT t FROM TipoCliente t"),
	@NamedQuery(name = "TipoCliente.findById", query = "SELECT t FROM TipoCliente t WHERE t.id = :id"),		
	@NamedQuery(name = "TipoCliente.findByCodigo", query = "SELECT t FROM TipoCliente t WHERE t.codigo = :codigo")
	
})
public class TipoCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TIPO_CLIENTE_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TIPO_CLIENTE", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TIPO_CLIENTE_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;
	
	private String orden;

	public TipoCliente() {
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

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}
	

}
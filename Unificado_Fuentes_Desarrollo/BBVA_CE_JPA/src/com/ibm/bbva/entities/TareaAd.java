package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_TAREA_AD database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TAREA_AD", schema = "CONELE")
@NamedQueries({
		
	@NamedQuery(name = "TareaAd.findById", query = "SELECT t FROM TareaAd t WHERE t.id = :id") 
})
public class TareaAd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String codigo;

	private String descripcion;

	private String pagina;

	public TareaAd() {
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

	public String getPagina() {
		return this.pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	

}
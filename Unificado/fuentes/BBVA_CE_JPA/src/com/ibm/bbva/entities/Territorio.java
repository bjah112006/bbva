package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the TBL_CE_IBM_TERRITORIO_CE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TERRITORIO_CE", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name = "Territorio.findAll", query="SELECT t FROM Territorio t"),
	@NamedQuery(name = "Territorio.findById", query = "SELECT t FROM Territorio t WHERE t.id = :id")
})

public class Territorio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TERRITORIO_CE_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TERRITORIO_CE", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TERRITORIO_CE_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	@Column(name="FLAG_PROV")
	private String flagProv;
	
	@Column(name="FLAG_ACTIVO")
	private String flagActivo;
	
	private String ubicacion;

	//bi-directional many-to-one association to Oficina
	@OneToMany(mappedBy="territorio")
	private List<Oficina> oficinas;

	public Territorio() {
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

	public String getFlagProv() {
		return this.flagProv;
	}

	public void setFlagProv(String flagProv) {
		this.flagProv = flagProv;
	}

	public String getUbicacion() {
		return this.ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public List<Oficina> getOficinas() {
		return this.oficinas;
	}

	public void setOficinas(List<Oficina> oficinas) {
		this.oficinas = oficinas;
	}

	public Oficina addOficina(Oficina oficina) {
		getOficinas().add(oficina);
		oficina.setTerritorio(this);

		return oficina;
	}

	public Oficina removeOficina(Oficina oficina) {
		getOficinas().remove(oficina);
		oficina.setTerritorio(null);

		return oficina;
	}

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

}
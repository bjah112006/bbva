package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the TBL_CE_IBM_TABLA database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TABLA", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name="Tabla.findAll", query="SELECT t FROM Tabla t"),
	@NamedQuery(name = "Tabla.findById", query = "SELECT t FROM Tabla t WHERE t.id = :id") 
})

public class Tabla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TABLA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TABLA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TABLA_ID_GENERATOR")
	private long id;

	@Column(name="BOTON_ACTUALIZAR")
	private String botonActualizar;

	@Column(name="BOTON_AGREGAR")
	private String botonAgregar;

	@Column(name="BOTON_ELIMINAR")
	private String botonEliminar;

	private String nombre;

	@Column(name="NOMBRE_MOSTRAR")
	private String nombreMostrar;

	private String orden;

	private String tipo;

	//bi-directional many-to-one association to Columna
	@OneToMany(mappedBy="tabla")
	private List<Columna> columnas;

	public Tabla() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBotonActualizar() {
		return this.botonActualizar;
	}

	public void setBotonActualizar(String botonActualizar) {
		this.botonActualizar = botonActualizar;
	}

	public String getBotonAgregar() {
		return this.botonAgregar;
	}

	public void setBotonAgregar(String botonAgregar) {
		this.botonAgregar = botonAgregar;
	}

	public String getBotonEliminar() {
		return this.botonEliminar;
	}

	public void setBotonEliminar(String botonEliminar) {
		this.botonEliminar = botonEliminar;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreMostrar() {
		return this.nombreMostrar;
	}

	public void setNombreMostrar(String nombreMostrar) {
		this.nombreMostrar = nombreMostrar;
	}

	public String getOrden() {
		return this.orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<Columna> getColumnas() {
		return this.columnas;
	}

	public void setColumnas(List<Columna> columnas) {
		this.columnas = columnas;
	}

	public Columna addColumna(Columna columna) {
		getColumnas().add(columna);
		columna.setTabla(this);

		return columna;
	}

	public Columna removeColumna(Columna columna) {
		getColumnas().remove(columna);
		columna.setTabla(null);

		return columna;
	}

}
package com.ibm.bbva.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: PautaClasificacion
 *
 */
@Entity
@Table(name="TBL_CE_IBM_PAUTA_CLASIFICACION", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="PautaClasificacion.findAll", query="SELECT c FROM PautaClasificacion c"),
	@NamedQuery(name="PautaClasificacion.findByTipoPersona", query="SELECT c FROM PautaClasificacion c WHERE c.persona.id = :idPersona")
})
public class PautaClasificacion implements Serializable {
	private static final long serialVersionUID = 1L;
	   
	@Id
	@SequenceGenerator(name="TBL_CE_IBM_PAUTA_CLASIFICACION_ID_GENERATOR", sequenceName="SEQ_CE_IBM_PAUTA_CLASIFICACION", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_PAUTA_CLASIFICACION_ID_GENERATOR")	
	private long id;	
	
	//uni-directional many-to-one association to TipoMoneda
	@ManyToOne	
	@JoinColumn(name="ID_PERSONA_FK")
	private Persona persona;
	
	private String simbolo;
	
	private double valor;
	

	public PautaClasificacion() {
		super();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	} 

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}
	public String getSimbolo() {
		return simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
   
}

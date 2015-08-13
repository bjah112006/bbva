package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_DELEG_RIESG_COND database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_DELEG_RIESG_COND", schema = "CONELE")
@NamedQueries({	
	@NamedQuery(name="DelegacionRiesgoCondicion.findAll", query="SELECT d FROM DelegacionRiesgoCondicion d"),
	@NamedQuery(name = "DelegacionRiesgoCondicion.findById", query = "SELECT e FROM DelegacionRiesgoCondicion e WHERE e.id = :id ")
})
public class DelegacionRiesgoCondicion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_DELEG_RIESG_COND_ID_GENERATOR", sequenceName="SEQ_CE_IBM_DELEG_RIESG_COND", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_DELEG_RIESG_COND_ID_GENERATOR")
	private long id;
	
	@Column(name="SOLICITADO")
	private double solicitado;

	@Column(name="APROBADO")
	private double aprobado;

	
	//uni-directional many-to-one association to Simbolo
	@ManyToOne
	@JoinColumn(name="ID_SIMBOLO_FK")
	private Simbolo simbolo;
	
	public DelegacionRiesgoCondicion() {
	}

	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	


	public double getSolicitado() {
		return solicitado;
	}





	public void setSolicitado(double solicitado) {
		this.solicitado = solicitado;
	}





	public double getAprobado() {
		return aprobado;
	}





	public void setAprobado(double aprobado) {
		this.aprobado = aprobado;
	}





	public Simbolo getSimbolo() {
		return simbolo;
	}





	public void setSimbolo(Simbolo simbolo) {
		this.simbolo = simbolo;
	}



	
}
package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_VERIFICACION_EXP database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_VERIFICACION_EXP", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name = "VerificacionExp.findAll", query="SELECT v FROM VerificacionExp v"),
	@NamedQuery(name = "VerificacionExp.findById", query = "SELECT v FROM VerificacionExp v WHERE v.id = :id"), 	
	@NamedQuery(name = "VerificacionExp.findByExpediente", query = "SELECT v FROM VerificacionExp v WHERE v.expediente.id = :id ORDER BY v.id"),
	@NamedQuery(name = "VerificacionExp.findByExpTipoVer", query = "SELECT v FROM VerificacionExp v WHERE v.expediente.id = :idExp and v.tipoVerificacion.id = :idTipoVer")
})

public class VerificacionExp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_VERIFICACION_EXP_ID_GENERATOR", sequenceName="SEQ_CE_IBM_VERIFICACION_EXP", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_VERIFICACION_EXP_ID_GENERATOR")
	private long id;

	@Column(name="FLAG_VERIF")
	private String flagVerif;

	//uni-directional many-to-one association to Expediente
	@ManyToOne
	@JoinColumn(name="ID_EXPEDIENTE_FK")
	private Expediente expediente;

	//uni-directional many-to-one association to TipoVerificacion
	@ManyToOne
	@JoinColumn(name="ID_TIPO_VERIFICACION_FK")
	private TipoVerificacion tipoVerificacion;

	public VerificacionExp() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFlagVerif() {
		return this.flagVerif;
	}

	public void setFlagVerif(String flagVerif) {
		this.flagVerif = flagVerif;
	}

	public Expediente getExpediente() {
		return this.expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public TipoVerificacion getTipoVerificacion() {
		return this.tipoVerificacion;
	}

	public void setTipoVerificacion(TipoVerificacion tipoVerificacion) {
		this.tipoVerificacion = tipoVerificacion;
	}

}
package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the TBL_CE_IBM_DATCORR_X_PERFIL_CC database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_DATCORR_X_PERFIL_CC", schema = "CONELE")
public class DatosCorreoXPerfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_DATCORR_X_PERFIL_CC_ID_GENERATOR", sequenceName="SEQ_CE_IBM_DATCORR_X_PERFIL_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_DATCORR_X_PERFIL_CC_ID_GENERATOR")
	@Column(unique=true, nullable=false, precision=10)
	private Integer id;

	//bi-directional many-to-one association to DatosCorreo
    @ManyToOne
	@JoinColumn(name="ID_DATOS_CORREO_FK", unique=true, nullable=false)
	private DatosCorreo datosCorreo;

	//uni-directional many-to-one association to Perfil
    @ManyToOne
	@JoinColumn(name="ID_PERFIL_FK", unique=true, nullable=false)
	private Perfil perfil;

    public DatosCorreoXPerfil() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DatosCorreo getDatosCorreo() {
		return this.datosCorreo;
	}

	public void setDatosCorreo(DatosCorreo datosCorreo) {
		this.datosCorreo = datosCorreo;
	}
	
	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
}
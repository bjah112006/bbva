package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the TBL_CE_IBM_DATOS_CORREO_DESTIN database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_DATOS_CORREO_DESTIN", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name = "DatosCorreoDestin.findAll", query="SELECT DISTINCT t FROM DatosCorreoDestin t ORDER BY t.id"),
	@NamedQuery(name = "DatosCorreoDestin.findByIdPerfil", query = "SELECT t FROM DatosCorreoDestin t WHERE t.perfil.id = :idPerfil"),
	@NamedQuery(name = "DatosCorreoDestin.findById", query="SELECT d FROM DatosCorreoDestin d WHERE d.id = :id"),
	@NamedQuery(name = "DatosCorreoDestin.findByPerfilAndCabecera", query="SELECT d FROM DatosCorreoDestin d WHERE d.perfil.id=:idPerfil AND d.datosCorreo.id=:idCabecera")
})

public class DatosCorreoDestin implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_DATOS_CORREO_DESTIN_ID_GENERATOR", sequenceName="SEQ_CE_IBM_CORREO_DESTIN", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_DATOS_CORREO_DESTIN_ID_GENERATOR")
	private long id;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="ID_PERFIL_FK")
	private Perfil perfil;

	//bi-directional many-to-one association to DatosCorreo
	@ManyToOne
	@JoinColumn(name="ID_DATOS_CORREO_FK")
	private DatosCorreo datosCorreo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public DatosCorreo getDatosCorreo() {
		return datosCorreo;
	}

	public void setDatosCorreo(DatosCorreo datosCorreo) {
		this.datosCorreo = datosCorreo;
	}	
}

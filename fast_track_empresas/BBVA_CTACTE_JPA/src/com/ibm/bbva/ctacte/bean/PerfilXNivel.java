package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the TBL_CE_IBM_PERFIL_X_NIVEL_CC database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_PERFIL_X_NIVEL_CC", schema = "CONELE")
public class PerfilXNivel implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PerfilXNivelPK id;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO")
	private Date fechaRegistro;

	@Column(name="FLAG_ACTIVO", nullable=false, length=1)
	private String flagActivo;

	//uni-directional many-to-one association to Empleado
    @ManyToOne
	@JoinColumn(name="ID_EMPLEADO_FK")
	private Empleado empleado;

	//uni-directional many-to-one association to Perfil
    @ManyToOne
	@JoinColumn(name="ID_PERFIL_FK", nullable=false)
	private Perfil perfil;

    public PerfilXNivel() {
    }

	public PerfilXNivelPK getId() {
		return this.id;
	}

	public void setId(PerfilXNivelPK id) {
		this.id = id;
	}
	
	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getFlagActivo() {
		return this.flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public Empleado getEmpleado() {
		return this.empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	
	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
}
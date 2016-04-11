package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the TBL_CE_IBM_DATOS_CORREO_CC database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_DATOS_CORREO_CC", schema = "CONELE")
public class DatosCorreo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_DATOS_CORREO_CC_ID_GENERATOR", sequenceName="SEQ_CE_IBM_DATOS_CORREO_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_DATOS_CORREO_CC_ID_GENERATOR")
	@Column(unique=true, nullable=false, precision=10)
	private Integer id;

	@Column(length=100)
	private String asunto;

    @Lob()
	private byte[] cuerpo;

	@Column(name="FLAG_ACTIVO", length=1)
	private String flagActivo;

	//bi-directional many-to-one association to DatosCorreoXPerfil
	@OneToMany(mappedBy="datosCorreo", cascade={ CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<DatosCorreoXPerfil> datosCorreoXPerfil;

	//uni-directional many-to-one association to Accion
    @ManyToOne
	@JoinColumn(name="ID_ACCION_FK", unique=true)
	private Accion accion;
    
    @Transient
    private boolean seleccionado = false; // para el mantenimiento

    public DatosCorreo() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public byte[] getCuerpo() {
		return this.cuerpo;
	}

	public void setCuerpo(byte[] cuerpo) {
		this.cuerpo = cuerpo;
	}

	public String getFlagActivo() {
		return this.flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public List<DatosCorreoXPerfil> getDatosCorreoXPerfil() {
		return this.datosCorreoXPerfil;
	}

	public void setDatosCorreoXPerfil(List<DatosCorreoXPerfil> datosCorreoXPerfil) {
		this.datosCorreoXPerfil = datosCorreoXPerfil;
	}
	
	public Accion getAccion() {
		return this.accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
	
	public String getCuerpoString() {
		if (cuerpo != null) {
			return new String(cuerpo);
		} else {
			return "";
		}
	}
	
}
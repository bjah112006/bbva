package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the TBL_CE_IBM_EMPLEADO_AD_CE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_EMPLEADO_AD_CE", schema = "CONELE")
@NamedQueries({		
	@NamedQuery(name = "EmpleadoAd.findByCodigo", query = "SELECT e FROM EmpleadoAd e WHERE e.codigo = :codigo")
})

public class EmpleadoAd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_EMPLEADO_AD_ID_GENERATOR", sequenceName="SEQ_CE_IBM_EMPLEADO_AD_CE", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_EMPLEADO_AD_ID_GENERATOR")
	private long id;

	private String apemat;

	private String apepat;

	private String codigo;

	private String correo;

	@Temporal(TemporalType.DATE)
	private Date fecegr;

	@Temporal(TemporalType.DATE)
	private Date fecing;

	@Column(name="FLAG_ACTIVO")
	private String flagActivo;

	private String nombres;

	@Column(name="NOMBRES_COMPLETOS")
	private String nombresCompletos;

	//uni-directional many-to-one association to Nivel
	@ManyToOne
	@JoinColumn(name="ID_NIVEL_FK")
	private Nivel nivel;

	//bi-directional many-to-one association to Oficina
	@ManyToOne
	@JoinColumn(name="ID_OFICINA_FK")
	private Oficina oficina;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="ID_PERFIL_FK")
	private Perfil perfil;

	//uni-directional many-to-one association to TipoCategoria
	@ManyToOne
	@JoinColumn(name="ID_TIPO_CATEGORIA_FK")
	private TipoCategoria tipoCategoria;


	public EmpleadoAd() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApemat() {
		return this.apemat;
	}

	public void setApemat(String apemat) {
		this.apemat = apemat;
	}

	public String getApepat() {
		return this.apepat;
	}

	public void setApepat(String apepat) {
		this.apepat = apepat;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Date getFecegr() {
		return this.fecegr;
	}

	public void setFecegr(Date fecegr) {
		this.fecegr = fecegr;
	}

	public Date getFecing() {
		return this.fecing;
	}

	public void setFecing(Date fecing) {
		this.fecing = fecing;
	}

	public String getFlagActivo() {
		return this.flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getNombresCompletos() {
		return this.nombresCompletos;
	}

	public void setNombresCompletos(String nombresCompletos) {
		this.nombresCompletos = nombresCompletos;
	}

	public Nivel getNivel() {
		return this.nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public Oficina getOficina() {
		return this.oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public TipoCategoria getTipoCategoria() {
		return this.tipoCategoria;
	}

	public void setTipoCategoria(TipoCategoria tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}

	
}
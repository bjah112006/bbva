package com.ibm.bbva.ctacte.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TBL_CE_IBM_HORARIO_CC", schema = "CONELE")
public class HorarioCC implements java.io.Serializable {

	private static final long serialVersionUID = -3511878860129829599L;
	
	@Id
	@SequenceGenerator(name = "seqHorarioCC", sequenceName = "SEQ_CE_IBM_HORARIO_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqHorarioCC")
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PERFIL_FK", nullable = false)
	private Perfil perfil;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_OFICINA_FK", nullable = false)
	private Oficina oficina;

	@Column(name = "DESCRIPCION", length = 50)
	private String descripcion;

	@Column(name = "ESTADO", length = 1)
	private String estado;

	@Column(name = "EXCEPCION", length = 1)
	private String excepcion;

	@Column(name = "APLICA_TODO", length = 1)
	private String aplicaTodo;

	@Column(name = "FECHA_EXCEPCION", length = 7)
	private Date fechaExcepcion;
	
	@Column(name = "NUMERO_DIA", precision = 5, scale = 0)
	private int dia;

	@Column(name = "APLICA_OFICINA", length = 1)
	private String aplicaOficina;
	
	@Transient
	private List<RangoCC> rangos;
	
	
	
	public HorarioCC() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getExcepcion() {
		return excepcion;
	}

	public void setExcepcion(String excepcion) {
		this.excepcion = excepcion;
	}

	public String getAplicaTodo() {
		return aplicaTodo;
	}

	public void setAplicaToto(String aplicaTodo) {
		this.aplicaTodo = aplicaTodo;
	}

	public Date getFechaExcepcion() {
		return fechaExcepcion;
	}

	public void setFechaExcepcion(Date fechaExcepcion) {
		this.fechaExcepcion = fechaExcepcion;
	}

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public List<RangoCC> getRangos() {
		return rangos;
	}

	public void setRangos(List<RangoCC> rangosCC) {
		this.rangos = rangosCC;
	}

	public String getAplicaOficina() {
		return aplicaOficina;
	}

	public void setAplicaOficina(String aplicaOficina) {
		this.aplicaOficina = aplicaOficina;
	}

	public void setAplicaTodo(String aplicaTodo) {
		this.aplicaTodo = aplicaTodo;
	}

	
	

}

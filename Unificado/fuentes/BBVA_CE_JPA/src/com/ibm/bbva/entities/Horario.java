package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_CE_IBM_HORARIO_CE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_HORARIO_CE", schema = "CONELE")

@NamedQueries({
	@NamedQuery(name="Horario.findAll", query="SELECT h FROM Horario h"),
	@NamedQuery(name="Horario.findById", query="SELECT h FROM Horario h WHERE h.id = :id")
})

public class Horario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_HORARIO_CE_ID_GENERATOR", sequenceName="SEQ_CE_IBM_HORARIO_CE", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_HORARIO_CE_ID_GENERATOR")
	private long id;

	private String activo;

	@Temporal(TemporalType.DATE)
	@Column(name="DIA_FIN")
	private Date diaFin;

	@Temporal(TemporalType.DATE)
	@Column(name="DIA_INICIO")
	private Date diaInicio;

	private BigDecimal dom;

	private String excepcion;

	@Column(name="HORA_FIN")
	private String horaFin;

	@Column(name="HORA_INICIO")
	private String horaInicio;

	private BigDecimal jue;

	private BigDecimal lun;

	private BigDecimal mar;

	private BigDecimal mie;

	@Column(nullable = true)
	private BigDecimal sab;

	@Column(name="TODO_DIA")
	private String todoDia;

	private BigDecimal vie;

	//bi-directional many-to-one association to HorarioOficina
	@OneToMany(mappedBy="horario")
	private List<HorarioOficina> horarioOficinas;

	public Horario() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public Date getDiaFin() {
		return this.diaFin;
	}

	public void setDiaFin(Date diaFin) {
		this.diaFin = diaFin;
	}

	public Date getDiaInicio() {
		return this.diaInicio;
	}

	public void setDiaInicio(Date diaInicio) {
		this.diaInicio = diaInicio;
	}

	public BigDecimal getDom() {
		return this.dom;
	}

	public void setDom(BigDecimal dom) {
		this.dom = dom;
	}

	public String getExcepcion() {
		return this.excepcion;
	}

	public void setExcepcion(String excepcion) {
		this.excepcion = excepcion;
	}

	public String getHoraFin() {
		return this.horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public String getHoraInicio() {
		return this.horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public BigDecimal getJue() {
		return this.jue;
	}

	public void setJue(BigDecimal jue) {
		this.jue = jue;
	}

	public BigDecimal getLun() {
		return this.lun;
	}

	public void setLun(BigDecimal lun) {
		this.lun = lun;
	}

	public BigDecimal getMar() {
		return this.mar;
	}

	public void setMar(BigDecimal mar) {
		this.mar = mar;
	}

	public BigDecimal getMie() {
		return this.mie;
	}

	public void setMie(BigDecimal mie) {
		this.mie = mie;
	}

	public BigDecimal getSab() {
		return this.sab;
	}

	public void setSab(BigDecimal sab) {
		this.sab = sab;
	}

	public String getTodoDia() {
		return this.todoDia;
	}

	public void setTodoDia(String todoDia) {
		this.todoDia = todoDia;
	}

	public BigDecimal getVie() {
		return this.vie;
	}

	public void setVie(BigDecimal vie) {
		this.vie = vie;
	}

	public List<HorarioOficina> getHorarioOficinas() {
		return this.horarioOficinas;
	}

	public void setHorarioOficinas(List<HorarioOficina> horarioOficinas) {
		this.horarioOficinas = horarioOficinas;
	}

	public HorarioOficina addHorarioOficina(HorarioOficina horarioOficina) {
		getHorarioOficinas().add(horarioOficina);
		horarioOficina.setHorario(this);

		return horarioOficina;
	}

	public HorarioOficina removeHorarioOficina(HorarioOficina horarioOficina) {
		getHorarioOficinas().remove(horarioOficina);
		horarioOficina.setHorario(null);

		return horarioOficina;
	}

}
package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="TBL_CE_IBM_OFICINA_TEMPORAL", schema = "CONELE")
@NamedQuery(name = "OficinaTemporal.findById", query = "SELECT d FROM OficinaTemporal d WHERE d.id = :id")
public class OficinaTemporal implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TBL_CE_IBM_OFICINA_TEMPORAL_ID_GENERATOR", sequenceName="SEQ_CE_IBM_OFI_TEMP", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_OFICINA_TEMPORAL_ID_GENERATOR")	
	private long id;
	
	@ManyToOne
	@JoinColumn(name="ID_EMPLEADO")
	private Empleado empleado;
	
	@ManyToOne
	@JoinColumn(name="ID_OFICINA")
	private Oficina oficina;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_INICIO")
	private Date fechaInicio;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_FIN")
	private Date fechaFin;

	@Column(name="HORA_INICIO")
	private String horaInicio;

	@Column(name="HORA_FIN")
	private String horaFin;
	
	@Column(name="ESTADO")
	private String estado;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}

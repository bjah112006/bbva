package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_CE_IBM_MOTIVO_DEVOLUCION database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_MOTIVO_DEVOLUCION", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="MotivoDevolucion.findAll", query="SELECT m FROM MotivoDevolucion m"),
	@NamedQuery(name="MotivoDevolucion.findbyId", query="SELECT m FROM MotivoDevolucion m WHERE m.id = :id"),
	@NamedQuery(name="MotivoDevolucion.findbyIdTarea", query="SELECT m FROM MotivoDevolucion m WHERE m.tarea.id = :idTarea AND m.flagRechazo like :idActivo AND m.flagActivo like :idActivo"),
	@NamedQuery(name="MotivoDevolucion.findbyIdTareaDevolver", query="SELECT m FROM MotivoDevolucion m WHERE m.tarea.id = :idTarea AND m.flagActivo like :idActivo"),
	@NamedQuery(name="MotivoDevolucion.findbyIdTareaYAccion", query="SELECT m FROM MotivoDevolucion m WHERE m.tarea.id = :idTarea AND m.accion = :accion"),
	@NamedQuery(name="MotivoDevolucion.findbyIdMotivoIdTareaFlagOtros", query="SELECT m FROM MotivoDevolucion m WHERE m.id = :idMotivo and m.tarea.id = :idTarea AND m.flagOtros = :flagOtros AND m.flagActivo like :idActivo"),	
})
public class MotivoDevolucion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_MOTIVO_DEVOLUCION_ID_GENERATOR", sequenceName="SEQ_CE_IBM_MOTIVO_DEVOLUCION", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_MOTIVO_DEVOLUCION_ID_GENERATOR")
	private long id;

	private BigDecimal accion;

	private String codigo;

	private String descripcion;

	@Column(name="FLAG_OTROS")
	private String flagOtros;
	
	@Column(name="FLAG_ACTIVO")
	private String flagActivo;	

	@Column(name="FLAG_RECHAZO")
	private String flagRechazo;
	
	//uni-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;

	public MotivoDevolucion() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAccion() {
		return this.accion;
	}

	public void setAccion(BigDecimal accion) {
		this.accion = accion;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFlagOtros() {
		return this.flagOtros;
	}

	public void setFlagOtros(String flagOtros) {
		this.flagOtros = flagOtros;
	}

	public Tarea getTarea() {
		return this.tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public String getFlagRechazo() {
		return flagRechazo;
	}

	public void setFlagRechazo(String flagRechazo) {
		this.flagRechazo = flagRechazo;
	}
	

}
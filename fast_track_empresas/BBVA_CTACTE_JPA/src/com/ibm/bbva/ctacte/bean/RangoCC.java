package com.ibm.bbva.ctacte.bean;

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

@Entity
@Table(name = "TBL_CE_IBM_RANGO_CC", schema = "CONELE")
public class RangoCC implements java.io.Serializable {

	private static final long serialVersionUID = -5127439598472697776L;

	@Id
	@SequenceGenerator(name = "seqRango", sequenceName = "SEQ_CE_IBM_RANGO_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqRango")
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_HORARIO_FK", nullable = false)
	private HorarioCC horario;

	@Column(name = "RANGO_INI", length = 5)
	private String rangoInicial;

	@Column(name = "RANGO_FIN", length = 5)
	private String rangoFinal;
	
	@Column(name = "ESTADO", length = 1)
	private String estado;

	public RangoCC() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public HorarioCC getHorario() {
		return horario;
	}

	public void setHorario(HorarioCC horario) {
		this.horario = horario;
	}

	public String getRangoInicial() {
		return rangoInicial;
	}

	public void setRangoInicial(String rangoInicial) {
		this.rangoInicial = rangoInicial;
	}

	public String getRangoFinal() {
		return rangoFinal;
	}

	public void setRangoFinal(String rangoFinal) {
		this.rangoFinal = rangoFinal;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getDescripcion(){
		if(this.rangoInicial!=null && !this.rangoInicial.equalsIgnoreCase("")){
			return this.rangoInicial + " - " + this.rangoFinal;
		}else{
			return "N/A";
		}
	}

}

package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_COBRO_COMISION_CC", schema = "CONELE")

public class CobroComision  implements java.io.Serializable {
	
	private static final long serialVersionUID = -5657836223012142226L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	private Integer id;
	
	@Column(name = "DIAS_REINTENTO", nullable = false, precision = 10, scale = 0)
	private Integer diasReintento;
	
	@Column(name = "HORA_EJECUCION_1", nullable = false, length = 8)
	private String horaEjecucion1;
	
	@Column(name = "HORA_EJECUCION_2", nullable = false, length = 8)
	private String horaEjecucion2;
	
	@Column(name = "HORA_EJECUCION_3", nullable = false, length = 8)
	private String horaEjecucion3;
	
	@Column(name = "HORA_EJECUCION_4", nullable = false, length = 8)
	private String horaEjecucion4;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDiasReintento() {
		return diasReintento;
	}

	public void setDiasReintento(Integer diasReintento) {
		this.diasReintento = diasReintento;
	}

	public String getHoraEjecucion1() {
		return horaEjecucion1;
	}

	public void setHoraEjecucion1(String horaEjecucion1) {
		this.horaEjecucion1 = horaEjecucion1;
	}

	public String getHoraEjecucion2() {
		return horaEjecucion2;
	}

	public void setHoraEjecucion2(String horaEjecucion2) {
		this.horaEjecucion2 = horaEjecucion2;
	}

	public String getHoraEjecucion3() {
		return horaEjecucion3;
	}

	public void setHoraEjecucion3(String horaEjecucion3) {
		this.horaEjecucion3 = horaEjecucion3;
	}

	public String getHoraEjecucion4() {
		return horaEjecucion4;
	}

	public void setHoraEjecucion4(String horaEjecucion4) {
		this.horaEjecucion4 = horaEjecucion4;
	}

	public String toString () {
		StringBuilder sb = new StringBuilder ();
		sb.append("[id: ").append(id).append("]");
		sb.append("[diasReintento: ").append(getDiasReintento()).append("]");
		sb.append("[horaEjecucion1: ").append(getHoraEjecucion1()).append("]");
		sb.append("[horaEjecucion2: ").append(getHoraEjecucion2()).append("]");
		sb.append("[horaEjecucion3: ").append(getHoraEjecucion3()).append("]");
		sb.append("[horaEjecucion4: ").append(getHoraEjecucion4()).append("]");
		return sb.toString();
	}
}

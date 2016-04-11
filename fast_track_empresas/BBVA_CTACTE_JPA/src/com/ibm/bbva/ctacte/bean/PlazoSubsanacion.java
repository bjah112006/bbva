package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_PLAZO_SUBSANA_CC", schema = "CONELE")
public class PlazoSubsanacion  implements java.io.Serializable {
	
	private static final long serialVersionUID = 6251778817666815251L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@Column(name = "PLAZO_DIAS", nullable = false, precision = 5, scale = 0)
	private long plazoDias;
	
	public PlazoSubsanacion() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getPlazoDias() {
		return plazoDias;
	}

	public void setPlazoDias(long plazoDias) {
		this.plazoDias = plazoDias;
	}

}

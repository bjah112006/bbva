package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="TBL_CE_IBM_DESC_LDAP_CARTERIZ", schema = "CONELE")
public class DescargaLDAPCarteriz implements Serializable 
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TBL_CE_IBM_DESC_LDAP_CARTERIZ_ID_GENERATOR", sequenceName="SEQ_CE_IBM_DES_LD_CAR", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_DESC_LDAP_CARTERIZ_ID_GENERATOR")	
	private long id;
	
	@ManyToOne
	@JoinColumn(name="ID_DESC_LDAP")
	private DescargaLDAP descargaLDAP;
		
	@ManyToOne
	@JoinColumn(name="ID_CARTERIZACION")
	private CarterizacionCE carterizacion;

	public DescargaLDAP getDescargaLDAP() {
		return descargaLDAP;
	}

	public void setDescargaLDAP(DescargaLDAP descargaLDAP) {
		this.descargaLDAP = descargaLDAP;
	}

	public CarterizacionCE getCarterizacion() {
		return carterizacion;
	}

	public void setCarterizacion(CarterizacionCE carterizacion) {
		this.carterizacion = carterizacion;
	}
		
}

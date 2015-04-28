package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the LDAPPERU2 database table.
 * 
 */
@Entity
@Table(name="LDAPPERU2")
public class LDAPUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CODUSU", unique = true, nullable = false, precision = 10, scale = 0)
	private String codusu;

	private String apemat;

	private String apepat;

	private String carblan;

	private String codcargo;

	private String codemp;

	private String codempant;

	private String codger;

	private String codnivel1;

	private String codnivel10;

	private String codnivel2;

	private String codnivel3;

	private String codnivel4;

	private String codnivel5;

	private String codnivel6;

	private String codnivel7;

	private String codnivel8;

	private String codnivel9;

	private String codofi;

	private String codpais;

	private String coduniorg;

	private String codusujefe;

	private String corelec;

	private String desemp;

	private String desnivel1;

	private String desnivel10;

	private String desnivel2;

	private String desnivel3;

	private String desnivel4;

	private String desnivel5;

	private String desnivel6;

	private String desnivel7;

	private String desnivel8;

	private String desnivel9;

	private String desofi;

	private String despais;

	private String desuniorg;

	private String fecing;

	private String fecnac;

	private String gesper;

	private String nombre;

	private String nomcargo;

	private String nomger;

	private BigDecimal nrohijos;

	private String numdoc;

	private String nummat;

	private String proinf;

	private String registrop;

	private String registrou;

	private String sexo;

	private String telefono1;

	private String telefono2;

	private String telofi1;

	private String telofi2;

	private String tipdoc;

	private String tipofi;

	private String usofut1;

	private String usofut10;

	private String usofut2;

	private String usofut3;

	private String usofut4;

	private String usofut5;

	private String usofut6;

	private String usofut7;

	private String usofut8;

	private String usofut9;

    public LDAPUsuario() {
    }

	public String getCodusu() {
		return this.codusu;
	}

	public void setCodusu(String codusu) {
		this.codusu = codusu;
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

	public String getCarblan() {
		return this.carblan;
	}

	public void setCarblan(String carblan) {
		this.carblan = carblan;
	}

	public String getCodcargo() {
		return this.codcargo;
	}

	public void setCodcargo(String codcargo) {
		this.codcargo = codcargo;
	}

	public String getCodemp() {
		return this.codemp;
	}

	public void setCodemp(String codemp) {
		this.codemp = codemp;
	}

	public String getCodempant() {
		return this.codempant;
	}

	public void setCodempant(String codempant) {
		this.codempant = codempant;
	}

	public String getCodger() {
		return this.codger;
	}

	public void setCodger(String codger) {
		this.codger = codger;
	}

	public String getCodnivel1() {
		return this.codnivel1;
	}

	public void setCodnivel1(String codnivel1) {
		this.codnivel1 = codnivel1;
	}

	public String getCodnivel10() {
		return this.codnivel10;
	}

	public void setCodnivel10(String codnivel10) {
		this.codnivel10 = codnivel10;
	}

	public String getCodnivel2() {
		return this.codnivel2;
	}

	public void setCodnivel2(String codnivel2) {
		this.codnivel2 = codnivel2;
	}

	public String getCodnivel3() {
		return this.codnivel3;
	}

	public void setCodnivel3(String codnivel3) {
		this.codnivel3 = codnivel3;
	}

	public String getCodnivel4() {
		return this.codnivel4;
	}

	public void setCodnivel4(String codnivel4) {
		this.codnivel4 = codnivel4;
	}

	public String getCodnivel5() {
		return this.codnivel5;
	}

	public void setCodnivel5(String codnivel5) {
		this.codnivel5 = codnivel5;
	}

	public String getCodnivel6() {
		return this.codnivel6;
	}

	public void setCodnivel6(String codnivel6) {
		this.codnivel6 = codnivel6;
	}

	public String getCodnivel7() {
		return this.codnivel7;
	}

	public void setCodnivel7(String codnivel7) {
		this.codnivel7 = codnivel7;
	}

	public String getCodnivel8() {
		return this.codnivel8;
	}

	public void setCodnivel8(String codnivel8) {
		this.codnivel8 = codnivel8;
	}

	public String getCodnivel9() {
		return this.codnivel9;
	}

	public void setCodnivel9(String codnivel9) {
		this.codnivel9 = codnivel9;
	}

	public String getCodofi() {
		return this.codofi;
	}

	public void setCodofi(String codofi) {
		this.codofi = codofi;
	}

	public String getCodpais() {
		return this.codpais;
	}

	public void setCodpais(String codpais) {
		this.codpais = codpais;
	}

	public String getCoduniorg() {
		return this.coduniorg;
	}

	public void setCoduniorg(String coduniorg) {
		this.coduniorg = coduniorg;
	}

	public String getCodusujefe() {
		return this.codusujefe;
	}

	public void setCodusujefe(String codusujefe) {
		this.codusujefe = codusujefe;
	}

	public String getCorelec() {
		return this.corelec;
	}

	public void setCorelec(String corelec) {
		this.corelec = corelec;
	}

	public String getDesemp() {
		return this.desemp;
	}

	public void setDesemp(String desemp) {
		this.desemp = desemp;
	}

	public String getDesnivel1() {
		return this.desnivel1;
	}

	public void setDesnivel1(String desnivel1) {
		this.desnivel1 = desnivel1;
	}

	public String getDesnivel10() {
		return this.desnivel10;
	}

	public void setDesnivel10(String desnivel10) {
		this.desnivel10 = desnivel10;
	}

	public String getDesnivel2() {
		return this.desnivel2;
	}

	public void setDesnivel2(String desnivel2) {
		this.desnivel2 = desnivel2;
	}

	public String getDesnivel3() {
		return this.desnivel3;
	}

	public void setDesnivel3(String desnivel3) {
		this.desnivel3 = desnivel3;
	}

	public String getDesnivel4() {
		return this.desnivel4;
	}

	public void setDesnivel4(String desnivel4) {
		this.desnivel4 = desnivel4;
	}

	public String getDesnivel5() {
		return this.desnivel5;
	}

	public void setDesnivel5(String desnivel5) {
		this.desnivel5 = desnivel5;
	}

	public String getDesnivel6() {
		return this.desnivel6;
	}

	public void setDesnivel6(String desnivel6) {
		this.desnivel6 = desnivel6;
	}

	public String getDesnivel7() {
		return this.desnivel7;
	}

	public void setDesnivel7(String desnivel7) {
		this.desnivel7 = desnivel7;
	}

	public String getDesnivel8() {
		return this.desnivel8;
	}

	public void setDesnivel8(String desnivel8) {
		this.desnivel8 = desnivel8;
	}

	public String getDesnivel9() {
		return this.desnivel9;
	}

	public void setDesnivel9(String desnivel9) {
		this.desnivel9 = desnivel9;
	}

	public String getDesofi() {
		return this.desofi;
	}

	public void setDesofi(String desofi) {
		this.desofi = desofi;
	}

	public String getDespais() {
		return this.despais;
	}

	public void setDespais(String despais) {
		this.despais = despais;
	}

	public String getDesuniorg() {
		return this.desuniorg;
	}

	public void setDesuniorg(String desuniorg) {
		this.desuniorg = desuniorg;
	}

	public String getFecing() {
		return this.fecing;
	}

	public void setFecing(String fecing) {
		this.fecing = fecing;
	}

	public String getFecnac() {
		return this.fecnac;
	}

	public void setFecnac(String fecnac) {
		this.fecnac = fecnac;
	}

	public String getGesper() {
		return this.gesper;
	}

	public void setGesper(String gesper) {
		this.gesper = gesper;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNomcargo() {
		return this.nomcargo;
	}

	public void setNomcargo(String nomcargo) {
		this.nomcargo = nomcargo;
	}

	public String getNomger() {
		return this.nomger;
	}

	public void setNomger(String nomger) {
		this.nomger = nomger;
	}

	public BigDecimal getNrohijos() {
		return this.nrohijos;
	}

	public void setNrohijos(BigDecimal nrohijos) {
		this.nrohijos = nrohijos;
	}

	public String getNumdoc() {
		return this.numdoc;
	}

	public void setNumdoc(String numdoc) {
		this.numdoc = numdoc;
	}

	public String getNummat() {
		return this.nummat;
	}

	public void setNummat(String nummat) {
		this.nummat = nummat;
	}

	public String getProinf() {
		return this.proinf;
	}

	public void setProinf(String proinf) {
		this.proinf = proinf;
	}

	public String getRegistrop() {
		return this.registrop;
	}

	public void setRegistrop(String registrop) {
		this.registrop = registrop;
	}

	public String getRegistrou() {
		return this.registrou;
	}

	public void setRegistrou(String registrou) {
		this.registrou = registrou;
	}

	public String getSexo() {
		return this.sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getTelefono1() {
		return this.telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return this.telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getTelofi1() {
		return this.telofi1;
	}

	public void setTelofi1(String telofi1) {
		this.telofi1 = telofi1;
	}

	public String getTelofi2() {
		return this.telofi2;
	}

	public void setTelofi2(String telofi2) {
		this.telofi2 = telofi2;
	}

	public String getTipdoc() {
		return this.tipdoc;
	}

	public void setTipdoc(String tipdoc) {
		this.tipdoc = tipdoc;
	}

	public String getTipofi() {
		return this.tipofi;
	}

	public void setTipofi(String tipofi) {
		this.tipofi = tipofi;
	}

	public String getUsofut1() {
		return this.usofut1;
	}

	public void setUsofut1(String usofut1) {
		this.usofut1 = usofut1;
	}

	public String getUsofut10() {
		return this.usofut10;
	}

	public void setUsofut10(String usofut10) {
		this.usofut10 = usofut10;
	}

	public String getUsofut2() {
		return this.usofut2;
	}

	public void setUsofut2(String usofut2) {
		this.usofut2 = usofut2;
	}

	public String getUsofut3() {
		return this.usofut3;
	}

	public void setUsofut3(String usofut3) {
		this.usofut3 = usofut3;
	}

	public String getUsofut4() {
		return this.usofut4;
	}

	public void setUsofut4(String usofut4) {
		this.usofut4 = usofut4;
	}

	public String getUsofut5() {
		return this.usofut5;
	}

	public void setUsofut5(String usofut5) {
		this.usofut5 = usofut5;
	}

	public String getUsofut6() {
		return this.usofut6;
	}

	public void setUsofut6(String usofut6) {
		this.usofut6 = usofut6;
	}

	public String getUsofut7() {
		return this.usofut7;
	}

	public void setUsofut7(String usofut7) {
		this.usofut7 = usofut7;
	}

	public String getUsofut8() {
		return this.usofut8;
	}

	public void setUsofut8(String usofut8) {
		this.usofut8 = usofut8;
	}

	public String getUsofut9() {
		return this.usofut9;
	}

	public void setUsofut9(String usofut9) {
		this.usofut9 = usofut9;
	}

}
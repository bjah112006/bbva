package com.ibm.bbva.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="TBL_CE_IBM_DESCARGA_LDAP", schema = "CONELE")
@NamedQuery(name = "DescargaLDAP.findById", query = "SELECT d FROM DescargaLDAP d LEFT JOIN FETCH d.descargaLDAPCarterizaciones WHERE d.id = :id")
public class DescargaLDAP implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TBL_CE_IBM_DESCARGA_LDAP_ID_GENERATOR", sequenceName="SEQ_CE_IBM_DESC_LDAP", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_DESCARGA_LDAP_ID_GENERATOR")	
	private long id;
	
	@Column(name="TIPO")
	private String tipo;
	
	@Column(name="CODIGO")
	private String codigo;
	
	@ManyToOne
	@JoinColumn(name="ID_PERFIL")
	private Perfil perfil;
		
	@Column(name="ESTADO")
	private String estado;

	@OneToMany(mappedBy="descargaLDAP")
	private List<DescargaLDAPCarteriz> descargaLDAPCarterizaciones;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<DescargaLDAPCarteriz> getDescargaLDAPCarterizaciones() {
		return descargaLDAPCarterizaciones;
	}

	public void setDescargaLDAPCarterizaciones(
			List<DescargaLDAPCarteriz> descargaLDAPCarterizaciones) {
		this.descargaLDAPCarterizaciones = descargaLDAPCarterizaciones;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
}

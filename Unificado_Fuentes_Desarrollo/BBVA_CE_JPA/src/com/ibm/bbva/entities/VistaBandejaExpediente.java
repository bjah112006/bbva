package com.ibm.bbva.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the VIEW_CE_IBM_CANT_EXPEDIENTES database table.
 * 
 */
@Entity
@Table(name="VIEW_CE_IBM_BANDJ_EXPEDIENTE", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="VistaBandejaExpediente.findAll", query="SELECT v FROM VistaBandejaExpediente v")
})
public class VistaBandejaExpediente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID")
	private long id;	
	@Column(name="ID_TAREA_FK")
	private long idTarea;
	@Column(name="ID_ESTADO_FK")
	private long idEstado;
	@Column(name="ID_OFICINA")
	private long idOficina;	
	@Column(name="ID_SUBPROD")
	private long idSubProd;	
	@Column(name="ID_TIPO_OFERTA")
	private long idTipoOferta;		
	@Column(name="DESCRIP_ESTADO")
	private String desEstado;	
	@Column(name="ID_GRUPO_SEGMENTO_FK")
	private long idGrupoSegmento;	
	@Column(name="DESCRIP_SEGMENTO")
	private String desSegmento;
	@Column(name="DESCRIP_TIPO_OFERTA")
	private String desTipoOferta;
	@Column(name="DESCRIP_TIPO_DOC")
	private String desTipoDoc;
	@Column(name="NUM_DOI")
	private String numDoi;	
	@Column(name="APE_PAT")
	private String apePat;
	@Column(name="APE_MAT")
	private String apeMat;	
	@Column(name="NOMBRE")
	private String nombre;	
	@Column(name="ID_PRODUCTO")
	private long idProd;		
	@Column(name="DESCRIP_PRODUCTO")
	private String desProducto;	
	@Column(name="DESCRIP_SUB_PRODUCTO")
	private String desSubProducto;	
	@Column(name="DESCRIP_TIPO_MONEDA")
	private String desTipoMoneda;	
	@Column(name="DESCRIP_OFICINA")
	private String desOficina;	
	@Column(name="DESCRIP_TERRITORIO")
	private String desTerritorio;	
	@Column(name="RVGL")
	private String rvgl;	
	@Column(name="NRO_CONTRATO")
	private String nroContrato;	
	@Column(name="COD_PRE_EVAL")
	private String codPreEval;	
	@Column(name="COMENTARIO")
	private String comentario;		
	@Column(name="LINEA_CRED_SOL")
	private double lineaCredSol;
	@Column(name="LINEA_CRED_APROB")
	private double lineaCredAprob;
	@Column(name="FLAG_RETRAER")
	private String flagRetraer;	
	@Column(name="NRO_DEV")
	private int nroDevoluciones;	
	
	
	/**
	 * Cambio 27 de mayo
	 * OPTIMIZACION BANDEJA DE ASIGNACION
	 * */
	@Column(name="ID_OFICINA_USU")
	private long idOficinaUsu;
	
	@Column(name="DESCRIP_PERFIL")
	private String desPerfil;	
	
	@Column(name="COD_EMPLEADO")	
	private String codEmpleado;
	
	@Column(name="NOM_EMPLEADO")
	private String nomEmpleado;
	
	@Column(name="ESTADO_CE")
	private long idEstadoCe;
	
	@Column(name="ID_OFICINA_PRINCIPAL_FK")
	private long idOficinaPrincipal;	
	
	@Column(name="FLAG_DESPLAZADA")
	private String flagDesplazada;	
	
	/**
	 * */
	public VistaBandejaExpediente(){
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(long idTarea) {
		this.idTarea = idTarea;
	}

	public long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(long idEstado) {
		this.idEstado = idEstado;
	}

	public String getDesEstado() {
		return desEstado;
	}

	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}

	public String getDesSegmento() {
		return desSegmento;
	}

	public void setDesSegmento(String desSegmento) {
		this.desSegmento = desSegmento;
	}

	public String getDesTipoOferta() {
		return desTipoOferta;
	}

	public void setDesTipoOferta(String desTipoOferta) {
		this.desTipoOferta = desTipoOferta;
	}

	public String getDesTipoDoc() {
		return desTipoDoc;
	}

	public void setDesTipoDoc(String desTipoDoc) {
		this.desTipoDoc = desTipoDoc;
	}

	public String getApePat() {
		return apePat;
	}

	public void setApePat(String apePat) {
		this.apePat = apePat;
	}

	public String getApeMat() {
		return apeMat;
	}

	public void setApeMat(String apeMat) {
		this.apeMat = apeMat;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDesProducto() {
		return desProducto;
	}

	public void setDesProducto(String desProducto) {
		this.desProducto = desProducto;
	}

	public String getDesSubProducto() {
		return desSubProducto;
	}

	public void setDesSubProducto(String desSubProducto) {
		this.desSubProducto = desSubProducto;
	}

	public String getDesTipoMoneda() {
		return desTipoMoneda;
	}

	public void setDesTipoMoneda(String desTipoMoneda) {
		this.desTipoMoneda = desTipoMoneda;
	}

	public String getDesOficina() {
		return desOficina;
	}

	public void setDesOficina(String desOficina) {
		this.desOficina = desOficina;
	}

	public String getDesTerritorio() {
		return desTerritorio;
	}

	public void setDesTerritorio(String desTerritorio) {
		this.desTerritorio = desTerritorio;
	}

	public String getRvgl() {
		return rvgl;
	}

	public void setRvgl(String rvgl) {
		this.rvgl = rvgl;
	}

	public String getNroContrato() {
		return nroContrato;
	}

	public void setNroContrato(String nroContrato) {
		this.nroContrato = nroContrato;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public double getLineaCredSol() {
		return lineaCredSol;
	}

	public void setLineaCredSol(double lineaCredSol) {
		this.lineaCredSol = lineaCredSol;
	}

	public double getLineaCredAprob() {
		return lineaCredAprob;
	}

	public void setLineaCredAprob(double lineaCredAprob) {
		this.lineaCredAprob = lineaCredAprob;
	}

	public String getNumDoi() {
		return numDoi;
	}

	public void setNumDoi(String numDoi) {
		this.numDoi = numDoi;
	}

	public long getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(long idOficina) {
		this.idOficina = idOficina;
	}

	public String getFlagRetraer() {
		return flagRetraer;
	}

	public void setFlagRetraer(String flagRetraer) {
		this.flagRetraer = flagRetraer;
	}

	public String getCodPreEval() {
		return codPreEval;
	}

	public void setCodPreEval(String codPreEval) {
		this.codPreEval = codPreEval;
	}

	public long getIdSubProd() {
		return idSubProd;
	}

	public void setIdSubProd(long idSubProd) {
		this.idSubProd = idSubProd;
	}

	public long getIdTipoOferta() {
		return idTipoOferta;
	}

	public void setIdTipoOferta(long idTipoOferta) {
		this.idTipoOferta = idTipoOferta;
	}

	public long getIdGrupoSegmento() {
		return idGrupoSegmento;
	}

	public void setIdGrupoSegmento(long idGrupoSegmento) {
		this.idGrupoSegmento = idGrupoSegmento;
	}

	public long getIdProd() {
		return idProd;
	}

	public void setIdProd(long idProd) {
		this.idProd = idProd;
	}

	public int getNroDevoluciones() {
		return nroDevoluciones;
	}

	public void setNroDevoluciones(int nroDevoluciones) {
		this.nroDevoluciones = nroDevoluciones;
	}

	public String getDesPerfil() {
		return desPerfil;
	}

	public void setDesPerfil(String desPerfil) {
		this.desPerfil = desPerfil;
	}

	public String getCodEmpleado() {
		return codEmpleado;
	}

	public void setCodEmpleado(String codEmpleado) {
		this.codEmpleado = codEmpleado;
	}

	public String getNomEmpleado() {
		return nomEmpleado;
	}

	public void setNomEmpleado(String nomEmpleado) {
		this.nomEmpleado = nomEmpleado;
	}

	public long getIdEstadoCe() {
		return idEstadoCe;
	}

	public void setIdEstadoCe(long idEstadoCe) {
		this.idEstadoCe = idEstadoCe;
	}

	public long getIdOficinaPrincipal() {
		return idOficinaPrincipal;
	}

	public void setIdOficinaPrincipal(long idOficinaPrincipal) {
		this.idOficinaPrincipal = idOficinaPrincipal;
	}

	public String getFlagDesplazada() {
		return flagDesplazada;
	}

	public void setFlagDesplazada(String flagDesplazada) {
		this.flagDesplazada = flagDesplazada;
	}

	public long getIdOficinaUsu() {
		return idOficinaUsu;
	}

	public void setIdOficinaUsu(long idOficinaUsu) {
		this.idOficinaUsu = idOficinaUsu;
	}

	
}

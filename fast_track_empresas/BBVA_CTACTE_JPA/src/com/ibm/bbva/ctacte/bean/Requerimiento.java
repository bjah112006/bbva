package com.ibm.bbva.ctacte.bean;
// default package
// Generated 17/05/2012 03:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name = "TBL_CE_IBM_REQUERIMIENTO_CC", schema = "CONELE")
public class Requerimiento implements java.io.Serializable {

	private static final long serialVersionUID = 1678278869359113033L;

	private Long id								;
	private String cod_central                         ;
	private String razon_social                        ;
	private String nro_doi                        ;
	private Long tipo_operacion                      ;
	private String es_migrado                          ;
	private Integer version                             ;
	private byte [] descripcion_reclamo_Consulta        ;
	private Long detalle_categoria_reclamo           ;
	private byte [] descripcion_absolucion              ;
	private String reconsideracion                     ;
	private byte  []motivo_reconsideracion              ;
	private String usuario_registra                    ;
	private String usuario_absuelve                    ;
	private String usuario_reconsidera                 ;
	private String usuario_absuelve_reconsidera    ;
	private Date fecha_registro                      ;
	private Date fecha_absuelve                      ;
	private Date fecha_reconsidera                   ;
	private Date fecha_absuelve_reconsidera      ;
	private Long estado                              ;
	private Long resultado                              ;
	private String nombre_usuario_registra;
	private Long idClasificacion;
	private String tipoDoi;
	private String cod_oficina;
	private byte [] descripcion_absolucion_recon;
	private String idAbogado;
	private String nombreAbogadoAsignado;
	
	
	@Id
	@SequenceGenerator(name = "seqRequerimiento", sequenceName = "SEQ_CE_IBM_REQUERIM_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqRequerimiento")
	@Column(name = "ID", unique = true, nullable = false, precision = 36, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "COD_CENTRAL", length = 8)
	public String getCod_central() {
		return cod_central;
	}
	public void setCod_central(String cod_central) {
		this.cod_central = cod_central;
	}
	
	@Column(name = "RAZON_SOCIAL", length = 100)
	public String getRazon_social() {
		return razon_social;
	}
	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}
	@Column(name = "TIPO_OPERACION", precision = 5, scale = 0)
	public Long getTipo_operacion() {
		return tipo_operacion;
	}
	public void setTipo_operacion(Long tipo_operacion) {
		this.tipo_operacion = tipo_operacion;
	}
	@Column(name = "ES_MIGRADO", length = 1)
	public String getEs_migrado() {
		return es_migrado;
	}
	public void setEs_migrado(String es_migrado) {
		this.es_migrado = es_migrado;
	}
	
	@Column(name = "VERSION",precision = 38, scale = 0 )
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Column(name = "DESCRIPCION_RECLAMO_CONSULTA")
	public byte[] getDescripcion_reclamo_Consulta() {
		return descripcion_reclamo_Consulta;
	}
	public void setDescripcion_reclamo_Consulta(byte[] descripcion_reclamo_Consulta) {
		this.descripcion_reclamo_Consulta = descripcion_reclamo_Consulta;
	}
	
	@Column(name = "DETALLE_CATEGORIA_RECLAMO", precision = 5, scale = 0)
	public Long getDetalle_categoria_reclamo() {
		return detalle_categoria_reclamo;
	}
	public void setDetalle_categoria_reclamo(Long detalle_categoria_reclamo) {
		this.detalle_categoria_reclamo = detalle_categoria_reclamo;
	}
	
	@Column(name = "DESCRIPCION_ABSOLUCION")
	public byte [] getDescripcion_absolucion() {
		return descripcion_absolucion;
	}
	public void setDescripcion_absolucion(byte [] descripcion_absolucion) {
		this.descripcion_absolucion = descripcion_absolucion;
	}
	
	@Column(name = "RECONSIDERACION", length=1)	
	public String getReconsideracion() {
		return reconsideracion;
	}
	public void setReconsideracion(String reconsideracion) {
		this.reconsideracion = reconsideracion;
	}
	
	@Column(name = "MOTIVO_RECONSIDERACION")	
	public byte[] getMotivo_reconsideracion() {
		return motivo_reconsideracion;
	}
	public void setMotivo_reconsideracion(byte[] motivo_reconsideracion) {
		this.motivo_reconsideracion = motivo_reconsideracion;
	}
	
	@Column(name = "USUARIO_REGISTRA", length=8)	
	public String getUsuario_registra() {
		return usuario_registra;
	}
	public void setUsuario_registra(String usuario_registra) {
		this.usuario_registra = usuario_registra;
	}
	
	@Column(name = "USUARIO_ABSUELVE", length=8)	
	public String getUsuario_absuelve() {
		return usuario_absuelve;
	}
	public void setUsuario_absuelve(String usuario_absuelve) {
		this.usuario_absuelve = usuario_absuelve;
	}
	
	@Column(name = "USUARIO_RECONSIDERA", length=8)	
	public String getUsuario_reconsidera() {
		return usuario_reconsidera;
	}
	public void setUsuario_reconsidera(String usuario_reconsidera) {
		this.usuario_reconsidera = usuario_reconsidera;
	}
	
	@Column(name = "USUARIO_ABSUELVE_RECONSIDERA", length=8)	
	public String getUsuario_absuelve_reconsidera() {
		return usuario_absuelve_reconsidera;
	}
	public void setUsuario_absuelve_reconsidera(
			String usuario_absuelve_reconsidera) {
		this.usuario_absuelve_reconsidera = usuario_absuelve_reconsidera;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_REGISTRO", length = 10)
	public Date getFecha_registro() {
		return fecha_registro;
	}
	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ABSUELVE", length = 10)
	public Date getFecha_absuelve() {
		return fecha_absuelve;
	}	
	public void setFecha_absuelve(Date fecha_absuelve) {
		this.fecha_absuelve = fecha_absuelve;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_RECONSIDERA", length = 10)
	public Date getFecha_reconsidera() {
		return fecha_reconsidera;
	}
	public void setFecha_reconsidera(Date fecha_reconsidera) {
		this.fecha_reconsidera = fecha_reconsidera;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ABSUELVE_RECONSIDERA", length = 10)
	public Date getFecha_absuelve_reconsidera() {
		return fecha_absuelve_reconsidera;
	}
	public void setFecha_absuelve_reconsidera(
			Date fecha_absuelve_reconsidera) {
		this.fecha_absuelve_reconsidera = fecha_absuelve_reconsidera;
	}
	
	@Column(name = "ESTADO", precision = 5, scale = 0)
	public Long getEstado() {
		return estado;
	}
	public void setEstado(Long estado) {
		this.estado = estado;
	}
	
	@Column(name = "NRO_DOI", length=20)	
	public String getNro_doi() {
		return nro_doi;
	}
	public void setNro_doi(String nro_doi) {
		this.nro_doi = nro_doi;
	}
	
	@Column(name = "RESULTADO", precision = 5, scale = 0)
	public Long getResultado() {
		return resultado;
	}
	public void setResultado(Long resultado) {
		this.resultado = resultado;
	}
	
	@Column(name = "NOMBRE_USUARIO_REGISTRA", length=100)	
	public String getNombre_usuario_registra() {
		return nombre_usuario_registra;
	}
	public void setNombre_usuario_registra(String nombre_usuario_registra) {
		this.nombre_usuario_registra = nombre_usuario_registra;
	}
	
	@Column(name = "ID_CLASIFICACION", precision = 5, scale = 0)
	public Long getIdClasificacion() {
		return idClasificacion;
	}
	public void setIdClasificacion(Long idClasificacion) {
		this.idClasificacion = idClasificacion;
	}
	@Column(name = "TIPO_DOI", length = 35)
	public String getTipoDoi() {
		return tipoDoi;
	}
	public void setTipoDoi(String tipoDoi) {
		this.tipoDoi = tipoDoi;
	}	
	@Column(name = "COD_OFICINA", length = 5)
	public String getCod_oficina() {
		return cod_oficina;
	}
	public void setCod_oficina(String cod_oficina) {
		this.cod_oficina = cod_oficina;
	}
	
	@Column(name = "DESCRIPCION_ABSOLUCION_RECON")	
	public byte [] getDescripcion_absolucion_recon() {
		return descripcion_absolucion_recon;
	}
	public void setDescripcion_absolucion_recon(
			byte [] descripcion_absolucion_recon) {
		this.descripcion_absolucion_recon = descripcion_absolucion_recon;
	}
	@Column(name = "ID_ABOGADO", length = 8)
	public String getIdAbogado() {
		return idAbogado;
	}
	public void setIdAbogado(String idAbogado) {
		this.idAbogado = idAbogado;
	}
	@Column(name = "NOMBRE_ABOGADO_ASIGNADO", length = 100)
	public String getNombreAbogadoAsignado() {
		return nombreAbogadoAsignado;
	}
	public void setNombreAbogadoAsignado(String nombreAbogadoAsignado) {
		this.nombreAbogadoAsignado = nombreAbogadoAsignado;
	}
	

}

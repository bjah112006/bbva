package com.ibm.bbva.ctacte.model;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import com.ibm.bbva.ctacte.bean.Attachment;
import com.ibm.bbva.ctacte.bean.Requerimiento;
import com.ibm.websphere.management.cmdframework.UploadFile;

public class RequerimientoModel {
	private Requerimiento requerimiento;
	private Long id;
	private String strId;
	private String cod_central;
	private String nro_identidad;
	private String razon_social;
	private String nro_doi;
	private String tipoDoi;
	private Long tipo_operacion;
	private String es_migrado;
	private Integer version;
	private String descripcion_reclamo_Consulta;
	private Long detalle_categoria_reclamo;
	private String str_categoria_reclamo;
	private String descripcion_absolucion;
	private boolean reconsideracion;
	private String motivo_reconsideracion;
	private String usuario_registra;
	private String usuario_absuelve;
	private String usuario_reconsidera;
	private String usuario_absuelve_reconsidera;
	private Date fecha_registro;
	private Date fecha_absuelve;
	private Date fecha_reconsidera;
	private Date fecha_absuelve_reconsidera;
	private Long estado;
	private String rucDni;
	private String oficina;
	private String strEstado;
	private Date fechaInicio;
	private Date fechafin;
	private boolean incluirCerrados;
	private boolean showCategoriaReclamo;
	private String str_tipo_operacion;
	private String srt_gestorRegistrante;
	private String descripcion_reclamo_Consulta_vp;
	private Long dictamen_reclamo;	
	private Long idResultado;
	private String str_resultado;
	private Long idClasificacion;
	private String nombre_usuario_registra;
	private boolean showReconsideracion;
	private boolean showResultado;
	private boolean showClasificacion;
	private boolean showDictamenReclamo;
	private boolean showAbsolucionRecon;
	private boolean showAbsolucion;
	private boolean disableReconsideracion;
	private boolean disableResultado;
	private boolean disableClasificacion;
	private boolean disableDictamenReclamo;
	private boolean disableAbsolucionRecon;
	private boolean disableAbsolucion;
	private boolean readOnly;
	private boolean esAbogado;
	private String textoDetalleCategoria;
	private boolean disableBtnRegistrar;
	private String str_oficina;
	private String cod_oficina;
	private List<Attachment> listaAttachment;
	
	private String descripcion_absolucion_recon;
	private String mensajeExito;
	private String mensajeError;
	
	private String strFechaRegistro;
	
	private String strDescripcion_reclamo_Consulta;
	private String strDescripcion_absolucion;
	private String strMotivo_reconsideracion;
	private String strDescripcion_absolucion_recon;
	
	private String idAbogado;
	private String nombreAbogadoAsignado;
	
//	private UploadFil  archivo;
	public RequerimientoModel() {
		

	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechafin() {
		return fechafin;
	}

	public void setFechafin(Date fechafin) {
		this.fechafin = fechafin;
	}

	public boolean isIncluirCerrados() {
		return incluirCerrados;
	}

	public void setIncluirCerrados(boolean incluirCerrados) {
		this.incluirCerrados = incluirCerrados;
	}

	public String getRucDni() {
		return rucDni;
	}

	public void setRucDni(String rucDni) {
		this.rucDni = rucDni;
	}


	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getStrEstado() {
		return strEstado;
	}

	public void setStrEstado(String strEstado) {
		this.strEstado = strEstado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCod_central() {
		return cod_central;
	}

	public void setCod_central(String cod_central) {
		this.cod_central = cod_central;
	}

	public String getNro_identidad() {
		return nro_identidad;
	}

	public void setNro_identidad(String nro_identidad) {
		this.nro_identidad = nro_identidad;
	}

	public String getRazon_social() {
		return razon_social;
	}

	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}

	public Long getTipo_operacion() {
		return tipo_operacion;
	}

	public void setTipo_operacion(Long tipo_operacion) {
		this.tipo_operacion = tipo_operacion;
	}

	public String getEs_migrado() {
		return es_migrado;
	}

	public void setEs_migrado(String es_migrado) {
		this.es_migrado = es_migrado;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	

	public String getDescripcion_reclamo_Consulta() {
		return descripcion_reclamo_Consulta;
	}

	public void setDescripcion_reclamo_Consulta(String descripcion_reclamo_Consulta) {
		this.descripcion_reclamo_Consulta = descripcion_reclamo_Consulta;
	}

	public Long getDetalle_categoria_reclamo() {
		return detalle_categoria_reclamo;
	}

	public void setDetalle_categoria_reclamo(Long detalle_categoria_reclamo) {
		this.detalle_categoria_reclamo = detalle_categoria_reclamo;
	}

	public String getDescripcion_absolucion() {
		return descripcion_absolucion;
	}

	public void setDescripcion_absolucion(String descripcion_absolucion) {
		this.descripcion_absolucion = descripcion_absolucion;
	}


	public String getMotivo_reconsideracion() {
		return motivo_reconsideracion;
	}

	public void setMotivo_reconsideracion(String motivo_reconsideracion) {
		this.motivo_reconsideracion = motivo_reconsideracion;
	}

	public String getUsuario_registra() {
		return usuario_registra;
	}

	public void setUsuario_registra(String usuario_registra) {
		this.usuario_registra = usuario_registra;
	}

	public String getUsuario_absuelve() {
		return usuario_absuelve;
	}

	public void setUsuario_absuelve(String usuario_absuelve) {
		this.usuario_absuelve = usuario_absuelve;
	}

	public String getUsuario_reconsidera() {
		return usuario_reconsidera;
	}

	public void setUsuario_reconsidera(String usuario_reconsidera) {
		this.usuario_reconsidera = usuario_reconsidera;
	}

	public String getUsuario_absuelve_reconsidera() {
		return usuario_absuelve_reconsidera;
	}

	public void setUsuario_absuelve_reconsidera(
			String usuario_absuelve_reconsidera) {
		this.usuario_absuelve_reconsidera = usuario_absuelve_reconsidera;
	}

	public Date getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	public Date getFecha_absuelve() {
		return fecha_absuelve;
	}

	public void setFecha_absuelve(Date fecha_absuelve) {
		this.fecha_absuelve = fecha_absuelve;
	}

	public Date getFecha_reconsidera() {
		return fecha_reconsidera;
	}

	public void setFecha_reconsidera(Date fecha_reconsidera) {
		this.fecha_reconsidera = fecha_reconsidera;
	}

	public Date getFecha_absuelve_reconsidera() {
		return fecha_absuelve_reconsidera;
	}

	public void setFecha_absuelve_reconsidera(Date fecha_absuelve_reconsidera) {
		this.fecha_absuelve_reconsidera = fecha_absuelve_reconsidera;
	}

	public Long getEstado() {
		return estado;
	}

	public void setEstado(Long estado) {
		this.estado = estado;
	}

	public boolean isShowCategoriaReclamo() {
		return showCategoriaReclamo;
	}

	public void setShowCategoriaReclamo(boolean showCategoriaReclamo) {
		this.showCategoriaReclamo = showCategoriaReclamo;
	}
	

	
	

	@Override
	public String toString() {
		return "RequerimientoModel [id=" + id + ", cod_central=" + cod_central
				+ ", nro_identidad=" + nro_identidad + ", razon_social="
				+ razon_social + ", nro_doi=" + nro_doi + ", tipoDoi="
				+ tipoDoi + ", tipo_operacion=" + tipo_operacion
				+ ", es_migrado=" + es_migrado + ", version=" + version
				+ ", descripcion_reclamo_Consulta="
				+ descripcion_reclamo_Consulta + ", detalle_categoria_reclamo="
				+ detalle_categoria_reclamo + ", str_categoria_reclamo="
				+ str_categoria_reclamo + ", descripcion_absolucion="
				+ descripcion_absolucion + ", reconsideracion="
				+ reconsideracion + ", motivo_reconsideracion="
				+ motivo_reconsideracion + ", usuario_registra="
				+ usuario_registra + ", usuario_absuelve=" + usuario_absuelve
				+ ", usuario_reconsidera=" + usuario_reconsidera
				+ ", usuario_absuelve_reconsidera="
				+ usuario_absuelve_reconsidera + ", fecha_registro="
				+ fecha_registro + ", fecha_absuelve=" + fecha_absuelve
				+ ", fecha_reconsidera=" + fecha_reconsidera
				+ ", fecha_absuelve_reconsidera=" + fecha_absuelve_reconsidera
				+ ", estado=" + estado + ", rucDni=" + rucDni + ", oficina="
				+ oficina + ", strEstado=" + strEstado + ", fechaInicio="
				+ fechaInicio + ", fechafin=" + fechafin + ", incluirCerrados="
				+ incluirCerrados + ", showCategoriaReclamo="
				+ showCategoriaReclamo + ", str_tipo_operacion="
				+ str_tipo_operacion + ", srt_gestorRegistrante="
				+ srt_gestorRegistrante + ", descripcion_reclamo_Consulta_vp="
				+ descripcion_reclamo_Consulta_vp + ", dictamen_reclamo="
				+ dictamen_reclamo + ", showDictamenReclamo="
				+ showDictamenReclamo + ", idResultado=" + idResultado
				+ ", str_resultado=" + str_resultado + ", idClasificacion="
				+ idClasificacion + ", nombre_usuario_registra="
				+ nombre_usuario_registra + ", showReconsideracion="
				+ showReconsideracion + ", showResultado=" + showResultado
				+ ", showClasificacion=" + showClasificacion
				+ ", disableReconsideracion=" + disableReconsideracion
				+ ", disableResultado=" + disableResultado
				+ ", disableClasificacion=" + disableClasificacion
				+ ", readOnly=" + readOnly + ", esAbogado=" + esAbogado
				+ ", textoDetalleCategoria=" + textoDetalleCategoria + "]";
	}

	public String getNro_doi() {
		return nro_doi;
	}

	public void setNro_doi(String nro_doi) {
		this.nro_doi = nro_doi;
	}

	public boolean isReconsideracion() {
		return reconsideracion;
	}

	public void setReconsideracion(boolean reconsideracion) {
		this.reconsideracion = reconsideracion;
	}

	public String getStr_tipo_operacion() {
		return str_tipo_operacion;
	}

	public void setStr_tipo_operacion(String str_tipo_operacion) {
		this.str_tipo_operacion = str_tipo_operacion;
	}

	public String getSrt_gestorRegistrante() {
		return srt_gestorRegistrante;
	}

	public void setSrt_gestorRegistrante(String srt_gestorRegistrante) {
		this.srt_gestorRegistrante = srt_gestorRegistrante;
	}

	public String getDescripcion_reclamo_Consulta_vp() {
		descripcion_reclamo_Consulta_vp=descripcion_reclamo_Consulta.length()<=99?descripcion_reclamo_Consulta.substring(0, descripcion_reclamo_Consulta.length()):descripcion_reclamo_Consulta.substring(0,100);
		return descripcion_reclamo_Consulta_vp;
	}

	public void setDescripcion_reclamo_Consulta_vp(
			String descripcion_reclamo_Consulta_vp) {
		this.descripcion_reclamo_Consulta_vp = descripcion_reclamo_Consulta_vp;
	}

	public Long getDictamen_reclamo() {
		return dictamen_reclamo;
	}

	public void setDictamen_reclamo(Long dictamen_reclamo) {
		this.dictamen_reclamo = dictamen_reclamo;
	}

	public boolean isShowDictamenReclamo() {
		return showDictamenReclamo;
	}

	public void setShowDictamenReclamo(boolean showDictamenReclamo) {
		this.showDictamenReclamo = showDictamenReclamo;
	}

	public String getStr_resultado() {
		return str_resultado;
	}

	public void setStr_resultado(String str_resultado) {
		this.str_resultado = str_resultado;
	}

	public Long getIdClasificacion() {
		return idClasificacion;
	}

	public void setIdClasificacion(Long idClasificacion) {
		this.idClasificacion = idClasificacion;
	}

	public Long getIdResultado() {
		return idResultado;
	}

	public void setIdResultado(Long idResultado) {
		this.idResultado = idResultado;
	}

	public String getStr_categoria_reclamo() {
		return str_categoria_reclamo;
	}

	public void setStr_categoria_reclamo(String str_categoria_reclamo) {
		this.str_categoria_reclamo = str_categoria_reclamo;
	}

	public String getNombre_usuario_registra() {
		return nombre_usuario_registra;
	}

	public void setNombre_usuario_registra(String nombre_usuario_registra) {
		this.nombre_usuario_registra = nombre_usuario_registra;
	}

	public boolean isShowReconsideracion() {
		return showReconsideracion;
	}

	public void setShowReconsideracion(boolean showReconsideracion) {
		this.showReconsideracion = showReconsideracion;
	}

	public boolean isShowResultado() {
		return showResultado;
	}

	public void setShowResultado(boolean showResultado) {
		this.showResultado = showResultado;
	}

	public boolean isDisableReconsideracion() {
		return disableReconsideracion;
	}

	public void setDisableReconsideracion(boolean disableReconsideracion) {
		this.disableReconsideracion = disableReconsideracion;
	}

	public boolean isDisableResultado() {
		return disableResultado;
	}

	public void setDisableResultado(boolean disableResultado) {
		this.disableResultado = disableResultado;
	}

	public boolean isDisableClasificacion() {
		return disableClasificacion;
	}

	public void setDisableClasificacion(boolean disableClasificacion) {
		this.disableClasificacion = disableClasificacion;
	}

	public boolean isShowClasificacion() {
		return showClasificacion;
	}

	public void setShowClasificacion(boolean showClasificacion) {
		this.showClasificacion = showClasificacion;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isEsAbogado() {
		return esAbogado;
	}

	public void setEsAbogado(boolean esAbogado) {
		this.esAbogado = esAbogado;
	}

	public String getTipoDoi() {
		return tipoDoi;
	}

	public void setTipoDoi(String tipoDoi) {
		this.tipoDoi = tipoDoi;
	}

	public String getTextoDetalleCategoria() {
		return textoDetalleCategoria;
	}

	public void setTextoDetalleCategoria(String textoDetalleCategoria) {
		this.textoDetalleCategoria = textoDetalleCategoria;
	}

	public boolean isDisableBtnRegistrar() {
		return disableBtnRegistrar;
	}

	public void setDisableBtnRegistrar(boolean disableBtnRegistrar) {
		this.disableBtnRegistrar = disableBtnRegistrar;
	}

	public String getCod_oficina() {
		return cod_oficina;
	}

	public void setCod_oficina(String cod_oficina) {
		this.cod_oficina = cod_oficina;
	}

	public List<Attachment> getListaAttachment() {
		return listaAttachment;
	}

	public void setListaAttachment(List<Attachment> listaAttachment) {
		this.listaAttachment = listaAttachment;
	}

	public String getStr_oficina() {
		return str_oficina;
	}

	public void setStr_oficina(String str_oficina) {
		this.str_oficina = str_oficina;
	}

	public String getDescripcion_absolucion_recon() {
		return descripcion_absolucion_recon;
	}

	public void setDescripcion_absolucion_recon(
			String descripcion_absolucion_recon) {
		this.descripcion_absolucion_recon = descripcion_absolucion_recon;
	}

	public String getStrId() {
		return strId;
	}

	public void setStrId(String strId) {
		this.strId = strId;
	}

	public boolean isShowAbsolucionRecon() {
		return showAbsolucionRecon;
	}

	public void setShowAbsolucionRecon(boolean showAbsolucionRecon) {
		this.showAbsolucionRecon = showAbsolucionRecon;
	}

	public boolean isDisableAbsolucionRecon() {
		return disableAbsolucionRecon;
	}

	public void setDisableAbsolucionRecon(boolean disableAbsolucionRecon) {
		this.disableAbsolucionRecon = disableAbsolucionRecon;
	}

	public boolean isShowAbsolucion() {
		return showAbsolucion;
	}

	public void setShowAbsolucion(boolean showAbsolucion) {
		this.showAbsolucion = showAbsolucion;
	}

	public boolean isDisableAbsolucion() {
		return disableAbsolucion;
	}

	public void setDisableAbsolucion(boolean disableAbsolucion) {
		this.disableAbsolucion = disableAbsolucion;
	}

	public boolean isDisableDictamenReclamo() {
		return disableDictamenReclamo;
	}

	public void setDisableDictamenReclamo(boolean disableDictamenReclamo) {
		this.disableDictamenReclamo = disableDictamenReclamo;
	}

	public String getMensajeExito() {
		return mensajeExito;
	}

	public void setMensajeExito(String mensajeExito) {
		this.mensajeExito = mensajeExito;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}


	public String getStrFechaRegistro() {
		return strFechaRegistro;
	}

	public void setStrFechaRegistro(String strFechaRegistro) {
		this.strFechaRegistro = strFechaRegistro;
	}

	public String getStrDescripcion_reclamo_Consulta() {
		return strDescripcion_reclamo_Consulta;
	}

	public void setStrDescripcion_reclamo_Consulta(
			String strDescripcion_reclamo_Consulta) {
		this.strDescripcion_reclamo_Consulta = strDescripcion_reclamo_Consulta;
	}

	public String getStrDescripcion_absolucion() {
		return strDescripcion_absolucion;
	}

	public void setStrDescripcion_absolucion(String strDescripcion_absolucion) {
		this.strDescripcion_absolucion = strDescripcion_absolucion;
	}

	public String getStrMotivo_reconsideracion() {
		return strMotivo_reconsideracion;
	}

	public void setStrMotivo_reconsideracion(String strMotivo_reconsideracion) {
		this.strMotivo_reconsideracion = strMotivo_reconsideracion;
	}

	public String getStrDescripcion_absolucion_recon() {
		return strDescripcion_absolucion_recon;
	}

	public void setStrDescripcion_absolucion_recon(
			String strDescripcion_absolucion_recon) {
		this.strDescripcion_absolucion_recon = strDescripcion_absolucion_recon;
	}

	public String getIdAbogado() {
		return idAbogado;
	}

	public void setIdAbogado(String idAbogado) {
		this.idAbogado = idAbogado;
	}

	public String getNombreAbogadoAsignado() {
		return nombreAbogadoAsignado;
	}

	public void setNombreAbogadoAsignado(String nombreAbogadoAsignado) {
		this.nombreAbogadoAsignado = nombreAbogadoAsignado;
	}

	public Requerimiento getRequerimiento() {
		Requerimiento r=new Requerimiento();
		r.setId(id);
		r.setCod_central(cod_central);
		r.setDescripcion_absolucion(descripcion_absolucion==null?null:descripcion_absolucion.isEmpty()?null:descripcion_absolucion.getBytes());
		r.setDescripcion_reclamo_Consulta(descripcion_reclamo_Consulta==null?null:descripcion_reclamo_Consulta.isEmpty()?null:descripcion_reclamo_Consulta.getBytes());
		r.setMotivo_reconsideracion(motivo_reconsideracion==null?null:motivo_reconsideracion.isEmpty()?null:motivo_reconsideracion.getBytes());
		r.setDetalle_categoria_reclamo(detalle_categoria_reclamo);
		r.setEs_migrado(es_migrado.equalsIgnoreCase("SI")?"S":"N");
		r.setEstado(estado);
		r.setResultado(idResultado);
		r.setFecha_absuelve(fecha_absuelve);
		r.setFecha_absuelve_reconsidera(fecha_absuelve_reconsidera);
		r.setFecha_reconsidera(fecha_reconsidera);
		r.setFecha_registro(fecha_registro);
		r.setNro_doi(nro_doi);
		r.setRazon_social(razon_social);
		r.setVersion(version);
		r.setReconsideracion(reconsideracion==false?"N":"S");
		r.setTipo_operacion(tipo_operacion);
		r.setUsuario_absuelve(usuario_absuelve);
		r.setUsuario_absuelve_reconsidera(usuario_absuelve_reconsidera);
		r.setUsuario_reconsidera(usuario_reconsidera);
		r.setUsuario_registra(usuario_registra);
		r.setNombre_usuario_registra(nombre_usuario_registra);
		r.setResultado(idResultado);
		r.setIdClasificacion(idClasificacion);
		r.setTipoDoi(tipoDoi);
		r.setCod_oficina(cod_oficina);
		r.setDescripcion_absolucion_recon(descripcion_absolucion_recon==null?null:descripcion_absolucion_recon.isEmpty()?null:descripcion_absolucion_recon.getBytes());
		
		return requerimiento;
	}

	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}




	

	
	
}

package com.pe.bbva.pyme.model;

public class Solicitud {
	private String nroSolicitud;
	private String tipoDOICliente;
	private String nroDOICliente;
	private String nombreCliente;
	private String nombreTarea;
	private String tipoOferta;
	private String oficinaSolicitud;
	private String fechaLlegada;
	private String rolEjecutorTarea;
	private String usuarioEjecutorTarea;
	private String fechaEnvio;
	private String nroRVGL;
	private String nroContrato;
	private String nroGarantia;
	private String dictamen;
	private String estado;
	//jcisneros 10.06.2015: campos adicionales
	private String producto;
	private String campania;
	private String causal_clte_cancela;
	private String causal_devol_gmc;
	private String clasificacion_clte;
	private String moneda;
	private String monto;
	private String plazo;
	private String tasa;
	private String abn_registante;
	private String num_preimpreso;
	private String centro_negocio_riesgos;
	
	public Solicitud() {
		super();
	}
	public Solicitud(Solicitud solicitud) {
		super();
		this.nroSolicitud = solicitud.nroSolicitud;
		this.tipoDOICliente = solicitud.tipoDOICliente;
		this.nroDOICliente = solicitud.nroDOICliente;
		this.nombreCliente = solicitud.nombreCliente;
		this.nombreTarea = solicitud.nombreTarea;
		this.tipoOferta = solicitud.tipoOferta;
		this.oficinaSolicitud = solicitud.oficinaSolicitud;
		this.fechaLlegada = solicitud.fechaLlegada;
		this.rolEjecutorTarea = solicitud.rolEjecutorTarea;
		this.usuarioEjecutorTarea = solicitud.usuarioEjecutorTarea;
		this.fechaEnvio = solicitud.fechaEnvio;
		this.nroRVGL = solicitud.nroRVGL;
		this.nroContrato = solicitud.nroContrato;
		this.nroGarantia = solicitud.nroGarantia;
		this.dictamen = solicitud.dictamen;
		this.estado = solicitud.estado;
		
		this.producto=solicitud.producto;
		this.campania=solicitud.campania;
		this.causal_clte_cancela=solicitud.causal_clte_cancela;
		this.causal_devol_gmc=solicitud.causal_devol_gmc;
		this.clasificacion_clte=solicitud.clasificacion_clte;
		this.moneda=solicitud.moneda;
		this.monto=solicitud.monto;
		this.abn_registante=solicitud.abn_registante;
		this.tasa=solicitud.tasa;
		this.plazo=solicitud.plazo;
		this.num_preimpreso=solicitud.num_preimpreso;
		
		this.centro_negocio_riesgos=solicitud.centro_negocio_riesgos;
	}
	
	public String getNroSolicitud() {
		return nroSolicitud;
	}
	
	public void setNroSolicitud(String nroSolicitud) {
		this.nroSolicitud = nroSolicitud;
	}
	
	public String getTipoDOICliente() {
		return tipoDOICliente;
	}
	
	public void setTipoDOICliente(String tipoDOICliente) {
		this.tipoDOICliente = tipoDOICliente;
	}
	
	public String getNroDOICliente() {
		return nroDOICliente;
	}
	
	public void setNroDOICliente(String nroDOICliente) {
		this.nroDOICliente = nroDOICliente;
	}
	
	public String getNombreCliente() {
		return nombreCliente;
	}
	
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	
	public String getNombreTarea() {
		return nombreTarea;
	}
	
	public void setNombreTarea(String nombreTarea) {
		this.nombreTarea = nombreTarea;
	}
	
	public String getTipoOferta() {
		return tipoOferta;
	}
	public void setTipoOferta(String tipoOferta) {
		this.tipoOferta = tipoOferta;
	}
	
	public String getOficinaSolicitud() {
		return oficinaSolicitud;
	}
	
	public void setOficinaSolicitud(String oficinaSolicitud) {
		this.oficinaSolicitud = oficinaSolicitud;
	}
	
	public String getRolEjecutorTarea() {
		return rolEjecutorTarea;
	}
	
	public void setRolEjecutorTarea(String rolEjecutorTarea) {
		this.rolEjecutorTarea = rolEjecutorTarea;
	}
	
	public String getUsuarioEjecutorTarea() {
		return usuarioEjecutorTarea;
	}
	
	public void setUsuarioEjecutorTarea(String usuarioEjecutorTarea) {
		this.usuarioEjecutorTarea = usuarioEjecutorTarea;
	}
	
	public String getNroRVGL() {
		return nroRVGL;
	}
	
	public void setNroRVGL(String nroRVGL) {
		this.nroRVGL = nroRVGL;
	}
	
	public String getNroContrato() {
		return nroContrato;
	}
	
	public void setNroContrato(String nroContrato) {
		this.nroContrato = nroContrato;
	}
	
	public String getNroGarantia() {
		return nroGarantia;
	}
	
	public void setNroGarantia(String nroGarantia) {
		this.nroGarantia = nroGarantia;
	}
	
	public String getDictamen() {
		return dictamen;
	}
	
	public void setDictamen(String dictamen) {
		this.dictamen = dictamen;
	}
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getFechaLlegada() {
		return fechaLlegada;
	}
	
	public void setFechaLlegada(String fechaLlegada) {
		this.fechaLlegada = fechaLlegada;
	}
	
	public String getFechaEnvio() {
		return fechaEnvio;
	}
	
	public void setFechaEnvio(String fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public String getCampania() {
		return campania;
	}
	public void setCampania(String campania) {
		this.campania = campania;
	}
	public String getCausal_clte_cancela() {
		return causal_clte_cancela;
	}
	public void setCausal_clte_cancela(String causal_clte_cancela) {
		this.causal_clte_cancela = causal_clte_cancela;
	}
	public String getCausal_devol_gmc() {
		return causal_devol_gmc;
	}
	public void setCausal_devol_gmc(String causal_devol_gmc) {
		this.causal_devol_gmc = causal_devol_gmc;
	}
	public String getClasificacion_clte() {
		return clasificacion_clte;
	}
	public void setClasificacion_clte(String clasificacion_clte) {
		this.clasificacion_clte = clasificacion_clte;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getPlazo() {
		return plazo;
	}
	public void setPlazo(String plazo) {
		this.plazo = plazo;
	}
	public String getTasa() {
		return tasa;
	}
	public void setTasa(String tasa) {
		this.tasa = tasa;
	}
	public String getAbn_registante() {
		return abn_registante;
	}
	public void setAbn_registante(String abn_registante) {
		this.abn_registante = abn_registante;
	}
	public String getNum_preimpreso() {
		return num_preimpreso;
	}
	public void setNum_preimpreso(String num_preimpreso) {
		this.num_preimpreso = num_preimpreso;
	}
	
	public String getCentro_negocio_riesgos() {
		return centro_negocio_riesgos;
	}
	public void setCentro_negocio_riesgos(String centro_negocio_riesgos) {
		this.centro_negocio_riesgos = centro_negocio_riesgos;
	}
}

package pe.ibm.bean;

import java.io.Serializable;

public class ExpedienteTC implements Serializable {

	private static final long serialVersionUID = 6876468622158533062L;
	
	private String codigo; 
	private String estado;  
	private String accion;  
	private String verificacionDomiciliaria;  
	private String modificacionScoring;  
	private String scoringAprobado;  
	private String devueltoPor;  
	private String segmento; 
	private String tipoOferta;	 
	private Double lineaCredito;  
	private String moneda;  
	private Cliente cliente;
	private Producto producto;
	private String codigoRVGL;
	private String codigoPreEvaluador;
	private String idOficina;
	private String idTerritorio;
	private String codigoEmpleadoResponsable;
	private String codigoUsuarioActual;
	private String idPerfilUsuarioActual;
	private String perfilUsuarioActual;
	private String nombreUsuarioAnterior;
	private String perfilUsuarioAnterior;
	private String codigoUsuarioAnterior;
	private String numeroContrato;
	private String envioContent;
	private String flagProvincia;
	private String flagRetraer; 
	private String idPerfilUsuarioAnterior;
	private String flagSubrogacion;
	private Integer numeroDevoluciones;
	
	private String idTipoOferta;
	private String idGrupoSegmento;


	public String getIdTipoOferta() {
		return idTipoOferta;
	}

	public void setIdTipoOferta(String idTipoOferta) {
		this.idTipoOferta = idTipoOferta;
	}

	public String getIdGrupoSegmento() {
		return idGrupoSegmento;
	}

	public void setIdGrupoSegmento(String idGrupoSegmento) {
		this.idGrupoSegmento = idGrupoSegmento;
	}

	public Integer getNumeroDevoluciones() {
		return numeroDevoluciones;
	}

	public void setNumeroDevoluciones(Integer numeroDevoluciones) {
		this.numeroDevoluciones = numeroDevoluciones;
	}
	public String getPerfilUsuarioActual() {
		return perfilUsuarioActual;
	}

	public void setPerfilUsuarioActual(String perfilUsuarioActual) {
		this.perfilUsuarioActual = perfilUsuarioActual;
	}
	
	public String getFlagRetraer() {
		return flagRetraer;
	}

	public void setFlagRetraer(String flagRetraer) {
		this.flagRetraer = flagRetraer;
	}
	
	public String getFlagProvincia() {
		return flagProvincia;
	}

	public void setFlagProvincia(String flagProvincia) {
		this.flagProvincia = flagProvincia;
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
	
	/**
	 * Asigna el proceso
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getVerificacionDomiciliaria() {
		return verificacionDomiciliaria;
	}

	/**
	 * CU 4
	 * @param verificacionDomiciliaria
	 */
	public void setVerificacionDomiciliaria(String verificacionDomiciliaria) {
		this.verificacionDomiciliaria = verificacionDomiciliaria;
	}

	public String getModificacionScoring() {
		return modificacionScoring;
	}

	public void setModificacionScoring(String modificacionScoring) {
		this.modificacionScoring = modificacionScoring;
	}

	public String getScoringAprobado() {
		return scoringAprobado;
	}

	/**
	 * CU 3
	 * @param scoringAprobado
	 */
	public void setScoringAprobado(String scoringAprobado) {
		this.scoringAprobado = scoringAprobado;
	}

	public String getDevueltoPor() {
		return devueltoPor;
	}

	/**
	 * CU 15
	 * @param devueltoPor
	 */
	public void setDevueltoPor(String devueltoPor) {
		this.devueltoPor = devueltoPor;
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public String getTipoOferta() {
		return tipoOferta;
	}

	public void setTipoOferta(String tipoOferta) {
		this.tipoOferta = tipoOferta;
	}

	public Double getLineaCredito() {
		return lineaCredito;
	}

	public void setLineaCredito(Double lineaCredito) {
		this.lineaCredito = lineaCredito;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public String getCodigoRVGL() {
		return codigoRVGL;
	}

	public void setCodigoRVGL(String codigoRVGL) {
		this.codigoRVGL = codigoRVGL;
	}

	public String getCodigoPreEvaluador() {
		return codigoPreEvaluador;
	}

	public void setCodigoPreEvaluador(String codigoPreEvaluador) {
		this.codigoPreEvaluador = codigoPreEvaluador;
	}

	public String getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(String idOficina) {
		this.idOficina = idOficina;
	}

	public String getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(String idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public String getCodigoEmpleadoResponsable() {
		return codigoEmpleadoResponsable;
	}

	public void setCodigoEmpleadoResponsable(String codigoEmpleadoResponsable) {
		this.codigoEmpleadoResponsable = codigoEmpleadoResponsable;
	}

	public String getCodigoUsuarioActual() {
		return codigoUsuarioActual;
	}

	public void setCodigoUsuarioActual(String codigoUsuarioActual) {
		this.codigoUsuarioActual = codigoUsuarioActual;
	}

	public String getIdPerfilUsuarioActual() {
		return idPerfilUsuarioActual;
	}

	public void setIdPerfilUsuarioActual(String idPerfilUsuarioActual) {
		this.idPerfilUsuarioActual = idPerfilUsuarioActual;
	}

	public String getNombreUsuarioAnterior() {
		return nombreUsuarioAnterior;
	}

	public void setNombreUsuarioAnterior(String nombreUsuarioAnterior) {
		this.nombreUsuarioAnterior = nombreUsuarioAnterior;
	}

	public String getPerfilUsuarioAnterior() {
		return perfilUsuarioAnterior;
	}

	public void setPerfilUsuarioAnterior(String perfilUsuarioAnterior) {
		this.perfilUsuarioAnterior = perfilUsuarioAnterior;
	}

	public String getCodigoUsuarioAnterior() {
		return codigoUsuarioAnterior;
	}

	public void setCodigoUsuarioAnterior(String codigoUsuarioAnterior) {
		this.codigoUsuarioAnterior = codigoUsuarioAnterior;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public String getEnvioContent() {
		return envioContent;
	}

	public void setEnvioContent(String envioContent) {
		this.envioContent = envioContent;
	}

	public String getIdPerfilUsuarioAnterior() {
		return idPerfilUsuarioAnterior;
	}

	public void setIdPerfilUsuarioAnterior(String idPerfilUsuarioAnterior) {
		this.idPerfilUsuarioAnterior = idPerfilUsuarioAnterior;
	}

	public String getFlagSubrogacion() {
		return flagSubrogacion;
	}

	public void setFlagSubrogacion(String flagSubrogacion) {
		this.flagSubrogacion = flagSubrogacion;
	}
	
	
}

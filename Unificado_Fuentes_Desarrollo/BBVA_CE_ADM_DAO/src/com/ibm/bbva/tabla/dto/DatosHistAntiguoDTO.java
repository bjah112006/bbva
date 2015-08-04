package com.ibm.bbva.tabla.dto;

import com.ibm.as.core.dto.AbstractDTO;

public class DatosHistAntiguoDTO  extends AbstractDTO{

	static final long serialVersionUID = 1243526157593L;
	
	private String origen;
	
	//EXPEDIENTE
	private String nroExpediente;
	private String fechaRegistroExp;
	private String flagActivoExp;
	private String fechaEnvioExp;
	private String fechaFinExp;
	private String fechaProgramadaExp;
	private String numeroTerminalExp;
	private String codClienteNatural; 
	private String codEmpleado; 
	private String codEstado;
	private String codProducto; 
	
	//cliente natural
	private String apellidoPatCliente;
	private String apellidoMatCliente;
	private String nombreCliente;
	private String fechaVenDoiCliente;
	private String ingNetoMensualCliente;
	private String numDoiCliente;
	private String pagoHabCliente;
	private String perExpPubCliente;
	private String codEstCivilCliente;
	private String codPersonaCliente;
	private String codTipoCliente;
	private String codTipoDoiCliente;
	private String codCategRentaCliente;
	private String estadoCivilCliente;
	private String persDescripCliente;
	private String persCodProductoCliente;
	private String codSegmentoCliente;
	private String codGrupoSegmentoCliente;
	private String segmentoCliente;
	private String tipoClienteDescrip;
	private String tipoDoiClienteDescrip;
	
	private String avalCliente;
	private String celularCliente;
	private String correoCliente;
	private String subrogCliente;
	private String codigoSegmentoCliente;
	private String catRenDescripCliente;
	
	
	//Conyugue del Cliente
	private String codConyugue;
	private String apellidoPatConyugue;
	private String apellidoMatConyugue;
	private String nombreConyugue;
	private String fechaVenDoiConyugue;
	private String ingNetoMensualConyugue;
	private String numDoiConyugue;
	private String pagoHabConyugue;
	private String perExpPubConyugue;
	private String codEstCivilConyugue;
	private String codPersonaConyugue;
	private String codSegmentoConyugue;
	private String codTipoConyugue;
	private String codTipoDoiConyugue;
	private String codCategRentaConyugue;
	private String estadoCivilConyugue;
	private String persDescripConyugue;
	private String persCodProductoConyugue;
	private String codGrupoSegmentoConyugue;
	private String segmentoConyugue;
	private String tipoConyugueDescrip;
	private String tipoDoiConyugueDescrip;
	
	private String avalConyugue;
	private String celularConyugue;
	private String correoConyugue;
	private String subrogConyugue;
	private String codigoSegmentoConyugue;
	private String catRenDescripConyugue;
	
	//Empleado
	private String flagActivoEmpleado;
	private String nombresCompletosEmpleado;
	private String codNivelEmpleado;
	private String codOficinaEmpleado;
	private String codPerfilEmleado;
	private String perfilEmpleado;
	private String codTipoCategEmpleado;
	private String nivEmpDescripEmpleado;
//	private String codOficinaEmpleado;
	private String oficinaEmpleado;
	private String flagAreaRiesgoOfiEmpleado;
	private String flagDesplezadaOfiEmpleado;
	private String flagEscaneoWebOfiEmpleado;
	private String montoTopeOfiEmpleado;
	private String tasaTransOfiEmpleado;
	private String flagActivoOfiEmpleado;
	private String codOfiPrincipalEmpleado;
	private String codTerritorioOfiEmpleado;
	private String tipoCategEmpleado;
	private String flagSuperiorTipoCategEmpleado;
	
	private String correoEmpleado;
	private String apePatEmpleado;
	private String apeMatEmpleado;
	private String nombresEmpleado;
	private String descripTerOfiEmpleado;
	private String ubicaTerOfiEmpleado;
	
	
	//Empleado Resp
	private String codEmpleadoResp;
	private String flagActivoEmpleadoResp;
	private String nombresCompletosEmpleadoResp;
	private String codNivelResp;
	private String codOficinaResp;
	private String codPerfilEmleadoResp;
	private String perfilEmpleadoResp;
	private String codTipoCategEmpleadoResp;
	private String nivEmpDescripEmpleadoResp;
	private String codOficinaEmpleadoResp;
	private String oficinaEmpleadoResp;
	private String flagAreaRiesgoOfiEmpleadoResp;
	private String flagDesplezadaOfiEmpleadoResp;
	private String flagEscaneoWebOfiEmpleadoResp;
	private String montoTopeOfiEmpleadoResp;
	private String tasaTransOfiEmpleadoResp;
	private String flagActivoOfiEmpleadoResp;
	private String codOfiPrincipalEmpleadoResp;
	private String codTerritorioOfiEmpleadoResp;
	private String tipoCategEmpleadoResp;
	private String flagSuperiorTipoCategEmpleadoResp;
	
	private String correoEmpleadoResp;
	private String apePatEmpleadoResp;
	private String apeMatEmpleadoResp;
	private String nombresEmpleadoResp;
	private String descripTerOfiEmpleadoResp;
	private String ubicaTerOfiEmpleadoResp;
	
	
	//Estado
	private String estadoCodigo;
	private String estadoDescrip;
	private String codTareaEstado;
	

	//Estado Tipo Prestamo Soles
	private String codEstadoTipresol;
	private String codEstadoSol;
	private String descripEstadoSol;
	private String codTareaEstadoSol;
	
	//Estado Envio Worckflow
	private String codEstadoEnWorckflow;
	private String codEstadoWkf;
	private String descripEstadoWkf;
	private String codTareaEstadoWkf;
	
	//Perfil
	private String codPerfil;
	private String descripPerfil;
	private String flagAdminPerfil;
	private String flagAsignacionPerfil;
	private String flagRegistraAyuPerfil;
	private String flagRegistraExpPerfil;
	private String listaCorreoJefesPerfil;
	private String flagPendientesPerfil;
	private String flagProcesoPerfil;
	private String flagMenuRegExpPerfil;
	private String flagMenuBandPendientesPerfil;
	private String flagMenuBusquedaPerfil;
	private String flagMenuBandHistoricaPerfil;
	private String flagMenuBandAsignacionPerfil;
	private String flagMenuBandMantenPerfil;
	private String flagMenuRepHistorialPerfil;
	private String flagMenuRepConsolidadoPerfil;
	private String flagMenuRepToePerfil;
	private String flagMenuHorarioPerfil;
	private String flagMenuDescargaLdapPerfil;
	
	//Motivo Devolución
	private String codMotivoDevol;
	private String codigoMotivoDevol;
	private String motivoDescripcion;
	private String flagOtrosMotivoDevol;
	private String flagActivoMotivoDevol;
	private String flagRechazoMotivoDevol;
	private String codTareaMotivoDevol;
	
	
	//Producto
	private String producto;
	
	
	//SubProducto
	private String codSubProducto;
	private String subProducto;
	private String codProdSubProducto;
	private String flagActivoSubProducto;
	private String tipMonedaSubProducto;
	
	//Tipo Buro
	private String codTipoBuro;
	private String codigoTipoBuro;
	private String tipoBuro;
	
	//Tipo envio
	private String codTipoEnvio;
	private String codigoTipoEnvio;
	private String tipoEnvio;
	
	//Tipo Moneda
	private String codTipoMoneda;
	private String codigoTipoMoneda;
	private String tipoMoneda;
	
	//Tipo Moneda Prob
	private String codTipoMonedaProb;
	private String codigoTipoMonedaProb;
	private String tipoMonedaProb;

	//Tipo Oferta
	private String codTipoOferta;
	private String tipoOferta;
	
	//Tipo Scoring
	private String codTipoScoring;
	private String tipoScoring;
	
	//Tarea
	private String codTarea;
	private String tarea;
	
	//Oficina
	private String codOficina;
	private String oficina;
	private String flagAreaRiesgoOficina;
	private String flagDesplazadaOficina;
	private String flagEscaneoWebOficina;
	private String montoTopeOficina;
	private String tasaTransfOficina;
	private String flagActivoOficina;
	private String codOfiPrincipalOficina;
	private String codTerritorioOficina;
	private String territorioOficina;
	private String ubicacionOficina;
	private String flagProvOficina;
	//private String flagActivoOficina;
	
	//Clasificacion Banco
	private String codClasifBanco;
	private String clasificacionBanco;
	private String codProdClasifBanco;
	
	//CLASIFICACION BANCO CONYUGUE
	private String codClasifBancoConyugue;
	private String clasificacionBancoConyugue;
	private String codProdClasifBancoConyugue;
	

	//Historial
	private String id;
	private String clasificacionSBS;
	private String codPreEval;
	private String comentarioAyuMem;
	private String comentarioDelegacion;
	private String comentarioRechazo;
	private String fechaRegistro;
	private String fechaEnvio;
	private String fechaFin;
	private String fechaProgramada;
	private String fechaT1;
	private String fechaT2;
	private String fechaT3;
	private String flagComentario;
	private String flagDelegacion;
	private String flagDevolucion;
	private String flagExcDelegacion;
	private String flagModifScore;
	private String flagRetraer;
	private String flagSolTasaEsp;
	private String lineaConsumo;
	private String lineaCredAprob;
	private String lineaCredSol;
	private String nroContrato;
	private String nroCuenta;
	private String numTerminal;
	private String tiempoCola;
	private String tiempoEjecucion;
	private String fechaT1R;
	private String fechaT2R;
	private String fechaT3R;
	private String tiempoColar;
	private String tiempoEjecucionR;
	private String tiempoColaRetraer;
	private String porcentajeEndeudamiento;
	private String riesgoCliente;
	private String sbsConyugue;
	private String tasaEsp;
	private String tipoResolucion;
	private String verifDom;
	private String verifLab;
	private String verifDPS;
	private String zonaPel;
	private String plazoSol;
	private String plazoSolAprob;
	private String tipoCambioExp;
	private String fechaTipoCambioExp;
	private String tiempoObfTC;
	private String tiempoObjTE;
	private String tiempoPreTC;
	private String tiempoPreTe;
	private String nomColumna;
	private String ans;
	
	//Para Antiguo
	private String codigoCentralCliente;
	private String flagProvinciaTerEmpleado;
	private String codGarantiaProducto;
	private String garantiaProducto;
	private String nombreTipoOferta;
	private String clasifSbsCony;
	private String nombreLagroEstadoExp;
	private String plazoSolicitadoExp;
	private String flagVerificacionLabExp;
	private String flagVerificacionDomExp;
	private String codigoPreevaluadorExp;
	private String modificarTasaExp;
	private String indicadorSubrogado;
	private String indicadorDesemCent;
	private String usuarioEmpleado;
	private String codigoPerfilEmpleadoResp;
	private String codigoTerritorioEmpleadoResp;
	private String codigoTerritorioEmpleado;
	private String codigoTipoOferta;
	private String codigoEmpleado;
	private String codigoPerfilEmpleado;
	private String flagProvinciaTerEmpleadoResp;
	private String codigoOfiEmpleadoResp;
	private String codigoRVGL;
	private String modificarScoring;
	private String codigoOficina;
	
	
	
	public String getCodigoOficina() {
		return codigoOficina;
	}
	public void setCodigoOficina(String codigoOficina) {
		this.codigoOficina = codigoOficina;
	}
	public String getModificarScoring() {
		return modificarScoring;
	}
	public void setModificarScoring(String modificarScoring) {
		this.modificarScoring = modificarScoring;
	}
	public String getCodigoRVGL() {
		return codigoRVGL;
	}
	public void setCodigoRVGL(String codigoRVGL) {
		this.codigoRVGL = codigoRVGL;
	}
	public String getCodigoOfiEmpleadoResp() {
		return codigoOfiEmpleadoResp;
	}
	public void setCodigoOfiEmpleadoResp(String codigoOfiEmpleadoResp) {
		this.codigoOfiEmpleadoResp = codigoOfiEmpleadoResp;
	}
	public String getFlagProvinciaTerEmpleadoResp() {
		return flagProvinciaTerEmpleadoResp;
	}
	public void setFlagProvinciaTerEmpleadoResp(String flagProvinciaTerEmpleadoResp) {
		this.flagProvinciaTerEmpleadoResp = flagProvinciaTerEmpleadoResp;
	}
	public String getCodigoTerritorioEmpleado() {
		return codigoTerritorioEmpleado;
	}
	public void setCodigoTerritorioEmpleado(String codigoTerritorioEmpleado) {
		this.codigoTerritorioEmpleado = codigoTerritorioEmpleado;
	}
	public String getCodigoPerfilEmpleado() {
		return codigoPerfilEmpleado;
	}
	public void setCodigoPerfilEmpleado(String codigoPerfilEmpleado) {
		this.codigoPerfilEmpleado = codigoPerfilEmpleado;
	}
	public String getCodigoTerritorioEmpleadoResp() {
		return codigoTerritorioEmpleadoResp;
	}
	public void setCodigoTerritorioEmpleadoResp(String codigoTerritorioEmpleadoResp) {
		this.codigoTerritorioEmpleadoResp = codigoTerritorioEmpleadoResp;
	}
	
	
	public String getCodigoPerfilEmpleadoResp() {
		return codigoPerfilEmpleadoResp;
	}
	public void setCodigoPerfilEmpleadoResp(String codigoPerfilEmpleadoResp) {
		this.codigoPerfilEmpleadoResp = codigoPerfilEmpleadoResp;
	}
	
	public String getCodigoEmpleado() {
		return codigoEmpleado;
	}
	public void setCodigoEmpleado(String codigoEmpleado) {
		this.codigoEmpleado = codigoEmpleado;
	}
	public String getCodigoPreevaluadorExp() {
		return codigoPreevaluadorExp;
	}
	public void setCodigoPreevaluadorExp(String codigoPreevaluadorExp) {
		this.codigoPreevaluadorExp = codigoPreevaluadorExp;
	}
	public String getCodigoTipoOferta() {
		return codigoTipoOferta;
	}
	public void setCodigoTipoOferta(String codigoTipoOferta) {
		this.codigoTipoOferta = codigoTipoOferta;
	}
	
	public String getUsuarioEmpleado() {
		return usuarioEmpleado;
	}
	public void setUsuarioEmpleado(String usuarioEmpleado) {
		this.usuarioEmpleado = usuarioEmpleado;
	}
	public String getCodigoCentralCliente() {
		return codigoCentralCliente;
	}
	public void setCodigoCentralCliente(String codigoCentralCliente) {
		this.codigoCentralCliente = codigoCentralCliente;
	}
	public String getFlagProvinciaTerEmpleado() {
		return flagProvinciaTerEmpleado;
	}
	public void setFlagProvinciaTerEmpleado(String flagProvinciaTerEmpleado) {
		this.flagProvinciaTerEmpleado = flagProvinciaTerEmpleado;
	}
	public String getCodGarantiaProducto() {
		return codGarantiaProducto;
	}
	public void setCodGarantiaProducto(String codGarantiaProducto) {
		this.codGarantiaProducto = codGarantiaProducto;
	}
	public String getGarantiaProducto() {
		return garantiaProducto;
	}
	public void setGarantiaProducto(String garantiaProducto) {
		this.garantiaProducto = garantiaProducto;
	}
	public String getNombreTipoOferta() {
		return nombreTipoOferta;
	}
	public void setNombreTipoOferta(String nombreTipoOferta) {
		this.nombreTipoOferta = nombreTipoOferta;
	}
	public String getClasifSbsCony() {
		return clasifSbsCony;
	}
	public void setClasifSbsCony(String clasifSbsCony) {
		this.clasifSbsCony = clasifSbsCony;
	}
	public String getNombreLagroEstadoExp() {
		return nombreLagroEstadoExp;
	}
	public void setNombreLagroEstadoExp(String nombreLagroEstadoExp) {
		this.nombreLagroEstadoExp = nombreLagroEstadoExp;
	}
	public String getPlazoSolicitadoExp() {
		return plazoSolicitadoExp;
	}
	public void setPlazoSolicitadoExp(String plazoSolicitadoExp) {
		this.plazoSolicitadoExp = plazoSolicitadoExp;
	}
	public String getFlagVerificacionLabExp() {
		return flagVerificacionLabExp;
	}
	public void setFlagVerificacionLabExp(String flagVerificacionLabExp) {
		this.flagVerificacionLabExp = flagVerificacionLabExp;
	}
	public String getFlagVerificacionDomExp() {
		return flagVerificacionDomExp;
	}
	public void setFlagVerificacionDomExp(String flagVerificacionDomExp) {
		this.flagVerificacionDomExp = flagVerificacionDomExp;
	}
	
	public String getModificarTasaExp() {
		return modificarTasaExp;
	}
	public void setModificarTasaExp(String modificarTasaExp) {
		this.modificarTasaExp = modificarTasaExp;
	}
	public String getIndicadorSubrogado() {
		return indicadorSubrogado;
	}
	public void setIndicadorSubrogado(String indicadorSubrogado) {
		this.indicadorSubrogado = indicadorSubrogado;
	}
	public String getIndicadorDesemCent() {
		return indicadorDesemCent;
	}
	public void setIndicadorDesemCent(String indicadorDesemCent) {
		this.indicadorDesemCent = indicadorDesemCent;
	}
	public String getPerfilEmpleado() {
		return perfilEmpleado;
	}
	public void setPerfilEmpleado(String perfilEmpleado) {
		this.perfilEmpleado = perfilEmpleado;
	}
	public String getPerfilEmpleadoResp() {
		return perfilEmpleadoResp;
	}
	public void setPerfilEmpleadoResp(String perfilEmpleadoResp) {
		this.perfilEmpleadoResp = perfilEmpleadoResp;
	}
	
	public String getNroExpediente() {
		return nroExpediente;
	}
	public void setNroExpediente(String nroExpediente) {
		this.nroExpediente = nroExpediente;
	}
	public String getFechaRegistroExp() {
		return fechaRegistroExp;
	}
	public void setFechaRegistroExp(String fechaRegistroExp) {
		this.fechaRegistroExp = fechaRegistroExp;
	}
	public String getFlagActivoExp() {
		return flagActivoExp;
	}
	public void setFlagActivoExp(String flagActivoExp) {
		this.flagActivoExp = flagActivoExp;
	}
	public String getFechaEnvioExp() {
		return fechaEnvioExp;
	}
	public void setFechaEnvioExp(String fechaEnvioExp) {
		this.fechaEnvioExp = fechaEnvioExp;
	}
	public String getFechaFinExp() {
		return fechaFinExp;
	}
	public void setFechaFinExp(String fechaFinExp) {
		this.fechaFinExp = fechaFinExp;
	}
	public String getFechaProgramadaExp() {
		return fechaProgramadaExp;
	}
	public void setFechaProgramadaExp(String fechaProgramadaExp) {
		this.fechaProgramadaExp = fechaProgramadaExp;
	}
	public String getNumeroTerminalExp() {
		return numeroTerminalExp;
	}
	public void setNumeroTerminalExp(String numeroTerminalExp) {
		this.numeroTerminalExp = numeroTerminalExp;
	}
	public String getCodClienteNatural() {
		return codClienteNatural;
	}
	public void setCodClienteNatural(String codClienteNatural) {
		this.codClienteNatural = codClienteNatural;
	}
	public String getCodEmpleado() {
		return codEmpleado;
	}
	public void setCodEmpleado(String codEmpleado) {
		this.codEmpleado = codEmpleado;
	}
	public String getCodEstado() {
		return codEstado;
	}
	public void setCodEstado(String codEstado) {
		this.codEstado = codEstado;
	}
	public String getCodProducto() {
		return codProducto;
	}
	public void setCodProducto(String codProducto) {
		this.codProducto = codProducto;
	}
	public String getApellidoPatCliente() {
		return apellidoPatCliente;
	}
	public void setApellidoPatCliente(String apellidoPatCliente) {
		this.apellidoPatCliente = apellidoPatCliente;
	}
	public String getApellidoMatCliente() {
		return apellidoMatCliente;
	}
	public void setApellidoMatCliente(String apellidoMatCliente) {
		this.apellidoMatCliente = apellidoMatCliente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getFechaVenDoiCliente() {
		return fechaVenDoiCliente;
	}
	public void setFechaVenDoiCliente(String fechaVenDoiCliente) {
		this.fechaVenDoiCliente = fechaVenDoiCliente;
	}
	public String getIngNetoMensualCliente() {
		return ingNetoMensualCliente;
	}
	public void setIngNetoMensualCliente(String ingNetoMensualCliente) {
		this.ingNetoMensualCliente = ingNetoMensualCliente;
	}
	public String getNumDoiCliente() {
		return numDoiCliente;
	}
	public void setNumDoiCliente(String numDoiCliente) {
		this.numDoiCliente = numDoiCliente;
	}
	public String getPagoHabCliente() {
		return pagoHabCliente;
	}
	public void setPagoHabCliente(String pagoHabCliente) {
		this.pagoHabCliente = pagoHabCliente;
	}
	public String getPerExpPubCliente() {
		return perExpPubCliente;
	}
	public void setPerExpPubCliente(String perExpPubCliente) {
		this.perExpPubCliente = perExpPubCliente;
	}
	public String getCodEstCivilCliente() {
		return codEstCivilCliente;
	}
	public void setCodEstCivilCliente(String codEstCivilCliente) {
		this.codEstCivilCliente = codEstCivilCliente;
	}
	public String getCodPersonaCliente() {
		return codPersonaCliente;
	}
	public void setCodPersonaCliente(String codPersonaCliente) {
		this.codPersonaCliente = codPersonaCliente;
	}
	public String getCodSegmentoCliente() {
		return codSegmentoCliente;
	}
	public void setCodSegmentoCliente(String codSegmentoCliente) {
		this.codSegmentoCliente = codSegmentoCliente;
	}
	public String getCodTipoCliente() {
		return codTipoCliente;
	}
	public void setCodTipoCliente(String codTipoCliente) {
		this.codTipoCliente = codTipoCliente;
	}
	public String getCodTipoDoiCliente() {
		return codTipoDoiCliente;
	}
	public void setCodTipoDoiCliente(String codTipoDoiCliente) {
		this.codTipoDoiCliente = codTipoDoiCliente;
	}
	public String getCodCategRentaCliente() {
		return codCategRentaCliente;
	}
	public void setCodCategRentaCliente(String codCategRentaCliente) {
		this.codCategRentaCliente = codCategRentaCliente;
	}
	public String getEstadoCivilCliente() {
		return estadoCivilCliente;
	}
	public void setEstadoCivilCliente(String estadoCivilCliente) {
		this.estadoCivilCliente = estadoCivilCliente;
	}
	public String getPersDescripCliente() {
		return persDescripCliente;
	}
	public void setPersDescripCliente(String persDescripCliente) {
		this.persDescripCliente = persDescripCliente;
	}
	public String getPersCodProductoCliente() {
		return persCodProductoCliente;
	}
	public void setPersCodProductoCliente(String persCodProductoCliente) {
		this.persCodProductoCliente = persCodProductoCliente;
	}
	public String getCodGrupoSegmentoCliente() {
		return codGrupoSegmentoCliente;
	}
	public void setCodGrupoSegmentoCliente(String codGrupoSegmentoCliente) {
		this.codGrupoSegmentoCliente = codGrupoSegmentoCliente;
	}
	public String getSegmentoCliente() {
		return segmentoCliente;
	}
	public void setSegmentoCliente(String segmentoCliente) {
		this.segmentoCliente = segmentoCliente;
	}
	public String getTipoClienteDescrip() {
		return tipoClienteDescrip;
	}
	public void setTipoClienteDescrip(String tipoClienteDescrip) {
		this.tipoClienteDescrip = tipoClienteDescrip;
	}
	public String getTipoDoiClienteDescrip() {
		return tipoDoiClienteDescrip;
	}
	public void setTipoDoiClienteDescrip(String tipoDoiClienteDescrip) {
		this.tipoDoiClienteDescrip = tipoDoiClienteDescrip;
	}
	public String getCodConyugue() {
		return codConyugue;
	}
	public void setCodConyugue(String codConyugue) {
		this.codConyugue = codConyugue;
	}
	public String getApellidoPatConyugue() {
		return apellidoPatConyugue;
	}
	public void setApellidoPatConyugue(String apellidoPatConyugue) {
		this.apellidoPatConyugue = apellidoPatConyugue;
	}
	public String getApellidoMatConyugue() {
		return apellidoMatConyugue;
	}
	public void setApellidoMatConyugue(String apellidoMatConyugue) {
		this.apellidoMatConyugue = apellidoMatConyugue;
	}
	public String getNombreConyugue() {
		return nombreConyugue;
	}
	public void setNombreConyugue(String nombreConyugue) {
		this.nombreConyugue = nombreConyugue;
	}
	public String getFechaVenDoiConyugue() {
		return fechaVenDoiConyugue;
	}
	public void setFechaVenDoiConyugue(String fechaVenDoiConyugue) {
		this.fechaVenDoiConyugue = fechaVenDoiConyugue;
	}
	public String getIngNetoMensualConyugue() {
		return ingNetoMensualConyugue;
	}
	public void setIngNetoMensualConyugue(String ingNetoMensualConyugue) {
		this.ingNetoMensualConyugue = ingNetoMensualConyugue;
	}
	public String getNumDoiConyugue() {
		return numDoiConyugue;
	}
	public void setNumDoiConyugue(String numDoiConyugue) {
		this.numDoiConyugue = numDoiConyugue;
	}
	public String getPagoHabConyugue() {
		return pagoHabConyugue;
	}
	public void setPagoHabConyugue(String pagoHabConyugue) {
		this.pagoHabConyugue = pagoHabConyugue;
	}
	public String getPerExpPubConyugue() {
		return perExpPubConyugue;
	}
	public void setPerExpPubConyugue(String perExpPubConyugue) {
		this.perExpPubConyugue = perExpPubConyugue;
	}
	public String getCodEstCivilConyugue() {
		return codEstCivilConyugue;
	}
	public void setCodEstCivilConyugue(String codEstCivilConyugue) {
		this.codEstCivilConyugue = codEstCivilConyugue;
	}
	public String getCodPersonaConyugue() {
		return codPersonaConyugue;
	}
	public void setCodPersonaConyugue(String codPersonaConyugue) {
		this.codPersonaConyugue = codPersonaConyugue;
	}
	public String getCodSegmentoConyugue() {
		return codSegmentoConyugue;
	}
	public void setCodSegmentoConyugue(String codSegmentoConyugue) {
		this.codSegmentoConyugue = codSegmentoConyugue;
	}
	public String getCodTipoConyugue() {
		return codTipoConyugue;
	}
	public void setCodTipoConyugue(String codTipoConyugue) {
		this.codTipoConyugue = codTipoConyugue;
	}
	public String getCodTipoDoiConyugue() {
		return codTipoDoiConyugue;
	}
	public void setCodTipoDoiConyugue(String codTipoDoiConyugue) {
		this.codTipoDoiConyugue = codTipoDoiConyugue;
	}
	public String getCodCategRentaConyugue() {
		return codCategRentaConyugue;
	}
	public void setCodCategRentaConyugue(String codCategRentaConyugue) {
		this.codCategRentaConyugue = codCategRentaConyugue;
	}
	public String getEstadoCivilConyugue() {
		return estadoCivilConyugue;
	}
	public void setEstadoCivilConyugue(String estadoCivilConyugue) {
		this.estadoCivilConyugue = estadoCivilConyugue;
	}
	public String getPersDescripConyugue() {
		return persDescripConyugue;
	}
	public void setPersDescripConyugue(String persDescripConyugue) {
		this.persDescripConyugue = persDescripConyugue;
	}
	public String getPersCodProductoConyugue() {
		return persCodProductoConyugue;
	}
	public void setPersCodProductoConyugue(String persCodProductoConyugue) {
		this.persCodProductoConyugue = persCodProductoConyugue;
	}
	public String getCodGrupoSegmentoConyugue() {
		return codGrupoSegmentoConyugue;
	}
	public void setCodGrupoSegmentoConyugue(String codGrupoSegmentoConyugue) {
		this.codGrupoSegmentoConyugue = codGrupoSegmentoConyugue;
	}
	public String getSegmentoConyugue() {
		return segmentoConyugue;
	}
	public void setSegmentoConyugue(String segmentoConyugue) {
		this.segmentoConyugue = segmentoConyugue;
	}
	public String getTipoConyugueDescrip() {
		return tipoConyugueDescrip;
	}
	public void setTipoConyugueDescrip(String tipoConyugueDescrip) {
		this.tipoConyugueDescrip = tipoConyugueDescrip;
	}
	public String getTipoDoiConyugueDescrip() {
		return tipoDoiConyugueDescrip;
	}
	public void setTipoDoiConyugueDescrip(String tipoDoiConyugueDescrip) {
		this.tipoDoiConyugueDescrip = tipoDoiConyugueDescrip;
	}
	public String getFlagActivoEmpleado() {
		return flagActivoEmpleado;
	}
	public void setFlagActivoEmpleado(String flagActivoEmpleado) {
		this.flagActivoEmpleado = flagActivoEmpleado;
	}
	public String getNombresCompletosEmpleado() {
		return nombresCompletosEmpleado;
	}
	public void setNombresCompletosEmpleado(String nombresCompletosEmpleado) {
		this.nombresCompletosEmpleado = nombresCompletosEmpleado;
	}
	public String getCodNivelEmpleado() {
		return codNivelEmpleado;
	}
	public void setCodNivelEmpleado(String codNivelEmpleado) {
		this.codNivelEmpleado = codNivelEmpleado;
	}
	public String getCodOficinaEmpleado() {
		return codOficinaEmpleado;
	}
	public void setCodOficinaEmpleado(String codOficinaEmpleado) {
		this.codOficinaEmpleado = codOficinaEmpleado;
	}
	public String getCodPerfilEmleado() {
		return codPerfilEmleado;
	}
	public void setCodPerfilEmleado(String codPerfilEmleado) {
		this.codPerfilEmleado = codPerfilEmleado;
	}
	public String getCodTipoCategEmpleado() {
		return codTipoCategEmpleado;
	}
	public void setCodTipoCategEmpleado(String codTipoCategEmpleado) {
		this.codTipoCategEmpleado = codTipoCategEmpleado;
	}
	public String getNivEmpDescripEmpleado() {
		return nivEmpDescripEmpleado;
	}
	public void setNivEmpDescripEmpleado(String nivEmpDescripEmpleado) {
		this.nivEmpDescripEmpleado = nivEmpDescripEmpleado;
	}
	public String getOficinaEmpleado() {
		return oficinaEmpleado;
	}
	public void setOficinaEmpleado(String oficinaEmpleado) {
		this.oficinaEmpleado = oficinaEmpleado;
	}
	public String getFlagAreaRiesgoOfiEmpleado() {
		return flagAreaRiesgoOfiEmpleado;
	}
	public void setFlagAreaRiesgoOfiEmpleado(String flagAreaRiesgoOfiEmpleado) {
		this.flagAreaRiesgoOfiEmpleado = flagAreaRiesgoOfiEmpleado;
	}
	public String getFlagDesplezadaOfiEmpleado() {
		return flagDesplezadaOfiEmpleado;
	}
	public void setFlagDesplezadaOfiEmpleado(String flagDesplezadaOfiEmpleado) {
		this.flagDesplezadaOfiEmpleado = flagDesplezadaOfiEmpleado;
	}
	public String getFlagEscaneoWebOfiEmpleado() {
		return flagEscaneoWebOfiEmpleado;
	}
	public void setFlagEscaneoWebOfiEmpleado(String flagEscaneoWebOfiEmpleado) {
		this.flagEscaneoWebOfiEmpleado = flagEscaneoWebOfiEmpleado;
	}
	public String getMontoTopeOfiEmpleado() {
		return montoTopeOfiEmpleado;
	}
	public void setMontoTopeOfiEmpleado(String montoTopeOfiEmpleado) {
		this.montoTopeOfiEmpleado = montoTopeOfiEmpleado;
	}
	public String getTasaTransOfiEmpleado() {
		return tasaTransOfiEmpleado;
	}
	public void setTasaTransOfiEmpleado(String tasaTransOfiEmpleado) {
		this.tasaTransOfiEmpleado = tasaTransOfiEmpleado;
	}
	public String getFlagActivoOfiEmpleado() {
		return flagActivoOfiEmpleado;
	}
	public void setFlagActivoOfiEmpleado(String flagActivoOfiEmpleado) {
		this.flagActivoOfiEmpleado = flagActivoOfiEmpleado;
	}
	public String getCodOfiPrincipalEmpleado() {
		return codOfiPrincipalEmpleado;
	}
	public void setCodOfiPrincipalEmpleado(String codOfiPrincipalEmpleado) {
		this.codOfiPrincipalEmpleado = codOfiPrincipalEmpleado;
	}
	public String getCodTerritorioOfiEmpleado() {
		return codTerritorioOfiEmpleado;
	}
	public void setCodTerritorioOfiEmpleado(String codTerritorioOfiEmpleado) {
		this.codTerritorioOfiEmpleado = codTerritorioOfiEmpleado;
	}
	public String getTipoCategEmpleado() {
		return tipoCategEmpleado;
	}
	public void setTipoCategEmpleado(String tipoCategEmpleado) {
		this.tipoCategEmpleado = tipoCategEmpleado;
	}
	public String getFlagSuperiorTipoCategEmpleado() {
		return flagSuperiorTipoCategEmpleado;
	}
	public void setFlagSuperiorTipoCategEmpleado(
			String flagSuperiorTipoCategEmpleado) {
		this.flagSuperiorTipoCategEmpleado = flagSuperiorTipoCategEmpleado;
	}
	public String getCodEmpleadoResp() {
		return codEmpleadoResp;
	}
	public void setCodEmpleadoResp(String codEmpleadoResp) {
		this.codEmpleadoResp = codEmpleadoResp;
	}
	public String getFlagActivoEmpleadoResp() {
		return flagActivoEmpleadoResp;
	}
	public void setFlagActivoEmpleadoResp(String flagActivoEmpleadoResp) {
		this.flagActivoEmpleadoResp = flagActivoEmpleadoResp;
	}
	public String getNombresCompletosEmpleadoResp() {
		return nombresCompletosEmpleadoResp;
	}
	public void setNombresCompletosEmpleadoResp(String nombresCompletosEmpleadoResp) {
		this.nombresCompletosEmpleadoResp = nombresCompletosEmpleadoResp;
	}
	public String getCodNivelResp() {
		return codNivelResp;
	}
	public void setCodNivelResp(String codNivelResp) {
		this.codNivelResp = codNivelResp;
	}
	public String getCodOficinaResp() {
		return codOficinaResp;
	}
	public void setCodOficinaResp(String codOficinaResp) {
		this.codOficinaResp = codOficinaResp;
	}
	public String getCodPerfilEmleadoResp() {
		return codPerfilEmleadoResp;
	}
	public void setCodPerfilEmleadoResp(String codPerfilEmleadoResp) {
		this.codPerfilEmleadoResp = codPerfilEmleadoResp;
	}
	public String getCodTipoCategEmpleadoResp() {
		return codTipoCategEmpleadoResp;
	}
	public void setCodTipoCategEmpleadoResp(String codTipoCategEmpleadoResp) {
		this.codTipoCategEmpleadoResp = codTipoCategEmpleadoResp;
	}
	public String getNivEmpDescripEmpleadoResp() {
		return nivEmpDescripEmpleadoResp;
	}
	public void setNivEmpDescripEmpleadoResp(String nivEmpDescripEmpleadoResp) {
		this.nivEmpDescripEmpleadoResp = nivEmpDescripEmpleadoResp;
	}
	public String getCodOficinaEmpleadoResp() {
		return codOficinaEmpleadoResp;
	}
	public void setCodOficinaEmpleadoResp(String codOficinaEmpleadoResp) {
		this.codOficinaEmpleadoResp = codOficinaEmpleadoResp;
	}
	public String getOficinaEmpleadoResp() {
		return oficinaEmpleadoResp;
	}
	public void setOficinaEmpleadoResp(String oficinaEmpleadoResp) {
		this.oficinaEmpleadoResp = oficinaEmpleadoResp;
	}
	public String getFlagAreaRiesgoOfiEmpleadoResp() {
		return flagAreaRiesgoOfiEmpleadoResp;
	}
	public void setFlagAreaRiesgoOfiEmpleadoResp(
			String flagAreaRiesgoOfiEmpleadoResp) {
		this.flagAreaRiesgoOfiEmpleadoResp = flagAreaRiesgoOfiEmpleadoResp;
	}
	public String getFlagDesplezadaOfiEmpleadoResp() {
		return flagDesplezadaOfiEmpleadoResp;
	}
	public void setFlagDesplezadaOfiEmpleadoResp(
			String flagDesplezadaOfiEmpleadoResp) {
		this.flagDesplezadaOfiEmpleadoResp = flagDesplezadaOfiEmpleadoResp;
	}
	public String getFlagEscaneoWebOfiEmpleadoResp() {
		return flagEscaneoWebOfiEmpleadoResp;
	}
	public void setFlagEscaneoWebOfiEmpleadoResp(
			String flagEscaneoWebOfiEmpleadoResp) {
		this.flagEscaneoWebOfiEmpleadoResp = flagEscaneoWebOfiEmpleadoResp;
	}
	public String getMontoTopeOfiEmpleadoResp() {
		return montoTopeOfiEmpleadoResp;
	}
	public void setMontoTopeOfiEmpleadoResp(String montoTopeOfiEmpleadoResp) {
		this.montoTopeOfiEmpleadoResp = montoTopeOfiEmpleadoResp;
	}
	public String getTasaTransOfiEmpleadoResp() {
		return tasaTransOfiEmpleadoResp;
	}
	public void setTasaTransOfiEmpleadoResp(String tasaTransOfiEmpleadoResp) {
		this.tasaTransOfiEmpleadoResp = tasaTransOfiEmpleadoResp;
	}
	public String getFlagActivoOfiEmpleadoResp() {
		return flagActivoOfiEmpleadoResp;
	}
	public void setFlagActivoOfiEmpleadoResp(String flagActivoOfiEmpleadoResp) {
		this.flagActivoOfiEmpleadoResp = flagActivoOfiEmpleadoResp;
	}
	public String getCodOfiPrincipalEmpleadoResp() {
		return codOfiPrincipalEmpleadoResp;
	}
	public void setCodOfiPrincipalEmpleadoResp(String codOfiPrincipalEmpleadoResp) {
		this.codOfiPrincipalEmpleadoResp = codOfiPrincipalEmpleadoResp;
	}
	public String getCodTerritorioOfiEmpleadoResp() {
		return codTerritorioOfiEmpleadoResp;
	}
	public void setCodTerritorioOfiEmpleadoResp(String codTerritorioOfiEmpleadoResp) {
		this.codTerritorioOfiEmpleadoResp = codTerritorioOfiEmpleadoResp;
	}
	public String getTipoCategEmpleadoResp() {
		return tipoCategEmpleadoResp;
	}
	public void setTipoCategEmpleadoResp(String tipoCategEmpleadoResp) {
		this.tipoCategEmpleadoResp = tipoCategEmpleadoResp;
	}
	public String getFlagSuperiorTipoCategEmpleadoResp() {
		return flagSuperiorTipoCategEmpleadoResp;
	}
	public void setFlagSuperiorTipoCategEmpleadoResp(
			String flagSuperiorTipoCategEmpleadoResp) {
		this.flagSuperiorTipoCategEmpleadoResp = flagSuperiorTipoCategEmpleadoResp;
	}
	public String getEstadoCodigo() {
		return estadoCodigo;
	}
	public void setEstadoCodigo(String estadoCodigo) {
		this.estadoCodigo = estadoCodigo;
	}
	public String getEstadoDescrip() {
		return estadoDescrip;
	}
	public void setEstadoDescrip(String estadoDescrip) {
		this.estadoDescrip = estadoDescrip;
	}
	public String getCodTareaEstado() {
		return codTareaEstado;
	}
	public void setCodTareaEstado(String codTareaEstado) {
		this.codTareaEstado = codTareaEstado;
	}
	public String getCodEstadoTipresol() {
		return codEstadoTipresol;
	}
	public void setCodEstadoTipresol(String codEstadoTipresol) {
		this.codEstadoTipresol = codEstadoTipresol;
	}
	public String getCodEstadoSol() {
		return codEstadoSol;
	}
	public void setCodEstadoSol(String codEstadoSol) {
		this.codEstadoSol = codEstadoSol;
	}
	public String getDescripEstadoSol() {
		return descripEstadoSol;
	}
	public void setDescripEstadoSol(String descripEstadoSol) {
		this.descripEstadoSol = descripEstadoSol;
	}
	public String getCodTareaEstadoSol() {
		return codTareaEstadoSol;
	}
	public void setCodTareaEstadoSol(String codTareaEstadoSol) {
		this.codTareaEstadoSol = codTareaEstadoSol;
	}
	public String getCodEstadoEnWorckflow() {
		return codEstadoEnWorckflow;
	}
	public void setCodEstadoEnWorckflow(String codEstadoEnWorckflow) {
		this.codEstadoEnWorckflow = codEstadoEnWorckflow;
	}
	public String getCodEstadoWkf() {
		return codEstadoWkf;
	}
	public void setCodEstadoWkf(String codEstadoWkf) {
		this.codEstadoWkf = codEstadoWkf;
	}
	public String getDescripEstadoWkf() {
		return descripEstadoWkf;
	}
	public void setDescripEstadoWkf(String descripEstadoWkf) {
		this.descripEstadoWkf = descripEstadoWkf;
	}
	public String getCodTareaEstadoWkf() {
		return codTareaEstadoWkf;
	}
	public void setCodTareaEstadoWkf(String codTareaEstadoWkf) {
		this.codTareaEstadoWkf = codTareaEstadoWkf;
	}
	public String getCodPerfil() {
		return codPerfil;
	}
	public void setCodPerfil(String codPerfil) {
		this.codPerfil = codPerfil;
	}
	public String getFlagAdminPerfil() {
		return flagAdminPerfil;
	}
	public void setFlagAdminPerfil(String flagAdminPerfil) {
		this.flagAdminPerfil = flagAdminPerfil;
	}
	public String getFlagAsignacionPerfil() {
		return flagAsignacionPerfil;
	}
	public void setFlagAsignacionPerfil(String flagAsignacionPerfil) {
		this.flagAsignacionPerfil = flagAsignacionPerfil;
	}
	public String getFlagRegistraAyuPerfil() {
		return flagRegistraAyuPerfil;
	}
	public void setFlagRegistraAyuPerfil(String flagRegistraAyuPerfil) {
		this.flagRegistraAyuPerfil = flagRegistraAyuPerfil;
	}
	public String getFlagRegistraExpPerfil() {
		return flagRegistraExpPerfil;
	}
	public void setFlagRegistraExpPerfil(String flagRegistraExpPerfil) {
		this.flagRegistraExpPerfil = flagRegistraExpPerfil;
	}
	public String getListaCorreoJefesPerfil() {
		return listaCorreoJefesPerfil;
	}
	public void setListaCorreoJefesPerfil(String listaCorreoJefesPerfil) {
		this.listaCorreoJefesPerfil = listaCorreoJefesPerfil;
	}
	public String getFlagPendientesPerfil() {
		return flagPendientesPerfil;
	}
	public void setFlagPendientesPerfil(String flagPendientesPerfil) {
		this.flagPendientesPerfil = flagPendientesPerfil;
	}
	public String getFlagProcesoPerfil() {
		return flagProcesoPerfil;
	}
	public void setFlagProcesoPerfil(String flagProcesoPerfil) {
		this.flagProcesoPerfil = flagProcesoPerfil;
	}
	public String getFlagMenuRegExpPerfil() {
		return flagMenuRegExpPerfil;
	}
	public void setFlagMenuRegExpPerfil(String flagMenuRegExpPerfil) {
		this.flagMenuRegExpPerfil = flagMenuRegExpPerfil;
	}
	public String getFlagMenuBandPendientesPerfil() {
		return flagMenuBandPendientesPerfil;
	}
	public void setFlagMenuBandPendientesPerfil(String flagMenuBandPendientesPerfil) {
		this.flagMenuBandPendientesPerfil = flagMenuBandPendientesPerfil;
	}
	public String getFlagMenuBusquedaPerfil() {
		return flagMenuBusquedaPerfil;
	}
	public void setFlagMenuBusquedaPerfil(String flagMenuBusquedaPerfil) {
		this.flagMenuBusquedaPerfil = flagMenuBusquedaPerfil;
	}
	public String getFlagMenuBandHistoricaPerfil() {
		return flagMenuBandHistoricaPerfil;
	}
	public void setFlagMenuBandHistoricaPerfil(String flagMenuBandHistoricaPerfil) {
		this.flagMenuBandHistoricaPerfil = flagMenuBandHistoricaPerfil;
	}
	public String getFlagMenuBandAsignacionPerfil() {
		return flagMenuBandAsignacionPerfil;
	}
	public void setFlagMenuBandAsignacionPerfil(String flagMenuBandAsignacionPerfil) {
		this.flagMenuBandAsignacionPerfil = flagMenuBandAsignacionPerfil;
	}
	public String getFlagMenuBandMantenPerfil() {
		return flagMenuBandMantenPerfil;
	}
	public void setFlagMenuBandMantenPerfil(String flagMenuBandMantenPerfil) {
		this.flagMenuBandMantenPerfil = flagMenuBandMantenPerfil;
	}
	public String getFlagMenuRepHistorialPerfil() {
		return flagMenuRepHistorialPerfil;
	}
	public void setFlagMenuRepHistorialPerfil(String flagMenuRepHistorialPerfil) {
		this.flagMenuRepHistorialPerfil = flagMenuRepHistorialPerfil;
	}
	public String getFlagMenuRepConsolidadoPerfil() {
		return flagMenuRepConsolidadoPerfil;
	}
	public void setFlagMenuRepConsolidadoPerfil(String flagMenuRepConsolidadoPerfil) {
		this.flagMenuRepConsolidadoPerfil = flagMenuRepConsolidadoPerfil;
	}
	public String getFlagMenuRepToePerfil() {
		return flagMenuRepToePerfil;
	}
	public void setFlagMenuRepToePerfil(String flagMenuRepToePerfil) {
		this.flagMenuRepToePerfil = flagMenuRepToePerfil;
	}
	public String getFlagMenuHorarioPerfil() {
		return flagMenuHorarioPerfil;
	}
	public void setFlagMenuHorarioPerfil(String flagMenuHorarioPerfil) {
		this.flagMenuHorarioPerfil = flagMenuHorarioPerfil;
	}
	public String getFlagMenuDescargaLdapPerfil() {
		return flagMenuDescargaLdapPerfil;
	}
	public void setFlagMenuDescargaLdapPerfil(String flagMenuDescargaLdapPerfil) {
		this.flagMenuDescargaLdapPerfil = flagMenuDescargaLdapPerfil;
	}
	public String getCodMotivoDevol() {
		return codMotivoDevol;
	}
	public void setCodMotivoDevol(String codMotivoDevol) {
		this.codMotivoDevol = codMotivoDevol;
	}
	public String getCodigoMotivoDevol() {
		return codigoMotivoDevol;
	}
	public void setCodigoMotivoDevol(String codigoMotivoDevol) {
		this.codigoMotivoDevol = codigoMotivoDevol;
	}
	public String getMotivoDescripcion() {
		return motivoDescripcion;
	}
	public void setMotivoDescripcion(String motivoDescripcion) {
		this.motivoDescripcion = motivoDescripcion;
	}
	public String getFlagOtrosMotivoDevol() {
		return flagOtrosMotivoDevol;
	}
	public void setFlagOtrosMotivoDevol(String flagOtrosMotivoDevol) {
		this.flagOtrosMotivoDevol = flagOtrosMotivoDevol;
	}
	public String getFlagActivoMotivoDevol() {
		return flagActivoMotivoDevol;
	}
	public void setFlagActivoMotivoDevol(String flagActivoMotivoDevol) {
		this.flagActivoMotivoDevol = flagActivoMotivoDevol;
	}
	public String getFlagRechazoMotivoDevol() {
		return flagRechazoMotivoDevol;
	}
	public void setFlagRechazoMotivoDevol(String flagRechazoMotivoDevol) {
		this.flagRechazoMotivoDevol = flagRechazoMotivoDevol;
	}
	public String getCodTareaMotivoDevol() {
		return codTareaMotivoDevol;
	}
	public void setCodTareaMotivoDevol(String codTareaMotivoDevol) {
		this.codTareaMotivoDevol = codTareaMotivoDevol;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public String getCodSubProducto() {
		return codSubProducto;
	}
	public void setCodSubProducto(String codSubProducto) {
		this.codSubProducto = codSubProducto;
	}
	public String getSubProducto() {
		return subProducto;
	}
	public void setSubProducto(String subProducto) {
		this.subProducto = subProducto;
	}
	public String getCodProdSubProducto() {
		return codProdSubProducto;
	}
	public void setCodProdSubProducto(String codProdSubProducto) {
		this.codProdSubProducto = codProdSubProducto;
	}
	public String getFlagActivoSubProducto() {
		return flagActivoSubProducto;
	}
	public void setFlagActivoSubProducto(String flagActivoSubProducto) {
		this.flagActivoSubProducto = flagActivoSubProducto;
	}
	public String getTipMonedaSubProducto() {
		return tipMonedaSubProducto;
	}
	public void setTipMonedaSubProducto(String tipMonedaSubProducto) {
		this.tipMonedaSubProducto = tipMonedaSubProducto;
	}
	public String getCodTipoBuro() {
		return codTipoBuro;
	}
	public void setCodTipoBuro(String codTipoBuro) {
		this.codTipoBuro = codTipoBuro;
	}
	public String getCodigoTipoBuro() {
		return codigoTipoBuro;
	}
	public void setCodigoTipoBuro(String codigoTipoBuro) {
		this.codigoTipoBuro = codigoTipoBuro;
	}
	public String getTipoBuro() {
		return tipoBuro;
	}
	public void setTipoBuro(String tipoBuro) {
		this.tipoBuro = tipoBuro;
	}
	public String getCodTipoEnvio() {
		return codTipoEnvio;
	}
	public void setCodTipoEnvio(String codTipoEnvio) {
		this.codTipoEnvio = codTipoEnvio;
	}
	public String getCodigoTipoEnvio() {
		return codigoTipoEnvio;
	}
	public void setCodigoTipoEnvio(String codigoTipoEnvio) {
		this.codigoTipoEnvio = codigoTipoEnvio;
	}
	public String getTipoEnvio() {
		return tipoEnvio;
	}
	public void setTipoEnvio(String tipoEnvio) {
		this.tipoEnvio = tipoEnvio;
	}
	public String getCodTipoMoneda() {
		return codTipoMoneda;
	}
	public void setCodTipoMoneda(String codTipoMoneda) {
		this.codTipoMoneda = codTipoMoneda;
	}
	public String getCodigoTipoMoneda() {
		return codigoTipoMoneda;
	}
	public void setCodigoTipoMoneda(String codigoTipoMoneda) {
		this.codigoTipoMoneda = codigoTipoMoneda;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public String getCodTipoMonedaProb() {
		return codTipoMonedaProb;
	}
	public void setCodTipoMonedaProb(String codTipoMonedaProb) {
		this.codTipoMonedaProb = codTipoMonedaProb;
	}
	public String getCodigoTipoMonedaProb() {
		return codigoTipoMonedaProb;
	}
	public void setCodigoTipoMonedaProb(String codigoTipoMonedaProb) {
		this.codigoTipoMonedaProb = codigoTipoMonedaProb;
	}
	public String getTipoMonedaProb() {
		return tipoMonedaProb;
	}
	public void setTipoMonedaProb(String tipoMonedaProb) {
		this.tipoMonedaProb = tipoMonedaProb;
	}
	public String getCodTipoOferta() {
		return codTipoOferta;
	}
	public void setCodTipoOferta(String codTipoOferta) {
		this.codTipoOferta = codTipoOferta;
	}
	public String getTipoOferta() {
		return tipoOferta;
	}
	public void setTipoOferta(String tipoOferta) {
		this.tipoOferta = tipoOferta;
	}
	public String getCodTipoScoring() {
		return codTipoScoring;
	}
	public void setCodTipoScoring(String codTipoScoring) {
		this.codTipoScoring = codTipoScoring;
	}
	public String getTipoScoring() {
		return tipoScoring;
	}
	public void setTipoScoring(String tipoScoring) {
		this.tipoScoring = tipoScoring;
	}
	public String getCodTarea() {
		return codTarea;
	}
	public void setCodTarea(String codTarea) {
		this.codTarea = codTarea;
	}
	public String getTarea() {
		return tarea;
	}
	public void setTarea(String tarea) {
		this.tarea = tarea;
	}
	public String getCodOficina() {
		return codOficina;
	}
	public void setCodOficina(String codOficina) {
		this.codOficina = codOficina;
	}
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	public String getFlagAreaRiesgoOficina() {
		return flagAreaRiesgoOficina;
	}
	public void setFlagAreaRiesgoOficina(String flagAreaRiesgoOficina) {
		this.flagAreaRiesgoOficina = flagAreaRiesgoOficina;
	}
	public String getFlagDesplazadaOficina() {
		return flagDesplazadaOficina;
	}
	public void setFlagDesplazadaOficina(String flagDesplazadaOficina) {
		this.flagDesplazadaOficina = flagDesplazadaOficina;
	}
	public String getFlagEscaneoWebOficina() {
		return flagEscaneoWebOficina;
	}
	public void setFlagEscaneoWebOficina(String flagEscaneoWebOficina) {
		this.flagEscaneoWebOficina = flagEscaneoWebOficina;
	}
	public String getMontoTopeOficina() {
		return montoTopeOficina;
	}
	public void setMontoTopeOficina(String montoTopeOficina) {
		this.montoTopeOficina = montoTopeOficina;
	}
	public String getTasaTransfOficina() {
		return tasaTransfOficina;
	}
	public void setTasaTransfOficina(String tasaTransfOficina) {
		this.tasaTransfOficina = tasaTransfOficina;
	}
	public String getFlagActivoOficina() {
		return flagActivoOficina;
	}
	public void setFlagActivoOficina(String flagActivoOficina) {
		this.flagActivoOficina = flagActivoOficina;
	}
	public String getCodOfiPrincipalOficina() {
		return codOfiPrincipalOficina;
	}
	public void setCodOfiPrincipalOficina(String codOfiPrincipalOficina) {
		this.codOfiPrincipalOficina = codOfiPrincipalOficina;
	}
	public String getCodTerritorioOficina() {
		return codTerritorioOficina;
	}
	public void setCodTerritorioOficina(String codTerritorioOficina) {
		this.codTerritorioOficina = codTerritorioOficina;
	}
	public String getTerritorioOficina() {
		return territorioOficina;
	}
	public void setTerritorioOficina(String territorioOficina) {
		this.territorioOficina = territorioOficina;
	}
	public String getUbicacionOficina() {
		return ubicacionOficina;
	}
	public void setUbicacionOficina(String ubicacionOficina) {
		this.ubicacionOficina = ubicacionOficina;
	}
	public String getFlagProvOficina() {
		return flagProvOficina;
	}
	public void setFlagProvOficina(String flagProvOficina) {
		this.flagProvOficina = flagProvOficina;
	}
	public String getCodClasifBanco() {
		return codClasifBanco;
	}
	public void setCodClasifBanco(String codClasifBanco) {
		this.codClasifBanco = codClasifBanco;
	}
	public String getClasificacionBanco() {
		return clasificacionBanco;
	}
	public void setClasificacionBanco(String clasificacionBanco) {
		this.clasificacionBanco = clasificacionBanco;
	}
	public String getCodProdClasifBanco() {
		return codProdClasifBanco;
	}
	public void setCodProdClasifBanco(String codProdClasifBanco) {
		this.codProdClasifBanco = codProdClasifBanco;
	}
	public String getCodClasifBancoConyugue() {
		return codClasifBancoConyugue;
	}
	public void setCodClasifBancoConyugue(String codClasifBancoConyugue) {
		this.codClasifBancoConyugue = codClasifBancoConyugue;
	}
	public String getClasificacionBancoConyugue() {
		return clasificacionBancoConyugue;
	}
	public void setClasificacionBancoConyugue(String clasificacionBancoConyugue) {
		this.clasificacionBancoConyugue = clasificacionBancoConyugue;
	}
	public String getCodProdClasifBancoConyugue() {
		return codProdClasifBancoConyugue;
	}
	public void setCodProdClasifBancoConyugue(String codProdClasifBancoConyugue) {
		this.codProdClasifBancoConyugue = codProdClasifBancoConyugue;
	}
	public String getClasificacionSBS() {
		return clasificacionSBS;
	}
	public void setClasificacionSBS(String clasificacionSBS) {
		this.clasificacionSBS = clasificacionSBS;
	}
	public String getCodPreEval() {
		return codPreEval;
	}
	public void setCodPreEval(String codPreEval) {
		this.codPreEval = codPreEval;
	}
	public String getComentarioAyuMem() {
		return comentarioAyuMem;
	}
	public void setComentarioAyuMem(String comentarioAyuMem) {
		this.comentarioAyuMem = comentarioAyuMem;
	}
	public String getComentarioDelegacion() {
		return comentarioDelegacion;
	}
	public void setComentarioDelegacion(String comentarioDelegacion) {
		this.comentarioDelegacion = comentarioDelegacion;
	}
	public String getComentarioRechazo() {
		return comentarioRechazo;
	}
	public void setComentarioRechazo(String comentarioRechazo) {
		this.comentarioRechazo = comentarioRechazo;
	}
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(String fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getFechaProgramada() {
		return fechaProgramada;
	}
	public void setFechaProgramada(String fechaProgramada) {
		this.fechaProgramada = fechaProgramada;
	}
	public String getFechaT1() {
		return fechaT1;
	}
	public void setFechaT1(String fechaT1) {
		this.fechaT1 = fechaT1;
	}
	public String getFechaT2() {
		return fechaT2;
	}
	public void setFechaT2(String fechaT2) {
		this.fechaT2 = fechaT2;
	}
	public String getFechaT3() {
		return fechaT3;
	}
	public void setFechaT3(String fechaT3) {
		this.fechaT3 = fechaT3;
	}
	public String getFlagComentario() {
		return flagComentario;
	}
	public void setFlagComentario(String flagComentario) {
		this.flagComentario = flagComentario;
	}
	public String getFlagDelegacion() {
		return flagDelegacion;
	}
	public void setFlagDelegacion(String flagDelegacion) {
		this.flagDelegacion = flagDelegacion;
	}
	public String getFlagDevolucion() {
		return flagDevolucion;
	}
	public void setFlagDevolucion(String flagDevolucion) {
		this.flagDevolucion = flagDevolucion;
	}
	public String getFlagExcDelegacion() {
		return flagExcDelegacion;
	}
	public void setFlagExcDelegacion(String flagExcDelegacion) {
		this.flagExcDelegacion = flagExcDelegacion;
	}
	public String getFlagModifScore() {
		return flagModifScore;
	}
	public void setFlagModifScore(String flagModifScore) {
		this.flagModifScore = flagModifScore;
	}
	public String getFlagRetraer() {
		return flagRetraer;
	}
	public void setFlagRetraer(String flagRetraer) {
		this.flagRetraer = flagRetraer;
	}
	public String getFlagSolTasaEsp() {
		return flagSolTasaEsp;
	}
	public void setFlagSolTasaEsp(String flagSolTasaEsp) {
		this.flagSolTasaEsp = flagSolTasaEsp;
	}
	public String getLineaConsumo() {
		return lineaConsumo;
	}
	public void setLineaConsumo(String lineaConsumo) {
		this.lineaConsumo = lineaConsumo;
	}
	public String getLineaCredAprob() {
		return lineaCredAprob;
	}
	public void setLineaCredAprob(String lineaCredAprob) {
		this.lineaCredAprob = lineaCredAprob;
	}
	public String getLineaCredSol() {
		return lineaCredSol;
	}
	public void setLineaCredSol(String lineaCredSol) {
		this.lineaCredSol = lineaCredSol;
	}
	public String getNroContrato() {
		return nroContrato;
	}
	public void setNroContrato(String nroContrato) {
		this.nroContrato = nroContrato;
	}
	public String getNroCuenta() {
		return nroCuenta;
	}
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}
	public String getNumTerminal() {
		return numTerminal;
	}
	public void setNumTerminal(String numTerminal) {
		this.numTerminal = numTerminal;
	}
	public String getTiempoCola() {
		return tiempoCola;
	}
	public void setTiempoCola(String tiempoCola) {
		this.tiempoCola = tiempoCola;
	}
	public String getTiempoEjecucion() {
		return tiempoEjecucion;
	}
	public void setTiempoEjecucion(String tiempoEjecucion) {
		this.tiempoEjecucion = tiempoEjecucion;
	}
	public String getFechaT1R() {
		return fechaT1R;
	}
	public void setFechaT1R(String fechaT1R) {
		this.fechaT1R = fechaT1R;
	}
	public String getFechaT2R() {
		return fechaT2R;
	}
	public void setFechaT2R(String fechaT2R) {
		this.fechaT2R = fechaT2R;
	}
	public String getFechaT3R() {
		return fechaT3R;
	}
	public void setFechaT3R(String fechaT3R) {
		this.fechaT3R = fechaT3R;
	}
	public String getTiempoColar() {
		return tiempoColar;
	}
	public void setTiempoColar(String tiempoColar) {
		this.tiempoColar = tiempoColar;
	}
	public String getTiempoEjecucionR() {
		return tiempoEjecucionR;
	}
	public void setTiempoEjecucionR(String tiempoEjecucionR) {
		this.tiempoEjecucionR = tiempoEjecucionR;
	}
	public String getTiempoColaRetraer() {
		return tiempoColaRetraer;
	}
	public void setTiempoColaRetraer(String tiempoColaRetraer) {
		this.tiempoColaRetraer = tiempoColaRetraer;
	}
	public String getPorcentajeEndeudamiento() {
		return porcentajeEndeudamiento;
	}
	public void setPorcentajeEndeudamiento(String porcentajeEndeudamiento) {
		this.porcentajeEndeudamiento = porcentajeEndeudamiento;
	}
	public String getRiesgoCliente() {
		return riesgoCliente;
	}
	public void setRiesgoCliente(String riesgoCliente) {
		this.riesgoCliente = riesgoCliente;
	}
	public String getSbsConyugue() {
		return sbsConyugue;
	}
	public void setSbsConyugue(String sbsConyugue) {
		this.sbsConyugue = sbsConyugue;
	}
	public String getTasaEsp() {
		return tasaEsp;
	}
	public void setTasaEsp(String tasaEsp) {
		this.tasaEsp = tasaEsp;
	}
	public String getTipoResolucion() {
		return tipoResolucion;
	}
	public void setTipoResolucion(String tipoResolucion) {
		this.tipoResolucion = tipoResolucion;
	}
	public String getVerifDom() {
		return verifDom;
	}
	public void setVerifDom(String verifDom) {
		this.verifDom = verifDom;
	}
	public String getVerifLab() {
		return verifLab;
	}
	public void setVerifLab(String verifLab) {
		this.verifLab = verifLab;
	}
	public String getVerifDPS() {
		return verifDPS;
	}
	public void setVerifDPS(String verifDPS) {
		this.verifDPS = verifDPS;
	}
	public String getZonaPel() {
		return zonaPel;
	}
	public void setZonaPel(String zonaPel) {
		this.zonaPel = zonaPel;
	}
	public String getPlazoSol() {
		return plazoSol;
	}
	public void setPlazoSol(String plazoSol) {
		this.plazoSol = plazoSol;
	}
	public String getPlazoSolAprob() {
		return plazoSolAprob;
	}
	public void setPlazoSolAprob(String plazoSolAprob) {
		this.plazoSolAprob = plazoSolAprob;
	}
	public String getTipoCambioExp() {
		return tipoCambioExp;
	}
	public void setTipoCambioExp(String tipoCambioExp) {
		this.tipoCambioExp = tipoCambioExp;
	}
	public String getFechaTipoCambioExp() {
		return fechaTipoCambioExp;
	}
	public void setFechaTipoCambioExp(String fechaTipoCambioExp) {
		this.fechaTipoCambioExp = fechaTipoCambioExp;
	}
	public String getTiempoObfTC() {
		return tiempoObfTC;
	}
	public void setTiempoObfTC(String tiempoObfTC) {
		this.tiempoObfTC = tiempoObfTC;
	}
	public String getTiempoObjTE() {
		return tiempoObjTE;
	}
	public void setTiempoObjTE(String tiempoObjTE) {
		this.tiempoObjTE = tiempoObjTE;
	}
	public String getTiempoPreTC() {
		return tiempoPreTC;
	}
	public void setTiempoPreTC(String tiempoPreTC) {
		this.tiempoPreTC = tiempoPreTC;
	}
	public String getTiempoPreTe() {
		return tiempoPreTe;
	}
	public void setTiempoPreTe(String tiempoPreTe) {
		this.tiempoPreTe = tiempoPreTe;
	}
	public String getNomColumna() {
		return nomColumna;
	}
	public void setNomColumna(String nomColumna) {
		this.nomColumna = nomColumna;
	}
	public String getAns() {
		return ans;
	}
	public void setAns(String ans) {
		this.ans = ans;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public String getAvalCliente() {
		return avalCliente;
	}
	public void setAvalCliente(String avalCliente) {
		this.avalCliente = avalCliente;
	}
	public String getCelularCliente() {
		return celularCliente;
	}
	public void setCelularCliente(String celularCliente) {
		this.celularCliente = celularCliente;
	}
	public String getCorreoCliente() {
		return correoCliente;
	}
	public void setCorreoCliente(String correoCliente) {
		this.correoCliente = correoCliente;
	}
	public String getSubrogCliente() {
		return subrogCliente;
	}
	public void setSubrogCliente(String subrogCliente) {
		this.subrogCliente = subrogCliente;
	}
	public String getAvalConyugue() {
		return avalConyugue;
	}
	public void setAvalConyugue(String avalConyugue) {
		this.avalConyugue = avalConyugue;
	}
	public String getCelularConyugue() {
		return celularConyugue;
	}
	public void setCelularConyugue(String celularConyugue) {
		this.celularConyugue = celularConyugue;
	}
	public String getCorreoConyugue() {
		return correoConyugue;
	}
	public void setCorreoConyugue(String correoConyugue) {
		this.correoConyugue = correoConyugue;
	}
	public String getSubrogConyugue() {
		return subrogConyugue;
	}
	public void setSubrogConyugue(String subrogConyugue) {
		this.subrogConyugue = subrogConyugue;
	}
	public String getCodigoSegmentoCliente() {
		return codigoSegmentoCliente;
	}
	public void setCodigoSegmentoCliente(String codigoSegmentoCliente) {
		this.codigoSegmentoCliente = codigoSegmentoCliente;
	}
	public String getCatRenDescripCliente() {
		return catRenDescripCliente;
	}
	public void setCatRenDescripCliente(String catRenDescripCliente) {
		this.catRenDescripCliente = catRenDescripCliente;
	}
	public String getCodigoSegmentoConyugue() {
		return codigoSegmentoConyugue;
	}
	public void setCodigoSegmentoConyugue(String codigoSegmentoConyugue) {
		this.codigoSegmentoConyugue = codigoSegmentoConyugue;
	}
	public String getCatRenDescripConyugue() {
		return catRenDescripConyugue;
	}
	public void setCatRenDescripConyugue(String catRenDescripConyugue) {
		this.catRenDescripConyugue = catRenDescripConyugue;
	}
	public String getCorreoEmpleado() {
		return correoEmpleado;
	}
	public void setCorreoEmpleado(String correoEmpleado) {
		this.correoEmpleado = correoEmpleado;
	}
	public String getApePatEmpleado() {
		return apePatEmpleado;
	}
	public void setApePatEmpleado(String apePatEmpleado) {
		this.apePatEmpleado = apePatEmpleado;
	}
	public String getApeMatEmpleado() {
		return apeMatEmpleado;
	}
	public void setApeMatEmpleado(String apeMatEmpleado) {
		this.apeMatEmpleado = apeMatEmpleado;
	}
	public String getNombresEmpleado() {
		return nombresEmpleado;
	}
	public void setNombresEmpleado(String nombresEmpleado) {
		this.nombresEmpleado = nombresEmpleado;
	}
	public String getCorreoEmpleadoResp() {
		return correoEmpleadoResp;
	}
	public void setCorreoEmpleadoResp(String correoEmpleadoResp) {
		this.correoEmpleadoResp = correoEmpleadoResp;
	}
	public String getApePatEmpleadoResp() {
		return apePatEmpleadoResp;
	}
	public void setApePatEmpleadoResp(String apePatEmpleadoResp) {
		this.apePatEmpleadoResp = apePatEmpleadoResp;
	}
	public String getApeMatEmpleadoResp() {
		return apeMatEmpleadoResp;
	}
	public void setApeMatEmpleadoResp(String apeMatEmpleadoResp) {
		this.apeMatEmpleadoResp = apeMatEmpleadoResp;
	}
	public String getNombresEmpleadoResp() {
		return nombresEmpleadoResp;
	}
	public void setNombresEmpleadoResp(String nombresEmpleadoResp) {
		this.nombresEmpleadoResp = nombresEmpleadoResp;
	}
	public String getDescripTerOfiEmpleado() {
		return descripTerOfiEmpleado;
	}
	public void setDescripTerOfiEmpleado(String descripTerOfiEmpleado) {
		this.descripTerOfiEmpleado = descripTerOfiEmpleado;
	}
	public String getUbicaTerOfiEmpleado() {
		return ubicaTerOfiEmpleado;
	}
	public void setUbicaTerOfiEmpleado(String ubicaTerOfiEmpleado) {
		this.ubicaTerOfiEmpleado = ubicaTerOfiEmpleado;
	}
	public String getDescripTerOfiEmpleadoResp() {
		return descripTerOfiEmpleadoResp;
	}
	public void setDescripTerOfiEmpleadoResp(String descripTerOfiEmpleadoResp) {
		this.descripTerOfiEmpleadoResp = descripTerOfiEmpleadoResp;
	}
	public String getUbicaTerOfiEmpleadoResp() {
		return ubicaTerOfiEmpleadoResp;
	}
	public void setUbicaTerOfiEmpleadoResp(String ubicaTerOfiEmpleadoResp) {
		this.ubicaTerOfiEmpleadoResp = ubicaTerOfiEmpleadoResp;
	}
	public String getDescripPerfil() {
		return descripPerfil;
	}
	public void setDescripPerfil(String descripPerfil) {
		this.descripPerfil = descripPerfil;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}

package com.ibm.bbva.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TBL_CE_IBM_HISTORIAL database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_HISTORIAL", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="Historial.findAll", query="SELECT h FROM Historial h"),
	@NamedQuery(name = "Historial.findById", query = "SELECT s FROM Historial s WHERE s.id = :id"), 
	@NamedQuery(name="Historial.findbyIdExpediente", query="SELECT h FROM Historial h WHERE h.expediente.id = :idExpediente ORDER BY h.id DESC"),
	@NamedQuery(name="Historial.findbyIdExpFlagRetraer", query="SELECT h FROM Historial h WHERE h.expediente.id = :idExpediente and h.flagRetraer = : flagRetaer ORDER BY h.id DESC")
})
public class Historial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_HISTORIAL_ID_GENERATOR", sequenceName="SEQ_CE_IBM_HISTORIAL", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_HISTORIAL_ID_GENERATOR")
	private long id;

	private String accion;

	//bi-directional many-to-one association to Clasif_Banco
	@ManyToOne
	@JoinColumn(name="BANCO_CONYUGE")		
	private ClasifBanco bancoConyuge;

	//bi-directional many-to-one association to Clasif_Banco
	@ManyToOne
	@JoinColumn(name="CLASIFICACION_BANCO")
	private ClasifBanco clasificacionBanco;

	@Column(name="CLASIFICACION_SBS")
	private double clasificacionSbs;

	@Column(name="COD_PRE_EVAL")
	private String codPreEval;

	private String comentario;

	@Column(name="COMENTARIO_AYU_MEM")
	private String comentarioAyuMem;

	@Column(name="COMENTARIO_DELEGACION")
	private String comentarioDelegacion;

	@Column(name="COMENTARIO_RECHAZO")
	private String comentarioRechazo;

	@Column(name="FEC_REGISTRO", insertable=false, updatable=false)
	private Timestamp fecRegistro;

	@Column(name="FECHA_ENVIO")
	private Timestamp fechaEnvio;

	@Column(name="FECHA_FIN")
	private Timestamp fechaFin;

	@Column(name="FECHA_PROGRAMADA")
	private Timestamp fechaProgramada;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_T1")
	private Timestamp fechaT1;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_T2")
	private Timestamp fechaT2;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_T3")
	private Timestamp fechaT3;

	@Column(name="FLAG_COMENTARIO")
	private String flagComentario;

	@Column(name="FLAG_DELEGACION")
	private String flagDelegacion;

	@Column(name="FLAG_DEVOLUCION")
	private String flagDevolucion;

	@Column(name="FLAG_EXC_DELEGACION")
	private String flagExcDelegacion;

	@Column(name="FLAG_MODIF_SCORE")
	private String flagModifScore;

	@Column(name="FLAG_RETRAER")
	private String flagRetraer;

	@Column(name="FLAG_SOL_TASA_ESP")
	private String flagSolTasaEsp;

	@Column(name="LINEA_CONSUMO")
	private double lineaConsumo;

	@Column(name="LINEA_CRED_APROB")
	private double lineaCredAprob;

	@Column(name="LINEA_CRED_SOL")
	private double lineaCredSol;

	@Column(name="NRO_CONTRATO")
	private String nroContrato;

	@Column(name="NRO_CTA")
	private String nroCta;

	@Column(name="NUM_TERMINAL")
	private String numTerminal;
	
	@Column(name="TIEMPO_COLA")
	private double tiempoCola;
	
	@Column(name="TIEMPO_EJECUCION")
	private double tiempoEjecucion;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_T1R")
	private Timestamp fechaT1R;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_T2R")
	private Timestamp fechaT2R;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_T3R")
	private Timestamp fechaT3R;

	@Column(name="TIEMPO_COLAR")
	private double tiempoColar;
	
	@Column(name="TIEMPO_EJECUCIONR")
	private double tiempoEjecucionr;
	
	@Column(name="TIEMPO_COLA_RETRAER")
	private double tiempoColaRetraer;
	
	@Column(name="PORCENTAJE_ENDEUDAMIENTO")
	private double porcentajeEndeudamiento;

	@Column(name="RIESGO_CLIENTE")
	private double riesgoCliente;

	private String rvgl;

	@Column(name="SBS_CONYUGE")
	private double sbsConyuge;

	@Column(name="SOL_TASA_ESP")
	private String solTasaEsp;

	@Column(name="TASA_ESP")
	private double tasaEsp;

	@Column(name="TIPO_RESOLUCION")
	private String tipoResolucion;

	@Column(name="VERIF_DOM")
	private String verifDom;

	@Column(name="VERIF_LAB")
	private String verifLab;
	
	@Column(name="VERIF_DPS")
	private String verifDps;

	@Column(name="ZONA_PEL")
	private String zonaPel;
	
	@Column(name="PLAZO_SOL")
	private String plazoSolicitado;
	
	@Column(name="PLAZO_SOL_APROB")
	private String plazoSolicitadoApr;
	
	@Column(name="TIPO_CAMBIO_EXP")
	private BigDecimal tipoCambioExp;
	
	@Temporal( TemporalType.DATE)
	@Column(name="FECHA_TIPOCAMBIO_EXP")	
	private Date fechaTipoCambioExp;
	
	//uni-directional many-to-one association to Garantia
	@ManyToOne
	@JoinColumn(name="ID_OFICINA_FK")
	private Oficina oficina;	
	
	//uni-directional many-to-one association to Garantia
	@ManyToOne
	@JoinColumn(name="ID_PERFIL_FK")
	private Perfil perfil;	

	//uni-directional many-to-one association to ClienteNatural
	@ManyToOne
	@JoinColumn(name="ID_CLIENTE_NATURAL_CONYUGE_FK")
	private ClienteNatural clienteNaturalConyuge;

	//uni-directional many-to-one association to ClienteNatural
	@ManyToOne
	@JoinColumn(name="ID_CLIENTE_NATURAL_FK")
	private ClienteNatural clienteNatural;

	//uni-directional many-to-one association to Empleado
	@ManyToOne
	@JoinColumn(name="ID_EMPLEADO_FK")
	private Empleado empleado;

	//uni-directional many-to-one association to Empleado
	@ManyToOne
	@JoinColumn(name="ID_EMP_RESP_FK")
	private Empleado empleadoResp;

	//uni-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADOENVWORKFLOWTC_FK")
	private Estado estadoEnvWorkflowTc;

	//uni-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADOTIPRESOL_FK")
	private Estado estadoTipoResol;

	//uni-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO_FK")
	private Estado estado;

	//uni-directional many-to-one association to Expediente
	@ManyToOne
	@JoinColumn(name="ID_EXPEDIENTE_FK")
	private Expediente expediente;

	//uni-directional many-to-one association to MotivoDevolucion
	@ManyToOne
	@JoinColumn(name="ID_MOTIVO_DEVOLUCION_FK")
	private MotivoDevolucion motivoDevolucion;

	//uni-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	//uni-directional many-to-one association to Subproducto
	@ManyToOne
	@JoinColumn(name="ID_SUBPRODUCTO_FK")
	private Subproducto subproducto;

	//uni-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;

	//uni-directional many-to-one association to TipoBuro
	@ManyToOne
	@JoinColumn(name="ID_TIPO_BURO_FK")
	private TipoBuro tipoBuro;

	//uni-directional many-to-one association to TipoEnvio
	@ManyToOne
	@JoinColumn(name="ID_TIPO_ENVIO_FK")
	private TipoEnvio tipoEnvio;

	//uni-directional many-to-one association to TipoMoneda
	@ManyToOne
	@JoinColumn(name="ID_TIPMONAPROB_FK")
	private TipoMoneda tipoMonedaAprob;

	//uni-directional many-to-one association to TipoMoneda
	@ManyToOne
	@JoinColumn(name="ID_TIPMONSOL_FK")
	private TipoMoneda tipoMonedaSol;

	//uni-directional many-to-one association to TipoOferta
	@ManyToOne
	@JoinColumn(name="ID_TIPO_OFERTA_FK")
	private TipoOferta tipoOferta;

	//uni-directional many-to-one association to TipoScoring
	@ManyToOne
	@JoinColumn(name="ID_TIPO_SCORING_FK")
	private TipoScoring tipoScoring;
	
	@Column(name="TIEMPO_OBJ_TC")
	private int tiempoObjTC;
	
	@Column(name="TIEMPO_OBJ_TE")
	private int tiempoObjTE;
	
	@Column(name="TIEMPO_PRE_TC")
	private int tiempoPreTC;
	
	@Column(name="TIEMPO_PRE_TE")
	private int tiempoPreTE;
	
	@Column(name="NOM_COLUMNA")
	private String nomColumna;
	
	@Column(name="ANS")
	private int ans;
	

	public Historial() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public ClasifBanco getBancoConyuge() {
		return this.bancoConyuge;
	}

	public void setBancoConyuge(ClasifBanco bancoConyuge) {
		this.bancoConyuge = bancoConyuge;
	}

	public ClasifBanco getClasificacionBanco() {
		return this.clasificacionBanco;
	}

	public void setClasificacionBanco(ClasifBanco clasificacionBanco) {
		this.clasificacionBanco = clasificacionBanco;
	}

	public double getClasificacionSbs() {
		return this.clasificacionSbs;
	}

	public void setClasificacionSbs(double clasificacionSbs) {
		this.clasificacionSbs = clasificacionSbs;
	}

	public String getCodPreEval() {
		return this.codPreEval;
	}

	public void setCodPreEval(String codPreEval) {
		this.codPreEval = codPreEval;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getComentarioAyuMem() {
		return this.comentarioAyuMem;
	}

	public void setComentarioAyuMem(String comentarioAyuMem) {
		this.comentarioAyuMem = comentarioAyuMem;
	}

	public String getComentarioDelegacion() {
		return this.comentarioDelegacion;
	}

	public void setComentarioDelegacion(String comentarioDelegacion) {
		this.comentarioDelegacion = comentarioDelegacion;
	}

	public String getComentarioRechazo() {
		return this.comentarioRechazo;
	}

	public void setComentarioRechazo(String comentarioRechazo) {
		this.comentarioRechazo = comentarioRechazo;
	}

	public Timestamp getFecRegistro() {
		return this.fecRegistro;
	}

	public void setFecRegistro(Timestamp fecRegistro) {
		this.fecRegistro = fecRegistro;
	}

	public Timestamp getFechaEnvio() {
		return this.fechaEnvio;
	}

	public void setFechaEnvio(Timestamp fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Timestamp getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Timestamp fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Timestamp getFechaProgramada() {
		return this.fechaProgramada;
	}

	public void setFechaProgramada(Timestamp fechaProgramada) {
		this.fechaProgramada = fechaProgramada;
	}

	public Timestamp getFechaT1() {
		return this.fechaT1;
	}

	public void setFechaT1(Timestamp fechaT1) {
		this.fechaT1 = fechaT1;
	}

	public Timestamp getFechaT2() {
		return this.fechaT2;
	}

	public void setFechaT2(Timestamp fechaT2) {
		this.fechaT2 = fechaT2;
	}

	public Timestamp getFechaT3() {
		return this.fechaT3;
	}

	public void setFechaT3(Timestamp fechaT3) {
		this.fechaT3 = fechaT3;
	}

	public String getFlagComentario() {
		return this.flagComentario;
	}

	public void setFlagComentario(String flagComentario) {
		this.flagComentario = flagComentario;
	}

	public String getFlagDelegacion() {
		return this.flagDelegacion;
	}

	public void setFlagDelegacion(String flagDelegacion) {
		this.flagDelegacion = flagDelegacion;
	}

	public String getFlagDevolucion() {
		return this.flagDevolucion;
	}

	public void setFlagDevolucion(String flagDevolucion) {
		this.flagDevolucion = flagDevolucion;
	}

	public String getFlagExcDelegacion() {
		return this.flagExcDelegacion;
	}

	public void setFlagExcDelegacion(String flagExcDelegacion) {
		this.flagExcDelegacion = flagExcDelegacion;
	}

	public String getFlagModifScore() {
		return this.flagModifScore;
	}

	public void setFlagModifScore(String flagModifScore) {
		this.flagModifScore = flagModifScore;
	}

	public String getFlagRetraer() {
		return this.flagRetraer;
	}

	public void setFlagRetraer(String flagRetraer) {
		this.flagRetraer = flagRetraer;
	}

	public String getFlagSolTasaEsp() {
		return this.flagSolTasaEsp;
	}

	public void setFlagSolTasaEsp(String flagSolTasaEsp) {
		this.flagSolTasaEsp = flagSolTasaEsp;
	}

	public double getLineaConsumo() {
		return this.lineaConsumo;
	}

	public void setLineaConsumo(double lineaConsumo) {
		this.lineaConsumo = lineaConsumo;
	}

	public double getLineaCredAprob() {
		return this.lineaCredAprob;
	}

	public void setLineaCredAprob(double lineaCredAprob) {
		this.lineaCredAprob = lineaCredAprob;
	}

	public double getLineaCredSol() {
		return this.lineaCredSol;
	}

	public void setLineaCredSol(double lineaCredSol) {
		this.lineaCredSol = lineaCredSol;
	}

	public String getNroContrato() {
		return this.nroContrato;
	}

	public void setNroContrato(String nroContrato) {
		this.nroContrato = nroContrato;
	}

	public String getNroCta() {
		return this.nroCta;
	}

	public void setNroCta(String nroCta) {
		this.nroCta = nroCta;
	}

	public String getNumTerminal() {
		return this.numTerminal;
	}

	public void setNumTerminal(String numTerminal) {
		this.numTerminal = numTerminal;
	}

	public double getPorcentajeEndeudamiento() {
		return this.porcentajeEndeudamiento;
	}

	public void setPorcentajeEndeudamiento(double porcentajeEndeudamiento) {
		this.porcentajeEndeudamiento = porcentajeEndeudamiento;
	}

	public double getRiesgoCliente() {
		return this.riesgoCliente;
	}

	public void setRiesgoCliente(double riesgoCliente) {
		this.riesgoCliente = riesgoCliente;
	}

	public String getRvgl() {
		return this.rvgl;
	}

	public void setRvgl(String rvgl) {
		this.rvgl = rvgl;
	}

	public double getSbsConyuge() {
		return this.sbsConyuge;
	}

	public void setSbsConyuge(double sbsConyuge) {
		this.sbsConyuge = sbsConyuge;
	}

	public String getSolTasaEsp() {
		return this.solTasaEsp;
	}

	public void setSolTasaEsp(String solTasaEsp) {
		this.solTasaEsp = solTasaEsp;
	}

	public double getTasaEsp() {
		return this.tasaEsp;
	}

	public void setTasaEsp(double tasaEsp) {
		this.tasaEsp = tasaEsp;
	}

	public String getTipoResolucion() {
		return this.tipoResolucion;
	}

	public void setTipoResolucion(String tipoResolucion) {
		this.tipoResolucion = tipoResolucion;
	}

	public String getVerifDom() {
		return this.verifDom;
	}

	public void setVerifDom(String verifDom) {
		this.verifDom = verifDom;
	}

	public String getVerifLab() {
		return this.verifLab;
	}

	public void setVerifLab(String verifLab) {
		this.verifLab = verifLab;
	}

	public String getZonaPel() {
		return this.zonaPel;
	}

	public void setZonaPel(String zonaPel) {
		this.zonaPel = zonaPel;
	}

	public ClienteNatural getClienteNaturalConyuge() {
		return this.clienteNaturalConyuge;
	}

	public void setClienteNaturalConyuge(ClienteNatural clienteNaturalConyuge) {
		this.clienteNaturalConyuge = clienteNaturalConyuge;
	}

	public ClienteNatural getClienteNatural() {
		return this.clienteNatural;
	}

	public void setClienteNatural(ClienteNatural clienteNatural) {
		this.clienteNatural = clienteNatural;
	}

	public Empleado getEmpleado() {
		return this.empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Empleado getEmpleadoResp() {
		return this.empleadoResp;
	}

	public void setEmpleadoResp(Empleado empleadoResp) {
		this.empleadoResp = empleadoResp;
	}

	public Estado getEstadoEnvWorkflowTc() {
		return this.estadoEnvWorkflowTc;
	}

	public void setEstadoEnvWorkflowTc(Estado estadoEnvWorkflowTc) {
		this.estadoEnvWorkflowTc = estadoEnvWorkflowTc;
	}

	public Estado getEstadoTipoResol() {
		return this.estadoTipoResol;
	}

	public void setEstadoTipoResol(Estado estadoTipoResol) {
		this.estadoTipoResol = estadoTipoResol;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Expediente getExpediente() {
		return this.expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public MotivoDevolucion getMotivoDevolucion() {
		return this.motivoDevolucion;
	}

	public void setMotivoDevolucion(MotivoDevolucion motivoDevolucion) {
		this.motivoDevolucion = motivoDevolucion;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Subproducto getSubproducto() {
		return this.subproducto;
	}

	public void setSubproducto(Subproducto subproducto) {
		this.subproducto = subproducto;
	}

	public Tarea getTarea() {
		return this.tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public TipoBuro getTipoBuro() {
		return this.tipoBuro;
	}

	public void setTipoBuro(TipoBuro tipoBuro) {
		this.tipoBuro = tipoBuro;
	}

	public TipoEnvio getTipoEnvio() {
		return this.tipoEnvio;
	}

	public void setTipoEnvio(TipoEnvio tipoEnvio) {
		this.tipoEnvio = tipoEnvio;
	}

	public TipoMoneda getTipoMonedaAprob() {
		return this.tipoMonedaAprob;
	}

	public void setTipoMonedaAprob(TipoMoneda tipoMonedaAprob) {
		this.tipoMonedaAprob = tipoMonedaAprob;
	}

	public TipoMoneda getTipoMonedaSol() {
		return this.tipoMonedaSol;
	}

	public void setTipoMonedaSol(TipoMoneda tipoMonedaSol) {
		this.tipoMonedaSol = tipoMonedaSol;
	}

	public TipoOferta getTipoOferta() {
		return this.tipoOferta;
	}

	public void setTipoOferta(TipoOferta tipoOferta) {
		this.tipoOferta = tipoOferta;
	}

	public TipoScoring getTipoScoring() {
		return this.tipoScoring;
	}

	public void setTipoScoring(TipoScoring tipoScoring) {
		this.tipoScoring = tipoScoring;
	}

	public double getTiempoCola() {
		return tiempoCola;
	}

	public void setTiempoCola(double tiempoCola) {
		this.tiempoCola = tiempoCola;
	}

	public double getTiempoEjecucion() {
		return tiempoEjecucion;
	}

	public void setTiempoEjecucion(double tiempoEjecucion) {
		this.tiempoEjecucion = tiempoEjecucion;
	}

	public Timestamp getFechaT1R() {
		return fechaT1R;
	}

	public void setFechaT1R(Timestamp fechaT1R) {
		this.fechaT1R = fechaT1R;
	}

	public Timestamp getFechaT2R() {
		return fechaT2R;
	}

	public void setFechaT2R(Timestamp fechaT2R) {
		this.fechaT2R = fechaT2R;
	}

	public Timestamp getFechaT3R() {
		return fechaT3R;
	}

	public void setFechaT3R(Timestamp fechaT3R) {
		this.fechaT3R = fechaT3R;
	}

	public double getTiempoColar() {
		return tiempoColar;
	}

	public void setTiempoColar(double tiempoColar) {
		this.tiempoColar = tiempoColar;
	}

	public double getTiempoEjecucionr() {
		return tiempoEjecucionr;
	}

	public void setTiempoEjecucionr(double tiempoEjecucionr) {
		this.tiempoEjecucionr = tiempoEjecucionr;
	}
	
	public double getTiempoColaRetraer() {
		return tiempoColaRetraer;
	}

	public void setTiempoColaRetraer(double tiempoColaRetraer) {
		this.tiempoColaRetraer = tiempoColaRetraer;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public String getVerifDps() {
		return verifDps;
	}

	public void setVerifDps(String verifDps) {
		this.verifDps = verifDps;
	}

	public String getPlazoSolicitado() {
		return plazoSolicitado;
	}

	public void setPlazoSolicitado(String plazoSolicitado) {
		this.plazoSolicitado = plazoSolicitado;
	}

	public String getPlazoSolicitadoApr() {
		return plazoSolicitadoApr;
	}

	public void setPlazoSolicitadoApr(String plazoSolicitadoApr) {
		this.plazoSolicitadoApr = plazoSolicitadoApr;
	}

	public BigDecimal getTipoCambioExp() {
		return tipoCambioExp;
	}

	public void setTipoCambioExp(BigDecimal tipoCambioExp) {
		this.tipoCambioExp = tipoCambioExp;
	}

	public Date getFechaTipoCambioExp() {
		return fechaTipoCambioExp;
	}

	public void setFechaTipoCambioExp(Date fechaTipoCambioExp) {
		this.fechaTipoCambioExp = fechaTipoCambioExp;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public int getTiempoObjTC() {
		return tiempoObjTC;
	}

	public void setTiempoObjTC(int tiempoObjTC) {
		this.tiempoObjTC = tiempoObjTC;
	}

	public int getTiempoObjTE() {
		return tiempoObjTE;
	}

	public void setTiempoObjTE(int tiempoObjTE) {
		this.tiempoObjTE = tiempoObjTE;
	}

	public int getTiempoPreTC() {
		return tiempoPreTC;
	}

	public void setTiempoPreTC(int tiempoPreTC) {
		this.tiempoPreTC = tiempoPreTC;
	}

	public int getTiempoPreTE() {
		return tiempoPreTE;
	}

	public void setTiempoPreTE(int tiempoPreTE) {
		this.tiempoPreTE = tiempoPreTE;
	}

	public String getNomColumna() {
		return nomColumna;
	}

	public void setNomColumna(String nomColumna) {
		this.nomColumna = nomColumna;
	}

	public int getAns() {
		return ans;
	}

	public void setAns(int ans) {
		this.ans = ans;
	}	
	
}
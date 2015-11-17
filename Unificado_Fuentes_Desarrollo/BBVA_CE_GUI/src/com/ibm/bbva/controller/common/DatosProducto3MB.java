package com.ibm.bbva.controller.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRs;
import pe.com.grupobbva.sce.tc84.CtTipoCambio;
import pe.com.grupobbva.sce.tc84.CtTipos;
import bbva.ws.api.view.BBVAFacadeLocal;
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DelegacionRiesgoCondicion;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Garantia;
import com.ibm.bbva.entities.Nivel;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.ProductoEtiqueta;
import com.ibm.bbva.entities.Subproducto;
import com.ibm.bbva.entities.TipoBuro;
import com.ibm.bbva.entities.TipoCambioCE;
import com.ibm.bbva.entities.TipoEnvio;
import com.ibm.bbva.entities.TipoMoneda;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.entities.TipoScoring;
import com.ibm.bbva.service.business.client.ExpedienteDTO;
import com.ibm.bbva.session.DelegacionRiesgoCondicionBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.MensajesBeanLocal;
import com.ibm.bbva.session.NivelBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.ProductoEtiquetaBeanLocal;
import com.ibm.bbva.session.ProductoTareaBeanLocal;
import com.ibm.bbva.session.TipoCambioCEBeanLocal;
import com.ibm.bbva.session.TipoMonedaBeanLocal;
import com.ibm.bbva.util.AyudaVerificacionExp;
import com.ibm.bbva.util.Util;
import com.ibm.bbva.util.UtilWebService;


@SuppressWarnings("serial")
@ManagedBean(name = "datosProducto3")
@RequestScoped
public class DatosProducto3MB extends AbstractMBean {

	@EJB
	EmpleadoBeanLocal empleadoBean;
	@EJB
	TipoMonedaBeanLocal tipoMonedaBean;
	@EJB
	private FacadeLocal facade;
	@EJB
	private ProductoEtiquetaBeanLocal productoEtiquetabean;	
	@EJB
	private NivelBeanLocal nivelBean;		
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	@EJB
	private TipoCambioCEBeanLocal tipoCambioCEBeanLocal;
	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;
	@EJB
	private ProductoTareaBeanLocal productoTareaBean;
	@EJB
	private MensajesBeanLocal mensajeBean;
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	OficinaBeanLocal oficinaBean;
	
	private Empleado empleadoSession;
	private Producto producto;
	private Subproducto subProducto;
	private TipoOferta tipoOferta;
	private Expediente expediente;
	private TipoEnvio tipoEnvio;
	private TipoBuro tipoBuro;
	private TipoScoring tipoScoring;
	private TipoMoneda tipoMonedaCreSol;
	private TipoMoneda tipoMonedaCreApr;
	private ClienteNatural clienteNatural;
	private Estado estado;
	private Garantia garantia;
	private String tipoMonedaSel;	
	private List<SelectItem> listTipoMoneda;
	private Double lineaCreditoOriginal;
	private boolean selectedItemTe;
	private boolean selectedItemSc;
	private boolean selectedItemDe;
	private boolean renderedTe;
	private boolean renderedSc;
	private boolean renderedDe;
	private boolean renderedCredApro;
	private boolean renderedTasaEsp;
	private boolean renderedWfTc;	
	private boolean habCredApro;
	private boolean habCheckTe;
	private boolean habCheckSc;
	private boolean habCheckDe;
	private boolean renderedNumContrVer;
	private boolean renderedNumContrT;
	private boolean renderedNumContr;
	private boolean renderedNumTarjetaVer;
	private boolean renderedNumTarjetaT;
	private boolean renderedNumTarjeta;	
	private boolean renderedAdicional;
	
	private boolean habTipoMonedaCredApro;
	private boolean renderedTMCredApro;
	private boolean habPlazoSolApr;

	/* String para formato numerico */
	private String lineaCredAprob;
	private String lineaCredSol;
	private String tasaEsp;	
	private String lineaCreditoAprobado;
	
	private String estadoTarjeta;
	private String numTarjeta;
	/**
	 * Cambio Para PLD
	 * */
	private boolean renderedEnvioTarjeta;
	private boolean renderedPlazoSolicitado;
	private boolean renderedPlazoSolicitadoApr;
	private boolean renderedSolicitudTasaEsp;
	private boolean renderedGarantia;
	private boolean renderedPlazoSolicitadoAprobOk;
	private boolean renderedPlazoSolicitadoAprobEdit;
	private String msjOperacion; 
	private boolean msjOperacionBol;
	/*INICIO REQUERIMIENTO 292 20.11.2014*/
	private String msjOperacion292=""; 
	private boolean msjOperacionBol292=false;
	/*INICIO REQUERIMIENTO 292 20.11.2014*/
	
	private boolean disabledSolicitudTasaEsp;
	private boolean renderedLineaCredAprob;
	private boolean renderedPlazoSolicAprob;
	
	//FIX2 ERIKA ABREGU
	private boolean disabledPorsEndeudamiento;
	private Double porcentEndeudaCambiado;
	private String porcentEndeudaCambiadoOriginal;
	//FIN DE FIX2 ERIKA ABREGU
	
	//FIX2 ERIKA ABREGU	
	//private DelegacionRiesgoCondicion delegacionRiesgoCondicion;
	//private DelegacionRiesgoCondicionBeanLocal delegacionRiesgoCondicionBeanLocalBean;
	@EJB
	DelegacionRiesgoCondicionBeanLocal delegacionRiesgoCondicionBeanLocalBean;
	
	
	private String msgErrorPersonalizado;
	
	private String msgPersonalizado;
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosProducto3MB.class);
	
	public DatosProducto3MB() {
		
	}
	
	@PostConstruct
    public void init() {
		expediente = (Expediente)getObjectSession(Constantes.EXPEDIENTE_SESION);
		renderedPlazoSolicitadoAprobEdit=false;
		
		//FIX2 ERIKA ABREGU
		disabledPorsEndeudamiento = true;
		
		String nombJSP = getNombreJSPPrincipal();
		
		//if (!nombJSP.equals("formVisualizarExpediente")){
			if(expediente.getId() > 0 && (expediente.getClienteNatural() == null || expediente.getClienteNatural().getId() <= 0)){
				expediente = expedienteBean.buscarPorId(expediente.getId());
			}			
		//}else{
		//	LOG.info("NO ES VISUALIZACION DE EXP");
		//}
		
			LOG.info("Plazo Solicitado:::"+expediente.getExpedienteTC().getPlazoSolicitado());
			
		empleadoSession = (Empleado)getObjectSession(Constantes.USUARIO_SESION);
		estadoTarjeta = (String) getObjectSession(Constantes.ESTADO_TARJETA);
		disabledSolicitudTasaEsp=true;
		
		String lineaCredAprob = Double.toString(expediente.getExpedienteTC().getLineaCredAprob());	
		String plazoSolicitadoApr = expediente.getExpedienteTC().getPlazoSolicitadoApr();
		if (expediente!=null && expediente.getExpedienteTC()!=null) {
			expediente.getExpedienteTC().setLineaCredAprob(Util.convertStringToDouble(Util.esNuloVacio(lineaCredAprob) ? "0" : lineaCredAprob.trim()));
			expediente.getExpedienteTC().setPlazoSolicitadoApr(Util.esNuloVacio(plazoSolicitadoApr) ? "" : plazoSolicitadoApr.trim());			
		}
		
		/*Mostrar o Ocultar Campos*/
		
		if (nombJSP.equals("formAprobarExpediente")) {
			iniciarAprobarExpediente();
		} else if (nombJSP.equals("formVerificarResultadoDomiciliaria")) {
			iniciarVerificarResultadoDomiciliaria();
		} else if (nombJSP.equals("formVerificarEstadoContratacion")) {
			iniciarVerificarEstadoContratacion();
		} else if (nombJSP.equals("formVerificarConformidadCierreExp")) {
			iniciarVerificarConformidadCierreExp();
		} else if (nombJSP.equals("formRegistrarAprobResolucion")) {
			iniciarRegistrarAprobResolucion();
		} else if (nombJSP.equals("formVerificarExpDesestimados")) {
			iniciarVerificarExpDesestimados();
		} else if (nombJSP.equals("formCambiarSituacionExp")) {
			iniciarCambiarSituacionExp();	
		} else if (nombJSP.equals("formCambiarSituacionTramite")) {
			iniciarCambiarSituacionTramite();	
		} else if (nombJSP.equals("formEjecutarEvalCrediticia")) {
			iniciarEjecutarEvalCrediticia();
		} else if (nombJSP.equals("formRealizarAltaTarjeta")) {	
			iniciarRealizarAltaTarjeta ();
		} else if (nombJSP.equals("formRevisarRegistrarDictamen")) {
			iniciarRevisarRegistrarDictamen();
		} else if (nombJSP.equals("formEvaluarFactibilidadOp")) {
			iniciarEvaluarFactibilidadOp();
		} else if (nombJSP.equals("formSolicitarVerificacionDomiciliaria")) {
			iniciarformSolicitarVerificacionDomiciliaria();
		} else if (nombJSP.equals("formArchivarExpediente")) {
			iniciarArchivarExpediente();			
		} else if (nombJSP.equals("formVisualizarExpediente")) {
			renderedNumTarjetaVer = true; 
			
				
			renderedNumContrVer = true;			
			renderedNumContrT = true;
			
			if (expediente!=null && expediente.getExpedienteTC()!=null) 
				if(expediente.getExpedienteTC().getNroContrato()!=null && !expediente.getExpedienteTC().getNroContrato().equals("") && 
						expediente.getExpedienteTC().getNroContrato().length()==20){					
					renderedNumContr = true;	
					renderedNumTarjetaT = true;	
					renderedNumTarjeta = true;
					renderedNumContrT = true;
					
				}else{
					renderedNumContr = false;
					renderedNumTarjeta = false;
					renderedNumTarjetaT = true;	
				}
			
			renderedCredApro = true;
			renderedTMCredApro = true;
			
		}
		
		obtenerDatos();
		validarVisualizacionDatos(expediente);
		
		this.estado.setDescripcion(estadoTarjeta);
		LOG.info("renderedPlazoSolicitadoAprobEdit:::"+renderedPlazoSolicitadoAprobEdit);
		LOG.info("Plazo Solicitado:::"+expediente.getExpedienteTC().getPlazoSolicitado());
		
	}
	
	private void validarVisualizacionDatos(Expediente expediente) {
		String nombJSP = getNombreJSPPrincipal();
		
		if (nombJSP.equals("formAprobarExpediente") || nombJSP.equals("formRevisarRegistrarDictamen") || 
				nombJSP.equals("formEjecutarEvalCrediticia")){
			renderedLineaCredAprob = true;
			// Plazo solicitado aprobado solo debe mostrarse en producto PLD
			if (expediente.getProducto().getId() == Constantes.ID_APLICATIVO_PLD) {
				renderedPlazoSolicitadoApr = true;
			} else {
				renderedPlazoSolicitadoApr = false;
			}
		}else{
			if (expediente.getExpedienteTC().getLineaCredAprob() == 0.0) {
				renderedLineaCredAprob = false;
			} else {
				renderedLineaCredAprob = true;
			}
			LOG.info("valor de renderedPlazoSolicAprob despues de ObtenerDatos::"+renderedPlazoSolicAprob);
			LOG.info("valor de renderedPlazoSolicitadoApr::"+renderedPlazoSolicitadoApr);
			if(renderedPlazoSolicitadoApr)
				if (expediente.getExpedienteTC().getPlazoSolicitadoApr()==null || expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals("")) {
					renderedPlazoSolicitadoApr = false;
				} else {
					renderedPlazoSolicitadoApr = true;
				}			
			
		}

	}

	private void iniciarArchivarExpediente() {
		renderedTe = false;
		renderedSc = true;
		renderedDe = false;
		//renderedCredApro = false;
		//renderedTMCredApro = false;
		renderedTasaEsp = false;
		habCredApro = false;
		habTipoMonedaCredApro = false;
		habCheckTe = true;
		habCheckSc = true;
		renderedWfTc = false;
		renderedPlazoSolicitadoApr = false;
		habPlazoSolApr = false;
		renderedNumTarjetaVer = true; 
		renderedNumContrVer = true;			
		
		
		renderedNumContr = true;	
		renderedNumTarjetaT = true;	
		renderedNumTarjeta = true;
		renderedNumContrT = true;

		renderedCredApro = true;
		renderedTMCredApro = true;		
	}
	
	private void iniciarformSolicitarVerificacionDomiciliaria() {
		renderedTe = false;
		renderedSc = false;
		renderedDe = false;
		//renderedCredApro = false;
		//renderedTMCredApro = false;
		renderedTasaEsp = true;
		habCredApro = true;
		habTipoMonedaCredApro = true;
		renderedPlazoSolicitadoApr = false;
		habPlazoSolApr = true;
		
		renderedCredApro = true;
		renderedTMCredApro = true;	
	}
	
	private void iniciarEvaluarFactibilidadOp() {
		renderedTe = true;
		renderedSc = true;			
		renderedTasaEsp = false;
		habCheckTe = true;
		habCheckSc = true;		
		renderedWfTc = false;
		renderedDe = false;
		ExpedienteDTO objExpedienteDTO=new ExpedienteDTO();
		objExpedienteDTO=mapearExpedienteAExpedienteDTO();
		AyudaVerificacionExp ayudaVerificacionExp=null;
		/*Obtener datos Empleado*/		
		Empleado empleado = empleadoBean.buscarPorId(expediente.getEmpleado().getId());
				
		String nombJSP = getNombreJSPPrincipal();
		boolean validaDelegacion=true;
		
		if (nombJSP.equals("formEjecutarEvalCrediticia")) {
		  	// Comentado el 02 de Mayo 14 ,  validaDelegacion = facade.ServiceIBMBBVA_delegacionRiesgosWS(Integer.valueOf(String.valueOf(empleado.getTipoCategoria().getId())), expediente.getId());
			/*INICIO REQUERIMIENTO 292 17/11/2014*/
			LOG.info("SERVICIO DELEGACION RIESGO");
			if (objExpedienteDTO!=null){
				LOG.info("objExpedienteDTO no nulo");
				LOG.info("Validacion de tipo de cambio.");
				if (String.valueOf(objExpedienteDTO.getCodigoTipoMonedaSol()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
					if(verificarTipoCambio() == null){
						LOG.info("NO hay tipo de cambio, no puede seguir el flujo.");
						msjOperacion292="No Existe Tipo de Cambio el d�a de hoy.";
						msjOperacionBol292=true;
					}
				}
			}
			/*FIN  REQUERIMIENTO 292 17/11/2014*/
			validaDelegacion = facade.ServiceIBMBBVA_delegacionRiesgosWS(Integer.valueOf(String.valueOf(empleado.getTipoCategoria().getId())), expediente.getId());
			expediente.getExpedienteTC().setFlagDelegacion(validaDelegacion?"1":"0");
		}else{
			if(nombJSP.equals("formAprobarExpediente")){
				ayudaVerificacionExp=new AyudaVerificacionExp();
				LOG.info("SERVICIO DELEGACION OFICINA");
				if(objExpedienteDTO.getCodigoNivel()>0)
					LOG.info("Validacion de tipo de cambio.");
					if (String.valueOf(objExpedienteDTO.getCodigoTipoMonedaSol()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
						if (verificarTipoCambio() == null) {
							LOG.info("NO hay tipo de cambio, no puede seguir el flujo.");
							msjOperacion292 = "No Existe Tipo de Cambio el d�a de hoy.";
							msjOperacionBol292 = true;						
						}
					}
					if(ayudaVerificacionExp.delegacionOficinaValidacion(objExpedienteDTO)){
						//validaDelegacion = facade.ServiceIBMBBVA_delegacionOficinaMemoriaWS(Integer.valueOf(String.valueOf(empleado.getNivel().getId())), expediente.getId());
					 	validaDelegacion = facade.ServiceIBMBBVA_delegacionOficinaMemoriaWS(objExpedienteDTO);
					 	expediente.getExpedienteTC().setFlagDelegacion(validaDelegacion?"1":"0");
					}else{
						LOG.info("Validaci�n Delegacion Oficina es falso");
						msjOperacion="No se encuentra configurado valores de Delegaci�n Oficina";
						msjOperacionBol=true;				
					}
				
		        // si esta dentro de su delegacion
				if (validaDelegacion && expediente!= null) {    
					// habilita campos linea credito aprobada
					habCredApro = true;		
					habTipoMonedaCredApro = false;
					renderedCredApro = true;
					renderedTMCredApro  = true;
					renderedPlazoSolicitadoApr = true;
					habPlazoSolApr = true;
					/*
					 * se realiza lo mismo del boton "Aceptar" de AprobarExpediente para habilitar
					 * el boton "Exclusion de la delegacion"
					 */
					// se obtiene el tipo Scoring
					TipoScoring tipoScoring = expediente.getExpedienteTC().getTipoScoring();
					
					// se obtiene el tipo Oferta
					TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
					
					if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo()) ||
							Constantes.CODIGO_SCORING_APROBADO.equals(tipoScoring.getCodigo())) {
						renderedDe = true;
						habCheckDe = false;
					}
				} else { // no esta dentro de su delegacion
					habCredApro = false;	
					habTipoMonedaCredApro = false;
					renderedCredApro = false;
					renderedTMCredApro  = false;
					renderedPlazoSolicitadoApr = false;
					habPlazoSolApr = false;
					
				}				
			}

		}

	}	
		
	private void iniciarRevisarRegistrarDictamen() {
		renderedTe = false;
		renderedSc = true;
		renderedDe = false;		
		renderedTasaEsp = false;
		renderedCredApro = true;		
		renderedTMCredApro  = true;
		habCredApro = true;
		habTipoMonedaCredApro = true;
		habCheckTe = true;
		habCheckSc = true;
		renderedWfTc = false;
		renderedPlazoSolicitadoApr = true;
		habPlazoSolApr = true;
		renderedPlazoSolicitadoAprobEdit=true;
		//FIX2 ERIKA ABREGU
		disabledPorsEndeudamiento = false;
	}
	
	private void iniciarEjecutarEvalCrediticia() {
		renderedTe = false;
		renderedSc = true;
		renderedDe = false;		
		renderedTasaEsp = false;
		habCheckTe = true;
		habCheckSc = true;
		renderedWfTc = false;
		renderedPlazoSolicitadoAprobEdit=true;
		//FIX2 ERIKA ABREGU
		disabledPorsEndeudamiento = true;
		
		/*Obtener datos Empleado*/		
		Empleado empleado = empleadoBean.buscarPorId(expediente.getEmpleado().getId());
		/*INICIO REQUERIMIENTO 292 17.11.2014*/
		ExpedienteDTO objExpedienteDTO=new ExpedienteDTO();
		objExpedienteDTO=mapearExpedienteAExpedienteDTO();
		if (String.valueOf(objExpedienteDTO.getCodigoTipoMonedaSol()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
			if(verificarTipoCambio() == null){
				LOG.info("NO hay tipo de cambio, no puede seguir el flujo.");
				msjOperacion292="No Existe Tipo de Cambio el d�a de hoy.";
				msjOperacionBol292=true;
			}
		}
		/*FIN REQUERIMIENTO 292 17.11.2014*/
        //if (facade.ServiceIBMBBVA_delegacionRiesgosWS(Integer.valueOf(String.valueOf(empleado.getTipoCategoria().getId())), expediente.getId())) {
		boolean validaDelegacion=facade.ServiceIBMBBVA_delegacionRiesgosWS(Integer.valueOf(String.valueOf(empleado.getTipoCategoria().getId())), expediente.getId());
		expediente.getExpedienteTC().setFlagDelegacion(validaDelegacion?"1":"0");
		//validaDelegacion=true;
		if (validaDelegacion) {	
			renderedCredApro = true;
			renderedTMCredApro  = true;
			habCredApro = true;       
			habTipoMonedaCredApro = true;
			renderedPlazoSolicitadoApr = true;
			habPlazoSolApr = true;
			//FIX2 ERIKA ABREGU
			disabledPorsEndeudamiento = false;
			
			
			
		} else {
        	renderedCredApro = false;
        	renderedTMCredApro  = false;
        	habCredApro = false;
        	habTipoMonedaCredApro = false;
        	renderedPlazoSolicitadoApr = false;
        	habPlazoSolApr = false;
        	//FIX2 ERIKA ABREGU
        	disabledPorsEndeudamiento = true;
		}     
	}
	
	private void iniciarCambiarSituacionTramite() {
		renderedTe = false;
		renderedSc = true;
		renderedDe = false;
		//renderedCredApro = false;
		renderedTMCredApro = false;
		renderedTasaEsp = false;
		habCredApro = false;
		
		habTipoMonedaCredApro = false;
		habCheckTe = true;
		habCheckSc = true;
		renderedPlazoSolicitadoApr = false;
		habPlazoSolApr = false;
		
		renderedCredApro = true;
	}
	
	private void iniciarCambiarSituacionExp() {
		renderedTe = false;
		renderedSc = true;
		renderedDe = false;
		renderedCredApro = false;
		renderedTMCredApro = false;
		renderedTasaEsp = false;
		habCredApro = false;
		habTipoMonedaCredApro = false;
		habCheckTe = true;
		habCheckSc = true;
		renderedPlazoSolicitadoApr = false;
		habPlazoSolApr = false;
	}
	
	private void iniciarRealizarAltaTarjeta() {
		renderedTe = false;
		renderedSc = true;
		renderedDe = false;
	//	renderedCredApro = false;
	//	renderedTMCredApro = false;
		renderedTasaEsp = false;
		habCredApro = false;
		habTipoMonedaCredApro = false;
		habCheckTe = true;
		habCheckSc = true;
		renderedWfTc = false;
		renderedNumContrVer = true;
		renderedNumTarjetaVer = true; 	
		renderedNumContrT = true;
		renderedNumContr = true;
		renderedNumTarjetaT = true;
		renderedNumTarjeta = true;
		renderedPlazoSolicitadoApr = false;
		habPlazoSolApr = false;
		
		renderedCredApro = true;
		renderedTMCredApro = true;
		
	}

	private void iniciarVerificarExpDesestimados() {
		renderedTe = false;
		renderedSc = true;
		renderedDe = false;
		renderedCredApro = false;
		renderedTMCredApro = false;
		renderedTasaEsp = false;
		habCredApro = false;
		habTipoMonedaCredApro = false;
		habCheckTe = true;
		habCheckSc = true;
		renderedWfTc = false;
		renderedPlazoSolicitadoApr = false;
		habPlazoSolApr = false;
	}
	
	private void iniciarVerificarEstadoContratacion() {
		renderedTe = true;
		renderedSc = true;
		renderedDe = false;		
		renderedTasaEsp = false;
		habCredApro = true;
		habTipoMonedaCredApro = true;
		habCheckTe = true;
		habCheckSc = true;
		renderedWfTc = true;
		renderedNumContr = false;
		renderedNumTarjeta = false;
		renderedNumTarjetaT = true;
		habPlazoSolApr = true;
	}

	private void iniciarVerificarConformidadCierreExp() {
		renderedTe = true;
		renderedSc = true;
		renderedDe = false;
		renderedCredApro = false;
		renderedTMCredApro = false;
		renderedTasaEsp = false;
		habCredApro = false;
		habTipoMonedaCredApro = false;
		habCheckTe = true;
		habCheckSc = true;
		renderedNumContr = false;
		renderedNumTarjeta = false;
		renderedNumTarjetaT = true;
		renderedPlazoSolicitadoApr = false;
		habPlazoSolApr = false;
	}
	
	private void iniciarRegistrarAprobResolucion() {
		renderedTe = false;
		renderedSc = true;
		renderedDe = false;
		renderedCredApro = true;
		renderedTMCredApro = false;
		renderedTasaEsp = false;
		habCredApro = true;
		habTipoMonedaCredApro = true;
		renderedNumContr = false;
		habCheckTe = true;
		habCheckSc = true;
		renderedPlazoSolicitadoApr = false;
		habPlazoSolApr = true;
	}

	private void iniciarVerificarResultadoDomiciliaria() {
		renderedTe = false;
		renderedSc = false;
		renderedDe = false;
		renderedCredApro = false;
		renderedCredApro = true;
		renderedTMCredApro= false;
		renderedTasaEsp = false;
		habCredApro = true;
		habTipoMonedaCredApro = true;
		renderedPlazoSolicitadoApr = false;
		habPlazoSolApr = true;
	}
	
	private ExpedienteDTO mapearExpedienteAExpedienteDTO(){
		ExpedienteDTO objExpedienteDTO=new ExpedienteDTO();
		objExpedienteDTO.setCodigoExpediente(expediente.getId());
		objExpedienteDTO.setCodigoProducto(expediente.getProducto().getId());
		objExpedienteDTO.setCodigoTipoMonedaSol(expediente.getExpedienteTC().getTipoMonedaSol().getId());
		//objExpedienteDTO.setCodigoTipoScoring(expediente.getExpedienteTC().getTipoScoring().getId());
		objExpedienteDTO.setPlazoSolicitado(expediente.getExpedienteTC().getPlazoSolicitado()!=null && 
				!expediente.getExpedienteTC().getPlazoSolicitado().trim().equals("")? expediente.getExpedienteTC().getPlazoSolicitado().trim(): "");
		LOG.info("PLAZO SOLICITADO = "+objExpedienteDTO.getPlazoSolicitado());
		
		if(expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoScoring()!=null){
			objExpedienteDTO.setCodigoTipoScoring(expediente.getExpedienteTC().getTipoScoring().getId());
		}else
			LOG.info("Expediente no tiene Scoring");
		
		objExpedienteDTO.setCodigoNivel(Long.parseLong("0"));
		
		Oficina objOficina=null;
		//objExpedienteDTO.setCodigoNivel(expediente.getEmpleado().getNivel().getId());
		if(expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getOficina()!=null){
			LOG.info("Buscando Gerente activo para la Oficina con Id "+expediente.getExpedienteTC().getOficina().getId());
			List<Empleado> listEmpleado = empleadoBean.buscarGerenteActivoPorOficinaPerfil(expediente.getExpedienteTC().getOficina().getId(), Constantes.PERFIL_GERENTE_OFICINA);

			/**
			 * Cambio 14 Abril 2015 
			 * Oficinas Desplazadas
			 * */			
			if(listEmpleado==null || listEmpleado.size()<1){
				LOG.info("Verificando si es oficina Desplazada:::: Id oficina::"+expediente.getExpedienteTC().getOficina().getId());
				objOficina = oficinaBean.buscarPorId(expediente.getExpedienteTC().getOficina().getId());
				
				if(objOficina!=null &&  objOficina.getFlagDesplazada()!=null && !objOficina.getFlagDesplazada().trim().equals("") && 
						objOficina.getFlagDesplazada().trim().equals(Constantes.FLAG_ACTIVO)){
					LOG.info("Id oficina::"+objOficina.getId()+":::Flag Desplazada:::"+objOficina.getFlagDesplazada());
					if(objOficina.getOficinaPrincipal()!=null){
						LOG.info("Id oficina::"+objOficina.getId()+":::tiene como Oficina Principal a Oficina con id:::"+objOficina.getOficinaPrincipal().getId());
						listEmpleado = empleadoBean.buscarGerenteActivoPorOficinaPerfil(objOficina.getOficinaPrincipal().getId(), Constantes.PERFIL_GERENTE_OFICINA);
					}else{
						LOG.info("Id oficina Principal::"+objOficina.getId()+"::: No tiene configurado su Oficina Principal");
					}
					
				}else
					LOG.info("Id oficina::"+objOficina.getId()+":::Flag Desplazada:::"+objOficina.getFlagDesplazada());
			}
			/**
			 **/
			
			if(listEmpleado!=null && listEmpleado.size()>0){
				if(listEmpleado.get(0).getNivel()!=null){
					objExpedienteDTO.setCodigoNivel(listEmpleado.get(0).getNivel().getId());
					LOG.info("Nivel de Empleado con id "+listEmpleado.get(0).getId()+" es "+objExpedienteDTO.getCodigoNivel());
					msjOperacionBol=false;
				}else{
					LOG.info("Nivel de Empleado con id "+listEmpleado.get(0).getId()+" no esta configurado");
					msjOperacion="No se encuentra configurado el Nivel del Empleado "+listEmpleado.get(0).getNombresCompletos()+" perteneciente a la Oficina "+expediente.getEmpleado().getOficina().getDescripcion();
					msjOperacionBol=true;						
				}					
			}else{
				LOG.info("No existe Gerente Activo");
				Nivel objNivel=nivelBean.sinNivel(Constantes.NIVEL_SIN_NIVEL);
				if(objNivel!=null){
					objExpedienteDTO.setCodigoNivel(objNivel.getId());
					//msjOperacion="No se encuentra Gerente Activo para la Oficina "+expediente.getEmpleado().getOficina().getDescripcion()+", Nivel a considerar : "+objNivel.getDescripcion();
					/**
					 * Cambio 14 Abril 2015 
					 * Oficinas Desplazadas
					 * */
					if(objOficina!=null && objOficina.getFlagDesplazada()!=null && !objOficina.getFlagDesplazada().trim().equals("") && 
							objOficina.getFlagDesplazada().trim().equals(Constantes.FLAG_ACTIVO)){
						msjOperacion="No Existe Gerente Activo para: \n * Oficina Desplazada : "+expediente.getExpedienteTC().getOficina().getCodigo()+" - "+expediente.getExpedienteTC().getOficina().getDescripcion().toUpperCase();
						if(objOficina.getOficinaPrincipal()!=null)
							msjOperacion+="\n  * Oficina Principal : "+objOficina.getOficinaPrincipal().getId()+" - "+objOficina.getOficinaPrincipal().getDescripcion();
						else{
							msjOperacion+="\n - Oficina Desplazada "+expediente.getExpedienteTC().getOficina().getCodigo()+" - "+expediente.getExpedienteTC().getOficina().getDescripcion().toUpperCase()+" no tiene configurado su Oficina Principal";
						}
						
						msjOperacion+="\n - Nivel a considerar : "+objNivel.getDescripcion();	
						
					}else{
						msjOperacion="No se encuentra Gerente Activo para: \n * Oficina : "+expediente.getExpedienteTC().getOficina().getCodigo()+" - "+expediente.getExpedienteTC().getOficina().getDescripcion()+", Nivel a considerar : "+objNivel.getDescripcion();
					}
					/**
					 * */					
					msjOperacionBol=true;						
				}else{
					msjOperacion="No existe configuraci�n 'Sin Nivel'";
					msjOperacionBol=true;						
				}					
			}
		}else{
			LOG.info("Empleado o Oficina es nulo");
			msjOperacionBol=true;	
		}

		//objExpedienteDTO.setCodigoNivel(empleadoSession.getNivel().getId());
		objExpedienteDTO.setCodigoEstadoCivilTitular(expediente.getClienteNatural().getEstadoCivil().getId());
		if(expediente.getExpedienteTC().getClasificacionBanco()!=null){
			objExpedienteDTO.setClasificacionBanco(expediente.getExpedienteTC().getClasificacionBanco().getId());
		}		
		objExpedienteDTO.setClasificacionSbs(expediente.getExpedienteTC().getClasificacionSbs());
		objExpedienteDTO.setLineaConsumo(expediente.getExpedienteTC().getLineaConsumo());
		
		objExpedienteDTO.setLineaCredAprob(expediente.getExpedienteTC().getLineaCredAprob());
		LOG.info("Valor de linea cred aprobada = "+objExpedienteDTO.getLineaCredAprob());
		
		objExpedienteDTO.setLineaCredSol(expediente.getExpedienteTC().getLineaCredSol());
		LOG.info("LineaCredSol = "+objExpedienteDTO.getLineaCredSol());
		
		objExpedienteDTO.setPerExpPub(expediente.getClienteNatural().getPerExpPub());
		objExpedienteDTO.setPorcentajeEndeudamiento(expediente.getExpedienteTC().getPorcentajeEndeudamiento());
		objExpedienteDTO.setRiesgoCliente(expediente.getExpedienteTC().getRiesgoCliente());
		objExpedienteDTO.setPlazoSolicitado(expediente.getExpedienteTC().getPlazoSolicitado()!=null && 
				!expediente.getExpedienteTC().getPlazoSolicitado().trim().equals("")? expediente.getExpedienteTC().getPlazoSolicitado().trim(): "");
		if(objExpedienteDTO.getCodigoEstadoCivilTitular().equals(Constantes.EST_CIVIL_CASADO)){
			objExpedienteDTO.setBancoConyuge(expediente.getExpedienteTC().getBancoConyuge()==null?null:expediente.getExpedienteTC().getBancoConyuge().getId());
			objExpedienteDTO.setSbsConyuge(expediente.getExpedienteTC().getSbsConyuge());
		}
		
		return objExpedienteDTO;
	}
	private void iniciarAprobarExpediente() {
		LOG.info("metodo iniciarAprobarExpediente de MB");
		renderedTe = false;
		renderedSc = true;	
		renderedCredApro = true;
		renderedTMCredApro  = true;
		renderedTasaEsp = false;
		habCredApro = true;
		habTipoMonedaCredApro = true;
		habCheckTe = true;
		habCheckSc = true;		
		renderedDe = false;
		habCheckDe = false;		
		renderedPlazoSolicitadoApr = true;
		habPlazoSolApr = true;
		renderedPlazoSolicitadoAprobEdit=true;
		
		LOG.info("Valor de LineaCredAprob = "+expediente.getExpedienteTC().getLineaCredAprob());

		AyudaVerificacionExp ayudaVerificacionExp=null;
		ExpedienteDTO objExpedienteDTO=mapearExpedienteAExpedienteDTO();
		
		ayudaVerificacionExp=new AyudaVerificacionExp();
		LOG.info("SERVICIO DELEGACION OFICINA");
		if(objExpedienteDTO.getCodigoNivel()>0){
			LOG.info("Nivel a considerar "+objExpedienteDTO.getCodigoNivel());
			if (String.valueOf(objExpedienteDTO.getCodigoTipoMonedaSol()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
				if (verificarTipoCambio() == null) {
					LOG.info("NO hay tipo de cambio, no puede seguir el flujo.");
					msjOperacion292 = "No Existe Tipo de Cambio el d�a de hoy.";
					msjOperacionBol292 = true;						
				}
			}
			if(ayudaVerificacionExp.delegacionOficinaValidacion(objExpedienteDTO)){
				LOG.info("Entra a Servicio");
				boolean validaDelegacion = facade.ServiceIBMBBVA_delegacionOficinaMemoriaWS(objExpedienteDTO);
				expediente.getExpedienteTC().setFlagDelegacion(validaDelegacion?"1":"0");
			//	validaDelegacion=true;
				if (validaDelegacion){
					LOG.info("Dentro de Delegacion Oficina");
					// habilita campos linea credito aprobada
					habCredApro = true;
					habTipoMonedaCredApro = false;
					habCheckDe = true;					
					habPlazoSolApr = true;
					
					/*
					 * se realiza lo mismo del boton "Aceptar" de AprobarExpediente para habilitar
					 * el boton "Exclusion de la delegacion"
					 */			
					// se obtiene el tipo Scoring
					TipoScoring tipoScoring = expediente.getExpedienteTC().getTipoScoring();
								
					// se obtiene el tipo Oferta
					TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
					
					if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo()) ||
							Constantes.CODIGO_SCORING_APROBADO.equals(tipoScoring.getCodigo())) {
						habCheckDe = false;
					}
				
				} else { // no esta dentro de su delegacion
					LOG.info("Fuera de Delegacion Oficina");
					habCredApro = false;
					habTipoMonedaCredApro = false;
					habCheckDe = true;					
					renderedCredApro=false;
					renderedTMCredApro=false;
					renderedPlazoSolicitadoApr = false;
					habPlazoSolApr = false;
				}			
			}else{
				LOG.info("Validaci�n Delegacion Oficina es falso");
				msjOperacion="No se encuentra configurado valores de Delegaci�n oficina";
				msjOperacionBol=true;				
			}			
		}


	}	
	
	public void obtenerDatos() {
		
		/*Obtiene Datos de Expediente*/
		lineaCreditoOriginal = expediente.getExpedienteTC().getLineaCredAprob();
		
		/*Obtiene Datos de Cliente*/
		clienteNatural= new ClienteNatural();
		if (expediente.getClienteNatural().getId() != 0) {
			clienteNatural= expediente.getClienteNatural();
		}
		
		/**
		 * Cambio de James 28/04/14
		 * */
		/*Obtiene Datos de Producto*/
		/*producto= new Producto();
		if (expediente.getProducto().getId() != 0) {
			producto= expediente.getProducto();
		}*/
		
		
		/**
		 * Fix2 de Erika Abregu
		 * */
		/*Obtiene Porcentaje de Endeudamiento*/
		porcentEndeudaCambiado = expediente.getExpedienteTC().getPorcentajeEndeudamiento()!=0?expediente.getExpedienteTC().getPorcentajeEndeudamiento():0.00;
		porcentEndeudaCambiadoOriginal = Double.toString(porcentEndeudaCambiado);
		/** FIN de Fix2 de Erika Abregu * */
		
		producto= new Producto();
		if (expediente.getProducto().getId() != 0) {
			producto= expediente.getProducto();
			if( producto.getId() == Constantes.ID_APLICATIVO_PLD ){
				renderedNumContrVer = false;
				renderedNumTarjetaVer = false; 
			}
		}		
		
		/*Obtiene Datos de Subproducto*/
		subProducto= new Subproducto();
		if (expediente!=null && expediente.getExpedienteTC()!=null && 
				expediente.getExpedienteTC().getSubproducto()!=null && expediente.getExpedienteTC().getSubproducto().getId() != 0) {
			subProducto= expediente.getExpedienteTC().getSubproducto();
		}
		
		/*Obtiene Datos de Tipo de Oferta*/
		tipoOferta= new TipoOferta();
		if (expediente!=null && expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoOferta()!=null && expediente.getExpedienteTC().getTipoOferta().getId() != 0) {
			tipoOferta= expediente.getExpedienteTC().getTipoOferta();
		}
		
		/*Obtiene Datos de Tipo de Envio*/
		tipoEnvio= new TipoEnvio();
		if (expediente.getExpedienteTC().getTipoEnvio()!=null && expediente.getExpedienteTC().getTipoEnvio().getId() != 0) {
			tipoEnvio= expediente.getExpedienteTC().getTipoEnvio();
		}

		/*Obtener la lista de tipo Moneda*/
		listTipoMoneda = Util.crearItems(tipoMonedaBean.buscarTodos(), true, "id", "descripcion");
		
		/*Obtener Datos de Tipo Buro*/
		tipoBuro= new TipoBuro();
		if (expediente.getExpedienteTC().getTipoBuro()!=null && 
				expediente.getExpedienteTC().getTipoBuro().getId() != 0) {
			tipoBuro= expediente.getExpedienteTC().getTipoBuro();
		}
		
		/*Obtener Datos de tipo Scoring*/
		tipoScoring= new TipoScoring();
		if (expediente.getExpedienteTC().getTipoScoring()!=null &&
				expediente.getExpedienteTC().getTipoScoring().getId() != 0) {
			tipoScoring= expediente.getExpedienteTC().getTipoScoring();
		}
		
		/*Obtener Datos de Estado WfTc*/
		estado= new Estado();
		if (expediente.getExpedienteTC().getEstadoEnvWorkflowTc()!=null && expediente.getExpedienteTC().getEstadoEnvWorkflowTc().getId() != 0) {
			estado= expediente.getExpedienteTC().getEstadoEnvWorkflowTc();	
		}
		
		/*Obtiene Datos de Garantia*/
		garantia= new Garantia();
		if (expediente.getExpedienteTC().getGarantia()!=null && expediente.getExpedienteTC().getGarantia().getId() != 0) {
			garantia= expediente.getExpedienteTC().getGarantia();
		}
		
		/*Setear Check*/		
		if (expediente!=null && expediente.getExpedienteTC().getFlagSolTasaEsp()!=null && expediente.getExpedienteTC().getFlagSolTasaEsp().equals(Constantes.CHECK_SELECCIONADO)) {			
			selectedItemTe = true;			
		}
		if (expediente!=null && expediente.getExpedienteTC().getFlagModifScore()!=null && expediente.getExpedienteTC().getFlagModifScore().equals(Constantes.CHECK_SELECCIONADO)) {			
			selectedItemSc = true;
		}
		if (expediente!=null && expediente.getExpedienteTC().getFlagDelegacion()!=null && expediente.getExpedienteTC().getFlagDelegacion().equals(Constantes.CHECK_SELECCIONADO)) {		
			selectedItemDe = true;
		}
		
	    /*Setear Tipo de Moneda*/
		tipoMonedaCreSol= new TipoMoneda();
		tipoMonedaCreApr= new TipoMoneda();
		for (SelectItem list : listTipoMoneda) {			
			if (expediente.getExpedienteTC().getTipoMonedaSol()!=null && expediente.getExpedienteTC().getTipoMonedaSol().getId()!=0 && Long.toString(expediente.getExpedienteTC().getTipoMonedaSol().getId()).equals(list.getValue().toString())){				
				tipoMonedaCreSol.setDescripcion(list.getLabel());
			}
			if (expediente.getExpedienteTC().getTipoMonedaAprob()!=null && expediente.getExpedienteTC().getTipoMonedaAprob().getId()!=0 && Long.toString(expediente.getExpedienteTC().getTipoMonedaAprob().getId()).equals(list.getValue().toString())){				
				tipoMonedaCreApr.setDescripcion(list.getLabel());
			}
		}
		
		if (expediente.getExpedienteTC().getTipoMonedaAprob()!=null && expediente.getExpedienteTC().getTipoMonedaAprob().getId() != 0) {
			tipoMonedaSel = Long.toString(expediente.getExpedienteTC().getTipoMonedaAprob().getId());
		}else{
			tipoMonedaSel = Long.toString(expediente.getExpedienteTC().getTipoMonedaSol().getId());
		}
		
		copiarDeExpediente();
		
		if (expediente.getExpedienteTC().getSbsConyuge() == 0) {
			expediente.getExpedienteTC().setSbsConyuge(0D);
		}	
		
		if(expediente!=null && expediente.getProducto()!=null && expediente.getProducto().getId()>0)
			campoProducto(expediente.getProducto().getId());		
		
		String nombJSP = getNombreJSPPrincipal();
		LOG.info("Pantalla es "+nombJSP );
		if (!nombJSP.equals("formVisualizarExpediente")) {
			//Validamos si tiene permisos de solicitar tasa especial
			if(expediente.getExpedienteTC().getEmpleado() != null && expediente.getExpedienteTC().getEmpleado().getPerfil() != null){
				LOG.info("Perfil es "+empleadoSession.getPerfil().getId() );	
				if(empleadoSession.getPerfil().getId()== Constantes.PERFIL_GESTOR_PLATAFORMA ||
						empleadoSession.getPerfil().getId() == Constantes.ID_PERFIL_CONTROLLER){
					disabledSolicitudTasaEsp = false;
				}else{
					disabledSolicitudTasaEsp = true;
				}
			}
		}else{
			this.disabledSolicitudTasaEsp = true;
		}
		LOG.info("disabledSolicitudTasaEsp es "+disabledSolicitudTasaEsp );
		LOG.info("VALOR DE TASA ES "+expediente.getExpedienteTC().getTasaEsp() );
		
	}
	
	private void campoProducto(long idProd){
		List<ProductoEtiqueta> listProductoEtiqueta=productoEtiquetabean.buscarPorIdProducto(idProd);
		
		/*verificacion Domiciliaria*/
		FacesContext ctx = FacesContext.getCurrentInstance();  
		VerificarAprobarMB solVerificarAprobar = (VerificarAprobarMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");
		
		if(listProductoEtiqueta!=null && listProductoEtiqueta.size()>0){
			for(ProductoEtiqueta value : listProductoEtiqueta){
				if(value.getEtiqueta().equals(Constantes.CAMPO_ENVIO_TARJETA))
					setRenderedEnvioTarjeta(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
				if(value.getEtiqueta().equals(Constantes.CAMPO_PLAZO_SOLICITADO))
					setRenderedPlazoSolicitado(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
				if(value.getEtiqueta().equals(Constantes.CAMPO_PLAZO_SOLICITADO_APR))
					setRenderedPlazoSolicitadoApr(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
				if(value.getEtiqueta().equals(Constantes.CAMPO_SOL_TASA_ESP)){
					setRenderedSolicitudTasaEsp(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
					/*
					if(expediente.getEmpleado().getPerfil().getId()==Constantes.ID_PERFIL_CONTROLLER || 
							expediente.getEmpleado().getPerfil().getId()==Constantes.ID_PERFIL_ANALISIS_Y_ALTAS){

					}else{
						setRenderedSolicitudTasaEsp(false);
					}*/					
				}
					
				if(value.getEtiqueta().equals(Constantes.CAMPO_GARANTIA))
					setRenderedGarantia(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);				
				//if(value.getEtiqueta().equals(Constantes.CAMPO_DPS))
				//	solVerificarAprobar.setVerDPS(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
			}
		}
	}	
	
	public void actTextoTasaespecial(AjaxBehaviorEvent event) {
		disabledSolicitudTasaEsp=false;		
	}	

	private void copiarAExpediente() {
		/* Copia string formateados a campos numericos */
		LOG.info("copiarAExpediente()");
		lineaCredSol = Double.toString(expediente.getExpedienteTC().getLineaCredSol());
		lineaCredAprob = Double.toString(expediente.getExpedienteTC().getLineaCredAprob());
		
		expediente.getExpedienteTC().setLineaCredSol(Util.convertStringToDouble(Util.esNuloVacio(lineaCredSol) ? "0" : lineaCredSol));
		expediente.getExpedienteTC().setLineaCredAprob(Util.convertStringToDouble(Util.esNuloVacio(lineaCredAprob) ? "0" : lineaCredAprob));
	//	expediente.getExpedienteTC().setTasaEsp(Util.convertStringToDouble(Util.esNuloVacio(tasaEsp) ? "0" : tasaEsp));

		
		if(expediente!=null && expediente.getExpedienteTC()!=null )
			LOG.info("VALOR DE TASA ES 4 "+expediente.getExpedienteTC().getTasaEsp() );
		else
			LOG.info("null 4");		
		
		
	}
	
	private void copiarDeExpediente() {
		/* Mostrar string datos numericos formateados */
		LOG.info("copiarDeExpediente()");
		setLineaCredSol(expediente.getExpedienteTC().getLineaCredSol()!=0?Util.convertDoubleToString(expediente.getExpedienteTC().getLineaCredSol(),Constantes.FORMATO_DECIMAL_MILLON):"");
		String nombJSP = getNombreJSPPrincipal();
		if (!nombJSP.equals("formAprobarExpediente")) {
			//setLineaCredAprob(Util.convertDoubleToString(0,Constantes.FORMATO_DECIMAL_MILLON));
			setLineaCredAprob("");
		}else
			//setLineaCredAprob(expediente.getExpedienteTC().getLineaCredAprob()!=0?Util.convertDoubleToString(expediente.getExpedienteTC().getLineaCredAprob(),Constantes.FORMATO_DECIMAL_MILLON):getLineaCredSol());
			setLineaCredAprob(expediente.getExpedienteTC().getLineaCredAprob()!=0?Util.convertDoubleToString(expediente.getExpedienteTC().getLineaCredAprob(),Constantes.FORMATO_DECIMAL_MILLON):"");
		
	    setTasaEsp(expediente.getExpedienteTC().getTasaEsp()!=0?Util.convertDoubleToString(expediente.getExpedienteTC().getTasaEsp(),Constantes.FORMATO_DECIMAL_DECENAS):"");
	    
		//Agregado por Emitson
		//expediente.getExpedienteTC().setLineaCredAprob(expediente.getExpedienteTC().getLineaCredSol());
	}
	
//	public boolean validarNumeroContrato(String numContrato) {
//
//		boolean flagOK=false;
//		String formulario = getNombreJSPPrincipal();
//		
//		if (formulario.equals("formRealizarAltaTarjeta")) {
//			formulario="frmRealizarAltaTarjeta";
//		}
//		
//		 Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION); 
//
//		 CtVerificarExisteContratoNACARRq inVerificarExisteContratoNACARR=null;
//
//		 CtVerificarExisteContratoNACARRs outVerificarExisteContratoNACARR=null;
//		 CtHeader headRq = null;
//		 CtBodyRq bodyRq = null;
//		 inVerificarExisteContratoNACARR = new CtVerificarExisteContratoNACARRq();	
//		 outVerificarExisteContratoNACARR = new CtVerificarExisteContratoNACARRs();
//		 headRq = new CtHeader();
//		 bodyRq = new CtBodyRq();
//		 headRq.setUsuario(empleado.getCodigo());
//		 bodyRq.setNumeroContrato(numContrato);
//		 bodyRq.setCodigoCentral("");
//
//		 inVerificarExisteContratoNACARR.setHeader(headRq);
//		 inVerificarExisteContratoNACARR.setData(bodyRq);
//		 LOG.info("-------------------------- INICIO CONSULTA AL SERVICIO NUMERO CONTRATO ----------------------------");	
//		 
//		 try{
//			 LOG.info("DATOS REQUEST SERVICIO NUM CONTRATO: " +
//			 		"["+ "Usuario="+headRq.getUsuario() +"; " 
//					   + "NumContrato="+bodyRq.getNumeroContrato() +"]");
//			 
//			 outVerificarExisteContratoNACARR = bbvaFacade.SCEPE20_verificarExisteContratoNACAR(inVerificarExisteContratoNACARR);
//			 
//		 }catch(Throwable e){
//			 LOG.error("Ocurrio un error de conexion con el servicio num contrato "+ e);
//		 }
//		 LOG.info("-------------------------- FIN CONSULTA AL SERVICIO NUMERO CONTRATO ----------------------------");
//		
//		 if(outVerificarExisteContratoNACARR!=null && outVerificarExisteContratoNACARR.getHeader()!=null  && outVerificarExisteContratoNACARR.getHeader().getCodigo()!=null && 
//				 !outVerificarExisteContratoNACARR.getHeader().getCodigo().equals(Constantes.CODIGO_EXITO_NUM_CONTRATO)){
//			 LOG.info("Resultado:" + "["+ outVerificarExisteContratoNACARR.getHeader().getCodigo()+"] -" + "No existen datos en Servicio Num Contrato...");
//		 }else{
//			 if(outVerificarExisteContratoNACARR!=null && outVerificarExisteContratoNACARR.getHeader()!=null)
//				 LOG.info("Resultado:" + "["+ outVerificarExisteContratoNACARR.getHeader().getCodigo()+"] -" + "Consulta exitosa de Servicio Num Contrato...");
//		 }
//			 
//		 if(outVerificarExisteContratoNACARR!=null && outVerificarExisteContratoNACARR.getHeader()!=null && outVerificarExisteContratoNACARR.getHeader().getCodigo()!=null &&
//				 outVerificarExisteContratoNACARR.getHeader().getCodigo().equals(Constantes.CODIGO_EXITO_NUM_CONTRATO)){
//				 
//			 LOG.info("DATOS RESPONSE SERVICIO NUM CONTRATO: " +	"["+"Num Contrato="+outVerificarExisteContratoNACARR.getHeader().getDescripcion()+" "+outVerificarExisteContratoNACARR.getHeader().getAviso()+"]");
//
//			 flagOK = true;
//   			  
//		 }else{
//			 flagOK = false;
//			 addMessageError(formulario + ":numContrato", 
//						"com.ibm.bbva.common.datosProducto3.msg.numContrato.valida");			 
//		 }
//			 LOG.info("valida servicio nro contrato : "+formulario + ":numContrato");
//			 
//		 outVerificarExisteContratoNACARR=null;
//		 inVerificarExisteContratoNACARR=null;
//		
//		 return flagOK;
//
//		
//	}	
	
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto= producto;
	}

	public Subproducto getSubProducto() {
		return subProducto;
	}

	public void setSubProducto(Subproducto subProducto) {
		this.subProducto= subProducto;
	}

	public TipoOferta getTipoOferta() {
		return tipoOferta;
	}

	public void setTipoOferta(TipoOferta tipoOferta) {
		this.tipoOferta= tipoOferta;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente= expediente;
	}

	public TipoEnvio getTipoEnvio() {
		return tipoEnvio;
	}

	public void setTipoEnvio(TipoEnvio tipoEnvio) {
		this.tipoEnvio= tipoEnvio;
	}
	
	public TipoBuro getTipoBuro() {
		return tipoBuro;
	}

	public void setTipoBuro(TipoBuro tipoBuro) {
		this.tipoBuro= tipoBuro;
	}

	public TipoScoring getTipoScoring() {
		return tipoScoring;
	}

	public void setTipoScoringVO(TipoScoring tipoScoring) {
		this.tipoScoring= tipoScoring;
	}
	
	public List<SelectItem> getListTipoMoneda() {
		return listTipoMoneda;
	}

	public void setListTipoMoneda(List<SelectItem> listTipoMoneda) {
		this.listTipoMoneda = listTipoMoneda;
	}
	
	public String getTipoMonedaSel() {
		return tipoMonedaSel;
	}

	public void setTipoMonedaSel(String tipoMonedaSel) {
		this.tipoMonedaSel = tipoMonedaSel;
	}

	public ClienteNatural getClienteNatural() {
		return clienteNatural;
	}

	public void setClienteNatural(ClienteNatural clienteNatural) {
		this.clienteNatural= clienteNatural;
	}

	public boolean isSelectedItemTe() {
		return selectedItemTe;
	}

	public void setSelectedItemTe(boolean selectedItemTe) {
		this.selectedItemTe = selectedItemTe;
	}

	public boolean isSelectedItemSc() {
		return selectedItemSc;
	}

	public void setSelectedItemSc(boolean selectedItemSc) {
		this.selectedItemSc = selectedItemSc;
	}

	public boolean isSelectedItemDe() {
		return selectedItemDe;
	}

	public void setSelectedItemDe(boolean selectedItemDe) {
		this.selectedItemDe = selectedItemDe;
	}

	public boolean isRenderedTe() {
		return renderedTe;
	}

	public void setRenderedTe(boolean renderedTe) {
		this.renderedTe = renderedTe;
	}

	public boolean isRenderedSc() {
		return renderedSc;
	}

	public void setRenderedSc(boolean renderedSc) {
		this.renderedSc = renderedSc;
	}

	public boolean isRenderedDe() {
		return renderedDe;
	}

	public void setRenderedDe(boolean renderedDe) {
		this.renderedDe = renderedDe;
	}

	public boolean isRenderedCredApro() {
		return renderedCredApro;
	}

	public void setRenderedCredApro(boolean renderedCredApro) {
		this.renderedCredApro = renderedCredApro;
	}

	public boolean isRenderedPlazoSolicitado() {
		return renderedPlazoSolicitado;
	}

	public void setRenderedPlazoSolicitado(boolean renderedPlazoSolicitado) {
		this.renderedPlazoSolicitado = renderedPlazoSolicitado;
	}
	
	public TipoMoneda getTipoMonedaCreSol() {
		return tipoMonedaCreSol;
	}

	public void setTipoMonedaCreSol(TipoMoneda tipoMonedaCreSol) {
		this.tipoMonedaCreSol= tipoMonedaCreSol;
	}

	public TipoMoneda getTipoMonedaCreApr() {
		return tipoMonedaCreApr;
	}

	public void setTipoMonedaCreApr(TipoMoneda tipoMonedaCreApr) {
		this.tipoMonedaCreApr= tipoMonedaCreApr;
	}

	public boolean isRenderedTasaEsp() {
		return renderedTasaEsp;
	}

	public void setRenderedTasaEsp(boolean renderedTasaEsp) {
		this.renderedTasaEsp = renderedTasaEsp;
	}

	public boolean isHabCredApro() {
		return habCredApro;
	}

	public void setHabCredApro(boolean habCredApro) {
		this.habCredApro = habCredApro;
	}

	public boolean isHabPlazoSolApr() {
		return habPlazoSolApr;
	}

	public void setHabPlazoSol(boolean habPlazoSolApr) {
		this.habPlazoSolApr = habPlazoSolApr;
	}
	
	public Double getLineaCreditoOriginal() {
		return lineaCreditoOriginal;
	}

	public void setLineaCreditoOriginal(Double lineaCreditoOriginal) {
		this.lineaCreditoOriginal = lineaCreditoOriginal;
	}

	public boolean esValido () {		
		boolean existeError = false;
		String idComponente = "";
		
		if(expediente!=null && expediente.getExpedienteTC()!=null )
			LOG.info("VALOR DE TASA ES 3 "+expediente.getExpedienteTC().getTasaEsp() );
		else
			LOG.info("null 3");
		
		copiarAExpediente();
		
		String jspPrinc = getNombreJSPPrincipal();
		LOG.info("jspPrinc = "+jspPrinc);
		if (jspPrinc.equals("formAprobarExpediente")) {
			existeError = validarAprobarExpediente();
		} else if (jspPrinc.equals("formVerificarResultadoDomiciliaria")) {
			existeError = validarVerificarResultadoDomiciliaria();
		} else if (jspPrinc.equals("formVerificarEstadoContratacion")) {
			existeError = validarVerificarEstadoContratacion();
		} else if (jspPrinc.equals("formEjecutarEvalCrediticia")) {
			existeError = validarEjecutarEvalCrediticia();
		} else if (jspPrinc.equals("formRevisarRegistrarDictamen")) {
			existeError = validarRevisarRegistrarDictamen();	
		} else if (jspPrinc.equals("formEvaluarFactibilidadOp")) {
			existeError = validarEvaluarFactibilidadOp();				
		} else if (jspPrinc.equals("formRealizarAltaTarjeta")) {
			existeError = validarRealizarAltaTarjeta ();
			idComponente="frmRealizarAltaTarjeta";
		} else if (jspPrinc.equals("formSolicitarVerificacionDomiciliaria")) {
			existeError = validarSolicitarVerificacionDomiciliaria();
		} else if (jspPrinc.equals("formCambiarSituacionExp")) {
			existeError = validarCambiarSituacionExp();
		} else if (jspPrinc.equals("formRegistrarAprobResolucion")) {
			existeError = validarRegistrarAprobResolucion();
		}		
		
		/**
		 * Acceder al algoritmo que valida que el Num Cta / Contrato sean validos.
		 * 09-Marzo-2015
		 * */
	/*	if(idComponente.equals("frmRealizarAltaTarjeta")){
			String valorNumCuenta = expediente.getExpedienteTC().getNroContrato();			
			if (valorNumCuenta == null || valorNumCuenta.trim().equals("") || valorNumCuenta.length()<20) {
				addMessageError(idComponente + ":numContrato",
						"com.ibm.bbva.common.datosProducto3.msg.numContrato");
				existeError = true;
			}else if (valorNumCuenta.length()==20){
				if(!Util.validaCuenta(valorNumCuenta)){
				addMessageError(idComponente + ":numContrato",
						"com.ibm.bbva.common.datosProducto3.msg.numeroContratoNoValido");
				existeError = true;
				}
			}			
		}*/

		//if (expediente.getProducto().getDescripcion().equals(Constantes.NOMBRE_TIPO_PRODUCTO_PLD)){

		//}
		/**
		 * Fin de cambio 09-Marzo-2015
		 * */		
			

				//Validacion de nro de contrato
				msgPersonalizado = null;
				if(productoTareaBean.buscarPorIdTareaIdProducto(Long.valueOf(expediente.getProducto().getCodigo()), 
						Long.valueOf(expediente.getExpedienteTC().getTarea().getCodigo())) != null)
				{
					existeError = !validacionNumeroContradoPackPyme();
				}				
			

		return !existeError;
	}
	
	private String formatearCuentaContrato(String cta){
		String result="";
		if (cta!=null && cta.length()==20 ) {
			String strEntidad=cta.substring(0,4);
			LOG.info("strEntidad:::"+strEntidad);
			String strOficina=cta.substring(4,8);
			LOG.info("strOficina:::"+strOficina);
			String strDigitoChequeo=cta.substring(8,10);
			LOG.info("strDigitoChequeo:::"+strDigitoChequeo);
			String strNumeroCuenta=cta.substring(10,cta.length());
			LOG.info("strNumeroCuenta:::"+strNumeroCuenta);
			
			result=strEntidad+"-"+strOficina+"-"+strDigitoChequeo+"-"+strNumeroCuenta;
						 
		}
		LOG.info("result::::"+result);
		return result;
	}
	
	
	/**
	 * Validacion de nro de contrato PackPyme
	 * @return
	 */
	private boolean validacionNumeroContradoPackPyme(){
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		msgPersonalizado = UtilWebService.validacionNumeroContradoClientePackPyme(bbvaFacade, mensajeBean, expediente, empleado.getCodigo(), getIdSession());
		return msgPersonalizado == null;
	}
	
	private boolean validarSolicitarVerificacionDomiciliaria() {
		boolean existeError = false;
		String formulario = "frmSolicitarVerificacionDomiciliaria";
	
		if (selectedItemTe && !disabledSolicitudTasaEsp) {
			/*Obtiene Datos de Expediente*/		    	
			if (expediente.getExpedienteTC().getTasaEsp() == 0){
				addMessageError(formulario + ":chkTasaEspecial", 
				"com.ibm.bbva.common.datosProducto3.msg.tasaEsp");
		        existeError = true;
			}		    	
		}
		return existeError;
	}
	
	private boolean validarCambiarSituacionExp() {
		boolean existeError = false;
		String formulario = "frmCambiarSituacionExp";
	
		LOG.info("RenderedSolicitudTasaEsp = "+isRenderedSolicitudTasaEsp());
		LOG.info("tasaEspecial = "+selectedItemTe);
		
		if (isRenderedSolicitudTasaEsp() && selectedItemTe && !disabledSolicitudTasaEsp) {
			/*Obtiene Datos de Expediente*/		    	
			if (expediente.getExpedienteTC().getTasaEsp() <= 0){
				addMessageError(formulario + ":idValorTasaEsp3", 
				"com.ibm.bbva.common.productoNuevo.msg.plazoSolicitado");
		        existeError = true;
			}		    	
		}
		

		/*if (isRenderedSolicitudTasaEsp() && selectedItemTe && !disabledSolicitudTasaEsp) {
			if(expediente.getExpedienteTC().getTasaEsp()<=0){
				addMessageError(formulario + ":idValorTasaEsp", 
						"com.ibm.bbva.common.productoNuevo.msg.porcentajeTasaEspecial");
				existeError = true;
				LOG.info("entro 3= ");
			}
		}*/	
		
		return existeError;
	}
	
	private boolean validarRegistrarAprobResolucion() {
		boolean existeError = false;
		String formulario = "frmRegistrarAprobResolucion";
	
		LOG.info("RenderedSolicitudTasaEsp = "+isRenderedSolicitudTasaEsp());
		LOG.info("tasaEspecial = "+selectedItemTe);
		
		if (isRenderedSolicitudTasaEsp() && selectedItemTe && !disabledSolicitudTasaEsp) {
			/*Obtiene Datos de Expediente*/		    	
			if (expediente.getExpedienteTC().getTasaEsp() <= 0){
				addMessageError(formulario + ":idValorTasaEsp3", 
				"com.ibm.bbva.common.productoNuevo.msg.plazoSolicitado");
		        existeError = true;
			}		    	
		}
		

		/*if (isRenderedSolicitudTasaEsp() && selectedItemTe && !disabledSolicitudTasaEsp) {
			if(expediente.getExpedienteTC().getTasaEsp()<=0){
				addMessageError(formulario + ":idValorTasaEsp", 
						"com.ibm.bbva.common.productoNuevo.msg.porcentajeTasaEspecial");
				existeError = true;
				LOG.info("entro 3= ");
			}
		}*/	
		
		return existeError;
	}
	
	private boolean validarEvaluarFactibilidadOp() {
		boolean existeError = false;
		String formulario = "frmEvaluarFactibilidadOp";
			
		if (habCredApro) {			
			if (tipoMonedaSel == null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(tipoMonedaSel)) {
				addMessageError(formulario + ":selectTipoMoneda", 
						"com.ibm.bbva.common.datosProducto3.msg.tipoMoneda");
				existeError = true;
			}
			Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
			if (linCredAprob == null || linCredAprob.doubleValue() <= 0) {
				addMessageError(formulario + ":lineaCredAprob", 
						"com.ibm.bbva.common.datosProducto3.msg.linCredAprobMenor");
				existeError = true;
			} else if (lineaCreditoOriginal != null && lineaCreditoOriginal > 0 && 
					linCredAprob.doubleValue() > lineaCreditoOriginal.doubleValue()) {
				addMessageError(formulario + ":lineaCredAprob", 
						"com.ibm.bbva.common.datosProducto3.msg.linCredAprobMayor", lineaCreditoOriginal);
				existeError = true;
			}
		}	
			
		return existeError;
	}
	
	private boolean validarRevisarRegistrarDictamen() {
		boolean existeError = false;
		String formulario = "frmRevisarRegistrarDictamen";
		
		String accion = (String) getObjectSession(Constantes.ACCION_SESION);	
		if (accion==null) accion="";
		
		if (habCredApro) {			
			if (tipoMonedaSel == null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(tipoMonedaSel)) {
				addMessageError(formulario + ":selectTipoMoneda", 
						"com.ibm.bbva.common.datosProducto3.msg.tipoMoneda");
				existeError = true;
			}
			/*
			Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
			if (linCredAprob == null || linCredAprob.doubleValue() <= 0) {
				addMessageError(formulario + ":lineaCredAprob", 
						"com.ibm.bbva.common.datosProducto3.msg.linCredAprobMenor");
				existeError = true;
			} else if (lineaCreditoOriginal != null && lineaCreditoOriginal > 0 && 
					linCredAprob.doubleValue() > lineaCreditoOriginal.doubleValue()) {
				addMessageError(formulario + ":lineaCredAprob", 
						"com.ibm.bbva.common.datosProducto3.msg.linCredAprobMayor", lineaCreditoOriginal);
				existeError = true;
			}*/
			long plazoSol = ((expediente.getExpedienteTC().getPlazoSolicitado()==null || expediente.getExpedienteTC().getPlazoSolicitado().equals(""))?0:Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitado().trim()));
			//long plazoSolApr = ((expediente.getExpedienteTC().getPlazoSolicitadoApr()==null || expediente.getExpedienteTC().getPlazoSolicitadoApr().equals(""))?0:Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitadoApr()));
			double lineaSol = (expediente.getExpedienteTC().getLineaCredSol()>0?expediente.getExpedienteTC().getLineaCredSol():0);
			
			LOG.info("plazoSol -> "+plazoSol);
			
			//Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitadoApr());
			
			 if (accion.equals(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS) || accion.equals(Constantes.ACCION_BOTON_APROBAR_OPERACION)){
				if (plazoSol>0){
					
					//FIX2 ERIKA ABREGU
					DelegacionRiesgoCondicion objDelegacionRiesgoCondPlazo=delegacionRiesgoCondicionBeanLocalBean.buscarPorId(Constantes.CONDICION_PLAZO);
					
					if(objDelegacionRiesgoCondPlazo == null || objDelegacionRiesgoCondPlazo.getSimbolo() == null ||
							"".equals(objDelegacionRiesgoCondPlazo.getSimbolo().getDescripcion())){
						
						addMessageError(formulario + ":idPlazoSolApr", 
								"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprSinCond", plazoSol);
						existeError = true;
					}else {
						if(expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null && !expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals("")){
							long plazoSolApr = (expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null && !expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals("")?Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitadoApr().trim()):0);
							LOG.info("plazoSolApr -> "+plazoSolApr);
							
							if(!"SV".equals(objDelegacionRiesgoCondPlazo.getSimbolo().getDescripcion())){
								String condicion = objDelegacionRiesgoCondPlazo.getSimbolo().getDescripcion();
								LOG.info("plazoSolApr sin condicion SV -> "+plazoSolApr);
								if("<=".equals(condicion)){
									if(!(plazoSolApr > 0 && plazoSol <= plazoSolApr)){
										LOG.info("plazoSolApr con condicion <=  --> "+plazoSolApr);
										addMessageError(formulario + ":idPlazoSolApr", 
												"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprCond_1", plazoSol);
										existeError = true;							
									}
								}else if(">=".equals(condicion)){
									if(!(plazoSolApr > 0 && plazoSol >= plazoSolApr)){
										LOG.info("plazoSolApr con condicion >=  --> "+plazoSolApr);
										addMessageError(formulario + ":idPlazoSolApr", 
												"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprCond_2", plazoSol);
										existeError = true;							
									}
								}else if ("==".equals(condicion)){
									if(!(plazoSolApr > 0 && plazoSol == plazoSolApr)){
										LOG.info("plazoSolApr con condicion ==  --> "+plazoSolApr);
										addMessageError(formulario + ":idPlazoSolApr", 
												"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprCond_3", plazoSol);
										existeError = true;							
									}
								}else{
									LOG.info("plazoSolApr con ninguna condicion -> "+plazoSolApr);
									addMessageError(formulario + ":idPlazoSolApr", 
											"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprSinCond", plazoSol);
									existeError = true;
								}
									
							}else{
								if(!(plazoSolApr > 0)){
									addMessageError(formulario + ":idPlazoSolApr", 
											"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprCero", plazoSol);
									existeError = true;							
								}
							}
							
							/*if(!(plazoSolApr > 0 && plazoSolApr <= plazoSol)){
								addMessageError(formulario + ":idPlazoSolApr", 
										"com.ibm.bbva.common.datosProducto3.msg.plazoSolApr", plazoSol);
								existeError = true;							
							}*/
						}
						
					}
				}
				
				if(lineaSol>0){
					LOG.info("lineaCreditoAprobado :::: "+lineaCreditoAprobado);
					
					//FIX2 ERIKA ABREGU
					DelegacionRiesgoCondicion objDelegacionRiesgoCondImporte=delegacionRiesgoCondicionBeanLocalBean.buscarPorId(Constantes.CONDICION_IMPORTE);
					
					if (objDelegacionRiesgoCondImporte == null || objDelegacionRiesgoCondImporte.getSimbolo() == null ||
							"".equals(objDelegacionRiesgoCondImporte.getSimbolo().getDescripcion())) {
							
						addMessageError(formulario + ":lineaCredAprob", 
								"com.ibm.bbva.common.datosProducto3.msg.linCredAprobSinCond", lineaSol);
						existeError = true;
					
					}else {
						
						if(lineaCreditoAprobado!=null && !lineaCreditoAprobado.equals("")){
							Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
							LOG.info("linCredAprob :::: "+linCredAprob);
						
							if(!"SV".equals(objDelegacionRiesgoCondImporte.getSimbolo().getDescripcion())){
								LOG.info("linCredAprob sin condicion SV -> "+linCredAprob);
								String condicion = objDelegacionRiesgoCondImporte.getSimbolo().getDescripcion();
								
								if("<=".equals(condicion)){
									if (!(linCredAprob.doubleValue()>0 && lineaSol <= linCredAprob.doubleValue() )){
										LOG.info("linCredAprob con condicion <=  :::: "+linCredAprob);
										addMessageError(formulario + ":lineaCredAprob", 
												"com.ibm.bbva.common.datosProducto3.msg.linCredAprobCond_1", lineaSol);
										existeError = true;
									}
								}else if(">=".equals(condicion)){
									if (!(linCredAprob.doubleValue()>0 && lineaSol >= linCredAprob.doubleValue() )){
										LOG.info("linCredAprob con condicion >=  :::: "+linCredAprob);
										addMessageError(formulario + ":lineaCredAprob", 
												"com.ibm.bbva.common.datosProducto3.msg.linCredAprobCond_2", lineaSol);
										existeError = true;
									}
								}else if ("==".equals(condicion)){
									if (!(linCredAprob.doubleValue()>0 && lineaSol == linCredAprob.doubleValue() )){
										LOG.info("linCredAprob con condicion ==  :::: "+linCredAprob);
										addMessageError(formulario + ":lineaCredAprob", 
												"com.ibm.bbva.common.datosProducto3.msg.linCredAprobCond_3", lineaSol);
										existeError = true;
									}
								}else{
									LOG.info("linCredAprob con ninguna condicion :::: "+linCredAprob);
									addMessageError(formulario + ":lineaCredAprob", 
											"com.ibm.bbva.common.datosProducto3.msg.linCredAprobSinCond", lineaSol);
									existeError = true;
								}
								
							}else{
								if (!(linCredAprob.doubleValue()>0)){
									addMessageError(formulario + ":lineaCredAprob", 
											"com.ibm.bbva.common.datosProducto3.msg.linCredAprobCero", lineaSol);
									existeError = true;
								}
							}
							
						}
						
					}
					
					//COMENTADO POR ERIKA ABREGU PARA EL CAMBIO DE FIX2
					/*if(lineaCreditoAprobado!=null && !lineaCreditoAprobado.equals("")){
						Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
						LOG.info("linCredAprob :::: "+linCredAprob);
						if (!(linCredAprob.doubleValue()>0 && linCredAprob.doubleValue()<=lineaSol )){
							addMessageError(formulario + ":lineaCredAprob", 
									"com.ibm.bbva.common.datosProducto3.msg.linCredAprobValidacion", lineaSol);
							existeError = true;
						}
					}*/
				}
			}			

		}
		return existeError;
	}	
	
	private boolean validarEjecutarEvalCrediticia() {
		boolean existeError = false;
		String formulario = "frmEjecutarEvalCrediticia";
		
		String accion = (String) getObjectSession(Constantes.ACCION_SESION);	
		if (accion==null) accion="";
		
		if (habCredApro) {			
			if (tipoMonedaSel == null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(tipoMonedaSel)) {
				addMessageError(formulario + ":selectTipoMoneda", 
						"com.ibm.bbva.common.datosProducto3.msg.tipoMoneda");
				existeError = true;
			}
			//Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
			/*if (linCredAprob == null || linCredAprob.doubleValue() <= 0) {
				addMessageError(formulario + ":lineaCredAprob", 
						"com.ibm.bbva.common.datosProducto3.msg.linCredAprobMenor");
				existeError = true;
			} else if (lineaCreditoOriginal != null && lineaCreditoOriginal > 0 && 
					linCredAprob.doubleValue() > lineaCreditoOriginal.doubleValue() ) {
				addMessageError(formulario + ":lineaCredAprob", 
						"com.ibm.bbva.common.datosProducto3.msg.linCredAprobMayor", lineaCreditoOriginal);
				existeError = true;
			}*/
			long plazoSol = (expediente.getExpedienteTC().getPlazoSolicitado()!=null && !expediente.getExpedienteTC().getPlazoSolicitado().trim().equals("")?Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitado().trim()):0);
			double lineaSol = (expediente.getExpedienteTC().getLineaCredSol()>0?expediente.getExpedienteTC().getLineaCredSol():0);
			//long plazoSolApr = (expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null && !expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals("")?Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitadoApr()):0);
			
			 if (accion.equals(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS) || accion.equals(Constantes.ACCION_BOTON_APROBAR_OPERACION)){
				if (plazoSol>0){
					
					//FIX2 ERIKA ABREGU
					DelegacionRiesgoCondicion objDelegacionRiesgoCondPlazo=delegacionRiesgoCondicionBeanLocalBean.buscarPorId(Constantes.CONDICION_PLAZO);
					
					if(objDelegacionRiesgoCondPlazo == null || objDelegacionRiesgoCondPlazo.getSimbolo() == null ||
							"".equals(objDelegacionRiesgoCondPlazo.getSimbolo().getDescripcion())){
						
						addMessageError(formulario + ":idPlazoSolApr", 
								"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprSinCond", plazoSol);
						existeError = true;
					}else {
						
						if(expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null && !expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals("")){
							long plazoSolApr = (expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null && !expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals("")?Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitadoApr().trim()):0);
							LOG.info("plazoSolApr -> "+plazoSolApr);
						
							if(!"SV".equals(objDelegacionRiesgoCondPlazo.getSimbolo().getDescripcion())){
								String condicion = objDelegacionRiesgoCondPlazo.getSimbolo().getDescripcion();
								LOG.info("plazoSolApr sin condicion SV -> "+plazoSolApr);
								if("<=".equals(condicion)){
									if(!(plazoSolApr > 0 && plazoSol <= plazoSolApr)){
										LOG.info("plazoSolApr con condicion <=  --> "+plazoSolApr);
										addMessageError(formulario + ":idPlazoSolApr", 
												"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprCond_1", plazoSol);
										existeError = true;							
									}
								}else if(">=".equals(condicion)){
									if(!(plazoSolApr > 0 && plazoSol >= plazoSolApr)){
										LOG.info("plazoSolApr con condicion >=  --> "+plazoSolApr);
										addMessageError(formulario + ":idPlazoSolApr", 
												"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprCond_2", plazoSol);
										existeError = true;							
									}
								}else if ("==".equals(condicion)){
									if(!(plazoSolApr > 0 && plazoSol == plazoSolApr)){
										LOG.info("plazoSolApr con condicion ==  --> "+plazoSolApr);
										addMessageError(formulario + ":idPlazoSolApr", 
												"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprCond_3", plazoSol);
										existeError = true;							
									}
								}else{
									LOG.info("plazoSolApr con ninguna condicion -> "+plazoSolApr);
									addMessageError(formulario + ":idPlazoSolApr", 
											"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprSinCond", plazoSol);
									existeError = true;
								}
									
							}else{
								if(!(plazoSolApr > 0)){
									addMessageError(formulario + ":idPlazoSolApr", 
											"com.ibm.bbva.common.datosProducto3.msg.plazoSolAprCero", plazoSol);
									existeError = true;							
								}
							}
							
						}
						
					}
			
				}
				
				if(lineaSol>0){
					LOG.info("lineaCreditoAprobado :::: "+lineaCreditoAprobado);
					
					//FIX2 ERIKA ABREGU
					DelegacionRiesgoCondicion objDelegacionRiesgoCondImporte=delegacionRiesgoCondicionBeanLocalBean.buscarPorId(Constantes.CONDICION_IMPORTE);
					
					if (objDelegacionRiesgoCondImporte == null || objDelegacionRiesgoCondImporte.getSimbolo() == null ||
							"".equals(objDelegacionRiesgoCondImporte.getSimbolo().getDescripcion())) {
							
						addMessageError(formulario + ":lineaCredAprob", 
								"com.ibm.bbva.common.datosProducto3.msg.linCredAprobSinCond", lineaSol);
						existeError = true;
					
					}else {
						
						if(lineaCreditoAprobado!=null && !lineaCreditoAprobado.equals("")){
							Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
							LOG.info("linCredAprob :::: "+linCredAprob);
						
							if(!"SV".equals(objDelegacionRiesgoCondImporte.getSimbolo().getDescripcion())){
								LOG.info("linCredAprob sin condicion SV -> "+linCredAprob);
								String condicion = objDelegacionRiesgoCondImporte.getSimbolo().getDescripcion();
								
								if("<=".equals(condicion)){
									if (!(linCredAprob.doubleValue()>0 && lineaSol <= linCredAprob.doubleValue() )){
										LOG.info("linCredAprob con condicion <=  :::: "+linCredAprob);
										addMessageError(formulario + ":lineaCredAprob", 
												"com.ibm.bbva.common.datosProducto3.msg.linCredAprobCond_1", lineaSol);
										existeError = true;
									}
								}else if(">=".equals(condicion)){
									if (!(linCredAprob.doubleValue()>0 && lineaSol >= linCredAprob.doubleValue() )){
										LOG.info("linCredAprob con condicion >=  :::: "+linCredAprob);
										addMessageError(formulario + ":lineaCredAprob", 
												"com.ibm.bbva.common.datosProducto3.msg.linCredAprobCond_2", lineaSol);
										existeError = true;
									}
								}else if ("==".equals(condicion)){
									if (!(linCredAprob.doubleValue()>0 && lineaSol == linCredAprob.doubleValue() )){
										LOG.info("linCredAprob con condicion ==  :::: "+linCredAprob);
										addMessageError(formulario + ":lineaCredAprob", 
												"com.ibm.bbva.common.datosProducto3.msg.linCredAprobCond_3", lineaSol);
										existeError = true;
									}
								}else{
									LOG.info("linCredAprob con ninguna condicion :::: "+linCredAprob);
									addMessageError(formulario + ":lineaCredAprob", 
											"com.ibm.bbva.common.datosProducto3.msg.linCredAprobSinCond", lineaSol);
									existeError = true;
								}
								
							}else{
								if (!(linCredAprob.doubleValue()>0)){
									addMessageError(formulario + ":lineaCredAprob", 
											"com.ibm.bbva.common.datosProducto3.msg.linCredAprobCero", lineaSol);
									existeError = true;
								}
							}
							
						}
						
					}
					
					//COMENTADO POR ERIKA ABREGU PARA EL CAMBIO DE FIX2
					/*if(lineaCreditoAprobado!=null && !lineaCreditoAprobado.equals("")){
						Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
						LOG.info("linCredAprob :::: "+linCredAprob);
						if (!(linCredAprob.doubleValue()>0 && linCredAprob.doubleValue()<=lineaSol )){
							addMessageError(formulario + ":lineaCredAprob", 
									"com.ibm.bbva.common.datosProducto3.msg.linCredAprobValidacion", lineaSol);
							existeError = true;
						}
					}*/
				}
			}
			 
			//long plazoSolApr = Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitadoApr());
			/*if (plazoSol>0 && !(plazoSolApr > 0 && plazoSolApr <= plazoSol)) {
				addMessageError(formulario + ":idPlazoSolApr", 
						"com.ibm.bbva.common.datosProducto3.msg.plazoSolApr", plazoSol);
				existeError = true;
			}*/
		}
		return existeError;
	}	
	
	private boolean validarVerificarEstadoContratacion() {
		boolean existeError = false;
		String formulario = "frmVerificarEstadoContratacion";
		
		if (habCredApro) {			
			if (tipoMonedaSel == null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(tipoMonedaSel)) {
				addMessageError(formulario + ":selectTipoMoneda", 
						"com.ibm.bbva.common.datosProducto3.msg.tipoMoneda");
				existeError = true;
			}
			Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
			if (linCredAprob == null || linCredAprob.doubleValue() <= 0) {
				addMessageError(formulario + ":lineaCredAprob", 
						"com.ibm.bbva.common.datosProducto3.msg.linCredAprobMenor");
				existeError = true;
			} else if (lineaCreditoOriginal != null && lineaCreditoOriginal > 0 && 
					linCredAprob.doubleValue() > lineaCreditoOriginal.doubleValue()) {
				addMessageError(formulario + ":lineaCredAprob", 
						"com.ibm.bbva.common.datosProducto3.msg.linCredAprobMayor", lineaCreditoOriginal);
				existeError = true;
			}
		}
		return existeError;
	}	
	
	/*
	private boolean validarRealizarAltaTarjeta() {
		boolean existeError = false;
		String formulario = "frmRealizarAltaTarjeta";
		String nroContrato = expediente.getExpedienteTC().getNroContrato();
		String nroTarjeta = expediente.getExpedienteTC().getNumTarjeta();
		
		if (nroContrato==null || nroContrato.trim().equals("")) {
			addMessageError(formulario + ":numContrato", 
						"com.ibm.bbva.common.datosProducto3.msg.numContrato");
			existeError = true;
		}
		if(nroTarjeta==null || nroTarjeta.trim().equals("")){
			addMessageError(formulario + ":numTarjeta", 
					"com.ibm.bbva.common.datosProducto3.msg.numTarjeta");
			existeError = true;
		}		
		return existeError;
	}*/
	/**
	 * Modificacion James - 29042014
	 * **/
	private boolean validarRealizarAltaTarjeta() {
		boolean existeError = false;
		if(producto!=null && producto.getId() == Constantes.ID_APLICATIVO_TC){
			String formulario = "frmRealizarAltaTarjeta";
			String nroContrato = expediente.getExpedienteTC().getNroContrato();
			String nroTarjeta = expediente.getExpedienteTC().getNumTarjeta();
			if (nroContrato==null || nroContrato.trim().equals("")) {
				addMessageError(formulario + ":numContrato", 
							"com.ibm.bbva.common.datosProducto3.msg.numContrato");
				existeError = true;
			}
			if(nroTarjeta==null || nroTarjeta.trim().equals("")){
				addMessageError(formulario + ":numTarjeta", 
						"com.ibm.bbva.common.datosProducto3.msg.numTarjeta");
				existeError = true;
			}
		}
		return existeError;
	}
	
	private boolean validarVerificarResultadoDomiciliaria() {
		boolean existeError = false;
		String formulario = "frmVerificarResultadoDomiciliaria";
		LOG.info("validarVerificarResultadoDomiciliaria");	
		if (selectedItemTe && !disabledSolicitudTasaEsp) {
			/*Obtiene Datos de Expediente*/		    	
			if (expediente.getExpedienteTC().getTasaEsp() == 0){
				addMessageError(formulario + ":idValorTasaEsp3", 
				"com.ibm.bbva.common.productoNuevo.msg.plazoSolicitado");
		        existeError = true;
			}		    	
		}
		LOG.info("existeError : "+existeError);
		return existeError;
	}

	private boolean validarAprobarExpediente() {
		boolean existeError = false;
		String formulario = "frmAprobarExpediente";
		
		String accion = (String) getObjectSession(Constantes.ACCION_SESION);	
		if (accion==null) accion="";
		
		LOG.info("validarAprobarExpediente - Accion : " + accion);
		
		if (habCredApro) {			
			if (tipoMonedaSel == null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(tipoMonedaSel)) {
				addMessageError(formulario + ":selectTipoMoneda", 
						"com.ibm.bbva.common.datosProducto3.msg.tipoMoneda");
				existeError = true;
			}
			//Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
			//if (!accion.equals(Constantes.ACCION_BOTON_ELEVAR_A_RIESGOS) &&
		/*
		 * Comentado el 04082014
		 * 
		 * if (accion.equals(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS) &&
					(linCredAprob == null || linCredAprob.doubleValue() <= 0)) {
				addMessageError(formulario + ":lineaCredAprob", 
						"com.ibm.bbva.common.datosProducto3.msg.linCredAprobMenor");
				existeError = true;
			} else if (lineaCreditoOriginal !=null && lineaCreditoOriginal > 0 && 
					linCredAprob.doubleValue() > lineaCreditoOriginal.doubleValue()) {
				addMessageError(formulario + ":lineaCredAprob", 
						"com.ibm.bbva.common.datosProducto3.msg.linCredAprobMayor", lineaCreditoOriginal);
				existeError = true;
			}*/
			
		//	long plazoSol = Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitado());
		//	long plazoSolApr = Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitadoApr());
			
			long plazoSol = (expediente.getExpedienteTC().getPlazoSolicitado()!=null && !expediente.getExpedienteTC().getPlazoSolicitado().trim().equals("")?Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitado().trim()):0);
			//long plazoSolApr = (expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null && !expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals("")?Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitadoApr()):0);
			double lineaSol = (expediente.getExpedienteTC().getLineaCredSol()>0?expediente.getExpedienteTC().getLineaCredSol():0);
			//if (!accion.equals(Constantes.ACCION_BOTON_ELEVAR_A_RIESGOS)) {
			 
			 if (accion.equals(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS) || accion.equals(Constantes.ACCION_BOTON_APROBAR_OPERACION)){
				if (plazoSol>0){
					if(expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null && !expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals("")){
						long plazoSolApr = (expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null && !expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals("")?Long.parseLong(expediente.getExpedienteTC().getPlazoSolicitadoApr().trim()):0);
						LOG.info("plazoSolApr -> "+plazoSolApr);
						if(!(plazoSolApr > 0 && plazoSolApr <= plazoSol)){
							addMessageError(formulario + ":idPlazoSolApr", 
									"com.ibm.bbva.common.datosProducto3.msg.plazoSolApr", plazoSol);
							existeError = true;							
						}
					}
				}
				
				if(lineaSol>0){
					LOG.info("lineaCreditoAprobado :::: "+lineaCreditoAprobado);
					if(lineaCreditoAprobado!=null && !lineaCreditoAprobado.equals("")){
						Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
						LOG.info("linCredAprob :::: "+linCredAprob);
						if (!(linCredAprob.doubleValue()>0 && linCredAprob.doubleValue()<=lineaSol )){
							addMessageError(formulario + ":lineaCredAprob", 
									"com.ibm.bbva.common.datosProducto3.msg.linCredAprobValidacion", lineaSol);
							existeError = true;
						}
					}
				}
			}			 
		}
		return existeError;
	}
	
	public void copiarDatosExpediente () {
		String jspPrinc = getNombreJSPPrincipal();
		if (jspPrinc.equals("formAprobarExpediente") ||
			jspPrinc.equals("formEjecutarEvalCrediticia") ||
			jspPrinc.equals("formRevisarRegistrarDictamen") ||
			jspPrinc.equals("formEvaluarFactibilidadOp") ||
			jspPrinc.equals("formVerificarEstadoContratacion")){			
			// ya tiene copiado la linea de credito aprobada (esta asignado directamente al campo de la ventana)		
			if (tipoMonedaSel!=null) {				
				int monApr = Integer.parseInt(tipoMonedaSel);
				TipoMoneda tipoMoneda = new TipoMoneda();
				tipoMoneda.setId(monApr);
			    expediente.getExpedienteTC().setTipoMonedaAprob(tipoMoneda);
			    LOG.info("monto aprobado "+expediente.getExpedienteTC().getLineaCredAprob());
			}
			
			//FIX2 ERIKA ABREGU
			if(porcentEndeudaCambiado!=null && porcentEndeudaCambiado != expediente.getExpedienteTC().getPorcentajeEndeudamiento()){
				expediente.getExpedienteTC().setPorcentajeEndeudamiento(porcentEndeudaCambiado);
			}
		}else if (jspPrinc.equals("formVerificarResultadoDomiciliaria") ||
				  jspPrinc.equals("formCambiarSituacionExp") ||
				  jspPrinc.equals("formRegistrarAprobResolucion") ||
			 jspPrinc.equals("formSolicitarVerificacionDomiciliaria")) {
			LOG.info("copiarDatosExpediente: "+jspPrinc);
			expediente.getExpedienteTC().setFlagSolTasaEsp(seleccion(selectedItemTe));
			if(!selectedItemTe && !disabledSolicitudTasaEsp){
				expediente.getExpedienteTC().setTasaEsp(0);
			}
		} // else if (jspPrinc.equals("formRealizarAltaTarjeta")) // ya tiene copiado el numero de contrato
	}
	
	private String seleccion (boolean val) {
		return val ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO;
	}
	
	private TipoCambioCE verificarTipoCambio() {
		
		LOG.info(" Verificando Tipo de Cambio");
		String strFecha = Util.formatDateObject("dd/MM/yyyy", new Date());
		LOG.info("Fecha Actual:" + strFecha);
		Date fecha = Util.parseStringToDate(strFecha, "dd/MM/yyyy");		
		TipoCambioCE objTipoCambioCE = tipoCambioCEBeanLocal.buscarPorFecha(fecha);		
		if (objTipoCambioCE == null) {
			LOG.info("Se intenta obtener el tipo de cambio llamando al servicio");
			objTipoCambioCE = null;
			Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION); 
			CtExtraerTipoCambioRq inCtExtraerTipoCambioRq  	= null;
			CtExtraerTipoCambioRs outCtExtraerTipoCambioRs  = null;
			pe.com.grupobbva.sce.tc84.CtHeader ctHeader = null;
			pe.com.grupobbva.sce.tc84.CtBodyRq CtBodyRq = null;
			
			inCtExtraerTipoCambioRq = new CtExtraerTipoCambioRq();	
			outCtExtraerTipoCambioRs = new CtExtraerTipoCambioRs();
			ctHeader = new pe.com.grupobbva.sce.tc84.CtHeader();
			CtBodyRq = new pe.com.grupobbva.sce.tc84.CtBodyRq();
			
			strFecha = Util.formatDateObject("dd.MM.yyyy", new Date());
			String tipoCambioDivisa = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_TIPO_CAMBIO_DIVISA").getValorVariable();
			LOG.info("Fecha solicitud: = " + strFecha);
			LOG.info("Tipo Cambio Solicitud = " + tipoCambioDivisa);
			
			ctHeader.setUsuario(empleado.getCodigo());			
			CtBodyRq.setFechaCambio(strFecha);
			
			CtBodyRq.setTipoCambio(tipoCambioDivisa);
			 
			inCtExtraerTipoCambioRq.setHeader(ctHeader);
			inCtExtraerTipoCambioRq.setData(CtBodyRq);
			LOG.info("-------------------------- INICIO CONSULTA AL SERVICIO EXTRAER TIPO CAMBIO ----------------------------");	
			 
			try{
			 LOG.info("DATOS REQUEST SERVICIO TIPO CAMBIO: " +
			 		"["+ "Codigo=" + ctHeader.getUsuario() +"; " 
					   + "Fecha Cambio=" + CtBodyRq.getFechaCambio() + "]");			 
			 outCtExtraerTipoCambioRs = bbvaFacade.extraerTipoCambio(inCtExtraerTipoCambioRq);
			 
			}catch(Throwable e){
			 LOG.error("Ocurrio un error de conexion con el servicio TIPO CAMBIO " + e);
			}
			LOG.info("-------------------------- FIN CONSULTA AL SERVICIO EXTRAER TIPO CAMBIO ----------------------------");
		
			if(outCtExtraerTipoCambioRs != null 
					&& outCtExtraerTipoCambioRs.getData() != null){
				LOG.info("DATOS RESPONSE EXTRAER TIPO CAMBIO: " +	"["+"TipoCambioCE=" + outCtExtraerTipoCambioRs.getData().getFechaCambio() 
						+ " " + outCtExtraerTipoCambioRs.getData().getTipoCambio() + "]");
				LOG.info("Fecha WS:" + outCtExtraerTipoCambioRs.getData().getFechaCambio());
				LOG.info("Monto WS:" + outCtExtraerTipoCambioRs.getData().getTipoCambio());
				CtTipos tipos = outCtExtraerTipoCambioRs.getData().getTipos();
				List<CtTipoCambio> lstCambios = tipos.getTipoCambio();
				for (int i = 0; i < lstCambios.size() ; i++) {
					CtTipoCambio cambio = lstCambios.get(i);
					if (cambio.getDivisa().trim().equals("USD")) {
						objTipoCambioCE = new TipoCambioCE();
						Double monto = Util.convertStringToDouble(cambio.getFixing(), ',', ' ');
						LOG.info("Monto Fixing Tipo Cambio:" + monto);
						objTipoCambioCE.setMonto(BigDecimal.valueOf(monto));
						String strOutFechaTC = outCtExtraerTipoCambioRs.getData().getFechaCambio();
						objTipoCambioCE.setFecha(Util.parseStringToDate(strOutFechaTC.replace('.', '/'), "dd/MM/yyyy"));
						i = lstCambios.size();
					}
					
				}
				LOG.info("TIPO CAMBIO--> Fecha:" + objTipoCambioCE.getFecha() + " Monto:" + objTipoCambioCE.getMonto());
				LOG.info("Insertamos el tipo de cambio extraido del servicio");
				tipoCambioCEBeanLocal.create(objTipoCambioCE);		
				LOG.info("Tipo de cambio para la fecha " + strFecha + " insertado en BD.");
			}
		}else{
			LOG.info("Ya existe tipo de cambio para la fecha actual.");
		}
		return objTipoCambioCE;
	}


	public boolean isHabCheckTe() {
		return habCheckTe;
	}

	public void setHabCheckTe(boolean habCheckTe) {
		this.habCheckTe = habCheckTe;
	}

	public boolean isHabCheckSc() {
		return habCheckSc;
	}

	public void setHabCheckSc(boolean habCheckSc) {
		this.habCheckSc = habCheckSc;
	}

	public boolean isHabCheckDe() {
		return habCheckDe;
	}

	public void setHabCheckDe(boolean habCheckDe) {
		this.habCheckDe = habCheckDe;
	}

	public boolean isRenderedWfTc() {
		return renderedWfTc;
	}

	public void setRenderedWfTc(boolean renderedWfTc) {
		this.renderedWfTc = renderedWfTc;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstadoVO(Estado estado) {
		this.estado= estado;
	}

	public boolean isRenderedNumContr() {
		return renderedNumContr;
	}

	public void setRenderedNumContr(boolean renderedNumContr) {
		this.renderedNumContr = renderedNumContr;
	}

	public String getLineaCredAprob() {
		return lineaCredAprob;
	}

	public void setLineaCredAprob(String lineaCredAprob) {
		this.lineaCredAprob = lineaCredAprob;
	}

	public String getTasaEsp() {
		return tasaEsp;
	}

	public void setTasaEsp(String tasaEsp) {
		this.tasaEsp = tasaEsp;
	}

	public String getLineaCredSol() {
		return lineaCredSol;
	}

	public void setLineaCredSol(String lineaCredSol) {
		this.lineaCredSol = lineaCredSol;
	}

	public boolean isRenderedNumTarjeta() {
		return renderedNumTarjeta;
	}

	public void setRenderedNumTarjeta(boolean renderedNumTarjeta) {
		this.renderedNumTarjeta = renderedNumTarjeta;
	}

	public boolean isRenderedNumTarjetaT() {
		return renderedNumTarjetaT;
	}

	public void setRenderedNumTarjetaT(boolean renderedNumTarjetaT) {
		this.renderedNumTarjetaT = renderedNumTarjetaT;
	}
	public boolean isHabTipoMonedaCredApro() {
		return habTipoMonedaCredApro;
	}

	public void setHabTipoMonedaCredApro(boolean habTipoMonedaCredApro) {
		this.habTipoMonedaCredApro = habTipoMonedaCredApro;
	}	

	public boolean isRenderedTMCredApro() {
		return renderedTMCredApro;
	}

	public void setRenderedTMCredApro(boolean renderedTMCredApro) {
		this.renderedTMCredApro = renderedTMCredApro;
	}
	
	public String getEstadoTarjeta() {
		return estadoTarjeta;
	}

	public void setEstadoTarjeta(String estadoTarjeta) {
		this.estadoTarjeta = estadoTarjeta;
	}

	public boolean isRenderedNumContrVer() {
		return renderedNumContrVer;
	}

	public void setRenderedNumContrVer(boolean renderedNumContrVer) {
		this.renderedNumContrVer = renderedNumContrVer;
	}

	public boolean isRenderedNumContrT() {
		return renderedNumContrT;
	}

	public void setRenderedNumContrT(boolean renderedNumContrT) {
		this.renderedNumContrT = renderedNumContrT;
	}

	public boolean isRenderedNumTarjetaVer() {
		return renderedNumTarjetaVer;
	}

	public void setRenderedNumTarjetaVer(boolean renderedNumTarjetaVer) {
		this.renderedNumTarjetaVer = renderedNumTarjetaVer;
	}

	public boolean isRenderedEnvioTarjeta() {
		return renderedEnvioTarjeta;
	}

	public void setRenderedEnvioTarjeta(boolean renderedEnvioTarjeta) {
		this.renderedEnvioTarjeta = renderedEnvioTarjeta;
	}

	public boolean isRenderedSolicitudTasaEsp() {
		return renderedSolicitudTasaEsp;
	}

	public void setRenderedSolicitudTasaEsp(boolean renderedSolicitudTasaEsp) {
		this.renderedSolicitudTasaEsp = renderedSolicitudTasaEsp;
	}

	public boolean isRenderedGarantia() {
		return renderedGarantia;
	}

	public void setRenderedGarantia(boolean renderedGarantia) {
		this.renderedGarantia = renderedGarantia;
	}

	public Garantia getGarantia() {
		return garantia;
	}

	public void setGarantia(Garantia garantia) {
		this.garantia = garantia;
	}

	public boolean isDisabledSolicitudTasaEsp() {
		return disabledSolicitudTasaEsp;
	}

	public void setDisabledSolicitudTasaEsp(boolean disabledSolicitudTasaEsp) {
		this.disabledSolicitudTasaEsp = disabledSolicitudTasaEsp;
	}

	public String getMsjOperacion() {
		return msjOperacion;
	}

	public void setMsjOperacion(String msjOperacion) {
		this.msjOperacion = msjOperacion;
	}

	public boolean isMsjOperacionBol() {
		return msjOperacionBol;
	}

	public void setMsjOperacionBol(boolean msjOperacionBol) {
		this.msjOperacionBol = msjOperacionBol;
	}

	public boolean isRenderedPlazoSolicitadoApr() {
		return renderedPlazoSolicitadoApr;
	}

	public void setRenderedPlazoSolicitadoApr(boolean renderedPlazoSolicitadoApr) {
		this.renderedPlazoSolicitadoApr = renderedPlazoSolicitadoApr;
	}
	
	public String getLineaCreditoAprobado(){
		LOG.info("linea cred aprobada ::: "+expediente.getExpedienteTC().getLineaCredAprob());
		String result = ((expediente.getExpedienteTC().getLineaCredAprob()>0)?String.valueOf(expediente.getExpedienteTC().getLineaCredAprob()):"");
		LOG.info("result 1::: "+result);
		
		if(result==null || result.equals("")){
			result=((lineaCreditoAprobado==null || this.lineaCreditoAprobado.equals(""))?"":result);			
		}
		LOG.info("result ::: "+result);
		
		LOG.info("lineaCreditoAprobado ::: "+lineaCreditoAprobado);
		return result;
	}
	
	public boolean isRenderedLineaCreditoAprobadoMuestra(){
		LOG.info("linea cred aprobada ::: "+expediente.getExpedienteTC().getLineaCredAprob());
		String nombJSP = getNombreJSPPrincipal();
		
		if (nombJSP.equals("formAprobarExpediente") || nombJSP.equals("formRevisarRegistrarDictamen") || 
				nombJSP.equals("formEjecutarEvalCrediticia"))
			return true;
		else{
			String result = ((expediente.getExpedienteTC().getLineaCredAprob()>0)?String.valueOf(expediente.getExpedienteTC().getLineaCredAprob()):"");
			LOG.info("result ::: "+result);
			if(!result.equals("")){
				return true;
			}else
				return false;			
		}

	}	

	public void setLineaCreditoAprobado(String lineaCreditoAprobado) {
		this.lineaCreditoAprobado = lineaCreditoAprobado;
		LOG.info("lineaCreditoAprobado ::: "+lineaCreditoAprobado);
		expediente.getExpedienteTC().setLineaCredAprob(((this.lineaCreditoAprobado==null || this.lineaCreditoAprobado.equals(""))?0:Double.parseDouble(this.lineaCreditoAprobado)));
	}

	public boolean isRenderedPlazoSolicitadoAprobOk() {
		LOG.info("expediente.getExpedienteTC().getPlazoSolicitadoApr() "+expediente.getExpedienteTC().getPlazoSolicitadoApr());
		String nombJSP = getNombreJSPPrincipal();
		
		if (nombJSP.equals("formAprobarExpediente") || nombJSP.equals("formRevisarRegistrarDictamen") || 
				nombJSP.equals("formEjecutarEvalCrediticia")){
			return true;
		}else{
			if(expediente.getExpedienteTC()!=null && 
					expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null && !expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals(""))
				return true;
			else
				return false;			
		}		
		
	}


	public void setRenderedPlazoSolicitadoAprobOk(
			boolean renderedPlazoSolicitadoAprobOk) {
		this.renderedPlazoSolicitadoAprobOk = renderedPlazoSolicitadoAprobOk;
	}

	public void cambiarTasaEspecial(AjaxBehaviorEvent event) {
		LOG.info("cambiarTasaEspecial");
		FacesContext ctx = FacesContext.getCurrentInstance();		
		PanelDocumentosMB panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}
	
	//FIX2 ERIKA ABREGU
	public void cambiarDocumentoObligatorio(AjaxBehaviorEvent event) {
		LOG.info("DatosProducto3MB -- cambiarDocumentosObligatorios ");
		FacesContext ctx = FacesContext.getCurrentInstance();		
		PanelDocumentosMB panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
		
		if(porcentEndeudaCambiado!=null){
			if(porcentEndeudaCambiado != expediente.getExpedienteTC().getPorcentajeEndeudamiento()){
				/*Cambiar en la Guia Documentaria el doc Otros documentos sustentarios � Analista de Riesgos de Opcional a Obligatorio*/
				LOG.info("porcentEndeudaCambiado es = " + porcentEndeudaCambiado + "y no es igual a % endeudamiento inicial " + expediente.getExpedienteTC().getPorcentajeEndeudamiento());
				//panelDocumento.cambiarEstadoObligatorio(event, "1");
				LOG.info("El documento se puso Obligatorio con exito ");
			
			}else{
				LOG.info("porcentEndeudaCambiado es = " + porcentEndeudaCambiado + "y el % endeudamiento inicial es " + expediente.getExpedienteTC().getPorcentajeEndeudamiento());
				//panelDocumento.cambiarEstadoObligatorio(event, "0");
				LOG.info("El documento se restableci� a No Obligatorio con �xito ");
			}
		}
	}

	public String getMsjOperacion292() {
		return msjOperacion292;
	}

	public void setMsjOperacion292(String msjOperacion292) {
		this.msjOperacion292 = msjOperacion292;
	}

	public boolean isMsjOperacionBol292() {
		return msjOperacionBol292;
	}

	public void setMsjOperacionBol292(boolean msjOperacionBol292) {
		this.msjOperacionBol292 = msjOperacionBol292;
	}

	public boolean isRenderedLineaCredAprob() {
		return renderedLineaCredAprob;
	}

	public void setRenderedLineaCredAprob(boolean renderedLineaCredAprob) {
		this.renderedLineaCredAprob = renderedLineaCredAprob;
	}

	public boolean isRenderedPlazoSolicAprob() {
		return renderedPlazoSolicAprob;
	}

	public void setRenderedPlazoSolicAprob(boolean renderedPlazoSolicAprob) {
		this.renderedPlazoSolicAprob = renderedPlazoSolicAprob;
	}

	public String getMsgErrorPersonalizado() {
		return msgErrorPersonalizado;
	}

	public void setMsgErrorPersonalizado(String msgErrorPersonalizado) {
		this.msgErrorPersonalizado = msgErrorPersonalizado;
	}

	public String getMsgPersonalizado() {
		return msgPersonalizado;
	}

	public void setMsgPersonalizado(String msgPersonalizado) {
		this.msgPersonalizado = msgPersonalizado;
	}

	public String getNumTarjeta() {
		//fix2 erika abregu
		//numTarjeta=expediente.getExpedienteTC().getNroCta();
		numTarjeta ="";
		 
		if(expediente.getExpedienteTC().getNroCta()!=null && !expediente.getExpedienteTC().getNroCta().equals("")){
			String[] arrayNumeroT = expediente.getExpedienteTC().getNroCta().split("-");
			if(arrayNumeroT.length >0){
				for (int i = 0; i < arrayNumeroT.length; i++) {
					numTarjeta += arrayNumeroT[i];
				}
			}else{
				numTarjeta=expediente.getExpedienteTC().getNroCta();
			}	
		}
		
		LOG.info("numTarjeta inicial:::"+numTarjeta);
		//fin de fix 2 erika abregu
		
		if(numTarjeta!=null && !numTarjeta.equals("")){
			LOG.info("numTarjeta:::"+numTarjeta);
			String strEntidad=numTarjeta.substring(0,4);
			LOG.info("strEntidad:::"+strEntidad);
			String strOficina=numTarjeta.substring(4,8);
			LOG.info("strOficina:::"+strOficina);
			String strDigitoChequeo=numTarjeta.substring(8,10);
			LOG.info("strDigitoChequeo:::"+strDigitoChequeo);
			String strNumeroCuenta=numTarjeta.substring(10,numTarjeta.length());
			LOG.info("strNumeroCuenta:::"+strNumeroCuenta);
			numTarjeta=strEntidad+"-"+strOficina+"-"+strDigitoChequeo+"-"+strNumeroCuenta;
			LOG.info("numTarjeta:::"+numTarjeta);
		}
		
		return numTarjeta;
	}

	public void setNumTarjeta(String numTarjeta) {
		this.numTarjeta = numTarjeta;
	}

	public boolean isRenderedAdicional() {
		String nombJSP = getNombreJSPPrincipal();
		LOG.info("nombJSP:::"+nombJSP);
		if (nombJSP.equals("formArchivarExpediente") || nombJSP.equals("formVisualizarExpediente")) {
			renderedAdicional=true;			
		} else 
			renderedAdicional=false;
			
		LOG.info("renderedAdicional:::"+renderedAdicional);
		
		return renderedAdicional;
	}

	public void setRenderedAdicional(boolean renderedAdicional) {
		this.renderedAdicional = renderedAdicional;
	}

	public boolean isRenderedPlazoSolicitadoAprobEdit() {
		return renderedPlazoSolicitadoAprobEdit;
	}

	public void setRenderedPlazoSolicitadoAprobEdit(
			boolean renderedPlazoSolicitadoAprobEdit) {
		this.renderedPlazoSolicitadoAprobEdit = renderedPlazoSolicitadoAprobEdit;
	}
	
	//fix2 erika abregu
	public boolean isDisabledPorsEndeudamiento() {
		return disabledPorsEndeudamiento;
	}

	public void setDisabledPorsEndeudamiento(boolean disabledPorsEndeudamiento) {
		this.disabledPorsEndeudamiento = disabledPorsEndeudamiento;
	}

	public Double getPorcentEndeudaCambiado() {
		return porcentEndeudaCambiado;
	}

	public void setPorcentEndeudaCambiado(Double porcentEndeudaCambiado) {
		this.porcentEndeudaCambiado = porcentEndeudaCambiado;
	}

	public String getPorcentEndeudaCambiadoOriginal() {
		return porcentEndeudaCambiadoOriginal;
	}

	public void setPorcentEndeudaCambiadoOriginal(
			String porcentEndeudaCambiadoOriginal) {
		this.porcentEndeudaCambiadoOriginal = porcentEndeudaCambiadoOriginal;
	}

}

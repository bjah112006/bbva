package com.ibm.bbva.controller.common;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.grupobbva.sce.tc84.CtBodyRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRs;
import pe.com.grupobbva.sce.tc84.CtHeader;
import pe.com.grupobbva.sce.tc84.CtTipoCambio;
import pe.com.grupobbva.sce.tc84.CtTipos;

import bbva.ws.api.view.BBVAFacadeLocal;
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.AprobarExpedienteMB;
import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Nivel;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.TipoCambioCE;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.service.business.client.ExpedienteDTO;
import com.ibm.bbva.session.CategoriaRentaBeanLocal;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.NivelBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.TipoCambioCEBeanLocal;
import com.ibm.bbva.util.AyudaVerificacionExp;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "detalleExpediente")
@RequestScoped
public class DetalleExpedienteMB extends AbstractMBean {
	
	@EJB
	private ClienteNaturalBeanLocal clienteNaturalBean;
	@EJB
	private CategoriaRentaBeanLocal categoriaRentaBean;
	@EJB
	private EmpleadoBeanLocal empleadoBean;
	@EJB
	private FacadeLocal facade;
	@EJB
	private NivelBeanLocal nivelBean;
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	@EJB
	private TipoCambioCEBeanLocal tipoCambioCEBeanLocal;
	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	OficinaBeanLocal oficinaBean;
	
	private ClienteNatural clienteNatural;
	private Expediente expediente; 
	//private Empleado empleadoSession;
	private String numeroExpediente;
	
	private List<String> selectedItems;
	private List<String> selectedItemsCat;
	private List<SelectItem> listaCategoriaRenta;
	
	private boolean renderedTxtOper;
	private String mensajeOperacion;
	private boolean cumpleOperacion;
	
	private String msjOperacion; 
	private boolean msjOperacionBol;
	
	private static final Logger LOG = LoggerFactory.getLogger(DetalleExpedienteMB.class);
	
	public DetalleExpedienteMB() {		
	}	

	@PostConstruct
	public void obtenerDatos() {
		LOG.info("metodo obtenerDatos de MB");
		String nombJSP = getNombreJSPPrincipal();
		
		/*Obtiene Datos de Expediente*/
		//empleadoSession = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		numeroExpediente = String.valueOf(expediente.getId());
		
		/*Obtiene Datos de Cliente*/
		/*if(expediente.getId() > 0 && (expediente.getClienteNatural() == null || expediente.getClienteNatural().getId() <= 0)){
			expediente = expedienteBean.buscarPorId(expediente.getId());
		}*/
		//clienteNatural = clienteNaturalBean.buscarPorId(expediente.getClienteNatural().getId());
		clienteNatural=	expediente.getClienteNatural();
		
		/*Obtiene Datos Categoria de Renta*/
		listaCategoriaRenta = Util.crearItems(categoriaRentaBean.buscarTodos(),
				"id", "descripcion", false);
		
		recortarDatosLista();
		
		if (!nombJSP.equals("formVisualizarExpediente")) {
			Collections.sort(listaCategoriaRenta, new Util.SecletItemComparator(true));  
		}
	//	
		
		if(clienteNatural!=null){
			LOG.info("clienteNatural no null, ID::"+clienteNatural.getId());
			/*Obtiene Datos Cliente Categoria Renta*/
			selectedItemsCat = new ArrayList<String>();
			if (clienteNatural.getCategoriasRenta() != null) {
				for (CategoriaRenta lista : clienteNatural.getCategoriasRenta()) {			
					selectedItemsCat.add(lista.getCodigo());
				}
			}
			
			selectedItems = new ArrayList<String>();
			
			if (clienteNatural.getPerExpPub()!=null && clienteNatural.getPerExpPub().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("1");
			}
	        if (clienteNatural.getPagoHab()!=null && clienteNatural.getPagoHab().equals(Constantes.CHECK_SELECCIONADO)) {
	        	selectedItems.add("2");
			}		
			if (clienteNatural.getAval()!=null && clienteNatural.getAval().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("3");
			}		
			if (clienteNatural.getSubrog()!=null && clienteNatural.getSubrog().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("4");
			}
			//fix2 erika abregu 
			if (clienteNatural.getMonocuota()!=null && clienteNatural.getMonocuota().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("5");
			}
		}else{
			LOG.info("clienteNatural NULL");
		}

		
		LOG.info("Valor de LineaCredAprob 2 = "+expediente.getExpedienteTC().getLineaCredAprob());
		/*if(expediente!=null && expediente.getExpedienteTC()!=null && 
				expediente.getExpedienteTC().getLineaCredAprob()==0){
			expediente.getExpedienteTC().setLineaCredAprob(expediente.getExpedienteTC().getLineaCredSol());
			LOG.info("Seteando valor, nuevo valor expediente.getExpedienteTC().setLineaCredAprob  2 = "+expediente.getExpedienteTC().getLineaCredAprob());
		}*/
		
		AyudaVerificacionExp ayudaVerificacionExp=null;
		
		ExpedienteDTO objExpedienteDTO=new ExpedienteDTO();
		
		LOG.info("nombJSP:::"+nombJSP);
		
		if (nombJSP.equals("formAprobarExpediente") ||
			nombJSP.equals("formEvaluarFactibilidadOp")) {
		
			renderedTxtOper = true;
			objExpedienteDTO=mapearExpedienteAExpedienteDTO();
			ayudaVerificacionExp=new AyudaVerificacionExp();
			
			// si esta dentro de su delegacion
			//if (facade.ServiceIBMBBVA_delegacionOficinaWS(Integer.valueOf(String.valueOf(expediente.getEmpleado().getNivel().getId())), expediente.getId()))
			LOG.info("SERVICIO DELEGACION OFICINA");
			if(objExpedienteDTO.getCodigoNivel()>0){
				LOG.info("Nivel a considerar "+objExpedienteDTO.getCodigoNivel());
				LOG.info("Validacion de tipo de cambio.");
				if (nombJSP.equals("formAprobarExpediente")){
					if (String.valueOf(objExpedienteDTO.getCodigoTipoMonedaSol()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
						if(verificarTipoCambio() == null){
							LOG.info("NO hay tipo de cambio, no puede seguir el flujo.");
							msjOperacion="No Existe Tipo de Cambio el día de hoy.";
							msjOperacionBol=true;
						}
					}
				}
				
				if(ayudaVerificacionExp.delegacionOficinaValidacion(objExpedienteDTO)){
					LOG.info("Entra a Servicio");
					boolean validaDelegacion = facade.ServiceIBMBBVA_delegacionOficinaMemoriaWS(objExpedienteDTO);
					expediente.getExpedienteTC().setFlagDelegacion(validaDelegacion?"1":"0");
					if (validaDelegacion) {
						LOG.info("Dentro de Delegacion Oficina 2 ");
						mensajeOperacion = Mensajes.getMensaje("com.ibm.bbva.common.detalleExpediente.operDentro");
					} else { // no esta dentro de su delegacion
						LOG.info("Fuera de Delegacion Oficina 2 ");
						mensajeOperacion = Mensajes.getMensaje("com.ibm.bbva.common.detalleExpediente.operFuera");
						ELContext elContext = FacesContext.getCurrentInstance().getELContext();
						AprobarExpedienteMB aprobarExpedienteMB = (AprobarExpedienteMB) FacesContext
								.getCurrentInstance().getApplication().getELResolver()
								.getValue(elContext, null, "aprobarExpediente");
						aprobarExpedienteMB.setActivoAprOperacion(true);
					}	
					
				}else{
					LOG.info("Validación Delegacion Oficina es falso");
					msjOperacion="No se encuentra configurado valores de Delegación oficina";
					msjOperacionBol=true;				
				}			
			}
			

		} else if (nombJSP.equals("formEjecutarEvalCrediticia")) {
			renderedTxtOper = true;
						
			// si esta dentro de su delegacion
			//if (facade.ServiceIBMBBVA_delegacionRiesgosWS(Integer.valueOf(String.valueOf(expediente.getEmpleado().getTipoCategoria().getId())), expediente.getId())) {
			boolean validaDelegacion = facade.ServiceIBMBBVA_delegacionRiesgosWS(Integer.valueOf(String.valueOf(expediente.getEmpleado().getTipoCategoria().getId())), expediente.getId());
			expediente.getExpedienteTC().setFlagDelegacion(validaDelegacion?"1":"0");
			
			if (validaDelegacion) {				
				mensajeOperacion = Mensajes.getMensaje("com.ibm.bbva.common.detalleExpediente.operDentroAnalista");
				cumpleOperacion = true;
			} else { // no esta dentro de su delegacion
				mensajeOperacion = Mensajes.getMensaje("com.ibm.bbva.common.detalleExpediente.operFueraAnalista");
				cumpleOperacion = false;
			}
		} else {
			renderedTxtOper = false;
		}
	}
	
	private ExpedienteDTO mapearExpedienteAExpedienteDTO(){
		ExpedienteDTO objExpedienteDTO=new ExpedienteDTO();
		objExpedienteDTO.setCodigoExpediente(expediente.getId());
		objExpedienteDTO.setCodigoProducto(expediente.getProducto().getId());
		objExpedienteDTO.setCodigoTipoMonedaSol(expediente.getExpedienteTC().getTipoMonedaSol().getId());
		//objExpedienteDTO.setCodigoTipoScoring(expediente.getExpedienteTC().getTipoScoring()==null?null:expediente.getExpedienteTC().getTipoScoring().getId());
		
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
			 * */			
			
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
					msjOperacion="No existe configuración 'Sin Nivel'";
					msjOperacionBol=true;						
				}				
			}
		}else{
			LOG.info("Empleado o Oficina es nulo");
			msjOperacionBol=true;	
		}
		
		//addObjectSession(Constantes.OBS_SESSION, msjOperacionBol);
		
		objExpedienteDTO.setCodigoEstadoCivilTitular(expediente.getClienteNatural().getEstadoCivil().getId());
		if(expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getClasificacionBanco()!=null && 
				expediente.getExpedienteTC().getClasificacionBanco().getId()>0)
			objExpedienteDTO.setClasificacionBanco(expediente.getExpedienteTC().getClasificacionBanco().getId());
		else
			objExpedienteDTO.setClasificacionBanco(Long.parseLong("0"));

		objExpedienteDTO.setClasificacionSbs(expediente.getExpedienteTC().getClasificacionSbs());			
		objExpedienteDTO.setLineaConsumo(expediente.getExpedienteTC().getLineaConsumo());
		objExpedienteDTO.setLineaCredAprob(expediente.getExpedienteTC().getLineaCredAprob());
		objExpedienteDTO.setLineaCredSol(expediente.getExpedienteTC().getLineaCredSol());
		objExpedienteDTO.setPerExpPub(expediente.getClienteNatural().getPerExpPub()!=null?expediente.getClienteNatural().getPerExpPub():null);
		objExpedienteDTO.setPorcentajeEndeudamiento(expediente.getExpedienteTC().getPorcentajeEndeudamiento());
		objExpedienteDTO.setRiesgoCliente(expediente.getExpedienteTC().getRiesgoCliente());
		objExpedienteDTO.setPlazoSolicitado(expediente.getExpedienteTC().getPlazoSolicitado() != null?expediente.getExpedienteTC().getPlazoSolicitado().trim():null);
		
		if(objExpedienteDTO.getCodigoEstadoCivilTitular().equals(Constantes.EST_CIVIL_CASADO)){
			objExpedienteDTO.setBancoConyuge(expediente.getExpedienteTC().getBancoConyuge()==null?null:expediente.getExpedienteTC().getBancoConyuge().getId());
			objExpedienteDTO.setSbsConyuge(expediente.getExpedienteTC().getSbsConyuge());
		}
		
		return objExpedienteDTO;
	}

	public void recortarDatosLista() {
		for (SelectItem lista: listaCategoriaRenta) {
			lista.setLabel(lista.getLabel().substring(0, 7)+".");			
		}
	}	
	
	public void copiarDatosClienteNatural (ClienteNatural clienteNatural) {
		clienteNatural.setEstadoCivil(this.clienteNatural.getEstadoCivil());
		clienteNatural.setApeMat(this.clienteNatural.getApeMat());
		clienteNatural.setApePat(this.clienteNatural.getApePat());
		clienteNatural.setNombre(this.clienteNatural.getNombre());		
		clienteNatural.setPerExpPub(this.clienteNatural.getPerExpPub());
		clienteNatural.setPagoHab(this.clienteNatural.getPagoHab());
		clienteNatural.setAval(this.clienteNatural.getAval());
		clienteNatural.setSubrog(this.clienteNatural.getSubrog());
		clienteNatural.setNumDoi(this.clienteNatural.getNumDoi());
		clienteNatural.setCodCliente(this.clienteNatural.getCodCliente());
		clienteNatural.setFecVenDoi(this.clienteNatural.getFecVenDoi());
		clienteNatural.setId(this.clienteNatural.getId());
		clienteNatural.setSegmento(this.clienteNatural.getSegmento());
		clienteNatural.setTipoCliente(this.clienteNatural.getTipoCliente());
		clienteNatural.setTipoDoi(this.clienteNatural.getTipoDoi());
		clienteNatural.setIngNetoMensual(this.clienteNatural.getIngNetoMensual());
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
			CtHeader ctHeader = null;
			CtBodyRq CtBodyRq = null;
			
			inCtExtraerTipoCambioRq = new CtExtraerTipoCambioRq();	
			outCtExtraerTipoCambioRs = new CtExtraerTipoCambioRs();
			ctHeader = new CtHeader();
			CtBodyRq = new CtBodyRq();
			
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

	
	public List<String> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<String> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public List<String> getSelectedItemsCat() {
		return selectedItemsCat;
	}

	public void setSelectedItemsCat(List<String> selectedItemsCat) {
		this.selectedItemsCat = selectedItemsCat;
	}

	public List<SelectItem> getListaCategoriaRenta() {
		return listaCategoriaRenta;
	}

	public void setListaCategoriaRenta(List<SelectItem> listaCategoriaRenta) {
		this.listaCategoriaRenta = listaCategoriaRenta;
	}
	
	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public ClienteNatural getClienteNatural() {
		return clienteNatural;
	}

	public void setClienteNatural(ClienteNatural clienteNatural) {
		this.clienteNatural = clienteNatural;
	}

	public boolean isRenderedTxtOper() {
		return renderedTxtOper;
	}

	public void setRenderedTxtOper(boolean renderedTxtOper) {
		this.renderedTxtOper = renderedTxtOper;
	}

	public String getMensajeOperacion() {
		return mensajeOperacion;
	}

	public void setMensajeOperacion(String mensajeOperacion) {
		this.mensajeOperacion = mensajeOperacion;
	}

	public String getNumeroExpediente() {
		return numeroExpediente;
	}

	public void setNumeroExpediente(String numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}

	public boolean isCumpleOperacion() {
		return cumpleOperacion;
	}

	public void setCumpleOperacion(boolean cumpleOperacion) {
		this.cumpleOperacion = cumpleOperacion;
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

}

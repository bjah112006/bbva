package com.ibm.bbva.controller.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
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
import com.ibm.bbva.controller.form.EjecutarEvalCrediticiaMB;
import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.CartTerritorioCE;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.TipoCambioCE;
import com.ibm.bbva.entities.TipoCategoria;
import com.ibm.bbva.service.business.client.ExpedienteDTO;
import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.TipoCambioCEBeanLocal;
import com.ibm.bbva.session.TipoCategoriaBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "elevaSuperior")
@RequestScoped
public class ElevaSuperiorMB extends AbstractMBean {
	@EJB
	private TipoCategoriaBeanLocal tipoCategoriaBean;
	@EJB
	private EmpleadoBeanLocal empleadoBean;

	private boolean mostrarDialogo = false; // inicialmente no se muestra
	private List<SelectItem> listaTipos;
	private String tipoSeleccionado;
	private String titulo;
	private HtmlPanelGroup dialogoModal;
	private String nombreFormPadre;
	
	//FIX2 ERIKA ABREGU
	private Expediente expediente;
	private String msjOperacion292=""; 
	private boolean msjOperacionBol292=false;
	@EJB
	private FacadeLocal facade;
	@EJB
	private TipoCambioCEBeanLocal tipoCambioCEBeanLocal;
	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	@EJB
	private CartEmpleadoCEBeanLocal cartEmpleadoCEBeanLocalBean;
	
	private static final Logger LOG = LoggerFactory.getLogger(ElevaSuperiorMB.class);
	
	public ElevaSuperiorMB() {
	}
	
	@PostConstruct
	public void init() {
		String nombJSP = getNombreJSPPrincipal();
		nombreFormPadre = nombJSP.replaceFirst("form", "frm");
		
		//fix2 erika abregu
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		if(expediente != null && expediente.getId() > 0){
			LOG.info("Numero de Expediente para evaluar en ElevaSuperior es "+expediente.getId());
			
			/*Obtener tipos de categorias*/
			List<TipoCategoria> listTipoCategoria = tipoCategoriaBean.buscarTodos();
			List<Empleado> lista = new ArrayList<Empleado>();
			
			/*Cargar lista de Superiores*/
			for (TipoCategoria tipoCatego : listTipoCategoria) {
				if (tipoCatego.getFlagSuperior().equals(Constantes.FLAG_SUPERIOR_1)) {
					
					List<Empleado> listEmpleado = empleadoBean
							.buscarPorIdTipoCategoriaActivo(tipoCatego.getId(), Constantes.ID_PERFIL_RIESGOS_SUPERIOR);
					if (listEmpleado != null && !listEmpleado.isEmpty()) {
						LOG.info("Tamaño de listEmpleado con idCategoria "+tipoCatego.getId()+" es "+listEmpleado.size());
						
						for(Empleado objEmpleado : listEmpleado){
							LOG.info("(Elevar Superior)--> Entro para evaluar delegacion del empleado "+objEmpleado.getCodigo());
							
							//VALIDAR SI EL EMPLEADO ESTA DENTRO DE LA CARTETIZACION DEL PRODUCTO DEL EXPEDIENTE
							List<CartEmpleadoCE> listCarterizacion= new ArrayList<CartEmpleadoCE>();			
							LOG.info("CARTERIZACION X EMPLEADO :  CODIGO EMPLEADO : " + objEmpleado.getId()+" CODIGO CARTERIZACION: "+expediente.getProducto().getId());
							listCarterizacion.addAll(cartEmpleadoCEBeanLocalBean.buscarPorIdCaractIdEmpleado(expediente.getProducto().getId(), objEmpleado.getId()));
							
							if(!listCarterizacion.isEmpty()){
								LOG.info("(Elevar Superior)--> Validando Tipo de Moneda del Expediente "+expediente.getId()+" con Tipo de Moneda "+expediente.getExpedienteTC().getTipoMonedaSol().getDescripcion());
								if (String.valueOf(expediente.getExpedienteTC().getTipoMonedaSol().getId()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
									if(verificarTipoCambio() == null){
										LOG.info("NO hay tipo de cambio, no puede seguir el flujo.");
										msjOperacion292="No Existe Tipo de Cambio el día de hoy.";
										msjOperacionBol292=true;
									}else{
										LOG.info("(Elevar Superior)--> Validando Delegacion Riesgo del Empleado (Dolares)::: ");
										boolean validaDelegacion=facade.ServiceIBMBBVA_delegacionRiesgosWS(Integer.valueOf(String.valueOf(objEmpleado.getTipoCategoria().getId())), expediente.getId());
										if(validaDelegacion){
											lista.addAll(listEmpleado);
											LOG.info("(Elevar Superior) Empleado paso validacion por delegacion (Dolares) -->" 
													+objEmpleado.getCodigo()+", "+objEmpleado.getNombresCompletos()+", "+objEmpleado.getNivel().getDescripcion()+" Tarea 19");
										}else{
											LOG.info("(Elevar Superior) Empleado no paso validacion por delegacion (Dolares) -->" 
													+objEmpleado.getCodigo()+", "+objEmpleado.getNombresCompletos()+", "+objEmpleado.getNivel().getDescripcion()+" Tarea 19");
										}
									}
								}else{
									LOG.info("(Elevar Superior)--> Validando Delegacion Riesgo del Empleado (Soles)::: ");
									boolean validaDelegacion=facade.ServiceIBMBBVA_delegacionRiesgosWS(Integer.valueOf(String.valueOf(objEmpleado.getTipoCategoria().getId())), expediente.getId());
									if(validaDelegacion){
										lista.addAll(listEmpleado);
										LOG.info("(Elevar Superior) Empleado paso validacion por delegacion (Soles) -->" 
												+objEmpleado.getCodigo()+", "+objEmpleado.getNombresCompletos()+", "+objEmpleado.getNivel().getDescripcion()+" Tarea 19");
									}else{
										LOG.info("(Elevar Superior) Empleado no paso validacion por delegacion (Soles) -->" 
												+objEmpleado.getCodigo()+", "+objEmpleado.getNombresCompletos()+", "+objEmpleado.getNivel().getDescripcion()+" Tarea 19");
									}
								}
							}else{
								LOG.info("(Elevar Superior) Empleado no paso validacion por estar fuera de CARTERIZACION -->" 
										+objEmpleado.getCodigo()+", "+objEmpleado.getNombresCompletos()+", "+objEmpleado.getNivel().getDescripcion());
								LOG.info("(Elevar Superior) Expediente -->"+expediente.getId()); 
								LOG.info("(Elevar Superior) Pdocuducto del Expediente --> ID = "+expediente.getProducto().getId()+" Descripcion = "+expediente.getProducto().getDescripcion());
						
							}
					
						}
						
					}else
						LOG.info("listEmpleado con idCategoria "+tipoCatego.getId()+" es vacío");
				}
			}
			if(lista!=null)
				LOG.info("Tamaño de lista es "+lista.size());
			else
				LOG.info("Lista es nulo");
			
			listaTipos = Util.crearItems(lista, true, "id", "nombresCompletos");
			
		}else
			LOG.info("Numero de Expediente para evaluar en ElevaSuperior es vacío");
		//fin de fix2 erika abregu
		
		/*
		* cambiado por erika abregu para fix2
		* se debe filtrara por empleados con flag superior, que sean de riesgo superior y que sean activos
		/*Obtener tipos de categorias*/
		/*List<TipoCategoria> listTipoCategoria = tipoCategoriaBean.buscarTodos();
		List<Empleado> lista = new ArrayList<Empleado>();
		
		/*Cargar lista de Superiores*/
		/*for (TipoCategoria tipoCatego : listTipoCategoria) {
			if (tipoCatego.getFlagSuperior().equals(Constantes.FLAG_SUPERIOR_1)) {
				
				 List<Empleado> listEmpleado = empleadoBean
						.buscarPorIdTipoCategoria(tipoCatego.getId(), Constantes.ID_PERFIL_RIESGOS_SUPERIOR);
				if (listEmpleado != null && !listEmpleado.isEmpty()) {
					LOG.info("Tamaño de listEmpleado con idCategoria "+tipoCatego.getId()+" es "+listEmpleado.size());
					lista.addAll(listEmpleado);
				}else
					LOG.info("listEmpleado con idCategoria "+tipoCatego.getId()+" es vacío");
			}
		}
		if(lista!=null)
			LOG.info("Tamaño de lista es "+lista.size());
		else
			LOG.info("Lista es nulo");
		
		listaTipos = Util.crearItems(lista, true, "id", "nombresCompletos");
		*/
	}
	
	//fix2 erika abregu
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
	//fin de fix2 erika abregu

	public boolean isMostrarDialogo() {
		return mostrarDialogo;
	}

	public void setMostrarDialogo(boolean mostrarDialogo) {
		this.mostrarDialogo = mostrarDialogo;
	}

	public List<SelectItem> getListaTipos() {
		return listaTipos;
	}

	public void setListaTipos(List<SelectItem> listaTipos) {
		this.listaTipos = listaTipos;
	}

	public String getTipoSeleccionado() {
		return tipoSeleccionado;
	}

	public void setTipoSeleccionado(String tipoSeleccionado) {
		this.tipoSeleccionado = tipoSeleccionado;
		LOG.info("tipoSeleccionado = "+this.tipoSeleccionado);
	}
	
	private boolean esValido () {
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		if (jspPrinc.equals("formEjecutarEvalCrediticia")) {
			formulario = "frmEjecutarEvalCrediticia";
		}
		
		boolean existeError = false;
		if (tipoSeleccionado==null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(tipoSeleccionado)) {
			addMessageError(formulario + ":elevaSuperior", 
					"com.ibm.bbva.common.elevaSuperior.msg.elevaSuperior");
			existeError = true;
		}
		return !existeError;
	}
	
	public String aceptar () {
		if (!esValido ()) {
			mostrarDialogo = true;
		} else {
			String jspPrinc = getNombreJSPPrincipal();
			if (jspPrinc.equals("formEjecutarEvalCrediticia")) {				
				// se ejecuta la accion para guardar los datos
				ELContext elContext = FacesContext.getCurrentInstance().getELContext();
				EjecutarEvalCrediticiaMB ejecutarEvalCrediticiaMB = (EjecutarEvalCrediticiaMB) FacesContext
						.getCurrentInstance().getApplication().getELResolver()
						.getValue(elContext, null, "ejecutarEvalCrediticia");
				LOG.info("tipoSeleccionado = "+tipoSeleccionado);
				String result=ejecutarEvalCrediticiaMB.guardarElevaSup(tipoSeleccionado);
				mostrarDialogo = false;
				LOG.info("result::::"+result);
				if(result!=null)
					return result;
			}
			return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
		}
		return null;
	}
	
	public String cancelar () {
		mostrarDialogo = false;
		return null;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public HtmlPanelGroup getDialogoModal() {
		return dialogoModal;
	}

	public void setDialogoModal(HtmlPanelGroup dialogoModal) {
		this.dialogoModal = dialogoModal;
	}

	public String getNombreFormPadre() {
		return nombreFormPadre;
	}

	public void setNombreFormPadre(String nombreFormPadre) {
		this.nombreFormPadre = nombreFormPadre;
	}
	
	//FIX2 ERIKA ABREGU
	public boolean isMsjOperacionBol292() {
		return msjOperacionBol292;
	}

	public void setMsjOperacionBol292(boolean msjOperacionBol292) {
		this.msjOperacionBol292 = msjOperacionBol292;
	}
	
	public String getMsjOperacion292() {
		return msjOperacion292;
	}

	public void setMsjOperacion292(String msjOperacion292) {
		this.msjOperacion292 = msjOperacion292;
	}
}

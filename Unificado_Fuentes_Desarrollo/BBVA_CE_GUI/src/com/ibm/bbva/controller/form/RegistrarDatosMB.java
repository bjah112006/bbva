package com.ibm.bbva.controller.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.grupobbva.sce.tc84.CtBodyRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRs;
import pe.com.grupobbva.sce.tc84.CtHeader;
import pe.com.grupobbva.sce.tc84.CtTipoCambio;
import pe.com.grupobbva.sce.tc84.CtTipos;
import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bpd.RemoteUtils;
import bbva.ws.api.view.BBVAFacadeLocal;
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.DatosProducto2MB;
import com.ibm.bbva.controller.common.DocumentosEscaneadosMB;
import com.ibm.bbva.controller.common.ObservacionRechazoMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DevolucionTarea;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.Nivel;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.TipoCambioCE;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.entities.TipoMoneda;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.harec.client.BodyRq;
import com.ibm.bbva.harec.client.DatosGeneralesXPersonaRq;
import com.ibm.bbva.harec.client.DatosGeneralesXpersonaRs;
import com.ibm.bbva.harec.client.HeadRq;
import com.ibm.bbva.service.business.client.ExpedienteDTO;
import com.ibm.bbva.service.business.client.PerfilDTO;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.DevolucionTareaBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.LineaMaximaBeanLocal;
import com.ibm.bbva.session.NivelBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.TipoCambioCEBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import com.ibm.bbva.session.TipoScoringBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaDocumento;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.AyudaVerificacionExp;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "registrarDatos")
@RequestScoped
public class RegistrarDatosMB extends AbstractMBean {
	
	@EJB
	private TipoClienteBeanLocal tipoClienteBean;	
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private ClienteNaturalBeanLocal clienteNaturalBean;
	@EJB
	HistorialBeanLocal historialBean;
	@EJB
	LineaMaximaBeanLocal lineaMaximaBean;	
	@EJB
	private DevolucionTareaBeanLocal devolucionTareaBean;
	@EJB
	private TipoScoringBeanLocal tipoScoringBean;
	@EJB
	private FacadeLocal facade;
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	@EJB
	private EmpleadoBeanLocal empleadoBean;		
	@EJB
	private NivelBeanLocal nivelBean;		
	@EJB
	private TipoCambioCEBeanLocal tipoCambioCEBeanLocal;
	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	@EJB
	OficinaBeanLocal oficinaBean;
	
	private Expediente expediente;
	private Expediente expedienteEntrada;
	private boolean msgGuiaDocumentaria;
	private boolean flagConsultaNacar;
	private String msjOperacion; 
	private boolean msjOperacionBol;
	
	private boolean msjOperacionOfBol;
	private String msjOperacionOf; 

	private String msjOperacion292; 
	private boolean msjOperacionBol292;
	private String msgErrorPersonalizado;
	private String msgPersonalizado;
	
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarDatosMB.class);
	
	public RegistrarDatosMB() {
		
	}
	
	@PostConstruct
    public void init() {
		LOG.info("Entro RegistrarDatos");
        // se obtiene el expedeinte de sesion, recuperado de la base de datos
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		// objeto donde se almacena los cambios en el expediente
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		
		if (expediente != null) {
			// se verifica si es cliente BBVA
			TipoCliente tipoCliente = tipoClienteBean.buscarPorId(Long.parseLong(Constantes.CODIGO_TIPO_CLIENTE_BBVA));
//			LOG.info("expediente.getClienteNatural().getTipoCliente().getId() = "+expediente.getClienteNatural().getTipoCliente().getId());
//			LOG.info("tipoCliente.getId() = "+tipoCliente.getId());
			if(getObjectSession(Constantes.FLAG_NACAR_SESION)!=null){
				flagConsultaNacar=(Boolean) getObjectSession(Constantes.FLAG_NACAR_SESION);
				LOG.info("flagConsultaNacar = "+flagConsultaNacar);
			}
			
			if (expediente.getClienteNatural().getTipoCliente().getId() != tipoCliente.getId()) {
				/*Nacar nacar = new Nacar (); 
			    ClienteHarecVO clienteNacar = null; 
				try {
				    clienteNacar = nacar.buscarCliente(expediente.getClienteNatural().getTipoDoi().getCodigoTipoDoi(), expediente.getClienteNatural().getNumDoi()); 
				} catch (Throwable e) { 
					 //logger.error(e); 
				}*/	
				flagConsultaNacar=true;
			    addObjectSession(Constantes.FLAG_NACAR_SESION, flagConsultaNacar);
				// validar si recibe informacion de nacar
				//Emi
				 Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION); 
				 DatosGeneralesXPersonaRq inDatosGeneralesXPersona = null;	
				 DatosGeneralesXpersonaRs OutDatosGeneralesXPersona = null;
				 HeadRq headRq = null;
				 BodyRq bodyRq = null;
				 
				 inDatosGeneralesXPersona = new DatosGeneralesXPersonaRq();	
				 OutDatosGeneralesXPersona = new DatosGeneralesXpersonaRs();
				 headRq = new HeadRq();
				 bodyRq = new BodyRq();
				 
				 headRq.setUsuario(empleado.getCodigo());
				 bodyRq.setTipoDOI(expediente.getClienteNatural().getTipoDoi().getCodigoTipoDoi());
				 bodyRq.setNroDOI(expediente.getClienteNatural().getNumDoi());
				 
				 inDatosGeneralesXPersona.setHead(headRq);
				 inDatosGeneralesXPersona.setBody(bodyRq);			 
				 
				 try{
					 OutDatosGeneralesXPersona = bbvaFacade.consultaDatosHarec(inDatosGeneralesXPersona);
					 
				 }catch(Throwable e){
					 LOG.error("Ocurrio un error de conexion con el servicio Harec : BBVA_RENIEC_WSDLSOAP_HTTP_Service"+ e);
				 }
				 
			}
		}
	}
	
	public String aceptar() {		
		LOG.info("Enviar a aprobacion - Exp : "+expediente.getId());
		AyudaVerificacionExp ayudaVerificacionExp;
		DatosProducto2MB datosProducto = null;
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		if (esValido()) {
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "documentosEscaneados");
			LOG.info("Doc escaneados, validacion guia escaneada = "+documentosEscaneados.getValidaGuiaEscaneada());
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				ayudaVerificacionExp=new AyudaVerificacionExp();
				
				datosProducto = (DatosProducto2MB)  
						 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto2");	
				
				// se obtiene el expedeinte de sesion
				Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);			
				ClienteNatural clienteNatural = expediente.getClienteNatural();
				
				// se actualiza con los datos ingresados por el usuario
				datosProducto.copiarDatos(clienteNatural);
				
				ExpedienteDTO objExpedienteDTO=mapearExpedienteAExpedienteDTO();
				
				LOG.info("Validacion de tipo de cambio.");
				if (objExpedienteDTO!=null){
					if (String.valueOf(objExpedienteDTO.getCodigoTipoMonedaSol()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
						LOG.info("La moneda es Dolares.");
						if(verificarTipoCambio(expediente.getEmpleado()) == null){
								LOG.info("NO hay tipo de cambio, no puede seguir el flujo.");
								msjOperacion292="No Existe Tipo de Cambio el día de hoy.";
								msjOperacionBol292=true;
								return null;
							}
					}
				}
				LOG.info("Validación Linea Maxima");
				Perfil objPerfil= ayudaVerificacionExp.lineaMaximaValidacion(objExpedienteDTO);
				Oficina objOficina=null;
				if(objPerfil!=null){
					List<Empleado> listEmpleado=null;
					
					if(objPerfil.getId()==Constantes.PERFIL_SUB_GERENTE_OFICINA){
						LOG.info("Buscando SubGerente activo para la Oficina con Id"+expediente.getExpedienteTC().getOficina().getId());
						listEmpleado=empleadoBean.buscarPorPerfilEmpleadoActivo(objPerfil.getId(), expediente.getExpedienteTC().getOficina().getId());
						
						/**
						 * Cambio 14 Abril 2015 
						 * Oficinas Desplazadas
						 * */
						if(listEmpleado==null || listEmpleado.size()<1){
							LOG.info("Verificando si es oficina Desplazada:::: Id oficina::"+expediente.getExpedienteTC().getOficina().getId());
							objOficina = oficinaBean.buscarPorId(expediente.getExpedienteTC().getOficina().getId());
							
							if(objOficina!=null && objOficina.getFlagDesplazada()!=null && !objOficina.getFlagDesplazada().trim().equals("") && 
									objOficina.getFlagDesplazada().trim().equals(Constantes.FLAG_ACTIVO)){
								LOG.info("Id oficina::"+objOficina.getId()+":::Flag Desplazada:::"+objOficina.getFlagDesplazada());
								if(objOficina.getOficinaPrincipal()!=null){
									LOG.info("Id oficina::"+objOficina.getId()+":::tiene como Oficina Principal a Oficina con id:::"+objOficina.getOficinaPrincipal().getId());
									listEmpleado = empleadoBean.buscarPorPerfilEmpleadoActivo(objPerfil.getId(), objOficina.getOficinaPrincipal().getId());
								}else{
									LOG.info("Id Oficina desplazada::"+objOficina.getId()+"::: No tiene configurado su Oficina Principal");
								}
								
							}else
								LOG.info("Id oficina::"+objOficina.getId()+":::Flag Desplazada:::"+objOficina.getFlagDesplazada());
						}
						/**
						 * */
						
					}else
					{
						listEmpleado=empleadoBean.buscarPorPerfilActivo(objPerfil.getId());
					}
					LOG.info("Id Perfil= "+objPerfil.getId());

					if(listEmpleado!=null && listEmpleado.size()>0){
						LOG.info("Existe Perfil "+objPerfil.getDescripcion()+" para oficina "+expediente.getExpedienteTC().getOficina().getCodigo()+" - "+expediente.getExpedienteTC().getOficina().getDescripcion());
						if(objExpedienteDTO.getCodigoNivel()>0){
							LOG.info("Nivel a considerar "+objExpedienteDTO.getCodigoNivel());
							LOG.info("Validacion de tipo de cambio.");
							if (String.valueOf(objExpedienteDTO.getCodigoTipoMonedaSol()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
								if(verificarTipoCambio(expediente.getEmpleado()) == null){
									LOG.info("NO hay tipo de cambio, no puede seguir el flujo.");
									msjOperacion292="No Existe Tipo de Cambio el día de hoy.";
									msjOperacionBol292=true;
									return null;
								}
							}
							if(ayudaVerificacionExp.delegacionOficinaValidacion(objExpedienteDTO)){
								LOG.info("Entra a grabar");
							    grabarExp(Constantes.ACCION_BOTON_ENVIAR_APROBACION,
										  Constantes.ESTADO_APROBADO_TAREA_3);				
								addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
								addObjectSession(Constantes.ID_EXPEDIENTE_SESION, expediente.getId());
								msgGuiaDocumentaria = false;
								msjOperacionBol=false;
								//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";		
								return "/moverArchivos/formMoverArchivos?faces-redirect=true";
							}else{
								LOG.info("Validación Delegacion Oficina es falso");
								Nivel objNivel=nivelBean.buscarPorId(objExpedienteDTO.getCodigoNivel());
								msjOperacion="No se encuentra configurado valores de Delegación oficina para el Nivel "+objNivel.getDescripcion()+" con Producto "+ objExpedienteDTO.getCodigoProducto();
								msjOperacionBol=true;				
							}							
						}else
							LOG.info("Nivel es nulo");	
					}else{
						if(objPerfil.getId()==Constantes.ID_PERFIL_SUB_GERENTE){
							LOG.info("No Existe Perfil "+objPerfil.getDescripcion()+" para oficina "+expediente.getExpedienteTC().getOficina().getCodigo()+" - "+expediente.getExpedienteTC().getOficina().getDescripcion());
							
							if(objOficina!=null && objOficina.getFlagDesplazada()!=null && !objOficina.getFlagDesplazada().trim().equals("") && 
									objOficina.getFlagDesplazada().trim().equals(Constantes.FLAG_ACTIVO)){
								msjOperacion="No Existe Perfil "+objPerfil.getDescripcion().toUpperCase()+" activo para: \n * Oficina Desplazada "+expediente.getExpedienteTC().getOficina().getCodigo()+" - "+expediente.getExpedienteTC().getOficina().getDescripcion().toUpperCase();
								if(objOficina.getOficinaPrincipal()!=null)
									msjOperacion+="\n  * Oficina Principal "+objOficina.getOficinaPrincipal().getId()+" - "+objOficina.getOficinaPrincipal().getDescripcion();
								else
									msjOperacion+="\n - Oficina Desplazada "+expediente.getExpedienteTC().getOficina().getCodigo()+" - "+expediente.getExpedienteTC().getOficina().getDescripcion().toUpperCase()+" no tiene configurado su Oficina Principal";
							}else{
								msjOperacion="No Existe Perfil "+objPerfil.getDescripcion()+" para oficina "+expediente.getExpedienteTC().getOficina().getCodigo()+" - "+expediente.getExpedienteTC().getOficina().getDescripcion();
							}
							msjOperacionBol=true;
						}else
						{
							LOG.info("No Existe Perfil "+objPerfil.getDescripcion()+" activo");
							msjOperacion="No Existe Perfil "+objPerfil.getDescripcion()+" activo";
						msjOperacionBol=true;
					}

					}
				}else{
					LOG.info("Perfil es nulo");
					msjOperacion="No se encuentra configurado valores de Línea Máxima";
					msjOperacionBol=true;
				}
			}else{				
				msgGuiaDocumentaria = true;
				return null;
			}
		}
		return null;	
	}

	private boolean esValido() {		
		FacesContext ctx = FacesContext.getCurrentInstance();
		DatosProducto2MB datosProducto = null;
		ComentarioMB comentario = null;

		datosProducto = (DatosProducto2MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto2");
		
		comentario = (ComentarioMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
		
		boolean datProd = datosProducto.esValido();
		boolean validoComentario = comentario.esValido();
		LOG.info("datProd = "+datProd);
		LOG.info("validoComentario = "+validoComentario);
		
		msgErrorPersonalizado = datosProducto.getMsgErrorPersonalizado();
		msgPersonalizado = datosProducto.getMsgPersonalizado();
		return datProd && validoComentario;
	}
	
	public boolean isMsgGuiaDocumentaria() {
		return msgGuiaDocumentaria;
	}

	public void setMsgGuiaDocumentaria(boolean msgGuiaDocumentaria) {
		this.msgGuiaDocumentaria = msgGuiaDocumentaria;
	}
	
	public String devolver() {
		LOG.info("Devolver - Exp : "+expediente.getId());
		addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "observacionRechazo");
		observacionRechazoMB.init();
		observacionRechazoMB.setMostrarDialogo(true);
		addObjectSession("actualizarObservados", "OK");
		return null;
	}	
	
	/*
	 * Llamado por el metodo aceptar de la clase ObservacionRechazoMB
	 */
	public String accionVentana(List<DevolucionTarea> lista) {
		expediente.setMotivoDevolucion(lista.get(0).getMotivoDevolucion());
		/*
		 * Se Agrego
		 * 
		 * */
		expediente.getExpedienteTC().setComentarioRechazo(lista.get(0).getExpediente().getExpedienteTC().getComentarioRechazo());
		LOG.info("COMENTARIO RECHAZO:" + expediente.getExpedienteTC().getComentarioRechazo());
		
		for (DevolucionTarea devTarea : lista) {
			devolucionTareaBean.create(devTarea);
		}
		grabarExp(Constantes.ACCION_BOTON_DEVOLVER,
				Constantes.ESTADO_POR_VALIDAR_MESA_CONTROL_TAREA_3);
        
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}
	
	private ExpedienteDTO mapearExpedienteAExpedienteDTO(){
		
		ExpedienteDTO objExpedienteDTO=new ExpedienteDTO();
		objExpedienteDTO.setCodigoExpediente(expediente.getId());
		objExpedienteDTO.setCodigoProducto(expediente.getProducto().getId());
		objExpedienteDTO.setCodigoTipoMonedaSol(expediente.getExpedienteTC().getTipoMonedaSol().getId());
		
		objExpedienteDTO.setPlazoSolicitado(expediente.getExpedienteTC().getPlazoSolicitado());
		LOG.info("PLAZO SOLICITADO = "+objExpedienteDTO.getPlazoSolicitado());
		
		if(expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoScoring()!=null){
			objExpedienteDTO.setCodigoTipoScoring(expediente.getExpedienteTC().getTipoScoring().getId());
		}else
			LOG.info("Expediente no tiene Scoring");
		
		objExpedienteDTO.setCodigoNivel(Long.parseLong("0"));
		
		Oficina objOficina=null;
		
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
					msjOperacion="No existe configuración 'SIN NIVEL DE DELEGACIÓN'";
					msjOperacionBol=true;						
				}					
			}
		}else{
			LOG.info("Empleado o Oficina es nulo");
			msjOperacionBol=true;	
		}
		//objExpedienteDTO.setCodigoNivel(expediente.getEmpleado().getNivel().getId());		
		
		objExpedienteDTO.setCodigoEstadoCivilTitular(expediente.getClienteNatural().getEstadoCivil().getId());
		objExpedienteDTO.setClasificacionBanco(expediente.getExpedienteTC().getClasificacionBanco().getId());
		objExpedienteDTO.setClasificacionSbs(expediente.getExpedienteTC().getClasificacionSbs());
		objExpedienteDTO.setLineaConsumo(expediente.getExpedienteTC().getLineaConsumo());
		objExpedienteDTO.setLineaCredAprob(expediente.getExpedienteTC().getLineaCredAprob());
		LOG.info("LineaConsumo = "+objExpedienteDTO.getLineaConsumo());
		LOG.info("LineaCredAprob = "+objExpedienteDTO.getLineaCredAprob());
		
		if (String.valueOf(objExpedienteDTO.getCodigoTipoMonedaSol()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {		
			TipoCambioCE objTipoCambioCE = verificarTipoCambio(expediente.getEmpleado());
			if(objTipoCambioCE == null){
				LOG.info("NO hay tipo de cambio, no puede seguir el flujo.");
				msjOperacion292="No Existe Tipo de Cambio el día de hoy.";
				msjOperacionBol292=true;
				return null;
			}
			
			objExpedienteDTO.setLineaConsumo(calcularTipoCambio(objExpedienteDTO.getLineaConsumo(), objExpedienteDTO.getCodigoTipoMonedaSol() ,objTipoCambioCE));
			objExpedienteDTO.setLineaCredAprob(calcularTipoCambio(objExpedienteDTO.getLineaCredAprob(), objExpedienteDTO.getCodigoTipoMonedaSol() ,objTipoCambioCE));
			LOG.info("LineaConsumo TC = "+objExpedienteDTO.getLineaConsumo());
			LOG.info("LineaCredAprob TC = "+objExpedienteDTO.getLineaCredAprob());
		}	
		objExpedienteDTO.setLineaCredSol(expediente.getExpedienteTC().getLineaCredSol());
		LOG.info("LineaCredSol = "+objExpedienteDTO.getLineaCredSol());
		
		objExpedienteDTO.setPerExpPub(expediente.getClienteNatural().getPerExpPub());
		objExpedienteDTO.setPorcentajeEndeudamiento(expediente.getExpedienteTC().getPorcentajeEndeudamiento());
		objExpedienteDTO.setRiesgoCliente(expediente.getExpedienteTC().getRiesgoCliente());
		objExpedienteDTO.setFlagSubRogado(expediente.getClienteNatural().getSubrog());
		
		if(objExpedienteDTO.getCodigoEstadoCivilTitular().equals(Constantes.EST_CIVIL_CASADO)){
			objExpedienteDTO.setBancoConyuge(expediente.getExpedienteTC().getBancoConyuge()==null?null:expediente.getExpedienteTC().getBancoConyuge().getId());
			objExpedienteDTO.setSbsConyuge(expediente.getExpedienteTC().getSbsConyuge());
		}
		
		if(objExpedienteDTO!=null)
			LOG.info("objExpedienteDTO no null");
		
		return objExpedienteDTO;
	}	
	
	public String grabarExp(String accion, Integer estado) {

		FacesContext ctx = FacesContext.getCurrentInstance();
		
		DatosProducto2MB datosProducto = null;
		ComentarioMB comentario = null;
		PanelDocumentosMB panelDocumentoMB = null;
		
		datosProducto = (DatosProducto2MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto2");	
		
		// se obtiene el comentario
		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
		
		// se obtiene el expedeinte de sesion
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);			
		ClienteNatural clienteNatural = expediente.getClienteNatural();
		
		// se actualiza con los datos ingresados por el usuario
		datosProducto.copiarDatos(clienteNatural);
		
		// se asigna el nombre de la accion para que cambie de estado
		expediente.setAccion(accion);
		
		Estado estadoTmp = new Estado();
		estadoTmp.setId(estado.longValue());
		LOG.info("estado.longValue()="+estado.longValue());
		PerfilDTO perfilDTO=null;

		if(!accion.equals(Constantes.ACCION_BOTON_DEVOLVER)){
			/*Estado segun linea maxima*/
			if (!String.valueOf(expediente.getExpedienteTC().getTipoOferta().getId()).equals(Constantes.CODIGO_OFERTA_APROBADO)) {
				ExpedienteDTO objExpedienteDTO=mapearExpedienteAExpedienteDTO();
				if(objExpedienteDTO!=null){
					//PerfilDTO perfilDTO = facade.ServiceIBMBBVA_lineaMaximaMemoria(expediente.getId(), expediente.getExpedienteTC().getTipoScoring().getId());
					LOG.info("objExpedienteDTO.getFlagSubRogado()= "+objExpedienteDTO.getFlagSubRogado());
					perfilDTO = facade.ServiceIBMBBVA_lineaMaximaMemoria(objExpedienteDTO);
					LOG.info("Perfil enviado por Linea Maxima = "+perfilDTO.getId());
					if (perfilDTO.getId() == Constantes.ID_PERFIL_CONTROLLER) {
						LOG.info("expediente.getExpedienteTC().getVerifDom()="+expediente.getExpedienteTC().getVerifDom());
						//if (expediente.getExpedienteTC().getVerifDom().equals(Constantes.CHECK_SELECCIONADO)) {
						if (expediente.getExpedienteTC().getVerifDom().equals(Constantes.CHECK_SELECCIONADO) || 
								expediente.getExpedienteTC().getVerifLab().equals(Constantes.CHECK_SELECCIONADO) || 
								(expediente.getProducto().getId()==Constantes.ID_APLICATIVO_PLD && expediente.getExpedienteTC().getVerifDps().equals(Constantes.CHECK_SELECCIONADO))) {
						    estadoTmp.setId(Constantes.ESTADO_EN_VERIFICACION_TAREA_3);
						    LOG.info("Exite Verificacion Domiciliaria, valor de Estado = "+estadoTmp.getId());
						}
					}else if (perfilDTO.getId() == Constantes.PERFIL_SUB_GERENTE_OFICINA) {
						estadoTmp.setId(Constantes.ESTADO_POR_APROBAR_TAREA_3);
					}else if (perfilDTO.getId() == Constantes.ID_PERFIL_ANALISIS_DE_RIESGOS) {
						estadoTmp.setId(Constantes.ESTADO_EN_ANALISIS_Y_RIESGOS_TAREA_3);
					}					
				}else{
					LOG.info("objExpedienteDTO es nulo");
					return null;
				}

			}			
		}

		expediente.setEstado(estadoTmp);
		
		
		LOG.info("Estado :"+estadoTmp.getId());
		
		// se copia el comentario
		comentario.copiarDatosExpediente();
					
		// se actualiza en la base de datos
		expediente.getExpedienteTC().setLineaConsumo(Util.convertStringToDouble(Double.toString(expediente.getExpedienteTC().getLineaConsumo())));
        RemoteUtils objRemoteUtils=new RemoteUtils();
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		
		if (Constantes.ESTADO_POR_VALIDAR_MESA_CONTROL_TAREA_3==estado) {
			Util.actualizaDevoluciones(expediente);
		}		
		
		expedienteBean.edit(expediente);	
		clienteNaturalBean.edit(clienteNatural);
		
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC ();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion, Integer.parseInt(""+expediente.getEstado().getId()));
		
		if(perfilDTO!=null){
			LOG.info("setIdPerfilUsuarioActual 2: "+expedienteTCWPS.getIdPerfilUsuarioActual());
			expedienteTCWPS.setIdPerfilUsuarioActual(""+perfilDTO.getId());
		}else
			LOG.info("perfilDTO es nulo");		
		
		//expedienteTCWPS.setScoringAprobado(expediente.getExpedienteTC().getTipoScoring().getDescripcion());
		String tkiid = expedienteTCWPS.getTaskID();
				
		//bandeja.enviarExpedienteTC(tkiid, expedienteTCWPS);
		
		/**
		 * ===========================================================
		 * Bloque de revisión de nuevos
		 * archivos subidos, debe ir antes de enviar el mensaje
		 * al process.
		 */
		
		PanelDocumentosMB panelDocumentos = (PanelDocumentosMB)getObjectRequest("paneldocumentos");
		
		expedienteTCWPS = ayudaExpedienteTC.setFlagEnvioContent(expedienteTCWPS, panelDocumentos);
		
		LOG.debug("Se han subido archivos al expediente : " + expedienteTCWPS.getFlagEnvioContent());
		
		/** =============================================================== **/
		
		
		objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
		ayudaExpedienteTC.actualizarListaExpedienteTC(new Consulta());
        
		// se almacena en el historial
		Estado estadoTmp2 = new Estado();
		estadoTmp2.setId(expedienteEntrada.getEstado().getId());
		expediente.setEstado(estadoTmp2);		
		
		LOG.info("Estado 2 :"+estadoTmp2.getId());
		//LOG.info("getClasificacionBanco : "+expediente.getExpedienteTC().getClasificacionBanco().getId());
		//LOG.info("getBancoConyuge : "+expediente.getExpedienteTC().getBancoConyuge());
		
		Historial historial = ConvertExpediente.convertToHistorialVO(expediente);
		/*INICIO REQUERIMIENTO 286 27.11.2014*/
		List<ToePerfilEstado> lstToePerfilEstado= new ArrayList<ToePerfilEstado>();
		lstToePerfilEstado=Util.recuperaValoresTOEPerfilEstado(expediente, toePerfilEstadoBeanLocal);
		ToePerfilEstado toePerfilEstado=new ToePerfilEstado();
		if (lstToePerfilEstado.size()>0){
			toePerfilEstado = lstToePerfilEstado.get(0);
		}else{
			toePerfilEstado = null;
		}
		/*FIN REQUERIMIENTO 286 27.11.2014*/
		
		AyudaHistorial ayudaHistorial = new AyudaHistorial();		
		ayudaHistorial.asignarFecha(historial, expedienteTCWPS);
		/*INICIO REQUERIMIENTO 286 27.11.2014*/
		int ans=0;
		if (toePerfilEstado!=null){
			historial.setTiempoObjTC(toePerfilEstado.getTiempoObjTc().intValueExact());
			historial.setTiempoObjTE(toePerfilEstado.getTiempoObjTe().intValueExact());
			historial.setTiempoPreTC(toePerfilEstado.getTiempoPreTc().intValueExact());
			historial.setTiempoPreTE(toePerfilEstado.getTiempoPreTe().intValueExact());
			historial.setNomColumna(toePerfilEstado.getNomColumna());
			ans=toePerfilEstado.getTiempoObjTc().intValueExact()+toePerfilEstado.getTiempoObjTe().intValueExact();
			historial.setAns(ans);
		}
		else{
			historial.setTiempoObjTC(0);
			historial.setTiempoObjTE(0);
			historial.setTiempoPreTC(0);
			historial.setTiempoPreTE(0);
			historial.setNomColumna(null);
			historial.setAns(0);
		}
		/*FIN REQUERIMIENTO 286 27.11.2014*/
		historialBean.create(historial);

		//Adjunta Documentos Expediente
		AyudaDocumento ayudaDocumento = new AyudaDocumento();
		ayudaDocumento.adjuntarDocumentoExpediente();
		
		ayudaDocumento.editarDocumentoExpediente();
		
		removeObjectSession(Constantes.LISTA_DOC_EXP_ADJ);
		removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
		
		//todos los documentos se actualizan como no observados
        panelDocumentoMB = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
        panelDocumentoMB.actualizarNoObservados();
		
		// se remueve el expedeinte de sesion
		removeObjectSession(Constantes.EXPEDIENTE_SESION);
		removeObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		return "";
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

	public boolean isMsjOperacionOfBol() {
		return msjOperacionOfBol;
	}

	public void setMsjOperacionOfBol(boolean msjOperacionOfBol) {
		this.msjOperacionOfBol = msjOperacionOfBol;
	}

	public String getMsjOperacionOf() {
		return msjOperacionOf;
	}

	public void setMsjOperacionOf(String msjOperacionOf) {
		this.msjOperacionOf = msjOperacionOf;
	}
	
	
	
	/**
	 * Metodo para calcular el tipo de cambio si el tipo de moneda es dolares.
	 * @param monto
	 * @param objTipoMoneda
	 * @param tipoCambioCE
	 * @return
	 */
	private double calcularTipoCambio(double monto, Object objTipoMoneda,  TipoCambioCE tipoCambioCE){
		
		if (Util.isDouble(String.valueOf(monto))) {
			if (objTipoMoneda instanceof TipoMoneda) {
				TipoMoneda tipoMoneda = (TipoMoneda) objTipoMoneda;
				if (tipoMoneda.getCodigo().equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
					BigDecimal nuevoMonto = new BigDecimal(monto).multiply(tipoCambioCE.getMonto());
					return nuevoMonto.doubleValue();
				} else {
					return monto;
				}
			} else if (objTipoMoneda instanceof Long) {
				Long tipoMoneda = (Long) objTipoMoneda;
				if (String.valueOf(tipoMoneda).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
					BigDecimal nuevoMonto = new BigDecimal(monto).multiply(tipoCambioCE.getMonto());
					return nuevoMonto.doubleValue();
				} else {
					return monto;
				}
			} else {
				return monto;
			}
		} else{
			return monto;
		}		
	}
	
	
	private TipoCambioCE verificarTipoCambio(Empleado empleado) {
		
		LOG.info(" Verificando Tipo de Cambio");
		String strFecha = Util.formatDateObject("dd/MM/yyyy", new Date());
		LOG.info("Fecha Actual:" + strFecha);
		Date fecha = Util.parseStringToDate(strFecha, "dd/MM/yyyy");		
		TipoCambioCE objTipoCambioCE = tipoCambioCEBeanLocal.buscarPorFecha(fecha);		
		if (objTipoCambioCE == null) {
			LOG.info("Se intenta obtener el tipo de cambio llamando al servicio");
			objTipoCambioCE = null;
			//Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION); 
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
}
package com.ibm.bbva.controller.form;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bpd.RemoteUtils;
import pe.ibm.util.Convertidor;
import bbva.ws.api.view.BBVAFacadeLocal;
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.DatosProducto2MB;
import com.ibm.bbva.controller.common.DocumentosEscaneadosMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.harec.client.BodyRq;
import com.ibm.bbva.harec.client.DatosGeneralesXPersonaRq;
import com.ibm.bbva.harec.client.DatosGeneralesXpersonaRs;
import com.ibm.bbva.harec.client.HeadRq;
import com.ibm.bbva.service.business.client.ExpedienteDTO;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaDocumento;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.AyudaVerificacionExp;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "corregirExp32")
@RequestScoped
public class CorregirExpediente32MB extends AbstractMBean {
	
	@EJB
	private TipoClienteBeanLocal tipoClienteBean;	
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private ClienteNaturalBeanLocal clienteNaturalBean;
	@EJB
	HistorialBeanLocal historialBean;
	@EJB
	private FacadeLocal facade;
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	@EJB
	private EmpleadoBeanLocal empleadoBean;		
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	
	private Expediente expediente;
	private Expediente expedienteEntrada;
	private boolean msgGuiaDocumentaria;
	private String msjOperacion; 
	private boolean msjOperacionBol;
	
	private boolean msjOperacionOfBol;
	private String msjOperacionOf;
	private String msgPersonalizado;
	
	private static final Logger LOG = LoggerFactory.getLogger(CorregirExpediente32MB.class);
	
	public CorregirExpediente32MB() {
		
	}
	
	@PostConstruct
    public void init() {
		LOG.info("Entro CorregirExpediente33MB");
        // se obtiene el expedeinte de sesion, recuperado de la base de datos
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		// objeto donde se almacena los cambios en el expediente
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		
		if (expediente != null) {
			// se verifica si es cliente BBVA
			TipoCliente tipoCliente = tipoClienteBean.buscarPorId(Long.parseLong(Constantes.CODIGO_TIPO_CLIENTE_BBVA));
			
			if (expediente.getClienteNatural().getTipoCliente().getId() != tipoCliente.getId()) {
				/*Nacar nacar = new Nacar (); 
			    ClienteHarecVO clienteNacar = null; 
				try {
				    clienteNacar = nacar.buscarCliente(expediente.getClienteNatural().getTipoDoi().getCodigoTipoDoi(), expediente.getClienteNatural().getNumDoi()); 
				} catch (Throwable e) { 
					 //logger.error(e); 
				}	*/
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
	
	public String confirmar() {		
		LOG.info("Enviar a confirmación - Exp : "+expediente.getId());
		AyudaVerificacionExp ayudaVerificacionExp;
		DatosProducto2MB datosProducto = null;
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		if (esValido()) {
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "documentosEscaneados");            
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				ayudaVerificacionExp=new AyudaVerificacionExp();
				
				datosProducto = (DatosProducto2MB)  
						 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto2");	
				
				// se obtiene el expedeinte de sesion
				Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);			
				ClienteNatural clienteNatural = expediente.getClienteNatural();
				
				// se actualiza con los datos ingresados por el usuario
				datosProducto.copiarDatos(clienteNatural);
				
			    grabarExp(Constantes.ACCION_BOTON_NO_CONFORME,
						  Constantes.ESTADO_EN_VERIFICACION_TAREA_32);				
				addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
				addObjectSession(Constantes.ID_EXPEDIENTE_SESION, expediente.getId());
				msgGuiaDocumentaria = false;
				//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";	
				return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				
			//	ExpedienteDTO objExpedienteDTO=mapearExpedienteAExpedienteDTO();
			//	LOG.info("Validación Linea Maxima");
				/*Perfil objPerfil= ayudaVerificacionExp.lineaMaximaValidacion(objExpedienteDTO);
				if(objPerfil!=null){
					if(empleadoBean.buscarPorPerfilEmpleadoActivo(objPerfil.getId())){
//						if(!msjOperacionOfBol)
//							if(ayudaVerificacionExp.delegacionOficinaValidacion(objExpedienteDTO)){
								LOG.info("Entra a grabar");
							    grabarExp(Constantes.ACCION_BOTON_NO_CONFORME,
										  Constantes.ESTADO_EN_VERIFICACION_TAREA_32);				
								addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
								addObjectSession(Constantes.ID_EXPEDIENTE_SESION, expediente.getId());
								msgGuiaDocumentaria = false;
								return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";									
//							}else{
//								LOG.info("Validación Delegacion Oficina es falso");
//								msjOperacionOf="No se encuentra configurado valores de Delegación oficina";
//								msjOperacionBol=true;				
//							}
					}else{
						LOG.info("No hay Perfil activo");
						msjOperacion="No hay Empleado con Perfil "+objPerfil.getDescripcion()+" activo para la Oficina "+expediente.getEmpleado().getOficina().getDescripcion();
						msjOperacionBol=true;
					}
				
				}else{
					LOG.info("Perfil es nulo");
					msjOperacion="No se encuentra configurado valores de Línea Máxima";
					msjOperacionBol=true;
				}*/
					
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
		msgPersonalizado = datosProducto.getMsgPersonalizado();
		return datProd && validoComentario;
	}
	

	/*public String devolver() {
		LOG.info("Devolver - Exp : "+expediente.getId());
		addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "observacionRechazo");
		observacionRechazoMB.init();
		observacionRechazoMB.setMostrarDialogo(true);
		return null;
	}*/	
	
	/*
	 * Llamado por el metodo aceptar de la clase ObservacionRechazoMB
	 */
	/*public String accionVentana(List<DevolucionTarea> lista) {
		
		for (DevolucionTarea devTarea : lista) {
			devolucionTareaBean.create(devTarea);
		}
		grabarExp(Constantes.ACCION_BOTON_DEVOLVER,
				Constantes.ESTADO_POR_VALIDAR_MESA_CONTROL_TAREA_32);
        
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}*/
	
	private ExpedienteDTO mapearExpedienteAExpedienteDTO(){
		ExpedienteDTO objExpedienteDTO=new ExpedienteDTO();
		objExpedienteDTO.setCodigoExpediente(expediente.getId());
		objExpedienteDTO.setCodigoProducto(expediente.getProducto().getId());
		objExpedienteDTO.setCodigoTipoMonedaSol(expediente.getExpedienteTC().getTipoMonedaSol().getId());
		//objExpedienteDTO.setCodigoTipoScoring(expediente.getExpedienteTC().getTipoScoring().getId());
		if(expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoScoring()!=null){
			objExpedienteDTO.setCodigoTipoScoring(expediente.getExpedienteTC().getTipoScoring().getId());
		}else
			LOG.info("Expediente no tiene Scoring");
		
		//objExpedienteDTO.setCodigoNivel(expediente.getEmpleado().getNivel().getId());
		if(expediente.getEmpleado()!=null && expediente.getEmpleado().getOficina()!=null){
			List<Empleado> listEmpleado = empleadoBean.buscarGerenteActivoPorOficinaPerfil(expediente.getEmpleado().getOficina().getId(), Constantes.PERFIL_GERENTE_OFICINA);
			if(listEmpleado!=null && listEmpleado.size()>0){
				if(listEmpleado.get(0).getNivel()!=null){
					objExpedienteDTO.setCodigoNivel(listEmpleado.get(0).getNivel().getId());
					LOG.info("Nivel de Empleado con id "+listEmpleado.get(0).getId()+" es "+objExpedienteDTO.getCodigoNivel());
					msjOperacionOfBol=false;
				}else{
					LOG.info("Nivel de Empleado con id "+listEmpleado.get(0).getId()+" no esta configurado");
					msjOperacionOf="No se encuentra configurado el Nivel del Empleado "+listEmpleado.get(0).getNombresCompletos()+" perteneciente a la Oficina "+expediente.getEmpleado().getOficina().getDescripcion();
					msjOperacionOfBol=true;						
				}					
			}else{
				LOG.info("No existe Gerente Activo");
				msjOperacionOf="No se encuentra Gerente Activo para la Oficina "+expediente.getEmpleado().getOficina().getDescripcion();
				msjOperacionOfBol=true;					
			}
		}else{
			LOG.info("Empleado o Oficina es nulo");
			msjOperacionOfBol=true;	
		}
		
		objExpedienteDTO.setCodigoEstadoCivilTitular(expediente.getClienteNatural().getEstadoCivil().getId());
		objExpedienteDTO.setClasificacionBanco(expediente.getExpedienteTC().getClasificacionBanco().getId());
		objExpedienteDTO.setClasificacionSbs(expediente.getExpedienteTC().getClasificacionSbs());
		objExpedienteDTO.setLineaConsumo(expediente.getExpedienteTC().getLineaConsumo());
		objExpedienteDTO.setLineaCredAprob(expediente.getExpedienteTC().getLineaCredAprob());
		objExpedienteDTO.setLineaCredSol(expediente.getExpedienteTC().getLineaCredSol());
		objExpedienteDTO.setPerExpPub(expediente.getClienteNatural().getPerExpPub());
		objExpedienteDTO.setPorcentajeEndeudamiento(expediente.getExpedienteTC().getPorcentajeEndeudamiento());
		objExpedienteDTO.setRiesgoCliente(expediente.getExpedienteTC().getRiesgoCliente());
		objExpedienteDTO.setFlagSubRogado(expediente.getClienteNatural().getSubrog());
		
		if(objExpedienteDTO.getCodigoEstadoCivilTitular().equals(Constantes.EST_CIVIL_CASADO)){
			objExpedienteDTO.setBancoConyuge(expediente.getExpedienteTC().getBancoConyuge()==null?null:expediente.getExpedienteTC().getBancoConyuge().getId());
			objExpedienteDTO.setSbsConyuge(expediente.getExpedienteTC().getSbsConyuge());
		}
		
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
		//PerfilDTO perfilDTO=null;
		
		/*Estado segun linea maxima*/
		/*if (!String.valueOf(expediente.getExpedienteTC().getTipoOferta().getId()).equals(Constantes.CODIGO_OFERTA_APROBADO)) {
			ExpedienteDTO objExpedienteDTO=mapearExpedienteAExpedienteDTO();
					//PerfilDTO perfilDTO = facade.ServiceIBMBBVA_lineaMaximaMemoria(expediente.getId(), expediente.getExpedienteTC().getTipoScoring().getId());
					perfilDTO = facade.ServiceIBMBBVA_lineaMaximaMemoria(objExpedienteDTO);
					LOG.info("perfilDTO.getId()="+perfilDTO.getId());
					if (perfilDTO.getId() == Constantes.ID_PERFIL_CONTROLLER) {
						LOG.info("expediente.getExpedienteTC().getVerifDom()="+expediente.getExpedienteTC().getVerifDom());
						if (expediente.getExpedienteTC().getVerifDom().equals(Constantes.CHECK_SELECCIONADO) || 
								expediente.getExpedienteTC().getVerifLab().equals(Constantes.CHECK_SELECCIONADO) || 
								(expediente.getProducto().getId()==Constantes.ID_APLICATIVO_PLD && expediente.getExpedienteTC().getVerifDps().equals(Constantes.CHECK_SELECCIONADO))) {
						    estadoTmp.setId(Constantes.ESTADO_EN_VERIFICACION_TAREA_3);
						    LOG.info("Exite Verificacion Domiciliaria, valor de Estado = "+estadoTmp.getId());
						}
					}else if (perfilDTO.getId() == Constantes.PERFIL_GERENTE_OFICINA) {
						estadoTmp.setId(Constantes.ESTADO_POR_APROBAR_TAREA_3);
					}else if (perfilDTO.getId() == Constantes.ID_PERFIL_ANALISIS_DE_RIESGOS) {
						estadoTmp.setId(Constantes.ESTADO_EN_ANALISIS_Y_RIESGOS_TAREA_3);
					}					

		}*/
		
		expediente.setEstado(estadoTmp);
		
		
		LOG.info("Estado :"+estadoTmp.getId());
		
		// se copia el comentario
		comentario.copiarDatosExpediente();
					
		// se actualiza en la base de datos
		expediente.getExpedienteTC().setLineaConsumo(Util.convertStringToDouble(Double.toString(expediente.getExpedienteTC().getLineaConsumo())));
        RemoteUtils objRemoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		
		/*if (Constantes.ESTADO_POR_VALIDAR_MESA_CONTROL_TAREA_32==estado) {
			Util.actualizaDevoluciones(expediente);
		}*/	
		
		//Desactivar expediente para bandeja de asignacion no muestre mensaje
		expediente.setFlagActivo("0");
		
		//Eliminar espacios en el campo Plazo Solicitado
		expediente.getExpedienteTC().setPlazoSolicitado((expediente.getExpedienteTC().getPlazoSolicitado()!=null && 
						!("").equals(expediente.getExpedienteTC().getPlazoSolicitado()))? expediente.getExpedienteTC().getPlazoSolicitado().trim(): "");
			
		expedienteBean.edit(expediente);			
		clienteNaturalBean.edit(clienteNatural);
		
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC ();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion, Integer.parseInt(""+expediente.getEstado().getId()));
		//if(perfilDTO!=null)
			//expedienteTCWPS.setIdPerfilUsuarioActual(""+perfilDTO.getId());
		
		LOG.info("setIdPerfilUsuarioActual 2: "+expedienteTCWPS.getIdPerfilUsuarioActual());
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
		ExpedienteTCWPSWeb expedienteTCWPS1=Convertidor.fromObjExpedienteTCWPSToObjExpedienteTCWPSWeb(expedienteTCWPS, "1");
		ayudaHistorial.asignarFecha(historial, expedienteTCWPS1);
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
	
	public boolean isMsgGuiaDocumentaria() {
		return msgGuiaDocumentaria;
	}

	public void setMsgGuiaDocumentaria(boolean msgGuiaDocumentaria) {
		this.msgGuiaDocumentaria = msgGuiaDocumentaria;
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

	public String getMsgPersonalizado() {
		return msgPersonalizado;
	}

	public void setMsgPersonalizado(String msgPersonalizado) {
		this.msgPersonalizado = msgPersonalizado;
	}
	
}
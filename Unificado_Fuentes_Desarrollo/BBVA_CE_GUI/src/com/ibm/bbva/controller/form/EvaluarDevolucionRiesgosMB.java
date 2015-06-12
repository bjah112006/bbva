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
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.DetalleExpediente1MB;
import com.ibm.bbva.controller.common.DocumentosEscaneadosMB;
import com.ibm.bbva.controller.common.ObservacionRechazoMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.controller.common.ProductoNuevoMB;
import com.ibm.bbva.controller.common.VerificarAprobarMB;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DevolucionTarea;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.session.CategoriaRentaBeanLocal;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.DevolucionTareaBeanLocal;
import com.ibm.bbva.session.EstadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.MensajesBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaDocumento;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "evaluarDevolucionRiesgos")
@RequestScoped
public class EvaluarDevolucionRiesgosMB extends AbstractMBean {

	@EJB
	EstadoBeanLocal estadoBean;
	@EJB
	DevolucionTareaBeanLocal devolucionTareaBean;
	@EJB
	ClienteNaturalBeanLocal clienteNaturalBean;
	@EJB
	HistorialBeanLocal historialBean;
	@EJB
	ExpedienteBeanLocal expedienteBean;
	@EJB
	CategoriaRentaBeanLocal categoriaRentaBean;
	@EJB
	private FacadeLocal facade;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	@EJB
	private MensajesBeanLocal mensajeBean;
	
	private Expediente expediente;
	private Expediente expedienteEntrada;
	private boolean msgGuiaDocumentaria;
	private boolean mostrarPanelConyuge;
	private boolean mostrarPanelDatosConyuge;
	private boolean activoAceptar;
	
	private static final Logger LOG = LoggerFactory.getLogger(EvaluarDevolucionRiesgosMB.class);
	
	public EvaluarDevolucionRiesgosMB() {
		
	}
	
	@PostConstruct
    public void init() {
		LOG.info("Entro EvaluarDevolucionRiesgos");
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);

		mostrarPanelConyuge = false;
		mostrarPanelDatosConyuge = false;
		if(expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoOferta()!=null && expediente.getExpedienteTC().getTipoOferta().getCodigo()!=null){
			
			if (String.valueOf(expediente.getExpedienteTC().getTipoOferta().getId()).equals(Constantes.CODIGO_OFERTA_APROBADO)
					&& (expediente.getClienteNatural().getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CASADO 
					||  expediente.getClienteNatural().getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CONVIVIENTE)) {
			   mostrarPanelConyuge = true;
			   
			   if (expediente.getExpedienteTC().getClienteNaturalConyuge()!=null && expediente.getExpedienteTC().getClienteNaturalConyuge().getId()>0) {
			       mostrarPanelDatosConyuge = true;			  
			   }
			}
			
		}else{
			//LOG.info("TIPO DE OFERTA ES NULL");
			//LOG.info("CODIGO DE EXPEDIENTE= "+ expediente.getId());
		}
		
		boolean pautaClas=false;
		if(expediente.getExpedienteTC().getTipoOferta().getCodigo().equals(Constantes.CODIGO_OFERTA_APROBADO)){
			pautaClas = facade.ServiceIBMBBVA_pautasClasificacionMemoria(
					expediente.getExpedienteTC().getTipoOferta().getId(), 
					Integer.valueOf(String.valueOf(expediente.getClienteNatural().getEstadoCivil().getId())), 
					expediente.getClienteNatural().getPersona().getId(), 
					(expediente.getExpedienteTC().getClienteNaturalConyuge()==null?0:expediente.getExpedienteTC().getClienteNaturalConyuge().getId()), 
					expediente.getExpedienteTC().getClasificacionSbs(), 
					expediente.getExpedienteTC().getSbsConyuge());

			if (!pautaClas) {
			 	activoAceptar=false;
			}else{
			 	activoAceptar=true;
			}			
		}else
			activoAceptar=true;
	}
	
	public String aceptar () {	
		LOG.info("Solicitar actualizacion de scoring - Exp : "+expediente.getId());
		FacesContext ctx = FacesContext.getCurrentInstance();  
		
		if (esValido()) {			
			/*Valida si cumple con guia documentaria*/
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "documentosEscaneados");
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				msgGuiaDocumentaria = false;
				
				if (expediente.getExpedienteTC().getFlagModifScore().equals(Constantes.CHECK_SELECCIONADO)) {
					actualizar(Constantes.ACCION_BOTON_SOLICITAR_ACTUALIZACION_SCORING, Constantes.ESTADO_CAMBIO_A_EN_TRAMITE_TAREA_15);	
				}else{
					ExpedienteTCWPSWeb expedienteTCWPS = (ExpedienteTCWPSWeb) getObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION);
					
					if ((expedienteTCWPS!=null && expedienteTCWPS.getIdPerfilUsuarioAnterior()!=null) && (expedienteTCWPS.getIdPerfilUsuarioAnterior().equals(String.valueOf(Constantes.ID_PERFIL_SUPERIOR_RIESGOS)) ||
							expedienteTCWPS.getIdPerfilUsuarioAnterior().equals(String.valueOf(Constantes.ID_ANALISTA_RIESGOS)))) {
							actualizar(Constantes.ACCION_BOTON_SOLICITAR_ACTUALIZACION_SCORING, Constantes.ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_15);
					}else{
						actualizar(Constantes.ACCION_BOTON_SOLICITAR_ACTUALIZACION_SCORING, Constantes.ESTADO_POR_APROBAR_TAREA_15);
					}
				}
			}else{
				msgGuiaDocumentaria = true;
				return null;
			}
			
			String msg = obtenerMensajeBD(Constantes.ID_MENSAJE_EXPEDIENTE_CORREGIDO, Constantes.DESCRIPCION_MENSAJE_CORREGIDO);
			addObjectSession(Constantes.DESCRIPCION_MENSAJE_CORREGIDO, msg);
			
			addObjectSession(Constantes.TIPO_MENSAJE, Constantes.TIPO_MENSAJE_EVALUAR_DEVOLUCION_RIESGOS);
			addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
			addObjectSession(Constantes.ID_EXPEDIENTE_SESION, String.valueOf(expediente.getId()));
			return "/mensajes/formMensajes?faces-redirect=true";
		}
		return null;
	}
	
	private String obtenerMensajeBD(String idMensaje, String idSesion) {
		String mensajeBD = obtenerMensajeSesion(idSesion);
		if (mensajeBD.trim().equals("")) {
			try {
				com.ibm.bbva.entities.Mensajes mensajeSinFiltro = new com.ibm.bbva.entities.Mensajes();
				mensajeSinFiltro = mensajeBean.buscarPorId(Long.valueOf(idMensaje));
				mensajeBD = new String(mensajeSinFiltro.getContenido());
				LOG.info("MensajeBD: " + mensajeBD);
			} catch (Exception e) {
				mensajeBD = Mensajes.getMensaje("com.ibm.bbva.mensajes.formMensajes.evaluarDevolucionRiesgos");
			}
		}		
		return mensajeBD;
	}
	
	private String obtenerMensajeSesion(String idSesion) {
		Object objMensaje = getObjectSession(idSesion);
		return (objMensaje == null || ((String)objMensaje).trim().equals("")) ? "" : (String)objMensaje;
	}
	
	public String cancelarOperacion () {
		LOG.info("Rechazar expediente - Exp : "+expediente.getId());
		if (esValido()) {
			// si el estado del expediente es diferente a prestamo formalizado 
			if (!Constantes.CODIGO_PRESTAMO_FORMALIZADO_TAREA15.equals(expediente.getEstado().getCodigo())) {
				ELContext elContext = FacesContext.getCurrentInstance().getELContext();
				addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, 1);
				//addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
				ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
						.getCurrentInstance().getApplication().getELResolver()
						.getValue(elContext, null, "observacionRechazo");
				observacionRechazoMB.init();
				observacionRechazoMB.setMostrarDialogo(true);
				observacionRechazoMB.setTituloVentana(Mensajes.getMensaje("com.ibm.bbva.common.observacionRechazo.titulo4"));
				//observacionRechazo.setTituloVentana(Mensajes.getMensaje("com.ibm.bbva.common.observacionRechazo.titulo3"));
			}
		}
		
		return null;
	}

	private void actualizar(String accion, Integer estado) {
		
        FacesContext ctx = FacesContext.getCurrentInstance();
		
		DetalleExpediente1MB detalleExpediente1 = null;
		ProductoNuevoMB productoNuevo = null;
		VerificarAprobarMB verificarAprobar = null;
		ComentarioMB comentario = null;
		PanelDocumentosMB panelDocumentoMB = null;
				
		detalleExpediente1 = (DetalleExpediente1MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "detalleExpediente1");
		
		productoNuevo = (ProductoNuevoMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");
		
		expediente.setAccion(accion);
		Estado estadoTmp = new Estado();
		estadoTmp.setId(estado.longValue());		
		expediente.setEstado(estadoTmp);
		
		ClienteNatural clienteNatural = detalleExpediente1.obtenerClienteNatural();
		expediente.setClienteNatural(clienteNatural);
		
		ClienteNatural clienteNaturalConyuge = expediente.getExpedienteTC().getClienteNaturalConyuge();
		expediente.getExpedienteTC().setClienteNaturalConyuge(clienteNaturalConyuge);
		
		productoNuevo.copiarDatosExpediente();

		verificarAprobar = (VerificarAprobarMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");		
		
		verificarAprobar.copiarDatosExpediente();
		
		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
		
		comentario.copiarDatosExpediente();
		
		if (clienteNatural.getCategoriasRenta().size() == 0) {
			clienteNatural.setCategoriasRenta(null);
		}

		// se actualiza los datos del cliente
		clienteNaturalBean.edit(clienteNatural);
		
		// se actualiza los datos del cliente conyuge
		clienteNaturalBean.edit(clienteNaturalConyuge);
		
		// se actualiza el expediente
        RemoteUtils objRemoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		expedienteBean.edit(expediente);

		//process
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion,	estado);

		Long idEmpAnt = (Long) getObjectSession(Constantes.ID_EMPLEADO_ESTADO_ANTERIOR_SESION);
		expedienteTCWPS.setDevueltoPor(String.valueOf(idEmpAnt));
		expedienteTCWPS.setModificacionScoring(expediente.getExpedienteTC().getFlagModifScore());
		
		String tkiid = expedienteTCWPS.getTaskID();		
		//objRemoteUtils.enviarExpedienteTC(tkiid, expedienteTCWPS);
		
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
        
		//fin process
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
		
		/*Adjunta Documentos Expediente*/
		AyudaDocumento ayudaDocumento = new AyudaDocumento();		
		ayudaDocumento.adjuntarDocumentoExpediente();
		
		ayudaDocumento.editarDocumentoExpediente();
		
		removeObjectSession(Constantes.LISTA_DOC_EXP_ADJ);
		removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
		
		//todos los documentos se actualizan como no observados
        panelDocumentoMB = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
        panelDocumentoMB.actualizarNoObservados();
	}

	// Ejecutado por ObservacionRechazoMB
	public String accionVentana(ArrayList<DevolucionTarea> lista) {
		
		
		for (DevolucionTarea devTarea : lista) {
			devolucionTareaBean.create(devTarea);
		}		
		expediente.setMotivoDevolucion(lista.get(0).getMotivoDevolucion());
		/*
		 * Se Agrego
		 * 
		 * */
		expediente.getExpedienteTC().setComentarioRechazo(lista.get(0).getExpediente().getExpedienteTC().getComentarioRechazo());
		LOG.info("COMENTARIO RECHAZO:" + expediente.getExpedienteTC().getComentarioRechazo());
		
		actualizar(Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE, Constantes.ESTADO_RECHAZADO_TAREA_15);
		
		
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}

	private boolean esValido() {
        FacesContext ctx = FacesContext.getCurrentInstance();
		
		DetalleExpediente1MB detalleExpediente1 = null;
		ProductoNuevoMB producto = null;
		ComentarioMB comentario = null;
				
		detalleExpediente1 = (DetalleExpediente1MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "detalleExpediente1");

		producto = (ProductoNuevoMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");

		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
	
		detalleExpediente1.setCodigoTipoOferta(producto.getCodTipoOferta());
		
		boolean detExp = detalleExpediente1.esValido();		
		boolean prodNue = producto.esValido();
		boolean validoComentario = comentario.esValido();
		
		return detExp && prodNue && validoComentario;
	}	
	
	public boolean isMsgGuiaDocumentaria() {
		return msgGuiaDocumentaria;
	}

	public void setMsgGuiaDocumentaria(boolean msgGuiaDocumentaria) {
		this.msgGuiaDocumentaria = msgGuiaDocumentaria;
	}
	
	public boolean isMostrarPanelConyuge() {
		return mostrarPanelConyuge;
	}

	public void setMostrarPanelConyuge(boolean mostrarPanelConyuge) {
		this.mostrarPanelConyuge = mostrarPanelConyuge;
	}

	public boolean isMostrarPanelDatosConyuge() {
		return mostrarPanelDatosConyuge;
	}

	public void setMostrarPanelDatosConyuge(boolean mostrarPanelDatosConyuge) {
		this.mostrarPanelDatosConyuge = mostrarPanelDatosConyuge;
	}	
	
	public void visualizarPanelConyuge (boolean isVisible) {
		mostrarPanelConyuge = isVisible;
	}
	
	public void visualizarDatosPanelConyuge (boolean isVisible) {
		mostrarPanelDatosConyuge = isVisible;
	}

	public boolean isActivoAceptar() {
		return activoAceptar;
	}

	public void setActivoAceptar(boolean activoAceptar) {
		this.activoAceptar = activoAceptar;
	}
}
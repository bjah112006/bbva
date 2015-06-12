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
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.ObservacionRechazoMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.controller.common.VerificarAprobarMB;
import com.ibm.bbva.entities.DevolucionTarea;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.MotivoDevolucion;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.session.DevolucionTareaBeanLocal;
import com.ibm.bbva.session.EstadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.MotivoDevolucionBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaDocumento;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "verificarConformidadExpediente")
@RequestScoped
public class VerificarConformidadExpedienteMB extends AbstractMBean {
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private HistorialBeanLocal historialBean;
	@EJB
	private EstadoBeanLocal estadoBean;
	@EJB
	private DevolucionTareaBeanLocal devolucionTareaBean;
	@EJB
	private MotivoDevolucionBeanLocal motivoDevolucionBean;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	
	private boolean disabled = false;
	private Expediente expediente;
	private Expediente expedienteEntrada;
	private String titulo = "";
	private boolean msgGuiaDocumentaria;

	private static final Logger LOG = LoggerFactory.getLogger(VerificarConformidadExpedienteMB.class);
	
	public VerificarConformidadExpedienteMB() {
		LOG.info("VerificarConformidadExpedienteMB");
	}
	
	@PostConstruct
    public void init() {	
		LOG.info("Entro VerificarConformidadExpediente");
		//se obtiene el expedeinte de sesion, recuperado de la base de datos
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		//objeto donde se almacena los cambios en el expediente
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
	}

	public String aceptar() {	
		LOG.info("Confirmar validacion - Exp : "+expediente.getId());
		FacesContext ctx = FacesContext.getCurrentInstance(); 
		PanelDocumentosMB panelDocumentos=null;
		if (esValido()) {
			LOG.info("Es valido");
			panelDocumentos = (PanelDocumentosMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");	
			
		//	ELContext elContext = FacesContext.getCurrentInstance().getELContext();
	//		DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
	//				.getCurrentInstance().getApplication().getELResolver()
	//				.getValue(elContext, null, "documentosEscaneados");
			//if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
			if(panelDocumentos.validarDocumentos()){
				LOG.info("Documentos Obligatorios OK");
				LOG.info("Inicio de Grabación");
				grabarExp(Constantes.ACCION_BOTON_CONFIRMAR_VALIDACION, Constantes.ESTADO_POR_REALIZAR_ALTA_TAREA_2);
				LOG.info("Fin de Grabación");
				addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
				addObjectSession(Constantes.ID_EXPEDIENTE_SESION, String.valueOf(expediente.getId()));
				msgGuiaDocumentaria = false;
				//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
				return "/moverArchivos/formMoverArchivos?faces-redirect=true";
			} else {
				LOG.info("VALIDAR DOCUMENTO FALSEEEE!!!");
				msgGuiaDocumentaria = true;
			}
		}
		return null;
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

	public String grabarExp(String accion, Integer estado) {
		//Se obtiene las Verificaciones
		FacesContext ctx = FacesContext.getCurrentInstance();  
		VerificarAprobarMB verificacion = null;
		ComentarioMB comentario = null;
		PanelDocumentosMB panelDocumentoMB = null;

		verificacion = (VerificarAprobarMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");

		//se obtiene el comentario
		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
		
		/* Se copia Verificacion */
		verificacion.copiarDatosExpediente();

		// se copia el comentario
		comentario.copiarDatosExpediente();
		LOG.info("expediente buro="+expediente.getExpedienteTC().getTipoBuro());
		LOG.info("scor="+expediente.getExpedienteTC().getTipoScoring());
		LOG.info("expediente comentario rechazo="+expediente.getExpedienteTC().getComentarioRechazo());
		/* Se actualiza expediente */
        RemoteUtils objRemoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		expediente.setAccion(accion);		
		Estado estadoTmp = new Estado();
		estadoTmp.setId(estado.longValue());
		expediente.setEstado(estadoTmp);		
		
		if (Constantes.ESTADO_EN_REGISTRO_TAREA_2==estado) {
			Util.actualizaDevoluciones(expediente);
		}	
		
		expedienteBean.edit(expediente);		

		//process
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion,	estado);

		String tkiid = expedienteTCWPS.getTaskID();	
		
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
		
		//expedienteTCWPS.getEn
		//objRemoteUtils.enviarExpedienteTC(tkiid, expedienteTCWPS);
		objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
		/**
		 * Se agrego 14 marzo 2014
		 * */
		/*Consulta consulta = new Consulta();
		
		List<String> listUsuarios=new ArrayList<String>();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		listUsuarios.add(empleado.getCodigo()); 
		consulta.setUsuarios(listUsuarios);
		consulta.setConsiderarUsuarios(true);
		
		LOG.info("Usuario es codigo = "+empleado.getCodigo());
		LOG.info("setConsiderarUsuarios es = "+consulta.isConsiderarUsuarios());
		
		ayudaExpedienteTC.actualizarListaExpedienteTC(consulta);*/
		/**
		 * fin de cambios
		 * */		
		ayudaExpedienteTC.actualizarListaExpedienteTC(new Consulta());
        
		//fin process
				//se almacena en el historial
		Estado estadoTmp2 = new Estado();
		estadoTmp2.setId(expedienteEntrada.getEstado().getId());
		expediente.setEstado(estadoTmp2);		
		
		LOG.info("Estado 2 :"+estadoTmp2.getId());
		MotivoDevolucion motivoDevolucion = (MotivoDevolucion) getObjectSession(Constantes.MOTIVO_DEVOLUCION_TAREA_SESION);
		if (motivoDevolucion!=null){
			LOG.info("Motivo Devolucion Id:" + motivoDevolucion.getId());
			LOG.info("Motivo Devolucion Codigo:" + motivoDevolucion.getCodigo());
			LOG.info("Motivo Devolucion Descripcion:" + motivoDevolucion.getDescripcion());
		}
		
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
		historial.setMotivoDevolucion(motivoDevolucion);
		
		AyudaHistorial ayudaHistorial = new AyudaHistorial();		
		ayudaHistorial.asignarFecha(historial, expedienteTCWPS);
		int ans=0;
		/*INICIO REQUERIMIENTO 286 27.11.2014*/
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
		
		// se remueve el expediente de sesion
		removeObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		removeObjectSession(Constantes.EXPEDIENTE_SESION);
		removeObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION);
		return "";
	}

	/*
	 * Llamado por el metodo aceptar de la clase ObservacionRechazoMB
	 */
	public String accionVentana(List<DevolucionTarea> lista) {
		expediente.setMotivoDevolucion(lista.get(0).getMotivoDevolucion());
		expediente.getExpedienteTC().setComentarioRechazo(lista.get(0).getExpediente().getExpedienteTC().getComentarioRechazo());
		LOG.info("COMENTARIO RECHAZO:" + expediente.getExpedienteTC().getComentarioRechazo());
		
		for (DevolucionTarea devTarea : lista) {
			LOG.info("Motivo Devolucion Id:" + devTarea.getMotivoDevolucion().getId());
			LOG.info("Motivo Devolucion Codigo:" + devTarea.getMotivoDevolucion().getCodigo());
			LOG.info("Motivo Devolucion Descripcion:" + devTarea.getMotivoDevolucion().getDescripcion());
			addObjectSession(Constantes.MOTIVO_DEVOLUCION_TAREA_SESION,devTarea.getMotivoDevolucion());
			devolucionTareaBean.create(devTarea);
		}
		
		
		grabarExp(Constantes.ACCION_BOTON_DEVOLVER,
				Constantes.ESTADO_EN_REGISTRO_TAREA_2);
        
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}

	private boolean esValido() {
		ComentarioMB comentario = (ComentarioMB) getObjectRequest("comentario");
		boolean validoComentario = comentario.esValido();
		return validoComentario;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isMsgGuiaDocumentaria() {
		return msgGuiaDocumentaria;
	}

	public void setMsgGuiaDocumentaria(boolean msgGuiaDocumentaria) {
		this.msgGuiaDocumentaria = msgGuiaDocumentaria;
	}
}

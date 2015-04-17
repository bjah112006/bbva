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
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bpd.RemoteUtils;
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.DatosProducto3MB;
import com.ibm.bbva.controller.common.DocumentosEscaneadosMB;
import com.ibm.bbva.controller.common.EnvioMailMB;
import com.ibm.bbva.controller.common.ObservacionMB;
import com.ibm.bbva.controller.common.ObservacionRechazoMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.entities.DevolucionTarea;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.session.DevolucionTareaBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaDocumento;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "aprobarExpediente")
@RequestScoped
public class AprobarExpedienteMB extends AbstractMBean {

	@EJB
	DevolucionTareaBeanLocal devolucionTareaBean; 
	@EJB
	ExpedienteBeanLocal expedienteBean;
	@EJB
	HistorialBeanLocal historialBean;
	@EJB
	private FacadeLocal facade;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	
	private Expediente expediente;
	private Expediente expedienteEntrada;
	private String accion;
	private Integer estado;
	private boolean msgGuiaDocumentaria;
	private boolean activoAprOperacion;
	private boolean activoDevolver;
	private boolean activoRecExpediente;
	private boolean activoActScoring;
	private boolean activoAproModificacion;
	private boolean activoEleRies;
	
	private String msjOperacion; 
	private boolean msjOperacionBol;

	private static final Logger LOG = LoggerFactory.getLogger(AprobarExpedienteMB.class);
	
	public boolean isActivoEleRies() {
		return activoEleRies;
	}

	public void setActivoEleRies(boolean activoEleRies) {
		this.activoEleRies = activoEleRies;
	}

	public AprobarExpedienteMB() {
		
	}

	@PostConstruct
    public void init() {
		LOG.info("Entro AprobarExpediente");
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);	
		
        Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		
		if (empleado.getCodigo().equals(String.valueOf(Constantes.PERFIL_GESTOR_PLATAFORMA))){		
			activoAprOperacion = true;
			activoDevolver = true;
			activoRecExpediente = true;
			activoActScoring = true;
			activoAproModificacion = true;
		}
	}
	
	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String aprobar() {
		LOG.info("Aprobar Operacion - Exp : "+expediente.getId());
		FacesContext ctx = FacesContext.getCurrentInstance();
		DatosProducto3MB datosProducto3MB = null;
		ComentarioMB comentarioMB = null;
		
		datosProducto3MB = (DatosProducto3MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");		

		addObjectSession(Constantes.ACCION_SESION, Constantes.ACCION_BOTON_APROBAR_OPERACION);
		
		if (esValido()) {
			//Valida si cumple con guia documentaria
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "documentosEscaneados");
			
			TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
			datosProducto3MB.copiarDatosExpediente();
			
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				msgGuiaDocumentaria = false;
				//if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo())) {
					// se copia los comentarios al expediente de sesion
					comentarioMB = (ComentarioMB)  
							 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");		
					
					comentarioMB.copiarDatosExpediente();					
					// guardar estado expediente
					actualizar(Constantes.ACCION_BOTON_APROBAR_OPERACION, Constantes.ESTADO_APROBADO_TAREA_4);
					addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
					addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
					//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
					return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				//} else {					
				//	actualizar(Constantes.ACCION_BOTON_DEVOLVER_GESTOR, Constantes.ESTADO_DERIVADO_POR_GERENTE_TAREA_4);
				//	return "bandeja";
					/*TipoResolucionMB tipoResolucionMB = new TipoResolucionMB();
					tipoResolucionMB.setMostrarDialogo(true);
					addObjectRequest("tipoResolucion", tipoResolucionMB);*/
					// se muestra la ventana y si el usuario selecciona la opcion Aceptar se ejecuta
					// el metodo guardarOfertaNoAprobado()
				//}				
			}else{
				msgGuiaDocumentaria = true;
				return null;
			}
		}
		return null;
	}
	
	/* Llamado por TipoResolucionMB para seleccionar una opcion opcion 
	 * Observado por gerente o Aprobado sin modificación
	 */
	public void guardarOfertaNoAprobado (String accion, Integer estado) {
		actualizar(accion, estado);
	}
	
	public String devolver () {
		LOG.info("Devolver - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		
		if (esValido()) {
			datosProducto3MB.copiarDatosExpediente();
			
			//addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, Constantes.CODIGO_BOTON_DEVOLVER_AA_CU4);
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			addObjectSession("actualizarObservados", "OK");
			accion = Constantes.ACCION_BOTON_DEVOLVER;
			estado = Constantes.ESTADO_POR_SUBSANAR_TAREA_4;
		}
		
		return null;
	}
	
	public String aprobarConModificacion () {
		LOG.info("Aprobar con Modificacion - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		
		addObjectSession(Constantes.ACCION_SESION, Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS);
		
		if (esValido()) {
			datosProducto3MB.copiarDatosExpediente();
			
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, Constantes.CODIGO_BOTON_DEVOLVER_GESTOR_CU4);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			addObjectSession("actualizarObservados", "OK");
			accion = Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS;
			estado = Constantes.ESTADO_APROBADO_CON_MODIFICACION_TAREA_4;
		}
		
		return null;
	}
	
	public String denegar() {
		LOG.info("Rechazar Expediente - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		if (esValido()) {
			datosProducto3MB.copiarDatosExpediente();
			/*
			ObservacionMB observacionMB = (ObservacionMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacion");
			observacionMB.setMostrarDialogo(true);
			String titulo = Mensajes.getMensaje("com.ibm.bbva.aprobarExpediente.formAprobarExpediente.observacion.tituloRechazo");
			observacionMB.setTitulo(titulo);*/
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, 1);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			observacionRechazoMB.setTituloVentana(Mensajes.getMensaje("com.ibm.bbva.common.observacionRechazo.titulo4"));
			
			accion = Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE;
			estado = Constantes.ESTADO_RECHAZADO_TAREA_4;
		}
		return null;
	}
	
	public String enviarRiesgo() {
		LOG.info("Elevar a Riesgo - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		
		addObjectSession(Constantes.ACCION_SESION, Constantes.ACCION_BOTON_ELEVAR_A_RIESGOS);
		
		if (esValido()) {
			datosProducto3MB.copiarDatosExpediente();
			
			ObservacionMB observacionMB = (ObservacionMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacion");
			observacionMB.setMostrarDialogo(true);
			String titulo = Mensajes.getMensaje("com.ibm.bbva.aprobarExpediente.formAprobarExpediente.observacion.tituloExclusion");
			observacionMB.setTitulo(titulo);
			accion = Constantes.ACCION_BOTON_ELEVAR_A_RIESGOS;
			estado = Constantes.ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_4;
		}
		return null;
	}

	/* (Devolver) Llamado por el metodo aceptar de la clase ObservacionRechazoMB
	 */
	public String accionVentana(List<DevolucionTarea> lista) {
		// se copia los comentarios al expediente de sesion
		expediente.setMotivoDevolucion(lista.get(0).getMotivoDevolucion());
		
		/*
		 * Se Agrego
		 * 
		 * */
		expediente.getExpedienteTC().setComentarioRechazo(lista.get(0).getExpediente().getExpedienteTC().getComentarioRechazo());
		LOG.info("COMENTARIO RECHAZO:" + expediente.getExpedienteTC().getComentarioRechazo());
		
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		ComentarioMB comentarioMB = null;
		
		comentarioMB = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
		comentarioMB.copiarDatosExpediente();
		
		Integer valorAccion=(Integer) getObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO);
		LOG.info("valorAccion::::"+valorAccion);
		if(valorAccion!=null && valorAccion==1){//RECHAZAR
			LOG.info("RECHAZO");
		}else{
			for (DevolucionTarea devTarea : lista) {
				devolucionTareaBean.create(devTarea);
			}			
		}	

		actualizar(accion, estado);		
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}
	
	/* Llamado por el metodo aceptar de la clase ObservacionMB
	 */
	public void accionVentObs (String observacion) {
		expediente.getExpedienteTC().setComentarioRechazo(observacion);
		// se copia los comentarios al expediente de sesion		
		FacesContext ctx = FacesContext.getCurrentInstance();		
		ComentarioMB comentarioMB = null;
				
		comentarioMB = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
		
		comentarioMB.copiarDatosExpediente();

		actualizar(accion, estado);
	}

	public String aprobarConObservacion () {
		LOG.info("Aprobar con Observacion - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		
		if (esValido()) {
			datosProducto3MB.copiarDatosExpediente();
			
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, Constantes.CODIGO_BOTON_DEVOLVER_GESTOR_CU4);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			accion = Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS;
			estado = Constantes.ESTADO_APROBADO_CON_OBSERVACION_TAREA_4;			
		}
		
		return null;
	}	
	
	private void actualizar (String accion, Integer estado) {
		// se copia los comentarios al expediente de sesion
		FacesContext ctx = FacesContext.getCurrentInstance();		
		ComentarioMB comentarioMB = null;
		PanelDocumentosMB panelDocumentoMB = null;
				
		comentarioMB = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
		comentarioMB.copiarDatosExpediente();
		
		// se guarda el expediente
		expediente.setAccion(accion);
		Estado estadoTmp = new Estado();
		estadoTmp.setId(estado);
		expediente.setEstado(estadoTmp);
        RemoteUtils objRemoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		LOG.info("Estado 1 :"+estadoTmp.getId());
		
		if (Constantes.ESTADO_POR_SUBSANAR_TAREA_4==estado) {
			Util.actualizaDevoluciones(expediente);
		}
		
		LOG.info("accion  :::: "+accion);
		if(accion.equals(Constantes.ACCION_BOTON_ELEVAR_A_RIESGOS)){
			expediente.getExpedienteTC().setFlagExcDelegacion("1");
		}			
		
		AyudaHistorial ayudaHistorial = new AyudaHistorial();
		
		/*Si el expediente es una retraccion*/
		Historial hretraer = null;
		if (expediente.getExpedienteTC().getFlagRetraer().equals(Constantes.FLAGRETRAERR)) {			
		    hretraer = ayudaHistorial.actualizaRetraer(expediente.getId(), expediente.getExpedienteTC().getFlagRetraer());
		    expediente.getExpedienteTC().setFlagRetraer(Constantes.FLAGRETRAERD);
		}
		
		if (!(accion.equals(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS) || accion.equals(Constantes.ACCION_BOTON_APROBAR_OPERACION))) {
			LOG.info("No Actualiza campos TipoMonedaAprob-LineaCredAprob-PlazoSolicitadoApr");
			expediente.getExpedienteTC().setTipoMonedaAprob(expedienteEntrada.getExpedienteTC().getTipoMonedaAprob());
			expediente.getExpedienteTC().setLineaCredAprob(expedienteEntrada.getExpedienteTC().getLineaCredAprob());
			expediente.getExpedienteTC().setPlazoSolicitadoApr(expedienteEntrada.getExpedienteTC().getPlazoSolicitadoApr());
		}
		
		LOG.info("estado  :::: "+estado);
		
		if(accion.equals(Constantes.ACCION_BOTON_APROBAR_OPERACION))
			expediente.getExpedienteTC().setTipoResolucion(Constantes.RESOLUCION_SIN_MODIFICACION);
		else{
			if(accion.equals(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS) && 
					Constantes.ESTADO_APROBADO_CON_MODIFICACION_TAREA_4==estado)
					expediente.getExpedienteTC().setTipoResolucion(Constantes.RESOLUCION_CON_MODIFICACION);
		}
		
        LOG.info("TipoMonedaAprob : "+expediente.getExpedienteTC().getTipoMonedaAprob());
        LOG.info("LineaCredAprob : "+expediente.getExpedienteTC().getLineaCredAprob());
        LOG.info("PlazoSolicitadoApr : "+expediente.getExpedienteTC().getPlazoSolicitadoApr());
      
        expedienteBean.edit(expediente);
		
		//process
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion,	estado);

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
		/**
		 * Se agrego 14 marzo 2014
		 * */
		/*Consulta consulta = new Consulta();
		
		List<String> listUsuarios=new ArrayList<String>();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		listUsuarios.add(empleado.getCodigo()); 
		consulta.setUsuarios(listUsuarios);
		consulta.setConsiderarUsuarios(true);
		
		
		ayudaExpedienteTC.actualizarListaExpedienteTC(consulta);*/
		/**
		 * fin de cambios
		 * */			
		ayudaExpedienteTC.actualizarListaExpedienteTC(new Consulta());
        
		//fin process
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
		// se guarda el historial
		Estado estadoTmp2 = new Estado();
		estadoTmp2.setId(expedienteEntrada.getEstado().getId());
		expediente.setEstado(estadoTmp2);		
		
		LOG.info("Estado 2 :"+estadoTmp2.getId());
				
		Historial historial = ConvertExpediente.convertToHistorialVO(expediente);
		
		/*Si el expediente es una retraccion*/
		if (hretraer!=null) {
			historial.setFechaT1R(hretraer.getFechaT1());
			historial.setFechaT2R(hretraer.getFechaT2());
			historial.setFechaT3R(hretraer.getFechaT3());
			historial.setTiempoColar(ConvertExpediente.calculoTiempoCola(expediente.getEmpleado().getOficina().getId(),historial.getFechaT1R(),historial.getFechaT2R()));
			historial.setTiempoEjecucionr(ConvertExpediente.calculoTiempoEjecucion(historial.getFechaT2R(), historial.getFechaT3R()));
			historial.setTiempoColaRetraer(ConvertExpediente.calculoTiempoCola(expediente.getEmpleado().getOficina().getId(),historial.getFechaT3R(),historial.getFechaT1()));
		}

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
		
		ayudaHistorial.asignarFecha(historial, expedienteTCWPS);
		
		historialBean.create(historial);
		
		//Adjunta Documentos Expediente		
		AyudaDocumento ayudaDocumento = new AyudaDocumento();
		ayudaDocumento.adjuntarDocumentoExpediente();
		
		ayudaDocumento.editarDocumentoExpediente();
		
		removeObjectSession(Constantes.LISTA_DOC_EXP_ADJ);
		removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
        removeObjectSession(Constantes.ACCION_SESION);
        
        //todos los documentos se actualizan como no observados
        panelDocumentoMB = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
        panelDocumentoMB.actualizarNoObservados();
	}

	private boolean esValido() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		DatosProducto3MB datosProducto3MB = null;		
		ComentarioMB comentario = null;
		
		datosProducto3MB = (DatosProducto3MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");	
		
		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");	
		
		boolean validoDatosProd = datosProducto3MB.esValido();		
		boolean validoComentario = comentario.esValido();
		return validoComentario && validoDatosProd;
	}
	
	public boolean isMsgGuiaDocumentaria() {
		return msgGuiaDocumentaria;
	}

	public void setMsgGuiaDocumentaria(boolean msgGuiaDocumentaria) {
		this.msgGuiaDocumentaria = msgGuiaDocumentaria;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	
	public String actualizaScoring() {
		LOG.info("Actualizar Scoring - Exp : "+expediente.getId());
		FacesContext ctx = FacesContext.getCurrentInstance();		
		ComentarioMB comentarioMB = null;		
		comentarioMB = (ComentarioMB)  
		ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");		
					
		comentarioMB.copiarDatosExpediente();					
		// guardar estado expediente
		actualizar(Constantes.ACCION_BOTON_SOLICITAR_ACTUALIZACION_SCORING, Constantes.ESTADO_CAMBIO_A_EN_TRAMITE_TAREA_4);
		addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
		return "/moverArchivos/formMoverArchivos?faces-redirect=true";
	}
	
	public boolean isActivoAprOperacion() {
		return activoAprOperacion;
	}

	public void setActivoAprOperacion(boolean activoAprOperacion) {
		this.activoAprOperacion = activoAprOperacion;
	}

	public boolean isActivoDevolver() {
		return activoDevolver;
	}

	public void setActivoDevolver(boolean activoDevolver) {
		this.activoDevolver = activoDevolver;
	}

	public boolean isActivoRecExpediente() {
		return activoRecExpediente;
	}

	public void setActivoRecExpediente(boolean activoRecExpediente) {
		this.activoRecExpediente = activoRecExpediente;
	}

	public boolean isActivoActScoring() {
		return activoActScoring;
	}

	public void setActivoActScoring(boolean activoActScoring) {
		this.activoActScoring = activoActScoring;
	}

	public boolean isActivoAproModificacion() {
		return activoAproModificacion;
	}

	public void setActivoAproModificacion(boolean activoAproModificacion) {
		this.activoAproModificacion = activoAproModificacion;
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
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
import com.ibm.bbva.controller.ConstantesAdmin;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.DatosProducto3MB;
import com.ibm.bbva.controller.common.DocumentosEscaneadosMB;
import com.ibm.bbva.controller.common.ObservacionMB;
import com.ibm.bbva.controller.common.ObservacionRechazoMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.controller.common.VerificarAprobar2MB;
import com.ibm.bbva.entities.DevolucionTarea;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.TipoVerificacion;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.entities.VerificacionExp;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.session.DevolucionTareaBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.session.VerificacionExpBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaDocumento;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.AyudaPLD;
import com.ibm.bbva.util.AyudaVerificacionExp;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "verificarResultadoDomiciliaria")
@RequestScoped
public class VerificarResultadoDomiciliariaMB extends AbstractMBean {
	
	@EJB
	ExpedienteBeanLocal expedienteBean;
	@EJB
	VerificacionExpBeanLocal verificacionExpBean;
	@EJB
	DevolucionTareaBeanLocal devolucionTareaBean;
	@EJB
	HistorialBeanLocal historialBean;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	
	private VerificacionExp verificacionExp; 
	private Expediente expediente;
	private Expediente expedienteEntrada;
	private String titulo="";
	private boolean msgGuiaDocumentaria;
	private boolean botRechazarExp;
	//private boolean botDevolverGestor;
	private boolean renderedNoConforme;
	private boolean renderedResolver;
	private boolean renderedEnEspera;
	private boolean renderedObs;
	private boolean renderedDevolver;
	private boolean renderedRechazar;
	private AyudaPLD ayudaPLD;
	
	private static final Logger LOG = LoggerFactory.getLogger(VerificarResultadoDomiciliariaMB.class);
	
	public VerificarResultadoDomiciliariaMB() { 
  
	}
		
	@PostConstruct
    public void init() {
		LOG.info("Entro VerificarResultadoDomiciliaria");
		// se obtiene el expedeinte de sesion
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		ayudaPLD=new AyudaPLD();
		
		if(expediente!=null && expediente.getId()>0)
			obtenerOpcionBotones(expediente.getId());
		
		if(expediente!=null && expediente.getExpedienteTC()!=null )
			LOG.info("VALOR DE TASA ES "+expediente.getExpedienteTC().getTasaEsp() );
		else
			LOG.info("null");
		
	}
	
	public void obtenerOpcionBotones(long idExpediente){
		setRenderedNoConforme(ayudaPLD.activarOpcionNoConforme(idExpediente));
		setRenderedDevolver(!ayudaPLD.activarOpcionNoConforme(idExpediente));
	}

	public void obtenerOpcionBotonesNoConforme(){
		setRenderedNoConforme(true);
	}
	
	public String resolver() {
		LOG.info("Resolver - Exp : "+expediente.getId());
        /*Valida si cumple con guia documentaria*/
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "documentosEscaneados");	
				
		if(expediente!=null && expediente.getExpedienteTC()!=null )
			LOG.info("VALOR DE TASA ES 2 "+expediente.getExpedienteTC().getTasaEsp() );
		else
			LOG.info("null 2");
		
		if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
			if(validarDatos()) {
			int estado=0;	
			   if (expediente.getProducto().getId()==Constantes.ID_APLICATIVO_TC) {	
		           estado = Constantes.ESTADO_RESUELTO_TAREA_5;
			   }else{
				   estado = Constantes.ESTADO_DESEMBOLSO_DEL_PRESTAMO_TAREA_5;
			   }
			   grabarExp(Constantes.ACCION_BOTON_RESOLVER, estado);
		       addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
		       addObjectSession(Constantes.ID_EXPEDIENTE_SESION, String.valueOf(expediente).toString());
		       //return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";	
		       return "/moverArchivos/formMoverArchivos?faces-redirect=true";
			}
			msgGuiaDocumentaria = false;
		}else{
			msgGuiaDocumentaria = true;
			return null;
		}
		return "";
	}
	
	public String enEspera() {
		LOG.info("En espera - Exp : "+expediente.getId());
        if(validarDatos()) {
        	LOG.info("Si entro");
        	grabarExp(Constantes.ACCION_BOTON_EN_ESPERA, Constantes.ESTADO_EN_ESPERA_DE_RESPUESTA_TAREA_5);
        	addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
        	addObjectSession(Constantes.ID_EXPEDIENTE_SESION, String.valueOf(expediente.getId()));
   			//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
        	return "/moverArchivos/formMoverArchivos?faces-redirect=true";
        }else{
        	return null;
        }
	}
	
	public String noConforme() {
		LOG.info("noConforme - Exp : "+expediente.getId());
		if (validarDatos()) {			

			grabarExp(Constantes.ACCION_BOTON_NO_CONFORME, Constantes.ESTADO_POR_REALIZAR_ALTA_TAREA_6);
			addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
        	addObjectSession(Constantes.ID_EXPEDIENTE_SESION, String.valueOf(expediente.getId()));
			//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
			return "/moverArchivos/formMoverArchivos?faces-redirect=true";
		}		
		return null;
	}		
	
	public String ventanaDevolver () {
		LOG.info("Devolver - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
		ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "observacionRechazo");
		observacionRechazoMB.init();
		observacionRechazoMB.setMostrarDialogo(true);
		addObjectSession("actualizarObservados", "OK");
		//observacionRechazoMB.setTituloVentana(Mensajes.getMensaje("com.ibm.bbva.common.observacionRechazo.titulo4"));
		return null;
	}	
	
	public String ventanaObservar() {
		LOG.info("Observar verificacion - Exp : "+expediente.getId());
        /*Valida si cumple con guia documentaria*/
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "documentosEscaneados");	
				
		//if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
			if(validarDatos()) {
				grabarExp(Constantes.ACCION_BOTON_OBSERVAR_VERIFICACION, Constantes.ESTADO_POR_SUBSANAR_TAREA_5);
		       
				FacesContext ctx = FacesContext.getCurrentInstance();  
				PanelDocumentosMB panelDocumentos = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
				panelDocumentos.actualizarObservados();
		       
				addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
				addObjectSession(Constantes.ID_EXPEDIENTE_SESION, String.valueOf(expediente).toString());
				//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
				return "/moverArchivos/formMoverArchivos?faces-redirect=true";
			}
			msgGuiaDocumentaria = false;
		/*}else{
			msgGuiaDocumentaria = true;
			return null;
		}*/
		return "";
	}	
	
	public String ventanaRechazar() {
		LOG.info("Rechazar expediente - Exp : ");
		FacesContext ctx = FacesContext.getCurrentInstance();  
		VerificarAprobar2MB verificacionAprobadoDomiciliaria = (VerificarAprobar2MB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar2");	
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		if (validarDatos()) {
			datosProducto3MB.copiarDatosExpediente();
			/*ObservacionMB observacionMB = (ObservacionMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacion");
			observacionMB.setMostrarDialogo(true);
			String titulo = Mensajes.getMensaje("com.ibm.bbva.verificarResultadoDomiciliaria.formVerificarResultadoDomiciliaria.observacion.tituloRechazo");
			observacionMB.setTitulo(titulo);*/
			
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, 1);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			observacionRechazoMB.setTituloVentana(Mensajes.getMensaje("com.ibm.bbva.common.observacionRechazo.titulo4"));			
			
			if(verificacionAprobadoDomiciliaria.getSelectedItemsResultVl3().equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) ||
				verificacionAprobadoDomiciliaria.getSelectedItemsResultVd3().equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) ){
				addObjectSession(Constantes.VERIFICACION_LABORAL,verificacionAprobadoDomiciliaria.getSelectedItemsResultVl3());
				addObjectSession(Constantes.VERIFICACION_DOMICILIARIA,verificacionAprobadoDomiciliaria.getSelectedItemsResultVd3());
				LOG.info("ventanaRechazar::VERIFICACION_DOMICILIARIA: "+getObjectSession(Constantes.VERIFICACION_DOMICILIARIA));
				LOG.info("ventanaRechazar::VERIFICACION_LABORAL: "+getObjectSession(Constantes.VERIFICACION_LABORAL));
				verificacionAprobadoDomiciliaria.ocultaBotones2(null);				
			}
		} 
		return "";
	}

	public void accionVentObs (String observacion) {
        expediente.getExpedienteTC().setComentarioRechazo(observacion);
        grabarExp(Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE, Constantes.ESTADO_RECHAZADO_TAREA_5);
	}
	
	public String grabarExp(String accion, Integer estado) {
		String nomColumna="";
		//Se obtiene el producto
		FacesContext ctx = FacesContext.getCurrentInstance();
		DatosProducto3MB datosProducto3 = null;
		ComentarioMB comentario = null;
		VerificarAprobar2MB verificacion = null;
		PanelDocumentosMB panelDocumentoMB = null;
		
		datosProducto3 = (DatosProducto3MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");
		
		//Se obtiene las Verificaciones
		verificacion = (VerificarAprobar2MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar2");		
		
		// se obtiene el comentario
		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");	
		
		//Se actualiza Verificacion Expediente
		List<VerificacionExp> listaVe = verificacion.cargarListaVerificaciones();
		if (listaVe!=null && listaVe.size()>0){
			nomColumna="VFC";
		}else{
			nomColumna="VFS";
		}
		
		VerificacionExp registro;
		LOG.info("Recorrer lista");
		for (VerificacionExp lista: listaVe) {
			registro = new VerificacionExp();
			
			TipoVerificacion tipoVerTmp = new TipoVerificacion();
			tipoVerTmp.setId(lista.getTipoVerificacion().getId());
				
			registro.setExpediente(expediente);
			registro.setTipoVerificacion(tipoVerTmp);	
			LOG.info("lista.getId()="+lista.getId());
			VerificacionExp vexp = verificacionExpBean.buscarPorId(lista.getId());			
			verificacionExp = new VerificacionExp();
			verificacionExp.setExpediente(new Expediente());
			verificacionExp.setTipoVerificacion(new TipoVerificacion());
			
			
			if (vexp == null) {		
				verificacionExpBean.create(lista);
			}else{				  
				if (Util.esNuloVacio(vexp.getFlagVerif()) &&
						Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID.equals(lista.getFlagVerif())) {				
					verificacionExpBean.create(registro);				
				}
			
				verificacionExp = vexp;
				verificacionExp.setFlagVerif(lista.getFlagVerif());
				verificacionExpBean.edit(verificacionExp);
			}		    
		}
		
		//Se copia el producto 
		datosProducto3.copiarDatosExpediente();
		
		//Se copia las verificaciones
		verificacion.copiarDatosExpediente();
		
		// se copia el comentario
		comentario.copiarDatosExpediente();
		
		//Se actualiza expediente
        RemoteUtils objRemoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));

		expediente.setAccion(accion);
		Estado estadoTmp = new Estado();
		estadoTmp.setId(estado.longValue());
		expediente.setEstado(estadoTmp);
		
		LOG.info("Estado 1 :"+estadoTmp.getId());
		
		if (Constantes.ESTADO_POR_SUBSANAR_TAREA_5==estado) {
			Util.actualizaDevoluciones(expediente);
		}
		
		//Desactivar expediente para bandeja de asignacion no muestre mensaje
		expediente.setFlagActivo("0");
		
		expedienteBean.edit(expediente);
		
		//process
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion,	estado);

		Long idEmpAnt = (Long) getObjectSession(Constantes.ID_EMPLEADO_ESTADO_ANTERIOR_SESION);
		expedienteTCWPS.setDevueltoPor(String.valueOf(idEmpAnt));
		LOG.info("expedienteTCWPS.setDevueltoPor -> "+expedienteTCWPS.getDevueltoPor());
		
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
		LOG.info(">>>>>>>>>>>>>>> TAREA 5 <<<<<<<<<<<<<<<<<<<<<");
		if (Constantes.ACCION_BOTON_EN_ESPERA.equals(accion)) {
			expedienteTCWPS.setCodigoUsuarioActual(expedienteTCWPS.getCodigoUsuarioAnterior());
			expedienteTCWPS.setNombreUsuarioActual(expedienteTCWPS.getNombreUsuarioAnterior());
		//	expedienteTCWPS.setIdPerfilUsuarioActual(expedienteTCWPS.getIdPerfilUsuarioAnterior());
		//	expedienteTCWPS.setPerfilUsuarioActual(expedienteTCWPS.getPerfilUsuarioAnterior());
			LOG.info("Tarea 5, accion  "+accion);
			LOG.info("Codigo Usuario Actual es "+expedienteTCWPS.getCodigoUsuarioActual());
			LOG.info("Nombre Actual es "+expedienteTCWPS.getNombreUsuarioActual());

			//objRemoteUtils.enviarExpedienteTC(tkiid, expedienteTCWPS);
		}
			
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
			//historial.setNomColumna(toePerfilEstado.getNomColumna());
			historial.setNomColumna(nomColumna);
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
		
		if(historial.getFechaT1()!=null && historial.getFechaT2()!=null){
			LOG.info("historial- Fecha y Hora fecha 1:::"+historial.getFechaT1().toString());
			LOG.info("historial- Fecha y Hora fecha 2:::"+historial.getFechaT2().toString());
		}else
			LOG.info("f1 y f2 nulos");

		LOG.info("historial- Fecha y Hora fecha 3:::"+expedienteTCWPS.getActivado().toString());
		
		
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
		
		return "";
	}
	
	public boolean validarDatos() {		
		DatosProducto3MB datosProducto3 = (DatosProducto3MB) getObjectRequest("datosProducto3");
		VerificarAprobar2MB verificarAprobar2 = (VerificarAprobar2MB) getObjectRequest("verificarAprobar2");
		ComentarioMB comentario = (ComentarioMB) getObjectRequest("comentario");				
		boolean validaDatosProducto3 = datosProducto3.esValido();
		boolean validaVerificarAprobar2 = verificarAprobar2.esValido();
		boolean validoComentario = comentario.esValido();	
		LOG.info("validaDatosProducto3="+validaDatosProducto3);
		LOG.info("validaVerificarAprobar2="+validaVerificarAprobar2);
		LOG.info("validoComentario="+validoComentario);
		
		if(validoComentario && validaDatosProducto3 && validaVerificarAprobar2) {
			return true;
		}else{	
			FacesContext ctx = FacesContext.getCurrentInstance();  
			VerificarAprobar2MB verificacionAprobadoDomiciliaria = (VerificarAprobar2MB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar2");	
			
			if(validaVerificarAprobar2)
				verificacionAprobadoDomiciliaria.ocultaBotones3(null);
		}
		return false;
	}
	
	/* Llamado por el metodo aceptar de la clase ObservacionRechazoMB
	 */
	public String accionVentana(List<DevolucionTarea> lista) {
		expediente.setMotivoDevolucion(lista.get(0).getMotivoDevolucion());
		
		/*
		 * Se Agrego
		 * 
		 * */
		expediente.getExpedienteTC().setComentarioRechazo(lista.get(0).getExpediente().getExpedienteTC().getComentarioRechazo());
		LOG.info("COMENTARIO RECHAZO:" + expediente.getExpedienteTC().getComentarioRechazo());
		
		Integer valorAccion=(Integer) getObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO);
		LOG.info("valorAccion::::"+valorAccion);
		if(valorAccion!=null && valorAccion==1){//RECHAZAR
			LOG.info("RECHAZO");
		}else{//	DEVOLUCION
			LOG.info("DEVOLUCION");
			for (DevolucionTarea devTarea : lista) {
				devolucionTareaBean.create(devTarea);
			}			
		}

		//grabarExp(Constantes.ACCION_BOTON_DEVOLVER, Constantes.ESTADO_MODIFICACION_DE_ALTA_TAREA_5);
		grabarExp(Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE, Constantes.ESTADO_RECHAZADO_TAREA_5);
		
		
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public boolean isMsgGuiaDocumentaria() {
		return msgGuiaDocumentaria;
	}

	public void setMsgGuiaDocumentaria(boolean msgGuiaDocumentaria) {
		this.msgGuiaDocumentaria = msgGuiaDocumentaria;
	}

	public boolean isBotRechazarExp() {
		 AyudaVerificacionExp ayudaVerificacionExp = new AyudaVerificacionExp();
		 
		 if (ayudaVerificacionExp.existeTresObsDomLab(expediente)) {			
			 botRechazarExp = false;			
		 }else{
			 botRechazarExp = true;			 
		 }		
		return botRechazarExp;
	}

	public void setBotRechazarExp(boolean botRechazarExp) {
		this.botRechazarExp = botRechazarExp;
	}

	/*public boolean isBotDevolverGestor() {
		AyudaVerificacionExp ayudaVerificacionExp = new AyudaVerificacionExp();
		
		botRechazarExp = false;
		if(expediente!=null && expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoOferta()!=null && expediente.getExpedienteTC().getTipoOferta().getCodigo()!=null) {
			if(String.valueOf(expediente.getExpedienteTC().getTipoOferta().getId()).equals(Constantes.CODIGO_OFERTA_REGULAR)) {
				botRechazarExp = true;
			}else{
				if (ayudaVerificacionExp.existeTresObsDomLab(expediente)) {
				    botRechazarExp = true;
				}else{
					botRechazarExp = false;
				}
			}
		}
		return botRechazarExp;
	}

	public void setBotDevolverGestor(boolean botDevolverGestor) {
		this.botDevolverGestor = botDevolverGestor;
	}*/

	public boolean isRenderedNoConforme() {
		return renderedNoConforme;
	}

	public void setRenderedNoConforme(boolean renderedNoConforme) {
		this.renderedNoConforme = renderedNoConforme;
	}

	public boolean isRenderedResolver() {
		return renderedResolver;
	}

	public void setRenderedResolver(boolean renderedResolver) {
		this.renderedResolver = renderedResolver;
	}

	public boolean isRenderedEnEspera() {
		return renderedEnEspera;
	}

	public void setRenderedEnEspera(boolean renderedEnEspera) {
		this.renderedEnEspera = renderedEnEspera;
	}

	public boolean isRenderedObs() {
		return renderedObs;
	}

	public void setRenderedObs(boolean renderedObs) {
		this.renderedObs = renderedObs;
	}

	public boolean isRenderedDevolver() {
		if(expediente!=null && expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoOferta()!=null && expediente.getExpedienteTC().getTipoOferta().getCodigo()!=null) {
			if(String.valueOf(expediente.getExpedienteTC().getTipoOferta().getId()).equals(Constantes.CODIGO_OFERTA_REGULAR)) {
				renderedDevolver = true;
			}
		}
		return renderedDevolver;
	}

	public void setRenderedDevolver(boolean renderedDevolver) {
		this.renderedDevolver = renderedDevolver;
	}

	public boolean isRenderedRechazar() {
		return renderedRechazar;
	}

	public void setRenderedRechazar(boolean renderedRechazar) {
		this.renderedRechazar = renderedRechazar;
	}
	
}
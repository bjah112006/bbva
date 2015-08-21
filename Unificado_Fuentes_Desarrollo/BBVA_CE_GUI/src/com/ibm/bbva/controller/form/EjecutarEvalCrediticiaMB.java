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
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.DatosProducto3MB;
import com.ibm.bbva.controller.common.DetalleExpedienteMB;
import com.ibm.bbva.controller.common.ElevaSuperiorMB;
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
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaDocumento;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "ejecutarEvalCrediticia")
@RequestScoped
public class EjecutarEvalCrediticiaMB extends AbstractMBean {
	@EJB
	private DevolucionTareaBeanLocal devolucionTareaBean;
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private HistorialBeanLocal historialBean;
	@EJB
	private FacadeLocal facade;
	@EJB
	private EmpleadoBeanLocal empleadoBean;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	
	private Expediente expediente;
	private Expediente expedienteEntrada;
	private boolean activoAceptar;
	private boolean activoDevCpm;
	private boolean activoDenegar;
	private boolean activoElevarSup;
	private boolean activoDevGest;
	private String accion;
	private Integer estado;
	private boolean msgGuiaDocumentaria;
	private Empleado empleado;
	
	private static final Logger LOG = LoggerFactory.getLogger(EjecutarEvalCrediticiaMB.class);
	
	public EjecutarEvalCrediticiaMB() {		
		
	}
	
	@PostConstruct
	public void init() {
		LOG.info("Entro EjecutarEvalCrediticia");
		activoDenegar = true;
		activoDevGest = true;
		activoDevCpm = true;
		activoElevarSup = true;		
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);	
		FacesContext ctx = FacesContext.getCurrentInstance();
		DetalleExpedienteMB detalleExpediente = (DetalleExpedienteMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "detalleExpediente");
		
		LOG.info("String.valueOf(expediente.getEmpleado().getTipoCategoria().getId() : "+String.valueOf(expediente.getEmpleado().getTipoCategoria().getId()));
		LOG.info("expediente.getId() : "+expediente.getId());
		/*if (expediente != null && facade.ServiceIBMBBVA_delegacionRiesgosWS(Integer.valueOf(String.valueOf(expediente.getEmpleado().getTipoCategoria().getId())), expediente.getId())) {
			activoAceptar = true;
		}else{
			activoAceptar = false;
		}*/
		activoAceptar = true;
		
		// agregado por EM
		if(expedienteEntrada.getExpedienteTC().getTipoOferta().getCodigo().equals(Constantes.CODIGO_OFERTA_REGULAR)){
			activoDevGest = false;
		}
		if(expedienteEntrada.getExpedienteTC().getTipoOferta().getCodigo().equals(Constantes.CODIGO_OFERTA_REGULAR) && 
				   detalleExpediente != null && !detalleExpediente.isCumpleOperacion()){
					activoAceptar = false;
		}	
		
		if (detalleExpediente.isCumpleOperacion()) {
			activoElevarSup=false;
		}
	}
		
	public String aprobar() {
		LOG.info("Aprobar Operacion - Exp : "+expediente.getId());
		FacesContext ctx = FacesContext.getCurrentInstance(); 
		PanelDocumentosMB panelDocumentos=null;
		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		
		addObjectSession(Constantes.ACCION_SESION, Constantes.ACCION_BOTON_APROBAR_OPERACION);
		
		if (esValido()) {
			LOG.info("Es valido");
			
			panelDocumentos = (PanelDocumentosMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");			
			/*Valida si cumple con guia documentaria*/
	//		DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
		//			.getCurrentInstance().getApplication().getELResolver()
			//		.getValue(elContext, null, "documentosEscaneados");
			
			TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
			datosProducto3MB.copiarDatosExpediente();
			
			//if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
			if(panelDocumentos.validarDocumentos()){
				LOG.info("Documentos Obligatorios OK");
				msgGuiaDocumentaria = false;
			//	if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo())) {
					expediente.setAccion(Constantes.ACCION_BOTON_APROBAR_OPERACION);
					Estado estadoObj = new Estado();
					estadoObj.setId(Constantes.ESTADO_APROBADO_TAREA_12.longValue());
					expediente.setEstado(estadoObj);
					
					// se copia los comentarios al expediente de sesion
					ComentarioMB comentarioMB = (ComentarioMB) FacesContext
							.getCurrentInstance().getApplication().getELResolver()
							.getValue(elContext, null, "comentario");
					comentarioMB.copiarDatosExpediente();
					LOG.info("entra a actualizar");
					actualizar(Constantes.ACCION_BOTON_APROBAR_OPERACION, 
							Constantes.ESTADO_APROBADO_TAREA_12, null);
					LOG.info("fin de actualizar");
					addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
					addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
					// guardar estado expediente
					//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
					return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				//} else {
					/*TipoResolucionMB tipoResolucionMB = new TipoResolucionMB();
					tipoResolucionMB.setMostrarDialogo(true);
					addObjectRequest("tipoResolucion", tipoResolucionMB);*/ // TODO modal
					// se muestra la ventana y si el usuario selecciona la opcion Aceptar se ejecuta
					// el metodo guardarOfertaNoAprobado()
				//}
			} else {
				LOG.info("VALIDAR DOCUMENTO FALSEEEE!!!");
				msgGuiaDocumentaria = true;
				return null;
			}				
		}else
			LOG.info("No valido");
		return null;
	}

	public String aprobarMod() {
		LOG.info("Aprobar con Modificacion - Exp : "+expediente.getId());
		FacesContext ctx = FacesContext.getCurrentInstance(); 
		PanelDocumentosMB panelDocumentos=null;
		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		
		addObjectSession(Constantes.ACCION_SESION, Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS);
		
		if (esValido()) {
			LOG.info("Es valido");
			
			panelDocumentos = (PanelDocumentosMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");			
			/*Valida si cumple con guia documentaria*/
			//DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
				//	.getCurrentInstance().getApplication().getELResolver()
					//.getValue(elContext, null, "documentosEscaneados");
			
			TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
			datosProducto3MB.copiarDatosExpediente();
			
			//if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
			if(panelDocumentos.validarDocumentos()){
				LOG.info("Documentos Obligatorios OK");
				msgGuiaDocumentaria = false;
				//if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo())) {
					expediente.setAccion(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS);
					Estado estadoObj = new Estado();
					estadoObj.setId(Constantes.ESTADO_APROBADO_CON_MODIFICACION_TAREA_12.longValue());
					expediente.setEstado(estadoObj);
					
					// se copia los comentarios al expediente de sesion
					ComentarioMB comentarioMB = (ComentarioMB) FacesContext
							.getCurrentInstance().getApplication().getELResolver()
							.getValue(elContext, null, "comentario");
					comentarioMB.copiarDatosExpediente();
					LOG.info("entra a actualizar");
					actualizar(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS, 
							Constantes.ESTADO_APROBADO_CON_MODIFICACION_TAREA_12, null);
					LOG.info("salio de actualizar");
					
					panelDocumentos.actualizarObservados();
					
					addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
					addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
					// guardar estado expediente
					//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
					return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				//} else {
					/*TipoResolucionMB tipoResolucionMB = new TipoResolucionMB();
					tipoResolucionMB.setMostrarDialogo(true);
					addObjectRequest("tipoResolucion", tipoResolucionMB);*/ // TODO modal
					// se muestra la ventana y si el usuario selecciona la opcion Aceptar se ejecuta
					// el metodo guardarOfertaNoAprobado()
				//}
			} else {
				LOG.info("VALIDAR DOCUMENTO FALSEEEE!!!");
				msgGuiaDocumentaria = true;
				return null;
			}				
		}else
			LOG.info("No valido");
		return null;
	}
	
	public String aprobarObs() {
		LOG.info("Aprobar con Modificacion - Exp : "+expediente.getId());
		FacesContext ctx = FacesContext.getCurrentInstance(); 
		PanelDocumentosMB panelDocumentos=null;
		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		
		addObjectSession(Constantes.ACCION_SESION, Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS);
		
		if (esValido()) {
			LOG.info("Es valido");
			/*Valida si cumple con guia documentaria*/
		//	DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
			///		.getCurrentInstance().getApplication().getELResolver()
				//	.getValue(elContext, null, "documentosEscaneados");
			
			panelDocumentos = (PanelDocumentosMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
			
			TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
			datosProducto3MB.copiarDatosExpediente();
			
			if(panelDocumentos.validarDocumentos()){
				LOG.info("Documentos Obligatorios OK");
			//if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				msgGuiaDocumentaria = false;
				//if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo())) {
					expediente.setAccion(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS);
					Estado estadoObj = new Estado();
					estadoObj.setId(Constantes.ESTADO_APROBADO_CON_OBSERVACION_TAREA_12.longValue());
					expediente.setEstado(estadoObj);
					
					// se copia los comentarios al expediente de sesion
					ComentarioMB comentarioMB = (ComentarioMB) FacesContext
							.getCurrentInstance().getApplication().getELResolver()
							.getValue(elContext, null, "comentario");
					comentarioMB.copiarDatosExpediente();
					LOG.info("entra a actualizar");
					actualizar(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS, 
							Constantes.ESTADO_APROBADO_CON_OBSERVACION_TAREA_12, null);
					LOG.info("salio de actualizar");
					
					addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
					addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
					// guardar estado expediente
					//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
					return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				//} else {
					/*TipoResolucionMB tipoResolucionMB = new TipoResolucionMB();
					tipoResolucionMB.setMostrarDialogo(true);
					addObjectRequest("tipoResolucion", tipoResolucionMB);*/ // TODO modal
					// se muestra la ventana y si el usuario selecciona la opcion Aceptar se ejecuta
					// el metodo guardarOfertaNoAprobado()
				//}
			} else {
				LOG.info("VALIDAR DOCUMENTO FALSEEEE!!!");
				msgGuiaDocumentaria = true;
				return null;
			}				
		}else
			LOG.info("No valido");
		return null;
	}
		
	public String actScoring() {
		LOG.info("Solicitar actualizacion de scoring - Exp : "+expediente.getId());
		FacesContext ctx = FacesContext.getCurrentInstance(); 
		PanelDocumentosMB panelDocumentos=null;
		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		if (esValido()) {
			LOG.info("Es valido");
			
			panelDocumentos = (PanelDocumentosMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
			
			/*Valida si cumple con guia documentaria*/
		//	DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
			//		.getCurrentInstance().getApplication().getELResolver()
				//	.getValue(elContext, null, "documentosEscaneados");
			
			TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
			datosProducto3MB.copiarDatosExpediente();
			
			if(panelDocumentos.validarDocumentos()){
				LOG.info("Documentos Obligatorios OK");
			//if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				msgGuiaDocumentaria = false;
				//if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo())) {
					expediente.setAccion(Constantes.ACCION_BOTON_SOLICITAR_ACTUALIZACION_SCORING);
					Estado estadoObj = new Estado();
					estadoObj.setId(Constantes.ESTADO_CAMBIO_A_EN_TRAMITE_TAREA_12.longValue());
					expediente.setEstado(estadoObj);
					
					// se copia los comentarios al expediente de sesion
					ComentarioMB comentarioMB = (ComentarioMB) FacesContext
							.getCurrentInstance().getApplication().getELResolver()
							.getValue(elContext, null, "comentario");
					comentarioMB.copiarDatosExpediente();
					LOG.info("entra a actualizar");
					actualizar(Constantes.ACCION_BOTON_SOLICITAR_ACTUALIZACION_SCORING, 
							Constantes.ESTADO_CAMBIO_A_EN_TRAMITE_TAREA_12, null);
					LOG.info("salio de actualizar");
					addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
					addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
					// guardar estado expediente
					//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
					return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				//} else {
					/*TipoResolucionMB tipoResolucionMB = new TipoResolucionMB();
					tipoResolucionMB.setMostrarDialogo(true);
					addObjectRequest("tipoResolucion", tipoResolucionMB);*/ // TODO modal
					// se muestra la ventana y si el usuario selecciona la opcion Aceptar se ejecuta
					// el metodo guardarOfertaNoAprobado()
				//}
			} else {
				LOG.info("VALIDAR DOCUMENTO FALSEEEE!!!");
				msgGuiaDocumentaria = true;
				return null;
			}				
		}else
			LOG.info("No valido");
		return null;
	}
	
	public String devolverCpm() {		
		/*ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		if (esValido()) {
			datosProducto3MB.copiarDatosExpediente();
			
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, Constantes.CODIGO_BOTON_DEVOLVER_CPM_CU12);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			accion = Constantes.ACCION_BOTON_DEVOLVER_CPM;
			estado = Constantes.ESTADO_OBSERVADO_POR_RIESGOS_TAREA_12;			
		}*/
		return null;
	}
	
	public String devolver() {
		LOG.info("Devolver - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		if (esValido()) {
			datosProducto3MB.copiarDatosExpediente();
			
			//addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, Constantes.CODIGO_BOTON_DEVOLVER_GESTOR_CU12);
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			addObjectSession("actualizarObservados", "OK");
			accion = Constantes.ACCION_BOTON_DEVOLVER;
			estado = Constantes.ESTADO_POR_SUBSANAR_TAREA_12;
		}
		return null;
	}

	public String rechazarExp() {
		LOG.info("Rechazar expediente - Exp : "+expediente.getId());
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
			String titulo = Mensajes.getMensaje("com.ibm.bbva.ejecutarEvalCrediticia.formEjecutarEvalCrediticia.observacion.tituloRechazo");
			observacionMB.setTitulo(titulo);*/
			
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, 1);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			observacionRechazoMB.setTituloVentana(Mensajes.getMensaje("com.ibm.bbva.common.observacionRechazo.titulo4"));
			
			accion = Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE;
			estado = Constantes.ESTADO_RECHAZADO_TAREA_12;
		}
		return null;
	}	

    public String elevaSuperior() {
    	LOG.info("Elevar a superior - Exp : "+expediente.getId());
    	ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		if (esValido()) {
		    datosProducto3MB.copiarDatosExpediente();
		   /* FacesContext ctx = FacesContext.getCurrentInstance(); 
			PanelDocumentosMB panelDocumentos=null;
			panelDocumentos = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");			
			TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
			datosProducto3MB.copiarDatosExpediente();*/
			
			ElevaSuperiorMB elevaSuperiorMB = (ElevaSuperiorMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "elevaSuperior");
		    elevaSuperiorMB.setMostrarDialogo(true);
			
			/*if(panelDocumentos.validarDocumentos()){
				LOG.info("Documentos Obligatorios OK");
				msgGuiaDocumentaria = false;
				expediente.setAccion(Constantes.ACCION_BOTON_ELEVAR_SUPERIOR);
				Estado estadoObj = new Estado();
				// Siguiente estado
				estadoObj.setId(Constantes.ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_19.longValue());
				expediente.setEstado(estadoObj);
				// se copia los comentarios al expediente de sesion
				ComentarioMB comentarioMB = (ComentarioMB) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "comentario");
				comentarioMB.copiarDatosExpediente();
				actualizar(Constantes.ACCION_BOTON_ELEVAR_SUPERIOR, Constantes.ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_19, null);
				panelDocumentos.actualizarObservados();
				addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
				addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
				return "/moverArchivos/formMoverArchivos?faces-redirect=true";
			} else {
				LOG.info("VALIDAR DOCUMENTO FALSEEEE!!!");
				msgGuiaDocumentaria = true;
				return null;
			}*/
		}
		return null;		
	}
    
	/* Llamado por ElevaSuperiorMB para seleccionar una opcion */
	public String guardarElevaSup (String idEmpResp) {
		LOG.info("idEmpResp = "+idEmpResp);
	   
	    accion = Constantes.ACCION_BOTON_ELEVAR_SUPERIOR;
	    estado = Constantes.ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_19;
	    /*
		expediente.setAccion(accion);
		Estado estadoObj = new Estado();
		estadoObj.setId(estado.longValue());
		expediente.setEstado(estadoObj);
		actualizar(accion, estado, idEmpResp);
		return null;*/

		/**
		 * Incidencia 106 (27/02/2015)-  Se adjunta un documento PDF yal enviar el expediente con el boton "ELEVAR A SUPERIOR", 
		 * el expediente se dirige a userAD y se verifica que el documento PDF permanece en la carpeta 
		 * "D:\ContratacionElectronica\Transferencias_TC"
		 * */
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
	    FacesContext ctx = FacesContext.getCurrentInstance(); 
		PanelDocumentosMB panelDocumentos=null;
		panelDocumentos = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");			
		TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
		
		if(panelDocumentos.validarDocumentos()){
			LOG.info("Documentos Obligatorios OK");
			msgGuiaDocumentaria = false;
			expediente.setAccion(Constantes.ACCION_BOTON_ELEVAR_SUPERIOR);
			Estado estadoObj = new Estado();
			// Siguiente estado
			estadoObj.setId(Constantes.ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_19.longValue());
			expediente.setEstado(estadoObj);
			// se copia los comentarios al expediente de sesion
			ComentarioMB comentarioMB = (ComentarioMB) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "comentario");
			comentarioMB.copiarDatosExpediente();
			actualizar(Constantes.ACCION_BOTON_ELEVAR_SUPERIOR, Constantes.ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_19, idEmpResp);
			panelDocumentos.actualizarObservados();
			addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
			addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
			return "/moverArchivos/formMoverArchivos?faces-redirect=true";
		} else {
			LOG.info("VALIDAR DOCUMENTO FALSEEEE!!!");
			msgGuiaDocumentaria = true;
			return null;
		}
		
	}
    
	/* Llamado por TipoResolucionMB para seleccionar una opcion opcion 
	 * Observado por gerente o Aprobado sin modificación
	 */
	public void guardarOfertaNoAprobado (String accion, Integer estado) {
		expediente.setAccion(accion);
		Estado estadoObj = new Estado();
		estadoObj.setId(estado.longValue());
		expediente.setEstado(estadoObj);
		
		actualizar(accion, estado, null);
	}
	
	/* Llamado por el metodo aceptar de la clase ObservacionRechazoMB
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
		
		
		Integer valorAccion=(Integer) getObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO);
		LOG.info("valorAccion::::"+valorAccion);
		if(valorAccion!=null && valorAccion==1){//RECHAZAR
			LOG.info("RECHAZO");
		}else{//	DEVOLUCION
			LOG.info("DEVOLUCION");
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			ComentarioMB comentarioMB = (ComentarioMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "comentario");
			comentarioMB.copiarDatosExpediente();
			
			for (DevolucionTarea devTarea : lista) {
				devolucionTareaBean.create(devTarea);
			}			
		}
		expediente.setAccion(accion);
		Estado estadoObj = new Estado();
		estadoObj.setId(estado.longValue());
		expediente.setEstado(estadoObj);
		actualizar(accion, estado, null);
		
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}
	
	
	
	/* Llamado por el metodo aceptar de la clase ObservacionMB
	 */
	public void accionVentObs (String observacion) {
        expediente.getExpedienteTC().setComentarioRechazo(observacion);		
		expediente.setAccion(accion);
		Estado estadoObj = new Estado();
		estadoObj.setId(estado.longValue());
		expediente.setEstado(estadoObj);
		actualizar(accion, estado, null);
	}
	
	private void actualizar (String accion, Integer estado, String idEmpResp) {
		// se copia los comentarios al expediente de sesion
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ComentarioMB comentarioMB = (ComentarioMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "comentario");
		comentarioMB.copiarDatosExpediente();
		
		// se guarda el expediente
        RemoteUtils objRemoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		//Actualiza devoluciones
		if (Constantes.ESTADO_APROBADO_CON_MODIFICACION_TAREA_12==estado || 
				Constantes.ESTADO_POR_SUBSANAR_TAREA_12 == estado) {
			Util.actualizaDevoluciones(expediente);
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
					Constantes.ESTADO_APROBADO_CON_MODIFICACION_TAREA_12==estado)
					expediente.getExpedienteTC().setTipoResolucion(Constantes.RESOLUCION_CON_MODIFICACION);
		}
				
        LOG.info("TipoMonedaAprob : "+expediente.getExpedienteTC().getTipoMonedaAprob());
        LOG.info("LineaCredAprob : "+expediente.getExpedienteTC().getLineaCredAprob());
        LOG.info("PlazoSolicitadoApr : "+expediente.getExpedienteTC().getPlazoSolicitadoApr());
        
        // si se eleva a superior el empleado se asigna directamente
        Empleado empleadoNuevo = null;
        Empleado empleadoAntiguo = null;
        if (accion.equals(Constantes.ACCION_BOTON_ELEVAR_SUPERIOR)
        		&& idEmpResp != null) {
        	empleadoNuevo = empleadoBean.buscarPorId(Long.parseLong(idEmpResp));
        	empleadoAntiguo = expediente.getEmpleado();
        	expediente.setEmpleado(empleadoNuevo);
        }
        
		//Desactivar expediente para bandeja de asignacion no muestre mensaje
		expediente.setFlagActivo("0");
		
		expedienteBean.edit(expediente);
		
		//process
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion,	estado);

		if (idEmpResp != null) {
			LOG.info("idEmpResp no null = "+idEmpResp);
			empleado=empleadoBean.buscarPorId(Long.parseLong(idEmpResp));
			expedienteTCWPS.setCodigoUsuarioActual(empleado.getCodigo());
			expedienteTCWPS.setIdPerfilUsuarioActual(""+empleado.getPerfil().getId());
			expedienteTCWPS.setPerfilUsuarioActual(""+empleado.getPerfil().getDescripcion());
			expedienteTCWPS.setNombreUsuarioActual(empleado.getNombresCompletos());
			
			if(accion.equals(Constantes.ACCION_BOTON_ELEVAR_SUPERIOR))
				expedienteTCWPS.setIdTareaAnterior(12);
			
			LOG.info("idEmpResp setCodigoUsuarioActual = "+expedienteTCWPS.getCodigoUsuarioActual());
			LOG.info("idEmpResp setCodigoUsuarioActual = "+expedienteTCWPS.getCodigoUsuarioActual());
			LOG.info("idEmpResp setIdPerfilUsuarioActual = "+expedienteTCWPS.getIdPerfilUsuarioActual());
			LOG.info("idEmpResp setPerfilUsuarioActual = "+expedienteTCWPS.getPerfilUsuarioActual());
			LOG.info("idEmpResp setIdTareaAnterior = "+expedienteTCWPS.getIdTareaAnterior());
		}
		
		String tkiid = expedienteTCWPS.getTaskID();		
		//objRemoteUtils.enviarExpedienteTC(tkiid, expedienteTCWPS);
		LOG.info("objRemoteUtils 1");
		
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
		LOG.info("objRemoteUtils 2");
		ayudaExpedienteTC.actualizarListaExpedienteTC(new Consulta());
        
		//fin process
		// se almacena en el historial
		if (accion.equals(Constantes.ACCION_BOTON_ELEVAR_SUPERIOR) && empleadoAntiguo != null) {
			expediente.setEmpleado(empleadoAntiguo);
		}
		Historial historial = ConvertExpediente.convertToHistorialVO(expediente);
		
		historial.setEstado(expedienteEntrada.getEstado());
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
		/*Si el expediente es una retraccion*/
		int ans=0;
		if (hretraer!=null) {
			historial.setFechaT1R(hretraer.getFechaT1());
			historial.setFechaT2R(hretraer.getFechaT2());
			historial.setFechaT3R(hretraer.getFechaT3());
			historial.setTiempoColar(ConvertExpediente.calculoTiempoCola(expediente.getEmpleado().getOficina().getId(),historial.getFechaT1R(),historial.getFechaT2R()));
			historial.setTiempoEjecucionr(ConvertExpediente.calculoTiempoEjecucion(historial.getFechaT2R(), historial.getFechaT3R()));
			historial.setTiempoColaRetraer(ConvertExpediente.calculoTiempoCola(expediente.getEmpleado().getOficina().getId(),historial.getFechaT3R(),historial.getFechaT1()));
		}
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
		ExpedienteTCWPSWeb expedienteTCWPS1=Convertidor.fromObjExpedienteTCWPSToObjExpedienteTCWPSWeb(expedienteTCWPS, "1");		
		ayudaHistorial.asignarFecha(historial, expedienteTCWPS1);
		
		historialBean.create(historial);
		
		/*Adjunta Documentos Expediente*/
		AyudaDocumento ayudaDocumento = new AyudaDocumento();
		ayudaDocumento.adjuntarDocumentoExpediente();
		
		ayudaDocumento.editarDocumentoExpediente();
		
		removeObjectSession(Constantes.LISTA_DOC_EXP_ADJ);
		removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
		
		//todos los documentos se actualizan como no observados
		PanelDocumentosMB panelDocumentosMB = (PanelDocumentosMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "paneldocumentos");
		panelDocumentosMB.actualizarNoObservados();
	}

	private boolean esValido() {		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		ComentarioMB comentarioMB = (ComentarioMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "comentario");
		boolean datprod = datosProducto3MB.esValido();	
		boolean validoComentario = comentarioMB.esValido();
		LOG.info("datprod="+datprod);
		LOG.info("validoComentario="+validoComentario);
		
		return datprod && validoComentario;
	}
	
	public boolean isActivoDevCpm() {
		return activoDevCpm;
	}

	public void setActivoDevCpm(boolean activoDevCpm) {
		this.activoDevCpm = activoDevCpm;
	}
	
	public boolean isActivoAceptar() {
		return activoAceptar;
	}

	public void setActivoAceptar(boolean activoAceptar) {
		this.activoAceptar = activoAceptar;
	}

	public boolean isActivoDenegar() {
		return activoDenegar;
	}

	public void setActivoDenegar(boolean activoDenegar) {
		this.activoDenegar = activoDenegar;
	}

	public boolean isActivoDevGest() {
		return activoDevGest;
	}

	public void setActivoDevGest(boolean activoDevGest) {
		this.activoDevGest = activoDevGest;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public boolean isActivoElevarSup() {
		return activoElevarSup;
	}

	public void setActivoElevarSup(boolean activoElevarSup) {
		this.activoElevarSup = activoElevarSup;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public boolean isMsgGuiaDocumentaria() {
		return msgGuiaDocumentaria;
	}

	public void setMsgGuiaDocumentaria(boolean msgGuiaDocumentaria) {
		this.msgGuiaDocumentaria = msgGuiaDocumentaria;
	}
	
}

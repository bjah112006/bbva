package com.ibm.bbva.controller.form;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bpd.RemoteUtils;
import pe.ibm.util.Convertidor;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.DatosProducto3MB;
import com.ibm.bbva.controller.common.DocumentosEscaneadosMB;
import com.ibm.bbva.controller.common.EnvioMailMB;
import com.ibm.bbva.controller.common.ObservacionMB;
import com.ibm.bbva.controller.common.ObservacionRechazoMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.controller.common.VerificarAprobarMB;
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
@ManagedBean(name = "revisarRegistrarDictamen")
@RequestScoped
public class RevisarRegistrarDictamenMB extends AbstractMBean {
	@EJB
	private DevolucionTareaBeanLocal devolucionTareaBean;
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private HistorialBeanLocal historialBean;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;

	private Expediente expediente;
	private Expediente expedienteEntrada;
	private boolean activoAceptar;
	private boolean activoDevRiesgos;
	private boolean activoDenegar;
	private boolean activoDevGest;
	private String accion;
	private Integer estado;
	private boolean msgGuiaDocumentaria;
	
	private static final Logger LOG = LoggerFactory.getLogger(RevisarRegistrarDictamenMB.class);
	
	public RevisarRegistrarDictamenMB() {
		LOG.info("Entro RevisarRegistrarDictamen");
		activoAceptar = true;
		activoDenegar = true;
		activoDevGest = true;
		activoDevRiesgos = true;				
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
	}
	
	public String aprobar() {
		LOG.info("Aprobar Operacion - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		addObjectSession(Constantes.ACCION_SESION, Constantes.ACCION_BOTON_APROBAR_OPERACION);
		
		if (esValido()) {
			/*Valida si cumple con guia documentaria*/
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "documentosEscaneados");
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				msgGuiaDocumentaria = false;
				
				TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
				datosProducto3MB.copiarDatosExpediente();
				
				//if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo())) {
					expediente.setAccion(Constantes.ACCION_BOTON_APROBAR_OPERACION);
					Estado estadoObj = new Estado();
					estadoObj.setId(Constantes.ESTADO_EN_VERIFICACION_TAREA_19.longValue());
					expediente.setEstado(estadoObj);
					
					// se copia los comentarios al expediente de sesion
					ComentarioMB comentarioMB = (ComentarioMB) FacesContext
							.getCurrentInstance().getApplication().getELResolver()
							.getValue(elContext, null, "comentario");
					comentarioMB.copiarDatosExpediente();
					
					int estado = Constantes.ESTADO_APROBADO_TAREA_19;
					
					try {
						
						if(expediente.getExpedienteTC().getVerifDom() != null){
							if(expediente.getExpedienteTC().getVerifDom().equals(Constantes.CHECK_SELECCIONADO)){
								estado=Constantes.ESTADO_EN_VERIFICACION_TAREA_19;
							}
						}
						
						if(expediente.getExpedienteTC().getVerifLab() != null){
							if(expediente.getExpedienteTC().getVerifLab().equals(Constantes.CHECK_SELECCIONADO)){
								estado=Constantes.ESTADO_EN_VERIFICACION_TAREA_19;
							}
						}
						
						if(expediente.getExpedienteTC().getVerifDps() != null){
							if(expediente.getExpedienteTC().getVerifDps().equals(Constantes.CHECK_SELECCIONADO)){
								estado=Constantes.ESTADO_EN_VERIFICACION_TAREA_19;
							}
						}
						
					}catch(NullPointerException ex){
						
						/**
						 * 
						 * Lógica anterior inválida, 
						 * 
						 * Puesto que se debe obtener
						 * la info desde expediente.getExpedienteTC y no directamente desde expediente.
						 * 
						 * 
							if(expediente.getVerificacionExpDom() != null){
								if(expediente.getVerificacionExpDom().getFlagVerif().equals(Constantes.CHECK_SELECCIONADO)){
									estado=Constantes.ESTADO_EN_VERIFICACION_TAREA_19;
								}
							}
							
							if(expediente.getVerificacionExpLab() != null){
								if(expediente.getVerificacionExpLab().getFlagVerif().equals(Constantes.CHECK_SELECCIONADO)){
									estado=Constantes.ESTADO_EN_VERIFICACION_TAREA_19;
								}
							}
						*
						*
						**/
					}
					
					actualizar(Constantes.ACCION_BOTON_APROBAR_OPERACION, estado);
					// guardar estado expediente
					addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
					addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
					//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
					return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				//} else {
					/*TipoResolucionMB tipoResolucionMB = new TipoResolucionMB();
					tipoResolucionMB.setMostrarDialogo(true);
					addObjectRequest("tipoResolucion", tipoResolucionMB);*/ // TODO modal
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

	public String aprobarMod() {
		LOG.info("Aprobar con modificacion - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		
		addObjectSession(Constantes.ACCION_SESION, Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS);
		
		if (esValido()) {
			/*Valida si cumple con guia documentaria*/
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "documentosEscaneados");
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				msgGuiaDocumentaria = false;
				
				TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
				datosProducto3MB.copiarDatosExpediente();
				
				//if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo())) {
					expediente.setAccion(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS);
					Estado estadoObj = new Estado();
					estadoObj.setId(Constantes.ESTADO_APROBADO_CON_MODIFICACION_TAREA_19.longValue());
					expediente.setEstado(estadoObj);
					
					// se copia los comentarios al expediente de sesion
					ComentarioMB comentarioMB = (ComentarioMB) FacesContext
							.getCurrentInstance().getApplication().getELResolver()
							.getValue(elContext, null, "comentario");
					comentarioMB.copiarDatosExpediente();
					actualizar(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS, Constantes.ESTADO_APROBADO_CON_MODIFICACION_TAREA_19);
					
					FacesContext ctx = FacesContext.getCurrentInstance();  
					PanelDocumentosMB panelDocumentos = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
					panelDocumentos.actualizarObservados();
					
					// guardar estado expediente
					addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
					addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
					//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
					return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				//} else {
					/*TipoResolucionMB tipoResolucionMB = new TipoResolucionMB();
					tipoResolucionMB.setMostrarDialogo(true);
					addObjectRequest("tipoResolucion", tipoResolucionMB);*/ // TODO modal
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
	
	public String aprobarObs() {
		LOG.info("Aprobar con modificacion - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		
		addObjectSession(Constantes.ACCION_SESION, Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS);
		
		if (esValido()) {
			/*Valida si cumple con guia documentaria*/
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "documentosEscaneados");
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				msgGuiaDocumentaria = false;
				
				TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
				datosProducto3MB.copiarDatosExpediente();
				
				//if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo())) {
					expediente.setAccion(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS);
					Estado estadoObj = new Estado();
					estadoObj.setId(Constantes.ESTADO_APROBADO_CON_OBSERVACION_TAREA_19.longValue());
					expediente.setEstado(estadoObj);
					
					// se copia los comentarios al expediente de sesion
					ComentarioMB comentarioMB = (ComentarioMB) FacesContext
							.getCurrentInstance().getApplication().getELResolver()
							.getValue(elContext, null, "comentario");
					comentarioMB.copiarDatosExpediente();
					actualizar(Constantes.ACCION_BOTON_APROBADO_CON_MOD_OBS, Constantes.ESTADO_APROBADO_CON_OBSERVACION_TAREA_19);
					// guardar estado expediente
					addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
					addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
					//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
					return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				//} else {
					/*TipoResolucionMB tipoResolucionMB = new TipoResolucionMB();
					tipoResolucionMB.setMostrarDialogo(true);
					addObjectRequest("tipoResolucion", tipoResolucionMB);*/ // TODO modal
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
	
	public String devolverRiesgo() {
		LOG.info("Devolver Riesgos - Exp : "+expediente.getId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		if (esValido()) {
			/*Valida si cumple con guia documentaria*/
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "documentosEscaneados");
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				msgGuiaDocumentaria = false;
				
				TipoOferta tipoOferta = expediente.getExpedienteTC().getTipoOferta();
				datosProducto3MB.copiarDatosExpediente();
				
				//if (Constantes.CODIGO_OFERTA_APROBADO.equals(tipoOferta.getCodigo())) {
					expediente.setAccion(Constantes.ACCION_BOTON_DEVOLVER_RIESGOS);
					Estado estadoObj = new Estado();
					estadoObj.setId(Constantes.ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_19.longValue());
					expediente.setEstado(estadoObj);
					
					// se copia los comentarios al expediente de sesion
					ComentarioMB comentarioMB = (ComentarioMB) FacesContext
							.getCurrentInstance().getApplication().getELResolver()
							.getValue(elContext, null, "comentario");
					comentarioMB.copiarDatosExpediente();
					actualizar(Constantes.ACCION_BOTON_DEVOLVER_RIESGOS, Constantes.ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_19);
					// guardar estado expediente
					addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
					addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
					//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
					return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				//} else {
					/*TipoResolucionMB tipoResolucionMB = new TipoResolucionMB();
					tipoResolucionMB.setMostrarDialogo(true);
					addObjectRequest("tipoResolucion", tipoResolucionMB);*/ // TODO modal
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
	
	public String devolverRiesgos() {		
		/*ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		if (esValido()) {			
			datosProducto3MB.copiarDatosExpediente();
			
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			accion = Constantes.ACCION_BOTON_DEVOLVER_RIESGOS;
			estado = Constantes.ESTADO_DEVUELTO_POR_RIESGOS_TAREA_19;
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
			
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			addObjectSession("actualizarObservados", "OK");
			accion = Constantes.ACCION_BOTON_DEVOLVER;
			estado = Constantes.ESTADO_POR_SUBSANAR_TAREA_19;
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
			/*ObservacionMB observacionMB = (ObservacionMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacion");
			observacionMB.setMostrarDialogo(true);
			String titulo = Mensajes.getMensaje("com.ibm.bbva.revisarRegistrarDictamen.formRevisarRegistrarDictamen.observacion.tituloRechazo");
			observacionMB.setTitulo(titulo);*/
			
			addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, 1);
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "observacionRechazo");
			observacionRechazoMB.init();
			observacionRechazoMB.setMostrarDialogo(true);
			observacionRechazoMB.setTituloVentana(Mensajes.getMensaje("com.ibm.bbva.common.observacionRechazo.titulo4"));
			
			accion = Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE;
			estado = Constantes.ESTADO_RECHAZADO_TAREA_19;			
		}
		return null;
	}
    
	/* Llamado por TipoResolucionMB para seleccionar una opcion opcion 
	 * Observado por gerente o Aprobado sin modificación
	 */
	public void guardarOfertaNoAprobado (String accion, Integer estado) {
		actualizar(accion, estado);
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

		actualizar(accion, estado);
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}
	
	/* Llamado por el metodo aceptar de la clase ObservacionMB
	 */
	public void accionVentObs (String observacion) {
        expediente.getExpedienteTC().setComentarioRechazo(observacion);
		actualizar(accion, estado);
	}
	
	private void actualizar (String accion, Integer estado) {
		FacesContext ctx = FacesContext.getCurrentInstance();		
		VerificarAprobarMB verificarAprobar = null;
		expediente.setAccion(accion);
		Estado estadoObj = new Estado();
		estadoObj.setId(estado.longValue());
		expediente.setEstado(estadoObj);
		
		verificarAprobar = (VerificarAprobarMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");		
		
		verificarAprobar.copiarDatosExpediente();
		
		// se copia los comentarios al expediente de sesion
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ComentarioMB comentarioMB = (ComentarioMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "comentario");
		comentarioMB.copiarDatosExpediente();
		// se guarda el expediente
        RemoteUtils objRemoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		if (Constantes.ESTADO_POR_SUBSANAR_TAREA_19==estado) {
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
					Constantes.ESTADO_APROBADO_CON_MODIFICACION_TAREA_19==estado)
					expediente.getExpedienteTC().setTipoResolucion(Constantes.RESOLUCION_CON_MODIFICACION);
		}

        LOG.info("TipoMonedaAprob : "+expediente.getExpedienteTC().getTipoMonedaAprob());
        LOG.info("LineaCredAprob : "+expediente.getExpedienteTC().getLineaCredAprob());
        LOG.info("PlazoSolicitadoApr : "+expediente.getExpedienteTC().getPlazoSolicitadoApr());	
        
		//Desactivar expediente para bandeja de asignacion no muestre mensaje
		expediente.setFlagActivo("0");
		
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
		ayudaExpedienteTC.actualizarListaExpedienteTC(new Consulta());
        
		//fin process
		LOG.info("oficina : "+expediente.getEmpleado().getOficina().getId());
		LOG.info("T1 : "+expediente.getExpedienteTC().getFechaT1());
		LOG.info("T2 : "+expediente.getExpedienteTC().getFechaT2());
		LOG.info("T3 : "+expediente.getExpedienteTC().getFechaT3());
		
		// se almacena en el historial
		Historial historial = ConvertExpediente.convertToHistorialVO(expediente);
		/*INICIO REQUERIMIENTO 286 27.11.2014*/
		int ans=0;
		List<ToePerfilEstado> lstToePerfilEstado= new ArrayList<ToePerfilEstado>();
		lstToePerfilEstado=Util.recuperaValoresTOEPerfilEstado(expediente, toePerfilEstadoBeanLocal);
		ToePerfilEstado toePerfilEstado=new ToePerfilEstado();
		if (lstToePerfilEstado.size()>0){
			toePerfilEstado = lstToePerfilEstado.get(0);
		}else{
			toePerfilEstado = null;
		}
		/*FIN REQUERIMIENTO 286 27.11.2014*/
		historial.setEstado(expedienteEntrada.getEstado());
		
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
		
		//Adjunta Documentos Expediente
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
		boolean datProd = datosProducto3MB.esValido();
		boolean validoComentario = comentarioMB.esValido();
		return datProd && validoComentario;
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
	
	public boolean isActivoDevRiesgos() {
		return activoDevRiesgos;
	}

	public void setActivoDevRiesgos(boolean activoDevRiesgos) {
		this.activoDevRiesgos = activoDevRiesgos;
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

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
import com.ibm.bbva.controller.common.DatosProducto3MB;
import com.ibm.bbva.controller.common.DocumentosEscaneadosMB;
import com.ibm.bbva.controller.common.ObservacionRechazoMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.controller.common.ProductoNuevoMB;
import com.ibm.bbva.entities.DevolucionTarea;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.session.DevolucionTareaBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.AyudaPLD;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "registrarAprobResolucion")
@RequestScoped
public class RegistrarAprobResolucionMB extends AbstractMBean {
	
	private boolean msgGuiaDocumentaria;
	private boolean activoDevolver;
	
	@EJB
	DevolucionTareaBeanLocal devolucionTareaBean; 
	@EJB
	ExpedienteBeanLocal expedienteBean;
	@EJB
	HistorialBeanLocal historialBean;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
		
	private Expediente expediente;
	private Expediente expedienteEntrada;
	
	private boolean renderedNoConforme;
	private AyudaPLD ayudaPLD;
	
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarAprobResolucionMB.class);
	
	public RegistrarAprobResolucionMB() {
	}
	
	@PostConstruct
    public void init() {
		LOG.info("Entro RegistrarAprobResolucion");
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		ayudaPLD=new AyudaPLD();
		
		if(expediente!=null && expediente.getId()>0)
			obtenerOpcionBotones(expediente.getId());		
	}	
	
	public void obtenerOpcionBotones(long idExpediente){
		setRenderedNoConforme(ayudaPLD.activarOpcionNoConforme(idExpediente));
	}	
	
	public String aceptar() {
		LOG.info("Aceptar - Exp : "+expediente.getId());
		Integer estado;
		if (esValido()) {
			
			/* Valida si cumple con guia documentaria */
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB)getObjectRequest("documentosEscaneados");
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {	
					
				msgGuiaDocumentaria = false;
				if(expediente!=null && expediente.getProducto()!=null && expediente.getProducto().getId()>0 && expediente.getProducto().getId()==Constantes.ID_APLICATIVO_TC)
					estado=Constantes.ESTADO_RESUELTO_TAREA_TC_TAREA_6;
				else
					estado=Constantes.ESTADO_RESUELTO_TAREA_DPS_TAREA_6;
				
				guardarExpediente(Constantes.ACCION_BOTON_RESOLVER, estado);
				
				addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
				addObjectSession(Constantes.ID_EXPEDIENTE_SESION, expediente.getId());
				msgGuiaDocumentaria = false;
				
		     } else {				
				msgGuiaDocumentaria = true;
				return null;
			}	
				
			//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";		
			return "/moverArchivos/formMoverArchivos?faces-redirect=true";
		}		
		return null;
	}
	
	public String devolver() {
		LOG.info("Devolver - Exp : "+expediente.getId());
		// muestra la ventana de observaciones
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
		ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "observacionRechazo");
		observacionRechazoMB.init();
		observacionRechazoMB.setMostrarDialogo(true);
		addObjectSession("actualizarObservados", "OK");
		return null;
	}
	
	public String noConforme() {
		LOG.info("noConforme - Exp : "+expediente.getId());
		if (esValido()) {
			guardarExpediente(Constantes.ACCION_BOTON_NO_CONFORME, Constantes.ESTADO_POR_REALIZAR_ALTA_TAREA_6);
			addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
        	addObjectSession(Constantes.ID_EXPEDIENTE_SESION, String.valueOf(expediente.getId()));
			//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
			return "/moverArchivos/formMoverArchivos?faces-redirect=true";
		}		
		return null;
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
		
		
		guardarExpediente (Constantes.ACCION_BOTON_DEVOLVER, Constantes.ESTADO_MODIFICACION_DE_ALTA_TAREA_6);
		for (DevolucionTarea devTarea : lista) {			
			devolucionTareaBean.create(devTarea);
		}
		
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}
	
	private void guardarExpediente (String accion, Integer estado) {
		
		FacesContext ctx = FacesContext.getCurrentInstance();		
		ComentarioMB comentario = null;
		DatosProducto3MB datosProducto3 = null;
		PanelDocumentosMB panelDocumentoMB = null;
		
		datosProducto3 = (DatosProducto3MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");
		
		//Se copia el producto 
		datosProducto3.copiarDatosExpediente();
		
		comentario = (ComentarioMB)  				
		ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");	
		comentario.copiarDatosExpediente();
		
        RemoteUtils objRemoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		expediente.setAccion(accion);
		Estado estadoTmp = new Estado();
		estadoTmp.setId(estado);
		expediente.setEstado(estadoTmp);
		
		//Desactivar expediente para bandeja de asignacion no muestre mensaje
		expediente.setFlagActivo("0");
		
		expedienteBean.edit(expediente);
		
		if(expediente!=null && expediente.getId()>0)
			LOG.info("Se actualizo Expediente "+ expediente.getId()+" en BD");
		//process
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion,	estado);

		String tkiid = expedienteTCWPS.getTaskID();
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

		
		ayudaExpedienteTC.actualizarListaExpedienteTC(consulta);*/
		/**
		 * fin de cambios
		 * */
		LOG.info("Se actualiza lista expediente");
		ayudaExpedienteTC.actualizarListaExpedienteTC(new Consulta());
		LOG.info("Fin de actualizar lista expediente");
		//fin process
		// se almacena en el historial
		Estado estadoTmp2 = new Estado();
		estadoTmp2.setId(expedienteEntrada.getEstado().getId());
		expediente.setEstado(estadoTmp2);
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
		
		//todos los documentos se actualizan como no observados
        panelDocumentoMB = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
        panelDocumentoMB.actualizarNoObservados();
		
		LOG.info("Se agrego a historial");
		
	}

	private boolean esValido() {		
		FacesContext ctx = FacesContext.getCurrentInstance();		
		ComentarioMB comentario = null;
		DatosProducto3MB producto  = null;
		
				
		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");	
		producto = (DatosProducto3MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");
		
		boolean validoComentario = comentario.esValido();
		boolean validoProd = producto.esValido();
		LOG.info("validoComentario = "+validoComentario);
		LOG.info("validoProd = "+validoProd);
		
		return validoComentario && validoProd;
	}
	
	public boolean isMsgGuiaDocumentaria() {
		return msgGuiaDocumentaria;
	}

	public void setMsgGuiaDocumentaria(boolean msgGuiaDocumentaria) {
		this.msgGuiaDocumentaria = msgGuiaDocumentaria;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Expediente getExpedienteEntrada() {
		return expedienteEntrada;
	}

	public void setExpedienteEntrada(Expediente expedienteEntrada) {
		this.expedienteEntrada = expedienteEntrada;
	}

	public boolean isActivoDevolver() {
		activoDevolver = false;
		if(expediente!=null && expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoOferta()!=null && expediente.getExpedienteTC().getTipoOferta().getCodigo()!=null) {
			if(String.valueOf(expediente.getExpedienteTC().getTipoOferta().getId()).equals(Constantes.CODIGO_OFERTA_REGULAR)) {
				activoDevolver = true;
			}else{
				activoDevolver = false;
			}
		}
		return activoDevolver;
	}

	public void setActivoDevolver(boolean activoDevolver) {
		this.activoDevolver = activoDevolver;
	}

	public boolean isRenderedNoConforme() {
		return renderedNoConforme;
	}

	public void setRenderedNoConforme(boolean renderedNoConforme) {
		this.renderedNoConforme = renderedNoConforme;
	}
	
}

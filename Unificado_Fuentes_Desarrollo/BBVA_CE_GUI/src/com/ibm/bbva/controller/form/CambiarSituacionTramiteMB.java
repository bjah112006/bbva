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

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.DocumentosEscaneadosMB;
import com.ibm.bbva.controller.common.ObservacionRechazoMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.controller.common.VerificarAprobarMB;
import com.ibm.bbva.entities.DevolucionTarea;
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
import com.ibm.bbva.util.Util;


@SuppressWarnings("serial")
@ManagedBean(name = "cambiarSituacionTramite")
@RequestScoped
public class CambiarSituacionTramiteMB extends AbstractMBean {
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
	private String titulo="";
	private boolean msgGuiaDocumentaria;
	
	private static final Logger LOG = LoggerFactory.getLogger(CambiarSituacionTramiteMB.class);
	
	public CambiarSituacionTramiteMB() {		

	}
	
	@PostConstruct
    public void init() {
		LOG.info("Entro CambiarSituacionTramite");
		// se obtiene el expedeinte de sesion
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
	}
	
	public String aceptar() {
		LOG.info("Confirmar Validacion - Exp : "+expediente.getId());
		if (esValido()) {
			/* Valida si cumple con guia documentaria */
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB)getObjectRequest("documentosEscaneados");
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				msgGuiaDocumentaria = false;
				grabarExp(Constantes.ACCION_BOTON_CONFIRMAR_VALIDACION,Constantes.ESTADO_POR_REALIZAR_ALTA_TAREA_17);
				addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
			    addObjectSession(Constantes.ID_EXPEDIENTE_SESION, expediente.getId());
			    msgGuiaDocumentaria = false;
			    //return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
			    return "/moverArchivos/formMoverArchivos?faces-redirect=true";
			} else {
				msgGuiaDocumentaria = true;
				return null;
			}
		}
		return null;
	}
	
	public String devolver() {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
		ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "observacionRechazo");
		observacionRechazoMB.init();
		observacionRechazoMB.setMostrarDialogo(true);
		return null;
	}	

	public String grabarExp(String accion, Integer estado) {
		// se obtiene el expedeinte de sesion
		FacesContext ctx = FacesContext.getCurrentInstance();  
		VerificarAprobarMB verificacion = null;
		ComentarioMB comentario = null;
		PanelDocumentosMB panelDocumentoMB = null;
		
		/*Se obtiene las Verificaciones*/
		verificacion = (VerificarAprobarMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");		
		
		// se obtiene el comentario
		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");		
		
		/*Se copia Verificacion*/
		verificacion.copiarDatosExpediente();		
		
		// se copia el comentario
		comentario.copiarDatosExpediente();
		
		/*Se actualiza expediente*/
		expediente.setAccion(accion);
		Estado estadoTmp = new Estado();
		estadoTmp.setId(estado.longValue());
		expediente.setEstado(estadoTmp);
        RemoteUtils objRemoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		//Desactivar expediente para bandeja de asignacion no muestre mensaje
		expediente.setFlagActivo("0");
		
		expedienteBean.edit(expediente);
		
		//process
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion,	estado);

		String tkiid = expedienteTCWPS.getTaskID();
		//objRemoteUtils.enviarExpedienteTC(tkiid, expedienteTCWPS);
		objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
		ayudaExpedienteTC.actualizarListaExpedienteTC(new Consulta());
        
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
		AyudaHistorial ayudaHistorial = new AyudaHistorial();	
		ExpedienteTCWPSWeb expedienteTCWPS1=Convertidor.fromObjExpedienteTCWPSToObjExpedienteTCWPSWeb(expedienteTCWPS, "1");
		ayudaHistorial.asignarFecha(historial, expedienteTCWPS1);
		
		historialBean.create(historial);
		
		//todos los documentos se actualizan como no observados
        panelDocumentoMB = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
        panelDocumentoMB.actualizarNoObservados();
		
		// se remueve el expedeinte de sesion
		removeObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		removeObjectSession(Constantes.EXPEDIENTE_SESION);
		
		return "";
	}
	
	/* Llamado por el metodo aceptar de la clase ObservacionRechazoMB
	 */
	/*public String accionVentana(List<DevolucionTarea> lista) {		
		
		for (DevolucionTarea devTarea : lista) {
			devolucionTareaBean.create(devTarea);
		}	
		grabarExp(Constantes.ACCION_BOTON_DEVOLVER, Constantes.ESTADO_EXPEDIENTE_OBSERVADO_TAREA_17);
		
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}*/
	
	private boolean esValido() {	
		
		FacesContext ctx = FacesContext.getCurrentInstance();	
		ComentarioMB comentario = null;
		
		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");	
		
		boolean validoComentario = comentario.esValido();
		return validoComentario;
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
	
	
}

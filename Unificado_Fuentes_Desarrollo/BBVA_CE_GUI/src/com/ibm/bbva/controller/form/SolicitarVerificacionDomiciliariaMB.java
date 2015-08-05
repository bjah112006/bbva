package com.ibm.bbva.controller.form;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.controller.common.VerificarAprobar2MB;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.TipoVerificacion;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.entities.VerificacionExp;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.session.VerificacionExpBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaDocumento;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "solicitarVerificacionDomiciliaria")
@RequestScoped
public class SolicitarVerificacionDomiciliariaMB extends AbstractMBean {
	
	@EJB
	ExpedienteBeanLocal expedienteBean;
	@EJB
	HistorialBeanLocal historialBean;
	@EJB
	VerificacionExpBeanLocal verificacionExpBean;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	
	private VerificacionExp verificacionExp; 
	private Expediente expediente;
	private Expediente expedienteEntrada;
	private String titulo="";
	
	private static final Logger LOG = LoggerFactory.getLogger(SolicitarVerificacionDomiciliariaMB.class);
	
	public SolicitarVerificacionDomiciliariaMB() { 
		
	}
	
	@PostConstruct
    public void init() {
		LOG.info("Entro RevisarRegistrarDictamen");
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
	}	
	
	public String enEspera() {
		LOG.info("En Espera - Exp : "+expediente.getId());
        if(esValido()) {        	
           grabarExp(Constantes.ACCION_BOTON_EN_ESPERA,
        		   Constantes.ESTADO_EN_ESPERA_DE_RESPUESTA_TAREA_26);
           addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
           addObjectSession(Constantes.ID_EXPEDIENTE_SESION, String.valueOf(expediente.getId()));
        }
		//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
		return "/moverArchivos/formMoverArchivos?faces-redirect=true";
	}
	
	public String grabarExp(String accion, Integer estado) {
        FacesContext ctx = FacesContext.getCurrentInstance();
		
		DatosProducto3MB datosProducto3 = null;
		VerificarAprobar2MB verificarAprobar2 = null;
		ComentarioMB comentario = null;
		PanelDocumentosMB panelDocumentoMB = null;
		
		/*Se obtiene el producto*/
		datosProducto3 = (DatosProducto3MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");
		/*Se obtiene las Verificaciones*/
		verificarAprobar2 = (VerificarAprobar2MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar2");
		// se obtiene el comentario		
		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");		
		
		/*Se actualiza Verificacion Expediente*/		
		List<VerificacionExp> listaVe = verificarAprobar2.cargarListaVerificaciones();
		
		VerificacionExp registro;
		
		for (VerificacionExp lista: listaVe) {
			registro = new VerificacionExp();
			
			TipoVerificacion tipoVerTmp = new TipoVerificacion();
			tipoVerTmp.setId(lista.getTipoVerificacion().getId());
				
			registro.setExpediente(expediente);
			registro.setTipoVerificacion(tipoVerTmp);
			
			if (lista.getId() == 0) {	
				verificacionExpBean.create(lista);
			}else{
				VerificacionExp vexp = verificacionExpBean.buscarPorId(lista.getId());				
				verificacionExp = new VerificacionExp();
				verificacionExp.setExpediente(new Expediente());
				verificacionExp.setTipoVerificacion(new TipoVerificacion());
				
				verificacionExp = vexp;
				verificacionExp.setFlagVerif(lista.getFlagVerif());
				verificacionExpBean.edit(verificacionExp);
			}		    
		}
		
		/*Se copia el producto */
		datosProducto3.copiarDatosExpediente();
		
		/*Se copia las verificaciones */
		verificarAprobar2.copiarDatosExpediente();
		
		// se copia el comentario
		comentario.copiarDatosExpediente();
		
		// se actualiza el expediente
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
		
		// se remueve el expedeinte de sesion
		removeObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		removeObjectSession(Constantes.EXPEDIENTE_SESION);
		
		return "";
	}
	
	private boolean esValido() {	
        FacesContext ctx = FacesContext.getCurrentInstance();
		
		DatosProducto3MB datosProducto3 = null;
		VerificarAprobar2MB verificarAprobar2 = null;
		ComentarioMB comentario = null;
		
		datosProducto3 = (DatosProducto3MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");
		
		verificarAprobar2 = (VerificarAprobar2MB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar2");
				
		comentario = (ComentarioMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");

		boolean validaDatosProducto3 = datosProducto3.esValido();		
		boolean validaVerificarAprobar2 = verificarAprobar2.esValido();
		boolean validoComentario = comentario.esValido();
		
		if(validaDatosProducto3 && validaVerificarAprobar2 && validoComentario) {
			return true;
		}		
		return false;
	}		

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}	
}

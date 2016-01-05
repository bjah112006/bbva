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
import com.ibm.bbva.controller.common.EnvioMailMB;
import com.ibm.bbva.controller.common.MenuMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.controller.common.VerificarAprobarMB;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "archivarExpediente")
@RequestScoped
public class ArchivarExpedienteMB extends AbstractMBean {
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private HistorialBeanLocal historialBean;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	
	private Expediente expediente;
	private Expediente expedienteEntrada;
	
	private String titulo="";
	
	private static final Logger LOG = LoggerFactory.getLogger(ArchivarExpedienteMB.class);
	
	public ArchivarExpedienteMB() {	

	}
	
	@PostConstruct
    public void init() {
		LOG.info("Entro ArchivarExpediente");
		// se obtiene el expedeinte de sesion
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
	}	
	
	public String aceptar() {
		LOG.info("Cerrar Expediente - Exp : "+expediente.getId());
		if (esValido()) {
			grabarExp(Constantes.ACCION_BOTON_CERRAR_EXPEDIENTE, Constantes.ESTADO_CERRADO_TAREA_27);
			addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
			addObjectSession(Constantes.ID_EXPEDIENTE_SESION, expediente.getId());
			try {
				enviarCorreo(expediente);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				LOG.error("Error al enviar el Mail..al guardar datos.", e);
			}
		}
		//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
		//EPY 28-12-2015
		//return "/moverArchivos/formMoverArchivos?faces-redirect=true";
		return redireccionar();
	}
	
	public String redireccionar() {
		String redireccion = null;
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		MenuMB menuMB = (MenuMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "menu");
		redireccion = menuMB.bandejaPendientes();
		return redireccion;
	}
	
	public void enviarCorreo(Expediente expediente) {
		FacesContext ctx=FacesContext.getCurrentInstance();
		EnvioMailMB envioMailMB = (EnvioMailMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "envioMail");
		try{
			envioMailMB.processEnvioCorreo(expediente);	
		}
		catch(Exception e){
			LOG.error(e.getMessage(), e);
			LOG.error("Error al enviar el Mail.. en Registrar Expediente", e);
		}
	}
		
	public void grabarExp(String accion, Integer estado) {			
		/*Se obtiene las Verificaciones*/
		FacesContext ctx = FacesContext.getCurrentInstance();  
		VerificarAprobarMB verificacion = null;
		ComentarioMB comentario = null;
		PanelDocumentosMB panelDocumentoMB = null;
		
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
		LOG.info(">>>>>>>>>>>>>>> TAREA 27 <<<<<<<<<<<<<<<<<<<<<");
		//process
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion,	estado);

		String tkiid = expedienteTCWPS.getTaskID();		
		//objRemoteUtils.enviarExpedienteTC(tkiid, expedienteTCWPS);
		objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
		//EPY 28/12/2015
		//ayudaExpedienteTC.actualizarListaExpedienteTC(new Consulta());
        
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
		
		if(historial.getFechaT1()!=null && historial.getFechaT2()!=null){
			LOG.info("historial- Fecha y Hora fecha 1:::"+historial.getFechaT1().toString());
			LOG.info("historial- Fecha y Hora fecha 2:::"+historial.getFechaT2().toString());
		}else
			LOG.info("f1 y f2 nulos");

		LOG.info("historial- Fecha y Hora fecha 3:::"+expedienteTCWPS.getActivado().toString());
		
		
		historialBean.create(historial);
		
		Historial historial2 = ConvertExpediente.convertToHistorialVO(expediente);
		
		// se almacena en el historial ultimo reg expediente
		Estado estadoTmp3 = new Estado();
		estadoTmp2.setId(estado);
		historial2.setEstado(estadoTmp2);
		historial2.setComentario("");
		/*INICIO REQUERIMIENTO 286 27.11.2014*/
		ans=0;
		if (toePerfilEstado!=null){
			historial2.setTiempoObjTC(toePerfilEstado.getTiempoObjTc().intValueExact());
			historial2.setTiempoObjTE(toePerfilEstado.getTiempoObjTe().intValueExact());
			historial2.setTiempoPreTC(toePerfilEstado.getTiempoPreTc().intValueExact());
			historial2.setTiempoPreTE(toePerfilEstado.getTiempoPreTe().intValueExact());
			historial2.setNomColumna(toePerfilEstado.getNomColumna());
			ans=toePerfilEstado.getTiempoObjTc().intValueExact()+toePerfilEstado.getTiempoObjTe().intValueExact();
			historial2.setAns(ans);
		}
		else{
			historial2.setTiempoObjTC(0);
			historial2.setTiempoObjTE(0);
			historial2.setTiempoPreTC(0);
			historial2.setTiempoPreTE(0);
			historial2.setNomColumna(null);
			historial2.setAns(0);
		}
		/*FIN REQUERIMIENTO 286 27.11.2014*/
		
		historialBean.create(historial2);
		
		//todos los documentos se actualizan como no observados
        panelDocumentoMB = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
        panelDocumentoMB.actualizarNoObservados();
		
		// se remueve el expedeinte de sesion
		removeObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		removeObjectSession(Constantes.EXPEDIENTE_SESION);		
	}
	
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

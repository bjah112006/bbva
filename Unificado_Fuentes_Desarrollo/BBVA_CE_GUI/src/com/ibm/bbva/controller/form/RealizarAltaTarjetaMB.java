package com.ibm.bbva.controller.form;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.DatosProducto3MB;
import com.ibm.bbva.controller.common.DocumentosEscaneadosMB;
import com.ibm.bbva.controller.common.EnvioMailMB;
import com.ibm.bbva.controller.common.ObservacionRechazoMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
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
import com.ibm.bbva.util.AyudaDocumento;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHistorial;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "realizarAltaTarjeta")
@RequestScoped
public class RealizarAltaTarjetaMB extends AbstractMBean {
	@EJB
	private DevolucionTareaBeanLocal devolucionTareaBean;
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private HistorialBeanLocal historialBean;
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	
	private Expediente expediente;
	private Expediente expedienteEntrada;
	//private MyLogger logger = MyLogger.getLogger(RealizarAltaTarjetaMB.class);
	private String strIdExp;
	private String msgErrorPersonalizado;
	private String msgPersonalizado;
	private boolean msgGuiaDocumentaria;
	
	private static final Logger LOG = LoggerFactory.getLogger(RealizarAltaTarjetaMB.class);
	
	public String getStrIdExp() {
		return strIdExp;
	}

	public void setStrIdExp(String strIdExp) {
		this.strIdExp = strIdExp;
	}

	public RealizarAltaTarjetaMB() {	
		LOG.info("Entro RealizarAltaTarjeta");
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expedienteEntrada = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		this.setStrIdExp(Long.toString(expediente.getId()));
	}	
	
	public String aceptar() {
		LOG.info("Cerrar Contrato y Alta - Exp : "+expediente.getId());
		if (esValido()) {
			//TipoEnvioDelegate tipoEnvioDelegate = new TipoEnvioDelegate ();
			//TipoEnvioVO filtEnv = new TipoEnvioVO ();
			//filtEnv.setId(expediente.getIdTipoEnvioFk());
			//TipoEnvioVO tipoEnvio = tipoEnvioDelegate.buscar(filtEnv).get(0);
		/*	if (Constantes.CODIGO_TIPO_ENVIO_OFICINA_GESTORA.equals(tipoEnvio.getCodigo())) {
				CorreoMB correoMB = new CorreoMB ();
				correoMB.setMostrarDialogo(true);
				addObjectRequest("correo", correoMB);
			} else {*/
				//LOG.info("Aceptar-expediente.getId().toString() "+expediente.getId().toString());
			
				ELContext elContext = FacesContext.getCurrentInstance().getELContext();
				DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
						.getCurrentInstance().getApplication().getELResolver()
						.getValue(elContext, null, "documentosEscaneados");
				LOG.info("valor de ValidaGuiaEscaneada ... "+documentosEscaneados.getValidaGuiaEscaneada());
				if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
					LOG.info("Si cumple condicion");
					LOG.info("VALIDO APPLET");
				
					actualizar(Constantes.ACCION_BOTON_CERRAR_CONTRATO_Y_ALTA, Constantes.ESTADO_TARJETA_EN_ELABORACION_ENVIO_TAREA_7);
					addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
					//LOG.info("this.getStrIdExp() "+this.getStrIdExp());
					addObjectSession(Constantes.ID_EXPEDIENTE_SESION, this.getStrIdExp());
					try {
						enviarCorreo(expediente);
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
						LOG.error("Error al enviar el Mail..al guardar datos.", e);
					}
					msgGuiaDocumentaria = false;
					//return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
					return "/moverArchivos/formMoverArchivos?faces-redirect=true";
				}
				else{
					LOG.info("No cumple condicion");
					//documentosEscaneados.setCleanTransferDir(Constantes.CLEANTRANFERDIR_CERO);
					msgGuiaDocumentaria = true;
					return null;
				}
			//}
		}
		return null;
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
	
	public String devolver() {
		LOG.info("Devolver - Exp : "+expediente.getId());
		//if (esValido()) {
		addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, null);
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "observacionRechazo");
		observacionRechazoMB.init();
		observacionRechazoMB.setMostrarDialogo(true);
		addObjectSession("actualizarObservados", "OK");
		//}
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
		
		
		actualizar(Constantes.ACCION_BOTON_DEVOLVER, Constantes.ESTADO_APROBADO_TAREA_7);
		
		for (DevolucionTarea devTarea : lista) {
			devolucionTareaBean.create(devTarea);
		}
		
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}
	
	private void actualizar (String accion, Integer estado) {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		LOG.info("Entro a metodo actualizar");
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		datosProducto3MB.copiarDatosExpediente();
		
		ComentarioMB comentarioMB = (ComentarioMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "comentario");
		comentarioMB.copiarDatosExpediente();

        RemoteUtils objRemoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(objRemoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		expediente.setAccion(accion);
		
		if (Constantes.ESTADO_APROBADO_TAREA_7==estado) {
			Util.actualizaDevoluciones(expediente);
		}
		
		LOG.info("accion:::"+accion);
		if(accion.equals(Constantes.ACCION_BOTON_DEVOLVER)){
			List<Historial> listTmp= historialBean.buscarXCriterioExpedienteYTarea_TareaSiete(expediente.getId(), Constantes.ID_TAREA_5.longValue(), Constantes.ID_TAREA_6.longValue());
			if(listTmp!=null && listTmp.size()>0){
				LOG.info("listTmp tamaño:::"+listTmp.size());
				String idEstadoTmp=String.valueOf(listTmp.get(0).getEstado().getId());
				LOG.info("idEstadoTmp:::"+idEstadoTmp);
				estado=Integer.parseInt(idEstadoTmp);
			}else
				LOG.info("listTmp es null o vacio");
		}
		
		Estado estadoExp = new Estado();
		estadoExp.setId(estado.longValue());
		expediente.setEstado(estadoExp);		
		
		
		//Desactivar expediente para bandeja de asignacion no muestre mensaje
		expediente.setFlagActivo("0");
		
		expedienteBean.edit(expediente);
		
		//process
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		LOG.info("Grabando en process estado:::"+estado);
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, accion,	estado);
		
		// para esta acción no se invoca al obtener usuario en Process
		if (accion.equals(Constantes.ACCION_BOTON_CERRAR_CONTRATO_Y_ALTA)) {
			Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
			
			expedienteTCWPS.setCodigoUsuarioActual(empleado.getCodigo());
			expedienteTCWPS.setIdPerfilUsuarioActual(""+empleado.getPerfil().getId());
			expedienteTCWPS.setPerfilUsuarioActual(""+empleado.getPerfil().getDescripcion());
			expedienteTCWPS.setNombreUsuarioActual(empleado.getNombresCompletos());
		}

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
	/*	if(expediente.getExpedienteTC().getFechaT1()!=null && expediente.getExpedienteTC().getFechaT2()!=null){
			LOG.info("Fecha y Hora fecha 1:::"+expediente.getExpedienteTC().getFechaT1().toString());
			LOG.info("Fecha y Hora fecha 2:::"+expediente.getExpedienteTC().getFechaT2().toString());
		}else
			LOG.info("f1 y f2 nulos");

		LOG.info("Fecha y Hora fecha 3:::"+expedienteTCWPS.getActivado().toString());*/
		LOG.info(">>>>>>>>>>>>>>> TAREA 7 <<<<<<<<<<<<<<<<<<<<<");
		objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
		//INICIO EPY 29122015
		//ayudaExpedienteTC.actualizarListaExpedienteTC(new Consulta());
		//FIN EPY 29122015
		
		//Adjunta Documentos Expediente
		AyudaDocumento ayudaDocumento = new AyudaDocumento();
		ayudaDocumento.adjuntarDocumentoExpediente();
		
		ayudaDocumento.editarDocumentoExpediente();
		
		removeObjectSession(Constantes.LISTA_DOC_EXP_ADJ);
		removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
		removeObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION);
		
		//todos los documentos se actualizan como no observados
		PanelDocumentosMB panelDocumentosMB = (PanelDocumentosMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "paneldocumentos");
		//panelDocumentosMB.actualizarNoObservados(); ---EPY 30122015
		panelDocumentosMB.actualizarNoObservados();
		
		//Remueve el tipo y numero de documento
		removeObjectSession("numeroDOI");
		removeObjectSession("tipoDOI");
				
		//fin process		
		// se almacena en el historial
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
		historial.setEstado(expedienteEntrada.getEstado());
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
	}
	
	/* Llamado por el metodo aceptar de la clase CorreoMB
	 */
	public void guardarDatos() {
		actualizar(Constantes.ACCION_BOTON_DEVOLVER, Constantes.ESTADO_APROBADO_TAREA_7);
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
		LOG.info("datProd : "+datProd);
		boolean validoComentario = comentarioMB.esValido();
		LOG.info("validoComentario : "+validoComentario);
		
		msgErrorPersonalizado = datosProducto3MB.getMsgErrorPersonalizado();
		msgPersonalizado = datosProducto3MB.getMsgPersonalizado();
		return datProd && validoComentario;
	}	
	
	private boolean validaNumContrato() {		
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		String formulario = obtenerNombreFormulario();
		boolean result = false;		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		DatosProducto3MB datosProducto3MB = (DatosProducto3MB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "datosProducto3");
		
		LOG.info("Nro Contrato : "+datosProducto3MB.getExpediente().getExpedienteTC().getNroContrato());
		LOG.info("empleado : "+empleado.getCodigo());
		
		result = bbvaFacade.verificarExisteContratoNacar(empleado.getCodigo(),datosProducto3MB.getClienteNatural().getCodCliente(),datosProducto3MB.getExpediente().getExpedienteTC().getNroContrato());
		LOG.info("result : "+result);
		if (!result){
			addMessageError(formulario + ":numContrato","com.ibm.bbva.common.datosProducto3.msg.numContrato.valida");
		}else{	
		    //logger.error("s.getHeader().getCodigo() : "+s.getHeader().getCodigo());
		}
		return result;
	}

	public String obtenerNombreFormulario() {
		String jspPrinc = getNombreJSPPrincipal();		
		return jspPrinc.substring(0, 1).concat(jspPrinc.substring(2));
	}
	
	public boolean isMsgGuiaDocumentaria() {
		return msgGuiaDocumentaria;
	}

	public void setMsgGuiaDocumentaria(boolean msgGuiaDocumentaria) {
		this.msgGuiaDocumentaria = msgGuiaDocumentaria;
	}

	public String getMsgErrorPersonalizado() {
		return msgErrorPersonalizado;
	}

	public void setMsgErrorPersonalizado(String msgErrorPersonalizado) {
		this.msgErrorPersonalizado = msgErrorPersonalizado;
	}	

	public String getMsgPersonalizado() {
		return msgPersonalizado;
	}

	public void setMsgPersonalizado(String msgPersonalizado) {
		this.msgPersonalizado = msgPersonalizado;
	}	
}
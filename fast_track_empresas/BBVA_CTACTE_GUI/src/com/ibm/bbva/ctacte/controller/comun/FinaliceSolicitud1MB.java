package com.ibm.bbva.ctacte.controller.comun;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Log;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IFinaliceSolicitud1;
import com.ibm.bbva.ctacte.controller.form.PreRegistroMB;
import com.ibm.bbva.ctacte.controller.form.RegistrarNuevoBastanteoMB;
import com.ibm.bbva.ctacte.controller.form.SubsanarDocumentoMB;
import com.ibm.bbva.ctacte.controller.form.SubsanarFirmasMB;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.EstadoTareaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaDAO;
import com.ibm.bbva.ctacte.dao.LogDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="finaliceSolicitud1")
@ViewScoped
public class FinaliceSolicitud1MB extends AbstractMBean {

	private static final long serialVersionUID = -5188963060411724143L;
	private static final Logger LOG = LoggerFactory.getLogger(FinaliceSolicitud1MB.class);
	
	@ManagedProperty (value="#{registrarNuevoBastanteo}")
	private RegistrarNuevoBastanteoMB registrarNuevoBastanteo;
	@ManagedProperty (value="#{preRegistro}")
	private PreRegistroMB preRegistro;
	@ManagedProperty (value="#{subsanarDocumento}")
	private SubsanarDocumentoMB subsanarDocumento;
	@ManagedProperty(value="#{subsanarFirmas}")
	private SubsanarFirmasMB subsanarFirmas;
	private IFinaliceSolicitud1 managedBean;
	
	private boolean chkIndAdicionales;
	private boolean chkEnviarSMS;
	private boolean chkEnviarNotifMail;
	private boolean blnReasignar = true;
	private String indicAdicionales;
	private String sms1;
	private String sms2;
	private String mail1;
	private String mail2;
	private String sms1Mem;
	private String sms2Mem;
	private String mail1Mem;
	private String mail2Mem;
	private String idMotivo;
	private boolean mostrar;
	private Expediente expediente;	
	private Operacion operacion;
	private Tarea tarea;
	private EstadoExpediente estado;
	private boolean mostSubDoc;
	private boolean mostRegMod;
	private boolean mostPreReg;
	private boolean mostMotCanc;
	private boolean mostBtnCanc;
	private boolean mostBtnPreR;
	private boolean disRegEnvExp;
	private boolean disGenerarPreReg;
	private boolean disGrabar;
	private boolean disCancProc;
	private List<SelectItem> listaMotivos;
	private List<Log> listalog;
	
	@EJB
	private ClienteBusiness clienteBusiness;
	@EJB
	private MotivoDAO motivoDAO;
	@EJB
	private EstadoTareaDAO estadoTareaDAO;
	@EJB
	private EstadoExpedienteDAO estadoExpedienteDAO;
	@EJB
	private TareaDAO tareaDAO;
	@EJB
	private ExpedienteTareaDAO expedienteTareaDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private LogDAO logDAO;
	@EJB
	private ExpedienteBusiness business;
	
	@PostConstruct
	public void iniciar () {		
		LOG.info("iniciar ()");
		iniciarEnvioFTP();
		mostrar = true;
		disRegEnvExp = true;
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		if (expediente != null) {
			operacion = expediente.getOperacion();
		}
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		if ("formRegistrarNuevoBastanteo".equals(pagina)) {
			managedBean = registrarNuevoBastanteo;
			managedBean.setFinaliceSolicitud1(this);
			mostRegMod = true;
			mostSubDoc = false;
			mostPreReg = false;
			mostMotCanc = false;
			disRegEnvExp = true;
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("estado boton registrar expediente:"+disRegEnvExp);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("estado boton Generar PreRegistro:"+disGenerarPreReg);
		} else if ("formSubsanarDocumentos".equals(pagina)){
			LOG.info("Entro a subsanarDocumentos");
			managedBean = subsanarDocumento;
			managedBean.setFinaliceSolicitud1(this);
			cargarDatosCliente1(expediente.getCliente(), expediente);
			LOG.info("numero celular"+expediente.getCliente().getNumeroCelular1());
			mostSubDoc = true;
			mostRegMod = false;
			mostPreReg = false;
			mostMotCanc = false;
			disRegEnvExp = false;
		} else if ("formSubsanarFirmas".equals(pagina)){
			LOG.info("Entro a SubsanarFirmas");
			managedBean=subsanarFirmas;
			managedBean.setFinaliceSolicitud1(this);
			cargarDatosCliente1(expediente.getCliente(), expediente);
			mostSubDoc = true;
			mostRegMod = false;
			indicAdicionales=null;
			mostPreReg = false;
			mostMotCanc = false;
			disRegEnvExp = false;
		} else if ("formPreRegistro".equals(pagina)){
			LOG.info("Entro a formPreRegistro");
			managedBean=preRegistro;
			managedBean.setFinaliceSolicitud1(this);
			cargarDatosCliente(expediente.getCliente(), expediente);
			mostSubDoc = false;
			mostRegMod = false;
			mostPreReg = true;
			mostMotCanc = false;
			disGrabar = false;
			LOG.info("Expediente en Preregistro");
			/*
			//+POR SOLICITUD BBVA+System.out..println("expediente.getResultadoBastanteo():"+expediente.getResultadoBastanteo());
			//+POR SOLICITUD BBVA+System.out..println("expediente.getCliente().getCodigoCentral():"+expediente.getCliente().getCodigoCentral());
			*/
//			if (expediente.getCuentaCobroComision() != null) {
//				mostrar = true;
//			} else {
//				mostrar = false;
//			}
			if (clienteBusiness.anteriorEsRevocatoria(expediente.getCliente().getCodigoCentral())) {
				disCancProc = true;
			}
			else
			{
				disCancProc = false;
			}		
			//int ID_ESTADO_EXPEDIENTE_TERMINADO = 3;
			//int ID_ESTADO_EXPEDIENTE_CANCELADO = 4;
			
			//LOG.info("************************PREREGISTRO********************");
			//LOG.info("************************ESTADO EXPEDIENTE ********************"+expediente.getEstado().getId());
			//LOG.info("************************BOTON CANCELAR DISABLED=********************"+disCancProc);
		
			if(expediente.getEstado().getId()==ConstantesBusiness.ID_ESTADO_EXPEDIENTE_TERMINADO || expediente.getEstado().getId()==ConstantesBusiness.ID_ESTADO_TAREA_CANCELADO)
			{
				disCancProc=true;
				disGrabar=true;
			}
			else if(expediente.getEstado().getId()==ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO)
			{
				disCancProc=false;
				disGrabar=false;
			}
			
		}
		listaMotivos = Util.crearItems(motivoDAO.findByTarea(ConstantesBusiness.ID_TAREA_PRE_REGISTRO, 
				ConstantesBusiness.TIPO_MOTIVO_CANCELAR_PROCESO), true, "id", "descripcion");
		LOG.info("expediente init : {}",expediente);
	}
	
	public void cargarDatosCliente (ClientePJCore clientePJCore) {
		LOG.info("cargarDatosCliente (ClientePJCore clientePJCore)");
		DatosClientePJCore datosCliente = clientePJCore.getDatosCore();
		sms1 = datosCliente.getNroCelular1(); 
		sms1Mem = datosCliente.getNroCelular1();
		sms2 = datosCliente.getNroCelular2(); 
		sms2Mem = datosCliente.getNroCelular2();
		mail1 = datosCliente.getCorreoElectronico1(); 
		mail1Mem = datosCliente.getCorreoElectronico1();
		mail2 = datosCliente.getCorreoElectronico2(); 
		mail2Mem = datosCliente.getCorreoElectronico2();
		habilitarCampos();
	}
	
	public void cancelarExpediente(ActionEvent ae){
		LOG.info("cancelarExpediente(ActionEvent ae)");		
		String accion=ConstantesBusiness.ACCION_EXPEDIENTE_RECHAZAR;
		ejecutarCancelarExpediente(accion);
		redirectAction("../bandeja/bandeja");		
	}
	
	private void ejecutarCancelarExpediente(String accion){
		indicAdicionales=null;
		sms1=null; 
		sms1Mem = null;
		sms2=null; 
		sms2Mem = null;
		mail1=null; 
		mail1Mem = null;
		mail2=null;	
		mail2Mem = null;	 
	}
	
	public void cancelarProceso (AjaxBehaviorEvent event) {
		LOG.info("cancelarProceso ()");
		mostMotCanc = true;		
	}
	
	public void seleccionarMotivo (AjaxBehaviorEvent event) {
		LOG.info("seleccionarMotivo ()");
		LOG.info("idMotivo : {}", idMotivo);
		cancelarExpediente(ConstantesBusiness.ID_TAREA_PRE_REGISTRO, Integer.parseInt(idMotivo));
		addCallbackParam("mensaje", "Se ha cancelado el expediente con el código "+expediente.getId());
	}
	
	public void redireccionar (ActionEvent event) {
		redirectAction("../bandeja/bandeja");
	}

	private void cancelarExpediente(int idTarea, int idMotivo) {
		LOG.info("cancelarExpediente(int idTarea, int idMotivo)");
		EstadoTarea estadoTarea = estadoTareaDAO.load(ConstantesBusiness.ID_ESTADO_TAREA_CANCELADO);
		
		EstadoExpediente estadoExpediente = estadoExpedienteDAO.load(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_CANCELADO);
		
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		expediente.setEstado(estadoExpediente);
		
		ExpedienteTarea expedienteTarea = new ExpedienteTarea ();
		expedienteTarea.setEstadoTarea(estadoTarea);
		expedienteTarea.setExpediente(expediente);
		
		expedienteTarea.setTarea(tareaDAO.load(idTarea));
		
		expedienteTareaDAO.save(expedienteTarea);
		expediente.getExpedienteTareas().add(expedienteTarea);
		
		expediente.setMotivo(motivoDAO.load(idMotivo));
		
		expediente = expedienteDAO.update(expediente);
		
		Util.generarRegistroHistExp(expediente);
		
		// envío de correo por cancelar proceso
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		business.enviarCorreo(expediente.getId().toString(),
				String.valueOf(ConstantesBusiness.ID_TAREA_REGISTRAR_EXPEDIENTE),
				"undefined",
				empleado.getCodigo(),
				"undefined",
				ConstantesAdmin.ACCION_CANCELAR_PROCESO);
	}
	
	public void reenviarExpediente(ActionEvent ae){
		LOG.info("reenviarExpediente(ActionEvent ae)");
		if (esValidoNumerosSMS()) {
			String accion=ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR;
			boolean grabaOk = validaLog();
			managedBean.grabarReenviarExpediente(accion);
			if (grabaOk){
				grabaLog();
			}
		} else {
			mensajeErrorPrincipal("idFinaliceSol:idSms", "Ingrese un número de celular correcto");
		}
	}

	private void habilitarCampos() {
		LOG.info("habilitarCampos()");
		/*COMENTADO 27/11/2015*/
		chkEnviarSMS = false;
		chkEnviarNotifMail=false;
		/*--------------------*/
		if (StringUtils.isNotEmpty(sms1) || StringUtils.isNotEmpty(sms2)) {
//			chkEnviarSMS = true;			
		}
		LOG.info("chkEnviarSMS : {}", chkEnviarSMS);
		if (StringUtils.isNotEmpty(mail1) || StringUtils.isNotEmpty(mail2)) {
//			chkEnviarNotifMail = true;
		}
		if (StringUtils.isNotEmpty(indicAdicionales)) {
			chkIndAdicionales = true;
		}
		LOG.info("chkEnviarNotifMail : {}", chkEnviarNotifMail);
	}
	
	private void cargarDatosCliente (Cliente cliente, Expediente expediente) {
		LOG.info("cargarDatosCliente (Cliente cliente)");
		sms1 = cliente.getNumeroCelular1(); 
		sms1Mem = cliente.getNumeroCelular1(); 
		sms2 = cliente.getNumeroCelular2(); 
		sms2Mem = cliente.getNumeroCelular2(); 
		mail1 = cliente.getCorreoElectronico1(); 
		mail1Mem = cliente.getCorreoElectronico1(); 
		mail2 = cliente.getCorreoElectronico2(); 
		mail2Mem = cliente.getCorreoElectronico2(); 
		indicAdicionales = expediente.getObservaciones();
		habilitarCampos();
	}
	
	private void cargarDatosCliente1 (Cliente cliente, Expediente expediente) {
		LOG.info("cargarDatosCliente (Cliente cliente)");
		sms1 = cliente.getNumeroCelular1(); 
		sms1Mem = cliente.getNumeroCelular1(); 
		sms2 = cliente.getNumeroCelular2(); 
		sms2Mem = cliente.getNumeroCelular2(); 
		mail1 = cliente.getCorreoElectronico1(); 
		mail1Mem = cliente.getCorreoElectronico1(); 
		mail2 = cliente.getCorreoElectronico2(); 
		mail2Mem = cliente.getCorreoElectronico2(); 
		
		habilitarCampos();
	}
	
	
	public boolean esValido () {
		LOG.info("esValido ()");
		boolean esCorrecto = true;
		LOG.info("chkIndAdicionales : {}",chkIndAdicionales);
		LOG.info("operacion : {}",operacion);
		/*if (ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(operacion.getCodigoOperacion())) {
			if (!chkIndAdicionales || StringUtils.isEmpty(indicAdicionales)) {			
				mensajeErrorPrincipal("idFinaliceSol:idIndAd", "Ingrese las indicaciones Adicionales");
				esCorrecto = false;
			}
		} else {*/
			if (chkIndAdicionales){
				LOG.info("indicAdicionales : {}",indicAdicionales);
				if (StringUtils.isEmpty(indicAdicionales)) {
					mensajeErrorPrincipal("idFinaliceSol:idIndAd", "Ingrese las indicaciones Adicionales");
					esCorrecto = false;
				}
			}
		//}
		LOG.info("chkEnviarNotifMail : {}",chkEnviarNotifMail);
		if (chkEnviarNotifMail) {
			LOG.info("mail1 : {}  -  mail2 : {}",mail1, mail2);
			if (StringUtils.isEmpty(mail1) && StringUtils.isEmpty(mail2)) {
				mensajeErrorPrincipal("idFinaliceSol:idMail", "Ingrese un Correo Electrónico");
				esCorrecto = false;
			} else if (StringUtils.isNotEmpty(mail1) && !Util.validoEmail(mail1)) {
				mensajeErrorPrincipal("idFinaliceSol:idMail", 
						"El formato del Primer Correo Electrónico es incorrecto");
				esCorrecto = false;
			} else if (StringUtils.isNotEmpty(mail2) && !Util.validoEmail(mail2)) {
				mensajeErrorPrincipal("idFinaliceSol:idMail", 
						"El formato del Segundo Correo Electrónico es incorrecto");
				esCorrecto = false;
			}
		}
		LOG.info("chkEnviarSMS : {}",chkEnviarSMS);
		if (chkEnviarSMS) {
			LOG.info("sms1 : {}  -  sms2 : {}",sms1, sms2);
			if (StringUtils.isEmpty(sms1) && StringUtils.isEmpty(sms2)) {
				mensajeErrorPrincipal("idFinaliceSol:idSms", "Ingrese un número de Celular");
				esCorrecto = false;
			} else if (!esValidoNumerosSMS()) {
				mensajeErrorPrincipal("idFinaliceSol:idSms", "Ingrese un número de celular correcto");
				esCorrecto = false;
			}
		}
		return esCorrecto;
	}
	
	public boolean esValidoNumerosSMS () {
		if (chkEnviarSMS) {
			LOG.info("-sms1 : {}",sms1);
			if (StringUtils.isNotEmpty(sms1) && (!StringUtils.isNumeric(sms1) || !(sms1.charAt(0)=='9') || !(sms1.length()==9))) {
				return false;
			}
			LOG.info("-sms2 : {}",sms2);
			if (StringUtils.isNotEmpty(sms2) && (!StringUtils.isNumeric(sms2) || !(sms2.charAt(0)=='9') || !(sms2.length()==9))) {
				return false;
			}
		}
		return true;
	}

	public boolean hayIndicaciones(){
		boolean esCorrecto = true;
		LOG.info("chkIndAdicionales : {}",chkIndAdicionales);
		if (chkIndAdicionales) {
			LOG.info("indicAdicionales : {}",indicAdicionales);
			if (StringUtils.isEmpty(indicAdicionales)) {
				mensajeErrorPrincipal("idFinaliceSol:idIndAd", "Ingrese las indicaciones Adicionales");
				esCorrecto = false;
			}
		}
		
		return esCorrecto;	
	}	
	
	public void copiarDatos() {
		LOG.info("copiarDatos(List<DocumentoExp> listaDocumentoExp)");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		LOG.info("expediente fin "+expediente);
		if (chkIndAdicionales) {
			expediente.setObservaciones(indicAdicionales);
		} else {
			expediente.setObservaciones(null);
		}
		Cliente cliente = expediente.getCliente();
		LOG.info("chkEnviarNotifMail : {}",chkEnviarNotifMail);
		if (chkEnviarSMS) {
			cliente.setNumeroCelular1(sms1);
			LOG.info("sms1 : {}",sms1);
			cliente.setNumeroCelular2(sms2);
			LOG.info("sms2 : {}",sms2);
			expediente.setFlagCorreo(ConstantesBusiness.FLAG_TIENE_CORREO);
		} else {
			cliente.setNumeroCelular1(null);
			cliente.setNumeroCelular2(null);
			expediente.setFlagCorreo(ConstantesBusiness.FLAG_NO_TIENE_CORREO);
		}
		LOG.info("chkEnviarSMS : {}",chkEnviarSMS);
		if (chkEnviarNotifMail) {
			cliente.setCorreoElectronico1(mail1);
			LOG.info("mail1 : {}",mail1);
			cliente.setCorreoElectronico2(mail2);
			LOG.info("mail2 : {}",mail2);
			expediente.setFlagSmstexto(ConstantesBusiness.FLAG_TIENE_SMS);
		} else {
			cliente.setCorreoElectronico1(null);
			cliente.setCorreoElectronico2(null);
			expediente.setFlagSmstexto(ConstantesBusiness.FLAG_NO_TIENE_SMS);
		}
	}
	
	public String generarPreRegistro () {
		LOG.info("generarPreregistro ()");
		LOG.info("**********FinaliceSolicitudMB-generarPrerRegistro()");
		if (esValidoNumerosSMS()) {
			/*
			try
			{
			*/
				Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
				managedBean.copiarDatos();
				managedBean.guardarExpediente (ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO,
						ConstantesBusiness.ID_TAREA_PRE_REGISTRO, 
						ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO, 
						ConstantesAdmin.ACCION_GENERAR_PRE_REGISTRO);
				// envío de correo por pre-registro
				Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
				business.enviarCorreo(expediente.getId().toString(),
						String.valueOf(ConstantesBusiness.ID_TAREA_REGISTRAR_EXPEDIENTE),
						"undefined",
						empleado.getCodigo(),
						"undefined",
						ConstantesAdmin.ACCION_GENERAR_PRE_REGISTRO);
				
				return redirectMensaje("Se ha guardado correctamente el expediente "+expediente.getId()+".");
			/*
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
			*/
			
		} else {
			mensajeErrorPrincipal("idFinaliceSol:idSms", "Ingrese un número de celular correcto");
			return null;
		}
	}
	
	public String registrarExpediente () {
		LOG.info("registrarExpediente ()");
		LOG.info("**********FinaliceSolicitudMB-registrarExpediente()");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		if (managedBean.esValido()) {
			try
			{
				LOG.info("**********TRY-FinaliceSolicitudMB-registrarExpediente()");
				managedBean.copiarDatos();
				boolean grabaOk = validaLog();
				LOG.info("**********grabaOk***********"+grabaOk);
				managedBean.guardarExpediente(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO,
						ConstantesBusiness.ID_TAREA_REGISTRAR_EXPEDIENTE, 
						ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE, 
						ConstantesAdmin.ACCION_REGISTRAR_ENVIAR_EXPEDIENTE);
				if (grabaOk){
					grabaLog();
				}
				return redirectMensaje("Se ha registrado correctamente el expediente "+expediente.getId()+".");
			}catch(Exception e){
				LOG.error("**********CATCH-FinaliceSolicitudMB-registrarExpediente()", e);
				//e.printStackTrace();
				return null;
			}	
		}
		return null;
	}
	
	public boolean validaLog(){
		boolean result = false;
		Log log = null;
		listalog = new ArrayList<Log>();

		if (expediente != null && expediente.getId() != null) {
			Expediente expedienteOri = expedienteDAO.load(expediente.getId());			
			sms1Mem = expedienteOri.getCliente().getNumeroCelular1() == null ? "" : expedienteOri.getCliente().getNumeroCelular1();
			sms2Mem = expedienteOri.getCliente().getNumeroCelular2() == null ? "" : expedienteOri.getCliente().getNumeroCelular2();
			mail1Mem = expedienteOri.getCliente().getCorreoElectronico1() == null ? "" : expedienteOri.getCliente().getCorreoElectronico1();
			mail2Mem = expedienteOri.getCliente().getCorreoElectronico2() == null ? "" : expedienteOri.getCliente().getCorreoElectronico2();		
		}else{
			sms1Mem = sms1Mem == null ? "" : sms1Mem;
			sms2Mem = sms2Mem == null ? "" : sms2Mem;
			mail1Mem = mail1Mem == null ? "" : mail1Mem;
			mail2Mem = mail2Mem == null ? "" : mail2Mem;			
		}		

		sms1 = sms1 == null ? "" : sms1;
		sms2 = sms2 == null ? "" : sms2;
		mail1 = mail1 == null ? "" : mail1;
		mail2 = mail2 == null ? "" : mail2;		
		
		if (!sms1.equalsIgnoreCase(sms1Mem)) {
			log = new Log();
		    log.setDescripcion("Se ha modificado el campo NUMERO_CELULAR_1 al valor "+sms1);
		    listalog.add(log);
		    result=true;
		}
		if (!sms2.equalsIgnoreCase(sms2Mem)) {
			log = new Log();
		    log.setDescripcion("Se ha modificado el campo NUMERO_CELULAR_2 al valor "+sms2);
		    listalog.add(log);
		    result=true;
		}
		if (!mail1.equalsIgnoreCase(mail1Mem)) {
			log = new Log();
		    log.setDescripcion("Se ha modificado el campo CORREO_ELECTRONICO_1 al valor "+mail1);
		    listalog.add(log);
		    result=true;
		}
		if (!mail2.equalsIgnoreCase(mail2Mem)) {
			log = new Log();
		    log.setDescripcion("Se ha modificado el campo CORREO_ELECTRONICO_2 al valor "+mail2);
			listalog.add(log);
			result=true;
		}

		return result;
	}
	
	public void grabaLog(){
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
        for(Log log: listalog) {  
        	//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("++log.getDescripcion() for: "+log.getDescripcion());        	
    		log.setEmpleado(expediente.getEmpleado());    		
    		log.setEstadoExpediente(expediente.getEstado());		
    		log.setFecRegistro(new Date());
    		log.setExpediente(expediente);
    		log.setNumTerminal(Util.obtenerIp());    		
        	logDAO.save(log);
        }
	}
	
	public void selecIndAdicionales (AjaxBehaviorEvent event) {
		LOG.info("selecIndAdicionales ()");
		activarGrabar ();
	}
	
	public void cambiarIndicAdicionales () {
		LOG.info("cambiarIndicAdicionales ()");
		activarGrabar ();
	}
	
	public void selecEnviarSMS (AjaxBehaviorEvent event) {
		LOG.info("selecEnviarSMS ()");
		activarGrabar ();
	}
	
	public void cambiarSms1 (AjaxBehaviorEvent event) {
		LOG.info("cambiarSms1 ()");
		activarGrabar ();
	}
	
	public void cambiarSms2 (AjaxBehaviorEvent event) {
		LOG.info("cambiarSms2 ()");
		activarGrabar ();
	}
	
	public void selecEnviarNotifMail (AjaxBehaviorEvent event) {
		LOG.info("selecEnviarNotifMail ()");
		activarGrabar ();
	}
	
	public void cambiarMail1 (AjaxBehaviorEvent event) {
		LOG.info("cambiarMail1 ()");
		activarGrabar ();
	}
	
	public void cambiarMail2 (AjaxBehaviorEvent event) {
		LOG.info("cambiarMail2 ()");
		activarGrabar ();
	}
	
	private void activarGrabar () {
		if (disRegEnvExp) {
			disGrabar = false;
		}
	}

	public void setOperacion(Operacion operacion) {
		LOG.info("setOperacion(Operacion operacion) : {}", operacion);
		this.operacion = operacion;
		if (ConstantesBusiness.CODIGO_REVOCATORIA_ESPECIFICA.equals(operacion.getCodigoOperacion())) {
			mostBtnCanc = true;
			mostBtnPreR = false;
		} else if (ConstantesBusiness.CODIGO_CAMBIO_FIRMAS.equals(operacion.getCodigoOperacion())
				|| ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(operacion.getCodigoOperacion())) {
			mostBtnPreR = false;
			mostBtnCanc = false;
		} else {
			mostBtnCanc = false;
			mostBtnPreR = true;
		}
		
	}

	public Operacion getOperacion() {
		return operacion;
	}
	
	public boolean isMostrar() {
		return mostrar;
	}

	public void setMostrar(boolean mostrar) {
		this.mostrar = mostrar;
	}

	public boolean isChkIndAdicionales() {
		return chkIndAdicionales;
	}

	public void setChkIndAdicionales(boolean chkIndAdicionales) {
		this.chkIndAdicionales = chkIndAdicionales;
	}

	public boolean isChkEnviarSMS() {
		return chkEnviarSMS;
	}

	public void setChkEnviarSMS(boolean chkEnviarSMS) {
		this.chkEnviarSMS = chkEnviarSMS;
	}

	public boolean isChkEnviarNotifMail() {
		return chkEnviarNotifMail;
	}

	public void setChkEnviarNotifMail(boolean chkEnviarNotifMail) {
		this.chkEnviarNotifMail = chkEnviarNotifMail;
	}

	public String getIndicAdicionales() {
		return indicAdicionales;
	}

	public void setIndicAdicionales(String indicAdicionales) {
		this.indicAdicionales = indicAdicionales;
	}

	public String getSms1() {
		return sms1;
	}

	public void setSms1(String sms1) {
		this.sms1 = sms1;
	}

	public String getSms2() {
		return sms2;
	}

	public void setSms2(String sms2) {
		this.sms2 = sms2;
	}

	public String getMail1() {
		return mail1;
	}

	public void setMail1(String mail1) {
		this.mail1 = mail1;
	}

	public String getMail2() {
		return mail2;
	}

	public void setMail2(String mail2) {
		this.mail2 = mail2;
	}

	public SubsanarDocumentoMB getSubsanarDocumento() {
		return subsanarDocumento;
	}

	public void setSubsanarDocumento(SubsanarDocumentoMB subsanarDocumento) {
		this.subsanarDocumento = subsanarDocumento;
	}

	public boolean isMostSubDoc() {
		return mostSubDoc;
	}

	public void setMostSubDoc(boolean mostSubDoc) {
		this.mostSubDoc = mostSubDoc;
	}

	public boolean isMostRegMod() {
		return mostRegMod;
	}

	public void setMostRegMod(boolean mostRegMod) {
		this.mostRegMod = mostRegMod;
	}

	public SubsanarFirmasMB getSubsanarFirmas() {
		return subsanarFirmas;
	}

	public void setSubsanarFirmas(SubsanarFirmasMB subsanarFirmas) {
		this.subsanarFirmas = subsanarFirmas;
	}

	public IFinaliceSolicitud1 getManagedBean() {
		return managedBean;
	}

	public void setManagedBean(IFinaliceSolicitud1 managedBean) {
		this.managedBean = managedBean;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public EstadoExpediente getEstado() {
		return estado;
	}

	public void setEstado(EstadoExpediente estado) {
		this.estado = estado;
	}
	
	public boolean isDisRegEnvExp() {
		return disRegEnvExp;
	}

	public void setDisRegEnvExp(boolean disRegEnvExp) {
		this.disRegEnvExp = disRegEnvExp;
	}

	public boolean isDisGenerarPreReg() {
		return disGenerarPreReg;
	}

	public void setDisGenerarPreReg(boolean disGenerarPreReg) {
		this.disGenerarPreReg = disGenerarPreReg;
	}

	public boolean isMostPreReg() {
		return mostPreReg;
	}

	public void setMostPreReg(boolean mostPreReg) {
		this.mostPreReg = mostPreReg;
	}

	public boolean isMostMotCanc() {
		return mostMotCanc;
	}

	public void setMostMotCanc(boolean mostMotCanc) {
		this.mostMotCanc = mostMotCanc;
	}

	public List<SelectItem> getListaMotivos() {
		return listaMotivos;
	}

	public void setListaMotivos(List<SelectItem> listaMotivos) {
		this.listaMotivos = listaMotivos;
	}

	public String getIdMotivo() {
		return idMotivo;
	}

	public void setIdMotivo(String idMotivo) {
		this.idMotivo = idMotivo;
	}

	public boolean isDisGrabar() {
		return disGrabar;
	}

	public void setDisGrabar(boolean disGrabar) {
		this.disGrabar = disGrabar;
	}

	public void setRegistrarNuevoBastanteo(RegistrarNuevoBastanteoMB registrarNuevoBastanteo) {
		this.registrarNuevoBastanteo = registrarNuevoBastanteo;
	}

	public RegistrarNuevoBastanteoMB getRegistrarNuevoBastanteo() {
		return registrarNuevoBastanteo;
	}

	public void setPreRegistro(PreRegistroMB preRegistro) {
		this.preRegistro = preRegistro;
	}

	public PreRegistroMB getPreRegistro() {
		return preRegistro;
	}

	public void setMostBtnCanc(boolean mostBtnCanc) {
		this.mostBtnCanc = mostBtnCanc;
	}

	public boolean isMostBtnCanc() {
		return mostBtnCanc;
	}

	public void setMostBtnPreR(boolean mostBtnPreR) {
		this.mostBtnPreR = mostBtnPreR;
	}

	public boolean isMostBtnPreR() {
		return mostBtnPreR;
	}

	public boolean isBlnReasignar() {
		return blnReasignar;
	}

	public void setBlnReasignar(boolean blnReasignar) {
		this.blnReasignar = blnReasignar;
	}

	public void setDisCancProc(boolean disCancProc) {
		this.disCancProc = disCancProc;
	}

	public boolean isDisCancProc() {
		return disCancProc;
	}

	public List<Log> getListalog() {
		return listalog;
	}

	public void setListalog(List<Log> listalog) {
		this.listalog = listalog;
	}

	public String getSms1Mem() {
		return sms1Mem;
	}

	public void setSms1Mem(String sms1Mem) {
		this.sms1Mem = sms1Mem;
	}

	public String getSms2Mem() {
		return sms2Mem;
	}

	public void setSms2Mem(String sms2Mem) {
		this.sms2Mem = sms2Mem;
	}

	public String getMail1Mem() {
		return mail1Mem;
	}

	public void setMail1Mem(String mail1Mem) {
		this.mail1Mem = mail1Mem;
	}

	public String getMail2Mem() {
		return mail2Mem;
	}

	public void setMail2Mem(String mail2Mem) {
		this.mail2Mem = mail2Mem;
	}	
}

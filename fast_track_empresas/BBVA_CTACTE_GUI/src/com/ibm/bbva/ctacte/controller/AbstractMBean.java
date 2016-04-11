package com.ibm.bbva.ctacte.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.comun.EnvioFTPMB;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.jsf2.custom.context.CustomPartialResponseWriter;

public abstract class AbstractMBean implements Serializable {

	private static final long serialVersionUID = 2298554199433638716L;
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMBean.class);

	private String servidorServWeb;
	private String puertoServWeb;
	private String descargados;
	private String transferencias;
	private String log;
	private String servidorFTP;
	private String usuarioFTP;
	private String passwordFTP;
	private String periodoTranfFTP;
	private String tasaKBytes;
	private String servidorConvPDF;
	private String puertoConvPDF;
	private String urlEscaneoWeb;
	private String emailMesaDocumentos;
	private String emailMesaFirmas;
	private String hostServidorCorreo;
	private String puertoServidorCorreo;
	private String urlManualUsuario;
	private String carpetaFTP;
	private String tamanoMaxPDF;
	private String numReintentos;
	private String intentosWS;

	public AbstractMBean() {
		LOG.info("AbstractMBean()");
		ParametrosSistema parametros = ParametrosSistema.getInstance();
		Properties properties = parametros.getProperties(ParametrosSistema.CONF);
		servidorServWeb = properties.getProperty(ConstantesParametros.SERVIDOR_SERV_WEB);
		puertoServWeb = properties.getProperty(ConstantesParametros.PUERTO_SERV_WEB);
		descargados = properties.getProperty(ConstantesParametros.CARPETA_CLIENTE_DESCARGADOS);
		transferencias = properties.getProperty(ConstantesParametros.CARPETA_CLIENTE_TRANSFERENCIAS);
		log = properties.getProperty(ConstantesParametros.CARPETA_CLIENTE_LOG);
		servidorFTP = properties.getProperty(ConstantesParametros.SERVIDOR_FTP);
		usuarioFTP = properties.getProperty(ConstantesParametros.USUARIO_FTP);
		passwordFTP = properties.getProperty(ConstantesParametros.PASSWORD_FTP);
		periodoTranfFTP = properties.getProperty(ConstantesParametros.PERIODO_TRANSFERENCIA_FTP);
		tasaKBytes = properties.getProperty(ConstantesParametros.TASA_KBYTES_FTP);
		servidorConvPDF = properties.getProperty(ConstantesParametros.SERVIDOR_CONV_PDF);
		puertoConvPDF = properties.getProperty(ConstantesParametros.PUERTO_CONV_PDF);	
		urlEscaneoWeb = properties.getProperty(ConstantesParametros.URL_ESCANEO_WEB);
		emailMesaDocumentos = properties.getProperty(ConstantesParametros.EMAIL_MESA_DOCUMENTOS);
		emailMesaFirmas = properties.getProperty(ConstantesParametros.EMAIL_MESA_FIRMAS);
		hostServidorCorreo = properties.getProperty(ConstantesParametros.HOST_SERVIDOR_CORREO);
		puertoServidorCorreo = properties.getProperty(ConstantesParametros.PUERTO_SERVIDOR_CORREO);
		
		//urlManualUsuario = properties.getProperty(ConstantesParametros.URL_MANUAL_USUARIO);
		//urlManualUsuario = "/BBVA_CTACTE_GUI/manualUsuario/manual_usuario.html";
		
		String junction	= (String) ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).get(ConstantesParametros.JUNCTION_NAME_CTACTE);
		//System.out.println("junction::::"+junction);
		if(junction == null){
			junction = "";
		}
		else{
			junction = "/"+junction;
		}
		urlManualUsuario = junction + "/BBVA_CTACTE_GUI/manualUsuario/manual_usuario.html";
		
		carpetaFTP = properties.getProperty(ConstantesParametros.CARPETA_FTP);
		tamanoMaxPDF = properties.getProperty(ConstantesParametros.TAMANO_MAXIMO_ARCHIVO_MB);
		numReintentos = properties.getProperty(ConstantesParametros.NUMERO_REINTENTOS);
		intentosWS	= properties.getProperty(ConstantesParametros.INTENTOS_WS_CTACTE);
		
		
////////////////////////////////////////////////////////////////////////////
//		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
//		if (empleado == null) {
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("empleado nulo");
//			empleado = DAOFactory.getInstance().getEmpleadoDAO().load(26);
//			Util.addObjectSession(ConstantesAdmin.EMPLEADO_SESION, empleado);
//		}
//		String nombre = getNombrePrincipal();
//		if (!"formRegistrarModificatorias".equals(nombre)
//				&& !"bandeja/temporal".equals(nombre) && !"preRegistro/formPreRegistro".equals(nombre)) {
//			cargarDatos(nombre);
//		}
//		

	}
	
	private static synchronized void cargarDatos (String nombre) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("void cargarDatos (String nombre)");
		if (Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION)==null) {
			//+POR SOLICITUD BBVA+System.out..println("expediente nulo");
			Expediente expediente;
			if ("formRegistrarModificatorias".equals(nombre)) {
				expediente = new Expediente();
			} else {
				ExpedienteDAO dao = EJBLocator.getExpedienteDAO();
				expediente = dao.load(123);
			}
			Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
			expediente.setEmpleado(empleado);
			expediente.setOficina(empleado.getOficina());
			//+POR SOLICITUD BBVA+System.out..println("OFICINAAAAA *******"+expediente.getEmpleado().getOficina());
			Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);
		}
		if (Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION)==null) {
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("expedienteCC nulo");
			ExpedienteCC expedienteCC = new ExpedienteCC ();
			Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION, expedienteCC);
		}
	}

	/**
	 * @return Nombre de la pagina
	 */
	protected String getNombrePrincipal () {
		FacesContext context = FacesContext.getCurrentInstance();
		String path = context.getViewRoot().getViewId();
		return path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('.'));
	}

	/**
	 * Refresca la pagina jsp
	 */
	protected void refrescarJSP () {
		FacesContext context = FacesContext.getCurrentInstance();
		String viewId = context.getViewRoot().getViewId();
		ViewHandler handler = context.getApplication().getViewHandler();
		UIViewRoot root = handler.createView(context, viewId);
		root.setViewId(viewId);
		context.setViewRoot(root);
	}
	
	protected void mensajeGenerico(String idComponente, String mensaje, Severity severity, Object... params) {
		if (mensaje != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			String msg = (params==null ? mensaje : MessageFormat.format(mensaje, params));
			context.addMessage(idComponente, new FacesMessage(severity,"", msg));
		}
	}

	protected void mensajeError(String idComponente, String mensaje, Object... params) {
		mensajeGenerico(idComponente, mensaje, FacesMessage.SEVERITY_ERROR, params);
	}

	protected void mensajeErrorPrincipal(String dubId, String mensaje, Object... params) {
		mensajeError (ConstantesAdmin.NOMBRE_FORMULARIO + ":" + dubId, mensaje, params);
	}
	
	protected void mensajeInfo(String idComponente, String mensaje, Object... params) {
		mensajeGenerico(idComponente, mensaje, FacesMessage.SEVERITY_INFO, params);
	}
	
	protected void mensajeInfoPrincipal(String dubId, String mensaje, Object... params) {
		mensajeInfo (ConstantesAdmin.NOMBRE_FORMULARIO + ":" + dubId, mensaje, params);
	}

	protected ExternalContext getExternalContext () {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	protected Application getApplication () {
		return FacesContext.getCurrentInstance().getApplication();
	}

	protected void addCallbackParam(String param, Object valor) {
		PartialResponseWriter writer = (CustomPartialResponseWriter) FacesContext
				.getCurrentInstance().getPartialViewContext()
				.getPartialResponseWriter();
		if (writer instanceof CustomPartialResponseWriter) {
			((CustomPartialResponseWriter) writer).addCallbackParam(param,
					valor);
		}
	}
	
	protected String redirect (String pagina, String[]... parametros) {
		StringBuilder sb = new StringBuilder(pagina).append("?faces-redirect=true");
		if (parametros != null) {
			for (String[] param : parametros) {
				sb.append('&').append(param[0]).append('=').append(param[1]);
			}
		}
		return sb.toString();
	}
	
	protected String redirectMensaje (String menjaje) {
		return redirect("/mensaje/formMensaje", 
				new String[]{ConstantesAdmin.PARAMETRO_MENSAJE, menjaje});
	}
	
	protected void redirectAction (String pagina, String[]... parametros) {
		StringBuilder sb = new StringBuilder(pagina).append(".faces");
		if (parametros != null) {
			boolean esPrimero =  true;
			for (String[] param : parametros) {
				if (esPrimero) {
					sb.append('?');
				} else {
					sb.append('&');
				}
				sb.append(param[0]).append('=').append(param[1]);
				esPrimero = false;
			}
		}
		
		try {
			getExternalContext().redirect(sb.toString());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * @param parameter Nombre del parametro
	 * @return Parametro de la solicitud
	 */
	public String getRequestParameter (String parameter) {
		return getExternalContext().getRequestParameterMap().get(parameter);
	}

	public String getServidorServWeb() {
		return servidorServWeb;
	}

	public void setServidorServWeb(String servidorServWeb) {
		this.servidorServWeb = servidorServWeb;
	}

	public String getPuertoServWeb() {
		return puertoServWeb;
	}

	public void setPuertoServWeb(String puertoServWeb) {
		this.puertoServWeb = puertoServWeb;
	}

	public String getDescargados() {
		return descargados;
	}

	public void setDescargados(String descargados) {
		this.descargados = descargados;
	}

	public String getTransferencias() {
		return transferencias;
	}

	public void setTransferencias(String transferencias) {
		this.transferencias = transferencias;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
	public String getServidorFTP() {
		return servidorFTP;
	}

	public void setServidorFTP(String servidorFTP) {
		this.servidorFTP = servidorFTP;
	}

	public String getUsuarioFTP() {
		return usuarioFTP;
	}

	public void setUsuarioFTP(String usuarioFTP) {
		this.usuarioFTP = usuarioFTP;
	}

	public String getPasswordFTP() {
		return passwordFTP;
	}

	public void setPasswordFTP(String passwordFTP) {
		this.passwordFTP = passwordFTP;
	}

	public String getPeriodoTranfFTP() {
		return periodoTranfFTP;
	}

	public void setPeriodoTranfFTP(String periodoTranfFTP) {
		this.periodoTranfFTP = periodoTranfFTP;
	}

	public String getTasaKBytes() {
		return tasaKBytes;
	}

	public void setTasaKBytes(String tasaKBytes) {
		this.tasaKBytes = tasaKBytes;
	}
	
	public void subirArchivos (boolean subir) {
		subirArchivos (subir, false);
	}
	
	public void subirArchivos (boolean subir, boolean crearDocumentos) {
		EnvioFTPMB envioFTP = (EnvioFTPMB) Util.getObjectSession("envioFTP");
		if (envioFTP == null) {
			envioFTP = new EnvioFTPMB ();
			Util.addObjectSession("envioFTP", envioFTP);
		}
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		DocumentoExpDAO docExpDAO = EJBLocator.getDocumentoExpDAO();
		List<DocumentoExp> listaDocExpediente = docExpDAO.findByExpdiente(expediente.getId());
		
		StringBuilder descarga = new StringBuilder ();
		boolean esPrimeroD = true;
		for (DocumentoExp de : listaDocExpediente) {
			if (ConstantesBusiness.FLAG_ESCANEADO.equals(de.getFlagEscaneado())
					&& ConstantesBusiness.TIPO_VISOR_AVANZADO.equals(de.getDocumento().getTipoVisor())) {
				if (!esPrimeroD){
					descarga.append(";");
				}
				descarga.append(de.getDocumento().getCodigoDocumento());
				esPrimeroD = false;
			}
		}
		envioFTP.subirArchivos(subir, descarga.toString(), crearDocumentos);
	}
	
	public void iniciarEnvioFTP () {
		EnvioFTPMB envioFTP = (EnvioFTPMB) Util.getObjectSession("envioFTP");
		if (envioFTP == null) {
			envioFTP = new EnvioFTPMB ();
			Util.addObjectSession("envioFTP", envioFTP);
		}
		envioFTP.iniciar();
	}

	public String getContextPath()
	{
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}
	/**
	 * Obtiene un objeto del ambito Session
	 * @param nombreMB Nombre del objeto
	 * @return
	 */
	public Object getObjectSession (String nombreMB) {
		Object objeto = null;
		if (nombreMB != null) {
			objeto = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nombreMB);
		}
		//+POR SOLICITUD BBVA+LOG.info("*******************SESSION CREADA************************:"+objeto.toString());
		return objeto;
	}
	
	public void validarSesion() {
	   //+POR SOLICITUD BBVA+LOG.info("************validarSesion()*********************");
	   FacesContext contex = FacesContext.getCurrentInstance();
	   try {
	        if (contex.getExternalContext().getSessionMap().get("isSesionAlive") == null | contex.getExternalContext().getSessionMap().get("isSesionAlive").toString().trim().equals("false")) {	            
	            contex.getExternalContext().redirect("/BBVA_CE_CTACTE_Administracion/inicio/login.jsp");
	            contex.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Autenticacion", "Expiro la sesión!!"));
	            }
	   } catch (Exception e) {
	        try {
	                FacesContext.getCurrentInstance().getExternalContext().redirect("/icaptu/index.xhtml");
	        } catch (IOException ex) {
	        }
	   }
    }
	        
	/**
	 * Alamacena un objeto del ambito Session
	 * @param nombreMB Nombre del objeto
	 * @param objeto Objeto que se almacenara en la sesion
	 */
	public void addObjectSession (String nombreMB, Object objeto) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nombreMB, objeto);
		//+POR SOLICITUD BBVA+LOG.info("*******************addObjectSession*****************");
		//+POR SOLICITUD BBVA+LOG.info("*******************nombreMB*****************"+nombreMB);
		//+POR SOLICITUD BBVA+LOG.info("*******************objeto*****************"+objeto.toString());
	}
	
	public boolean isModoConsulta() {
		boolean resultado = true;
		try {
			Empleado empleado = (Empleado) Util
					.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
			String codigoEmpl = empleado.getCodigo();

			ExpedienteCC expedienteCC = (ExpedienteCC) Util
					.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION);
			Expediente expediente = (Expediente) Util
					.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
			EstadoExpediente estado = expediente.getEstado();
			String codUsrAct = expedienteCC.getCodUsuarioActual();
			if ((estado != null && ConstantesBusiness.ID_ESTADO_EXPEDIENTE_TERMINADO != estado
					.getId())
					&& (codUsrAct == null || codUsrAct
							.equalsIgnoreCase(codigoEmpl))) {
				resultado = false;
			}
			if (expediente == null || expediente.getId() == null)
				resultado = false;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		String codEstadoTarea = getRequestParameter(ConstantesAdmin.PARAMETRO_CODIGO_ESTADO_TAREA);
		if (codEstadoTarea != null
				&& (codEstadoTarea
						.trim()
						.equals(Integer
								.toString(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO)) || codEstadoTarea
						.trim()
						.equals(Integer
								.toString(ConstantesBusiness.ID_ESTADO_TAREA_CANCELADO)))) {
			resultado = true;
		}
		LOG.info("********resultado***********" + resultado);
		return resultado;
	}
	
	public boolean isSupervisor(){
		boolean flag = false;
		Empleado empleadoSesion = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		if(empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_GERENTE_OFICINA) ||
				   empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_ABOGADO_BASTANTEO) ||
				   empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_JEFE_BASTANTEO) ||
				   empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_FIRMA) ||
				   empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_DIGITADOR)
				  )
			flag =  true;
		
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("flag-->"+flag);
		return flag;
	}
	
	public List<Empleado> getUsuariosVistosPorPerfil() {
		LOG.info("getUsuariosVistosPorPerfil()");
		/*
		 * Devuelve todos los usuarios de todos los perfiles hijos del perfil
		 * logueado
		 */
		List<Empleado> listaEmp = null;
		Empleado empleadoSesion = (Empleado) Util
				.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		listaEmp = buscarEmpleadosSupervisados(empleadoSesion);
	
		LOG.info("listaEmp --> "+listaEmp.size());
		for (Empleado p : listaEmp) {
			LOG.info("Empleado --> "+p.getCodigo());
		}
		
		return listaEmp;
	}
	
	public List<Empleado> buscarEmpleadosSupervisados(Empleado empleado) {
		LOG.info("buscarEmpleadosSupervisados(Empleado empleado)");
		List<Empleado> listaResult = new ArrayList<Empleado>();
		
		List<Perfil> listaPerfilesSupervisados = buscarPerfilesHijos(empleado.getPerfil().getId());
		List<Integer> listaIdsPerfilesSupervisados = new ArrayList<Integer>();
		for (Perfil perfil : listaPerfilesSupervisados) {
			listaIdsPerfilesSupervisados.add(perfil.getId());
		}
		if (listaIdsPerfilesSupervisados.size() > 0) {
			if (empleado.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_ABOGADO_BASTANTEO)) {
				// El Supervisor de Abogado de Bastanteo solo puede reasignar los expedientes de su estudio de abogados
				if (empleado.getEstudio() != null) {
					listaResult = EJBLocator.getEmpleadoDAO().getEmpleadosPorPerfilesYEstudio(listaIdsPerfilesSupervisados, empleado.getEstudio().getId());
				} else {
					LOG.warn("El empleado "+empleado.getCodigo()+" con perfil Supervisor Abogado de bastanteo no tiene estudio de abogado.");
				}
			} else {
				listaResult = EJBLocator.getEmpleadoDAO().getEmpleadosPorPerfiles(listaIdsPerfilesSupervisados);
			}
		}
		
		return listaResult;
	}
	
	private List<Perfil> buscarPerfilesHijos(Integer idPerfilPadre) {
		LOG.info("buscarPerfilesHijos({})", idPerfilPadre);
		List<Perfil> listaResult = new ArrayList<Perfil>();
		List<Perfil> listaHijos = EJBLocator.getPerfilDAO().buscarSupervizados(idPerfilPadre);
		if (listaHijos == null || listaHijos.size() == 0) {
			LOG.info("Perfil ID="+idPerfilPadre+" no tiene hijos");
			return new ArrayList<Perfil>();
		}
		LOG.info("Perfil ID="+idPerfilPadre+" tiene "+listaHijos.size()+" hijos");
		for (Perfil perfil : listaHijos) {
			listaResult.add(perfil);
			List<Perfil> listaTmp = buscarPerfilesHijos(perfil.getId());
			listaResult.addAll(listaTmp);
		}
		return listaResult;
	}
	
	public boolean faltanDocumentosContent(ExpedienteCC expedienteCC) {
		boolean resultado = false;
		if (expedienteCC == null || expedienteCC.getDatosFlujoCtaCte() == null) {
			return false; // en este caso está en modo consulta
		}
		LOG.info("expedienteCC.getDatosFlujoCtaCte().getFlagEventoActivadoCM() -> "+expedienteCC.getDatosFlujoCtaCte().getFlagEventoActivadoCM());
		if (expedienteCC.getDatosFlujoCtaCte().getFlagEventoActivadoCM() != null && expedienteCC.getDatosFlujoCtaCte().getFlagEventoActivadoCM().equals("0")) {
			String flag = EJBLocator.getDocumentoExpDAO().validarFlagEnvioContent(Integer.parseInt(expedienteCC.getCodigoExpediente()));
			if (flag.equals("1")) {
				resultado = true;
			} else {
				resultado = false;
			}
		} else {
			resultado = false;
		}
		LOG.info("faltanDocumentosContent()="+resultado);
		return resultado;
	}

	public void setServidorConvPDF(String servidorConvPDF) {
		this.servidorConvPDF = servidorConvPDF;
	}

	public String getServidorConvPDF() {
		return servidorConvPDF;
	}

	public void setPuertoConvPDF(String puertoConvPDF) {
		this.puertoConvPDF = puertoConvPDF;
	}

	public String getPuertoConvPDF() {
		return puertoConvPDF;
	}
	
	/**
	 * Agrega un mensaje de error a un componente gráfico de la pantalla.
	 * 
	 * @param idComponente
	 *            Id del componente al cual se le va a asociar el mensaje de
	 *            error.
	 * @param mensaje
	 *            Mensaje de error a mostrar para el componente.
	 */
	public void addComponentMessage(String idComponente, String mensaje) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(idComponente, new FacesMessage(mensaje));
	}
	
	/**
	 * Agrega un mensaje de error al contexto de la pantalla.
	 * 
	 * @param e
	 *            - Excepción cuyo mensaje se va a agregar.
	 */
	public void addErrorMessage(Exception e) {
		if (e != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), ""));
		}
	}
	
	/**
	 * Agrega un mensaje de error
	 * @param idComponente Identificador del componente
	 * @param idMensaje Identificador del mensaje en el archivo de propiedades
	 * @param params Parametros para el mensaje
	 */
	protected void addComponentMessageFromPropertiesFile(String idComponente, String idMensaje, Object... params) {
		if (idMensaje != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(idComponente, Mensajes.getFacesMessageError(idMensaje, params));
		}
	}
	
	/**
	 * Agrega un mensaje de error al contexto de la pantalla.
	 * 
	 * @param e
	 *            -Mensaje a agregar.
	 */
	public void addErrorMessage(String mensaje) {
		if (mensaje != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, ""));
		}
	}
	
	/**
	 * Elimina un objeto del ambito Session
	 * @param nombreMB Nombre del objeto
	 */
	protected void removeObjectSession (String nombreMB) {
		FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().remove(nombreMB);
	}
	
	public String getUrlEscaneoWeb() {
		return urlEscaneoWeb;
	}

	public void setUrlEscaneoWeb(String urlEscaneoWeb) {
		this.urlEscaneoWeb = urlEscaneoWeb;
	}
	
	public String getEmailMesaDocumentos() {
		return emailMesaDocumentos;
	}

	public void setEmailMesaDocumentos(String emailMesaDocumentos) {
		this.emailMesaDocumentos = emailMesaDocumentos;
	}

	public String getEmailMesaFirmas() {
		return emailMesaFirmas;
	}

	public void setEmailMesaFirmas(String emailMesaFirmas) {
		this.emailMesaFirmas = emailMesaFirmas;
	}

	public String getHostServidorCorreo() {
		return hostServidorCorreo;
	}

	public void setHostServidorCorreo(String hostServidorCorreo) {
		this.hostServidorCorreo = hostServidorCorreo;
	}

	public String getPuertoServidorCorreo() {
		return puertoServidorCorreo;
	}

	public void setPuertoServidorCorreo(String puertoServidorCorreo) {
		this.puertoServidorCorreo = puertoServidorCorreo;
	}

	public String getUrlManualUsuario() {
		return urlManualUsuario;
	}

	public void setUrlManualUsuario(String urlManualUsuario) {
		this.urlManualUsuario = urlManualUsuario;
	}

	public String getCarpetaFTP() {
		return carpetaFTP;
	}

	public void setCarpetaFTP(String carpetaFTP) {
		this.carpetaFTP = carpetaFTP;
	}

	public String getTamanoMaxPDF() {
		return tamanoMaxPDF;
	}

	public void setTamanoMaxPDF(String tamanoMaxPDF) {
		this.tamanoMaxPDF = tamanoMaxPDF;
	}

	public String getNumReintentos() {
		return numReintentos;
	}

	public void setNumReintentos(String numReintentos) {
		this.numReintentos = numReintentos;
	}
	
	public String getIntentosWS() {
		return intentosWS;
	}

	public void setIntentosWS(String intentosWS) {
		this.intentosWS = intentosWS;
	}
	
}
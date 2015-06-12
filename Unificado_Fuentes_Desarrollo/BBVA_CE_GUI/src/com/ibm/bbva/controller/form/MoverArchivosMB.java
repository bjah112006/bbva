package com.ibm.bbva.controller.form;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.MenuMB;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.session.ParametrosConfBeanLocal;

@SuppressWarnings("serial")
@ManagedBean(name = "moverArchivos")
@RequestScoped
public class MoverArchivosMB extends AbstractMBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(MoverArchivosMB.class);
	
	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;

	private String hostParam;
	private String userParam;
	private String passwordParam;
	private String periodoParam;
	private String tasaKBytesParam;
	private String escaneosPathParam;
	private String prefixParam;
	private String strHostWs;
	private String pathLog;
	private String carpetaFTP;
	private String pathDescargados;
	private String listaDocumentosAdjuntos;

	public MoverArchivosMB() {		

	}

	@PostConstruct
	public void init() {				
		hostParam = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_HOST").getValorVariable();
		periodoParam = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_PERIODO").getValorVariable();
		strHostWs = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_HOSTWS").getValorVariable();
		
		//LOG.info("hostParam : "+hostParam);
		//LOG.info("periodoParam : "+periodoParam);
		//LOG.info("strHostWs : "+strHostWs);
		
		//hostParam =  ConstantesServidor.CONSTANTE_FTP_HOST;      //ParametersDelegate.getInstance().getValue(Constantes.CONSTANTE_FTP_HOST);
		userParam = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_USER").getValorVariable(); // ConstantesServidor.CONSTANTE_FTP_USER;       //ParametersDelegate.getInstance().getValue(Constantes.CONSTANTE_FTP_USER);
		passwordParam = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_PASSWORD").getValorVariable();// ConstantesServidor.CONSTANTE_FTP_PASS;   //ParametersDelegate.getInstance().getValue(Constantes.CONSTANTE_FTP_PASS);
		//periodoParam = ConstantesServidor.CONSTANTE_FTP_PERIODO; //ParametersDelegate.getInstance().getValue(Constantes.CONSTANTE_FTP_PERIODO);
		//tasaKBytesParam = ParametersDelegate.getInstance().getValue(Constantes.CONSTANTE_FTP_TASA);
		Oficina oficina = (Oficina)getObjectSession(Constantes.OFICINA_SESION);
		if (oficina != null && oficina.getTasaTransf() != null) {
			tasaKBytesParam = String.valueOf(oficina.getTasaTransf());
		} else {
			tasaKBytesParam = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_TASA").getValorVariable();
			if (tasaKBytesParam == null || tasaKBytesParam.trim().equals("")) {
				tasaKBytesParam = "256";
			} else {
				try {
					Integer.parseInt(tasaKBytesParam);
				} catch(NumberFormatException e) {
					tasaKBytesParam = "256";
				}
			}
		}
		
		//escaneosPathParam = Constantes.DIRECTORIO_DOC_ESCANEADOS;
		escaneosPathParam = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_DIRECTORIO_DOC_ESCANEADOS").getValorVariable();
		LOG.info("escaneosPathParam "+escaneosPathParam);
		//this.pathLog = Constantes.DIRECTORIO_LOG_DOC_ESCANEADOS;
		this.pathLog = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_DIRECTORIO_LOG_DOC_ESCANEADOS").getValorVariable();
		LOG.info("pathLog "+pathLog);
		//strHostWs = ConstantesServidor.CONSTANTE_HOSTWS;         //ParametersDelegate.getInstance().getValue(Constantes.CONSTANTE_HOSTWS);
		LOG.info("getObjectSession(Constantes.ID_EXPEDIENTE_SESION):" + getObjectSession(Constantes.ID_EXPEDIENTE_SESION));
		prefixParam = String.valueOf(getObjectSession(Constantes.ID_EXPEDIENTE_SESION));
		LOG.info("prefixParam "+prefixParam);
		this.carpetaFTP = Constantes.CARPETA_FTP;
		//this.pathDescargados = Constantes.DIRECTORIO_DOC_ESCANEADOS_BAJADA;
		this.pathDescargados =parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_DIRECTORIO_DOC_ESCANEADOS_BAJADA").getValorVariable();
		LOG.info("pathDescargados "+pathDescargados);
		this.listaDocumentosAdjuntos = String.valueOf(getObjectSession(Constantes.LISTA_DOCUMENTOS_ADJUNTOS));
		LOG.info("listaDocumentosAdjuntos "+listaDocumentosAdjuntos);
		removeObjectSession(Constantes.ID_EXPEDIENTE_SESION);	
		removeObjectSession(Constantes.LISTA_DOCUMENTOS_ADJUNTOS);
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
	
	public String getHostParam() {
		return hostParam;
	}

	public void setHostParam(String hostParam) {
		this.hostParam = hostParam;
	}

	public String getUserParam() {
		return userParam;
	}

	public void setUserParam(String userParam) {
		this.userParam = userParam;
	}

	public String getPasswordParam() {
		return passwordParam;
	}

	public void setPasswordParam(String passwordParam) {
		this.passwordParam = passwordParam;
	}

	public String getPeriodoParam() {
		return periodoParam;
	}

	public void setPeriodoParam(String periodoParam) {
		this.periodoParam = periodoParam;
	}

	public String getTasaKBytesParam() {
		return tasaKBytesParam;
	}

	public void setTasaKBytesParam(String tasaKBytesParam) {
		this.tasaKBytesParam = tasaKBytesParam;
	}

	public String getEscaneosPathParam() {
		return escaneosPathParam;
	}

	public void setEscaneosPathParam(String escaneosPathParam) {
		this.escaneosPathParam = escaneosPathParam;
	}

	public String getPrefixParam() {
		return prefixParam;
	}

	public void setPrefixParam(String prefixParam) {
		this.prefixParam = prefixParam;
	}

	public String getStrHostWs() {
		return strHostWs;
	}

	public void setStrHostWs(String strHostWs) {
		this.strHostWs = strHostWs;
	}

	public String getPathLog() {
		return pathLog;
	}

	public void setPathLog(String pathLog) {
		this.pathLog = pathLog;
	}
	
	public String getCarpetaFTP() {
		return carpetaFTP;
	}

	public void setCarpetaFTP(String carpetaFTP) {
		this.carpetaFTP = carpetaFTP;
	}

	public String getPathDescargados() {
		return pathDescargados;
	}

	public void setPathDescargados(String pathDescargados) {
		this.pathDescargados = pathDescargados;
	}

	public String getListaDocumentosAdjuntos() {
		return listaDocumentosAdjuntos;
	}

	public void setListaDocumentosAdjuntos(String listaDocumentosAdjuntos) {
		this.listaDocumentosAdjuntos = listaDocumentosAdjuntos;
	}
	
	
}
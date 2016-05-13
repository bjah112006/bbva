package com.ibm.bbva.controller.applet;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.session.ParametrosConfBeanLocal;

@SuppressWarnings("serial")
@ManagedBean(name = "avisoMoverArchivos")
@RequestScoped
public class AvisoMoverArchivosMB extends AbstractMBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(AvisoMoverArchivosMB.class);
	
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
	private String carpetaTemporal;
	private String transferencias;
	private String nombreJarVersion;
	private String rutaDestinoDescargaJar;

	public AvisoMoverArchivosMB() {		

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
		tasaKBytesParam = String.valueOf(oficina.getTasaTransf());
		
		//escaneosPathParam = Constantes.DIRECTORIO_DOC_ESCANEADOS;
		//Obtener nombre de la carpeta que contiene todos los documentos para el exp especificado
		String carpetaTemDocsPorExp = (String) getObjectSession(Constantes.CARPETA_DOC_ESCANEADOS_POR_EXPEDIENTE);
		if(carpetaTemDocsPorExp != null){
			escaneosPathParam = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_DIRECTORIO_DOC_ESCANEADOS").getValorVariable();
			LOG.info("escaneosPathParam "+escaneosPathParam);
			transferencias = escaneosPathParam+"\\\\"+carpetaTemDocsPorExp;
			LOG.info("transferencias "+transferencias);
			this.carpetaTemporal = carpetaTemDocsPorExp;
			LOG.info("carpetaTemporal "+carpetaTemporal);
			this.nombreJarVersion = (String) getObjectSession(Constantes.NOMBRE_JAR);
			LOG.info("nombreJarVersion "+nombreJarVersion);
			this.rutaDestinoDescargaJar =(String) getObjectSession(Constantes.RUTA_DESTINO_DESCARGA_JAR);
			LOG.info("rutaDestinoDescargaJar = "+rutaDestinoDescargaJar);

		}else{
			escaneosPathParam = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_DIRECTORIO_DOC_ESCANEADOS").getValorVariable();
			LOG.info("escaneosPathParam "+escaneosPathParam);
			transferencias = escaneosPathParam;
			LOG.info("transferencias "+transferencias);
			this.carpetaTemporal = "";
			LOG.info("carpetaTemporal "+carpetaTemporal);
			this.nombreJarVersion = "";
			LOG.info("nombreJarVersion "+nombreJarVersion);
			this.rutaDestinoDescargaJar ="";
			LOG.info("rutaDestinoDescargaJar = "+rutaDestinoDescargaJar);
		}
		
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
		//removeObjectSession(Constantes.ID_EXPEDIENTE_SESION);	
		//removeObjectSession(Constantes.LISTA_DOCUMENTOS_ADJUNTOS);
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

	public String getCarpetaTemporal() {
		return carpetaTemporal;
	}

	public void setCarpetaTemporal(String carpetaTemporal) {
		this.carpetaTemporal = carpetaTemporal;
	}

	public String getTransferencias() {
		return transferencias;
	}

	public void setTransferencias(String transferencias) {
		this.transferencias = transferencias;
	}

	public String getNombreJarVersion() {
		return nombreJarVersion;
	}

	public void setNombreJarVersion(String nombreJarVersion) {
		this.nombreJarVersion = nombreJarVersion;
	}

	public String getRutaDestinoDescargaJar() {
		return rutaDestinoDescargaJar;
	}

	public void setRutaDestinoDescargaJar(String rutaDestinoDescargaJar) {
		this.rutaDestinoDescargaJar = rutaDestinoDescargaJar;
	}
	
	
}
package com.ibm.bbva.ctacte.controller.comun;


import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.cm.ConsultaContentManager;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IUrlContentManager;
import com.ibm.bbva.ctacte.controller.form.VerificarCalidadFirmasMB;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name="urlContentManager")
@ViewScoped
public class UrlContentManagerMB extends AbstractMBean {
	
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(UrlContentManagerMB.class);
	private IUrlContentManager managedBean;
	@ManagedProperty (value="#{verificarCalidadFirma}")
	private VerificarCalidadFirmasMB verificarCalidadFirma;
	private Expediente expediente;
	private Empleado empleado;
	private DocumentoExp documentoExp;
	private String rutaDOI;
	private Integer idExpediente;
	private String codDocumento;
	private String urlSFPFirmas;
	private String mensajeContent="";
	@EJB
	private DocumentoExpDAO documentoExpDAO;
	@EJB
	private ClienteBusiness clienteBusiness;
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("pagina"+pagina);
		if (ConstantesAdmin.FORM_SUBSANAR_FIRMAS.endsWith(pagina)) {
			LOG.info("entro al if");
			managedBean = verificarCalidadFirma;	
			LOG.info("salio del if");
		}
		//LOG.info("antes del managedBean.setUrlContentManager(this);	" );
		//managedBean.setUrlContentManager(this);	
		LOG.info("antes del expediente= (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);");
		expediente=(Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		empleado=(Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		LOG.info("despues del expediente= (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);");
		LOG.info("Expediente" + expediente);
		idExpediente = expediente.getId();
		LOG.info("idExpediente" + idExpediente);
		codDocumento = ConstantesBusiness.DOCUMENTO_IDENTIDAD;	
		LOG.info("codDOcumento "+ codDocumento);
		obtenerDocumentoCM(codDocumento,idExpediente);
		
		ExpedienteCC expedienteCC = (ExpedienteCC) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION);
		
		
		//ParametrosSistema parametros = ParametrosSistema.getInstance();
		//Properties properties = parametros.getProperties(ParametrosSistema.CONF);
		//String strParametrosSFP = "?" + ConstantesBusiness.SFP_PARAMETRO_USUARIO + empleado.getCodigo() + 
		String strParametrosSFP = "?" + ConstantesBusiness.SFP_PARAMETRO_USUARIO + expedienteCC.getCodUsuarioActual() +
				"&" + ConstantesBusiness.SFP_PARAMETRO_EXPEDIENTE + expediente.getId() + 
				"&" + ConstantesBusiness.SFP_PARAMETRO_ALEATORIO + 
				(new Random().nextInt(ConstantesBusiness.SFP_PARAMETRO_MAXIMO_ALEATORIO) +
						ConstantesBusiness.SFP_PARAMETRO_MINIMO_ALEATORIO);
		urlSFPFirmas = ConstantesBusiness.PAGINA_ASOCIACION_FIRMAS_IBM + strParametrosSFP;		 
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Link SFP: " + urlSFPFirmas);
		
		// mensaje faltan documentos content
		if (faltanDocumentosContent(expedienteCC)) {
			mensajeContent = ConstantesAdmin.MSG_ERROR_FALTA_DOC_CONTENT;
		}
	}
	
	
	public void obtenerDocumentoCM(String codigoDocumento, int idExpediente){
		LOG.info("public void obtenerDocumentoCM(String codigoDocumento, int idExpediente)");
		
		DocumentoExp documentoExp = documentoExpDAO.findByCodDocExp(codigoDocumento, idExpediente);
		com.ibm.bbva.cm.service.Documento documento = null;
		if (documentoExp != null
				&& documentoExp.getFlagEscaneado() != null
				&& documentoExp.getFlagEscaneado().equals(
						ConstantesBusiness.FLAG_ESCANEADO)
				&& documentoExp.getIdCm() != null
				&& documentoExp.getIdCm().intValue() > 0) {
			ConsultaContentManager consultaCM = new ConsultaContentManager ();
			documento = consultaCM.CM_ObtenerDocumentItemType(codigoDocumento,idExpediente);
		}
		
		if (documento!=null)
			rutaDOI=documento.getUrlContent();
		else
			rutaDOI = "";
		LOG.info("rutaDOI: "+rutaDOI);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("ruta DOI: " + rutaDOI);
	
	}
	
	public void actualizarParticipes(AjaxBehaviorEvent event) {
		LOG.info("public void actualizarParticipes()");
		
		try {
			List<ClientePJCore> listaCliente = clienteBusiness.buscarCodigoCentral(expediente.getCliente().getCodigoCentral(), empleado.getCodigo());
			if (listaCliente.isEmpty()) {
				mensajeErrorPrincipal("btnActualizarParticipes", "El cliente no existe.");
			} else if (listaCliente.size()==1) {
				DatosClientePJCore datos = clienteBusiness.obtenerDatosClientePJ(listaCliente.get(0).getCodigoCentral(), empleado.getCodigo());
				listaCliente.get(0).setDatosCore(datos);
				
				boolean resultado = Util.actualizarParticipes(expediente, listaCliente.get(0));
				if (resultado) {
					mensajeInfoPrincipal("btnActualizarParticipes", "Los partícipes fueron actualizados.");
				} else {
					mensajeErrorPrincipal("btnActualizarParticipes", "Ocurrió un error al actualizar los partícipes.");
				}
			} else {
				mensajeErrorPrincipal("btnActualizarParticipes", "Ocurrió un error al actualizar los partícipes.");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			mensajeErrorPrincipal("btnActualizarParticipes", "Ocurrió un error al actualizar los partícipes.");
		}
	}

	public String getRutaDOI() {
		return rutaDOI;
	}

	public void setRutaDOI(String rutaDOI) {
		this.rutaDOI = rutaDOI;
	}

	public IUrlContentManager getManagedBean() {
		return managedBean;
	}

	public void setManagedBean(IUrlContentManager managedBean) {
		this.managedBean = managedBean;
	}

	public VerificarCalidadFirmasMB getVerificarCalidadFirma() {
		return verificarCalidadFirma;
	}

	public void setVerificarCalidadFirma(
			VerificarCalidadFirmasMB verificarCalidadFirma) {
		this.verificarCalidadFirma = verificarCalidadFirma;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public DocumentoExp getDocumentoExp() {
		return documentoExp;
	}

	public void setDocumentoExp(DocumentoExp documentoExp) {
		this.documentoExp = documentoExp;
	}

	public int getIdExpediente() {
		return idExpediente;
	}

	public void setIdExpediente(int idExpediente) {
		this.idExpediente = idExpediente;
	}

	public String getCodDocumento() {
		return codDocumento;
	}

	public void setCodDocumento(String codDocumento) {
		this.codDocumento = codDocumento;
	}

	public String getUrlSFPFirmas() {
		return urlSFPFirmas;
	}

	public void setUrlSFPFirmas(String urlSFPFirmas) {
		this.urlSFPFirmas = urlSFPFirmas;
	}

	public String getMensajeContent() {
		return mensajeContent;
	}

	public void setMensajeContent(String mensajeContent) {
		this.mensajeContent = mensajeContent;
	}

}

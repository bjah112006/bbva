package com.ibm.bbva.ctacte.controller.comun;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IRevisarDigitaliceDocumentacion;
import com.ibm.bbva.ctacte.controller.form.RevisarEjecutarRevocatoriaMB;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="revisarDigitaliceDocumentacion")
@ViewScoped
public class RevisarDigitaliceDocumentacionMB extends AbstractMBean{
	
	private static final long serialVersionUID = 8512389245523752041L;
	private static final Logger LOG = LoggerFactory.getLogger(RevisarDigitaliceDocumentacionMB.class);

	@ManagedProperty(value="#{revisarEjecutarRevocatoria}")
	private RevisarEjecutarRevocatoriaMB revisarEjecutarRevocatoria;
	private IRevisarDigitaliceDocumentacion managedBean;
	private Expediente expediente;
	private Operacion operacion;
	private String selTipoPJ;
	private boolean mostRegMod;	
	private boolean mostObsGen;
	private boolean mostFirmas;
	private boolean mostEscaneado;
	private boolean mostApplet;
	private boolean soloLectTipoPJ;
	private String abrirArchivo;
	private List<DocumentoExp> listaDocumentoExp;
	@EJB
	private DocumentoExpDAO docExpDAO;
	
	@PostConstruct
	public void iniciar () {
		listaDocumentoExp = Collections.EMPTY_LIST;
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);		
		if ("formRevisarEjecutarRevocatoria".equals(pagina)){
			LOG.info("Entro al if");
			managedBean=revisarEjecutarRevocatoria;
			LOG.info("Entro al if 2");
			//operacion = expediente.getOperacion();
			
		}
		//managedBean.setRevisarDigitaliceDocumentacion(this);
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		
		// creando la lista de Documentos
		LOG.info("expediente.getId()-->"+expediente.getId());
		listaDocumentoExp=docExpDAO.findByExpdienteCartaRevocatoria(expediente.getId(),
				ConstantesBusiness.CODIGO_CARTA_REVOCATORIA);
		LOG.info("lista tam-->"+listaDocumentoExp.size());		
		String nombreDocumento=listaDocumentoExp.get(0).getDocumento().getDescripcion();
		LOG.info("nombre Documento"+nombreDocumento);
	}
	
	public void abrirDocumento (ActionEvent ae) {
		LOG.info("abrirDocumento (ActionEvent ae)");
		String docum = getRequestParameter("documento");
		abrirArchivo = docum;
	}
	


	public RevisarEjecutarRevocatoriaMB getRevisarEjecutarRevocatoria() {
		return revisarEjecutarRevocatoria;
	}


	public void setRevisarEjecutarRevocatoria(
			RevisarEjecutarRevocatoriaMB revisarEjecutarRevocatoria) {
		this.revisarEjecutarRevocatoria = revisarEjecutarRevocatoria;
	}


	public IRevisarDigitaliceDocumentacion getManagedBean() {
		return managedBean;
	}


	public void setManagedBean(IRevisarDigitaliceDocumentacion managedBean) {
		this.managedBean = managedBean;
	}


	public Expediente getExpediente() {
		return expediente;
	}


	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}


	public Operacion getOperacion() {
		return operacion;
	}


	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}


	public String getSelTipoPJ() {
		return selTipoPJ;
	}


	public void setSelTipoPJ(String selTipoPJ) {
		this.selTipoPJ = selTipoPJ;
	}


	public boolean isMostRegMod() {
		return mostRegMod;
	}


	public void setMostRegMod(boolean mostRegMod) {
		this.mostRegMod = mostRegMod;
	}


	public boolean isMostObsGen() {
		return mostObsGen;
	}


	public void setMostObsGen(boolean mostObsGen) {
		this.mostObsGen = mostObsGen;
	}


	public boolean isMostFirmas() {
		return mostFirmas;
	}


	public void setMostFirmas(boolean mostFirmas) {
		this.mostFirmas = mostFirmas;
	}


	public boolean isMostApplet() {
		return mostApplet;
	}


	public void setMostApplet(boolean mostApplet) {
		this.mostApplet = mostApplet;
	}


	public boolean isSoloLectTipoPJ() {
		return soloLectTipoPJ;
	}


	public void setSoloLectTipoPJ(boolean soloLectTipoPJ) {
		this.soloLectTipoPJ = soloLectTipoPJ;
	}

	public String getAbrirArchivo() {
		return abrirArchivo;
	}

	public void setAbrirArchivo(String abrirArchivo) {
		this.abrirArchivo = abrirArchivo;
	}
	
	public boolean isMostEscaneado() {
		return mostEscaneado;
	}


	public void setMostEscaneado(boolean mostEscaneado) {
		this.mostEscaneado = mostEscaneado;
	}


	public List<DocumentoExp> getListaDocumentoExp() {
		return listaDocumentoExp;
	}


	public void setListaDocumentoExp(List<DocumentoExp> listaDocumentoExp) {
		this.listaDocumentoExp = listaDocumentoExp;
	}
	
	
	
}

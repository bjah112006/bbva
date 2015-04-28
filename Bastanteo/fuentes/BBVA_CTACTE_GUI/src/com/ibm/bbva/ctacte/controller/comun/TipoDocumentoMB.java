package com.ibm.bbva.ctacte.controller.comun;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Documento;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.interf.ITipoDocumentos;
import com.ibm.bbva.ctacte.controller.form.VerificarCalidadFirmasMB;
import com.ibm.bbva.ctacte.dao.DocumentoDAO;
import com.ibm.bbva.ctacte.util.Util;
//import javax.annotation.PostConstruct;
//import javax.faces.event.ActionEvent;
//import org.apache.commons.lang3.StringUtils;
//import org.jfree.util.Log;
//import com.ibm.bbva.ctacte.exepciones.ParametroIlegalException;

@ManagedBean (name="tipoDocumento")
@ViewScoped
public class TipoDocumentoMB extends AbstractMBean{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(TipoDocumentoMB.class);

	@ManagedProperty ("#{verificarCalidadFirmas}")
	private VerificarCalidadFirmasMB verificarCalidadFirma;
	private ITipoDocumentos managedBean;
	private List<SelectItem> listaDocumentos;
	private String documento;
	@EJB
	private DocumentoDAO documentoDAO;

/*	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		
		String pagina=getNombrePrincipal();
		LOG.info("Pagina actual {}",pagina);
		if("formVerificarCalidadFirma".endsWith(pagina)){
			managedBean=verificarCalidadFirma;			
		}
		crearListaDocumentos();
		managedBean.setTipoDocumentos(this);	
	}*/
	
	public VerificarCalidadFirmasMB getVerificarCalidadFirma() {
		return verificarCalidadFirma;
	}

	public void setVerificarCalidadFirma(
			VerificarCalidadFirmasMB verificarCalidadFirma) {
		this.verificarCalidadFirma = verificarCalidadFirma;
	}
		
	
	private void crearListaDocumentos(){
		List<Documento> documentos=documentoDAO.findAll();
		listaDocumentos=Util.crearItems(documentos,true,"id","descripcion");
		
	}	
	
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public List<SelectItem> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(List<SelectItem> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}
	}
	


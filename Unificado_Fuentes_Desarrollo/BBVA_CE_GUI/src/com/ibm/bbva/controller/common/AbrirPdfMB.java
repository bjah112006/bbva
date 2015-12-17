package com.ibm.bbva.controller.common;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.cm.iice.service.ContentServiceImplDelegateProxy;
import com.ibm.bbva.cm.service.impl.Documento;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.session.ParametrosConfBeanLocal;

@ManagedBean(name = "abrirPDF")
@RequestScoped
public class AbrirPdfMB implements Serializable {

	private static final long serialVersionUID = -4990318622014182410L;

	private static final Logger LOG = LoggerFactory.getLogger(AbrirPdfMB.class);
	
	/*FIX ERIKA ABREGU 27/06/2015 */
	private Expediente expediente;
	//private ContentServiceImplDelegateProxy contentIICE = null;
	private String URL_PDF_CE_ANTIGUA= "";
	private ParametrosConfBeanLocal parametrosConfBean;
	DateFormat dfAnio = new SimpleDateFormat("yyyy");
	DateFormat dfMes = new SimpleDateFormat("MM");
	DateFormat dfDia = new SimpleDateFormat("dd");
	
	
	@EJB
	private FacadeLocal facade;

	private String rutaCM;
	private String nombreDoc;

	@PostConstruct
	public void iniciar() {
		String idCm = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idcm");
		String codTipoDoc = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codtipodoc");
		String redirect = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("redirect");
		String tipoDocDescrip = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tipodocdescrip");
		
		//fix erika abregu
		expediente = (Expediente) FacesContext.getCurrentInstance().getExternalContext()
        		.getSessionMap().get(Constantes.EXPEDIENTE_SESION);
		
		if (idCm != null && idCm.trim().length() > 0) {
			DocumentoExpTc documentoExp = new DocumentoExpTc();
			documentoExp.setIdCm(new BigDecimal(idCm));
			documentoExp.setTipoDocumento(new TipoDocumento());
			documentoExp.getTipoDocumento().setCodigo(codTipoDoc);
			documentoExp.getTipoDocumento().setDescripcion((tipoDocDescrip!=null && tipoDocDescrip.trim().length()>0)? tipoDocDescrip : "");
			
			if (documentoExp.getIdCm().intValue() > 0) {
				Documento doc = new Documento();
				/**FIX ERIKA ABREGU 05/07/2015
				 * ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
				 */
				/**
				 * 16122015 CAMBIO DE ruta de lectura de PDFs de la antigua contratación
				 */
				if (expediente != null && expediente.getId() > 0) {
					if(Constantes.EXPEDIENTE_ANTIGUO.equals(expediente.getOrigen())){
						LOG.info("Metodo init de AbrirPdfMB Antiguo = "+expediente.getId());						
						try{
							parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext()
							.lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
							URL_PDF_CE_ANTIGUA = parametrosConfBean.buscarPorVariable(1, "URL_PDF_CE_ANTIGUA").getValorVariable();
						}catch (NamingException e) {
							LOG.error(e.getMessage(), e);
						}
						LOG.info("FECHA REGISTRO="+this.expediente.getFecRegistro());
						LOG.info("ID EXPEDIENTE="+this.expediente.getId());
						String urlPdf =   URL_PDF_CE_ANTIGUA +
										  "/" + dfAnio.format(this.expediente.getFecRegistro()) +
										  "/" + dfMes.format(this.expediente.getFecRegistro()) +
										  "/" + dfDia.format(this.expediente.getFecRegistro()) +
										  "/" + this.expediente.getId() +
										  "/" + documentoExp.getIdCm() +
										  ".pdf";
						LOG.info("URL_PDF_CE_ANTIGUA="+urlPdf);
						doc.setUrlContent(urlPdf);
					}else{
						doc = facade.obtenerDocumentoCM(documentoExp);
					}
				}else{
					doc = facade.obtenerDocumentoCM(documentoExp);
				}
				if (doc != null) {
					this.rutaCM = doc.getUrlContent();
					this.nombreDoc = tipoDocDescrip;
				} else {
					LOG.warn("No se encontro el documento con idCm="+idCm+" en el Content.");
					this.rutaCM = null;
					this.nombreDoc = null;
				}
			} else {
				this.rutaCM = null;
				this.nombreDoc = null;
			}
			if (redirect != null && redirect.equals("1")) {
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect(rutaCM);
					//FacesContext.getCurrentInstance().getExternalContext().redirect(nombreDoc);
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		
		
	}

	public String getRutaCM() {
		return rutaCM;
	}

	public void setRutaCM(String rutaCM) {
		this.rutaCM = rutaCM;
	}

	public String getNombreDoc() {
		return nombreDoc;
	}

	public void setNombreDoc(String nombreDoc) {
		this.nombreDoc = nombreDoc;
	}

}

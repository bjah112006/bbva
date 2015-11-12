package com.ibm.bbva.controller.common;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;

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
	private ContentServiceImplDelegateProxy contentIICE = null;
	private com.ibm.bbva.cm.iice.service.Documento docIice = null;
	private String CONTENT_SERVICE_IMPL_IICE= "";
	private ParametrosConfBeanLocal parametrosConfBean;
	
	
	@EJB
	private FacadeLocal facade;

	private String rutaCM;

	@PostConstruct
	public void iniciar() {
		String idCm = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idcm");
		String codTipoDoc = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codtipodoc");
		String redirect = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("redirect");
		
		//fix erika abregu
		expediente = (Expediente) FacesContext.getCurrentInstance().getExternalContext()
        		.getSessionMap().get(Constantes.EXPEDIENTE_SESION);
		
		if (idCm != null && idCm.trim().length() > 0) {
			DocumentoExpTc documentoExp = new DocumentoExpTc();
			documentoExp.setIdCm(new BigDecimal(idCm));
			documentoExp.setTipoDocumento(new TipoDocumento());
			documentoExp.getTipoDocumento().setCodigo(codTipoDoc);
			
			if (documentoExp.getIdCm().intValue() > 0) {
				Documento doc = new Documento();
				/**FIX ERIKA ABREGU 05/07/2015
				 * ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
				 */
				if (expediente != null && expediente.getId() > 0) {
					if(Constantes.EXPEDIENTE_ANTIGUO.equals(expediente.getOrigen())){
						LOG.info("Metodo init de AbrirPdfMB Antiguo = "+expediente.getId());
						
						//consulta a IICE por ws por la url de la ruta del escaneado
						this.docIice = new com.ibm.bbva.cm.iice.service.Documento();
						this.docIice.setId(Integer.parseInt(String.valueOf(documentoExp.getIdCm()))); 	
						
						try{
							parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext()
							.lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
							
							CONTENT_SERVICE_IMPL_IICE = parametrosConfBean.buscarPorVariable(1, "CONTENT_SERVICE_IMPL_IICE").getValorVariable();
							
						}catch (NamingException e) {
							LOG.error(e.getMessage(), e);
						}
												
						this.contentIICE = new ContentServiceImplDelegateProxy();
						this.contentIICE.setEndpoint(CONTENT_SERVICE_IMPL_IICE);
						
						LOG.info("WEB SERVICE DEL CONTENT IICE ES ::: "+ this.contentIICE.getEndpoint());
						LOG.info("ID DEL DOCUMENTO ES ::: "+ this.docIice.getId());
						
						//this.contentIICE._getDescriptor().setEndpoint("http://118.180.60.70:80/PLDWEBCM/services/ContentServiceImpl");
						try {
							this.docIice = this.contentIICE.find(this.docIice);
						} catch (RemoteException e) {
							LOG.error("Error de invocacion : "+e.getCause(),e);
						}
						
						if(this.docIice != null && this.docIice.getId()!=null && this.docIice.getUrlContent()!=null){
//							doc.setCodCliente(this.docIice.getCodCliente()!=null?this.docIice.getCodCliente():null);
//							doc.setContenido(this.docIice.getContenido()!=null?this.docIice.getContenido():null);
//							doc.setFechaCreacion(this.docIice.getFechaCreacion()!=null?this.docIice.getFechaCreacion():null);
//							doc.setFechaExpiracion(value)
							System.out.println(this.docIice.getFechaExpiracion()!=null?this.docIice.getFechaExpiracion():null);
							doc.setId(this.docIice.getId()!=null?this.docIice.getId():0);
//							doc.setMandatorio(this.docIice.get);
//							doc.setNombreArchivo(this.docIice.getNombreArchivo()!=null?this.docIice.getNombreArchivo():null);
//							doc.setNumDoi(this.docIice.getNumDoi()!=null?this.docIice.getNumDoi():null);
//							doc.setOrigen(this.docIice.getOrigen()!=null?this.docIice.getOrigen():null);
							doc.setTipo(this.docIice.getTipo()!=null?this.docIice.getTipo():null);
//							doc.setTipoDoi(this.docIice.getTipoDoi()!=null?this.docIice.getTipoDoi():null);
							doc.setUrlContent(this.docIice.getUrlContent()!=null?this.docIice.getUrlContent():null);
							
						}
						
					}else{
						doc = facade.obtenerDocumentoCM(documentoExp);
					}
				}else{
					doc = facade.obtenerDocumentoCM(documentoExp);
				}
				
				
				
				if (doc != null) {
					this.rutaCM = doc.getUrlContent();
				} else {
					LOG.warn("No se encontr� el documento con idCm="+idCm+" en el Content.");
					this.rutaCM = null;
				}
			} else {
				this.rutaCM = null;
			}
			if (redirect != null && redirect.equals("1")) {
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect(rutaCM);
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

}

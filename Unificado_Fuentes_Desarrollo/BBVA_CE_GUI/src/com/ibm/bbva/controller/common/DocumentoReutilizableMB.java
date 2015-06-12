package com.ibm.bbva.controller.common;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.RegistrarExpedienteMB;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.DocumentoExpTcBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.TipoDocumentoBeanLocal;
import com.ibm.bbva.tabla.util.vo.DocumentoReutilizable;
import com.ibm.bbva.util.AyudaPanelDocumentos;

@SuppressWarnings("serial")
@ManagedBean(name = "documentoReutilizable")
@RequestScoped
public class DocumentoReutilizableMB extends AbstractMBean {

	@EJB
	ClienteNaturalBeanLocal clienteNaturalBean;
	@EJB
	ExpedienteBeanLocal expedienteBean;
	@EJB
	DocumentoExpTcBeanLocal documentoExpTcBean;
	@EJB
	TipoDocumentoBeanLocal tipoDocumentoBean;
	@EJB
	private FacadeLocal facade;	
	
	private Expediente expediente;
	private List<DocumentoReutilizable> listaDocumentoReutilizable;
	private HtmlCommandButton btnReutilizarDocumentoContent;
	private HtmlInputText htmlIdDocReu;
	private HtmlInputText htmlIdSeleccionados;
	private String strDocReu;
	private String strSeleccionados;
	private HtmlInputText htmlIdExpDocReu;
	private String strIdExpDocReu;
	private String mensaje;
	private boolean mostrarMensaje;
	private boolean seleccion;

	private static final Logger LOG = LoggerFactory.getLogger(DocumentoReutilizableMB.class);
	
	public DocumentoReutilizableMB() {	
		
	} 

	@PostConstruct
    public void init() {
    	//FacesContext ctx = FacesContext.getCurrentInstance();  
    	//DocumentosEscaneadosMB documentosEscaneados = null;
    	
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		this.strIdExpDocReu = String.valueOf(expediente.getId()); 
		
	    listaDocumentoReutilizable = new ArrayList<DocumentoReutilizable>();
	    /* TODO
	    if (expediente.getId() != 0){
	     	
	    	documentosEscaneados = (DocumentosEscaneadosMB)  
	 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "documentosEscaneados");

	        documentosEscaneados.cargaGuiaDocApplet();
	    
	        AyudaDocumentoReutilizable ayudaDocumentoReutilizable = new AyudaDocumentoReutilizable();		
	        documentosEscaneados.setDocumentosReutilizables(ayudaDocumentoReutilizable.cargaDocumentoReutilizable(documentosEscaneados.getGuiaDoc(), this));
	    }
	    */
	    obtenerDatos();
	    this.mostrarMensaje = false;
	    this.mensaje = "";

	}
	
	public List<DocumentoReutilizable> obtenerDatosOriginal() {
		List<DocumentoReutilizable> listaDocReuSession = (List<DocumentoReutilizable>)getObjectSession("listaDocReuSession");
		Integer tamListGuiaDocumentaria = (Integer) getObjectSession("tamListaGuiaDoc");
		if(listaDocReuSession == null && tamListGuiaDocumentaria != null && tamListGuiaDocumentaria.intValue() > 0){
			LOG.info("obtenerDatos()");
			List<DocumentoReutilizable> listaDocReu = new ArrayList<DocumentoReutilizable>();
			List<DocumentoExpTc> listaDoc = new ArrayList<DocumentoExpTc>();
			List<ClienteNatural> lstClienteNat = new ArrayList<ClienteNatural>();
			
			String numeroDOI = (String) getObjectSession("numeroDOI");		
			String tipoDOI = (String) getObjectSession("tipoDOI");
			if(numeroDOI == null){
				numeroDOI = (String) getObjectSession("numeroDOIReu");
			}
			if(tipoDOI == null){
				tipoDOI = (String) getObjectSession("tipoDOIReu");
			}
			
			LOG.info("numeroDOI:" + numeroDOI);
			LOG.info("tipoDOI:" + tipoDOI);
			/*Obtener datos del cliente natural*/
			ClienteNatural clienteNatural = new ClienteNatural();
			clienteNatural.setTipoDoi(new TipoDoi());
			
			if (numeroDOI!=null) {
				clienteNatural.setNumDoi(numeroDOI);
				clienteNatural.getTipoDoi().setId(Integer.parseInt(tipoDOI));
				lstClienteNat = clienteNaturalBean.buscarPorTipoNumDoi(clienteNatural);
			} else if (expediente.getClienteNatural() != null && expediente.getClienteNatural().getId()!=0) {
				LOG.info("expediente.getClienteNatural().getId():" + expediente.getClienteNatural().getId());
				clienteNatural.setId(expediente.getClienteNatural().getId());
				clienteNatural = clienteNaturalBean.buscarPorId(clienteNatural.getId());
				LOG.info("numeroDOI:" + clienteNatural.getNumDoi());
				LOG.info("tipoDOI:" + clienteNatural.getTipoDoi().getId());
				lstClienteNat.add(clienteNatural);
				
			} else {
				this.listaDocumentoReutilizable = listaDocReu;
			}
			
			//List<ClienteNatural> lstClienteNat = clienteNaturalBean.buscarPorTipoNumDoi(clienteNatural);
			LOG.info("lstClienteNat.size(): " + lstClienteNat.size());
			if (!lstClienteNat.isEmpty()){
				List<ClienteNatural> listClienteNatural = clienteNaturalBean.buscarPorTipoNumDoi(lstClienteNat.get(0));
				LOG.info("listClienteNatural.size(): " + listClienteNatural.size());
				
				//Expediente filtroExpediente;
				List<Expediente> listaExpediente;
				List<DocumentoExpTc> listaDocumentoExpediente;
				
				DocumentoReutilizable documentoReutilizable;
				TipoDocumento tipoDocumento;
				Date fechaActual = new Date();
				listaDocReu = new ArrayList();
				
				/*Obtener Documentos Adjuntos*/
				List<DocumentoExpTc> listDocExpAdjVO = documentoExpTcBean.buscarPorExpediente(expediente.getId());
				LOG.info("listDocExpAdjVO.size: " + listDocExpAdjVO.size());
								
				for (ClienteNatural listaCn :listClienteNatural) {			
					/*Obtener lista de datos del expediente segun id ClienteNatural*/
			        listaExpediente = new ArrayList();		        
			        listaExpediente = expedienteBean.buscarPorIdCliente(listaCn.getId());
			        LOG.info("listaExpediente.size: " + listaExpediente.size());
			        
			        for (Expediente listaEx : listaExpediente) {
			        	LOG.info("Expediente.getId():" + listaEx.getId());
			           	/*Obtener lista de datos de documentoExpediente segun id Expediente*/
			        	listaDocumentoExpediente = new ArrayList();		        	
			        	listaDocumentoExpediente = documentoExpTcBean.buscarPorExpediente(listaEx.getId());
			        	LOG.info("listaDocumentoExpediente.size: " + listaDocumentoExpediente.size());
			        	
			        	listaDoc.addAll(listaDocumentoExpediente);		        	
			        }
				}
				
				LOG.info("listaDoc.size: " + listaDoc.size());
				
				if (!listaDoc.isEmpty()) {
		        	Collections.sort(listaDoc, new Comparator<DocumentoExpTc>() {     
		    			public int compare(DocumentoExpTc m1, DocumentoExpTc m2) {
	//	    				Date d1 = m1.getFecVen() ==  null? new Date() : m1.getFecVen();
	//	    				Date d2 = m2.getFecVen() ==  null? new Date() : m2.getFecVen();
		    				Date d1 = m1.getFecReg() ==  null? new Date() : m1.getFecReg();
		    				Date d2 = m2.getFecReg() ==  null? new Date() : m2.getFecReg();
		    				//return m2.getFecVen().compareTo(m1.getFecVen());
		    				return d2.compareTo(d1);
		    			}
		    		});
		        		        	
		        	/*Obtener datos de documentoExpediente segun id Documento*/	     		        	
		        	//Long idExpediente = listaDoc.get(0).getExpediente().getId();
		        	for (DocumentoExpTc ldocumento : listaDoc) {
		        		/*Obtener datos del tipo de documento*/
		        		documentoReutilizable = new DocumentoReutilizable();
		        		tipoDocumento = new TipoDocumento();	        		
		        		tipoDocumento = tipoDocumentoBean.buscarPorId(ldocumento.getTipoDocumento().getId());
		        		LOG.info("tipoDocumento.getCodigo: " + tipoDocumento.getCodigo());
		        			        		
		        		Long idExpediente = ldocumento.getExpediente().getId();
		        		documentoReutilizable.setId(tipoDocumento.getId());
		        		documentoReutilizable.setDescripcion(tipoDocumento.getDescripcion());
		        		documentoReutilizable.setCodigoDocumento(tipoDocumento.getCodigo());		        		
		        		documentoReutilizable.setIdExpediente(idExpediente.toString());
		        		documentoReutilizable.setIdDocumentoExp(String.valueOf(ldocumento.getId()));
		        		
		        		for (DocumentoExpTc docExpAdj :listDocExpAdjVO) {		
		        		   if (docExpAdj.getTipoDocumento().getId() == tipoDocumento.getId()) {	
		        			   if (docExpAdj.getFlagDocReutilizable() != null){
		        		          documentoReutilizable.setSeleccion(docExpAdj.getFlagDocReutilizable().equals(Constantes.FLAG_DOCUMENTO_REUTILIZABLE) ? true:false);
		        			   }
		        		   }
		        		}
		        		
		        		if (tipoDocumento.getFlagReutilizable().equals(Constantes.FLAG_DOCUMENTO_REUTILIZABLE)) {	        			
		        			if (!buscarItemlista(listaDocReu, tipoDocumento.getId())) {
		        				LOG.info("ldocumento.getFecVen(): " + ldocumento.getFecVen());
		        				LOG.info("ldocumento.getFecReg(): " + ldocumento.getFecReg());
		        				/*if (ldocumento.getFecVen() == null  && ldocumento.getFecReg() != null) {
		        					if (((fechaActual.getTime() - ldocumento.getFecReg().getTime()) / Constantes.MILISEGUNDOS_POR_DIA) <= tipoDocumento.getValidez().longValue()) {
		        						listaDocReu.add(documentoReutilizable);
		        					}
		        				}else if (ldocumento.getFecVen() != null){
		        					if (ldocumento.getFecVen().after(fechaActual)){
		        						listaDocReu.add(documentoReutilizable);
		        					}	        					
		        				}*/
		        				if (ldocumento.getFecReg() != null) {
		        					if (((fechaActual.getTime() - ldocumento.getFecReg().getTime()) / Constantes.MILISEGUNDOS_POR_DIA) <= tipoDocumento.getValidez().longValue()) {
		        						if(ldocumento.getFecVen() != null && ldocumento.getTipoDocumento().getCodigo().equalsIgnoreCase("DOID0")){
		        							if (ldocumento.getFecVen().after(fechaActual)){
		    	        						listaDocReu.add(documentoReutilizable);
		    	        					}
		        						}
		        						else{
		        							listaDocReu.add(documentoReutilizable);
		        						}
		        						
		        					}
		        				}
		        			}
		        		}
		        		if (expediente.getId()== 0) {
		        			documentoReutilizable.setSeleccion(false);
		        		}	        		
		        	}
				}
	    	}
			this.listaDocumentoReutilizable = listaDocReu;
			addObjectSession("listaDocReuSession", listaDocReu);
			return listaDocReu;
		}
		else{
			this.listaDocumentoReutilizable = listaDocReuSession;
			return listaDocReuSession;
		}
	}
	
	public List<DocumentoReutilizable> obtenerDatos() {
		List<DocumentoReutilizable> listaDocReuSession = (List<DocumentoReutilizable>)getObjectSession("listaDocReuSession");
		Integer tamListGuiaDocumentaria = (Integer) getObjectSession("tamListaGuiaDoc");
		if(listaDocReuSession == null && tamListGuiaDocumentaria != null && tamListGuiaDocumentaria.intValue() > 0){
			LOG.info("obtenerDatos()");
			List<DocumentoReutilizable> listaDocReu = new ArrayList<DocumentoReutilizable>();
			List<DocumentoExpTc> listaDoc = new ArrayList<DocumentoExpTc>();
			List<ClienteNatural> lstClienteNat = new ArrayList<ClienteNatural>();
			
			String numeroDOI = (String) getObjectSession("numeroDOI");		
			String tipoDOI = (String) getObjectSession("tipoDOI");
			if(numeroDOI == null){
				numeroDOI = (String) getObjectSession("numeroDOIReu");
			}
			if(tipoDOI == null){
				tipoDOI = (String) getObjectSession("tipoDOIReu");
			}
			
			LOG.info("numeroDOI:" + numeroDOI);
			LOG.info("tipoDOI:" + tipoDOI);
			/*Obtener datos del cliente natural*/
			ClienteNatural clienteNatural = new ClienteNatural();
			clienteNatural.setTipoDoi(new TipoDoi());
			
			if (numeroDOI!=null) {
				clienteNatural.setNumDoi(numeroDOI);
				clienteNatural.getTipoDoi().setId(Integer.parseInt(tipoDOI));
				lstClienteNat = clienteNaturalBean.buscarPorTipoNumDoi(clienteNatural);
			} else if (expediente.getClienteNatural() != null && expediente.getClienteNatural().getId()!=0) {
				LOG.info("expediente.getClienteNatural().getId():" + expediente.getClienteNatural().getId());
				clienteNatural.setId(expediente.getClienteNatural().getId());
				clienteNatural = clienteNaturalBean.buscarPorId(clienteNatural.getId());
				LOG.info("numeroDOI:" + clienteNatural.getNumDoi());
				LOG.info("tipoDOI:" + clienteNatural.getTipoDoi().getId());
				lstClienteNat.add(clienteNatural);
				
			} else {
				this.listaDocumentoReutilizable = listaDocReu;
			}
			
			LOG.info("lstClienteNat.size(): " + lstClienteNat.size());
			
			if (lstClienteNat != null && !lstClienteNat.isEmpty()){
				
				listaDocReu = new ArrayList();
								
				listaDoc = documentoExpTcBean.buscarDocumentosReutilizables(lstClienteNat.get(0).getTipoDoi().getId(), lstClienteNat.get(0).getNumDoi());

				if (listaDoc != null && !listaDoc.isEmpty()) {
		        	Collections.sort(listaDoc, new Comparator<DocumentoExpTc>() {     
		    			public int compare(DocumentoExpTc m1, DocumentoExpTc m2) {
		    				Date d1 = m1.getFecReg() ==  null? new Date() : m1.getFecReg();
		    				Date d2 = m2.getFecReg() ==  null? new Date() : m2.getFecReg();
		    				return d2.compareTo(d1);
		    			}
		    		});
		        		        	
		        	for (DocumentoExpTc ldocumento : listaDoc) {
		        		/*Obtener datos del tipo de documento*/
		        		DocumentoReutilizable documentoReutilizable = new DocumentoReutilizable();
		        			        		
		        		Long idExpediente = ldocumento.getExpediente().getId();
		        		documentoReutilizable.setId(ldocumento.getTipoDocumento().getId());
		        		documentoReutilizable.setDescripcion(ldocumento.getTipoDocumento().getDescripcion());
		        		documentoReutilizable.setCodigoDocumento(ldocumento.getTipoDocumento().getCodigo());		        		
		        		documentoReutilizable.setIdExpediente(idExpediente.toString());
		        		documentoReutilizable.setIdDocumentoExp(String.valueOf(ldocumento.getId()));
		        		documentoReutilizable.setSeleccion(false);

	        			if (!buscarItemlista(listaDocReu, ldocumento.getTipoDocumento().getId())) {
	        				listaDocReu.add(documentoReutilizable);
	        			}        		
		        	}
				}
	    	}
			this.listaDocumentoReutilizable = listaDocReu;
			addObjectSession("listaDocReuSession", listaDocReu);
			return listaDocReu;
		}
		else{
			this.listaDocumentoReutilizable = listaDocReuSession;
			return listaDocReuSession;
		}
	}
	
	public boolean buscarItemlista(List<DocumentoReutilizable> lista, Long id) {
		for (DocumentoReutilizable listaDr : lista) {
			if (listaDr.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public List<DocumentoReutilizable> desHabilitarDocumentoReutilizable (String strCodigoTipoDocEliminar){
		List<DocumentoReutilizable> listaDocumento = new ArrayList<DocumentoReutilizable>();
		for(DocumentoReutilizable doc: listaDocumentoReutilizable){
			DocumentoExpTc documentoExpTc = new DocumentoExpTc();
			LOG.info("*CodigoDocumento(): " + doc.getCodigoDocumento()+" *Descripcion(): "+doc.getDescripcion()+" *IdDocumentoExp : "+doc.getIdDocumentoExp()+" *IdExpediente: "+doc.getIdExpediente()+" *Id(): "+doc.getId()+" Tipo check: "+doc.isSeleccion()+"ID_EXP: "+expediente.getId());
			documentoExpTc=documentoExpTcBean.consultarDocumentoExpediente(Long.parseLong(doc.getIdExpediente()), doc.getCodigoDocumento());
			if(documentoExpTc!=null && String.valueOf(documentoExpTc.getIdCm()).equals(strCodigoTipoDocEliminar)){
				doc.setSeleccion(false);
				LOG.info(" ::::NUEVO ESTADO::: "+doc.isSeleccion());	
				listaDocumento.add(doc);
			}
		}
		this.listaDocumentoReutilizable = listaDocumento;
		eliminarGrillayCargarEnApplet(strCodigoTipoDocEliminar);
		return listaDocumentoReutilizable;
	}
	
	public void reutilizarDocumento(AjaxBehaviorEvent event) {
		String  idDocExp = "";
		String  codTipDoc = "";
		String  idExp = "";
		String  accion = "";
		mensaje = "";
		LOG.info("reutilizarDocumento(AjaxBehaviorEvent event)");
		if (htmlIdDocReu != null) {
			strDocReu = String.valueOf(htmlIdDocReu.getValue());
			LOG.info("strDocReu:" + strDocReu);
		}
		if (strDocReu != null) {
			String[] arrDocReu = strDocReu.split("-");
			seleccion = Boolean.parseBoolean(arrDocReu[0]);
			FacesContext ctx = FacesContext.getCurrentInstance();
			
			expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
			
			RegistrarExpedienteMB registrarExpedienteMB = (RegistrarExpedienteMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente");
			
			PanelDocumentosMB panelDocumentosMB = (PanelDocumentosMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
			
			ProductoNuevoMB productoNuevoMB = (ProductoNuevoMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");
			
			ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "observacionRechazo");
			
			List<GuiaDocumentaria> listGuiaDocumentaria = (List<GuiaDocumentaria>) FacesContext.getCurrentInstance().
					getExternalContext().getSessionMap().get(Constantes.LISTA_DOC_EXP_ADJ);
			
			idDocExp = arrDocReu[1];
			codTipDoc = arrDocReu[2];
			idExp = arrDocReu[3];
			accion = arrDocReu[4];
			LOG.info("Reu-idDocExp:" + idDocExp);
			LOG.info("Reu-codTipDoc:" + codTipDoc);
			LOG.info("Reu-idExp:" + idExp);
			LOG.info("Reu-accion:" + accion);
			LOG.info("Expediente.getId:" + expediente.getId());
			
			for(DocumentoReutilizable documentoReutilizable : listaDocumentoReutilizable){
				if(documentoReutilizable.getCodigoDocumento().equals(codTipDoc)){
					documentoReutilizable.setSeleccion(seleccion);
				}
			}
			
			// Para reutilizar 
			if (arrDocReu[0].equals("true")) {
				if (accion.equals(Constantes.FLAG_REUGUIADOC_EXP_EXISTE)) {
					if (listGuiaDocumentaria != null) {
						LOG.info("Reutilizando documento a expediente que ya existe.");
						//Se obtiene el que está observado(si existiera, para actualizarlo), si no se trae el que estar por 
						// adjuntar. En caso que no cumpla los dos anteriores crea uno nuevo.
						
						List<DocumentoExpTc> lstDocExpTcs = new ArrayList<DocumentoExpTc>();
						lstDocExpTcs = documentoExpTcBean.consultarDocumentosExpediente(expediente.getId(), codTipDoc);
						DocumentoExpTc docExpGuia = null;
						long idDocExpFin = obtenerDocumentoObservado(codTipDoc, lstDocExpTcs);
						if (idDocExpFin != 0) {
							DocumentoExpTc docExpTC = documentoExpTcBean.buscarPorId(idDocExpFin);
							docExpTC.setFlagObs(Constantes.FLAG_OBS_DOCEXPTC_NOACTIVO);
							documentoExpTcBean.edit(docExpTC);
						}else{
							long id = obtenerDocumentoAjduntar(codTipDoc, lstDocExpTcs);
							if (id != 0) {								
								docExpGuia = documentoExpTcBean.buscarPorId(id);
							}
							idDocExpFin = id;							
						}
						
						if (idDocExpFin != 0) {
							// Se actualiza 
							if (docExpGuia == null) {
								docExpGuia = documentoExpTcBean.buscarPorId(Long.valueOf(idDocExpFin));
							}
							LOG.info("FIN:" + docExpGuia.getId());
							
							DocumentoExpTc documentoExpTc = documentoExpTcBean.buscarPorId(Long.valueOf(idDocExp));
							LOG.info("INICIO:" + documentoExpTc.getId());
							
							DocumentoExpTc actualizacion = new DocumentoExpTc();
							actualizacion = copiarDocumentoExpTC(docExpGuia, documentoExpTc, expediente);
							documentoExpTcBean.edit(actualizacion);
							
//							cargarDocumentosCM(actualizacion);
						}else{							
							// Se crea uno nuevo.
							DocumentoExpTc documentoExpTc = documentoExpTcBean.buscarPorId(Long.valueOf(idDocExp));
							LOG.info("INICIO:" + documentoExpTc.getId());
							
							DocumentoExpTc creacion = new DocumentoExpTc();
							creacion = nuevoDocumentoExpTC(documentoExpTc, expediente);
							documentoExpTcBean.create(creacion);
							
//							cargarDocumentosCM(creacion);
						}
						mensaje = "";
						mostrarMensaje = false;
					}else{
						LOG.info("listGuiaDocumentaria: " + listGuiaDocumentaria);
						mensaje = "El expediente no tiene guía documentaria.";
						mostrarMensaje = true;
					}					
				}
				if (accion.equals(Constantes.FLAG_REUGUIADOC_EXP_CREAR)) {
					// Registramos el expediente.					
					if (listGuiaDocumentaria != null) {
						LOG.info("Reutilizando documento a expediente que  hay que guardar.");
						addObjectSession("grabarReutilizable","OK");
						String respuesta = registrarExpedienteMB.grabar();
						LOG.info("Respuesta:" + respuesta);
						addObjectSession(Constantes.NUEVO_CLIENTE, "1"); // es un pre-registro
						
						//Como lo crea recien, se reutiliza al unico tipo documento que existe.
						DocumentoExpTc docExpGuia = documentoExpTcBean.consultarDocumentoExpediente(expediente.getId(), codTipDoc);
						LOG.info("docExpGuia.getId:" + docExpGuia.getId());
						DocumentoExpTc documentoExpTc = documentoExpTcBean.buscarPorId(Long.valueOf(idDocExp));
						LOG.info("documentoExpTc.getId:" + documentoExpTc.getId());
						
						DocumentoExpTc actualizacion = new DocumentoExpTc();
						actualizacion = copiarDocumentoExpTC(docExpGuia, documentoExpTc, expediente);
						documentoExpTcBean.edit(actualizacion);
						
						expediente = (Expediente) getObjectSession("expReu");
						if(expediente != null){
							this.strIdExpDocReu = String.valueOf(expediente.getId());
							
							expediente = expedienteBean.buscarPorId(expediente.getId());
							addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);
						}
						
//						cargarDocumentosCM(actualizacion);
						
						mensaje = "";
						mostrarMensaje = false;
					}else{
						LOG.info("listGuiaDocumentaria: " + listGuiaDocumentaria);
						mensaje = "El expediente no tiene guía documentaria.";
						mostrarMensaje = true;
					}
				}				
			}
			else{
				if (listGuiaDocumentaria != null) {
					DocumentoExpTc docExpGuia = documentoExpTcBean.consultarDocumentoExpedienteCM(expediente.getId(), codTipDoc);
					LOG.info("docExpGuia.getId::::::::" + docExpGuia.getId());
					
					docExpGuia.setFecReg(null);
					docExpGuia.setFlagCm(null);
					docExpGuia.setFlagObs(null);
					docExpGuia.setIdCm(null);
					docExpGuia.setNombreArchivo(null);
					docExpGuia.setPidCm(null);
					docExpGuia.setFlagEscaneado("0");
					docExpGuia.setFlagDocReutilizable("0");
					
					documentoExpTcBean.edit(docExpGuia);
					LOG.info("actualizado:::::::");
					
					eliminarGrillayCargarEnApplet(docExpGuia.getTipoDocumento().getCodigo());
					
//					cargarDocumentosCM(docExpGuia);
					
					mensaje = "";
					mostrarMensaje = false;
				
				}else{
					LOG.info("listGuiaDocumentaria: " + listGuiaDocumentaria);
					mensaje = "El expediente no tiene guía documentaria.";
					mostrarMensaje = true;
				}
			}
			
			if("".equals(mensaje)){
				LOG.info("panelDocumentosMB.cargarDocumentosPanel");
				panelDocumentosMB.cargarDocumentosPanel(event);
				
				LOG.info("productoNuevoMB");
				productoNuevoMB.cambiarLineaCreditoSol(null);
				productoNuevoMB.cambiarTasaEspecial(null);
				productoNuevoMB.cambiarTipoMoneda(null);
				productoNuevoMB.validaPautaClasificacionSBS(null);
				
				LOG.info("observacionRechazoMB.init");
				observacionRechazoMB.init();
			}
			
			LOG.info("FIN Reutilizacion");
		}
	}
	
	public void eliminarGrillayCargarEnApplet(String codigoTipoDocumento){
		FacesContext ctx = FacesContext.getCurrentInstance();
		DocumentosEscaneadosMB documentosEscaneadosMB = (DocumentosEscaneadosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "documentosEscaneados");
		
		List<AyudaPanelDocumentos> listAyuda = (List<AyudaPanelDocumentos>) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
		
		for(AyudaPanelDocumentos objAux : listAyuda){
			if (objAux.getObjGuiaDocumentaria() != null) {
				if(objAux.getObjGuiaDocumentaria().getTipoDocumento().getCodigo().equals(codigoTipoDocumento)){
					objAux.setDocEscaneado(false);
				}
			}
		}
		addObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS, listAyuda);
		
		String documentosContent = documentosEscaneadosMB.getTramaDocumentosContent();
		String documentosContentTmp = "";
		String[] codigos = documentosContent.split(",");
		for(String codigo : codigos){
			if(!codigo.equals(codigoTipoDocumento)){
				documentosContentTmp = documentosContentTmp + codigo + ",";
			}
		}
		if (documentosContentTmp.length() > 0){
			documentosContentTmp = documentosContentTmp.substring(0, documentosContentTmp.length() - 1);
		}
		documentosEscaneadosMB.setTramaDocumentosContent(documentosContentTmp);
	}
	
	private long obtenerDocumentoObservado(String codTipDoc, List<DocumentoExpTc> lstDocExpTc) {
		LOG.info("obtenerIdCmObservado::::lstDocExpTc: " + lstDocExpTc.size());
		long idCmObs = 0L;
		for (DocumentoExpTc documentoExpTc : lstDocExpTc) {
			String flagObs = documentoExpTc.getFlagObs();
			documentoExpTc.setFlagObs(flagObs == null? Constantes.FLAG_OBS_DOCEXPTC_NOACTIVO: flagObs);
			if (codTipDoc.equals(documentoExpTc.getTipoDocumento().getCodigo())
					&& documentoExpTc.getFlagObs().equals(Constantes.FLAG_OBS_DOCEXPTC_ACTIVO)) {
				idCmObs = documentoExpTc.getIdCm().longValue();
				break;
			}
		}
		return idCmObs;
	}
	
	private long obtenerDocumentoAjduntar(String codTipDoc,	List<DocumentoExpTc> lstDocExpTc) {
		LOG.info("obtenerIdAdjuntar::::lstDocExpTc: " + lstDocExpTc.size());
		long id = 0L;
		for (DocumentoExpTc documentoExpTc : lstDocExpTc) {
			String flagObs = documentoExpTc.getFlagObs();
			documentoExpTc.setFlagObs(flagObs == null? Constantes.FLAG_OBS_DOCEXPTC_NOACTIVO: flagObs);
			if (codTipDoc.equals(documentoExpTc.getTipoDocumento().getCodigo())
					&& documentoExpTc.getFecReg() == null) {
				id = documentoExpTc.getId();
				break;
			}
		}		
		return id;
	}

//	private void cargarDocumentosCM(DocumentoExpTc objDocumentoExpTc) {
//		/**
//		 * Obtiene URL Documento CM
//		 * */
//		LOG.info("Consulta CM");		
//		List<Documento> listaDocumentosCM = new ArrayList<Documento>();
//		Map<String, Object> mapListDocumentosCM = new TreeMap<String, Object> ();
//		mapListDocumentosCM = (Map<String, Object>) getObjectSession(Constantes.EXPEDIENTE_LISTA_DOCUMENTO_CM);
//		
//		objDocumentoExpTc.getExpediente().setClienteNatural(new ClienteNatural());
//		
//		String numeroDOI = expediente.getClienteNatural().getNumDoi();
//		if(numeroDOI == null){
//			numeroDOI = (String) getObjectSession("numeroDOI");
//		}
//		if(numeroDOI == null){
//			numeroDOI = (String) getObjectSession("numeroDOIReu");
//		}
//		LOG.info("numeroDOI: " + numeroDOI);
//		
//		//objDocumentoExpTc.getExpediente().getClienteNatural().setNumDoi(expediente.getClienteNatural().getNumDoi());
//		objDocumentoExpTc.getExpediente().getClienteNatural().setNumDoi(numeroDOI);
//		//LOG.info("Num Doi 1 ="+expediente.getClienteNatural().getNumDoi());
//		LOG.info("Num Doi = " + objDocumentoExpTc.getExpediente().getClienteNatural().getNumDoi());
//		
//		long tiempoInicio = System.currentTimeMillis();
//		try{
//			listaDocumentosCM = facade.obtenerListaDocumentoCM(objDocumentoExpTc);
//		}catch(Exception e){
//			e.getStackTrace();
//		}
//		long tiempoFin = System.currentTimeMillis();
//		LOG.info("DocumentoReutilizableMB.cargarDocumentosCM obtenerListaDocumentoCM demora: " + (tiempoFin - tiempoInicio) + " milisegundos");
//				
//		if(listaDocumentosCM!=null){
//			for(Documento objDocumento: listaDocumentosCM){
//				if(mapListDocumentosCM != null){									
//					mapListDocumentosCM.put(String.valueOf(objDocumento.getId()), objDocumento);
//					LOG.info("for objDocumento.getId() = "+objDocumento.getId());
//					LOG.info("for objDocumento URL CONTENT = "+objDocumento.getUrlContent());
//				}else{
//					mapListDocumentosCM = new TreeMap<String, Object> ();
//					mapListDocumentosCM.put(String.valueOf(objDocumento.getId()), objDocumento);
//					LOG.info("for objDocumento.getId() = "+objDocumento.getId());
//					LOG.info("for objDocumento URL CONTENT = "+objDocumento.getUrlContent());
//				}
//			}
//		}
//		else{
//			LOG.info("listaDocumentosCM es nulo");
//		}
//		addObjectSession(Constantes.EXPEDIENTE_LISTA_DOCUMENTO_CM, mapListDocumentosCM);
//	}

	private DocumentoExpTc copiarDocumentoExpTC(DocumentoExpTc fin,
			DocumentoExpTc inicio, Expediente expediente) {

		DocumentoExpTc retorno = new DocumentoExpTc();
		
		retorno.setCodCliente(inicio.getCodCliente());
		retorno.setExpediente(expediente);
		retorno.setFecReg(inicio.getFecReg());
		retorno.setFecVen(inicio.getFecVen());
		retorno.setFlagCm(inicio.getFlagCm());
		retorno.setFlagDocReutilizable("1");
		retorno.setFlagObs(inicio.getFlagObs());
		retorno.setIdCm(inicio.getIdCm());
		retorno.setId(fin.getId());
		retorno.setNombre(inicio.getNombre());
		retorno.setNombreArchivo(inicio.getNombreArchivo());
		retorno.setNroDoi(inicio.getNroDoi());
		retorno.setObligatorio(inicio.getObligatorio());
		retorno.setPersona(inicio.getPersona());
		retorno.setPidCm(inicio.getPidCm());
		retorno.setTarea(inicio.getTarea());
		retorno.setTipoDocumento(inicio.getTipoDocumento());
		retorno.setTipoDoi(inicio.getTipoDoi());
		retorno.setFlagEscaneado("1");
		
		return retorno;
	}
	
	private DocumentoExpTc nuevoDocumentoExpTC(DocumentoExpTc inicio, Expediente expediente) {
		
		DocumentoExpTc retorno = new DocumentoExpTc();
		
		retorno.setCodCliente(inicio.getCodCliente());
		retorno.setExpediente(expediente);
		retorno.setFecReg(inicio.getFecReg());
		retorno.setFecVen(inicio.getFecVen());
		retorno.setFlagCm(inicio.getFlagCm());
		retorno.setFlagDocReutilizable("1");
		retorno.setFlagObs(inicio.getFlagObs());
		retorno.setIdCm(inicio.getIdCm());
		retorno.setNombre(inicio.getNombre());
		retorno.setNombreArchivo(inicio.getNombreArchivo());
		retorno.setNroDoi(inicio.getNroDoi());
		retorno.setObligatorio(inicio.getObligatorio());
		retorno.setPersona(inicio.getPersona());
		retorno.setPidCm(inicio.getPidCm());
		retorno.setTarea(inicio.getTarea());
		retorno.setTipoDocumento(inicio.getTipoDocumento());
		retorno.setTipoDoi(inicio.getTipoDoi());
		retorno.setFlagEscaneado("1");
		
		return retorno;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpedienteVO(Expediente expediente) {
		this.expediente = expediente;
	}
	
	public void copiarDatosExpediente () {
	
	}

	public List<DocumentoReutilizable> getListaDocumentoReutilizable() {		
		return listaDocumentoReutilizable;
	}

	public void setListaDocumentoReutilizable(
			List<DocumentoReutilizable> listaDocumentoReutilizable) {
		this.listaDocumentoReutilizable = listaDocumentoReutilizable;
	}
	
	public void limpiarListaReu() {
		listaDocumentoReutilizable = new ArrayList<DocumentoReutilizable>();
	}
	
	public HtmlInputText getHtmlIdDocReu() {
		return htmlIdDocReu;
	}

	public void setHtmlIdDocReu(HtmlInputText htmlIdDocReu) {
		this.htmlIdDocReu = htmlIdDocReu;
	}
	
	public HtmlInputText getHtmlIdSeleccionados() {
		return htmlIdSeleccionados;
	}

	public void setHtmlIdSeleccionados(HtmlInputText htmlIdSeleccionados) {
		this.htmlIdSeleccionados = htmlIdSeleccionados;
	}

	public String getStrDocReu() {
		return strDocReu;
	}

	public void setStrDocReu(String strDocReu) {
		this.strDocReu = strDocReu;
	}
	
	public String getStrSeleccionados() {
		return strSeleccionados;
	}

	public void setStrSeleccionados(String strSeleccionados) {
		this.strSeleccionados = strSeleccionados;
		String reutilizablesSeleccionados = getObjectSession("reutilizablesSeleccionados") == null ? "" : (String)getObjectSession("reutilizablesSeleccionados");
		if(strSeleccionados.indexOf("-") >= 0){
			String codigoEliminar = strSeleccionados.substring(1, strSeleccionados.length());
			if(!"".equals(reutilizablesSeleccionados)){
				reutilizablesSeleccionados = reutilizablesSeleccionados.replace(codigoEliminar + ",", "");
			}
		}
		else{
			if(strSeleccionados != null && !"".equals(strSeleccionados)){
				reutilizablesSeleccionados = reutilizablesSeleccionados + strSeleccionados + ",";
			}
		}
		addObjectSession("reutilizablesSeleccionados", reutilizablesSeleccionados);
		this.strSeleccionados = "";
	}

	public HtmlCommandButton getBtnReutilizarDocumentoContent() {
		return btnReutilizarDocumentoContent;
	}

	public void setBtnReutilizarDocumentoContent(
			HtmlCommandButton btnReutilizarDocumentoContent) {
		this.btnReutilizarDocumentoContent = btnReutilizarDocumentoContent;
	}

	public HtmlInputText getHtmlIdExpDocReu() {
		return htmlIdExpDocReu;
	}

	public void setHtmlIdExpDocReu(HtmlInputText htmlIdExpDocReu) {
		this.htmlIdExpDocReu = htmlIdExpDocReu;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public boolean isMostrarMensaje() {
		return mostrarMensaje;
	}

	public void setMostrarMensaje(boolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	public String getStrIdExpDocReu() {
		return strIdExpDocReu;
	}

	public void setStrIdExpDocReu(String strIdExpDocReu) {
		this.strIdExpDocReu = strIdExpDocReu;
	}

	public boolean isSeleccion() {
		return seleccion;
	}

	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	} 

	
}
package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.RegistrarExpedienteMB;
import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.Persona;
import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.session.DocumentoExpTcBeanLocal;
import com.ibm.bbva.session.GuiaDocumentariaBeanLocal;
import com.ibm.bbva.session.TipoDocumentoBeanLocal;

@SuppressWarnings("serial")
@ManagedBean(name = "guiaDocumentaria")
@RequestScoped
public class GuiaDocumentariaMB extends AbstractMBean{

	@EJB
    TipoDocumentoBeanLocal tipoDocumentoBean;
	@EJB
	DocumentoExpTcBeanLocal documentoExpTcBean;
	@EJB
	GuiaDocumentariaBeanLocal guiaDocumentariaBean;
	
	private static final long serialVersionUID = 1L;
	private Expediente expediente;
	private ClienteNatural clienteNatural;
	private List<GuiaDocumentaria> listaGuiaDoc;
	private List<CategoriaRenta> listaClienteCategoriaRenta;
	private String[] docEscaneados;
	private String validaGuiaEscaneada;
	
	//Para el applet
	private String strTiposDocs;//TIPO,DESCRIPCION
	private String strGuiaDoc;// TIPOPERSONA(T,C,A),TIPODOC(DOID,RSPV,etc),FLAGOBLIGATORIO (1,0),FLAGESCANEO(1,0)

	private static final Logger LOG = LoggerFactory.getLogger(GuiaDocumentariaMB.class);
	
	public GuiaDocumentariaMB() {
		
	}
	
	@PostConstruct
    public void init() {
		// Obtiene Datos de Expediente
		expediente = (Expediente)getObjectSession(Constantes.EXPEDIENTE_SESION);		
	}
	
	public void obtenerDatos() {
		/*Obtener filtro para relizar busqueda guia documentaria*/
		listaGuiaDoc = new ArrayList<GuiaDocumentaria>(); 
		//obtenerDatosGuiaDoc(listaGuiaDoc);
	}

	public void obtenerDatosGuiaDoc(List<GuiaDocumentaria> listaGuiaDoc) {		
		ClienteNatural clienteNatural = new ClienteNatural();
		
		FacesContext ctx = FacesContext.getCurrentInstance();  
		RegistrarExpedienteMB regExp = null;
		DatosClienteNuevoMB datosClienteNuevo = null;
		DatosClienteMB datosCliente = null;
		DetalleExpediente1MB detalleExpediente1 = null;		
		DetalleExpedienteMB detalleExpediente = null;		
		VerificarAprobarMB verificarAprobar = null;
		VerificarAprobar2MB verificarAprobar2 = null;
		ProductoNuevoMB productoNuevo = null;		
		DatosProducto3MB datosProducto3 = null;	
		
		List<String> idsCatRenta = new ArrayList();

		String nombJSP = getNombreJSPPrincipal();
		
		if (nombJSP.equals("formRegistrarExpediente")) {
			regExp = (RegistrarExpedienteMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente");
			if (regExp.isEditarCliente()) {
				// Obtiene los Datos de Cliente Natural 
				datosClienteNuevo = (DatosClienteNuevoMB)  
						 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
				datosClienteNuevo.copiarDatosClienteNatural(clienteNatural);
				// Obtiene los Datos de Cliente Categoria de Renta 
				idsCatRenta = datosClienteNuevo.getCategoriasRenta();
			} else {
				// Obtiene los Datos de Cliente Natural 
				datosCliente = (DatosClienteMB)  
						 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosCliente");
				datosCliente.copiarDatosClienteNatural(clienteNatural);
				// Obtiene los Datos de Cliente Categoria de Renta 
				idsCatRenta = datosCliente.getCategoriasRenta();
			}
		} else if (nombJSP.equals("formConsultarClienteModificaciones")
				|| nombJSP.equals("formCoordinarClienteSubsanar")
				|| nombJSP.equals("formEvaluarDevolucionRiesgos")
				|| nombJSP.equals("formGestionarSubsanarOperacion")
				|| nombJSP.equals("formRegistrarExpedienteCu23")
				|| nombJSP.equals("formRegistrarExpedienteCu25")
				|| nombJSP.equals("formRegularizarEscanearDocumentacion")) {

			    // Obtiene los Datos de Cliente Natural 
			
			    detalleExpediente1 = (DetalleExpediente1MB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "detalleExpediente1");			
			    detalleExpediente1.copiarDatosClienteNatural(clienteNatural);
			    idsCatRenta = detalleExpediente1.getCategoriasRenta();
		} else {
			    // Obtiene los Datos de Cliente Natural 
			    detalleExpediente = (DetalleExpedienteMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "detalleExpediente");
		        detalleExpediente.copiarDatosClienteNatural(clienteNatural);
		        idsCatRenta = detalleExpediente.getSelectedItemsCat();
		}
		
		// Obtiene los Datos de las Verificaciones Dom. y Lab.
		/*verificarAprobar = (VerificarAprobarMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");
        if (verificarAprobar!=null) {
            verificarAprobar.copiarDatosExpediente();
        } else {               
        	
        	verificarAprobar2 = (VerificarAprobar2MB)  
   				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar2");
               if (verificarAprobar2!=null) {
                  verificarAprobar2.copiarDatosExpediente();
               }
        }*/
		if (nombJSP.equals("formSolicitarVerificacionDomiciliaria")
				|| nombJSP.equals("formVerificarResultadoDomiciliaria")){
        	verificarAprobar2 = (VerificarAprobar2MB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar2");
        	if (verificarAprobar2!=null) {
        		verificarAprobar2.copiarDatosExpediente();
        	}
        }
        else{
        	verificarAprobar = (VerificarAprobarMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");
        	if (verificarAprobar!=null) {
        		verificarAprobar.copiarDatosExpediente();
        	}
        }
        
        // Obtiene los Datos del producto
        /*productoNuevo = (ProductoNuevoMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");        
        if (productoNuevo != null) {
        	productoNuevo.copiarDatosExpediente();
        } else {
        	datosProducto3 = (DatosProducto3MB)  
   				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");        
               if (datosProducto3 != null) {
        	      datosProducto3.copiarDatosExpediente();
               }
        }*/
		if (nombJSP.equals("formConsultarClienteModificaciones")
        		|| nombJSP.equals("formCoordinarClienteSubsanar")
        		|| nombJSP.equals("formEvaluarDevolucionRiesgos")
        		|| nombJSP.equals("formRegistrarExpediente")
				|| nombJSP.equals("formRegularizarEscanearDocumentacion")){
        	productoNuevo = (ProductoNuevoMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");        
        	if (productoNuevo != null) {
        		productoNuevo.copiarDatosExpediente();
        	}
        }
        else{
        	datosProducto3 = (DatosProducto3MB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");        
            if (datosProducto3 != null) {
            	datosProducto3.copiarDatosExpediente();
            }
        }
      
        this.clienteNatural = clienteNatural;
        if (expediente.getClienteNatural()!=null) {
		    this.clienteNatural.setId(expediente.getClienteNatural().getId());
        }

		// filtro Guia Documentaria 
		GuiaDocumentaria guiaDocumentaria = new GuiaDocumentaria();
		guiaDocumentaria.setPersona(new Persona());
		guiaDocumentaria.setProducto(expediente.getProducto());
		//guiaDocumentaria.setSubproducto(expediente.getExpedienteTC().getSubproducto());
		guiaDocumentaria.setTipoOferta(expediente.getExpedienteTC().getTipoOferta());
		guiaDocumentaria.setFlagCliente(Constantes.CODIGO_CLIENTE_NUEVO.equals(clienteNatural.getCodCliente()) ? Constantes.CHECK_NO_SELECCIONADO : Constantes.CHECK_SELECCIONADO);
		guiaDocumentaria.setFlagPagoHab(Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getPagoHab()) ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		guiaDocumentaria.setFlagPep(Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getPerExpPub()) ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		guiaDocumentaria.setFlagTasaEsp(Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getFlagSolTasaEsp()) ? "1" : null);
		guiaDocumentaria.setFlagVerifDom(Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getVerifDom()) ? "1" : null);
		guiaDocumentaria.setFlagVerifLab(Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getVerifLab()) ? "1" : null);		
		guiaDocumentaria.getPersona().setId(Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getAval()) ? 3 : 0);
		guiaDocumentaria.setTarea(expediente.getExpedienteTC().getTarea());
		guiaDocumentaria.setFlagDps(Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getVerifDps()) ? "1" : null);
		
		boolean idConyuge = false;
		if (clienteNatural.getEstadoCivil()!=null && clienteNatural.getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CASADO) {
		    idConyuge = true;
		}
		listaGuiaDoc.addAll(guiaDocumentariaBean.obtenerGuiaDoc(guiaDocumentaria, idConyuge, idsCatRenta));
	}
	
	public void llenarGuiaDocApplet(List<GuiaDocumentaria> listaGuiaDoc){
		TipoDocumento tipoDocumento = null;

		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		for(GuiaDocumentaria vo : listaGuiaDoc){			
			tipoDocumento = new TipoDocumento();			
			tipoDocumento = tipoDocumentoBean.buscarPorId(vo.getTipoDocumento().getId());
			
			sb2.append(tipoDocumento.getCodigo());
			sb2.append(",");
			sb2.append(tipoDocumento.getDescripcion().replaceAll(",", " "));
			sb2.append(",");
			if(vo.getPersona()==null || vo.getPersona().getId()==0 || String.valueOf(vo.getPersona().getId()).equals(Constantes.CODIGO_TIPO_PERSONA_TITULAR)){
				sb.append("T,");
				sb.append(tipoDocumento.getCodigo());
				sb.append(",");
				sb.append(vo.getObligatorio());
				sb.append(",");
				sb.append(obtenerTipoDoc(vo));	
				sb.append(",");
			}else{
				if(vo.getPersona()!=null && vo.getPersona().getId()!=0 && String.valueOf(vo.getPersona().getId()).equals(Constantes.CODIGO_TIPO_PERSONA_CONYUGUE)){
					sb.append("C,");
					sb.append(tipoDocumento.getCodigo());
					sb.append(",");
					sb.append(vo.getObligatorio());
					sb.append(",");
					sb.append(obtenerTipoDoc(vo));
					sb.append(",");
				}else{
					if(vo.getPersona()!=null && vo.getPersona().getId()!=0 && String.valueOf(vo.getPersona().getId()).equals(Constantes.CODIGO_TIPO_PERSONA_AVAL)){
						sb.append("A,");
						sb.append(tipoDocumento.getCodigo());
						sb.append(",");
						sb.append(vo.getObligatorio());
						sb.append(",");
						sb.append(obtenerTipoDoc(vo));
						sb.append(",");
					}
				}
			}
		}
		if (listaGuiaDoc.size() > 0) {
			strGuiaDoc = sb.substring(0, sb.length() - 1).toString();
			strTiposDocs = sb2.substring(0, sb2.length() - 1).toString();
		}
		
		addObjectSession(Constantes.LISTA_DOC_EXP_ADJ, listaGuiaDoc);
		addObjectSession("tamListaGuiaDoc", new Integer(listaGuiaDoc == null ? 0 : listaGuiaDoc.size()));
	}
	
	public String obtenerTipoDoc(GuiaDocumentaria vo) {
		String resultado= "0";
		if (expediente.getId() > 0) {
			/*Obtener los datos de los documentos escaneados por expediente*/
	
			List<DocumentoExpTc> listaDocExp = new ArrayList<DocumentoExpTc>();		
			listaDocExp = documentoExpTcBean.buscarPorExpediente(expediente.getId());

			for (DocumentoExpTc lista : listaDocExp) {	
				/*Selecciona los documentos que ya han sido escaneados*/				
				if (vo.getTipoDocumento().getId() == lista.getTipoDocumento().getId()) {
					resultado= "1";
					break;			
				}
			}
		}
		return resultado;
	}
	
	

	public void cargaInicial() {
		obtenerDatos();
	}
	
	public void cargaApplet() {
		/*Obtener filtro para relizar busqueda guia documentaria*/
		listaGuiaDoc = new ArrayList<GuiaDocumentaria>(); 
		//obtenerDatosGuiaDoc(listaGuiaDoc);
		obtenerDatosGuiaDocOrd(listaGuiaDoc); // nuevo método, se estaría ejecutando 2 veces, FIXME
		llenarGuiaDocApplet(listaGuiaDoc);
	}
	
	public void tramaAppletAdj() {
		TipoDocumento tipoDocumento = null;
		
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		
		List<DocumentosContent> listaDC = listaDocsContent ();
		for(DocumentosContent dc : listaDC){
			
			tipoDocumento = new TipoDocumento();			
			tipoDocumento = tipoDocumentoBean.buscarPorId(dc.getDocumentoExpTc().getTipoDocumento().getId());
			
			sb2.append(tipoDocumento.getCodigo());
			sb2.append(",");
			sb2.append(tipoDocumento.getDescripcion().replaceAll(",", " "));
			sb2.append(",");
			long idPersona = dc.getDocumentoExpTc().getPersona().getId();
			if(idPersona==0 || String.valueOf(idPersona).equals(Constantes.CODIGO_TIPO_PERSONA_TITULAR)){
				sb.append("T,");
			}else{
				if(idPersona!=0 && String.valueOf(idPersona).equals(Constantes.CODIGO_TIPO_PERSONA_CONYUGUE)){
					sb.append("C,");
				}else{
					if(idPersona!=0 && String.valueOf(idPersona).equals(Constantes.CODIGO_TIPO_PERSONA_AVAL)){
						sb.append("A,");
					}
				}
			}
			sb.append(tipoDocumento.getCodigo());
			sb.append(",");
			sb.append(dc.getDocumentoExpTc().getObligatorio());
			sb.append(",");
			if (dc.getDocumentoExpTc() == null) {
				sb.append("0");
			} else {
				DocumentoExpTc documentoExpTc = dc.getDocumentoExpTc();
				sb.append("1".equals(documentoExpTc.getFlagCm()) ? "1" : "0");
			}
			sb.append(",");
		}
		strGuiaDoc = sb.substring(0, sb.length() - 1).toString();
		strTiposDocs = sb2.substring(0, sb2.length() - 1).toString();
	}
	
	private List<DocumentosContent> listaDocsContent () {
		Long idExpediente = expediente.getId();
		
		List<DocumentoExpTc> documentoExpTc = documentoExpTcBean.buscarPorExpediente(idExpediente);
				
		List<DocumentosContent> lista = new ArrayList<DocumentosContent> ();
				
		for(DocumentoExpTc dea : documentoExpTc){
			lista.add(new DocumentosContent (dea));
		}		
		return lista;
	}
	
	public List<GuiaDocumentaria> cargaPanelDocumentos() {
		/*Obtener filtro para relizar busqueda guia documentaria*/
		listaGuiaDoc = new ArrayList<GuiaDocumentaria>(); 
		obtenerDatosGuiaDocOrd(listaGuiaDoc);
		addObjectSession(Constantes.LISTA_DOC_EXP_ADJ, listaGuiaDoc);
		addObjectSession("tamListaGuiaDoc", new Integer(listaGuiaDoc == null ? 0 : listaGuiaDoc.size()));
		return listaGuiaDoc;
	}		

	public void obtenerDatosGuiaDocOrd(List<GuiaDocumentaria> listaGuiaDoc) {		
		ClienteNatural clienteNatural = new ClienteNatural();
		
		FacesContext ctx = FacesContext.getCurrentInstance();  
		RegistrarExpedienteMB regExp = null;
		DatosClienteNuevoMB datosClienteNuevo = null;
		DatosClienteMB datosCliente = null;
		DetalleExpediente1MB detalleExpediente1 = null;		
		DetalleExpedienteMB detalleExpediente = null;		
		VerificarAprobarMB verificarAprobar = null;
		VerificarAprobar2MB verificarAprobar2 = null;
		ProductoNuevoMB productoNuevo = null;		
		DatosProducto3MB datosProducto3 = null;	
		
		List<String> idsCatRenta = new ArrayList();

		String nombJSP = getNombreJSPPrincipal();

		if (nombJSP.equals("formRegistrarExpediente")) {
			regExp = (RegistrarExpedienteMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente");
			if (regExp.isEditarCliente()) {
				LOG.info("Cliente nuevo");
				// Obtiene los Datos de Cliente Natural 
				datosClienteNuevo = (DatosClienteNuevoMB)  
						 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
				datosClienteNuevo.copiarDatosClienteNatural(clienteNatural);
				// Obtiene los Datos de Cliente Categoria de Renta 
				idsCatRenta = datosClienteNuevo.getCategoriasRenta();
			} else {
				LOG.info("Cliente no nuevo");
				// Obtiene los Datos de Cliente Natural 
				datosCliente = (DatosClienteMB)  
						 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosCliente");
				datosCliente.copiarDatosClienteNatural(clienteNatural);
				// Obtiene los Datos de Cliente Categoria de Renta 
				idsCatRenta = datosCliente.getCategoriasRenta();
			}
		} else if (nombJSP.equals("formConsultarClienteModificaciones")
				|| nombJSP.equals("formCoordinarClienteSubsanar")
				|| nombJSP.equals("formEvaluarDevolucionRiesgos")
				|| nombJSP.equals("formGestionarSubsanarOperacion")
				|| nombJSP.equals("formRegistrarExpedienteCu23")
				|| nombJSP.equals("formRegistrarExpedienteCu25")
				|| nombJSP.equals("formRegularizarEscanearDocumentacion")) {

			    // Obtiene los Datos de Cliente Natural 
			
			    detalleExpediente1 = (DetalleExpediente1MB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "detalleExpediente1");			
			    detalleExpediente1.copiarDatosClienteNatural(clienteNatural);
			    idsCatRenta = detalleExpediente1.getCategoriasRenta();
		} else {
			    // Obtiene los Datos de Cliente Natural 
			    detalleExpediente = (DetalleExpedienteMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "detalleExpediente");
		        detalleExpediente.copiarDatosClienteNatural(clienteNatural);
		        idsCatRenta = detalleExpediente.getSelectedItemsCat();
		}
		
		// Obtiene los Datos de las Verificaciones Dom. y Lab.
		/*verificarAprobar = (VerificarAprobarMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");
        if (verificarAprobar!=null) {
            verificarAprobar.copiarDatosExpediente();
        } else {               
        	
        	verificarAprobar2 = (VerificarAprobar2MB)  
   				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar2");
               if (verificarAprobar2!=null) {
                  verificarAprobar2.copiarDatosExpediente();
               }
        }*/
        if (nombJSP.equals("formSolicitarVerificacionDomiciliaria")
				|| nombJSP.equals("formVerificarResultadoDomiciliaria")){
        	verificarAprobar2 = (VerificarAprobar2MB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar2");
        	if (verificarAprobar2!=null) {
        		verificarAprobar2.copiarDatosExpediente();
        	}
        }
        else{
        	verificarAprobar = (VerificarAprobarMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");
        	if (verificarAprobar!=null) {
        		verificarAprobar.copiarDatosExpediente();
        	}
        }
        
        // Obtiene los Datos del producto
        /*productoNuevo = (ProductoNuevoMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");        
        if (productoNuevo != null) {
        	productoNuevo.copiarDatosExpediente();
        } else {
        	datosProducto3 = (DatosProducto3MB)  
   				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");        
               if (datosProducto3 != null) {
        	      datosProducto3.copiarDatosExpediente();
               }
        }*/
        if (nombJSP.equals("formConsultarClienteModificaciones")
        		|| nombJSP.equals("formCoordinarClienteSubsanar")
        		|| nombJSP.equals("formEvaluarDevolucionRiesgos")
        		|| nombJSP.equals("formRegistrarExpediente")
				|| nombJSP.equals("formRegularizarEscanearDocumentacion")){
        	productoNuevo = (ProductoNuevoMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");        
        	if (productoNuevo != null) {
        		productoNuevo.copiarDatosExpediente();
        	}
        }
        else{
        	datosProducto3 = (DatosProducto3MB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosProducto3");        
            if (datosProducto3 != null) {
            	datosProducto3.copiarDatosExpediente();
            }
        }
      
        this.clienteNatural = clienteNatural;
        if (expediente.getClienteNatural()!=null) {
		    this.clienteNatural.setId(expediente.getClienteNatural().getId());
        }

		// filtro Guia Documentaria 
		GuiaDocumentaria guiaDocumentaria = new GuiaDocumentaria();
		guiaDocumentaria.setPersona(new Persona());
		guiaDocumentaria.setProducto(expediente.getProducto());
		//guiaDocumentaria.setSubproducto(expediente.getExpedienteTC().getSubproducto());
		guiaDocumentaria.setTipoOferta(expediente.getExpedienteTC().getTipoOferta());
		guiaDocumentaria.setFlagCliente(Constantes.CODIGO_CLIENTE_NUEVO.equals(clienteNatural.getCodCliente()) ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		guiaDocumentaria.setFlagPagoHab(Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getPagoHab()) ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		guiaDocumentaria.setFlagPep(Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getPerExpPub()) ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		guiaDocumentaria.setFlagTasaEsp(Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getFlagSolTasaEsp()) ? "1" : null);
		guiaDocumentaria.setFlagVerifDom(Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getVerifDom()) ? "1" : null);
		guiaDocumentaria.setFlagVerifLab(Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getVerifLab()) ? "1" : null);		
		guiaDocumentaria.getPersona().setId(Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getAval()) ? 3 : 0);
		guiaDocumentaria.setTarea(expediente.getExpedienteTC().getTarea());
		guiaDocumentaria.setFlagDps(Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getVerifDps()) ? "1" : null);
		
		LOG.info("expediente.getExpedienteTC().getTarea() : "+expediente.getExpedienteTC().getTarea());
		
		boolean idConyuge = false;
		if (clienteNatural.getEstadoCivil()!=null && 
				(clienteNatural.getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CASADO ||  
				clienteNatural.getEstadoCivil().getId()== Constantes.ESTADO_CIVIL_CONVIVIENTE)) {
		    idConyuge = true;
		}
		//if(guiaDocumentaria!=null && guiaDocumentaria.getProducto()!=null && guiaDocumentaria.getSubproducto()!=null && guiaDocumentaria.getTipoOferta()!=null)
		//if(guiaDocumentaria!=null && guiaDocumentaria.getProducto()!=null && guiaDocumentaria.getTipoOferta()!=null)
		if(guiaDocumentaria!=null && guiaDocumentaria.getProducto()!=null && guiaDocumentaria.getTipoOferta()!=null)
			if(expediente != null && expediente.getExpedienteTC() != null && 
					expediente.getExpedienteTC().getSubproducto()!=null && expediente.getExpedienteTC().getSubproducto().getId()>0){
				
				guiaDocumentaria.setSubproducto(expediente.getExpedienteTC().getSubproducto());
				listaGuiaDoc.addAll(guiaDocumentariaBean.obtenerGuiaDocXSubProdOrden(guiaDocumentaria, idConyuge, idsCatRenta));
				
			}else{
				listaGuiaDoc.addAll(guiaDocumentariaBean.obtenerGuiaDocOrden(guiaDocumentaria, idConyuge, idsCatRenta));
			}
			
	}
	
	public String obtenerTipoDocCM(GuiaDocumentaria vo) {
		String resultado= "0";
		if (expediente.getId() > 0) {	
			List<DocumentoExpTc> listaDocExp = new ArrayList<DocumentoExpTc>();		
			listaDocExp = documentoExpTcBean.buscarPorExpediente(expediente.getId());

			for (DocumentoExpTc lista : listaDocExp) {
				if(vo!=null && vo.getTipoDocumento()!=null && lista!=null && lista.getTipoDocumento()!=null && lista.getFlagCm()!=null)
				if (vo.getTipoDocumento().getId() == lista.getTipoDocumento().getId() && lista.getFlagCm().trim().equals("1")) {
					resultado= "1";
					break;			
				}
			}
		}
		return resultado;
	}	
	
	public DocumentoExpTc obtenerUrlTipoDocCM(GuiaDocumentaria vo) {
		DocumentoExpTc objTemp=new DocumentoExpTc();
		
		if (expediente.getId() > 0) {
			List<DocumentoExpTc> listaDocExp = new ArrayList<DocumentoExpTc>();		
			listaDocExp = documentoExpTcBean.buscarPorExpediente(expediente.getId());
			for (DocumentoExpTc lista : listaDocExp) {	
				if (vo.getTipoDocumento().getId() == lista.getTipoDocumento().getId() && lista.getFlagCm().trim().equals("1")) {
					objTemp=lista;
					break;			
				}
			}
		}
		return objTemp;
	}	
	
	public List<CategoriaRenta> getListaClienteCategoriaRenta() {
		return listaClienteCategoriaRenta;
	}

	public void setListaClienteCategoriaRenta(
			List<CategoriaRenta> listaClienteCategoriaRenta) {
		this.listaClienteCategoriaRenta = listaClienteCategoriaRenta;
	}

	public ClienteNatural getClienteNatural() {
		return clienteNatural;
	}

	public void setClienteNatural(ClienteNatural clienteNatural) {
		this.clienteNatural = clienteNatural;
	}

	public String[] getDocEscaneados() {
		return docEscaneados;
	}

	public void setDocEscaneados(String[] docEscaneados) {
		this.docEscaneados = docEscaneados;
	}

	public String getStrTiposDocs() {
		return strTiposDocs;
	}

	public void setStrTiposDocs(String strTiposDocs) {
		this.strTiposDocs = strTiposDocs;
	}

	public String getStrGuiaDoc() {
		return strGuiaDoc;
	}

	public void setStrGuiaDoc(String strGuiaDoc) {
		this.strGuiaDoc = strGuiaDoc;
	}

	public String getValidaGuiaEscaneada() {
		return validaGuiaEscaneada;
	}

	public void setValidaGuiaEscaneada(String validaGuiaEscaneada) {
		this.validaGuiaEscaneada = validaGuiaEscaneada;
	}

	public List<GuiaDocumentaria> getListaGuiaDoc() {
		return listaGuiaDoc;
	}

	public void setListaGuiaDoc(List<GuiaDocumentaria> listaGuiaDoc) {
		this.listaGuiaDoc = listaGuiaDoc;
	}
	
	private class DocumentosContent {				
		private DocumentoExpTc documentoExpTc;
		
		public DocumentosContent(DocumentoExpTc documentoExpTc) {
			this.documentoExpTc = documentoExpTc;
			
		}
		
		public DocumentoExpTc getDocumentoExpTc() {
			return documentoExpTc;
		}		
	}
}
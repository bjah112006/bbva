package com.ibm.bbva.controller.common;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.ProductoEtiqueta;
import com.ibm.bbva.entities.Territorio;
import com.ibm.bbva.entities.TipoVerificacion;
import com.ibm.bbva.session.ProductoEtiquetaBeanLocal;
import com.ibm.bbva.session.TerritorioBeanLocal;
import com.ibm.bbva.session.TipoVerificacionBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "verificarAprobar")
@RequestScoped
public class VerificarAprobarMB extends AbstractMBean {

	@EJB
	private TipoVerificacionBeanLocal tipoVerificacionbean;
	@EJB
	private TerritorioBeanLocal territoriobean;
	@EJB
	private ProductoEtiquetaBeanLocal productoEtiquetabean;	
	
	private TipoVerificacion tipoVerificacionVO;
	private Expediente expedienteVO;
	private List<String> selectedItems;
	private List<SelectItem> listaTipoVerificacion;
	private boolean readOnlyVl;
	private boolean readOnlyVd;
	private boolean readOnlyZp;
	private boolean readOnlyDPS;
	
	private boolean verLaboral;
	private boolean verDomiciliaria;
	private boolean verZonaPeligrosa;
	private boolean verDPS;
	
	private boolean renderedDps;
	
	private static final Logger LOG = LoggerFactory.getLogger(VerificarAprobarMB.class);
	
	public VerificarAprobarMB() {

	}	

	@PostConstruct
    public void init() {
		
		expedienteVO = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		obtenerDatos();
		iniciarDatos (expedienteVO);
		readOnlyVl = false;
		readOnlyVd = false;
		readOnlyZp = false;
		readOnlyDPS=false;
		String nombJSP = getNombreJSPPrincipal();
		if (nombJSP.equals("formRegistrarExpediente") ||
				nombJSP.equals("formCoordinarClienteSubsanar") ||
				nombJSP.equals("formEvaluarDevolucionRiesgos") ||
				nombJSP.equals("formConsultarClienteModificaciones") ||
				nombJSP.equals("formRegularizarEscanearDocumentacion") ||
				nombJSP.equals("formGestionarSubsanarOperacion") ||
				nombJSP.equals("formRegistrarExpedienteCu23") ||
				nombJSP.equals("formRevisarRegistrarDictamen")) {
			LOG.info("CONDICION A");
			Empleado empleado = (Empleado)getObjectSession(Constantes.USUARIO_SESION);
			LOG.info("Busqueda de Territorio "+empleado.getOficina().getId());
			
			Territorio territorio = empleado.getOficina().getTerritorio();
			if(territorio!=null)
				iniciarRegExp(Constantes.CHECK_NO_SELECCIONADO.equals(territorio.getFlagProv()));
			else
				LOG.info("Busqueda de Territorio es nulo ");
			
		} else if (nombJSP.equals("formVerificarConformidadExpediente") ||
			       nombJSP.equals("formRegistrarExpedienteCu25")) {
			LOG.info("CONDICION B");
			iniciarConfExp (expedienteVO);
		} else if (nombJSP.equals("formRegistrarDatos") || 
				   nombJSP.equals("formAprobarExpediente") ||
				   nombJSP.equals("formRegistrarAprobResolucion") || 
				   nombJSP.equals("formVerificarConformidadCierreExp") ||
				   nombJSP.equals("formVerificarEstadoContratacion") ||
				   nombJSP.equals("formVerificarExpDesestimados") ||
				   nombJSP.equals("formCambiarSituacionExp") ||
			       nombJSP.equals("formCambiarSituacionTramite") ||
			       nombJSP.equals("formAnularModificarOpCu14") ||
			       nombJSP.equals("formAnularModificarOpCu18") ||
			       nombJSP.equals("formEjecutarEvalCrediticia") ||
   				   nombJSP.equals("formRealizarAltaTarjeta") ||
   				   nombJSP.equals("formEvaluarFactibilidadOp") ||
			       nombJSP.equals("formArchivarExpediente") ||
			       nombJSP.equals("formVisualizarExpediente") ||
			       nombJSP.equals("formGestionEnvioContent")) {
			LOG.info("CONDICION C");
			iniciarConfExp(expedienteVO);
			// deshabilita los checks
			readOnlyVl = true;
			readOnlyVd = true;
			readOnlyZp = true;
			readOnlyDPS=true;
		}
	}
	
	private void iniciarDatos (Expediente expedienteVO) {
		
		if (expedienteVO.getExpedienteTC().getVerifLab()!=null && expedienteVO.getExpedienteTC().getVerifLab().equals(Constantes.CHECK_SELECCIONADO)) {
			verLaboral = true;
		}
		
		if (expedienteVO.getExpedienteTC().getVerifDom()!=null && expedienteVO.getExpedienteTC().getVerifDom().equals(Constantes.CHECK_SELECCIONADO)) {
			verDomiciliaria = true;
		}
		
		if (expedienteVO.getExpedienteTC().getZonaPel()!=null && expedienteVO.getExpedienteTC().getZonaPel().equals(Constantes.CHECK_SELECCIONADO)) {
			verZonaPeligrosa = true;
		}
		
		if (expedienteVO.getExpedienteTC().getVerifDps()!=null && expedienteVO.getExpedienteTC().getVerifDps().equals(Constantes.CHECK_SELECCIONADO)){
			verDPS = true;
			System.out.println("verDPS es true");
		}else{
			verDPS = false;
			System.out.println("verDPS es false");
		}
			
				
	}
	
	public void copiarDatosExpediente () {
		if (!readOnlyVl) {
			expedienteVO.getExpedienteTC().setVerifLab(verLaboral ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		} 
		if (!readOnlyVd) {
			expedienteVO.getExpedienteTC().setVerifDom(verDomiciliaria ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		} 
		if (!readOnlyZp) {
			expedienteVO.getExpedienteTC().setZonaPel(verZonaPeligrosa ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		}
		if (!readOnlyDPS) {
			expedienteVO.getExpedienteTC().setVerifDps(verDPS ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		}		
	}
	
	private void iniciarConfExp(Expediente expedienteVO) {
		
		if (expedienteVO.getExpedienteTC().getVerifLab()!=null && expedienteVO.getExpedienteTC().getVerifLab().equals(Constantes.CHECK_SELECCIONADO)) {
			verLaboral = true;
		}
		
		if (expedienteVO.getExpedienteTC().getVerifDom()!=null && expedienteVO.getExpedienteTC().getVerifDom().equals(Constantes.CHECK_SELECCIONADO)) {
			verDomiciliaria = true;
		}
		
		if (expedienteVO.getExpedienteTC().getZonaPel()!=null && expedienteVO.getExpedienteTC().getZonaPel().equals(Constantes.CHECK_SELECCIONADO)) {
			verZonaPeligrosa = true;
		}
		
		if (expedienteVO.getExpedienteTC().getVerifDps()!=null && expedienteVO.getExpedienteTC().getVerifDps().equals(Constantes.CHECK_SELECCIONADO)) {
			verDPS = true;
			System.out.println("verDPS ...  es true");
		}else{
			verDPS = false;
			System.out.println("verDPS ...  es false");
		}
			
	}

	private void iniciarRegExp (boolean esUsuarioLima) {		
		if (esUsuarioLima) {
			readOnlyVl = true;					
			readOnlyVd = true;
			readOnlyDPS = true;
		}
	}
	
	private void obtenerDatos() {	
		
		if(expedienteVO!=null && expedienteVO.getProducto()!=null && expedienteVO.getProducto().getId()>0){
			List<ProductoEtiqueta> listProductoEtiqueta=productoEtiquetabean.buscarPorIdProducto(expedienteVO.getProducto().getId());
			
			if(listProductoEtiqueta!=null && listProductoEtiqueta.size()>0){
				for(ProductoEtiqueta value : listProductoEtiqueta){				
					if(value.getEtiqueta().equals(Constantes.CAMPO_DPS))
						setRenderedDps(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
						//setReadOnlyDPS(true);
				}
			}			
		}
		
		listaTipoVerificacion = Util.crearItems(tipoVerificacionbean.buscarTodos(),
				"codigo", "descripcion");
	}
	
	public void cambiarVerLaboral(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();		
		PanelDocumentosMB panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}

	public void cambiarVerDomiciliaria(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();		
		PanelDocumentosMB panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}
	
	public void cambiarVerDps(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();		
		PanelDocumentosMB panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}
	
	public boolean isVerLaboral() {
		return verLaboral;
	}

	public void setVerLaboral(boolean verLaboral) {
		this.verLaboral = verLaboral;
	}
	
	public boolean isVerZonaPeligrosa() {
		return verZonaPeligrosa;
	}

	public void setVerZonaPeligrosa(boolean verZonaPeligrosa) {
		this.verZonaPeligrosa = verZonaPeligrosa;
	}

	public boolean isVerDomiciliaria() {
		return verDomiciliaria;
	}

	public void setVerDomiciliaria(boolean verDomiciliaria) {
		this.verDomiciliaria = verDomiciliaria;
	}
	
	public TipoVerificacion getTipoVerificacionVO() {
		return tipoVerificacionVO;
	}

	public void setTipoVerificacionVO(TipoVerificacion tipoVerificacionVO) {
		this.tipoVerificacionVO = tipoVerificacionVO;
	}

	public List<String> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<String> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public List<SelectItem> getListaTipoVerificacion() {
		return listaTipoVerificacion;
	}

	public void setListaTipoVerificacion(List<SelectItem> listaTipoVerificacion) {
		this.listaTipoVerificacion = listaTipoVerificacion;
	}

	public boolean isReadOnlyVl() {
		return readOnlyVl;
	}

	public void setReadOnlyVl(boolean readOnlyVl) {
		this.readOnlyVl = readOnlyVl;
	}

	public boolean isReadOnlyVd() {
		return readOnlyVd;
	}

	public void setReadOnlyVd(boolean readOnlyVd) {
		this.readOnlyVd = readOnlyVd;
	}

	public boolean isReadOnlyZp() {
		return readOnlyZp;
	}

	public void setReadOnlyZp(boolean readOnlyZp) {
		this.readOnlyZp = readOnlyZp;
	}

	public boolean isRenderedDps() {
		return renderedDps;
	}

	public void setRenderedDps(boolean renderedDps) {
		this.renderedDps = renderedDps;
	}

	public boolean isReadOnlyDPS() {
		return readOnlyDPS;
	}

	public void setReadOnlyDPS(boolean readOnlyDPS) {
		this.readOnlyDPS = readOnlyDPS;
	}

	public boolean isVerDPS() {
		return verDPS;
	}

	public void setVerDPS(boolean verDPS) {
		this.verDPS = verDPS;
	}	
	
}

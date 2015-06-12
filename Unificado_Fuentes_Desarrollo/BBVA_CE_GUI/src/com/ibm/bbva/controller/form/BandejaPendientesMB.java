package com.ibm.bbva.controller.form;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.BuscarBandejaPendMB;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.session.ParametrosConfBeanLocal;

@SuppressWarnings("serial")
@ManagedBean(name = "bandejaPendientes")
@RequestScoped
public class BandejaPendientesMB extends AbstractMBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(BandejaPendientesMB.class);
	
	private String flCopiaArchivos;
	private String numeroIntentos;
	private HtmlCommandButton btnActualizaWebSeal;
	
	public BandejaPendientesMB() {		
		flCopiaArchivos = (String) getObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);		
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		
		/*String idTarea = (String) getObjectSession("tareaSession");
		Integer tamListGuiaDocumentaria = (Integer) getObjectSession("tamListaGuiaDoc");
		if((tamListGuiaDocumentaria == null || tamListGuiaDocumentaria.intValue() == 0) && !"1".equals(idTarea)){
			flCopiaArchivos = "0";
		}*/
		
		String strListaDocsTransferencias = (String)getObjectSession("strListaDocsTransferencias");
		if(strListaDocsTransferencias == null || "".equals(strListaDocsTransferencias) || "null".equals(strListaDocsTransferencias)){
			flCopiaArchivos = "0";
		}
		
		try{
			ParametrosConfBeanLocal parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			numeroIntentos = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, Constantes.INTENTOS_WS_UN).getValorVariable();
			LOG.info("numeroIntentos pinWebSeal - UNIFICADO: "+numeroIntentos);
		}
		catch(Exception e){
			LOG.error(e.getMessage(), e);
		}
	}

	public void buscar(AjaxBehaviorEvent event) {	
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarBandejaPendMB busqueda = (BuscarBandejaPendMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaPend");		
		if (busqueda.valida()) {
			addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, null);
			addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO, null);			
			busqueda.buscar();
		}		
	}

	public String limpiar() {	
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarBandejaPendMB busqueda = (BuscarBandejaPendMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaPend");
		busqueda.limpiar();				
		return null;
	}	
	
	public String cancelar() {		
		return "/index?faces-redirect=true";
	}
	
	public String getFlCopiaArchivos() {
		return flCopiaArchivos;
	}

	public void setFlCopiaArchivos(String flCopiaArchivos) {
		this.flCopiaArchivos = flCopiaArchivos;
	}
	
	public HtmlCommandButton getBtnActualizaWebSeal() {
		return btnActualizaWebSeal;
	}

	public void setBtnActualizaWebSeal(
			HtmlCommandButton btnActualizaWebSeal) {
		this.btnActualizaWebSeal = btnActualizaWebSeal;
	}
	
	public String getNumeroIntentos() {
		return numeroIntentos;
	}

	public void setNumeroIntentos(String numeroIntentos) {
		this.numeroIntentos = numeroIntentos;
	}
	
	public void pinWebSeal(AjaxBehaviorEvent event) {
		LOG.info("En el metodo pinWebSeal de bandeja de pendientes - UNIFICADO");
	}
	
}
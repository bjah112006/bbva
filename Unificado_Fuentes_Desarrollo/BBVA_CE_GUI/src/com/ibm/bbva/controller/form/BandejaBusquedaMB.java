package com.ibm.bbva.controller.form;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import com.ibm.bbva.controller.Constantes; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.common.BuscarBandejaMB;

@SuppressWarnings("serial")
@ManagedBean(name = "bandejaBusqueda")
@RequestScoped
public class BandejaBusquedaMB extends AbstractMBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(BandejaBusquedaMB.class);
	
	public BandejaBusquedaMB() {
	}

	//public void buscar(AjaxBehaviorEvent event) {	
	public String buscar(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarBandejaMB buscarBandejaMB = (BuscarBandejaMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandeja");		
		if (buscarBandejaMB.valida()) {	
			addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, null);
			addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO, null);
			removeObjectSession(Constantes.EXPEDIENTE_SESION_HISTORICO);
			
		    buscarBandejaMB.buscar();
	    }
		return null;
	}

	public String limpiar() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarBandejaMB buscarBandejaMB = (BuscarBandejaMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandeja");					
		buscarBandejaMB.limpiar();	    
		return null;
	}
	
	public String cancelar() {		
		return "/index?faces-redirect=true";
	}
}
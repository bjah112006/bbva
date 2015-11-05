package com.ibm.bbva.controller.form;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.BuscarBandejaHistMB;
import com.ibm.bbva.controller.common.TablaBandejaHistMB;
import com.ibm.bbva.tabla.util.vo.HistorialDetalle;

@SuppressWarnings("serial")
@ManagedBean(name = "bandejaHistorica")
@RequestScoped
public class BandejaHistoricaMB extends AbstractMBean {

	private static final Logger LOG = LoggerFactory.getLogger(BandejaHistoricaMB.class);

	public BandejaHistoricaMB() {
	}

	public String buscar(){
	//public void buscar(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();  

		BuscarBandejaHistMB buscarBandejaHist = (BuscarBandejaHistMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaHist");
		
		if (buscarBandejaHist.valida()) {
			List<HistorialDetalle> lista = buscarBandejaHist.buscar();
			addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
						
			TablaBandejaHistMB tablaBandejaHist = (TablaBandejaHistMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaBandejaHist");
			
			tablaBandejaHist.actualizarLista();
			tablaBandejaHist.ordenarPorDefecto();
		}
		return null;
	}	

	public String limpiar() {
		BuscarBandejaHistMB buscarBandejaHist = (BuscarBandejaHistMB) getObjectRequest("buscarBandejaHist");
		buscarBandejaHist.limpiar();	
		return null;
	}
	
	public String cancelar() {		
		return "/index?faces-redirect=true";
	}

}

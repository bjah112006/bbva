package com.ibm.bbva.controller.form;

import java.text.DateFormat;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.BuscarTCMB;

@SuppressWarnings("serial")
@ManagedBean(name = "reporteTC")
@RequestScoped
public class ReporteTC {
	private boolean viewGenerar;
	private ArrayList arrayList;

	public ReporteTC(){
	}
	
	public String generar(){
		this.setViewGenerar(false);
		ArrayList arrayList=null;
		BuscarTCMB buscarTCMB=null;
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		buscarTCMB = (BuscarTCMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarTC");
		arrayList = buscarTCMB.obtenerParametros();
		
		if(arrayList!=null){
			obtRangoFechas();
			this.setViewGenerar(true);
		}
		this.arrayList=arrayList;
		return null;
	}
	public String obtRangoFechas(){
		BuscarTCMB buscarTCMB=null;	
		String fechaRango=null;
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		buscarTCMB = (BuscarTCMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarTC");
		
		DateFormat df =  DateFormat.getDateInstance();
		if(buscarTCMB!=null && buscarTCMB.getFechaInicio()!=null && buscarTCMB.getFechaFin()!=null){
			fechaRango= df.format(buscarTCMB.getFechaInicio())+ Constantes.DESCRIPCION_SUBTITULO_4+ df.format(buscarTCMB.getFechaFin());
		}
		return fechaRango;
	}
	
	public boolean isViewGenerar() {
		return viewGenerar;
	}

	public void setViewGenerar(boolean viewGenerar) {
		this.viewGenerar = viewGenerar;
	}
}

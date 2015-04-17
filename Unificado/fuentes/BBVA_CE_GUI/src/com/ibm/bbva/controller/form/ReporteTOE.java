package com.ibm.bbva.controller.form;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.openjpa.lib.log.Log;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.BuscarTOEMB;
import com.ibm.bbva.controller.common.TablaGenerarTOEMB;
import com.ibm.bbva.entities.PosibleValor;
import com.ibm.bbva.session.PosibleValorBeanLocal;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosVO;


//import com.ibm.bbva.util.ExportarFichaExcel;

@SuppressWarnings("serial")
@ManagedBean(name = "reporteTOE")
@RequestScoped
public class ReporteTOE {

	@EJB
	private PosibleValorBeanLocal posibleValorBean;	
	
	private boolean viewTabla;
	private boolean viewGenerar;
	private boolean existeErrorGenerar=true;
	private ArrayList arrayList;

	/**
	 * Parametros para Applet
	 * */
					
		public Map<Integer,Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>>> paramMapDatosGenerados;
		
		public String accion;
		
	public ReporteTOE(){
	}
	
	public String limpiar(){
		BuscarTOEMB buscarTOEMB=null;
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		buscarTOEMB = (BuscarTOEMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarTOE");
		
		buscarTOEMB.limpiar();
		this.existeErrorGenerar=true;
		return null;
	}
	
	public String buscar(){
		this.setViewGenerar(false);
		ArrayList arrayList=null;
		BuscarTOEMB buscarTOEMB=null;
		TablaGenerarTOEMB tablaGenerarTOEMB=null;		
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		buscarTOEMB = (BuscarTOEMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarTOE");	

		arrayList=buscarTOEMB.obtenerParametros();

		if(arrayList!=null){
			
			System.out.println("parm 2"+ arrayList.get(2));
			System.out.println("parm 3"+ arrayList.get(3));
			System.out.println("parm 4"+ arrayList.get(4));
			System.out.println("parm 5"+ arrayList.get(5));
			System.out.println("parm 6"+ arrayList.get(6));
			System.out.println("parm 7"+ arrayList.get(7));
			
			tablaGenerarTOEMB= (TablaGenerarTOEMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaGenerarTOE");
			
			tablaGenerarTOEMB.buscarTOE(arrayList);
			this.setViewTabla(true);
		}
		else{
			this.setViewTabla(false);
		}
		
		this.arrayList=arrayList;
		this.setAccion(Constantes.ACCION_BUSQUEDA);
		this.existeErrorGenerar=true;
		return null;
		
	}
	

	public String generar(){

		ArrayList arrayList=null;
		
		TablaGenerarTOEMB tablaGenerarTOEMB=null;	
		BuscarTOEMB buscarGenerarTOEMB=null;	
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		buscarGenerarTOEMB = (BuscarTOEMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarTOE");	

		arrayList=buscarGenerarTOEMB.obtenerParametros();
		
		if(arrayList!=null){
			if(buscarGenerarTOEMB.getParamTipoReporteApplet().equals(Constantes.ID_TOE_ESPECIFICO)){
				tablaGenerarTOEMB= (TablaGenerarTOEMB)  
						 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaGenerarTOE");
				System.out.println("Generar Reporte Especifico");

				if(tablaGenerarTOEMB.validarReporteEspecificoTOE(arrayList)){
					//SE GENERA EL EXCEL
					this.existeErrorGenerar=false;
					this.setViewTabla(false);
					System.out.println("existeErrorGenerar falso");
				}else{
					//MUESTRA MENSAJE QUE NO HAY DATA
					this.existeErrorGenerar=true;
					this.setViewTabla(true);
					System.out.println("existeErrorGenerar true");
				}	
			}else
				this.existeErrorGenerar=false;
			
		}else
			this.existeErrorGenerar=true;
		
		this.arrayList=arrayList;
		
		return null;
		
	}	
	

	public String obtRangoFechas(){
		BuscarTOEMB buscarTOEMB=null;	
		String fechaRango=null;
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		buscarTOEMB = (BuscarTOEMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarTOE");
		
		DateFormat df =  DateFormat.getDateInstance();
		if(buscarTOEMB!=null && buscarTOEMB.getFechaInicio()!=null && buscarTOEMB.getFechaFin()!=null){
			fechaRango= df.format(buscarTOEMB.getFechaInicio())+ Constantes.DESCRIPCION_SUBTITULO_4+ df.format(buscarTOEMB.getFechaFin());
		}
		
		return fechaRango;
	}
	
	public String obtTituloEncabezado(String tipoTit){
		String subTitulo=null;
		if(arrayList!=null && arrayList.size()>0){
			System.out.println("arrayList 4 = "+this.arrayList.get(4).toString());
			
	        List<PosibleValor> listPosibleValor = posibleValorBean.buscarPorIdColumna(Long.parseLong(Constantes.ID_POSIBLE_VALOR_PERFIL_ESTADO));
	        String flujo="";
	        if(listPosibleValor!=null && listPosibleValor.size()>0)
	            for(int i=0; i<listPosibleValor.size();i++){
	            	if(listPosibleValor.get(i).getValor().equals(this.arrayList.get(5).toString().trim())){
	            		flujo=listPosibleValor.get(i).getEtiqueta();
	            	}
	            }
	        
	        subTitulo=Constantes.DESCRIPCION_SUBTITULO_1 +this.obtRangoFechas();
	        if(tipoTit.equals(Constantes.ID_TOE_GENERAL))
	        	subTitulo=subTitulo+ Constantes.VALOR_SEPARADOR_TITULO+Constantes.DESCRIPCION_SUBTITULO_3+ flujo;					
		}

        return subTitulo;
	}
	
	public boolean isViewTabla() {
		return viewTabla;
	}

	public void setViewTabla(boolean viewTabla) {
		this.viewTabla = viewTabla;
	}

	public boolean isViewGenerar() {
		return viewGenerar;
	}

	public void setViewGenerar(boolean viewGenerar) {
		this.viewGenerar = viewGenerar;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public boolean isExisteErrorGenerar() {
		return existeErrorGenerar;
	}

	public void setExisteErrorGenerar(boolean existeErrorGenerar) {
		this.existeErrorGenerar = existeErrorGenerar;
	}

	
}
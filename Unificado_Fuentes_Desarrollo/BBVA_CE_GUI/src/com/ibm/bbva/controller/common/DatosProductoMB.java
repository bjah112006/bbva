package com.ibm.bbva.controller.common;

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
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.ProductoEtiqueta;
import com.ibm.bbva.session.ProductoEtiquetaBeanLocal;


@SuppressWarnings("serial")
@ManagedBean(name = "datosProducto")
@RequestScoped
public class DatosProductoMB extends AbstractMBean {
	
	@EJB
	private ProductoEtiquetaBeanLocal productoEtiquetabean;	
	
	private Expediente expediente;
	private boolean selectedItem;
	private boolean tasaEspecial;
	/**
	 * Cambio Para PLD
	 * */
	private boolean renderedEnvioTarjeta;
	private boolean renderedPlazoSolicitado;
	private boolean renderedSolicitudTasaEsp;
	private boolean renderedGarantia;
	//private boolean renderedDps;	
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosProductoMB.class);
	
	public DatosProductoMB() {
		
	}
	
	@PostConstruct
    public void init() {
		obtenerDatos();
	}
	
	public void obtenerDatos() {
		/*Obtiene Datos de Expediente*/
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
				
		if (expediente.getExpedienteTC().getFlagSolTasaEsp()!=null && expediente.getExpedienteTC().getFlagSolTasaEsp().equals(Constantes.CHECK_SELECCIONADO)) {
			selectedItem = true;
		}	
		
		if(expediente!=null && expediente.getProducto()!=null && expediente.getProducto().getId()>0)
			campoProducto(expediente.getProducto().getId());
		
		
	}
	
	private void campoProducto(long idProd){
		List<ProductoEtiqueta> listProductoEtiqueta=productoEtiquetabean.buscarPorIdProducto(idProd);
		
		/*verificacion Domiciliaria*/
		FacesContext ctx = FacesContext.getCurrentInstance();  
		VerificarAprobarMB solVerificarAprobar = (VerificarAprobarMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");
		
		if(listProductoEtiqueta!=null && listProductoEtiqueta.size()>0){
			for(ProductoEtiqueta value : listProductoEtiqueta){
				if(value.getEtiqueta().equals(Constantes.CAMPO_ENVIO_TARJETA))
					setRenderedEnvioTarjeta(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
				if(value.getEtiqueta().equals(Constantes.CAMPO_PLAZO_SOLICITADO))
					setRenderedPlazoSolicitado(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
				if(value.getEtiqueta().equals(Constantes.CAMPO_SOL_TASA_ESP))
					setRenderedSolicitudTasaEsp(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);	
				if(value.getEtiqueta().equals(Constantes.CAMPO_GARANTIA))
					setRenderedGarantia(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);				
				//if(value.getEtiqueta().equals(Constantes.CAMPO_DPS))
					//solVerificarAprobar.setVerDPS(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
			}
		}
	}

	public Expediente getExpediente() {
		return expediente;
	}


	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public boolean isSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(boolean selectedItem) {
		this.selectedItem = selectedItem;
	}

	public boolean isRenderedEnvioTarjeta() {
		return renderedEnvioTarjeta;
	}

	public void setRenderedEnvioTarjeta(boolean renderedEnvioTarjeta) {
		this.renderedEnvioTarjeta = renderedEnvioTarjeta;
	}

	public boolean isRenderedPlazoSolicitado() {
		return renderedPlazoSolicitado;
	}

	public void setRenderedPlazoSolicitado(boolean renderedPlazoSolicitado) {
		this.renderedPlazoSolicitado = renderedPlazoSolicitado;
	}

	public boolean isRenderedSolicitudTasaEsp() {
		return renderedSolicitudTasaEsp;
	}

	public void setRenderedSolicitudTasaEsp(boolean renderedSolicitudTasaEsp) {
		this.renderedSolicitudTasaEsp = renderedSolicitudTasaEsp;
	}

	public boolean isTasaEspecial() {
		return tasaEspecial;
	}

	public void setTasaEspecial(boolean tasaEspecial) {
		this.tasaEspecial = tasaEspecial;
	}

	public boolean isRenderedGarantia() {
		return renderedGarantia;
	}

	public void setRenderedGarantia(boolean renderedGarantia) {
		this.renderedGarantia = renderedGarantia;
	}
		
}

package com.ibm.bbva.controller.common;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.RegistrarExpedienteMB;
import com.ibm.bbva.entities.EstadoCivil;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.session.EstadoCivilBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "datosClienteNuevo")
@RequestScoped
public class DatosClienteNuevoMB extends DatosClienteMB {
	
	@EJB
	private EstadoCivilBeanLocal estadoCivilbean;
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosClienteNuevoMB.class);
	private boolean itemDisabledcaractPep = true;
	private boolean itemDisabledcaractAval = true;
	private boolean mostrarBusquedaConyuge = false;
	private boolean renderedNombre=false;
	private boolean renderedRazonSocial=false;
	private boolean renderedApellidos=false;
	
	public DatosClienteNuevoMB(){
		
	}
	
	public boolean esValido () {
		BuscarClienteMB buscarCliente = null;
		FacesContext ctx = FacesContext.getCurrentInstance();  
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		if (jspPrinc.equals("formRegistrarExpediente")) {
			formulario = "frmRegistrarExpediente";
			buscarCliente = (BuscarClienteMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarCliente");
		}
		
		boolean existeError = false;
		// validar fecha vencimiento
		Date fechaVenc = getClienteNatural().getFecVenDoi();
		Date fechaActual = new Date ();
		if (fechaVenc == null) {
			addMessageError(formulario + ":fecVenDoiN", 
					"com.ibm.bbva.common.datosClienteNuevo.msg.fecVenDoi.vacio");
			existeError = true;
		} else if (!fechaVenc.after(fechaActual)) {
			addMessageError(formulario + ":fecVenDoiN", 
					"com.ibm.bbva.common.datosClienteNuevo.msg.fecVenDoi.incorrecto");
			existeError = true;
		}
		
		// validar nombres y apellidos
		String nombre = getClienteNatural().getNombre();
		if (nombre==null || nombre.trim().equals("")) {
			addMessageError(formulario + ":dcnNombresN", 
					"com.ibm.bbva.common.datosClienteNuevo.msg.dcnNombres");
			existeError = true;
		}
		
		if (!buscarCliente.getTipoDOISeleccionado().equals(Constantes.CODIGO_TIPO_DOI_RUC)){
			String apePat = getClienteNatural().getApePat();
			if (apePat==null || apePat.trim().equals("")) {
				addMessageError(formulario + ":dcnApePatN", 
						"com.ibm.bbva.common.datosClienteNuevo.msg.dcnApePat");
				existeError = true;
			}
			String apeMat = getClienteNatural().getApeMat();
			if (apeMat==null || apeMat.trim().equals("")) {
				addMessageError(formulario + ":dcnApeMatN", 
						"com.ibm.bbva.common.datosClienteNuevo.msg.dcnApeMat");
				existeError = true;
			}
		}
		
		// validar estado civil
		if (getIdEstadoCivil()==null || getIdEstadoCivil().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			addMessageError(formulario + ":estadoCivilN", 
					"com.ibm.bbva.common.datosClienteNuevo.msg.estadoCivil");
			existeError = true;
		}
		
		// validar categorias renta
		if (getCategoriasRenta().isEmpty()) {
			addMessageError(formulario + ":categoriasRentaN", 
					"com.ibm.bbva.common.datosClienteNuevo.msg.categoriasRenta");
			existeError = true;
		}
		
		//if (this.getCodigoTipoOferta()!=null && this.getCodigoTipoOferta().equals(Constantes.CODIGO_OFERTA_APROBADO)) {
			String correo = getClienteNatural().getCorreo();
			String celular = getClienteNatural().getCelular();
			boolean correoVacio=false;
			boolean celularVacio=false;
			// validar correo
			if ((correo.trim().equals("") || correo.trim().length()<1 || correo.trim().length()>50) && 
					(celular.trim().equals("") || celular.trim().length()<1 || celular.trim().length()>14)) {
			//if (correo.isEmpty() && celular.isEmpty()) {
				addMessageError(formulario + ":dcnCelularN", 
						"com.ibm.bbva.common.datosClienteNuevo.msg.celularCorreo");
				existeError = true;
			}else{
				if (correo.isEmpty() && !celular.isEmpty()){
					correoVacio=true;
				}else if (celular.isEmpty() && !correo.isEmpty()){
					celularVacio=true;
				}
	
				if (!correoVacio){
					boolean validoMail=Util.validarEmail(correo);
					if (!validoMail){
						addMessageError(formulario + ":dcnCorreoN", 
								"com.ibm.bbva.common.datosClienteNuevo.msg.correoIncorrecto");
						existeError = true;
					}
				}
				
				
				// validar celular
				if (!celularVacio){
					if (celular.isEmpty()) {
						addMessageError(formulario + ":dcnCelularN", 
								"com.ibm.bbva.common.datosClienteNuevo.msg.celular");
						existeError = true;
					}
				}
					
			}
	    //}				
		
		return !existeError;
	}
	
	public void cambiarEstadoCivil(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		RegistrarExpedienteMB regExpMB = null;
		ProductoNuevoMB productoNuevo  = null;
		PanelDocumentosMB panelDocumento=null;
		
		regExpMB = (RegistrarExpedienteMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente");
				
		productoNuevo = (ProductoNuevoMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");
		
		panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
		
		/*Datos Conyuge*/
		//DatosConyugeMB datosConyuge = (DatosConyugeMB)  
			//	 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosConyuge");
		
		/*Buscar Conyuge*/ //JBTA
		BuscarConyugeMB buscarConyuge = (BuscarConyugeMB) //JBTA
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarConyuge"); //JBTA
		//datosConyuge.setMostrarDatosConyugePrincipal(false);
		buscarConyuge.setNumeroDOI("");
		
		/**
		 * @author Daniel Flores
		 * Borrar conyugue guardado en expediente en sesión
		 */
		
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		expediente.getExpedienteTC().setClienteNaturalConyuge(null);
		// esto solo debería funcionar para pre-registro
		if (expediente.getClienteNatural() != null) {
			if (getIdEstadoCivil() != null && !getIdEstadoCivil().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
				EstadoCivil estadoCivil = estadoCivilbean.buscarPorId(Long.parseLong(getIdEstadoCivil()));
				if (estadoCivil != null) {
					expediente.getClienteNatural().setEstadoCivil(estadoCivil);
				}
			}
		}
		addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);
		
		
		
		System.out.println("Aki 1");
		if (!isCasado()) {
			regExpMB.visualizarPanelConyuge(false);
			regExpMB.visualizarDatosPanelConyuge(false);
			//datosConyuge.setMostrarDatosConyuge(false);	
			System.out.println("no es casado");
			productoNuevo.setRenderedConyuge(true);	
			productoNuevo.getExpediente().getExpedienteTC().setSbsConyuge(0);
			productoNuevo.cambiarTipoOferta(null);
			regExpMB.setMostrarPanelConyuge(false);
		} else {
			System.out.println("casado con tipo oferta = "+productoNuevo.getCodTipoOferta());
			if (productoNuevo.getCodTipoOferta()!= null 
					&& productoNuevo.getCodTipoOferta().equals(Constantes.CODIGO_OFERTA_APROBADO)) {
				//datosConyuge.setMostrarDatosConyuge(true);
				regExpMB.visualizarPanelConyuge(true);
				//regExpMB.visualizarDatosPanelConyuge(true);
				regExpMB.setMostrarPanelConyuge(true);
				
				productoNuevo.setRenderedConyuge(false);
				productoNuevo.validaPautaClasificacion(getIdEstadoCivil());
				buscarConyuge.setTipoDOISeleccionado(Constantes.CODIGO_TIPO_DOI_DNI); //JBTA
				System.out.println("tipo oferta aprobado");

				
			}else {
				//datosConyuge.setMostrarDatosConyuge(false);	
				regExpMB.setMostrarPanelConyuge(false);
				System.out.println("tipo oferta 	NO aprobado");
			}
		}
		
		if (isSoltero())
			productoNuevo.setRenderedSoltero(true);
		else
			productoNuevo.setRenderedSoltero(false);

	}
	
	public void cambiarCaracteristicas(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		PanelDocumentosMB panelDocumento = null;
		
		panelDocumento = (PanelDocumentosMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
		
		List<String> codigo = (List<String>) getCaracAdicionales();
		this.setCaracAdicionales(codigo);		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}
	
	public void cambiarCategorias(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		PanelDocumentosMB panelDocumento = null;
		
		panelDocumento = (PanelDocumentosMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
				
		List<String> codigo = (List<String>) getCategoriasRenta();
		this.setCategoriasRenta(codigo);	
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);		
	}
	
	public boolean isCasado() {
		if (getIdEstadoCivil() == null
				|| getIdEstadoCivil().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)
				|| !(getIdEstadoCivil().equals(Constantes.ESTADO_CIVIL_CASADO.toString())
				|| getIdEstadoCivil().equals(Constantes.ESTADO_CIVIL_CONVIVIENTE.toString()))) {
			return false;
		}
		return true;
	}
	
	public boolean isSoltero() {
		if (!getIdEstadoCivil().equals(Constantes.ESTADO_CIVIL_SOLTERO)) {
			return false;
		}
		return true;
	}

	public boolean getItemDisabledcaractPep() {
		return itemDisabledcaractPep;
	}

	public void setItemDisabledcaractPep(boolean itemDisabledcaractPep) {
		this.itemDisabledcaractPep = itemDisabledcaractPep;
	}

	public boolean isItemDisabledcaractAval() {
		return itemDisabledcaractAval;
	}

	public void setItemDisabledcaractAval(boolean itemDisabledcaractAval) {
		this.itemDisabledcaractAval = itemDisabledcaractAval;
	}

	public boolean isRenderedNombre() {
		return renderedNombre;
	}

	public void setRenderedNombre(boolean renderedNombre) {
		this.renderedNombre = renderedNombre;
	}

	public boolean isRenderedRazonSocial() {
		return renderedRazonSocial;
	}

	public void setRenderedRazonSocial(boolean renderedRazonSocial) {
		this.renderedRazonSocial = renderedRazonSocial;
	}

	public boolean isRenderedApellidos() {
		return renderedApellidos;
	}

	public void setRenderedApellidos(boolean renderedApellidos) {
		this.renderedApellidos = renderedApellidos;
	}
	
	
	

	
}
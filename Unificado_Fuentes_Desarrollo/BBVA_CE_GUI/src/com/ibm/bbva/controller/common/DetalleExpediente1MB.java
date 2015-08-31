package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.ConsultarClienteModificacionesMB;
import com.ibm.bbva.controller.form.CoordinarClienteSubsanarMB;
import com.ibm.bbva.controller.form.EvaluarDevolucionRiesgosMB;
import com.ibm.bbva.controller.form.RegularizarEscanearDocumentacionMB;
import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.EstadoCivil;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.session.CategoriaRentaBeanLocal;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.EstadoCivilBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "detalleExpediente1")
@RequestScoped
public class DetalleExpediente1MB extends AbstractMBean {

	@EJB
	private ClienteNaturalBeanLocal clienteNaturalBean;
	@EJB
	private CategoriaRentaBeanLocal categoriaRentaBean;
	@EJB
	private EstadoCivilBeanLocal estadoCivilBean; 
	@EJB
	private ExpedienteBeanLocal expedienteBean;
		
	private ClienteNatural clienteNatural;
	private CategoriaRenta categoriaRenta;	
	private Expediente expediente; 
	private List<SelectItem> listaEstadoCivil;
	private List<String> selectedItems;
	private List<String> categoriasRenta;
	private List<SelectItem> listaCategoriaRenta;	
	private String numeroExpediente;	
	private String idEstadoCivil;
	private String codigoTipoOferta;
	private boolean renderedFechaVencimiento;
	private boolean renderedEstadoCivil;
	private boolean disabledFlagPep;
	private boolean disabledFlagPHabiente;
	private boolean disabledFlagPAval;
	private boolean disabledFlagSubRogacion;

	private static final Logger LOG = LoggerFactory.getLogger(DetalleExpediente1MB.class);
	
	/*
	 * FIX2 ERIKA ABREGU 17-07-2015
	*/
	private boolean itemDisabledMonocuota;
	
	public DetalleExpediente1MB() {		
	
	}	

	@PostConstruct
    public void init() {
		
		String jspPrinc = getNombreJSPPrincipal();
		//inicio man1
		disabledFlagPep = true;
		disabledFlagPAval = true;
		//fin man1
		if (jspPrinc.equals("formCoordinarClienteSubsanar")) {
			renderedFechaVencimiento = true;
			renderedEstadoCivil = true;
			disabledFlagPep = true;
			disabledFlagPHabiente = true;
			disabledFlagPAval = true;			
		}
		
		obtenerDatos();
	}	
	
	public void obtenerDatos() {
		LOG.info("metodo obtenerDatos: ");
		/*Obtiene Datos de Expediente*/
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);

		if (expediente.getId() > 0){
			LOG.info("metodo expediente.getId(): "+expediente.getId());

			numeroExpediente = String.valueOf(expediente.getId());
			
			/*Obtiene Datos de Cliente*/
			if(expediente.getId() > 0 && (expediente.getClienteNatural() == null || expediente.getClienteNatural().getId() <= 0)){
				expediente = expedienteBean.buscarPorId(expediente.getId());
			}
			clienteNatural = clienteNaturalBean.buscarPorId(expediente.getClienteNatural().getId());
						
			/*Obtiene Datos Estado Civil*/			
			listaEstadoCivil = Util.crearItems(estadoCivilBean.buscarTodos(), true,	"id", "descripcion");
			
			idEstadoCivil = String.valueOf(expediente.getClienteNatural().getEstadoCivil().getId());
					
			/*Obtiene Datos Categoria de Renta*/
			listaCategoriaRenta = Util.crearItems(categoriaRentaBean.buscarTodos(),	"id", "descripcion", true);
								
			recortarDatosLista();
	
			Collections.sort(listaCategoriaRenta, new Util.SecletItemComparator(true));  
			
			/*Obtiene Datos Cliente Categoria Renta*/
			categoriasRenta = new ArrayList<String>();
			if (clienteNatural.getCategoriasRenta() != null) {
				for (CategoriaRenta lista : clienteNatural.getCategoriasRenta()) {			
					categoriasRenta.add(lista.getCodigo());
				}
			}			
			
			selectedItems = new ArrayList<String>();
			
			if (clienteNatural.getPerExpPub()!=null && clienteNatural.getPerExpPub().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("1");
			}
	        if (clienteNatural.getPagoHab()!=null && clienteNatural.getPagoHab().equals(Constantes.CHECK_SELECCIONADO)) {
	        	selectedItems.add("2");
			}		
			if (clienteNatural.getAval()!=null && clienteNatural.getAval().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("3");
			}		
			LOG.info("metodo clienteNatural.getSubrog(): "+clienteNatural.getSubrog());

			if (clienteNatural.getSubrog()!=null && clienteNatural.getSubrog().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("4");
			}else if (clienteNatural.getSubrog()!=null && clienteNatural.getSubrog().equals(Constantes.CHECK_NO_SELECCIONADO)) {
					 disabledFlagSubRogacion=true;
			}
			//fix2 erika abregu
			if (clienteNatural.getMonocuota()!=null && clienteNatural.getMonocuota().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("5");
				String jspPrinc = getNombreJSPPrincipal();
				if (jspPrinc.equals("formCoordinarClienteSubsanar")){
					itemDisabledMonocuota=false;
				}else{
					itemDisabledMonocuota=true;
				}
			}else if (clienteNatural.getMonocuota()!=null && clienteNatural.getMonocuota().equals(Constantes.CHECK_NO_SELECCIONADO)) {
				String jspPrinc = getNombreJSPPrincipal();
				if (jspPrinc.equals("formCoordinarClienteSubsanar")){
					itemDisabledMonocuota=false;
				}else{
					itemDisabledMonocuota=true;
				}
			}
			//fin de fix
		} else{
			
			/*Obtiene Datos Estado Civil*/					
			listaEstadoCivil = Util.crearItems(estadoCivilBean.buscarTodos(), true,	"id", "descripcion");
			
			/*Obtiene Datos Categoria de Renta*/			
			listaCategoriaRenta = Util.crearItems(categoriaRentaBean.buscarTodos(),	"id", "descripcion");						
		}
	}
	
	public void recortarDatosLista() {
		for (SelectItem lista: listaCategoriaRenta) {
			lista.setLabel(lista.getLabel().substring(0, 7)+".");			
		}
	}	
	
	public void copiarDatosClienteNatural (ClienteNatural clienteNatural) {
		clienteNatural.setEstadoCivil(this.clienteNatural.getEstadoCivil());
		clienteNatural.setApeMat(this.clienteNatural.getApeMat());
		clienteNatural.setApePat(this.clienteNatural.getApePat());
		clienteNatural.setNombre(this.clienteNatural.getNombre());		
		clienteNatural.setPerExpPub(this.clienteNatural.getPerExpPub());
		clienteNatural.setPagoHab(this.clienteNatural.getPagoHab());
		clienteNatural.setAval(this.clienteNatural.getAval());
		clienteNatural.setSubrog(this.clienteNatural.getSubrog());
		//fix2 erika abregu
		clienteNatural.setMonocuota(this.clienteNatural.getMonocuota());
		//fin de fix2 erika abregu
		clienteNatural.setNumDoi(this.clienteNatural.getNumDoi());
		clienteNatural.setCodCliente(this.clienteNatural.getCodCliente());
		clienteNatural.setFecVenDoi(this.clienteNatural.getFecVenDoi());
		clienteNatural.setId(this.clienteNatural.getId());
		clienteNatural.setSegmento(this.clienteNatural.getSegmento());
		clienteNatural.setTipoCliente(this.clienteNatural.getTipoCliente());
		clienteNatural.setTipoDoi(this.clienteNatural.getTipoDoi());
		clienteNatural.setIngNetoMensual(this.clienteNatural.getIngNetoMensual());		
	}
	
	public List<String> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<String> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public List<SelectItem> getListaCategoriaRenta() {
		return listaCategoriaRenta;
	}

	public void setListaCategoriaRenta(List<SelectItem> listaCategoriaRenta) {
		this.listaCategoriaRenta = listaCategoriaRenta;
	}
	
	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}
		
	public ClienteNatural getClienteNatural() {
		return clienteNatural;
	}

	public void setClienteNatural(ClienteNatural clienteNatural) {
		this.clienteNatural = clienteNatural;
	}

	public CategoriaRenta getCategoriaRenta() {
		return categoriaRenta;
	}

	public void setCategoriaRenta(CategoriaRenta categoriaRenta) {
		this.categoriaRenta = categoriaRenta;
	}

	public String getIdEstadoCivil() {
		return idEstadoCivil;
	}

	public void setIdEstadoCivil(String idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	public List<String> getCategoriasRenta() {
		return categoriasRenta;
	}

	public void setCategoriasRenta(List<String> categoriasRenta) {
		this.categoriasRenta = categoriasRenta;
	}

	public List<SelectItem> getListaEstadoCivil() {
		return listaEstadoCivil;
	}

	public void setListaEstadoCivil(List<SelectItem> listaEstadoCivil) {
		this.listaEstadoCivil = listaEstadoCivil;
	}

	public boolean esValido () {
		boolean existeError = false;
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		if (jspPrinc.equals("formCoordinarClienteSubsanar")) {
			formulario = "frmCoordinarClienteSubsanar";
		} else if (jspPrinc.equals("formEvaluarDevolucionRiesgos")){
			formulario = "frmEvaluarDevolucionRiesgos";
		} else if (jspPrinc.equals("formConsultarClienteModificaciones")){
			formulario = "frmConsultarClienteModificaciones";		
		} else if (jspPrinc.equals("formRegularizarEscanearDocumentacion")) {
			formulario = "frmRegularizarEscanearDocumentacion";
		} else if (jspPrinc.equals("formGestionarSubsanarOperacion")) {
			formulario = "frmGestionarSubsanarOperacion";
		} else if (jspPrinc.equals("formRegistrarExpedienteCu23")) {
			formulario = "frmRegistrarExpedienteCu23";
		} else if (jspPrinc.equals("formRegistrarExpedienteCu25")) {
			formulario = "frmRegistrarExpedienteCu25";
		}
		
		// validar fecha vencimiento
		Date fechaVenc = clienteNatural.getFecVenDoi();
		Date fechaActual = new Date ();
		if(fechaVenc == null){
			addMessageError(formulario + ":fecVenDoi", "com.ibm.bbva.common.detalleExpediente1.msg.fecVenDoi.vacio");
			existeError = true;
		}else if (!formulario.equalsIgnoreCase("frmCoordinarClienteSubsanar") && !fechaVenc.after(fechaActual)) {			
			addMessageError(formulario + ":fecVenDoi", "com.ibm.bbva.common.detalleExpediente1.msg.fecVenDoi.incorrecto");
			existeError = true;			
		}		
		// validar estado civil
		if (idEstadoCivil==null || idEstadoCivil.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			addMessageError(formulario + ":estadoCivil", "com.ibm.bbva.common.detalleExpediente1.msg.estadoCivil");
			existeError = true;
		}

		//if (this.getCodigoTipoOferta()!=null && this.getCodigoTipoOferta().equals(Constantes.CODIGO_OFERTA_APROBADO)) {
			String correo = getClienteNatural().getCorreo();
			String celular = getClienteNatural().getCelular();
			boolean correoVacio=false;
			boolean celularVacio=false;
			
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
	   // }		
		
		return !existeError;
	}
	
	/*
	 * Se llama despues de validar los valores 
	 */
	public ClienteNatural obtenerClienteNatural () {
		clienteNatural.setPerExpPub(seleccion("1"));
		clienteNatural.setPagoHab(seleccion("2"));
		clienteNatural.setAval(seleccion("3"));
		clienteNatural.setSubrog(seleccion("4"));
		//fix2 erika abregu
		clienteNatural.setMonocuota(seleccion("5"));
		//fin de fix2 erika abregu
		EstadoCivil estadoCivil = new EstadoCivil();
		estadoCivil.setId(Long.parseLong(idEstadoCivil));		
		clienteNatural.setEstadoCivil(estadoCivil);
        
        clienteNatural.setCategoriasRenta(new ArrayList<CategoriaRenta>());
		for (int i = 0, n = getCategoriasRenta().size(); i < n; i++) {
			clienteNatural.getCategoriasRenta().add(categoriaRentaBean.buscarPorId(Long.parseLong(getCategoriasRenta().get(i))));
		}
		
		return clienteNatural;
	}
	
	private String seleccion (String val) {
		LOG.info("seleccion: "+val);

		if(selectedItems == null || selectedItems.size() == 0){
			selectedItems = new ArrayList<String>();
			
			if (clienteNatural.getPerExpPub()!=null && clienteNatural.getPerExpPub().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("1");
			}
	        if (clienteNatural.getPagoHab()!=null && clienteNatural.getPagoHab().equals(Constantes.CHECK_SELECCIONADO)) {
	        	selectedItems.add("2");
			}		
			if (clienteNatural.getAval()!=null && clienteNatural.getAval().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("3");
			}		
			if (clienteNatural.getSubrog()!=null && clienteNatural.getSubrog().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("4");
			}
			
			//fix2 erika abregu
			if (clienteNatural.getMonocuota()!=null && clienteNatural.getMonocuota().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItems.add("5");
			}
			//fin de fix2 erika abregu
		}
		return selectedItems.contains(val) ? 
				Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO;
	}

	public String getNumeroExpediente() {
		return numeroExpediente;
	}

	public void setNumeroExpediente(String numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}

	public String getCodigoTipoOferta() {
		return codigoTipoOferta;
	}

	public void setCodigoTipoOferta(String codigoTipoOferta) {
		this.codigoTipoOferta = codigoTipoOferta;
	}
	
	public void cambiarEstadoCivil(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ProductoNuevoMB productoNuevo = (ProductoNuevoMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");		
		String jspPrinc = getNombreJSPPrincipal();
		boolean isVisual = validaVisualizaPaneles(productoNuevo.getCodTipoOferta());
		
		// si cambia el estado civil hay que actualizar tambi�n el objeto en sesi�n
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		if (expediente.getClienteNatural() != null) {
			if (getIdEstadoCivil() != null && !getIdEstadoCivil().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
				EstadoCivil estadoCivil = estadoCivilBean.buscarPorId(Long.parseLong(getIdEstadoCivil()));
				if (estadoCivil != null) {
					expediente.getClienteNatural().setEstadoCivil(estadoCivil);
				}
			}
		}
		addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);
		
		if (jspPrinc.equals("formCoordinarClienteSubsanar")) {
			CoordinarClienteSubsanarMB coordinarClienteSubsanar = (CoordinarClienteSubsanarMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "coordinarClienteSubsanar");			
			coordinarClienteSubsanar.visualizarPanelConyuge(isVisual);
			coordinarClienteSubsanar.visualizarDatosPanelConyuge(isVisual);
			
		} else if (jspPrinc.equals("formEvaluarDevolucionRiesgos")){			
			EvaluarDevolucionRiesgosMB evaluarDevolucionRiesgos = (EvaluarDevolucionRiesgosMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "evaluarDevolucionRiesgos");			
			evaluarDevolucionRiesgos.visualizarPanelConyuge(isVisual);
			evaluarDevolucionRiesgos.visualizarDatosPanelConyuge(isVisual);
			
			if (!isCasado()) {
				productoNuevo.setRenderedConyuge(true);	
				productoNuevo.getExpediente().getExpedienteTC().setSbsConyuge(0);
			}else{
				productoNuevo.setRenderedConyuge(false);
				productoNuevo.validaPautaClasificacion(getIdEstadoCivil());
			}
			
		} else if (jspPrinc.equals("formConsultarClienteModificaciones")){
			ConsultarClienteModificacionesMB consultarClienteModificaciones = (ConsultarClienteModificacionesMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "consultarClienteModificaciones");			
			consultarClienteModificaciones.visualizarPanelConyuge(isVisual);	
			consultarClienteModificaciones.visualizarDatosPanelConyuge(isVisual);
			
			if (!isCasado()) {
				productoNuevo.setRenderedConyuge(true);	
				productoNuevo.getExpediente().getExpedienteTC().setSbsConyuge(0);
			}else{
				productoNuevo.setRenderedConyuge(false);
				productoNuevo.validaPautaClasificacion(getIdEstadoCivil());
			}
			
		} else if (jspPrinc.equals("formRegularizarEscanearDocumentacion")) {
			RegularizarEscanearDocumentacionMB regularizarEscanearDocumentacion = (RegularizarEscanearDocumentacionMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "regularizarEscanearDocumentacion");			
			regularizarEscanearDocumentacion.visualizarPanelConyuge(isVisual);
			regularizarEscanearDocumentacion.visualizarDatosPanelConyuge(isVisual);
		
			if (!isCasado()) {
				productoNuevo.setRenderedConyuge(true);	
				productoNuevo.getExpediente().getExpedienteTC().setSbsConyuge(0);
			}else{
				productoNuevo.setRenderedConyuge(false);
				productoNuevo.validaPautaClasificacion(getIdEstadoCivil());
			}
		}		
	}	
	
	public boolean validaVisualizaPaneles (String tipoOferta) {
		boolean result = false;
		if (!isCasado()) {
			result = false;
		} else {
			if (tipoOferta!= null 
					&& tipoOferta.equals(Constantes.CODIGO_OFERTA_APROBADO)) {
				result = true;
			}
		}
		return result;
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
	
	public boolean isRenderedEstadoCivil() {
		return renderedEstadoCivil;
	}

	public void setRenderedEstadoCivil(boolean renderedEstadoCivil) {
		this.renderedEstadoCivil = renderedEstadoCivil;
	}

	public boolean isRenderedFechaVencimiento() {
		return renderedFechaVencimiento;
	}

	public void setRenderedFechaVencimiento(boolean renderedFechaVencimiento) {
		this.renderedFechaVencimiento = renderedFechaVencimiento;
	}

	public EstadoCivilBeanLocal getEstadoCivilBean() {
		return estadoCivilBean;
	}

	public void setEstadoCivilBean(EstadoCivilBeanLocal estadoCivilBean) {
		this.estadoCivilBean = estadoCivilBean;
	}

	public boolean isDisabledFlagPep() {
		return disabledFlagPep;
	}

	public void setDisabledFlagPep(boolean disabledFlagPep) {
		this.disabledFlagPep = disabledFlagPep;
	}

	public boolean isDisabledFlagPHabiente() {
		return disabledFlagPHabiente;
	}

	public void setDisabledFlagPHabiente(boolean disabledFlagPHabiente) {
		this.disabledFlagPHabiente = disabledFlagPHabiente;
	}

	public boolean isDisabledFlagPAval() {
		return disabledFlagPAval;
	}

	public void setDisabledFlagPAval(boolean disabledFlagPAval) {
		this.disabledFlagPAval = disabledFlagPAval;
	}

	public boolean isDisabledFlagSubRogacion() {
		return disabledFlagSubRogacion;
	}

	public void setDisabledFlagSubRogacion(boolean disabledFlagSubRogacion) {
		this.disabledFlagSubRogacion = disabledFlagSubRogacion;
	}	
	
	
	
	//fix2 erika abregu
	public boolean isItemDisabledMonocuota() {
		return itemDisabledMonocuota;
	}

	public void setItemDisabledMonocuota(boolean itemDisabledMonocuota) {
		this.itemDisabledMonocuota = itemDisabledMonocuota;
	}
	//fin de fix erika abregu

	public void cambiarCategorias1(AjaxBehaviorEvent event) {
		LOG.info("cambiarCategorias1:::::::");
		FacesContext ctx = FacesContext.getCurrentInstance();  
		PanelDocumentosMB panelDocumento = null;
		
		panelDocumento = (PanelDocumentosMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
				
		List<String> codigo = (List<String>) getCategoriasRenta();
		this.setCategoriasRenta(codigo);	
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);		
	}
	
	public void cambiarCaracteristicas1(AjaxBehaviorEvent event) {
		LOG.info("cambiarCaracteristicas1:::::::");
		FacesContext ctx = FacesContext.getCurrentInstance();  
		PanelDocumentosMB panelDocumento = null;
		
		panelDocumento = (PanelDocumentosMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
		
		List<String> codigo = (List<String>) getSelectedItems();
		this.setSelectedItems(codigo);		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}
	
}
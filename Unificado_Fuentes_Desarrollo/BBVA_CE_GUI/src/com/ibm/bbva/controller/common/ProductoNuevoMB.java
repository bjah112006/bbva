package com.ibm.bbva.controller.common;

import java.util.ArrayList;
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

import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.ConsultarClienteModificacionesMB;
import com.ibm.bbva.controller.form.EvaluarDevolucionRiesgosMB;
import com.ibm.bbva.controller.form.RegistrarExpedienteMB;
import com.ibm.bbva.controller.form.RegularizarEscanearDocumentacionMB;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.ExpedienteTC;
import com.ibm.bbva.entities.Garantia;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.ProductoEtiqueta;
import com.ibm.bbva.entities.Subproducto;
import com.ibm.bbva.entities.TipoMoneda;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.GarantiaBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.ProductoEtiquetaBeanLocal;
import com.ibm.bbva.session.SubproductoBeanLocal;
import com.ibm.bbva.session.TipoEnvioBeanLocal;
import com.ibm.bbva.session.TipoMonedaBeanLocal;
import com.ibm.bbva.session.TipoOfertaBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "productoNuevo")
@RequestScoped
public class ProductoNuevoMB extends AbstractMBean {

	@EJB
	private ProductoBeanLocal productobean;
	@EJB
	private TipoOfertaBeanLocal tipoOfertaobean;
	@EJB
	private TipoMonedaBeanLocal tipoMonedabean;
	@EJB
	private TipoEnvioBeanLocal tipoEnviobean;
	@EJB
	private SubproductoBeanLocal subproductobean;
	@EJB
	private ExpedienteBeanLocal expedientebean;
	@EJB
	private FacadeLocal facade;
	@EJB
	private ProductoEtiquetaBeanLocal productoEtiquetabean;	
	@EJB
	private GarantiaBeanLocal garantiabean;
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	
	private Expediente expediente;
	private List<SelectItem> listaProducto;
	public String codProducto;
	private List<SelectItem> listaTipoOferta;
	public String codTipoOferta;
	private String desTipoMoneda;
	private List<SelectItem> listaTipoMoneda;
	private String codTipoMoneda;
	private List<SelectItem> listaTipoEnvio;
	private String codTipoEnvio;
	private List<SelectItem> listaSubproducto;
	public String codSubproducto;
	private List<SelectItem> listaGarantia;
	public String codGarantia;	
	private boolean tasaEspecial;
	private boolean modificacionScoring;
	private boolean renderedScoring;
	private boolean renderedTasaEspecial;
	private boolean renderedProducto;
	private boolean renderedSubProducto;
	private boolean renderedTipoOferta;
	private boolean renderedLineaCredTipMoneda;
	private boolean renderedPreEvaluador;
	private boolean renderedConyuge;
	private boolean renderedSoltero;
	private boolean renderedTitular;
	private boolean renderedCantCred;
	/* String para formato numerico */	
	private String lineaCredSol;
	private boolean renderedMsgPauta;
	//private boolean subrogacionDeshabilitado;
	
	/**
	 * Visible por Producto
	 * */
	private boolean renderedEnvioTarjeta;
	private boolean renderedPlazoSolicitado;
	private boolean renderedSolicitudTasaEsp;
	private boolean disabledInputTasaEsp;
	private boolean renderedGarantia;
	private boolean disabledSolicitudTasaEsp;
	private boolean renderedClasificacionSbs = false;
	private boolean renderedLineaCreditoApr;
	private boolean renderedPlazoSolicitadoApr;
	
	private static final Logger LOG = LoggerFactory.getLogger(ProductoNuevoMB.class);
	
	public ProductoNuevoMB() {		
	}
	
	@PostConstruct
    public void init() {	
		LOG.info("ENTRO ProductoNuevo");
		
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		if(expediente.getId() > 0 && (expediente.getClienteNatural() == null || expediente.getClienteNatural().getId() <= 0)){
			expediente = expedienteBean.buscarPorId(expediente.getId());
		}
		
		if(expediente!=null && expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getSubproducto()!=null)
		   LOG.info("init Subproducto : "+expediente.getExpedienteTC().getSubproducto().getId());
		
		
		renderedScoring = false;
		renderedTasaEspecial = false;
		renderedCantCred=false;
		disabledSolicitudTasaEsp=false;
		
		List<Producto> listTempProducto=productobean.buscarTodos();
		listaProducto=new ArrayList<SelectItem>();
		codProducto = (String) getObjectSession(Constantes.COD_PRODUCTO_SESION);
		
		if(codProducto==null || codProducto.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			codProducto=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			listaProducto.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO));
			listaSubproducto = Util.listaVacia ();
			listaSubproducto = Util.setEtiqueta(listaSubproducto, "-1", Constantes.DEFECTO_COMBO);
			codSubproducto = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			
		}else{
			cambiarSubProductos(null);
		}
		
		if(listTempProducto.size()>0)
			for(Producto value : listTempProducto){
				listaProducto.add(new SelectItem(value.getCodigo(),value.getDescripcion()));
			}
		
		LOG.info("valor codProducto = "+codProducto);
		
		LOG.info("valor codSubproducto = "+codSubproducto);
		
		if(listaSubproducto!=null && listaSubproducto.size()>0){
			LOG.info("listaSubproducto = "+listaSubproducto.size());
		}else
			LOG.info("listaSubproducto es nulo o vacio");	
		
		//listaProducto = Util.crearItems(listTempProducto, true,
			//	"id", "descripcion");

		
		List<TipoOferta> listTempTipoOferta= tipoOfertaobean.buscarTodos();
		listaTipoOferta=new ArrayList<SelectItem>();
		
		if(codTipoOferta!=null && !codTipoOferta.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			LOG.info("codTipoOferta="+codTipoOferta);
		}else{
			listaTipoOferta.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO));			
		}
		
		
		if(listTempTipoOferta.size()>0)
		for(TipoOferta value : listTempTipoOferta){
			listaTipoOferta.add(new SelectItem(value.getCodigo(),value.getDescripcion()));
		}
	
		
		listaTipoMoneda = Util.crearItems(tipoMonedabean.buscarTodos(), true,
				"id", "descripcion");
		
		listaTipoEnvio = Util.crearItems(tipoEnviobean.buscarTodos(), true,
				"id", "descripcion");
		
		//listaGarantia=Util.crearItems(garantiabean.buscarTodos(), true,
			//	"id", "descripcion");		
		
		if(expediente!=null && expediente.getExpedienteTC()!=null)
			expediente.getExpedienteTC().setFlagModifScore(expediente.getExpedienteTC().getFlagModifScore()==null?Constantes.CHECK_NO_SELECCIONADO : expediente.getExpedienteTC().getFlagModifScore());
				
		String jspPrinc = getNombreJSPPrincipal();
		
		if (jspPrinc.equals("formEvaluarDevolucionRiesgos") ||
				jspPrinc.equals("formConsultarClienteModificaciones") ||
				jspPrinc.equals("formGestionarSubsanarOperacion")) {
			renderedScoring = true;
			renderedSolicitudTasaEsp=false;
		}

		if (jspPrinc.equals("formEvaluarDevolucionRiesgos") ||
				jspPrinc.equals("formConsultarClienteModificaciones") ||
				jspPrinc.equals("formRegularizarEscanearDocumentacion") ) {						
			renderedProducto = true;
			renderedSubProducto = true;
			renderedTipoOferta = true;
			renderedSolicitudTasaEsp=false;
		
			String idEstadoCivil = String.valueOf(expediente.getClienteNatural().getEstadoCivil().getId());			
			if (!isCasado(idEstadoCivil)) {
				renderedConyuge=true;
			}				
			validaPautaClasificacion(idEstadoCivil);
		}
		
		
		
		if (jspPrinc.equals("formCoordinarClienteSubsanar") ) {						
			renderedProducto = true;
			renderedSubProducto = true;
			renderedTipoOferta = true;
			renderedLineaCredTipMoneda = true;
			renderedPreEvaluador=true;
			renderedConyuge=true;
			renderedTitular=true;
			renderedCantCred=true;
		}
		
		copiarDeExpediente();
		
		iniciar ();
		
		visualizarSbs();
		
		/*if(codGarantia==null){
			setTipoGarantiasDefecto();
		}*/		
		
		if (jspPrinc.equals("formRegistrarExpediente")) {
			if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getEstadoCivil()!=null) {
				String idEstadoCivil = String.valueOf(expediente.getClienteNatural().getEstadoCivil().getId());			
				if (!isCasado(idEstadoCivil)) {
					renderedConyuge=true;
				}
				validaPautaClasificacion(idEstadoCivil);
			}
			
			renderedLineaCreditoApr=false;
		}else{
			//productoNuevo.expediente.expedienteTC.lineaCredAprob
			LOG.info("Linea Cred Aprobado=="+expediente.getExpedienteTC().getLineaCredAprob());
			validarVisualizacionDatos(expediente);

		}
		
		if(codTipoMoneda==null && listaTipoMoneda != null){
			for(SelectItem item : listaTipoMoneda) {
				if(item.getValue().toString().equals(Constantes.TIPO_MONEDA_SOLES)) {
					codTipoMoneda = Constantes.TIPO_MONEDA_SOLES;
					break;
				}
			}
		}		
		LOG.info("renderedClasificacionSbs:" + renderedClasificacionSbs);		
		
		if (jspPrinc.equals("formEvaluarDevolucionRiesgos") ||
				jspPrinc.equals("formConsultarClienteModificaciones")) {
			this.setModificacionScoring(false);
		}
		
		LOG.info("codProducto -> "+codProducto);
		LOG.info("codSubproducto -> "+codSubproducto);
		LOG.info("ENTRO ProductoNuevo");
		
		LOG.info("Init - renderedSoltero:::"+renderedSoltero);
		LOG.info("Init - renderedConyuge :"+renderedConyuge);
	}
	
	private void validarVisualizacionDatos(Expediente expediente) {
		
		if (expediente.getExpedienteTC().getLineaCredAprob()>0 || expediente.getExpedienteTC().getLineaCredAprob()>0.0) {
			renderedLineaCreditoApr = true;
		} else {
			renderedLineaCreditoApr = false;
		}
		LOG.info("valor de renderedPlazoSolicAprob despues de ObtenerDatos::"+renderedPlazoSolicitadoApr);
		if(renderedPlazoSolicitadoApr)
			if (expediente.getExpedienteTC().getPlazoSolicitadoApr()==null || expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals("")) {
				renderedPlazoSolicitadoApr = false;
			} else {
				renderedPlazoSolicitadoApr = true;
			}
	}
	
	
	public void validaPautaClasificacionSBS(AjaxBehaviorEvent event) {	
		LOG.info("validaPautaClasificacionSBS");
		String idEstadoCivil = this.retornaEstadoCivil();		
		validaPautaClasificacion(idEstadoCivil);
	}	
	
	public void validaPautaClasificacion(String idEstadoCivil) {
		LOG.info("validaPautaClasificacion : - EstadoCivil "+idEstadoCivil);
		/*COMPLETAR LA LOGICA CON LA LLAMADA DE LOS SERVICIOS*/
		
		String jspPrinc = getNombreJSPPrincipal();

		LOG.info("codTipoOferta : "+codTipoOferta);
		
		if (!isCasado(idEstadoCivil)) {
		    this.setRenderedConyuge(true);	
		}
		LOG.info("RenderedConyuge 1 : "+this.isRenderedConyuge());
		if (codTipoOferta!=null && codTipoOferta.equals(Constantes.CODIGO_OFERTA_APROBADO)) {
			
			/*Ini Completar codigo*/
			     //Titular : Llama al Servicio “Extraer calificación Central de Riesgos”
			/*Fin Completar codigo*/
	
			String formulario = "";
			if (jspPrinc.equals("formEvaluarDevolucionRiesgos")) {
				formulario = "frmEvaluarDevolucionRiesgos";
			} else if (jspPrinc.equals("formConsultarClienteModificaciones")) {
				formulario = "frmConsultarClienteModificaciones";
			} else if (jspPrinc.equals("formRegularizarEscanearDocumentacion")) {
				formulario = "frmRegularizarEscanearDocumentacion";
			} else if (jspPrinc.equals("formRegistrarExpediente")) {
				formulario = "frmRegistrarExpediente";
			}
			
			/*Ini Completar codigo*/
			
			/*Si encontro los valores en central de riesgos se setea
			    expediente.getExpedienteTC().setClasificacionSbs(0);*/
		   
			/*Si No encontro los valores en central de riesgos se emite mensaje
				addMessageError(formulario + ":clasificacionSbs", 
						"com.ibm.bbva.common.productoNuevo.msg.serv.titular");*/
			/*Fin Completar codigo*/

			if (isCasado(idEstadoCivil)) {
				LOG.info("CASADO ");
				/*Ini Completar codigo*/
				
				//Conyuge : Llama al Servicio “Extraer calificación Central de Riesgos”
				
				/*Si encontro los valores en central de riesgos se setea
			    expediente.getExpedienteTC().setSbsConyuge(0);*/
			    
                /*Si No encontro los valores en central de riesgos se emite mensaje
				addMessageError(formulario + ":sbsConyuge", 
						"com.ibm.bbva.common.productoNuevo.msg.serv.conyuge");*/
				/*Fin Completar codigo*/
			}else{
				 this.setRenderedConyuge(true);
			}
			LOG.info("RenderedConyuge 1 : "+this.isRenderedConyuge());
			boolean pautaClas=false;
			/*Ini Completar codigo*/
			
			if(codTipoOferta.equals(Constantes.CODIGO_OFERTA_APROBADO)){
				boolean ejecutar=false;
				if(isCasado(idEstadoCivil) && (expediente.getExpedienteTC().getClasificacionSbs() != 0 && expediente.getExpedienteTC().getSbsConyuge() != 0)) {
					ejecutar=true;
				}else if(!isCasado(idEstadoCivil) && expediente.getExpedienteTC().getClasificacionSbs() != 0 ){
					ejecutar=true;
				}else {
					ejecutar=false;
					pautaClas=true;
				}

				LOG.info("ejecutar : "+ejecutar);
				if(ejecutar==true){
					pautaClas = facade.ServiceIBMBBVA_pautasClasificacionMemoria(
								Long.valueOf(codTipoOferta), 
								Integer.valueOf(idEstadoCivil), 
								Long.valueOf(Constantes.CODIGO_TIPO_PERSONA_TITULAR), 
								(isCasado(idEstadoCivil)?Long.valueOf(Constantes.CODIGO_TIPO_PERSONA_CONYUGUE):0), 
								expediente.getExpedienteTC().getClasificacionSbs(), 
								expediente.getExpedienteTC().getSbsConyuge());
				}
				LOG.info("pautaClas : "+pautaClas);
				
				setRenderedClasificacionSbs(true);
				LOG.info("renderedClasificacionSbs: " + renderedClasificacionSbs);
				
			}else
				pautaClas=true;
			
/*			
			if(codTipoOferta.equals(Constantes.CODIGO_OFERTA_APROBADO)){
				pautaClas = facade.ServiceIBMBBVA_pautasClasificacionMemoria(
						Long.valueOf(codTipoOferta), 
						Integer.valueOf(idEstadoCivil), 
						Long.valueOf(Constantes.CODIGO_TIPO_PERSONA_TITULAR), 
						(isCasado(idEstadoCivil)?Long.valueOf(Constantes.CODIGO_TIPO_PERSONA_CONYUGUE):0), 
						expediente.getExpedienteTC().getClasificacionSbs(), 
						expediente.getExpedienteTC().getSbsConyuge());				
			}else
				pautaClas=true;
*/
			/*Fin Completar codigo*/
			
			habilitaBoton(pautaClas);
			setRenderedMsgPauta(pautaClas);
			
//			if (!pautaClas) {
//			     addMessageError(formulario + ":idMsgPautaClasificacion", 
//					"com.ibm.bbva.common.productoNuevo.msg.serv.clasificacionSbs");
//			}
			
			LOG.info("RenderedConyuge : "+this.isRenderedConyuge());
		}
	}
	
	private void copiarAExpediente() {
		LOG.info("COPIAR A EXPEDIENTE");
		lineaCredSol = Double.toString(expediente.getExpedienteTC().getLineaCredSol());
		/* Copia string formateados a campos numericos */
		expediente.getExpedienteTC().setLineaCredSol(Util.convertStringToDouble(Util.esNuloVacio(lineaCredSol) ? "0" : lineaCredSol));
	}
	
	private void copiarDeExpediente() {
		/* Mostrar string datos numericos formateados */
		if(expediente!=null && expediente.getExpedienteTC()!=null){
				LOG.info("expediente no es nulo");
				setLineaCredSol(Double.valueOf(expediente.getExpedienteTC().getLineaCredSol())!=0?Util.convertDoubleToString(expediente.getExpedienteTC().getLineaCredSol(),Constantes.FORMATO_DECIMAL_MILLON):"");
		}
		else{
			LOG.info("expediente es nulo");
			setLineaCredSol("");
		}
			
	}
	
	public List<SelectItem> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<SelectItem> listaProducto) {
		this.listaProducto = listaProducto;
	}

	public String getCodProducto() {
		return codProducto;
	}

	public void setCodProducto(String codProducto) {
		this.codProducto = codProducto;
	}

	public List<SelectItem> getListaTipoOferta() {
		return listaTipoOferta;
	}

	public void setListaTipoOferta(List<SelectItem> listaTipoOferta) {
		this.listaTipoOferta = listaTipoOferta;
	}

	public String getCodTipoOferta() {
		return codTipoOferta;
	}

	public void setCodTipoOferta(String codTipoOferta) {
		this.codTipoOferta = codTipoOferta;
	}

	public List<SelectItem> getListaTipoMoneda() {
		return listaTipoMoneda;
	}

	public void setListaTipoMoneda(List<SelectItem> listaTipoMoneda) {
		this.listaTipoMoneda = listaTipoMoneda;
	}

	public String getCodTipoMoneda() {
		return codTipoMoneda;
	}

	public void setCodTipoMoneda(String codTipoMoneda) {
		this.codTipoMoneda = codTipoMoneda;
	}

	public List<SelectItem> getListaTipoEnvio() {
		return listaTipoEnvio;
	}

	public void setListaTipoEnvio(List<SelectItem> listaTipoEnvio) {
		this.listaTipoEnvio = listaTipoEnvio;
	}

	public String getCodTipoEnvio() {
		return codTipoEnvio;
	}

	public void setCodTipoEnvio(String codTipoEnvio) {
		this.codTipoEnvio = codTipoEnvio;
	}

	public List<SelectItem> getListaSubproducto() {
		return listaSubproducto;
	}

	public void setListaSubproducto(List<SelectItem> listaSubproducto) {
		this.listaSubproducto = listaSubproducto;
	}

	public String getCodSubproducto() {
		return codSubproducto;
	}

	public void setCodSubproducto(String codSubproducto) {
		this.codSubproducto = codSubproducto;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}
	
	public boolean isRenderedScoring() {
		return renderedScoring;
	}

	public void setRenderedScoring(boolean renderedScoring) {
		this.renderedScoring = renderedScoring;
	}

	public boolean isTasaEspecial() {
		return tasaEspecial;
	}

	public void setTasaEspecial(boolean tasaEspecial) {
		this.tasaEspecial = tasaEspecial;
	}

	public boolean isModificacionScoring() {
		return modificacionScoring;
	}

	public void setModificacionScoring(boolean modificacionScoring) {
		this.modificacionScoring = modificacionScoring;
	}
	
	public void cambiarTasaEspecial(AjaxBehaviorEvent event) {
		LOG.info("cambiarTasaEspecial");
		FacesContext ctx = FacesContext.getCurrentInstance();		
		PanelDocumentosMB panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}

	public void cambiarSubProductos(AjaxBehaviorEvent event) {
		LOG.info("cambiarSubProductos");
		/*Object codigo = vce.getNewValue();
		codProducto = (String)codigo;*/
		FacesContext ctx = FacesContext.getCurrentInstance();  
		
		this.setCodSubproducto(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
		codGarantia = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		//codTipoOferta= Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		
		expediente.getExpedienteTC().setClasificacionSbs(000.0);
		expediente.getExpedienteTC().setSbsConyuge(000.0);
		expediente.getExpedienteTC().setTasaEsp(000.0);
		tasaEspecial=false;
		
		if (expediente.getExpedienteTC()==null){
			expediente.setExpedienteTC(new ExpedienteTC());
		}
			
		if(expediente.getProducto()==null){
			expediente.setProducto(new Producto());
		}
		
		if (expediente.getExpedienteTC().getSubproducto()==null){
			expediente.getExpedienteTC().setSubproducto(new Subproducto());
		}
		
		if(codProducto!=null && !codProducto.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			addObjectSession(Constantes.COD_PRODUCTO_SESION, codProducto);
			LOG.info("entro en comparacion codProducto = "+codProducto);
			expediente.getProducto().setId(Integer.parseInt(codProducto));
			expediente.getExpedienteTC().getSubproducto().setId(Integer.parseInt(codSubproducto));
			crearListaSubProductos (Integer.parseInt(codProducto));
			
			campoProducto(Integer.parseInt(codProducto));
			/*Guia Documentaria*/
			if(event!=null){
				PanelDocumentosMB panelDocumentos = (PanelDocumentosMB)  
						 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
				
				panelDocumentos.cargarDocumentosPanel(event);					
			}
		
		}else{
			listaSubproducto=null;
			listaGarantia=null;
			LOG.info("no es igual");
		}
		
		//Seteamos el código de la garantia por defecto
		setTipoGarantiasDefecto();	
		//Deshabilito el checkBox cuando el producto seleccionado es tarjeta de crédito
		RegistrarExpedienteMB registrarExpediente = (RegistrarExpedienteMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente");
		
		if(Integer.parseInt(codProducto)==(Constantes.ID_APLICATIVO_PLD)){
			registrarExpediente.setItemDisabledSubrogacion(false);
			setDisabledInputTasaEsp(true);
		}else{
			removeCaracAdicionales("4");
			registrarExpediente.setItemDisabledSubrogacion(true);
			//this.setSubrogacionDeshabilitado(true);
		}
		
		//LOG.info("setSubrogacionDeshabilitado = "+isSubrogacionDeshabilitado());
		LOG.info("reg setSubrogacionDeshabilitado = "+registrarExpediente.isItemDisabledSubrogacion());
		
		/*Busqueda Conyuge*/
		
		BuscarConyugeMB buscarConyugeMB = (BuscarConyugeMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarConyuge");
		
		buscarConyugeMB.setNumeroDOI("");		
	//	buscarConyugeMB.setEstBuscar(false);
		
		/*Datos Conyuge*/
		
		DatosConyugeMB datosConyuge = (DatosConyugeMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosConyuge");
		
		datosConyuge.setMostrarDatosConyugePrincipal(false);
	}
	
	
	private String removeCaracAdicionales (String val) {
		FacesContext ctx = FacesContext.getCurrentInstance(); 
		
		DatosClienteNuevoMB datosCliente = (DatosClienteNuevoMB)  
		ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
		
		//return caracAdicionales.contains(val) ? 
			//	Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO;
		LOG.info("caracAdicionales "+datosCliente.caracAdicionales.contains(val));
		
		if(datosCliente.caracAdicionales.contains(val))
			datosCliente.caracAdicionales.remove(val);
		
		return null;
	}
	
	private void crearListaSubProductos (long idProd) {	
		listaSubproducto = Util.crearItems(subproductobean.buscarPorIdProducto(idProd), true,
				"id", "descripcion");
		
		listaGarantia=Util.crearItems(garantiabean.buscarPorIdProducto(idProd), true,
				"id", "descripcion");
		
		if(listaSubproducto!=null && listaSubproducto.size()>0){
			listaSubproducto = Util.setEtiqueta(listaSubproducto, "-1", Constantes.DEFECTO_COMBO);
			this.setCodSubproducto((String)listaSubproducto.get(0).getValue());
			LOG.info("crearListaSubProductos codSubproducto = "+this.getCodSubproducto());
		}
		
		if(listaGarantia!=null && listaGarantia.size()>0){
			codGarantia=(String)listaGarantia.get(0).getValue();
			LOG.info("crearlistaGarantia codGarantia = "+codGarantia);
		}		
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
				if(value.getEtiqueta().equals(Constantes.CAMPO_PLAZO_SOLICITADO_APR))
					setRenderedPlazoSolicitadoApr(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);				
				if(value.getEtiqueta().equals(Constantes.CAMPO_SOL_TASA_ESP))
					setRenderedSolicitudTasaEsp(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);	
				if(value.getEtiqueta().equals(Constantes.CAMPO_GARANTIA))
					setRenderedGarantia(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);					
				if(value.getEtiqueta().equals(Constantes.CAMPO_DPS))
					solVerificarAprobar.setRenderedDps(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
			}
		}
	}
	
	
	
	public boolean esValido () {		
		LOG.info("Verificanco si es valido ProductoNuevo");
		copiarAExpediente();
		
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		if (jspPrinc.equals("formRegistrarExpediente")) {
			formulario = "frmRegistrarExpediente";
		} else if (jspPrinc.equals("formCoordinarClienteSubsanar")) {
			formulario = "frmCoordinarClienteSubsanar";
		} else if (jspPrinc.equals("formEvaluarDevolucionRiesgos")) {
			formulario = "frmEvaluarDevolucionRiesgos";
		} else if (jspPrinc.equals("formRegistrarExpedienteCu25")) {
			formulario = "frmRegistrarExpedienteCu25";
		} else if (jspPrinc.equals("formConsultarClienteModificaciones")) {
			formulario = "frmConsultarClienteModificaciones";
		} else if (jspPrinc.equals("formRegularizarEscanearDocumentacion")) {
			formulario = "frmRegularizarEscanearDocumentacion";
		}else if (jspPrinc.equals("formRegistrarExpedienteCu23")) {
			formulario = "frmRegistrarExpedienteCu23";
		}

		boolean existeError = false;
		if (!esValidoSelect(codProducto)) {
			addMessageError(formulario + ":selProducto", 
					"com.ibm.bbva.common.productoNuevo.msg.producto");
			existeError = true;
		}
		if (!esValidoSelect(this.getCodSubproducto())) {
			addMessageError(formulario + ":selSubproducto", 
					"com.ibm.bbva.common.productoNuevo.msg.subproducto");
			existeError = true;
		} 
		LOG.info("envio tarjeta = "+isRenderedEnvioTarjeta());
		if (!esValidoSelect(codTipoEnvio) && isRenderedEnvioTarjeta()) {
			addMessageError(formulario + ":selTipoEnvio", 
					"com.ibm.bbva.common.productoNuevo.msg.tipoEnvio");
			existeError = true;
			LOG.info("entro 0 ");
		} 
		if (!esValidoSelect(codTipoMoneda)) {
			addMessageError(formulario + ":selTipoMoneda", 
					"com.ibm.bbva.common.productoNuevo.msg.tipoMoneda");
			existeError = true;
		} 
		if (!esValidoSelect(codTipoOferta)) {
			addMessageError(formulario + ":selTipoOferta", 
					"com.ibm.bbva.common.productoNuevo.msg.tipoOferta");
			existeError = true;
		}
		LOG.info("garantia = "+isRenderedGarantia());
		if (!esValidoSelect(codGarantia) && isRenderedGarantia()) {
			addMessageError(formulario + ":selGarantia", 
					"com.ibm.bbva.common.productoNuevo.msg.garantia");
			existeError = true;
			LOG.info("entro 1 ");
		}		
		Double linCred = expediente.getExpedienteTC().getLineaCredSol();
		if (linCred==null || linCred.doubleValue() <= 0) {
			if (renderedScoring) {
			addMessageError(formulario + ":lineaCredSol", 
					"com.ibm.bbva.common.productoNuevo.msg.lineaCredito");
			}else{
				addMessageError(formulario + ":lineaCredSolInp", 
						"com.ibm.bbva.common.productoNuevo.msg.lineaCredito");
			}
			existeError = true;
		}
		String codPreEval = expediente.getExpedienteTC().getCodPreEval();
		if (codPreEval==null || codPreEval.trim().equals("") || codPreEval.trim().length() < 4 || codPreEval.trim().length() > 14) {
			addMessageError(formulario + ":codPreEval", 
					"com.ibm.bbva.common.productoNuevo.msg.codPreEval");
			existeError = true;
		}
		
		LOG.info("Plazo solicitado = "+isRenderedPlazoSolicitado());
		String plazoSol = (expediente.getExpedienteTC().getPlazoSolicitado() != null && expediente.getExpedienteTC().getPlazoSolicitado().trim().length()>0)
				?expediente.getExpedienteTC().getPlazoSolicitado().trim():"";
		if (isRenderedPlazoSolicitado() && (plazoSol==null || plazoSol.trim().equals("") || plazoSol.trim().length()<1)) {
			addMessageError(formulario + ":idPlazoSol", 
					"com.ibm.bbva.common.productoNuevo.msg.plazoSolicitado");
			existeError = true;
			LOG.info("entro 2= ");
		}
		
		LOG.info("RenderedSolicitudTasaEsp = "+isRenderedSolicitudTasaEsp());
		LOG.info("tasaEspecial = "+tasaEspecial);
		if (isRenderedSolicitudTasaEsp() && tasaEspecial && !disabledSolicitudTasaEsp) {
			if(expediente.getExpedienteTC().getTasaEsp()<=0){
				addMessageError(formulario + ":idValorTasaEsp", 
						"com.ibm.bbva.common.productoNuevo.msg.porcentajeTasaEspecial");
				existeError = true;
				LOG.info("entro 3= ");
			}
		}		
		
		if (codTipoOferta.equals(Constantes.CODIGO_OFERTA_APROBADO)) {
			Double clasificacionSbs = expediente.getExpedienteTC().getClasificacionSbs();
			if (clasificacionSbs==null || clasificacionSbs <=0 ) {
				addMessageError(formulario + ":clasificacionSbs", 
						"com.ibm.bbva.common.productoNuevo.msg.titular");
				existeError = true;
			}		
			
			String idEstadoCivil=retornaEstadoCivil();		

			if (isCasado(idEstadoCivil) || idEstadoCivil.equals(Constantes.ESTADO_CIVIL_CONVIVIENTE.toString())) {
				Double clasificacionConyuge = expediente.getExpedienteTC().getSbsConyuge();
								
				if (clasificacionConyuge==null || clasificacionConyuge <=0 ) {			
					addMessageError(formulario + ":sbsConyuge", 
							"com.ibm.bbva.common.productoNuevo.msg.conyuge");
					existeError = true;
					if(clasificacionConyuge!=null)
						LOG.info("clasificacionConyuge: "+clasificacionConyuge);
					else
						LOG.info("clasificacionConyuge es nulo");
				}
					LOG.info("clasificacionConyuge: "+clasificacionConyuge);
		    }else
		    	setRenderedConyuge(true);
			
			setRenderedClasificacionSbs(true);
		}
		return !existeError;
	}
	
	public boolean isCasado(String idEstadoCivil) {
		if (idEstadoCivil == null
			|| idEstadoCivil.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)
			|| !(idEstadoCivil.equals(Constantes.ESTADO_CIVIL_CASADO.toString())
			|| idEstadoCivil.equals(Constantes.ESTADO_CIVIL_CONVIVIENTE.toString()))) {
			return false;
		}
		return true;
	}
	
	public boolean isSoltero(String idEstadoCivil) {
		if (!idEstadoCivil.equals(Constantes.ESTADO_CIVIL_SOLTERO.toString())) {
			return false;
		}
		return true;
	}
	
	private String retornaEstadoCivil() {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		DetalleExpediente1MB detalleExpediente1 = null;
		DatosClienteMB datosCliente = null;
		RegistrarExpedienteMB regExpMB = null;
		
		String jspPrinc = getNombreJSPPrincipal();
		String idEstadoCivil=null;
		
		if (jspPrinc.equals("formRegistrarExpediente")) {
		
			regExpMB = (RegistrarExpedienteMB)  
					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente");
			
			if (regExpMB.isEditarCliente()) {
				datosCliente = (DatosClienteMB)  
						ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
				
			} else {
				datosCliente = (DatosClienteMB)  
						ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosCliente");
			}
			
			idEstadoCivil = datosCliente.getIdEstadoCivil();
			
		} else if (jspPrinc.equals("formEvaluarDevolucionRiesgos") || 
				jspPrinc.equals("formCoordinarClienteSubsanar") ||
				jspPrinc.equals("formConsultarClienteModificaciones") ||
				jspPrinc.equals("formRegularizarEscanearDocumentacion")) {
		
			detalleExpediente1 = (DetalleExpediente1MB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "detalleExpediente1");
			
			idEstadoCivil = detalleExpediente1.getIdEstadoCivil();
		}
		return idEstadoCivil;
	}
	
	private void habilitaBoton(boolean activo) {
		FacesContext ctx = FacesContext.getCurrentInstance();		
		String jspPrinc = getNombreJSPPrincipal();
				
		if (jspPrinc.equals("formEvaluarDevolucionRiesgos")) {
			EvaluarDevolucionRiesgosMB evaluarDevolucionRiesgos = (EvaluarDevolucionRiesgosMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "evaluarDevolucionRiesgos");
			evaluarDevolucionRiesgos.setActivoAceptar(activo);
		}else if (jspPrinc.equals("formRegistrarExpediente")) {
			RegistrarExpedienteMB registrarExpediente = (RegistrarExpedienteMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente");
			registrarExpediente.setActivoAceptar(activo);
		}else if (jspPrinc.equals("formConsultarClienteModificaciones")) {
			ConsultarClienteModificacionesMB consultarClienteModificaciones = (ConsultarClienteModificacionesMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "consultarClienteModificaciones");
			consultarClienteModificaciones.setActivoAceptar(activo);
		}else if (jspPrinc.equals("formRegularizarEscanearDocumentacion")) {
			RegularizarEscanearDocumentacionMB regularizarEscanearDocumentacion = (RegularizarEscanearDocumentacionMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "regularizarEscanearDocumentacion");
			regularizarEscanearDocumentacion.setActivoAceptar(activo);
		}
	}	
	
	private boolean esValidoSelect (String seleccionado) {
		if (seleccionado==null || seleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) || 
				seleccionado.trim().equals("")) {
			return false;
		}
		return true;
	}
	
	// copia los datos al expediente de la sesion
	public void copiarDatosExpediente () {
		FacesContext ctx = FacesContext.getCurrentInstance();	
		String jspPrinc = getNombreJSPPrincipal();
		DatosClienteNuevoMB datosClienteNuevo =null;
		LOG.info("tasa Especial :"+expediente.getExpedienteTC().getTasaEsp());
		if (jspPrinc.equals("formRegistrarExpediente")) {
			datosClienteNuevo = (DatosClienteNuevoMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
			
		}
		LOG.info("codProducto -> "+codProducto);
		if (esValidoSelect (codProducto)) {			
			expediente.setProducto(productobean.buscarPorId(Long.parseLong(codProducto)));
		}else{
			expediente.setProducto(null);
		}
		LOG.info("codSubproducto -> "+this.getCodSubproducto());
		if (esValidoSelect (this.getCodSubproducto())) {	
			if(expediente.getExpedienteTC()!=null){
				LOG.info("getExpedienteTC no null ");
			}else
				LOG.info("getExpedienteTC null ");
			expediente.getExpedienteTC().setSubproducto(subproductobean.buscarPorId(Long.parseLong(this.getCodSubproducto())));
		}else{
			expediente.getExpedienteTC().setSubproducto(null);
		}
		if (esValidoSelect (codTipoOferta)) {			
			expediente.getExpedienteTC().setTipoOferta(tipoOfertaobean.buscarPorId(Long.parseLong(codTipoOferta)));
		}else{
			expediente.getExpedienteTC().setTipoOferta(null);
		}
		if (esValidoSelect (codTipoMoneda)) {			
			expediente.getExpedienteTC().setTipoMonedaSol(tipoMonedabean.buscarPorId(Long.parseLong(codTipoMoneda)));
		}else{
			expediente.getExpedienteTC().setTipoMonedaSol(null);
		}
		if (esValidoSelect (codTipoEnvio)) {			
			expediente.getExpedienteTC().setTipoEnvio(tipoEnviobean.buscarPorId(Long.parseLong(codTipoEnvio)));
		}else{
			expediente.getExpedienteTC().setTipoEnvio(null);
		}
		if (esValidoSelect (codGarantia)) {
			LOG.info("codGarantia="+codGarantia);
			expediente.getExpedienteTC().setGarantia(garantiabean.buscarPorId(Long.parseLong(codGarantia)));
		}else{
			expediente.getExpedienteTC().setGarantia(null);
		}		
		expediente.getExpedienteTC().setFlagSolTasaEsp(tasaEspecial ? Constantes.CHECK_SELECCIONADO : 
			Constantes.CHECK_NO_SELECCIONADO);
		
		if (isRenderedSolicitudTasaEsp() && !tasaEspecial && !disabledSolicitudTasaEsp) {
			expediente.getExpedienteTC().setTasaEsp(0);
		}
		
		if (renderedScoring) {
			expediente.getExpedienteTC().setFlagModifScore(modificacionScoring ? Constantes.CHECK_SELECCIONADO : 
					Constantes.CHECK_NO_SELECCIONADO);
		}
		
		copiarAExpediente();
	    
		String idEstadoCivil = null;

		if (expediente.getClienteNatural()!=null &&
				expediente.getClienteNatural().getEstadoCivil()!=null) {	    		
			idEstadoCivil = String.valueOf(expediente.getClienteNatural().getEstadoCivil()==null?0:expediente.getClienteNatural().getEstadoCivil().getId());
			
			LOG.info("idEstadoCivil : "+idEstadoCivil);
			if (!isCasado(idEstadoCivil) && idEstadoCivil!=null) {
				LOG.info("si entro : ");
				expediente.getExpedienteTC().setClienteNaturalConyuge(null);
				expediente.getExpedienteTC().setBancoConyuge(null);
				expediente.getExpedienteTC().setSbsConyuge(0);
			}
				LOG.info("No entro : ");
				
	    }

	}
	
	// inicia los datos despues de obtener el expediente de la sesion
	private void iniciar () {
		LOG.info("iniciar this.codSubproducto : "+this.getCodSubproducto());
		if (expediente!=null && expediente.getProducto() != null && expediente.getProducto().getId() > 0 && 
				(this.getCodSubproducto()==null || this.getCodSubproducto().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
			LOG.info("iniciar expediente existe");
			LOG.info("codProducto : "+codProducto);
			codProducto = String.valueOf(expediente.getProducto().getId());
			crearListaSubProductos (expediente.getProducto().getId());
			campoProducto(Long.parseLong(codProducto));
		}
		/*else{
			LOG.info("iniciar expediente no existe");
			//this.setCodProducto(String.valueOf(Constantes.CODIGO_CODIGO_CAMPO_VACIO));
			//crearListaSubProductos(Constantes.CODIGO_ID_PRODUCTO);
			//listaSubproducto=null;
		}*/
		
		if(listaSubproducto!=null && listaSubproducto.size()>0){
			LOG.info("listaSubproducto = "+listaSubproducto.size());
		}else
			LOG.info("listaSubproducto es nulo o vacio");
		
		
		if (expediente!=null && expediente.getExpedienteTC() != null) {
			if (expediente.getExpedienteTC().getSubproducto() != null
					&& expediente.getExpedienteTC().getSubproducto().getId() > 0) {
				this.setCodSubproducto(String.valueOf(expediente.getExpedienteTC().getSubproducto().getId()));
				LOG.info("codSubproducto = "+this.getCodSubproducto());
			}
			if (expediente.getExpedienteTC().getTipoOferta() != null
					&& expediente.getExpedienteTC().getTipoOferta().getId() > 0) {
				codTipoOferta = String.valueOf(expediente.getExpedienteTC().getTipoOferta().getId());		
			}
			if (expediente.getExpedienteTC().getTipoMonedaSol() != null
					&& expediente.getExpedienteTC().getTipoMonedaSol().getId() > 0) {
				codTipoMoneda = String.valueOf(expediente.getExpedienteTC().getTipoMonedaSol().getId());
				desTipoMoneda = String.valueOf(expediente.getExpedienteTC().getTipoMonedaSol().getDescripcion());
			}
			if (expediente.getExpedienteTC().getTipoEnvio() != null
					&& expediente.getExpedienteTC().getTipoEnvio().getId() > 0) {
				codTipoEnvio = String.valueOf(expediente.getExpedienteTC().getTipoEnvio().getId());
			}
			if (expediente.getExpedienteTC().getGarantia() != null
					&& expediente.getExpedienteTC().getGarantia().getId() > 0) {
				codGarantia = String.valueOf(expediente.getExpedienteTC().getGarantia().getId());
			}			
			tasaEspecial = Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getFlagSolTasaEsp());
			if (tasaEspecial){	
				setDisabledInputTasaEsp(false);}	
			else{ setDisabledInputTasaEsp(true);}
			if (renderedScoring) {
				modificacionScoring = Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getFlagModifScore());
			}
		}else{
			if(listaSubproducto!=null && listaSubproducto.size()>0){
				this.setCodSubproducto((String)listaSubproducto.get(0).getValue());
				LOG.info("codSubproducto = "+this.getCodSubproducto());
			}
			
			if(listaSubproducto!=null && listaSubproducto.size()>0){
				LOG.info("listaSubproducto 2= "+listaSubproducto.size());
			}else
				LOG.info("listaSubproducto 2 es nulo o vacio");
			
		}
		//Validamos si tiene permisos de solicitar tasa especial
		
			Empleado empleado = expediente.getExpedienteTC().getEmpleado();
			if(empleado != null && empleado.getPerfil() != null){
				if(empleado.getPerfil().getId() == Constantes.PERFIL_GESTOR_PLATAFORMA ||
				   empleado.getPerfil().getId() == Constantes.ID_PERFIL_CONTROLLER){
				   disabledSolicitudTasaEsp = false;
				}else{
				   disabledSolicitudTasaEsp = true;
				}
			}
		
	}
	
	public void cambiarTipoOferta(AjaxBehaviorEvent event) { 
		LOG.info("cambiarTipoOferta");
		FacesContext ctx = FacesContext.getCurrentInstance();  
		RegistrarExpedienteMB regExpMB = null;
		DatosClienteMB datosCliente = null;
		BuscarConyugeMB buscarConyugeMB=null;
		
		regExpMB = (RegistrarExpedienteMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente");
		
		if (regExpMB.isEditarCliente()) {
			datosCliente = (DatosClienteMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
			
		} else {
			datosCliente = (DatosClienteMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosCliente");
		}
		
		buscarConyugeMB = (BuscarConyugeMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarConyuge");
		
		
		buscarConyugeMB.setNumeroDOI("");
		
		if (!isCasado(datosCliente.getIdEstadoCivil())) {
			regExpMB.visualizarPanelConyuge(false);
			regExpMB.setMostrarPanelConyuge(false);
			buscarConyugeMB.setMostrarBuscarConyuge(false);
			this.setRenderedConyuge(true);	
		} else {
			if (getCodTipoOferta().equals(Constantes.CODIGO_OFERTA_APROBADO)) {
				regExpMB.visualizarPanelConyuge(true);
				buscarConyugeMB.setNumeroDOI("");
				regExpMB.setMostrarPanelConyuge(true);
				//buscarConyugeMB.setTipoDOISeleccionado(""); JBTA
				buscarConyugeMB.setTipoDOISeleccionado(Constantes.CODIGO_TIPO_DOI_DNI); //JBTA 
				buscarConyugeMB.setMostrarBuscarConyuge(true);
				this.setRenderedConyuge(false);
			}else{
				regExpMB.visualizarPanelConyuge(false);
				regExpMB.setMostrarPanelConyuge(false);
				//buscarConyugeMB.setMostrarBuscarConyuge(false);
			}
		}
		
		if (isSoltero(datosCliente.getIdEstadoCivil()))
			this.setRenderedSoltero(true);
		else
			this.setRenderedSoltero(false);
		
		//buscarConyugeMB.setEstBuscar(false);
		
		/*Guia Documentaria*/
		PanelDocumentosMB panelDocumentos = (PanelDocumentosMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
		panelDocumentos.cargarDocumentosPanel(event);
		
		/*Datos Conyuge*/
		
		DatosConyugeMB datosConyuge = (DatosConyugeMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosConyuge");
		
		datosConyuge.setMostrarDatosConyugePrincipal(false);
		if (!isCasado(datosCliente.getIdEstadoCivil())){
			regExpMB.visualizarDatosPanelConyuge(false);
			datosConyuge.setMostrarDatosConyuge(false);	
			datosConyuge.setMostrarDatosConyugePrincipal(false);			
		}else {
			if (getCodTipoOferta().equals(Constantes.CODIGO_OFERTA_APROBADO)) {	
				
				datosCliente = (DatosClienteMB)  
						ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
				
				//if(!buscarConyugeMB.isMostrarBuscarConyuge()){
					//datosConyuge.setMostrarDatosConyugePrincipal(false);
					//regExpMB.visualizarDatosPanelConyuge(false);					
				//}
			}else{
				datosConyuge.setMostrarDatosConyugePrincipal(false);
				regExpMB.visualizarDatosPanelConyuge(false);
			}
				
		}
		//Visualización SBS
		visualizarSbs();
		
		expediente.getExpedienteTC().setClasificacionSbs(000.0);
		expediente.getExpedienteTC().setSbsConyuge(000.0);
		
		
		//LOG.info("Producto mostrarPanelConyuge : "+regExpMB.isMostrarPanelConyuge());
		LOG.info("Producto buscarConyugeMB : "+buscarConyugeMB.isMostrarBuscarConyuge());
		LOG.info("Producto isMostrarDatosConyugePrincipal : "+datosConyuge.isMostrarDatosConyugePrincipal());
	
	}
	
	public void cambiarLineaCreditoSol(AjaxBehaviorEvent event) {		
		double lineaCreditoSolOriginal = expedientebean.buscarPorId(expediente.getId()).getExpedienteTC().getLineaCredSol();
		
		if (lineaCreditoSolOriginal != expediente.getExpedienteTC().getLineaCredSol()) {
			this.setModificacionScoring(true);
		}else{
			this.setModificacionScoring(false);
		}	
	}
	
	public void cambiarTipoMoneda(AjaxBehaviorEvent event) {
		LOG.info("cambiarTipoMoneda");
		if (this.getCodSubproducto()!=null || this.getCodSubproducto().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			LOG.info(this.getCodSubproducto());
			Subproducto subProducto = subproductobean.buscarPorId(Long.parseLong(this.getCodSubproducto()));
			if(subProducto != null){
				LOG.info("Sub Producto seleccionado es " + subProducto.getDescripcion());
			    String tipoMoneda = String.valueOf(subProducto.getTipoMoneda().getId());
			    this.setCodTipoMoneda(tipoMoneda);
			    expediente.getExpedienteTC().setSubproducto(subProducto);
			    TipoMoneda tipoMonedaSol = new TipoMoneda();
			    tipoMonedaSol.setId(Long.parseLong(tipoMoneda));
			    expediente.getExpedienteTC().setTipoMonedaSol(tipoMonedaSol);
			}
		}
	}
	
	public void cambiarTipoMonedaSubProd(AjaxBehaviorEvent event) {
		LOG.info("cambiarTipoMoneda");
		if (this.getCodSubproducto()!=null || this.getCodSubproducto().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			LOG.info(this.getCodSubproducto());
			Subproducto subProducto = subproductobean.buscarPorId(Long.parseLong(this.getCodSubproducto()));
			if(subProducto != null){
				LOG.info("Sub Producto seleccionado es " + subProducto.getDescripcion());
			    String tipoMoneda = String.valueOf(subProducto.getTipoMoneda().getId());
			    this.setCodTipoMoneda(tipoMoneda);
			    expediente.getExpedienteTC().setSubproducto(subProducto);
			    TipoMoneda tipoMonedaSol = new TipoMoneda();
			    tipoMonedaSol.setId(Long.parseLong(tipoMoneda));
			    expediente.getExpedienteTC().setTipoMonedaSol(tipoMonedaSol);
			    
			    /*Guia Documentaria*/
			    FacesContext ctx = FacesContext.getCurrentInstance(); 
			    
				PanelDocumentosMB panelDocumentos = (PanelDocumentosMB)  
						 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
				panelDocumentos.cargarDocumentosPanel(event);
			}
		}
	}
	
	public void visualizarSbs(){
		//Visualización SBS
		if(codTipoOferta != null){
			if(codTipoOferta.equals(Constantes.CODIGO_OFERTA_APROBADO)){
				setRenderedClasificacionSbs(true);
			}else{
				setRenderedClasificacionSbs(false);
			}
		}
		LOG.info("renderedClasificacionSbs: " + renderedClasificacionSbs);
	}
	public void setTipoGarantiasDefecto() {
		if(codProducto != null) {
			List<Garantia> listGarantia = garantiabean.buscarPorFlagSinGarantia(Long.parseLong(codProducto) , "1");
			if(listGarantia.size()>0) {
				codGarantia = String.valueOf(listGarantia.get(0).getId());
			}
		}
	}
	
	public String getLineaCredSol() {
		return lineaCredSol;
	}

	public void setLineaCredSol(String lineaCredSol) {
		this.lineaCredSol = lineaCredSol;
	}
	public boolean isRenderedTasaEspecial() {
		return renderedTasaEspecial;
	}

	public void setRenderedTasaEspecial(boolean renderedTasaEspecial) {
		this.renderedTasaEspecial = renderedTasaEspecial;
	}

	public boolean isRenderedProducto() {
		return renderedProducto;
	}

	public void setRenderedProducto(boolean renderedProducto) {
		this.renderedProducto = renderedProducto;
	}

	public boolean isRenderedSubProducto() {
		return renderedSubProducto;
	}

	public void setRenderedSubProducto(boolean renderedSubProducto) {
		this.renderedSubProducto = renderedSubProducto;
	}

	public boolean isRenderedTipoOferta() {
		return renderedTipoOferta;
	}

	public void setRenderedTipoOferta(boolean renderedTipoOferta) {
		this.renderedTipoOferta = renderedTipoOferta;
	}

	public boolean isRenderedLineaCredTipMoneda() {
		return renderedLineaCredTipMoneda;
	}

	public void setRenderedLineaCredTipMoneda(boolean renderedLineaCredTipMoneda) {
		this.renderedLineaCredTipMoneda = renderedLineaCredTipMoneda;
	}

	public boolean isRenderedPreEvaluador() {
		return renderedPreEvaluador;
	}

	public void setRenderedPreEvaluador(boolean renderedPreEvaluador) {
		this.renderedPreEvaluador = renderedPreEvaluador;
	}

	public String getDesTipoMoneda() {
		return desTipoMoneda;
	}

	public void setDesTipoMoneda(String desTipoMoneda) {
		this.desTipoMoneda = desTipoMoneda;
	}

	public boolean isRenderedConyuge() {
		return renderedConyuge;
	}

	public void setRenderedConyuge(boolean renderedConyuge) {
		this.renderedConyuge = renderedConyuge;
	}

	public boolean isRenderedTitular() {
		return renderedTitular;
	}

	public void setRenderedTitular(boolean renderedTitular) {
		this.renderedTitular = renderedTitular;
	}

	public boolean isRenderedCantCred() {
		return renderedCantCred;
	}

	public void setRenderedCantCred(boolean renderedCantCred) {
		this.renderedCantCred = renderedCantCred;
	}

	public boolean isRenderedMsgPauta() {
		return renderedMsgPauta;
	}

	public void setRenderedMsgPauta(boolean renderedMsgPauta) {
		this.renderedMsgPauta = renderedMsgPauta;
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

	public List<SelectItem> getListaGarantia() {
		return listaGarantia;
	}

	public void setListaGarantia(List<SelectItem> listaGarantia) {
		this.listaGarantia = listaGarantia;
	}

	public String getCodGarantia() {
		return codGarantia;
	}

	public void setCodGarantia(String codGarantia) {
		this.codGarantia = codGarantia;
	}

	public boolean isRenderedGarantia() {
		return renderedGarantia;
	}

	public void setRenderedGarantia(boolean renderedGarantia) {
		this.renderedGarantia = renderedGarantia;
	}

	public boolean isDisabledSolicitudTasaEsp() {
		return disabledSolicitudTasaEsp;
	}

	public void setDisabledSolicitudTasaEsp(boolean disabledSolicitudTasaEsp) {
		this.disabledSolicitudTasaEsp = disabledSolicitudTasaEsp;
	}
	
	public boolean isRenderedClasificacionSbs() {
		return renderedClasificacionSbs;
	}

	public void setRenderedClasificacionSbs(boolean renderedClasificacionSbs) {
		this.renderedClasificacionSbs = renderedClasificacionSbs;
	}

	public boolean isDisabledInputTasaEsp() {
		return disabledInputTasaEsp;
	}

	public void setDisabledInputTasaEsp(boolean disabledInputTasaEsp) {
		this.disabledInputTasaEsp = disabledInputTasaEsp;
	}

	public boolean isRenderedLineaCreditoApr() {
		return renderedLineaCreditoApr;
	}

	public void setRenderedLineaCreditoApr(boolean renderedLineaCreditoApr) {
		this.renderedLineaCreditoApr = renderedLineaCreditoApr;
	}

	public boolean isRenderedPlazoSolicitadoApr() {
		return renderedPlazoSolicitadoApr;
	}

	public void setRenderedPlazoSolicitadoApr(boolean renderedPlazoSolicitadoApr) {
		this.renderedPlazoSolicitadoApr = renderedPlazoSolicitadoApr;
	}

	public boolean isRenderedSoltero() {
		return renderedSoltero;
	}

	public void setRenderedSoltero(boolean renderedSoltero) {
		this.renderedSoltero = renderedSoltero;
	}
	
}

package com.ibm.bbva.controller.common;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.ClasifBanco;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.ProductoEtiqueta;
import com.ibm.bbva.entities.ProductoTarea;
import com.ibm.bbva.entities.TipoBuro;
import com.ibm.bbva.entities.TipoScoring;
import com.ibm.bbva.session.ClasifBancoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.MensajesBeanLocal;
import com.ibm.bbva.session.ProductoEtiquetaBeanLocal;
import com.ibm.bbva.session.ProductoTareaBeanLocal;
import com.ibm.bbva.session.TipoBuroBeanLocal;
import com.ibm.bbva.session.TipoScoringBeanLocal;
import com.ibm.bbva.util.Util;
import com.ibm.bbva.util.UtilWebService;

@SuppressWarnings("serial")
@ManagedBean(name = "datosProducto2")
@RequestScoped
public class DatosProducto2MB extends AbstractMBean {

	@EJB
	private TipoBuroBeanLocal tipoBuroBean;
	@EJB
	private ClasifBancoBeanLocal clasifBancoBean;
	@EJB
	private TipoScoringBeanLocal tipoScoringBean;
	@EJB
	private ProductoEtiquetaBeanLocal productoEtiquetabean;		
	@EJB
	private ProductoTareaBeanLocal productoTareaBean;
	@EJB
	private MensajesBeanLocal mensajeBean;
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	@EJB
	private ExpedienteBeanLocal expedienteBean;	
	
	private Expediente expediente;	
	private ClienteNatural clienteNatural;	
	private boolean selectedItem;
	private List<SelectItem> listClasifBanco;
	private List<SelectItem> listClasifBancoCony;
	private List<SelectItem> listCodigoBuro;
	private String codigoBuroSeleccionado;
	private List<SelectItem> listScoring;
	private String scoringSeleccionado;

	private boolean habLineaConsumo;
	private boolean habEndeudamiento;
	private boolean habRiesgoCliente;
	private boolean habClasificacionSbs;
	private boolean habSbsConyuge;
	private boolean habClasificacionBanco;
	private boolean habBancoConyuge;
	private boolean habRvgl;
	private boolean habIngNetoMensual;
	private boolean habNumeroCuenta;
	private boolean habSelectCodigoBuro;
	private boolean habSelectScoring;

	/* String para formato numerico */
	private String lineaConsumo;
	private String riesgoCliente;
	private String ingNetoMensual;

	/**
	 * Cambio Para PLD
	 * */	
	private boolean renderedEnvioTarjeta;
	private boolean renderedPlazoSolicitado;
	private boolean renderedSolicitudTasaEsp;
	private boolean renderedGarantia;
	//private boolean renderedDps;
	private boolean renderedLineaCreditoApr;
	private boolean renderedPlazoSolicitadoApr;
	
	private String clasificacionBancoSel;
	private String bancoConyugeSel;
	
	/**
	 * Para Numero de cuenta Mejora 149
	 */
	
	private String strEntidad;
	private String strOficina;
	private String strDigitoChequeo;
	private String strNumeroCuenta;
	private String numeroCuenta; // numero de Cuenta que se obtiene de la base de datos , del expediente ya registrado.
	
	private String msgErrorPersonalizado;
	private String msgPersonalizado;
	private static final Logger LOG = LoggerFactory.getLogger(DatosProducto2MB.class);
	
	public DatosProducto2MB() {
		
	}

	@PostConstruct
    public void init() {
		LOG.info("init DatosProducto2MB:::");
		String nombJSP = getNombreJSPPrincipal();
		if (nombJSP.equals("formRegistrarDatos")) {
			renderedLineaCreditoApr=true;
			//renderedPlazoSolicitadoApr=true;
		}else
			renderedLineaCreditoApr=false;
		
		obtenerDatos();
		if(!habClasificacionSbs){
			if (clienteNatural.getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CASADO.longValue() || 
					clienteNatural.getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CONVIVIENTE.longValue()){
				habBancoConyuge = false;
				habSbsConyuge = false;			
			}else{		
				habBancoConyuge = true;
				habSbsConyuge = true;
			}			
		}else{
			habSbsConyuge = true;
			if (clienteNatural.getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CASADO.longValue() || 
					clienteNatural.getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CONVIVIENTE.longValue()){
				habBancoConyuge = false;	
			}else
				habBancoConyuge = true;
			//habBancoConyuge = true;
		}
	
		LOG.info("renderedLineaCreditoApr:::"+renderedLineaCreditoApr);
		//habSbsConyuge = true;
		
		strEntidad=Constantes.CODIGO_ENTIDAD_NUMERO_CUENTA;
		
		numeroCuenta=expediente.getExpedienteTC().getNroCta();
		LOG.info("numeroCuenta:::"+numeroCuenta);
		if (nombJSP.equals("formAnularModificarOpCu14") ||
		     (nombJSP.equals("formAnularModificarOpCu18")) ||
		     (nombJSP.equals("formCorregirExpediente32")) ||
		     (nombJSP.equals("formCorregirExpediente33")) ||
		     (nombJSP.equals("formRegistrarDatos"))){
			if (numeroCuenta!=null && numeroCuenta.length()>19) {
				strEntidad=numeroCuenta.substring(0,4);
				LOG.info("strEntidad:::"+strEntidad);
				strOficina=numeroCuenta.substring(4,8);
				LOG.info("strOficina:::"+strOficina);
				strDigitoChequeo=numeroCuenta.substring(8,10);
				LOG.info("strDigitoChequeo:::"+strDigitoChequeo);
				strNumeroCuenta=numeroCuenta.substring(10,numeroCuenta.length());
				LOG.info("strNumeroCuenta:::"+strNumeroCuenta);		
			}
		}
		
	}	
	
	public void obtenerDatos() {

		/* Obtener la lista de codigo Buro */		
		listCodigoBuro = Util.crearItems(tipoBuroBean.buscarTodos(),
				true, "id", "descripcion");

		//listClasifBancoCony = Util.crearItems(clasifBancoBean.buscarTodos(),
			//	true, "id", "descripcion");
		
		//listClasifBanco = Util.crearItems(clasifBancoBean.buscarTodos(),
			//	true, "id", "descripcion");
		
		/* Obtener la lista de tipo Scoring */

		listScoring = Util.crearItems(tipoScoringBean.buscarTodos(),
				true, "id", "descripcion");

		/* Obtiene Datos de Expediente */
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);

		/* Obtiene Datos de Cliente */
		clienteNatural = new ClienteNatural();
		
		if(expediente!=null){
			
			if(expediente.getId() > 0 && (expediente.getClienteNatural() == null || expediente.getClienteNatural().getId() <= 0)){
				expediente = expedienteBean.buscarPorId(expediente.getId());
			}
			clienteNatural = expediente.getClienteNatural();
			/*
			if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoOferta()!=null && expediente.getExpedienteTC().getTipoOferta().getCodigo()!=null)
				LOG.info("tipo oferta= "+expediente.getExpedienteTC().getTipoOferta().getCodigo());
			else
				LOG.info("No existe tipo oferta= ");*/
				
			if (expediente.getExpedienteTC().getFlagSolTasaEsp()!=null && expediente.getExpedienteTC().getFlagSolTasaEsp().equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItem = true;
			}
			
			if (expediente.getExpedienteTC().getTipoOferta()!=null && expediente.getExpedienteTC().getTipoOferta()!=null && 
					expediente.getExpedienteTC().getTipoOferta().getId()>0){
				if(expediente.getExpedienteTC().getTipoOferta().getId()==1){
					habSbsConyuge = true;
					habClasificacionSbs=true;
				}else{
					habSbsConyuge = false;
					habClasificacionSbs=false;				
				}
					
			}
			//	if(expediente!=null && expediente.getProducto()!=null && expediente.getProducto().getId()>0)
			//	campoProducto(expediente.getProducto().getId());
			
			if(expediente!=null && expediente.getProducto()!=null && expediente.getProducto().getId()>0){
				
				campoProducto(expediente.getProducto().getId());
				
				listClasifBancoCony = Util.crearItems(clasifBancoBean.buscarPorIdProducto(expediente.getProducto().getId()),
						true, "id", "descripcion");
				
				listClasifBanco = Util.crearItems(clasifBancoBean.buscarPorIdProducto(expediente.getProducto().getId()),
						true, "id", "descripcion");
							
			}
			
			codigoBuroSeleccionado = expediente.getExpedienteTC().getTipoBuro() == null ? null : String.valueOf(expediente.getExpedienteTC().getTipoBuro().getId());
			scoringSeleccionado = expediente.getExpedienteTC().getTipoScoring() == null ? null : String.valueOf(expediente.getExpedienteTC().getTipoScoring().getId());

			clasificacionBancoSel = expediente.getExpedienteTC().getClasificacionBanco() == null ? null : String.valueOf(expediente.getExpedienteTC().getClasificacionBanco().getId());
			bancoConyugeSel= expediente.getExpedienteTC().getBancoConyuge() == null ? null : String.valueOf(expediente.getExpedienteTC().getBancoConyuge().getId());
			
			copiarDeExpediente();			
			
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
				if(value.getEtiqueta().equals(Constantes.CAMPO_SOL_TASA_ESP))
					setRenderedSolicitudTasaEsp(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);	
				if(value.getEtiqueta().equals(Constantes.CAMPO_GARANTIA))
					setRenderedGarantia(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);	
				if(value.getEtiqueta().equals(Constantes.CAMPO_PLAZO_SOLICITADO_APR))
					setRenderedPlazoSolicitadoApr(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);				

					
				//if(value.getEtiqueta().equals(Constantes.CAMPO_DPS))
				//	solVerificarAprobar.setVerDPS(value.getVisible().trim().equals(Constantes.CAMPO_OPCION_VISIBLE)?true:false);
			}
		}
	}	

	/**
	 * Copia los datos a los objetos pasados como parametros y valores que
	 * faltan al objeto expediente que esta en sesion
	 * 
	 * @param clienteNatural
	 */
	public void copiarDatos(ClienteNatural clienteNatural) {
		//clienteNatural.setIngNetoMensual(clienteNatural.getIngNetoMensual());
		
		if(ingNetoMensual!=null && Util.convertStringToDouble(Util.esNuloVacio(ingNetoMensual) ? "0" : ingNetoMensual)>0){
			LOG.info("ingNetoMensual : "+ingNetoMensual);						
			clienteNatural.setIngNetoMensual(Util.convertStringToDouble(Util.esNuloVacio(ingNetoMensual) ? "0" : ingNetoMensual));
		}
		
		if(codigoBuroSeleccionado!=null && Integer.parseInt(codigoBuroSeleccionado)!=Integer.parseInt(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			int codBuro = Integer.parseInt(codigoBuroSeleccionado);
			TipoBuro tipoBuro = new TipoBuro();
			tipoBuro.setId(codBuro);		
			expediente.getExpedienteTC().setTipoBuro(tipoBuro);
		}
		
		if(scoringSeleccionado!=null && Integer.parseInt(scoringSeleccionado)!=Integer.parseInt(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			int scor = Integer.parseInt(scoringSeleccionado);
			TipoScoring tipoScoring = new TipoScoring();
			tipoScoring.setId(scor);
			expediente.getExpedienteTC().setTipoScoring(tipoScoring);	
			LOG.info("Tipo Scoring es "+expediente.getExpedienteTC().getTipoScoring().getId());
		}else
			LOG.info("Tipo Scoring es nulo o vacío");
		
		if(clasificacionBancoSel!=null && Integer.parseInt(clasificacionBancoSel)!=Integer.parseInt(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			LOG.info("clasificacionBancoSel : "+clasificacionBancoSel);						
			ClasifBanco clasifBanco = clasifBancoBean.buscarPorId(Long.parseLong(clasificacionBancoSel));
			expediente.getExpedienteTC().setClasificacionBanco(clasifBanco);
		}
		
		if(bancoConyugeSel!=null && Integer.parseInt(bancoConyugeSel)!=Integer.parseInt(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			LOG.info("bancoConyugeSel : "+bancoConyugeSel);		
   		    ClasifBanco clasifBanco = clasifBancoBean.buscarPorId(Long.parseLong(bancoConyugeSel));
			expediente.getExpedienteTC().setBancoConyuge(clasifBanco);
		}
		
		copiarAExpediente();
	}

	private void copiarAExpediente() {
		/* Copia string formateados a campos numericos */		
		expediente.getExpedienteTC().setLineaConsumo(Util.convertStringToDouble(Util.esNuloVacio(lineaConsumo) ? "0" : lineaConsumo));
		expediente.getExpedienteTC().setRiesgoCliente(Util.convertStringToDouble(Util.esNuloVacio(riesgoCliente) ? "0" : riesgoCliente));
		clienteNatural.setIngNetoMensual(Util.convertStringToDouble(Util.esNuloVacio(ingNetoMensual) ? "0" : ingNetoMensual));
		expediente.setClienteNatural(clienteNatural);
	}
	
	private void copiarDeExpediente() {
		/* Mostrar string datos numericos formateados */
		setLineaConsumo(expediente.getExpedienteTC().getLineaConsumo() > 0?Util.convertDoubleToString(expediente.getExpedienteTC().getLineaConsumo(),Constantes.FORMATO_DECIMAL_MILLON):"");
		setRiesgoCliente(expediente.getExpedienteTC().getRiesgoCliente() > 0?Util.convertDoubleToString(expediente.getExpedienteTC().getRiesgoCliente(),Constantes.FORMATO_DECIMAL_MILLON):"");
		setIngNetoMensual(clienteNatural.getIngNetoMensual() > 0?Util.convertDoubleToString(clienteNatural.getIngNetoMensual(),Constantes.FORMATO_DECIMAL_MILLON):"");
	}
	
	public Expediente getexpediente() {
		return expediente;
	}

	public void setexpediente(Expediente expediente) {
		this.expediente = expediente;
	}
	
	public boolean isSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(boolean selectedItem) {
		this.selectedItem = selectedItem;
	}

	public String getCodigoBuroSeleccionado() {
		return codigoBuroSeleccionado;
	}

	public void setCodigoBuroSeleccionado(String codigoBuroSeleccionado) {
		this.codigoBuroSeleccionado = codigoBuroSeleccionado;
	}

	public String getScoringSeleccionado() {
		return scoringSeleccionado;
	}

	public void setScoringSeleccionado(String scoringSeleccionado) {
		this.scoringSeleccionado = scoringSeleccionado;
	}

	public List<SelectItem> getListCodigoBuro() {
		return listCodigoBuro;
	}

	public void setListCodigoBuro(List<SelectItem> listCodigoBuro) {
		this.listCodigoBuro = listCodigoBuro;
	}

	public List<SelectItem> getListScoring() {
		return listScoring;
	}

	public void setListScoring(List<SelectItem> listScoring) {
		this.listScoring = listScoring;
	}

	public ClienteNatural getclienteNatural() {
		return clienteNatural;
	}

	public void setclienteNatural(ClienteNatural clienteNatural) {
		this.clienteNatural = clienteNatural;
	}

	public boolean esValido() {
		String jspPrinc = getNombreJSPPrincipal();
		String idComponente = "";
		String Cuenta ="";
		
		if (jspPrinc.equals("formRegistrarDatos")) {
			idComponente = "frmRegistrarDatos";						
		} else if (jspPrinc.equals("formAnularModificarOpCu14")) {
			idComponente = "frmAnularModificarOpCu14";
		} else if (jspPrinc.equals("formAnularModificarOpCu18")) {
			idComponente = "frmAnularModificarOpCu18";
		} else if (jspPrinc.equals("formCorregirExpediente32")) {
			idComponente = "frmRegistrarDatos";
		} else if (jspPrinc.equals("formCorregirExpediente33")) {
			idComponente = "frmRegistrarDatos";
		}
		
		Cuenta=getStrEntidad()+getStrOficina()+getStrDigitoChequeo()+getStrNumeroCuenta();
		expediente.getExpedienteTC().setNroCta(Cuenta);
		
		LOG.info("Entidad : "+ getStrEntidad());
		LOG.info("Oficina : "+ getStrOficina());
		LOG.info("Digito Chequeo : "+ getStrDigitoChequeo());
		LOG.info("Numero Cuenta : "+ getStrNumeroCuenta());
		LOG.info("Numero Cuenta Expediente : "+ expediente.getExpedienteTC().getNroCta());
		
		//Cuenta=getStrEntidad()+"-"+getStrOficina()+"-"+getStrDigitoChequeo()+"-"+getStrNumeroCuenta();
		/*Cuenta=getStrEntidad()+getStrOficina()+getStrDigitoChequeo()+getStrNumeroCuenta();
		expediente.getExpedienteTC().setNroCta(Cuenta);
		*/
		boolean existeError = false;

		copiarAExpediente();
		 
		Double linCons = expediente.getExpedienteTC().getLineaConsumo();
		if (linCons == null || linCons.doubleValue() <= 0) {
			addMessageError(idComponente + ":lineaConsumo",
					"com.ibm.bbva.common.datosProducto2.msg.lineaConsumo");
			existeError = true;
		}
		Double porcEnd = expediente.getExpedienteTC().getPorcentajeEndeudamiento();
		if (porcEnd == null || porcEnd.doubleValue() <= Constantes.PORCENTAJE_ENDEUDAMIENTO_MIN.doubleValue() || porcEnd > Constantes.PORCENTAJE_ENDEUDAMIENTO_MAX) {
			addMessageError(idComponente + ":endeudamiento",
					"com.ibm.bbva.common.datosProducto2.msg.endeudamiento");
			existeError = true;
		}
		Double riesgo = expediente.getExpedienteTC().getRiesgoCliente();
		if (riesgo == null || riesgo.doubleValue() <= 0) {
			addMessageError(idComponente + ":riesgoCliente",
					"com.ibm.bbva.common.datosProducto2.msg.riesgoCliente");
			existeError = true;
		}
		Double clasif = expediente.getExpedienteTC().getClasificacionSbs();
		if (clasif == null || clasif.doubleValue() <= 0 || clasif > 100) {
			addMessageError(idComponente + ":clasificacionSbs",
					"com.ibm.bbva.common.datosProducto2.msg.clasificacionSbs");
			existeError = true;
		}
		Double clasifCon = expediente.getExpedienteTC().getSbsConyuge();
		if ((clienteNatural.getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CASADO.longValue() || 
				clienteNatural.getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CONVIVIENTE.longValue()) && 
				(clasifCon == null || clasifCon.doubleValue() <= 0
				|| clasifCon > 100)) {
			addMessageError(idComponente + ":sbsConyuge",
					"com.ibm.bbva.common.datosProducto2.msg.clasificacionSbs");
			existeError = true;
		}
		String clasifBan = clasificacionBancoSel;
		LOG.info("clasifBan : "+clasifBan);
		if (clasifBan == null || clasifBan.equals("") || clasifBan.equals("-1")) {
			addMessageError(idComponente + ":clasificacionBanco",
					"com.ibm.bbva.common.datosProducto2.msg.clasificacionBanco");
			existeError = true;
		}
		String clasifBanCon = bancoConyugeSel;
		if ((clienteNatural.getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CASADO.longValue() || 
				clienteNatural.getEstadoCivil().getId() == Constantes.ESTADO_CIVIL_CONVIVIENTE.longValue()) && 
				(clasifBanCon == null || clasifBanCon.equals("") || clasifBanCon.equals("-1"))) {
			addMessageError(idComponente + ":bancoConyuge",
					"com.ibm.bbva.common.datosProducto2.msg.clasificacionBanco");
			existeError = true;
		}
		String codRVGL = expediente.getExpedienteTC().getRvgl();
		if (codRVGL == null || codRVGL.trim().equals("") || codRVGL.trim().length()<1) {
			addMessageError(idComponente + ":rvgl",
					"com.ibm.bbva.common.datosProducto2.msg.rvgl");
			existeError = true;
		}
		Double ingNeto = clienteNatural.getIngNetoMensual();
		if (ingNeto == null || ingNeto.doubleValue() <= 0) {
			addMessageError(idComponente + ":ingNetoMensual",
					"com.ibm.bbva.common.datosProducto2.msg.ingNetoMensual");
			existeError = true;
		}
		
		String numCuenta=expediente.getExpedienteTC().getNroCta();
		if (numCuenta == null || numCuenta.trim().equals("") || numCuenta.length()<20) {
			addMessageError(idComponente + ":entidad",
					"com.ibm.bbva.common.datosProducto2.msg.numeroCuenta");
			existeError = true;
		}
		/**
		 * Acceder al algoritmo que valida que el Num Cta / Contrato sean validos.
		 * 09-Marzo-2015
		 * */
		String valorNumCuenta = expediente.getExpedienteTC().getNroCta();
		if (expediente.getProducto().getId()==Constantes.ID_APLICATIVO_PLD){
			if (valorNumCuenta == null || valorNumCuenta.trim().equals("")) {
				addMessageError(idComponente + ":entidad",
						"com.ibm.bbva.common.datosProducto2.msg.numeroCuenta");
				existeError = true;
			}else 
				if (valorNumCuenta.length()==20){
					if(!Util.validaCuenta(formatearCuentaContrato(valorNumCuenta))){
					addMessageError(idComponente + ":entidad",
							"com.ibm.bbva.common.datosProducto2.msg.numeroCuentaNoValido");
					existeError = true;
					}
				}else
				{
					addMessageError(idComponente + ":entidad",
							"com.ibm.bbva.common.datosProducto2.msg.numeroCuentaNoValido");
					existeError = true;					
				}
		}
		if (expediente.getProducto().getId()==Constantes.ID_APLICATIVO_TC){
			if (numCuenta!=null && !numCuenta.trim().equals("")){
				if (valorNumCuenta.length()==20){
					if(!Util.validaCuenta(formatearCuentaContrato(valorNumCuenta))){
						addMessageError(idComponente + ":entidad",
								"com.ibm.bbva.common.datosProducto2.msg.numeroCuentaNoValido");
						existeError = true;
					}
				}else{
					addMessageError(idComponente + ":entidad",
							"com.ibm.bbva.common.datosProducto2.msg.numeroCuentaNoValido");
					existeError = true;					
				}
			}
		}
		/**
		 * Fin de cambio 09-Marzo-2015
		 * */
		
		if (codigoBuroSeleccionado == null
				|| Constantes.CODIGO_CODIGO_CAMPO_VACIO
						.equals(codigoBuroSeleccionado)) {
			addMessageError(idComponente + ":selectCodigoBuro",
					"com.ibm.bbva.common.datosProducto2.msg.selectCodigoBuro");
			existeError = true;
		}
		if (scoringSeleccionado == null
				|| Constantes.CODIGO_CODIGO_CAMPO_VACIO
						.equals(scoringSeleccionado)) {
			addMessageError(idComponente + ":selectScoring",
					"com.ibm.bbva.common.datosProducto2.msg.selectScoring");
			LOG.info("Scoring seleccionado es vacio o nulo ");
			existeError = true;
		}else
			LOG.info("Scoring seleccionado = "+scoringSeleccionado);

		//Validacion de nro de contrato
		//Para la Tarea 18 fue colocado en FLAG_VALIDACION de LA TABLA PRODUCTO_TAREA en 0, 
		//para obviar la validacion de numero de contrato.
		msgPersonalizado = null;
		ProductoTarea objProductoTarea= productoTareaBean.buscarPorIdTareaIdProducto(Long.valueOf(expediente.getProducto().getCodigo()), 
				Long.valueOf(expediente.getExpedienteTC().getTarea().getCodigo()));
		if( objProductoTarea!= null && objProductoTarea.getFlagValidacion()!=null && 
				objProductoTarea.getFlagValidacion().trim().equals(Constantes.FLAG_ACTIVO))
		{
			try{
				existeError = !validacionNumeroContradoPackPyme();
			}catch(Exception ex){
				LOG.info("Exception validacionNumeroContradoPackPyme::"+ex.toString());
			}
			
		}
		return !existeError;
	}
	
	private String formatearCuentaContrato(String cta){
		String result="";
		if (cta!=null && cta.length()==20 ) {
			String strEntidad=cta.substring(0,4);
			LOG.info("strEntidad:::"+strEntidad);
			String strOficina=cta.substring(4,8);
			LOG.info("strOficina:::"+strOficina);
			String strDigitoChequeo=cta.substring(8,10);
			LOG.info("strDigitoChequeo:::"+strDigitoChequeo);
			String strNumeroCuenta=cta.substring(10,cta.length());
			LOG.info("strNumeroCuenta:::"+strNumeroCuenta);
			
			result=strEntidad+"-"+strOficina+"-"+strDigitoChequeo+"-"+strNumeroCuenta;
						 
		}
		LOG.info("result::::"+result);
		return result;
	}
	
	/**
	 * Validacion de nro de contrato PackPyme
	 * @return
	 */
	private boolean validacionNumeroContradoPackPyme(){
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		msgPersonalizado = UtilWebService.validacionNumeroContradoClientePackPyme(bbvaFacade, mensajeBean, expediente, empleado.getCodigo(), getIdSession());
		return msgPersonalizado == null;
	}

	public boolean isHabLineaConsumo() {
		return habLineaConsumo;
	}

	public void setHabLineaConsumo(boolean habLineaConsumo) {
		this.habLineaConsumo = habLineaConsumo;
	}

	public boolean isHabEndeudamiento() {
		return habEndeudamiento;
	}

	public void setHabEndeudamiento(boolean habEndeudamiento) {
		this.habEndeudamiento = habEndeudamiento;
	}

	public boolean isHabRiesgoCliente() {
		return habRiesgoCliente;
	}

	public void setHabRiesgoCliente(boolean habRiesgoCliente) {
		this.habRiesgoCliente = habRiesgoCliente;
	}

	public boolean isHabClasificacionSbs() {
		return habClasificacionSbs;
	}

	public void setHabClasificacionSbs(boolean habClasificacionSbs) {
		this.habClasificacionSbs = habClasificacionSbs;
	}

	public boolean isHabSbsConyuge() {
		return habSbsConyuge;
	}

	public void setHabSbsConyuge(boolean habSbsConyuge) {
		this.habSbsConyuge = habSbsConyuge;
	}

	public boolean isHabClasificacionBanco() {
		return habClasificacionBanco;
	}

	public void setHabClasificacionBanco(boolean habClasificacionBanco) {
		this.habClasificacionBanco = habClasificacionBanco;
	}

	public boolean isHabBancoConyuge() {
		return habBancoConyuge;
	}

	public void setHabBancoConyuge(boolean habBancoConyuge) {
		this.habBancoConyuge = habBancoConyuge;
	}

	public boolean isHabRvgl() {
		return habRvgl;
	}

	public void setHabRvgl(boolean habRvgl) {
		this.habRvgl = habRvgl;
	}

	public boolean isHabIngNetoMensual() {
		return habIngNetoMensual;
	}

	public void setHabIngNetoMensual(boolean habIngNetoMensual) {
		this.habIngNetoMensual = habIngNetoMensual;
	}

	public boolean isHabNumeroCuenta() {
		return habNumeroCuenta;
	}

	public void setHabNumeroCuenta(boolean habNumeroCuenta) {
		this.habNumeroCuenta = habNumeroCuenta;
	}

	public boolean isHabSelectCodigoBuro() {
		return habSelectCodigoBuro;
	}

	public void setHabSelectCodigoBuro(boolean habSelectCodigoBuro) {
		this.habSelectCodigoBuro = habSelectCodigoBuro;
	}

	public boolean isHabSelectScoring() {
		return habSelectScoring;
	}

	public void setHabSelectScoring(boolean habSelectScoring) {
		this.habSelectScoring = habSelectScoring;
	}

	public String getLineaConsumo() {
		return lineaConsumo;
	}

	public void setLineaConsumo(String lineaConsumo) {
		this.lineaConsumo = lineaConsumo;
	}

	public String getRiesgoCliente() {
		return riesgoCliente;
	}

	public void setRiesgoCliente(String riesgoCliente) {
		this.riesgoCliente = riesgoCliente;
	}

	public String getIngNetoMensual() {
		return ingNetoMensual;
	}

	public void setIngNetoMensual(String ingNetoMensual) {
		this.ingNetoMensual = ingNetoMensual;
	}

	public List<SelectItem> getListClasifBanco() {
		return listClasifBanco;
	}

	public void setListClasifBanco(List<SelectItem> listClasifBanco) {
		this.listClasifBanco = listClasifBanco;
	}

	public List<SelectItem> getListClasifBancoCony() {
		return listClasifBancoCony;
	}

	public void setListClasifBancoCony(List<SelectItem> listClasifBancoCony) {
		this.listClasifBancoCony = listClasifBancoCony;
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

	public boolean isRenderedGarantia() {
		return renderedGarantia;
	}

	public void setRenderedGarantia(boolean renderedGarantia) {
		this.renderedGarantia = renderedGarantia;
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

	public String getClasificacionBancoSel() {
		return clasificacionBancoSel;
	}

	public void setClasificacionBancoSel(String clasificacionBancoSel) {
		this.clasificacionBancoSel = clasificacionBancoSel;
	}

	public String getBancoConyugeSel() {
		return bancoConyugeSel;
	}

	public void setBancoConyugeSel(String bancoConyugeSel) {
		this.bancoConyugeSel = bancoConyugeSel;
	}

	public String getStrEntidad() {
		return strEntidad;
	}

	public void setStrEntidad(String strEntidad) {
		this.strEntidad = strEntidad;
	}

	public String getStrOficina() {
		return strOficina;
	}

	public void setStrOficina(String strOficina) {
		this.strOficina = strOficina;
	}

	public String getStrDigitoChequeo() {
		return strDigitoChequeo;
	}

	public void setStrDigitoChequeo(String strDigitoChequeo) {
		this.strDigitoChequeo = strDigitoChequeo;
	}

	public String getStrNumeroCuenta() {
		return strNumeroCuenta;
	}

	public void setStrNumeroCuenta(String strNumeroCuenta) {
		this.strNumeroCuenta = strNumeroCuenta;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getMsgPersonalizado() {
		return msgPersonalizado;
	}

	public void setMsgPersonalizado(String msgPersonalizado) {
		this.msgPersonalizado = msgPersonalizado;
	}

	public String getMsgErrorPersonalizado() {
		return msgErrorPersonalizado;
	}

	public void setMsgErrorPersonalizado(String msgErrorPersonalizado) {
		this.msgErrorPersonalizado = msgErrorPersonalizado;
	}
	
	public boolean isRenderedPlazoSolicitadoAprobOk() {
		LOG.info("expediente.getExpedienteTC().getPlazoSolicitadoApr() "+expediente.getExpedienteTC().getPlazoSolicitadoApr());
		String nombJSP = getNombreJSPPrincipal();
				
		if (nombJSP.equals("formRegistrarDatos")) {
			return false;
		}
		
		if(expediente.getExpedienteTC()!=null && 
					expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null && !expediente.getExpedienteTC().getPlazoSolicitadoApr().trim().equals(""))
			return true;
		else
			return false;			
				
		
	}
	
	public boolean isRenderedLineaCreditoAprobadoMuestra(){
		LOG.info("linea cred aprobada ::: "+expediente.getExpedienteTC().getLineaCredAprob());
		String nombJSP = getNombreJSPPrincipal();
		
		if (nombJSP.equals("formRegistrarDatos")) {
			return false;
		}
		
		String result = ((expediente.getExpedienteTC().getLineaCredAprob()>0)?String.valueOf(expediente.getExpedienteTC().getLineaCredAprob()):"");
		LOG.info("result ::: "+result);
		if(!result.equals("")){
			return true;
		}else
			return false;			
		

	}	
	
	
}

package com.ibm.bbva.controller.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.InitialContext;
import javax.naming.NamingException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import pe.com.grupobbva.sce.tc84.CtBodyRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRs;
import pe.com.grupobbva.sce.tc84.CtHeader;
import pe.com.grupobbva.sce.tc84.CtTipoCambio;
import pe.com.grupobbva.sce.tc84.CtTipos;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bpd.RemoteUtils;
import bbva.ws.api.view.BBVAFacadeLocal;
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.BuscarConyugeMB;
import com.ibm.bbva.controller.common.ComentarioMB;
import com.ibm.bbva.controller.common.DatosClienteMB;
import com.ibm.bbva.controller.common.DatosConyugeMB;
import com.ibm.bbva.controller.common.DetalleExpediente1MB;
import com.ibm.bbva.controller.common.DocumentosEscaneadosMB;
import com.ibm.bbva.controller.common.EnvioMailMB;
import com.ibm.bbva.controller.common.ObservacionRechazoMB;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.controller.common.ProductoNuevoMB;
import com.ibm.bbva.controller.common.VerificarAprobarMB;
import com.ibm.bbva.entities.AyudaMemoria;
import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DevolucionTarea;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TipoCambioCE;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.session.AyudaMemoriaBeanLocal;
import com.ibm.bbva.session.CategoriaRentaBeanLocal;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.DevolucionTareaBeanLocal;
import com.ibm.bbva.session.EstadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.MensajesBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.TipoCambioCEBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaDocumento;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.Util;
import com.ibm.ccl.soa.test.common.framework.log.Log;

@SuppressWarnings("serial")
@ManagedBean(name = "registrarExpediente")
@RequestScoped
public class RegistrarExpedienteMB extends AbstractMBean {
	
	@EJB
	EstadoBeanLocal estadoBean;
	@EJB
	ClienteNaturalBeanLocal clienteNaturalBean;
	@EJB
	ExpedienteBeanLocal expedienteBean;
	@EJB
	AyudaMemoriaBeanLocal ayudaMemoriaBean;
	@EJB
	HistorialBeanLocal historialBean;
	@EJB
	CategoriaRentaBeanLocal categoriaRentaBean;
	@EJB
	DevolucionTareaBeanLocal devolucionTareaBean;
	@EJB
	private FacadeLocal facade;
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	@EJB
	private TipoCambioCEBeanLocal tipoCambioCEBeanLocal;
	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	@EJB
	private MensajesBeanLocal mensajeBean;
	
	public static final int DATOS_HAREC = 10;
	public static final int DATOS_RENIEC = 20;
	public static final int DATOS_NUEVO = 30;
	
	private boolean mostrarPaneles;
	private boolean editarCliente;
	private Expediente expediente;	
	private boolean msgGuiaDocumentaria;
	private boolean validaAprobacion;
	private boolean mostrarPanelConyuge;
	private boolean mostrarPanelDatosConyuge;
	private boolean activoAceptar;
	
	private String tipoDoi;
	
	private boolean itemDisabledSubrogacion;
	private String numeroIntentos;
	private HtmlCommandButton btnActualizaWebSeal;
	
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarExpedienteMB.class);
	
	public boolean isValidaAprobacion() {
		return validaAprobacion;
	}

	public void setValidaAprobacion(boolean validaAprobacion) {
		this.validaAprobacion = validaAprobacion;
	}

	public RegistrarExpedienteMB() {
		
	}
	
	@PostConstruct
    public void init() {
		LOG.info("Entra INIT RegistrarExpedienteMB");
		mostrarPanelDatosConyuge = false;
		validaAprobacion = true;
		activoAceptar = true;
		//mostrarPanelConyuge	= (boolean) getObjectSession(Constantes.PANEL_SESSION);	
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		if (expediente!=null && expediente.getId() > 0) {
			LOG.info("RegistrarExpedienteMB - Exp : "+expediente.getId());
			mostrarPaneles = true;
			/* si el flag registro esta como CHECK_SELECCIONADO se muestra los 
			 * campos habilitados para editar datos del cliente
			 */
			editarCliente = Constantes.CHECK_SELECCIONADO.equals(expediente.getExpedienteTC().getFlagRegistro());
		}

		if(expediente.getClienteNatural()==null || expediente.getClienteNatural().getEstadoCivil()==null)
			activoAceptar=true;
		
		//if(expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getClienteNaturalConyuge()!=null && expediente.getExpedienteTC().getClienteNaturalConyuge().getId()>0){
		if(expediente.getClienteNatural()!=null && expediente.getExpedienteTC().getTipoOferta()!=null && expediente.getClienteNatural().getEstadoCivil()!=null && 
				expediente.getExpedienteTC().getTipoOferta().getId()==Integer.parseInt(Constantes.CODIGO_OFERTA_APROBADO) && 
				(expediente.getClienteNatural().getEstadoCivil().getId()==Integer.parseInt(Constantes.EST_CIVIL_CASADO) || 
				 expediente.getClienteNatural().getEstadoCivil().getId()==Integer.parseInt(Constantes.EST_CIVIL_CONVIVIENTE))){
			mostrarPanelConyuge = true;
		}else
			mostrarPanelConyuge = false;
		
		LOG.info("activoAceptar 1 = "+activoAceptar);	
		
		if (expediente.getClienteNatural()!=null &&
				expediente.getExpedienteTC().getTipoOferta()!=null &&
				expediente.getClienteNatural().getEstadoCivil()!=null &&	
				expediente.getExpedienteTC().getClasificacionSbs()>0 ) {
				LOG.info("Entro a condicion");
				boolean pautaClas=false;
				if(expediente.getExpedienteTC().getTipoOferta().getCodigo().equals(Constantes.CODIGO_OFERTA_APROBADO)){
					pautaClas = facade.ServiceIBMBBVA_pautasClasificacionMemoria(
							expediente.getExpedienteTC().getTipoOferta().getId(), 
							Integer.valueOf(String.valueOf(expediente.getClienteNatural().getEstadoCivil().getId())), 
							Long.valueOf(Constantes.CODIGO_TIPO_PERSONA_TITULAR), 
							Long.valueOf(Constantes.CODIGO_TIPO_PERSONA_CONYUGUE), 
							expediente.getExpedienteTC().getClasificacionSbs(), 
							expediente.getExpedienteTC().getSbsConyuge());
					
					LOG.info("pautaClas = "+pautaClas);
					if (!pautaClas) 
					 	activoAceptar=false;
					else
					 	activoAceptar=true;
				}else
					activoAceptar=true;				
			}	
		
		if(expediente!=null && expediente.getProducto()!=null && expediente.getProducto().getId()>0){
			LOG.info("Expediente existe, Producto = "+expediente.getProducto().getId());
			if(expediente.getProducto().getId()== Constantes.ID_APLICATIVO_PLD)
				itemDisabledSubrogacion=false;
			else
				itemDisabledSubrogacion=true;
			LOG.info("itemDisabledSubrogacion = "+itemDisabledSubrogacion);
		}
			
		
		LOG.info("activoAceptar = "+activoAceptar);	
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		DocumentosEscaneadosMB documentosEscaneadosMB = (DocumentosEscaneadosMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "documentosEscaneados");
		//documentosEscaneadosMB.init();
		
		try{
			ParametrosConfBeanLocal parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			numeroIntentos = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, Constantes.INTENTOS_WS_UN).getValorVariable();
			LOG.info("numeroIntentos pinWebSeal - UNIFICADO: "+numeroIntentos);
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public String aceptar () {
		LOG.info("Entro Aceptar");
		mostrarPaneles = true;

		if (esValido()) {
			LOG.info("ES VALIDO");
			//Valida si cumple con guia documentaria
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "documentosEscaneados");
			LOG.info("valor de ValidaGuiaEscaneada ... "+documentosEscaneados.getValidaGuiaEscaneada());
			if (documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
				System.out.println("Si cumple condicion");
				LOG.info("VALIDO APPLET");
				String resultado = grabarDatos(Constantes.ACCION_BOTON_REGISTRAR,	Constantes.ESTADO_POR_VALIDAR_MESA_CONTROL_TAREA_1);
				addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
				addObjectSession(Constantes.ID_EXPEDIENTE_SESION, String.valueOf(expediente.getId()));
				msgGuiaDocumentaria = false;
				return resultado;
			}else{
				System.out.println("No cumple condicion");
				//documentosEscaneados.setCleanTransferDir(Constantes.CLEANTRANFERDIR_CERO);
				msgGuiaDocumentaria = true;
				return null;
			}
		}else
			LOG.info("NO ES VALIDO");
		
			validaPautaClasificacionSBS2();
			
		return null;
	}
	
	public String grabar () {
		LOG.info("ENTRO A GRABAR");
		validaDocObligatorios();
		
		mostrarPaneles = true;		
		String resultado = grabarDatos(Constantes.ACCION_BOTON_GRABAR, 	Constantes.ESTADO_EN_REGISTRO_TAREA_1);
		addObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION, Constantes.FLAG_COPIA_ARCHIVO);
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		return resultado; 
	}
	
	private String obtenerMensajeBD(String idMensaje, String idSesion) {
		String mensajeBD = obtenerMensajeSesion(idSesion);
		if (mensajeBD.trim().equals("")) {
			try {
				com.ibm.bbva.entities.Mensajes mensajeSinFiltro = new com.ibm.bbva.entities.Mensajes();
				mensajeSinFiltro = mensajeBean.buscarPorId(Long.valueOf(idMensaje));
				mensajeBD = new String(mensajeSinFiltro.getContenido());
				System.out.println("MensajeBD: " + mensajeBD);
			} catch (Exception e) {
				if (idMensaje.equals(Constantes.ID_MENSAJE_EXPEDIENTE_PRE_REGISTRADO)) {
					mensajeBD = Mensajes.getMensaje("com.ibm.bbva.mensajes.formMensajes.preRegistro");
				} else {
					mensajeBD = Mensajes.getMensaje("com.ibm.bbva.mensajes.formMensajes.registrado");
				}
			}
		}		
		return mensajeBD;
	}
	
	private String obtenerMensajeSesion(String idSesion) {
		Object objMensaje = getObjectSession(idSesion);
		return (objMensaje == null || ((String)objMensaje).trim().equals("")) ? "" : (String)objMensaje;
	}
	
	public String cancelarOperacion() {
		mostrarPaneles = true;
		LOG.info("xpediente.getId(): "+expediente.getId());
		if (expediente.getId() == 0) 		return "/index?faces-redirect=true"; //return "/mensajes/formMensajes?faces-redirect=true";
		
		if (expediente.getEstado().getId() > 0) {
		    Estado estadoVO = estadoBean.buscarPorId(expediente.getEstado().getId());
		
			// si el estado del expediente es diferente a prestamo formalizado
			if (!Constantes.CODIGO_PRESTAMO_FORMALIZADO_TAREA1.equals(estadoVO.getCodigo())) {
				ELContext elContext = FacesContext.getCurrentInstance().getELContext();
				addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, 1);
				addObjectSession(Constantes.CONSIDERAR_TAREA_1, "1");
				ObservacionRechazoMB observacionRechazoMB = (ObservacionRechazoMB) FacesContext
						.getCurrentInstance().getApplication().getELResolver()
						.getValue(elContext, null, "observacionRechazo");
				observacionRechazoMB.init();
				observacionRechazoMB.setMostrarDialogo(true);
				observacionRechazoMB.setTituloVentana(Mensajes.getMensaje("com.ibm.bbva.common.observacionRechazo.titulo4"));
			}
		}

		return null;
	}
	
	private boolean esValido () {		
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		if (jspPrinc.equals("formRegistrarExpediente")) {
			formulario = "frmRegistrarExpediente";
		}
		
		FacesContext ctx = FacesContext.getCurrentInstance();  
		DatosClienteMB datosCliente = null;
		ProductoNuevoMB producto  = null;
		ComentarioMB comentario = null;
		BuscarConyugeMB buscarConyuge=null;
		boolean validoConyuge=false;
		DatosConyugeMB datosConyuge=null;
		
		producto = (ProductoNuevoMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");
		
		datosConyuge = (DatosConyugeMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosConyuge");
		
		if (editarCliente) {
			datosCliente = (DatosClienteMB)  
			             ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");			
		} else {
			datosCliente = (DatosClienteMB)  
					     ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosCliente");			
		}		

		long idTipoDoi = datosCliente.getClienteNatural().getTipoDoi().getId();
		String strTipoDOI = String.valueOf(idTipoDoi);
		
		if (strTipoDOI.equals(Constantes.CODIGO_TIPO_DOI_RUC)){
			datosCliente.setRenderedNombre(false);
			datosCliente.setRenderedRazonSocial(true);
			datosCliente.setRenderedApellidos(true);
		}else{
			datosCliente.setRenderedNombre(true);
			datosCliente.setRenderedRazonSocial(false);
			datosCliente.setRenderedApellidos(false);
		}
		
		
		datosCliente.setCodigoTipoOferta(producto.getCodTipoOferta());

		if(datosCliente!=null && datosCliente.getIdEstadoCivil()!=null && datosCliente.getCodigoTipoOferta()!=null && 
				datosCliente.getCodigoTipoOferta().equals(Constantes.CODIGO_OFERTA_APROBADO) && 
				(datosCliente.getIdEstadoCivil().equals(Constantes.EST_CIVIL_CASADO) || 
				datosCliente.getIdEstadoCivil().equals(Constantes.EST_CIVIL_CONVIVIENTE))){
			LOG.info("Primera condicional");
//			buscarConyuge = (BuscarConyugeMB)  
//					 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarConyuge");
//			
//			if(buscarConyuge!=null && buscarConyuge.isEstBuscar())
			/* La buscarConyuge.isEstBuscar() siempre devuelve false aquí porque solo se setea al buscar y se pierde el valor del request */
			if(datosConyuge!=null && datosConyuge.isMostrarDatosConyugePrincipal())
			{
				LOG.info("Segunda condicional");
				validoConyuge=true;
			}
			else{
				LOG.info("Tercera condicional");
				validoConyuge=false;
				addMessageError(formulario + ":numeroDOIC", 
						"com.ibm.bbva.registrarExpediente.formRegistrarExpediente.msg.DoiConyuge");
			}
			mostrarPanelConyuge = true; //JBTA
		}else
			validoConyuge=true;
		
		producto.setDisabledInputTasaEsp(!producto.isTasaEspecial());
		boolean validoDatCli = datosCliente.esValido();
		boolean validoProd = producto.esValido();		
		
		comentario = (ComentarioMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
		
        boolean validoComentario = comentario.esValido();
        LOG.info("validoProd : "+validoProd);
        LOG.info("validoDatCli : "+validoDatCli);
        LOG.info("validoComentario : "+validoComentario);
        LOG.info("validoConyuge : "+validoConyuge);
        validaAprobacion = validoProd && validoDatCli && validoComentario && validoConyuge;
        datosConyuge.init();
        
		return validaAprobacion;
	}

	// Ejecutado por ObservacionRechazoMB
	public String accionVentana(ArrayList<DevolucionTarea> lista) {		
		expediente.setMotivoDevolucion(lista.get(0).getMotivoDevolucion());
		
		/*
		 * Se Agrego
		 * 
		 * */
		expediente.getExpedienteTC().setComentarioRechazo(lista.get(0).getExpediente().getExpedienteTC().getComentarioRechazo());
		LOG.info("COMENTARIO RECHAZO:" + expediente.getExpedienteTC().getComentarioRechazo());
		
		
		for (DevolucionTarea devTareaVO : lista) {
			devolucionTareaBean.create(devTareaVO);
		}
		LOG.info("Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE="+Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE);
		LOG.info("Constantes.ESTADO_EXPEDIENTE_POR_CERRAR_TAREA_1="+Constantes.ESTADO_EXPEDIENTE_POR_CERRAR_TAREA_1);
		String retorno = grabarDatos(Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE, 
				Constantes.ESTADO_EXPEDIENTE_POR_CERRAR_TAREA_1);
		LOG.info("retorno="+retorno);
		return retorno;
	}

	public void validaDocObligatorios() {
		/*TODO		  
		DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB)getObjectRequest("documentosEscaneados");
		if (!documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
			addObjectSession(Constantes.REGISTRAR_EXPEDIENTE_SESION, documentosEscaneados.getDocumentosFaltantes());
		}
		*/
		
		DocumentosEscaneadosMB documentosEscaneados = (DocumentosEscaneadosMB)getObjectRequest("documentosEscaneados");
		if (!documentosEscaneados.getValidaGuiaEscaneada().equals(Constantes.VALIDA_DOC_ESC_EXISTE)) {
			addObjectSession(Constantes.REGISTRAR_EXPEDIENTE_SESION, documentosEscaneados.getDocumentosFaltantes());
		}
		
	}
	

	
	// es llamado por BuscarClienteMB
	public void panelClienteNatural (ClienteNatural clienteNatural, int fuente, String tipoDOISeleccionado) {
		setTipoDoi(tipoDOISeleccionado);
		if (fuente==DATOS_HAREC || fuente==DATOS_RENIEC) {
			editarCliente = false;
		} else {
			editarCliente = true;
		}
		addObjectSession("editarCliente",editarCliente);
		FacesContext ctx = FacesContext.getCurrentInstance();  
		DatosClienteMB datosCliente = null;
		ProductoNuevoMB productoNuevo  = null;
		VerificarAprobarMB  verificarAprobar  = null;
		ComentarioMB comentario = null;
		
				
		if (editarCliente) {
			datosCliente = (DatosClienteMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
			
		} else {
			datosCliente = (DatosClienteMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosCliente");
		}
		
		if (tipoDOISeleccionado.equals(Constantes.CODIGO_TIPO_DOI_RUC)){
			datosCliente.setRenderedRazonSocial(true);
			datosCliente.setRenderedNombre(false);
			datosCliente.setRenderedApellidos(true);
		}else{
			datosCliente.setRenderedRazonSocial(false);
			datosCliente.setRenderedNombre(true);
			datosCliente.setRenderedApellidos(false);
		}
		
		datosCliente.iniciarDatos(clienteNatural);
				
		productoNuevo = (ProductoNuevoMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");
		
		verificarAprobar = (VerificarAprobarMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");
		
		comentario = (ComentarioMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
	}
	
	public void visualizarPaneles (boolean valorFlag) {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		DatosClienteMB datosCliente = null;;
		
		if(valorFlag){
			editarCliente = true;
		}
		
		if (editarCliente) {
			datosCliente = (DatosClienteMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
			
		} else {
			datosCliente = (DatosClienteMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosCliente");
		}
		
		LOG.info("entro a visualizarPaneles");
		mostrarPaneles = true;
		//if(datosCliente!=null && datosCliente.idEstadoCivil.equals(Constantes.ESTADO_CIVIL_CASADO) && datosCliente.getCodigoTipoOferta().equals(Constantes.CODIGO_OFERTA_APROBADO)){
			//mostrarPanelConyuge = true;
		//}else{
			mostrarPanelConyuge = false;
		//}
		//addObjectSession(Constantes.PANEL_SESSION, mostrarPanelConyuge);
		mostrarPanelDatosConyuge = false;
		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		DocumentosEscaneadosMB documentosEscaneadosMB = (DocumentosEscaneadosMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "documentosEscaneados");
		documentosEscaneadosMB.limpiarCarpetaTransferencias();
	}
	
	private String grabarDatos (String accion, Integer estado) {
		/*if(expediente.getId() != 0){
			expediente = expedienteBean.buscarPorId(expediente.getId());
		}*/
		LOG.info("grabarDatos 1 con ACCION="+accion);
		LOG.info("grabarDatos 1 con ESTADO="+estado);
		LOG.info("tasa Especial :"+expediente.getExpedienteTC().getTasaEsp());
		if (Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE.equals(accion)) { // accion cancelar
			addObjectSession(Constantes.TIPO_MENSAJE, Constantes.TIPO_MENSAJE_CANCELADO);
			
			//Process
			RemoteUtils bandeja = new RemoteUtils();
			AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
			ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this,
					null, accion, estado);	
			String tkiid = expedienteTCWPS.getTaskID();	
			
			/**
			 * ===========================================================
			 * Bloque de revisión de nuevos
			 * archivos subidos, debe ir antes de enviar el mensaje
			 * al process.
			 */
			
			PanelDocumentosMB panelDocumentos = (PanelDocumentosMB)getObjectRequest("paneldocumentos");
			
			expedienteTCWPS = ayudaExpedienteTC.setFlagEnvioContent(expedienteTCWPS, panelDocumentos);
			
			LOG.debug("Se han subido archivos al expediente : " + expedienteTCWPS.getFlagEnvioContent());
			
			/** =============================================================== **/
			
			bandeja.completarTarea(tkiid, expedienteTCWPS);
			    
			//fin process
			//Historico
			Historial historialVO = ConvertExpediente.convertToHistorialVO(expediente);			
			/*INICIO REQUERIMIENTO 286 27.11.2014*/
			List<ToePerfilEstado> lstToePerfilEstado= new ArrayList<ToePerfilEstado>();
			lstToePerfilEstado=Util.recuperaValoresTOEPerfilEstado(expediente, toePerfilEstadoBeanLocal);
			ToePerfilEstado toePerfilEstado=new ToePerfilEstado();
			if (lstToePerfilEstado.size()>0){
				toePerfilEstado = lstToePerfilEstado.get(1);
			}else{
				toePerfilEstado = null;
			}
			/*FIN REQUERIMIENTO 286 27.11.2014*/
			historialVO.setFechaFin(new Timestamp(new Date().getTime()));
			historialVO.getEstado().setId(Constantes.ESTADO_CERRADO_TAREA_27);
			int ans=0;
			/*INICIO REQUERIMIENTO 286 27.11.2014*/
			if (toePerfilEstado!=null){
				historialVO.setTiempoObjTC(toePerfilEstado.getTiempoObjTc().intValueExact());
				historialVO.setTiempoObjTE(toePerfilEstado.getTiempoObjTe().intValueExact());
				historialVO.setTiempoPreTC(toePerfilEstado.getTiempoPreTc().intValueExact());
				historialVO.setTiempoPreTE(toePerfilEstado.getTiempoPreTe().intValueExact());
				historialVO.setNomColumna(toePerfilEstado.getNomColumna());
				ans=toePerfilEstado.getTiempoObjTc().intValueExact()+toePerfilEstado.getTiempoObjTe().intValueExact();
				historialVO.setAns(ans);
			}
			else{
				historialVO.setTiempoObjTC(0);
				historialVO.setTiempoObjTE(0);
				historialVO.setTiempoPreTC(0);
				historialVO.setTiempoPreTE(0);
				historialVO.setNomColumna(null);
				historialVO.setAns(0);
			}
			/*FIN REQUERIMIENTO 286 27.11.2014*/
			historialBean.create(historialVO);			
			//Fin Historico
			return "/mensajes/formMensajes?faces-redirect=true";
		}
		LOG.info("tasa Especial :"+expediente.getExpedienteTC().getTasaEsp());
		FacesContext ctx = FacesContext.getCurrentInstance();
		DatosClienteMB datosCliente = null;
		ProductoNuevoMB productoNuevo  = null;
		VerificarAprobarMB  verificarAprobar  = null;
		ComentarioMB comentario = null;
		PanelDocumentosMB panelDocumentoMB = null;
		LOG.info("tasa Especial :"+expediente.getExpedienteTC().getTasaEsp());	
		productoNuevo = (ProductoNuevoMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");
		
		verificarAprobar = (VerificarAprobarMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarAprobar");
		
		comentario = (ComentarioMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");		
		LOG.info("grabarDatos 2 ");
		if (editarCliente) {
			LOG.info("grabarDatos 3 ");
			datosCliente = (DatosClienteMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
			
		} else {
			LOG.info("grabarDatos 4 ");
			datosCliente = (DatosClienteMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosCliente");
		}		
		LOG.info("tasa Especial :"+expediente.getExpedienteTC().getTasaEsp());
		ClienteNatural clienteNatural = null;
		ClienteNatural clienteNaturalConyuge = null;
		
		LOG.info("expediente.getId()::::"+expediente.getId());
		
		//boolean esNuevo = expediente.getId()== 0;
		String esNuevoSesion= (String)getObjectSession(Constantes.NUEVO_CLIENTE);
		LOG.info("esNuevoSesion::::"+esNuevoSesion);
		boolean esNuevo = ((esNuevoSesion!=null && esNuevoSesion.equals("1"))?false:true);
		
		if (esNuevo) {
			LOG.info("grabarDatos 5 ::: esNuevo -> True");
			clienteNatural = new ClienteNatural();
			clienteNaturalConyuge = new ClienteNatural();
		} else {
			LOG.info("grabarDatos 6 ::: esNuevo -> false ");
			clienteNatural = expediente.getClienteNatural();
			//clienteNaturalConyuge = expediente.getExpedienteTC().getClienteNaturalConyuge();
		}
		LOG.info("grabarDatos 7 ");
		datosCliente.copiarDatosClienteNatural(clienteNatural);	
		
		//if(expediente.getExpedienteTC().getClienteNaturalConyuge()!=null)
			clienteNaturalConyuge = expediente.getExpedienteTC().getClienteNaturalConyuge();
	//	else
		//	LOG.info("conyuge es nulooooooo");
		
//		if(clienteNaturalConyuge!=null){
//			clienteNaturalConyuge=clienteNaturalBean.buscarPorTipoNumDoi(clienteNaturalConyuge).size()>0?clienteNaturalBean.buscarPorTipoNumDoi(clienteNaturalConyuge).get(0):null;
//			if(clienteNaturalConyuge!=null)
//				LOG.info("clienteNaturalConyuge : "+clienteNaturalConyuge.getNombre());
//			else
//				LOG.info("clienteNaturalConyuge es null 2");
//		}else{
//			clienteNaturalConyuge=null;
//			LOG.info("clienteNaturalConyuge es null 1");
//		}
				
		
		LOG.info("grabarDatos 10 ");
		List<String> idsCatRenta = datosCliente.getCategoriasRenta();		
		clienteNatural.setCategoriasRenta(new ArrayList<CategoriaRenta>());
		for (int i = 0, n = idsCatRenta.size(); i < n; i++) {
			clienteNatural.getCategoriasRenta().add(categoriaRentaBean.buscarPorId(Long.parseLong(idsCatRenta.get(i))));
		}
		if (clienteNatural.getCategoriasRenta().size() == 0) {
			clienteNatural.setCategoriasRenta(null);
		}
		
		System.out.println("gruposegmento Grabar : "+clienteNatural.getSegmento().getGrupoSegmento().getId());
		
	
		if (esNuevo) {
			clienteNatural = clienteNaturalBean.create(clienteNatural);
			if(clienteNatural.getEstadoCivil()!=null && clienteNatural.getEstadoCivil().getId()>0){
				if((clienteNatural.getEstadoCivil().getId()==Constantes.ESTADO_CIVIL_CASADO || 
						clienteNatural.getEstadoCivil().getId()==Constantes.ESTADO_CIVIL_CONVIVIENTE) 
						&& expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoOferta()!=null && 
						expediente.getExpedienteTC().getTipoOferta().getId()==Long.parseLong(Constantes.CODIGO_OFERTA_APROBADO))
					
					System.out.println("clienteNaturalConyuge Grabar : "+clienteNaturalConyuge);
				    if (clienteNaturalConyuge!=null) {
				        System.out.println("clienteNaturalConyuge.getTipoCliente() Grabar : "+clienteNaturalConyuge.getTipoCliente());
				    }
				if (clienteNaturalConyuge != null) {

					if (clienteNaturalConyuge.getTipoCliente() != null
							&& clienteNaturalConyuge.getTipoCliente().getId() > 0) {
						clienteNaturalConyuge = clienteNaturalBean
								.create(clienteNaturalConyuge);
					} else {
						ClienteNatural clienteNaturalConyuge2 = new ClienteNatural();
						clienteNaturalConyuge2.setTipoDoi(clienteNaturalConyuge
								.getTipoDoi());
						clienteNaturalConyuge2.setNumDoi(clienteNaturalConyuge
								.getNumDoi());
						clienteNaturalConyuge = clienteNaturalBean
								.create(clienteNaturalConyuge2);
					}
				}
				
				LOG.info("cli tipo doi: "+clienteNatural.getTipoDoi().getId());
				LOG.info("cli tipo doi descrip: "+clienteNatural.getTipoDoi().getDescripcion());				
			}else
				LOG.info("Cliente no tiene estado civil");	

			

		} else {
			clienteNaturalBean.edit(clienteNatural);
			clienteNaturalBean.edit(clienteNaturalConyuge);
		}

		verificarAprobar.copiarDatosExpediente();
		LOG.info("grabarDatos 13 ");
		expediente.setClienteNatural(clienteNatural);
		LOG.info("grabarDatos 14 ");
		productoNuevo.copiarDatosExpediente();		
		LOG.info("grabarDatos 15 ");
		comentario.copiarDatosExpediente();
		
		expediente.setAccion(accion);
				
		expediente.getExpedienteTC().setClienteNaturalConyuge(clienteNaturalConyuge);
		expediente.getExpedienteTC().setEstadoEnvWorkflowTc(null);
		expediente.getExpedienteTC().setEstadoTipoResol(null);
		expediente.getExpedienteTC().setTipoBuro(null);
		expediente.getExpedienteTC().setTipoScoring(null);
		expediente.getExpedienteTC().setTipoMonedaAprob(null);
		
		Tarea tarea = new Tarea();
		tarea.setId(Long.parseLong(Constantes.REGISTRAR_EXPEDIENTE_TAREA_1));		
		expediente.getExpedienteTC().setTarea(tarea);
		
		expediente.getExpedienteTC().setOficina(expediente.getExpedienteTC().getEmpleado().getOficina());

		RemoteUtils remoteUtils=new RemoteUtils();
		
		expediente.getExpedienteTC().setFechaT3(new Timestamp(remoteUtils.obtenerTimestampServidorProcess().getTimeInMillis()));
		
		LOG.info("ExpedienteTC->Tipo de Cambio");		
		TipoCambioCE objTipoCambioCE = null;
		objTipoCambioCE = obtenerTipoCambio(expediente.getEmpleado());
		if (objTipoCambioCE != null) {
			expediente.getExpedienteTC().setFechaTipoCambioExp(objTipoCambioCE.getFecha());
			expediente.getExpedienteTC().setTipoCambioExp(objTipoCambioCE.getMonto());
		}else{
			LOG.info("No hay tipo de cambio");
		}
		
		if (Constantes.ACCION_BOTON_REGISTRAR.equals(accion)) {
			LOG.info("grabarDatos 16 ");
			LOG.info("B-Aceptar");
			
			expediente.setEstado(new Estado());
			expediente.getEstado().setId(estado);
			
			expediente.setFecRegistro(new Timestamp(new Date().getTime()));
			// cambiar estado e historial a estado "Expediente registrado"
			if (esNuevo) {
				LOG.info("Guardar Expediente");
				expediente = expedienteBean.create(expediente);	
				LOG.info("Guardar AyudaMemoria");
				guardarAyudaMemoria(expediente);
			} else {				
				expedienteBean.edit(expediente);
			}
			Historial historialVO = ConvertExpediente.convertToHistorialVO(expediente);			
			/*INICIO REQUERIMIENTO 286 27.11.2014*/
			List<ToePerfilEstado> lstToePerfilEstado= new ArrayList<ToePerfilEstado>();
			lstToePerfilEstado=Util.recuperaValoresTOEPerfilEstado(expediente, toePerfilEstadoBeanLocal);
			ToePerfilEstado toePerfilEstado=new ToePerfilEstado();
			if (lstToePerfilEstado.size()>0){
				toePerfilEstado = lstToePerfilEstado.get(0);
			}else{
				toePerfilEstado = null;
			}
			/*FIN REQUERIMIENTO 286 27.11.2014*/
			historialVO.setFechaFin(expediente.getFecRegistro());
			historialVO.setFecRegistro(expediente.getFecRegistro());
			historialVO.setClienteNaturalConyuge(null);
			historialVO.setEstadoEnvWorkflowTc(null);
			historialVO.setEstadoTipoResol(null);
			historialVO.setTipoBuro(null);
			historialVO.setTipoScoring(null);
			historialVO.setTipoMonedaAprob(null);
			historialVO.setTarea(null);
			historialVO.setMotivoDevolucion(null);
			historialVO.getEstado().setId(Constantes.ESTADO_EXPEDIENTE_REGISTRADO_TAREA_1);
			/*INICIO REQUERIMIENTO 286 27.11.2014*/
			int ans=0;
			if (toePerfilEstado!=null){
				historialVO.setTiempoObjTC(toePerfilEstado.getTiempoObjTc().intValueExact());
				historialVO.setTiempoObjTE(toePerfilEstado.getTiempoObjTe().intValueExact());
				historialVO.setTiempoPreTC(toePerfilEstado.getTiempoPreTc().intValueExact());
				historialVO.setTiempoPreTE(toePerfilEstado.getTiempoPreTe().intValueExact());
				historialVO.setNomColumna(toePerfilEstado.getNomColumna());
				ans=toePerfilEstado.getTiempoObjTc().intValueExact()+toePerfilEstado.getTiempoObjTe().intValueExact();
				historialVO.setAns(ans);
			}
			else{
				historialVO.setTiempoObjTC(0);
				historialVO.setTiempoObjTE(0);
				historialVO.setTiempoPreTC(0);
				historialVO.setTiempoPreTE(0);
				historialVO.setNomColumna(null);
				historialVO.setAns(0);
			}
			/*FIN REQUERIMIENTO 286 27.11.2014*/
			
			
			historialBean.create(historialVO);

		}else if (Constantes.ACCION_BOTON_GRABAR.equals(accion)) {
			
			LOG.info("grabarDatos 17 ");
			LOG.info("B-Grabar");
			
			expediente.setEstado(new Estado());
			expediente.getEstado().setId(estado);
			if (esNuevo) {
				expediente.getExpedienteTC().setFlagRegistro(editarCliente ? Constantes.CHECK_SELECCIONADO
								: Constantes.CHECK_NO_SELECCIONADO);

				LOG.info("exp-crea");
				
				expediente = expedienteBean.create(expediente);
				LOG.info("sale-exp-crea: "+expediente.getId());

				// se guarda la ayuda memoria
				
				LOG.info("ayuda-Mem-crea");
				
				guardarAyudaMemoria(expediente);
				
				LOG.info("sale-ayuda-Mem-crea");
				
				Historial historialVO = ConvertExpediente.convertToHistorialVO(expediente);			
				/*INICIO REQUERIMIENTO 286 27.11.2014*/
				List<ToePerfilEstado> lstToePerfilEstado= new ArrayList<ToePerfilEstado>();
				lstToePerfilEstado=Util.recuperaValoresTOEPerfilEstado(expediente, toePerfilEstadoBeanLocal);
				ToePerfilEstado toePerfilEstado=new ToePerfilEstado();
				if (lstToePerfilEstado.size()>0){
					toePerfilEstado = lstToePerfilEstado.get(0);
				}else{
					toePerfilEstado = null;
				}
				/*FIN REQUERIMIENTO 286 27.11.2014*/
				historialVO.setFechaFin(new Timestamp(new Date().getTime()));
				
				historialVO.setClienteNaturalConyuge(null);
				historialVO.setEstadoEnvWorkflowTc(null);
				historialVO.setEstadoTipoResol(null);
				historialVO.setTipoBuro(null);
				historialVO.setTipoScoring(null);
				historialVO.setTipoMonedaAprob(null);
				historialVO.setTarea(null);
				historialVO.setMotivoDevolucion(null);
				historialVO.getEstado().setId(estado);
				/*INICIO REQUERIMIENTO 286 27.11.2014*/
				int ans=0;
				if (toePerfilEstado!=null){
					historialVO.setTiempoObjTC(toePerfilEstado.getTiempoObjTc().intValueExact());
					historialVO.setTiempoObjTE(toePerfilEstado.getTiempoObjTe().intValueExact());
					historialVO.setTiempoPreTC(toePerfilEstado.getTiempoPreTc().intValueExact());
					historialVO.setTiempoPreTE(toePerfilEstado.getTiempoPreTe().intValueExact());
					historialVO.setNomColumna(toePerfilEstado.getNomColumna());
					ans=toePerfilEstado.getTiempoObjTc().intValueExact()+toePerfilEstado.getTiempoObjTe().intValueExact();
					historialVO.setAns(ans);
				}
				else{
					historialVO.setTiempoObjTC(0);
					historialVO.setTiempoObjTE(0);
					historialVO.setTiempoPreTC(0);
					historialVO.setTiempoPreTE(0);
					historialVO.setNomColumna(null);
					historialVO.setAns(0);
				}
				/*FIN REQUERIMIENTO 286 27.11.2014*/
				historialBean.create(historialVO);
				
			} else {
				LOG.info("exp-edit");
				expedienteBean.edit(expediente);
				LOG.info("sale-exp-crea: "+expediente.getId());
			
				List<Historial> listHistorialVO = historialBean.buscarPorIdExpediente(expediente.getId());
				Historial objHistorial=null;
				for(Historial h : listHistorialVO){
					if(h.getEstado().getId()==estado){
						objHistorial=h;
						break;
					}
				}
				
				Historial historialVO = ConvertExpediente.convertToHistorialVO(expediente);	
				historialVO.setId(objHistorial.getId());
				historialVO.setFechaFin(new Timestamp(new Date().getTime()));
				historialVO.setClienteNaturalConyuge(null);
				historialVO.setEstadoEnvWorkflowTc(null);
				historialVO.setEstadoTipoResol(null);
				historialVO.setTipoBuro(null);
				historialVO.setTipoScoring(null);
				historialVO.setTipoMonedaAprob(null);
				historialVO.setTarea(null);
				historialVO.setMotivoDevolucion(null);
				historialVO.getEstado().setId(estado);
				
				historialBean.edit(historialVO);
				
			}
			

			
		} else { // Constantes.ACCION_BOTON_CANCELAR_OPERACION
			
			Historial historialVO = ConvertExpediente.convertToHistorialVO(expediente);			
			/*INICIO REQUERIMIENTO 286 27.11.2014*/
			List<ToePerfilEstado> lstToePerfilEstado= new ArrayList<ToePerfilEstado>();
			lstToePerfilEstado=Util.recuperaValoresTOEPerfilEstado(expediente, toePerfilEstadoBeanLocal);
			ToePerfilEstado toePerfilEstado=new ToePerfilEstado();
			if (lstToePerfilEstado.size()>0){
				toePerfilEstado = lstToePerfilEstado.get(0);
			}else{
				toePerfilEstado = null;
			}
			/*FIN REQUERIMIENTO 286 27.11.2014*/
			/*INICIO REQUERIMIENTO 286 27.11.2014*/
			int ans=0;
			if (toePerfilEstado!=null){
				historialVO.setTiempoObjTC(toePerfilEstado.getTiempoObjTc().intValueExact());
				historialVO.setTiempoObjTE(toePerfilEstado.getTiempoObjTe().intValueExact());
				historialVO.setTiempoPreTC(toePerfilEstado.getTiempoPreTc().intValueExact());
				historialVO.setTiempoPreTE(toePerfilEstado.getTiempoPreTe().intValueExact());
				historialVO.setNomColumna(toePerfilEstado.getNomColumna());
				ans=toePerfilEstado.getTiempoObjTc().intValueExact()+toePerfilEstado.getTiempoObjTe().intValueExact();
				historialVO.setAns(ans);
			}
			else{
				historialVO.setTiempoObjTC(0);
				historialVO.setTiempoObjTE(0);
				historialVO.setTiempoPreTC(0);
				historialVO.setTiempoPreTE(0);
				historialVO.setNomColumna(null);
				historialVO.setAns(0);
			}
			/*FIN REQUERIMIENTO 286 27.11.2014*/
			historialVO.setFechaFin(new Timestamp(new Date().getTime()));
			historialBean.create(historialVO);
		}

		
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this,
				clienteNatural, accion, estado);
		String tkiid = expedienteTCWPS.getTaskID();
		LOG.info("tkiid::::::"+tkiid);
		
		/**
		 * ===========================================================
		 * Bloque de revisión de nuevos
		 * archivos subidos, debe ir antes de enviar el mensaje
		 * al process.
		 */
		
		PanelDocumentosMB panelDocumentos = (PanelDocumentosMB)getObjectRequest("paneldocumentos");
		
		expedienteTCWPS = ayudaExpedienteTC.setFlagEnvioContent(expedienteTCWPS, panelDocumentos);
		
		LOG.debug("Se han subido archivos al expediente : " + expedienteTCWPS.getFlagEnvioContent());
		
		/** =============================================================== **/
			
		
		if (Constantes.ACCION_BOTON_REGISTRAR.equals(accion)) { // accion aceptar
			addObjectSession(Constantes.TIPO_MENSAJE,
					Constantes.TIPO_MENSAJE_REGISTRADO);
			
			String msg = obtenerMensajeBD(Constantes.ID_MENSAJE_EXPEDIENTE_REGISTRADO, Constantes.DESCRIPCION_MENSAJE_REGISTRADO);
			addObjectSession(Constantes.DESCRIPCION_MENSAJE_REGISTRADO, msg);
			
			if (tkiid == null) {
				 tkiid = remoteUtils.iniciarInstanciaProceso(String
						.valueOf(expediente.getId()), expedienteTCWPS);	
			} else {
				
				//remoteUtils.enviarExpedienteTC(tkiid, expedienteTCWPS); // Comentado anteriormente
				remoteUtils.completarTarea(tkiid, expedienteTCWPS);
			}
				
		}else { // accion guardar
			// si expediente nuevo
			LOG.info("esNuevo="+esNuevo);
			if (esNuevo) {
				LOG.info("*********************Es nuevooooo");
				addObjectSession(Constantes.TIPO_MENSAJE,
						Constantes.TIPO_MENSAJE_PREREGISTRADO);
				
				String msg = obtenerMensajeBD(Constantes.ID_MENSAJE_EXPEDIENTE_PRE_REGISTRADO, Constantes.DESCRIPCION_MENSAJE_PRE_REGISTRADO);
				addObjectSession(Constantes.DESCRIPCION_MENSAJE_PRE_REGISTRADO, msg);
				
				String grabarReutilizable = getObjectSession("grabarReutilizable") == null ? "" : (String)getObjectSession("grabarReutilizable");
				if("OK".equals(grabarReutilizable)){
					tkiid = remoteUtils.iniciarInstanciaProcesoTask(String.valueOf(expediente.getId()), expedienteTCWPS);
					expedienteTCWPS.setTaskID(tkiid);
					System.out.println("expedienteTCWPS.getTaskID():::::"+expedienteTCWPS.getTaskID());
					addObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION, expedienteTCWPS);
				}
				else{
					remoteUtils.iniciarInstanciaProceso(String.valueOf(expediente.getId()), expedienteTCWPS);
				}
					
	/*TODO		bandeja.iniciarInstanciaProceso(String.valueOf(expediente
						.getId()), expedienteTC);
		*/				
			} else { // si expediente no nuevo
				// obtener documentos pendientes
				LOG.info("*********************No es nuevooooo");
								
				String registrarExpediente = (String) getObjectSession(Constantes.REGISTRAR_EXPEDIENTE_SESION);
				if (registrarExpediente == null) {
					LOG.info("*********************registrarExpediente nulo");
					addObjectSession(Constantes.TIPO_MENSAJE,
							Constantes.TIPO_MENSAJE_PREREGISTRADO);
					
					String msg = obtenerMensajeBD(Constantes.ID_MENSAJE_EXPEDIENTE_PRE_REGISTRADO, Constantes.DESCRIPCION_MENSAJE_PRE_REGISTRADO);
					addObjectSession(Constantes.DESCRIPCION_MENSAJE_PRE_REGISTRADO, msg);
					
				} else {
					LOG.info("*********************registrarExpediente No nulo");
					addObjectSession(Constantes.TIPO_MENSAJE,
							Constantes.TIPO_MENSAJE_PENDIENTE);
				}
				
				
				LOG.info("nombre = "+expedienteTCWPS.getCliente().getNombre());
				LOG.info("nombre = "+expedienteTCWPS.getCliente().getApPaterno());
				LOG.info("nombre = "+expedienteTCWPS.getCliente().getApMaterno());
				remoteUtils.enviarExpedienteTC(tkiid, expedienteTCWPS);
					
			}
		}
				
		//Adjunta Documentos Expediente
		AyudaDocumento ayudaDocumento = new AyudaDocumento();
		ayudaDocumento.adjuntarDocumentoExpediente();
		
		ayudaDocumento.editarDocumentoExpediente();
		
		removeObjectSession(Constantes.LISTA_DOC_EXP_ADJ);
		removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
		String grabarReutilizable = getObjectSession("grabarReutilizable") == null ? "" : (String)getObjectSession("grabarReutilizable");
		removeObjectSession("grabarReutilizable");
		if(!"OK".equals(grabarReutilizable)){
			removeObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION);
		}
		
		//todos los documentos se actualizan como no observados
        panelDocumentoMB = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
        panelDocumentoMB.actualizarNoObservados();
        
        //para reutilizable
        addObjectSession("expReu", expediente);
        addObjectSession("numeroDOIReu", clienteNatural.getNumDoi());
        addObjectSession("tipoDOIReu", String.valueOf(clienteNatural.getTipoDoi().getId()));
		
		//Remueve el tipo y numero de documento
		removeObjectSession("numeroDOI");
		removeObjectSession("tipoDOI");
		return "/mensajes/formMensajes?faces-redirect=true";
	}

	private TipoCambioCE obtenerTipoCambio(Empleado empleado) {
		
		TipoCambioCE objTipoCambioCE = null;
		LOG.info(" Verificando Tipo de Cambio");
		String strFecha = Util.formatDateObject("dd/MM/yyyy", new Date());
		LOG.info("Fecha Actual:" + strFecha);
		Date fecha = Util.parseStringToDate(strFecha, "dd/MM/yyyy");		
		objTipoCambioCE = tipoCambioCEBeanLocal.buscarPorFecha(fecha);		
		if (objTipoCambioCE == null) {
			LOG.info("Se intenta obtener el tipo de cambio llamando al servicio");
			objTipoCambioCE = null;
			CtExtraerTipoCambioRq inCtExtraerTipoCambioRq  	= null;
			CtExtraerTipoCambioRs outCtExtraerTipoCambioRs  = null;
			CtHeader ctHeader = null;
			CtBodyRq CtBodyRq = null;
			
			inCtExtraerTipoCambioRq = new CtExtraerTipoCambioRq();	
			outCtExtraerTipoCambioRs = new CtExtraerTipoCambioRs();
			ctHeader = new CtHeader();
			CtBodyRq = new CtBodyRq();
			
			strFecha = Util.formatDateObject("dd.MM.yyyy", new Date());
			String tipoCambioDivisa = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_TIPO_CAMBIO_DIVISA").getValorVariable();
			LOG.info("Fecha solicitud: = " + strFecha);
			LOG.info("Tipo Cambio Solicitud = " + tipoCambioDivisa);
			ctHeader.setUsuario(empleado.getCodigo());			
			CtBodyRq.setFechaCambio(strFecha);
			CtBodyRq.setTipoCambio(tipoCambioDivisa);
			 
			inCtExtraerTipoCambioRq.setHeader(ctHeader);
			inCtExtraerTipoCambioRq.setData(CtBodyRq);
			LOG.info("-------------------------- INICIO CONSULTA AL SERVICIO EXTRAER TIPO CAMBIO ----------------------------");	
			long tiempoInicio = System.currentTimeMillis(); 
			try{
			 LOG.info("DATOS REQUEST SERVICIO TIPO CAMBIO: " +
			 		"["+ "Codigo=" + ctHeader.getUsuario() +"; " 
					   + "Fecha Cambio=" + CtBodyRq.getFechaCambio() + "]");		 
			 outCtExtraerTipoCambioRs = bbvaFacade.extraerTipoCambio(inCtExtraerTipoCambioRq);
			 
			}catch(Throwable e){
			 LOG.error("Ocurrio un error de conexion con el servicio TIPO CAMBIO " + e);
			}
			long tiempoFin = System.currentTimeMillis();
			LOG.info("RegistrarExpedienteMB.obtenerTipoCambio extraerTipoCambio demora: " + (tiempoFin - tiempoInicio) + " milisegundos");
			LOG.info("-------------------------- FIN CONSULTA AL SERVICIO EXTRAER TIPO CAMBIO ----------------------------");
		
			if(outCtExtraerTipoCambioRs != null 
					&& outCtExtraerTipoCambioRs.getData() != null){
				LOG.info("DATOS RESPONSE EXTRAER TIPO CAMBIO: " +	"["+"TipoCambioCE=" + outCtExtraerTipoCambioRs.getData().getFechaCambio() 
						+ " " + outCtExtraerTipoCambioRs.getData().getTipoCambio() + "]");
				LOG.info("Fecha WS:" + outCtExtraerTipoCambioRs.getData().getFechaCambio());
				LOG.info("Monto WS:" + outCtExtraerTipoCambioRs.getData().getTipoCambio());
				CtTipos tipos = outCtExtraerTipoCambioRs.getData().getTipos();
				List<CtTipoCambio> lstCambios = tipos.getTipoCambio();
				for (int i = 0; i < lstCambios.size() ; i++) {
					CtTipoCambio cambio = lstCambios.get(i);
					if (cambio.getDivisa().trim().equals("USD")) {
						objTipoCambioCE = new TipoCambioCE();
						Double monto = Util.convertStringToDouble(cambio.getFixing(), ',', ' ');
						LOG.info("Monto Fixing Tipo Cambio:" + monto);
						objTipoCambioCE.setMonto(BigDecimal.valueOf(monto));
						String strOutFechaTC = outCtExtraerTipoCambioRs.getData().getFechaCambio();
						objTipoCambioCE.setFecha(Util.parseStringToDate(strOutFechaTC.replace('.', '/'), "dd/MM/yyyy"));
						i = lstCambios.size();
					}
					
				}
				LOG.info("TIPO CAMBIO--> Fecha:" + objTipoCambioCE.getFecha() + " Monto:" + objTipoCambioCE.getMonto());
				LOG.info("Insertamos el tipo de cambio extraido del servicio");
				tipoCambioCEBeanLocal.create(objTipoCambioCE);		
				LOG.info("Tipo de cambio para la fecha " + strFecha + " insertado en BD.");
			}
//			if (objTipoCambioCE == null) {
//				LOG.info("Obtenemos el tipo de cambio TOP.");			
//				objTipoCambioCE = tipoCambioCEBeanLocal.buscarTop();				
//			}
		}else{
			LOG.info("Ya existe tipo de cambio para la fecha actual.");
		}		
		//LOG.info("Tipo de cambio === Fecha:" + objTipoCambioCE.getFecha() + " Monto: " + objTipoCambioCE.getMonto());
		return objTipoCambioCE;
	}

	private void guardarAyudaMemoria(Expediente exp) {		
		List<AyudaMemoria> listAyudaMemoria = (List<AyudaMemoria>)getObjectSession(Constantes.AYUDA_MEMORIA_SESION);
		if (listAyudaMemoria != null) {
			for (AyudaMemoria ayudaMemoria : listAyudaMemoria) {
				ayudaMemoria.setId(0);
				ayudaMemoria.setExpediente(exp);
				ayudaMemoriaBean.create(ayudaMemoria);
			}
			removeObjectSession(Constantes.AYUDA_MEMORIA_SESION);
		}
	}

	//----------------------------- Cambbios Emitson ------------------------------
	public void validaPautaClasificacionSBS2() {	
		LOG.info("validaPautaClasificacionSBS2");
		
		String idEstadoCivil = this.retornaEstadoCivil();
		FacesContext ctx = FacesContext.getCurrentInstance();
		ProductoNuevoMB productoNuevoMB = (ProductoNuevoMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");		
		productoNuevoMB.validaPautaClasificacion(idEstadoCivil);
			
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
	//----------------------------- Fin Cambbios Emitson ------------------------------
	
	public boolean isMsgGuiaDocumentaria() {
		return msgGuiaDocumentaria;
	}

	public void setMsgGuiaDocumentaria(boolean msgGuiaDocumentaria) {
		this.msgGuiaDocumentaria = msgGuiaDocumentaria;
	}
	
	public void visualizarPanelConyuge (boolean isVisible) {
		LOG.info("entro a visualizarPanelConyuge con : "+isVisible );
		mostrarPanelConyuge = isVisible;
	}
	
	public void visualizarDatosPanelConyuge (boolean isVisible) {
		mostrarPanelDatosConyuge = isVisible;
	}
	
	public boolean isMostrarPanelConyuge() {
		return mostrarPanelConyuge;
	}

	public void setMostrarPanelConyuge(boolean mostrarPanelConyuge) {
		LOG.info("entro a setMostrarPanelConyuge con : "+mostrarPanelConyuge );
		this.mostrarPanelConyuge = mostrarPanelConyuge;
	}
	public boolean isMostrarPanelDatosConyuge() {
		return mostrarPanelDatosConyuge;
	}

	public void setMostrarPanelDatosConyuge(boolean mostrarPanelDatosConyuge) {
		this.mostrarPanelDatosConyuge = mostrarPanelDatosConyuge;
	}

	public boolean isActivoAceptar() {
		return activoAceptar;
	}

	public void setActivoAceptar(boolean activoAceptar) {
		this.activoAceptar = activoAceptar;
	}	
	
	public boolean isMostrarPaneles() {
		return mostrarPaneles;
	}

	public void setMostrarPaneles(boolean mostrarPaneles) {
		this.mostrarPaneles = mostrarPaneles;
	}

	public boolean isEditarCliente() {
		return editarCliente;
	}

	public void setEditarCliente(boolean editarCliente) {
		this.editarCliente = editarCliente;
	}

	public boolean isItemDisabledSubrogacion() {
		return itemDisabledSubrogacion;
	}

	public void setItemDisabledSubrogacion(boolean itemDisabledSubrogacion) {
		this.itemDisabledSubrogacion = itemDisabledSubrogacion;
	}

	public String getTipoDoi() {
		return tipoDoi;
	}

	public void setTipoDoi(String tipoDoi) {
		this.tipoDoi = tipoDoi;
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
		LOG.info("En el metodo pinWebSeal de registrar expediente - UNIFICADO");
	}

	
}
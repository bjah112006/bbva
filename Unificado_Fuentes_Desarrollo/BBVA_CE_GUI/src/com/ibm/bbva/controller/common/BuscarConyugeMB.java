package com.ibm.bbva.controller.common;

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

import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.RegistrarExpedienteMB;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Persona;
import com.ibm.bbva.entities.Segmento;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.harec.client.BodyRq;
import com.ibm.bbva.harec.client.DatosGeneralesXPersonaRq;
import com.ibm.bbva.harec.client.DatosGeneralesXpersonaRs;
import com.ibm.bbva.harec.client.HeadRq;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.SegmentoBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import com.ibm.bbva.session.TipoDoiBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "buscarConyuge")
@RequestScoped
public class BuscarConyugeMB extends AbstractMBean {

	@EJB
	private TipoDoiBeanLocal tipoDoiBean;
	@EJB
	private ClienteNaturalBeanLocal clienteNaturalBean;
	@EJB	
	private SegmentoBeanLocal segmentoBean;
	@EJB
	private TipoClienteBeanLocal tipoClienteBean;
	@EJB
	private BBVAFacadeLocal bbvaFacade;

	private static final int CARACTERES_DNI = 8;
	private static final int CARACTERES_RUC = 11;
	private static final int MAX_CARACTERES_OTROS = 10;

	private List<SelectItem> tiposDOI;
	private List<TipoDoi> listTipoDoi;
	private String tipoDOISeleccionado;
	private String numeroDOI;
	private String sizeNumeroDOI;
	
	public boolean estBuscar;
	public boolean mostrarBuscarConyuge;

	private static final Logger LOG = LoggerFactory.getLogger(BuscarConyugeMB.class);

	public BuscarConyugeMB() {
	}

	@PostConstruct
	public void init() {
		
		LOG.info("Entro INIT BUSCAR CONYUGE");
		estBuscar=false;
		
		listTipoDoi= tipoDoiBean.buscarTodos();
		tiposDOI = Util.crearItems(listTipoDoi, true, "id","descripcion");
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		
		/**
		 * @author Daniel Flores
		 * Obtener los datos de conyugue del cliente y mostrarlos
		 * en el formulario.
		 */
		
		FacesContext ctx = FacesContext.getCurrentInstance();  

		DatosClienteMB datosClienteMB = (DatosClienteMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosCliente");
		
		if(datosClienteMB.isCasado()){
			if(expediente.getExpedienteTC().getClienteNaturalConyuge() != null){
				tipoDOISeleccionado = String.valueOf(expediente.getExpedienteTC().getClienteNaturalConyuge().getTipoDoi().getId());
				actSizeTipoDOIConyuge(null);
				numeroDOI = expediente.getExpedienteTC().getClienteNaturalConyuge().getNumDoi();
				mostrarBuscarConyuge = true;
				estBuscar = true;
			}
		};
			
		if(tipoDOISeleccionado == null || tipoDOISeleccionado.equals("") || 
				tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))
			actSizeTipoDOIConyuge(null);
		else
			LOG.info("tipoDOISeleccionado = "+tipoDOISeleccionado);
		
	}

	public void actSizeTipoDOIConyuge(AjaxBehaviorEvent event) {
		LOG.info("actSizeTipoDOI");

//		FacesContext ctx = FacesContext.getCurrentInstance(); 
//		RegistrarExpedienteMB registrarExpediente = (RegistrarExpedienteMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente"); //JBTA
//		registrarExpediente.setMostrarPanelConyuge(true); 
		
		if(listTipoDoi != null && listTipoDoi.size()>0) {
			String codigoTipoDoiSelecc;
			
			if(tipoDOISeleccionado == null || tipoDOISeleccionado.equals("") || 
						tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
				codigoTipoDoiSelecc=Constantes.CODIGO_TIPO_DOI_DNI;
			}else{
				codigoTipoDoiSelecc=tipoDOISeleccionado;
			}//fin if tipoDOISeleccionado
			
			LOG.info("codigoTipoDoiSelecc ... "+codigoTipoDoiSelecc);
			/*
			for(TipoDoi item : listTipoDoi){
				if(item.getCodigo().toString().equals(codigoTipoDoiSelecc)) {
					tipoDOISeleccionado = codigoTipoDoiSelecc;
					sizeNumeroDOI=String.valueOf(item.getLongitud());
					numeroDOI="";
					LOG.info("sizeNumeroDOI ... "+tipoDOISeleccionado+" es ..."+sizeNumeroDOI);
					break;
				}						
			}//fin for
			*/
			for(TipoDoi item : listTipoDoi){
				if(item.getCodigo().toString().equals(codigoTipoDoiSelecc)) {
					tipoDOISeleccionado = codigoTipoDoiSelecc;
					
					if(item.getLongitud()!=null){
						java.math.BigDecimal bg = new java.math.BigDecimal(String.valueOf("0"));
						int res = item.getLongitud().compareTo(bg); // compare bg1 with bg2
						LOG.info("res "+res);
						if( res != 1 )// si longitud no es mayor que cero
							sizeNumeroDOI=String.valueOf(MAX_CARACTERES_OTROS);
						else
							sizeNumeroDOI=String.valueOf(item.getLongitud());
					}else{
						LOG.info("Longitud de TipoDOI "+tipoDOISeleccionado+" es nulo");
						sizeNumeroDOI=String.valueOf(MAX_CARACTERES_OTROS);
					}
					
					numeroDOI="";
					LOG.info("sizeNumeroDOI ... "+tipoDOISeleccionado+" es ..."+sizeNumeroDOI);	
					break;

				}	//if comparator codigo					
			}//fin for			
		}else
			LOG.info("listTipoDoi es nulo o vacío ... ");

	}
	public void buscar(AjaxBehaviorEvent event) { 
	    LOG.info("BUSCAR CONYUGE");
	    
		FacesContext ctx = FacesContext.getCurrentInstance(); 
		estBuscar=false;
		RegistrarExpedienteMB registrarExpediente = (RegistrarExpedienteMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente"); //JBTA
		//RegistrarExpedienteMB regExpMB = (RegistrarExpedienteMB) getObjectRequest("registrarExpediente");
		String nombJSP = getNombreJSPPrincipal();
		String idMsgError = "";
		if (nombJSP.equals("formRegistrarExpediente")) {
			idMsgError = "frmRegistrarExpediente";
		} else if (nombJSP.equals("formRegularizarEscanearDocumentacion")) {
			idMsgError = "frmRegularizarEscanearDocumentacion";
		} else if (nombJSP.equals("formEvaluarDevolucionRiesgos")) {
			idMsgError = "frmEvaluarDevolucionRiesgos";
		} else if (nombJSP.equals("formCoordinarClienteSubsanar")) {
			idMsgError = "frmCoordinarClienteSubsanar";
		} else if (nombJSP.equals("formConsultarClienteModificaciones")) {
			idMsgError = "frmConsultarClienteModificaciones";
		}
		LOG.info("idMsgError - "+idMsgError);

//		if (nombJSP.equals("formConsultarClienteModificaciones")
//				|| nombJSP.equals("formCoordinarClienteSubsanar")
//				|| nombJSP.equals("formEvaluarDevolucionRiesgos")
//				|| nombJSP.equals("formGestionarSubsanarOperacion")
//				|| nombJSP.equals("formRegistrarExpediente")
//				|| nombJSP.equals("formRegistrarExpedienteCu23")
//				|| nombJSP.equals("formRegistrarExpedienteCu25")
//				|| nombJSP.equals("formRegularizarEscanearDocumentacion")) {
//			/*
//			 * TODO DocumentoReutilizableMB docReu = (DocumentoReutilizableMB)
//			 * getObjectSession("documentoReutilizable");
//			 * docReu.limpiarListaReu();
//			 */
//		}

		// Se verifican valores ingresados
		boolean verificarConsistencia = true;
		LOG.info("verificarConsistencia 1 - "+verificarConsistencia);
		if (tipoDOISeleccionado == null
				|| tipoDOISeleccionado
						.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			addMessageError(idMsgError + ":selectTipoDOIC",
					"com.ibm.bbva.common.buscarCliente.msg.seleccion.tipoDOI");
			LOG.info("selectTipoDOIC VACIO ");
			verificarConsistencia = false;
			LOG.info("verificarConsistencia 2 - ");
		}
		if (numeroDOI == null || numeroDOI.trim().equals("")) {
			addMessageError(idMsgError + ":numeroDOIC",
					"com.ibm.bbva.common.buscarCliente.msg.numero");
			verificarConsistencia = false;
			LOG.info("numeroDOIC VACIO ");
		}
		// Cuando los valores de los campos son ingresados se valida la
		// consistencia entre ambos
		LOG.info("verificarConsistencia 2"+verificarConsistencia);
		if (verificarConsistencia) {
			LOG.info("verificarConsistencia 3 - ");
			//TipoDoi filtro = new TipoDoi();
			//filtro = tipoDoiBean.buscarPorId(Integer
				//	.parseInt(tipoDOISeleccionado));
			String codTipoDoi = tipoDOISeleccionado.trim();//filtro.getCodigo();

			if (codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_DNI)
					&& numeroDOI.length() != CARACTERES_DNI) {
				addMessageError(idMsgError + ":numeroDOIC",
						"com.ibm.bbva.common.buscarCliente.msg.caracteres",
						"DNI", CARACTERES_DNI);
				verificarConsistencia=false;
				LOG.info("verificarConsistencia 4 - ");
			} else if (codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_RUC)
					&& numeroDOI.length() != CARACTERES_RUC) {
				addMessageError(idMsgError + ":numeroDOIC",
						"com.ibm.bbva.common.buscarCliente.msg.caracteres",
						"RUC", CARACTERES_RUC);
				verificarConsistencia=false;
				LOG.info("verificarConsistencia 5 - ");
			} else if (!codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_DNI)
					&& !codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_RUC)
					&& numeroDOI.length() > Integer.parseInt(sizeNumeroDOI)) {
				addMessageError(idMsgError + ":numeroDOIC",
						"com.ibm.bbva.common.buscarCliente.msg.maxCaracteres",
						sizeNumeroDOI);
				verificarConsistencia=false;
				LOG.info("verificarConsistencia 6 - ");
			} else {
				// DESCOMENTAR
				  obtenerDatosCliente();
				
				
				LOG.info("verificarConsistencia 7 - ");
			}
			
			LOG.info("verificarConsistencia 2 "+verificarConsistencia);
		}
			
		LOG.info("tipoDOISeleccionado : "+tipoDOISeleccionado);
		LOG.info("numeroDOI : "+numeroDOI);	
			
		addObjectSession("numeroDOI", numeroDOI);
		addObjectSession("tipoDOI", tipoDOISeleccionado);
		
		DatosConyugeMB 	datosConyuge=null;	
		datosConyuge = (DatosConyugeMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosConyuge");
		
		registrarExpediente.setMostrarPanelConyuge(true); //JBTA
		
		if(verificarConsistencia){
			
			//regExpMB.visualizarDatosPanelConyuge(true);
			LOG.info("visualizarDatosPanelConyuge SI");
			//comentario = (ComentarioMB)  
				//	ctx.getApplication().getVariableResolver().resolveVariable(ctx, "comentario");
			datosConyuge.setMostrarDatosConyugePrincipal(true);			
			addObjectSession("showDatosConyuge", "1");
		}else{
			datosConyuge.setMostrarDatosConyugePrincipal(false);	
			LOG.info("visualizarDatosPanelConyuge NO");
			addObjectSession("showDatosConyuge", "0");
			//regExpMB.visualizarDatosPanelConyuge(true);
		}
			
		LOG.info("fin : ");	
	}

	private void obtenerDatosCliente() {
		// ManageBean principal
		RegistrarExpedienteMB regExpMB = (RegistrarExpedienteMB) getObjectRequest("registrarExpediente");
		// si los datos son diferentes se realiza nuevamente la busqueda
		
			// se obtiene el tipo doi
			TipoDoi tipoDoi = new TipoDoi();
			tipoDoi = tipoDoiBean.buscarPorId(Integer.parseInt(tipoDOISeleccionado));
			
			// si todos los datos estan correctos se busca en los servicios
			ClienteNatural clienteNatural = new ClienteNatural();
			clienteNatural.setSegmento(new Segmento());
			clienteNatural.setTipoCliente(new TipoCliente());
			clienteNatural.setTipoDoi(tipoDoi);

			// fuente de datos
			int fuente = RegistrarExpedienteMB.DATOS_NUEVO;
			LOG.info("fuente = "+fuente);

			 Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION); 
			 DatosGeneralesXPersonaRq inDatosGeneralesXPersona = null;	
			 DatosGeneralesXpersonaRs OutDatosGeneralesXPersona = null;
			 HeadRq headRq = null;
			 BodyRq bodyRq = null;
			 inDatosGeneralesXPersona = new DatosGeneralesXPersonaRq();	
			 OutDatosGeneralesXPersona = new DatosGeneralesXpersonaRs();
			 headRq = new HeadRq();
			 bodyRq = new BodyRq();
			 headRq.setUsuario(empleado.getCodigo());
			 bodyRq.setTipoDOI(tipoDoi.getCodigoTipoDoi());
			 bodyRq.setNroDOI(numeroDOI);
			 
			 inDatosGeneralesXPersona.setHead(headRq);
			 inDatosGeneralesXPersona.setBody(bodyRq);			 
			 
			 LOG.info("-------------------------- INICIO CONSULTA AL SERVICIO HAREC ----------------------------");	
			 try{
				 LOG.info("DATOS REQUEST HAREC: " +
					 		"["+ "Usuario="+headRq.getUsuario() +"; " 
							   + "NumDOI="+bodyRq.getNroDOI()+"; "
							   + "TipoDOI="+bodyRq.getTipoDOI() +"]");
				 	OutDatosGeneralesXPersona = bbvaFacade.consultaDatosHarec(inDatosGeneralesXPersona);
				
			 }catch(Throwable e){
				 LOG.error("Ocurrio un error de conexion con el servicio Harec : BBVA_RENIEC_WSDLSOAP_HTTP_Service"+ e);
			 }	
			 LOG.info("-------------------------- FIN CONSULTA AL SERVICIO HAREC ----------------------------");
			 
			 if(OutDatosGeneralesXPersona!=null && !OutDatosGeneralesXPersona.getBody().getResultado().equals(Constantes.CODIGO_EXITO_HAREC)){
				 LOG.info("Resultado:" + "["+ OutDatosGeneralesXPersona.getBody().getResultado()+"] -" + "No existen datos en HAREC...");
			 }else{
				 if (OutDatosGeneralesXPersona!=null)
					 LOG.info("Resultado:" + "["+ OutDatosGeneralesXPersona.getBody().getResultado()+"] -" + "Consulta exitosa de HAREC...");
				 else
					 LOG.info("OutDatosGeneralesXPersona: null");
			 }
				 
			 if(OutDatosGeneralesXPersona!=null && OutDatosGeneralesXPersona.getBody().getResultado().equals(Constantes.CODIGO_EXITO_HAREC)){
					 
				 LOG.info("DATOS RESPONSE HAREC: " +	"["+"Persona="+OutDatosGeneralesXPersona.getBody().getNombres()+" "+OutDatosGeneralesXPersona.getBody().getApellidoPaterno()+" "+OutDatosGeneralesXPersona.getBody().getApellidoMaterno()+"]");
					 
					 clienteNatural.setApePat(OutDatosGeneralesXPersona.getBody().getApellidoPaterno());			    
				     clienteNatural.setApeMat(OutDatosGeneralesXPersona.getBody().getApellidoMaterno());			    
				     clienteNatural.setNombre(OutDatosGeneralesXPersona.getBody().getNombres());
				     clienteNatural.setCodCliente(OutDatosGeneralesXPersona.getBody().getCodigoCentral());			     
				     clienteNatural.setNumDoi(OutDatosGeneralesXPersona.getBody().getDoi().getNumero());
				 
				     if(OutDatosGeneralesXPersona.getBody().getSegmento().getCodigo() != null && !OutDatosGeneralesXPersona.getBody().getSegmento().getCodigo().equals("")){
			    	 
					    	// se obtiene el segmento 
					    	try{ 
						    	 Segmento segmento = segmentoBean.buscarPorCodigoSegmento(OutDatosGeneralesXPersona.getBody().getSegmento().getCodigo());
						    	 LOG.info("SEGMENTO ... "+OutDatosGeneralesXPersona.getBody().getSegmento().getCodigo());
							     clienteNatural.setSegmento(segmento);
					    	}catch(Exception e){
					    		LOG.error(e.getMessage(), e);
					    	}
						     
				     }else{		
				    	 
					    	// se obtiene el segmento 
					    	try{ 
						    	 Segmento segmento = segmentoBean.buscarPorCodigoSegmento(Constantes.CODIGO_SEGMENTO_SIN_ASIGNAR);
						    	 LOG.info("SEGMENTO ... "+Constantes.CODIGO_SEGMENTO_SIN_ASIGNAR);
							     clienteNatural.setSegmento(segmento);
					    	}catch(Exception e){
					    		LOG.error(e.getMessage(), e);
					    	}				    	 

				     }
			     
				     // se obtiene los datos de tipoCLiente TipoClienteVO filtTC = new
				     TipoCliente tipoCliente = tipoClienteBean.buscarPorId(Long.valueOf(Constantes.CODIGO_TIPO_CLIENTE_BBVA));
				     clienteNatural.setTipoCliente(tipoCliente);
			 
	   			     fuente = RegistrarExpedienteMB.DATOS_HAREC;
	   			     
	   			  OutDatosGeneralesXPersona=null;
	   			  inDatosGeneralesXPersona=null;
	   			  
			 }else if (tipoDoi.getCodigo().equals(Constantes.CODIGO_TIPO_DOI_DNI)){ 
				 
				   LOG.info("Consultar Cliente en RENIEC...");
				  
		    	   com.ibm.bbva.reniec.message.ConsultaPorDNIRequest inConsultaReniecPorDNI = null;
		    	   com.ibm.bbva.reniec.message.ConsultaPorDNIResponse outConsultaReniecPorDNI = null;
		    	   com.ibm.bbva.reniec.header.RequestHeader header = null;
		    	   com.ibm.bbva.reniec.consultapordni.ConsultaPorDNIRequest body = null;
		    	   
		    	   inConsultaReniecPorDNI = new com.ibm.bbva.reniec.message.ConsultaPorDNIRequest();
		    	   outConsultaReniecPorDNI = new com.ibm.bbva.reniec.message.ConsultaPorDNIResponse(); 
		    	   header = new com.ibm.bbva.reniec.header.RequestHeader();
		    	   body = new com.ibm.bbva.reniec.consultapordni.ConsultaPorDNIRequest();
		    	   
		    	   header.setCanal(Constantes.RENIEC_CANAL);
		    	   header.setCodigoAplicacion(Constantes.RENIEC_CODIGOAPP);
		    	   header.setIdEmpresa(Constantes.RENIEC_IDEMPRESA);			    	   
		    	   header.setUsuario(empleado.getCodigo());
		    	   header.setFechaHoraEnvio(Util.fechaTimeStamp());
		    	   header.setIdTransaccion(Util.getIdTx(Constantes.RENIEC_CODIGOAPP,empleado.getCodigo()));
		    	   header.setCodigoInterfaz(Constantes.RENIEC_CPERRENXDNI);
		    	   
		    	   body.setTipoAplicacion(Constantes.RENIEC_TIPOAPP);			    	   
		    	   body.setRegistroCodUsuario(empleado.getCodigo());			    	   
		    	   body.setNumeroDNIConsultado(numeroDOI);
		    	   body.setIndConsultaDatos(Constantes.RENIEC_INDCONSULTADATOS);
		    	   body.setIndConsultaFoto(Constantes.RENIEC_INDCONSULTAFOTO);
		    	   body.setIndConsultaFirma(Constantes.RENIEC_INDCONSULTAFIRMA);
		    	   
		    	   inConsultaReniecPorDNI.setRefRequestHeader(header);
		    	   inConsultaReniecPorDNI.setRefConsultaPorDNIRequest(body);
			       LOG.info("-------------------------- INICIO CONSULTA AL SERVICIO RENIEC ----------------------------");	
		    	   try{
		    		   LOG.info("DATOS REQUEST RENIEC: " +
			    		   		"["+"Canal="+header.getCanal() +"; " 
			    		   		+ "CodApp="+header.getCodigoAplicacion()+"; "
			    		   		+ "IdEmp="+header.getIdEmpresa()+"; "
			    				+ "Usuario="+header.getUsuario()+"; "
			    				+ "CodInterfaz="+header.getCodigoInterfaz()+"; "
			    				+ "FechaHoraEnvio="+header.getFechaHoraEnvio()+"; "
			    				+ "IdTransaccion="+header.getIdTransaccion()+"; "
		    				   	+ "TipoApp="+body.getTipoAplicacion()+"; "
		    				   	+ "RegCodUsuario="+body.getRegistroCodUsuario()+"; "
		    				   	+ "NumDNIConsultado="+body.getNumeroDNIConsultado()+"; "
			    				+ "Ind ConsultaDatos="+body.getIndConsultaDatos()+"; "
			    				+ "Ind ConsultaFoto="+body.getIndConsultaFoto()+"; "
			    				+ "Ind ConsultaFirma="+body.getIndConsultaFirma()+"]");
			    		   
		    		   outConsultaReniecPorDNI = bbvaFacade.consultaDatosReniec(inConsultaReniecPorDNI);
		    		   
		    	   }catch(Throwable e){
		    		   LOG.error("Ocurrio un error de conexion con el servicio Reniec : BBVA_RENIEC_WSDLSOAP_HTTP_Service"+ e);
		    	   }
		    	   LOG.info("-------------------------- FIN CONSULTA AL SERVICIO RENIEC ----------------------------");
								 
		    	   if(outConsultaReniecPorDNI.getRefResponseHeader()!= null && outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta().equalsIgnoreCase(Constantes.CODIGO_EXITO_RENIEC)){					 
		    		   LOG.info("Consulta exitosa de RENIEC...");
		    		   LOG.info("RESPONSE RENIEC: "+"["+"Codigo Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta()+";"
								+"Mensaje Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getMensajeRespuesta()+"]");
		    		   LOG.info("DATOS RESPONSE RENIEC: " +
							 	"["+ "Persona="+outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getNombres()+" "+outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getApellidoPaterno()+" "+outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getApellidoMaterno()+";"
							 	   + "Nº DOI="+outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getNumeroDNIConsultado()+"]");
									 
						 clienteNatural.setApePat(outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getApellidoPaterno());			    
					     clienteNatural.setApeMat(outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getApellidoMaterno());			    
					     clienteNatural.setNombre(outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getNombres());
					     clienteNatural.setNumDoi(outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getNumeroDNIConsultado());
					     clienteNatural.setCodCliente(Constantes.CODIGO_CLIENTE_NUEVO);
					     clienteNatural.setTipoCliente(tipoClienteBean.buscarPorId(Long.parseLong(Constantes.CODIGO_TIPO_CLIENTE_NUEVO)));
					     
					     // se obtiene el segmento 
				    	 try{ 
					    	 Segmento segmento = segmentoBean.buscarPorCodigoSegmento(Constantes.CODIGO_SEGMENTO_SIN_ASIGNAR);
					    	 LOG.info("SEGMENTO ... "+Constantes.CODIGO_SEGMENTO_SIN_ASIGNAR);
						     clienteNatural.setSegmento(segmento);
				    	 }catch(Exception e){
				    		LOG.error(e.getMessage(), e);
				    	 }	
					 
		   			     fuente = RegistrarExpedienteMB.DATOS_RENIEC;
					   			 
					}else{ 
					 
						 if(outConsultaReniecPorDNI.getRefResponseHeader() == null){
				    		 LOG.info("Ocurió un error con el servicio de RENIEC");
							 
						 }else{
							 LOG.info("No existen datos en RENIEC...");
							 LOG.info("RESPONSE RENIEC: "+"["+"Codigo Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta()+";"
									 +"Mensaje Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getMensajeRespuesta()+"]");
						 }					 
						 
						 clienteNuevo(clienteNatural);
						 fuente = RegistrarExpedienteMB.DATOS_NUEVO; 
					}
		    	   
		    	   outConsultaReniecPorDNI=null;
		    	   inConsultaReniecPorDNI=null;
			    	  
			 }
			
			// Para simular la búsqueda correcta por HAREC **COMENTAR EN PRODUCCION**
//			String apellidoPaterno = "";
//			String apellidoMaterno = "";
//			String nombres = "";
//			String codigoCentral = "";
//			if (numeroDOI.equals("99999999")) {
//				apellidoPaterno = "MERRY";
//				apellidoMaterno = "CRITMAS";
//				nombres = "NAVIDAD";
//				codigoCentral = "77784110";
//			} else if (numeroDOI.equals("88888888")) {
//				apellidoPaterno = "BENAVENTE";
//				apellidoMaterno = "JAUREGUI";
//				nombres = "BENITO";
//				codigoCentral = "77784108";
//			} else {
//				apellidoPaterno = "Yanac";
//				apellidoMaterno = "Talaverano";
//				nombres = "Bernabe";
//				codigoCentral = "100";
//			}
//			clienteNatural.setApePat(apellidoPaterno);
//			clienteNatural.setApeMat(apellidoMaterno);
//			clienteNatural.setNombre(nombres);
//			clienteNatural.setCodCliente(codigoCentral);
//			clienteNatural.setNumDoi(numeroDOI);
//			try {
//				Segmento segmento = segmentoBean.buscarPorCodigoSegmento(Constantes.CODIGO_SEGMENTO_SIN_ASIGNAR);
//				clienteNatural.setSegmento(segmento);
//			}catch(Exception e){
//				LOG.error(e.getMessage(), e);
//			}
//			TipoCliente tipoCliente = tipoClienteBean.buscarPorId(Long.valueOf(Constantes.CODIGO_TIPO_CLIENTE_BBVA));
//			clienteNatural.setTipoCliente(tipoCliente);
//			fuente = RegistrarExpedienteMB.DATOS_HAREC;
			//FIN codigo pruebas
			 
			LOG.info("**** DatosConyugeMB ");
			FacesContext ctx = FacesContext.getCurrentInstance();  
			DatosConyugeMB datosConyuge = null;
			
			datosConyuge = (DatosConyugeMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosConyuge");
			
			if (fuente == RegistrarExpedienteMB.DATOS_HAREC) {
				datosConyuge.setMostrarTituloConyugeA(true);
			}else{
				datosConyuge.setMostrarTituloConyugeA(false);
			}
			//Para Pruebas
			/* clienteNatural.setApePat("Yanac");			    
		     clienteNatural.setApeMat("Talaverano");			    
		     clienteNatural.setNombre("Bernabe");
		     clienteNatural.setCodCliente("100");			     
		     clienteNatural.setNumDoi("77777777");*/
		     //Eliminar lo de arriba - solo para pruebas
		     
			datosConyuge.setClienteNatural(clienteNatural);
			datosConyuge.setMensajeOperacion("");
			
			Persona persona = new Persona();
			persona.setId(Long.valueOf(Constantes.CODIGO_TIPO_PERSONA_CONYUGUE));
			clienteNatural.setPersona(persona);
			
			/**
			 * @author Daniel Flores
			 * El expediente debe ser modificado, en este caso lo consultan
			 * de sesión, por lo tanto debemos actualizar el objeto en
			 * ese ámbito
			 */
			LOG.info("Obteniendo expediente de sesión ...");
			
			Expediente expediente = (Expediente)getObjectSession(Constantes.EXPEDIENTE_SESION);
			expediente.getExpedienteTC().setClienteNaturalConyuge(clienteNatural);
			
			LOG.info("****expediente = "+expediente);
			
			if(expediente!=null){
				LOG.info("expediente es diff null");
				datosConyuge.copiarDatos(expediente);
			}
			
			LOG.info("Guardando expediente en sesión con datos de nuevo conyugue ...");
			
			addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);
			
			estBuscar=true;
			regExpMB.visualizarDatosPanelConyuge(true);
		
		LOG.info("Termino metodo");
		
	}
	
	private void clienteNuevo(ClienteNatural clienteNatural) {
		// se obtiene el segmento
		Segmento segmento = new Segmento();
		segmento = segmentoBean.buscarPorCodigo(Constantes.CODIGO_SEGMENTO_SIN_ASIGNAR);
		clienteNatural.setSegmento(segmento);
		
		// codigo para cliente nuevo
		clienteNatural.setCodCliente(Constantes.CODIGO_CLIENTE_NUEVO);		
	    clienteNatural.setTipoCliente(tipoClienteBean.buscarPorId(Long.parseLong(Constantes.CODIGO_TIPO_CLIENTE_NUEVO)));
	}

	public boolean isEstBuscar() {
		return estBuscar;
	}

	public void setEstBuscar(boolean estBuscar) {
		this.estBuscar = estBuscar;
	}

	public boolean isMostrarBuscarConyuge() {
		return mostrarBuscarConyuge;
	}

	public void setMostrarBuscarConyuge(boolean mostrarBuscarConyuge) {
		this.mostrarBuscarConyuge = mostrarBuscarConyuge;
	}
	public List<SelectItem> getTiposDOI() {
		return tiposDOI;
	}

	public void setTiposDOI(List<SelectItem> tiposDOI) {
		this.tiposDOI = tiposDOI;
	}

	public String getTipoDOISeleccionado() {
		return tipoDOISeleccionado;
	}

	public void setTipoDOISeleccionado(String tipoDOISeleccionado) {
		this.tipoDOISeleccionado = tipoDOISeleccionado;
	}

	public String getNumeroDOI() {
		return numeroDOI;
	}

	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
	}

	public List<TipoDoi> getListTipoDoi() {
		return listTipoDoi;
	}

	public void setListTipoDoi(List<TipoDoi> listTipoDoi) {
		this.listTipoDoi = listTipoDoi;
	}

	public String getSizeNumeroDOI() {
		return sizeNumeroDOI;
	}

	public void setSizeNumeroDOI(String sizeNumeroDOI) {
		this.sizeNumeroDOI = sizeNumeroDOI;
	}	
	
	
}
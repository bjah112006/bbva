package com.ibm.bbva.controller.common;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bpd.RemoteUtils;
import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.RegistrarExpedienteMB;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.GrupoSegmento;
import com.ibm.bbva.entities.Segmento;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.harec.client.BodyRq;
import com.ibm.bbva.harec.client.DatosGeneralesXPersonaRq;
import com.ibm.bbva.harec.client.DatosGeneralesXpersonaRs;
import com.ibm.bbva.harec.client.HeadRq;
import com.ibm.bbva.session.SegmentoBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import com.ibm.bbva.session.TipoDoiBeanLocal;
import com.ibm.bbva.util.AyudaExpedienteVO;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "buscarCliente")
@SessionScoped
public class BuscarClienteMB extends AbstractMBean {

	@EJB
	private TipoDoiBeanLocal tipoDoiBean;
	@EJB
	private SegmentoBeanLocal segmentoBean;
	@EJB
	private TipoClienteBeanLocal tipoClienteBean;
	@EJB
	private BBVAFacadeLocal bbvaFacade;

	private static final int CARACTERES_DNI = 8;
	private static final int CARACTERES_RUC = 11;
	private static final int MAX_CARACTERES_OTROS = 12;

	private List<SelectItem> tiposDOI;
	private List<TipoDoi> listTipoDoi;
	private String tipoDOISeleccionado;
	private String numeroDOI;
	private String sizeNumeroDOI;
	private boolean doiSeleccionadoRuc;	
	
	public boolean isDoiSeleccionadoRuc() {
		doiSeleccionadoRuc = false;
		if (tipoDOISeleccionado.equals("6"))
			doiSeleccionadoRuc = true;
		return doiSeleccionadoRuc;
	}

	public void setDoiSeleccionadoRuc(boolean doiSeleccionadoRuc) {
		this.doiSeleccionadoRuc = doiSeleccionadoRuc;
	}
	
	boolean flagEditar = true;
	
	private static final Logger LOG = LoggerFactory.getLogger(BuscarClienteMB.class);

	public BuscarClienteMB() {
	}

	@PostConstruct
	public void init() {
		/*List<TipoDoi> objTemp=new ArrayList<TipoDoi>();
		TipoDoi obj=new TipoDoi();
		obj.setCodigo("-1");
		obj.setDescripcion(">>Seleccione<<");
		objTemp.add(obj);*/
		listTipoDoi= tipoDoiBean.buscarTodos();
		tiposDOI = Util.crearItems(listTipoDoi, true, "id",
				"descripcion");
		//tiposDOI.add(objTemp);
		
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);

		if (expediente != null && expediente.getClienteNatural() != null
				&& expediente.getClienteNatural().getId() > 0) {
			
			tipoDOISeleccionado = String.valueOf(expediente.getClienteNatural().getTipoDoi().getId());
			numeroDOI = expediente.getClienteNatural().getNumDoi();
			

		}
		
		if(tipoDOISeleccionado == null || tipoDOISeleccionado.equals("") || 
				tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			actSizeTipoDOI(null);
		}
		else{
			LOG.info("tipoDOISeleccionado = "+tipoDOISeleccionado);
			actSizeTipoDOI(null);
		}
	/*	
		if(listTipoDoi != null && listTipoDoi.size()>0) {
			String codigoTipoDoiSelecc;
			
			if(tipoDOISeleccionado == null || tipoDOISeleccionado.equals("") || 
						tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
				codigoTipoDoiSelecc=Constantes.CODIGO_TIPO_DOI_DNI;
			}else{
				codigoTipoDoiSelecc=tipoDOISeleccionado;
			}//fin if tipoDOISeleccionado
			
			LOG.info("codigoTipoDoiSelecc ... "+codigoTipoDoiSelecc);
			
			for(TipoDoi item : listTipoDoi){
				if(item.getCodigo().toString().equals(codigoTipoDoiSelecc)) {
					tipoDOISeleccionado = codigoTipoDoiSelecc;
					sizeNumeroDOI=String.valueOf(item.getLongitud());
					LOG.info("sizeNumeroDOI ... "+tipoDOISeleccionado+" es ..."+sizeNumeroDOI);
					break;
				}						
			}//fin for
		}*/		
	}
	
	public void actSizeTipoDOI(AjaxBehaviorEvent event) {
		LOG.info("actSizeTipoDOI");

		if(listTipoDoi != null && listTipoDoi.size()>0) {
			String codigoTipoDoiSelecc;
			
			if(tipoDOISeleccionado == null || tipoDOISeleccionado.equals("") || 
						tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
				codigoTipoDoiSelecc=Constantes.CODIGO_TIPO_DOI_DNI;
			}else{
				codigoTipoDoiSelecc=tipoDOISeleccionado;
				
				FacesContext ctx = FacesContext.getCurrentInstance();  
				DatosClienteNuevoMB datosClienteNuevo = null;
				datosClienteNuevo = (DatosClienteNuevoMB)  
						 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosClienteNuevo");
				if (tipoDOISeleccionado.equals(Constantes.CODIGO_TIPO_DOI_RUC)){
					datosClienteNuevo.setRenderedNombre(false);
					datosClienteNuevo.setRenderedRazonSocial(true);
					datosClienteNuevo.setRenderedApellidos(true);
				}else{
					datosClienteNuevo.setRenderedNombre(true);
					datosClienteNuevo.setRenderedRazonSocial(false);
					datosClienteNuevo.setRenderedApellidos(false);
				}
				
				
			}//fin if tipoDOISeleccionado
			
			LOG.info("codigoTipoDoiSelecc ... "+codigoTipoDoiSelecc);
			
			
			
			
			for(TipoDoi item : listTipoDoi){
				if(item.getCodigo().toString().equals(codigoTipoDoiSelecc)) {
					tipoDOISeleccionado = codigoTipoDoiSelecc;
					
					if(item.getLongitud()!=null){
						java.math.BigDecimal bg = new java.math.BigDecimal(String.valueOf("0"));
						int res = item.getLongitud().compareTo(bg); // compare bg1 with bg2
						LOG.info("res "+res);
						if( res != 1 )// si longitud no es mayor que cero
							sizeNumeroDOI=String.valueOf(MAX_CARACTERES_OTROS);
						else {
							if(item.getLongitud().intValue() == 0){
								sizeNumeroDOI=String.valueOf(MAX_CARACTERES_OTROS);
							}
							else{
								sizeNumeroDOI=String.valueOf(item.getLongitud());
							}
						}
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

	public void buscar(ActionEvent ae) {
	//public void buscar(AjaxBehaviorEvent event) {	
		LOG.info("BUSCAR CLIENTE");
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarConyugeMB buscarConyuge = (BuscarConyugeMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarConyuge");
		String nombJSP = getNombreJSPPrincipal();
		String idMsgError = "";
		if (nombJSP.equals("formRegistrarExpediente")) {
			idMsgError = "frmRegistrarExpediente";
		}

		if (nombJSP.equals("formConsultarClienteModificaciones")
				|| nombJSP.equals("formCoordinarClienteSubsanar")
				|| nombJSP.equals("formEvaluarDevolucionRiesgos")
				|| nombJSP.equals("formGestionarSubsanarOperacion")
				|| nombJSP.equals("formRegistrarExpediente")
				|| nombJSP.equals("formRegistrarExpedienteCu23")
				|| nombJSP.equals("formRegistrarExpedienteCu25")
				|| nombJSP.equals("formRegularizarEscanearDocumentacion")) {
			/*
			 * TODO DocumentoReutilizableMB docReu = (DocumentoReutilizableMB)
			 * getObjectSession("documentoReutilizable");
			 * docReu.limpiarListaReu();
			 */
		}
		// Se verifican valores ingresados
		boolean verificarConsistencia = true;
		
		if (tipoDOISeleccionado == null
				|| tipoDOISeleccionado
						.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			addMessageError(idMsgError + ":selectTipoDOI",
					"com.ibm.bbva.common.buscarCliente.msg.seleccion.tipoDOI");
			verificarConsistencia = false;
		}
		if (numeroDOI == null || numeroDOI.trim().equals("")) {
			addMessageError(idMsgError + ":numeroDOI",
					"com.ibm.bbva.common.buscarCliente.msg.numero");
			verificarConsistencia = false;
		}
		// Cuando los valores de los campos son ingresados se valida la
		// consistencia entre ambos
		if (verificarConsistencia) {
			TipoDoi filtro = new TipoDoi();
			filtro = tipoDoiBean.buscarPorId(Integer
					.parseInt(tipoDOISeleccionado));
			String codTipoDoi = filtro.getCodigo();

			if(codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_DNI) && numeroDOI.length() != CARACTERES_DNI){
				addMessageError(idMsgError + ":numeroDOI", "com.ibm.bbva.common.buscarCliente.msg.caracteres", "DNI", CARACTERES_DNI);
				
			}else if(codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_RUC) && numeroDOI.length() != CARACTERES_RUC){
				addMessageError(idMsgError + ":numeroDOI", "com.ibm.bbva.common.buscarCliente.msg.caracteres", "RUC", CARACTERES_RUC);
				
			}else if(!codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_DNI)
					&& !codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_RUC)
					&& numeroDOI.length() > Integer.parseInt(sizeNumeroDOI)){
				addMessageError(idMsgError + ":numeroDOI", "com.ibm.bbva.common.buscarCliente.msg.maxCaracteres", sizeNumeroDOI);
				
			}else{
				obtenerDatosCliente();
				
			}
		}
		LOG.info("tipoDOISeleccionado : "+tipoDOISeleccionado);
		LOG.info("numeroDOI : "+numeroDOI);
		
		addObjectSession("numeroDOI", numeroDOI);
		addObjectSession("tipoDOI", tipoDOISeleccionado);
		
		//Pasar a sesion el nombre de la carpeta que contendra los documentos de dicho expediente
		Calendar fecha = Calendar.getInstance();
		int mes = fecha.get(Calendar.MONTH) + 1;
		String fechaAhora =String.valueOf("_"+fecha.get(Calendar.YEAR)+"-"+mes+"-"+
				//cal1.get(Calendar.DATE)+"_"+cal1.get(Calendar.HOUR)+"-"+cal1.get(Calendar.MINUTE)+"-"+cal1.get(Calendar.SECOND));
				fecha.get(Calendar.DAY_OF_MONTH)+"_"+fecha.get(Calendar.HOUR_OF_DAY)+"-"+fecha.get(Calendar.MINUTE));
		addObjectSession(Constantes.CARPETA_DOC_ESCANEADOS_POR_EXPEDIENTE, 
				numeroDOI+fechaAhora);
		
		if(buscarConyuge != null){ //JBTA
			buscarConyuge.init(); //JBTA
		}
		
		/*DocumentoReutilizableMB documentoReutilizableMB = (DocumentoReutilizableMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "documentoReutilizable");
		documentoReutilizableMB.obtenerDatos();*/
	}

	private void obtenerDatosCliente() {
		
		// ManageBean principal
		RegistrarExpedienteMB regExpMB = (RegistrarExpedienteMB) getObjectRequest("registrarExpediente");
		LOG.info("obtenerDatosCliente");
		// si los datos son diferentes se realiza nuevamente la busqueda
	
		LOG.info("!datosIgualAnterior");
		Expediente expediente = AyudaExpedienteVO.instanciar();
		
		Tarea tarea = new Tarea();
		tarea.setId(Long.parseLong(Constantes.REGISTRAR_EXPEDIENTE_TAREA_1));
		expediente.getExpedienteTC().setTarea(tarea);
		
		RemoteUtils remoteUtils=new RemoteUtils();
		Timestamp fecha = new Timestamp(remoteUtils.obtenerTimestampServidorProcess().getTimeInMillis());
		
		if (expediente.getExpedienteTC().getFechaT1()==null) {
		    //expediente.getExpedienteTC().setFechaT1(new Timestamp(new Date().getTime()));			    
			expediente.getExpedienteTC().setFechaT1(fecha);
		}
		
		if (expediente.getExpedienteTC().getFechaT2()==null) {
		    //expediente.getExpedienteTC().setFechaT2(new Timestamp(new Date().getTime()));
		    expediente.getExpedienteTC().setFechaT2(fecha);
		}
		
		LOG.info("T1 : "+expediente.getExpedienteTC().getFechaT1());
		LOG.info("T2 : "+expediente.getExpedienteTC().getFechaT2());
		addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);

		// se obtiene el tipo doi
		TipoDoi tipoDoi = new TipoDoi();
		tipoDoi = tipoDoiBean.buscarPorId(Integer.parseInt(tipoDOISeleccionado));
		LOG.info("tipoDoi:" + tipoDoi);
		// si todos los datos estan correctos se busca en los servicios
		ClienteNatural clienteNatural = new ClienteNatural();
		clienteNatural.setSegmento(new Segmento());
		clienteNatural.getSegmento().setGrupoSegmento(new GrupoSegmento());
		clienteNatural.setTipoCliente(new TipoCliente());
		clienteNatural.setTipoDoi(tipoDoi);

		// fuente de datos
		int fuente = RegistrarExpedienteMB.DATOS_NUEVO;

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
			 LOG.error("Ocurrio un error de conexion con el servicio Harec "+ e);
		 }
		 LOG.info("-------------------------- FIN CONSULTA AL SERVICIO HAREC ----------------------------");
		
		 if(OutDatosGeneralesXPersona!=null && OutDatosGeneralesXPersona.getBody()!=null && OutDatosGeneralesXPersona.getBody().getResultado()!=null && 
				 !OutDatosGeneralesXPersona.getBody().getResultado().equals(Constantes.CODIGO_EXITO_HAREC)){
			 LOG.info("Resultado:" + "["+ OutDatosGeneralesXPersona.getBody().getResultado()+"] -" + "No existen datos en HAREC...");
		 }else{
			 if(OutDatosGeneralesXPersona!=null && OutDatosGeneralesXPersona.getBody()!=null)
				 LOG.info("Resultado:" + "["+ OutDatosGeneralesXPersona.getBody().getResultado()+"] -" + "Consulta exitosa de HAREC...");
		 }
			 
		 if(OutDatosGeneralesXPersona!=null && OutDatosGeneralesXPersona.getBody()!=null && OutDatosGeneralesXPersona.getBody().getResultado()!=null &&
				 OutDatosGeneralesXPersona.getBody().getResultado().equals(Constantes.CODIGO_EXITO_HAREC)){
				 
			 LOG.info("DATOS RESPONSE HAREC: " +	"["+"Persona="+OutDatosGeneralesXPersona.getBody().getNombres()+" "+OutDatosGeneralesXPersona.getBody().getApellidoPaterno()+" "+OutDatosGeneralesXPersona.getBody().getApellidoMaterno()+"]");
						 
				 flagEditar = false;
				 clienteNatural.setApePat(OutDatosGeneralesXPersona.getBody().getApellidoPaterno());			    
			     clienteNatural.setApeMat(OutDatosGeneralesXPersona.getBody().getApellidoMaterno());			    
			     clienteNatural.setNombre(OutDatosGeneralesXPersona.getBody().getNombres());
			     clienteNatural.setCodCliente(OutDatosGeneralesXPersona.getBody().getCodigoCentral());			     
			     clienteNatural.setNumDoi(OutDatosGeneralesXPersona.getBody().getDoi().getNumero());
			 
			     if(OutDatosGeneralesXPersona.getBody().getSegmento().getCodigo() != null && !OutDatosGeneralesXPersona.getBody().getSegmento().getCodigo().equals("")){
			    	 
			    	// se obtiene el segmento 
			    	try{ 
			    		Segmento segmento = segmentoBean.buscarPorCodigoSegmento(OutDatosGeneralesXPersona.getBody().getSegmento().getCodigo().trim());
			    		clienteNatural.setSegmento(segmento);
			    	}catch(Exception e){
			    		LOG.error(e.getMessage(), e);
			    	}
			     }else{
			    	// se obtiene el segmento 
			    	 try{
			    		 Segmento segmento = segmentoBean.buscarPorCodigoSegmento(Constantes.CODIGO_SEGMENTO_SIN_ASIGNAR);
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
	    	   
			   if(outConsultaReniecPorDNI!=null && outConsultaReniecPorDNI.getRefResponseHeader()!=null && outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta()!=null &&
					 !outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta().equalsIgnoreCase(Constantes.CODIGO_EXITO_RENIEC)){
				   LOG.info("No existen datos en RENIEC...");
				   LOG.info("RESPONSE RENIEC: "+"["+"Codigo Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta()+";"
						 +"Mensaje Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getMensajeRespuesta()+"]");;
			   }else{
				   LOG.info("Consulta exitosa de RENIEC...");
				   if(outConsultaReniecPorDNI!=null && outConsultaReniecPorDNI.getRefResponseHeader()!=null && outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta()!=null)
					   LOG.info("RESPONSE RENIEC: "+"["+"Codigo Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta()+";"
							 +"Mensaje Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getMensajeRespuesta()+"]");
			   }
	    		   
	    	   if(outConsultaReniecPorDNI!=null && outConsultaReniecPorDNI.getRefResponseHeader()!=null && outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta()!=null && outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta().equalsIgnoreCase(Constantes.CODIGO_EXITO_RENIEC)){
	    		   LOG.info("Consulta exitosa de RENIEC...");
	    		   LOG.info("RESPONSE RENIEC: "+"["+"Codigo Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta()+";"
	    				   +"Mensaje Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getMensajeRespuesta()+"]");
	    		   LOG.info("DATOS RESPONSE RENIEC: " +
							 	"["+ "Persona="+outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getNombres()+" "+outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getApellidoPaterno()+" "+outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getApellidoMaterno()+";"
							 	   + "Nº DOI="+outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getNumeroDNIConsultado()+"]");
						 
					flagEditar = false;
					clienteNatural.setApePat(outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getApellidoPaterno());			    
					clienteNatural.setApeMat(outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getApellidoMaterno());			    
					clienteNatural.setNombre(outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getNombres());
					clienteNatural.setNumDoi(outConsultaReniecPorDNI.getRefConsultaPorDNIResponse().getRespuestaDatos().getDatosPersona().getNumeroDNIConsultado());
					clienteNatural.setCodCliente(Constantes.CODIGO_CLIENTE_NUEVO);
					clienteNatural.setTipoCliente(tipoClienteBean.buscarPorId(Long.parseLong(Constantes.CODIGO_TIPO_CLIENTE_NUEVO)));
					
					// se obtiene el segmento 
			    	 try{
			    		 Segmento segmento = segmentoBean.buscarPorCodigoSegmento(Constantes.CODIGO_SEGMENTO_SIN_ASIGNAR);
			    		 clienteNatural.setSegmento(segmento);
			    	 }catch(Exception e){
			    		LOG.error(e.getMessage(), e);
			    	}
					
					fuente = RegistrarExpedienteMB.DATOS_HAREC;
		   			     
	    	   }else{
	    		   if(outConsultaReniecPorDNI!=null && outConsultaReniecPorDNI.getRefResponseHeader() == null){
	    			   LOG.info("Ocurió un error con el servicio de RENIEC");
					 
	    		   }else{
	    			   LOG.info("No existen datos en RENIEC...");
	    			   if(outConsultaReniecPorDNI!=null && outConsultaReniecPorDNI.getRefResponseHeader()!=null && outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta()!=null)
	    				   LOG.info("RESPONSE RENIEC: "+"["+"Codigo Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getCodigoRespuesta()+";"
								 +"Mensaje Respuesta:"+outConsultaReniecPorDNI.getRefResponseHeader().getMensajeRespuesta()+"]");
				  }					 
			 
	    		   clienteNuevo(clienteNatural);
	    		   fuente = RegistrarExpedienteMB.DATOS_NUEVO;
	    	   }
	    	   
	    	   outConsultaReniecPorDNI = null;   
	    	   inConsultaReniecPorDNI = null;
		    	  
		} else {
	       clienteNuevo(clienteNatural);
	       fuente = RegistrarExpedienteMB.DATOS_NUEVO;
		}

		clienteNatural.setNumDoi(numeroDOI);

		// se inicia los datos del ManageBean principal
		regExpMB.panelClienteNatural(clienteNatural, fuente,tipoDOISeleccionado);
		
		if(flagEditar){
			// Consultar tipo doi
			tipoDoi = tipoDoiBean.buscarPorId(Integer.parseInt(tipoDOISeleccionado));			
			clienteNatural.setSegmento(new Segmento());
			clienteNatural.getSegmento().setGrupoSegmento(new GrupoSegmento());
			clienteNatural.setTipoDoi(tipoDoi);
			
			// Consultar tipo Cliente
			clienteNatural.setCodCliente(Constantes.CODIGO_CLIENTE_NUEVO);
			clienteNatural.setTipoCliente(tipoClienteBean.buscarPorId(Long.parseLong(Constantes.CODIGO_TIPO_CLIENTE_NUEVO)));
			clienteNatural.setNumDoi(numeroDOI);
			
			int fuente2 = RegistrarExpedienteMB.DATOS_NUEVO;			
			regExpMB.panelClienteNatural(clienteNatural, fuente2,tipoDOISeleccionado);			
			regExpMB.visualizarPaneles(true);
			
		}else{
			regExpMB.visualizarPaneles(false);
		}
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
		LOG.info("this.tipoDOISeleccionado ... "+this.tipoDOISeleccionado);
	}

	public String getNumeroDOI() {
		return numeroDOI;
	}

	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
		LOG.info("this.numeroDOI="+this.numeroDOI);
	}

	public boolean isFlagEditar() {
		return flagEditar;
	}

	public void setFlagEditar(boolean flagEditar) {
		this.flagEditar = flagEditar;
	}

	public String getSizeNumeroDOI() {
		return sizeNumeroDOI;
	}

	public void setSizeNumeroDOI(String sizeNumeroDOI) {
		this.sizeNumeroDOI = sizeNumeroDOI;
	}

	public List<TipoDoi> getListTipoDoi() {
		return listTipoDoi;
	}

	public void setListTipoDoi(List<TipoDoi> listTipoDoi) {
		this.listTipoDoi = listTipoDoi;
	}
	

}
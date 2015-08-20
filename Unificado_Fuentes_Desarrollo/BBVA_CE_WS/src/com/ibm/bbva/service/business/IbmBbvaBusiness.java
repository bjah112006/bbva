package com.ibm.bbva.service.business;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import pe.ibm.bpd.RemoteUtils;
import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.entities.Accion;
import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.DatosCorreoDestin;
import com.ibm.bbva.entities.DelegacionRiesgo;
import com.ibm.bbva.entities.DelegacionRiesgoClasificacionBanco;
import com.ibm.bbva.entities.DevolucionTarea;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.ExpedienteTC;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.LineaMaxima;
import com.ibm.bbva.entities.MotivoDevolucion;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.ParametrosProceso;
import com.ibm.bbva.entities.PautaClasificacion;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.RetraccionTarea;
import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.entities.TblCeIbmDelegacionOficina;
import com.ibm.bbva.entities.TipoCambioCE;
import com.ibm.bbva.entities.TipoMoneda;
import com.ibm.bbva.entities.VistaExpedienteCantidad;
import com.ibm.bbva.entities.VistaExpedienteComplejidad;
import com.ibm.bbva.service.bean.DocumentoCM;
import com.ibm.bbva.service.bean.EmpleadoPerfilVO;
import com.ibm.bbva.service.bean.EmpleadoVO;
import com.ibm.bbva.service.bean.ExpedienteDTO;
import com.ibm.bbva.service.bean.ParametrosProcesoDTO;
import com.ibm.bbva.service.bean.PerfilDTO;
import com.ibm.bbva.service.util.Util;
import com.ibm.bbva.session.AccionBeanLocal;
import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;
import com.ibm.bbva.session.CartTerritorioCEBeanLocal;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.DatosCorreoBeanLocal;
import com.ibm.bbva.session.DatosCorreoDestinBeanLocal;
import com.ibm.bbva.session.DelegacionRiesgoBeanLocal;
import com.ibm.bbva.session.DelegacionRiesgoClasificacionBancoBeanLocal;
import com.ibm.bbva.session.DevolucionTareaBeanLocal;
import com.ibm.bbva.session.DocumentoExpTcBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.LineaMaximaBeanLocal;
import com.ibm.bbva.session.MontoPesoBeanLocal;
import com.ibm.bbva.session.MotivoDevolucionBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.ParametrosProcesoBeanLocal;
import com.ibm.bbva.session.PautaClasificacionBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.RetraccionTareaBeanLocal;
import com.ibm.bbva.session.TareaPerfilBeanLocal;
import com.ibm.bbva.session.TblCeIbmDelegacionOficinaLocal;
import com.ibm.bbva.session.TipoCambioCEBeanLocal;
import com.ibm.bbva.session.TipoMonedaBeanLocal;
import com.ibm.bbva.session.VistaExpedienteCantidadBeanLocal;
import com.ibm.bbva.session.VistaExpedienteComplejidadBeanLocal;
import com.ibm.bbva.ws.service.Constantes;


/**
 * @author cfveliz
 * 
 */
public class IbmBbvaBusiness {

	private DocumentoExpTcBeanLocal expTcBeanLocalBean;
	private ExpedienteBeanLocal expedienteBeanLocalBean;
	private DelegacionRiesgoBeanLocal delegacionRiesgoBeanLocalBean;
	private ClienteNaturalBeanLocal clienteNaturalBeanLocalBean;
	private TblCeIbmDelegacionOficinaLocal delegacionOficinaLocalBean;	
	private EmpleadoBeanLocal empleadoBeanLocalBean;
	private PautaClasificacionBeanLocal pautaClasificacionBeanLocalBean;
	private LineaMaximaBeanLocal lineaMaximaBeanLocalBean;
	private PerfilBeanLocal perfilBeanLocalBean;
	private RetraccionTareaBeanLocal retraccionTareaBeanLocalBean;
	private HistorialBeanLocal historialBeanLocalBean;
	private CartEmpleadoCEBeanLocal cartEmpleadoCEBeanLocalBean;
	private CartTerritorioCEBeanLocal cartTerritorioCEBeanLocalBean;
	private MontoPesoBeanLocal montoPesoBeanLocalBean;
	private TareaPerfilBeanLocal tareaPerfilBeanLocalBean;
	private ParametrosProcesoBeanLocal parametrosProcesoBeanLocalBean;
	private ParametrosConfBeanLocal parametrosConfBeanLocalBean;
	private VistaExpedienteCantidadBeanLocal vistaExpedienteCantidadBean;
	private VistaExpedienteComplejidadBeanLocal vistaExpedienteComplejidadBean;
	private TipoCambioCEBeanLocal tipoCambioCEBeanLocal;
	
	//FIX2 ERIKA ABREGU	
	private DelegacionRiesgoClasificacionBancoBeanLocal delegacionRiesgoClasifBcoBeanLocalBean;
	
	private DatosCorreoBeanLocal datosCorreoBeanLocal;
	private DatosCorreoDestinBeanLocal datosCorreoDestinBeanLocal;
	private AccionBeanLocal accionBeanLocal;
	private TipoMonedaBeanLocal tipoMonedaBeanLocal;
	private MotivoDevolucionBeanLocal motivoDevolucionBeanLocal;
	private DevolucionTareaBeanLocal devolucionTareaBeanLocal;
	private OficinaBeanLocal oficinaBeanLocal;
	
	@EJB
	private BBVAFacadeLocal bbvaFacade;

	private static final Logger LOG = LoggerFactory.getLogger(IbmBbvaBusiness.class);
	private ParametrosConfBeanLocal parametrosConfBean;
	
	private List<Historial> listHistAccion=null;
	public IbmBbvaBusiness() throws NamingException {
		super();
		expTcBeanLocalBean = (DocumentoExpTcBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.DocumentoExpTcBeanLocal");
		expedienteBeanLocalBean = (ExpedienteBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.ExpedienteBeanLocal");
		delegacionRiesgoBeanLocalBean = (DelegacionRiesgoBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.DelegacionRiesgoBeanLocal");	
		clienteNaturalBeanLocalBean = (ClienteNaturalBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.ClienteNaturalBeanLocal");
		delegacionOficinaLocalBean = (TblCeIbmDelegacionOficinaLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.TblCeIbmDelegacionOficinaLocal");
		empleadoBeanLocalBean=(EmpleadoBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.EmpleadoBeanLocal");	
		pautaClasificacionBeanLocalBean=(PautaClasificacionBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.PautaClasificacionBeanLocal");
		lineaMaximaBeanLocalBean = (LineaMaximaBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.LineaMaximaBeanLocal");	
		perfilBeanLocalBean = (PerfilBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.PerfilBeanLocal");		
		retraccionTareaBeanLocalBean = (RetraccionTareaBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.RetraccionTareaBeanLocal");	
		historialBeanLocalBean = (HistorialBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.HistorialBeanLocal");
		cartEmpleadoCEBeanLocalBean = (CartEmpleadoCEBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.CartEmpleadoCEBeanLocal");
		cartTerritorioCEBeanLocalBean = (CartTerritorioCEBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.CartTerritorioCEBeanLocal");
		montoPesoBeanLocalBean = (MontoPesoBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.MontoPesoBeanLocal");
		tareaPerfilBeanLocalBean = (TareaPerfilBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.TareaPerfilBeanLocal");	
		parametrosProcesoBeanLocalBean = (ParametrosProcesoBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.ParametrosProcesoBeanLocal");
		parametrosConfBeanLocalBean = (ParametrosConfBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");	
		vistaExpedienteCantidadBean = (VistaExpedienteCantidadBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.VistaExpedienteCantidadBeanLocal");
		vistaExpedienteComplejidadBean = (VistaExpedienteComplejidadBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.VistaExpedienteComplejidadBeanLocal");
		tipoCambioCEBeanLocal = (TipoCambioCEBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.TipoCambioCEBeanLocal");
		accionBeanLocal=(AccionBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.AccionBeanLocal");
		datosCorreoBeanLocal=(DatosCorreoBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.DatosCorreoBeanLocal");
		datosCorreoDestinBeanLocal=(DatosCorreoDestinBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.DatosCorreoDestinBeanLocal");
		tipoMonedaBeanLocal=(TipoMonedaBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.TipoMonedaBeanLocal");
		motivoDevolucionBeanLocal=(MotivoDevolucionBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.MotivoDevolucionBeanLocal");
		devolucionTareaBeanLocal=(DevolucionTareaBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.DevolucionTareaBeanLocal");
		oficinaBeanLocal=(OficinaBeanLocal) new InitialContext()
				.lookup("ejblocal:com.ibm.bbva.session.OficinaBeanLocal");
		
		//Fix2 erika abregu
		delegacionRiesgoClasifBcoBeanLocalBean = (DelegacionRiesgoClasificacionBancoBeanLocal) new InitialContext()
		.lookup("ejblocal:com.ibm.bbva.session.DelegacionRiesgoClasificacionBancoBeanLocal");
		
	}
	
	public boolean delegacionRiesgos(Integer idTipoCategoria, Long idExpediente){
		LOG.info("Metodo delegacionRiesgos");
		LOG.info("idTipoCategoria = "+idTipoCategoria);
		LOG.info("idExpediente = "+idExpediente);
		idTipoCategoria = idTipoCategoria != null ? idTipoCategoria: 0;
		idExpediente = idExpediente != null ? idExpediente: 0L;
		TipoCambioCE objTipoCambioCE = null;
		if(Util.isLong(idExpediente.toString()) && Util.isInteger(idTipoCategoria.toString())){
			LOG.info("Parametros correctos");
			Expediente objExpediente = expedienteBeanLocalBean.buscarPorId(idExpediente);
			if(objExpediente!=null && objExpediente.getProducto()!=null){
				LOG.info("Expediente existe!");
				
				ExpedienteTC objExpedienteTC=objExpediente.getExpedienteTC();
				LOG.info("Buscanco configuración Delegación Riesgo, con lo sgtes valores ");
				LOG.info("idTipoCategoria = "+ idTipoCategoria);
				LOG.info("idProducto = "+ objExpediente.getProducto().getId());
				LOG.info("idMoneda = "+ objExpedienteTC.getTipoMonedaSol().getId());				
				
				/**
				 * comentado para fix2 para erika abregu
				 * se comento para cambiar el parametro de tipo de moneda, debe ser siempre soles, puesto
				 * que en la parametria no existen parametros para dolares
				 * DelegacionRiesgo objDelegacionRiesgo=delegacionRiesgoBeanLocalBean.buscarPorId(idTipoCategoria, 
						objExpediente.getProducto().getId(), objExpedienteTC.getTipoMonedaSol().getId());
				 */
				 
				//lo cambiado para fix2 erika abregu
				 DelegacionRiesgo objDelegacionRiesgo=delegacionRiesgoBeanLocalBean.buscarPorId(idTipoCategoria, 
						objExpediente.getProducto().getId(), Long.parseLong(Constantes.CODIGO_TIPO_CAMBIO_SOLES));
				//fin de lo cambiado para fix2 erika abregu
				 
				//FIX2 ERIKA ABREGU 22-07-2015
				DelegacionRiesgoClasificacionBanco objDelegacionRiesgoClasifBco=delegacionRiesgoClasifBcoBeanLocalBean.buscarPorId(idTipoCategoria, 
						objExpediente.getProducto().getId());
				
				
				if(objDelegacionRiesgo!=null && objExpedienteTC!=null){
					LOG.info("objDelegacionRiesgo existe!");
					
					if(objExpedienteTC.getTipoMonedaAprob() != null && objExpedienteTC.getTipoMonedaAprob().getCodigo()!=null && 
							objExpedienteTC.getTipoMonedaAprob().getCodigo().equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)){
						
						LOG.info("objExpedienteTC.getTipoMonedaAprob().getCodigo():" + objExpedienteTC.getTipoMonedaAprob().getCodigo());
						LOG.info("objExpedienteTC.getTipoMonedaSol().getCodigo():" + objExpedienteTC.getTipoMonedaSol().getCodigo());
						objTipoCambioCE = obtenerTipoCambioExp(objExpedienteTC, objExpediente.getEmpleado());
						objExpedienteTC.setLineaCredAprob(calcularTipoCambio(objExpedienteTC.getLineaCredAprob(), objExpedienteTC.getTipoMonedaAprob(), objTipoCambioCE));
						
					}
//					if(objExpedienteTC.getSubproducto().getTipoMoneda() != null && objExpedienteTC.getSubproducto().getTipoMoneda().getCodigo()!=null && 
//							objExpedienteTC.getSubproducto().getTipoMoneda().getCodigo().equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)){
//						
//						LOG.info("objExpedienteTC.getSubproducto().getTipoMoneda().getCodigo():" + objExpedienteTC.getSubproducto().getTipoMoneda().getCodigo());
//						objTipoCambioCE = obtenerTipoCambioExp(objExpedienteTC, objExpediente.getEmpleado());
//						objExpedienteTC.setLineaConsumo(calcularTipoCambio(objExpedienteTC.getLineaConsumo(), objExpedienteTC.getTipoMonedaAprob() ,objTipoCambioCE));						
//					}
					
					if(objExpedienteTC.getTipoMonedaSol() != null && objExpedienteTC.getTipoMonedaSol().getCodigo()!=null && 
							objExpedienteTC.getTipoMonedaSol().getCodigo().equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)){
						
						LOG.info("objExpedienteTC.getTipoMonedaSol().getCodigo():" + objExpedienteTC.getTipoMonedaSol().getCodigo());
						objTipoCambioCE = obtenerTipoCambioExp(objExpedienteTC, objExpediente.getEmpleado());						
						
						//comentado para fix2 erika abregu
						//objExpedienteTC.setLineaCredSol(calcularTipoCambio(objExpedienteTC.getLineaCredSol(), objExpedienteTC.getTipoMonedaAprob() ,objTipoCambioCE));
						//fin de comentado para fix2 erika abregu
						//lo cambiado para el fix2 erika abregu
						objExpedienteTC.setLineaCredSol(calcularTipoCambio(objExpedienteTC.getLineaCredSol(), objExpedienteTC.getTipoMonedaSol() ,objTipoCambioCE));
						//fin de lo cambiado para el fix2 erika abregu
					}
					
					Double lineaCredito = Util.isDouble(""+objExpedienteTC.getLineaCredAprob()) ? objExpedienteTC.getLineaCredAprob() : null;
					/*Fix2 de Erika Abregu
					 * comentado para que valide tanto si la linea Credito no sea null ni 0.00*/
					if (lineaCredito == null || lineaCredito == 0) {
					//if (lineaCredito == null) {
						lineaCredito = Util.isDouble(""+objExpedienteTC.getLineaCredSol()) ? objExpedienteTC.getLineaCredSol() : null;
						LOG.info("lineaCredito = LineaCredSol = "+lineaCredito);
					}else
						LOG.info("lineaCredito existe = LineaCredAprob = "+lineaCredito);

					if(objDelegacionRiesgo.getTipoMoneda() != null && objDelegacionRiesgo.getTipoMoneda().getCodigo().equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)){
						if (objTipoCambioCE == null) {
							objTipoCambioCE = obtenerTipoCambioExp(objExpedienteTC, objExpediente.getEmpleado());
						}
						objDelegacionRiesgo.setLineaCredAprob(calcularTipoCambio(objDelegacionRiesgo.getLineaCredAprob(), objDelegacionRiesgo.getTipoMoneda(),objTipoCambioCE));
					}
					
					/*
					 * FIX2 ERIKA ABREGU
					 * 21-07-2015
					 */
					Double lineaCredAprobEnDelegacRiesgo = Util.isDouble(""+objDelegacionRiesgo.getLineaCredAprob()) ? objDelegacionRiesgo.getLineaCredAprob() : null;
					
					if(objExpedienteTC.getClasificacionBanco().getId() == Constantes.CODIGO_CLASIF_BANCO_NORMAL_TC || 
							objExpedienteTC.getClasificacionBanco().getId() == Constantes.CODIGO_CLASIF_BANCO_NORMAL_PLD){
						
						lineaCredAprobEnDelegacRiesgo = lineaCredAprobEnDelegacRiesgo*objDelegacionRiesgoClasifBco.getClasifNormal();
					}
					else if(objExpedienteTC.getClasificacionBanco().getId() == Constantes.CODIGO_CLASIF_BANCO_CPP_TC || 
							objExpedienteTC.getClasificacionBanco().getId() == Constantes.CODIGO_CLASIF_BANCO_CPP_PLD){
						
						lineaCredAprobEnDelegacRiesgo = lineaCredAprobEnDelegacRiesgo*objDelegacionRiesgoClasifBco.getClasifCpp();
					}
					else if(objExpedienteTC.getClasificacionBanco().getId() == Constantes.CODIGO_CLASIF_BANCO_DUDOSO_TC || 
							objExpedienteTC.getClasificacionBanco().getId() == Constantes.CODIGO_CLASIF_BANCO_DUDOSO_PLD){
						
						lineaCredAprobEnDelegacRiesgo = lineaCredAprobEnDelegacRiesgo*objDelegacionRiesgoClasifBco.getClasifDudoso();
					}
					else if(objExpedienteTC.getClasificacionBanco().getId() == Constantes.CODIGO_CLASIF_BANCO_DEFIC_TC || 
							objExpedienteTC.getClasificacionBanco().getId() == Constantes.CODIGO_CLASIF_BANCO_DEFIC_PLD){
						
						lineaCredAprobEnDelegacRiesgo = lineaCredAprobEnDelegacRiesgo*objDelegacionRiesgoClasifBco.getClasifDeficiente();
					}
					else if(objExpedienteTC.getClasificacionBanco().getId() == Constantes.CODIGO_CLASIF_BANCO_PERDIDA_TC || 
							objExpedienteTC.getClasificacionBanco().getId() == Constantes.CODIGO_CLASIF_BANCO_PERDIDA_PLD){
						
						lineaCredAprobEnDelegacRiesgo = lineaCredAprobEnDelegacRiesgo*objDelegacionRiesgoClasifBco.getClasifPerdida();
					}
					//VALIDAR LINEA DE CREDITO RESPECTO A CLASIFICACION BANCO
					if(lineaCredito!=null && Util.isDouble(""+lineaCredAprobEnDelegacRiesgo) &&
							lineaCredAprobEnDelegacRiesgo >= lineaCredito){
						LOG.info("LineaCredAprob respecto a Clasificacion Banco es mayor a lineaCredito de exp, lineaCredito de exp= "+lineaCredito);
						
						if("1".equals(objExpediente.getClienteNatural().getMonocuota())){
							lineaCredAprobEnDelegacRiesgo = lineaCredAprobEnDelegacRiesgo*objDelegacionRiesgo.getMonocuota();
						}
						
						//COMENTADO POR ERIKA ABREGU PARA EL CAMBIO DE FIX2
						//if(lineaCredito!=null && Util.isDouble(""+objDelegacionRiesgo.getLineaCredAprob()) &&
								//objDelegacionRiesgo.getLineaCredAprob() >= lineaCredito){
						if(lineaCredito!=null && Util.isDouble(""+lineaCredAprobEnDelegacRiesgo) &&
								lineaCredAprobEnDelegacRiesgo >= lineaCredito){
							LOG.info("LineaCredAprob es mayor a lineaCredito de exp, lineaCredito de exp= "+lineaCredito);
							if(Util.isDouble(""+objDelegacionRiesgo.getRiesgoCliente()) && Util.isDouble(""+objExpedienteTC.getRiesgoCliente()) && 
									objDelegacionRiesgo.getRiesgoCliente() >= objExpedienteTC.getRiesgoCliente()){
								LOG.info("RiesgoCliente es mayor a RiesgoCliente de exp, RiesgoCliente de exp = "+objExpedienteTC.getRiesgoCliente());
								if((objDelegacionRiesgo.getTipoBuro()!=null && objExpedienteTC.getTipoBuro()!=null && 
										objDelegacionRiesgo.getTipoBuroIni().getId() <= objExpedienteTC.getTipoBuro().getId()) && 
										(objDelegacionRiesgo.getTipoBuro().getId() >= objExpedienteTC.getTipoBuro().getId())){
										LOG.info("TipoBuroIni es menor igual a TipoBuro de exp y TipoBuro es mayor igual a TipoBuro de exp. TipoBuro de exp = "+objExpedienteTC.getTipoBuro().getId());
										ClienteNatural objClienteNatural = objExpediente.getClienteNatural();
										if(objClienteNatural!=null){
											LOG.info("objClienteNatural existe!");
											if(objDelegacionRiesgo.getPep()!=null && objClienteNatural.getPerExpPub()!=null && 
													objDelegacionRiesgo.getPep().trim().equalsIgnoreCase(objClienteNatural.getPerExpPub().trim())){
												LOG.info("Pep = Pep de cliente natural, Valor Pep = "+objDelegacionRiesgo.getPep().trim());
												if(objClienteNatural.getSegmento()!=null && objClienteNatural.getSegmento().getDescripcion()!=null && 
														objClienteNatural.getSegmento().getDescripcion().trim().equalsIgnoreCase(Constantes.TIPO_SEGMENTO_VIP)){
														LOG.info("Vip. Descripción de Segmento = "+objClienteNatural.getSegmento().getDescripcion().trim());
														if(objDelegacionRiesgo.getPrctjeEndeudamientoVipMax() >= objExpedienteTC.getPorcentajeEndeudamiento() && 
																objDelegacionRiesgo.getPrctjeEndeudamientoVipMin() <= objExpedienteTC.getPorcentajeEndeudamiento()){
															LOG.info("Validacion por PrctjeEndeudamientoVip. Dentro de delegación");
															return true;
														}													
												}else{
													LOG.info("No Vip . Descripción de Segmento = "+objClienteNatural.getSegmento().getDescripcion().trim());
													//COMENTADO POR ERIKA ABREGU PARA EL CAMBIO DE FIX2
													/*if(objDelegacionRiesgo.getPorcentajeEndeudamientoUd() >= objExpedienteTC.getPorcentajeEndeudamiento()){
														LOG.info("Validacion por PorcentajeEndeudamientoUd. Dentro de delegación");
														return true;
													}*/
												
													//FIX2 ERIKA ABREGU --VALIDAR % PORCENTAJE CONTRA SCORING
													if(objExpedienteTC.getTipoScoring() != null && objExpedienteTC.getTipoScoring().getId() != 0 &&
														objExpedienteTC.getTipoScoring().getId()== Constantes.CODIGO_TIPO_SCORING_APROBADO){
													
														if(objDelegacionRiesgo.getEndeudamientoScoreAprobado() >= objExpedienteTC.getPorcentajeEndeudamiento()){
															LOG.info("Validacion por PorcentajeEndeudamientoScoreAprobado. Dentro de delegación");
															return true;
														}
													}else if(objExpedienteTC.getTipoScoring() != null && objExpedienteTC.getTipoScoring().getId() != 0 &&
															objExpedienteTC.getTipoScoring().getId()== Constantes.CODIGO_TIPO_SCORING_RECHAZADO){
													
														if(objDelegacionRiesgo.getEndeudamientoScoreRechazado() >= objExpedienteTC.getPorcentajeEndeudamiento()){
															LOG.info("Validacion por PorcentajeEndeudamientoScoreRechazado. Dentro de delegación");
															return true;
														}
													}else if(objExpedienteTC.getTipoScoring() != null && objExpedienteTC.getTipoScoring().getId() != 0 &&
															objExpedienteTC.getTipoScoring().getId()== Constantes.CODIGO_TIPO_SCORING_DUDA){
													
														if(objDelegacionRiesgo.getEndeudamientoScoreDuda() >= objExpedienteTC.getPorcentajeEndeudamiento()){
															LOG.info("Validacion por PorcentajeEndeudamientoScoreDuda. Dentro de delegación");
															return true;
														}
													}
												
												
												}										
											}else
												LOG.info("Pep <> Pep de cliente natural");
										}else
											LOG.info("objClienteNatural es null");
									}else
										LOG.info("TipoBuroIni es menor igual a TipoBuro de exp y/o TipoBuro es mayor igual a TipoBuro de exp");
								}else
									LOG.info("RiesgoCliente no cumple condición");
							}else
								LOG.info("lineaCredito es nulo o no cumple condición");
						}else
							LOG.info("lineaCredito respecto a Clasificacion Banco es nulo o no cumple condición");
				}else
					LOG.info("objDelegacionRiesgo es nulo");
			}else
				LOG.info("Expediente "+idExpediente+" es nulo");
		}else
		LOG.info("Uno de los valores parametros es nulo");	

		return false;
	}
	
	public boolean delegacionOficinaBD(Integer idNivel, Long idExpediente){
		idNivel = idNivel != null ? idNivel: 0;
		idExpediente = idExpediente != null ? idExpediente : 0L;
		if(Util.isLong(idExpediente.toString()) && Util.isInteger(idNivel.toString())){
			Expediente objExpediente=expedienteBeanLocalBean.buscarPorId(idExpediente);
			LOG.info("delegacionOficina, "+idNivel +"-"+idExpediente);
			if(objExpediente!=null && objExpediente.getExpedienteTC()!=null && objExpediente.getClienteNatural()!=null){
				LOG.info("no nulos");
				ExpedienteTC objExpedienteTC=objExpediente.getExpedienteTC();
				ClienteNatural objClienteNatural=clienteNaturalBeanLocalBean.buscarPorId(objExpediente.getClienteNatural().getId());
				TblCeIbmDelegacionOficina objDelegacionOficina=delegacionOficinaLocalBean.buscarPorId(idNivel);

				if(objDelegacionOficina!=null && objExpediente!=null && objExpedienteTC!=null && objClienteNatural!=null){
					LOG.info("continua 0");
					if(Util.isDouble(""+objDelegacionOficina.getRiesgoCliente()) && Util.isDouble(""+objExpedienteTC.getRiesgoCliente()) && 
							objDelegacionOficina.getRiesgoCliente()>= objExpedienteTC.getRiesgoCliente()){
						LOG.info("continua 1");
						if(Util.isDouble(""+objDelegacionOficina.getPorcentajeEndeudamiento()) && Util.isDouble(""+objExpedienteTC.getPorcentajeEndeudamiento()) && 
								objDelegacionOficina.getPorcentajeEndeudamiento() >= objExpedienteTC.getPorcentajeEndeudamiento()){
							LOG.info("continua 2");
							if(objDelegacionOficina.getClasificacionBanco().getId()==objExpedienteTC.getClasificacionBanco().getId()){
								LOG.info("continua 3");
								if(Util.isDouble(""+objDelegacionOficina.getClasificacionSbs()) && Util.isDouble(""+objExpedienteTC.getClasificacionSbs()) && 
										objDelegacionOficina.getClasificacionSbs()==objExpedienteTC.getClasificacionSbs()){
									LOG.info("continua 4");
									if(Util.isDouble(""+objDelegacionOficina.getLimiteConsumo()) && Util.isDouble(""+objExpedienteTC.getLineaConsumo()) && 
											objDelegacionOficina.getLimiteConsumo() >= objExpedienteTC.getLineaConsumo())
										if(objDelegacionOficina.getPep()!=null && objClienteNatural.getPerExpPub()!=null && 
											objDelegacionOficina.getPep().trim().equalsIgnoreCase(objClienteNatural.getPerExpPub().trim()))
											if(objDelegacionOficina.getTipoScoring()!=null && objExpedienteTC.getTipoScoring()!=null && 
												objDelegacionOficina.getTipoScoring().getId() == objExpedienteTC.getTipoScoring().getId())
												if(Util.isDouble(""+objExpedienteTC.getLineaConsumo()) && ((!Util.isDouble(""+objExpedienteTC.getLineaCredAprob()) || 
														objExpedienteTC.getLineaCredAprob() == 0) ? objExpedienteTC.getLineaCredSol(): 
															objExpedienteTC.getLineaCredAprob()) <= objExpedienteTC.getLineaConsumo()){
													LOG.info("continua 2");
													if(objClienteNatural.getEstadoCivil()!=null && objClienteNatural.getEstadoCivil().getCodigo().equals(Constantes.ESTADO_CIVIL_CASADO)){
														LOG.info("Cliente casado");
														if( objDelegacionOficina.getBancoConyuge()!=null && 
															objDelegacionOficina.getBancoConyuge().getId()==objExpedienteTC.getBancoConyuge().getId())
															if(Util.isDouble(""+objDelegacionOficina.getSbsConyuge()) && 
																objDelegacionOficina.getSbsConyuge()==(!Util.isDouble(""+objExpedienteTC.getSbsConyuge())?null:objExpedienteTC.getSbsConyuge()))
																		return true;					
													}else															
														return true;															
												}
								}								
							}
							
						}
						
					}
					
				}
			}
			
		}

		return false;
	}
	
	//public boolean delegacionOficinaMemoria(Integer idNivel, Long idExpediente)
	public boolean delegacionOficinaMemoria(ExpedienteDTO objExpedienteDTO){
		LOG.info("metodo delegacionOficinaMemoria");
		TipoCambioCE objTipoCambioCE = null;
		if(objExpedienteDTO!=null){
			LOG.info("CodigoNivel = "+objExpedienteDTO.getCodigoNivel());
			LOG.info("CodigoTipoMonedaSol = "+objExpedienteDTO.getCodigoTipoMonedaSol());
			LOG.info("CodigoProducto = "+objExpedienteDTO.getCodigoProducto());
			if(objExpedienteDTO.getCodigoNivel()>0 && objExpedienteDTO.getCodigoTipoMonedaSol()>0 && objExpedienteDTO.getCodigoProducto()>0){				
				TblCeIbmDelegacionOficina objDelegacionOficina=delegacionOficinaLocalBean.buscarPorIdNivelIdProducto(objExpedienteDTO.getCodigoNivel(), objExpedienteDTO.getCodigoProducto());

				Expediente objExpediente = expedienteBeanLocalBean.buscarPorId(objExpedienteDTO.getCodigoExpediente());
				
				
				//if(objDelegacionOficina!=null){
				if(objDelegacionOficina!=null && objExpediente!=null && objExpediente.getClienteNatural()!=null ){
					LOG.info("Nivel = "+objDelegacionOficina.getNivel());
					/*comentado para el fix2 nde erika abregu
					 * 
					if (objExpedienteDTO.getCodigoTipoMonedaSol() != null && objExpedienteDTO.getCodigoTipoMonedaSol().equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
						objTipoCambioCE = obtenerTipoCambioExp(objExpediente.getExpedienteTC(),objExpediente.getEmpleado());
						objExpedienteDTO.setLineaCredAprob(calcularTipoCambio(objExpedienteDTO.getLineaCredAprob(), objExpedienteDTO.getCodigoTipoMonedaSol(), objTipoCambioCE));
						objExpedienteDTO.setLineaConsumo(calcularTipoCambio(objExpedienteDTO.getLineaConsumo(), objExpedienteDTO.getCodigoTipoMonedaSol(), objTipoCambioCE));
					}
					*/
					//lo nuevo para fix2 de erika aregu
					if (objExpedienteDTO.getCodigoTipoMonedaSol() != null && Long.toString(objExpedienteDTO.getCodigoTipoMonedaSol()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
						
						objTipoCambioCE = obtenerTipoCambioExp(objExpediente.getExpedienteTC(),objExpediente.getEmpleado());
						objExpedienteDTO.setLineaCredAprob(calcularTipoCambio(((!Util.isDouble(""+objExpedienteDTO.getLineaCredAprob()) || 
								objExpedienteDTO.getLineaCredAprob() == 0) ? objExpedienteDTO.getLineaCredSol(): 
									objExpedienteDTO.getLineaCredAprob()), objExpedienteDTO.getCodigoTipoMonedaSol(), objTipoCambioCE));
						
						objExpedienteDTO.setLineaConsumo(calcularTipoCambio(objExpedienteDTO.getLineaConsumo(), objExpedienteDTO.getCodigoTipoMonedaSol(), objTipoCambioCE));
					}
					//fin de lo nuevo para el fix2 de erika abregu
					
					if (objDelegacionOficina.getTipoMoneda() != null && objDelegacionOficina.getTipoMoneda().getCodigo().equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
						if (objTipoCambioCE == null) {
							objTipoCambioCE = obtenerTipoCambioExp(objExpediente.getExpedienteTC(), objExpediente.getEmpleado());
						}
						objDelegacionOficina.setLimiteConsumo(calcularTipoCambio(objDelegacionOficina.getLimiteConsumo(), objDelegacionOficina.getTipoMoneda(), objTipoCambioCE));
						objDelegacionOficina.setLimiteProducto(calcularTipoCambio(objDelegacionOficina.getLimiteProducto(), objDelegacionOficina.getTipoMoneda(), objTipoCambioCE));						
					}
					if(objDelegacionOficina.getProducto()!=null && objDelegacionOficina.getProducto().getId()>0){
						LOG.info("Validación Tipo Producto OK");
						if(Util.isDouble(""+objDelegacionOficina.getRiesgoCliente()) && Util.isDouble(""+objExpedienteDTO.getRiesgoCliente()) && 
								objDelegacionOficina.getRiesgoCliente()>= objExpedienteDTO.getRiesgoCliente()){
									LOG.info("Validación Riesgo Cliente OK");
									if(Util.isDouble(""+objDelegacionOficina.getPorcentajeEndeudamiento()) && Util.isDouble(""+objExpedienteDTO.getPorcentajeEndeudamiento()) && 
											objDelegacionOficina.getPorcentajeEndeudamiento() >= objExpedienteDTO.getPorcentajeEndeudamiento()){
										LOG.info("Validación Porcentaje de Endeudamiento OK");
										if(objDelegacionOficina.getClasificacionBanco().getId()==objExpedienteDTO.getClasificacionBanco().longValue()){
											LOG.info("Validación Clasificacion Banco Titular OK");
											if(Util.isDouble(""+objDelegacionOficina.getClasificacionSbs()) && Util.isDouble(""+objExpedienteDTO.getClasificacionSbs()) && 
													//objDelegacionOficina.getClasificacionSbs()==objExpedienteDTO.getClasificacionSbs()){
													//comentado para el fix2 por erika abregu
													objDelegacionOficina.getClasificacionSbs()>=objExpedienteDTO.getClasificacionSbs()){
												LOG.info("Validación Clasificacion Sbs Titular OK");
												if(Util.isDouble(""+objDelegacionOficina.getLimiteConsumo()) && Util.isDouble(""+objExpedienteDTO.getLineaConsumo()) && 
														objDelegacionOficina.getLimiteConsumo() >= objExpedienteDTO.getLineaConsumo()){
													LOG.info("Validación Limite Consumo OK");
													if(objDelegacionOficina.getPep()!=null && objExpedienteDTO.getPerExpPub()!=null && 
															objDelegacionOficina.getPep().trim().equalsIgnoreCase(objExpedienteDTO.getPerExpPub().trim())){
															LOG.info("Validación Pep OK");
															if(objDelegacionOficina.getTipoScoring()!=null && objExpedienteDTO.getCodigoTipoScoring()!=null && 
																	objDelegacionOficina.getTipoScoring().getId() == objExpedienteDTO.getCodigoTipoScoring()){
																LOG.info("Validación Tipo Scoring OK");
																LOG.info("Cod. Producto es "+objExpedienteDTO.getCodigoProducto());
																
																	
																/** COMENTADO 13/08/2015 POR ERIKA ABREGU PARA FIX2
																 * SE CAMBIA DEBIDO A QUE SE IMPLEMENTA 3 TIPOS DE PLAZOS MAXIMOS
																 * PARA CLIENTES VIP ; PARA CLIENNTES QUE GANEN MAYOR A 1500 SOLES ; PARA CLIENTES QUE GANEN MENOR A 1500 SOLES
																 * Se agrego condicional para la Condicional de Plazo Solicitado
																 * 
																if(objExpedienteDTO.getCodigoProducto()==Constantes.ID_APLICATIVO_PLD){
																	LOG.info("Descrip. Producto es PLD");
																	
																	LOG.info("objExpedienteDTO.getPlazoSolicitado() = "+objExpedienteDTO.getPlazoSolicitado());
																	LOG.info("objDelegacionOficina.getPlazoMax() = "+objDelegacionOficina.getPlazoMax());
																	if(objExpedienteDTO.getPlazoSolicitado()!=null && !objExpedienteDTO.getPlazoSolicitado().trim().equals("") && 
																			Util.isLong(objExpedienteDTO.getPlazoSolicitado().trim()) && 
																			Long.parseLong(objDelegacionOficina.getPlazoMax())>=Long.parseLong(objExpedienteDTO.getPlazoSolicitado().trim())){
																		LOG.info("Validación Plazo Máximo OK");
																	}else{
																		LOG.info("Validación Plazo Máximo No OK");
																		return false;
																	}
																}else{
																	LOG.info("Descrip. Producto es TC");
																}
																/**
																 * */
																
																
																/** FIX2 ERIKA ABREGU
																 * SE CAMBIA DEBIDO A QUE SE IMPLEMENTA 3 TIPOS DE PLAZOS MAXIMOS
																 * PARA CLIENTES VIP ; PARA CLIENNTES QUE GANEN MAYOR A 1500 SOLES ; PARA CLIENTES QUE GANEN MENOR A 1500 SOLES
																 * */
																if(objExpedienteDTO.getCodigoProducto()==Constantes.ID_APLICATIVO_PLD){
																	LOG.info("Descrip. Producto es PLD");
																	
																	LOG.info("objExpedienteDTO.getPlazoSolicitado() = "+objExpedienteDTO.getPlazoSolicitado());
																	LOG.info("objDelegacionOficina.getPlazoMaxCliAvaVip() = "+objDelegacionOficina.getPlazoMaxCliAvaVip());
																	LOG.info("objDelegacionOficina.getPlazoMaxCliIngMayor() = "+objDelegacionOficina.getPlazoMaxCliIngMayor());
																	LOG.info("objDelegacionOficina.getPlazoMaxCliIngMenor() = "+objDelegacionOficina.getPlazoMaxCliIngMenor());
																	
//																	long plazoExpDTO=0;
//																	
																	if(objExpedienteDTO.getPlazoSolicitado()!=null && !objExpedienteDTO.getPlazoSolicitado().trim().equals("") && 
																			Util.isLong(objExpedienteDTO.getPlazoSolicitado().trim())){ 
//																		plazoExpDTO = Long.parseLong(objExpedienteDTO.get.trim());
//																	}else{
//																		plazoExpDTO = Long.parseLong(objExpedienteDTO.getPlazoSolicitado().trim());
//																	}
																		long plazoExpDTO=0;
																		plazoExpDTO = ((objExpediente.getExpedienteTC().getPlazoSolicitadoApr()==null) || 
																				(!Util.isLong(""+objExpediente.getExpedienteTC().getPlazoSolicitadoApr())) ? Long.parseLong(objExpedienteDTO.getPlazoSolicitado().trim()): 
																					Long.parseLong(objExpediente.getExpedienteTC().getPlazoSolicitadoApr()));
																		LOG.info("plazoExpDTO = "+plazoExpDTO);
																		
																		//VALIDAR PLAZO MAXIMO
																		if(Util.isLong(""+objDelegacionOficina.getPlazoMaxCliAvaVip()) && 
																				Util.isLong(""+objDelegacionOficina.getPlazoMaxCliIngMayor()) &&
																				Util.isLong(""+objDelegacionOficina.getPlazoMaxCliIngMenor())){
																			LOG.info("Empezando a validar Plazo Máximo...");
																			
																			//VALIDAR SUBLIMITE X PROD -- CLIENTE VIP
																			if(objExpediente.getClienteNatural().getSegmento()!= null && objExpediente.getClienteNatural().getSegmento().getGrupoSegmento()!=null &&
																					objExpediente.getClienteNatural().getSegmento().getGrupoSegmento().getId()==Constantes.CODIGO_GRUPO_SEGMENTO_VIP &&
																					Long.parseLong(objDelegacionOficina.getPlazoMaxCliAvaVip()) < Long.parseLong(objExpedienteDTO.getPlazoSolicitado().trim())){
																				LOG.info("No cumple la condicion de Cliente VIP (Plazo) ");
																				return false;
																			}
																			
																			//VALIDAR SUBLIMITE X PROD -- CLIENTE INGRESO NETO MENSUAL MAYOR A 1500
																			if(objExpediente.getClienteNatural().getIngNetoMensual()>= 1500 && 
																					Long.parseLong(objDelegacionOficina.getPlazoMaxCliIngMayor()) < Long.parseLong(objExpedienteDTO.getPlazoSolicitado().trim())){
																				LOG.info("No cumple la condicion de Cliente Mayor a 1500 (Plazo)");	
																				return false;
																			}
																			
																			//VALIDAR SUBLIMITE X PROD -- CLIENTE INGRESO NETO MENSUAL MENOR A 1500
																			if(objExpediente.getClienteNatural().getIngNetoMensual()< 1500 && 
																					Long.parseLong(objDelegacionOficina.getPlazoMaxCliIngMenor()) < Long.parseLong(objExpedienteDTO.getPlazoSolicitado().trim())){
																				LOG.info("No cumple la condicion de Cliente Menor a 1500 (Plazo)");
																				return false;
																			}//else{// EL RESTO DE LOS CASOS
																				//if(Long.parseLong(objDelegacionOficina.getPlazoMax()) < Long.parseLong(objExpedienteTC.getPlazoSolicitado())){
																				//	LOG.info("No cumple la condicion de Plazo Maximo ");	
																				//}
																			//}
																			
																		}else{
																			LOG.info("No se puede comparar Plazo Maximo, esta mal parametrizado ");	
																			return false;
																		}
																			
																	}else{
																		LOG.info("Validación Plazo Máximo No OK");
																		return false;
																	}
																}else{
																	LOG.info("Descrip. Producto es TC");
																}
																/**
																 * */
													
																
																/*LOG.info("objExpedienteDTO.getPlazoSolicitado() = "+objExpedienteDTO.getPlazoSolicitado());
																LOG.info("objDelegacionOficina.getPlazoMax() = "+objDelegacionOficina.getPlazoMax());
																if(objExpedienteDTO.getPlazoSolicitado()!=null && !objExpedienteDTO.getPlazoSolicitado().trim().equals("") && 
																		Util.isLong(objExpedienteDTO.getPlazoSolicitado().trim()) && 
																		Long.parseLong(objDelegacionOficina.getPlazoMax())>=Long.parseLong(objExpedienteDTO.getPlazoSolicitado().trim())){
																	LOG.info("Validación Plazo Máximo OK");*/
																	LOG.info("objExpedienteDTO.getLineaCredAprob() = "+objExpedienteDTO.getLineaCredAprob());
																	LOG.info("objDelegacionOficina.getFinanciamientoMax() = "+objDelegacionOficina.getFinanciamientoMax());
																	if(Util.isDouble(""+objExpedienteDTO.getLineaCredAprob())){
																		LOG.info("Validación Financiamiento Máximo OK");
																		
																		
																		/** COMENTADO 13/08/2015 POR ERIKA ABREGU PARA FIX2
																		 * SE CAMBIA DEBIDO A QUE SE IMPLEMENTA 3 TIPOS DE SUBLIMITE
																		 * PARA CLIENTES VIP ; PARA CLIENNTES QUE GANEN MAYOR A 1500 SOLES ; PARA CLIENTES QUE GANEN MENOR A 1500 SOLES
																		 * Se agrego condicional para la Condicional de subproducto Solicitado
																		 * 
																		if(Util.isDouble(""+objDelegacionOficina.getLimiteProducto()) && ((!Util.isDouble(""+objExpedienteDTO.getLineaCredAprob()) || 
																				objExpedienteDTO.getLineaCredAprob() == 0) ? objExpedienteDTO.getLineaCredSol(): 
																					objExpedienteDTO.getLineaCredAprob()) <= objDelegacionOficina.getLimiteProducto()){
																			LOG.info("Validación Sublimite OK");
																		/**
																		 * */
																		
																		/** FIX2 ERIKA ABREGU
																		 * SE CAMBIA DEBIDO A QUE SE IMPLEMENTA 3 TIPOS DE PLAZOS MAXIMOS
																		 * PARA CLIENTES VIP ; PARA CLIENNTES QUE GANEN MAYOR A 1500 SOLES ; PARA CLIENTES QUE GANEN MENOR A 1500 SOLES
																		 * */
																		LOG.info("objExpedienteDTO.getLineaCredAprob() = "+objExpedienteDTO.getLineaCredAprob());
																		LOG.info("objExpedienteDTO.getLineaCredSol() = "+objExpedienteDTO.getLineaCredSol());
																		LOG.info("objDelegacionOficina.getLimiteProductoCliAvaVip() = "+objDelegacionOficina.getLimiteProductoCliAvaVip());
																		LOG.info("objDelegacionOficina.getLimiteProductoCliIngMayor() = "+objDelegacionOficina.getLimiteProductoCliIngMayor());
																		LOG.info("objDelegacionOficina.getLimiteProductoCliIngMenor() = "+objDelegacionOficina.getLimiteProductoCliIngMenor());
																		
																		if((objExpedienteDTO.getLineaCredSol()!=0 && Util.isDouble(""+objExpedienteDTO.getLineaCredSol())) || 
																				(objExpedienteDTO.getLineaCredAprob() != 0 && Util.isDouble(""+objExpedienteDTO.getLineaCredAprob()))){
																																						
																			double importeExpDTO=0;
																			importeExpDTO = ((!Util.isDouble(""+objExpedienteDTO.getLineaCredAprob()) || 
																								objExpedienteDTO.getLineaCredAprob() == 0) ? objExpedienteDTO.getLineaCredSol(): 
																									objExpedienteDTO.getLineaCredAprob());
																			LOG.info("plazoExpDTO = "+importeExpDTO);
																			
																			//VALIDAR IMPORTE DE SUBPRODUCTO 
																			if(Util.isDouble(""+objDelegacionOficina.getLimiteProductoCliAvaVip()) && 
																					Util.isDouble(""+objDelegacionOficina.getLimiteProductoCliIngMayor()) &&
																					Util.isDouble(""+objDelegacionOficina.getLimiteProductoCliIngMenor())){
																					
																				LOG.info("Empezando a validar sublimite por producto");	
																				//VALIDAR SUBLIMITE X PROD -- CLIENTE VIP
																				if(objExpediente.getClienteNatural().getSegmento()!= null && objExpediente.getClienteNatural().getSegmento().getGrupoSegmento()!=null &&
																						objExpediente.getClienteNatural().getSegmento().getGrupoSegmento().getId()==Constantes.CODIGO_GRUPO_SEGMENTO_VIP &&
																						objDelegacionOficina.getLimiteProductoCliAvaVip() < importeExpDTO){
																					LOG.info("No cumple la condicion de Cliente VIP (Importe Sublimite)");
																					return false;
																				}
																				
																				//VALIDAR SUBLIMITE X PROD -- CLIENTE INGRESO NETO MENSUAL MAYOR A 1500
																				if(objExpediente.getClienteNatural().getIngNetoMensual()>= 1500 && 
																						objDelegacionOficina.getLimiteProductoCliIngMayor() < importeExpDTO){
																					LOG.info("No cumple la condicion de Cliente Mayor a 1500 (Importe Sublimite)");
																					return false;
																				}
																				
																				//VALIDAR SUBLIMITE X PROD -- CLIENTE INGRESO NETO MENSUAL MENOR A 1500
																				if(objExpediente.getClienteNatural().getIngNetoMensual()< 1500 && 
																						objDelegacionOficina.getLimiteProductoCliIngMenor() < importeExpDTO){
																					LOG.info("No cumple la condicion de Cliente Menor a 1500 (Importe Sublimite)");	
																					return false;
																				}//else{// EL RESTO DE LOS CASOS
																					//if(Long.parseLong(objDelegacionOficina.getPlazoMax()) < Long.parseLong(objExpedienteTC.getPlazoSolicitado())){
																					//	LOG.info("No cumple la condicion de Plazo Maximo ");	
																					//}
																				//}
																					
																			}else{
																				LOG.info("No se puede comparar Importe Subproducto Maximo, esta mal parametrizado ");	
																				return false;
																			}
																		/**
																		 * */	
																			
																			//if(objExpedienteDTO.getCodigoEstadoCivilTitular()!=null && objExpedienteDTO.getCodigoEstadoCivilTitular().equals(Constantes.ESTADO_CIVIL_CASADO)){
																			//comentado para fix2 por erika abregu
																			if(objExpedienteDTO.getCodigoEstadoCivilTitular()!=null && 
																					(Long.toString(objExpedienteDTO.getCodigoEstadoCivilTitular()).equals(String.valueOf(Constantes.ESTADO_CIVIL_CASADO)) || 
																							Long.toString(objExpedienteDTO.getCodigoEstadoCivilTitular()).equals(String.valueOf(Constantes.ESTADO_CIVIL_CONVIVIENTE)))){
																				LOG.info("Iniciando Validación Conyuge ... ");
																				if( objDelegacionOficina.getBancoConyuge()!=null && objExpediente.getExpedienteTC().getBancoConyuge() != null && objExpediente.getExpedienteTC().getBancoConyuge().getId() >0 &&
																					objDelegacionOficina.getBancoConyuge().getId()==objExpediente.getExpedienteTC().getBancoConyuge().getId()){
																					LOG.info("Validación Clasificacion Banco Conyuge OK");
																					//comentado para el fix2 por erika abregu
																					/*if(Util.isDouble(""+objDelegacionOficina.getSbsConyuge()) && 
																							objDelegacionOficina.getSbsConyuge()==(!Util.isDouble(""+objExpedienteDTO.getSbsConyuge())?null:objExpedienteDTO.getSbsConyuge())){
																					*/		
																					if(Util.isDouble(""+objDelegacionOficina.getSbsConyuge()) && Util.isDouble(""+objExpediente.getExpedienteTC().getSbsConyuge()) &&  		
																							objDelegacionOficina.getSbsConyuge()>=objExpediente.getExpedienteTC().getSbsConyuge()){
																						LOG.info("Validación Clasificacion Sbs Conyuge OK");
																						return true;
																					}
																				}					
																			}else															
																				return true;
																																				
																		}else{
																			LOG.info("Validación Sublimite No Ok");
																			return false;
																		}	
																	}else
																		LOG.info("Validación Financiamiento Máximo No OK");
															//	}else
																//	LOG.info("Validación Plazo Máximo No OK");
															}else
																LOG.info("Validación Tipo Scoring No OK");
														}else
															LOG.info("Validación Pep No OK");
													}else
														LOG.info("Validación Limite Consumo No OK");
												}else
													LOG.info("Validación Clasificacion Sbs Titular No OK");
											}else
												LOG.info("Validación Clasificacion Banco Titular No OK");
										}else
											LOG.info("Validación Porcentaje de Endeudamiento No OK");
									}else
										LOG.info("Validación Riesgo Cliente No OK");
								}else
									LOG.info("Validación Tipo Producto No OK");
							}else
								LOG.info("objDelegacionOficina o objExpediente o objExpediente.getClienteNatural() es nulo");			
			}else
				LOG.info("Valor parametro es nulo");			
		}else
			LOG.info("Valor objExpedienteDTO es nulo");
	


		return false;
	}	
	
	public DocumentoCM consultarDocumentoExpediente(Long idExpediente,
			String codigoTipoDoc) {
		if(Util.isLong(idExpediente.toString()) && codigoTipoDoc!=null && !codigoTipoDoc.trim().equals("")){
//			DocumentoExpTc objDocTC = expTcBeanLocalBean.consultarDocumentoExpediente(
//					idExpediente, codigoTipoDoc);
			DocumentoExpTc objDocTC = expTcBeanLocalBean.buscarPorId(idExpediente);
			if (objDocTC!=null)
				return Util.mapearDocumentoBDADocumentoCM(objDocTC);				
		}
		return null;
	}

	public boolean actualizarDocumentoExpTC(DocumentoCM objDocumentoCM) {
		
		boolean updated = false;
		
		if(objDocumentoCM!=null){
			DocumentoExpTc objDocTC = Util
					.mapearDocumentoCMADocumentoBD(objDocumentoCM);

			int est = expTcBeanLocalBean.actualizarDocumentoExpediente(objDocTC);

			if (est != 0)
				updated = true;
			
			if(updated){
				
				/**
				 * Se revisa si se han completado todos los archivos para el expediente
				 * asociado al presente archivo además de una posterior notificación a process.
				 * 
				 * Antes se pensaba incluir esa lógica directamente en el DaemonTask.
				 */
				
				// Obteniendo documentos por expediente.
								
				LOG.info("Obteniendo documentos por expediente : " + objDocTC.getExpediente().getId());
				
				Expediente expediente = expedienteBeanLocalBean.buscarPorId(objDocTC.getExpediente().getId());
				
				List<DocumentoExpTc> docs = consultarDocumentoExpedientePorIdExpediente(objDocTC.getExpediente().getId());
				
				boolean existenDocumentosFaltantesParaExpediente = false;
				
				LOG.info("Revisando si los documentos cuenta con el ID CM nulo ...");
				
				for(DocumentoExpTc documento : docs){
					
					LOG.info("Filtrando solo documentos de la tarea del estado del expediente : Documento " + documento.getId() + " , Expediente " + documento.getExpediente().getId() + " , Tarea : " + documento.getTarea().getId());
					
					/*
					 * @author Daniel Flores
					 * 
					 * Comentado. Existen escenarios como la tarea 20, donde se pueden adjuntar archivos
					 * que pertenecen a la tarea 1 (Verif. Domicialiaria y Verif. Laboral). Por lo tanto
					 * se debe descartar el filtro por tarea. Además el estado del expediente en BD es anterior
					 * al real.
					 * 
					 * LOG.info("Filtrando solo documentos de la tarea del estado del expediente : Documento " + documento.getId() + " , Expediente " + documento.getExpediente().getId() + " , Tarea : " + documento.getTarea().getId());
					 *
					 * if(documento.getTarea().getId() != expediente.getEstado().getTarea().getId()){
						LOG.info("Documento con diferente tarea del estado del expediente, descartar revisión");
						continue;
					   }
					*/
					
					LOG.info("Filtraqndo solo documentos que no sean reutilizables");
					
					if(documento.getFlagDocReutilizable().equals("1")){
						LOG.info("Documento reutilizable, descartar revisión");
						continue;
					}
					
					if(documento.getFlagEscaneado().equals("0")){
						LOG.info("Documento no adjuntado , FLAG_ESCANEADO = 0, descartar revisión");
						continue;
					}
										
					// Revisando si los documentos que han sido escaneado y/o adjuntados cuentan con el ID CM nulo , 
					// si es así se consideran documentos pendientes
					// de sincronizar con BD.
					
					LOG.info("Documento con FLAG_ESCANEADO = 1 , revisar presencia en CM");
					
					LOG.info("Documento con id = " + documento.getId() + " y ID CM = " + documento.getIdCm());
					
					if(documento.getIdCm() == null || documento.getIdCm().equals(new BigDecimal(0))){
						LOG.info("Documento faltante, concluir revisión y cancelar enviar notificación. Solo es necesario obtener un documento faltante para avisar al Process");
						existenDocumentosFaltantesParaExpediente = true;
						break;
					}
				}
				
				// Notificación a Process
				
				if(!existenDocumentosFaltantesParaExpediente){
					LOG.info("No existen documentos faltantes para el expediente : " + objDocTC.getExpediente().getId());
					
					RemoteUtils objRemoteUtils = new RemoteUtils();
					
					objRemoteUtils.ejecutarOperacionEspera(String.valueOf(objDocTC.getExpediente().getId()));
				
				}else{
					LOG.info("Existen documentos faltantes para el expediente : " + objDocTC.getExpediente().getId());							
				}
			}
		}
		
		return updated;
	}
	/*
	public EmpleadoDTO distribucionCarga(Long idTerritorio,Long idExpediente) {
		if(Util.isLong(idExpediente.toString()) && Util.isLong(idTerritorio.toString())){
			Expediente objExpediente = expedienteBeanLocalBean.buscarPorId(idExpediente);
			if(objExpediente!=null && objExpediente.getProducto()!=null){
				Empleado objEmpleado=empleadoBeanLocalBean.calculoCarga(idTerritorio, objExpediente.getProducto().getId(), Constantes.FLAG_ACTIVO);
				if(objEmpleado!=null)
					return Util.mapearEmpleadoAEmpleadoDTO(objEmpleado);
			}			
		}
		return null;
	}
	*/
	public boolean pautasClasificacionBD(Long idExpediente){
		PautaClasificacion objPautaClasificacion=null;
		boolean cumplimiento=false;

		if(Util.isLong(idExpediente.toString())){
			try{
				Expediente objExpediente = expedienteBeanLocalBean.buscarPorId(idExpediente);
				
				if(objExpediente!=null){
					ExpedienteTC objExpedienteTC=objExpediente.getExpedienteTC();
					ClienteNatural objClienteNatural = objExpediente.getClienteNatural();
					
					if(objExpedienteTC!=null && objClienteNatural!=null)					
						if(objExpedienteTC.getTipoOferta()!=null && objExpedienteTC.getTipoOferta().getCodigo()!=null && 
								objExpedienteTC.getTipoOferta().getCodigo().trim().equals(Constantes.CODIGO_OFERTA_APROBADO)){
							if(objClienteNatural.getPersona()!=null)
								objPautaClasificacion=pautaClasificacionBeanLocalBean.buscarPorIdPersona(objClienteNatural.getPersona().getId());
							
							if(objPautaClasificacion!=null)
								cumplimiento=Util.comparacionSegunSimbolo(objExpedienteTC.getClasificacionSbs(),
										objPautaClasificacion.getSimbolo().trim(),objPautaClasificacion.getValor());
				
							if(cumplimiento && objClienteNatural.getEstadoCivil()!=null &&  
									(Integer.parseInt(""+objClienteNatural.getEstadoCivil().getId())==Constantes.ESTADO_CIVIL_CASADO || 
									Integer.parseInt(""+objClienteNatural.getEstadoCivil().getId())==Constantes.ESTADO_CIVIL_CONVIVIENTE)) {
								
								if(objExpedienteTC.getClienteNaturalConyuge()!=null){
									objClienteNatural=clienteNaturalBeanLocalBean.buscarPorId(objExpedienteTC.getClienteNaturalConyuge().getId());
									if(objClienteNatural!=null)
										objPautaClasificacion=pautaClasificacionBeanLocalBean.buscarPorIdPersona(objClienteNatural.getPersona().getId());
									else
										cumplimiento=false;
										
									if(objPautaClasificacion!=null)
										cumplimiento=Util.comparacionSegunSimbolo(objExpedienteTC.getSbsConyuge(),
												objPautaClasificacion.getSimbolo().trim(),objPautaClasificacion.getValor());
									else
										cumplimiento=false;								
								}else
									cumplimiento=false;
							}
						}						
				}
			
			}catch(Exception e){
				LOG.error(e.getMessage(), e);
			}			
		}

		return cumplimiento;
	}
	
	public boolean pautasClasificacionMemoria(Long idTipoOferta, Integer idEstadoCivilTitular, Long idPersonaTitular, 
			Long idPersonaConyuge, Double sbsTitular, Double sbsConyuge){
		LOG.info("pautasClasificacionMemoria");
		LOG.debug("TipoOferta : {} - EstadoCivilTitular : {}",idTipoOferta, idEstadoCivilTitular);
		LOG.debug("PersonaTitular : {} - PersonaConyuge : {}",idPersonaTitular, idPersonaConyuge);
		LOG.debug("SbsTitular : {} - SbsConyuge : {}",sbsTitular, sbsConyuge);
		
		PautaClasificacion objPautaClasificacion=null;
		boolean cumplimiento=false;

		if(Util.isLong(idTipoOferta.toString()) && Util.isLong(idPersonaTitular.toString()) && 
				Util.isLong(idPersonaConyuge.toString()) && Util.isDouble(sbsTitular.toString()) && 
				Util.isDouble(sbsConyuge.toString()) && Util.isInteger(idEstadoCivilTitular.toString())){
			try{
				if(	idTipoOferta==Long.parseLong(Constantes.CODIGO_OFERTA_APROBADO))
					objPautaClasificacion=pautaClasificacionBeanLocalBean.buscarPorIdPersona(idPersonaTitular);
							
						if(objPautaClasificacion!=null){
							cumplimiento=Util.comparacionSegunSimbolo(sbsTitular,
										objPautaClasificacion.getSimbolo().trim(),objPautaClasificacion.getValor());
				
							if(cumplimiento &&  
								    (idEstadoCivilTitular==Constantes.ESTADO_CIVIL_CASADO || 
									idEstadoCivilTitular==Constantes.ESTADO_CIVIL_CONVIVIENTE)) {
	
								objPautaClasificacion=pautaClasificacionBeanLocalBean.buscarPorIdPersona(idPersonaConyuge);
	
								if(objPautaClasificacion!=null)
									cumplimiento=Util.comparacionSegunSimbolo(sbsConyuge,
												objPautaClasificacion.getSimbolo().trim(),objPautaClasificacion.getValor());
								else
									cumplimiento=false;								
							}							
						}

										
			
			}catch(Exception e){
				LOG.error(e.getMessage(), e);
			}			
		}
		return cumplimiento;
	}
	
	
	
	public PerfilDTO lineaMaximaBD(Long idExpediente){
		
		Perfil objPerfil=null;
		boolean enviado=false;
		String delegacionExpediente="";
		
		if(Util.isLong(idExpediente.toString())){
			try{
				Expediente objExpediente = expedienteBeanLocalBean.buscarPorId(idExpediente);
				if(objExpediente!=null){
					ExpedienteTC objExpedienteTC=objExpediente.getExpedienteTC();
					List<LineaMaxima> listLineaMaxima=lineaMaximaBeanLocalBean.buscarTodos();

					if(objExpedienteTC!=null && listLineaMaxima.size()>0)
						for(LineaMaxima lineaMax : listLineaMaxima)
							if(lineaMax!=null)
								if(lineaMax.getProducto()!=null && objExpediente.getProducto()!=null && 
										lineaMax.getProducto().getId()==objExpediente.getProducto().getId())
									if(lineaMax.getTipoMoneda()!=null && objExpedienteTC.getTipoMonedaSol()!=null && 
											lineaMax.getTipoMoneda().getId()==objExpedienteTC.getTipoMonedaSol().getId())
										if(lineaMax.getTipoScoring()!=null && objExpedienteTC.getTipoScoring()!=null && 
												lineaMax.getTipoScoring().getId()==objExpedienteTC.getTipoScoring().getId()){
												
												boolean condicion=Util.comparacionSegunSimbolo(objExpedienteTC.getLineaCredSol(),
														lineaMax.getSimbolo().trim(),lineaMax.getMonto());
												
												if(condicion){
													if(objExpediente.getEmpleado()!=null && objExpediente.getEmpleado().getNivel()!=null){
														boolean delegacion=delegacionOficinaBD(Integer.parseInt(""+objExpediente.getEmpleado().getNivel().getId()),objExpediente.getId());
														if (delegacion)
															delegacionExpediente=Constantes.CON_DELEGACION_GERENTE;
														else
															delegacionExpediente=Constantes.SIN_DELEGACION_GERENTE;
														
														if(lineaMax.getFlagDelegacion().trim().equals(delegacionExpediente)){
															objPerfil=lineaMax.getPerfil();	
															enviado=true;
															
															break;
														}
														
													}
												}												
											
										}		

					if(!enviado)
						objPerfil=perfilBeanLocalBean.buscarPorId(Constantes.PERFIL_DEFECTO);					
				}

			}catch(Exception e){
				objPerfil=null;
				LOG.error(e.getMessage(), e);
			}
			
			if(objPerfil!=null)
				return Util.mapearPerfilAPerfilDTO(objPerfil);			
		}
		
		return null;
	}
	//public PerfilDTO lineaMaximaMemoria(Long idExpediente, Long idTipoScoring)
	public PerfilDTO lineaMaximaMemoria(ExpedienteDTO objExpedienteDTO){
		
		Perfil objPerfil=null;
		boolean enviado=false;
		String delegacionExpediente="";
		TipoCambioCE objTipoCambioCE = null;
		LOG.info("metodo lineaMaximaMemoria");
		if(objExpedienteDTO!=null){
			try{
				LOG.info("Expediente es "+objExpedienteDTO.getCodigoExpediente());
					List<LineaMaxima> listLineaMaxima=lineaMaximaBeanLocalBean.buscarTodos();
					Expediente objExpediente = expedienteBeanLocalBean.buscarPorId(objExpedienteDTO.getCodigoExpediente());	
					LOG.info("objExpedienteDTO.getCodigoTipoMonedaSol(): " + objExpedienteDTO.getCodigoTipoMonedaSol());
					if (objExpedienteDTO.getCodigoTipoMonedaSol() != null && String.valueOf(objExpedienteDTO.getCodigoTipoMonedaSol()).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {					
						objTipoCambioCE = obtenerTipoCambioExp(objExpediente.getExpedienteTC(), objExpediente.getEmpleado());
						objExpedienteDTO.setLineaCredSol(calcularTipoCambio(objExpedienteDTO.getLineaCredSol(), objExpedienteDTO.getCodigoTipoMonedaSol(), objTipoCambioCE));
					}

					if(listLineaMaxima.size()>0){
						for(LineaMaxima lineaMax : listLineaMaxima)
							if(lineaMax!=null)
								if(lineaMax.getProducto()!=null && objExpedienteDTO.getCodigoProducto()!=null && 
										lineaMax.getProducto().getId()==objExpedienteDTO.getCodigoProducto()){
									LOG.info("objExpedienteDTO.getFlagSubRogado() = "+objExpedienteDTO.getFlagSubRogado());
									LOG.info("lineaMax.getProducto().getId() = "+lineaMax.getProducto().getId());
									LOG.info("lineaMax.getFlagSubrogado() = "+lineaMax.getFlagSubrogado());
									LOG.info("lineaMax.getTipoMoneda().getCodigo() = " + lineaMax.getTipoMoneda().getCodigo());
									if (lineaMax.getTipoMoneda() != null && lineaMax.getTipoMoneda().getCodigo().equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
										if (objTipoCambioCE == null) {
											objTipoCambioCE = obtenerTipoCambioExp(objExpediente.getExpedienteTC(), objExpediente.getEmpleado());
										}
										lineaMax.setMonto(calcularTipoCambio(lineaMax.getMonto(), lineaMax.getTipoMoneda(), objTipoCambioCE));
									}
									
									if(lineaMax.getProducto().getId()==Constantes.ID_APLICATIVO_PLD &&
											objExpedienteDTO.getFlagSubRogado()!=null && !lineaMax.getFlagSubrogado().trim().equals(Constantes.CADENA_VACIA) &&
											objExpedienteDTO.getFlagSubRogado().trim().equals(Constantes.CODIGO_FLAG_SUBROGADO_ACTIVO) && 
											objExpedienteDTO.getFlagSubRogado().trim().equals(lineaMax.getFlagSubrogado().trim())){
												LOG.info("lineaMax.getFlagSubrogado() ="+lineaMax.getFlagSubrogado());
												objPerfil=lineaMax.getPerfil();	
												enviado=true;
												LOG.info("enviado=true");
												break;										
									}else{										
										if(!lineaMax.getFlagSubrogado().trim().equals(Constantes.CODIGO_FLAG_SUBROGADO_ACTIVO)){
											LOG.info("No es subrogado");
											if(lineaMax.getTipoMoneda()!=null && objExpedienteDTO.getCodigoTipoMonedaSol()!=null && 
													//lineaMax.getTipoMoneda().getId()==objExpedienteDTO.getCodigoTipoMonedaSol())
													lineaMax.getTipoMoneda().getId()== Long.valueOf(Constantes.CODIGO_TIPO_CAMBIO_SOLES))
												if(lineaMax.getTipoScoring()!=null && lineaMax.getTipoScoring().getId()==objExpedienteDTO.getCodigoTipoScoring()){
														boolean condicion=Util.comparacionSegunSimbolo(objExpedienteDTO.getLineaCredSol(),
																lineaMax.getSimbolo().trim(),lineaMax.getMonto());
														LOG.info("condicion="+condicion);
														if(condicion){
															LOG.info("ntro a condicion");
																boolean delegacion=delegacionOficinaMemoria(objExpedienteDTO);
																LOG.info("delegacion="+delegacion);
																if (delegacion)
																	delegacionExpediente=Constantes.CON_DELEGACION_GERENTE;
																else
																	delegacionExpediente=Constantes.SIN_DELEGACION_GERENTE;
																LOG.info("delegacionExpediente="+delegacionExpediente);
																LOG.info("lineaMax = "+lineaMax.getId());
																
																if(lineaMax.getFlagDelegacion()!=null){
																	LOG.info("Flag Delegacion Linea Maxima = "+lineaMax.getFlagDelegacion());
																if(lineaMax.getFlagDelegacion().trim().equals(delegacionExpediente)){
																	objPerfil=lineaMax.getPerfil();	
																	enviado=true;
																		LOG.info("enviado=true");
																	break;
																}	
														}
												   }
											   }	
										  }										
									}
								}
					}else
						LOG.info("listLineaMaxima es vacío");
		

					if(!enviado || objPerfil==null)
						objPerfil=perfilBeanLocalBean.buscarPorId(Constantes.PERFIL_DEFECTO);					
				

			}catch(Exception e){
				objPerfil=null;
				LOG.error(e.getMessage(), e);
			}
			
			if(objPerfil!=null)
				return Util.mapearPerfilAPerfilDTO(objPerfil);			
		}else
			LOG.info("Objeto parametro objExpedienteDTO es nulo");
		
		return null;
	}
	
	public boolean retraerTareas(Long idAccion, Long salida, Long llegada){
		if(Util.isLong(idAccion.toString()) && Util.isLong(salida.toString()) && Util.isLong(llegada.toString())){
			RetraccionTarea objRetraccionTarea=retraccionTareaBeanLocalBean.buscarPorParametros(idAccion, salida, llegada);
			
			if(objRetraccionTarea!=null)
				return true;			
		}
		return false;
	}
	

	public EmpleadoPerfilVO obtenerUsuario(Integer idTerritorio,Integer idProducto, Integer idPerfil, 
			Long idExpediente, Long idTarea){
		LOG.info("metodo obtenerUsuario");
		LOG.info("idTerritorio = "+idTerritorio);
		LOG.info("idProducto = "+idProducto);
		LOG.info("idPerfil = "+idPerfil);
		LOG.info("idExpediente = "+idExpediente);
		LOG.info("idTarea donde va= "+idTarea);
		EmpleadoPerfilVO empleadoPerfilVO=new EmpleadoPerfilVO();
		boolean balanceo=true;
		long idAccion=0L;
		long idTareaActual=0L;
		try{
		
		List<TareaPerfil> listTareaPerfil=tareaPerfilBeanLocalBean.buscarPorIdTarea(idTarea);
		TareaPerfil objTareaPerfil=listTareaPerfil==null?null:listTareaPerfil.get(0);
		
		Expediente objExpediente=null;
		objExpediente=expedienteBeanLocalBean.buscarPorId(idExpediente);
		idTareaActual=objExpediente.getExpedienteTC().getTarea().getId();

		if(objTareaPerfil!=null && objTareaPerfil.getPerfil()!=null){
			LOG.info("TareaPerfil no es nulo ");
			idPerfil=Integer.parseInt(""+objTareaPerfil.getPerfil().getId());
			LOG.info("idPerfil = "+idPerfil);
			LOG.info("idProducto = "+idProducto);
			LOG.info("idTerritorio = "+idTerritorio);
			LOG.info("idTarea = "+idTarea);
			long idEmpleado = Constantes.ID_USUARIO_ADMINISTRADOR;
			
			Empleado objEmpleado=null;
			List<Historial> listHistorial=null;
			
			listHistorial=historialBeanLocalBean.buscarXCriterioExpedienteYPerfilEjecutivo(idExpediente, Long.parseLong(idPerfil+""));
				
			if(listHistorial!=null && !listHistorial.isEmpty()){
				LOG.info("listHistorial tamaño:::"+listHistorial.size());
				objEmpleado = listHistorial.get(0).getEmpleado();
				//idAccion=obtenerUltimaAccion(idExpediente);
				idAccion=obtenerUltimaAccion(objExpediente);
				LOG.info("idAccion Devolver : "+idAccion);
				
				if(objEmpleado!=null && objEmpleado.getOficina()!=null){
					LOG.info("Oficina = "+objEmpleado.getOficina().getId());
					LOG.info("Empleado = "+objEmpleado.getId());
					List<CartEmpleadoCE> listCartEmpleadoCE=cartEmpleadoCEBeanLocalBean.verificarCartEmpleado(idPerfil, objEmpleado.getOficina().getId(), 
							idProducto, objEmpleado.getId(), idTerritorio);
					if(listCartEmpleadoCE!=null && listCartEmpleadoCE.size()>0){
						LOG.info("listCartEmpleadoCE size::"+listCartEmpleadoCE.size());
						
						balanceo=false;
						idEmpleado = objEmpleado.getId(); //estaba actualizando el usuario administrador en BD
						LOG.info("idEmpleado = "+idEmpleado);
					}
					
					LOG.info("balanceo = "+balanceo);
				}
			}
			if(listHistorial==null || listHistorial.size()<=0 || balanceo){
					idAccion=obtenerUltimaAccion(objExpediente);
					LOG.info("idAccion - Avanza" + idAccion);
					if (idPerfil.equals(Constantes.ID_PERFIL_MESA_DE_CONTROL) || idPerfil.equals(Constantes.ID_PERFIL_ANALISIS_Y_ALTAS)){
						LOG.info("Mesa de control || Analisis y Alta");
						List<VistaExpedienteCantidad> listVistaExpedienteCantidad = vistaExpedienteCantidadBean.buscarPorIdProdIdTerrIdPer(idProducto, idTerritorio, idPerfil);
						if(listVistaExpedienteCantidad!=null && !listVistaExpedienteCantidad.isEmpty())
							idEmpleado = listVistaExpedienteCantidad.get(0).getIdEmpleado();
						
					}else if(idPerfil.equals(Constantes.ID_PERFIL_ANALISIS_DE_RIESGOS) || idPerfil.equals(Constantes.ID_PERFIL_SUPERIOR_DE_RIESGOS)){
						LOG.info("Analisis de riesgos || Riesgo superior");
						List<VistaExpedienteComplejidad> listVistaExpedienteComplejidad = vistaExpedienteComplejidadBean.buscarPorIdProdIdTerrIdPer(idProducto, idTerritorio, idPerfil);
						if(listVistaExpedienteComplejidad!=null && !listVistaExpedienteComplejidad.isEmpty())
							idEmpleado = listVistaExpedienteComplejidad.get(0).getIdEmpleado();
					}else if (idPerfil.equals(Constantes.ID_PERFIL_SUBGERENTE_OFICINA)) {
						LOG.info("Subgerente");
						List<Historial> lisHistorial = historialBeanLocalBean.buscarPorIdExpediente(idExpediente);
						
						if(lisHistorial!=null && lisHistorial.size()>0){
							LOG.info("lisHistorial = "+lisHistorial.get(0).getId());
							List<VistaExpedienteCantidad> listVistaExpedienteCantidad=null;
							
							long idOficina = lisHistorial.get(0).getOficina().getId();
							LOG.info("idOficina = "+idOficina);
							//List<Empleado> empDes = empleadoBeanLocalBean.buscarPorPerfilEmpleadoActivo(Constantes.ID_PERFIL_SUBGERENTE_OFICINA,idOficina);
							listVistaExpedienteCantidad =vistaExpedienteCantidadBean.buscarPorIdProdIdTerrIdOfIdPer(idProducto, idTerritorio, idOficina, Constantes.ID_PERFIL_SUBGERENTE_OFICINA);
							/**
							 * Cambio 14 Abril 2015 
							 * Oficinas Desplazadas
							 * */			
							if(listVistaExpedienteCantidad==null || listVistaExpedienteCantidad.size()<1){
								LOG.info("Verificando si es oficina Desplazada:::: Id oficina::"+idOficina);
								Oficina objOficina = oficinaBeanLocal.buscarPorId(idOficina);
								
								if(objOficina!=null &&  objOficina.getFlagDesplazada()!=null && !objOficina.getFlagDesplazada().trim().equals("") && 
										objOficina.getFlagDesplazada().trim().equals(Constantes.FLAG_ACTIVO)){
									LOG.info("Id oficina::"+objOficina.getId()+":::Flag Desplazada:::"+objOficina.getFlagDesplazada());
									if(objOficina.getOficinaPrincipal()!=null){
										LOG.info("Id oficina::"+objOficina.getId()+":::tiene como Oficina Principal a Oficina con id:::"+objOficina.getOficinaPrincipal().getId());
										//empDes = empleadoBeanLocalBean.buscarPorPerfilEmpleadoActivo(Constantes.ID_PERFIL_SUBGERENTE_OFICINA,objOficina.getOficinaPrincipal().getId());
										listVistaExpedienteCantidad =vistaExpedienteCantidadBean.buscarPorIdProdIdTerrIdOfIdPer(idProducto, idTerritorio, objOficina.getOficinaPrincipal().getId(), Constantes.ID_PERFIL_SUBGERENTE_OFICINA);
									}else{
										LOG.info("Id oficina Principal::"+objOficina.getId()+"::: No tiene configurado su Oficina Principal");
									}
									
								}else
									LOG.info("Id oficina::"+objOficina.getId()+":::Flag Desplazada:::"+objOficina.getFlagDesplazada());
							}
							/**
							 **/
							
							if (listVistaExpedienteCantidad !=null && !listVistaExpedienteCantidad.isEmpty())	{
								LOG.info("Tamaño de empDes::::"+listVistaExpedienteCantidad.size());
								idEmpleado = listVistaExpedienteCantidad.get(0).getIdEmpleado();
							}							
							
						}else
							LOG.info("Expediente no tiene historial");				
					
					}else if (idPerfil.equals(Constantes.ID_PERFIL_EJECUTIVO)){
						LOG.info("Ejecutivo");
						List<VistaExpedienteCantidad> listVistaExpedienteCantidad=null;
						if(objEmpleado!=null){
							LOG.info("Buscando otro ejecutivo en la oficina del ejecutivo anterior, Oficina:"+objEmpleado.getOficina().getId());
							listVistaExpedienteCantidad =vistaExpedienteCantidadBean.buscarPorIdProdIdTerrIdOfIdPer(idProducto, idTerritorio, objEmpleado.getOficina().getId(), idPerfil);
						}else{
							LOG.info("Buscando la oficina del expediente, Oficina:"+objExpediente.getExpedienteTC().getOficina().getId());
							listVistaExpedienteCantidad =vistaExpedienteCantidadBean.buscarPorIdProdIdTerrIdOfIdPer(idProducto, idTerritorio, objExpediente.getExpedienteTC().getOficina().getId(), idPerfil);
						}
						
						if(listVistaExpedienteCantidad!=null && !listVistaExpedienteCantidad.isEmpty())
							idEmpleado = listVistaExpedienteCantidad.get(0).getIdEmpleado();						
					}else{					
						List<VistaExpedienteCantidad> listVistaExpedienteCantidad = vistaExpedienteCantidadBean.buscarPorIdProdIdPer(idProducto, idPerfil);
						if(listVistaExpedienteCantidad!=null && !listVistaExpedienteCantidad.isEmpty())
							idEmpleado = listVistaExpedienteCantidad.get(0).getIdEmpleado();
					}
					LOG.info("idPerfil::"+idPerfil+" , idEmpleado:::"+idEmpleado);
					
					//objEmpleado = empleadoBeanLocalBean.buscarPorId(idEmpleado);
			}
			objEmpleado = empleadoBeanLocalBean.buscarPorId(idEmpleado);
			
			actualizaEmpleadoExpediente(idEmpleado, idExpediente);
			EmpleadoVO empleadoVO =Util.mapearEmpleadoBDAEmpleadoVO(objEmpleado);
			if(empleadoVO!=null){
				String descripcionPerfil = objEmpleado.getPerfil().getDescripcion();
				empleadoPerfilVO.setEmpleadoVO(empleadoVO);
				if(empleadoVO.getId()==1){
					empleadoPerfilVO.setDescripcionPerfil(objTareaPerfil.getPerfil().getDescripcion());
					LOG.info("Usuario Administrador, seteamos perfil correcto para mensaje de process al usuario");
				}else
					empleadoPerfilVO.setDescripcionPerfil(descripcionPerfil);
				LOG.info("Codigo Empleado = "+empleadoVO.getCodigo());
				LOG.info("Descripción de Perfil = "+descripcionPerfil);
								
			}

		}
		LOG.info("antes de enviar el correo");
		enviarCorreo(idTerritorio,idProducto,idPerfil,idExpediente,idTarea,idAccion,listHistAccion,empleadoPerfilVO,idTareaActual, objExpediente);  
		LOG.info("despues de enviar el correo");
		return empleadoPerfilVO;
		
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return empleadoPerfilVO;
		}
	}	
	
	public long obtenerUltimaAccion(Expediente objExpediente){
		long idAccion=0L;
		String strAccion="";
		listHistAccion=new ArrayList<Historial>();
		listHistAccion = historialBeanLocalBean.buscarPerfilRecientePorId(objExpediente.getId());
		strAccion=objExpediente.getAccion();
		if (strAccion!=null){
				LOG.info("obtenerUltimaAccion - strAccion "+ strAccion);
				LOG.info(" antes de instanciar accion");
				Accion accion =null;
				LOG.info("Accion instanciada");
				accion=accionBeanLocal.buscarPorAccion(strAccion);
				if (accion!=null){
					idAccion=accion.getId();
				}
			}
		return idAccion;	
	}
	
	
	public void enviarCorreo(Integer idTerritorio,Integer idProducto, Integer idPerfil, 
			Long idExpediente, Long idTarea, Long idAccion, List<Historial> listHistAccion, 
			EmpleadoPerfilVO empleadoPerfil, Long idTareaActual, Expediente objExpediente) throws AddressException{
		LOG.info("Entro al EnvioCorreo");
		LOG.info("Enviar Correo idTerritorio = "+idTerritorio);
		LOG.info("Enviar Correo  idProducto = "+idProducto);
		LOG.info("Enviar Correo  idPerfil = "+idPerfil);
		LOG.info("Enviar Correo  idExpediente = "+idExpediente);
		LOG.info("Enviar Correo  idTarea = "+idTarea);
		LOG.info("Enviar Correo  idTarea Actual = "+idTareaActual);
		LOG.info("Enviar Correo  idAccion = "+idAccion);
		DatosCorreo datosCorreo= null;
		datosCorreo=datosCorreoBeanLocal.buscarPorTareaAccion(idTareaActual,idAccion,idProducto);
		if (datosCorreo!=null){
			LOG.info("HAY DATOS DE CORREO ");
			if (datosCorreo.getFlagActivo().equals("1")){ // Si el correo esta habilitado
				List<InternetAddress> listAddresses = new ArrayList<InternetAddress> (); // To
				List<InternetAddress> listAddressesCopia= new ArrayList<InternetAddress>(); //CC
				String strUsuarioSgteTarea="";
				String strGestorActual=""; // Usuario ( gestor ) que posee el expediente en la tarea actual. 
				String destinatario="";
				String asunto="";
				String nuevoAsunto="";
				String strCuerpo="";
				String nuevoCuerpo="";
				long idOficinaExpediente=0;
				List<DatosCorreoDestin> detalle=null;
				LOG.info("Existe cabecera del correo a enviar");
				long idCabecera=datosCorreo.getId();
				detalle=datosCorreoDestinBeanLocal.buscarPorIdDatosCorreo(idCabecera);
				asunto=datosCorreo.getAsunto();
				LOG.info("asunto" + asunto);
				nuevoAsunto=modificarTexto(asunto, objExpediente,listHistAccion,empleadoPerfil);
				LOG.info("asunto" + nuevoAsunto);
				byte[] byteCuerpo = datosCorreo.getCuerpo();
				strCuerpo = byteCuerpo==null ? null : new String (byteCuerpo);
				LOG.info("strCuerpo" + strCuerpo);
				nuevoCuerpo=modificarTexto(strCuerpo,objExpediente,listHistAccion,empleadoPerfil);
				LOG.info("nuevoCuerpo" + nuevoCuerpo);
				//Oficina donde se creo el expediente
				if (objExpediente!=null && objExpediente.getExpedienteTC()!=null && objExpediente.getExpedienteTC().getOficina()!=null){
					idOficinaExpediente=objExpediente.getExpedienteTC().getOficina().getId(); // Oficina donde se creo el expediente
				}
				List<Empleado> lstEmpleados = null;
				for (int i=0; i<detalle.size();i++){
					Perfil objPerfil=null;
					objPerfil=detalle.get(i).getPerfil();
					if(objPerfil.getId() == Constantes.ID_PERFIL_GERENTE || objPerfil.getId() == Constantes.ID_PERFIL_SUBGERENTE_OFICINA){
						LOG.info("Perfil:::: "+objPerfil.getId()+" - idOficinaExpediente:::"+idOficinaExpediente);
						lstEmpleados = empleadoBeanLocalBean.buscarPorOficinaPerfil(idOficinaExpediente, Long.valueOf(objPerfil.getId()));
						
						/**
						 * Cambio 14 Abril 2015 
						 * Oficinas Desplazadas
						 * */
						if(lstEmpleados==null || lstEmpleados.size()<1){
							LOG.info("Verificando si es oficina Desplazada:::: Id oficina::"+idOficinaExpediente);
							Oficina objOficina = oficinaBeanLocal.buscarPorId(idOficinaExpediente);
							
							if(objOficina!=null && objOficina.getFlagDesplazada()!=null && !objOficina.getFlagDesplazada().trim().equals("") && 
									objOficina.getFlagDesplazada().trim().equals(Constantes.FLAG_ACTIVO)){
								LOG.info("Id oficina::"+objOficina.getId()+":::Flag Desplazada:::"+objOficina.getFlagDesplazada());
								if(objOficina.getOficinaPrincipal()!=null){
									LOG.info("Id oficina::"+objOficina.getId()+":::tiene como Oficina Principal a Oficina con id:::"+objOficina.getOficinaPrincipal().getId());
									lstEmpleados = empleadoBeanLocalBean.buscarPorOficinaPerfil(objOficina.getOficinaPrincipal().getId(), objPerfil.getId());
								}else{
									LOG.info("Id Oficina desplazada::"+objOficina.getId()+"::: No tiene configurado su Oficina Principal");
								}
								
							}else
								LOG.info("Id oficina::"+objOficina.getId()+":::Flag Desplazada:::"+objOficina.getFlagDesplazada());
						}
						/**
						 * */
						
						if (lstEmpleados.size()>0){
							for (int j=0; j<lstEmpleados.size();j++){
								if (lstEmpleados.get(j).getCorreo()!=null){
									destinatario=lstEmpleados.get(j).getCorreo();
									if (destinatario!=null){
										LOG.info("destinatario no es nulo - Empleado "+lstEmpleados.get(j).getCodigo()+", con perfil:::"+objPerfil.getId());
										listAddresses.add(new InternetAddress(destinatario));
									}
								}
							}
						}
					}
					else if(objPerfil.getId() == Constantes.ID_PERFIL_JEFE_EQUIPO_CPM || objPerfil.getId() == Constantes.ID_PERFIL_SUPERVISOR){
						lstEmpleados=empleadoBeanLocalBean.buscarPorPerfilActivo(Long.valueOf(objPerfil.getId()));
						if (lstEmpleados.size()>0){
							for (int j=0; j<lstEmpleados.size();j++){
								if (lstEmpleados.get(j).getCorreo()!=null){
									destinatario=lstEmpleados.get(j).getCorreo();
									if (destinatario!=null){
										LOG.info("destinatario no es nulo - Empleado "+lstEmpleados.get(j).getCodigo()+", con perfil:::"+objPerfil.getId());
										listAddresses.add(new InternetAddress(destinatario));
									}
								}
							}
						}
					}
					else{
						List<Historial> lstHistExpediente = new ArrayList<Historial>();
						lstHistExpediente=historialBeanLocalBean.buscarXCriterioExpedienteXPerfil(idExpediente, objPerfil.getId());
						String strUltimoParticipante="";
						if (lstHistExpediente.size()>0){
							for (Historial item :lstHistExpediente ){
								strUltimoParticipante=item.getEmpleado().getCorreo();
								if (strUltimoParticipante!=null){
									LOG.info("destinatario no es nulo - Empleado "+item.getEmpleado().getCodigo()+", con perfil:::"+objPerfil.getId());
									listAddresses.add(new InternetAddress(strUltimoParticipante));
								}
							}
						}
					}
				}
				// validacion del usuario actual y el siguiente
				for (int j=0; j<detalle.size();j++){
					Perfil objPerfil=null;
					objPerfil=detalle.get(j).getPerfil();
					if (objPerfil!=null){
						if (objPerfil.getId()==objExpediente.getEmpleado().getPerfil().getId()){
							strGestorActual=objExpediente.getEmpleado().getCorreo(); // CORREO DEL USUARIO QUIEN LO TIENE EN ESE MOMENTO.
							if (strGestorActual!=null){
								LOG.info("Validacion usuario actual, Empleado  "+objExpediente.getEmpleado().getCodigo()+", con perfil:::"+objPerfil.getId());
								listAddresses.add(new InternetAddress(strGestorActual));
							}
						}
						if (objPerfil.getId()==empleadoPerfil.getEmpleadoVO().getIdPerfilFk()){
							strUsuarioSgteTarea=empleadoPerfil.getEmpleadoVO().getCorreo(); // CORREO DEL USUARIO A QUIEN VA , DE LA SIGUIENTE TAREA
							if (strUsuarioSgteTarea!=null){
								LOG.info("Validacion usuario siguiente, Empleado  "+empleadoPerfil.getEmpleadoVO().getCodigo()+", con perfil:::"+objPerfil.getId());
								listAddresses.add(new InternetAddress(strUsuarioSgteTarea));
							}
						}
					}
				}
				InternetAddress[] arrAddresses = listAddresses.toArray(new InternetAddress[0]);
				InternetAddress[] arrAddressCopia =listAddressesCopia.toArray(new InternetAddress[0]);
				LOG.info("Entrara al Util Enviar Correo");
				enviarMail(nuevoAsunto, arrAddresses,arrAddressCopia, nuevoCuerpo);
			}
		}else{
			LOG.info("No existe correo");
		}
	 }
	
	public String modificarTexto(String original, Expediente expediente, List<Historial> listHistorial, 
			EmpleadoPerfilVO empleadoPerfil){
		String newOriginal="";
		
		if (expediente.getId()!=0){
			original=original.replaceAll("NRO_EXPEDIENTE",String.valueOf(expediente.getId()));
		}
		
		if (expediente.getEstado()!=null){
			original=original.replaceAll("ESTADO_EXPEDIENTE",String.valueOf(expediente.getEstado().getDescripcion()));
		}
		
		if (expediente.getClienteNatural()!=null){
			original=original.replaceAll("NOMBRE_CLIENTE",String.valueOf(expediente.getClienteNatural().getNombre()));
		}
		if (expediente.getClienteNatural()!=null){
			original=original.replaceAll("APEPAT_CLIENTE",String.valueOf(expediente.getClienteNatural().getApePat()));
			original=original.replaceAll("APEMAT_CLIENTE",String.valueOf(expediente.getClienteNatural().getApeMat()));
		}	
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoMonedaSol()!=null){
			long idTipoMonedaSol=expediente.getExpedienteTC().getTipoMonedaSol().getId();
			TipoMoneda tipoMoneda = new TipoMoneda();
			tipoMoneda = tipoMonedaBeanLocal.buscarPorId(idTipoMonedaSol);
			
			String tipoMonedaDescripcion = tipoMoneda.getDescripcion();
			
			if(tipoMonedaDescripcion != null){
				if(tipoMonedaDescripcion.equals("$")){
					original=original.replaceAll("TIPO_MONEDA_SOLICITADO","\\" + tipoMonedaDescripcion);
				}else{
					original=original.replaceAll("TIPO_MONEDA_SOLICITADO", tipoMonedaDescripcion);
				}
			}
			
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoMonedaAprob()!=null){
			long idTipoMonedaAprob=expediente.getExpedienteTC().getTipoMonedaAprob().getId();
			TipoMoneda tipoMoneda = new TipoMoneda();
			tipoMoneda = tipoMonedaBeanLocal.buscarPorId(idTipoMonedaAprob);
			original=original.replaceAll("TIPO_MONEDA_APROBADO",String.valueOf(tipoMoneda.getDescripcion()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getLineaCredAprob()>=0){
			original=original.replaceAll("LINEA_CREDITO_APROBADA",String.valueOf(expediente.getExpedienteTC().getLineaCredAprob()));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getLineaCredSol()>=0){
			original=original.replaceAll("LINEA_CREDITO_SOLICITADA",String.valueOf(expediente.getExpedienteTC().getLineaCredSol()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getLineaCredSol()>=0){
			original=original.replaceAll("LINEA_CREDITO",String.valueOf(expediente.getExpedienteTC().getLineaCredSol()));
		}
		
		/*INICIO INCIDENCIA 85 06.01.2015*/
		if (expediente!=null && expediente.getEmpleado()!=null){ 
			List<Historial> lstHistExpediente = new ArrayList<Historial>();
			long idPerfilEjecutivo=6;
			//lstHistExpediente=historialBeanLocalBean.buscarXCriterioExpedienteXPerfil(expediente.getId(),expediente.getEmpleado().getPerfil().getId());
			lstHistExpediente=historialBeanLocalBean.buscarXCriterioExpedienteXPerfil(expediente.getId(),idPerfilEjecutivo);  // busca el ultimo EJECUTIVO que reviso el expediente
			String strCodigoUltimoEjecutivo="";
			String strUltimoEjecutivo="";
			String strUltimoEjec="";
			if (lstHistExpediente.size()>0){
				for (Historial item :lstHistExpediente ){
					strCodigoUltimoEjecutivo=item.getEmpleado().getCodigo();
					strUltimoEjecutivo=item.getEmpleado().getNombresCompletos();
					strUltimoEjec=strCodigoUltimoEjecutivo+ " - " +strUltimoEjecutivo;
					if (strUltimoEjecutivo!=null){
						original=original.replaceAll("ULTIMO_EJECUT_HISTORIAL",strUltimoEjec);
						
					}
				}
			}
		}

		/*FIN INCIDENCIA 85 06.01.2015*/

		if (expediente!=null && expediente.getEmpleado()!=null){
			String strPerfil="";
			Empleado empleado= new Empleado();
			empleado=empleadoBeanLocalBean.buscarPorId(expediente.getEmpleado().getId());
			if (empleado!=null){
				Perfil perfil= new Perfil();
				perfil=perfilBeanLocalBean.buscarPorId(empleado.getPerfil().getId());
				strPerfil=perfil.getDescripcion();
			}
			if (strPerfil!=null){
				original=original.replaceAll("PERFIL_USUARIO_ORIGEN",strPerfil);
			}else{
				original=original.replaceAll("PERFIL_USUARIO_ORIGEN","");
			}
		}
		
		
		if (empleadoPerfil!=null){
			original=original.replaceAll("PERFIL_USUARIO_DESTINO",String.valueOf(empleadoPerfil.getDescripcionPerfil()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getOficina()!=null){
			original=original.replaceAll("COD_OFICINA",String.valueOf(expediente.getExpedienteTC().getOficina().getCodigo()));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getOficina()!=null){
			original=original.replaceAll("NOMBRE_OFICINA",String.valueOf(expediente.getExpedienteTC().getOficina().getDescripcion()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getNroCta()!=null){
			original=original.replaceAll("NRO_CUENTA",String.valueOf(expediente.getExpedienteTC().getNroCta()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getNroContrato()!=null){
			original=original.replaceAll("NRO_CONTRATO",String.valueOf(expediente.getExpedienteTC().getNroContrato()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getNumTarjeta()!=null){
			original=original.replaceAll("NUM_TARJETA",String.valueOf(expediente.getExpedienteTC().getNumTarjeta()));
		}
	
		if (expediente.getComentario()!=null){
			original=original.replaceAll("COMENTARIO",String.valueOf(expediente.getComentario()));
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getCodCliente()!=null){
			original=original.replaceAll("COD_CLIENTE",String.valueOf(expediente.getClienteNatural().getCodCliente()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getClienteNaturalConyuge()!=null){
			long idConyuge=expediente.getExpedienteTC().getClienteNaturalConyuge().getId();
			ClienteNatural conyuge = new ClienteNatural();
			conyuge=clienteNaturalBeanLocalBean.buscarPorId(idConyuge);
			String tipoDoiConyuge=conyuge.getTipoDoi().getDescripcion();
			String doiConyuge=conyuge.getNumDoi();
			String nombreConyuge=conyuge.getNombre();
			String apePatConyuge=conyuge.getApePat();
			String apeMatConyuge=conyuge.getApeMat();
			
			String nombreCompletoConyuge=nombreConyuge+" "+apePatConyuge+" "+apeMatConyuge;
			
			if (tipoDoiConyuge!=null){
				//if (expediente.getClienteNatural().getPersona().getCodigo().equals("2")){
				original=original.replaceAll("TIPO_DOI_CONYUGE",tipoDoiConyuge);
				//}
			}
			
			if (doiConyuge!=null){
				//if (expediente.getClienteNatural().getPersona().getCodigo().equals("2")){
					original=original.replaceAll("NRO_DOI_CONYUGE",doiConyuge);
				//}
			}
				
			if (nombreCompletoConyuge!=null){
				//if (expediente.getClienteNatural().getPersona().getCodigo().equals("2")){
					//String nombreConyuge = expediente.getClienteNatural().getNombre()+" "+expediente.getClienteNatural().getApePat()+" "+expediente.getClienteNatural().getApeMat();
					original=original.replaceAll("NOMBRE_COMPLETO_CONYUGE",nombreCompletoConyuge);
				//}
			}
			
		}else{
			original=original.replaceAll("TIPO_DOI_CONYUGE"," ");
			original=original.replaceAll("NRO_DOI_CONYUGE"," ");
			original=original.replaceAll("NOMBRE_COMPLETO_CONYUGE"," ");
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getTipoDoi()!=null){	
			original=original.replaceAll("TIPO_DOI",String.valueOf(expediente.getClienteNatural().getTipoDoi().getDescripcion()));
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getNumDoi()!=null){	
			original=original.replaceAll("NRO_DOI",String.valueOf(expediente.getClienteNatural().getNumDoi()));
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getFecVenDoi()!=null){	
			SimpleDateFormat dateformat= new SimpleDateFormat("dd-MM-yyyy");
			String fechaVenDoi=dateformat.format(expediente.getClienteNatural().getFecVenDoi());
			//original=original.replaceAll("FECHA_VEN_DOI",String.valueOf(expediente.getClienteNatural().getFecVenDoi()));
			original=original.replaceAll("FECHA_VEN_DOI",fechaVenDoi);
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getEstadoCivil()!=null){	
			original=original.replaceAll("ESTADO_CIVIL",String.valueOf(expediente.getClienteNatural().getEstadoCivil().getDescripcion()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getSubproducto()!=null){	
			original=original.replaceAll("SUB_PRODUCTO",String.valueOf(expediente.getExpedienteTC().getSubproducto().getDescripcion()));
		}
		
		if (expediente.getProducto()!=null){
			original=original.replaceAll("PRODUCTO",String.valueOf(expediente.getProducto().getDescripcion()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoOferta()!=null){	
			original=original.replaceAll("TIPO_OFERTA",String.valueOf(expediente.getExpedienteTC().getTipoOferta().getDescripcion()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getPlazoSolicitado()!=null){
			original=original.replaceAll("PLAZO_SOLICITADO",String.valueOf(expediente.getExpedienteTC().getPlazoSolicitado()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null){
			original=original.replaceAll("PLAZO_APROBADO",String.valueOf(expediente.getExpedienteTC().getPlazoSolicitadoApr()));
		}
		
		if (expediente!=null && expediente.getEmpleado()!=null){
			String strEmpleado="";
			Empleado empleado = new Empleado();
			empleado=empleadoBeanLocalBean.buscarPorId(expediente.getEmpleado().getId());
			if (empleado!=null){
				strEmpleado=empleado.getCodigo();
			}
			if (strEmpleado!=null){
				original=original.replaceAll("USUARIO_ORIGEN",strEmpleado);
			}else{
				original=original.replaceAll("USUARIO_ORIGEN","");
			}
		}
		
		if (empleadoPerfil!=null && empleadoPerfil.getEmpleadoVO()!=null){
			original=original.replaceAll("USUARIO_DESTINO",String.valueOf(empleadoPerfil.getEmpleadoVO().getCodigo()));
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getSegmento()!=null){
			original=original.replaceAll("SEGMENTO",String.valueOf(expediente.getClienteNatural().getSegmento().getDescripcion()));
		}
		
		List<CategoriaRenta> listCategorias=null;
		if (expediente.getClienteNatural().getCategoriasRenta()!=null){
			listCategorias=expediente.getClienteNatural().getCategoriasRenta();
			String categorias="";
				for (int i=0; i<listCategorias.size();i++){
					categorias+=listCategorias.get(i).getDescripcion()+",";
				}
				original=original.replaceAll("CATEGORIA_RENTA",categorias);
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getCodPreEval()!=null){
			original=original.replaceAll("COD_PRE_EVALUADOR",String.valueOf(expediente.getExpedienteTC().getCodPreEval()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getVerifDom()!=null){
			if (expediente.getExpedienteTC().getVerifDom().equals("1")){
				original=original.replaceAll("VERIF_DOMICILIARIA","VERIFICACION DOMICILIARIA ACTIVA");
			}else{
				original=original.replaceAll("VERIF_DOMICILIARIA","VERIFICACION DOMICILIARIA NO ACTIVA");
			}
			
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getVerifLab()!=null){
			//original=original.replaceAll("VERIF_LABORAL",String.valueOf(expediente.getExpedienteTC().getVerifLab()));
			if (expediente.getExpedienteTC().getVerifDom().equals("1")){
				original=original.replaceAll("VERIF_LABORAL","VERIFICACION LABORAL ACTIVA");
			}else{
				original=original.replaceAll("VERIF_LABORAL","VERIFICACION LABORAL NO ACTIVA");
			}
		}
			
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getVerifDps()!=null){
			//original=original.replaceAll("VERIF_DPS",String.valueOf(expediente.getExpedienteTC().getVerifDps()));
			if (expediente.getExpedienteTC().getVerifDom().equals("1")){
				original=original.replaceAll("VERIF_DPS","VERIFICACION DPS ACTIVA");
			}else{
				original=original.replaceAll("VERIF_DPS","VERIFICACION DPS NO ACTIVA");
			}
		}	
		original=original.replaceAll("PERFIL_SUPERIOR_RIESGOS","SUPERVISOR RIESGOS");
		
		
		/*INICIO INCIDENCIA 86 06.01.2015*/
		if (expediente!=null && expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getComentarioRechazo()!=null){
			original=original.replaceAll("COMENTARIO_RECHAZO",String.valueOf(expediente.getExpedienteTC().getComentarioRechazo()));
		}else{
			original=original.replaceAll("COMENTARIO_RECHAZO","");
		}
		
		if (expediente!=null && expediente.getExpedienteTC().getTarea()!=null){ 
			List<DevolucionTarea> lstDevolucionTareaByExp=new ArrayList<DevolucionTarea>();
			lstDevolucionTareaByExp=devolucionTareaBeanLocal.buscarPorIdExpedienteIdTarea(expediente.getId(),expediente.getExpedienteTC().getTarea().getId());
			if (lstDevolucionTareaByExp.size()>0){
				for (DevolucionTarea item : lstDevolucionTareaByExp){
					item=lstDevolucionTareaByExp.get(0);
					MotivoDevolucion motivoDevolucion =new MotivoDevolucion();
					motivoDevolucion=motivoDevolucionBeanLocal.buscarPorId(Long.valueOf(item.getMotivoDevolucion().getId()));
					if (motivoDevolucion!=null){
						original=original.replaceAll("MOTIVO_RECHAZO",String.valueOf(motivoDevolucion.getDescripcion()));
					}else{
						List<Historial> lstHistComentRechazo = new ArrayList<Historial>();
						lstHistComentRechazo=historialBeanLocalBean.buscarXCriterioExpedienteXPerfil(expediente.getId(),expediente.getEmpleado().getPerfil().getId());
						long idMotDev=0L;
						if (lstHistComentRechazo.size()>0){
							for (Historial item1 :lstHistComentRechazo ){
								if (item.getMotivoDevolucion()!=null){
									MotivoDevolucion motDev = new MotivoDevolucion();
									idMotDev=item1.getMotivoDevolucion().getId();
									motDev=motivoDevolucionBeanLocal.buscarPorId(idMotDev);
									if (motDev!=null){
										original=original.replaceAll("MOTIVO_RECHAZO",String.valueOf(motDev.getDescripcion()));
									}
								}
							}
						}
					}
				}
			}
		}
		/*FIN INCIDENCIA 86 06.01.2015*/
		
	///////INICIO cambios 09-07-205 Generar Tags de Analisis y Alta
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoScoring() != null){
				original=original.replaceAll("SCORING",String.valueOf(expediente.getExpedienteTC().getTipoScoring().getDescripcion()));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getPorcentajeEndeudamiento() > 0){
			String output = String.format( new Locale("es_PE"),"%.2f", expediente.getExpedienteTC().getPorcentajeEndeudamiento());
			original=original.replaceAll("PORCENTAJE_ENDEUDAMIENTO",String.valueOf(output)+"%");
		}else{
			original=original.replaceAll("PORCENTAJE_ENDEUDAMIENTO",String.valueOf("--"));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getRvgl() != null){
			original=original.replaceAll("RVGL",String.valueOf(expediente.getExpedienteTC().getRvgl()));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoBuro() != null){
			original=original.replaceAll("BURO",String.valueOf(expediente.getExpedienteTC().getTipoBuro().getDescripcion()));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getSbsConyuge() > 0){
			String output = String.format( new Locale("es_PE"),"%.2f", expediente.getExpedienteTC().getSbsConyuge());
			original=original.replaceAll("SBS_CONYUGE",String.valueOf(output)+"%");
		}else{
			original=original.replaceAll("SBS_CONYUGE",String.valueOf("--"));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getClasificacionSbs()> 0){
			String output = String.format( new Locale("es_PE"),"%.2f", expediente.getExpedienteTC().getClasificacionSbs());
			original=original.replaceAll("SBS_TITULAR",String.valueOf(output)+"%");
		}else{
			original=original.replaceAll("SBS_TITULAR",String.valueOf("--"));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getBancoConyuge() !=null ){
			original=original.replaceAll("BANCO_CONYUGE",String.valueOf(expediente.getExpedienteTC().getBancoConyuge().getDescripcion()));
		}else{
			original=original.replaceAll("BANCO_CONYUGE",String.valueOf("--"));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getClasificacionBanco() !=null ){
			original=original.replaceAll("BANCO_TITULAR",String.valueOf(expediente.getExpedienteTC().getClasificacionBanco().getDescripcion()));
		}
		///////FIN cambios 09-07-205 Generar Tags de Analisis y Alta
		
		newOriginal=original;
		return newOriginal;
	}
	
	private void agregar (Map<Integer, ArrayList<Empleado>> mapa, Integer peso, Empleado idEmpleado) {
		ArrayList<Empleado> lista = mapa.get(peso);
		if (lista==null) {
			lista = new ArrayList<Empleado> ();
			mapa.put(peso, lista);
		}
		lista.add(idEmpleado);
	}
	
	public boolean actualizaEmpleadoExpediente(Long idEmpleado, Long idExpediente){
		LOG.info("actualizaEmpleadoExpediente >>>> idEmpleado : "+idEmpleado+" idExpediente : "+idExpediente);	

		Expediente objExpediente=expedienteBeanLocalBean.buscarPorId(idExpediente);
		if(objExpediente!=null){
			objExpediente.setEmpleado(new Empleado());
			objExpediente.getEmpleado().setId(idEmpleado);
			expedienteBeanLocalBean.edit(objExpediente);	
			return true;
		}
		return false;		
    }	
	
	public ParametrosProcesoDTO obtenerParametroProceso(String nombreVariable){
		LOG.info("obtenerParametroProceso >>>> nombreVariable : "+nombreVariable);	

		ParametrosProceso objParametro=parametrosProcesoBeanLocalBean.buscarPorVariable(nombreVariable);		
		
		ParametrosProcesoDTO parametrosProceso = new ParametrosProcesoDTO();
		if(objParametro!=null){
			parametrosProceso.setId(objParametro.getId());
			parametrosProceso.setNombreVariable(objParametro.getNombreVariable());
			parametrosProceso.setValorVariable(objParametro.getValorVariable());
		}		
		return parametrosProceso;
    }
	
	
	/**
	 * Metodo para obtener el tipo de cambio del día en la tabla
	 * Si no existe se obtiene el tipo de cambio top de la tabla.
	 */
	private TipoCambioCE obtenerTipoCambioExp(ExpedienteTC expedienteTC, Empleado empleado) {
		
		TipoCambioCE objTipoCambioCE = null;
		LOG.info("expedienteTC.getFechaTipoCambioExp:" + expedienteTC.getFechaTipoCambioExp());
		LOG.info("expedienteTC.getTipoCambioExp:" + expedienteTC.getTipoCambioExp());
		if (expedienteTC.getFechaTipoCambioExp() == null && expedienteTC.getTipoCambioExp() == null) {
			objTipoCambioCE = obtenerTipoCambio(empleado);
		}else{
			objTipoCambioCE = new TipoCambioCE();
			objTipoCambioCE.setFecha(expedienteTC.getFechaTipoCambioExp());
			objTipoCambioCE.setMonto(expedienteTC.getTipoCambioExp());
		}
		return objTipoCambioCE;
	}
	private TipoCambioCE obtenerTipoCambio(Empleado empleado) {
		
		TipoCambioCE objTipoCambioCE = null;		
		LOG.info(" Verificando Tipo de Cambio");
		Date fecha = Util.formatearFecha(new Date(), "dd/MM/yyyy");				
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
					
			String strFecha = Util.formatDateObject("dd.MM.yyyy", new Date());
			String tipoCambioDivisa = parametrosConfBeanLocalBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_TIPO_CAMBIO_DIVISA").getValorVariable();
			LOG.info("Fecha solicitud: = " + strFecha);
			LOG.info("Tipo Cambio Solicitud = " + tipoCambioDivisa);
			LOG.info("Codigo Empleado: " + empleado.getCodigo());
			ctHeader.setUsuario(empleado.getCodigo());			
			CtBodyRq.setFechaCambio(strFecha);
			CtBodyRq.setTipoCambio(tipoCambioDivisa);
			 
			inCtExtraerTipoCambioRq.setHeader(ctHeader);
			inCtExtraerTipoCambioRq.setData(CtBodyRq);
			LOG.info("-------------------------- INICIO CONSULTA AL SERVICIO EXTRAER TIPO CAMBIO ----------------------------");	
			 
			try{
			 LOG.info("DATOS REQUEST SERVICIO TIPO CAMBIO: " +
			 		"["+ "Codigo=" + ctHeader.getUsuario() +"; " 
					   + "Fecha Cambio=" + CtBodyRq.getFechaCambio() + "]");			 
			 outCtExtraerTipoCambioRs = bbvaFacade.extraerTipoCambio(inCtExtraerTipoCambioRq);
			 
			}catch(Throwable e){
			 LOG.error("Ocurrio un error de conexion con el servicio TIPO CAMBIO " + e);
			}
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
		LOG.info("Tipo de cambio = " + objTipoCambioCE);
		return objTipoCambioCE;
	}
	
	/**
	 * Metodo para calcular el tipo de cambio si el tipo de moneda es dolares.
	 * @param monto
	 * @param objTipoMoneda
	 * @param tipoCambioCE
	 * @return
	 */
	private double calcularTipoCambio(double monto, Object objTipoMoneda,  TipoCambioCE tipoCambioCE){
		
		if (Util.isDouble(String.valueOf(monto))) {
			if (objTipoMoneda instanceof TipoMoneda) {
				TipoMoneda tipoMoneda = (TipoMoneda) objTipoMoneda;
				if (tipoMoneda.getCodigo().equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
					BigDecimal nuevoMonto = new BigDecimal(monto).multiply(tipoCambioCE.getMonto());
					return nuevoMonto.doubleValue();
				} else {
					return monto;
				}
			} else if (objTipoMoneda instanceof Long) {
				Long tipoMoneda = (Long) objTipoMoneda;
				if (String.valueOf(tipoMoneda).equals(Constantes.CODIGO_TIPO_CAMBIO_DOLARES)) {
					BigDecimal nuevoMonto = new BigDecimal(monto).multiply(tipoCambioCE.getMonto());
					return nuevoMonto.doubleValue();
				} else {
					return monto;
				}
			} else {
				return monto;
			}
		} else{
			return monto;
		}		
	}
	
	
public void enviarMail(String asunto, InternetAddress[] listAddresses, InternetAddress[] listAddressesCopia,String mensaje) {
		
		LOG.info("---------- Inicio Método enviarMail ------------");
		String hostServerMail = null;
		String puertoServerMail = null;
		String remitenteMail = null;
				
		try{
			LOG.info("Consultar en TBL_CE_IBM_PARAMETROS_CONF: Host, Puerto, Remitente");
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			hostServerMail = parametrosConfBean.buscarPorVariable(Constantes.COD_ENVIOMAIL_HOST, Constantes.NOMBRE_VARIABLE_MAILHOST).getValorVariable();
			LOG.info("hostServerMail: "+hostServerMail);
			puertoServerMail = parametrosConfBean.buscarPorVariable(Constantes.COD_ENVIOMAIL_PUERTO, Constantes.NOMBRE_VARIABLE_MAILPUERTO).getValorVariable();
			LOG.info("puertoServerMail: "+puertoServerMail);
			remitenteMail = parametrosConfBean.buscarPorVariable(Constantes.COD_ENVIOMAIL_REMITENTE, Constantes.NOMBRE_VARIABLE_MAILREMITENTE).getValorVariable();
			LOG.info("remitenteMail: "+ remitenteMail);
			
		}catch (NamingException e){
			LOG.error(e.getMessage(), e);
			LOG.error("Error al enviar el Mail..faltan parametros", e);
		}
		
		LOG.info("Host: " + hostServerMail);
		LOG.info("Puerto: " + puertoServerMail);
		LOG.info("Remitente: " + remitenteMail);
		LOG.info("Asunto: " + asunto);
		LOG.info("Cuerpo: " + mensaje);
		
		
		if(listAddresses != null){
			for(int i = 0; i < listAddresses.length; i++)     
				LOG.info("Email To: " + listAddresses[i]);
		}
		
		if (listAddressesCopia!=null){
			for(int i = 0; i < listAddressesCopia.length; i++)     
				LOG.info("Email CC: " + listAddressesCopia[i]);
		}
				
		try{
			LOG.info("Enviar Mail...");
			Properties properties = new Properties();
			LOG.info("Instancio properties");
			Session session = Session.getDefaultInstance(properties);
			session.getProperties().setProperty("mail.smtp.host", hostServerMail);
			LOG.info("mail.smtp.host: " + session.getProperties().getProperty("mail.smtp.host", hostServerMail));
			session.getProperties().setProperty("mail.smtp.port", puertoServerMail);
			LOG.info("mail.smtp.port: " + session.getProperties().getProperty("mail.smtp.port", puertoServerMail));
			
			// compose the message
			MimeMessage message = new MimeMessage(session);			
			message.setFrom(new InternetAddress(remitenteMail));
			
			if(listAddresses != null){
				message.addRecipients(Message.RecipientType.TO, listAddresses);
			}
			
			if (listAddressesCopia!=null){
				message.addRecipients(Message.RecipientType.CC, listAddressesCopia);
			}
			
			message.setSubject(asunto);
			
			message.setContent(mensaje, "text/html; charset=utf-8");

			// Send message
			Transport.send(message);
			LOG.info("Se envió el Mail satisfactoriamente.");
			
			
		}catch(MessagingException e){
			LOG.error("Error al enviar el Mail..", e);
			
		}		
		LOG.info("---------- Fin Método enviarMail ------------");
		
	}
	
	public Perfil obtenerPerfilActual(Long idTarea){
		long idPerfilActual=0;
		if (idTarea==1 || idTarea==10 || idTarea==15 ||idTarea== 16 || idTarea==20){
			idPerfilActual=6;
		}else if (idTarea==2 ){
			idPerfilActual=5;
		}else if (idTarea==3 || idTarea==14 || idTarea==18 ){
			idPerfilActual=3;
		}else if (idTarea==4){
			idPerfilActual=13;
		}else if (idTarea==5 || idTarea==6 || idTarea==13 ||idTarea== 17 || idTarea==26){
			idPerfilActual=10;
		}else if (idTarea==7 || idTarea==11 || idTarea==27){
			idPerfilActual=2;
		}else if (idTarea==12){
			idPerfilActual=8;
		}else if (idTarea==19){
			idPerfilActual=4;
			}
		Perfil perfilActual=null;
		perfilActual=perfilBeanLocalBean.buscarPorId(idPerfilActual);
		return perfilActual;
	}
	
	/**
	 * Obtener todos los documentos por un id de expediente
	 * @param idExpediente
	 * @return
	 */
	public List<DocumentoExpTc> consultarDocumentoExpedientePorIdExpediente(Long idExpediente) {
		
		if(Util.isLong(idExpediente.toString())){
			List<DocumentoExpTc> docs = expTcBeanLocalBean.buscarPorExpediente(idExpediente);
			return docs;
		}
		return null;
	}
	
}
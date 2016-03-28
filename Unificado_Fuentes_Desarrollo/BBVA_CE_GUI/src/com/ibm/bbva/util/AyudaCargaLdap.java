package com.ibm.bbva.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Cliente;
import pe.ibm.bean.ClienteWeb;
import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bean.Producto;
import pe.ibm.bean.ProductoWeb;
import pe.ibm.bpd.RemoteUtils;

import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.entities.TipoScoring;
import com.ibm.bbva.proxy.ext.UsuarioExtendido;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.EstadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import com.ibm.bbva.session.TipoDoiBeanLocal;
import com.ibm.bbva.session.TipoScoringBeanLocal;

public class AyudaCargaLdap extends AbstractMBean{

	private static final Logger LOG = LoggerFactory.getLogger(AyudaCargaLdap.class);

	private TareaBeanLocal tareaBean;
	private BBVAFacadeLocal bbvaFacade;
	private TipoClienteBeanLocal tipoClienteBean;
	private ExpedienteBeanLocal expedienteBean;
	private AnsBeanLocal ansBean;
	private EmpleadoBeanLocal empleadobean;	
	private CartEmpleadoCEBeanLocal cartEmpleadoCEBeanLocal;
		
	public AyudaCargaLdap()  {
		super();
		try {
			tareaBean=(TareaBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.TareaBeanLocal");
			bbvaFacade=(BBVAFacadeLocal) new InitialContext().lookup("ejblocal:bbva.ws.api.view.BBVAFacadeLocal");
			tipoClienteBean=(TipoClienteBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.TipoClienteBeanLocal");
			expedienteBean=(ExpedienteBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ExpedienteBeanLocal");
			ansBean=(AnsBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.AnsBeanLocal");
			empleadobean=(EmpleadoBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.EmpleadoBeanLocal");
			cartEmpleadoCEBeanLocal=(CartEmpleadoCEBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.CartEmpleadoCEBeanLocal");
				
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}	
	}
	
	
	
	public Boolean validarTemporalidadPuestoIDM(UsuarioExtendido usuarioIDM){
		LOG.info("FECHA INICIO PUESTO TEMPORAL DE IDM  "+ (usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaInicio().toGregorianCalendar().getTime().toString():"0"));
		Date puestoTempFechaInicioIDM = usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaInicio().toGregorianCalendar().getTime():null;
		LOG.info("FECHA INICIO PUESTO TEMPORAL DE IDM CON FORMATO "+puestoTempFechaInicioIDM);
		
		LOG.info("FECHA FIN PUESTO TEMPORAL DE IDM  "+ (usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaFin().toGregorianCalendar().getTime().toString():"0"));
		Date puestoTempFechaFinIDM = usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaFin().toGregorianCalendar().getTime():null;
		LOG.info("FECHA FIN PUESTO TEMPORAL DE IDM CON FORMATO "+puestoTempFechaFinIDM);
					
		boolean flagTienePuestoTemporal = (usuarioIDM.getPuestoTemporal()!=null && 
				(puestoTempFechaInicioIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss:SSS"),"dd/MM/yyyy HH:mm:ss:SSS"))) &&
				(!puestoTempFechaFinIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss:SSS"),"dd/MM/yyyy HH:mm:ss:SSS")))
				&& (!usuarioIDM.getPuestoTemporal().getDescripcionPuesto().equals(usuarioIDM.getPuesto().getNombreCargoFuncionalLocal())))?
										  StringUtils.isNotBlank(
										  usuarioIDM.getPuestoTemporal().getDescripcionPuesto()):false;
										  
		return flagTienePuestoTemporal;							  
	}
	
	public Boolean validarTemporalidadOficinaIDM(UsuarioExtendido usuarioIDM){
		LOG.info("FECHA INICIO PUESTO TEMPORAL DE IDM  "+ (usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaInicio().toGregorianCalendar().getTime().toString():"0"));
		Date puestoTempFechaInicioIDM = usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaInicio().toGregorianCalendar().getTime():null;
		LOG.info("FECHA INICIO PUESTO TEMPORAL DE IDM CON FORMATO "+puestoTempFechaInicioIDM);
		
		LOG.info("FECHA FIN PUESTO TEMPORAL DE IDM  "+ (usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaFin().toGregorianCalendar().getTime().toString():"0"));
		Date puestoTempFechaFinIDM = usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaFin().toGregorianCalendar().getTime():null;
		LOG.info("FECHA FIN PUESTO TEMPORAL DE IDM CON FORMATO "+puestoTempFechaFinIDM);
					
		boolean flagTienePuestoTemporal = (usuarioIDM.getPuestoTemporal()!=null && 
				(puestoTempFechaInicioIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss:SSS"),"dd/MM/yyyy HH:mm:ss:SSS"))) &&
				(!puestoTempFechaFinIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss:SSS"),"dd/MM/yyyy HH:mm:ss:SSS")))
				&& (!usuarioIDM.getPuestoTemporal().getDescripcionPuesto().equals(usuarioIDM.getPuesto().getNombreCargoFuncionalLocal())))?
										  StringUtils.isNotBlank(
										  usuarioIDM.getPuestoTemporal().getDescripcionPuesto()):false;
										  
		return flagTienePuestoTemporal;							  
	}
	
	public void reasignarExpedientes(Empleado subGerenteTieneExp, Empleado subGerenteDebeTenerExp){
    	Consulta consulta = null;
    	List<String> listUsuarios = null;
    	List<ExpedienteTCWPSWeb> listaReasignable = null;
    	ExpedienteTCWrapper objExpedienteTCWrapper = null;
    	RemoteUtils tareasBDelegate = new RemoteUtils();
    	String mensaje = "ERROR";
				
		try{
			
			if(subGerenteTieneExp != null && subGerenteTieneExp.getCodigo().length()>0){
				consulta = new Consulta();		
				listUsuarios=new ArrayList<String>();
				listUsuarios.add(subGerenteTieneExp.getCodigo()); 
				consulta.setUsuarios(listUsuarios);
				consulta.setConsiderarUsuarios(true);		
				listaReasignable = tareasBDelegate.obtenerListaTareasBandPendiente(consulta);
				
				if(listaReasignable != null){
					for(ExpedienteTCWPSWeb objExpedienteTCWPSWeb : listaReasignable){
	
						if(subGerenteTieneExp.getId() != subGerenteDebeTenerExp.getId()){
							LOG.info("Expediente: " + objExpedienteTCWPSWeb.getCodigo()+ " Task: " + objExpedienteTCWPSWeb.getTaskID());
							LOG.info("EMPLEADO ASIGNADO CON PERFIL SGT = "+subGerenteDebeTenerExp.getCodigo());
							mensaje = "ERROR";	
							mensaje = tareasBDelegate.transferirTarea(subGerenteTieneExp.getCodigo(), subGerenteDebeTenerExp.getCodigo(), objExpedienteTCWPSWeb.getTaskID());
							
							if (mensaje.equals("SUCCESS")){
								LOG.info("Transferencia exitosa para expediente:" + objExpedienteTCWPSWeb.getCodigo());
								objExpedienteTCWrapper = new ExpedienteTCWrapper(objExpedienteTCWPSWeb,
												null, tareaBean, bbvaFacade,
												expedienteBean, tipoClienteBean, ansBean);

								objExpedienteTCWrapper.setIdPerfilUsuarioActual(String.valueOf(subGerenteDebeTenerExp.getPerfil().getId()));
								objExpedienteTCWrapper.setPerfilUsuarioActual(subGerenteDebeTenerExp.getPerfil().getDescripcion());
								objExpedienteTCWrapper.setCodigoUsuarioActual(subGerenteDebeTenerExp.getCodigo());
													
								objExpedienteTCWPSWeb.setNombreUsuarioActual(subGerenteDebeTenerExp.getNombresCompletos());
								objExpedienteTCWPSWeb.setIdPerfilUsuarioActual(String.valueOf(subGerenteDebeTenerExp.getPerfil().getId()));
								objExpedienteTCWPSWeb.setPerfilUsuarioActual(subGerenteDebeTenerExp.getPerfil().getDescripcion());
								objExpedienteTCWPSWeb.setCodigoUsuarioActual(subGerenteDebeTenerExp.getCodigo());
													
								tareasBDelegate.enviarExpedienteTC(objExpedienteTCWrapper.getTaskID(), objExpedienteTCWrapper.getExpedienteTC());
								LOG.info("Envio exitoso para expediente:" + objExpedienteTCWrapper.getExpedienteTC().getCodigo());
								
								Expediente objExpediente = expedienteBean.buscarPorId(Long.valueOf(objExpedienteTCWPSWeb.getCodigo()));
								LOG.info("Actualizacion de Empleado Asignado con perfil SGT");
								if(objExpediente != null){
									LOG.info("actualizaEmpleadoExpediente >>>> idEmpleado : "+subGerenteDebeTenerExp.getId()+" idExpediente : "+objExpediente.getId());
									objExpediente.setEmpleado(new Empleado());
									objExpediente.getEmpleado().setId(subGerenteDebeTenerExp.getId());
									expedienteBean.edit(objExpediente);		
								}										
							}	
							
						}
					}	
					
				}

			}	
				
		}catch(Exception ex){
			LOG.error("Error::"+ex.getMessage(),ex);
		}
    }
	
	public void actualizarDatosOficinaEmpleado(Empleado objEmpleado){
		objEmpleado.setOficina(objEmpleado.getOficinaBackup());
		objEmpleado.setOficinaBackup(null);
		empleadobean.edit(objEmpleado);
		
		List<CartEmpleadoCE> listaCartEmpleadoCE = cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(objEmpleado.getId());
		for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
		{
			objCartEmpleadoCE.setOficina(objEmpleado.getOficina());
			cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
		}
    }
	
	public void actualizarDatosEmpleado(Empleado objEmpleado){
		objEmpleado.setPerfil(objEmpleado.getPerfilBackup());
		objEmpleado.setPerfilBackup(null);
		objEmpleado.setCodigoCargo(objEmpleado.getCodigoCargoBackup());
		objEmpleado.setCodigoCargoBackup(null);
		this.empleadobean.edit(objEmpleado);
		
		List<CartEmpleadoCE> listaCartEmpleadoCE = cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(objEmpleado.getId());
		for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
		{
			objCartEmpleadoCE.setPerfil(objEmpleado.getPerfil());
			cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
		}
    }
	
}

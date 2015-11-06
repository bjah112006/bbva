package com.ibm.bbva.controller.servlet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.soap.providers.com.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bpd.RemoteUtils;

import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.proxy.Usuario;
import com.ibm.bbva.proxy.WSLDAPServiceImplPortProxy;
import com.ibm.bbva.proxy.WSLdapException_Exception;
import com.ibm.bbva.proxy.ext.UsuarioExtendido;
import com.ibm.bbva.proxy.ext.WSLDAPServiceExtImplPortProxy;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;
import com.ibm.bbva.session.DescargaLDAPBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.LdapTempBeanLocal;
import com.ibm.bbva.session.LogJobBeanLocal;
import com.ibm.bbva.session.LogJobDetBeanLocal;
import com.ibm.bbva.session.MutitablaBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TerritorioBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import com.ibm.bbva.session.VistaBandjCartProdBeanLocal;
import com.ibm.bbva.util.ExpedienteTCWrapper;
import com.ibm.bbva.util.Util;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.DescargaLDAP;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.LdapTemp;
import com.ibm.bbva.entities.LogJob;
import com.ibm.bbva.entities.LogJobDet;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.Territorio;
import com.ibm.bbva.entities.VistaBandjCartProd;

@WebServlet("/CargaLdapServlet")
public class CargaLdapServlet extends HttpServlet
{

	private static final long serialVersionUID = 4440011247408877539L;
	
	private static final Logger LOG = LoggerFactory.getLogger(CargaLdapServlet.class);
	
	private ParametrosConfBeanLocal parametrosConfBeanLocal;
	private LdapTempBeanLocal ldapTempBeanLocal;
	private EmpleadoBeanLocal empleadoBeanLocal;
	private WSLDAPServiceExtImplPortProxy objWSLDAPServiceImplPortProxy;
	private VistaBandjCartProdBeanLocal vistaBandjCartProdBeanLocal;
	private ExpedienteBeanLocal expedienteBean;
	private TareaBeanLocal tareaBean;
	private BBVAFacadeLocal bbvaFacade;
	private TipoClienteBeanLocal tipoClienteBean;
	private AnsBeanLocal ansBean;
	private CartEmpleadoCEBeanLocal cartEmpleadoCEBeanLocal;
	private MutitablaBeanLocal multitablaBeanLocal;
	private LogJobBeanLocal logJobBeanLocal;
	private LogJobDetBeanLocal logJobDetBeanLocal;

	private OficinaBeanLocal oficinabean;
	private DescargaLDAPBeanLocal descargaLDAPBeanLocal;
	private EmpleadoBeanLocal empleadobean;	

	public CargaLdapServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		LOG.info("CargaLdapServlet:INICIO PROCESO DE CARGA DE EMPLEADOS");
		try 
		{
			response.setHeader("Content-Type", "text/plain");
				
			parametrosConfBeanLocal = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			ldapTempBeanLocal = (LdapTempBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.LdapTempBeanLocal");
			empleadoBeanLocal = (EmpleadoBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.EmpleadoBeanLocal");
			vistaBandjCartProdBeanLocal = (VistaBandjCartProdBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.VistaBandjCartProdBeanLocal");
			expedienteBean = (ExpedienteBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ExpedienteBeanLocal");
			tareaBean = (TareaBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.TareaBeanLocal");
			bbvaFacade = (BBVAFacadeLocal) new InitialContext().lookup("ejblocal:bbva.ws.api.view.BBVAFacadeLocal");
			tipoClienteBean = (TipoClienteBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.TipoClienteBeanLocal");
			ansBean = (AnsBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.AnsBeanLocal");
			cartEmpleadoCEBeanLocal = (CartEmpleadoCEBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.CartEmpleadoCEBeanLocal");
			multitablaBeanLocal = (MutitablaBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.MutitablaBeanLocal");
			logJobBeanLocal = (LogJobBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.LogJobBeanLocal");
			logJobDetBeanLocal = (LogJobDetBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.LogJobDetBeanLocal");
			oficinabean = (OficinaBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.OficinaBeanLocal");;
			descargaLDAPBeanLocal = (DescargaLDAPBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.DescargaLDAPBeanLocal");;
			empleadobean = (EmpleadoBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.EmpleadoBeanLocal");
	
			if(parametrosConfBeanLocal.buscarPorVariable(Constantes.CODIGO_APLICATIVO_PROCESO_LDAP, Constantes.JOB_CARGA_LDAP_HABILITADO).equals("N"))
			{				
				return;
			}
			
			LogJob objLogJob = new LogJob();
			objLogJob.setNombre("JOB - CARGA LDAP");
			objLogJob.setFechaInicio(new Date());
			objLogJob = logJobBeanLocal.create(objLogJob);
			
			LogJobDet objLogJobDet = null;
			
			objWSLDAPServiceImplPortProxy = new WSLDAPServiceExtImplPortProxy();
			objWSLDAPServiceImplPortProxy._getDescriptor().setEndpoint(parametrosConfBeanLocal.buscarPorVariable(Constantes.CODIGO_APLICATIVO_PROCESO_LDAP, Constantes.LDAP_WEB_SERVICE_EXT_ENDPOINT).getValorVariable());
				
			objLogJobDet = new LogJobDet();
			objLogJobDet.setNombre("Carga LDAP temporal");
			objLogJobDet.setFechaInicio(new Date());
			objLogJobDet.setLogJob(objLogJob);
			objLogJobDet = logJobDetBeanLocal.create(objLogJobDet);
			
					 
				ldapTempBeanLocal.clean();
				LOG.info("CargaLdapServlet: inicio invocacion obtener empleados");
				LOG.info("ws:"+parametrosConfBeanLocal.buscarPorVariable(Constantes.CODIGO_APLICATIVO_PROCESO_LDAP, Constantes.LDAP_WEB_SERVICE_EXT_ENDPOINT).getValorVariable());
				List<UsuarioExtendido> listaUsuarios = objWSLDAPServiceImplPortProxy.obtenerUsuarios(null);
				LOG.info("CargaLdapServlet: fin obtener empleados");
				LdapTemp objLdapTemp = null;
				String oficinasSincronizables = multitablaBeanLocal.buscarPorId(Constantes.PARAMETRO_OFICINAS_SINCRONIZABLES).getTexto();
				
				for(UsuarioExtendido objUsuario : listaUsuarios)
				{					
					objLdapTemp = new LdapTemp();					
					objLdapTemp.setId(objUsuario.getUsuario());
					objLdapTemp.setNombre(objUsuario.getNombres());
					objLdapTemp.setApellidoPaterno(objUsuario.getPrimerApellido());
					objLdapTemp.setApellidoMaterno(objUsuario.getSegundoApellido());
					objLdapTemp.setCorreoElectronico(objUsuario.getEMail());
					objLdapTemp.setCodigoOficinaTemp(null);	
					objLdapTemp.setCodigoCargoTemp(null);	
					//validar si existe temporalidad en el registro
					Date puestoTempFechaInicioIDM = objUsuario.getPuestoTemporal()!=null? objUsuario.getPuestoTemporal().getFechaInicio().toGregorianCalendar().getTime():null;
					Date puestoTempFechaFinIDM = objUsuario.getPuestoTemporal()!=null? objUsuario.getPuestoTemporal().getFechaFin().toGregorianCalendar().getTime():null;
					
					boolean flagTienePuestoTemporal = (objUsuario.getPuestoTemporal()!=null  && 
							(puestoTempFechaInicioIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss:SSS"),"dd/MM/yyyy HH:mm:ss:SSS"))) &&
							(!puestoTempFechaFinIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss:SSS"),"dd/MM/yyyy HH:mm:ss:SSS")))
							&& (!objUsuario.getPuestoTemporal().getDescripcionPuesto().equals(objUsuario.getPuesto().getNombreCargoFuncionalLocal())))?
							  StringUtils.isNotBlank(
									  objUsuario.getPuestoTemporal().getDescripcionPuesto()):false;
					if(flagTienePuestoTemporal){
						//VALIDAR SI CAMBIO DE PERFIL
						String codigoPuestoTemporal = objUsuario.getPuestoTemporal().getDescripcionPuesto();
						//Buscar nuevo peril
						List<DescargaLDAP> listaPerfilesNuevo = descargaLDAPBeanLocal.buscar("-1", codigoPuestoTemporal, "-1", "-1", "-1", "-1");
						String codigoPerfilNuevo = listaPerfilesNuevo.get(0).getPerfil().getCodigo();
						//Buscar peril original
						List<DescargaLDAP> listaPerfilesOriginal = descargaLDAPBeanLocal.buscar("-1", objUsuario.getPuesto().getNombreCargoFuncionalLocal(), "-1", "-1", "-1", "-1");
						String codigoPerfilOriginal = listaPerfilesOriginal.get(0).getPerfil().getCodigo();
						//Empleado empleado = empleadobean.buscarPorCodigo(objUsuario.getUsuario());  
						if(!codigoPerfilOriginal.equals(codigoPerfilNuevo)){
							objLdapTemp.setCodigoCargoTemp(codigoPuestoTemporal);	
						}
						objLdapTemp.setCodigoCargo(codigoPuestoTemporal);
					}else{
						objLdapTemp.setCodigoCargo(objUsuario.getPuesto() != null ? objUsuario.getPuesto().getNombreCargoFuncionalLocal() : null);
						
					}
					
					Date ofiTempFechaInicioIDM = objUsuario.getCentroTemporal()!=null? objUsuario.getCentroTemporal().getFechaInicio().toGregorianCalendar().getTime():null;
					Date ofiTempFechaFinIDM = objUsuario.getCentroTemporal()!=null? objUsuario.getCentroTemporal().getFechaFin().toGregorianCalendar().getTime():null;
					
					boolean flagTieneOficinaTemporal = (objUsuario.getCentroTemporal()!=null &&  
							(ofiTempFechaInicioIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss"),"dd/MM/yyyy HH:mm:ss"))) &&
							(!ofiTempFechaFinIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss"),"dd/MM/yyyy HH:mm:ss")))
							&& (!objUsuario.getCentroTemporal().getDescripcion().equals(objUsuario.getCodigoCentro())))?
												   StringUtils.isNotBlank(
														   objUsuario.getCentroTemporal().getDescripcion()):false;
					if(flagTieneOficinaTemporal){
						objLdapTemp.setCodigoOficinaTemp(objUsuario.getCentroTemporal().getDescripcion());	
						objLdapTemp.setCodigoOficina(objUsuario.getCentroTemporal().getDescripcion());	
					}else{
						objLdapTemp.setCodigoOficina(objUsuario.getCodigoCentro());	
					}
					//fin validacion de temporalidad
					if(oficinasSincronizables == null || oficinasSincronizables.length() == 0)
					{
						ldapTempBeanLocal.create(objLdapTemp);						
					}	
					else
					{
						if(objLdapTemp.getCodigoOficina() != null && oficinasSincronizables.indexOf(objLdapTemp.getCodigoOficina()) != -1)
						{
							ldapTempBeanLocal.create(objLdapTemp);						
						}
					}
				}						
			
			objLogJobDet.setFechaFin(new Date());
			logJobDetBeanLocal.edit(objLogJobDet);
			LOG.info("CargaLdapServlet: inicio invocacion package carga de empleados");
			empleadoBeanLocal.ejecutarDescargaLDAP(objLogJob.getId());
			LOG.info("CargaLdapServlet: fin invocacion package carga de empleados");
			/**
			 ******* REASIGNACIÓN ********			 
			 */
			
			objLogJobDet = new LogJobDet();
			objLogJobDet.setNombre("Reasignacion");
			objLogJobDet.setFechaInicio(new Date());
			objLogJobDet.setLogJob(objLogJob);
			objLogJobDet = logJobDetBeanLocal.create(objLogJobDet);
			
			List<Empleado> listaEmpleado = empleadoBeanLocal.buscarDebenReasignarse();
			RemoteUtils tareasBDelegate = new RemoteUtils();
			boolean reasignado = false; 
			String mensaje = "ERROR";
			boolean cambioOficinaPerfilEstado = true;
			
			Consulta consulta = null;			
			List<ExpedienteTCWPSWeb> listaReasignable = null;
			List<Empleado> listaEmpleadoParaAsignar = null;
			List<VistaBandjCartProd> listCartProducto = null;
			List<Long> listIdsProd = null;
			ExpedienteTCWrapper objExpedienteTCWrapper = null;
			List<String> listUsuarios = null;
			
			//System.out.println("Empleados reasignables : " + listaEmpleado.size());
			
			for(Empleado objEmpleado : listaEmpleado)
			{
				
				//System.out.println("Empleado Deben reasignarse : " + objEmpleado.getCodigo());
				
				cambioOficinaPerfilEstado = true;
				
				/*
				consulta = new Consulta();
				consulta.setTipoConsulta(4);
				consulta.setCodUsuarioActual(objEmpleado.getCodigo());				
				consulta.setIdPerfilUsuarioActual(String.valueOf(objEmpleado.getPerfilAnterior() != null ? objEmpleado.getPerfilAnterior().getId() : objEmpleado.getPerfil().getId()));
				consulta.setIdOficina(String.valueOf(objEmpleado.getOficinaAnterior() != null ? objEmpleado.getOficinaAnterior().getId() : objEmpleado.getOficina().getId()));
				System.out.println("Oficina anterior : " + String.valueOf(objEmpleado.getOficinaAnterior() != null ? objEmpleado.getOficinaAnterior().getId() : objEmpleado.getOficina().getId()));
				listaReasignable = tareasBDelegate.listarTareasBandejaAsignacion(consulta);
				*/
				
				consulta = new Consulta();		
				listUsuarios=new ArrayList<String>();
				listUsuarios.add(objEmpleado.getCodigo()); 
				consulta.setUsuarios(listUsuarios);
				consulta.setConsiderarUsuarios(true);		
				listaReasignable = tareasBDelegate.obtenerListaTareasBandPendiente(consulta);
				
				//System.out.println("Usuario - Expedientes : " + objEmpleado.getCodigo() +  " - " + listaReasignable.size());
				
				listCartProducto = vistaBandjCartProdBeanLocal.verificarCartXProducto(objEmpleado.getPerfil().getId(), 
																						objEmpleado.getOficina().getTerritorio().getId(), 
																					    objEmpleado.getId());
				
				//System.out.println("Número de Productos : " + listCartProducto.size());
				
				listIdsProd = new ArrayList<Long>();
				
				if(listCartProducto != null && listCartProducto.size() > 0)
				{
					for(VistaBandjCartProd obj : listCartProducto){
						if(obj != null && obj.getIdProduto() > 0){
							listIdsProd.add(new Long(obj.getIdProduto()));
							//System.out.println("Producto : " + obj.getIdProduto());
						}
					}
				}

				listaEmpleadoParaAsignar = empleadoBeanLocal.buscarPorPerfilEmpleadoActivo(objEmpleado.getPerfilAnterior() != null ? objEmpleado.getPerfilAnterior().getId() : objEmpleado.getPerfil().getId(), 
																							objEmpleado.getOficinaAnterior() != null ? objEmpleado.getOficinaAnterior().getId() : objEmpleado.getOficina().getId(), listIdsProd);
				//System.out.println("Empleados par asignar : " + listaEmpleadoParaAsignar.size());
				
				
				if(listaReasignable != null)
				{
					
					for(ExpedienteTCWPSWeb objExpedienteTCWPSWeb : listaReasignable)
					{

						reasignado = false;
						for(Empleado objEmpleadoAsignar : listaEmpleadoParaAsignar)
						{

							if(reasignado) { break; }
							if(objEmpleadoAsignar.getId() != objEmpleado.getId())
							{
								//System.out.println("Se intentará con : " + objEmpleadoAsignar.getCodigo());
								
								mensaje = "ERROR";	
								mensaje = tareasBDelegate.transferirTarea(objEmpleado.getCodigo(),	objEmpleadoAsignar.getCodigo(), objExpedienteTCWPSWeb.getTaskID());
								
								if (mensaje.equals("SUCCESS")) 
								{
											
										reasignado = true;
										
										objExpedienteTCWrapper = new ExpedienteTCWrapper(objExpedienteTCWPSWeb,
												null, tareaBean, bbvaFacade,
												expedienteBean, tipoClienteBean, ansBean);

													objExpedienteTCWrapper.setIdPerfilUsuarioActual(String.valueOf(objEmpleadoAsignar.getPerfil().getId()));
													objExpedienteTCWrapper.setPerfilUsuarioActual(objEmpleadoAsignar.getPerfil().getDescripcion());
													objExpedienteTCWrapper.setCodigoUsuarioActual(objEmpleadoAsignar.getCodigo());
													
													objExpedienteTCWPSWeb.setNombreUsuarioActual(objEmpleadoAsignar.getNombresCompletos());
													objExpedienteTCWPSWeb.setIdPerfilUsuarioActual(String.valueOf(objEmpleadoAsignar.getPerfil().getId()));
													objExpedienteTCWPSWeb.setPerfilUsuarioActual(objEmpleadoAsignar.getPerfil().getDescripcion());
													objExpedienteTCWPSWeb.setCodigoUsuarioActual(objEmpleadoAsignar.getCodigo());
													
													tareasBDelegate.enviarExpedienteTC(objExpedienteTCWrapper.getTaskID(), objExpedienteTCWrapper.getExpedienteTC());

													Expediente objExpediente = expedienteBean.buscarPorId(Long.valueOf(objExpedienteTCWPSWeb.getCodigo()));
													if(objExpediente != null)
													{
													objExpediente.setEmpleado(new Empleado());
													objExpediente.getEmpleado().setId(objEmpleadoAsignar.getId());
													expedienteBean.edit(objExpediente);										
													}										
									
		
								}	
							
							}
						}
						
						if(!reasignado && cambioOficinaPerfilEstado)
						{
							cambioOficinaPerfilEstado = false;
						}
						
					}	
					
					if(!cambioOficinaPerfilEstado)
					{
						
						if(objEmpleado.getOficinaAnterior() != null)
						{
							objEmpleado.setOficina(objEmpleado.getOficinaAnterior());
						}
						if(objEmpleado.getPerfilAnterior() != null)
						{
							objEmpleado.setPerfil(objEmpleado.getPerfilAnterior());
						}
						objEmpleado.setFlagActivo("1");
						
						empleadoBeanLocal.edit(objEmpleado);
						
						List<CartEmpleadoCE> listaCarterizacion = cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(objEmpleado.getId());
						
						for(CartEmpleadoCE objCartEmpleadoCE : listaCarterizacion)
						{
							objCartEmpleadoCE.setOficina(objEmpleado.getOficina());
							cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);						
						}
											
					}
				}

			}
				
			objLogJobDet.setFechaFin(new Date());
			logJobDetBeanLocal.edit(objLogJobDet);
			
			objLogJob.setFechaFin(new Date());
			logJobBeanLocal.edit(objLogJob);
			
			response.getWriter().println("OK");
			response.getWriter().flush();        
	    }
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
		LOG.info("CargaLdapServlet: FIN PROCESO DE CARGA DE EMPLEADOS");
	}

}

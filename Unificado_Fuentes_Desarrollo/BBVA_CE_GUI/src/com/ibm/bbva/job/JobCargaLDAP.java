//package com.ibm.bbva.job;
//
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//import pe.ibm.bean.Consulta;
//import pe.ibm.bean.ExpedienteTCWPSWeb;
//import pe.ibm.bpd.RemoteUtils;
//
//
//import bbva.ws.api.view.BBVAFacadeLocal;
//
//import com.ibm.bbva.controller.Constantes;
//import com.ibm.bbva.entities.CartEmpleadoCE;
//import com.ibm.bbva.entities.Empleado;
//import com.ibm.bbva.entities.Expediente;
//import com.ibm.bbva.entities.LdapTemp;
//import com.ibm.bbva.entities.LogJob;
//import com.ibm.bbva.entities.LogJobDet;
//import com.ibm.bbva.entities.Oficina;
//import com.ibm.bbva.entities.Perfil;
//import com.ibm.bbva.entities.VistaBandjCartProd;
//import com.ibm.bbva.proxy.Usuario;
//import com.ibm.bbva.proxy.WSLDAPServiceImplPortProxy;
//import com.ibm.bbva.proxy.WSLdapException_Exception;
//import com.ibm.bbva.session.AnsBeanLocal;
//import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;
//import com.ibm.bbva.session.EmpleadoBeanLocal;
//import com.ibm.bbva.session.ExpedienteBeanLocal;
//import com.ibm.bbva.session.LdapTempBeanLocal;
//import com.ibm.bbva.session.LogJobBeanLocal;
//import com.ibm.bbva.session.LogJobDetBeanLocal;
//import com.ibm.bbva.session.MutitablaBeanLocal;
//import com.ibm.bbva.session.OficinaBeanLocal;
//import com.ibm.bbva.session.ParametrosConfBeanLocal;
//import com.ibm.bbva.session.TareaBeanLocal;
//import com.ibm.bbva.session.TipoClienteBeanLocal;
//import com.ibm.bbva.session.VistaBandjCartProdBeanLocal;
//import com.ibm.bbva.util.ExpedienteTCWrapper;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.ejb.EJB;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//
//public class JobCargaLDAP implements Job{
//	
//	private ParametrosConfBeanLocal parametrosConfBeanLocal;
//	private LdapTempBeanLocal ldapTempBeanLocal;
//	private EmpleadoBeanLocal empleadoBeanLocal;
//	private WSLDAPServiceImplPortProxy objWSLDAPServiceImplPortProxy;
//	private VistaBandjCartProdBeanLocal vistaBandjCartProdBeanLocal;	
//	private ExpedienteBeanLocal expedienteBean;	
//	private TareaBeanLocal tareaBean;	
//	private BBVAFacadeLocal bbvaFacade;
//	private TipoClienteBeanLocal tipoClienteBean;
//	private AnsBeanLocal ansBean;
//	private CartEmpleadoCEBeanLocal cartEmpleadoCEBeanLocal;
//	private MutitablaBeanLocal multitablaBeanLocal;
//	private LogJobBeanLocal logJobBeanLocal;
//	private LogJobDetBeanLocal logJobDetBeanLocal;
//	
//	public void execute(JobExecutionContext ctx) throws JobExecutionException 
//	{
//
//		try 
//		{
//						
//			parametrosConfBeanLocal = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
//			ldapTempBeanLocal = (LdapTempBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.LdapTempBeanLocal");
//			empleadoBeanLocal = (EmpleadoBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.EmpleadoBeanLocal");
//			vistaBandjCartProdBeanLocal = (VistaBandjCartProdBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.VistaBandjCartProdBeanLocal");
//			expedienteBean = (ExpedienteBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ExpedienteBeanLocal");
//			tareaBean = (TareaBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.TareaBeanLocal");
//			bbvaFacade = (BBVAFacadeLocal) new InitialContext().lookup("ejblocal:bbva.ws.api.view.BBVAFacadeLocal");
//			tipoClienteBean = (TipoClienteBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.TipoClienteBeanLocal");
//			ansBean = (AnsBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.AnsBeanLocal");
//			cartEmpleadoCEBeanLocal = (CartEmpleadoCEBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.CartEmpleadoCEBeanLocal");
//			multitablaBeanLocal = (MutitablaBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.MutitablaBeanLocal");
//			logJobBeanLocal = (LogJobBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.LogJobBeanLocal");
//			logJobDetBeanLocal = (LogJobDetBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.LogJobDetBeanLocal");
//			
//			if(parametrosConfBeanLocal.buscarPorVariable(Constantes.CODIGO_APLICATIVO_PROCESO_LDAP, Constantes.JOB_CARGA_LDAP_HABILITADO).equals("N"))
//			{				
//				return;
//			}
//			
//			LogJob objLogJob = new LogJob();
//			objLogJob.setNombre("JOB - CARGA LDAP");
//			objLogJob.setFechaInicio(new Date());
//			objLogJob = logJobBeanLocal.create(objLogJob);
//			
//			LogJobDet objLogJobDet = null;
//			
//			objWSLDAPServiceImplPortProxy = new WSLDAPServiceImplPortProxy();
//			objWSLDAPServiceImplPortProxy._getDescriptor().setEndpoint(parametrosConfBeanLocal.buscarPorVariable(Constantes.CODIGO_APLICATIVO_PROCESO_LDAP, Constantes.LDAP_WEB_SERVICE_ENDPOINT).getValorVariable());
//				
//			objLogJobDet = new LogJobDet();
//			objLogJobDet.setNombre("Carga LDAP temporal");
//			objLogJobDet.setFechaInicio(new Date());
//			objLogJobDet.setLogJob(objLogJob);
//			objLogJobDet = logJobDetBeanLocal.create(objLogJobDet);
//			
//			try 
//			{ 				 
//				ldapTempBeanLocal.clean();
//				List<Usuario> listaUsuarios = objWSLDAPServiceImplPortProxy.obtenerUsuarios(null);
//				LdapTemp objLdapTemp = null;
//				for(Usuario objUsuario : listaUsuarios)
//				{					
//					objLdapTemp = new LdapTemp();					
//					objLdapTemp.setId(objUsuario.getUsuario());
//					objLdapTemp.setNombre(objUsuario.getNombres());
//					objLdapTemp.setApellidoPaterno(objUsuario.getPrimerApellido());
//					objLdapTemp.setApellidoMaterno(objUsuario.getSegundoApellido());
//					objLdapTemp.setCorreoElectronico(objUsuario.getEMail());
//					objLdapTemp.setCodigoCargo(objUsuario.getPuesto() != null ? objUsuario.getPuesto().getNombreCargoFuncionalLocal() : null);
//					objLdapTemp.setCodigoOficina(objUsuario.getCodigoCentro());								
//					ldapTempBeanLocal.create(objLdapTemp);										
//				}						
//			} 
//			catch (WSLdapException_Exception e) 
//			{
//				e.printStackTrace();
//			}
//			
//			
//			objLogJobDet.setFechaFin(new Date());
//			logJobDetBeanLocal.edit(objLogJobDet);
//			
//			empleadoBeanLocal.ejecutarDescargaLDAP(objLogJob.getId());
//			
//			/**
//			 ******* REASIGNACIÓN ********			 
//			 */
//			
//			objLogJobDet = new LogJobDet();
//			objLogJobDet.setNombre("Reasignacion");
//			objLogJobDet.setFechaInicio(new Date());
//			objLogJobDet.setLogJob(objLogJob);
//			objLogJobDet = logJobDetBeanLocal.create(objLogJobDet);
//			
//			List<Empleado> listaEmpleado = empleadoBeanLocal.buscarDebenReasignarse();
//			RemoteUtils tareasBDelegate = new RemoteUtils();
//			boolean reasignado = false; 
//			String mensaje = "ERROR";
//			boolean cambioOficinaCargo = true;
//			
//			Consulta consulta = null;			
//			List<ExpedienteTCWPSWeb> listaReasignable = null;
//			List<Empleado> listaEmpleadoParaAsignar = null;
//			List<VistaBandjCartProd> listCartProducto = null;
//			List<Long> listIdsProd = null;
//			ExpedienteTCWrapper objExpedienteTCWrapper = null;
//			
//			for(Empleado objEmpleado : listaEmpleado)
//			{
//				cambioOficinaCargo = true;
//				
//				consulta = new Consulta();
//				consulta.setTipoConsulta(4);
//				consulta.setCodUsuarioActual(objEmpleado.getCodigo());
//				consulta.setIdPerfilUsuarioActual(objEmpleado.getPerfil().getCodigo());
//				consulta.setIdOficina(String.valueOf(objEmpleado.getOficinaAnterior().getId()));
//				listaReasignable = tareasBDelegate.listarTareasBandejaAsignacion(consulta);
//							
//				listCartProducto = vistaBandjCartProdBeanLocal.verificarCartXProducto(objEmpleado.getPerfil().getId(), 
//																					  objEmpleado.getOficina().getTerritorio().getId(), 
//																					  objEmpleado.getId());
//				
//				listIdsProd = new ArrayList<Long>();
//				
//				if(listCartProducto != null && listCartProducto.size() > 0)
//				{
//					for(VistaBandjCartProd obj : listCartProducto){
//						if(obj != null && obj.getIdProduto() > 0){
//							listIdsProd.add(new Long(obj.getIdProduto()));
//						}
//					}
//				}
//
//				listaEmpleadoParaAsignar = empleadoBeanLocal.buscarPorPerfilEmpleadoActivo(objEmpleado.getPerfil().getId(), objEmpleado.getOficinaAnterior().getId(), listIdsProd);
//				
//				for(ExpedienteTCWPSWeb objExpedienteTCWPSWeb : listaReasignable)
//				{
//
//					reasignado = false;
//					for(Empleado objEmpleadoAsignar : listaEmpleadoParaAsignar)
//					{
//
//						if(reasignado) { break; }
//						if(objEmpleadoAsignar.getId() != objEmpleado.getId())
//						{
//							mensaje = "ERROR";	
//							mensaje = tareasBDelegate.transferirTarea(objEmpleado.getCodigo(),	objEmpleadoAsignar.getCodigo(), objExpedienteTCWPSWeb.getTaskID());
//							
//							if (mensaje.equals("SUCCESS")) 
//							{
//								reasignado = true;
//								
//								objExpedienteTCWrapper = new ExpedienteTCWrapper(objExpedienteTCWPSWeb,
//																						null, tareaBean, bbvaFacade,
//																						expedienteBean, tipoClienteBean, ansBean);
//								
//								objExpedienteTCWrapper.setIdPerfilUsuarioActual(String.valueOf(objEmpleadoAsignar.getPerfil().getId()));
//								objExpedienteTCWrapper.setPerfilUsuarioActual(objEmpleadoAsignar.getPerfil().getDescripcion());
//								objExpedienteTCWrapper.setCodigoUsuarioActual(objEmpleadoAsignar.getCodigo());
//								
//								objExpedienteTCWPSWeb.setNombreUsuarioActual(objEmpleadoAsignar.getNombresCompletos());
//								objExpedienteTCWPSWeb.setIdPerfilUsuarioActual(String.valueOf(objEmpleadoAsignar.getPerfil().getId()));
//								objExpedienteTCWPSWeb.setPerfilUsuarioActual(objEmpleadoAsignar.getPerfil().getDescripcion());
//								objExpedienteTCWPSWeb.setCodigoUsuarioActual(objEmpleadoAsignar.getCodigo());
//	
//								tareasBDelegate.enviarExpedienteTC(objExpedienteTCWrapper.getTaskID(), objExpedienteTCWrapper.getExpedienteTC());
//								
//								Expediente objExpediente = expedienteBean.buscarPorId(Long.valueOf(objExpedienteTCWPSWeb.getCodigo()));
//								if(objExpediente != null)
//								{
//									objExpediente.setEmpleado(new Empleado());
//									objExpediente.getEmpleado().setId(objEmpleadoAsignar.getId());
//									expedienteBean.edit(objExpediente);										
//								}	
//	
//							}	
//														
//						}
//					}
//					
//					if(!reasignado)
//					{
//						cambioOficinaCargo = false;
//					}
//					
//				}	
//				
//								
//				if(!cambioOficinaCargo)
//				{
//					
//					if(objEmpleado.getOficinaAnterior() != null)
//					{
//						objEmpleado.setOficina(objEmpleado.getOficinaAnterior());
//					}
//					if(objEmpleado.getCodigoCargoAnterior() != null)
//					{
//						objEmpleado.setCodigoCargo(objEmpleado.getCodigoCargoAnterior());
//					}
//					
//					empleadoBeanLocal.edit(objEmpleado);
//					
//					List<CartEmpleadoCE> listaCarterizacion = cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(objEmpleado.getId());
//					
//					for(CartEmpleadoCE objCartEmpleadoCE : listaCarterizacion)
//					{
//						objCartEmpleadoCE.setOficina(objEmpleado.getOficina());
//						cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);						
//					}
//										
//				}
//			}
//				
//			objLogJobDet.setFechaFin(new Date());
//			logJobDetBeanLocal.edit(objLogJobDet);
//			
//			objLogJob.setFechaFin(new Date());
//			logJobBeanLocal.edit(objLogJob);
//			
//		} catch (NamingException e) {
//			e.printStackTrace();
//		} 
//	
//	}
//	
//}

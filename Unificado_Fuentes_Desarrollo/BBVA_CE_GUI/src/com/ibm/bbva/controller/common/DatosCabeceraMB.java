package com.ibm.bbva.controller.common;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bpd.RemoteUtils;

import bbva.ws.api.view.BBVAFacadeLocal;

import com.grupobbva.bc.per.tele.seguridad.ServiciosSeguridadBbva;
import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.DescargaLDAP;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.proxy.ext.Perfil;
import com.ibm.bbva.proxy.ext.PerfilesUsuario;
import com.ibm.bbva.proxy.ext.Usuario;
import com.ibm.bbva.proxy.ext.UsuarioExtendido;
import com.ibm.bbva.proxy.ext.WSLDAPServiceExtImplPortProxy;
import com.ibm.bbva.proxy.ext.WSLdapException_Exception;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;
import com.ibm.bbva.session.DescargaLDAPBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.OficinaTemporalBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import com.ibm.bbva.util.AyudaCargaLdap;
import com.ibm.bbva.util.ExpedienteTCWrapper;
import com.ibm.bbva.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SuppressWarnings("serial")
@ManagedBean(name = "datosCabecera")
@SessionScoped
public class DatosCabeceraMB extends AbstractMBean {
	
	@EJB
	private EmpleadoBeanLocal empleadobean;	
	
	@EJB
	private CartEmpleadoCEBeanLocal cartEmpleadoCEBeanLocal;
	
	private Empleado empleado;
    
	@EJB
	private OficinaTemporalBeanLocal oficinaTemporalBeanLocal;
	
	@EJB
	private TareaBeanLocal tareaBean;
	
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	
	@EJB
	private TipoClienteBeanLocal tipoClienteBean;
	
	@EJB
	private AnsBeanLocal ansBean;
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosCabeceraMB.class);
	
	private String mensajeAdvertencia;
	private String etiquetaTemporal;
	
	private String etiquetaTemporalPerfil;
	
	private WSLDAPServiceExtImplPortProxy proxyIDM; 
	
	private boolean flagAccesoIDM;//validar acceso en IDM
	private UsuarioExtendido usuarioIDM;
	
	@EJB
	private PerfilBeanLocal perfilBean;
	
	@EJB
	private OficinaBeanLocal oficinabean;
	
	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;
	
	@EJB
	private DescargaLDAPBeanLocal descargaLDAPBeanLocal;
	
	private AyudaCargaLdap ayudaCargaLdap;
			
    public DatosCabeceraMB() {
	}
    
    @PostConstruct
    public void init() {
    	       
    	String user = "";
	    LOG.info("Usuario : {}",user);
	    ServiciosSeguridadBbva ssBbva = new ServiciosSeguridadBbva((HttpServletRequest) getExternalContext().getRequest());
		if(ssBbva != null) {
			ssBbva.obtener_ID();
			user  =  ssBbva.getUsuario().toUpperCase(); 
			LOG.info("Usuario : {}",user);
		}else{
			LOG.warn("No se encontro el Usurio en el request : {}",user);
		}
		
		
    	flagAccesoIDM = false;
    	    	
    	proxyIDM = new WSLDAPServiceExtImplPortProxy();
    	proxyIDM._getDescriptor().setEndpoint(parametrosConfBean.buscarPorVariable(Constantes.CODIGO_APLICATIVO_PROCESO_LDAP, Constantes.LDAP_WEB_SERVICE_EXT_ENDPOINT).getValorVariable());
    	List<String> listUsuario = new ArrayList<String>();
    	listUsuario.add(user);
    	List<UsuarioExtendido> lista = null;
    	try {
			lista = proxyIDM.obtenerUsuarios(listUsuario);
			List<com.ibm.bbva.entities.Perfil> listaPerfilesCS = perfilBean.buscarTodos();
			for(Object usuarioExt : lista){
				Usuario usuarioExtendido = (Usuario)usuarioExt;
				PerfilesUsuario perfilesUsuario= usuarioExtendido.getPerfiles();
				List<Perfil> perfilesIDM = perfilesUsuario.getPerfil();
				for(Perfil perfilIDM : perfilesIDM){
					for(com.ibm.bbva.entities.Perfil perfilCS : listaPerfilesCS ){
						if(perfilIDM.getNombre().equals(perfilCS.getCodigoIDM())){
							flagAccesoIDM = true;
							break;
						}
					}
				}
			}
		} catch (WSLdapException_Exception e1) {
			e1.printStackTrace();
		}
    	//FIN DE VALIDACION DE ACCESO A IDM
    	
    	FacesContext ctx = FacesContext.getCurrentInstance();
		MenuMB menu = (MenuMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "menu");
    	
		/**
		 * Si se realiza cambio en validacion de los booleanos flagTienePuestoTemporal y flagTieneOficinaTemporal
		 * se debe considerar los mismos cambios para la clase CargaLdapServlet
		 * para mantener la igualdad
		*/
		if(flagAccesoIDM){
			this.empleado = empleadobean.buscarPorCodigo(user);  
			//validar si tiene temporalidad
			usuarioIDM = lista.get(0);
			
			if(this.empleado != null){
				
				if (Constantes.FLAG_INACTIVO.equals(this.empleado.getFlagActivo()) && (!this.empleado.getPerfil().getCodigo().equals(Constantes.ID_PERFIL_SUB_GERENTE.toString()))) {
					LOG.info("El empleado {} esta inactivo", empleado.getCodigo());
					/*HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
					try {
						response.sendRedirect("ibm_security_logout?logoutExitPage=/index.faces");
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}*/
					try{
						menu.timeOutWAS();
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}else{
					
					ayudaCargaLdap = new AyudaCargaLdap();
					boolean flagTienePuestoTemporal = ayudaCargaLdap.validarTemporalidadPuestoIDM(usuarioIDM);
					
					/*LOG.info("FECHA INICIO PUESTO TEMPORAL DE IDM  "+ (usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaInicio().toGregorianCalendar().getTime().toString():"0"));
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
													  usuarioIDM.getPuestoTemporal().getDescripcionPuesto()):false;*/
					//String codigoPuesto = usuarioIDM.getPuesto().getDescripcionPuesto();
					String codigoPuesto = "";								  
					com.ibm.bbva.entities.Perfil perfilTemporal = empleado.getPerfil();
						
					if(flagTienePuestoTemporal){
						codigoPuesto = usuarioIDM.getPuestoTemporal().getDescripcionPuesto();
						List<DescargaLDAP> listaPerfiles = descargaLDAPBeanLocal.buscar("-1", codigoPuesto, "-1", "-1", "-1", "-1");
						for(DescargaLDAP descargaLdap : listaPerfiles){
							if(!empleado.getPerfil().getCodigo().equals(descargaLdap.getPerfil().getCodigo())){
								perfilTemporal = descargaLdap.getPerfil();
							}else{
								perfilTemporal = empleado.getPerfil();
							}
						}
					}
					
					boolean flagTieneOficinaTemporal =ayudaCargaLdap.validarTemporalidadOficinaIDM(usuarioIDM);
					
					/*LOG.info("FECHA INICIO OFICINA TEMPORAL DE IDM  "+ (usuarioIDM.getCentroTemporal()!=null? usuarioIDM.getCentroTemporal().getFechaInicio().toGregorianCalendar().getTime().toString():"0"));
					Date ofiTempFechaInicioIDM = usuarioIDM.getCentroTemporal()!=null? usuarioIDM.getCentroTemporal().getFechaInicio().toGregorianCalendar().getTime():null;
					LOG.info("FECHA INICIO OFICINA TEMPORAL DE IDM CON FORMATO "+ofiTempFechaInicioIDM);
					
					LOG.info("FECHA FIN OFICINA TEMPORAL DE IDM  "+ (usuarioIDM.getCentroTemporal()!=null? usuarioIDM.getCentroTemporal().getFechaFin().toGregorianCalendar().getTime().toString():"0"));
					Date ofiTempFechaFinIDM = usuarioIDM.getCentroTemporal()!=null? usuarioIDM.getCentroTemporal().getFechaFin().toGregorianCalendar().getTime():null;
					LOG.info("FECHA FIN OFICINA TEMPORAL DE IDM CON FORMATO "+ofiTempFechaFinIDM);
					
					boolean flagTieneOficinaTemporal = (usuarioIDM.getCentroTemporal()!=null &&  
							(ofiTempFechaInicioIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss"),"dd/MM/yyyy HH:mm:ss"))) &&
							(!ofiTempFechaFinIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss"),"dd/MM/yyyy HH:mm:ss")))
							&& (!usuarioIDM.getCentroTemporal().getDescripcion().equals(usuarioIDM.getCodigoCentro())))?
													   StringUtils.isNotBlank(
													   usuarioIDM.getCentroTemporal().getDescripcion()):false;*/
													   
					
					Oficina objOficinaTemporal = null;
					
					if(flagTieneOficinaTemporal){
						String codigoOficina = usuarioIDM.getCentroTemporal().getDescripcion();
						objOficinaTemporal = oficinabean.buscarPorCodigo(codigoOficina);
					}
			    	
					if(!(flagTieneOficinaTemporal))//if(objOficinaTemporal == null)
					{
						/*validación para el caso en que el usuario tenia configurada una oficina
						temporal y esta ya caduco se verifica que el usuario no tenga expedientes pendientes, en caso no tenga
						se procede a restaurar el valor original de su oficina*/
						if(this.empleado.getOficinaBackup() != null)
						{
							
							//si es un sub gerente que se va a una agencia como sg temporal, entonces:
							//Perfil que tiene registrado el empleado es Sub Gerente
							if(this.empleado.getPerfil().getCodigo().equals(Constantes.ID_PERFIL_SUB_GERENTE.toString()) ){
									
										//Buscar Sub Gerentes activos y de la oficina dada
										List<Empleado> subGerentesActivos = empleadobean.buscarGerenteActivoPorOficinaPerfil(this.empleado.getOficina().getId(), 
												Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
										
										//Buscar SG inactivos por Oficina, Perfil; Estado y Marca
										List<Empleado> subGerentesActivosMarcados = empleadobean.buscarGerenteActivoPorOficinaPerfilMarca(this.empleado.getOficina().getId(), 
												Constantes.ID_PERFIL_SUB_GERENTE, Constantes.FLAG_ACTIVO);
										
										if(subGerentesActivosMarcados != null && subGerentesActivosMarcados.size() >0){
											int contador=0;
											for(Empleado subGerenteInactivoMarcado : subGerentesActivosMarcados)
											{
												if(contador == 0){
													RemoteUtils remoteUtils = new RemoteUtils();
													long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
													if(cantexp > 0){
														ayudaCargaLdap.reasignarExpedientes(this.empleado, subGerenteInactivoMarcado);
														
													}
													contador ++;
													
												}
												
												//subGerenteInactivoMarcado.setFlagActivo(Constantes.FLAG_ACTIVO);
												subGerenteInactivoMarcado.setFlagEmpleadoSustituido(Constantes.FLAG_INACTIVO);
												this.empleadobean.edit(subGerenteInactivoMarcado);
											}
											ayudaCargaLdap.actualizarDatosOficinaEmpleado(this.empleado);
											
										}else if(subGerentesActivos != null && subGerentesActivos.size()>0){
											
											ayudaCargaLdap.reasignarExpedientes(this.empleado, subGerentesActivos.get(0));
											ayudaCargaLdap.actualizarDatosOficinaEmpleado(this.empleado);
										}else{
											//Mensaje Error
											this.setMensajeAdvertencia("No puedes culminar tu temporalidad debido " +
													"a que no existe un Subgerente activo para la oficina (" + 
													this.empleado.getOficina().getCodigo() + "-" + this.empleado.getOficina().getDescripcion()
													+"), por favor comuníquese con el Administrador");
											flagTieneOficinaTemporal = true;
											
										}
								
							}else{
								//si no :
								RemoteUtils remoteUtils = new RemoteUtils();
								long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
								if(cantexp > 0)
								{
									this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes para la oficina " + this.empleado.getOficina().getCodigo() + " - " + this.empleado.getOficina().getDescripcion() + " temporal configurada");
									flagTieneOficinaTemporal = true;
								}
								else
								{	
									ayudaCargaLdap.actualizarDatosOficinaEmpleado(this.empleado);
								}
							}
								
						}else{
							//Perfil usuario logedo es Sub Gerente
							if(this.empleado.getPerfil().getCodigo().equals(Constantes.ID_PERFIL_SUB_GERENTE.toString()) ){
								
								//validar si ese Sub Gerente logeado no es activo
								//if(this.empleado.getFlagActivo().equals(Constantes.FLAG_INACTIVO)){
									List<Empleado> subGTporPerfil = empleadobean.buscarSubGerenteTemporalPorOficinaPerfil(this.empleado.getOficina().getId(), 
										Constantes.ID_PERFIL_SUB_GERENTE);
								
									//Existen SGT vigentes en el IDM, que estan reemplazando al SubGerente?
									List<Empleado> subGerentesTemporales = empleadobean.buscarSubGerenteTemporalPorOficinaYOfiTemp(this.empleado.getOficina().getId(), 
											Constantes.ID_PERFIL_SUB_GERENTE);
									
									if(subGerentesTemporales != null && subGerentesTemporales.size()>0){
										boolean flagTieneOficinaTemporalSG = false;
										Empleado sgTemp = null;
										
										for(Empleado subGerenteTemporal : subGerentesTemporales){
											//Consultar IDM si ese subGerenteTemporal sigue vigente
											UsuarioExtendido usuarioIDMTemporal = null;
											List<String> listUsuario2 = new ArrayList<String>();
											//listUsuario = null;
											boolean flagAccesoIDMTemporalOficina = false;
									    	listUsuario2.add(subGerenteTemporal.getCodigo());
									    	lista = null;
											try {
												lista = proxyIDM.obtenerUsuarios(listUsuario2);
												if(lista != null && lista.size()>0){
													flagAccesoIDMTemporalOficina = true;
												}
												
											} catch (WSLdapException_Exception e1) {
												e1.printStackTrace();
											}
											
											if(flagAccesoIDMTemporalOficina){
												usuarioIDMTemporal = lista.get(0);
												
												Date oficinaTempFechaInicioIDMTemp = usuarioIDMTemporal.getCentroTemporal()!=null? usuarioIDMTemporal.getCentroTemporal().getFechaInicio().toGregorianCalendar().getTime():null;
												Date oficinaTempFechaFinIDMTemp = usuarioIDMTemporal.getCentroTemporal()!=null? usuarioIDMTemporal.getCentroTemporal().getFechaFin().toGregorianCalendar().getTime():null;
															
												flagTieneOficinaTemporalSG = (usuarioIDMTemporal.getCentroTemporal()!=null && 
														(oficinaTempFechaInicioIDMTemp.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss:SSS"),"dd/MM/yyyy HH:mm:ss:SSS"))) &&
														(!oficinaTempFechaFinIDMTemp.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss:SSS"),"dd/MM/yyyy HH:mm:ss:SSS")))
														&& (!usuarioIDMTemporal.getCentroTemporal().getDescripcion().equals(usuarioIDMTemporal.getCodigoCentro())))?
																				  StringUtils.isNotBlank(
																				  usuarioIDMTemporal.getCentroTemporal().getDescripcion()):false;
																
												if(flagTieneOficinaTemporalSG){
													sgTemp = subGerenteTemporal;
													break;
												}
											}	
											
										}
										
										if(flagTieneOficinaTemporalSG){
											//Mostrar mensaje de error
											this.setMensajeAdvertencia("Actualmente existe Subgerente Temporal vigente (" + sgTemp.getCodigo() + " - " + 
														sgTemp.getNombresCompletos() + ") para la oficina (" + sgTemp.getOficina().getCodigo() + " - " + sgTemp.getOficina().getDescripcion() +
														"), por favor verifique el periodo de temporalidad en IDM, luego vuelva a validar o comuníquese con el Administrador");
											 
											
										}else{
											if(subGTporPerfil == null || subGTporPerfil.size()==0){
												//activar al subGerente que en este caso se esta logeando
												if(this.empleado.getFlagEmpleadoSustituido().equals(Constantes.FLAG_ACTIVO)){
													//this.empleado.setFlagActivo(Constantes.FLAG_ACTIVO);
													this.empleado.setFlagEmpleadoSustituido(Constantes.FLAG_INACTIVO);
													this.empleadobean.edit(this.empleado);
													
													//Si tiene Exp. Pendientes se debe reasignar al SG activo
													if(subGerentesTemporales != null && subGerentesTemporales.size()>0){
																						
														
														for(Empleado subGerenteTemporal : subGerentesTemporales){
															ayudaCargaLdap.reasignarExpedientes(subGerenteTemporal, this.empleado);
															
															//Se atualiza Oficina 
															subGerenteTemporal.setOficina(subGerenteTemporal.getOficinaBackup());
															subGerenteTemporal.setOficinaBackup(null);
															this.empleadobean.edit(subGerenteTemporal);
															
															List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(subGerenteTemporal.getId());
															for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
															{
																objCartEmpleadoCE.setOficina(subGerenteTemporal.getOficina());
																this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
															}
														}
														
													}
												}
											}
												
										}
									}else{
										//Si no existe ninguna temporalidad para esta oficina y el que se logeo es un sub gerente
										if(subGTporPerfil == null || subGTporPerfil.size()==0){
											//Buscar Sub Gerentes activos y de la oficina dada
											List<Empleado> otrosSubGerentesActivos = empleadobean.buscarGerenteActivoPorOficinaPerfilSinSGT(this.empleado.getOficina().getId(), 
													Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
											
											if(otrosSubGerentesActivos != null && otrosSubGerentesActivos.size()>0){
												
												for(Empleado otroSubGerenteActivo : otrosSubGerentesActivos){
													ayudaCargaLdap.reasignarExpedientes(otroSubGerenteActivo, this.empleado);
												}
												
											}
										}
									}
										
								//}
							}
						}
						
					}
					else
					{
						//En caso el usuario no tenga configurada una oficina temporal
						if(this.empleado.getOficinaBackup() == null)
						{
							RemoteUtils remoteUtils = new RemoteUtils();
							long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
							if(cantexp > 0)
							{
								this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes, no se le puede establecer la oficina temporal configurada");
								flagTieneOficinaTemporal = false;
							}
							else
							{
								//si no :
								this.empleado.setOficinaBackup(this.empleado.getOficina());
								this.empleado.setOficina(objOficinaTemporal);	//this.empleado.setOficina(objOficinaTemporal.getOficina());				
								this.empleadobean.edit(this.empleado);
								
								List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
								for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
								{
									objCartEmpleadoCE.setOficina(this.empleado.getOficina());
									this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
								}
								
								//si es un sub gerente que se va a una agencia como sg temporal, entonces:
								//Perfil del empleado es Sub Gerente
								if(this.empleado.getPerfil().getCodigo().equals(Constantes.ID_PERFIL_SUB_GERENTE.toString()) ){
									//Buscar Sub Gerentes activos y de la oficina dada
									List<Empleado> subGerentesActivos = empleadobean.buscarGerenteActivoPorOficinaPerfil(this.empleado.getOficina().getId(), 
											Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
									
									if(subGerentesActivos != null && subGerentesActivos.size()>0){
										//Si existen Sub Gerentes activos y ademas tienen Exp. pendientes
										
										for(Empleado subGerenteActivo : subGerentesActivos)
										{
											long cantexpSGA = remoteUtils.countConsultaListaTareasTC(subGerenteActivo.getCodigo());					
											if(cantexpSGA > 0){
												ayudaCargaLdap.reasignarExpedientes(subGerenteActivo, this.empleado);
													
											}
											
											//Inactivas al Sub Gerente y marcarlo como Sustituido
											//subGerenteActivo.setFlagActivo(Constantes.FLAG_INACTIVO);
											subGerenteActivo.setFlagEmpleadoSustituido(Constantes.FLAG_ACTIVO);
											this.empleadobean.edit(subGerenteActivo);
										}
									}
								}
								
							}							
						}
						else
						{
							if(objOficinaTemporal!=null && this.empleado.getOficina().getId() != objOficinaTemporal.getId())
							{
								RemoteUtils remoteUtils = new RemoteUtils();
								long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
								if(cantexp > 0)
								{
									this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes para la oficina temporal establecida");
									flagTieneOficinaTemporal = true;
								}
								else
								{
									
									this.empleado.setOficina(objOficinaTemporal);
									this.empleadobean.edit(this.empleado);
									
									List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
									for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
									{
										objCartEmpleadoCE.setOficina(this.empleado.getOficina());
										this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
									}
									
									//si es un sub gerente que se va a una agencia como sg temporal, entonces:
									if(this.empleado.getCodigo().equals(Constantes.ID_PERFIL_SUB_GERENTE.toString()) ){
										//Buscar Sub Gerentes activos y de la oficina dada
										List<Empleado> subGerentesActivos = empleadobean.buscarGerenteActivoPorOficinaPerfil(objOficinaTemporal.getId(), 
												Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
										
										if(subGerentesActivos != null && subGerentesActivos.size()>0){
											//Si existen Sub Gerentes activos y ademas tienen Exp. pendientes
											for(Empleado subGerenteActivo : subGerentesActivos)
											{
												long cantexpSGA = remoteUtils.countConsultaListaTareasTC(subGerenteActivo.getCodigo());					
												if(cantexpSGA > 0){
													ayudaCargaLdap.reasignarExpedientes(subGerenteActivo, this.empleado);
														
												}
												
												//Inactivas al Sub Gerente
												//subGerenteActivo.setFlagActivo(Constantes.FLAG_INACTIVO);
												subGerenteActivo.setFlagEmpleadoSustituido(Constantes.FLAG_ACTIVO);
												this.empleadobean.edit(subGerenteActivo);
											}
										}
									}
									
								}
							}else{
								//Cuando existe temporalidad y el que se logea es el SGT
								//Esas acciones se ejecutaran cada ves que se logea un SGT con temporalidad vigente
								//Buscar Sub Gerentes activos y de la oficina dada
								List<Empleado> otrosSGPActivos = empleadobean.buscarGerenteActivoPorOficinaPerfil(objOficinaTemporal.getId(), 
										Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
								
								if(otrosSGPActivos != null && otrosSGPActivos.size()>0){
									//Si existen Sub Gerentes activos y ademas tienen Exp. pendientes
									for(Empleado otroSGPActivo : otrosSGPActivos)
									{
										ayudaCargaLdap.reasignarExpedientes(otroSGPActivo, this.empleado);
									}
								}
								
							}
						}
					}
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
					if(!(flagTienePuestoTemporal))//if(objOficinaTemporal == null)
					{
						/*validación para el caso en que el usuario tenia configurada una oficina
						temporal y esta ya caduco se verifica que el usuario no tenga expedientes pendientes, en caso no tenga
						se procede a restaurar el valor original de su oficina*/
						if(this.empleado.getPerfilBackup() != null)
						{				
							//Perfil temporal que tiene registrado el empleado es Sub Gerente
							if(this.empleado.getPerfil().getCodigo().equals(Constantes.ID_PERFIL_SUB_GERENTE.toString()) ){
									
										//Buscar Sub Gerentes activos y de la oficina dada
										List<Empleado> subGerentesActivos = empleadobean.buscarGerenteActivoPorOficinaPerfil(this.empleado.getOficina().getId(), 
												Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
										
										//Buscar SG inactivos por Oficina, Perfil; Estado y Marca
										List<Empleado> subGerentesInactivosMarcados = empleadobean.buscarGerenteInactivoPorOficinaPerfilMarca(this.empleado.getOficina().getId(), 
												Constantes.ID_PERFIL_SUB_GERENTE, Constantes.FLAG_ACTIVO);
										
										if(subGerentesInactivosMarcados != null && subGerentesInactivosMarcados.size() >0){
											int contador=0;
											for(Empleado subGerenteInactivoMarcado : subGerentesInactivosMarcados)
											{
												if(contador == 0){
													RemoteUtils remoteUtils = new RemoteUtils();
													long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
													if(cantexp > 0){
														ayudaCargaLdap.reasignarExpedientes(this.empleado, subGerenteInactivoMarcado);
														
													}
													contador ++;
													
												}
												
												//subGerenteInactivoMarcado.setFlagActivo(Constantes.FLAG_ACTIVO);
												subGerenteInactivoMarcado.setFlagEmpleadoSustituido(Constantes.FLAG_INACTIVO);
												this.empleadobean.edit(subGerenteInactivoMarcado);
											}
											ayudaCargaLdap.actualizarDatosEmpleado(this.empleado);
											
										}else if(subGerentesActivos != null && subGerentesActivos.size()>0){
											
											ayudaCargaLdap.reasignarExpedientes(this.empleado, subGerentesActivos.get(0));
											ayudaCargaLdap.actualizarDatosEmpleado(this.empleado);
										}else{
											//Mensaje Error
											this.setMensajeAdvertencia("No puedes culminar tu temporalidad debido " +
													"a que no existe un Subgerente activo para la oficina (" + 
													this.empleado.getOficina().getCodigo() + "-" + this.empleado.getOficina().getDescripcion()
													+"), por favor comuníquese con el Administrador");
											flagTienePuestoTemporal = true;
										}
										
										
							}else{
								RemoteUtils remoteUtils = new RemoteUtils();
								long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
								if(cantexp > 0)
								{
									this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes para el perfil " + this.empleado.getPerfil().getCodigo() + " - " + this.empleado.getPerfil().getDescripcion() + " temporal configurada");
									flagTienePuestoTemporal=true;
								}else{
									ayudaCargaLdap.actualizarDatosEmpleado(this.empleado);
								}
							}
						
						}else{
							//Perfil usuario logedo es Sub Gerente
							if(this.empleado.getPerfil().getCodigo().equals(Constantes.ID_PERFIL_SUB_GERENTE.toString()) ){
								
								//validar si ese Sub Gerente logeado no es activo
								//if(this.empleado.getFlagActivo().equals(Constantes.FLAG_INACTIVO)){
									//Existen SGT vigentes en el IDM, que estan reemplazando al SubGerente?
									List<Empleado> subGerentesTemporales = empleadobean.buscarSubGerenteTemporalPorOficinaPerfil(this.empleado.getOficina().getId(), 
											Constantes.ID_PERFIL_SUB_GERENTE);
									
									if(subGerentesTemporales != null && subGerentesTemporales.size()>0){
										boolean flagTienePuestoTemporalSG = false;
										Empleado sgTemp = null;
										
										for(Empleado subGerenteTemporal : subGerentesTemporales){
											//Consultar IDM si ese subGerenteTemporal sigue vigente
											UsuarioExtendido usuarioIDMTemporal = null;
											List<String> listUsuario2 = new ArrayList<String>();
											//listUsuario = null;
											boolean flagAccesoIDMTemporal = false;
									    	listUsuario2.add(subGerenteTemporal.getCodigo());
									    	lista = null;
											try {
												lista = proxyIDM.obtenerUsuarios(listUsuario2);
												if(lista != null && lista.size()>0){
													flagAccesoIDMTemporal = true;
												}
												
											} catch (WSLdapException_Exception e1) {
												e1.printStackTrace();
											}
											
											if(flagAccesoIDMTemporal){
												usuarioIDMTemporal = lista.get(0);
												
												Date puestoTempFechaInicioIDMTemp = usuarioIDMTemporal.getPuestoTemporal()!=null? usuarioIDMTemporal.getPuestoTemporal().getFechaInicio().toGregorianCalendar().getTime():null;
												Date puestoTempFechaFinIDMTemp = usuarioIDMTemporal.getPuestoTemporal()!=null? usuarioIDMTemporal.getPuestoTemporal().getFechaFin().toGregorianCalendar().getTime():null;
															
												flagTienePuestoTemporalSG = (usuarioIDMTemporal.getPuestoTemporal()!=null && 
														(puestoTempFechaInicioIDMTemp.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss:SSS"),"dd/MM/yyyy HH:mm:ss:SSS"))) &&
														(!puestoTempFechaFinIDMTemp.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss:SSS"),"dd/MM/yyyy HH:mm:ss:SSS")))
														&& (!usuarioIDMTemporal.getPuestoTemporal().getDescripcionPuesto().equals(usuarioIDMTemporal.getPuesto().getNombreCargoFuncionalLocal())))?
																				  StringUtils.isNotBlank(
																				  usuarioIDMTemporal.getPuestoTemporal().getDescripcionPuesto()):false;
																
												if(flagTienePuestoTemporalSG){
													sgTemp = subGerenteTemporal;
													break;
												}
											}	
											
										}
										
										if(flagTienePuestoTemporalSG){
											//Mostrar mensaje de error
											this.setMensajeAdvertencia("Actualmente existe Subgerente Temporal vigente (" + sgTemp.getCodigo() + " - " + 
														sgTemp.getNombresCompletos() + ") para la oficina (" + sgTemp.getOficina().getCodigo() + " - " + sgTemp.getOficina().getDescripcion() +
														"), por favor verifique el periodo de temporalidad en IDM, luego vuelva a validar o comuníquese con el Administrador");
											 
											
										}else{
											//activar al subGerente que en este caso se esta logeando
											if(this.empleado.getFlagEmpleadoSustituido().equals(Constantes.FLAG_ACTIVO)){
												//this.empleado.setFlagActivo(Constantes.FLAG_ACTIVO);
												this.empleado.setFlagEmpleadoSustituido(Constantes.FLAG_INACTIVO);
												this.empleadobean.edit(this.empleado);
												
												//Si tiene Exp. Pendientes se debe reasignar al SG activo
												if(subGerentesTemporales != null && subGerentesTemporales.size()>0){
																					
													for(Empleado subGerenteTemporal : subGerentesTemporales){
														ayudaCargaLdap.reasignarExpedientes(subGerenteTemporal, this.empleado);
														
														//Actulizar Perfil, Cargo y Carterizacion del SGT
														subGerenteTemporal.setPerfil(subGerenteTemporal.getPerfilBackup());
														subGerenteTemporal.setPerfilBackup(null);
														subGerenteTemporal.setCodigoCargo(subGerenteTemporal.getCodigoCargoBackup());
														subGerenteTemporal.setCodigoCargoBackup(null);
														this.empleadobean.edit(subGerenteTemporal);
														
														List<CartEmpleadoCE> listaCartEmpleadoSGTempCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(subGerenteTemporal.getId());
														for(CartEmpleadoCE objCartEmpleadoSGTempCE : listaCartEmpleadoSGTempCE)
														{
															objCartEmpleadoSGTempCE.setPerfil(subGerenteTemporal.getPerfil());
															this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoSGTempCE);
														}
													}
												}
											}
										
										}
									}else{
										//Si no existe ninguna temporalidad para esta oficina y el que se logeo es un sub gerente principal
										
										//Buscar Sub Gerentes activos y de la oficina dada
										List<Empleado> otrosSubGerentesActivos = empleadobean.buscarGerenteActivoPorOficinaPerfilSinSGT(this.empleado.getOficina().getId(), 
												Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
										
										if(otrosSubGerentesActivos != null && otrosSubGerentesActivos.size()>0){
											
											for(Empleado otroSubGerenteActivo : otrosSubGerentesActivos){
												ayudaCargaLdap.reasignarExpedientes(otroSubGerenteActivo, this.empleado);
											}
											
										}	
									}
										
								//}
							}
						}
					
						
					}else
					{
						//En caso el usuario no tenga configurada un perfil temporal
						if(this.empleado.getPerfilBackup() == null)
						{
							RemoteUtils remoteUtils = new RemoteUtils();
							long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
							if(cantexp > 0)
							{
								this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes, no se le puede establecer el perfil temporal configurado");
								flagTienePuestoTemporal = false;
							}
							else
							{
								//Perfil temporal que viene de IDM es Sub Gerente
								if(perfilTemporal.getCodigo().equals(Constantes.ID_PERFIL_SUB_GERENTE.toString()) ){
									//Buscar Sub Gerentes activos y de la oficina dada
									List<Empleado> subGerentesActivos = empleadobean.buscarGerenteActivoPorOficinaPerfil(this.empleado.getOficina().getId(), 
											Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
									
									if(subGerentesActivos != null && subGerentesActivos.size()>0){
										//Si existen Sub Gerentes activos y ademas tienen Exp. pendientes
										
										for(Empleado subGerenteActivo : subGerentesActivos)
										{
											long cantexpSGA = remoteUtils.countConsultaListaTareasTC(subGerenteActivo.getCodigo());					
											if(cantexpSGA > 0){
												ayudaCargaLdap.reasignarExpedientes(subGerenteActivo, this.empleado);
													
											}
											
											//Inactivas al Sub Gerente y marcarlo como Sustituido
											//subGerenteActivo.setFlagActivo(Constantes.FLAG_INACTIVO);
											subGerenteActivo.setFlagEmpleadoSustituido(Constantes.FLAG_ACTIVO);
											this.empleadobean.edit(subGerenteActivo);
										}
									}
								}
								
								this.empleado.setPerfilBackup(this.empleado.getPerfil());
								this.empleado.setPerfil(perfilTemporal);	//this.empleado.setOficina(objOficinaTemporal.getOficina());
								this.empleado.setCodigoCargoBackup(this.empleado.getCodigoCargo());
								this.empleado.setCodigoCargo(codigoPuesto);
								this.empleadobean.edit(this.empleado);
								
								List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
								for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
								{
									//objCartEmpleadoCE.setOficina(this.empleado.getOficina());
									//this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
									
									objCartEmpleadoCE.setPerfil(this.empleado.getPerfil());
									this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
								}
							}							
						}
						else
						{
							if(perfilTemporal!=null){
								if(this.empleado.getPerfil().getId() != perfilTemporal.getId())
								{
									RemoteUtils remoteUtils = new RemoteUtils();
									long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
									if(cantexp > 0)
									{
										this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes para el perfil temporal establecido");
										flagTienePuestoTemporal=true;
									}
									else
									{
										//Perfil temporal que viene de IDM es Sub Gerente
										if(perfilTemporal.getCodigo().equals(Constantes.ID_PERFIL_SUB_GERENTE.toString()) ){
											//Buscar Sub Gerentes activos y de la oficina dada
											List<Empleado> subGerentesActivos = empleadobean.buscarGerenteActivoPorOficinaPerfil(this.empleado.getOficina().getId(), 
													Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
											
											if(subGerentesActivos != null && subGerentesActivos.size()>0){
												//Si existen Sub Gerentes activos y ademas tienen Exp. pendientes
												for(Empleado subGerenteActivo : subGerentesActivos)
												{
													long cantexpSGA = remoteUtils.countConsultaListaTareasTC(subGerenteActivo.getCodigo());					
													if(cantexpSGA > 0){
														ayudaCargaLdap.reasignarExpedientes(subGerenteActivo, this.empleado);
															
													}
													//Inactivas al Sub Gerente
													//subGerenteActivo.setFlagActivo(Constantes.FLAG_INACTIVO);
													subGerenteActivo.setFlagEmpleadoSustituido(Constantes.FLAG_ACTIVO);
													this.empleadobean.edit(subGerenteActivo);
												}
											}
										}
										
										this.empleado.setPerfil(perfilTemporal);
										this.empleado.setCodigoCargo(codigoPuesto);
										this.empleadobean.edit(this.empleado);
										
										List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
										for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
										{
											//objCartEmpleadoCE.setOficina(this.empleado.getOficina());
											//this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
											
											objCartEmpleadoCE.setPerfil(this.empleado.getPerfil());
											this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
										}
									}
								}else{
									//Cuando existe temporalidad y el que se logea es el SGT
									//Esas acciones se ejecutaran cada ves que se logea un SGT con temporalidad vigente
									//Buscar Sub Gerentes activos y de la oficina dada
									List<Empleado> otrosSGPActivos = empleadobean.buscarGerenteActivoPorOficinaPerfil(this.empleado.getOficina().getId(), 
											Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
									
									if(otrosSGPActivos != null && otrosSGPActivos.size()>0){
										//Si existen Sub Gerentes activos y ademas tienen Exp. pendientes
										for(Empleado otroSGPActivo : otrosSGPActivos)
										{
											ayudaCargaLdap.reasignarExpedientes(otroSGPActivo, this.empleado);
										}
									}
									/*RemoteUtils remoteUtils = new RemoteUtils();
									long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
									if(cantexp > 0)
									{
										this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes para el perfil temporal establecido");
										flagTienePuestoTemporal=true;
									}
									else
									{
										//Perfil temporal que viene de IDM es Sub Gerente
										if(perfilTemporal.getCodigo().equals(Constantes.ID_PERFIL_SUB_GERENTE.toString()) ){
											//Buscar Sub Gerentes activos y de la oficina dada
											List<Empleado> subGerentesActivos = empleadobean.buscarGerenteActivoPorOficinaPerfil(this.empleado.getOficina().getId(), 
													Constantes.ID_PERFIL_SUB_GERENTE, this.empleado.getId());
											
											if(subGerentesActivos != null && subGerentesActivos.size()>0){
												//Si existen Sub Gerentes activos y ademas tienen Exp. pendientes
												for(Empleado subGerenteActivo : subGerentesActivos)
												{
													//Inactivas al Sub Gerente
													//subGerenteActivo.setFlagActivo(Constantes.FLAG_INACTIVO);
													subGerenteActivo.setFlagEmpleadoSustituido(Constantes.FLAG_ACTIVO);
													this.empleadobean.edit(subGerenteActivo);
												}
											}
										}
										
										List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
										for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
										{
											objCartEmpleadoCE.setPerfil(this.empleado.getPerfil());
											this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
										}
									}*/
								}
							}
						}
					}
					
					//if(this.empleado.getOficinaBackup() != null) { this.setEtiquetaTemporal("(Temporal)"); }
					if(flagTieneOficinaTemporal){ this.setEtiquetaTemporal("(Temporal)"); }
					if(flagTienePuestoTemporal ){ this.setEtiquetaTemporalPerfil("(Temporal)"); }
				}
			}else {
				LOG.info("El empleado (usuario: {}) esta registrado en LDAP y no esta en el sistema", user);
			} 
		}	
		addObjectSession(Constantes.USUARIO_SESION, empleado);
		addObjectSession("IDM_ACCESO", flagAccesoIDM);
    	
    	if(empleado != null && empleado.getPerfil() != null && empleado.getPerfil().getFlagAdministracion().equals("1"))
    		addObjectSession(Constantes.USUARIO_AD_SESION, empleado);
    	
    	//SUBIENDO A SESSION VALORES DE SCANEO DE DOCUMENTOS
	    addObjectSession(Constantes.COMANDO_SESION, Constantes.RUTA_GMD);
	    /*TODO parametros escaner
	    TipoDocumentoEscanerDelegate tipoDocumentoEscanerDelegate = new TipoDocumentoEscanerDelegate();
	    String parametrosEscaner = tipoDocumentoEscanerDelegate.obtenerParametros();
	    addObjectSession(Constantes.PARAMETROS_SESION, parametrosEscaner);*/
	    if(empleado!=null && empleado.getOficina()!=null)
	    	addObjectSession(Constantes.OFICINA_SESION, empleado.getOficina());	  
		
		
	    //muestra el menu correspondiente
	    try{
			menu.restringuirAcceso();
			
			//String relativePath = getExternalContext().getRealPath("");
			String rutaOrigen = parametrosConfBean.buscarPorVariable(1, "RUTA_ORIGEN_DESCARGA_JAR").getValorVariable();
			//rutaOrigen = relativePath + rutaOrigen;
			LOG.info("RUTA_ORIGEN_DESCARGA_JAR::: " +rutaOrigen);
			addObjectSession(Constantes.RUTA_ORIGEN_DESCARGA_JAR, rutaOrigen);	
			//String url = "\\applet\\";
			
			String rutaDestino = parametrosConfBean.buscarPorVariable(1, "RUTA_DESTINO_DESCARGA_JAR").getValorVariable();
			LOG.info("RUTA_DESTINO_DESCARGA_JAR::: " +rutaDestino);
			addObjectSession(Constantes.RUTA_DESTINO_DESCARGA_JAR, rutaDestino);
						
			String nombreArchivo = parametrosConfBean.buscarPorVariable(1, "NOMBRE_JAR").getValorVariable();
			LOG.info("NOMBRE_JAR::: " +nombreArchivo);
			addObjectSession(Constantes.NOMBRE_JAR, nombreArchivo);
			
			String extencionArchivo = parametrosConfBean.buscarPorVariable(1, "EXTENCION_JAR").getValorVariable();
			LOG.info("EXTENCION_JAR::: " +extencionArchivo);
			addObjectSession(Constantes.EXTENCION_JAR, extencionArchivo);
			
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

	    
	    
    }
    
    /*public void reasignarExpedientes(Empleado subGerenteTieneExp, Empleado subGerenteDebeTenerExp){
    	//inhabilitarActivosExp();
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
    }*/
    
    /*public void actualizarDatosEmpleado(){
    	this.empleado.setPerfil(this.empleado.getPerfilBackup());
		this.empleado.setPerfilBackup(null);
		this.empleado.setCodigoCargo(this.empleado.getCodigoCargoBackup());
		this.empleado.setCodigoCargoBackup(null);
		this.empleadobean.edit(this.empleado);
		
		List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
		for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
		{
			objCartEmpleadoCE.setPerfil(this.empleado.getPerfil());
			this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
		}
    }*/
    
    /*public void actualizarDatosOficinaEmpleado(){
    	this.empleado.setOficina(this.empleado.getOficinaBackup());
		this.empleado.setOficinaBackup(null);
		this.empleadobean.edit(this.empleado);
		
		List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
		for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
		{
			objCartEmpleadoCE.setOficina(this.empleado.getOficina());
			this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
		}
    }*/
  
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	
	public String cortarNombreCompleto(String nc){
		if(nc!=null && !nc.equals("") && nc.length()>42)
			return nc.substring(0, 42);
		else
			return nc;
	}

	public String getMensajeAdvertencia() {
		return mensajeAdvertencia;
	}

	public void setMensajeAdvertencia(String mensajeAdvertencia) {
		this.mensajeAdvertencia = mensajeAdvertencia;
	}

	public String getEtiquetaTemporal() {
		return etiquetaTemporal;
	}

	public void setEtiquetaTemporal(String etiquetaTemporal) {
		this.etiquetaTemporal = etiquetaTemporal;
	}

	public String getEtiquetaTemporalPerfil() {
		return etiquetaTemporalPerfil;
	}

	public void setEtiquetaTemporalPerfil(String etiquetaTemporalPerfil) {
		this.etiquetaTemporalPerfil = etiquetaTemporalPerfil;
	}
	
	
}

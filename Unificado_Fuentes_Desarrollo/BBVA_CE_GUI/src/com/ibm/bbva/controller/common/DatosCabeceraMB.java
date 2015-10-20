package com.ibm.bbva.controller.common;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bpd.RemoteUtils;

import com.grupobbva.bc.per.tele.seguridad.ServiciosSeguridadBbva;
import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.DescargaLDAP;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.OficinaTemporal;
import com.ibm.bbva.proxy.ext.Perfil;
import com.ibm.bbva.proxy.ext.PerfilesUsuario;
import com.ibm.bbva.proxy.ext.Usuario;
import com.ibm.bbva.proxy.ext.UsuarioExtendido;
import com.ibm.bbva.proxy.ext.WSLDAPServiceExtImplPortProxy;
import com.ibm.bbva.proxy.ext.WSLdapException_Exception;
import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;
import com.ibm.bbva.session.DescargaLDAPBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.OficinaTemporalBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.util.Util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
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
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosCabeceraMB.class);
	
	private String mensajeAdvertencia;
	private String etiquetaTemporal;
	
	private String etiquetaTemporalPerfil;
	
	private WSLDAPServiceExtImplPortProxy proxyIDM; 
	
	private boolean flagAccesoIDM;//validar acceso en IDM
	private boolean flagCambiarDatosXTemporalidad ; //flag cambiar datos de usuario por valores TEMPORALES
	private UsuarioExtendido usuarioIDM;
	
	@EJB
	private PerfilBeanLocal perfilBean;
	
	@EJB
	private OficinaBeanLocal oficinabean;
	
	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;
	
	@EJB
	private DescargaLDAPBeanLocal descargaLDAPBeanLocal;
	
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
    	flagCambiarDatosXTemporalidad = false;
    	
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
    	
		if(flagAccesoIDM){
			this.empleado = empleadobean.buscarPorCodigo(user);  
			//validar si tiene temporalidad
			usuarioIDM = lista.get(0);
			
			SimpleDateFormat sdf_puestoTemp = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			LOG.info("FECHA FIN PUESTO TEMPORAL DE IDM  "+ usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaFin().toGregorianCalendar().getTime().toString():"0");
			Date puestoTempFechaFinIDM = sdf_puestoTemp.parse(usuarioIDM.getPuestoTemporal()!=null? usuarioIDM.getPuestoTemporal().getFechaFin().toGregorianCalendar().getTime().toString():"0" , new ParsePosition(0));
			LOG.info("FECHA FIN PUESTO TEMPORAL DE IDM CON FORMATO "+puestoTempFechaFinIDM);
			
			boolean flagTienePuestoTemporal = (usuarioIDM.getPuestoTemporal()!=null && 
					puestoTempFechaFinIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss"),"dd/MM/yyyy HH:mm:ss"))
					&& (!usuarioIDM.getPuestoTemporal().getDescripcionPuesto().equals(usuarioIDM.getPuesto().getNombreCargoFuncionalLocal())))?
											  StringUtils.isNotBlank(
											  usuarioIDM.getPuestoTemporal().getDescripcionPuesto()):false;
			//String codigoPuesto = usuarioIDM.getPuesto().getDescripcionPuesto();
			String codigoPuesto = "";								  
			com.ibm.bbva.entities.Perfil perfilTemporal = null;
			if(flagTienePuestoTemporal){
				codigoPuesto = usuarioIDM.getPuestoTemporal().getDescripcionPuesto();
				List<DescargaLDAP> listaPerfiles = descargaLDAPBeanLocal.buscar("-1", codigoPuesto, "-1", "-1", "-1", "-1");
				for(DescargaLDAP descargaLdap : listaPerfiles){
					if(!empleado.getPerfil().getCodigo().equals(descargaLdap.getPerfil().getCodigo())){
						perfilTemporal = descargaLdap.getPerfil();
					}
				}
			}
			
			SimpleDateFormat sdf_oficinaTemp = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			LOG.info("FECHA FIN OFICINA TEMPORAL DE IDM  "+ usuarioIDM.getCentroTemporal()!=null? usuarioIDM.getCentroTemporal().getFechaFin().toGregorianCalendar().getTime().toString():"0");
			Date ofiTempFechaFinIDM = sdf_oficinaTemp.parse(usuarioIDM.getCentroTemporal()!=null? usuarioIDM.getCentroTemporal().getFechaFin().toGregorianCalendar().getTime().toString():"0" , new ParsePosition(0));
			LOG.info("FECHA FIN OFICINA TEMPORAL DE IDM CON FORMATO "+ofiTempFechaFinIDM);
			
			boolean flagTieneOficinaTemporal = (usuarioIDM.getCentroTemporal()!=null &&  
					ofiTempFechaFinIDM.before(Util.parseStringToDate(Util.getFechayHoraActualByFormato("dd/MM/yyyy HH:mm:ss"),"dd/MM/yyyy HH:mm:ss"))
					&& (!usuarioIDM.getCentroTemporal().getDescripcion().equals(usuarioIDM.getCodigoEntidadUsuario())))?
											   StringUtils.isNotBlank(
											   usuarioIDM.getCentroTemporal().getDescripcion()):false;
											   
			Oficina objOficinaTemporal = null;
			if(flagTieneOficinaTemporal){
				String codigoOficina = usuarioIDM.getCentroTemporal().getDescripcion();
				objOficinaTemporal = oficinabean.buscarPorCodigo(codigoOficina);
			}
	    		
	    		    				
			/*List<OficinaTemporal> listaOficinaTemporal = this.oficinaTemporalBeanLocal.obtenerActual(this.empleado.getId());
			OficinaTemporal objOficinaTemporal = null;		
			if(flagTieneOficinaTemporal)
			{
				objOficinaTemporal = listaOficinaTemporal.get(0);
				DateFormat dateformat= new SimpleDateFormat("dd/MM/yyyy");
				DateFormat timeformat= new SimpleDateFormat("HH:mm");
				try {
					Date fechaActual = dateformat.parse(dateformat.format(new Date()));
					Date horaActual = timeformat.parse(timeformat.format(new Date()));
					if(objOficinaTemporal != null && fechaActual.compareTo(objOficinaTemporal.getFechaInicio()) == 0)
					{
						if(timeformat.parse(objOficinaTemporal.getHoraInicio()).compareTo(horaActual) > 0)
						{
							objOficinaTemporal = null;
						}
					}
					if(objOficinaTemporal != null && fechaActual.compareTo(objOficinaTemporal.getFechaFin()) == 0)
					{
						if(timeformat.parse(objOficinaTemporal.getHoraFin()).compareTo(horaActual) < 0)
						{
							objOficinaTemporal = null;
						}
					}
				} catch (ParseException e) {
					// TODO Bloque catch generado automáticamente
					e.printStackTrace();
				}					
			}*/
			
			if(!(flagTieneOficinaTemporal))//if(objOficinaTemporal == null)
			{
				/*validación para el caso en que el usuario tenia configurada una oficina
				temporal y esta ya caduco se verifica que el usuario no tenga expedientes pendientes, en caso no tenga
				se procede a restaurar el valor original de su oficina*/
				if(this.empleado.getOficinaBackup() != null)
				{				
					RemoteUtils remoteUtils = new RemoteUtils();
					long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
					if(cantexp > 0)
					{
						this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes para la oficina " + this.empleado.getOficina().getCodigo() + " - " + this.empleado.getOficina().getDescripcion() + " temporal configurada");
						flagTieneOficinaTemporal = true;
					}
					else
					{	
						this.empleado.setOficina(this.empleado.getOficinaBackup());
						this.empleado.setOficinaBackup(null);
						this.empleadobean.edit(this.empleado);
						
						List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
						for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
						{
							objCartEmpleadoCE.setOficina(this.empleado.getOficina());
							//TODO:se debe actualizar el Perfil?
							this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
						}
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
						this.empleado.setOficinaBackup(this.empleado.getOficina());
						this.empleado.setOficina(objOficinaTemporal);	//this.empleado.setOficina(objOficinaTemporal.getOficina());				
						this.empleadobean.edit(this.empleado);
						
						List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
						for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
						{
							objCartEmpleadoCE.setOficina(this.empleado.getOficina());
							this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
						}
					}							
				}
				else
				{
					if(this.empleado.getOficina().getId() != objOficinaTemporal.getId())
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
							
						}
					}
				}
			}
			
			if(!(flagTienePuestoTemporal))//if(objOficinaTemporal == null)
			{
				/*validación para el caso en que el usuario tenia configurada una oficina
				temporal y esta ya caduco se verifica que el usuario no tenga expedientes pendientes, en caso no tenga
				se procede a restaurar el valor original de su oficina*/
				if(this.empleado.getPerfilBackup() != null)
				{				
					RemoteUtils remoteUtils = new RemoteUtils();
					long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
					if(cantexp > 0)
					{
						this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes para el perfil " + this.empleado.getPerfil().getCodigo() + " - " + this.empleado.getPerfil().getDescripcion() + " temporal configurada");
						flagTienePuestoTemporal=true;
					}
					else
					{	
						this.empleado.setPerfil(this.empleado.getPerfilBackup());
						this.empleado.setPerfilBackup(null);
						this.empleado.setCodigoCargo(this.empleado.getCodigoCargoBackup());
						this.empleado.setCodigoCargoBackup(null);
						this.empleadobean.edit(this.empleado);
						
						List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
						for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
						{
							//objCartEmpleadoCE.setOficina(this.empleado.getOficina());
							//TODO:se debe actualizar el Perfil?
							//this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
							
							objCartEmpleadoCE.getEmpleado().setPerfil(this.empleado.getPerfil());
							this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
						}
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
							
							objCartEmpleadoCE.getEmpleado().setPerfil(this.empleado.getPerfil());
							this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
						}
					}							
				}
				else
				{
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
							this.empleado.setPerfil(perfilTemporal);
							this.empleado.setCodigoCargo(codigoPuesto);
							this.empleadobean.edit(this.empleado);
							
							List<CartEmpleadoCE> listaCartEmpleadoCE = this.cartEmpleadoCEBeanLocal.buscarPorIdEmpleado(this.empleado.getId());
							for(CartEmpleadoCE objCartEmpleadoCE : listaCartEmpleadoCE)
							{
								//objCartEmpleadoCE.setOficina(this.empleado.getOficina());
								//this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
								
								objCartEmpleadoCE.getEmpleado().setPerfil(this.empleado.getPerfil());
								this.cartEmpleadoCEBeanLocal.edit(objCartEmpleadoCE);
							}
							
						}
					}
				}
			}
			
			//if(this.empleado.getOficinaBackup() != null) { this.setEtiquetaTemporal("(Temporal)"); }
			if(flagTieneOficinaTemporal){ this.setEtiquetaTemporal("(Temporal)"); }
			
			if(flagTienePuestoTemporal ){ this.setEtiquetaTemporalPerfil("(Temporal)"); }
			
			if(empleado == null){
				LOG.info("El empleado (usuario: {}) esta registrado en LDAP y no esta en el sistema", user);
			} else if (Constantes.FLAG_INACTIVO.equals(empleado.getFlagActivo())) {
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
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

	    
	    
    }

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

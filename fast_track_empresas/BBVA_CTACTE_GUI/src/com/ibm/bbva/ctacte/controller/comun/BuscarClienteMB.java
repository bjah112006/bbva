package com.ibm.bbva.ctacte.controller.comun;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.sfp.DatosClientePJSFP;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.ClienteDAO;
import com.ibm.bbva.ctacte.exepciones.ConexionServicioException;
import com.ibm.bbva.ctacte.exepciones.ErrorObteniendoDatosException;
import com.ibm.bbva.ctacte.exepciones.ParametroIlegalException;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.ClientePJCoreWrapper;

@ManagedBean (name="buscarClienteMB")
@ViewScoped
public class BuscarClienteMB extends AbstractMBean {

	private static final long serialVersionUID = -6399120168925416691L;
	private static final Logger LOG = LoggerFactory.getLogger(BuscarClienteMB.class);
	
	private String codigoCentral;
	private boolean disCodigoCentral;
	private String numeroDOI;
	private boolean disNumeroDOI;	
	private boolean disBuscar;	
	private List<ClientePJCoreWrapper> listaCliente;	
	private ClientePJCoreWrapper clienteWrapper;
	private Expediente expediente;
	
	@EJB
	private ClienteBusiness business;
	@EJB
	private ClienteDAO clienteDAO;
	
	@PostConstruct
	public void iniciar () {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("iniciar ()");
		String pagina = getNombrePrincipal();
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Pagina actual {}"+ pagina);		
		disBuscar = true;		
	}
	
	public void modificarEstadoNumDOI (AjaxBehaviorEvent event) {
		LOG.info("modificarEstadoNumDOI ()");
		if (StringUtils.isEmpty(codigoCentral)) {
			disNumeroDOI = false;
			disBuscar = true;
		} else {
			disNumeroDOI = true;
			disBuscar = false;
		}
		LOG.info("Deshabilitar Numero DOI : {}", disNumeroDOI);
	}
	
	public void modificarEstadoCodCentral (AjaxBehaviorEvent event) {
		LOG.info("modificarEstadoCodCentral ()");		
		if (StringUtils.isEmpty(numeroDOI)) {
			disCodigoCentral = false;
			disBuscar = true;
		} else {
			disCodigoCentral = true;
			disBuscar = false;
		}
		LOG.info("Deshabilitar Codigo Central : {}", disCodigoCentral);
	}	
	
	
	public void buscar (ActionEvent ae) {
		LOG.info("buscar (ActionEvent ae)");
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		Util.removeObjectSession(ConstantesAdmin.DATOS_CLIENTE_SESION); // sino se quedaba pegado si el último cliente
		if (StringUtils.isNotEmpty(codigoCentral)) {
			LOG.info("buscar por codigo central");
			
			if (codigoCentral.length() != ConstantesBusiness.CARACTERES_CODIGO_CENTRAL) {
				LOG.info("No tiene la cantidad de caracteres requridas : {}", ConstantesBusiness.CARACTERES_CODIGO_CENTRAL);
				mensajeErrorPrincipal("idCodCent", "Debe ingresar 8 dígitos");	
				return;
			}			
			
			try {
				// siempre retorna como maximo un solo valor en la lista
				try {
					listaCliente = convertirLista (business.buscarCodigoCentral(codigoCentral, empleado.getCodigo()));					
				} catch (ErrorObteniendoDatosException e) {
					listaCliente = new ArrayList<ClientePJCoreWrapper> (0);					
				}		
				boolean resultado = actualizarListas();
				if (!resultado) return;
			} catch (ParametroIlegalException e) {				
				mensajeErrorPrincipal("idCodCent", "Debe ingresar 8 dígitos");				
			} catch (ConexionServicioException e) {
				mensajeConexion (e);
			}
		} else {
			LOG.info("buscar por numero DOI");
			if (numeroDOI.length() < ConstantesBusiness.MIN_CARACTERES_NUMERO_DOI) {
				LOG.info("No tiene la cantidad minima de caracteres : {}",ConstantesBusiness.MIN_CARACTERES_NUMERO_DOI);
				mensajeErrorPrincipal("idNumDOI", "Debe ingresar 11 digitos ");
				return;
			}
			try {
				try {
					listaCliente = convertirLista (business.buscarNumeroDOIEmp(numeroDOI, empleado.getCodigo()));
				} catch (ErrorObteniendoDatosException e) {
					listaCliente = new ArrayList<ClientePJCoreWrapper> (0);
				}
				boolean resultado = actualizarListas();
				if (!resultado) return;
			} catch (ParametroIlegalException e) {
				mensajeErrorPrincipal("idNumDOI", "Debe ingresar 11 digitos ");
			} catch (ConexionServicioException e) {
				mensajeConexion(e);
			}
		}
		redirectAction("../vistaUnica/vistaUnicaLista");
	}
	
	private List<ClientePJCoreWrapper> convertirLista (List<ClientePJCore> lista) 
	 	throws ConexionServicioException, ErrorObteniendoDatosException {
		LOG.info("convertirLista (List<ClientePJCore> lista)");
		ArrayList<ClientePJCoreWrapper> listaTemp = new ArrayList<ClientePJCoreWrapper> ();
		for (ClientePJCore cliente : lista) {
			ClientePJCoreWrapper clienteWrapper = new ClientePJCoreWrapper ();
			clienteWrapper.setCliente(cliente);
			listaTemp.add(clienteWrapper);
		}
		
		if (listaTemp.size() == 1) {
			ClientePJCoreWrapper clienteWrapper = listaTemp.get(0);
			clienteWrapper.setSeleccionado(true);
			ClientePJCore clien = clienteWrapper.getCliente();
			Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
			DatosClientePJCore datos = business.obtenerDatosClientePJ(clien.getCodigoCentral(),
					empleado.getCodigo());
			clien.setDatosCore(datos);
		}
		return listaTemp;
    }
	
	private boolean actualizarListas() {
		LOG.info("actualizarListas()");
		if (listaCliente == null || listaCliente.isEmpty()) {
			LOG.debug("No se encontraron clientes en el servicio web.");
			Cliente cliente = null;
			if (StringUtils.isNotEmpty(codigoCentral)) {
				LOG.info("buscar por codigo central en BD");
				cliente = clienteDAO.findByCodigCentral(codigoCentral);
			} else {
				LOG.info("buscar por numero DOI en BD");
				cliente = clienteDAO.findByNumeroDoi(numeroDOI);
			}
			if (cliente == null) {
				LOG.debug("No se encontraron clientes en BD.");
				Util.removeObjectSession(ConstantesAdmin.DATOS_CLIENTE_SESION);
				mensajeErrorPrincipal("idBuscarCliente",	"El cliente no existe en el HOST");
				return false;
			} else {
				copiarDatos(cliente);
				Util.addObjectSession(ConstantesAdmin.DATOS_CLIENTE_SESION, expediente);
				return true;
			}
		} else if (listaCliente.size()==1) {
			LOG.info("tiene un solo cliente");
			clienteWrapper = listaCliente.get(0);
			copiarDatos();
			
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Código Central : "+expediente.getCliente().getCodigoCentral());
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Número DOI : "+expediente.getCliente().getNumeroDoi());
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("RazonSocial : "+expediente.getCliente().getRazonSocial());			
			
			Util.addObjectSession(ConstantesAdmin.DATOS_CLIENTE_SESION, expediente);
			
			return true;
		} else {
			LOG.info("tiene mas de un cliente");
			Util.removeObjectSession(ConstantesAdmin.DATOS_CLIENTE_SESION);
			clienteWrapper = null;
			return false;
		}	
	}	
	
	private void mensajeConexion(ConexionServicioException e) {
		LOG.error("mensajeConexion(ConexionServicioException e)", e);
		if (ConexionServicioException.Tipo.CORE.equals(e.getTipo())) {
			mensajeErrorPrincipal("idBuscarCliente", 
				"Ha ocurrido un error en la comunicación con HOST por favor intente más tarde");
		} else { 
			mensajeErrorPrincipal("idBuscarCliente", 
				"Ha ocurrido un error en la comunicación con SFP por favor intente más tarde");
		}		
	}
	
	public void copiarDatos() {
		LOG.info("copiarDatos ()");
		
		expediente = new Expediente();
				
		ClientePJCore clientePJ = clienteWrapper.getCliente();
		DatosClientePJCore datosCore = clientePJ.getDatosCore();
		
		Cliente cliente = new Cliente ();
		cliente.setActividadEconomicaCod(datosCore.getCodigoActEconomica());
		cliente.setActividadEconomicaDes(datosCore.getDescActEconomica());
		cliente.setCodigoCentral(clientePJ.getCodigoCentral());
		cliente.setCorreoElectronico1(datosCore.getCorreoElectronico1());
		cliente.setCorreoElectronico2(datosCore.getCorreoElectronico2());
		cliente.setDepartamentoCod(datosCore.getCodigoDepartamento());
		cliente.setDepartamentoDes(datosCore.getDescDepartamento());
		cliente.setDireccion(datosCore.getDireccion());
		cliente.setDistritoCod(datosCore.getCodigoDistrito());
		cliente.setDistritoDes(datosCore.getDescDistrito());
		cliente.setFechaConstitucion(clientePJ.getFechaConstitucion());
		cliente.setNumeroCelular1(datosCore.getNroCelular1());
		cliente.setNumeroCelular2(datosCore.getNroCelular2());
		cliente.setNumeroDoi(datosCore.getNumeroDOI());
		cliente.setProvinciaCod(datosCore.getCodigoProvincia());
		cliente.setProvinciaDes(datosCore.getDescProvincia());
		cliente.setRazonSocial(clientePJ.getRazonSocial());		
		cliente.setSectorCod(datosCore.getSectorCodigo());
		cliente.setSectorDes(datosCore.getSectorDescripcion());
		cliente.setTipoDoi(clientePJ.getTipoDOI());
		cliente.setTipoDoiDes(clientePJ.getDescripcionDOI());
		cliente.setUbicacion(datosCore.getUbicacion());
		expediente.setCliente(cliente);
		LOG.info("expediente cliente "+expediente.getCliente());
	}
	
	public void copiarDatos(Cliente cliente) {
		LOG.info("copiarDatos (cliente)");
		
		expediente = new Expediente();
		expediente.setCliente(cliente);
		LOG.info("expediente cliente "+expediente.getCliente());
	}
	
	public String getCodigoCentral() {
		return codigoCentral; 
	}

	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}

	public String getNumeroDOI() {
		return numeroDOI;
	}

	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
	}

	public List<ClientePJCoreWrapper> getListaCliente() {
		return listaCliente;
	}

	public void setListaCliente(List<ClientePJCoreWrapper> listaCliente) {
		this.listaCliente = listaCliente;
	}

	public boolean isDisCodigoCentral() {
		return disCodigoCentral;
	}

	public void setDisCodigoCentral(boolean disCodigoCentral) {
		this.disCodigoCentral = disCodigoCentral;
	}

	public boolean isDisNumeroDOI() {
		return disNumeroDOI;
	}

	public void setDisNumeroDOI(boolean disNumeroDOI) {
		this.disNumeroDOI = disNumeroDOI;
	}

	public boolean isDisBuscar() {
		return disBuscar;
	}

	public void setDisBuscar(boolean disBuscar) {
		this.disBuscar = disBuscar;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}
}

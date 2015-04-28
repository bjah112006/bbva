package com.ibm.bbva.ctacte.controller.comun;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.ctacte.bean.Campania;
import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.cm.ConsultaContentManager;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IIdentifiquePJOperacion2;
import com.ibm.bbva.ctacte.controller.form.PreRegistroMB;
import com.ibm.bbva.ctacte.dao.CampaniaDAO;
import com.ibm.bbva.ctacte.dao.ClienteDAO;
import com.ibm.bbva.ctacte.dao.CuentaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ProductoDAO;
import com.ibm.bbva.ctacte.exepciones.ConexionServicioException;
import com.ibm.bbva.ctacte.exepciones.ErrorObteniendoDatosException;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.ClientePJCoreWrapper;

@ManagedBean (name="identifiquePJOperacion2")
@ViewScoped
public class IdentifiquePJOperacion2MB extends AbstractMBean {

	private static final long serialVersionUID = -240794883285256190L;
	private static final Logger LOG = LoggerFactory.getLogger(IdentifiquePJOperacion2MB.class);
	
	@ManagedProperty (value="#{preRegistro}")
	private PreRegistroMB preRegistro;
	
	private IIdentifiquePJOperacion2 managedBean;
	
	private Expediente expediente;
	private List<ClientePJCoreWrapper> listaCliente;
	private List<Cuenta> cuentas;
	private List<SelectItem> listaCuentas;
	private String numCuenta;
	private String ccNegocio;
	private boolean mostrarCampania;
	private boolean mostrarSubTabla;
	private boolean disCargoComision;
	private List<Participe> participes;
	private List<SelectItem> listaCampanias;
	private String campania;
	private boolean mostrarCComision;
	private boolean mostrarTablaBast;
	private boolean mostrarPDFDictamen;
	private boolean mostrarPDFInstMod;
	private String urlDictamen;
	private String urlInstMod;
	private Expediente ultimExpBast;
	
	private List<Participe> listaParticipe;
	
	@EJB
	private ClienteBusiness business;
	@EJB
	private ClienteDAO clienteDAO;
	@EJB
	private CuentaDAO cuentaDAO;
	@EJB
	private CampaniaDAO campaniaDAO;
	@EJB
	private ProductoDAO productoDAO;
	@EJB
	private ExpedienteBusiness expedienteBusiness;
	@EJB
	private ClienteBusiness clibusiness;
	@EJB
	private ExpedienteDAO expedienteDAO;
	
	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		mostrarComision(false);
		mostrarTablaBast = false;
		String pagina = getNombrePrincipal();
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		LOG.info("Pagina actual {}", pagina);
		if ("formPreRegistro".equals(pagina)) {
			managedBean = preRegistro;
		}
		managedBean.setIdentifiquePJOperacion2(this);
		
		String tipoPreRegistro = getRequestParameter(ConstantesAdmin.PARAMETRO_TIPO_PRE_REGISTRO);
		if (ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(tipoPreRegistro)) {
			mostrarSubTabla = true;
		}
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		try {
			// no aparece en la lista porque se cambia en la bd y este hace una busqueda en el servicio
			//hry:busca en db o servicio según sea modo consulta  o no respectivamente
			LOG.info("***********isModoConsulta()*********"+isModoConsulta());
			if(isModoConsulta()){
				//LOG.info("isModoConsulta()");
				List<Cliente> listaClienteCon = clienteDAO.findByExpediente(expediente);
				LOG.info("listaClienteCon(): "+listaClienteCon.size());
				listaCliente = new ArrayList<ClientePJCoreWrapper>();
				for(Cliente c:listaClienteCon){
					ClientePJCoreWrapper clientePJ=new ClientePJCoreWrapper();
					ClientePJCore cliente = new ClientePJCore();
					LOG.info("c.getCodigoCentral(): "+c.getCodigoCentral());
					cliente.setCodigoCentral(c.getCodigoCentral());
					LOG.info("c.getTipoDoiDes(): "+c.getTipoDoiDes());
					cliente.setDescripcionDOI(c.getTipoDoiDes());
					LOG.info("c.getNumeroDoi(): "+c.getNumeroDoi());
					cliente.setNumeroDOI(c.getNumeroDoi());
					LOG.info("c.getTipoDoi(): "+c.getTipoDoi());
					cliente.setTipoDOI(c.getTipoDoi());
					LOG.info("c.getFechaConstitucion(): "+c.getFechaConstitucion());
					cliente.setFechaConstitucion(c.getFechaConstitucion());
					LOG.info("c.getRazonSocial(): "+c.getRazonSocial());
					cliente.setRazonSocial(c.getRazonSocial());
					clientePJ.setCliente(cliente);
					listaCliente.add(clientePJ);
				}
			}else{
				listaCliente = convertirLista(business.buscarCodigoCentral(expediente.getCliente().getCodigoCentral(),empleado.getCodigo()));
			}			
			//
			LOG.info("***********listaCliente.*********"+listaCliente);
			//listaCliente = convertirLista (business.buscarCodigoCentral(expediente.getCliente().getCodigoCentral(),empleado.getCodigo()));
			
			numCuenta = expediente.getNumeroCuentaCobro();
			LOG.info("numCuenta: "+numCuenta);
			seleccionarCuenta ();
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("expediente.getCliente() : "+expediente.getCliente());
			cuentas = cuentaDAO.findByClienteActivos(expediente.getCliente());
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("cuentas.isEmpty()1  : "+cuentas.isEmpty());
			if (cuentas.isEmpty()) {
				// solo trae un solo cliente
				String codCentral = expediente.getCliente().getCodigoCentral();
				String codEmpleado = empleado.getCodigo();
				ClientePJCore cliente = business.buscarCodigoCentral(codCentral, codEmpleado).get(0);
				DatosClientePJCore datos = business.obtenerDatosClientePJ(codCentral, codEmpleado);
				cliente.setDatosCore(datos);
				cuentas = Util.copiarListaCuentas(business.listaCuentasActivas(cliente));
			}
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("cuentas.isEmpty()2  : "+cuentas.isEmpty());
			boolean itemSelect = false;
			if (numCuenta == null) {
				itemSelect = true;
			}
			listaCuentas = Util.crearItems(cuentas, itemSelect, "numeroContrato", "monedaCod,numeroContrato,situacionCuenta", "{0} {1} {2}", true);
			if (!ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION.equals(expediente.getFlagExoneracionComision())) {
				disCargoComision = false;
			} else {
				disCargoComision = true;
			}
			if (ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())) {
				LOG.info("Subsanacion");
				mostrarTablaBast = true;
				// para mantener compatibilidad con expedientes antiguos se obtiene el último bastanteo con el query
				// si no existe la relación que se crea con el pre-registro automático
				LOG.info("IdExpUltBastanteo en BD: "+expediente.getIdExpUltBastanteo());
				if (expediente.getIdExpUltBastanteo() != null)
					ultimExpBast = expedienteDAO.load(expediente.getIdExpUltBastanteo());
				else
					ultimExpBast = clibusiness.obternerUltimoBastanteo(expediente.getCliente().getCodigoCentral());
				LOG.info("IdExpUltBastanteo obtenido: "+ultimExpBast.getId());
				LOG.info("managedBean.getFechaActual(): " + managedBean.getFechaActual());
				LOG.info("expediente.getFechaRegistro(): " + expediente.getFechaRegistro());
				int dentroPlazo = expedienteBusiness.dentroPlazoSubsanacion(managedBean.getFechaActual(), 
						ultimExpBast.getFechaFin()); 
				LOG.info("dentroPlazo: " + dentroPlazo);
				if (dentroPlazo == ConstantesBusiness.FUERA_PLAZO_SUBSANACION) {
					disCargoComision = false;
					mostrarComision(true);
				} else {
					disCargoComision = true;
					mostrarComision(false);
					expediente.setFlagExoneracionComision(ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION);
				}
				ConsultaContentManager consultaCM = new ConsultaContentManager ();
				try {
					mostrarPDFDictamen = ultimExpBast.getDictamenBastanteo() != null;
					Documento docCMDic = consultaCM.CM_ObtenerDocumentItemType(ConstantesBusiness.CODIGO_DOCUMENTO_DICTAMEN, ultimExpBast.getId());
					urlDictamen = docCMDic.getUrlContent();
					mostrarPDFInstMod = ultimExpBast.getInstruccionesBastanteo() != null;
					Documento docCMIns = consultaCM.CM_ObtenerDocumentItemType(ConstantesBusiness.CODIGO_DOCUMENTO_INSTRUCCIONES, ultimExpBast.getId());
					urlInstMod = docCMIns.getUrlContent();
				} catch (Exception e) {
					LOG.error("Error al obtener urls", e);
				}
			}
			LOG.info("disCargoComision: " + disCargoComision);
			
		} catch (ConexionServicioException e) {
			LOG.info("**********CATCH ConexionServicioException identifiquePJoperacion2MB********");
			mensajeConexion (e);
		} catch (Exception e) {
			LOG.info("**********CATCH EXEPTION identifiquePJoperacion2MB**********");
			LOG.error("Error", e);
			mensajeErrorPrincipal("dtClientes", "El expediente no tiene tareas...");
			return;
		}
	}

	private void mensajeConexion(ConexionServicioException e) {
		LOG.error("mensajeConexion(ConexionServicioException e)", e);
		if (ConexionServicioException.Tipo.CORE.equals(e.getTipo())) {
			mensajeErrorPrincipal("idIdenPJOp", 
				"Ha ocurrido un error en la comunicación el con HOST por favor intente más tarde");
		} else { // ConexionServicioException.Tipo.SFP.equals(e.getTipo())
			mensajeErrorPrincipal("idIdenPJOp", 
				"Ha ocurrido un error en la comunicación el con SFP por favor intente más tarde");
		}
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
	
	public void seleccionarCuenta (ValueChangeEvent ae) {
		LOG.info("seleccionarCuenta (ValueChangeEvent ae)");
		numCuenta = (String) ae.getNewValue();
		LOG.info("numero de cuenta: {}", numCuenta);
		seleccionarCuenta ();
	}
	
	private void seleccionarCuenta () {
		LOG.info("seleccionarCuenta (Cuenta cuenta)");
		if (!ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(numCuenta) &&
				!ConstantesBusiness.CODIGO_TIPO_CUENTA_TEMP_CUALQUIERA.equals(numCuenta)) {
			Cuenta cuenta = obtenerCuentaSeleccionada ();
			LOG.info("cuenta : {}",cuenta);
			if (cuenta != null) {
				numCuenta = cuenta.getNumeroContrato();
				ccNegocio = cuenta.getProductoCod() + cuenta.getSubproductoCod();
				actualizarListaCampania (cuenta);
				managedBean.mostrarComponentes(cuenta);
			}
		} else {
			managedBean.ocultarComponentes();
		}
	}
	
	private void actualizarListaCampania (Cuenta cuenta) {
		LOG.info("actualizarListaCampania (CuentaCore cuentaCore)");
		String codSubProd = cuenta.getSubproductoCod();
		if (ConstantesBusiness.CODIGO_SUBPRODUCTO_NEGOCIO_PJ_PEN.equals(codSubProd) ||
				ConstantesBusiness.CODIGO_SUBPRODUCTO_NEGOCIO_PJ_USD.equals(codSubProd) ||
				ConstantesBusiness.CODIGO_SUBPRODUCTO_NEGOCIO_PNN_PEN.equals(codSubProd)) {
			LOG.info("mostrando campanias");
			List<Campania> campanias = campaniaDAO.findAll();
			listaCampanias = Util.crearItems(campanias, true, "id", "descripcion");
			mostrarCampania = true;
		} else {
			LOG.info("no se muestra campanias");
			listaCampanias = Util.listaVacia();
			mostrarCampania = false;
		}
	}
	
	private Cuenta obtenerCuentaSeleccionada () {
		LOG.info("obtenerCuentaSeleccionada ()");
		LOG.info("cuentas : -{}-", cuentas);
		if (cuentas!=null && !cuentas.isEmpty()) {
			LOG.info("numCuenta : -{}-", numCuenta);
			for (Cuenta cuenta : cuentas) {
				LOG.info("cuenta.getNumeroContrato() : -{}-", cuenta.getNumeroContrato());
				if (cuenta.getNumeroContrato().equals(numCuenta)) {
					participes = new ArrayList<Participe>(cuenta.getParticipes());
					return cuenta;
				}
			}
		}
		return null;
	}
	
	public boolean esValido () {
		LOG.info("esValido ()");
		if (ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(campania)) {
			mensajeErrorPrincipal("idIdenPJOp:idCampania", "Seleccione una opcion");
			return false;
		}
		return true;
	}

	public void copiarDatos() {
		LOG.info("copiarDatos ()");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		
		LOG.info("copiarDatos:numCuenta: " + numCuenta);
		LOG.info("copiarDatos:expediente.getCliente().getId(): " + expediente.getCliente().getId());
		Cuenta cuenta = cuentaDAO.findByNumeroContrato(expediente.getCliente(), numCuenta);
		expediente.setSubproducto(cuenta.getSubproductoDes());
		expediente.setProducto(productoDAO.findProductoByCodigo(cuenta.getProductoCod()));
		expediente.setEstadoCuenta(cuenta.getSituacionCuenta());
		expediente.setNumeroCuentaCobro(cuenta.getNumeroContrato());
		expediente.setCuentaCobroComision(cuenta.getProductoDes());
		
//		Campania camp = factory.getCampaniaDAO().load(Integer.parseInt(campania));
//		expediente.setCampania(camp);
//		expediente.setFlagExoneracionComision(exonerado);
		LOG.info("expediente cliente "+expediente.getCliente());
	}

	//hry:busca en bd si es modo consulta
	public List<Participe> getListaClienteDB() {
		return listaParticipe;
	}

	public void setListaClienteDB(List<Participe> listaParticipe) {
		this.listaParticipe = listaParticipe;
	}
	//
	
	public List<ClientePJCoreWrapper> getListaCliente() {
		return listaCliente;
	}

	public void setListaCliente(List<ClientePJCoreWrapper> listaCliente) {
		this.listaCliente = listaCliente;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public List<SelectItem> getListaCuentas() {
		return listaCuentas;
	}

	public void setListaCuentas(List<SelectItem> listaCuentas) {
		this.listaCuentas = listaCuentas;
	}

	public String getNumCuenta() {
		return numCuenta;
	}

	public void setNumCuenta(String numCuenta) {
		this.numCuenta = numCuenta;
	}

	public boolean isMostrarCampania() {
		return mostrarCampania;
	}

	public void setMostrarCampania(boolean mostrarCampania) {
		this.mostrarCampania = mostrarCampania;
	}

	public String getCcNegocio() {
		return ccNegocio;
	}

	public void setCcNegocio(String ccNegocio) {
		this.ccNegocio = ccNegocio;
	}

	public void setDisCargoComision(boolean disCargoComision) {
		this.disCargoComision = disCargoComision;
	}

	public boolean isDisCargoComision() {
		return disCargoComision;
	}

	public void setPreRegistro(PreRegistroMB preRegistro) {
		this.preRegistro = preRegistro;
	}

	public PreRegistroMB getPreRegistro() {
		return preRegistro;
	}

	public void setMostrarSubTabla(boolean mostrarSubTabla) {
		this.mostrarSubTabla = mostrarSubTabla;
	}

	public boolean isMostrarSubTabla() {
		return mostrarSubTabla;
	}

	public void setParticipes(List<Participe> participes) {
		this.participes = participes;
	}

	public List<Participe> getParticipes() {
		return participes;
	}

	public List<Cuenta> getCuentas() {
		return cuentas;
	}

	public List<SelectItem> getListaCampanias() {
		return listaCampanias;
	}

	public void setListaCampanias(List<SelectItem> listaCampanias) {
		this.listaCampanias = listaCampanias;
	}

	public String getCampania() {
		return campania;
	}

	public void setCampania(String campania) {
		this.campania = campania;
	}

	private void mostrarComision(boolean mostrar) {
		LOG.info("mostrarComision(boolean mostrar)");
		mostrarCComision = mostrar;
	}
	
	public boolean isMostrarCComision() {
		return mostrarCComision;
	}

	public void setMostrarCComision(boolean mostrarCComision) {
		this.mostrarCComision = mostrarCComision;
	}
	
	public void setMostrarTablaBast(boolean mostrarTablaBast) {
		this.mostrarTablaBast = mostrarTablaBast;
	}

	public boolean isMostrarTablaBast() {
		return mostrarTablaBast;
	}
	
	public void setMostrarPDFDictamen(boolean mostrarPDFDictamen) {
		this.mostrarPDFDictamen = mostrarPDFDictamen;
	}

	public boolean isMostrarPDFDictamen() {
		return mostrarPDFDictamen;
	}

	public void setMostrarPDFInstMod(boolean mostrarPDFInstMod) {
		this.mostrarPDFInstMod = mostrarPDFInstMod;
	}

	public boolean isMostrarPDFInstMod() {
		return mostrarPDFInstMod;
	}

	public void setUrlDictamen(String urlDictamen) {
		this.urlDictamen = urlDictamen;
	}

	public String getUrlDictamen() {
		return urlDictamen;
	}

	public void setUrlInstMod(String urlInstMod) {
		this.urlInstMod = urlInstMod;
	}

	public String getUrlInstMod() {
		return urlInstMod;
	}
	
	public void setUltimExpBast(Expediente ultimExpBast) {
		this.ultimExpBast = ultimExpBast;
	}

	public Expediente getUltimExpBast() {
		return ultimExpBast;
	}
}

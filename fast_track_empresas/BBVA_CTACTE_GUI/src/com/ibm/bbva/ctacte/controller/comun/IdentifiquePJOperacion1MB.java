package com.ibm.bbva.ctacte.controller.comun;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ConsultaCC;
import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.ctacte.bean.Campania;
import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.sfp.DatosClientePJSFP;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.cm.ConsultaContentManager;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IIdentifiquePJOperacion1;
import com.ibm.bbva.ctacte.controller.form.RegistrarNuevoBastanteoMB;
import com.ibm.bbva.ctacte.dao.CampaniaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.OperacionDAO;
import com.ibm.bbva.ctacte.dao.ProductoDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.exepciones.ConexionServicioException;
import com.ibm.bbva.ctacte.exepciones.ErrorObteniendoDatosException;
import com.ibm.bbva.ctacte.exepciones.ParametroIlegalException;
import com.ibm.bbva.ctacte.exepciones.ValidacionOperacionException;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.ClientePJCoreWrapper;

@ManagedBean (name="identifiquePJOperacion1")
@ViewScoped
public class IdentifiquePJOperacion1MB extends AbstractMBean {

	private static final long serialVersionUID = -6399120168925416691L;
	private static final Logger LOG = LoggerFactory.getLogger(IdentifiquePJOperacion1MB.class);
	
	@ManagedProperty (value="#{registrarNuevoBastanteo}")
	private RegistrarNuevoBastanteoMB registrarNuevoBastanteo;
	
	private IIdentifiquePJOperacion1 managedBean;
	
	private String codigoCentral;
	private boolean disCodigoCentral;
	private String numeroDOI;
	private boolean disNumeroDOI;
	private boolean disBuscar;
	private boolean disCargoComis;
	private List<ClientePJCoreWrapper> listaCliente;
	private ClientePJCoreWrapper clienteWrapper;
	private List<SelectItem> listaOperacion;
	private String operacion;
	private List<SelectItem> listaCampanias;
	private String campania;
	private List<Cuenta> cuentas;
	private List<SelectItem> listaCuentas;
	private String numCuenta;
	private String ccNegocio;
	private boolean mostrarOperacion;
	private boolean mostrarCcNegocio;
	private boolean mostrarCampania;
	private boolean mostrarCComision;
	private boolean mostrarTabla;
	private boolean mostrarTablaBast;
	private boolean mostrarPDFDictamen;
	private boolean mostrarPDFInstMod;
	private String urlDictamen;
	private String urlInstMod;
	private String exonerado;
	private String tipoPreRegistro;
	private boolean mostrarSubTabla;
	private List<Participe> participes;
	private Expediente ultimExpBast;
	private List<ExpedienteCC> expedientesPorCerrar;
	private String codigoSubProducto;
	@EJB
	private ClienteBusiness clienteBusiness;
	@EJB
	private ExpedienteBusiness expedienteBusiness;
	@EJB
	private OperacionDAO opDAO;
	@EJB
	private CampaniaDAO campaniaDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private ProductoDAO productoDAO;
	@EJB
	private TareaDAO tareaDAO;

	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		if ("formRegistrarNuevoBastanteo".equals(pagina)) {
			managedBean = registrarNuevoBastanteo;
		}
		managedBean.setIdentifiquePJOperacion1(this);
		disBuscar = true;
		mostrarSubTabla = false;
		iniciarDatos ();
	}
	
	private void iniciarDatos () {
		LOG.info("iniciarDatos ()");
		mostrarTabla(false);
		mostrarDatosCliente(false);
		mostrarComision(false);
		managedBean.ocultarSecciones();
		mostrarSubTabla = false;
		mostrarTablaBast = false;
		expedientesPorCerrar = Collections.EMPTY_LIST;
	}

	private void mostrarTabla(boolean mostrar) {
		LOG.info("mostrarTabla(boolean mostrar)");
		if (!mostrar) {
			listaCliente = Collections.EMPTY_LIST;
			clienteWrapper = null;
		}
		mostrarTabla = mostrar;
	}

	private void mostrarComision(boolean mostrar) {
		LOG.info("mostrarComision(boolean mostrar)");
		if (!mostrar) {
			numCuenta = null;
		}
		mostrarCComision = mostrar;
	}
	
	private void mostrarDatosCliente(boolean mostrar) {
		LOG.info("mostrarDatosCliente(boolean mostrar)");
		if (!mostrar) {
			listaOperacion = Util.listaVacia();
			operacion = null;
			cuentas = Collections.EMPTY_LIST;
			listaCuentas = Util.listaVacia();
			//numCuenta = null;
		}
		//mostrarDatosCCNeg(false);
		mostrarDatosCampania(false);
		mostrarOperacion = mostrar;
		//mostrarCComision = mostrar;
	}

	private void mostrarDatosCampania(boolean mostrar) {
		LOG.info("mostrarDatosCampania(boolean mostrar)");
		if (!mostrar) {
			listaCampanias = Util.listaVacia();
			campania = null;
			ccNegocio = null;
		}
		mostrarCampania = mostrar;
		mostrarCcNegocio = mostrar;
	}
	
	private void limpiarSeleccionados () {
		LOG.info("limpiarSeleccionados ()");
		operacion = null;
		numCuenta = null;
		//ccNegocio = null;
		campania = null;
	}

	public void buscar (ActionEvent ae) {
		LOG.info("buscar (ActionEvent ae)");
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		if (StringUtils.isNotEmpty(codigoCentral)) {
			LOG.info("buscar por codigo central");
			try {
				// siempre retorna como maximo un solo valor en la lista
				listaCliente = convertirLista (clienteBusiness.buscarCodigoCentral(codigoCentral, empleado.getCodigo()));
				actualizarListas();
				//managedBean.actualizarDatosBusqueda();
			} catch (ParametroIlegalException e) {
				mensajeErrorPrincipal("idIdenPJOp:idCodCent", "Debe ingresar 8 dígitos");
				iniciarDatos();
			} catch (ConexionServicioException e) {
				mensajeConexion (e);
			} catch (ErrorObteniendoDatosException e) {
				iniciarDatos();
				mensajeErrorPrincipal("idIdenPJOp:dtClientes", "Ha ocurrido un error en el servicio HOST. Error: "+e.getMessage());
			}
		} else {
			LOG.info("buscar por numero DOI");
			try {
				listaCliente = convertirLista (clienteBusiness.buscarNumeroDOI(numeroDOI, empleado.getCodigo()));
				actualizarListas();
				//managedBean.actualizarDatosBusqueda();
			} catch (ParametroIlegalException e) {
				mensajeErrorPrincipal("idIdenPJOp:idNumDOI", "Debe ingresar por lo menos " + ConstantesBusiness.MIN_CARACTERES_NUMERO_DOI + " dígitos");
				iniciarDatos();
			} catch (ConexionServicioException e) {
				mensajeConexion(e);
			} catch (ErrorObteniendoDatosException e) {
				iniciarDatos();
				mensajeErrorPrincipal("idIdenPJOp:dtClientes", "Ha ocurrido un error en el servicio HOST. Error: "+e.getMessage());
			}
		}
	}

	private void actualizarListas() {
		LOG.info("actualizarListas()");
		if (listaCliente.isEmpty()) {
			LOG.info("no tiene clientes");
			mensajeErrorPrincipal("idIdenPJOp:dtClientes", "El cliente no existe.");
			mostrarTabla(false);
			mostrarDatosCliente(false);
		} else if (listaCliente.size()==1) {
			LOG.info("tiene un solo cliente");
			clienteWrapper = listaCliente.get(0);
			ClientePJCore clientePJ = clienteWrapper.getCliente();
			crearListaOperaciones(clientePJ);
			limpiarSeleccionados ();
			//mostrarDatosCCNeg(false);
			mostrarDatosCampania(false);
		} else {
			LOG.info("tiene mas de un cliente");
			mostrarTabla(true);
			mostrarDatosCliente(false);
			clienteWrapper = null;
		}
		mostrarComision(false);
		managedBean.ocultarSecciones();
		mostrarSubTabla = false;
		mostrarTablaBast = false;
	}

	private void crearListaOperaciones(ClientePJCore clientePJCore) {
		LOG.info("crearListaOperaciones (ClientePJCore clientePJCore)");
		expedientesPorCerrar = Collections.EMPTY_LIST;
		try {
			if (!clienteBusiness.esValidoExpediente(clientePJCore)) {
				mensajeErrorPrincipal("idIdenPJOp:dtClientes", 
						"El cliente tiene un expediente en Nuevo Bastanteo.");
				mostrarDatosCliente(false);
				mostrarComision(false);
				mostrarTabla(false);
				managedBean.ocultarSecciones();
				return;
			} else {
				// consultar si el empleado tiene expedientes por cerrar del mismo cliente 
				Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
				RemoteUtils bandejaTareasBDelegate = new RemoteUtils();
				ConsultaCC consulta = new ConsultaCC();
				consulta.setNumeroTarea(Integer.toString(ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE));
				consulta.setCodCentralCliente(clientePJCore.getCodigoCentral());
				consulta.setCodUsuarioActual(empleado.getCodigo());
				consulta.setConsiderarUsuarios(false);
				
				expedientesPorCerrar = bandejaTareasBDelegate.obtenerInstanciasTareasPorUsuarioCC(consulta);
				if (expedientesPorCerrar != null && expedientesPorCerrar.size() > 0) {
					String mensaje;
					if (expedientesPorCerrar.size() == 1)
						mensaje = "Antes de registrar un nuevo expediente, debe cerrar el siguiente expediente:";
					else
						mensaje = "Antes de registrar un nuevo expediente, debe cerrar los siguientes expedientes:";
					mensajeErrorPrincipal("idIdenPJOp:dtClientes", mensaje);
					mostrarDatosCliente(false);
					mostrarComision(false);
					mostrarTabla(false);
					managedBean.ocultarSecciones();
					return;
				}
			}
			
			List<Operacion> operaciones = clienteBusiness.listaOperaciones(clientePJCore);
			LOG.info("operaciones : {}", operaciones.size());
			listaOperacion = Util.crearItems(operaciones, true, "codigoOperacion", 
					"codigoOperacion,descripcion", "{0} - {1}", true);
			cuentas = Util.copiarListaCuentas(clienteBusiness.listaCuentasActivas(clientePJCore));
			mostrarTabla(true);
			mostrarDatosCliente(true);
			if (cuentas.size() == 1) {
				listaCuentas = Util.crearItems(cuentas, false, "numeroContrato", 
						"monedaCod,numeroContrato,situacionCuenta", "{0} {1} {2}", true);
			} else {
				listaCuentas = Util.crearItems(cuentas, true, "numeroContrato", 
						"monedaCod,numeroContrato,situacionCuenta", "{0} {1} {2}", true);
			}
			
			// Mostrar mensaje si no todas las cuentas activas tienen partícipes
			boolean valParticipes = true;
			for (Cuenta cta : cuentas) {
				if (cta.getParticipes() == null || cta.getParticipes().size() == 0) {
					valParticipes = false;
					break;
				}
			}
			if (!valParticipes) {
				mensajeInfoPrincipal("idIdenPJOp:idBuscar", 
						"Alguna de las cuentas no tienen partícipes.");
			}
		} catch (ValidacionOperacionException e) {
			if (ValidacionOperacionException.Tipo.CORE.equals(e.getTipo())) {
				mensajeErrorPrincipal("idIdenPJOp:dtClientes", 
						"El cliente no existe y/o no cuenta con Cuentas activas en HOST");
			} else { //ValidacionOperacionException.Tipo.SPF.equals(e.getTipo())
				mensajeErrorPrincipal("idIdenPJOp:dtClientes", 
						"El cliente no existe y/o no esta activo en SFP");
			}
			mostrarDatosCliente(false);
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
			DatosClientePJCore datos = clienteBusiness.obtenerDatosClientePJ(clien.getCodigoCentral(),
					empleado.getCodigo());
			clien.setDatosCore(datos);
		}
		return listaTemp;
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
	
	public void seleccionarCliente (ValueChangeEvent ae) {
		LOG.info("seleccionarCliente (ValueChangeEvent ae)");
		Boolean estado = (Boolean) ae.getNewValue();
		//String codCentral = getRequestParameter("codigoCentral");
		FacesContext ctx = FacesContext.getCurrentInstance();
		String codCentral = ctx.getApplication().evaluateExpressionGet(ctx, "#{item.cliente.codigoCentral}", String.class);
		LOG.info("seleccionado: {} - codigoCentral: {}", estado, codCentral);
		limpiarSeleccionados();
		if (estado) {
			try {
				for (ClientePJCoreWrapper cpj : listaCliente) {
					if (cpj.getCliente().getCodigoCentral().equals(codCentral)) {
						if (clienteWrapper != null) {
							LOG.info("deseleccionar: {}", clienteWrapper.getCliente().getCodigoCentral());
							clienteWrapper.setSeleccionado(false);
						}
						clienteWrapper = cpj;
						break;
					}
				}
				ClientePJCore clientePJ = clienteWrapper.getCliente();
				if (clientePJ.getDatosCore()==null) {
					Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
					DatosClientePJCore datos = clienteBusiness.obtenerDatosClientePJ(codCentral, 
							empleado.getCodigo());
					clientePJ.setDatosCore(datos);
				}
				crearListaOperaciones(clientePJ);
			} catch (ConexionServicioException e) {
				mensajeConexion(e);
				clienteWrapper = null;
				listaOperacion = Util.listaVacia();
			} catch (ErrorObteniendoDatosException e) {
				LOG.info("no tiene datos");
				mensajeErrorPrincipal("idIdenPJOp:dtClientes", 
						"Ha ocurrido un error en el servicio HOST. Error: "+e.getMessage());
			}
		} else {
			mostrarDatosCliente(false);
			
			mostrarComision(false);
			mostrarDatosCampania(false);
			mostrarSubTabla = false;
			managedBean.ocultarSecciones();
			mostrarPDFDictamen = false;
			mostrarPDFInstMod = false;
		}
	}
	
	public void seleccionarCuenta (ValueChangeEvent ae) {
		LOG.info("seleccionarCuenta (ValueChangeEvent ae)");
		numCuenta = (String) ae.getNewValue();
		LOG.info("numero de cuenta: {}", numCuenta);
		if (!ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(numCuenta)) {
			Cuenta cc = obtenerCuentaSeleccionada ();
			seleccionarCuenta (cc);
			if (!ConstantesBusiness.CODIGO_NUEVO_BASTANTEO.equals(operacion) && 
					!ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(operacion)) {
				mostrarSubTabla = true;
			} else {
				mostrarSubTabla = false;
			}
		} else {
			mostrarDatosCampania(false);
			//mostrarDatosCCNeg(false);
			managedBean.ocultarSecciones();
			numCuenta = null;
		}
	}
	
	private Cuenta obtenerCuentaSeleccionada () {
		LOG.info("obtenerCuentaSeleccionada ()");
		if (cuentas!=null && !cuentas.isEmpty()) {
			for (Cuenta cc : cuentas) {
				if (cc.getNumeroContrato().equals(numCuenta)) {
					participes = new ArrayList<Participe>(cc.getParticipes());
					return cc;
				}
			}
		}
		return null;
	}
	
	private void seleccionarCuenta (Cuenta cuentaSel) {
		LOG.info("seleccionarCuenta (Cuenta cuentaSel)");
		numCuenta = cuentaSel.getNumeroContrato();
		ccNegocio = cuentaSel.getProductoCod() + cuentaSel.getSubproductoCod();
		// se deberia concatenar con numero del subproducto pero en el objeto no existe
		codigoSubProducto = cuentaSel.getSubproductoCod();
		LOG.info("codigo de producto : {} - codigo subproducto : {}", cuentaSel.getProductoCod(), cuentaSel.getSubproductoCod());
		LOG.info("numero de cuenta : {} - operacion : {}", numCuenta, operacion);
		Cuenta cuenta = obtenerCuentaSeleccionada();
		ClientePJCore cliente = clienteWrapper.getCliente();
		if (ConstantesBusiness.CODIGO_NUEVO_BASTANTEO.equals(operacion)) {
			exonerado = clienteBusiness.exoneradoNuevoBastanteo(cliente, cuenta);
		}
		managedBean.mostrarSecciones (cuenta, opDAO.load(Integer.valueOf(operacion)), cliente);
		//mostrarDatosCCNeg(true);
		actualizarListaCampania (cuentaSel);
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
			mostrarDatosCampania(true);
		} else {
			LOG.info("no se muestra campanias");
			listaCampanias = Util.listaVacia();
			mostrarDatosCampania(false);
		}
	}

	private void mensajeConexion(ConexionServicioException e) {
		LOG.error("mensajeConexion(ConexionServicioException e)", e);
		if (ConexionServicioException.Tipo.CORE.equals(e.getTipo())) {
			mensajeErrorPrincipal("idIdenPJOp", 
				"Ha ocurrido un error en la comunicación con HOST por favor intente más tarde");
		} else { // ConexionServicioException.Tipo.SFP.equals(e.getTipo())
			mensajeErrorPrincipal("idIdenPJOp", 
				"Ha ocurrido un error en la comunicación con SFP por favor intente más tarde");
		}
		iniciarDatos();
	}
	
	public void seleccionarOperacion (ValueChangeEvent ae) {
		LOG.info("seleccionarOperacion (ValueChangeEvent ae)");
		operacion = (String) ae.getNewValue();
		LOG.info("codigo operacion : {}", operacion);
		if (!ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(operacion)) {
			ClientePJCore cliente = clienteWrapper.getCliente();
			String codCent = cliente.getCodigoCentral();
			LOG.info("codigo central : {}", codCent);
			Expediente exp = expedienteDAO.getExpediente(codCent, ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO, operacion);
			if (ConstantesBusiness.CODIGO_NUEVO_BASTANTEO.equals(operacion)) {
				if (exp != null) {
					LOG.info("tiene pre-registro");
					addCallbackParam("tienePreRegistro", true);
					addCallbackParam("mensaje", "Existe un trámite en Pre registro. ¿Desea continuar?");
					addCallbackParam("cliente", exp.getCliente().getRazonSocial());
					tipoPreRegistro = ConstantesBusiness.CODIGO_NUEVO_BASTANTEO;
					try {
						Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
						Util.copiarExpediente(exp, expediente);
					} catch (Exception e) {e.printStackTrace();}
				} else {
					exp = Util.crearExpedienteCU1();
					try {
						Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
						Util.copiarExpediente(exp, expediente);
					} catch (Exception e) {e.printStackTrace();}
					if (cuentas.size() == 1) {
						seleccionarCuenta(cuentas.get(0));
					}
					LOG.info("no tiene pre-registro");
					addCallbackParam("tienePreRegistro", false);
					mostrarComision(true);
				}
				mostrarSubTabla = false;
				disCargoComis = false;
				mostrarTablaBast = false;
				mostrarPDFDictamen = false;
				mostrarPDFInstMod = false;
			} else if (ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(operacion)) {
				managedBean.ocultarSecciones();
				if (exp != null) {
					LOG.info("tiene pre-registro");
					addCallbackParam("tienePreRegistro", true);
					addCallbackParam("mensaje", "Existe un trámite de Modificatoria en Pre registro. ¿Desea continuar?");
					addCallbackParam("cliente", exp.getCliente().getRazonSocial());
					tipoPreRegistro = ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO;
					try {
						Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
						Util.copiarExpediente(exp, expediente);
					} catch (Exception e) {e.printStackTrace();}
				} else {
					exp = Util.crearExpedienteCU1();
					try {
						Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
						Util.copiarExpediente(exp, expediente);
					} catch (Exception e) {e.printStackTrace();}
					LOG.info("cuentas.size() : {}", cuentas.size());
					if (cuentas.size() == 1) {
						seleccionarCuenta(cuentas.get(0));
					}
					LOG.info("no tiene pre-registro");
					addCallbackParam("tienePreRegistro", false);
					mostrarComision(true);
				}
				mostrarSubTabla = false;
				disCargoComis = false;
				mostrarTablaBast = false;
				mostrarPDFDictamen = false;
				mostrarPDFInstMod = false;
			} else if (ConstantesBusiness.CODIGO_CAMBIO_FIRMAS.equals(operacion)) {
				LOG.info("Cambio de firmas");
				addCallbackParam("tienePreRegistro", false);
				if (cuentas.size() == 1) {
					seleccionarCuenta(cuentas.get(0));
				}
				mostrarComision(false);
				exonerado = ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION;
				//mostrarDatosCCNeg(false);
				mostrarDatosCampania(false);
				mostrarSubTabla = true;
				mostrarTablaBast = false;
				disCargoComis = false;
				cuentas = Util.copiarListaCuentas (clienteWrapper.getCliente().getDatosCore().getCuentas());//Util.copiarListaCuentas(clienteBusiness.listaCuentasActivas(clientePJCore));
				participes = new ArrayList<Participe> ();
				for (Cuenta cuenta : cuentas) {
					participes.addAll(cuenta.getParticipes());
				}
				LOG.info("Ordenar participes");
				Collections.sort(participes, new Comparator<Participe>() {
					public int compare(Participe o1, Participe o2) {
						int comp = o1.getCodigoCentral().compareTo(o2.getCodigoCentral());
						if (comp == 0) {
							if (o1.getCuenta()== null) {LOG.info("1");
								comp = -1;
							} else if (o2.getCuenta() == null) {LOG.info("2");
								comp = 1;
							} else if (o1.getCuenta().getFechaCreacion() == null) {LOG.info("3");
								comp = -1;
							} else if (o2.getCuenta().getFechaCreacion() == null) {LOG.info("4");
								comp = 1;
							} else {LOG.info("5");
								comp = o1.getCuenta().getFechaCreacion().compareTo(o2.getCuenta().getFechaCreacion());
							}
						}
						return comp;
					}
				});
				managedBean.mostrarSecciones (null, opDAO.load(Integer.valueOf(operacion)), cliente);
				mostrarPDFDictamen = false;
				mostrarPDFInstMod = false;
			} else if (ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(operacion)) {
				LOG.info("Subsanacion");
				addCallbackParam("tienePreRegistro", false);
				mostrarTablaBast = true;
				ultimExpBast = clienteBusiness.obternerUltimoBastanteo(cliente.getCodigoCentral());
				int dentroPlazo = expedienteBusiness.dentroPlazoSubsanacion(managedBean.getFechaActual(), 
						ultimExpBast.getFechaFin());
				if (dentroPlazo == ConstantesBusiness.FUERA_PLAZO_SUBSANACION) {
					mostrarComision(true);
					disCargoComis = false;
					exonerado = ConstantesBusiness.FLAG_COBRO_COMISION;
				} else {
					mostrarComision(false);
					exonerado = ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION;
				}

//				String tipoPJ = cliente.getDatosSFP().getTipoPJ();
//				if (StringUtils.isNotEmpty(tipoPJ)) {
					managedBean.mostrarSecciones (null, opDAO.load(Integer.valueOf(operacion)), cliente);
//				}
				//mostrarDatosCCNeg(false);
				ConsultaContentManager consultaCM = new ConsultaContentManager ();
				try {
					mostrarPDFDictamen = ultimExpBast.getDictamenBastanteo() != null;
					Documento docCMDic = consultaCM.CM_DocumentoxCliente(ConstantesBusiness.CODIGO_DOCUMENTO_DICTAMEN, codigoCentral);
					urlDictamen = docCMDic.getUrlContent();
					mostrarPDFInstMod = ultimExpBast.getInstruccionesBastanteo() != null;
					Documento docCMIns = consultaCM.CM_DocumentoxCliente(ConstantesBusiness.CODIGO_DOCUMENTO_INSTRUCCIONES, codigoCentral);
					urlInstMod = docCMIns.getUrlContent();
				} catch (Exception e) {
					LOG.error("Error al obtener urls", e);
				}
			} else if (ConstantesBusiness.CODIGO_REVOCATORIA_ESPECIFICA.equals(operacion)) {
				LOG.info("Revocatoria");
				if (cuentas.size() == 1) {
					seleccionarCuenta(cuentas.get(0));
				}
				addCallbackParam("tienePreRegistro", false);
				mostrarComision(false);
				//mostrarDatosCCNeg(false);
				mostrarDatosCampania(false);
				mostrarSubTabla = false;
				mostrarTablaBast = false;
				disCargoComis = false;
				managedBean.mostrarSecciones (null, opDAO.load(Integer.valueOf(operacion)), cliente);
				mostrarPDFDictamen = false;
				mostrarPDFInstMod = false;
				exonerado = ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION;
			}
		} else {
			mostrarComision(false);
			//mostrarDatosCCNeg(false);
			mostrarDatosCampania(false);
			mostrarSubTabla = false;
			managedBean.ocultarSecciones();
			mostrarPDFDictamen = false;
			mostrarPDFInstMod = false;
		}
	}
	
	public void redireccionar (ActionEvent event) {
		LOG.info("redireccionar (ActionEvent event)");
		redirectAction("../bandeja/bandeja");
	}
	
	public void abrirExpedientePreRegistro (ActionEvent ae) {
		LOG.info("abrirExpedientePreRegistro (ActionEvent ae)");
		redirectAction("../preRegistro/formPreRegistro", 
				new String[]{ConstantesAdmin.PARAMETRO_TIPO_PRE_REGISTRO,tipoPreRegistro});
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
		LOG.info("expediente fin "+expediente);
		Operacion oper = opDAO.findByCodOperacion(operacion);
		expediente.setOperacion(oper);
		
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
		
		// Se está usando el campo "fechaEscritura" del servicio de SFP para saber si el cliente ya fue migrado o no
		String flagExpMigrado;
		if (clientePJ.getDatosSFP().getFechaEscritura() != null
				&& !clientePJ.getDatosSFP().getFechaEscritura().trim()
						.equals("")) {
			flagExpMigrado = "1";
		} else {
			flagExpMigrado = "0";
		}
		cliente.setFlagExpMigrado(flagExpMigrado);
		// TODO: Cambio por el campo tipo de persona: Nulo = No Migrado || !Nulo = Migrado
		cliente.setFlagOrigenSFP(clientePJ.getDatosSFP().getTipoPJ() == null || clientePJ.getDatosSFP().getTipoPJ().isEmpty() ? "0" : "1");
		
		expediente.setCliente(cliente);

		if (!ConstantesBusiness.CODIGO_REVOCATORIA_ESPECIFICA.equals(operacion)
				&& !ConstantesBusiness.CODIGO_CAMBIO_FIRMAS.equals(operacion)) {
			Cuenta cuenta = obtenerCuentaSeleccionada();
			if (cuenta != null) {
				expediente.setSubproducto(cuenta.getSubproductoDes());
				expediente.setProducto(productoDAO.findProductoByCodigo(cuenta.getProductoCod()));
				expediente.setEstadoCuenta(cuenta.getSituacionCuenta());
				expediente.setNumeroCuentaCobro(cuenta.getNumeroContrato());
				expediente.setCuentaCobroComision(cuenta.getProductoDes());
			}
		}
		
		if (StringUtils.isNotEmpty(campania)) {
			Campania camp = campaniaDAO.load(Integer.parseInt(campania));
			expediente.setCampania(camp);
		}
		expediente.setFlagExoneracionComision(exonerado);
		LOG.info("exonerado--exonerado "+exonerado);
		LOG.info("expediente cliente "+expediente.getCliente());
	}
	
	public void irACerrarExpdiente() {
		LOG.info("irACerrarExpdiente");
		
		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestParameterMap();
		String codExpediente = (String) requestMap.get("codExpediente");
		String codResponsable = (String) requestMap.get("codResponsable");

		LOG.info("***********codExpediente*********"+codExpediente);
		LOG.info("***********codResponsable*********"+codResponsable);
		
		ExpedienteCC expedienteCC = null;
		for (ExpedienteCC obj : expedientesPorCerrar) {
			if (obj.getCodigoExpediente().equals(codExpediente)) {
				expedienteCC = obj;
				break;
			}
		}
		if (expedienteCC != null) {
			Expediente expediente = expedienteDAO.load(Integer.parseInt(expedienteCC.getCodigoExpediente()));
			expediente.setNumTerminal(Util.obtenerIp());
			expediente.setFechaEnvio(expedienteCC.getActivado().getTime());
			Tarea tarea = tareaDAO.load(Integer.valueOf(expedienteCC.getDatosFlujoCtaCte().getIdTarea()));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(expediente.getFechaEnvio());
			calendar.add(Calendar.MINUTE, tarea.getVerdeMinutos());
			expediente.setFechaProgramada(calendar.getTime());
			
			ExpedienteTarea expedienteTarea = expediente.getExpedienteTareas().iterator().next();
			expedienteTarea.setTarea(tarea);
			String pagina = "../" + tarea.getNombrePagina();
			LOG.info("redirect : {}", pagina);
			
			Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION, expedienteCC);
			Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);		
			Util.addObjectSession(ConstantesAdmin.RESPONSABLE_SESION, codResponsable);
			
			redirectAction(pagina);
		}
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

	public List<SelectItem> getListaOperacion() {
		return listaOperacion;
	}

	public void setListaOperacion(List<SelectItem> listaOperacion) {
		this.listaOperacion = listaOperacion;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getCampania() {
		return campania;
	}

	public void setCampania(String campania) {
		this.campania = campania;
	}

	public String getCcNegocio() {
		return ccNegocio;
	}

	public void setCcNegocio(String ccNegocio) {
		this.ccNegocio = ccNegocio;
	}

	public boolean isMostrarOperacion() {
		return mostrarOperacion;
	}

	public void setMostrarOperacion(boolean mostrarOperacion) {
		this.mostrarOperacion = mostrarOperacion;
	}

	public boolean isMostrarCcNegocio() {
		return mostrarCcNegocio;
	}

	public void setMostrarCcNegocio(boolean mostrarCcNegocio) {
		this.mostrarCcNegocio = mostrarCcNegocio;
	}

	public boolean isMostrarCampania() {
		return mostrarCampania;
	}

	public void setMostrarCampania(boolean mostrarCampania) {
		this.mostrarCampania = mostrarCampania;
	}

	public boolean isMostrarCComision() {
		return mostrarCComision;
	}

	public void setMostrarCComision(boolean mostrarCComision) {
		this.mostrarCComision = mostrarCComision;
	}

	public boolean isMostrarTabla() {
		return mostrarTabla;
	}

	public void setMostrarTabla(boolean mostrarTabla) {
		this.mostrarTabla = mostrarTabla;
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

	public List<SelectItem> getListaCampanias() {
		return listaCampanias;
	}

	public void setListaCampanias(List<SelectItem> listaCampanias) {
		this.listaCampanias = listaCampanias;
	}

	public void setRegistrarNuevoBastanteo(RegistrarNuevoBastanteoMB registrarNuevoBastanteo) {
		this.registrarNuevoBastanteo = registrarNuevoBastanteo;
	}

	public RegistrarNuevoBastanteoMB getRegistrarNuevoBastanteo() {
		return registrarNuevoBastanteo;
	}

	public void setParticipes(List<Participe> participes) {
		this.participes = participes;
	}

	public List<Participe> getParticipes() {
		return participes;
	}

	public List<Cuenta> getCuentas() {
		return Util.copiarListaCuentas (clienteWrapper.getCliente().getDatosCore().getCuentas());
	}

	public void setMostrarSubTabla(boolean mostrarSubTabla) {
		this.mostrarSubTabla = mostrarSubTabla;
	}

	public boolean isMostrarSubTabla() {
		return mostrarSubTabla;
	}

	public void setMostrarTablaBast(boolean mostrarTablaBast) {
		this.mostrarTablaBast = mostrarTablaBast;
	}

	public boolean isMostrarTablaBast() {
		return mostrarTablaBast;
	}

	public void setUltimExpBast(Expediente ultimExpBast) {
		this.ultimExpBast = ultimExpBast;
	}

	public Expediente getUltimExpBast() {
		return ultimExpBast;
	}

	public void setDisCargoComis(boolean disCargoComis) {
		this.disCargoComis = disCargoComis;
	}

	public boolean isDisCargoComis() {
		return disCargoComis;
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

	public List<ExpedienteCC> getExpedientesPorCerrar() {
		return expedientesPorCerrar;
	}

	public void setExpedientesPorCerrar(List<ExpedienteCC> expedientesPorCerrar) {
		this.expedientesPorCerrar = expedientesPorCerrar;
	}

	public String getCodigoSubProducto() {
		return codigoSubProducto;
	}

	public void setCodigoSubProducto(String codigoSubProducto) {
		this.codigoSubProducto = codigoSubProducto;
	}
	
	public DatosClientePJSFP getPoderes(){
		return this.clienteWrapper.getCliente().getDatosSFP();
	}
	
}

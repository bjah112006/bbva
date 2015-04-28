package com.ibm.bbva.ctacte.business.impl;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ListaCerrada;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.CuentaCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.sfp.DatosClientePJSFP;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.ClienteDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ListaCerradaDAO;
import com.ibm.bbva.ctacte.dao.OperacionDAO;
import com.ibm.bbva.ctacte.dao.servicio.CoreDAO;
import com.ibm.bbva.ctacte.dao.servicio.SistemaFirmasPoderesDAO;
import com.ibm.bbva.ctacte.exepciones.ConexionServicioException;
import com.ibm.bbva.ctacte.exepciones.ErrorObteniendoDatosException;
import com.ibm.bbva.ctacte.exepciones.ParametroIlegalException;
import com.ibm.bbva.ctacte.exepciones.ValidacionOperacionException;

/**
 * Session Bean implementation class ClienteBusinessImpl
 */
@Stateless
@Local(ClienteBusiness.class)
public class ClienteBusinessImpl implements ClienteBusiness {
	
	private static final Logger LOG = LoggerFactory.getLogger(ClienteBusinessImpl.class);
	
	@EJB
	private CoreDAO coreDAO;
	@EJB
	private SistemaFirmasPoderesDAO sfpDAO;
	@EJB
	private ClienteDAO clienteDAO;
	@EJB
	private ListaCerradaDAO listaCerradaDAO;
	@EJB
	private OperacionDAO operacionDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;

    /**
     * Default constructor. 
     */
    public ClienteBusinessImpl() {
    }

	@Override
	public List<ClientePJCore> buscarCodigoCentral(String codigoCentral,
			String usuario) throws ParametroIlegalException,
			ConexionServicioException, ErrorObteniendoDatosException {
		LOG.info("buscarCodigoCentral (String codigoCentral)");
		try{
			LOG.info("************TRY ClienteBusiness buscarCodigoCentral****************");
			if (codigoCentral.length() != ConstantesBusiness.CARACTERES_CODIGO_CENTRAL) {
				LOG.info("No tiene la cantidad de caracteres requeridas : {}", ConstantesBusiness.CARACTERES_CODIGO_CENTRAL);
				throw new ParametroIlegalException ();
			}
		}catch (ParametroIlegalException e) {
			LOG.info("************CATCH ClienteBusiness buscarCodigoCentral****************");
			throw new ParametroIlegalException ();
		}
		
		try {
			List<ClientePJCore> listaDCCore = coreDAO.buscarClientePJ(codigoCentral, null, usuario);
			// corregir en caso la lista tiene valores del servicio repetidos
			obtenerDatosSFP(listaDCCore);
			
			return listaDCCore;
		} catch (ConnectException e) {
			throw new ConexionServicioException (e, ConexionServicioException.Tipo.CORE);
		}
	}

	@Override
	public List<ClientePJCore> buscarNumeroDOI(String numeroDOI, String usuario)
			throws ParametroIlegalException, ConexionServicioException,
			ErrorObteniendoDatosException {
		LOG.info("buscarNumeroDOI (String numeroDOI)");
		if (numeroDOI.length() < ConstantesBusiness.MIN_CARACTERES_NUMERO_DOI) {
			LOG.info("No tiene la cantidad minima de caracteres : {}", 
					ConstantesBusiness.MIN_CARACTERES_NUMERO_DOI);
			throw new ParametroIlegalException ();
		}
		try {
			List<ClientePJCore> listaDCCore = coreDAO.buscarClientePJ(null, numeroDOI, usuario);
			// corregir en caso la lista tiene valores del servicio repetidos
			obtenerDatosSFP(listaDCCore);
			
			return listaDCCore;
		} catch (ConnectException e) {
			throw new ConexionServicioException (e, ConexionServicioException.Tipo.CORE);
		}
	}

	@Override
	public List<ClientePJCore> buscarNumeroDOIEmp(String numeroDOI,
			String usuario) throws ParametroIlegalException,
			ConexionServicioException, ErrorObteniendoDatosException {
		LOG.info("buscarNumeroDOI (String numeroDOI)");
		if (numeroDOI.length() != ConstantesBusiness.CARACTERES_NUMERO_DOI) {		
			LOG.info("No tiene la cantidad de caracteres requridas : {}", 
					ConstantesBusiness.CARACTERES_NUMERO_DOI);
			throw new ParametroIlegalException ();
		}
		try {
			List<ClientePJCore> listaDCCore = coreDAO.buscarClientePJ(null, numeroDOI, usuario);
			// corregir en caso la lista tiene valores del servicio repetidos
			obtenerDatosSFP(listaDCCore);
			
			return listaDCCore;
		} catch (ConnectException e) {
			throw new ConexionServicioException (e, ConexionServicioException.Tipo.CORE);
		}
	}

	@Override
	public DatosClientePJCore obtenerDatosClientePJ(String codigoCentral,
			String usuario) throws ConexionServicioException,
			ErrorObteniendoDatosException {
		LOG.info("obtenerDatosClientePJ (ClientePJCore clientePJCore)");
		try {
			DatosClientePJCore datos = coreDAO.datosClientePJ(codigoCentral, usuario);
			List<CuentaCore> cuentas = datos.getCuentas();
			boolean todasInactivas = true;
			for (CuentaCore cta : cuentas) {
				if (ConstantesBusiness.ESTADO_CUENTA_ACTIVO.equals(cta.getSituacionCuenta())) {
					todasInactivas = false;
				}
			}
			LOG.info("Todas las cuentas son inactivas: {}", todasInactivas);
			if (todasInactivas) {
				throw new ErrorObteniendoDatosException ("El cliente no tiene cuentas activas.");
			}
			return datos;
		} catch (ConnectException e) {
			throw new ConexionServicioException (e, ConexionServicioException.Tipo.CORE);
		}
	}

	@Override
	public Cliente getDatosCliente(int codCliente) {
		LOG.info("getDatosCliente(int codCliente)");
		Cliente cliente = clienteDAO.load(codCliente);
		return cliente;
	}

	@Override
	public List<CuentaCore> listaCuentasActivas(ClientePJCore clientePJ) {
		LOG.info("listaCuentasActivas (ClientePJCore clientePJ)");
		List<CuentaCore> cuentas = clientePJ.getDatosCore().getCuentas();
		ArrayList<CuentaCore> lista = new ArrayList<CuentaCore> ();
		
		agregarCuentaActivoLista(cuentas, ConstantesBusiness.CODIGO_TIPO_CUENTA_CORRIENTE, true, lista);
		agregarCuentaActivoLista(cuentas, ConstantesBusiness.CODIGO_TIPO_CUENTA_AHORROS, true, lista);
		agregarCuentaActivoLista(cuentas, ConstantesBusiness.CODIGO_TIPO_CUENTA_PLAZO, true, lista);
		
		agregarCuentaActivoLista(cuentas, ConstantesBusiness.CODIGO_TIPO_CUENTA_CORRIENTE, false, lista);
		agregarCuentaActivoLista(cuentas, ConstantesBusiness.CODIGO_TIPO_CUENTA_AHORROS, false, lista);
		agregarCuentaActivoLista(cuentas, ConstantesBusiness.CODIGO_TIPO_CUENTA_PLAZO, false, lista);
		
		LOG.info("Cuentas Activas : {}", lista.size());
		return lista;
	}

	@Override
	public String exoneradoNuevoBastanteo(ClientePJCore clientePJ,
			Cuenta cuenta) {
		LOG.info("validarNuevoBastanteo (ClientePJCore clientePJ, CuentaCore cuenta)");
		String codSubProd = cuenta.getSubproductoCod();
		if (ConstantesBusiness.CODIGO_SUBPRODUCTO_NEGOCIO_PJ_PEN.equals(codSubProd) ||
				ConstantesBusiness.CODIGO_SUBPRODUCTO_NEGOCIO_PJ_USD.equals(codSubProd) ||
				ConstantesBusiness.CODIGO_SUBPRODUCTO_NEGOCIO_PNN_PEN.equals(codSubProd) ||
				ConstantesBusiness.CODIGO_SUBPRODUCTO_E_EMPRESARIO_PJ_PEN.equals(codSubProd) ||
				ConstantesBusiness.CODIGO_SUBPRODUCTO_E_EMPRESARIO_PJ_USD.equals(codSubProd)) {
			LOG.info("exonerado");
			return ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION;
		} else {
			ListaCerrada lista = listaCerradaDAO.findByCodigoCentral(clientePJ.getCodigoCentral());
			if (lista!=null && ConstantesBusiness.LISTA_CERRADA_ACTIVA.equals(lista.getFlagActivo())){
				LOG.info("exonerado");
				return ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION;
			}
		}
		LOG.info("no exonerado");
		return ConstantesBusiness.FLAG_COBRO_COMISION;
	}

	@Override
	public List<Operacion> listaOperaciones(ClientePJCore clientePJ)
			throws ValidacionOperacionException {
		LOG.info("listaOperaciones (ClientePJCore clientePJ)");
		int valHost1 = cumpleValidacionHost1(clientePJ.getDatosCore());
		int valHost2 = cumpleValidacionHost2(clientePJ.getDatosCore());
		if (valHost1 != ConstantesBusiness.CUMPLE_VALIDACION_HOST &&
				valHost2 != ConstantesBusiness.CUMPLE_VALIDACION_HOST) {
			throw new ValidacionOperacionException (ValidacionOperacionException.Tipo.CORE);
		}
		LOG.info("Validacion (Host) : valHost1={} - valHost2={}", new Integer[]{valHost1, valHost2});
		int valSFP1 = cumpleValidacionSFP1(clientePJ.getDatosSFP());
		int valSFP2 = cumpleValidacionSFP2(clientePJ.getDatosSFP());
		int valSFP3 = cumpleValidacionSFP3(clientePJ.getDatosSFP());
		if (valSFP1 != ConstantesBusiness.CUMPLE_VALIDACION_SFP &&
				valSFP2 != ConstantesBusiness.CUMPLE_VALIDACION_SFP &&
				valSFP3 != ConstantesBusiness.CUMPLE_VALIDACION_SFP) {
			throw new ValidacionOperacionException (ValidacionOperacionException.Tipo.SFP);
		}
		LOG.info("Validacion (SFP) : valSFP1={} - valSFP2={} - valSFP3={}", new Integer[]{valSFP1, valSFP2, valSFP3});
		
		ArrayList<String> codOpers = new ArrayList<String> (6);
		if (nuevoBastanteo(clientePJ) && valHost1 == ConstantesBusiness.CUMPLE_VALIDACION_HOST
				&& valSFP1 == ConstantesBusiness.CUMPLE_VALIDACION_SFP) {
			LOG.info("item : nuevoBastanteo");
			codOpers.add(ConstantesBusiness.CODIGO_NUEVO_BASTANTEO);
		}
		//if (modificatoriaBastanteo(clientePJ) && valHost1 == ConstantesBusiness.CUMPLE_VALIDACION_HOST
		if (valHost1 == ConstantesBusiness.CUMPLE_VALIDACION_HOST && valSFP2 == ConstantesBusiness.CUMPLE_VALIDACION_SFP) {
			LOG.info("item : modificatoriaBastanteo");
			codOpers.add(ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO);
		}
		/* No se considera porque en la especificacion del caso de uso cero (CU_CCPJ_C000)
		   indica que no sera parte de la implementacion
		if (consultaBastanteo(clientePJ) && valHost2 == ConstantesBusiness.CUMPLE_VALIDACION_HOST
				&& valSFP2 == ConstantesBusiness.CUMPLE_VALIDACION_SPF) {
			LOG.info("item : consultaBastanteo");
			codOpers.add(ConstantesBusiness.CODIGO_CONSULTA_BASTANTEO);
		}*/
		if (cambioFirmas(clientePJ) && valHost2 == ConstantesBusiness.CUMPLE_VALIDACION_HOST
				&& valSFP2 == ConstantesBusiness.CUMPLE_VALIDACION_SFP) {
			LOG.info("item : cambioFirmas");
			codOpers.add(ConstantesBusiness.CODIGO_CAMBIO_FIRMAS);
		}
		/* Se elimina la opción de Subsanación de Bastanteo desde el registrar expediente,
		 * ya que en todos los casos, será generada a través de un pre-registro.
		if (subsanacionBastanteo(clientePJ) && valHost1 == ConstantesBusiness.CUMPLE_VALIDACION_HOST
				&& valSFP3 == ConstantesBusiness.CUMPLE_VALIDACION_SFP) {
			LOG.info("item : subsanacionBastanteo");
			codOpers.add(ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO);
		}*/
		if (revocatoriaEspecifica(clientePJ) && valHost2 == ConstantesBusiness.CUMPLE_VALIDACION_HOST
				&& valSFP2 == ConstantesBusiness.CUMPLE_VALIDACION_SFP) {
			LOG.info("item : revocatoriaEspecífica");
			codOpers.add(ConstantesBusiness.CODIGO_REVOCATORIA_ESPECIFICA);
		}
		if (codOpers.size()==0) {
			LOG.info("codOpers Vacio");
			codOpers.add(ConstantesBusiness.CODIGO_REVOCATORIA_ESPECIFICA);
			codOpers.add(ConstantesBusiness.CODIGO_CAMBIO_FIRMAS);
		}
		
		List<Operacion> operaciones = operacionDAO.getOperaciones(codOpers);
		return operaciones;
	}

	@Override
	public boolean esValidoExpediente(ClientePJCore clientePJ) {
		LOG.info("validarExpediente (ClientePJCore clientePJ)");
		Expediente expediente = expedienteDAO.getExpediente(clientePJ.getCodigoCentral(), 
				ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO, ConstantesBusiness.CODIGO_NUEVO_BASTANTEO);
		return expediente == null;
	}

	@Override
	public boolean anteriorEsRevocatoria(String codCentral) {
		LOG.info("anteriorEsRevocatoria (String codCentral)");
		List<Expediente> listaReadOnly = expedienteDAO.findByCodigoCentral(codCentral);
		List<Expediente> lista = new ArrayList<Expediente>();
		lista.addAll(listaReadOnly);
		Collections.sort(lista, new Comparator<Expediente>(){
			public int compare(Expediente o1, Expediente o2) {
				return o2.getId().compareTo(o1.getId());
			}
		});
		for (Expediente exp : lista) {
			String operacion = exp.getOperacion().getCodigoOperacion();
			LOG.info("exp.getOperacion().getCodigoOperacion():"+operacion);
			if (ConstantesBusiness.CODIGO_REVOCATORIA_ESPECIFICA.equals(operacion)){
				return true;
			}
		}
		return false;
	}

	@Override
	public Expediente obternerUltimoBastanteo(String codCentral) {
		LOG.info("obternerUltimoBastanteo (String codCentral)");
		List<Expediente> listaReadOnly = expedienteDAO.findByCodigoCentral(codCentral);
		List<Expediente> lista = new ArrayList<Expediente>();
		lista.addAll(listaReadOnly);
		Collections.sort(lista, new Comparator<Expediente>(){
			public int compare(Expediente o1, Expediente o2) {
				return o2.getId().compareTo(o1.getId());
			}
		});
		LOG.info("lista: " + lista.size());
		Expediente expediente = null;
		for (Expediente exp : lista) {
			int estUltimExp = exp.getEstado().getId();
			String operacion = exp.getOperacion().getCodigoOperacion();
			String resultado = exp.getResultado();
			LOG.info("operacion: " + operacion);
			LOG.info("resultado: " + resultado);
			if ((ConstantesBusiness.CODIGO_NUEVO_BASTANTEO.equals(operacion) || 
					ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(operacion)) && 
					ConstantesBusiness.ID_ESTADO_EXPEDIENTE_TERMINADO == estUltimExp){
				if (ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO_PARCIAL.equals(resultado) || 
						ConstantesBusiness.ACCION_EXPEDIENTE_OBSERVADO.equals(resultado)) { 
					expediente = exp;
					break;
				}
				break;
			} else if (ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(operacion)) {
				if (ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO.equals(resultado)) {
					break;
				}
				if (ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO_PARCIAL.equals(resultado) || 
						ConstantesBusiness.ACCION_EXPEDIENTE_OBSERVADO.equals(resultado)) { 
					expediente = exp;
					break;
				}
			}
		}
		return expediente;
	}
	
	private void obtenerDatosSFP(List<ClientePJCore> listaDCCore) throws ConexionServicioException {
		LOG.info("obtenerDatosSFP(List<DatosClientePJCore> listaDCCore)");
		try {
			for (ClientePJCore dcCore : listaDCCore) {
				DatosClientePJSFP dcSFP = sfpDAO.consultaClientePJ(dcCore.getCodigoCentral(), dcCore.getTipoDOI(), 
						dcCore.getNumeroDOI());
				dcCore.setDatosSFP(dcSFP);
			}
		} catch (ConnectException e) {
			throw new ConexionServicioException (e, ConexionServicioException.Tipo.SPF);
		}
	}
	
	private void agregarCuentaActivoLista(List<CuentaCore> cuentas,
			String codCuenta, boolean monedaNacional,
			ArrayList<CuentaCore> lista) {
		LOG.info("agregarCuentaActivoLista ({}, {}, {} , {})", new Object[]{cuentas, codCuenta, monedaNacional, lista});
		for (CuentaCore cc : cuentas) {
			if (codCuenta.equalsIgnoreCase(cc.getCodigoProducto())
					&& ConstantesBusiness.ESTADO_CUENTA_ACTIVO.equalsIgnoreCase(cc.getSituacionCuenta())
					&& (monedaNacional ? esMonedaNacional(cc.getMonedaCodigo()) : 
						esMonedaExtranjera(cc.getMonedaCodigo()))) {
				lista.add(cc);
				LOG.info("Cu : "+codCuenta+" - Mon : "+cc.getMonedaCodigo()+" - NC : "+cc.getNumeroContrato());
			}
		}
	}
	
	private boolean esMonedaNacional (String codMoneda) {
		LOG.info("esMonedaNacional ({})", codMoneda);
		return ConstantesBusiness.CODIGO_MONEDA_NACIONAL.equalsIgnoreCase(codMoneda);
	}
	
	private boolean esMonedaExtranjera (String codMoneda) {
		LOG.info("esMonedaExtranjera ({})", codMoneda);
		return !ConstantesBusiness.CODIGO_MONEDA_NACIONAL.equalsIgnoreCase(codMoneda);
	}
	
	private boolean nuevoBastanteo (ClientePJCore clientePJ) {
		LOG.info("nuevoBastanteo (ClientePJCore clientePJ)");
		List<Integer> listEst = new ArrayList<Integer> (2);
		listEst.add(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO);
		listEst.add(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_CANCELADO);
		List<Expediente> lista = expedienteDAO.findByCodigoCentralSinEstado(clientePJ.getCodigoCentral(), 
				listEst);
		return lista.isEmpty();
	}
	
	private boolean modificatoriaBastanteo (ClientePJCore clientePJ) {
		LOG.info("modificatoriaBastanteo (ClientePJCore clientePJ)");
		List<Expediente> lista = expedienteDAO.findByCodigoCentralConEstado(clientePJ.getCodigoCentral(),ConstantesBusiness.ID_ESTADO_EXPEDIENTE_TERMINADO);
		return !lista.isEmpty();
	}
	
	// no eliminar
	private boolean consultaBastanteo (ClientePJCore clientePJ) {
		LOG.info("consultaBastanteo (ClientePJCore clientePJ)");
		List<Expediente> lista = expedienteDAO.findByCodigoCentral(clientePJ.getCodigoCentral());
		boolean tieneExpTerm = false;
		for (Expediente exp : lista) {
			if (ConstantesBusiness.ID_ESTADO_EXPEDIENTE_TERMINADO == exp.getEstado().getId().intValue()){
				tieneExpTerm = true;
			}
		}
		return tieneExpTerm;
	}
	
	private boolean cambioFirmas (ClientePJCore clientePJ) {
		LOG.info("cambioFirmas (ClientePJCore clientePJ)");
		List<Expediente> lista = expedienteDAO.findByCodigoCentral(clientePJ.getCodigoCentral());
		return !lista.isEmpty();
	}
	
	private boolean subsanacionBastanteo (ClientePJCore clientePJ) {
		LOG.info("subsanacionBastanteo (ClientePJCore clientePJ)");
		Expediente expediente = obternerUltimoBastanteo (clientePJ.getCodigoCentral());
		boolean ultimAprobObserv = false;
		if (expediente != null) {
			ultimAprobObserv = true;
		}
		LOG.info("ultimAprobObserv: " + ultimAprobObserv);
		return ultimAprobObserv;
	}
	
	private boolean revocatoriaEspecifica (ClientePJCore clientePJ) {
		LOG.info("revocatoriaEspecifica (ClientePJCore clientePJ)");
		return true;
	}
	
	/** para Nuevo Bastanteo, Modificatorias, Subsanación de Bastanteo
	 */
	private int cumpleValidacionHost1 (DatosClientePJCore datosClientePJ) {
		LOG.info("cumpleValidacionHost1 (DatosClientePJCore datosClientePJ)");
		if (existeCore(datosClientePJ) && tieneActivaPorLoMenosUnaCuenta(datosClientePJ)) {
			return ConstantesBusiness.CUMPLE_VALIDACION_HOST;
		}
		return ConstantesBusiness.NO_CUMPLE_VALIDACION_HOST;
	}
	
	/** para Cambio de firmas, Consulta, Revocatoria
	 */
	private int cumpleValidacionHost2 (DatosClientePJCore datosClientePJ) {
		LOG.info("cumpleValidacionHost2 (DatosClientePJCore datosClientePJ)");
		if (existeCore(datosClientePJ)) {
			return ConstantesBusiness.CUMPLE_VALIDACION_HOST;
		}
		return ConstantesBusiness.NO_CUMPLE_VALIDACION_HOST;
	}
	
	/** para Nuevo Bastanteo, Modificatorias, Subsanación de Bastanteo, Cambio de firmas,
	 *  Consulta, Revocatoria
	 */
	private boolean existeCore (DatosClientePJCore datosClientePJ) {
		LOG.info("existeCore (DatosClientePJCore datosClientePJ)");
		
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("datosClientePJ!=null : "+datosClientePJ!=null);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("datosClientePJ.getCodigoCentral()!=null : "+datosClientePJ.getCodigoCentral()!=null);
		
		boolean existe = datosClientePJ!=null && datosClientePJ.getCodigoCentral()!=null;
		LOG.info("existe : {}", existe);
		return existe;
	}
	
	/** para Nuevo Bastanteo, Modificatorias o Subsanación de Bastanteo
	 */
	private boolean tieneActivaPorLoMenosUnaCuenta (DatosClientePJCore datosClientePJ) {
		LOG.info("tieneActivaPorLoMenosUnaCuenta (DatosClientePJCore datosClientePJ)");
		boolean tieneActiva = false;
		List<CuentaCore> cuentas = datosClientePJ.getCuentas();
		if (cuentas!=null && !cuentas.isEmpty()) {
			for (CuentaCore cuenta : cuentas) {
				if (ConstantesBusiness.ESTADO_CUENTA_ACTIVO.equals(cuenta.getSituacionCuenta())) {
					tieneActiva = true;
				}
			}
		}
		LOG.info("tiene activa por lo menos una cuenta : {}", tieneActiva);
		return tieneActiva;
	}
	
	/** para Nuevo Bastanteo
	 */
	private int cumpleValidacionSFP1 (DatosClientePJSFP datosClientePJ) {
		LOG.info("cumpleValidacionSFP1 (DatosClientePJSFP datosClientePJ)");
		if (!existeSPF(datosClientePJ) || !estaActivo(datosClientePJ)) {
			return ConstantesBusiness.CUMPLE_VALIDACION_SFP;
		}
		return ConstantesBusiness.NO_CUMPLE_VALIDACION_SFP;
	}
	
	/** para Modificatorias, Cambio de firmas, Consulta, Revocatoria
	 */
	private int cumpleValidacionSFP2 (DatosClientePJSFP datosClientePJ) {
		LOG.info("cumpleValidacionSFP2 (DatosClientePJSFP datosClientePJ)");
		if (existeSPF(datosClientePJ) && estaActivo(datosClientePJ)) {
			return ConstantesBusiness.CUMPLE_VALIDACION_SFP;
		}
		return ConstantesBusiness.NO_CUMPLE_VALIDACION_SFP;
	}
	
	/** para Subsanación de Bastanteo
	 */
	private int cumpleValidacionSFP3 (DatosClientePJSFP datosClientePJ) {
		LOG.info("cumpleValidacionSFP3 (DatosClientePJSFP datosClientePJ)");
		if (existeSPF(datosClientePJ)) {
			return ConstantesBusiness.CUMPLE_VALIDACION_SFP;
		}
		return ConstantesBusiness.NO_CUMPLE_VALIDACION_SFP;
	}
	
	// para Nuevo Bastanteo, Modificatorias, Cambio de firmas, Consulta, Revocatoria
	private boolean existeSPF (DatosClientePJSFP datosClientePJ) {
		LOG.info("existeSPF (DatosClientePJSFP datosClientePJ)");
		boolean existe = datosClientePJ!=null;
		LOG.info("existe : {}", existe);
		return existe;
	}
	
	// para Nuevo Bastanteo, Modificatorias, Cambio de firmas, Consulta, Revocatoria
	private boolean estaActivo (DatosClientePJSFP datosClientePJ) {
		LOG.info("estaActivo (DatosClientePJSFP datosClientePJ)");
		String estado = null;
		LOG.info("datosClientePJ : {}", datosClientePJ);
		if (datosClientePJ!=null){
			LOG.info("datosClientePJ.getEstadoPJ() : {}", datosClientePJ.getEstadoPJ());
			if (datosClientePJ.getEstadoPJ()!=null) {
				estado = datosClientePJ.getEstadoPJ();
				LOG.info("***** estado servicio");
				for (char chr : estado.toCharArray()) {
					LOG.info("***** ---- char : {}", chr);
				}
				LOG.info("***** estado constante");
				for (char chr : ConstantesBusiness.ESTADO_CLIENTE_SFP_ACTIVO.toCharArray()) {
					LOG.info("***** ---- char : {}", chr);
				}
			} else if (datosClientePJ.getDescEstadoPJ() != null){
				String descEst = datosClientePJ.getDescEstadoPJ().toUpperCase();
				if (ConstantesBusiness.ESTADO_CLIENTE_SFP_DESC_ACTIVO.equals(descEst)) {
					estado = ConstantesBusiness.ESTADO_CLIENTE_SFP_ACTIVO;
				}
			}
		}
		boolean activo = datosClientePJ!=null && ConstantesBusiness.ESTADO_CLIENTE_SFP_ACTIVO.equals(estado);
		
		LOG.info("activo : {}, codigo : {}", activo, estado);
		return activo;
	}

}

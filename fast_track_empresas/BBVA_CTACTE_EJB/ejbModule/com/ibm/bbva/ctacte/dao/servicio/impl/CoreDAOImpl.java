package com.ibm.bbva.ctacte.dao.servicio.impl;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.grupobbva.accpj.ACCPJ_ServiciosWeb_PortProxy;
import pe.com.grupobbva.accpj.pey4.CtBodyRq;
import pe.com.grupobbva.accpj.pey4.CtBodyRs;
import pe.com.grupobbva.accpj.pey4.CtConsultarRelacionesRq;
import pe.com.grupobbva.accpj.pey4.CtConsultarRelacionesRs;
import pe.com.grupobbva.accpj.pey4.CtCuenta;
import pe.com.grupobbva.accpj.pey4.CtHeaderRs;
import pe.com.grupobbva.accpj.pey4.CtParticipe;
import pe.com.grupobbva.accpj.pj.CtBuscarPersonaJuridicaRq;
import pe.com.grupobbva.accpj.pj.CtBuscarPersonaJuridicaRs;
import pe.com.grupobbva.accpj.pj.CtHeader;
import pe.com.grupobbva.accpj.pj.CtPJ;

import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.CuentaCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.ParticipeCore;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.dao.servicio.CoreDAO;
import com.ibm.bbva.ctacte.dao.servicio.TempServ;
import com.ibm.bbva.ctacte.exepciones.ErrorObteniendoDatosException;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

/**
 * Session Bean implementation class CoreDAOImpl
 */
@Stateless
@Local(CoreDAO.class)
public class CoreDAOImpl implements CoreDAO {
	
	private static final String CODIGO_ERROR = "9999";
	private static final Logger LOG = LoggerFactory.getLogger(CoreDAOImpl.class);
	
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat dateFormatEx;

    /**
     * Default constructor. 
     */
    public CoreDAOImpl() {
    	dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormatEx = new SimpleDateFormat("dd/MM/yyyy");
    }
    
    private ACCPJ_ServiciosWeb_PortProxy getPortProxy() {
    	ACCPJ_ServiciosWeb_PortProxy ser = new ACCPJ_ServiciosWeb_PortProxy ();
		//http://118.180.36.26:7802/accpj/servicios/
		Properties properties = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF);
		String url = properties.getProperty(ConstantesParametros.SERVICIO_CORE);
		ser._getDescriptor().setEndpoint(url);
		return ser;
	}

	@Override
	public List<ClientePJCore> buscarClientePJ(String codigoCentral,
			String numeroDOI, String usuario) throws ConnectException,
			ErrorObteniendoDatosException {
		LOG.info("buscarClientePJ ({}, {}, {})", new Object[]{codigoCentral, numeroDOI, usuario});
		try {
			ACCPJ_ServiciosWeb_PortProxy port = getPortProxy ();
			CtBuscarPersonaJuridicaRq request = new CtBuscarPersonaJuridicaRq ();
			CtHeader header = new CtHeader ();
			header.setUsuario(usuario);
			request.setHeader(header);
			pe.com.grupobbva.accpj.pj.CtBodyRq data = new pe.com.grupobbva.accpj.pj.CtBodyRq();
			if (numeroDOI != null) {
				data.setNumDocumento(numeroDOI);
			} else {
				data.setCodigoCentral(codigoCentral);
			}
			request.setHeader(header);
			request.setData(data);
			CtBuscarPersonaJuridicaRs response = port.buscarPersonaJuridica(request);
			
			pe.com.grupobbva.accpj.pj.CtHeaderRs headerRs = response.getHeader();
			LOG.info("Codigo cabecera: {}", headerRs.getCodigo());
			if (CODIGO_ERROR.equals(headerRs.getCodigo())) {
				throw new ErrorObteniendoDatosException (headerRs.getDescripcion());
			}
			
			return obtenerListaPJ (response.getData().getPersonaJuridica());
		} catch (ErrorObteniendoDatosException e) {
			throw e;
		} catch (Exception e) {
			LOG.error("error en buscarClientePJ", e);
			throw new ConnectException ();
		}
		
//		List<ClientePJCore> lista = null;
//		if (numeroDOI != null) {
//			lista = TempServ.doiLike(numeroDOI);
//		} else {
//			lista = TempServ.ccEq(codigoCentral);
//		} 
//		return lista;
	}

	@Override
	public DatosClientePJCore datosClientePJ(String codigoCentral,
			String usuario) throws ConnectException,
			ErrorObteniendoDatosException {
		LOG.info("datos ClientePJ ({}, {})", codigoCentral, usuario); 
		try {
			ACCPJ_ServiciosWeb_PortProxy port = getPortProxy ();
			CtBuscarPersonaJuridicaRq request = new CtBuscarPersonaJuridicaRq ();
			CtHeader header = new CtHeader ();
			header.setUsuario(usuario);
			request.setHeader(header);
			pe.com.grupobbva.accpj.pj.CtBodyRq data = new pe.com.grupobbva.accpj.pj.CtBodyRq();
			data.setCodigoCentral(codigoCentral);
			request.setHeader(header);
			request.setData(data);
			CtBuscarPersonaJuridicaRs response = port.buscarPersonaJuridica(request);
			
			pe.com.grupobbva.accpj.pj.CtHeaderRs headerRs = response.getHeader();
			LOG.info("Codigo cabecera: {}", headerRs.getCodigo());
			if (CODIGO_ERROR.equals(headerRs.getCodigo())) {
				throw new ErrorObteniendoDatosException (headerRs.getDescripcion());
			}
			
			return obtenerClientePJCore (response.getData().getPersonaJuridica().get(0), usuario);
		} catch (ErrorObteniendoDatosException e) {
			throw e;
		} catch (Exception e) {
			LOG.error("error en datosClientePJ", e);
			throw new ConnectException ();
		}
		
//		DatosClientePJCore dc = TempServ.datosCliente(codigoCentral);
//		return dc;
	}
	
	private List<ClientePJCore> obtenerListaPJ (List<CtPJ> listaPJ) {
		ArrayList<ClientePJCore> lista = new ArrayList<ClientePJCore> ();
		if (listaPJ != null) {
			for (CtPJ ctPJ : listaPJ) {
				ClientePJCore pjCore = new ClientePJCore ();
				pjCore.setCodigoCentral(ctPJ.getCodigoCentral());
				pjCore.setDescripcionDOI(ctPJ.getDescTipoDOI());
				LOG.info("obtenerListaPJ: ctPJ.getFechaConstitucion(): " + ctPJ.getFechaConstitucion());
				try {
					LOG.info("linea 1");
					pjCore.setFechaConstitucion(dateFormat.parse(ctPJ.getFechaConstitucion()));
					LOG.info("pjCore.setFechaConstitucion : " + pjCore.getFechaConstitucion());
				} catch (Exception e) {
					try {
						pjCore.setFechaConstitucion(dateFormatEx.parse(ctPJ.getFechaConstitucion()));
					} catch (Exception ex) {
						pjCore.setFechaConstitucion(new Date (0));
					}
				}
				pjCore.setNumeroDOI(ctPJ.getNumDOI());
				pjCore.setRazonSocial(ctPJ.getNombreRazonSocial());
				pjCore.setTipoDOI(ctPJ.getTipoDOI());
				lista.add(pjCore);
			}
		}
		return lista;
	}
	
	private DatosClientePJCore obtenerClientePJCore (CtPJ ctPJ, String usuario) throws ConnectException, 
			ErrorObteniendoDatosException {
		DatosClientePJCore clientePJCore = new DatosClientePJCore ();
		clientePJCore.setCodigoActEconomica(ctPJ.getCodActEconomica());
		clientePJCore.setCodigoCentral(ctPJ.getCodigoCentral());
		clientePJCore.setCodigoDepartamento(ctPJ.getCodDepartamento());
		clientePJCore.setCodigoDistrito(ctPJ.getCodDistrito());
		clientePJCore.setCodigoProvincia(ctPJ.getCodProvincia());
		clientePJCore.setCorreoElectronico1(ctPJ.getEmail1());
		clientePJCore.setCorreoElectronico2(ctPJ.getEmail2());
		clientePJCore.setCuentas(obtenerCuentas(ctPJ.getCodigoCentral(), usuario));
		clientePJCore.setDescActEconomica(ctPJ.getDescActEconomica());
		clientePJCore.setDescDepartamento(ctPJ.getDescDepartamento());
		clientePJCore.setDescDistrito(ctPJ.getDescDistrito());
		clientePJCore.setDescProvincia(ctPJ.getDescProvincia());
		clientePJCore.setDescTipoDOI(ctPJ.getDescTipoDOI());
		clientePJCore.setDireccion(ctPJ.getDireccion());
		LOG.info("ctPJ.getFechaConstitucion(): " + ctPJ.getFechaConstitucion());
		clientePJCore.setFechaConstitucion(ctPJ.getFechaConstitucion());
		clientePJCore.setNombreRazonSocial(ctPJ.getNombreRazonSocial());
		clientePJCore.setNroCelular1(ctPJ.getNumCelular1());
		clientePJCore.setNroCelular2(ctPJ.getNumCelular2());
		clientePJCore.setNumeroDOI(ctPJ.getNumDOI());
		clientePJCore.setSectorCodigo(ctPJ.getCodSector());
		clientePJCore.setSectorDescripcion(ctPJ.getDescSector());
		clientePJCore.setTipoDOI(ctPJ.getTipoDOI());
		//clientePJCore.setUbicacion(ctPJ.get);
		return clientePJCore;
	}
	
	private List<CuentaCore> obtenerCuentas(String codigoCentral, String usuario) throws ConnectException, 
			ErrorObteniendoDatosException {
		LOG.info("obtenerCuentas({}, {})", codigoCentral, usuario);
		try {
			ACCPJ_ServiciosWeb_PortProxy port = getPortProxy ();
			CtConsultarRelacionesRq request = new CtConsultarRelacionesRq ();
			pe.com.grupobbva.accpj.pey4.CtHeader header = new pe.com.grupobbva.accpj.pey4.CtHeader ();
			header.setUsuario(usuario);
			request.setHeader(header);
			CtBodyRq data = new CtBodyRq ();
			data.setCodigoCentral(codigoCentral);
			request.setData(data);
			CtConsultarRelacionesRs response = port.consultarRelacionesPaginado(request);
			CtBodyRs bodyRs = response.getData();
			
			CtHeaderRs headerRs = response.getHeader();
			LOG.info("Codigo cabecera: {}", headerRs.getCodigo());
			if (CODIGO_ERROR.equals(headerRs.getCodigo())) {
				throw new ErrorObteniendoDatosException (headerRs.getDescripcion());
			}
				
			ArrayList<CuentaCore> lista = new ArrayList<CuentaCore> ();
			if (bodyRs.getCuentas().getCuenta() != null) {
				LOG.error("bodyRs.getCuentas().getCuenta(): {}", bodyRs.getCuentas().getCuenta());
				for (CtCuenta cuenta : bodyRs.getCuentas().getCuenta()) {
					LOG.error("cuenta: {}", cuenta);
					CuentaCore cuentaCore = new CuentaCore ();
					LOG.error("cuenta.getCodProducto(): {}", cuenta.getCodProducto());
					cuentaCore.setCodigoProducto(cuenta.getCodProducto());
					cuentaCore.setCodigoSubProducto(cuenta.getCodSubProd());
					cuentaCore.setDescProducto(cuenta.getDescProducto());
					cuentaCore.setDescSubProducto(cuenta.getDescSubProd());
					try {
						cuentaCore.setFechCreacionCtaCte(dateFormat.parse(cuenta.getFechaCreacion()));
					} catch (Exception e) {
						try {
							cuentaCore.setFechCreacionCtaCte(dateFormatEx.parse(cuenta.getFechaCreacion()));
						} catch (Exception ex) {
							cuentaCore.setFechCreacionCtaCte(new Date (0));
						}
					}
					cuentaCore.setMonedaCodigo(cuenta.getCodMoneda());
					cuentaCore.setMonedaDescripcion(cuenta.getDescMoneda());
					cuentaCore.setNumeroContrato(cuenta.getNumContrato());
					cuentaCore.setParticipes(obtenerParticipes (cuenta.getParticipes().getParticipe()));
					cuentaCore.setSituacionCuenta(cuenta.getSituacion());
					lista.add(cuentaCore);
				}
			}
			return lista;
		} catch (ErrorObteniendoDatosException e) {
			throw e;
		} catch (Exception e) {
			LOG.error("error en obtenerCuentas", e);
			throw new ConnectException ();
		}
	}
	
	private List<ParticipeCore> obtenerParticipes(List<CtParticipe> ctParticipes) {
		ArrayList<ParticipeCore> lista = new ArrayList<ParticipeCore> ();
		if (ctParticipes != null) {
			for (CtParticipe participe : ctParticipes) {
				ParticipeCore participeCore = new ParticipeCore ();
				participeCore.setApellidoMaterno(participe.getApeMaterno());
				participeCore.setApellidoPaterno(participe.getApePaterno());
				participeCore.setCodigoCentral(participe.getCodigoCentral());
				participeCore.setCodigoDepartamento(participe.getCodDepartamento());
				participeCore.setCodigoDistrito(participe.getCodDistrito());
				participeCore.setCodigoProvincia(participe.getCodProvincia());
	//			participeCore.setCombinacion(participe.get);
				participeCore.setDescDepartamento(participe.getDescDepartamento());
				participeCore.setDescDistrito(participe.getDescDistrito());
				participeCore.setDescProvincia(participe.getDescProvincia());
				participeCore.setDescTipoDOI(participe.getDescTipoDOI());
				participeCore.setDireccion(participe.getDireccion());
				participeCore.setFechaSerialFirma(participe.getFechaSerializacionFirma());
				participeCore.setIndFirma(participe.getIndrFirmaSerializada());
	//			participeCore.setIndFirmaAsociada(participe.get);
	//			participeCore.setIndFirmanteCtaCte(indFirmanteCtaCte);
//				participeCore.setIndFirmaSerializada(participe.getIndrFirmaSerializada());
				participeCore.setNivelIntervencion(participe.getNivelIntervencion());
				participeCore.setNombres(participe.getNombres());
				participeCore.setNumeroDOI(participe.getNumDOI());
				participeCore.setSecIntervencion(participe.getSecIntervencion());
				participeCore.setTipoDOI(participe.getTipoDOI());
	//			participeCore.setUbicacion(participe.get);
				lista.add(participeCore);
			}
		}
		return lista;
	}

}

package com.ibm.bbva.ctacte.dao.servicio.impl;

import hiper.spring.beans.hfirmas.webservices.xsd.ClientePJBE;
import hiper.spring.beans.hfirmas.webservices.xsd.CuentaBE;
import hiper.spring.beans.hfirmas.webservices.xsd.RepresentanteBE;
import hiper.spring.beans.hfirmas.webservices.xsd.TipoPJBE;
import hiper.webservices.impl.hfirmas.WSServicioSFPHttpSoap11EndpointProxy;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.servicio.sfp.CuentaSFP;
import com.ibm.bbva.ctacte.bean.servicio.sfp.DatosClientePJSFP;
import com.ibm.bbva.ctacte.bean.servicio.sfp.RepresentanteSFP;
import com.ibm.bbva.ctacte.bean.servicio.sfp.TipoPJSFP;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.dao.servicio.SistemaFirmasPoderesDAO;
import com.ibm.bbva.ctacte.dao.servicio.TempServ;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

/**
 * Session Bean implementation class SistemaFirmasPoderesDAOImpl
 */
@Stateless
@Local(SistemaFirmasPoderesDAO.class)
public class SistemaFirmasPoderesDAOImpl implements SistemaFirmasPoderesDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(SistemaFirmasPoderesDAOImpl.class);

    /**
     * Default constructor. 
     */
    public SistemaFirmasPoderesDAOImpl() {
    }
    
    private WSServicioSFPHttpSoap11EndpointProxy getServicioSFP () {
		LOG.info("getServicioSFP ()");
		WSServicioSFPHttpSoap11EndpointProxy servicioSFP = new WSServicioSFPHttpSoap11EndpointProxy ();
		Properties properties = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF);
		// http://118.180.34.15:9080/SFPWS/services/WSServicioSFP.WSServicioSFPHttpSoap11Endpoint/
		String url = properties.getProperty(ConstantesParametros.SERVICIO_SFP);
		servicioSFP._getDescriptor().setEndpoint(url);
		return servicioSFP;
	}

	@Override
	public DatosClientePJSFP consultaClientePJ(String codigoCentral,
			String tipoDOI, String numeroDOI) throws ConnectException {
		LOG.info("consultaClientePJ ({}, {}, {})", new Object[]{codigoCentral, tipoDOI, numeroDOI});
		try {
			WSServicioSFPHttpSoap11EndpointProxy servicioSFP = getServicioSFP ();
			LOG.info("servicioSFP : "+servicioSFP);
			return crearDatosClientePJSFP (servicioSFP.obtenerPoderes(codigoCentral, null, null, null));
		} catch (Exception e) {
			LOG.error("error en consultaClientePJ", e);
			throw new ConnectException ();
		}
		
//		DatosClientePJSFP dcSFP = TempServ.datosCliente(codigoCentral, tipoDOI, numeroDOI);
//		return dcSFP;
	}

	@Override
	public List<TipoPJSFP> obtenerTiposPJ() {
		try {
			WSServicioSFPHttpSoap11EndpointProxy servicioSFP = getServicioSFP ();
			return crearTipoPJSFP (servicioSFP.obtenerTipoPJ("C", "", "", "A"));
		} catch (Exception e) {
			LOG.error("error en obtenerTiposPJ", e);
			return new ArrayList<TipoPJSFP>();
		}
		
//		return TempServ.obtenerTiposPJ();
	}
	
	private DatosClientePJSFP crearDatosClientePJSFP(ClientePJBE obtenerPoderes) {
		DatosClientePJSFP datosCliente = new DatosClientePJSFP ();
		datosCliente.setArticulo(obtenerPoderes.getArticulo());
		datosCliente.setCodigoCentral(obtenerPoderes.getCodigoCentral());
		datosCliente.setCuentasCorriente(crearCuentas(obtenerPoderes.getListaCuentas()));
		LOG.info("obtenerPoderes.getCodigo_central() : "+obtenerPoderes.getCodigoCentral());
		LOG.info("obtenerPoderes.getEstado() : "+obtenerPoderes.getEstado());
		LOG.info("obtenerPoderes.getEstado_desc() : "+obtenerPoderes.getEstadoDesc());
		datosCliente.setEstadoPJ(obtenerPoderes.getEstado());
		datosCliente.setDescEstadoPJ(obtenerPoderes.getEstadoDesc());
		datosCliente.setEstadoVersion(obtenerPoderes.getEstadoVersion());
		datosCliente.setEstructura(obtenerPoderes.getEstructura());
		datosCliente.setFechaEscritura(obtenerPoderes.getFechaEscritura());
		datosCliente.setFechaVersionBastanteo(obtenerPoderes.getFechaVersion());
		datosCliente.setJurisdiccion(obtenerPoderes.getJurisdiccion());
		datosCliente.setNotario(obtenerPoderes.getNotario());
		datosCliente.setNroExpediente(obtenerPoderes.getNroExpediente());
		datosCliente.setNroVersion(obtenerPoderes.getNroExpediente());
		datosCliente.setNumeroDOI(obtenerPoderes.getNumeroDOI());
		datosCliente.setObjeto(obtenerPoderes.getObjeto());
		datosCliente.setOficinaRegistral(obtenerPoderes.getOficinaRegistral());
		datosCliente.setPartida(obtenerPoderes.getPartida());
		datosCliente.setRazonSocial(obtenerPoderes.getRazonSocial());
		datosCliente.setRepresentantes(crearRepresentantes (obtenerPoderes.getListaRepresentantes()));
		datosCliente.setTipoDOI(obtenerPoderes.getTipoDOI());
		datosCliente.setTipoPJ(obtenerPoderes.getTipoPersona());
		datosCliente.setZonaRegistral(obtenerPoderes.getZonaRegistral());
		
		return datosCliente;
	}
	
	private List<CuentaSFP> crearCuentas(List<CuentaBE> listaCuentas) {
		ArrayList<CuentaSFP> lista = new ArrayList<CuentaSFP> ();
		if (listaCuentas != null) {
			for (CuentaBE cuentaBE : listaCuentas) {
				if (cuentaBE != null) {
					CuentaSFP cuenta = new CuentaSFP ();
					cuenta.setIndicadorPoder(cuentaBE.getIndicadorPoder());
					cuenta.setNumeroCuenta(cuentaBE.getNroCuenta());
					cuenta.setProducto(cuentaBE.getProducto());
					cuenta.setSituacion(cuentaBE.getSituacion());
					cuenta.setSubProducto(cuentaBE.getSubproducto());
					//cuenta.setPartícipes(partícipes)
					lista.add(cuenta);
				}
			}
		}
		return lista;
	}
	
	private List<RepresentanteSFP> crearRepresentantes(List<RepresentanteBE> listaRepresentantes) {
		ArrayList<RepresentanteSFP> lista  = new ArrayList<RepresentanteSFP> ();
		if (listaRepresentantes != null) {
			for (RepresentanteBE representanteBE : listaRepresentantes) {
				if (representanteBE != null) {
					RepresentanteSFP representante = new RepresentanteSFP ();
					representante.setApellidoMaterno(representanteBE.getApeMaterno());
					representante.setApellidoPaterno(representanteBE.getApePaterno());
					representante.setCargo(representanteBE.getCargo());
					representante.setCodigoCentral(representanteBE.getCodigoCentral());
					representante.setIndFirma(representanteBE.getIndicadorFirma());
					representante.setNombres(representanteBE.getNombres());
					representante.setNumeroDOI(representanteBE.getNumeroDOI());
					representante.setSituacion(representanteBE.getSituacion());
					representante.setTipoDOI(representanteBE.getTipoDOI());
					representante.setVigencia(representanteBE.getVigencia());
					lista.add(representante);
				}
			}
		}
		return lista;
	}
	
	private List<TipoPJSFP> crearTipoPJSFP(List<TipoPJBE> listaPJBE) {
		ArrayList<TipoPJSFP> tipos = new ArrayList<TipoPJSFP> ();
		if (listaPJBE != null) {
			for (TipoPJBE tipoPJBE : listaPJBE) {
				TipoPJSFP pjSFP = new TipoPJSFP ();
				pjSFP.setCodigo(tipoPJBE.getCodigo());
				pjSFP.setDescripcion(tipoPJBE.getDescripcion());
				tipos.add(pjSFP);
			}
		}
		return tipos;
	}

}

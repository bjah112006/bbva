package com.ibm.bbva.ctacte.servicio.sfp.util;

import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.servicio.sfp.bean.CuentaSTD;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMRealizarBastanteoINResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.ParticipeSTD;

public class SFPUtil {
	
	public IBMRealizarBastanteoINResponse copiarDatosBastanteo(Expediente expediente){
		IBMRealizarBastanteoINResponse ibmRealizarBastanteoINResponse = new IBMRealizarBastanteoINResponse();
		
		ibmRealizarBastanteoINResponse.setCodigoCentral(expediente.getCliente().getCodigoCentral());
		ibmRealizarBastanteoINResponse.setFechaConstitucion(expediente.getCliente().getFechaConstitucion());
		ibmRealizarBastanteoINResponse.setNumeroDoi(expediente.getCliente().getNumeroDoi());
		ibmRealizarBastanteoINResponse.setRazonSocial(expediente.getCliente().getRazonSocial());
		ibmRealizarBastanteoINResponse.setTipoDoi(expediente.getCliente().getTipoDoi());
		String operacion = expediente.getOperacion().getCodigoOperacion();
		if (ConstantesBusiness.OPERACION_REVOCATORIA_IBM_STR.equals(operacion)) {
			ibmRealizarBastanteoINResponse.setTipoOperacion(ConstantesBusiness.OPERACION_REVOCATORIA_SFP_STR);
		} else {
			ibmRealizarBastanteoINResponse.setTipoOperacion(operacion);
		}
		ibmRealizarBastanteoINResponse.setTipoPJ(expediente.getCodTipoPj());
		return ibmRealizarBastanteoINResponse;
	}
	
	public CuentaSTD copiarDatosCuentaSTD(Cuenta cuenta){
		
		CuentaSTD cuentaSTD = new CuentaSTD();
		cuentaSTD.setFechaCreacion(cuenta.getFechaCreacion());
		cuentaSTD.setMonedaCod(cuenta.getMonedaCod());
		cuentaSTD.setMonedaDes(cuenta.getMonedaDes());
		cuentaSTD.setNumeroContrato(cuenta.getNumeroContrato());
		cuentaSTD.setProductoCod(cuenta.getProductoCod());
		cuentaSTD.setProductoDes(cuenta.getProductoDes());
		cuentaSTD.setSituacionCuenta(cuenta.getSituacionCuenta());
		cuentaSTD.setSubproductoCod(cuenta.getSubproductoCod());
		cuentaSTD.setSubproductoDes(cuenta.getSubproductoDes());
		
		return cuentaSTD;
	}
	
	public ParticipeSTD copiarDatosParticipeSTD (Participe participe) {
		ParticipeSTD participeSTD = new ParticipeSTD();
		participeSTD.setApellidoMaterno(participe.getApellidoMaterno());
		participeSTD.setApellidoPaterno(participe.getApellidoPaterno());
		participeSTD.setCodigoCentral(participe.getCodigoCentral());
		participeSTD.setDepartamentoCod(participe.getDepartamentoCod());
		participeSTD.setDepartamentoDes(participe.getDepartamentoDes());
		participeSTD.setDireccion(participe.getDireccion());
		participeSTD.setDistritoCod(participe.getDistritoCod());
		participeSTD.setDistritoDes(participe.getDistritoDes());
		participeSTD.setFechaSerializacion(participe.getFechaSerializacion());
		participeSTD.setIndicadorFirma(participe.getIndicadorFirma());
		participeSTD.setNivelIntervencion(participe.getNivelIntervencion());
		participeSTD.setNombre(participe.getNombre());
		participeSTD.setNumeroDoi(participe.getNumeroDoi());
		participeSTD.setProvinciaCod(participe.getProvinciaCod());
		participeSTD.setProvinciaDes(participe.getProvinciaDes());
		participeSTD.setSecuenciaIntervencion(participe.getSecuenciaIntervencion());
		participeSTD.setTipoDoi(participe.getTipoDoi());
		participeSTD.setTipoDoiDes(participe.getTipoDoiDes());
		participeSTD.setUbicacion(participe.getUbicacion());
		
		return participeSTD;
	}
}

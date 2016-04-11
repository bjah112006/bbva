package com.ibm.bbva.ctacte.dao.servicio;

import java.util.ArrayList;
import java.util.List;

import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.sfp.DatosClientePJSFP;
import com.ibm.bbva.ctacte.bean.servicio.sfp.TipoPJSFP;

public class TempServ {

	private static Servidor servidor = new Servidor();
	
	public static List<ClientePJCore> doiLike(String numeroDOI) {
		return servidor.doiLike(numeroDOI);
	}

	public static List<ClientePJCore> ccEq(String codigoCentral) {
		return servidor.ccEq(codigoCentral);
	}

	public static DatosClientePJCore datosCliente(String codigoCentral) {
		return servidor.datosCliente(codigoCentral);
	}

	public static DatosClientePJSFP datosCliente(String codigoCentral,
			String tipoDOI, String numeroDOI) {
		return servidor.datosClienteSFP(codigoCentral);
	}

	public static List<TipoPJSFP> obtenerTiposPJ() {
		ArrayList<TipoPJSFP> lista = new ArrayList<TipoPJSFP>();
		TipoPJSFP tipoPJSFP = new TipoPJSFP();
		tipoPJSFP.setCodigo("04");
		tipoPJSFP.setDescripcion("PARTIDOS POLITICOS");
		lista.add(tipoPJSFP);
		tipoPJSFP = new TipoPJSFP();
		tipoPJSFP.setCodigo("02");
		tipoPJSFP.setDescripcion("PUBLICO");
		lista.add(tipoPJSFP);
		return lista;
	}

	
}

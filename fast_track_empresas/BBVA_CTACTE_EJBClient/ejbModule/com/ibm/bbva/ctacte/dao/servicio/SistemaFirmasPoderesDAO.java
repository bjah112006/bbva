package com.ibm.bbva.ctacte.dao.servicio;

import java.net.ConnectException;
import java.util.List;

import com.ibm.bbva.ctacte.bean.servicio.sfp.DatosClientePJSFP;
import com.ibm.bbva.ctacte.bean.servicio.sfp.TipoPJSFP;

public interface SistemaFirmasPoderesDAO {
	
	public DatosClientePJSFP consultaClientePJ (String codigoCentral, String tipoDOI, String numeroDOI) throws ConnectException;
	public List<TipoPJSFP> obtenerTiposPJ ();

}

package com.ibm.bbva.ctacte.dao.servicio;

import java.net.ConnectException;
import java.util.List;

import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.exepciones.ErrorObteniendoDatosException;

public interface CoreDAO {
	
	public List<ClientePJCore> buscarClientePJ (String codigoCentral, String numeroDOI, String usuario) throws ConnectException, ErrorObteniendoDatosException;
	public DatosClientePJCore datosClientePJ (String codigoCentral, String usuario) throws ConnectException, ErrorObteniendoDatosException;

}

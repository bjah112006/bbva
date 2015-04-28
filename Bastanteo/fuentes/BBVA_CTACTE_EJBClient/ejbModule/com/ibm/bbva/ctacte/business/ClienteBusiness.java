package com.ibm.bbva.ctacte.business;

import java.util.List;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.CuentaCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.exepciones.ConexionServicioException;
import com.ibm.bbva.ctacte.exepciones.ErrorObteniendoDatosException;
import com.ibm.bbva.ctacte.exepciones.ParametroIlegalException;
import com.ibm.bbva.ctacte.exepciones.ValidacionOperacionException;

public interface ClienteBusiness {
	
	public List<ClientePJCore>  buscarCodigoCentral (String codigoCentral, String usuario) throws ParametroIlegalException, ConexionServicioException, ErrorObteniendoDatosException;
	public List<ClientePJCore>  buscarNumeroDOI (String numeroDOI, String usuario) throws ParametroIlegalException, ConexionServicioException, ErrorObteniendoDatosException;
	public List<ClientePJCore>  buscarNumeroDOIEmp (String numeroDOI, String usuario) throws ParametroIlegalException, ConexionServicioException, ErrorObteniendoDatosException;
	public DatosClientePJCore obtenerDatosClientePJ (String codigoCentral, String usuario) throws ConexionServicioException, ErrorObteniendoDatosException;
	public Cliente getDatosCliente(int codCliente);
	public List<CuentaCore> listaCuentasActivas (ClientePJCore clientePJ);
	public String exoneradoNuevoBastanteo (ClientePJCore clientePJ, Cuenta cuenta);
	public List<Operacion> listaOperaciones (ClientePJCore clientePJ) throws ValidacionOperacionException;
	public boolean esValidoExpediente (ClientePJCore clientePJ);
	public boolean anteriorEsRevocatoria (String codCentral);
	public Expediente obternerUltimoBastanteo (String codCentral);
	
}

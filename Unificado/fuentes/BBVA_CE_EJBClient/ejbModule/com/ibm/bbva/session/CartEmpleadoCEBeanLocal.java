package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.CartEmpleadoCE;

public interface CartEmpleadoCEBeanLocal {
	//Emitson 
	
	
	//public List<CartEmpleadoCE> buscarPorIdCaract(long idCaract);
	public List<CartEmpleadoCE> buscarPorIdCaractIdPerfil(long idCaract, long idPerfil);

	public List<CartEmpleadoCE> buscarPorIdCaractIdEmpleado(long idCaract,
			long idEmpleado);

	public List<CartEmpleadoCE> buscarPorIdEmpleado(long idEmpleado);
	
	public List<CartEmpleadoCE> buscarPorIdEmpleadoActivo(long idEmpleado);
	
	public Long contarPorIdCaractIdEmpleado(long idCaract, long idEmpleado);

	public List<CartEmpleadoCE> verificarCartEmpleado(long idPerfil, long idOficina,
			long idProducto, long idEmpleado);
}

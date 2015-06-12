package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.VistaBandjCartProd;

public interface VistaBandjCartProdBeanLocal {

	public List<VistaBandjCartProd> verificarCartXProducto(long idPerfil,
			long idTerritorio, long idEmpleado);

	public List<VistaBandjCartProd> buscarPorPerfil_CartProd(long idPerfil,
			List<Long> listIdsProd);

	public List<VistaBandjCartProd> buscarPorPerfilEmpleadoActivo(long idPerfil,
			long idOficina, List<Long> listIdsProd);

}

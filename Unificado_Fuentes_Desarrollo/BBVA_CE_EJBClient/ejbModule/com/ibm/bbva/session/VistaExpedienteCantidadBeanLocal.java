package com.ibm.bbva.session;

import java.util.List;
import java.util.Map;

import com.ibm.bbva.entities.VistaExpedienteCantidad;

public interface VistaExpedienteCantidadBeanLocal {
		public List<VistaExpedienteCantidad> buscarPorIdProdIdTerrIdPer(long idProducto, long idTerritorio, long idPerfil);
		public List<VistaExpedienteCantidad> buscarPorIdProdIdPer(long idProducto, long idPerfil);
		public List<VistaExpedienteCantidad> buscarPorIdEmpleado(long idEmpleado);
		public VistaExpedienteCantidad cantidadExpPorIdEmpleado(long idEmpleado);
		public Map<Integer,Object> cantidadExpPorEmpleado();
		public List<VistaExpedienteCantidad> buscarPorIdProdIdTerrIdOfIdPer(
				long idProducto, long idTerritorio, long idOficina,
				long idPerfil);
}

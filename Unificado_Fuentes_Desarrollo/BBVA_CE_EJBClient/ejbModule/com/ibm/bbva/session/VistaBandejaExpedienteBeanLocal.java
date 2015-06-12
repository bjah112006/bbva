package com.ibm.bbva.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ibm.bbva.entities.VistaBandejaExpediente;
import com.ibm.bbva.entities.VistaExpedienteCantidad;

public interface VistaBandejaExpedienteBeanLocal {
		public List<VistaBandejaExpediente> buscarPorIdsExpedientes(
				List<Long> listIds, ArrayList arrayList);

		public List<VistaBandejaExpediente> buscarPorIdsExpedientes_BandjAsignacion(
				List<Long> listIds, ArrayList arrayList, List<Long> listIdsProd);
}

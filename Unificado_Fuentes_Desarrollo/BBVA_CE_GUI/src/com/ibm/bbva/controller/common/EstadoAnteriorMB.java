package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.tabla.dto.DatosDetalleHistoricoIiceDTO;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.util.vo.ConvertHistorial;

@SuppressWarnings("serial")
@ManagedBean(name = "estadoAnterior")
@RequestScoped
public class EstadoAnteriorMB extends AbstractMBean {
	
	@EJB
	private HistorialBeanLocal historialBean;

	private Historial historial;
	
	/*FIX ERIKA ABREGU 27/06/2015 */
	private TablaFacadeBean tablaFacadeBean = null;
	private List<DatosDetalleHistoricoIiceDTO> listDetalleHistoricoIICE;
	private List<Historial> lista = new ArrayList<Historial>();

	private static final Logger LOG = LoggerFactory.getLogger(EstadoAnteriorMB.class);
	
	public EstadoAnteriorMB() {
	}
	
	@PostConstruct
	public void obtenerDatos() {
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		
		if (expediente != null && expediente.getId() > 0) {
			/**FIX ERIKA ABREGU 05/07/2015
			 * ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
			 */
			if(Constantes.EXPEDIENTE_ANTIGUO.equals(expediente.getOrigen())){
				LOG.info("Metodo obtenerDatos de EstadoAnteriorMB Antiguo = "+expediente.getId());
				
				//preparando parametros
				ArrayList<Object> listaParametros = new ArrayList<Object>();
				listaParametros.add(new Long(expediente.getId()));
				
				if (this.tablaFacadeBean == null) {
					this.tablaFacadeBean = new TablaFacadeBean();
				}
				
				listDetalleHistoricoIICE = tablaFacadeBean.getGenerarDetalleHistorialIICE(listaParametros);
				if (listDetalleHistoricoIICE == null) {
					listDetalleHistoricoIICE = new ArrayList<DatosDetalleHistoricoIiceDTO> ();
				}
				lista =  ConvertHistorial.convertToDetalleHistorial(listDetalleHistoricoIICE);
			}else{
				/*Obtener datos del Historial*/
				lista = historialBean.buscarPorIdExpediente(expediente.getId());
			}
			/**FIX ERIKA ABREGU 05/07/2015
			 * FIN DE ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
			 */
		}
		
		if (!lista.isEmpty()) {			
			historial = lista.get(0);
			addObjectSession(Constantes.ID_EMPLEADO_ESTADO_ANTERIOR_SESION, new Long(historial.getEmpleado().getId()));
		}
	}
	
	public Historial getHistorial() {
		return historial;
	}

	public void setHistorial(Historial historial) {
		this.historial = historial;
	}
	
	public String getDescripcionPerfil(){
		if(historial!=null && this.historial.getPerfil()!=null){
				LOG.info("Perfil no es null");
				return this.historial.getPerfil().getDescripcion();
			}else{
				LOG.info("Perfil es null");
				return "";
			}
		
	}
	
}

package com.ibm.bbva.controller.common;

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

@SuppressWarnings("serial")
@ManagedBean(name = "estadoAnterior")
@RequestScoped
public class EstadoAnteriorMB extends AbstractMBean {
	
	@EJB
	private HistorialBeanLocal historialBean;

	private Historial historial;

	private static final Logger LOG = LoggerFactory.getLogger(EstadoAnteriorMB.class);
	
	public EstadoAnteriorMB() {
	}
	
	@PostConstruct
	public void obtenerDatos() {
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		
		/*Obtener datos del Historial*/
		List<Historial> lista = historialBean.buscarPorIdExpediente(expediente.getId());
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

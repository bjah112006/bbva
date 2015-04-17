package com.ibm.bbva.controller.common;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.session.ExpedienteBeanLocal;

@SuppressWarnings("serial")
@ManagedBean(name = "datosHistorico")
@RequestScoped
public class DatosHistoricoMB extends AbstractMBean {
	
	@EJB
	private ExpedienteBeanLocal expedienteBean;

	private Empleado empleado;
	private Oficina oficina;
	private Expediente expediente;
	private Estado estado;
	private boolean selectedItem;
	private String titulo;

	private static final Logger LOG = LoggerFactory.getLogger(DatosHistoricoMB.class);
	
	public DatosHistoricoMB() {
		LOG.info("MB DatosHistoricoMB");

	}
	
	@PostConstruct
	public void init() {
		LOG.info("init");
		obtenerDatos();	
	}
	
	public void obtenerDatos() {	
		LOG.info("Metodo obtenerDatos");
		LOG.info("getNombreJSPPrincipal() = "+getNombreJSPPrincipal());
		if(getNombreJSPPrincipal().trim().equalsIgnoreCase(Constantes.NOMBRE_JSP_HISTORIAL_EXPEDIENTE)){
			setTitulo(Mensajes.getMensaje("com.ibm.bbva.historialExpediente.formHistorialExpediente.titulo"));
		}else if(getNombreJSPPrincipal().trim().equalsIgnoreCase(Constantes.NOMBRE_JSP_OBSERVACION_EXPEDIENTE)){
			setTitulo(Mensajes.getMensaje("com.ibm.bbva.observacionExpediente.formObservacionExpediente.titulo"));
		}else if(getNombreJSPPrincipal().trim().equalsIgnoreCase(Constantes.NOMBRE_JSP_AYUDA_MEMORIA_EXPEDIENTE)){
			setTitulo(Mensajes.getMensaje("com.ibm.bbva.ayudaMemoriaExpediente.formAyudaMemoriaExpediente.titulo"));
		}else if(getNombreJSPPrincipal().trim().equalsIgnoreCase(Constantes.NOMBRE_JSP_LOG_EXPEDIENTE)){
			setTitulo(Mensajes.getMensaje("com.ibm.bbva.logExpediente.formLogExpediente.titulo"));
		}
		
		oficina = (Oficina) getObjectSession(Constantes.OFICINA_SESION);				
		empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		/*Obtiene Datos de Expediente*/
		//inicio man1
		//expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		//expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_HISTORICO);
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		//fin man1
		if (expediente != null) {

			if(expediente.getId() > 0 && (expediente.getClienteNatural() == null || expediente.getClienteNatural().getId() <= 0)){
				expediente = expedienteBean.buscarPorId(expediente.getId());
			}
			
			if (expediente.getClienteNatural()!=null && (expediente.getClienteNatural().getSubrog()==null?Constantes.CHECK_NO_SELECCIONADO:expediente.getClienteNatural().getSubrog()).equals(Constantes.CHECK_SELECCIONADO)) {
				selectedItem = true;
			}else{
				selectedItem = false;
			}
		}else{
			LOG.info("expediente es null ");
			
		}
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public void setSelectedItem(boolean selectedItem) {
		this.selectedItem = selectedItem;
	}

	public boolean isSelectedItem() {
		return selectedItem;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}
}

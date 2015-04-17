package com.ibm.bbva.controller.common;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Oficina;

@SuppressWarnings("serial")
@ManagedBean(name = "datosGestor")
@RequestScoped
public class DatosGestorMB extends AbstractMBean {

	private Empleado empleado;
	private Oficina oficina;
	private Expediente expediente;
	private Estado estado;

	private static final Logger LOG = LoggerFactory.getLogger(DatosGestorMB.class);
	
	public DatosGestorMB() {
         obtenerDatos();
	}
	
	public void obtenerDatos() {

		/*Obtiene Datos de Expediente*/
		expediente = (Expediente)getObjectSession(Constantes.EXPEDIENTE_SESION);
		
		//oficina = (Oficina) expediente.getExpedienteTC().getEmpleado().getOficina();
		oficina = (Oficina) expediente.getExpedienteTC().getOficina();
		empleado = (Empleado) expediente.getExpedienteTC().getEmpleado();
		
		/*Obtiene Datos de Estado*/
		if(expediente!=null && expediente.getEstado()!=null)
		estado = expediente.getEstado();
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
}

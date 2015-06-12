package com.ibm.bbva.util;

import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.ExpedienteTC;

public class AyudaExpedienteVO {
	
	private static final Logger LOG = LoggerFactory.getLogger(AyudaExpedienteVO.class);

	public static Expediente instanciar () {

		FacesContext fc = FacesContext.getCurrentInstance();
		Empleado empleado = (Empleado) fc.getExternalContext().getSessionMap().get(Constantes.USUARIO_SESION);
		Expediente expediente = new Expediente();
		expediente.setEmpleado(empleado);
		expediente.setNumTerminal(Util.obtenerIp());
		expediente.setExpedienteTC(new ExpedienteTC());
		expediente.getExpedienteTC().setEmpleado(empleado);
		expediente.getExpedienteTC().setFlagRetraer(Constantes.FLAGRETRAERN);
		LOG.info("exp->"+ expediente.getId());
		return expediente;
	}	
}

package com.ibm.bbva.util;

import javax.faces.context.FacesContext;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.ExpedienteTC;

public class AyudaExpedienteVO {

	public static Expediente instanciar () {

		FacesContext fc = FacesContext.getCurrentInstance();
		Empleado empleado = (Empleado) fc.getExternalContext().getSessionMap().get(Constantes.USUARIO_SESION);
		Expediente expediente = new Expediente();
		expediente.setEmpleado(empleado);
		expediente.setNumTerminal(Util.obtenerIp());
		expediente.setExpedienteTC(new ExpedienteTC());
		expediente.getExpedienteTC().setEmpleado(empleado);
		expediente.getExpedienteTC().setFlagRetraer(Constantes.FLAGRETRAERN);
		System.out.println("exp->"+ expediente.getId());
		return expediente;
	}	
}

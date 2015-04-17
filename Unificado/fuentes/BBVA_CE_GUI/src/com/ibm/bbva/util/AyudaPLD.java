package com.ibm.bbva.util;


import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.session.ExpedienteBeanLocal;

public class AyudaPLD {

	private ExpedienteBeanLocal expedienteBeanLocalBean;
	private Expediente expedienteBean;
	
	
	public AyudaPLD()  {
		super();
		try {
			expedienteBeanLocalBean=(ExpedienteBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.ExpedienteBeanLocal");
	
		} catch (NamingException e) {
			e.printStackTrace();
		}	
	}
	
	public Tarea obtenerUltimaTarea(long idExpediente){
		expedienteBean=expedienteBeanLocalBean.buscarPorIdExpediente(idExpediente);
		if(expedienteBean!=null && expedienteBean.getExpedienteTC()!=null && 
				expedienteBean.getExpedienteTC().getTarea()!=null)
			return expedienteBean.getExpedienteTC().getTarea();
		return null;
	}
	
	/**
	 * Para las Tareas 32 y 33
	 * Cuando el Expediente ha sido enviado desde los 
	 * “CU4 – Aprobar Expediente”, 
	 * “CU12 – Ejecutar evaluación crediticia” o 
	 * “CU19 – Revisar y registrar dictamen” 
	 * se activa y se muestra el botón “No conforme” 
	 * */
	public boolean activarOpcionNoConforme(long idExpediente){
		Tarea tareaTmp=obtenerUltimaTarea(idExpediente);
		Expediente objExpediente=expedienteBeanLocalBean.buscarPorId(idExpediente);
		if(objExpediente!=null && objExpediente.getProducto()!=null && objExpediente.getProducto().getId()>0){
			if(tareaTmp!=null && tareaTmp.getId()>0){
				System.out.println("tareaTmp.getId()= "+tareaTmp.getId());
				if(String.valueOf(tareaTmp.getId()).equals(Constantes.CU_TAREA_4) || 
						String.valueOf(tareaTmp.getId()).equals(Constantes.CU_TAREA_12) ||
						String.valueOf(tareaTmp.getId()).equals(Constantes.CU_TAREA_19) ){
					System.out.println("true");
					return true;
				}
			}			
		}

		System.out.println("false");
		return false;
	}
	
}

package com.ibm.bbva.ctacte.controller.comun.interf;

import java.util.Date;

import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.controller.comun.IdentifiquePJOperacion1MB;

public interface IIdentifiquePJOperacion1 {

	void mostrarSecciones (Cuenta cuenta, Operacion operacion, ClientePJCore cliente);
	
	void ocultarSecciones ();
	
	void setIdentifiquePJOperacion1(
			IdentifiquePJOperacion1MB identifiquePJOperacion1);
	
	Date getFechaActual ();

}

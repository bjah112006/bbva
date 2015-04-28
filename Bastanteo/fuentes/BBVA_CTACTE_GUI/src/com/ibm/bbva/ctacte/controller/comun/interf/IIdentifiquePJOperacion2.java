package com.ibm.bbva.ctacte.controller.comun.interf;

import java.util.Date;

import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.controller.comun.IdentifiquePJOperacion2MB;

public interface IIdentifiquePJOperacion2 {

	void setIdentifiquePJOperacion2(
			IdentifiquePJOperacion2MB identifiquePJOperacion2);
	
	void mostrarComponentes(Cuenta cuenta);
	
	void ocultarComponentes();
	
	Date getFechaActual ();
	
}

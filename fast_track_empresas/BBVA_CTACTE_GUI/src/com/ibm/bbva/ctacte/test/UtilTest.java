package com.ibm.bbva.ctacte.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Historial;
import com.ibm.bbva.ctacte.bean.Oficina;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.util.Util;

public class UtilTest {

	
	
	
	public static void main(String[] args) {
		Empleado empleado = new Empleado();
		empleado.setPerfil(new Perfil());
		empleado.setOficina(new Oficina());
		
		empleado.getPerfil().setId(1);
		empleado.getOficina().setId(3);
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Historial historial = new Historial();
		
		Calendar fechaA = Calendar.getInstance(); 		
		fechaA.set(2015, 6, 26, 10, 20);
		
		Calendar fechaB = Calendar.getInstance(); 		
		fechaB.set(2015, 6, 30, 13, 20);
		
		System.out.println("Fecha inicial: " + df.format(fechaA.getTime()));
		System.out.println("Fecha Final: " + df.format(fechaB.getTime()));
		Util.obtenerAnsTienpoReal(historial, empleado);
	}

}

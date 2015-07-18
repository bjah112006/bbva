package com.pe.bbva.pyme.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class Utils {
        
    protected Utils() {
        throw new UnsupportedOperationException("No instanciar la clase");
    }
    
    public static boolean esAnioBisiesto() {
    	GregorianCalendar calendar = new GregorianCalendar();
    	return calendar.isLeapYear(Calendar.YEAR);
    }
    
    public static int getDiasMesActual(){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	int mes = (cal.get(Calendar.MONTH))+1;
        int diasMes = 0;  
        Integer[ ] mes30 = { 4, 6, 9, 11}; 
        Integer[ ] mes31 = { 1, 3, 5, 7, 8, 10, 12}; 
        if(mes==2){
        	diasMes = esAnioBisiesto()?29:28;
        }else if(buscarValorEnListaEnteros(mes30,mes)){
        	diasMes = 30;
        }else if(buscarValorEnListaEnteros(mes31,mes)){
        	diasMes = 31;
        }        
        return diasMes;     	
     }
    
    public static String convertirFechaActualEnCadena(String formato) {
    	Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat(formato);
		return dateFormat.format(date);
	}
    
    public static String convertirDateEnCadena(Date date, String formato) {
    	DateFormat dateFormat = new SimpleDateFormat(formato);
		return dateFormat.format(date);
	}
    
    public static String traducirEstadoSolicitud(String estado){
  	  	String estadoTraducido = "";
    	switch (estado) {
	  	  	case "initializing": estadoTraducido = ConstantesEnum.EST_SOL_INICIALIZANDO_TR.getNombre();	
  	  								break;
	  	  	case "executing":    estadoTraducido = ConstantesEnum.EST_SOL_EN_EJECUCION_TR.getNombre();	
	  	  							break;
	  	  	case "completed":    estadoTraducido = ConstantesEnum.EST_SOL_TERMINADO_TR.getNombre();		
	  	  							break;
	  	  	case "ready":        estadoTraducido = ConstantesEnum.EST_SOL_POR_INICIAR_TR.getNombre();	
	  	  							break;
			default: 
									break;
  	  	}
  	  	 return estadoTraducido;
    }
    
    public static boolean buscarValorEnListaEnteros(Integer[] arr, int targetValue) {
    	return Arrays.asList(arr).contains(targetValue);
    }
    
    public static Date addDaysTodate(Date fecha,int dias){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(fecha); 
    	calendar.add(Calendar.DAY_OF_YEAR, dias);
    	return calendar.getTime(); 
    }
}

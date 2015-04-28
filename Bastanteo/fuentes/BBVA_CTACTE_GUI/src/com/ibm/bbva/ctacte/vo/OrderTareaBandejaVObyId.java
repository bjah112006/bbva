package com.ibm.bbva.ctacte.vo;

import java.util.Comparator;

public class OrderTareaBandejaVObyId implements Comparator{

	public int compare(Object o1, Object o2) {
		TareaBandejaVO t1 = (TareaBandejaVO) o1; 
		TareaBandejaVO t2 = (TareaBandejaVO) o2;
		 
		if (Integer.parseInt(t1.getId())== Integer.parseInt(t2.getId())) { //si el idExp primero es igual a la edad de la segunda retornamos 0
            return 0;
        } else if (Integer.parseInt(t2.getId()) > Integer.parseInt(t1.getId())) {//si el idExp segundo  es mayor la edad de la primera retornamos 1
            return 1;
        } else {  //si el idExp segundo  es menor la edad de la primera retornamos -1
            return -1;
        }
	}
}

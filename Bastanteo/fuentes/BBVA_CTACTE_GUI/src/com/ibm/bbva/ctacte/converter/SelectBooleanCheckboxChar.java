package com.ibm.bbva.ctacte.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.ibm.bbva.ctacte.controller.ConstantesAdmin;

@FacesConverter ("selectBooleanCheckboxChar")
public class SelectBooleanCheckboxChar implements Converter {

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return Boolean.TRUE.toString().equalsIgnoreCase(arg2) ? 
				ConstantesAdmin.FLAG_VERDADERO : 
				ConstantesAdmin.FLAG_FALSO;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return ConstantesAdmin.FLAG_VERDADERO.equals((String)arg2) ? 
				Boolean.TRUE.toString() : Boolean.FALSE.toString();
	}

}

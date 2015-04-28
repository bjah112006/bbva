/**
 * 
 */
package com.ibm.jsf2.custom.component;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * @author cfveliz
 *
 */
@FacesComponent(value = "tab")
public class Tab extends UIPanel {
	
	@Override
	public String getFamily() {
		return "custom";
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		writer.writeAttribute("id", getClientId(), "clientId");
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.endElement("div");
	}

}

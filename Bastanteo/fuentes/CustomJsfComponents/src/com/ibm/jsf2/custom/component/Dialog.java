package com.ibm.jsf2.custom.component;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

@FacesComponent(value = "dialog")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class Dialog extends UIPanel {
	
	@Override
	public String getFamily() {
		return "custom";
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		writer.writeAttribute("id", getClientId(), "clientId");
		writer.writeAttribute("style", "display:none", null);
		writer.writeAttribute("title", getAttributes().get("header"), null);
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.endElement("div");
		writer.startElement("script", this);
		writer.writeAttribute("type", "text/javascript", null);
		StringBuilder options = new StringBuilder();
		options.append("{");
		options.append("autoOpen: false");
		String resizable = (String) getAttributes().get("resizable");
		if (resizable != null && !resizable.trim().equals("")) {
			options.append(", resizable: ").append(resizable);
		}
		options.append(", modal: true");
		options.append("}");
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("line.separator"));
		sb.append("$(function() {").append(System.getProperty("line.separator"));
		sb.append("$(\"[id='").append(getClientId()).append("']\").dialog(").append(options).append(").parent().appendTo($(\"[id='").append(getNamingContainer().getClientId()).append("']\"));").append(System.getProperty("line.separator"));
		String widgetVar = (String) getAttributes().get("widgetVar");
		if (widgetVar != null && !widgetVar.trim().equals("")) {
			sb.append(widgetVar).append(" = $(\"[id='").append(getClientId()).append("']\");").append(System.getProperty("line.separator"));
		}
		sb.append("});").append(System.getProperty("line.separator"));
		writer.writeText(sb.toString(), null);
		writer.endElement("script");
	}

}

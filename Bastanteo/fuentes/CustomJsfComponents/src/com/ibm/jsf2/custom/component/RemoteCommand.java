package com.ibm.jsf2.custom.component;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.FacesComponent;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import com.ibm.jsf2.custom.util.CustomComponentUtil;

@FacesComponent(value = "remoteCommand")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class RemoteCommand extends UICommand {
	
	@Override
	public void processEvent(ComponentSystemEvent event)
			throws AbortProcessingException {
		if (event instanceof PostAddToViewEvent) {
			FacesContext context = FacesContext.getCurrentInstance();
			CustomComponentUtil.addScriptToHead(context, "javascript",
					"jquery-1.8.2.min.js");
			CustomComponentUtil.addScriptToHead(context, "javax.faces",
					"jsf.js");
		}
		super.processEvent(event);
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException {
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException, FacesException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("script", this);
		writer.writeAttribute("id", getClientId(), "clientId");
		writer.writeAttribute("type", "text/javascript", null);
		String execute = CustomComponentUtil.getClientIdsString(this, (String) getAttributes().get("process"), "@this");
		String render = CustomComponentUtil.getClientIdsString(this, (String) getAttributes().get("update"), "@none");
		String onevent = (String) getAttributes().get("oncomplete");
		if (onevent != null && !onevent.trim().equals("")) {
			onevent = ",onevent: function(data) { if (data.status == 'success') { " + onevent + "(data); } }";
		} else {
			onevent = "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(getAttributes().get("name")).append(" = function() {").append(System.getProperty("line.separator"));
		sb.append("var options = {execute:'").append(execute).append("',render:'").append(render).append("'").append(onevent).append(",'javax.faces.behavior.event':'action'};").append(System.getProperty("line.separator"));
		sb.append("if (arguments.length > 0) { $.extend(options, arguments[0]);}").append(System.getProperty("line.separator"));
		sb.append("jsf.ajax.request('").append(getClientId()).append("',null,options);").append(System.getProperty("line.separator"));
		sb.append("}");
		writer.writeText(sb.toString(), null);
		writer.endElement("script");
		
		/*writer.startElement("input", this);
		writer.writeAttribute("id", getClientId(), "clientId");
		writer.writeAttribute("name", getClientId(), null);
		writer.writeAttribute("type", "submit", null);
		writer.writeAttribute("style", "display:none", null);
		writer.endElement("input");*/
	}

}

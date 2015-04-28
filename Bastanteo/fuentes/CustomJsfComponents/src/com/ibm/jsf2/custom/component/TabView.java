/**
 * 
 */
package com.ibm.jsf2.custom.component;

import java.io.IOException;
import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import com.ibm.jsf2.custom.util.CustomComponentUtil;

/**
 * @author cfveliz
 * 
 */
@FacesComponent(value = "tabView")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class TabView extends UINamingContainer {

	@Override
	public String getFamily() {
		return "custom";
	}
	
	@Override
	public void processEvent(ComponentSystemEvent event)
			throws AbortProcessingException {
		if (event instanceof PostAddToViewEvent) {
			FacesContext context = FacesContext.getCurrentInstance();
			CustomComponentUtil.addStyleSheetToHead(context,
					"css/jquery-ui", "jquery-ui.css");
			CustomComponentUtil.addScriptToHead(context, "javascript",
					"jquery-1.8.2.min.js");
			CustomComponentUtil.addScriptToHead(context, "javascript",
					"jquery-ui-1.8.24.custom.min.js");
		}
		super.processEvent(event);
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("script", this);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeText("$(function() { $(\"[id='"+getClientId()+"']\").tabs(); });", null);
		writer.endElement("script");
		writer.startElement("div", this);
		writer.writeAttribute("id", getClientId(), "clientId");
		String styleClass = (String) getAttributes().get("styleClass");
		if (styleClass != null) {
			writer.writeAttribute("class", styleClass, "styleClass");
		}
		String style = (String) getAttributes().get("style");
		if (style != null) {
			writer.writeAttribute("style", style, "style");
		}
		writer.startElement("ul", this);
		List<UIComponent> children = getChildren();
		for (UIComponent child : children) {
			writer.startElement("li", this);
			writer.startElement("a", this);
			writer.writeAttribute("href", "#"+child.getClientId(), null);
			writer.writeText(child.getAttributes().get("title"), null);
			writer.endElement("a");
			writer.endElement("li");
		}
		writer.endElement("ul");
		
		writer.startElement("div", this);
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.endElement("div");
		writer.endElement("div");
	}

}

package com.ibm.jsf2.custom.component;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import com.ibm.jsf2.custom.util.CustomComponentUtil;

@FacesComponent(value = "textAreaCounter")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class TextAreaCounter extends UIPanel {
	
	@Override
	public void processEvent(ComponentSystemEvent event)
			throws AbortProcessingException {
		if (event instanceof PostAddToViewEvent) {
			FacesContext context = FacesContext.getCurrentInstance();
			CustomComponentUtil.addScriptToHead(context, "javascript",
					"jquery-1.8.2.min.js");
			CustomComponentUtil.addScriptToHead(context, "javascript",
					"jquery.textareaCounter.plugin.js");
		}
		super.processEvent(event);
	}

	@Override
	public String getFamily() {
		return "custom";
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException {
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("script", this);
		writer.writeAttribute("id", getClientId(), "clientId");
		writer.writeAttribute("type", "text/javascript", null);
		String cr = System.getProperty("line.separator");
		String forInput = CustomComponentUtil.getClientIdsString(this, (String) getAttributes().get("for"), null);
		String maxCharacterSize = (String) getAttributes().get("maxCharacterSize");
		try {
			Integer.valueOf(maxCharacterSize);
		} catch (Exception e) {
			maxCharacterSize = "-1";
		}
		String warningNumber = (String) getAttributes().get("warningNumber");
		try {
			Integer.valueOf(warningNumber);
		} catch (Exception e) {
			warningNumber = "-1";
		}
		String originalStyle = (String) getAttributes().get("originalStyle");
		String warningStyle = (String) getAttributes().get("warningStyle");
		if (warningNumber.equals("-1")) {
			warningStyle = originalStyle;
		}
		String displayFormat = "#input de #max caracteres";
		StringBuilder sb = new StringBuilder();
		if (forInput != null) {
			sb.append("$(function() {").append(cr);
			sb.append("  var optionsTA = {").append(cr);
			sb.append("    maxCharacterSize:").append(maxCharacterSize).append(",").append(cr);
			sb.append("    warningNumber:").append(warningNumber).append(",").append(cr);
			if (originalStyle != null && !originalStyle.trim().equals("")) {
				sb.append("    originalStyle:'").append(originalStyle).append("'").append(cr);
			}
			if (warningStyle != null && !warningStyle.trim().equals("")) {
				sb.append("    warningStyle:'").append(warningStyle).append("'").append(cr);
			}
			sb.append("    displayFormat:'").append(displayFormat).append("'").append(cr);
			sb.append("  };").append(cr);
			sb.append("  $(\"[id='").append(forInput).append("']\").textareaCount(optionsTA);").append(cr);
			sb.append("});").append(cr);
		} else {
			sb.append("// Componente con id = '").append(getAttributes().get("for")).append("' no se encontró");
		}
		writer.writeText(sb.toString(), null);
		writer.endElement("script");
	}

}

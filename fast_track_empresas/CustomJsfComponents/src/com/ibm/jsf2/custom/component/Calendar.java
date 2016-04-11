package com.ibm.jsf2.custom.component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import com.ibm.jsf2.custom.util.CustomComponentUtil;

@FacesComponent(value = "calendar")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class Calendar extends UIInput implements ClientBehaviorHolder {
	
	private final String defaultPattern = "dd/MM/yyyy";
	
	@Override
	public void processEvent(ComponentSystemEvent event)
			throws AbortProcessingException {
		if (event instanceof PostAddToViewEvent) {
			String pattern = (String) getAttributes().get("pattern");
			if (pattern == null) {
				pattern = defaultPattern;
			}
			String mask = (String) getAttributes().get("mask");

			FacesContext context = FacesContext.getCurrentInstance();
			CustomComponentUtil.addStyleSheetToHead(context, "css/jquery-ui",
					"jquery-ui.css");
			if (hasTime(pattern)) {
				CustomComponentUtil.addStyleSheetToHead(context,
						"css/timepicker", "jquery-ui-timepicker-addon.css");
			}
			CustomComponentUtil.addScriptToHead(context, "javascript",
					"jquery-1.8.2.min.js");
			CustomComponentUtil.addScriptToHead(context, "javax.faces",
					"jsf.js");
			CustomComponentUtil.addScriptToHead(context, "javascript",
					"jquery-ui-1.8.24.custom.min.js");
			if (hasTime(pattern)) {
				CustomComponentUtil.addScriptToHead(context, "javascript",
						"jquery-ui-timepicker-addon.js");
			}
			if (mask != null && !mask.trim().equals("")) {
				CustomComponentUtil.addScriptToHead(context, "javascript",
						"jquery.mask.min.js");
			}
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
	public void encodeEnd(FacesContext context) throws IOException {
		ClientBehaviorContext behaviorContext = ClientBehaviorContext
				.createClientBehaviorContext(context, this, "change",
						getClientId(context), null);
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("span", this);
		writer.writeAttribute("id", getClientId(), "clientId");
		writer.writeAttribute("name", getClientId(), null);
		String styleClass = (String) getAttributes().get("styleClass");
		if (styleClass != null) {
			writer.writeAttribute("class", styleClass, "styleClass");
		}
		
		writer.startElement("input", this);
		writer.writeAttribute("id", getClientId()+"_input", "clientId");
		writer.writeAttribute("name", getClientId()+"_input", null);
		writer.writeAttribute("type", "text", null);
		String pattern = (String) getAttributes().get("pattern");
		if (pattern == null) {
			pattern = defaultPattern;
		}
		if (getConverter() != null) {
			writer.writeAttribute("value", getConverter().getAsString(context, this, getValue()), "value");
		} else {
			DateTimeConverter convert = new DateTimeConverter();
			convert.setPattern(pattern);
			convert.setTimeZone(TimeZone.getDefault());
			writer.writeAttribute("value", convert.getAsString(context, this, getValue()), "value");
		}
		Boolean readOnlyInputText;
		if (getAttributes().get("readOnlyInputText") == null) {
			readOnlyInputText = null;
		} else if (getAttributes().get("readOnlyInputText") instanceof String) {
			readOnlyInputText = Boolean.valueOf((String) getAttributes().get("readOnlyInputText"));
		} else {
			readOnlyInputText = (Boolean) getAttributes().get("readOnlyInputText");
		}
		if (readOnlyInputText != null && readOnlyInputText.equals(Boolean.TRUE)) {
			writer.writeAttribute("readonly", "readonly", "readOnlyInputText");
		}
		Boolean disabled;
		if (getAttributes().get("disabled") == null) {
			disabled = null;
		} else if (getAttributes().get("disabled") instanceof String) {
			disabled = Boolean.valueOf((String) getAttributes().get("disabled"));
		} else {
			disabled = (Boolean) getAttributes().get("disabled");
		}
		if (disabled != null && disabled.equals(Boolean.TRUE)) {
			writer.writeAttribute("disabled", "disabled", "disabled");
		}
		writer.endElement("input");
		
		writer.startElement("script", this);
		writer.writeAttribute("type", "text/javascript", null);
		StringBuilder sb = new StringBuilder();
		String cr = System.getProperty("line.separator");
		sb.append("$(function() {").append(cr);
		String function = "datepicker";
		if (hasTime(pattern)) {
			function = "datetimepicker";
		}
		sb.append("  $(\"[id='").append(getClientId()).append("_input']\").").append(function).append("({").append(cr);
		sb.append("    dateFormat: \"").append(convertToJQueryUIDateFormat(pattern)).append("\",").append(cr);
		if (hasTime(pattern)) {
			sb.append("    timeFormat: \"").append(convertToJQueryUITimeFormat(pattern)).append("\",").append(cr);
		}
		Map<String, List<ClientBehavior>> behaviors = getClientBehaviors();
		if (behaviors.containsKey("change")) {
			String ajax = behaviors.get("change").get(0).getScript(behaviorContext);
			ajax = ajax.replaceFirst("event", "null");// no tenemos el evento de javascript
			sb.append("    onSelect: function(selectedDate) { ").append(ajax).append(" },").append(cr);
		}
		Date maxdate = (Date) getAttributes().get("maxdate");
		if (maxdate != null) { 
			sb.append("    maxDate: new Date(").append(maxdate.getTime()).append("),").append(cr);
		}
		Date mindate = (Date) getAttributes().get("mindate");
		if (mindate != null) { 
			sb.append("    minDate: new Date(").append(mindate.getTime()).append("),").append(cr);
		}
		String effect = (String) getAttributes().get("effect");
		if (effect != null) {
			sb.append("    showAnim: \"").append(effect).append("\",").append(cr);
		}
		String showOn = (String) getAttributes().get("showOn");
		if (showOn != null) {
			sb.append("    showOn: \"").append(showOn).append("\",").append(cr);
		}
		String mask = (String) getAttributes().get("mask");
		if (mask == null || mask.trim().equals("")) { // la máscara no es compatible con "constrainInput"
			sb.append("    constrainInput: true").append(cr);
		} else {
			sb.append("    constrainInput: false").append(cr);
		}
		sb.append("  });").append(cr);
		if (mask != null && !mask.trim().equals("")) {
			sb.append("  $(\"[id='").append(getClientId()).append("_input']\").mask('").append(mask).append("');").append(cr);
		}
		sb.append("});").append(cr);
		writer.writeText(sb.toString(), null);
		writer.endElement("script");
		
		writer.endElement("span");
	}

	@Override
	public void decode(FacesContext context) {
		Map requestMap = context.getExternalContext().getRequestParameterMap();
		String value = ((String) requestMap.get(getClientId() + "_input"));
		setSubmittedValue(value);
		
		Map<String, List<ClientBehavior>> behaviors = getClientBehaviors();
		if (behaviors.isEmpty()) {
			return;
		}

		ExternalContext external = context.getExternalContext();
		Map<String, String> params = external.getRequestParameterMap();
		String behaviorEvent = params.get("javax.faces.behavior.event");

		if (behaviorEvent != null) {
			List<ClientBehavior> behaviorsForEvent = behaviors
					.get(behaviorEvent);

			if (behaviors.size() > 0) {
				String behaviorSource = params.get("javax.faces.source");
				String clientId = getClientId(context);
				if (behaviorSource != null && behaviorSource.equals(clientId)) {
					for (ClientBehavior behavior : behaviorsForEvent) {
						behavior.decode(context, this);
					}
				}
			}
		}
	}

	@Override
	protected Object getConvertedValue(FacesContext context,
			Object newSubmittedValue) throws ConverterException {
		Converter converter = null;
		if (getConverter() != null) {
			converter = getConverter();
		} else {
			String pattern = (String) getAttributes().get("pattern");
			if (pattern == null) {
				pattern = defaultPattern;
			}
			DateTimeConverter dtconv = new DateTimeConverter();
			dtconv.setPattern(pattern);
			dtconv.setTimeZone(TimeZone.getDefault());
			converter = dtconv;
		}
		Object convertedValue = converter.getAsObject(context, this, (String) newSubmittedValue);
		this.setValid(true); // si no se ponía en true nunca volvía a pasar la validación
		return convertedValue;
	}

	@Override
	public String getDefaultEventName() {
		return "change";
	}

	@Override
	public Collection<String> getEventNames() {
		return Arrays.asList("change");
	}
	
	private String convertToJQueryUIDateFormat(String localFormatString) {
		//Se asume que el patrón de hora siempre va a estar después de la fecha (limitaciones del plugin)
		if (localFormatString.indexOf("H") != -1) {
			localFormatString = localFormatString.substring(0, localFormatString.indexOf("H")).trim();
		} else if (localFormatString.indexOf("h") != -1) {
			localFormatString = localFormatString.substring(0, localFormatString.indexOf("h")).trim();
		}
		// Year
		if (Pattern.compile("y{3,}").matcher(localFormatString).find()) { /* YYYY */
			localFormatString = localFormatString.replaceAll("y{3,}", "yy");
		} else if (Pattern.compile("y{2}").matcher(localFormatString).find()) { /* YY */
			localFormatString = localFormatString.replaceAll("y{2}", "y");
		}

		// Month
		if (Pattern.compile("M{4,}").matcher(localFormatString).find()) { /* MMMM */
			localFormatString = localFormatString.replaceAll("M{4,}", "MM");
		} else if (Pattern.compile("M{3}").matcher(localFormatString).find()) { /* MMM */
			localFormatString = localFormatString.replaceAll("M{3}", "M");
		} else if (Pattern.compile("M{2}").matcher(localFormatString).find()) { /* MM */
			localFormatString = localFormatString.replaceAll("M{2}", "mm");
		} else if (Pattern.compile("M{1}").matcher(localFormatString).find()) { /* M */
			localFormatString = localFormatString.replaceAll("M{1}", "m");
		}

		// Day
		if (Pattern.compile("D{2,}").matcher(localFormatString).find()) { /* DD */
			localFormatString = localFormatString.replaceAll("D{2,}", "oo");
		} else if (Pattern.compile("D{1}").matcher(localFormatString).find()) { /* D */
			localFormatString = localFormatString.replaceAll("D{1}", "o");
		}

		// Day of month
		if (Pattern.compile("E{4,}").matcher(localFormatString).find()) { /* EEEE */
			localFormatString = localFormatString.replaceAll("E{4,}", "DD");
		} else if (Pattern.compile("E{2,3}").matcher(localFormatString).find()) { /* EEE */
			localFormatString = localFormatString.replaceAll("E{2,3}", "D");
		}
		return localFormatString;
	}
	
	private String convertToJQueryUITimeFormat(String localFormatString) {
		//Se asume que el patrón de hora siempre va a estar después de la fecha (limitaciones del plugin)
		if (localFormatString.indexOf("H") != -1) {
			localFormatString = localFormatString.substring(localFormatString.indexOf("H")).trim();
		} else if (localFormatString.indexOf("h") != -1) {
			localFormatString = localFormatString.substring(localFormatString.indexOf("h")).trim();
		}
		localFormatString = localFormatString.replaceAll("H", "h").replaceAll("a", "TT");
		return localFormatString;
	}
	
	private boolean hasTime(String pattern) {
		// el patrón tiene que tener al menos la hora para usar el datetimepicker
		if(pattern.indexOf("H") != -1 || pattern.indexOf("h") != -1) {
			return true;
		} else {
			return false;
		}
	}

}

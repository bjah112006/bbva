package com.ibm.jsf2.custom.util;

import java.util.StringTokenizer;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

public class CustomComponentUtil {

	private static final String TARGET_HEAD_NAME = "head";
	private static final String STYLESHEET_RENDERER_TYPE = "javax.faces.resource.Stylesheet";
	private static final String SCRIPT_RENDERER_TYPE = "javax.faces.resource.Script";

	public static void addResourceToHead(FacesContext context, String library,
			String name, String rendererType) {
		UIOutput resource = new UIOutput();
		resource.getAttributes().put("library", library);
		resource.getAttributes().put("name", name);
		resource.getAttributes().put("target", TARGET_HEAD_NAME);
		resource.setRendererType(rendererType);
		context.getViewRoot().addComponentResource(context, resource);

	}

	public static void addStyleSheetToHead(FacesContext context,
			String library, String name) {
		addResourceToHead(context, library, name, STYLESHEET_RENDERER_TYPE);
	}

	public static void addScriptToHead(FacesContext context, String library,
			String name) {
		addResourceToHead(context, library, name, SCRIPT_RENDERER_TYPE);
	}
	
	public static String getAbsoluteComponentId(UIComponent component, String relativeId) {
		if (relativeId.equals("@this") || relativeId.equals("@form") || relativeId.equals("@all") || relativeId.equals("@none")) {
			return relativeId;
		} else {
			UIComponent componentId = component.findComponent(relativeId);
			if (componentId != null) {
				return componentId.getClientId();
			} else {
				return null;
			}
		}
	}
	
	public static String getClientIdsString(UIComponent component, String relativeIds, String defaultValue) throws FacesException {
		if (relativeIds != null && !relativeIds.trim().equals("")) {
			String clientIds = "";
			StringTokenizer st = new StringTokenizer(relativeIds.trim(), " ");
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				String clientId = getAbsoluteComponentId(component, token);
				if (clientId != null) {
					clientIds = clientIds + clientId;
					if (st.hasMoreTokens()) {
						clientIds = clientIds + " ";
					}
//				} else {
//					throw new FacesException("Component with id:"+token+" not found");
				}
			}
			return clientIds.trim();
		} else {
			return defaultValue;
		}
	}

}

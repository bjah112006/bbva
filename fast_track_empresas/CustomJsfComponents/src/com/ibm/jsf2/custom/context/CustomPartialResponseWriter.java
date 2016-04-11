package com.ibm.jsf2.custom.context;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.context.ResponseWriter;

import org.apache.myfaces.context.PartialResponseWriterImpl;

public class CustomPartialResponseWriter extends PartialResponseWriterImpl {

	private Map<String, Object> params;

	public CustomPartialResponseWriter(ResponseWriter wrapped) {
		super(wrapped);
		params = new HashMap<String, Object>();
	}

	public void addCallbackParam(String name, Object value) {
		params.put(name, value);
	}

	@Override
	public void endDocument() throws IOException {
		if (!params.isEmpty()) {
			ResponseWriter writer = getWrapped();
			
	        super.startExtension(Collections.singletonMap("id", "custom"));
			StringBuilder jsonBuilder = new StringBuilder();
			jsonBuilder.append("{");

			for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
				String paramName = it.next();
				Object paramValue = params.get(paramName);

				if (paramValue != null) {
					jsonBuilder.append("\"").append(paramName).append("\":");
					if (paramValue instanceof Boolean || paramValue instanceof Number) {
						jsonBuilder.append(paramValue.toString());
			        } else if (paramValue instanceof String) {
			        	jsonBuilder.append("\"").append(paramValue.toString()).append("\"");
			        } else {
			        	jsonBuilder.append("\"").append(paramName).append("\":null");
			        }
				} else {
					jsonBuilder.append("\"").append(paramName).append("\":null");
				}

				if (it.hasNext()) {
					jsonBuilder.append(",");
				}
			}

			jsonBuilder.append("}");
			writer.startCDATA();
			write(jsonBuilder.toString());
            jsonBuilder.setLength(0);
            writer.endCDATA();
            super.endExtension();
		}

		super.endDocument();
	}

}

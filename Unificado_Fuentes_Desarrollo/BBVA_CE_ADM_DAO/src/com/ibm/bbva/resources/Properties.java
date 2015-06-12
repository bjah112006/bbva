package com.ibm.bbva.resources;

import java.util.HashMap;
import java.util.Map;

public class Properties {

	private Map<String,String> map = new HashMap<String,String>();

	public void setProperty(String name, String value) {
		map.put(name, value);
	}

	public String getProperty(String name) {
		return map.get(name);
	}
}

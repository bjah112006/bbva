package pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DocumentoResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, String> params;

	public DocumentoResponse() {
		super();
		params = new LinkedHashMap<String, String>();
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public int size() {
		return params.size();
	}

	public boolean containsKey(Object key) {
		return params.containsKey(key);
	}

	public DocumentoResponse put(String key, String value) {
		params.put(key, value);
		return this;
	}

	public String remove(Object key) {
		return params.remove(key);
	}

	public DocumentoResponse putAll(Map<? extends String, ? extends String> m) {
		params.putAll(m);
		return this;
	}

	public String get(Object key) {
		return params.get(key);
	}

	public Set<String> keySet() {
		return params.keySet();
	}

	public void clear() {
		params.clear();
	}

}

package pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DocumentoRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombreAplicacion;
	private String usuarioAplicacion;
	private String ipAplicacion;
	private String id;
	private Map<String, String> params;

	public DocumentoRequest() {
		super();
		params = new LinkedHashMap<String, String>();
	}

	public DocumentoRequest(String nombreAplicacion, String usuarioAplicacion, String ipAplicacion, String id) {
		this();
		this.nombreAplicacion = nombreAplicacion;
		this.usuarioAplicacion = usuarioAplicacion;
		this.ipAplicacion = ipAplicacion;
		this.id = id;
	}

	public String getNombreAplicacion() {
		return nombreAplicacion;
	}

	public void setNombreAplicacion(String nombreAplicacion) {
		this.nombreAplicacion = nombreAplicacion;
	}

	public String getUsuarioAplicacion() {
		return usuarioAplicacion;
	}

	public void setUsuarioAplicacion(String usuarioAplicacion) {
		this.usuarioAplicacion = usuarioAplicacion;
	}

	public String getIpAplicacion() {
		return ipAplicacion;
	}

	public void setIpAplicacion(String ipAplicacion) {
		this.ipAplicacion = ipAplicacion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public DocumentoRequest put(String key, String value) {
		params.put(key, value);
		return this;
	}

	public String remove(Object key) {
		return params.remove(key);
	}

	public DocumentoRequest putAll(Map<? extends String, ? extends String> m) {
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

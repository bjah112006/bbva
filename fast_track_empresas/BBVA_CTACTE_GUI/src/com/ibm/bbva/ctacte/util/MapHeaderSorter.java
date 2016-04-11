package com.ibm.bbva.ctacte.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapHeaderSorter<K,V>  extends HashMap<String, SortHeader> implements Map<String,SortHeader>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7122802084635166724L;
	private int contador;

	public void clear() {
		super.clear();
		
	}
	
	public boolean containsKey(Object key) {
		return super.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return super.containsValue(value);
	}

	public Set<java.util.Map.Entry<String, SortHeader>> entrySet() {
		return super.entrySet();
	}

	public SortHeader get(Object key) {
		return super.get(key);
	}

	public boolean isEmpty() {
		return super.isEmpty();
	}

	public Set<String> keySet() {		
		return super.keySet();
	}

	public SortHeader put(String key, SortHeader value) {
		contador++;
		value.setOrder(contador);
		return super.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends SortHeader> t) {
		super.putAll(t);
		
	}

	public SortHeader remove(Object key) {
		SortHeader retorno = null;
		if(containsKey(key)){
			retorno = super.remove(key);
			for(Map.Entry<String, SortHeader> entry:entrySet()){
				if(entry.getValue().getOrder()>retorno.getOrder()){
					entry.getValue().setOrder(entry.getValue().getOrder()-1);
					contador=entry.getValue().getOrder();
				}
			}
		}	
		
		return retorno;
	}

	public int size() {
		return super.size();
	}

	public Collection<SortHeader> values() {
		return super.values();
	}

}

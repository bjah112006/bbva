package com.ibm.bbva.ctacte.combo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ComboBoxCustomModel implements MutableComboBoxModel  {

	    private ArrayList<ComboItem> data = new ArrayList<ComboItem>();
	    private ComboItem selectedItem;
	    private ArrayList<ListDataListener> list = new ArrayList<ListDataListener>();
	    public static final ComboItem FIRST_ITEM_SELECCIONE = new ComboItem(null, "--Seleccione--");

	    public ComboBoxCustomModel(Map<String, String> tiposDocMap, ComboItem firstItem) {
	        data.add(firstItem);
	        for (Map.Entry<String, String> entry : tiposDocMap.entrySet()) {
	            data.add(new ComboItem(entry.getKey(), entry.getValue()));
	        }
	    }

	    public ComboBoxCustomModel(List<ComboItem> tiposDoclist, ComboItem firstItem) {
	        data = new ArrayList<ComboItem>();
	        if(!tiposDoclist.contains(firstItem)){
	            data.add(firstItem);
	        }        
	        data.addAll(tiposDoclist);
	    }

	    public ComboBoxCustomModel(Map<String, String> tiposDocMap) {
	        data.add(new ComboItem("", "--Seleccione--"));
	        for (Map.Entry<String, String> entry : tiposDocMap.entrySet()) {
	            data.add(new ComboItem(entry.getKey(), entry.getValue()));
	        }
	    }

	   
	    public void setSelectedItem(Object anItem) {
	        selectedItem = anItem instanceof ComboItem ? (ComboItem) anItem : null;
	        for (ListDataListener l : list) {
	            l.contentsChanged(new ListDataEvent(this, javax.swing.event.ListDataEvent.CONTENTS_CHANGED, 0, 0));
	        }
	    }

	   
	    public Object getSelectedItem() {
	        return selectedItem;
	    }

	  
	    public int getSize() {
	        return data.size();
	    }

	  
	    public ComboItem getElementAt(int index) {
	        return data.get(index);
	    }

	   
	    public void addListDataListener(ListDataListener l) {
	        list.add(l);
	    }

	   
	    public void removeListDataListener(ListDataListener l) {
	        list.remove(l);
	    }

	    public String getSelectedCodigo() {
	        return selectedItem == null ? null : selectedItem.getCodigo();
	    }

	    public String getSelectedDescripcion() {
	        return selectedItem == null ? null : selectedItem.getDescripcion();
	    }

	   
	    public void addElement(Object item) {
	        data.add((ComboItem)item);
	    }

	    public void removeElement(ComboItem obj) {
	        data.remove(obj);
	    }


	    public void insertElementAt(Object item, int index) {
	        data.add(index, (ComboItem)item);
	    }

	    
	    public void removeElementAt(int index) {
	        data.remove(index);
	    }

	    
	    public void removeElement(Object obj) {
	        data.remove((ComboItem) obj);
	    }
	
}

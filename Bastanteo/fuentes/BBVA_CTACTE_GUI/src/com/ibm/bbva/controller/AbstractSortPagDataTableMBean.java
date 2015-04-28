package com.ibm.bbva.controller;

import java.util.List;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;

/**
 * @author cfveliz
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractSortPagDataTableMBean extends AbstractLinksMBean {
	
	protected String sortField = null;
	protected boolean sortAscending = true;
	protected HtmlDataTable dataTable;
	private Integer numeroRegistro=10;
	public abstract void ordenar(ActionEvent event);
	
	public void ordenarPorDefecto() {
		System.out.println("ordenarPorDefecto:::::");
		this.dataTable.setFirst(0);
		this.sortField = null;
		this.sortAscending = true;
		
		String num = (String) getObjectSession(Constantes.PAGINA_SESSION);
		if(num!=null){
			if(num.equals("-1"))
				this.numeroRegistro=10000;
			else
				this.numeroRegistro=Integer.parseInt(num);
		}else
			this.numeroRegistro=10;
		System.out.println("this.numeroRegistro:-::::"+this.numeroRegistro);		
		//this.numeroRegistro=10;
		
	}
	
	public void ordenarPorAccionCombo() {
		this.dataTable.setFirst(0);
		this.sortField = null;
		this.sortAscending = true;
		
		String num = (String) getObjectSession(Constantes.PAGINA_SESSION);
		if(num!=null){
			if(num.equals("-1"))
				this.numeroRegistro=10000;
			else
				this.numeroRegistro=Integer.parseInt(num);
		}else
			this.numeroRegistro=10;
		System.out.println("this.numeroRegistro:::::"+this.numeroRegistro);
	}	
	
	public String obtenerEstiloColumna(String columna) {
		String estilo;
		if (sortField == null) {
			estilo = "sortHeader_sortbi";
		} else if (sortField.equals(columna)){
			if (sortAscending) {
				estilo = "sortHeader_sortbiup";
			} else {
				estilo = "sortHeader_sortbidown";
			}
		} else {
			estilo = "sortHeader_sortbi";
		}
		
		return estilo;
	}
	
	public String obtenerEstiloColumnaImg(String columna) {
		return obtenerEstiloColumna(columna) + "-img";
	}

	public void pageFirst(ActionEvent event) {
		if (sortField != null) {
			ordenar(event);
		}
		dataTable.setFirst(0);
	}

	public void pagePrevious(ActionEvent event) {
		if (sortField != null) {
			ordenar(event);
		}
		dataTable.setFirst(dataTable.getFirst() - dataTable.getRows());
	}

	public void pageNext(ActionEvent event) {
		if (sortField != null) {
			ordenar(event);
		}
		dataTable.setFirst(dataTable.getFirst() + dataTable.getRows());
	}

	public void pageLast(ActionEvent event) {
		if (sortField != null) {
			ordenar(event);
		}
		int count = dataTable.getRowCount();
		int rows = dataTable.getRows();
		dataTable.setFirst(count - ((count % rows != 0) ? count % rows : rows));
	}
	
	public int getCurrentPage() {
        int rows = dataTable.getRows();
        int first = dataTable.getFirst();
        int count = dataTable.getRowCount();
        return (count / rows) - ((count - first) / rows) + 1;
    }

    public int getTotalPages() {
        int rows = dataTable.getRows();
        int count = dataTable.getRowCount();
        System.out.println("count:::::"+count);
        System.out.println("rows:::::"+rows);
        if(rows==-1)
        	rows=count;
        return (count / rows) + ((count % rows != 0) ? 1 : 0);
    }
    
    public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public boolean isSortAscending() {
		return sortAscending;
	}

	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public Integer getNumeroRegistro() {
		
		String num = (String) getObjectSession(Constantes.PAGINA_SESSION);
		if(num!=null){
			this.numeroRegistro=Integer.parseInt(num);
		}else
			this.numeroRegistro=10;
		System.out.println("this.numeroRegistro:-:-:::"+this.numeroRegistro);
		
		return numeroRegistro;
	}

	public void setNumeroRegistro(Integer numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}
	
	public void cambiarPaginacion(ValueChangeEvent vce) {
		System.out.println("cambiarPaginacion");
		Object codigo = vce.getNewValue();
		numeroRegistro = (Integer)codigo;
		System.out.println("numeroRegistro:::"+numeroRegistro);
		addObjectSession(Constantes.PAGINA_SESSION, String.valueOf(this.numeroRegistro));
			ordenarPorAccionCombo();  	  
	}
}

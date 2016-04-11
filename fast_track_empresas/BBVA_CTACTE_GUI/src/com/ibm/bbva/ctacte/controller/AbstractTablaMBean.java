package com.ibm.bbva.ctacte.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTablaMBean extends AbstractMBean {
	
	private static final long serialVersionUID = -2749859184082501218L;
	private static final Logger LOG = LoggerFactory.getLogger(AbstractTablaMBean.class);
	
	private Integer rowsPerPage = 5;
	private Integer first = 0;
	private List<SelectItem> listOfSelectItems;
	
	public abstract int getRowCount();

	public void pageFirst(AjaxBehaviorEvent event) {
		this.first = 0;
	}

	public void pagePrevious(AjaxBehaviorEvent event) {
		this.first = first - rowsPerPage;
	}

	public void pageNext(AjaxBehaviorEvent event) {
		this.first = first + rowsPerPage;
	}

	public void pageLast(AjaxBehaviorEvent event) {
		int count = getRowCount();
		this.first = (count - ((count % rowsPerPage != 0) ? count % rowsPerPage : rowsPerPage));
	}
	
	public void goToPage(AjaxBehaviorEvent event) {
		Integer pagina = (Integer) event.getComponent().getAttributes().get("pagina");
		this.first = rowsPerPage*(pagina.intValue() - 1);
	}
	
	public void changeRowsPerPage(AjaxBehaviorEvent event) {
		this.first = first - (first % rowsPerPage);
	}
	
	public int getCurrentPage() {
		int count = getRowCount();
        return (count / rowsPerPage) - ((count - first) / rowsPerPage) + 1;
    }

    public int getTotalPages() {
		int count = getRowCount();
        return (count / rowsPerPage) + ((count % rowsPerPage != 0) ? 1 : 0);
    }

	public List<Integer> getPages() {
		int totalPages = getTotalPages();
		int currentPage = getCurrentPage();
		int inicio;
		int fin;
		// se muestra una ventana de hasta 10 páginas
		if (totalPages <= 10) {
			inicio = 1;
			fin = totalPages;
		} else {
			inicio = currentPage - 5;
			fin = currentPage + 4;
			if (inicio < 1) {
				fin = fin - inicio + 1;
				inicio = 1;
			} else if (fin > totalPages) {
				inicio = inicio - (fin - totalPages);
				fin = totalPages;
			}
		}
		List<Integer> pages = new ArrayList<Integer>();
		for (int i=inicio; i <= fin; i++) {
			pages.add(new Integer(i));
		}
		return pages;
	}

	public Integer getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(Integer rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public Integer getFirst() {
		return first;
	}

	public void setFirst(Integer first) {
		this.first = first;
	}

	public List<SelectItem> getListOfSelectItems() {
		return listOfSelectItems;
	}

	public void setListOfSelectItems(List<SelectItem> listOfSelectItems) {
		this.listOfSelectItems = listOfSelectItems;
	}

}

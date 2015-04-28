package com.ibm.bbva.controller;

import javax.faces.event.ActionEvent;

import com.ibm.bbva.ctacte.controller.AbstractMBean;

@SuppressWarnings("serial")
public abstract class AbstractTablaMBean extends AbstractMBean {
	
	private int paginaActual;
	private int totalPaginas;
	private int filasPorPagina;
	private String nombreSesionPagina;
	
	public void iniciar (String nombreSesionPagina, int numeroFilas, int filasPorPagina) {
		// para las filas de la tabla
		this.filasPorPagina = filasPorPagina;
		this.nombreSesionPagina = nombreSesionPagina;
		
		totalPaginas = (int)Math.ceil(((float)numeroFilas)/((float)filasPorPagina));
		
		Object pagAct = getObjectSession(nombreSesionPagina);
		if (pagAct == null) {
			paginaActual = 1;
			addObjectSession(nombreSesionPagina, paginaActual);
		} else {
			paginaActual = ((Integer)pagAct).intValue();
		}
		mostrarTabla (0, filasPorPagina);
	}

	public void siguiente (ActionEvent actionEvent) {
		if (paginaActual < totalPaginas) {
			paginaActual++;
			addObjectSession(nombreSesionPagina, paginaActual);
		}
		mostrarTabla ((paginaActual-1)*filasPorPagina, filasPorPagina);
	}
	
	public void anterior (ActionEvent actionEvent) {
		if (paginaActual > 1) {
			paginaActual--;
			addObjectSession(nombreSesionPagina, paginaActual);
		}
		mostrarTabla ((paginaActual-1)*filasPorPagina, filasPorPagina);
	}
	
	public void fin (ActionEvent actionEvent) {
		paginaActual = totalPaginas;
		addObjectSession(nombreSesionPagina, paginaActual);
		mostrarTabla ((paginaActual-1)*filasPorPagina, filasPorPagina);
	}
	
	public void inicio (ActionEvent actionEvent) {
		paginaActual = 1;
		addObjectSession(nombreSesionPagina, paginaActual);
		mostrarTabla (0, filasPorPagina);
	}
	
	protected abstract void mostrarTabla (int indiceInicioFila, int filas);
	
	public int getPaginaActual() {
		return paginaActual;
	}
	
	public void setPaginaActual(int paginaActual) {
		this.paginaActual = paginaActual;
	}
	
	public int getTotalPaginas() {
		return totalPaginas;
	}
	
	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	public int getFilas() {
		return filasPorPagina;
	}

	public void setFilas(int filas) {
		this.filasPorPagina = filas;
	}

}

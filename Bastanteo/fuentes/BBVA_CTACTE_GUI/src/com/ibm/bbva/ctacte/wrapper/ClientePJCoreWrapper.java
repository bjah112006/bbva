package com.ibm.bbva.ctacte.wrapper;

import java.io.Serializable;

import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;

public class ClientePJCoreWrapper implements Serializable {

	private static final long serialVersionUID = -6385908784818576656L;

	// para la Post condicion "Cliente PJ Seleccionado"
	private boolean seleccionado;
	private ClientePJCore cliente;

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public ClientePJCore getCliente() {
		return cliente;
	}

	public void setCliente(ClientePJCore cliente) {
		this.cliente = cliente;
	}
	
}

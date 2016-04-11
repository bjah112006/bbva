package com.ibm.bbva.ctacte.bean.auxiliares;

import java.io.Serializable;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Oficina;

public class EmpleadoAuxiliar implements Serializable {

	private static final long serialVersionUID = 8262647881227572554L;
	private Empleado empleado;
	private boolean esActivo;
	private boolean esOficinaNueva;
	private Oficina OficinaAntigua;
	private Oficina oficinaNueva;

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	

	public boolean isEsActivo() {
		return esActivo;
	}

	public void setEsActivo(boolean esActivo) {
		this.esActivo = esActivo;
	}

	public boolean isEsOficinaNueva() {
		return esOficinaNueva;
	}

	public void setEsOficinaNueva(boolean esOficinaNueva) {
		this.esOficinaNueva = esOficinaNueva;
	}

	public Oficina getOficinaAntigua() {
		return OficinaAntigua;
	}

	public void setOficinaAntigua(Oficina oficinaAntigua) {
		OficinaAntigua = oficinaAntigua;
	}

	public Oficina getOficinaNueva() {
		return oficinaNueva;
	}

	public void setOficinaNueva(Oficina oficinaNueva) {
		this.oficinaNueva = oficinaNueva;
	}

}

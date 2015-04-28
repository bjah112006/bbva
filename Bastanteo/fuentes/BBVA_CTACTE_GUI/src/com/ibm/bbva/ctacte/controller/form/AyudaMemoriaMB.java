package com.ibm.bbva.ctacte.controller.form;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.DatosAyudaMemoriaMB;
import com.ibm.bbva.ctacte.controller.comun.DatosClienteMB;
import com.ibm.bbva.ctacte.controller.comun.DatosGeneralesMB;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosAyudaMemoria;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosCliente;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosGenerales;

@ManagedBean (name="ayudaMemoria")
@ViewScoped
public class AyudaMemoriaMB extends AbstractMBean 
 implements IDatosGenerales, IDatosCliente, IDatosAyudaMemoria{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(RegistrarNuevoBastanteoMB.class);
	
	private DatosClienteMB datosCliente;
	private DatosGeneralesMB datosGenerales;
	private DatosAyudaMemoriaMB datosAyudaMemoria;
	
	public void setDatosCliente(DatosClienteMB datosCliente) {
		this.datosCliente = datosCliente;		
	}
	public void setDatosGenerales(DatosGeneralesMB datosGenerales) {
		this.datosGenerales = datosGenerales;
	}
	public void setAyudaMemoria(DatosAyudaMemoriaMB datosAyudaMemoria) {
		this.datosAyudaMemoria = datosAyudaMemoria;		
	}
}

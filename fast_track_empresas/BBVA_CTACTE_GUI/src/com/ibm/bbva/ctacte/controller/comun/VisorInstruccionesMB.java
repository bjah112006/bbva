package com.ibm.bbva.ctacte.controller.comun;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IVisorInstrucciones;
import com.ibm.bbva.ctacte.controller.form.EjecutarConfirmarModificatoriaMB;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name="visorInstrucciones")
@ViewScoped
public class VisorInstruccionesMB extends AbstractMBean{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PieEjecutarConfirmarModificatoriasMB.class);
	@ManagedProperty(value = "#{ejecutarConfirmarModificatoria}")
	private EjecutarConfirmarModificatoriaMB ejecutarConfirmarModificar;
	private IVisorInstrucciones iVisorInstrucciones;
	private String strInstrucciones;
	private Expediente expediente;
	private int idTarea;
	private boolean disInstrucciones;
	
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		if ("formEjecutarConfirmarModificatoria".endsWith(pagina)) {
			LOG.info("Entro al if");
			iVisorInstrucciones = ejecutarConfirmarModificar;
			LOG.info("Salio del if");
		}
		
		expediente=(Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);	
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_EJECUTAR_CONFIRMAR_MODIFICATORIA.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}
		}
		LOG.info("Instrucciones Bastanteo"+expediente.getInstruccionesBastanteo());
		disInstrucciones=true;
		/*strInstrucciones= new String(expediente.getInstruccionesBastanteo());
		LOG.info("strInstrucciones"+strInstrucciones);*/
		byte[] byteInstrucciones = expediente.getInstruccionesBastanteo();
		strInstrucciones=byteInstrucciones==null ? null : new String (byteInstrucciones);
		LOG.info("strInstrucciones"+strInstrucciones);
		expediente.setInstruccionesBastanteo(strInstrucciones==null ? null : strInstrucciones.getBytes());
		//strInstrucciones=new String (expediente.getInstruccionesBastanteo());// para recuperar el valor q grabe del BLOB
		LOG.info("Instrucciones"+strInstrucciones);
		iVisorInstrucciones.setVisorInstrucciones(this);
		
		
		// valor= new String(expediente.getResultadoRevocatoria()); para recuperar el valor q grabe del BLOB
	}

	public boolean isDisInstrucciones() {
		return disInstrucciones;
	}

	public void setDisInstrucciones(boolean disInstrucciones) {
		this.disInstrucciones = disInstrucciones;
	}

	public EjecutarConfirmarModificatoriaMB getEjecutarConfirmarModificar() {
		return ejecutarConfirmarModificar;
	}

	public void setEjecutarConfirmarModificar(
			EjecutarConfirmarModificatoriaMB ejecutarConfirmarModificar) {
		this.ejecutarConfirmarModificar = ejecutarConfirmarModificar;
	}


	public IVisorInstrucciones getIVisorInstrucciones() {
		return iVisorInstrucciones;
	}

	public void setIVisorInstrucciones(IVisorInstrucciones visorInstrucciones) {
		iVisorInstrucciones = visorInstrucciones;
	}

	
	public String getStrInstrucciones() {
		return strInstrucciones;
	}

	public void setStrInstrucciones(String strInstrucciones) {
		this.strInstrucciones = strInstrucciones;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}
	
	

}

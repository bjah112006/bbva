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
import com.ibm.bbva.ctacte.controller.comun.interf.IVisorDocumentos;
import com.ibm.bbva.ctacte.controller.form.VerificarResultadoTramiteMB;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name="visorDocumento")
@ViewScoped
public class VisorDocumentosMB extends AbstractMBean{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(VisorDocumentosMB.class);
	@ManagedProperty(value="#{verificarResultadoTramite}")
	private VerificarResultadoTramiteMB verificarResultadoTramite;
	private IVisorDocumentos iVisorDocumentos;
	private Expediente expediente;
	private boolean disInstrucciones;
	private boolean disResultado;
	private boolean disDictamen;
	private String strDictamen;
	private String strInstrucciones;
	private String strResultado;
	private int idTarea;
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("PaginaActual {}",pagina);
		if ("formVerificarResultadoTramite".equals(pagina)){
			LOG.info("entro a la pagina!!!!!!!");
			iVisorDocumentos=verificarResultadoTramite;			
		}
		expediente=(Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);	
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_VERIFICAR_RESULTADO_TRAMITE.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}
		}
		disInstrucciones=true;
		disResultado=true;
		disDictamen=true;
		byte[] byteDictamen = expediente.getDictamenBastanteo();
		strDictamen = byteDictamen==null ? null : new String(byteDictamen);// para recuperar el valor q grabe del BLOB
		LOG.info("Dictamen"+strDictamen);
		byte[] byteInstrucciones = expediente.getInstruccionesBastanteo();
		strInstrucciones = byteInstrucciones==null ? null : new String (byteInstrucciones);
		byte[] byteResultado = expediente.getResultadoRevocatoria();
		strResultado = byteResultado==null ? null : new String (byteResultado);
		// valor= new String(expediente.getResultadoRevocatoria()); para recuperar el valor q grabe del BLOB
	}	
	
	public VerificarResultadoTramiteMB getVerificarResultadoTramite() {
		return verificarResultadoTramite;
	}

	public void setVerificarResultadoTramite(
			VerificarResultadoTramiteMB verificarResultadoTramite) {
		this.verificarResultadoTramite = verificarResultadoTramite;
	}

	public IVisorDocumentos getIVisorDocumentos() {
		return iVisorDocumentos;
	}

	public void setIVisorDocumentos(IVisorDocumentos visorDocumentos) {
		iVisorDocumentos = visorDocumentos;
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

	public String getStrDictamen() {
		return strDictamen;
	}

	public void setStrDictamen(String strDictamen) {
		this.strDictamen = strDictamen;
	}

	public String getStrInstrucciones() {
		return strInstrucciones;
	}

	public void setStrInstrucciones(String strInstrucciones) {
		this.strInstrucciones = strInstrucciones;
	}

	public String getStrResultado() {
		return strResultado;
	}

	public void setStrResultado(String strResultado) {
		this.strResultado = strResultado;
	}

	public boolean isDisInstrucciones() {
		return disInstrucciones;
	}

	public void setDisInstrucciones(boolean disInstrucciones) {
		this.disInstrucciones = disInstrucciones;
	}

	public boolean isDisResultado() {
		return disResultado;
	}

	public void setDisResultado(boolean disResultado) {
		this.disResultado = disResultado;
	}

	public boolean isDisDictamen() {
		return disDictamen;
	}

	public void setDisDictamen(boolean disDictamen) {
		this.disDictamen = disDictamen;
	}
	
	

}

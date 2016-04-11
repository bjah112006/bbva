package com.ibm.bbva.ctacte.controller.form;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="consultaPoderes")
@ViewScoped
public class ConsultaPoderesMB extends AbstractMBean {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ConsultaPoderesMB.class);
	
	private String urlSFPPoderes;

	@PostConstruct
	public void iniciar() {
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		urlSFPPoderes = ConstantesBusiness.PAGINA_CONSULTA_PODERES_IBM + "?registro=" + empleado.getCodigo();
		LOG.info("urlSFPPoderes: "+urlSFPPoderes);
	}
	
	public String getUrlSFPPoderes() {
		return urlSFPPoderes;
	}

	public void setUrlSFPPoderes(String urlSFPPoderes) {
		this.urlSFPPoderes = urlSFPPoderes;
	}

}

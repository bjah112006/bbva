package com.ibm.bbva.ctacte.controller.form;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.interf.IIdentifiquePJOperacion3;

@ManagedBean (name="registrarModificatorias")
@ViewScoped
public class RegistrarModificatoriasMB extends AbstractMBean implements IIdentifiquePJOperacion3 {

	private static final Logger LOG = LoggerFactory.getLogger(RegistrarModificatoriasMB.class);
	
	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		
	}
}

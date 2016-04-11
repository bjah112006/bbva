package com.ibm.bbva.ctacte.controller.comun;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.interf.IIdentifiquePJOperacion3;
import com.ibm.bbva.ctacte.controller.form.RegistrarModificatoriasMB;

@ManagedBean (name="identifiquePJOperacion3")
@ViewScoped
public class IdentifiquePJOperacion3MB extends AbstractMBean {

	private static final Logger LOG = LoggerFactory.getLogger(IdentifiquePJOperacion3MB.class);
	
	@ManagedProperty (value="#{registrarModificatorias}")
	private RegistrarModificatoriasMB registrarModificatorias;
	
	private IIdentifiquePJOperacion3 managedBean;
	
	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		String pagina = getNombrePrincipal();
		if ("formRegistrarModificatorias".equals(pagina)) {
			managedBean = registrarModificatorias;
		}
	}
}

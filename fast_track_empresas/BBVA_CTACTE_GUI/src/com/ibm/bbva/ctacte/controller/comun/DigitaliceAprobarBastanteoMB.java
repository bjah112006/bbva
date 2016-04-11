package com.ibm.bbva.ctacte.controller.comun;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.form.RevisarAprobarBastanteoMB;

@ManagedBean (name="digitaliceAprobarBastanteo")
@ViewScoped
public class DigitaliceAprobarBastanteoMB extends AbstractMBean {
	
	private static final long serialVersionUID = 8512389245523752041L;
	private static final Logger LOG = LoggerFactory.getLogger(DigitaliceAprobarBastanteoMB.class);
	
	@ManagedProperty(value="#{revisarAprobarBastanteo}")
	private RevisarAprobarBastanteoMB revisarAprobarBastanteo;


}

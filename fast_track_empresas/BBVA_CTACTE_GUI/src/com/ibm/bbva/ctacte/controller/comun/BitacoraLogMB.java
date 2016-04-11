package com.ibm.bbva.ctacte.controller.comun;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Log;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IBitacoraLog;
import com.ibm.bbva.ctacte.controller.form.LogExpedienteMB;
import com.ibm.bbva.ctacte.dao.LogDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="bitacoraLog")
@ViewScoped
public class BitacoraLogMB extends AbstractMBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(BitacoraLogMB.class);
	
	@ManagedProperty (value="#{logExpediente}")
	private LogExpedienteMB logExpedienteMB;
	@EJB
	private LogDAO logDAO;
	
	private List<Log> listaLogsExp;
	private IBitacoraLog managedBean;

	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		int idExpediente = 0;
		if (!(expediente == null || expediente.getId() == null)) {
		   idExpediente = expediente.getId();
		}
		
		listaLogsExp = logDAO.findByExpdiente(idExpediente);
		
		
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		//if (ConstantesAdmin.FORM_OBSERVACIONES_EXPEDIENTE.endsWith(pagina)) {
			managedBean = logExpedienteMB;
					
		//}
		
		managedBean.setBitacoraLog(this);
	}

	public LogExpedienteMB getLogExpedienteMB() {
		return logExpedienteMB;
	}

	public void setLogExpedienteMB(LogExpedienteMB logExpedienteMB) {
		this.logExpedienteMB = logExpedienteMB;
	}

	public List<Log> getListaLogsExp() {
		return listaLogsExp;
	}

	public void setListaLogsExp(List<Log> listaLogsExp) {
		this.listaLogsExp = listaLogsExp;
	}



}

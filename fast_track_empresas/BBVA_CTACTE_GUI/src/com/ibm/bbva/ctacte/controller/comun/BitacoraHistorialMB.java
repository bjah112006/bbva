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
import com.ibm.bbva.ctacte.bean.Historial;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IBitacoraHistorial;
import com.ibm.bbva.ctacte.controller.form.HistorialExpedienteMB;
import com.ibm.bbva.ctacte.dao.HistorialDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="bitacoraHistorial")
@ViewScoped
public class BitacoraHistorialMB extends AbstractMBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(BitacoraHistorialMB.class);

	@ManagedProperty ("#{historialExpediente}")
	private HistorialExpedienteMB historialExpMB;
	private List<Historial> listaHistorialExp;
	private IBitacoraHistorial managedBean;
	@EJB
	private HistorialDAO historialDAO;

	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		int idExpediente = 0;
		if (!(expediente == null || expediente.getId() == null)) {
		   idExpediente = expediente.getId();
		}
		
		listaHistorialExp = historialDAO.findByExpdiente(idExpediente);
		
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
    	managedBean = historialExpMB;
		managedBean.setBitacoraHistorial(this);
	}

	public HistorialExpedienteMB getHistorialExpMB() {
		return historialExpMB;
	}

	public void setHistorialExpMB(HistorialExpedienteMB historialExpMB) {
		this.historialExpMB = historialExpMB;
	}

	public List<Historial> getListaHistorialExp() {
		return listaHistorialExp;
	}

	public void setListaHistorialExp(List<Historial> listaHistorialExp) {
		this.listaHistorialExp = listaHistorialExp;
	}
	
	

}

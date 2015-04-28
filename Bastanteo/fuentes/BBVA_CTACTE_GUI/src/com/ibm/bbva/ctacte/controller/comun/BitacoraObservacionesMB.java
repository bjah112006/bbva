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
import com.ibm.bbva.ctacte.controller.comun.interf.IBitacoraObservaciones;
import com.ibm.bbva.ctacte.controller.form.ObservacionesMB;
import com.ibm.bbva.ctacte.dao.HistorialDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="bitacoraObservaciones")
@ViewScoped
public class BitacoraObservacionesMB extends AbstractMBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(BitacoraObservacionesMB.class);
	
	@ManagedProperty ("#{observacionesExp}")
	private ObservacionesMB observacionesMB;
	private List<Historial> listaObservacionesExp;
	private IBitacoraObservaciones managedBean;
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
		
		listaObservacionesExp = historialDAO.findByExpdiente(idExpediente);
		
		
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);		
		managedBean = observacionesMB;		
		managedBean.setBitacoraObservaciones(this);
	}

	public ObservacionesMB getObservacionesMB() {
		return observacionesMB;
	}

	public void setObservacionesMB(ObservacionesMB observacionesMB) {
		this.observacionesMB = observacionesMB;
	}

	public List<Historial> getListaObservacionesExp() {
		return listaObservacionesExp;
	}

	public void setListaObservacionesExp(List<Historial> listaObservacionesExp) {
		this.listaObservacionesExp = listaObservacionesExp;
	}
	
	

}

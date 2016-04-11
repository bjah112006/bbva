package com.ibm.mant.mantenimiento;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.EstudioAbogado;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.dao.EstudioAbogadoDAO;

@ManagedBean(name="asignarCargaEstudio")
@ViewScoped
public class AsignarCargaEstudioUI extends AbstractMBean {
	
	private static final long serialVersionUID = 8108898070215612327L;
	
	private static final Logger LOG = LoggerFactory.getLogger(AsignarCargaEstudioUI.class);
	
	private List<EstudioAbogado> lstEstudioAbogado;
	
	@EJB
	private EstudioAbogadoDAO estudioAbogadoDAO;
	
	@PostConstruct
	public void init() {
		LOG.info("init()");
		
		load();
	}
	
	private void load() {
		lstEstudioAbogado = estudioAbogadoDAO.findListaEstudios();
	}
	
	public String guardar() {
		int sumaTotal = 0;
		for (EstudioAbogado estudio : lstEstudioAbogado) {
			if (estudio.getPorcentajeCarga() == null) {
				estudio.setPorcentajeCarga(0);
			}
			sumaTotal = sumaTotal + estudio.getPorcentajeCarga().intValue();
		}
		if (sumaTotal != 100) {
			mensajeError(null, "La suma total de los porcentajes de carga debe ser 100.");
		} else {
			try {
				for (EstudioAbogado estudio : lstEstudioAbogado) {
					estudioAbogadoDAO.update(estudio);
				}
				load();
			} catch (Exception e) {
				mensajeError(null, "Error en BBDD al intentar ejecutar la operación.");
			}
		}
		
		return null;
	}
	
	public String cancelar() {
		return "/mantenimiento/seleccionarTabla?faces-redirect=true";
	}

	public List<EstudioAbogado> getLstEstudioAbogado() {
		return lstEstudioAbogado;
	}

	public void setLstEstudioAbogado(List<EstudioAbogado> lstEstudioAbogado) {
		this.lstEstudioAbogado = lstEstudioAbogado;
	}

}

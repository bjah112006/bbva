package com.ibm.bbva.ctacte.controller.comun;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.AyudaMemoria;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosAyudaMemoria;
import com.ibm.bbva.ctacte.controller.form.AyudaMemoriaMB;
import com.ibm.bbva.ctacte.dao.AyudaMemoriaDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="datosAyudaMemoria")
@ViewScoped
public class DatosAyudaMemoriaMB extends AbstractMBean {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DatosGeneralesMB.class);
	@ManagedProperty (value="#{ayudaMemoria}")
	private AyudaMemoriaMB ayudaMemoria;
	private IDatosAyudaMemoria managedBean;
	private AyudaMemoria ayudaMemoriaBean;
	private List<AyudaMemoria> listaAyudaMemoriaBean;
	private Expediente expediente;
	private List<AyudaMemoria> listAyudaMem;
	private boolean habilitaGrabacion=false;
	private Empleado empleado;
	private String codResponsable;
	
	@EJB
	private AyudaMemoriaDAO ayudaMemoriaDAO;
	@EJB
	private EmpleadoDAO empleadoDAO;
	
	public String getCodResponsable() {
		return codResponsable;
	}

	public void setCodResponsable(String codResponsable) {
		this.codResponsable = codResponsable;
	}

	public boolean isHabilitaGrabacion() {
		return habilitaGrabacion;
	}

	public void setHabilitaGrabacion(boolean habilitaGrabacion) {
		this.habilitaGrabacion = habilitaGrabacion;
	}

	public List<AyudaMemoria> getListaAyudaMemoriaBean() {
		return listaAyudaMemoriaBean;
	}

	public void setListaAyudaMemoriaBean(List<AyudaMemoria> listaAyudaMemoriaBean) {
		this.listaAyudaMemoriaBean = listaAyudaMemoriaBean;
	}

	public AyudaMemoriaMB getAyudaMemoria() {
		return ayudaMemoria;
	}

	public void setAyudaMemoria(AyudaMemoriaMB ayudaMemoria) {
		this.ayudaMemoria = ayudaMemoria;
	}

	public AyudaMemoria getAyudaMemoriaBean() {
		return ayudaMemoriaBean;
	}

	public void setAyudaMemoriaBean(AyudaMemoria ayudaMemoriaBean) {
		this.ayudaMemoriaBean = ayudaMemoriaBean;
	}

	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");		
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		empleado = (Empleado)Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		codResponsable = (String) Util.getObjectSession(ConstantesAdmin.RESPONSABLE_SESION);
		
		Empleado empl = empleadoDAO.findByCodigo(codResponsable);
		
		if (expediente != null && expediente.getId() != null) {
			listaAyudaMemoriaBean = ayudaMemoriaDAO.findByExpediente(expediente.getId());
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("tamaño de la lista-->"+listaAyudaMemoriaBean.size());
		} else {
			listaAyudaMemoriaBean = new ArrayList<AyudaMemoria> ();
		}
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		//if (ConstantesAdmin.FORM_AYUDA_MEMORIA.contains(pagina)) {
			managedBean = ayudaMemoria;
			ayudaMemoriaBean = new AyudaMemoria();
			listAyudaMem = new ArrayList<AyudaMemoria>();
		//}
		managedBean.setAyudaMemoria(this);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("per1 - per2"+empleado.getPerfil().getCodigo()+" - "+empl.getPerfil().getCodigo());
		//if (empl!=null && empleado.getPerfil().getCodigo().equals(empl.getPerfil().getCodigo())) {
		if (empl!=null && (empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR) ||
				empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GERENTE_OFICINA))) {
		   this.habilitaGrabacion=false;
		}else{
		   this.habilitaGrabacion=true;
		}
	}
	
	public void guardarAyudaMemoria(AjaxBehaviorEvent event){
		LOG.info("guardarAyudaMemoria()");
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		
		ayudaMemoriaBean.setExpediente(expediente);
		ayudaMemoriaBean.setFechaRegistro(new Date());
		ayudaMemoriaBean.setEmpleado(empleado);
		ayudaMemoriaBean.setPerfil(empleado.getPerfil());
		
		if (!(ayudaMemoriaBean.getComentario()==null || ayudaMemoriaBean.getComentario().trim().equals(""))) {
		
			if (!(expediente == null || expediente.getId() == null)) {		
			   ayudaMemoriaDAO.save(ayudaMemoriaBean);
			}else{			
				listAyudaMem.add(ayudaMemoriaBean);
			}
			listaAyudaMemoriaBean.add(ayudaMemoriaBean);			
		}
		ayudaMemoriaBean = new AyudaMemoria ();
	}

	public List<AyudaMemoria> getListAyudaMem() {
		return listAyudaMem;
	}

	public void setListAyudaMem(List<AyudaMemoria> listAyudaMem) {
		this.listAyudaMem = listAyudaMem;
	}
}

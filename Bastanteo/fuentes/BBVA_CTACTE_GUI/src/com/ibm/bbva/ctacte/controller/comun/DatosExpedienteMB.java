package com.ibm.bbva.ctacte.controller.comun;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosExpediente;
import com.ibm.bbva.ctacte.controller.form.EjecutarConfirmarModificatoriaMB;
import com.ibm.bbva.ctacte.controller.form.GestionarCobroComisionMB;
import com.ibm.bbva.ctacte.controller.form.VerificarResultadoTramiteMB;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="datosExpediente")
@ViewScoped
public class DatosExpedienteMB extends AbstractMBean  {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DatosExpedienteMB.class);
	@ManagedProperty (value="#{ejecutarConfirmarModificatoria}")	
	private EjecutarConfirmarModificatoriaMB ejecutarConfirmarModificatoria;
	@ManagedProperty (value="#{verificarResultadoTramite}")
	private VerificarResultadoTramiteMB verificarResultadoTramite;	
	@ManagedProperty (value="#{gestionarCobroComision}")
	private GestionarCobroComisionMB gestionarCobroComision;	

	private IDatosExpediente iDatosExpediente;	
	private Expediente expediente;
	private int idTarea;
	private boolean mostrarLinkSFP = false;
	private String urlSFPPoderes;

	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");	
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		if ("formEjecutarConfirmarModificatoria".equals(pagina)) {
			iDatosExpediente = ejecutarConfirmarModificatoria;
			iDatosExpediente.setIDatosExpediente(this);
		}else if ("formVerificarResultadoTramite".equals(pagina)){			
			iDatosExpediente=verificarResultadoTramite;			
			iDatosExpediente.setIDatosExpediente(this);
			mostrarLinkSFP = true;
		}else if ("formGestionarCobroComision".equals(pagina)){			
			iDatosExpediente=gestionarCobroComision;			
			iDatosExpediente.setIDatosExpediente(this);
		}
		expediente = (Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);	
		/*for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_EJECUTAR_CONFIRMAR_MODIFICATORIA.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}else if (ConstantesAdmin.FORM_VERIFICAR_RESULTADO_TRAMITE.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea=expedienteTarea.getTarea().getId().intValue();
				break;
			}
		}*/
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		urlSFPPoderes = ConstantesBusiness.PAGINA_CONSULTA_PODERES_IBM + "?registro=" + empleado.getCodigo() + "&codcentral=" + expediente.getCliente().getCodigoCentral();
		LOG.info("urlSFPPoderes: "+urlSFPPoderes);
	}

	public GestionarCobroComisionMB getGestionarCobroComision() {
		return gestionarCobroComision;
	}

	public void setGestionarCobroComision(
			GestionarCobroComisionMB gestionarCobroComision) {
		this.gestionarCobroComision = gestionarCobroComision;
	}

	public EjecutarConfirmarModificatoriaMB getEjecutarConfirmarModificatoria() {
		return ejecutarConfirmarModificatoria;
	}

	public void setEjecutarConfirmarModificatoria(
			EjecutarConfirmarModificatoriaMB ejecutarConfirmarModificatoria) {
		this.ejecutarConfirmarModificatoria = ejecutarConfirmarModificatoria;
	}

	public VerificarResultadoTramiteMB getVerificarResultadoTramite() {
		return verificarResultadoTramite;
	}

	public void setVerificarResultadoTramite(
			VerificarResultadoTramiteMB verificarResultadoTramite) {
		this.verificarResultadoTramite = verificarResultadoTramite;
	}

	public IDatosExpediente getIDatosExpediente() {
		return iDatosExpediente;
	}

	public void setIDatosExpediente(IDatosExpediente datosExpediente) {
		iDatosExpediente = datosExpediente;
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
	
	public boolean isMostrarLinkSFP() {
		return mostrarLinkSFP;
	}

	public void setMostrarLinkSFP(boolean mostrarLinkSFP) {
		this.mostrarLinkSFP = mostrarLinkSFP;
	}

	public String getUrlSFPPoderes() {
		return urlSFPPoderes;
	}

	public void setUrlSFPPoderes(String urlSFPPoderes) {
		this.urlSFPPoderes = urlSFPPoderes;
	}
	
}

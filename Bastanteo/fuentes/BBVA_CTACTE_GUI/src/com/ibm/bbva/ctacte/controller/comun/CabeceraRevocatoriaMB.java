package com.ibm.bbva.ctacte.controller.comun;


import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.ICabeceraRevocatoria;
import com.ibm.bbva.ctacte.controller.form.AprobarRevocatoriaMB;
import com.ibm.bbva.ctacte.controller.form.RevisarAprobarBastanteoMB;
import com.ibm.bbva.ctacte.controller.form.RevisarEjecutarRevocatoriaMB;
import com.ibm.bbva.ctacte.controller.form.VerificarRealizarBastanteoMB;
import com.ibm.bbva.ctacte.util.Util;


@ManagedBean(name="cabeceraRevocatoria")
@ViewScoped
public class CabeceraRevocatoriaMB extends AbstractMBean{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CabeceraRevocatoriaMB.class);
	@ManagedProperty (value="#{aprobarRevocatoria}")
	private AprobarRevocatoriaMB aprobarRevocatoria;
	@ManagedProperty (value="#{revisarEjecutarRevocatoria}")
	private RevisarEjecutarRevocatoriaMB revisarEjecutarRevocatoria;
	@ManagedProperty (value="#{verificarRealizarBastanteo}")
	private VerificarRealizarBastanteoMB verificarRealizarBastanteo;
	@ManagedProperty(value="#{revisarAprobarBastanteo}")
	private RevisarAprobarBastanteoMB revisarAprobarBastanteo;
	private ICabeceraRevocatoria iCabeceraRevocatoria;
	private Expediente expediente;
	private Empleado empleado;
	private int idTarea;
	private String urlSFPFirmas;
	private String mensajeContent="";
	private String pagina;
	@EJB
	private ClienteBusiness clienteBusiness;
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		pagina=getNombrePrincipal();
		LOG.info("PaginaActual{}",pagina);
		if ("formAprobarRevocatoria".equals(pagina)){
			iCabeceraRevocatoria=aprobarRevocatoria;
			
			// mensaje faltan documentos content
			ExpedienteCC expedienteCC = (ExpedienteCC) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION);
			if (faltanDocumentosContent(expedienteCC)) {
				mensajeContent = ConstantesAdmin.MSG_ERROR_FALTA_DOC_CONTENT;
			}
		}else if ("formRevisarEjecutarRevocatoria".equals(pagina)){
			iCabeceraRevocatoria=revisarEjecutarRevocatoria;
		}else if ("formVerificarRealizarBastanteo".equals(pagina)){
			iCabeceraRevocatoria=verificarRealizarBastanteo;
		}else if ("formRevisarAprobarBastanteo".equals(pagina)){
			iCabeceraRevocatoria=revisarAprobarBastanteo;
		}		
		expediente = (Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		empleado = (Empleado)Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_APROBAR_REVOCATORIA.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}else if (ConstantesAdmin.FORM_REVISAR_EJECUTAR_REVOCATORIA.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}else if (ConstantesAdmin.FORM_REVISAR_APROBAR_BASTANTEO.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}else if (ConstantesAdmin.FORM_VERIFICAR_REALIZAR_BASTANTEO.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea=expedienteTarea.getTarea().getId().intValue();
			}
		}
		
		//ParametrosSistema parametros = ParametrosSistema.getInstance();
		//Properties properties = parametros.getProperties(ParametrosSistema.CONF);
		String strParametrosSFP = "?" + ConstantesBusiness.SFP_PARAMETRO_USUARIO + empleado.getCodigo() + 
				"&" + ConstantesBusiness.SFP_PARAMETRO_EXPEDIENTE + expediente.getId() + 
				"&" + ConstantesBusiness.SFP_PARAMETRO_ALEATORIO + 
				(new Random().nextInt(ConstantesBusiness.SFP_PARAMETRO_MAXIMO_ALEATORIO) +
						ConstantesBusiness.SFP_PARAMETRO_MINIMO_ALEATORIO);
		urlSFPFirmas = ConstantesBusiness.PAGINA_REALIZAR_BASTANTEO_IBM + strParametrosSFP;		 
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Link SFP: " + urlSFPFirmas);
		//iCabeceraRevocatoria.setICabeceraRevocatoria(this);
	}
	
	public void actualizarParticipes(AjaxBehaviorEvent event) {
		LOG.info("public void actualizarParticipes()");
		
		try {
			List<ClientePJCore> listaCliente = clienteBusiness.buscarCodigoCentral(expediente.getCliente().getCodigoCentral(), empleado.getCodigo());
			if (listaCliente.isEmpty()) {
				mensajeErrorPrincipal("btnActualizarParticipes", "El cliente no existe.");
			} else if (listaCliente.size()==1) {
				DatosClientePJCore datos = clienteBusiness.obtenerDatosClientePJ(listaCliente.get(0).getCodigoCentral(), empleado.getCodigo());
				listaCliente.get(0).setDatosCore(datos);
				
				boolean resultado = Util.actualizarParticipes(expediente, listaCliente.get(0));
				if (resultado) {
					mensajeInfoPrincipal("btnActualizarParticipes", "Los partícipes fueron actualizados.");
				} else {
					mensajeErrorPrincipal("btnActualizarParticipes", "Ocurrió un error al actualizar los partícipes.");
				}
			} else {
				mensajeErrorPrincipal("btnActualizarParticipes", "Ocurrió un error al actualizar los partícipes.");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			mensajeErrorPrincipal("btnActualizarParticipes", "Ocurrió un error al actualizar los partícipes.");
		}
	}

	public AprobarRevocatoriaMB getAprobarRevocatoria() {
		return aprobarRevocatoria;
	}

	public void setAprobarRevocatoria(AprobarRevocatoriaMB aprobarRevocatoria) {
		this.aprobarRevocatoria = aprobarRevocatoria;
	}

	public ICabeceraRevocatoria getICabeceraRevocatoria() {
		return iCabeceraRevocatoria;
	}

	public void setICabeceraRevocatoria(ICabeceraRevocatoria cabeceraRevocatoria) {
		iCabeceraRevocatoria = cabeceraRevocatoria;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public RevisarEjecutarRevocatoriaMB getRevisarEjecutarRevocatoria() {
		return revisarEjecutarRevocatoria;
	}

	public void setRevisarEjecutarRevocatoria(
			RevisarEjecutarRevocatoriaMB revisarEjecutarRevocatoria) {
		this.revisarEjecutarRevocatoria = revisarEjecutarRevocatoria;
	}

	public VerificarRealizarBastanteoMB getVerificarRealizarBastanteo() {
		return verificarRealizarBastanteo;
	}

	public void setVerificarRealizarBastanteo(
			VerificarRealizarBastanteoMB verificarRealizarBastanteo) {
		this.verificarRealizarBastanteo = verificarRealizarBastanteo;
	}	
	public RevisarAprobarBastanteoMB getRevisarAprobarBastanteo() {
		return revisarAprobarBastanteo;
	}

	public void setRevisarAprobarBastanteo(
			RevisarAprobarBastanteoMB revisarAprobarBastanteo) {
		this.revisarAprobarBastanteo = revisarAprobarBastanteo;
	}

	public String getUrlSFPFirmas() {
		return urlSFPFirmas;
	}

	public void setUrlSFPFirmas(String urlSFPFirmas) {
		this.urlSFPFirmas = urlSFPFirmas;
	}

	public String getMensajeContent() {
		return mensajeContent;
	}

	public void setMensajeContent(String mensajeContent) {
		this.mensajeContent = mensajeContent;
	}

	public String getPagina() {
		return pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	
}

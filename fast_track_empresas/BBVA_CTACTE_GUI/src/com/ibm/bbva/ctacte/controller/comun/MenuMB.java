package com.ibm.bbva.ctacte.controller.comun;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="menu")
@SessionScoped
public class MenuMB extends AbstractMBean {

	private static final Logger LOG = LoggerFactory.getLogger(MenuMB.class);
	private static final long serialVersionUID = 1L;
	private boolean renderedBanMant = false;
	private boolean renderedVistaUnica = false;
	private boolean renderedReporteExpediente = false;
	private boolean renderedReporteAns = false;
	private boolean renderedRegConsuRecla=false;
	private boolean renderedVerEstConsuRecla=false;
	private boolean renderedHorario=false;
	private String paginaInicio;

	public MenuMB() {
		paginaInicio = (String) ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).get(ConstantesParametros.PAGINA_INICIO_CTACTE);
		restringuirAcceso();
	}

	public String registarExpediente () {
		Expediente expediente = Util.crearExpedienteCU1();
		Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);
		ExpedienteCC expedienteCC = new ExpedienteCC ();
		Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION, expedienteCC);
		removeObjectSession(Constantes.BANDJ_TRABAJO_BASTANTEO);
		removeObjectSession(Constantes.BANDJ_FILTRO_BASTANTEO);
		return redirect("/registrarNuevoBastanteo/formRegistrarNuevoBastanteo");
	}

	public String mantenimiento () {
		addObjectSession(ConstantesAdmin.TIPO_TABLA_SESION, "100");
		removeObjectSession(Constantes.BANDJ_TRABAJO_BASTANTEO);
		removeObjectSession(Constantes.BANDJ_FILTRO_BASTANTEO);
		return redirect("/mantenimiento/seleccionarTabla");
	}

	public String bandejaTrabajo () {
		removeObjectSession(Constantes.BANDJ_TRABAJO_BASTANTEO);
		removeObjectSession(Constantes.BANDJ_FILTRO_BASTANTEO);
		return redirect("/bandeja/bandeja");
	}

	public String irHorarioCC(){
		return redirect("/horario/formHorarioCC");
	}


	public boolean isMostrarRegExp () {
		Empleado empleado = (Empleado)Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		return empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR);
	}

	public void restringuirAcceso() {		
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);		
		if (!(empleado == null || empleado.getPerfil() == null)) {
			String flagBanMant = empleado.getPerfil().getFlagAdministracion() == null ? "" : empleado.getPerfil().getFlagAdministracion().toString();			
			if (ConstantesAdmin.FLAG_BANDEJA_MANTENIMIENTO.equals(flagBanMant)) {
				renderedBanMant = true;
			}

			String flag = empleado.getPerfil().getFlagVistaUnica() == null ? "" : empleado.getPerfil().getFlagVistaUnica().toString();			
			if (ConstantesAdmin.FLAG_VISTA_UNICA.equalsIgnoreCase(flag)) {
				renderedVistaUnica = true;
			}

			flag = empleado.getPerfil().getFlagSabanaDatos() == null ? "" : empleado.getPerfil().getFlagSabanaDatos().toString();			
			if (ConstantesAdmin.FLAG_VISTA_SABANADATOS.equalsIgnoreCase(flag)) {
				renderedReporteExpediente = true;
			}

			flag = empleado.getPerfil().getFlagMostrarAns() == null ? "" : empleado.getPerfil().getFlagMostrarAns().toString();			
			if (ConstantesAdmin.FLAG_VISTA_ANS.equalsIgnoreCase(flag)) {
				renderedReporteAns = true;
			}
			
				
			if(	empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION)){
				renderedVerEstConsuRecla=true;
			}
			if(empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR)){
				renderedRegConsuRecla=true;
				renderedVerEstConsuRecla=true;
			}
			
			if(empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_ADMINISTRADOR)){
				renderedHorario=true;
			}
			
		}



	}
	

	public boolean isRenderedBanMant() {
		return renderedBanMant;
	}

	public void setRenderedBanMant(boolean renderedBanMant) {
		this.renderedBanMant = renderedBanMant;
	}

	public String vistaUnica () {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("VistaUnica");		
		removeObjectSession(Constantes.BANDJ_TRABAJO_BASTANTEO);
		removeObjectSession(Constantes.BANDJ_FILTRO_BASTANTEO);
		return redirect("/vistaUnica/vistaUnica");
	}

	public String consultaPoderes() {
		removeObjectSession(Constantes.BANDJ_TRABAJO_BASTANTEO);
		removeObjectSession(Constantes.BANDJ_FILTRO_BASTANTEO);
		return redirect("/consultaPoderes/consultaPoderes");
	}


	public String bandejaReclamo () {
		return redirect("/reclamo/bandeja");
	}
	public String registroReclamo () {
		return redirect("/reclamo/registro");
	}
	public String reporteExpedientes() {
		return redirect("/reportes/reporteExpedientes");
	}

	public String reporteAns() {
		return redirect("/ans/formReporteAns");
	}


	public boolean isRenderedVistaUnica() {
		return renderedVistaUnica;
	}

	public void setRenderedVistaUnica(boolean renderedVistaUnica) {
		this.renderedVistaUnica = renderedVistaUnica;
	}

	public String getPaginaInicio() {
		return paginaInicio;
	}

	public void setPaginaInicio(String paginaInicio) {
		this.paginaInicio = paginaInicio;
	}

	public String reenvioDocumentos() {
		return redirect("/reenvioDocumentos/reenvioDocumentos");
	}

	public boolean isMostrarReenvioDoc() {
		Empleado empleado = (Empleado)Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		LOG.info("------------isMostrarRegExp()--------------");
		LOG.info("empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR)"+empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR));
		return empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR);
	}

	public String irCerrarSesion() {
		HttpServletRequest request = (HttpServletRequest) getExternalContext()
				.getRequest();
		HttpSession session = request.getSession(false);
		session.invalidate();
		return "/error/errorTimeOut.faces?faces-redirect=true";
	}

	public void timeOutWAS() {
		HttpServletResponse response = (HttpServletResponse) getExternalContext()
				.getResponse();
		HttpServletRequest request = (HttpServletRequest) getExternalContext()
				.getRequest();
		HttpSession session = request.getSession(false);
		session.invalidate();
		try {
			response.sendRedirect(paginaInicio);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void logout() {
		HttpServletResponse response = (HttpServletResponse) getExternalContext()
				.getResponse();
		HttpServletRequest request = (HttpServletRequest) getExternalContext()
				.getRequest();
		try {
			HttpSession session = request.getSession(false);
			session.invalidate();
			String logoutPage = "/../../../pkmslogout?filename=logout.html";
			response.sendRedirect(response.encodeURL(logoutPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cerrarSesionWAS(ActionEvent event) {
		LOG.info("cerrarSesionWAS()");
		FacesContext.getCurrentInstance().getExternalContext()
		.invalidateSession();

		HttpServletRequest request = (HttpServletRequest) getExternalContext()
				.getRequest();
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	public boolean isRenderedReporteExpediente() {
		return renderedReporteExpediente;
	}

	public void setRenderedReporteExpediente(boolean renderedReporteExpediente) {
		this.renderedReporteExpediente = renderedReporteExpediente;
	}

	public boolean isRenderedReporteAns() {
		return renderedReporteAns;
	}

	public void setRenderedReporteAns(boolean renderedReporteAns) {
		this.renderedReporteAns = renderedReporteAns;
	}


	public boolean isRenderedRegConsuRecla() {
		return renderedRegConsuRecla;
	}

	public void setRenderedRegConsuRecla(boolean renderedRegConsuRecla) {
		this.renderedRegConsuRecla = renderedRegConsuRecla;
	}

	public boolean isRenderedVerEstConsuRecla() {
		return renderedVerEstConsuRecla;
	}

	public void setRenderedVerEstConsuRecla(boolean renderedVerEstConsuRecla) {
		this.renderedVerEstConsuRecla = renderedVerEstConsuRecla;
	}

	public boolean isRenderedHorario() {
		return renderedHorario;
	}

	public void setRenderedHorario(boolean renderedHorario) {
		this.renderedHorario = renderedHorario;
	}

}

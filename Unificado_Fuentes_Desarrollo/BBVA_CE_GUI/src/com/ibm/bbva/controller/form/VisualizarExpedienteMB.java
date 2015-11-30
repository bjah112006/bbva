package com.ibm.bbva.controller.form;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.ExpedienteTCWPSWeb;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.TablaBandejaMonitoreoMB;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.util.AyudaExpedienteTC;

@SuppressWarnings("serial")
@ManagedBean(name = "visualizarExpediente")
@ViewScoped
public class VisualizarExpedienteMB extends AbstractMBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(VisualizarExpedienteMB.class);
	
	private String nombrebandeja = "";
	private ExpedienteTCWPSWeb expedienteTCWPS = null;
	
	private String mensajeError = null;
	private String mensajeEnProceso = null;
	private String mensajeErrorBalanceo = null;
	/*
	 * FIX ERIKA ABREGU 07/07/2015
	*/
	private Expediente expediente = null;
	private String origen= null;
	
    private ParametrosConfBeanLocal parametrosConfBean;
    
    private Boolean desdeBandejaMonitoreo = false;
    private Integer bandejaMonitoreo_tipo = null;

	public VisualizarExpedienteMB() {
		this.init();
	}
	
	public void init() {
		nombrebandeja = getObjectSession(Constantes.NOMBRE_BANDEJA_SESION).toString();
		expedienteTCWPS = (ExpedienteTCWPSWeb) getObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION);
		mensajeErrorBalanceo = (String) getObjectSession(Constantes.DESCRIPCION_ERROR);
		
		mensajeError = null;
		mensajeEnProceso = null;
		
		/*
		 * FIX ERIKA ABREGU 07/07/2015
		*/
		origen = null;
		this.expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION_HISTORICO);
				
		if(this.expediente != null){
			this.setOrigen(this.expediente.getOrigen());
		}else{
			this.origen=null;
		}
		/*
		 * FIN DE FIX ERIKA ABREGU 07/07/2015
		*/
		
		
		
		/** Para visualizaciones a partir de la bandeja de monitoreo **/
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		if(facesContext.getExternalContext().getRequestParameterMap().get("bandejaMonitoreo") != null)
			this.desdeBandejaMonitoreo = Boolean.valueOf(facesContext.getExternalContext().getRequestParameterMap().get("bandejaMonitoreo"));
		
		if(facesContext.getExternalContext().getRequestParameterMap().get("tipo") != null)
			this.bandejaMonitoreo_tipo = Integer.valueOf(facesContext.getExternalContext().getRequestParameterMap().get("tipo"));
		
		try {
			
			/**
			 * @author Daniel Flores
			 * No mostrar mensajes de error y process  si
			 * la visualización proviene de bandeja de monitoreo
			 */
			
			if(!this.getDesdeBandejaMonitoreo()){
			
				parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
				
				/*
				 * @author Daniel Flores
				 * 
				 * Este mensaje error solo se muestra cuando el expedienteTCWPS tiene
				 * una tarea cuyo id sea 50, es decir, que no se llegó a subir los
				 * documentos y expiró el tiempo límite de espera.
				 * 
				 * Update : Refactorizado, seteado solo si el ID de Tarea es 50.
				 * El objeto ExpedienteTCWPS solo está disponible cuando se realiza la llamada
				 * desde TablaBandejaAsigMB o TablaBandejaPendMB
				 * 
				 */	
				
				if(expedienteTCWPS != null && expedienteTCWPS.getIdTarea() != null){
					if(expedienteTCWPS.getIdTarea().equals("50")){
						LOG.info("EXPEDIENTE CON ID TAREA 50");
						String mError = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "MENSAJE_ERROR_DOCUMENTO_GESTION_CM").getValorVariable();
						this.setMensajeError(mError);
					}
				}
				
				/*
				 * @author Daniel Flores
				 * 
				 * Si el expedienteTCWPS contiene el flagEnProcesoTimer activo entonces significa
				 * que el expediente se encuentra 'En proceso', por lo tanto se ignorará la tarea
				 * en la que se encuentra inicialmente por defecto (1). Se evaluó persistir un nuevo tipo
				 * de tarea (-1) pero por reglas de negocio se decidió usar un flag.
				 * 
				 * En la vista se evaluará el valor del flagEnProcesoTimer y se renderizará los mensajes
				 * adecuados.
				 */	
				
				if(expedienteTCWPS != null && expedienteTCWPS.getFlagEnProcesoTimer() != null){
					if(expedienteTCWPS.getFlagEnProcesoTimer().equals("1")){
						String mProceso = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "MENSAJE_EN_PROCESO_DOCUMENTO_GESTION_CM").getValorVariable();
						this.setMensajeEnProceso(mProceso);
					}
				}
			
			}
		
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public String continuar(){	
		FacesContext ctx = FacesContext.getCurrentInstance();
		BandejaMonitoreoMB bandejaMonitoreo = (BandejaMonitoreoMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "bandejaMonitoreo");
		AyudaExpedienteTC objAyuda= new AyudaExpedienteTC();
		ExpedienteTCWPS ojTemp = objAyuda.fromExpedienteTCWPSWebToExpedienteTCWPS(this.getExpedienteTCWPS());
		return bandejaMonitoreo.continuarContent(ojTemp);
	}
	
	public String reintentar(){	
		FacesContext ctx = FacesContext.getCurrentInstance();
		BandejaMonitoreoMB bandejaMonitoreo = (BandejaMonitoreoMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "bandejaMonitoreo");
		AyudaExpedienteTC objAyuda= new AyudaExpedienteTC();
		ExpedienteTCWPS ojTemp = objAyuda.fromExpedienteTCWPSWebToExpedienteTCWPS(this.getExpedienteTCWPS());
		return bandejaMonitoreo.reintentarProcess(ojTemp);
	}

	public String cancelar(){	
		FacesContext ctx = FacesContext.getCurrentInstance();
		BandejaMonitoreoMB bandejaMonitoreo = (BandejaMonitoreoMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "bandejaMonitoreo");
		AyudaExpedienteTC objAyuda= new AyudaExpedienteTC();
		ExpedienteTCWPS ojTemp = objAyuda.fromExpedienteTCWPSWebToExpedienteTCWPS(this.getExpedienteTCWPS());
		return bandejaMonitoreo.cancelar(ojTemp);
	}
	
	public String salir() {		
		String retorna = "";		
		if (nombrebandeja.equals("formBandejaPendientes")) {
			retorna = "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
		}else if (nombrebandeja.equals("formBandejaBusqueda")) {
			retorna = "/bandejaBusqueda/formBandejaBusqueda?faces-redirect=true";
		}else if (nombrebandeja.equals("formBandejaHistorica")) {
			retorna = "/bandejaHistorica/formBandejaHistorica?faces-redirect=true";
		}else if (nombrebandeja.equals("formBandejaReasigTareas")) {
			retorna = "/bandejaReasigTareas/formBandejaReasigTareas?faces-redirect=true";
			addObjectSession(Constantes.CONSULTA_BANDASIGN_EXP, true);
		}else if (nombrebandeja.equals("formBandejaMonitoreo")){
			retorna = "/bandejaMonitoreo/formBandejaMonitoreo?faces-redirect=true";
		}
		return retorna;
	}

	public String getNombrebandeja() {
		return nombrebandeja;
	}

	public void setNombrebandeja(String nombrebandeja) {
		this.nombrebandeja = nombrebandeja;
	}

	public ExpedienteTCWPSWeb getExpedienteTCWPS() {
		return expedienteTCWPS;
	}

	public void setExpedienteTCWPS(ExpedienteTCWPSWeb expedienteTCWPS) {
		this.expedienteTCWPS = expedienteTCWPS;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public String getMensajeEnProceso() {
		return mensajeEnProceso;
	}

	public void setMensajeEnProceso(String mensajeEnProceso) {
		this.mensajeEnProceso = mensajeEnProceso;
	}

	

	public Boolean getDesdeBandejaMonitoreo() {
		return desdeBandejaMonitoreo;
	}

	public void setDesdeBandejaMonitoreo(Boolean desdeBandejaMonitoreo) {
		this.desdeBandejaMonitoreo = desdeBandejaMonitoreo;
	}

	public Integer getBandejaMonitoreo_tipo() {
		return bandejaMonitoreo_tipo;
	}

	public void setBandejaMonitoreo_tipo(Integer bandejaMonitoreo_tipo) {
		this.bandejaMonitoreo_tipo = bandejaMonitoreo_tipo;
	}

	public String getMensajeErrorBalanceo() {
		mensajeErrorBalanceo=(mensajeErrorBalanceo==null?"":mensajeErrorBalanceo);
		return mensajeErrorBalanceo;
	}

	public void setMensajeErrorBalanceo(String mensajeErrorBalanceo) {
		this.mensajeErrorBalanceo = mensajeErrorBalanceo;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}
	
	
}

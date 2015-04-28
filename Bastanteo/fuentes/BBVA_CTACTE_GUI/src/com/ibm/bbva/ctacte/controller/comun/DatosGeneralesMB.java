package com.ibm.bbva.ctacte.controller.comun;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.business.BuscarExpediente;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosGenerales;
import com.ibm.bbva.ctacte.controller.form.AyudaMemoriaMB;
import com.ibm.bbva.ctacte.controller.form.HistorialExpedienteMB;
import com.ibm.bbva.ctacte.controller.form.LogExpedienteMB;
import com.ibm.bbva.ctacte.controller.form.ObservacionesMB;
import com.ibm.bbva.ctacte.controller.form.VerificarCalidadDocumentoMB;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="datosGenerales")
@ViewScoped
public class DatosGeneralesMB extends AbstractMBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DatosGeneralesMB.class);
	
	@ManagedProperty (value="#{verificarCalidadDocumentos}")
	private VerificarCalidadDocumentoMB verificarCalidadDocumentos;
	@ManagedProperty (value="#{ayudaMemoria}")
	private AyudaMemoriaMB ayudaMemoriaMB;
	@ManagedProperty (value="#{logExpediente}")
	private LogExpedienteMB logExpedienteMB;
	@ManagedProperty (value="#{historialExpediente}")
	private HistorialExpedienteMB historialExpedienteMB;
	@ManagedProperty (value="#{observacionesExp}")
	private ObservacionesMB observacionesMB;
	
	private IDatosGenerales managedBean;
	private String tituloSeccion;
//	private int idExpe = 123;
	@EJB
	private BuscarExpediente buscarExp;
	@EJB
	private ExpedienteBusiness expedienteBusiness;
	@EJB
	private ClienteBusiness clibusiness;
	@EJB
	private ExpedienteDAO expedienteDAO;
	
	public String getTituloSeccion() {
		return tituloSeccion;
	}

	public void setTituloSeccion(String tituloSeccion) {
		this.tituloSeccion = tituloSeccion;
	}

	private Expediente expediente;
	private String rutaANS="";
	private String mensajePlazoSubsanacion="";
	private String mensajeContent="";
	
	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		
		if (ConstantesAdmin.FORM_VERIFICAR_CALIDAD_DOCUMENTOS.contains(pagina)) {
			
			ExpedienteCC expedienteCC = (ExpedienteCC) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION);
			
			rutaANS = expedienteCC.getCodSemaforo();
			
			if (ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())) {
				// para mantener compatibilidad con expedientes antiguos se obtiene el último bastanteo con el query
				// si no existe la relación que se crea con el pre-registro automático
				Expediente ultimExpBast;
				if (expediente.getIdExpUltBastanteo() != null)
					ultimExpBast = expedienteDAO.load(expediente.getIdExpUltBastanteo());
				else
					ultimExpBast = clibusiness.obternerUltimoBastanteo(expediente.getCliente().getCodigoCentral());
				if (ultimExpBast != null) {
					LOG.info("ultimExpBast.getFechaFin(): " + ultimExpBast.getFechaFin());
					LOG.info("expediente.getFechaRegistro(): " + expediente.getFechaRegistro());
					LOG.info("new Date(): " + new Date());
					
					int dentroPlazo = expedienteBusiness.dentroPlazoSubsanacion(new Date(), 
							ultimExpBast.getFechaFin());
					LOG.info("dentroPlazo: " + dentroPlazo);
					if (dentroPlazo == ConstantesBusiness.FUERA_PLAZO_SUBSANACION) {
						mensajePlazoSubsanacion = ConstantesBusiness.MENSAJE_FUERA_PLAZO_SUBSANACION;
					}else{
						mensajePlazoSubsanacion = "";
					}
				} else {
					LOG.warn("ultimExpBast == null");
					mensajePlazoSubsanacion = "";
				}
			}
			
//			Tarea tarea=null;
//
//			if (tarea == null) tarea = new Tarea();
//			
//			int minutosTranscurridos = 0;
//
//			DateTime dti = null;
//			DateTime dtf = null;
//			dti = new DateTime (expediente.getFechaRegistro());
//			dtf = new DateTime (new Date());
//			minutosTranscurridos = Minutes.minutesBetween(dti, dtf).getMinutes();
//			
//			int minAmarillo = (tarea.getAmarilloMinutos() == null?0:tarea.getAmarilloMinutos());
//			int minVerde = (tarea.getVerdeMinutos() == null?0:tarea.getVerdeMinutos());
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("minAmarillo-->"+minAmarillo);
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("minVerde-->"+minVerde);			
//			if (minutosTranscurridos <= minVerde)
//			{
//				rutaANS = "../imagenes/verde.png";
//			}else if(minVerde > minutosTranscurridos && minutosTranscurridos <= minAmarillo){
//				rutaANS = "../imagenes/amarillo.png";
//			}else if( minutosTranscurridos > minAmarillo )
//			{
//				rutaANS = "../imagenes/rojo.png";
//			}
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("rutaANS-->"+rutaANS);
			
			managedBean = verificarCalidadDocumentos;
			managedBean.setDatosGenerales(this);
			
			// mensaje faltan documentos content
			if (faltanDocumentosContent(expedienteCC)) {
				mensajeContent = ConstantesAdmin.MSG_ERROR_FALTA_DOC_CONTENT;
			}
	
		}else if (ConstantesAdmin.FORM_AYUDA_MEMORIA.contains(pagina)) {
			setTituloSeccion("Ayuda Memoria/Condiciones Expeciales:");
			managedBean = ayudaMemoriaMB;
			
		}else if (ConstantesAdmin.FORM_HISTORIAL_EXPEDIENTE.contains(pagina)) {
			setTituloSeccion("Historial Expediente:");
			managedBean = historialExpedienteMB;
			
		}else if (ConstantesAdmin.FORM_LOG.contains(pagina)) {
			setTituloSeccion("Log del Expediente:");
			managedBean = logExpedienteMB;
			
		}else if (ConstantesAdmin.FORM_OBSERVACIONES_EXPEDIENTE.contains(pagina)) {
			setTituloSeccion("Observaciones y Comentarios:");
			managedBean = observacionesMB;
			
		}		

		//managedBean.setDatosGenerales(this);
	}

	public String getRutaANS() {
		return rutaANS;
	}

	public void setRutaANS(String rutaANS) {
		this.rutaANS = rutaANS;
	}
	
	public String getMensajePlazoSubsanacion() {
		return mensajePlazoSubsanacion;
	}

	public void setMensajePlazoSubsanacion(String mensajePlazoSubsanacion) {
		this.mensajePlazoSubsanacion = mensajePlazoSubsanacion;
	}

	public VerificarCalidadDocumentoMB getVerificarCalidadDocumentos() {
		return verificarCalidadDocumentos;
	}

	public void setVerificarCalidadDocumentos(
			VerificarCalidadDocumentoMB verificarCalidadDocumentos) {
		this.verificarCalidadDocumentos = verificarCalidadDocumentos;
	}

	public AyudaMemoriaMB getAyudaMemoriaMB() {
		return ayudaMemoriaMB;
	}

	public void setAyudaMemoriaMB(AyudaMemoriaMB ayudaMemoriaMB) {
		this.ayudaMemoriaMB = ayudaMemoriaMB;
	}

	public LogExpedienteMB getLogExpedienteMB() {
		return logExpedienteMB;
	}

	public void setLogExpedienteMB(LogExpedienteMB logExpedienteMB) {
		this.logExpedienteMB = logExpedienteMB;
	}

	public HistorialExpedienteMB getHistorialExpedienteMB() {
		return historialExpedienteMB;
	}

	public void setHistorialExpedienteMB(HistorialExpedienteMB historialExpedienteMB) {
		this.historialExpedienteMB = historialExpedienteMB;
	}

	public ObservacionesMB getObservacionesMB() {
		return observacionesMB;
	}

	public void setObservacionesMB(ObservacionesMB observacionesMB) {
		this.observacionesMB = observacionesMB;
	}

	public String getMensajeContent() {
		return mensajeContent;
	}

	public void setMensajeContent(String mensajeContent) {
		this.mensajeContent = mensajeContent;
	}
	
}

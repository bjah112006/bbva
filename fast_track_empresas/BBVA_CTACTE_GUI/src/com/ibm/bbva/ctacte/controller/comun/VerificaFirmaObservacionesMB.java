package com.ibm.bbva.ctacte.controller.comun;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.ParametrosConf;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.bean.servicio.sfp.DatosClientePJSFP;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IVerificaFirmaObservaciones;
import com.ibm.bbva.ctacte.controller.form.VerificarCalidadFirmasMB;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ParametrosConfDAO;
import com.ibm.bbva.ctacte.dao.ParticipeDAO;
import com.ibm.bbva.ctacte.dao.servicio.SistemaFirmasPoderesDAO;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.DocumentoExpWrapper;

@ManagedBean(name="verificaFirmasObservaciones")
@ViewScoped
public class VerificaFirmaObservacionesMB extends AbstractMBean{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(VerificaFirmaObservacionesMB.class);
	
	@ManagedProperty (value="#{verificarCalidadFirma}")
	private VerificarCalidadFirmasMB verificarCalidadFirmas;
	@ManagedProperty (value="#{verificaDocumentacionDigitalizada}")
	private VerificaDocumentacionDigitalizadaMB verificaDocumentacionDigitalizada;	
	private IVerificaFirmaObservaciones iVerificarFirmaObservaciones;
	private String strObservaciones;	
	private boolean blnTerminar = true;
	private boolean blnRechazar = true;
	private boolean habilitarOpcionRechazo;
	private int idTarea;
	private Expediente expediente;
	private String existeFirmaAsociada;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private ParticipeDAO participeDAO;
	@EJB
	private ParametrosConfDAO parametrosConfDAO;
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("Pagina actual {}",pagina);
		if ("formVerificarCalidadFirma".equals(pagina)){
			LOG.info("Entro a la pagina{}",pagina);
			iVerificarFirmaObservaciones=verificarCalidadFirmas;			
		}
		//blnTerminar=true;
		//blnRechazar=true;
		
		expediente=(Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_VERIFICAR_CALIDAD_FIRMA.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}
		}
		
		iVerificarFirmaObservaciones.setVerificaFirmaObservaciones(this);
		//iVerificarFirmaObservaciones.habilitarBotonRechazar(blnRechazar);
		
		// Habilitación del botón Terminar Vinculación según configuración
		// flagTerminarVinculacion = 1, Pide verificación de SFP
		// flagTerminarVinculacion = 0, Pide verificación de SFP excepto cuando es una Modificatoria y es un cliente no migrado
		// [Begin]-[15.04.08]-[Habilitación del botón Terminar Vinculación según configuración]
		ParametrosConf parametro = null;
		try {
			parametro = parametrosConfDAO.obtener(ConstantesBusiness.CODIGO_MODULO_CONF, ConstantesBusiness.CODIGO_FLAG_HABILITAR_TERMINAR_VINCULACION);
			
			blnTerminar = "0".equalsIgnoreCase(parametro.getValorVariable()); // Flag Habilitado
			blnTerminar = blnTerminar && ("0".equalsIgnoreCase(expediente.getCliente().getFlagOrigenSFP())); // Cliente No Esta Migrado
			blnTerminar = blnTerminar && (ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion()) || ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())); // Modificatoria
			blnTerminar = !blnTerminar;
			
			LOG.info(parametro.toString());
		} catch(Exception e) {
			LOG.info("Error multitabla", e);
		}
		// [End]-[15.04.08]-[Habilitación del botón Terminar Vinculación según configuración]
		
	}	
	
	public void rechazarDocumentacion(ActionEvent ae){
		LOG.info("rechazarDocumentacion(ActionEvent ae)");
		String accion=ConstantesBusiness.ACCION_EXPEDIENTE_RECHAZAR;
		iVerificarFirmaObservaciones.ejecutarRechazarDocumentacion(accion);		
	}
	
	public void terminarVinculacion(ActionEvent ae){
		LOG.info("terminarVinculacion(ActionEvent ae)");
		String accion=ConstantesBusiness.ACCION_EXPEDIENTE_TERMINAR;
		ejecutarTerminarVinculacion(accion);		
	}
	
	private void ejecutarTerminarVinculacion(String accion){
		LOG.info("ejecutarTerminarVinculacion(String accion)");
		Tarea tarea=new Tarea();
		EstadoTarea estadoTarea=new EstadoTarea();
		if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_TERMINAR)){
			expediente.setEstado(new EstadoExpediente());
			expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
			tarea.setId(ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE);
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
			
			expediente.setObservaciones(strObservaciones);
		}

		Util.generarRegistroHistExp(expediente);
		expediente = expedienteDAO.update(expediente);
		LOG.info("Actualizo el expediente");
		// para enviar el objeto al process
		LOG.info("Iniciara envio del objeto al process");
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		List<Participe> lista = participeDAO.findByExpedienteParticipesUnicos(expediente.getId());
		String existeFirmaAsociada = ConstantesBusiness.NO_EXISTE_FIRMA_ASOCIADA;
		String existeFirmaNoAsociada = ConstantesBusiness.NO_EXISTE_FIRMA_NO_ASOCIADA;
		for (Participe p : lista) {
			if (ConstantesBusiness.FLAG_FIRMA_ASOCIADA.equals(p.getFlagFirmaAsociada())) {
				LOG.info("existe firma asociada");
				existeFirmaAsociada = ConstantesBusiness.EXISTE_FIRMA_ASOCIADA;
			}
			if (!ConstantesBusiness.FLAG_FIRMA_ASOCIADA.equals(p.getFlagFirmaAsociada())) {
				LOG.info("existe firma no asociada");
				existeFirmaNoAsociada = ConstantesBusiness.EXISTE_FIRMA_NO_ASOCIADA;
			}
		}

		// Si blnTerminar es TRUE indica que es un cliente no migrado, el expediente es de tipo modificatoria y el flag del rol migrador es 0
		if(!blnTerminar) {
			// Al no estar migrado, se asume por defecto que tiene firmas asociadas
			expedienteCC.getDatosFlujoCtaCte().setFlagExisteFirmaAsociada(ConstantesBusiness.EXISTE_FIRMA_ASOCIADA);	
		} else {
			expedienteCC.getDatosFlujoCtaCte().setFlagExisteFirmaAsociada(existeFirmaAsociada);	
		}
//		expedienteCC.getDatosFlujoCtaCte().setFlagExisteFirmaNoAsociada(existeFirmaNoAsociada);
//		expedienteCC.getDatosFlujoCtaCte().setFlagExisteFirmaAsociada(ConstantesBusiness.NO_EXISTE_FIRMA_ASOCIADA);
		expedienteCC.getDatosFlujoCtaCte().setFlagExisteFirmaNoAsociada(ConstantesBusiness.NO_EXISTE_FIRMA_NO_ASOCIADA);
		
		LOG.info("expedienteCC.getDatosFlujoCtaCte().setFlagExisteFirmaNoAsociada(existeFirmaNoAsociada);");
		expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
		LOG.info("expedienteCC.getDatosFlujoCtaCte().setAccion(accion);");
		String tkiid = expedienteCC.getTaskID();
		LOG.info("tkiid" + tkiid);
		LOG.info("String tkiid = expedienteCC.getTaskID();");
		RemoteUtils bandeja = new RemoteUtils();
		LOG.info("BandejaTareasBDelegate bandeja = new BandejaTareasBDelegate();");
		//bandeja.enviarExpediente(tkiid, expedienteCC);
		LOG.info("bandeja.enviarExpediente(tkiid, expedienteCC);");
		bandeja.completarTarea(tkiid, expedienteCC);
		LOG.info("A la bandeja");
		redirectAction("../bandeja/bandeja");
	}
	
	public void  abrirFirmasNoAsociadas(){	
		if(blnTerminar) {
			LOG.info("abrirFirmasNoAsociadas()");
			
			List<Participe> lista = participeDAO.findByExpedienteParticipesUnicos(expediente.getId());
			existeFirmaAsociada = ConstantesBusiness.NO_EXISTE_FIRMA_ASOCIADA;
			for (Participe p : lista) {
				LOG.info("p.getFlagFirmaAsociada() : "+p.getFlagFirmaAsociada());
				if (ConstantesBusiness.FLAG_FIRMA_ASOCIADA.equals(p.getFlagFirmaAsociada())) {
					existeFirmaAsociada = ConstantesBusiness.EXISTE_FIRMA_ASOCIADA;
					break;
				}
			}
			LOG.info("existeFirmaNoAsociada: " + existeFirmaAsociada);
			iVerificarFirmaObservaciones.activarSegunFirmaAsociada(existeFirmaAsociada);
			if(!existeFirmaAsociada.equalsIgnoreCase(ConstantesBusiness.EXISTE_FIRMA_NO_ASOCIADA)) {
				List<DocumentoExpWrapper> listDocExpW = new ArrayList<DocumentoExpWrapper>();
				for ( DocumentoExpWrapper vlde : verificaDocumentacionDigitalizada.getListaDocExpedienteW()) {				
					vlde.setFlagComboRechazadoFirmas(false);
					listDocExpW.add(vlde);
				}
				verificaDocumentacionDigitalizada.setListaDocExpedienteW(listDocExpW);
			}
		}
//		if(existeFirmaNoAsociada.equalsIgnoreCase(ConstantesBusiness.EXISTE_FIRMA_NO_ASOCIADA)) {
//	    	//boton rechazar habilitar
//	    	//terminar vinculacion deshabilitar	    	
//			this.setBlnRechazar(false);
//			this.setBlnTerminar(true);
//		}else{
//			this.setBlnRechazar(true);
//			this.setBlnTerminar(false);
//			
//			List<DocumentoExpWrapper> listDocExpW = new ArrayList<DocumentoExpWrapper>();
//			for ( DocumentoExpWrapper vlde : verificaDocumentacionDigitalizada.getListaDocExpedienteW()) {				
//				vlde.setFlagComboRechazadoFirmas(false);
//				listDocExpW.add(vlde);				
//			}
//			verificaDocumentacionDigitalizada.setListaDocExpedienteW(listDocExpW);
//		}
	}
	
	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	
	
	public String getStrObservaciones() {
		return strObservaciones;
	}

	public void setStrObservaciones(String strObservaciones) {
		this.strObservaciones = strObservaciones;
	}

	public VerificarCalidadFirmasMB getVerificarCalidadFirmas() {
		return verificarCalidadFirmas;
	}

	public void setVerificarCalidadFirmas(
			VerificarCalidadFirmasMB verificarCalidadFirmas) {
		this.verificarCalidadFirmas = verificarCalidadFirmas;
	}

	public IVerificaFirmaObservaciones getIVerificarFirmaObservaciones() {
		return iVerificarFirmaObservaciones;
	}

	public void setIVerificarFirmaObservaciones(
			IVerificaFirmaObservaciones verificarFirmaObservaciones) {
		iVerificarFirmaObservaciones = verificarFirmaObservaciones;
	}

	public void setBlnTerminar(boolean blnTerminar) {
		this.blnTerminar = blnTerminar;
	}

	public boolean isBlnTerminar() {
		return blnTerminar;
	}

	public void setBlnRechazar(boolean blnRechazar) {
		LOG.info("estado boton rechazar4 {}",blnRechazar);
		this.blnRechazar = blnRechazar;
	}

	public boolean isBlnRechazar() {
		return blnRechazar;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public boolean isHabilitarOpcionRechazo() {
		return habilitarOpcionRechazo;
	}

	public void setHabilitarOpcionRechazo(boolean habilitarOpcionRechazo) {
		this.habilitarOpcionRechazo = habilitarOpcionRechazo;
	}

	public String getExisteFirmaAsociada() {
		return existeFirmaAsociada;
	}

	public void setExisteFirmaAsociada(String existeFirmaAsociada) {
		this.existeFirmaAsociada = existeFirmaAsociada;
	}

	public VerificaDocumentacionDigitalizadaMB getVerificaDocumentacionDigitalizada() {
		return verificaDocumentacionDigitalizada;
	}

	public void setVerificaDocumentacionDigitalizada(
			VerificaDocumentacionDigitalizadaMB verificaDocumentacionDigitalizada) {
		this.verificaDocumentacionDigitalizada = verificaDocumentacionDigitalizada;
	}
	
	
	
	
}

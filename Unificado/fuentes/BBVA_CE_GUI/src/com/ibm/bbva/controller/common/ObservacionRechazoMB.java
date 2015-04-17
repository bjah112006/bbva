package com.ibm.bbva.controller.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.AnularModificarOpCu14MB;
import com.ibm.bbva.controller.form.AnularModificarOpCu18MB;
import com.ibm.bbva.controller.form.AprobarExpedienteMB;
import com.ibm.bbva.controller.form.CambiarSituacionExpMB;
import com.ibm.bbva.controller.form.CambiarSituacionTramiteMB;
import com.ibm.bbva.controller.form.ConsultarClienteModificacionesMB;
import com.ibm.bbva.controller.form.CoordinarClienteSubsanarMB;
import com.ibm.bbva.controller.form.EjecutarEvalCrediticiaMB;
import com.ibm.bbva.controller.form.EvaluarDevolucionRiesgosMB;
import com.ibm.bbva.controller.form.RealizarAltaTarjetaMB;
import com.ibm.bbva.controller.form.RegistrarAprobResolucionMB;
import com.ibm.bbva.controller.form.RegistrarDatosMB;
import com.ibm.bbva.controller.form.RegistrarExpedienteMB;
import com.ibm.bbva.controller.form.RegularizarEscanearDocumentacionMB;
import com.ibm.bbva.controller.form.RevisarRegistrarDictamenMB;
import com.ibm.bbva.controller.form.VerificarConformidadExpedienteMB;
import com.ibm.bbva.controller.form.VerificarResultadoDomiciliariaMB;
import com.ibm.bbva.entities.DevolucionTarea;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.MotivoDevolucion;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.session.EstadoBeanLocal;
import com.ibm.bbva.session.MotivoDevolucionBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "observacionRechazo")
@RequestScoped
public class ObservacionRechazoMB extends AbstractMBean {
	
	@EJB
	MotivoDevolucionBeanLocal motivoDevolucionBean;
	@EJB
	private EstadoBeanLocal estadoBean;
	
	private Expediente expediente;
	private String motivoSeleccionado;
	private List<SelectItem> listaMotivoRechazo;
	private boolean mostrarDialogo = false; // inicialmente no se muestra
	private String tituloVentana;
	private String observacionRechazo;
	private String flagOtros;
	private String habilitarText;
	private HtmlPanelGroup dialogoModal;
	private String nombreFormPadre;
	private HtmlSelectOneMenu selectObservacion;
	private HtmlInputHidden inputFlagOtros;
	private HtmlInputHidden inputHabText;
	private HtmlInputTextarea inputTxtObservacion;
	private boolean activoTxtObservacion;
	private String msgValidacion;
	private boolean activoObervacion;
	private boolean actualizarObservados;
	
	private static final Logger LOG = LoggerFactory.getLogger(ObservacionRechazoMB.class);
	
	public ObservacionRechazoMB() {
		//obtenerDatos(null);
		//selectedItems = new ArrayList<String>();
		//listaMotivoRechazo = Util.listaVacia();
		if (tituloVentana == null || tituloVentana.equals("")) {
			tituloVentana = Mensajes
					.getMensaje("com.ibm.bbva.common.observacionRechazo.titulo");
		}
	}
	
	@PostConstruct
	public void init() {
		String considerar=(String) getObjectSession(Constantes.CONSIDERAR_TAREA_1);
		if(considerar!=null && considerar.equals("1")){
			int ent=(Integer)getObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO);
			if(ent==1){
				obtenerDatos((Integer) getObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO));
			}			
			String nombJSP = getNombreJSPPrincipal();
			nombreFormPadre = nombJSP.replaceFirst("form", "frm");
			activoObervacion = false;			
		}else{
			obtenerDatos((Integer) getObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO));
			String nombJSP = getNombreJSPPrincipal();
			nombreFormPadre = nombJSP.replaceFirst("form", "frm");
			activoObervacion = false;			
		}
			

	}

	/* invocar este método luego del PostConstruct para los casos en que el parámetro no es null */
	public void obtenerDatos(Integer intAccion) {
		/* Obtiene Datos de Expediente */
		System.out.println("obtenerDatos intAccion="+intAccion);
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		if (expediente != null && expediente.getId() > 0) {
			
			List<MotivoDevolucion> listMotivoTmp;
//			if (intAccion != null) {
//				listMotivoTmp = motivoDevolucionBean.buscarPorIdTareaYAccion(expediente.getExpedienteTC().getTarea().getId(), new BigDecimal(intAccion));
//				System.out.println("Entro aka");
			//} else {
				//listMotivoTmp = motivoDevolucionBean.buscarPorIdTarea(expediente.getExpedienteTC().getTarea().getId());
				//System.out.println("Entro aka 2");
		//	}
				
			if(intAccion!=null && intAccion==1){
				listMotivoTmp = motivoDevolucionBean.buscarPorIdTarea(expediente.getExpedienteTC().getTarea().getId());
				System.out.println("Rechazo");					
			}else{
				listMotivoTmp = motivoDevolucionBean.buscarPorIdTareaDevolver(expediente.getExpedienteTC().getTarea().getId());
				System.out.println("Devolver");					
			}
			
			List<MotivoDevolucion> listMotivo = new ArrayList<MotivoDevolucion>(listMotivoTmp); // de lo contrario la lista será read-only y el sort falla
			Comparator<MotivoDevolucion> comparator = new Comparator<MotivoDevolucion>() {
				public int compare(MotivoDevolucion o1, MotivoDevolucion o2) {
					return o1.getFlagOtros().compareTo(o2.getFlagOtros());
				}
			};
			if(listMotivoTmp!=null && listMotivo.size()>0){
				// "Otros" debe ir al final de la lista
				Collections.sort(listMotivo, comparator);
		
				listaMotivoRechazo = Util.crearItems(listMotivo, "id", "descripcion");
				
				// solo debería haber un motivo con el flag_otros 
				for (MotivoDevolucion motivo : listMotivo) {
					if (motivo.getFlagOtros().equals(Constantes.FLAG_OTROS_1)) {
						flagOtros = String.valueOf(motivo.getId());
						motivoSeleccionado = String.valueOf(motivo.getId());
						break;
					}else{
						motivoSeleccionado= String.valueOf(motivo.getId());
					}
				}
			}else{
				listaMotivoRechazo = Util.listaVacia();
				motivoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
			activoTxtObservacion = false;
			if(motivoSeleccionado != null && !motivoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			
				if(intAccion!=null && intAccion==1){
					activoTxtObservacion = true;
				}else{
					List<MotivoDevolucion> listMotvDevAux = motivoDevolucionBean.buscarPorIdMotivoIdTareaFlagOtros(Long.parseLong(motivoSeleccionado), expediente.getExpedienteTC().getTarea().getId(), Constantes.FLAG_OTROS_1);
					if(listMotvDevAux != null && listMotvDevAux.size() > 0){
						activoTxtObservacion = true;
					}					
				}
					

			}
			
		}else{
			listaMotivoRechazo = Util.listaVacia();
			motivoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		}
	}

	public String aceptar() {
		if (esValido()) {
			String redireccion = copiar();
			mostrarDialogo = false;
			//String accion = Constantes.ACCION_BOTON_DEVOLVER;
			try {
				String actObs = (String) getObjectSession("actualizarObservados");
				if(actObs != null && "OK".equals(actObs)){
					LOG.info("ingresa a actualizar los observados");
					FacesContext ctx = FacesContext.getCurrentInstance();  
					PanelDocumentosMB panelDocumento = (PanelDocumentosMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
					panelDocumento.actualizarObservados();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return redireccion;
		}else{
			mostrarDialogo = true;
			return null;
		}
	}

	public String cancelar() {
		mostrarDialogo = false;
		return null;
	}

	public String copiar() {
		String redireccion = null;
		String nombJSP = getNombreJSPPrincipal();
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		if (nombJSP.equals("formAprobarExpediente")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			AprobarExpedienteMB aprobarExpediente = (AprobarExpedienteMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "aprobarExpediente");
			redireccion = aprobarExpediente.accionVentana(lista);
		} else if (nombJSP.equals("formRegistrarExpediente")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			RegistrarExpedienteMB registrarExpedienteMB = (RegistrarExpedienteMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "registrarExpediente");
			redireccion = registrarExpedienteMB.accionVentana(lista);
		} else if (nombJSP.equals("formVerificarResultadoDomiciliaria")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			VerificarResultadoDomiciliariaMB verificarResultadoDomiciliaria = (VerificarResultadoDomiciliariaMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "verificarResultadoDomiciliaria");
			redireccion = verificarResultadoDomiciliaria.accionVentana(lista);
		} else if (nombJSP.equals("formRegistrarAprobResolucion")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			RegistrarAprobResolucionMB registrarAprobResolucionMB = (RegistrarAprobResolucionMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "registrarAprobResolucion");
			/*if (registrarAprobResolucionMB == null) {
				registrarAprobResolucionMB = new RegistrarAprobResolucionMB();
				addObjectRequest("registrarAprobResolucion",
						registrarAprobResolucionMB);
			}*/
			redireccion = registrarAprobResolucionMB.accionVentana(lista);
			
		} else if (nombJSP.equals("formVerificarConformidadExpediente")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			VerificarConformidadExpedienteMB verificarConformidadExpedienteMB = (VerificarConformidadExpedienteMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "verificarConformidadExpediente");
			redireccion = verificarConformidadExpedienteMB.accionVentana(lista);
		/*} else if (nombJSP.equals("formVerificarConformidadCierreExp")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			VerificarConformidadCierreExpMB verificarConformidadCierreExp = (VerificarConformidadCierreExpMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "verificarConformidadCierreExp");
			redireccion = verificarConformidadCierreExp.accionVentana(lista);

		} else if (nombJSP.equals("formVerificarEstadoContratacion")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			VerificarEstadoContratacionMB verificarEstadoContratacion = (VerificarEstadoContratacionMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "verificarEstadoContratacion");
			redireccion = verificarEstadoContratacion.accionVentana(lista);
		*/
		/*} else if (nombJSP.equals("formCambiarSituacionExp")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			CambiarSituacionExpMB cambiarSituacionExp = (CambiarSituacionExpMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "cambiarSituacionExp");
			redireccion = cambiarSituacionExp.accionVentana(lista);
        */
		/*} else if (nombJSP.equals("formCambiarSituacionTramite")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			CambiarSituacionTramiteMB cambiarSituacionTramite = (CambiarSituacionTramiteMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "cambiarSituacionTramite");
			redireccion = cambiarSituacionTramite.accionVentana(lista);
        */
		/*} else if (nombJSP.equals("formAnularModificarOpCu14")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			AnularModificarOpCu14MB anularModificarOpCu14 = (AnularModificarOpCu14MB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "anularModificarOpCu14");
			redireccion = anularModificarOpCu14.accionVentana(lista);
          */
		/*} else if (nombJSP.equals("formAnularModificarOpCu18")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			AnularModificarOpCu18MB anularModificarOpCu18 = (AnularModificarOpCu18MB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "anularModificarOpCu18");
			redireccion = anularModificarOpCu18.accionVentana(lista);
        */
		} else if (nombJSP.equals("formEjecutarEvalCrediticia")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			EjecutarEvalCrediticiaMB ejecutarEvalCrediticia = (EjecutarEvalCrediticiaMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "ejecutarEvalCrediticia");
			redireccion = ejecutarEvalCrediticia.accionVentana(lista);
		} else if (nombJSP.equals("formRealizarAltaTarjeta")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			RealizarAltaTarjetaMB realizarAltaTarjetaMB = (RealizarAltaTarjetaMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "realizarAltaTarjeta");
			/*if (realizarAltaTarjetaMB == null) {
				realizarAltaTarjetaMB = new RealizarAltaTarjetaMB();
				addObjectRequest("realizarAltaTarjeta", realizarAltaTarjetaMB);
			}*/
			redireccion = realizarAltaTarjetaMB.accionVentana(lista);
		} else if (nombJSP.equals("formCoordinarClienteSubsanar")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			CoordinarClienteSubsanarMB coordinarClienteSubsanarMB = (CoordinarClienteSubsanarMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "coordinarClienteSubsanar");
			/*if (coordinarClienteSubsanarMB == null) {
				coordinarClienteSubsanarMB = new CoordinarClienteSubsanarMB();
				addObjectRequest("coordinarClienteSubsanar",
						coordinarClienteSubsanarMB);
			}*/
			redireccion = coordinarClienteSubsanarMB.accionVentana(lista);
		} /*else if (nombJSP.equals("formRegistrarEstado")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			RegistrarEstadoMB registrarEstadoMB = (RegistrarEstadoMB) getObjectRequest("registrarEstado");
			redireccion = registrarEstadoMB.accionVentana(lista);
		}*/ else if (nombJSP.equals("formEvaluarDevolucionRiesgos")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			EvaluarDevolucionRiesgosMB evaluarDevolucionRiesgosMB = (EvaluarDevolucionRiesgosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "evaluarDevolucionRiesgos");
			/*if (evaluarDevolucionRiesgosMB == null) {
				evaluarDevolucionRiesgosMB = new EvaluarDevolucionRiesgosMB();
				addObjectRequest("evaluarDevolucionRiesgos",
						evaluarDevolucionRiesgosMB);
			}*/
			redireccion = evaluarDevolucionRiesgosMB.accionVentana(lista);
		} else if (nombJSP.equals("formConsultarClienteModificaciones")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			ConsultarClienteModificacionesMB consultarClienteModificacionesMB = (ConsultarClienteModificacionesMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "consultarClienteModificaciones");
			/*if (consultarClienteModificacionesMB == null) {
				consultarClienteModificacionesMB = new ConsultarClienteModificacionesMB();
				addObjectRequest("consultarClienteModificaciones",
						consultarClienteModificacionesMB);
			}*/
			redireccion = consultarClienteModificacionesMB.accionVentana(lista);
		} else if (nombJSP.equals("formRegularizarEscanearDocumentacion")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			RegularizarEscanearDocumentacionMB regularizarEscanearDocumentacionMB = (RegularizarEscanearDocumentacionMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "regularizarEscanearDocumentacion");
			if (regularizarEscanearDocumentacionMB == null) {
				regularizarEscanearDocumentacionMB = new RegularizarEscanearDocumentacionMB();
				addObjectRequest("regularizarEscanearDocumentacion",
						regularizarEscanearDocumentacionMB);
			}
			redireccion = regularizarEscanearDocumentacionMB
					.accionVentana(lista);
		} /*else if (nombJSP.equals("formGestionarSubsanarOperacion")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			GestionarSubsanarOperacionMB gestionarSubsanarOperacionMB = (GestionarSubsanarOperacionMB) getObjectRequest("gestionarSubsanarOperacion");
			if (gestionarSubsanarOperacionMB == null) {
				gestionarSubsanarOperacionMB = new GestionarSubsanarOperacionMB();
				addObjectRequest("gestionarSubsanarOperacion",
						gestionarSubsanarOperacionMB);
			}
			redireccion = gestionarSubsanarOperacionMB.accionVentana(lista);
		} else if (nombJSP.equals("formRegistrarExpedienteCu23")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			RegistrarExpedienteCu23MB registrarExpedienteCu23MB = (RegistrarExpedienteCu23MB) getObjectRequest("registrarExpedienteCu23");
			if (registrarExpedienteCu23MB == null) {
				registrarExpedienteCu23MB = new RegistrarExpedienteCu23MB();
				addObjectRequest("registrarExpedienteCu23",
						registrarExpedienteCu23MB);
			}
			redireccion = registrarExpedienteCu23MB.accionVentana(lista);
		} else if (nombJSP.equals("formRegistrarExpedienteCu25")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			RegistrarExpedienteCu25MB registrarExpedienteCu25MB = (RegistrarExpedienteCu25MB) getObjectRequest("registrarExpedienteCu25");
			if (registrarExpedienteCu25MB == null) {
				registrarExpedienteCu25MB = new RegistrarExpedienteCu25MB();
				addObjectRequest("registrarExpedienteCu25",
						registrarExpedienteCu25MB);
			}
			redireccion = registrarExpedienteCu25MB.accionVentana(lista);
		}*/ else if (nombJSP.equals("formRevisarRegistrarDictamen")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			RevisarRegistrarDictamenMB revisarRegistrarDictamen = (RevisarRegistrarDictamenMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "revisarRegistrarDictamen");
			redireccion = revisarRegistrarDictamen.accionVentana(lista);
		} else if (nombJSP.equals("formRegistrarDatos")) {
			ArrayList<DevolucionTarea> lista = cargarDatos();
			RegistrarDatosMB registrarDatosMB = (RegistrarDatosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "registrarDatos");
			redireccion = registrarDatosMB.accionVentana(lista);
		}
		return redireccion;		
	}

	public ArrayList<DevolucionTarea> cargarDatos() {
		expediente.getExpedienteTC().setComentarioRechazo(observacionRechazo);
		ArrayList<DevolucionTarea> lista = new ArrayList<DevolucionTarea>();

			DevolucionTarea devolucionTarea = new DevolucionTarea();
			devolucionTarea.setExpediente(expediente);
			MotivoDevolucion motivoDevolucion = motivoDevolucionBean.buscarPorId(Long.parseLong(motivoSeleccionado));
			//motivoDevolucion.setId(Long.parseLong(motivoSeleccionado));
			
			devolucionTarea.setMotivoDevolucion(motivoDevolucion);
			devolucionTarea.setTarea(expediente.getExpedienteTC().getTarea());			

		lista.add(devolucionTarea); // ahora la lista va a ser siempre de un elemento
		
		return lista;
	}

	public boolean esValido() {
		boolean existeError = true;
		activoObervacion = false;
		LOG.info("flagOtros:" + flagOtros);
		LOG.info("observacionRechazo:" + observacionRechazo);
		if (motivoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			existeError = false;
		}
		
		MotivoDevolucion motivoDevolucion = motivoDevolucionBean.buscarPorId(Long.parseLong(motivoSeleccionado));
		LOG.info("motivoDevolucion.getFlagOtros:" + motivoDevolucion.getFlagOtros());
		if (motivoDevolucion.getFlagOtros().equals(Constantes.CODIGO_FLAG_OTROS_ACTIVO) && observacionRechazo.trim().equals("")) {
				existeError = false;
				LOG.info("Agregando mensaje de validacion ");
				activoObervacion = true;
				msgValidacion = "Es obligatorio ingresar la observación";
				
		}		
		return existeError;		
	}
	
	public void obtenerEstado(Expediente expediente, int estado){
		Estado estadoTmp = new Estado();
		estadoTmp.setId(estado);
		Estado estados=estadoBean.buscarPorId(estadoTmp.getId());
		expediente.setEstado(estados);
	}	
	/****FIN PARA EL ENVIO DE CORREO****/

	public String getMotivoSeleccionado() {
		return motivoSeleccionado;
	}

	public void setMotivoSeleccionado(String motivoSeleccionado) {
		this.motivoSeleccionado = motivoSeleccionado;
	}

	public List<SelectItem> getListaMotivoRechazo() {
		return listaMotivoRechazo;
	}

	public void setListaMotivoRechazo(List<SelectItem> listaMotivoRechazo) {
		this.listaMotivoRechazo = listaMotivoRechazo;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public boolean isMostrarDialogo() {
		return mostrarDialogo;
	}

	public void setMostrarDialogo(boolean mostrarDialogo) {
		this.mostrarDialogo = mostrarDialogo;
	}

	public String getTituloVentana() {
		return tituloVentana;
	}

	public void setTituloVentana(String tituloVentana) {
		this.tituloVentana = tituloVentana;
	}

	public String getObservacionRechazo() {
		return observacionRechazo;
	}

	public void setObservacionRechazo(String observacionRechazo) {
		this.observacionRechazo = observacionRechazo;
	}

	public String getFlagOtros() {
		return flagOtros;
	}

	public void setFlagOtros(String flagOtros) {
		this.flagOtros = flagOtros;
	}
	
	public HtmlPanelGroup getDialogoModal() {
		return dialogoModal;
	}
	
	public void setDialogoModal(HtmlPanelGroup dialogoModal) {
		this.dialogoModal = dialogoModal;
	}

	public String getNombreFormPadre() {
		return nombreFormPadre;
	}

	public void setNombreFormPadre(String nombreFormPadre) {
		this.nombreFormPadre = nombreFormPadre;
	}

	public HtmlSelectOneMenu getSelectObservacion() {
		return selectObservacion;
	}

	public void setSelectObservacion(HtmlSelectOneMenu selectObservacion) {
		this.selectObservacion = selectObservacion;
	}

	public HtmlInputHidden getInputFlagOtros() {
		return inputFlagOtros;
	}

	public void setInputFlagOtros(HtmlInputHidden inputFlagOtros) {
		this.inputFlagOtros = inputFlagOtros;
	}

	public HtmlInputTextarea getInputTxtObservacion() {
		return inputTxtObservacion;
	}

	public void setInputTxtObservacion(HtmlInputTextarea inputTxtObservacion) {
		this.inputTxtObservacion = inputTxtObservacion;
	}

	public boolean isActivoTxtObservacion() {
		return activoTxtObservacion;
	}

	public void setActivoTxtObservacion(boolean activoTxtObservacion) {
		this.activoTxtObservacion = activoTxtObservacion;
	}

	public String getMsgValidacion() {
		return msgValidacion;
	}
	
	public String getHabilitarText() {
		LOG.info("getHabilitarText");
		Integer valor=(Integer) getObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO);
		if(valor!=null && valor==1){
			return "1";
		}else{
			return "";
		}
		//return msgValidacion;
		
	}	

	public void setHabilitarText(String habilitarText) {
		this.habilitarText = habilitarText;
	}

	public void setMsgValidacion(String msgValidacion) {
		this.msgValidacion = msgValidacion;
	}

	public boolean isActivoObervacion() {
		return activoObervacion;
	}

	public void setActivoObervacion(boolean activoObervacion) {
		this.activoObervacion = activoObervacion;
	}
	
	public boolean isActualizarObservados() {
		return actualizarObservados;
	}

	public void setActualizarObservados(boolean actualizarObservados) {
		this.actualizarObservados = actualizarObservados;
	}

	public HtmlInputHidden getInputHabText() {
		return inputHabText;
	}

	public void setInputHabText(HtmlInputHidden inputHabText) {
		this.inputHabText = inputHabText;
	}
	
	
	
}

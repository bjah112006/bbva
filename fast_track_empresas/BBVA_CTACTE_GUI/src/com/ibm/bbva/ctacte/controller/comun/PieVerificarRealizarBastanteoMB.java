package com.ibm.bbva.ctacte.controller.comun;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Historial;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.bean.ParametrosConf;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieVerificarRealizarBastanteo;
import com.ibm.bbva.ctacte.controller.form.VerificarRealizarBastanteoMB;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.HistorialDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.dao.ParametrosConfDAO;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.ListSorter;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.DocumentoExpWrapper1;

@ManagedBean(name="pieVerificarRealizarBastanteo")
@ViewScoped
public class PieVerificarRealizarBastanteoMB extends AbstractMBean {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PieVerificarRealizarBastanteoMB.class);
	@ManagedProperty(value="#{verificarRealizarBastanteo}")
	private VerificarRealizarBastanteoMB verificarRealizarBastanteo;
	private IPieVerificarRealizarBastanteo iPieVerificarRealizarBastanteo;

	// [Begin]-[15.07.23]-[Cambio valores de los documentos (casos de negocio)]
	@ManagedProperty(value="#{digitaliceDocumentacion}")
	private DigitaliceDocumentacionMB digitaliceDocumentacionMB;
	@EJB
	private DocumentoExpDAO docExpedienteDAO;
	// [End]-[15.07.23]
	private Expediente expediente;
	private Empleado empleado;
	private Motivo motivo;
	private String selDevolucion;
	private String strDevolucion;
	private String strComentarios;
	private String strDictamen;
	private String strInstrucciones;
	private String opcionResultado;
	private String existeFacultadesEspeciales; 
	private boolean habilitaDevolucion;
	private boolean habilitaComentarios;
	private boolean habilitaBotonDevolver;
	private boolean habilitaBotones;
	private boolean finalizaBastanteo;
	private boolean exitoOperacionSFP;
	// [Begin]-[15.07.23]-[Cambio check abogado]
	private boolean finalizaBastanteoTmp;
	// [End]-[15.07.23]
	private boolean boolDevolver;	
	private List<SelectItem> listaDevolucion;
	@EJB
	private MotivoDAO motivoDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private ParametrosConfDAO parametroConfDAO;
	@EJB
	private HistorialDAO historialDAO;
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("Pagina Actual{}",pagina);
		if ("formVerificarRealizarBastanteo".equals(pagina)){
			iPieVerificarRealizarBastanteo=verificarRealizarBastanteo;
		}
		iPieVerificarRealizarBastanteo.setVerificarRealizarBastanteo(this);
		crearListaDevolucion();
		// [Begin]-[15.07.23]-[Cambio valores de los documentos (casos de negocio)]
		expediente = (expediente==null) ? expediente= (Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION) : expediente;
		// [End]-[15.07.23]
		controlesAlInicio();		
		expediente=(Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		empleado=(Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		Motivo motivo=new Motivo();
		expediente.setMotivo(motivo);
		if (expediente.getDictamenBastanteo() != null) {
			strDictamen = new String (expediente.getDictamenBastanteo());	
		}
		
		if (expediente.getInstruccionesBastanteo() != null) {
			strInstrucciones = new String (expediente.getInstruccionesBastanteo());
		}
		
		if (expediente.getResultado() != null) {
			opcionResultado=expediente.getResultado();
		}
		//expediente.setEmpleado(empleado);
	}
	
	private void controlesAlInicio(){
		habilitaDevolucion=true;
		habilitaComentarios=true;
		habilitaBotonDevolver=true;
		habilitaBotones=false;
		boolDevolver=false;
		
		// Habilitación del botón Finalizar Proceso según configuración
		// flagFinalizarBastanteo = 1, Pide verificación de SFP
		// flagFinalizarBastanteo = 0, Pide verificación de SFP excepto cuando es una Modificatoria y es un cliente no migrado
		// [Begin]-[15.04.08]-[Habilitación del botón Finalizar Proceso según configuración]
		finalizaBastanteo=true;
		ParametrosConf parametro = null;
		try {
			parametro = parametroConfDAO.obtener(ConstantesBusiness.CODIGO_MODULO_CONF, ConstantesBusiness.CODIGO_FLAG_HABILITAR_FINALIZAR_BASTANTEO);
			
			finalizaBastanteo = "0".equalsIgnoreCase(parametro.getValorVariable()); // Flag Habilitado
			finalizaBastanteo = finalizaBastanteo && ("0".equalsIgnoreCase(expediente.getCliente().getFlagOrigenSFP())); // Cliente No Esta Migrado
			finalizaBastanteo = finalizaBastanteo && (ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion()) || ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())); // Modificatoria
			finalizaBastanteo = !finalizaBastanteo;
			LOG.info("PARAMETRO = " + parametro.getValorVariable());
			LOG.info("EXPEDIENTE = " + expediente.getOperacion().getCodigoOperacion());
			LOG.info("FLAG FINALIZA BASTANTEO = " + finalizaBastanteo);
			
			LOG.info(parametro.toString());
		} catch(Exception e) {
			LOG.info("Error multitabla", e);
		}
		// [End]-[15.04.08]-[Habilitación del botón Finalizar Proceso según configuración]
	}
	
	private void crearListaDevolucion(){
		List<Motivo> listadevolucion=motivoDAO.findByTarea(ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO, ConstantesBusiness.MOTIVO_DEVOLUCION);		
		listaDevolucion=Util.crearItems(listadevolucion,true,"id","descripcion");
		ListSorter.ordenarMotivos(listaDevolucion);
	}
	
	public void habilitarControles(ValueChangeEvent ae){
		LOG.info("habilitarControles()");
		LOG.info("boolDevolver.."+boolDevolver);
		if (boolDevolver==false){	
			// [Begin]-[15.07.23]-[Cambio check abogado]
			finalizaBastanteoTmp = finalizaBastanteo;
			// [End]-[15.07.23]
			habilitaDevolucion=false;
			habilitaComentarios=false;
			habilitaBotonDevolver=false;	
			habilitaBotones=true;
			
			finalizaBastanteo=true;
			boolDevolver=true;
		} else {
			habilitaDevolucion=true;
			habilitaComentarios=true;
			habilitaBotonDevolver=true;	
			habilitaBotones=false;
			// [Begin]-[15.07.23]-[Cambio check abogado]
			if (exitoOperacionSFP == true || finalizaBastanteoTmp == false)
				finalizaBastanteo=false;
			// [End]-[15.07.23]
			boolDevolver=false;
		}
		LOG.info("Salir de habilitar Controles.");
	}
	
	
	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public boolean isFinalizaBastanteo() {
		return finalizaBastanteo;
	}

	public void setFinalizaBastanteo(boolean finalizaBastanteo) {
		this.finalizaBastanteo = finalizaBastanteo;
	}

	public void accionDevolver(ActionEvent e){
		LOG.info("accionDevolver(ActionEvent ae");
		String accion = ConstantesBusiness.ACCION_DEVOLVER_A_MESA_SSJJ;
		ejecutarAccionDevolver(accion);
		
		
	}	
	
	public boolean siHayDevuelto(){
		LOG.info("siHayObservaciones()");
		LOG.info("selDevolucion2"+selDevolucion);
		boolean hayDevolucion=true;
		if (ConstantesAdmin.CODIGO_CAMPO_VACIO.equalsIgnoreCase(selDevolucion)){
			LOG.info("No selecciono ningun valor");
			mensajeError("form:idDevolucion", "Seleccione un tipo de Motivo de Devolución");
			hayDevolucion=false;
		}
		return hayDevolucion;
	}
	
	private void ejecutarAccionDevolver(String accion){
		LOG.info("ejecutarAccionDevolver(String accion)");
		Tarea tarea=new Tarea();
		EstadoTarea estadoTarea=new EstadoTarea();
		if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_DEVOLVER_A_MESA_SSJJ)){
			expediente.setEstado(new EstadoExpediente());
			expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
			tarea.setId(ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_DOCUMENTOS);
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
		}
		LOG.info("selDevolucion 1 "+selDevolucion);
		//selDevolucion=getRequestParameter("idDevolucion");	
		//LOG.info("selDevolucion"+selDevolucion);
		if (iPieVerificarRealizarBastanteo.siHayDevuelto()){
			LOG.info("entro al IF");
			expediente.setObservaciones(strComentarios);
			expediente.setAccion(ConstantesBusiness.ACCION_DEVOLVER_A_MESA_SSJJ);	
			LOG.info("accion del expediente " +  expediente.getAccion());
			LOG.info("va a actualizar el expediente");
			int Devolucion=Integer.valueOf(selDevolucion);
			motivo = motivoDAO.load(Devolucion);
			expediente.setMotivo(motivo);
		    Util.generarRegistroHistExp(expediente);
			expediente = expedienteDAO.update(expediente);
			// FIX para que no se pierda la descripción del estado del expediente
			Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);
			LOG.info("actualizo el expediente");
			LOG.info("entrara a actualizar el expediente en el process");
			AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
			ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
			expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
			String tkiid = expedienteCC.getTaskID();
			RemoteUtils bandeja = new RemoteUtils();
			//bandeja.enviarExpediente(tkiid, expedienteCC);
			bandeja.completarTarea(tkiid, expedienteCC);
			subirArchivos(true);
			redirectAction("../bandeja/bandeja");
		}
	}
	
	public boolean esValido () {
		LOG.info("esValido ()");
		boolean esCorrecto = true;
		LOG.info("indicAdicionales : {}",opcionResultado);
			if (StringUtils.isEmpty(opcionResultado)) {
				mensajeErrorPrincipal("idOptionsA", "Ingrese la opcion de Resultados");
				esCorrecto = false;
			}		
		return esCorrecto;
	}
	
	public boolean ingresarDictamen(){
		LOG.info("ingresarDictamen");
		boolean ingresarDictamen=true;
		LOG.info("strDictamen" + strDictamen);
		//opcionResultado = (String) ae.getNewValue();
		if (((opcionResultado.equals(ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO_PARCIAL)) || (opcionResultado.equals(ConstantesBusiness.ACCION_EXPEDIENTE_OBSERVADO))) 
					&& (strDictamen.equalsIgnoreCase(ConstantesBusiness.STR_CAMPO_VACIO))){
			LOG.info("Mensaje de error en dictamen");
			mensajeErrorPrincipal("idVerificar:idTabDictamen","Ingresar el Dictamen");
			ingresarDictamen=false;
		}
		return ingresarDictamen;
	}
	
	public void finalizarBastanteo(ActionEvent ae) {
		LOG.info("finalizarBastanteo(ActionEvent ae)");
		// LOG.info("opcion resultado"+opcionResultado.toString());
		
		if (iPieVerificarRealizarBastanteo.esValido()) {
			if (iPieVerificarRealizarBastanteo.ingresarDictamen()) {
				LOG.info("Entro al if");
				// se carga el expediente actualizado por el servlet
				LOG.info("id expediente" + expediente.getId());
				Expediente expActualizado = expedienteDAO.load(expediente.getId());
				String flagFacEsp = expActualizado.getFlagFacultadesEspeciales();
				String flagIndBas = expActualizado.getFlagIndicadorBastanteo();				
				expediente.setFlagFacultadesEspeciales(flagFacEsp);
				expediente.setFlagIndicadorBastanteo(flagIndBas);
				expediente.setAccion(ConstantesBusiness.ACCION_FINALIZAR_BASTANTEO);
				LOG.info("opcion Resultado" + opcionResultado);
				boolean crearDocumentos = false;
				if (opcionResultado.equals(ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO)) {
					LOG.info("Expediente Aprobado");
					String accion = ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO;
					ejecutarFinalizarBastanteo(accion);
				} else if (opcionResultado.equals(ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO_PARCIAL)) {
					LOG.info("Expediente Aprobado Parcial");
					String accion = ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO_PARCIAL;
					ejecutarFinalizarBastanteoNoAprobado(accion);
				} else if (opcionResultado.equals(ConstantesBusiness.ACCION_EXPEDIENTE_OBSERVADO)) {
					LOG.info("Expediente Observado");
					
					//crear documentos de dictamen e instrucciones si se va a generar un pre-registro de subsanación
					if (expediente.getOperacion().getCodigoOperacion().equals(ConstantesBusiness.CODIGO_NUEVO_BASTANTEO)
							|| expediente.getOperacion().getCodigoOperacion().equals(ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO)) {
						// solo crear documentos en esta tarea si es la primera vez que ocurre
						Historial hist = historialDAO.findUltimaTarea(expediente.getId(), ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO);
						if (hist == null) {
							crearDocumentos = true;
						}
					}
					
					String accion = ConstantesBusiness.ACCION_EXPEDIENTE_OBSERVADO;
					ejecutarFinalizarBastanteoNoAprobado(accion);
				}
				
				// [Begin]-[15.07.23]-[Cambio valores de los documentos (casos de negocio)]
				List<DocumentoExpWrapper1> listaDocExpediente = digitaliceDocumentacionMB.getListaDocumentoExp();
				DocumentoExp docExp = null;
				
				for (int i = 0; i < listaDocExpediente.size(); i++) {
					docExp = listaDocExpediente.get(i).getDocumentoExp();
					//docExp.setFlagAlterno(null); // este flag solo es temporal
					docExpedienteDAO.update(docExp);
				}
				// [End]-[15.07.23]
				
				// FIX para que no se pierda la descripción del estado del expediente
				Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);

				AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
				ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();

				expedienteCC.getDatosFlujoCtaCte().setFlagExisteModificatoria(StringUtils.isEmpty(strInstrucciones) ? ConstantesBusiness.FLAG_NO_EXISTE_MODIFICATORIA: ConstantesBusiness.FLAG_EXISTE_MODIFICATORIA);

				expedienteCC.getDatosFlujoCtaCte().setFlagExisteFacultadesEspeciales(String.valueOf(flagFacEsp));
				
				expedienteCC.getDatosFlujoCtaCte().setResultadoBastanteo(opcionResultado);
				
				//expedienteCC.setCodUsuarioActual(empleado.getCodigo());
				
				LOG.info("FlagExisteModificatoria" + expedienteCC.getDatosFlujoCtaCte().getFlagExisteModificatoria());
				LOG.info("FlagExisteFacultadesEspeciales" + expedienteCC.getDatosFlujoCtaCte().getFlagExisteFacultadesEspeciales());
				
				String tkiid = expedienteCC.getTaskID();
				RemoteUtils bandeja = new RemoteUtils();
				//bandeja.enviarExpediente(tkiid, expedienteCC);
				bandeja.completarTarea(tkiid, expedienteCC);
				
				subirArchivos(true, crearDocumentos);
				LOG.info("Redirecciona a la bandeja");
				redirectAction("../bandeja/bandeja");
				LOG.info("Redirecciono");
			}
		}
	}
	public void ejecutarFinalizarBastanteoNoAprobado(String accion){
	// cuando el dictamen debe ser obligatorio
		LOG.info("finalizarBastanteo(ActionEvent ae)");			
		LOG.info("strDictamen"+strDictamen);					
		Tarea tarea=new Tarea();
		EstadoTarea estadoTarea=new EstadoTarea();
		if ((accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO_PARCIAL)) || (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_OBSERVADO))){
			LOG.info("entro al if -  accion" + accion);				
			expediente.setEstado(new EstadoExpediente());
			expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
			tarea.setId(ConstantesBusiness.ID_TAREA_REVISAR_EJECUTAR_REVOCATORIA);
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
		}
		expediente.setResultado(opcionResultado);
		LOG.info("Resultado"+expediente.getResultado());		
		expediente.setDictamenBastanteo(strDictamen.getBytes());
		String dictamen=new String (expediente.getDictamenBastanteo());
		LOG.info("Dictamen: "+dictamen);
		expediente.setInstruccionesBastanteo(strInstrucciones.getBytes());
		expediente.setMotivo(null);
		expediente.setObservaciones(null); // en este caso se quedaba pegada la observación de la tarea anterior en el historial
		
		//Genero un expediente Historial con los datos nuevo
		Util.generarRegistroHistExp(expediente);
		
		expediente = expedienteDAO.update(expediente);	
		
	}
	public void ejecutarFinalizarBastanteo(String accion){
		LOG.info("finalizarBastanteo(ActionEvent ae)");		
		if (strDictamen!=null){		
			Tarea tarea=new Tarea();
			EstadoTarea estadoTarea=new EstadoTarea();
			if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR)){
				expediente.setEstado(new EstadoExpediente());
				expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
				tarea.setId(ConstantesBusiness.ID_TAREA_REVISAR_APROBAR_BASTANTEO);
				estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
			}
			expediente.setResultado(opcionResultado);
			LOG.info("Resultado"+expediente.getResultado());		
			expediente.setDictamenBastanteo(strDictamen.getBytes());
			//LOG.info("Resultado"+expediente.getDictamenBastanteo());
			expediente.setInstruccionesBastanteo(strInstrucciones.getBytes());
			//LOG.info("Resultado"+expediente.getInstruccionesBastanteo());
			//expediente.setEmpleado(null);
			expediente.setMotivo(null);
			expediente.setObservaciones(null); // en este caso se quedaba pegada la observación de la tarea anterior en el historial
			
			//Genero un expediente Historial con los datos nuevo
		    Util.generarRegistroHistExp(expediente);
			//Util.generarRegistroHistExpCompletado(expediente,tarea.getId());
			
		    expediente = expedienteDAO.update(expediente);
		} else {
			LOG.info("Llamada a la interface CRITERIO DE SUPERVISION");
		}
	}
	
	public void  verificarFacultadesEspeciales(ActionEvent event){
		// Habilitación del botón Finalizar Proceso según configuración
		// [Begin]-[15.04.08]-[Habilitación del botón Finalizar Proceso según configuración]
	
		if(finalizaBastanteo) {
			LOG.info("verificarFacultadesEspeciales()");
			// finalizaBastanteo = true;	
			exitoOperacionSFP = false;
			existeFacultadesEspeciales = ConstantesBusiness.NO_EXISTE_FACULTADES_ESPECIALES;
			
			Expediente exp = expedienteDAO.load(expediente.getId());
			
			if (ConstantesBusiness.FLAG_FACULTADES_ESPECIALES.equals(exp.getFlagFacultadesEspeciales())) {
				existeFacultadesEspeciales = ConstantesBusiness.EXISTE_FACULTADES_ESPECIALES;
			}	
			
			if (ConstantesBusiness.FLAG_INDICADOR_GRABO_BASTANTEO.equals(exp.getFlagIndicadorBastanteo())){ // si se ejecuto con exito el bastanteo en SFP
				LOG.info("ingreso : ");
					exitoOperacionSFP = true; 
					finalizaBastanteo = false;
			}
			
			LOG.info("finalizaBastanteo: " + finalizaBastanteo);
			LOG.info("existeFacultadesEspeciales: " + existeFacultadesEspeciales);
		}
		// [End]-[15.04.08]-[Habilitación del botón Finalizar Proceso según configuración]
	}

	public void setIPieVerificarRealizarBastanteo(
			IPieVerificarRealizarBastanteo iPieVerificarRealizarBastanteo) {
		this.iPieVerificarRealizarBastanteo = iPieVerificarRealizarBastanteo;
	}

	public IPieVerificarRealizarBastanteo getIPieVerificarRealizarBastanteo() {
		return iPieVerificarRealizarBastanteo;
	}	

	public VerificarRealizarBastanteoMB getVerificarRealizarBastanteo() {
		return verificarRealizarBastanteo;
	}

	public void setVerificarRealizarBastanteo(
			VerificarRealizarBastanteoMB verificarRealizarBastanteo) {
		this.verificarRealizarBastanteo = verificarRealizarBastanteo;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}
	

	public String getStrDevolucion() {
		return strDevolucion;
	}

	public void setStrDevolucion(String strDevolucion) {
		this.strDevolucion = strDevolucion;
	}

	public String getStrComentarios() {
		return strComentarios;
	}

	public void setStrComentarios(String strComentarios) {
		this.strComentarios = strComentarios;
	}

	public boolean isHabilitaBotones() {
		return habilitaBotones;
	}

	public void setHabilitaBotones(boolean habilitaBotones) {
		this.habilitaBotones = habilitaBotones;
	}

	public List<SelectItem> getListaDevolucion() {
		return listaDevolucion;
	}

	public void setListaDevolucion(List<SelectItem> listaDevolucion) {
		this.listaDevolucion = listaDevolucion;
	}

	public boolean isHabilitaDevolucion() {
		return habilitaDevolucion;
	}

	public void setHabilitaDevolucion(boolean habilitaDevolucion) {
		this.habilitaDevolucion = habilitaDevolucion;
	}

	public boolean isHabilitaComentarios() {
		return habilitaComentarios;
	}

	public void setHabilitaComentarios(boolean habilitaComentarios) {
		this.habilitaComentarios = habilitaComentarios;
	}

	public boolean isHabilitaBotonDevolver() {
		return habilitaBotonDevolver;
	}

	public void setHabilitaBotonDevolver(boolean habilitaBotonDevolver) {
		this.habilitaBotonDevolver = habilitaBotonDevolver;
	}

	public void setBoolDevolver(boolean boolDevolver) {
		this.boolDevolver = boolDevolver;
	}

	public boolean isBoolDevolver() {
		return boolDevolver;
	}

	public String getOpcionResultado() {
		return opcionResultado;
	}

	public void setOpcionResultado(String opcionResultado) {
		this.opcionResultado = opcionResultado;
	}

	
	public String getStrDictamen() {
		return strDictamen;
	}

	public void setStrDictamen(String strDictamen) {
		this.strDictamen = strDictamen;
	}

	public String getStrInstrucciones() {
		return strInstrucciones;
	}

	public void setStrInstrucciones(String strInstrucciones) {
		this.strInstrucciones = strInstrucciones;
	}

	public String getSelDevolucion() {
		return selDevolucion;
	}

	public void setSelDevolucion(String selDevolucion) {
		this.selDevolucion = selDevolucion;
	}

	public boolean isExitoOperacionSFP() {
		return exitoOperacionSFP;
	}

	public void setExitoOperacionSFP(boolean exitoOperacionSFP) {
		this.exitoOperacionSFP = exitoOperacionSFP;
	}

	// [Begin]-[15.07.23]-[Cambio valores de los documentos (casos de negocio)]
	public DigitaliceDocumentacionMB getDigitaliceDocumentacionMB() {
		return digitaliceDocumentacionMB;
	}

	public void setDigitaliceDocumentacionMB(
			DigitaliceDocumentacionMB digitaliceDocumentacionMB) {
		this.digitaliceDocumentacionMB = digitaliceDocumentacionMB;
	}
	// [End]-[15.07.23]
	
	
	
}

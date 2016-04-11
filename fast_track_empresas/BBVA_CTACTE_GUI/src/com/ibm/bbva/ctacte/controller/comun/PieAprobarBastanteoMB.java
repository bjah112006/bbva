package com.ibm.bbva.ctacte.controller.comun;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.EstudioAbogado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieAprobarBastanteo;
import com.ibm.bbva.ctacte.controller.form.RevisarAprobarBastanteoMB;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EstudioAbogadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.ListSorter;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name="pieAprobarBastanteo")
@ViewScoped
public class PieAprobarBastanteoMB extends AbstractMBean {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PieAprobarBastanteoMB.class);
	@ManagedProperty(value="#{revisarAprobarBastanteo}")
	private RevisarAprobarBastanteoMB revisarAprobarBastanteo;
	private IPieAprobarBastanteo iPieAprobarBastanteo;	
	private String strComentarios;
	private String strConformidad;
	private String strInstrucciones;
	private String strDictamen;	
	private String resultado;
	private String selAbogados;	
	private Expediente expediente;	
	private Motivo motivo;
	private Empleado empleado;
	private EstadoTarea estado;
	private List<SelectItem> abogadosExternos;
	private List<SelectItem> motivosReasignados;
	private Integer idTarea;
	private String idAbogado;
	private String idReasignado;
	@EJB
	private MotivoDAO motivoDAO;
	@EJB
	private EmpleadoDAO empleadoDAO;
	@EJB
	private EstudioAbogadoDAO estudioAbogadoDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("Pagina Actual{}",pagina);
		if ("formRevisarAprobarBastanteo".equals(pagina)){
			iPieAprobarBastanteo=revisarAprobarBastanteo;
		}
		expediente=(Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		ExpedienteTarea expedienteTarea = expediente.getExpedienteTareas().iterator().next();
		idTarea = expedienteTarea.getTarea().getId().intValue();
		
		/*Motivo motivo=new Motivo();		
		motivo.setDescripcion(idReasignado);		
		expediente.setMotivo(motivo);*/
		crearListaAbogados();
		crearListaMotivosReasignacion();
		
		byte[] byteDictamen = expediente.getDictamenBastanteo();
		strDictamen = byteDictamen==null ? null : new String (byteDictamen);
		LOG.info("Dictamen"+strDictamen);
		byte[] byteInsBast = expediente.getInstruccionesBastanteo();
		strInstrucciones = byteInsBast==null ? null : new String (byteInsBast);
	}
	
	public void reasignarExpediente(ActionEvent ae){
		LOG.info("reasignarExpediente(ActionEvent ae)");
		String accion=ConstantesBusiness.ACCION_REASIGNAR_EXPEDIENTE;
		ejecutarReasignarExpediente(accion);
	}
	
	public boolean noSeleccionados(){
		LOG.info("esValido ()");
		LOG.info("idAbogado" + idAbogado);
		LOG.info("idReasignado"  + idReasignado);		
		if ((ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(idAbogado)) && (ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(idReasignado))){
			LOG.info("si ambos o uno de los dos son nulos");
			mensajeErrorPrincipal("idAbogados", "Seleccione una opción");
			return true;			
		}else if ((ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(idAbogado))){
			mensajeErrorPrincipal("idAbogados", "Seleccione el Abogado de Revisión");
			return true;
		}else if (ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(idReasignado)){
			mensajeErrorPrincipal("idAbogados", "Seleccione el Motivo de Reasignación");
			return true;
		}
		return false;			
	}
	
	
	private void ejecutarReasignarExpediente(String accion){
		LOG.info("ejecutarReasignarExpediente(String accion)");
		
		//if ((!ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(idAbogado)) || (!Coni mstantesAdmin.CODIGO_CAMPO_VACIO.equals(idReasignado))){
		if (!noSeleccionados()){
			LOG.info("Se seleccionaron datos");
			Tarea tarea=new Tarea();
			EstadoTarea estadoTarea=new EstadoTarea();
			if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_REASIGNAR_EXPEDIENTE)){
				expediente.setEstado(new EstadoExpediente());
				expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
				tarea.setId(ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO);
				estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
			}
			LOG.info("idReasignado"  + idReasignado);	
			int idReasignados=Integer.valueOf(idReasignado);
			LOG.info("idReasignados"  + idReasignados);
			motivo = motivoDAO.load(idReasignados);
			expediente.setMotivo(motivo);
			// los comentarios que registra .
			expediente.setObservaciones(strComentarios);
			LOG.info("Observaciones: "+expediente.getObservaciones());
			expediente.setResultado(resultado);
			expediente.setAccion(accion);
			Util.generarRegistroHistExp(expediente);
			expediente = expedienteDAO.update(expediente);
			
			// FIX para que no se pierda la descripción del estado del expediente
			Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);
			
			AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
			ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();	
			expedienteCC.setCodUsuarioActual(idAbogado);
			Empleado empAB = empleadoDAO.findByCodigo(idAbogado);
			
			String codEstudioabogado = "", nomEstudioAbogado = "";
			if (empAB.getEstudio()!=null && empAB.getEstudio().getId()!=null) {
				codEstudioabogado = empAB.getEstudio().getId().toString();
				EstudioAbogado estudioDAO = estudioAbogadoDAO.findByIdEstudio(empAB.getEstudio().getId());
				nomEstudioAbogado = estudioDAO.getDescripcion();
			}			
			
			expedienteCC.setNomUsuarioActual(empAB.getNombres()+" "+empAB.getApepat()+" "+empAB.getApemat());
			expedienteCC.getDatosFlujoCtaCte().setCodUsuarioAbogado(idAbogado);
			expedienteCC.getDatosFlujoCtaCte().setNomUsuarioAbogado(empAB.getNombres()+" "+empAB.getApepat()+" "+empAB.getApemat());
			expedienteCC.getDatosFlujoCtaCte().setCodEstudioAbogado(codEstudioabogado);
			expedienteCC.getDatosFlujoCtaCte().setNomEstudioAbogado(nomEstudioAbogado);
			
			expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
			String tkiid = expedienteCC.getTaskID();
			RemoteUtils bandeja = new RemoteUtils();
			//bandeja.enviarExpediente(tkiid, expedienteCC);
			bandeja.completarTarea(tkiid, expedienteCC);
			subirArchivos(true);
			redirectAction("../bandeja/bandeja");
		}	
	}	
	
	public void terminarExpediente(ActionEvent ae){
		LOG.info("terminarExpediente(ActionEvent ae");
		// Conformidad registrada por el usuario		
		String accion=ConstantesBusiness.ACCION_EXPEDIENTE_TERMINAR;	 
		ejecutarTerminarExpediente(accion);
		redirectAction("../bandeja/bandeja");
	}
	
	private void ejecutarTerminarExpediente(String accion){
		LOG.info("ejecutarTerminarExpediente(String accion)");
		if (!(strInstrucciones==null || strInstrucciones.trim().equals(""))){ // si hay Modificatoria
			LOG.info("Instrucciones esta lleno");
			Tarea tarea=new Tarea();
			EstadoTarea estadoTarea=new EstadoTarea();
			if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_TERMINAR)){
				expediente.setEstado(new EstadoExpediente());
				expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
				tarea.setId(ConstantesBusiness.ID_TAREA_EJECUTAR_CONFIRMAR_MODIFICATORIAS);
				estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
			}
			expediente.setDictamenBastanteo(strDictamen==null ? null : strDictamen.getBytes());
			expediente.setInstruccionesBastanteo(strInstrucciones==null ? null : strInstrucciones.getBytes());
			expediente.setObservacionesBastanteo(strConformidad);			
			LOG.info("existe modificatoria" + expediente.getFlagModificatoria());
			
			expediente.setMotivo(null);
			Util.generarRegistroHistExp(expediente);
			expediente = expedienteDAO.update(expediente);
			
		} else { // si no hay Modificatoria
			LOG.info("Instrucciones esta vacio");
			EstadoTarea estadoTarea=new EstadoTarea();
			if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_TERMINAR)){
				expediente.setEstado(new EstadoExpediente());
				expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);				
				estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO);
			}
			expediente.setObservacionesBastanteo(strConformidad);	
			
			expediente.setMotivo(null);
			Util.generarRegistroHistExp(expediente);
			
			Expediente exp = expedienteDAO.load(expediente.getId());
			expediente.setFlagFacultadesEspeciales(exp.getFlagFacultadesEspeciales());
			expediente.setFlagIndicadorBastanteo(exp.getFlagIndicadorBastanteo());

			expediente = expedienteDAO.update(expediente);
		}
		
		// FIX para que no se pierda la descripción del estado del expediente
		Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);
		
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		if (!(strInstrucciones==null || strInstrucciones.trim().equals("")))
			expedienteCC.getDatosFlujoCtaCte().setFlagExisteModificatoria("1");
		else
			expedienteCC.getDatosFlujoCtaCte().setFlagExisteModificatoria("0");
		//expedienteCC.setCodUsuarioActual(idAbogado);
		expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
		String tkiid = expedienteCC.getTaskID();
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("tkiid : "+tkiid);
		RemoteUtils bandeja = new RemoteUtils();
		//bandeja.enviarExpediente(tkiid, expedienteCC);
		bandeja.completarTarea(tkiid, expedienteCC);
		subirArchivos(true);
		//subirArchivos(true, true);
	}

	private void crearListaAbogados(){
		LOG.info("crearListaAbogados()");		
		List<Empleado> listaAbogados=empleadoDAO.getAbogadosBastanteo(ConstantesBusiness.EMPLEADO_TIPO_ABOGADO);
		abogadosExternos=Util.crearItems(listaAbogados,true,"codigo","nombres,apepat,apemat", "{0} {1} {2}", true);
	}
	
	private void crearListaMotivosReasignacion(){
		LOG.info("crearListaMotivosReasignacion()");
		List<Motivo> listaReasignados=motivoDAO.findByTarea(ConstantesBusiness.ID_TAREA_REVISAR_APROBAR_BASTANTEO, ConstantesBusiness.MOTIVO_REASIGNADOS);
		setMotivosReasignados(Util.crearItems(listaReasignados,true,"id","descripcion"));		
		ListSorter.ordenarMotivos(motivosReasignados);
	}	
	
	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}
	
	public RevisarAprobarBastanteoMB getRevisarAprobarBastanteo() {
		return revisarAprobarBastanteo;
	}

	public void setRevisarAprobarBastanteo(
			RevisarAprobarBastanteoMB revisarAprobarBastanteo) {
		this.revisarAprobarBastanteo = revisarAprobarBastanteo;
	}

	public IPieAprobarBastanteo getIPieAprobarBastanteo() {
		return iPieAprobarBastanteo;
	}

	public void setIPieAprobarBastanteo(IPieAprobarBastanteo pieAprobarBastanteo) {
		iPieAprobarBastanteo = pieAprobarBastanteo;
	}

	public List<SelectItem> getAbogadosExternos() {
		return abogadosExternos;
	}

	public void setAbogadosExternos(List<SelectItem> abogadosExternos) {
		this.abogadosExternos = abogadosExternos;
	}

	public void setMotivosReasignados(List<SelectItem> motivosReasignados) {
		this.motivosReasignados = motivosReasignados;
	}

	public List<SelectItem> getMotivosReasignados() {
		return motivosReasignados;
	}

	public String getStrComentarios() {
		return strComentarios;
	}

	public void setStrComentarios(String strComentarios) {
		this.strComentarios = strComentarios;
	}
	
	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getResultado() {
		return resultado;
	}
	
	public EstadoTarea getEstado() {
		return estado;
	}

	public void setEstado(EstadoTarea estado) {
		this.estado = estado;
	}

	public Integer getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(Integer idTarea) {
		this.idTarea = idTarea;
	}


	public String getStrConformidad() {
		return strConformidad;
	}


	public void setStrConformidad(String strConformidad) {
		this.strConformidad = strConformidad;
	}

	public String getStrInstrucciones() {
		return strInstrucciones;
	}

	public void setStrInstrucciones(String strInstrucciones) {
		this.strInstrucciones = strInstrucciones;
	}

	public String getStrDictamen() {
		return strDictamen;
	}

	public void setStrDictamen(String strDictamen) {
		this.strDictamen = strDictamen;
	}

	public void setIdAbogado(String idAbogado) {
		this.idAbogado = idAbogado;
	}

	public String getIdAbogado() {
		return idAbogado;
	}

	public String getSelAbogados() {
		return selAbogados;
	}

	public void setSelAbogados(String selAbogados) {
		this.selAbogados = selAbogados;
	}

	public String getIdReasignado() {
		return idReasignado;
	}

	public void setIdReasignado(String idReasignado) {
		this.idReasignado = idReasignado;
	}
	


}

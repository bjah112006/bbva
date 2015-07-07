package com.ibm.bbva.ctacte.controller.comun;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTareaProceso;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.bean.TareasReasig;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaProcesoDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.dao.TareasReasigDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="reasignacionExpediente")
@ViewScoped
public class ReasignacionExpedienteMB extends AbstractMBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7180946667147409679L;
	/**
	 * 
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ReasignacionExpedienteMB.class);
	private List<Empleado> listaUsuarios;
	private List<Motivo> listaMotivos;
	private List<Perfil> listaPerfil;
	private String codUsuario = null;
	private String idMotivo = null;
	private boolean desHabilitarBotonReasignar;
	private boolean perfilSupervisor;
	private boolean motivoSeleccionado;
	private boolean usuarioSeleccionado;
	
	private boolean isModoConsulta;
	private boolean bolReasignar;
	private boolean isAbogadoRevision;

	@EJB
	private MotivoDAO motivoDAO;
	@EJB
	private EmpleadoDAO empleadoDAO;
	@EJB
	private TareaDAO tareaDAO;
	@EJB
	private TareasReasigDAO tareaReasigDAO;
	@EJB
	private ExpedienteTareaProcesoDAO expTareaProcesoDAO;

	public ReasignacionExpedienteMB() {
	}
	
	@PostConstruct
	public void iniciar() {
		LOG.info("ReasignacionExpedienteMB()");
		ExpedienteCC expedienteCC = (ExpedienteCC) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION);
		listaUsuarios = getPosiblesUsuariosOwnersPerfil(expedienteCC.getCodUsuarioActual());
		listaMotivos = motivoDAO.getMotivos(ConstantesBusiness.TIPO_MOTIVO_REASIGNACION_BANDEJA);
		
		codUsuario = ConstantesAdmin.CODIGO_CAMPO_VACIO;
		idMotivo = ConstantesAdmin.CODIGO_CAMPO_VACIO;
		desHabilitarBotonReasignar = true;
		
		perfilSupervisor=isSupervisorDeUsuarioActual(expedienteCC.getCodUsuarioActual());
		isModoConsulta=isModoConsulta();
		isAbogadoRevision=isAbogadoRevision();
		LOG.info("************perfilSupervisor-->"+perfilSupervisor);
		LOG.info("************isModoConsulta-->"+isModoConsulta);
		LOG.info("************isAbogadoRevision-->"+isAbogadoRevision);
		
		String pagina = getNombrePrincipal();
		LOG.info("**********Pagina**********"+pagina);
		bolReasignar=false;
		if (isModoConsulta && perfilSupervisor  &&  !(pagina.equalsIgnoreCase("formGestionarCobroComision"))) {
			bolReasignar=true;
		} else if (!isModoConsulta && isAbogadoRevision) {
			bolReasignar=true; // el abogado de revisión puede reasignar sus propios expedientes
		} else {
			bolReasignar=false;
		}
		LOG.info("**********bolReasignar**********"+bolReasignar);
	}
	
	public boolean isAbogadoRevision() {
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		if (empleado.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isSupervisorDeUsuarioActual(String codUsuario) {
		if (isSupervisor()) {
			List<Empleado> listEmpleadosSupervisados = getUsuariosVistosPorPerfil();
			boolean resultado = false;
			for (Empleado emp : listEmpleadosSupervisados) {
				if (emp.getCodigo().equals(codUsuario)) {
					resultado = true;
					break;
				}
			}
			return resultado;
		} else {
			// El abogado de revisión puede reasignar expedientes de los abogados de revisión y abogados de bastanteo
			Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
			if (empleado.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION)) {
				Empleado empleadoActual = empleadoDAO.findByCodigo(codUsuario);
				if (empleadoActual.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_BASTANTEO)
						|| empleadoActual.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	public  List<Empleado> getPosiblesUsuariosOwnersPerfil(String codUsuario){
		LOG.info("getPosiblesUsuariosOwnersPerfil(String codUsuario)");
		List<Empleado> listaEmp = null;
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		if (empleado.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_ABOGADO_BASTANTEO)) {
			if (empleado.getEstudio() != null) {
				listaEmp = empleadoDAO.getPosiblesOwnersPorEstudio(codUsuario, empleado.getEstudio().getId());
			} else {
				LOG.warn("El empleado "+empleado.getCodigo()+" con perfil Supervisor Abogado de bastanteo no tiene estudio de abogado.");
				listaEmp = new ArrayList<Empleado>();
			}
		} else {
			/*Devuelve el resto de usuarios que tienen el mismo perfil que el usuario 
			 *que viene por parámetro
			 */
			listaEmp = empleadoDAO.getPosiblesOwners(codUsuario);
		}
		return listaEmp;
	}	
	
	public void reasignarExpediente(ActionEvent action){
		LOG.info("reasignarExpediente (ActionEvent action)");
		
		ExpedienteCC expedienteCC = (ExpedienteCC) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION);
		
		//String numeroTarea = (expedienteCC.getNumeroTarea() == null?"0":expedienteCC.getNumeroTarea());
		String numeroTarea = (expedienteCC.getDatosFlujoCtaCte().getIdTarea() == null?"0":expedienteCC.getDatosFlujoCtaCte().getIdTarea());
		int idTarea = Integer.parseInt(numeroTarea);
		Tarea tarea = tareaDAO.findById(idTarea);
		
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		
		//Empleado de = empleadoDAO.load(expediente.getEmpleado().getId());
		Empleado de = empleadoDAO.findByCodigo(expedienteCC.getCodUsuarioActual());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Usuario de.."+de.getId());
		Empleado a = empleadoDAO.findByCodigo(codUsuario);
		//expedienteCC.setIdUsuarioActual(a.getId()+"");
		//expedienteCC.setCodUsuarioActual(codUsuario);
		//expedienteCC.setNomUsuarioActual(a.getNombresCompletos());
		
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Usuario a.."+a.getId());
		
		String tkiid = expedienteCC.getTaskID();
		RemoteUtils bandeja = new RemoteUtils();

		if (a != null) {
		    bandeja.transferirTarea(expedienteCC.getCodUsuarioActual(),codUsuario,tkiid);
		    
		    expedienteCC.setCodUsuarioActual(a.getCodigo());
		    expedienteCC.setNomUsuarioActual(a.getNombres()+" "+a.getApepat()+" "+a.getApemat());
		    expedienteCC.setCodOficina(a.getOficina().getCodigo());
		    
		    if (a.getFlagAbogado() != null && a.getFlagAbogado().equals("1")) {
		    	expedienteCC.getDatosFlujoCtaCte().setCodUsuarioAbogado(a.getCodigo());
		    	expedienteCC.getDatosFlujoCtaCte().setIdUsuarioAbogado(a.getId().toString());
		    	expedienteCC.getDatosFlujoCtaCte().setNomUsuarioAbogado(a.getNombres()+" "+a.getApepat()+" "+a.getApemat());
		    	if (a.getEstudio() != null) {
		    		expedienteCC.getDatosFlujoCtaCte().setCodEstudioAbogado(a.getEstudio().getId().toString());
		    		expedienteCC.getDatosFlujoCtaCte().setNomEstudioAbogado(a.getEstudio().getDescripcion());
		    	} else {
		    		expedienteCC.getDatosFlujoCtaCte().setCodEstudioAbogado("");
		    		expedienteCC.getDatosFlujoCtaCte().setNomEstudioAbogado("");
		    	}
		    } else {
		    	expedienteCC.getDatosFlujoCtaCte().setCodUsuarioAbogado("");
		    	expedienteCC.getDatosFlujoCtaCte().setIdUsuarioAbogado("");
		    	expedienteCC.getDatosFlujoCtaCte().setNomUsuarioAbogado("");
		    	expedienteCC.getDatosFlujoCtaCte().setCodEstudioAbogado("");
		    	expedienteCC.getDatosFlujoCtaCte().setNomEstudioAbogado("");
		    }
		    expedienteCC.getOperacionesCtaCte().setIdEmpleadoActual(a.getId());
		    
		    bandeja.enviarExpediente(tkiid, expedienteCC);
		}
		
		TareasReasig tareaReasig = new TareasReasig();
		tareaReasig.setTarea(tarea);
		tareaReasig.setEmpleadoByIdEmpleadoAFk(a);
		tareaReasig.setEmpleadoByIdEmpleadoDeFk(de);
		tareaReasig.setExpediente(expediente);
		
		Motivo motivo = motivoDAO.load(Integer.valueOf(idMotivo));
		tareaReasig.setMotivo(motivo);
		
		tareaReasigDAO.save(tareaReasig);
		
		// se actualiza la carga de trabajo al nuevo usuario
		ExpedienteTareaProceso expTarProc = expTareaProcesoDAO.findExpedienteTareaProceso(expediente.getId(), de.getId(), idTarea);
		if (expTarProc != null) {
			expTarProc.setIdEmpleado(a.getId());
			expTareaProcesoDAO.update(expTarProc);
		} else {
			LOG.warn("No existe carga de trabajo: (idExpediente={}, idEmpleado={}, idTarea={})", new Object[]{expediente.getId(), de.getId(), idTarea});
		}
		
		//		managedBean.aprobarDocumentacion(accion);
		redirectAction("../bandeja/bandeja");
		
	}


	public void cerrar(ActionEvent action){
		LOG.info("cerrar (ActionEvent action)");
		
		redirectAction("../bandeja/bandeja");
	}
	
	public void seleccionarUsuario (ValueChangeEvent ae) {
		LOG.info("seleccionarUsuario (ValueChangeEvent ae)");
		String strValue = (String)ae.getNewValue();
		usuarioSeleccionado = false;
		LOG.info("strValue-->"+strValue);
		strValue = (strValue==null?"-1":strValue);
		if (!strValue.equalsIgnoreCase(ConstantesAdmin.CODIGO_CAMPO_VACIO) )
			usuarioSeleccionado = true;

		validarEstadoBtnReasignar();
	}

	public void seleccionarMotivo (ValueChangeEvent ae) {
		LOG.info("seleccionarMotivo (ValueChangeEvent ae)");
		String strValue = (String)ae.getNewValue();
		motivoSeleccionado=false;
		LOG.info("strValue-->"+strValue);
		strValue = (strValue==null?"-1":strValue);
		if (!strValue.equalsIgnoreCase(ConstantesAdmin.CODIGO_CAMPO_VACIO) )
			motivoSeleccionado = true;
		
		validarEstadoBtnReasignar();
	}
	
	public void validarEstadoBtnReasignar(){
		desHabilitarBotonReasignar = true;
		if (motivoSeleccionado && usuarioSeleccionado){ 
			desHabilitarBotonReasignar = false;
		}
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("desHabilitarBotonReasignar.."+desHabilitarBotonReasignar);
	}
	
	
	
	public boolean isPerfilSupervisor() {
		return perfilSupervisor;
	}

	public void setPerfilSupervisor(boolean perfilSupervisor) {
		this.perfilSupervisor = perfilSupervisor;
	}

	public List<SelectItem> getUsuariosItems() {
		return Util.crearItems(listaUsuarios, "codigo", "nombresCompletos");
	}

	public void setListaUsuarios(List<Empleado> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public List<Motivo> getListaMotivos() {
		return listaMotivos;
	}
	
	public List<Empleado> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaMotivos(List<Motivo> listaMotivos) {
		this.listaMotivos = listaMotivos;
	}

	public List<SelectItem> getMotivosItems() {
		return Util.crearItems(listaMotivos, "id", "descripcion");
	}

	public String getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}

	public String getIdMotivo() {
		return idMotivo;
	}

	public void setIdMotivo(String idMotivo) {
		this.idMotivo = idMotivo;
	}

	public boolean isDesHabilitarBotonReasignar() {
		return desHabilitarBotonReasignar;
	}

	public void setDesHabilitarBotonReasignar(boolean desHabilitarBotonReasignar) {
		this.desHabilitarBotonReasignar = desHabilitarBotonReasignar;
	}
	
	public boolean isBolReasignar() {
		return bolReasignar;
	}

	public void setBolReasignar(boolean bolReasignar) {
		this.bolReasignar = bolReasignar;
	}

}
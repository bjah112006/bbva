package com.ibm.bbva.ctacte.controller.comun;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ConsultaCC;
import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bean.ExpedienteCC_H;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosExpediente;
import com.ibm.bbva.ctacte.controller.form.VerificarResultadoTramiteMB;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.HistorialDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.util.Util;

//@001A 11092015 Mostrar flag de migracion.
@ManagedBean (name="datosExpediente2")
@ViewScoped
public class DatosExpediente2MB extends AbstractMBean{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DatosExpediente2MB.class);
	@ManagedProperty (value="#{verificarResultadoTramite}")
	private VerificarResultadoTramiteMB verificarResultadoTramite;	
	private IDatosExpediente iDatosExpediente;	
	private Expediente expediente;
	private String exonerado;
	private int idTarea;
	private String esMigrado;
	private List<ExpedienteCC> expedientesPorCerrar;
	private List<ExpedienteCC_H> expedientesPorCerrar_H;
	private List<ExpedienteCC> expedientesPorCerrarDepurada;

	@EJB
	private ClienteBusiness buscarCliente;


	@EJB
	private ExpedienteDAO expedienteDAO;

	@EJB
	private TareaDAO tareaDAO;

	@EJB
	private HistorialDAO historialDAO;

	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("Pagina actual{}", pagina);
		if ("formVerificarResultadoTramite".equals(pagina)){			
			iDatosExpediente=verificarResultadoTramite;			
			iDatosExpediente.setIDatosExpediente2(this);
		}		
		expediente = (Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		exonerado = String.valueOf(expediente.getFlagExoneracionComision());

		//@001A08L
		Cliente c = buscarCliente.getDatosCliente(expediente.getCliente().getId());
		if (c!=null) {
			try {
				esMigrado = (!c.getFlagOrigenSFP().equalsIgnoreCase("1")) ? "No" : "Si"; 
			} catch (Exception e) {
				LOG.info("Error al verificar si el cliente es migrado", e);
			}
		}

		expedientesPorCerrar_H = new ArrayList<ExpedienteCC_H>();
		expedientesPorCerrarDepurada = new ArrayList<ExpedienteCC>();


		Expediente expSubsanacion =expedienteDAO.findByIdExpUltBastanteo(expediente.getId());
		LOG.info("DatosExpediente2MB - Iniciar - expediente.getId()" + expediente.getId());
		if( expSubsanacion != null){
			//creamos un objeto basico.
			LOG.info("expSubsanacion EXISTE" + expSubsanacion);
			ExpedienteCC_H expMostrar = new ExpedienteCC_H();
			expMostrar.setCodigoExpediente(String.valueOf(expSubsanacion.getId()));
			expMostrar.setMostrarExpediente(false);

			//Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
			RemoteUtils bandejaTareasBDelegate = new RemoteUtils();
			ConsultaCC consulta = new ConsultaCC();
			consulta.setNumeroTarea(Integer.toString(ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE));
			consulta.setConsiderarUsuarios(false);
			consulta.setCodigoExpediente(String.valueOf(expSubsanacion.getId()));
			expedientesPorCerrar = bandejaTareasBDelegate.obtenerInstanciasTareasPorUsuarioCC(consulta);
			
			LOG.info("expedientesPorCerrar cantidad:"+expedientesPorCerrar.size());
			for(ExpedienteCC e:expedientesPorCerrar){		
				if(!e.getCodigoExpediente().isEmpty()){
					LOG.info("e.getCodigoExpediente().isEmpty()"+e.getCodigoExpediente().isEmpty());
					expMostrar.setActivado(e.getActivado());
					expMostrar.setCodCentralCliente(e.getCodCentralCliente());
					expMostrar.setCodigoExpediente(e.getCodigoExpediente());
					expMostrar.setCodOficina(e.getCodOficina());
					expMostrar.setCodOperacion(e.getCodOperacion());
					expMostrar.setCodSemaforo(e.getCodSemaforo());
					expMostrar.setCodUsuarioActual(e.getCodUsuarioActual());
					expMostrar.setDatosFlujoCtaCte(e.getDatosFlujoCtaCte());
					expMostrar.setDesOficina(e.getDesOficina());
					expMostrar.setDesOperacion(e.getDesOperacion());
					expMostrar.setDesTerritorio(e.getDesTerritorio());
					expMostrar.setEnvioContent(e.getEnvioContent());
					expMostrar.setEstadoExpediente(e.getEstadoExpediente());
					expMostrar.setEstadoTarea(e.getEstadoTarea());
					expMostrar.setFechaRegistro(e.getFechaRegistro());
					expMostrar.setFechaServidorP(e.getFechaServidorP());
					expMostrar.setFechaUltimoBastanteo(e.getFechaUltimoBastanteo());
					expMostrar.setIdEstadoExpediente(e.getIdEstadoExpediente());
					expMostrar.setIdEstadoTarea(e.getIdEstadoTarea());
					expMostrar.setIdOperacion(e.getIdOperacion());
					expMostrar.setNombreTarea(e.getNombreTarea());
					expMostrar.setNomUsuarioActual(e.getNomUsuarioActual());
					expMostrar.setNumDOICliente(e.getNumDOICliente());
					expMostrar.setNumeroContrato(e.getNumeroContrato());
					expMostrar.setNumeroTarea(e.getNumeroTarea());
					expMostrar.setOperacionesCtaCte(e.getOperacionesCtaCte());
					expMostrar.setRazonSocialCliente(e.getRazonSocialCliente());
					expMostrar.setTaskID(e.getTaskID());
					expMostrar.setMostrarExpediente(true);
				}
			}

			expedientesPorCerrar_H.add(expMostrar);

		}
	}



	public void irACerrarExpdiente() {
		LOG.info("irACerrarExpdiente");

		if (!expedientesPorCerrar.isEmpty()) {
			ExpedienteCC_H expedienteCC= expedientesPorCerrar_H.get(0);
			
			Expediente expediente = expedienteDAO.load(Integer.parseInt(expedienteCC.getCodigoExpediente()));
			expediente.setNumTerminal(Util.obtenerIp());
			expediente.setFechaEnvio(expedienteCC.getActivado().getTime());
			Tarea tarea = tareaDAO.load(Integer.valueOf(expedienteCC.getDatosFlujoCtaCte().getIdTarea()));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(expediente.getFechaEnvio());
			calendar.add(Calendar.MINUTE, tarea.getVerdeMinutos());
			expediente.setFechaProgramada(calendar.getTime());

			ExpedienteTarea expedienteTarea = expediente.getExpedienteTareas().iterator().next();
			expedienteTarea.setTarea(tarea);
			String pagina = "../" + tarea.getNombrePagina();
			LOG.info("redirect : {}", pagina);

			Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION, expedienteCC);
			Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);		
			Util.addObjectSession(ConstantesAdmin.RESPONSABLE_SESION, expediente.getEmpleado().getCodigo());

			redirectAction(pagina);
		}
	}


	public List<ExpedienteCC> getExpedientesPorCerrar() {
		return expedientesPorCerrar;
	}

	public String getExonerado() {
		return exonerado;
	}

	public void setExonerado(String exonerado) {
		this.exonerado = exonerado;
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

	public String getEsMigrado() {
		return esMigrado;
	}

	public void setEsMigrado(String esMigrado) {
		this.esMigrado = esMigrado;
	}


	public List<ExpedienteCC> getExpedientesPorCerrarDepurada() {
		return expedientesPorCerrarDepurada;
	}


	public void setExpedientesPorCerrarDepurada(
			List<ExpedienteCC> expedientesPorCerrarDepurada) {
		this.expedientesPorCerrarDepurada = expedientesPorCerrarDepurada;
	}


	public List<ExpedienteCC_H> getExpedientesPorCerrar_H() {
		return expedientesPorCerrar_H;
	}


	public void setExpedientesPorCerrar_H(List<ExpedienteCC_H> expedientesPorCerrar_H) {
		this.expedientesPorCerrar_H = expedientesPorCerrar_H;
	}




}

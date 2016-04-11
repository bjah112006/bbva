package com.ibm.bbva.ctacte.controller.form;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteCuenta;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.DigitaliceDocumentacionMB;
import com.ibm.bbva.ctacte.controller.comun.FinaliceSolicitud1MB;
import com.ibm.bbva.ctacte.controller.comun.IdentifiquePJOperacion2MB;
import com.ibm.bbva.ctacte.controller.comun.interf.IDigitaliceDocumentacion;
import com.ibm.bbva.ctacte.controller.comun.interf.IFinaliceSolicitud1;
import com.ibm.bbva.ctacte.controller.comun.interf.IIdentifiquePJOperacion2;
import com.ibm.bbva.ctacte.dao.CuentaDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteCuentaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.util.AyudaCobroComision;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.RespuestaServicioDTO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="preRegistro")
@ViewScoped
public class PreRegistroMB extends AbstractMBean 
		implements IIdentifiquePJOperacion2, IDigitaliceDocumentacion, IFinaliceSolicitud1{

	private static final long serialVersionUID = 3002607858152390492L;
	private static final Logger LOG = LoggerFactory.getLogger(PreRegistroMB.class);
	
	private IdentifiquePJOperacion2MB identifiquePJOperacion2;
	private DigitaliceDocumentacionMB digitaliceDocumentacion;
	private FinaliceSolicitud1MB finaliceSolicitud1;
	private boolean faltaDocumentos;
	private Character flagDocumentos;

	private List<DocumentoExp> listaDocs;
	private List<Cuenta> cuentas;
	
	private Date fechaActual = new Date ();
	
	@EJB
	private ExpedienteBusiness business;
	@EJB
	private DocumentoExpDAO docdao;
	@EJB
	private CuentaDAO ctadao;
	@EJB
	private ExpedienteCuentaDAO expedienteCuentaDAO;
	@EJB
	private ExpedienteDAO expdao;
	
	@PostConstruct
	public void iniciar () {
		iniciarEnvioFTP();
	}
	
	public void actualizarSubidaDocumentos(Character flagDocumentos) {
		LOG.info("actualizarSubidaDocumentos(boolean faltaDocumentos)");
		LOG.info("faltaDocumentos : {}", faltaDocumentos);
		this.faltaDocumentos = ConstantesBusiness.FALTA_DOCUMENTOS.equals(flagDocumentos) ||
				ConstantesBusiness.SIN_DOCUMENTOS.equals(flagDocumentos);
		this.flagDocumentos = flagDocumentos;
		if (finaliceSolicitud1!=null) {
			if (faltaDocumentos) {
				finaliceSolicitud1.setDisRegEnvExp(true);
			} else {
				finaliceSolicitud1.setDisRegEnvExp(false);
			}
		}
	}

	public void copiarDatos() {
		LOG.info("copiarDatos()");
		Expediente ee = (Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		identifiquePJOperacion2.copiarDatos ();
		//LOG.info("exp = Expediente : {}",ee==identifiquePJOperacion1.getExpediente());
		LOG.info("exp1 : {}  --  copiarDatos cliente : {}",ee,ee.getCliente());
		//LOG.info("exp2 : {}  --  copiarDatos cliente : {}",identifiquePJOperacion1.getExpediente(),identifiquePJOperacion1.getExpediente().getCliente());
		cuentas = identifiquePJOperacion2.getCuentas();
		LOG.info("exp : {}  --  copiarDatos cliente : {}",ee,ee.getCliente());
		digitaliceDocumentacion.copiarDatos ();
		LOG.info("exp : {}  --  copiarDatos cliente : {}",ee,ee.getCliente());
		listaDocs = digitaliceDocumentacion.getListaDocExp();
		LOG.info("exp : {}  --  copiarDatos cliente : {}",ee,ee.getCliente());
		finaliceSolicitud1.copiarDatos ();
	}

	public boolean esValido() {
		LOG.info("esValido()");
		return identifiquePJOperacion2.esValido() & digitaliceDocumentacion.esValido()
				& finaliceSolicitud1.esValido();
	}

	public void setFinaliceSolicitud1(FinaliceSolicitud1MB finaliceSolicitud1MB) {
		this.finaliceSolicitud1 = finaliceSolicitud1MB;
		actualizarSubidaDocumentos (flagDocumentos);
	}

	public void setIdentifiquePJOperacion2(
			IdentifiquePJOperacion2MB identifiquePJOperacion2) {
		this.identifiquePJOperacion2 = identifiquePJOperacion2;
	}

	public void setDigitaliceDocumentacion(
			DigitaliceDocumentacionMB digitaliceDocumentacion) {
		this.digitaliceDocumentacion = digitaliceDocumentacion;
	}

	public void guardarExpediente(int idEstadoExp, int idTarea, int idEstado,
			String accion) {
		LOG.info("guardarExpediente (int idEstadoExp, int idTarea, int idEstado, String accion)");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		LOG.info("expediente "+expediente.hashCode());
		business.guardarExpediente(expediente, idEstadoExp, idTarea, idEstado, accion, null);
		Character flagExisteFRF = ConstantesBusiness.FLAG_NO_EXISTE_FRF;
		Character flagExisteDOI = ConstantesBusiness.FLAG_NO_EXISTE_DOI;
		for (DocumentoExp de : listaDocs) {
			DocumentoExp detemp = docdao.findByCodDocExp(de.getDocumento().getCodigoDocumento(), expediente.getId());
			if (detemp == null) {
				de.setExpediente(expediente);
				docdao.save(de);
			} else {
				de.setExpediente(expediente);
				de.setId(detemp.getId());
				docdao.update(de);
			}
			String cod = de.getDocumento().getCodigoDocumento();
			if (ConstantesBusiness.CODIGO_DOCUMENTO_FICHA_REGISTRO_FIRMAS.equals(cod)) {
				if (ConstantesBusiness.FLAG_ESCANEADO.equals(de.getFlagEscaneado())) {
					flagExisteFRF = ConstantesBusiness.FLAG_EXISTE_FRF;
				}
			} else if (ConstantesBusiness.CODIGO_DOCUMENTO_OFICIAL_IDENTIDAD.equals(cod)) {
				if (ConstantesBusiness.FLAG_ESCANEADO.equals(de.getFlagEscaneado())) {
					flagExisteDOI = ConstantesBusiness.FLAG_EXISTE_DOI;
				}
			}
		}///////////////////////
		List<ExpedienteCuenta> listaEC = expedienteCuentaDAO.findListaExpedienteCuenta(expediente.getId());
		for (Cuenta cuenta : cuentas) {
			Cliente cliente = expediente.getCliente();
			for (Participe p : cuenta.getParticipes()) {
				p.setCliente(cliente);
				p.setCuenta(cuenta);
			}
			cuenta.setCliente(cliente);
			ctadao.update(cuenta);
			
			ExpedienteCuenta ect = null;
			for (ExpedienteCuenta ec : listaEC) {
				if (cuenta.getId().equals(ec.getIdCuenta())) {
					ect = ec;
					listaEC.remove(ect);
					break;
				}
			}
			if (ect == null) {
				ExpedienteCuenta expedienteCuenta = new ExpedienteCuenta ();
				expedienteCuenta.setIdCuenta(cuenta.getId());
				expedienteCuenta.setIdExpediente(expediente.getId());
				expedienteCuentaDAO.save(expedienteCuenta);
			}
		}
		for (ExpedienteCuenta ece : listaEC) {
			expedienteCuentaDAO.delete(ece);
		}
		//////////////////////////
		if (ConstantesAdmin.ACCION_REGISTRAR_ENVIAR_EXPEDIENTE.equals(accion)) {
			AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
			Date fechaUltimoBastanteo = null;
			if (ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())) {
				fechaUltimoBastanteo = identifiquePJOperacion2.getUltimExpBast().getFechaFin();
			}
//			if (ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())) {
//				List<Expediente> lstExpedientesAntiguos = expdao
//						.findByCodigoCentralSinIdExpediente(expediente
//								.getCliente().getCodigoCentral(), expediente
//								.getId());
//				if (lstExpedientesAntiguos == null || lstExpedientesAntiguos.isEmpty()) {
//					flagExisteFRF = ConstantesBusiness.FLAG_EXISTE_FRF;
//					flagExisteDOI = ConstantesBusiness.FLAG_EXISTE_DOI;
//				}
//			}
			ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosCU01(fechaUltimoBastanteo, flagExisteFRF, flagExisteDOI);
			Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
			expedienteCC.getDatosFlujoCtaCte().setCodUsuarioResponsable(empleado.getCodigo()); 
			expedienteCC.getDatosFlujoCtaCte().setNomUsuarioResponsable(empleado.getNombres()+" "+
					empleado.getApepat()+" "+empleado.getApemat());	
			RemoteUtils bandeja = new RemoteUtils();
			if (expedienteCC.getDatosFlujoCtaCte().getClienteExonerado().equals(ConstantesBusiness.FLAG_COBRO_COMISION)) {
				RespuestaServicioDTO rpta = AyudaCobroComision.cobroComisionInmediato(String.valueOf(expediente.getId()), expedienteCC.getDatosFlujoCtaCte().getCodUsuarioResponsable());
				if (rpta.getCodigo().equals("0")){
					expedienteCC.getOperacionesCtaCte().setFlagCobroComision(ConstantesBusiness.FLAG_INDICADOR_EJECUTO_COBRO_COMISION_INMEDIATO);
				} else {
					expedienteCC.getOperacionesCtaCte().setFlagCobroComision(ConstantesBusiness.FLAG_INDICADOR_FALLO_COBRO_COMISION_INMEDIATO);
				}
				LOG.info("expedienteCC.getOperacionesCtaCte().getFlagCobroComision(): {}", expedienteCC.getOperacionesCtaCte().getFlagCobroComision());
				
				Util.addObjectSession(ConstantesAdmin.RESPUESTA_SERVICIO_SESION, rpta);
			}
			bandeja.iniciarInstanciaProceso(String.valueOf(expediente.getId()), expedienteCC);
			Util.generarRegistroHistExp(expediente);
		}
		subirArchivos(true);
	}

	public void mostrarComponentes(Cuenta cuenta) {
		if (finaliceSolicitud1!=null) {
			finaliceSolicitud1.setMostrar(true);
		}
		if (digitaliceDocumentacion != null) {
			digitaliceDocumentacion.setMostrar(true);
			digitaliceDocumentacion.setCuenta(cuenta);
			digitaliceDocumentacion.setCodigoCentral(cuenta.getCliente().getCodigoCentral());
			digitaliceDocumentacion.seleccionarTipoPJ();
		}
	}
	
	public void ocultarComponentes() {
		if (finaliceSolicitud1!=null) {
			finaliceSolicitud1.setMostrar(false);
		}
		if (digitaliceDocumentacion != null) {
			digitaliceDocumentacion.setMostrar(false);
		}
	}

	public boolean hayIndicaciones() {
		return false;
	}

	public void habilitarBoton(boolean habReasignar) {
	}
	
	public Date getFechaActual() {
		return fechaActual;
	}

	@Override
	public void grabarReenviarExpediente(String accion) {
	}

}

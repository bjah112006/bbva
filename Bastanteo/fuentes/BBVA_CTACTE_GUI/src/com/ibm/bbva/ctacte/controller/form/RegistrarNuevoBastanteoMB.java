package com.ibm.bbva.ctacte.controller.form;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.AyudaMemoria;
import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteCuenta;
import com.ibm.bbva.ctacte.bean.MultiTabla;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.sfp.DatosClientePJSFP;
import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.DatosAyudaMemoriaMB;
import com.ibm.bbva.ctacte.controller.comun.DigitaliceDocumentacionMB;
import com.ibm.bbva.ctacte.controller.comun.FinaliceSolicitud1MB;
import com.ibm.bbva.ctacte.controller.comun.IdentifiquePJOperacion1MB;
import com.ibm.bbva.ctacte.controller.comun.interf.IDigitaliceDocumentacion;
import com.ibm.bbva.ctacte.controller.comun.interf.IFinaliceSolicitud1;
import com.ibm.bbva.ctacte.controller.comun.interf.IIdentifiquePJOperacion1;
import com.ibm.bbva.ctacte.dao.AyudaMemoriaDAO;
import com.ibm.bbva.ctacte.dao.CuentaDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteCuentaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.MultiTablaDAO;
import com.ibm.bbva.ctacte.util.AyudaCobroComision;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.RespuestaServicioDTO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="registrarNuevoBastanteo")
@ViewScoped
public class RegistrarNuevoBastanteoMB extends AbstractMBean 
		implements IIdentifiquePJOperacion1, IDigitaliceDocumentacion, IFinaliceSolicitud1 {

	private static final long serialVersionUID = 7386098843178571669L;
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarNuevoBastanteoMB.class);
	
	private IdentifiquePJOperacion1MB identifiquePJOperacion1;
	private DigitaliceDocumentacionMB digitaliceDocumentacion;
	private FinaliceSolicitud1MB finaliceSolicitud1;
	
	private List<DocumentoExp> listaDocs;
	private List<Cuenta> cuentas;
	private Date fechaActual = new Date ();
	@ManagedProperty (value="#{datosAyudaMemoria}")
	private DatosAyudaMemoriaMB datosAyudaMemoria;
	@EJB
	private ExpedienteBusiness business;
	@EJB
	private DocumentoExpDAO docdao;
	@EJB
	private CuentaDAO ctadao;
	@EJB
	private ExpedienteCuentaDAO expedienteCuentaDAO;
	@EJB
	private AyudaMemoriaDAO ayudaMemoriaDAO;
	@EJB
	private ExpedienteDAO expdao;
	@EJB
	private MultiTablaDAO multiTablaDAO;
	
	@PostConstruct
	public void iniciar () {
		LOG.info("*********RegistrarNuevoBastanteo-iniciar()***************");
		LOG.info("*********iniciarEnvioFTP()***************");
		
		iniciarEnvioFTP();
	}

	public void mostrarSecciones(Cuenta cuenta, Operacion operacion,
			ClientePJCore cliente) {
		LOG.info("mostrarSecciones(CuentaCore cuenta, Operacion operacion, ClientePJCore cliente)");
		LOG.info("digitaliceDocumentacion : {} -- finaliceSolicitud1 : {}", digitaliceDocumentacion,
				finaliceSolicitud1);
		if (digitaliceDocumentacion!=null && finaliceSolicitud1!=null) {
			LOG.info("cliente.getCodigoCentral() : {}", cliente.getCodigoCentral());
			digitaliceDocumentacion.setCodigoCentral(cliente.getCodigoCentral());
			LOG.info("cuenta : {}", cuenta);
			digitaliceDocumentacion.setCuenta(cuenta);
			LOG.info("operacion : {}", operacion);
			digitaliceDocumentacion.setOperacion(operacion);

			DatosClientePJSFP datosCliente = cliente.getDatosSFP();
			String tipPJ = datosCliente==null ? null : datosCliente.getTipoPJ();
			String prodCod = cuenta==null ? null : cuenta.getProductoCod();
			LOG.info("tipPJ : {}", tipPJ);
			LOG.info("prodCod : {}", prodCod);
			digitaliceDocumentacion.seleccionarTipoPJ(tipPJ, prodCod);
			LOG.info("cliente : {}", cliente);
			finaliceSolicitud1.cargarDatosCliente(cliente);
			LOG.info("operacion : {}", operacion);
			finaliceSolicitud1.setOperacion(operacion);
			digitaliceDocumentacion.setMostrar(true);
			finaliceSolicitud1.setMostrar(true);
		}
	}

	public void ocultarSecciones() {
		if (digitaliceDocumentacion != null) {
			digitaliceDocumentacion.setMostrar(false);
		}
		if (finaliceSolicitud1 != null) {
			finaliceSolicitud1.setMostrar(false);
		}
	}

	public void setIdentifiquePJOperacion1(
			IdentifiquePJOperacion1MB identifiquePJOperacion1) {
		this.identifiquePJOperacion1 = identifiquePJOperacion1;
	}

	public void setDigitaliceDocumentacion(
			DigitaliceDocumentacionMB digitaliceDocumentacion) {
		this.digitaliceDocumentacion = digitaliceDocumentacion;
		digitaliceDocumentacion.setMostrar(false);
	}

	public void setFinaliceSolicitud1(FinaliceSolicitud1MB finaliceSolicitud1) {
		this.finaliceSolicitud1 = finaliceSolicitud1;
		finaliceSolicitud1.setMostrar(false);
	}

	public void actualizarSubidaDocumentos(Character flagDocumentos) {
		LOG.info("actualizarSubidaDocumentos({})", flagDocumentos);
		if (ConstantesBusiness.SIN_DOCUMENTOS.equals(flagDocumentos)) {
			finaliceSolicitud1.setDisRegEnvExp(true);
			finaliceSolicitud1.setDisGenerarPreReg(true);
		} else if (ConstantesBusiness.FALTA_DOCUMENTOS.equals(flagDocumentos)) {
			finaliceSolicitud1.setDisRegEnvExp(true);
			finaliceSolicitud1.setDisGenerarPreReg(false);
		} else {
			finaliceSolicitud1.setDisRegEnvExp(false);
			finaliceSolicitud1.setDisGenerarPreReg(true);
		}
		/*if (faltaDocumentos) {
			finaliceSolicitud1.setDisRegEnvExp(true);
			finaliceSolicitud1.setDisGenerarPreReg(false);
		} else {
			finaliceSolicitud1.setDisRegEnvExp(false);
			finaliceSolicitud1.setDisGenerarPreReg(true);
		}*/
	}
	
	public void copiarDatos() {
		LOG.info("copiarDatos()");
		identifiquePJOperacion1.copiarDatos();
		cuentas = identifiquePJOperacion1.getCuentas();
		LOG.info("Cuentas:", cuentas.size());
		digitaliceDocumentacion.copiarDatos ();
		listaDocs = digitaliceDocumentacion.getListaDocExp();
		finaliceSolicitud1.copiarDatos ();
	}

	public boolean esValido() {
		LOG.info("esValido()");
		return identifiquePJOperacion1.esValido() & digitaliceDocumentacion.esValido()
				& finaliceSolicitud1.esValido();
	}

	// antes de ejecutar este metodo se tiene que llamar a copiarDatos
	public void guardarExpediente (int idEstadoExp, int idTarea, int idEstado, String accion) {
		LOG.info("guardarExpediente (int idEstadoExp, int idTarea, int idEstado, String accion)");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		LOG.info("idExpediente "+expediente.getId());
		business.guardarExpediente(expediente, idEstadoExp, idTarea, idEstado, accion, fechaActual);
		Character flagExisteFRF = ConstantesBusiness.FLAG_NO_EXISTE_FRF;
		Character flagExisteDOI = ConstantesBusiness.FLAG_NO_EXISTE_DOI;
		for (DocumentoExp de : listaDocs) {
			de.setExpediente(expediente);
			docdao.save(de);
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
		}
		LOG.info("cuentas: "+cuentas.size());
		for (Cuenta cuenta : cuentas) {
			Cliente cliente = expediente.getCliente();
			LOG.info("cuenta.getParticipes(): "+cuenta.getParticipes().size());
			LOG.info("cliente: "+cliente);
			for (Participe p : cuenta.getParticipes()) {
				p.setCliente(cliente);
				p.setCuenta(cuenta);
			}
			cuenta.setCliente(cliente);
			ctadao.save(cuenta);

			LOG.info("cuenta.getId(): "+cuenta.getId());
			LOG.info("expediente.getId(): "+expediente.getId());
			ExpedienteCuenta expedienteCuenta = new ExpedienteCuenta ();
			expedienteCuenta.setIdCuenta(cuenta.getId());
			expedienteCuenta.setIdExpediente(expediente.getId());
			expedienteCuentaDAO.save(expedienteCuenta);
		}
		
		if ( !(datosAyudaMemoria.getListAyudaMem()==null || datosAyudaMemoria.getListAyudaMem().size()==0)){
			
			for (AyudaMemoria ayudaMem : datosAyudaMemoria.getListAyudaMem()) {
				 ayudaMem.setExpediente(expediente);
			     ayudaMemoriaDAO.save(ayudaMem);
			}
		}
		
		if (ConstantesAdmin.ACCION_REGISTRAR_ENVIAR_EXPEDIENTE.equals(accion)) {
			Util.generarRegistroHistExp(expediente);
			
			AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
			Date fechaUltimoBastanteo = null;
			if (ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())) {
				fechaUltimoBastanteo = identifiquePJOperacion1.getUltimExpBast().getFechaFin();
			}
			if (ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())) {
				List<Expediente> lstExpedientesAntiguos = expdao
						.findByCodigoCentralSinIdExpediente(expediente
								.getCliente().getCodigoCentral(), expediente
								.getId());
				if (lstExpedientesAntiguos == null || lstExpedientesAntiguos.isEmpty()) {
					flagExisteFRF = ConstantesBusiness.FLAG_EXISTE_FRF;
					flagExisteDOI = ConstantesBusiness.FLAG_EXISTE_DOI;
				}
			}
			ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosCU01(fechaUltimoBastanteo, flagExisteFRF, flagExisteDOI);
			// Flag Envio Content
			expedienteCC.getDatosFlujoCtaCte().setFlagEnvioContent(docdao.validarFlagEnvioContent(expediente.getId()));
			LOG.info("FlagEnvioContent: " + expedienteCC.getDatosFlujoCtaCte().getFlagEnvioContent());
			// Exonerado cobro comision
			// [Begin]-[15.04.06]-[Exoneracion cobro de comision por tipo de subproducto, cuando es un nuevo bastanteo]
			if (ConstantesBusiness.CODIGO_NUEVO_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion()) && expediente.getProducto()!= null) {
				LOG.info("Codigo Producto " + expediente.getProducto().getCodigo());
				LOG.info("Codigo Sub Producto " + identifiquePJOperacion1.getCodigoSubProducto());
				MultiTabla multiTabla = null;
				try {
					multiTabla = multiTablaDAO.obtener(ConstantesBusiness.CODIGO_PADRE_PRODUCTO_EXONERADOS, expediente.getProducto().getCodigo(), identifiquePJOperacion1.getCodigoSubProducto());
					LOG.info(multiTabla.toString());
				} catch(NoResultException e) {
					LOG.info("Error multitabla", e);
				} catch(Exception e) {
					LOG.error("Error multitabla", e);
				}
				expedienteCC.getDatosFlujoCtaCte().setClienteExonerado(String.valueOf(ConstantesBusiness.FLAG_COBRO_COMISION));
				if(multiTabla != null) { 
					expedienteCC.getDatosFlujoCtaCte().setClienteExonerado(String.valueOf(ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION));
				}
			} else if (ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())) {
				if (ConstantesBusiness.CODIGO_TIPO_PJ_MIGRACION.equals(expediente.getCodTipoPj())) {
					expedienteCC.getDatosFlujoCtaCte().setClienteExonerado(String.valueOf(ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION));
				} else {
					expedienteCC.getDatosFlujoCtaCte().setClienteExonerado(String.valueOf(ConstantesBusiness.FLAG_COBRO_COMISION));
				}
			}
			// [End]-[15.04.06]-[Exoneracion cobro de comision por tipo de subproducto, cuando es un nuevo bastanteo]
			
			LOG.info("expedienteCC.getDatosFlujoCtaCte().getClienteExonerado(): {}",
					expedienteCC.getDatosFlujoCtaCte().getClienteExonerado());
			Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
			expedienteCC.getDatosFlujoCtaCte().setCodUsuarioResponsable(empleado.getCodigo()); 
			expedienteCC.getDatosFlujoCtaCte().setNomUsuarioResponsable(empleado.getNombres()+" "+
					empleado.getApepat()+" "+empleado.getApemat());	
			//String tkiid = expedienteCC.getTaskID();
			RemoteUtils bandeja = new RemoteUtils();
			//bandeja.enviarExpediente(tkiid, expedienteCC);
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
			//bandeja.completarTarea(expedienteCC.getTaskID());
		}
		subirArchivos(true);
	}

	public boolean hayIndicaciones() {
		return false;
	}

	public void habilitarBoton(boolean habReasignar) {
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public DatosAyudaMemoriaMB getDatosAyudaMemoria() {
		return datosAyudaMemoria;
	}

	public void setDatosAyudaMemoria(DatosAyudaMemoriaMB datosAyudaMemoria) {
		this.datosAyudaMemoria = datosAyudaMemoria;
	}

	@Override
	public void grabarReenviarExpediente(String accion) {
	}
	
	public void pinwWEBSEAL (ActionEvent event) { 
		LOG.info("En el metodo pinWebSeal de registrar nuevo bastanteo - BASTANTEO");
	}

}

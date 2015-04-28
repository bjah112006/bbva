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

import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosAdicionalesCargoComision;
import com.ibm.bbva.ctacte.controller.form.GestionarCobroComisionMB;
import com.ibm.bbva.ctacte.dao.CuentaDAO;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.dao.ProductoDAO;
import com.ibm.bbva.ctacte.util.AyudaCobroComision;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.RespuestaServicioDTO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="datosCargoComision")
@ViewScoped
public class DatosAdicionalesCargoComisionMB extends AbstractMBean{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DatosExpedienteMB.class);
	@ManagedProperty (value="#{gestionarCobroComision}")
	private GestionarCobroComisionMB gestionarCobroComision;
	private IDatosAdicionalesCargoComision iDatosAdicionales;	
	private Expediente expediente;
	private String tieneCorreo;
	private String tieneTelefono;
	private String exoneradoComision;
	private List<SelectItem> listaSelectMotivoC;
	private int idTareaActual;
	private int numeroTareasExpediente;
	private String numCuenta;
	private List<Cuenta> cuentas;
	private List<SelectItem> listaCuentas;
	@EJB
	private MotivoDAO motivoDAO;
	@EJB
	private EstadoExpedienteDAO estadoExpedienteDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private CuentaDAO cuentaDAO;
	@EJB
	private ProductoDAO productoDAO;

	public int getIdTareaActual() {
		return idTareaActual;
	}

	public void setIdTareaActual(int idTareaActual) {
		this.idTareaActual = idTareaActual;
	}

	public int getNumeroTareasExpediente() {
		return numeroTareasExpediente;
	}

	public void setNumeroTareasExpediente(int numeroTareasExpediente) {
		this.numeroTareasExpediente = numeroTareasExpediente;
	}

	public List<SelectItem> getListaSelectMotivoC() {
		return listaSelectMotivoC;
	}

	public void setListaSelectMotivoC(List<SelectItem> listaSelectMotivoC) {
		this.listaSelectMotivoC = listaSelectMotivoC;
	}

	public String getExoneradoComision() {
		return ConstantesAdmin.FLAG_VERDADERO.equals(expediente.getFlagExoneracionComision()) ? 
				 "SI" : "NO";
	}

	public void setExoneradoComision(String exoneradoComision) {
		this.exoneradoComision = exoneradoComision;
	}

	public String getTieneCorreo() {
		return ConstantesAdmin.FLAG_VERDADERO.equals(expediente.getFlagCorreo()) ? 
				 "SI" : "NO";
	}

	public void setTieneCorreo(String tieneCorreo) {
		this.tieneCorreo = tieneCorreo;
	}

	public String getTieneTelefono() {
		return ConstantesAdmin.FLAG_VERDADERO.equals(expediente.getFlagSmstexto()) ?
				"SI" : "NO";
	}

	public void setTieneTelefono(String tieneTelefono) {
		this.tieneTelefono = tieneTelefono;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public String getNumCuenta() {
		return numCuenta;
	}

	public void setNumCuenta(String numCuenta) {
		this.numCuenta = numCuenta;
	}

	public List<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	public List<SelectItem> getListaCuentas() {
		return listaCuentas;
	}

	public void setListaCuentas(List<SelectItem> listaCuentas) {
		this.listaCuentas = listaCuentas;
	}

	@PostConstruct
	public void iniciar () {
		LOG.info("DatosAdicionalesCargoComisionMB iniciar ()");	
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		
		if (ConstantesAdmin.FORM_GESTIONAR_COBRO_COMISION.equals(pagina)){			
			iDatosAdicionales=gestionarCobroComision;			
			iDatosAdicionales.setIDatosAdicionales(this);
		}
		expediente = (Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		expediente.setMotivo(new Motivo());
		
		//Obteniendo idTarea
		ExpedienteTarea expedienteTarea = expediente.getExpedienteTareas().iterator().next();
		idTareaActual = expedienteTarea.getTarea().getId().intValue();
		
		//Llenando el combo de cuentas activas
		numCuenta = expediente.getNumeroCuentaCobro();
		cuentas = cuentaDAO.findByClienteActivos(expediente.getCliente());
		boolean itemSelect = false;
		if (numCuenta == null) {
			itemSelect = true;
		}
		listaCuentas = Util.crearItems(cuentas, itemSelect, "numeroContrato", "monedaCod,numeroContrato,situacionCuenta", "{0} {1} {2}", true);

		//LLenando el combo de motivos de cancelacion
		LOG.info("idTareaActualidTareaActual  "+idTareaActual);
		List<Motivo> listaMotivos = motivoDAO.findByTarea(idTareaActual,ConstantesBusiness.TIPO_MOTIVO_CANCELAR_PROCESO);
		List<SelectItem> listaSelectMov = Util.crearItems(listaMotivos, true, "id", "descripcion");
		setListaSelectMotivoC(listaSelectMov);
	}

	public GestionarCobroComisionMB getGestionarCobroComision() {
		return gestionarCobroComision;
	}

	public void setGestionarCobroComision(
			GestionarCobroComisionMB gestionarCobroComision) {
		this.gestionarCobroComision = gestionarCobroComision;
	}

	public void cancelarProceso(ActionEvent action){
		LOG.info("cancelarProceso(ActionEvent action)");
		if (expediente.getMotivo().getId() == -1){
			LOG.info("ES UN MOTIVO INVALIDO");
			mensajeErrorPrincipal("dialog",  "Debe seleccionar un motivo");
//			addCallbackParam("motivoValido", false);
		}else{
			//borrar todos los documentos escaneado para la operacion en curso
//			DocumentoExpDAO docExpDAO = DAOFactory.getInstance().getDocumentoExpDAO();
//			int result = docExpDAO.delDocumentoExpediente(getExpediente().getId());
//			LOG.info("Documentos borrados*******"+result);
			
//			if (result > 0){ // Eliminó minimo 1 doc	
			
			    /*
				expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_CANCELADO);
				expediente.setAccion(ConstantesBusiness.ACCION_EXPEDIENTE_CANCELADO);				
				expediente.getEstado().setDescripcion(ConstantesBusiness.DESC_ESTADO_TAREA_CANCELADO);	
				*/
			
			    expediente.setAccion(ConstantesBusiness.ACCION_EXPEDIENTE_CANCELADO);	
			
				//EstadoExpediente estadoExpediente = estadoExpedienteDAO.load(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO);
				EstadoExpediente estadoExpediente = estadoExpedienteDAO.load(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_CANCELADO);
				expediente.setEstado(estadoExpediente);

			
				LOG.info("expediente.getEstado().getDescripcion() -->"+expediente.getEstado().getDescripcion());
				LOG.info("estadoExpediente.getDescripcion() -->"+estadoExpediente.getDescripcion());
				LOG.info("expediente.getMotivo().getId()-->"+expediente.getMotivo().getId());
				LOG.info("expediente.getEstado().getId()"+expediente.getEstado().getId());
				LOG.info("expediente.getAccion()"+expediente.getAccion());    
				
				//Actualizo el expediente en BD
				LOG.info("*********UPDATE**********");
				expediente = expedienteDAO.update(expediente);
				Util.generarRegistroHistExp(expediente);
			
				Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION,expediente);			
				addCallbackParam("terminoBorrado", true);
				addCallbackParam("codigoExp", expediente.getId());
//			}else{
//				addCallbackParam("terminoBorrado", false);
//				addCallbackParam("codigoExp", expediente.getId());
//			}
			
			addCallbackParam("motivoValido", true);
		
			AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
			LOG.info("ACCION Expediente - accion -->"+expediente.getAccion());
			ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
			expedienteCC.getOperacionesCtaCte().setFlagCobroComision(ConstantesBusiness.FLAG_INDICADOR_FALLO_COBRO_COMISION_INMEDIATO);
//			LOG.info("Despues de copiar Expediente - accion -->"+expediente.getAccion());
			String tkiid = expedienteCC.getTaskID();
			RemoteUtils bandeja = new RemoteUtils();
			//bandeja.enviarExpediente(tkiid, expedienteCC);
			bandeja.completarTarea(tkiid, expedienteCC);
			
			redirectAction("../bandeja/bandeja");	
		}
	}
	
	public void cerrarPopup(){
		LOG.info("aceptarMotivoCancelacion()");
		mensajeErrorPrincipal("dialog",  "");
	}
	
	public String reintentarCobroComision() {
		expediente.setMotivo(null);// para evitar la excepción por CASCADE
		
		if (ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(numCuenta)) {
			mensajeErrorPrincipal("idGeneralesCobroC:numCuentaCobro", "Seleccione una opción");
			expediente.setMotivo(new Motivo());
			return null;
		}
		if (!numCuenta.equals(expediente.getNumeroCuentaCobro())) {
			Cuenta cuenta = obtenerCuentaSeleccionada();
			if (cuenta != null) {
				LOG.info("Nuevo número de cuenta: "+numCuenta);
				expediente.setSubproducto(cuenta.getSubproductoDes());
				expediente.setProducto(productoDAO.findProductoByCodigo(cuenta.getProductoCod()));
				expediente.setEstadoCuenta(cuenta.getSituacionCuenta());
				expediente.setNumeroCuentaCobro(cuenta.getNumeroContrato());
				expediente.setCuentaCobroComision(cuenta.getProductoDes());
				
				expediente = expedienteDAO.update(expediente);
				Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);
			} else {
				mensajeErrorPrincipal("idGeneralesCobroC:numCuentaCobro", "Seleccione una opción");
				expediente.setMotivo(new Motivo());
				return null;
			}
		}
		
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		
		RespuestaServicioDTO rpta = AyudaCobroComision.cobroComisionInmediato(String.valueOf(expediente.getId()), expedienteCC.getDatosFlujoCtaCte().getCodUsuarioResponsable());
		if (rpta.getCodigo().equals("0")) {
			Util.generarRegistroHistExp(expediente);
			expedienteCC.getOperacionesCtaCte().setFlagCobroComision(ConstantesBusiness.FLAG_INDICADOR_EJECUTO_COBRO_COMISION_INMEDIATO);
			String tkiid = expedienteCC.getTaskID();
			RemoteUtils bandeja = new RemoteUtils();
			bandeja.completarTarea(tkiid, expedienteCC);
			subirArchivos(false);
			Util.addObjectSession(ConstantesAdmin.RESPUESTA_SERVICIO_SESION, rpta);
			
			return redirectMensaje("Se ha registrado correctamente el expediente "+expediente.getId()+".");
		} else {
			mensajeErrorPrincipal("idAdicionalesCobroC:btnReintentarCobro", "El cobro de comisión de bastanteo falló.");
			expediente.setMotivo(new Motivo());
			return null;
		}
		
	}
	
	private Cuenta obtenerCuentaSeleccionada() {
		LOG.info("obtenerCuentaSeleccionada ()");
		if (cuentas!=null && !cuentas.isEmpty()) {
			for (Cuenta cc : cuentas) {
				if (cc.getNumeroContrato().equals(numCuenta)) {
					return cc;
				}
			}
		}
		return null;
	}
	
}

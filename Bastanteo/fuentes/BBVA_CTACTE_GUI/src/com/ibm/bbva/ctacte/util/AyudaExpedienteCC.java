package com.ibm.bbva.ctacte.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.DatosFlujoCC;
import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bean.OperacionesCC;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;

public class AyudaExpedienteCC {

	private static final Logger LOG = LoggerFactory.getLogger(AyudaExpedienteCC.class);
	
	//
	public ExpedienteCC copiarDatosGenerico () {
		return copiarDatos();
	}
	
	//CU:11 Verificar Calidad Documentos
	public ExpedienteCC copiarDatosCU11 () {
		return copiarDatos();
	}
	
	//CU:13 Revisar y aprobar bastanteo
	public ExpedienteCC copiarDatosGenericoCU13 (String codAbogado, String nombreAbogado) {
		
		ExpedienteCC expedienteCC = copiarDatos();
		//Evaluando si el Codigo de Usuario del Abogado viene de la pantalla 
		expedienteCC.setCodUsuarioActual(codAbogado);  					
		expedienteCC.setNomUsuarioActual(nombreAbogado);    			

		return expedienteCC;
	}
	
	//CU:01 Registrar Nuevo Bastanteo
	//CU:05 Registrar Modificatoria
	//CU:07 Registrar Subsanacion Bastanteo
	public ExpedienteCC copiarDatosCU01 (Date fechaUltimoBastanteo, Character flagExisteFRF, 
			Character flagExisteDOI ) {

		ExpedienteCC expedienteCC = copiarDatos();
		
		expedienteCC.setFechaUltimoBastanteo(fechaUltimoBastanteo);
		DatosFlujoCC datosFlujoCC = expedienteCC.getDatosFlujoCtaCte();
		datosFlujoCC.setExisteFileFRF(String.valueOf(flagExisteFRF));											 // 1: si existe File FRF
		datosFlujoCC.setExisteFileDOI(String.valueOf(flagExisteDOI));
		expedienteCC.setDatosFlujoCtaCte(datosFlujoCC);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("**********************  DATOS PROCEOS INICIO **************************");
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("fechaUltimoBastanteo-->"+fechaUltimoBastanteo);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("flagExisteFRF-->"+flagExisteFRF);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("flagExisteDOI-->"+flagExisteDOI);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("**********************  DATOS PROCEOS FIN **************************");
		
		return expedienteCC;
	}
	
	//CU:27,29
	public ExpedienteCC copiarDatosGenericoCU2729 (String flagRevocatoriaAprobada) {
		
		ExpedienteCC expedienteCC = copiarDatos();
		expedienteCC.getDatosFlujoCtaCte().setFlagRevocatoriaAprobada(flagRevocatoriaAprobada);

		return expedienteCC;
	}
	
	//CU:15 Verificar Firma
	public ExpedienteCC copiarDatosCU15 (ExpedienteTarea expedienteTareaActual,  
			  Character flagExisteFirmaAsociada, Character flagExisteFirmaNoAsociada) {
		
		ExpedienteCC expedienteCC = copiarDatos();

		DatosFlujoCC datosFlujoCC = expedienteCC.getDatosFlujoCtaCte();
		datosFlujoCC.setFlagExisteFirmaAsociada(String.valueOf(flagExisteFirmaAsociada));
		datosFlujoCC.setFlagExisteFirmaNoAsociada(String.valueOf(flagExisteFirmaNoAsociada));
	
		expedienteCC.setDatosFlujoCtaCte(datosFlujoCC);
		
		return expedienteCC;
	}	
	
	private ExpedienteCC copiarDatos () {	
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("copiarDatos ()");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		LOG.info("EXPEDIENTE ID:"+expediente.getId());
		LOG.info("EXPEDIENTE OPERACION ID:"+expediente.getOperacion().getId());
		LOG.info("DESCRIPCION ESTADO EXPEDIENTE: "+expediente.getEstado().getDescripcion());
		
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Estado del expediente.."+expediente.getEstado().getId());
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Accion.."+expediente.getAccion());
		
		ExpedienteTarea expedienteTareaActual = expediente.getExpedienteTareas().iterator().next();
		
		ExpedienteCC expedienteCC = (ExpedienteCC)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION);
		
		expedienteCC.setIdOperacion(String.valueOf(expediente.getOperacion().getId()));
		expedienteCC.setCodigoExpediente(String.valueOf(expediente.getId()));
		expedienteCC.setEstadoExpediente(String.valueOf(expediente.getEstado().getDescripcion()));
		expedienteCC.setIdEstadoExpediente(String.valueOf(expediente.getEstado().getId())); //**
		expedienteCC.setNumeroTarea(String.valueOf(expedienteTareaActual.getTarea().getId()));
		expedienteCC.setNombreTarea(expedienteTareaActual.getTarea().getDescripcion());
		expedienteCC.setEstadoTarea(String.valueOf(expedienteTareaActual.getEstadoTarea().getDescripcion()));  //**
		expedienteCC.setIdEstadoTarea(String.valueOf(expedienteTareaActual.getEstadoTarea().getId()));  //**
//		expedienteCC.setCodUsuarioActual(String.valueOf(expediente.getEmpleado().getId()));  						// se llena en el BPM - GestionOperacionesCtaCte
//		expedienteCC.setNomUsuarioActual(expediente.getEmpleado().getNombresCompletos());    						// no se usa en el BPM **
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("getEmpleado" + expediente.getId());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("getEmpleado" + expediente.getEmpleado().getCodigo());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("getOficina" + expediente.getOficina().getCodigo());
		expedienteCC.setCodOficina(String.valueOf(expediente.getEmpleado().getOficina().getId()));  //***
		expedienteCC.setDesOficina(expediente.getEmpleado().getOficina().getDescripcion());
		expedienteCC.setDesTerritorio(expediente.getEmpleado().getOficina().getTerritorio().getDescripcion());
		expedienteCC.setCodOperacion(expediente.getOperacion().getCodigoOperacion());
		expedienteCC.setDesOperacion(expediente.getOperacion().getDescripcion());
		expedienteCC.setCodCentralCliente(expediente.getCliente().getCodigoCentral());
		expedienteCC.setNumDOICliente(expediente.getCliente().getNumeroDoi());
		expedienteCC.setRazonSocialCliente(expediente.getCliente().getRazonSocial());
		expedienteCC.setFechaRegistro(expediente.getFechaRegistro());
//		expedienteCC.setFechaAtencion(new Date());
//		expedienteCC.setMinAmarillo(expedienteTareaActual.getTarea().getAmarilloMinutos());
//		expedienteCC.setMinVerde(expedienteTareaActual.getTarea().getVerdeMinutos());		
		
		DatosFlujoCC datosFlujoCC = expedienteCC.getDatosFlujoCtaCte();
		if (datosFlujoCC == null) {
			datosFlujoCC = new DatosFlujoCC();
			expedienteCC.setDatosFlujoCtaCte(datosFlujoCC);
		}

		datosFlujoCC.setAccion(expediente.getAccion());    	
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Ayuda Expediente datosFlujoCC.getAccion().." + datosFlujoCC.getAccion());
		LOG.info("expediente.getFlagEjecutoCobroComision() : {}", expediente.getFlagEjecutoCobroComision());
		LOG.info("expediente.getFlagExoneracionComision() : {}", expediente.getFlagExoneracionComision());
		if (ConstantesBusiness.FLAG_EJECUTO_COBRO_COMISION.equals(expediente.getFlagEjecutoCobroComision())
				|| ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION.equals(expediente.getFlagExoneracionComision())) {
			//datosFlujoCC.setClienteExonerado(String.valueOf(expediente.getFlagExoneracionComision()));
			datosFlujoCC.setClienteExonerado(String.valueOf(ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION));
		} else {
			datosFlujoCC.setClienteExonerado(String.valueOf(ConstantesBusiness.FLAG_COBRO_COMISION));
		}
		//datosFlujoCC.setClienteExonerado("0");
		datosFlujoCC.setFlagExisteFacultadesEspeciales(String.valueOf(expediente.getFlagFacultadesEspeciales()));
		datosFlujoCC.setFlagExisteModificatoria(String.valueOf(expediente.getFlagModificatoria()));
//		datosFlujoCC.setFlagExisteFirmaPorActivar();  															// se llena en el BPM - GestionOperaciones								
//		datosFlujoCC.setCodUsuarioAbogado(expediente)       ?????
//		datosFlujoCC.setNomUsuarioAbogado(expediente)       ?????
//		datosFlujoCC.setIdTarea(expediente)                                                                     // se llena en BPM
//		datosFlujoCC.setCodUsuarioResponsable()             							                      // el user q inicia el flujo. Deberia consultarse solo la primera vez 
//		datosFlujoCC.setNomUsuarioResponsable(exp)															  // no se usa en el BPM **
		datosFlujoCC.setIdTerritorio(String.valueOf(expediente.getOficina().getTerritorio().getId()));
		if (expediente.getProducto()!=null) {
			datosFlujoCC.setIdProducto(String.valueOf(expediente.getProducto().getId()));
		}
		datosFlujoCC.setResultadoBastanteo(expediente.getResultadoBastanteo());
		
		OperacionesCC operacionesCC = expedienteCC.getOperacionesCtaCte();
		if (operacionesCC == null) {
			operacionesCC = new OperacionesCC();
			expedienteCC.setOperacionesCtaCte(operacionesCC);
		}
//		operacionesCC.setTipoFlujoOperacion()                                                            // se llena en el BPM
//		operacionesCC.setFlagCriterioAuditoriaInterna()                  							     // se llena en el BPM - GestionOperaciones
//		operacionesCC.setFlagEstadoBastanteo(flagEstadoBastanteo)										 // se deberia llenar en el BPM
//		operacionesCC.setFlagPlazoSubsanacion()															 // se llena en el BPM - GestionOperaciones
//		operacionesCC.setFlagCobroComision(flagCobroComision);											// se deberia llenar en el flujo 
		
		
	
		return expedienteCC;
	}
	
	
	
	
//	private ExpedienteCC BcopiarDatos (ExpedienteTarea expedienteTareaActual,Date fechaUltimoBastanteo, 
//			  Character flagExisteFRF, Character flagExisteFirmaAsociada,
//			  Character flagExisteFirmaNoAsociada,Character flagRevocatoriaAprobada
//			   ) {
//		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
//		
//		return null;
//	}
//	
//	private ExpedienteCC copiarDatos (ExpedienteTarea expedienteTareaActual, Date fechaUltimoBastanteo, 
//			  Character flagExisteFRF, Character flagExisteDOI,
//			  Character flagExisteFirmaAsociada, Character flagExisteFirmaNoAsociada,
//			  Character flagRevocatoriaAprobada) {
//		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
//		
//		ExpedienteCC expedienteCC = (ExpedienteCC)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION);
//		
//		expedienteCC.setIdOperacion(String.valueOf(expediente.getOperacion().getId()));
//		expedienteCC.setCodigoExpediente(String.valueOf(expediente.getId()));
//		expedienteCC.setEstadoExpediente(expediente.getEstado().getDescripcion());
//		expedienteCC.setIdEstadoExpediente(String.valueOf(expediente.getEstado().getId())); //**
//		expedienteCC.setNumeroTarea(String.valueOf(expedienteTareaActual.getTarea().getId()));
//		expedienteCC.setNombreTarea(expedienteTareaActual.getTarea().getDescripcion());
//		expedienteCC.setEstadoTarea(expedienteTareaActual.getEstadoTarea().getDescripcion());
//		expedienteCC.setIdEstadoTarea(String.valueOf(expedienteTareaActual.getEstadoTarea().getId()));  //**
//		//expedienteCC.setCodUsuarioActual(String.valueOf(expediente.getEmpleado().getId()));  						// se llena en el BPM - GestionOperacionesCtaCte
//		//expedienteCC.setNomUsuarioActual(expediente.getEmpleado().getNombresCompletos());    						// no se usa en el BPM **
//		expedienteCC.setCodOficina(expediente.getEmpleado().getOficina().getCodigo());
//		expedienteCC.setDesOficina(expediente.getEmpleado().getOficina().getDescripcion());
//		expedienteCC.setDesTerritorio(expediente.getEmpleado().getOficina().getTerritorio().getDescripcion());
//		expedienteCC.setCodOperacion(expediente.getOperacion().getCodigoOperacion());
//		expedienteCC.setDesOperacion(expediente.getOperacion().getDescripcion());
//		expedienteCC.setCodCentralCliente(expediente.getCliente().getCodigoCentral());
//		expedienteCC.setNumDOICliente(expediente.getCliente().getNumeroDoi());
//		expedienteCC.setRazonSocialCliente(expediente.getCliente().getRazonSocial());
////		expedienteCC.setFechaAtencion(new Date());
//		expedienteCC.setFechaRegistro(expediente.getFechaRegistro());
//		expedienteCC.setFechaUltimoBastanteo(fechaUltimoBastanteo);
//		
//		DatosFlujoCC datosFlujoCC = new DatosFlujoCC();
//		datosFlujoCC.setAccion(expediente.getAccion());    														 
//		datosFlujoCC.setClienteExonerado(String.valueOf(expediente.getFlagExoneracionComision()));				 // 1: si esta exonerado
//		datosFlujoCC.setExisteFileFRF(String.valueOf(flagExisteFRF));											 // 1: si existe File FRF
//		datosFlujoCC.setExisteFileDOI(String.valueOf(flagExisteDOI));                        										     // ya no va
//		datosFlujoCC.setFlagExisteFirmaAsociada(String.valueOf(flagExisteFirmaAsociada));
//		datosFlujoCC.setFlagExisteFirmaNoAsociada(String.valueOf(flagExisteFirmaNoAsociada));
//		datosFlujoCC.setFlagExisteFacultadesEspeciales(String.valueOf(expediente.getFlagFacultadesEspeciales()));
//		datosFlujoCC.setFlagExisteModificatoria(String.valueOf(expediente.getFlagModificatoria()));
//		datosFlujoCC.setFlagRevocatoriaAprobada(String.valueOf(flagRevocatoriaAprobada));				
//		//datosFlujoCC.setFlagExisteFirmaPorActivar();  															// se llena en el BPM - GestionOperaciones								
//		//datosFlujoCC.setCodUsuarioAbogado(expediente)       ?????
//		//datosFlujoCC.setNomUsuarioAbogado(expediente)       ?????
//		//datosFlujoCC.setIdTarea(expediente)                                                                     // se llena en BPM
//		//datosFlujoCC.setCodUsuarioResponsable()             							                      // el user q inicia el flujo. Deberia consultarse solo la primera vez 
//		//datosFlujoCC.setNomUsuarioResponsable(exp)															  // no se usa en el BPM **
//		datosFlujoCC.setIdTerritorio(String.valueOf(expediente.getOficina().getTerritorio().getId()));//.getDescripcion());
//		datosFlujoCC.setIdProducto(expediente.getProducto().getDescripcion());
//		datosFlujoCC.setResultadoBastanteo(expediente.getResultadoBastanteo());
//		expedienteCC.setDatosFlujoCtaCte(datosFlujoCC);
//		
//		OperacionesCC operacionesCC = new OperacionesCC();
//		//operacionesCC.setTipoFlujoOperacion()                                                            // se llena en el BPM
//		//operacionesCC.setFlagCriterioAuditoriaInterna()                  							     // se llena en el BPM - GestionOperaciones
//		//operacionesCC.setFlagEstadoBastanteo(flagEstadoBastanteo)										 // se deberia llenar en el BPM
//		//operacionesCC.setFlagPlazoSubsanacion()															 // se llena en el BPM - GestionOperaciones
//		//operacionesCC.setFlagCobroComision(flagCobroComision);											// se deberia llenar en el flujo 
//		
//		expedienteCC.setOperacionesCtaCte(operacionesCC);
//		
//		return expedienteCC;
//	}


	/**
	 * Actualiza la lista con elementos de tipo ExpedienteTC y lo almacena en sesion
	 * para que luego sea utilizada en la bandeja de pendientes
	 * @param consulta
	 */
//	public void actualizarListaExpedienteTC(Consulta consulta) {
//		BandejaTareasBDelegate tareasBDelegate = new BandejaTareasBDelegate();
//		List<ExpedienteTC> lista = tareasBDelegate.obtenerInstanciasTareasPorUsuario(consulta);
//		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
//				Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
//	}
	

}
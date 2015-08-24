package com.ibm.bbva.tabla.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.DatosAyudaMemoriaIiceDTO;
import com.ibm.bbva.tabla.dto.DatosDetalleHistoricoIiceDTO;
import com.ibm.bbva.tabla.dto.DatosDetalleLogIiceDTO;
import com.ibm.bbva.tabla.dto.DatosDetalleObservacionesIiceDTO;
import com.ibm.bbva.tabla.dto.DatosHistAntiguoDTO;
import com.ibm.bbva.tabla.dto.DatosDocumentosExpIiceDTO;

public class DatosHistAntiguoDAO extends BaseDAO{
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosHistAntiguoDAO.class);

	private static DatosHistAntiguoDAO instance = null;
	
	private DatosHistAntiguoDAO() {
	}
	
	public static DatosHistAntiguoDAO getInstance() {
		if ( instance==null )
			 instance = new DatosHistAntiguoDAO();
		return instance;
	}
	
	public List<DatosHistAntiguoDTO> generarDatosHistAntiguo(ArrayList pParameters) throws DataAccessException {		
		List<DatosHistAntiguoDTO> listDatosGeneradosHisAntDTO=null;
		DatosHistAntiguoDTO objDatosGeneradosHisAntDTO=null;
		
		LOG.info("1.- this.getEntityName(): " + this.getEntityName());
		CallableStatement stmt=getSQLStoredProcedureHistAntiguo(this.getEntityName(),pParameters);
		LOG.info("2.- despues de CallableStatement");
		try{
			LOG.info("3.- try");
			stmt.execute();			
			LOG.info("4.- Execute");
			ResultSet results = (ResultSet) stmt.getObject(1);
	        LOG.info("5.- results");
	        listDatosGeneradosHisAntDTO=new ArrayList<DatosHistAntiguoDTO>();
	        LOG.info("antes del while");
	        while (results.next()) {
	        	
	        	objDatosGeneradosHisAntDTO=new DatosHistAntiguoDTO();
	            try{
	            	objDatosGeneradosHisAntDTO.setOrigen(results.getString("ORIGEN"));	            	
	                objDatosGeneradosHisAntDTO.setNroExpediente(results.getString("HIST_ID_EXPEDIENTE_FK"));
	                objDatosGeneradosHisAntDTO.setId(results.getString("HIST_ID"));
	            	objDatosGeneradosHisAntDTO.setFechaRegistroExp(results.getString("EXP_FEC_REGISTRO"));
	            	LOG.info("results.getString(EXP_FEC_REGISTRO)  "+results.getString("EXP_FEC_REGISTRO"));
	            	objDatosGeneradosHisAntDTO.setFlagActivoExp(results.getString("EXP_FLAG_ACTIVO"));
	            	objDatosGeneradosHisAntDTO.setFechaEnvioExp(results.getString("EXP_FECHA_ENVIO"));
	            	objDatosGeneradosHisAntDTO.setFechaFinExp(results.getString("EXP_FECHA_FIN"));
	            	objDatosGeneradosHisAntDTO.setFechaProgramadaExp(results.getString("EXP_FECHA_PROGRAMADA"));
	            	objDatosGeneradosHisAntDTO.setNumeroTerminalExp(results.getString("EXP_NUM_TERMINAL"));
	            	objDatosGeneradosHisAntDTO.setCodClienteNatural(results.getString("HIST_ID_CLIENTE_NATURAL_FK"));
	            	objDatosGeneradosHisAntDTO.setCodEmpleado(results.getString("HIST_ID_EMPLEADO_FK"));
	            	objDatosGeneradosHisAntDTO.setCodEstado(results.getString("HIST_ID_ESTADO_FK"));
	            	objDatosGeneradosHisAntDTO.setCodProducto(results.getString("HIST_ID_PRODUCTO_FK"));
	            	
	            	objDatosGeneradosHisAntDTO.setApellidoPatCliente(results.getString("CLI_APE_PAT"));
	            	objDatosGeneradosHisAntDTO.setApellidoMatCliente(results.getString("CLI_APE_MAT"));
	            	objDatosGeneradosHisAntDTO.setNombreCliente(results.getString("CLI_NOMBRE"));
	               	objDatosGeneradosHisAntDTO.setAvalCliente(results.getString("CLI_AVAL"));
	            	objDatosGeneradosHisAntDTO.setCelularCliente(results.getString("CLI_CELULAR"));
	            	objDatosGeneradosHisAntDTO.setCorreoCliente(results.getString("CLI_CORREO"));
	            	objDatosGeneradosHisAntDTO.setSubrogCliente(results.getString("CLI_SUBROG"));
	            	objDatosGeneradosHisAntDTO.setFechaVenDoiCliente(results.getString("CLI_FEC_VEN_DOI"));
	            	objDatosGeneradosHisAntDTO.setIngNetoMensualCliente(results.getString("CLI_ING_NETO_MENSUAL"));
	            	objDatosGeneradosHisAntDTO.setNumDoiCliente(results.getString("CLI_NUM_DOI"));
	            	objDatosGeneradosHisAntDTO.setPagoHabCliente(results.getString("CLI_PAGO_HAB"));
	            	objDatosGeneradosHisAntDTO.setPerExpPubCliente(results.getString("CLI_PER_EXP_PUB"));
	            	objDatosGeneradosHisAntDTO.setCodEstCivilCliente(results.getString("CLI_ID_ESTADO_CIVIL_FK"));
	            	objDatosGeneradosHisAntDTO.setCodPersonaCliente(results.getString("CLI_ID_PERSONA_FK"));
	            	objDatosGeneradosHisAntDTO.setCodSegmentoCliente(results.getString("CLI_ID_SEGMENTO_FK"));
	            	objDatosGeneradosHisAntDTO.setCodTipoCliente(results.getString("CLI_ID_TIPO_CLIENTE_FK"));
	            	objDatosGeneradosHisAntDTO.setCodTipoDoiCliente(results.getString("CLI_ID_TIPO_DOI_FK"));
	            	objDatosGeneradosHisAntDTO.setCodCategRentaCliente(results.getString("CLICAT_ID_CATEGORIA_RENTA_FK"));
	            	objDatosGeneradosHisAntDTO.setEstadoCivilCliente(results.getString("ESTCIV_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setPersCodProductoCliente(results.getString("PERS_ID_PRODUCTO_FK"));
	            	objDatosGeneradosHisAntDTO.setPersDescripCliente(results.getString("PERS_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setCodGrupoSegmentoCliente(results.getString("SEGM_ID_GRUPO_SEGMENTO_FK"));
	            	objDatosGeneradosHisAntDTO.setSegmentoCliente(results.getString("SEGM_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setTipoClienteDescrip(results.getString("TIPCLI_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setTipoDoiClienteDescrip(results.getString("TIPDOI_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setCodigoSegmentoCliente(results.getString("SEGM_CODIGO_SEGMENTO"));
	            	objDatosGeneradosHisAntDTO.setCatRenDescripCliente(results.getString("CATREN_DESCRIPCION"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodConyugue(results.getString("HIST_ID_CLI_NAT_CONYUGE_FK"));
	            	objDatosGeneradosHisAntDTO.setApellidoPatConyugue(results.getString("CLICON_APE_PAT"));
	            	objDatosGeneradosHisAntDTO.setApellidoMatConyugue(results.getString("CLICON_APE_MAT"));
	            	objDatosGeneradosHisAntDTO.setNombreConyugue(results.getString("CLICON_NOMBRE"));
	            	objDatosGeneradosHisAntDTO.setAvalConyugue(results.getString("CLICON_AVAL"));
	            	objDatosGeneradosHisAntDTO.setCelularConyugue(results.getString("CLICON_CELULAR"));
	            	objDatosGeneradosHisAntDTO.setCorreoConyugue(results.getString("CLICON_CORREO"));
	            	objDatosGeneradosHisAntDTO.setSubrogConyugue(results.getString("CLICON_SUBROG"));
	            	objDatosGeneradosHisAntDTO.setCodConyugue(results.getString("CLICON_COD_CLIENTE"));
	            	objDatosGeneradosHisAntDTO.setFechaVenDoiConyugue(results.getString("CLICON_FEC_VEN_DOI"));
	            	objDatosGeneradosHisAntDTO.setIngNetoMensualConyugue(results.getString("CLICON_ING_NETO_MENSUAL"));
	            	objDatosGeneradosHisAntDTO.setNumDoiConyugue(results.getString("CLICON_NUM_DOI"));
	            	objDatosGeneradosHisAntDTO.setPagoHabConyugue(results.getString("CLICON_PAGO_HAB"));
	            	objDatosGeneradosHisAntDTO.setPerExpPubConyugue(results.getString("CLICON_PER_EXP_PUB"));
	            	objDatosGeneradosHisAntDTO.setCodEstCivilConyugue(results.getString("CLICON_ID_ESTADO_CIVIL_FK"));
	            	objDatosGeneradosHisAntDTO.setCodPersonaConyugue(results.getString("CLICON_ID_PERSONA_FK"));
	            	objDatosGeneradosHisAntDTO.setCodSegmentoConyugue(results.getString("CLICON_ID_SEGMENTO_FK"));
	            	objDatosGeneradosHisAntDTO.setCodTipoConyugue(results.getString("CLICON_ID_TIPO_CLIENTE_FK"));
	            	objDatosGeneradosHisAntDTO.setCodTipoDoiConyugue(results.getString("CLICON_ID_TIPO_DOI_FK"));
	            	objDatosGeneradosHisAntDTO.setCodCategRentaConyugue(results.getString("CLICATCON_ID_CAT_RENTA_FK"));
	            	objDatosGeneradosHisAntDTO.setEstadoCivilConyugue(results.getString("ESTCIVCON_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setPersDescripConyugue(results.getString("PERSCON_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setPersCodProductoConyugue(results.getString("PERSCON_ID_PRODUCTO_FK"));
	            	objDatosGeneradosHisAntDTO.setCodGrupoSegmentoConyugue(results.getString("SEGMCON_ID_GRUPO_SEGMENTO_FK"));
	            	objDatosGeneradosHisAntDTO.setSegmentoConyugue(results.getString("SEGMCON_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setTipoConyugueDescrip(results.getString("TIPCLICON_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setTipoDoiConyugueDescrip(results.getString("TIPDOICON_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setCodigoSegmentoConyugue(results.getString("SEGMCON_CODIGO_SEGMENTO"));
	            	objDatosGeneradosHisAntDTO.setCatRenDescripConyugue(results.getString("CATRENCON_DESCRIPCION"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodigoEmpleado(results.getString("EMP_CODIGO"));
	            	objDatosGeneradosHisAntDTO.setFlagActivoEmpleado(results.getString("EMP_FLAG_ACTIVO"));
	            	objDatosGeneradosHisAntDTO.setNombresCompletosEmpleado(results.getString("EMP_NOMBRES_COMPLETOS"));
	            	objDatosGeneradosHisAntDTO.setCodNivelEmpleado(results.getString("EMP_ID_NIVEL_FK"));
	            	objDatosGeneradosHisAntDTO.setCodOficinaEmpleado(results.getString("EMP_ID_OFICINA_FK"));
	            	objDatosGeneradosHisAntDTO.setCodPerfilEmleado(results.getString("EMP_ID_PERFIL_FK"));
	            	objDatosGeneradosHisAntDTO.setPerfilEmpleado(results.getString("EMPPERF_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setCodTipoCategEmpleado(results.getString("EMP_ID_TIPO_CATEGORIA_FK"));
	             	objDatosGeneradosHisAntDTO.setNivEmpDescripEmpleado(results.getString("NIVEMP_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setOficinaEmpleado(results.getString("OFIEMP_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setFlagAreaRiesgoOfiEmpleado(results.getString("OFIEMP_FLAG_AREA_RIESGO"));
	            	objDatosGeneradosHisAntDTO.setFlagDesplezadaOfiEmpleado(results.getString("OFIEMP_FLAG_DESPLAZADA"));
	            	objDatosGeneradosHisAntDTO.setFlagEscaneoWebOfiEmpleado(results.getString("OFIEMP_FLAG_ESCANEO_WEB"));
	            	objDatosGeneradosHisAntDTO.setMontoTopeOfiEmpleado(results.getString("OFIEMP_MONTO_TOPE"));
	            	objDatosGeneradosHisAntDTO.setTasaTransOfiEmpleado(results.getString("OFIEMP_TASA_TRANSF"));
	            	objDatosGeneradosHisAntDTO.setFlagActivoOfiEmpleado(results.getString("OFIEMP_FLAG_ACTIVO"));
	            	objDatosGeneradosHisAntDTO.setCodOfiPrincipalEmpleado(results.getString("OFIEMP_ID_OFICINA_PRINCIPAL_FK"));
	            	objDatosGeneradosHisAntDTO.setCodTerritorioOfiEmpleado(results.getString("OFIEMP_ID_TERRITORIO_FK"));
	            	objDatosGeneradosHisAntDTO.setTipoCategEmpleado(results.getString("TIPCATEMP_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setFlagSuperiorTipoCategEmpleado(results.getString("TIPCATEMP_FLAG_SUPERIOR"));
	            	objDatosGeneradosHisAntDTO.setCorreoEmpleado(results.getString("EMP_CORREO"));
	            	objDatosGeneradosHisAntDTO.setApePatEmpleado(results.getString("EMP_APEPAT"));
	            	objDatosGeneradosHisAntDTO.setApeMatEmpleado(results.getString("EMP_APEMAT"));
	            	objDatosGeneradosHisAntDTO.setNombresEmpleado(results.getString("EMP_NOMBRES"));
	            	objDatosGeneradosHisAntDTO.setDescripTerOfiEmpleado(results.getString("OFIEMPTER_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setUbicaTerOfiEmpleado(results.getString("OFIEMPTER_UBICACION"));
	            	
	            	objDatosGeneradosHisAntDTO.setFlagActivoEmpleadoResp(results.getString("EMPRESP_FLAG_ACTIVO"));
	            	objDatosGeneradosHisAntDTO.setNombresCompletosEmpleadoResp(results.getString("EMPRESP_NOMBRES_COMPLETOS"));
	            	objDatosGeneradosHisAntDTO.setCodNivelResp(results.getString("EMPRESP_ID_NIVEL_FK"));
	            	objDatosGeneradosHisAntDTO.setCodOficinaEmpleadoResp(results.getString("EMPRESP_ID_OFICINA_FK"));
	            	objDatosGeneradosHisAntDTO.setCodPerfilEmleadoResp(results.getString("EMPRESP_ID_PERFIL_FK"));
	            	objDatosGeneradosHisAntDTO.setPerfilEmpleadoResp(results.getString("EMPRESPPERF_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setCodTipoCategEmpleadoResp(results.getString("EMPRESP_ID_TIPO_CATEGORIA_FK"));
	             	objDatosGeneradosHisAntDTO.setNivEmpDescripEmpleadoResp(results.getString("NIVEMPRESP_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setOficinaEmpleadoResp(results.getString("OFIEMPRESP_CODIGO"));
	            	objDatosGeneradosHisAntDTO.setFlagAreaRiesgoOfiEmpleadoResp(results.getString("OFIEMPRESP_FLAG_AREA_RIESGO"));
	            	objDatosGeneradosHisAntDTO.setFlagDesplezadaOfiEmpleadoResp(results.getString("OFIEMPRESP_FLAG_DESPLAZADA"));
	            	objDatosGeneradosHisAntDTO.setFlagEscaneoWebOfiEmpleadoResp(results.getString("OFIEMPRESP_FLAG_ESCANEO_WEB"));
	            	objDatosGeneradosHisAntDTO.setMontoTopeOfiEmpleadoResp(results.getString("OFIEMPRESP_MONTO_TOPE"));
	            	objDatosGeneradosHisAntDTO.setTasaTransOfiEmpleadoResp(results.getString("OFIEMPRESP_TASA_TRANSF"));
	            	objDatosGeneradosHisAntDTO.setFlagActivoOfiEmpleadoResp(results.getString("OFIEMPRESP_FLAG_ACTIVO"));
	            	objDatosGeneradosHisAntDTO.setCodOfiPrincipalEmpleadoResp(results.getString("OFIEMPRESP_ID_OFI_PRINCIPAL_FK"));
	            	objDatosGeneradosHisAntDTO.setCodTerritorioOfiEmpleadoResp(results.getString("OFIEMPRESP_ID_TERRITORIO_FK"));
	            	objDatosGeneradosHisAntDTO.setTipoCategEmpleadoResp(results.getString("TIPCATEMPRESP_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setFlagSuperiorTipoCategEmpleadoResp(results.getString("TIPCATEMPRESP_FLAG_SUPERIOR"));
	            	objDatosGeneradosHisAntDTO.setCorreoEmpleadoResp(results.getString("EMP_CORREO"));
	            	objDatosGeneradosHisAntDTO.setApePatEmpleadoResp(results.getString("EMP_APEPAT"));
	            	objDatosGeneradosHisAntDTO.setDescripTerOfiEmpleadoResp(results.getString("OFIEMPRESPTER_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setUbicaTerOfiEmpleadoResp(results.getString("OFIEMPRESPTER_UBICACION"));
	           		
	            	objDatosGeneradosHisAntDTO.setEstadoCodigo(results.getString("EST_CODIGO"));
	            	objDatosGeneradosHisAntDTO.setEstadoDescrip(results.getString("EST_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setCodTareaEstado(results.getString("EST_ID_TAREA_FK"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodEstadoTipresol(results.getString("HIST_ID_ESTADOTIPRESOL_FK"));
	            	objDatosGeneradosHisAntDTO.setCodEstadoSol(results.getString("ESTSOL_CODIGO"));
	            	objDatosGeneradosHisAntDTO.setDescripEstadoSol(results.getString("ESTSOL_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setCodTareaEstadoSol(results.getString("ESTSOL_ID_TAREA_FK"));
	           	
	            	objDatosGeneradosHisAntDTO.setCodEstadoEnWorckflow(results.getString("HIST_ID_ESTADOENVWORKFLOWTC_FK"));
	            	objDatosGeneradosHisAntDTO.setCodEstadoWkf(results.getString("ESTWTC_CODIGO"));
	            	objDatosGeneradosHisAntDTO.setDescripEstadoWkf(results.getString("ESTWTC_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setCodTareaEstadoWkf(results.getString("ESTWTC_ID_TAREA_FK"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodPerfil(results.getString("HIST_ID_PERFIL_FK"));
	            	objDatosGeneradosHisAntDTO.setFlagAdminPerfil(results.getString("PERF_FLAG_ADMINISTRACION"));
	            	objDatosGeneradosHisAntDTO.setFlagAsignacionPerfil(results.getString("PERF_FLAG_ASIGNACION"));
	            	objDatosGeneradosHisAntDTO.setFlagRegistraAyuPerfil(results.getString("PERF_FLAG_REGISTRA_AYU"));
	            	objDatosGeneradosHisAntDTO.setFlagRegistraExpPerfil(results.getString("PERF_FLAG_REGISTRA_EXP"));
	            	objDatosGeneradosHisAntDTO.setListaCorreoJefesPerfil(results.getString("PERF_LISTA_CORREO_JEFES"));
	             	objDatosGeneradosHisAntDTO.setFlagPendientesPerfil(results.getString("PERF_FLAG_PENDIENTES"));
	            	objDatosGeneradosHisAntDTO.setFlagProcesoPerfil(results.getString("PERF_FLAG_PROCESO"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuRegExpPerfil(results.getString("PERF_FLAG_MENU_REG_EXPEDIENTE"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuBandPendientesPerfil(results.getString("PERF_FLAG_MENU_BAND_PENDIENTES"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuBusquedaPerfil(results.getString("PERF_FLAG_MENU_BUSQUEDA"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuBandHistoricaPerfil(results.getString("PERF_FLAG_MENU_BAND_HISTORICA"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuBandAsignacionPerfil(results.getString("PERF_FLAG_MENU_BAND_ASIGNACION"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuBandMantenPerfil(results.getString("PERF_FLAG_MENU_BAND_MANTEN"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuRepHistorialPerfil(results.getString("PERF_FLAG_MENU_REP_HISTORIAL"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuRepConsolidadoPerfil(results.getString("PERF_FLAG_MENU_REP_CONSOLIDADO"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuRepToePerfil(results.getString("PERF_FLAG_MENU_REP_TOE"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuHorarioPerfil(results.getString("PERF_FLAG_MENU_HORARIO"));
	            	objDatosGeneradosHisAntDTO.setFlagMenuDescargaLdapPerfil(results.getString("PERF_FLAG_MENU_DESCARGA_LDAP"));
	            	objDatosGeneradosHisAntDTO.setDescripPerfil(results.getString("PERF_DESCRIPCION"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodMotivoDevol(results.getString("HIST_ID_MOTIVO_DEVOLUCION_FK"));
	            	objDatosGeneradosHisAntDTO.setCodigoMotivoDevol(results.getString("MOTDEV_CODIGO"));
	            	objDatosGeneradosHisAntDTO.setMotivoDescripcion(results.getString("MOTDEV_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setFlagOtrosMotivoDevol(results.getString("MOTDEV_FLAG_OTROS"));
	            	objDatosGeneradosHisAntDTO.setFlagActivoMotivoDevol(results.getString("MOTDEV_FLAG_ACTIVO"));
	            	objDatosGeneradosHisAntDTO.setFlagRechazoMotivoDevol(results.getString("MOTDEV_FLAG_RECHAZO"));
	             	objDatosGeneradosHisAntDTO.setCodTareaMotivoDevol(results.getString("MOTDEV_ID_TAREA_FK"));
	             	
	             	objDatosGeneradosHisAntDTO.setProducto(results.getString("PRODUC_DESCRIPCION"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodSubProducto(results.getString("HIST_ID_SUBPRODUCTO_FK"));
	            	objDatosGeneradosHisAntDTO.setSubProducto(results.getString("SUBPROD_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setCodProdSubProducto(results.getString("SUBPROD_ID_PRODUCTO_FK"));
	            	objDatosGeneradosHisAntDTO.setFlagActivoSubProducto(results.getString("SUBPROD_FLAG_ACTIVO"));
	            	objDatosGeneradosHisAntDTO.setTipMonedaSubProducto(results.getString("SUBPROD_ID_TIPO_MONEDA_FK"));
	            		            	
	            	objDatosGeneradosHisAntDTO.setCodTipoBuro(results.getString("HIST_ID_TIPO_BURO_FK"));
	            	objDatosGeneradosHisAntDTO.setCodigoTipoBuro(results.getString("TIPBURO_CODIGO"));
	            	objDatosGeneradosHisAntDTO.setTipoBuro(results.getString("TIPBURO_DESCRIPCION"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodTipoEnvio(results.getString("HIST_ID_TIPO_ENVIO_FK"));
	            	objDatosGeneradosHisAntDTO.setCodigoTipoEnvio(results.getString("TIPENVIO_CODIGO"));
	            	objDatosGeneradosHisAntDTO.setTipoEnvio(results.getString("TIPENVIO_DESCRIPCION"));
	           
	            	objDatosGeneradosHisAntDTO.setCodTipoMoneda(results.getString("HIST_ID_TIPMONSOL_FK"));
	            	objDatosGeneradosHisAntDTO.setCodigoTipoMoneda(results.getString("TIPMON_CODIGO"));
	            	objDatosGeneradosHisAntDTO.setTipoMoneda(results.getString("TIPMON_DESCRIPCION"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodTipoMonedaProb(results.getString("HIST_ID_TIPMONAPROB_FK"));
	            	objDatosGeneradosHisAntDTO.setCodigoTipoMonedaProb(results.getString("TIPMONPROB_CODIGO"));
	            	objDatosGeneradosHisAntDTO.setTipoMonedaProb(results.getString("TIPMONPROB_DESCRIPCION"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodTipoOferta(results.getString("HIST_ID_TIPO_OFERTA_FK"));
	            	objDatosGeneradosHisAntDTO.setTipoOferta(results.getString("TIPOFE_DESCRIPCION"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodTipoScoring(results.getString("HIST_ID_TIPO_SCORING_FK"));
	            	objDatosGeneradosHisAntDTO.setTipoScoring(results.getString("TIPSCOR_DESCRIPCION"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodTarea(results.getString("HIST_ID_TAREA_FK"));
	            	objDatosGeneradosHisAntDTO.setTarea(results.getString("TAREA_DESCRIPCION"));
	           	
	            	objDatosGeneradosHisAntDTO.setCodOficina(results.getString("HIST_ID_OFICINA_FK"));
	            	objDatosGeneradosHisAntDTO.setOficina(results.getString("OFI_DESCRIPCION"));
	            	objDatosGeneradosHisAntDTO.setFlagAreaRiesgoOficina(results.getString("OFI_FLAG_AREA_RIESGO"));
	            	objDatosGeneradosHisAntDTO.setFlagDesplazadaOficina(results.getString("OFI_FLAG_DESPLAZADA"));
	            	objDatosGeneradosHisAntDTO.setFlagEscaneoWebOficina(results.getString("OFI_FLAG_ESCANEO_WEB"));
	            	objDatosGeneradosHisAntDTO.setMontoTopeOficina(results.getString("OFI_MONTO_TOPE"));
	             	objDatosGeneradosHisAntDTO.setTasaTransfOficina(results.getString("OFI_TASA_TRANSF"));    
	             	
	             	objDatosGeneradosHisAntDTO.setFlagActivoOficina(results.getString("OFI_FLAG_ACTIVO"));
	             	objDatosGeneradosHisAntDTO.setCodOfiPrincipalOficina(results.getString("OFI_ID_OFI_PRINCIPAL_FK"));
	             	objDatosGeneradosHisAntDTO.setTerritorioOficina(results.getString("OFITERR_DESCRIPCION"));
	             	objDatosGeneradosHisAntDTO.setUbicacionOficina(results.getString("OFITERR_UBICACION"));
	             	objDatosGeneradosHisAntDTO.setFlagProvOficina(results.getString("OFITERR_FLAG_PROV"));
	            	
	            	objDatosGeneradosHisAntDTO.setCodClasifBanco(results.getString("HIST_CLASIFICACION_BANCO"));
	             	objDatosGeneradosHisAntDTO.setClasificacionBanco(results.getString("CLASBCO_DESCRIPCION"));
	             	objDatosGeneradosHisAntDTO.setCodProdClasifBanco(results.getString("CLASBCO_ID_PRODUCTO_FK"));
	            	
	             	objDatosGeneradosHisAntDTO.setCodClasifBancoConyugue(results.getString("HIST_CLASIFICACION_BANCO"));
	             	objDatosGeneradosHisAntDTO.setClasificacionBancoConyugue(results.getString("CLASBCO_DESCRIPCION"));
	             	objDatosGeneradosHisAntDTO.setCodProdClasifBancoConyugue(results.getString("CLASBCO_ID_PRODUCTO_FK"));
	            	/*
	            	objDatosGeneradosHisAntDTO.setClasificacionSBS(results.getString("HIST_CLASIFICACION_SBS"));
	            	objDatosGeneradosHisAntDTO.setCodPreEval(results.getString("HIST_COD_PRE_EVAL"));
	            	objDatosGeneradosHisAntDTO.setComentarioAyuMem(results.getString("HIST_COMENTARIO_AYU_MEM"));
	            	objDatosGeneradosHisAntDTO.setComentarioDelegacion(results.getString("HIST_COMENTARIO_DELEGACION"));
	            	objDatosGeneradosHisAntDTO.setComentarioRechazo(results.getString("HIST_COMENTARIO_RECHAZO"));
	            	objDatosGeneradosHisAntDTO.setFechaRegistro(results.getString("HIST_FEC_REGISTRO"));
	            	objDatosGeneradosHisAntDTO.setFechaEnvio(results.getString("HIST_FECHA_ENVIO"));
	            	objDatosGeneradosHisAntDTO.setFechaFin(results.getString("HIST_FECHA_FIN"));
	            	objDatosGeneradosHisAntDTO.setFechaProgramada(results.getString("HIST_FECHA_PROGRAMADA"));
	            	objDatosGeneradosHisAntDTO.setFechaT1(results.getString("HIST_FECHA_T1"));
	            	objDatosGeneradosHisAntDTO.setFechaT2(results.getString("HIST_FECHA_T2"));
	            	objDatosGeneradosHisAntDTO.setFechaT3(results.getString("HIST_FECHA_T3"));
	            	objDatosGeneradosHisAntDTO.setFlagComentario(results.getString("HIST_FLAG_COMENTARIO"));
	            	objDatosGeneradosHisAntDTO.setFlagDelegacion(results.getString("HIST_FLAG_DELEGACION"));
	            	objDatosGeneradosHisAntDTO.setFlagDevolucion(results.getString("HIST_FLAG_DEVOLUCION"));
	            	objDatosGeneradosHisAntDTO.setFlagExcDelegacion(results.getString("HIST_FLAG_EXC_DELEGACION"));
	            	objDatosGeneradosHisAntDTO.setFlagModifScore(results.getString("HIST_FLAG_MODIF_SCORE"));
	            	objDatosGeneradosHisAntDTO.setFlagRetraer(results.getString("HIST_FLAG_RETRAER"));
	            	objDatosGeneradosHisAntDTO.setFlagSolTasaEsp(results.getString("HIST_FLAG_SOL_TASA_ESP"));
	            	objDatosGeneradosHisAntDTO.setLineaConsumo(results.getString("HIST_LINEA_CONSUMO"));*/
	            	objDatosGeneradosHisAntDTO.setLineaCredAprob(results.getString("HIST_LINEA_CRED_APROB"));
	            	objDatosGeneradosHisAntDTO.setLineaCredSol(results.getString("HIST_LINEA_CRED_SOL"));
	            	objDatosGeneradosHisAntDTO.setNroContrato(results.getString("HIST_NRO_CONTRATO"));
	            	/*objDatosGeneradosHisAntDTO.setNroCuenta(results.getString("HIST_NRO_CTA"));
	            	objDatosGeneradosHisAntDTO.setNumTerminal(results.getString("HIST_NUM_TERMINAL"));
	            	objDatosGeneradosHisAntDTO.setTiempoCola(results.getString("HIST_TIEMPO_COLA"));
	            	objDatosGeneradosHisAntDTO.setTiempoEjecucion(results.getString("HIST_TIEMPO_EJECUCION"));
	            	objDatosGeneradosHisAntDTO.setFechaT1R(results.getString("HIST_FECHA_T1R"));
	            	objDatosGeneradosHisAntDTO.setFechaT2R(results.getString("HIST_FECHA_T2R"));
	            	objDatosGeneradosHisAntDTO.setFechaT3R(results.getString("HIST_FECHA_T3R"));
	            	objDatosGeneradosHisAntDTO.setTiempoColar(results.getString("HIST_TIEMPO_COLAR"));
	            	objDatosGeneradosHisAntDTO.setTiempoEjecucionR(results.getString("HIST_TIEMPO_EJECUCIONR"));
	            	objDatosGeneradosHisAntDTO.setTiempoColaRetraer(results.getString("HIST_TIEMPO_COLA_RETRAER"));
	            	objDatosGeneradosHisAntDTO.setPorcentajeEndeudamiento(results.getString("HIST_PORCENTAJE_ENDEUDAMIENTO"));
	            	objDatosGeneradosHisAntDTO.setRiesgoCliente(results.getString("HIST_RIESGO_CLIENTE"));
	            	objDatosGeneradosHisAntDTO.setSbsConyugue(results.getString("HIST_SBS_CONYUGE"));*/
	            	objDatosGeneradosHisAntDTO.setTasaEsp(results.getString("HIST_TASA_ESP"));
	            	/*objDatosGeneradosHisAntDTO.setTipoResolucion(results.getString("HIST_TIPO_RESOLUCION"));
	            	objDatosGeneradosHisAntDTO.setVerifDom(results.getString("HIST_VERIF_DOM"));
	            	objDatosGeneradosHisAntDTO.setVerifLab(results.getString("HIST_VERIF_LAB"));
	            	objDatosGeneradosHisAntDTO.setVerifDPS(results.getString("HIST_VERIF_DPS"));
	            	objDatosGeneradosHisAntDTO.setZonaPel(results.getString("HIST_ZONA_PEL"));*/
	            	objDatosGeneradosHisAntDTO.setPlazoSol(results.getString("HIST_PLAZO_SOL"));
	            	/*objDatosGeneradosHisAntDTO.setPlazoSolAprob(results.getString("HIST_PLAZO_SOL_APROB"));
	            	objDatosGeneradosHisAntDTO.setTipoCambioExp(results.getString("HIST_TIPO_CAMBIO_EXP"));
	            	objDatosGeneradosHisAntDTO.setFechaTipoCambioExp(results.getString("HIST_FECHA_TIPOCAMBIO_EXP"));
	            	objDatosGeneradosHisAntDTO.setTiempoObfTC(results.getString("HIST_TIEMPO_OBJ_TC"));
	            	objDatosGeneradosHisAntDTO.setTiempoObjTE(results.getString("HIST_TIEMPO_OBJ_TE"));
	            	objDatosGeneradosHisAntDTO.setTiempoPreTC(results.getString("HIST_TIEMPO_PRE_TC"));
	            	objDatosGeneradosHisAntDTO.setTiempoPreTe(results.getString("HIST_TIEMPO_PRE_TE"));
	            	objDatosGeneradosHisAntDTO.setNomColumna(results.getString("HIST_NOM_COLUMNA"));
	            	objDatosGeneradosHisAntDTO.setAns(results.getString("HIST_ANS"));
	                */
	            	objDatosGeneradosHisAntDTO.setCodigoRVGL(results.getString("HIST_CODIGO_RVGL"));
	            } catch(Throwable t){
	            	LOG.info("message: " + t.getMessage());
	            	t.printStackTrace();            	 
	            	
	            }
	            listDatosGeneradosHisAntDTO.add(objDatosGeneradosHisAntDTO);

	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("SQLException:getMessage" + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("Exception:getMessage" + e.getMessage());
		}

		return listDatosGeneradosHisAntDTO;
	}
	
	
	public DatosHistAntiguoDTO generarDatosHistAntiguoPorId(String idHistorico) throws DataAccessException {		
		DatosHistAntiguoDTO objDatosGeneradosHisAntPorIdDTO=null;
		
		LOG.info("1.- this.getEntityName(): " + this.getEntityName());
		CallableStatement stmt=getSQLStoredProcedureHistAntiguoPorId(this.getEntityNamePorId(),idHistorico);
		LOG.info("2.- despues de CallableStatement");
		try{
			LOG.info("3.- try");
			stmt.execute();			
			LOG.info("4.- Execute");
			ResultSet results = (ResultSet) stmt.getObject(1);
	        LOG.info("5.- results");
	        LOG.info("antes del if");
	        if(results.next()) {
	        	objDatosGeneradosHisAntPorIdDTO=new DatosHistAntiguoDTO();
	            try{
	            	objDatosGeneradosHisAntPorIdDTO.setOrigen(results.getString("ORIGEN"));	            	
	            	objDatosGeneradosHisAntPorIdDTO.setNroExpediente(results.getString("CORRELATIVO_EXPEDIENTE"));
	            	//objDatosGeneradosHisAntPorIdDTO.setId(results.getString("HIST_ID"));
	            	objDatosGeneradosHisAntPorIdDTO.setRiesgoCliente(results.getString("EXP_RIESGO_CLIENTE_GRUPAL"));
	            	
	            	objDatosGeneradosHisAntPorIdDTO.setCodOficina(results.getString("CORRELATIVO_OFICINA_EMP"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoOficina(results.getString("OFIEXP_CODIGO"));
	            	objDatosGeneradosHisAntPorIdDTO.setOficina(results.getString("OFIEXP_NOMBRE"));
	            	objDatosGeneradosHisAntPorIdDTO.setUbicacionOficina(results.getString("OFIEXP_UBICACION"));
	            	//objDatosGeneradosHisAntPorIdDTO.setofi(results.getString("OFIEXP_FECHA_CREACION"));
	            	//objDatosGeneradosHisAntPorIdDTO.setU)(results.getString("OFIEXP_USUARIO_CREACION"));
	            	//objDatosGeneradosHisAntPorIdDTO.setCodest(results.getString("OFIEXP_CORRELATIVO_ESTADO"));
	            	//objDatosGeneradosHisAntPorIdDTO.set(results.getString("ESTOFIEXP_CODIGO"));
	            	//objDatosGeneradosHisAntPorIdDTO.setes(results.getString("ESTOFIEXP_DESCRIPCION"));
	            	//objDatosGeneradosHisAntPorIdDTO.setri(results.getString("OFIEXP_INDICADOR_RIESGO"));
	            	objDatosGeneradosHisAntPorIdDTO.setMontoTopeOficina(results.getString("OFIEXP_MONTO_TOPE"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodTerritorioOficina(results.getString("OFIEXP_CORRELATIVO_TERRITORIO"));
	            	objDatosGeneradosHisAntPorIdDTO.setFlagEscaneoWebOficina(results.getString("OFIEXP_INDICADOR_ESCANNER"));
	            	objDatosGeneradosHisAntPorIdDTO.setTerritorioOficina(results.getString("TEROFIEXP_DESCRIPCION"));
	            	//objDatosGeneradosHisAntPorIdDTO.setUbicacionOficina(results.getString("TEROFIEXP_UBICACION"));
	            	//objDatosGeneradosHisAntPorIdDTO.setFlagProvOficina(results.getString("TEROFIEXP_FLAG_PROVINCIA"));
	            	
	            	
	            	objDatosGeneradosHisAntPorIdDTO.setCodClienteNatural(results.getString("CORRELATIVO_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setNombreCliente(results.getString("NOMBRE_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setApellidoPatCliente(results.getString("APELLIDO_PATERNO_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setApellidoMatCliente(results.getString("APELLIDO_MATERNO_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setNumDoiCliente(results.getString("DOI_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodTipoDoiCliente(results.getString("CORRELATIVO_TIPO_DOC_IDENTIDAD"));
	            	objDatosGeneradosHisAntPorIdDTO.setTipoDoiClienteDescrip(results.getString("NOMBRE_TIPO_DOC_IDENTIDAD"));
	            	objDatosGeneradosHisAntPorIdDTO.setPerExpPubCliente(results.getString("PERSO_EXPUESTA_PUBLICA_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setCorreoCliente(results.getString("CORREO"));
	            	objDatosGeneradosHisAntPorIdDTO.setCelularCliente(results.getString("CELULAR"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoCentralCliente(results.getString("CODIGO_CENTRAL_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodTipoCliente(results.getString("CORRELATIVO_TIPO_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setTipoClienteDescrip(results.getString("NOMBRE_TIPO_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoSegmentoCliente(results.getString("CODIGO_SEGMENTO_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setSegmentoCliente(results.getString("DESCRIPCION_SEGMENTO_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setPagoHabCliente(results.getString("PAGO_HABIENTE_CLIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodCategRentaCliente(results.getString("CODIGO_CAT_RENTA"));
	            	objDatosGeneradosHisAntPorIdDTO.setIngNetoMensualCliente(results.getString("CLI_INGRESO_NETO_MENSUAL"));
	            	
	            	objDatosGeneradosHisAntPorIdDTO.setNombresEmpleado(results.getString("NOMBRE_GESTOR_RIESGO"));
	            	objDatosGeneradosHisAntPorIdDTO.setApeMatEmpleado(results.getString("APELLIDO_PATERNO_GESTOR_RIESGO"));
	            	objDatosGeneradosHisAntPorIdDTO.setApeMatEmpleado(results.getString("APELLIDO_MATERNO_GESTOR_RIESGO"));
	            	objDatosGeneradosHisAntPorIdDTO.setNombresCompletosEmpleado(results.getString("NOMBRESCOMPLETOS_GESTOR_RIESGO"));
	            	
	            	objDatosGeneradosHisAntPorIdDTO.setCodEmpleado(results.getString("CODIGO_GESTOR_RIESGO"));
	            	objDatosGeneradosHisAntPorIdDTO.setUsuarioEmpleado(results.getString("USUARIO_GESTOR_RIESGO"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodPerfilEmleado(results.getString("CORRELATIVO_PERFIL_GR"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoPerfilEmpleado(results.getString("CODIGO_PERFIL_GR"));
	            	objDatosGeneradosHisAntPorIdDTO.setPerfilEmpleado(results.getString("NOMBRE_PERFIL_GR"));
	            	//objDatosGeneradosHisAntPorIdDTO.setCodigoSegmentoCliente(results.getString("CLI_ID_SEGMENTO_FK"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodOficinaEmpleado(results.getString("CODIGO_OFICINA_GR"));
	            	objDatosGeneradosHisAntPorIdDTO.setOficinaEmpleado(results.getString("NOMBRE_OFICINA_GR"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodTerritorioOfiEmpleado(results.getString("CORRELATIVO_TERRITORIO_GR"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoTerritorioEmpleado(results.getString("CODIGO_TERRITORIO_GR"));
	            	objDatosGeneradosHisAntPorIdDTO.setDescripTerOfiEmpleado(results.getString("NOMBRE_TERRITORIO_GR"));
	            	objDatosGeneradosHisAntPorIdDTO.setFlagProvinciaTerEmpleado(results.getString("FLAG_PROVINCIA_TERRITORIO_GR"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodProducto(results.getString("CORRELATIVO_PRODUCTO"));
	            	objDatosGeneradosHisAntPorIdDTO.setProducto(results.getString("NOMBRE_PRODUCTO"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodSubProducto(results.getString("CORRELATIVO_SUB_PRODUCTO"));
	            	objDatosGeneradosHisAntPorIdDTO.setSubProducto(results.getString("NOMBRE_SUB_PRODUCTO"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodGarantiaProducto(results.getString("CORRELATIVO_GARANTIA_PRODUCTO"));
	            	objDatosGeneradosHisAntPorIdDTO.setGarantiaProducto(results.getString("DESCRIPCION_GARANTIA_PRODUCTO"));
	            	
	            	objDatosGeneradosHisAntPorIdDTO.setCodTipoOferta(results.getString("CORRELATIVO_TIPO_OFERTA"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoTipoOferta(results.getString("CODIGO_TIPO_OFERTA"));
	            	objDatosGeneradosHisAntPorIdDTO.setNombreTipoOferta(results.getString("NOMBRE_TIPO_OFERTA"));
	            	objDatosGeneradosHisAntPorIdDTO.setTipoOferta(results.getString("DESCRIPCION_TIPO_OFERTA"));
	            	objDatosGeneradosHisAntPorIdDTO.setLineaCredSol(results.getString("IMPORTE_SOLICITADO_EXPEDIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setClasificacionSBS(results.getString("CALIFICACION_SBS_EXPEDIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setClasifSbsCony(results.getString("CLASIFICACION_SBS_CONY"));
	            	objDatosGeneradosHisAntPorIdDTO.setNroContrato(results.getString("EXP_CODIGO_CONTRATO"));
	            	
	            	
	            	objDatosGeneradosHisAntPorIdDTO.setClasificacionBanco(results.getString("CLASIFICACION_BANCO"));
	            	objDatosGeneradosHisAntPorIdDTO.setClasificacionBancoConyugue(results.getString("CLASIFICACION_BANCO_CONY"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoRVGL(results.getString("CODIGO_RVGL"));
	            	objDatosGeneradosHisAntPorIdDTO.setLineaCredAprob(results.getString("IMPORTE_APROBADO_PRESTAMO"));
	            	objDatosGeneradosHisAntPorIdDTO.setLineaConsumo(results.getString("LINEA_CONSUMO"));
	            	objDatosGeneradosHisAntPorIdDTO.setTasaEsp(results.getString("TASA_ESPECIAL"));
	            	//objDatosGeneradosHisAntPorIdDTO.setCodTipoScoring(results.getString("PR_SCORING_COD"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodTipoScoring(results.getString("TIPSCOR_COD"));
	            	objDatosGeneradosHisAntPorIdDTO.setTipoScoring(results.getString("TIPSCOR_descripcion"));
	            	
	            	objDatosGeneradosHisAntPorIdDTO.setPlazoSolAprob(results.getString("PLAZO_APROBADO"));
	            	objDatosGeneradosHisAntPorIdDTO.setPorcentajeEndeudamiento(results.getString("PORCENTAJE_ENDEUDAMIENTO"));
	            	objDatosGeneradosHisAntPorIdDTO.setModificarScoring(results.getString("MODIFICAR_SCORING"));
	            	objDatosGeneradosHisAntPorIdDTO.setTipoBuro(results.getString("EXP_GRUPO_BURO"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodEstCivilCliente(results.getString("ESTCIVIL_COD"));
	            	objDatosGeneradosHisAntPorIdDTO.setEstadoCivilCliente(results.getString("ESTCIVIL_descripcion"));
	            	
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoTipoMoneda(results.getString("MONEDA_IMPORTE_SOLI_EXPEDIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setFechaRegistroExp(results.getString("FECHA_REGISTRO_EXPEDIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodEstado(results.getString("CORRELATIVO_ESTADO"));
	            	objDatosGeneradosHisAntPorIdDTO.setEstadoCodigo(results.getString("NOMBRE_CORTO_ESTADO"));
	            	objDatosGeneradosHisAntPorIdDTO.setEstadoDescrip(results.getString("NOMBRE_LARGO_ESTADO"));
	            	objDatosGeneradosHisAntPorIdDTO.setFechaVenDoiCliente(results.getString("FECHA_VENCIMIENTO_DOI"));
	            	objDatosGeneradosHisAntPorIdDTO.setFechaFinExp(results.getString("FECHA_CIERRE_EXPEDIENTE"));
	            	objDatosGeneradosHisAntPorIdDTO.setNombresEmpleadoResp(results.getString("NOMBRE_GESTOR_DESTINO"));
	            	objDatosGeneradosHisAntPorIdDTO.setApellidoPatConyugue(results.getString("APELLIDO_PATER_GESTOR_DESTINO"));
	            	objDatosGeneradosHisAntPorIdDTO.setApellidoMatConyugue(results.getString("APELLIDO_MATER_GESTOR_DESTINO"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodEmpleadoResp(results.getString("CODIGO_GESTOR_DESTINO"));
	            	objDatosGeneradosHisAntPorIdDTO.setUsuarioEmpleado(results.getString("USUARIO_GESTOR_DESTINO"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodPerfilEmleadoResp(results.getString("CORRELATIVO_PERFIL_GD"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoPerfilEmpleadoResp(results.getString("CODIGO_PERFIL_GD"));
	            	objDatosGeneradosHisAntPorIdDTO.setPerfilEmpleadoResp(results.getString("NOMBRE_PERFIL_GD"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodOficinaEmpleadoResp(results.getString("CORRELATIVO_OFICINA_GD"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoOfiEmpleadoResp(results.getString("CODIGO_OFICINA_GD"));
	            	objDatosGeneradosHisAntPorIdDTO.setOficinaEmpleadoResp(results.getString("NOMBRE_OFICINA_GD"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodTerritorioOfiEmpleadoResp(results.getString("CORRELATIVO_TERRITORIO_GD"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoTerritorioEmpleadoResp(results.getString("CODIGO_TERRITORIO_GD"));
	            	objDatosGeneradosHisAntPorIdDTO.setDescripTerOfiEmpleadoResp(results.getString("NOMBRE_TERRITORIO_GD"));
	            	objDatosGeneradosHisAntPorIdDTO.setFlagProvinciaTerEmpleadoResp(results.getString("FLAG_PROVINCIA_TERRITORIO_GD"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodTipoMoneda(results.getString("CORRELATIVO_MONEDA_EXP"));
	            	objDatosGeneradosHisAntPorIdDTO.setTipoMoneda(results.getString("DESCRIPCION_MONEDA_EXP"));
	            	objDatosGeneradosHisAntPorIdDTO.setPlazoSolicitadoExp(results.getString("PLAZO_SOLICITADO_EXP"));
	            	objDatosGeneradosHisAntPorIdDTO.setFlagVerificacionLabExp(results.getString("FLAG_VERIFICACION_LABORAL_EXP"));
	            	objDatosGeneradosHisAntPorIdDTO.setFlagVerificacionDomExp(results.getString("FLAG_VERIFICA_DOMICILIARIA_EXP"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoPreevaluadorExp(results.getString("CODIGO_PREEVALUADOR_EXP"));
	            	objDatosGeneradosHisAntPorIdDTO.setModificarTasaExp(results.getString("MODIFICAR_TASA_EXP"));
	            	objDatosGeneradosHisAntPorIdDTO.setIndicadorSubrogado(results.getString("Indicador_Subrogado"));
	            	objDatosGeneradosHisAntPorIdDTO.setIndicadorDesemCent(results.getString("Indicador_Desem_Cent"));
	            	objDatosGeneradosHisAntPorIdDTO.setCodigoEmpleado(results.getString("codigo_usuario_creacion"));
	            	
	            		                
	            } catch(Throwable t){
	            	LOG.info("message: " + t.getMessage());
	            	t.printStackTrace();            	 
	            	
	            }
	            
	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("SQLException:getMessage" + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("Exception:getMessage" + e.getMessage());
		}

		return objDatosGeneradosHisAntPorIdDTO;
	}
	
	public List<DatosAyudaMemoriaIiceDTO> generarDatosAyudaMemoriaIICE(ArrayList pParameters) throws DataAccessException {		
		List<DatosAyudaMemoriaIiceDTO> listDatosGeneradosAyuMemoriaIiceDTO=null;
		DatosAyudaMemoriaIiceDTO objDatosGeneradosAyuMemoriaIiceDTO=null;
		
		LOG.info("1.- this.getEntityName(): " + this.getAyudaMemoriaIICE());
		CallableStatement stmt=getSQLStoredProcedureHistAntiguo(this.getAyudaMemoriaIICE(),pParameters);
		LOG.info("2.- despues de CallableStatement");
		try{
			LOG.info("3.- try");
			stmt.execute();			
			LOG.info("4.- Execute");
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        LOG.info("5.- results");
	        listDatosGeneradosAyuMemoriaIiceDTO=new ArrayList<DatosAyudaMemoriaIiceDTO>();
	        LOG.info("antes del while");
	        while (results.next()) {
	        	
	        	objDatosGeneradosAyuMemoriaIiceDTO=new DatosAyudaMemoriaIiceDTO();
	            try{
	            		            	
	            	objDatosGeneradosAyuMemoriaIiceDTO.setNroExpediente(results.getString("CORRELATIVO_EXPEDIENTE"));
	            	objDatosGeneradosAyuMemoriaIiceDTO.setIdAyuda(results.getString("CORRELATIVO_AYUDA"));
	            	objDatosGeneradosAyuMemoriaIiceDTO.setDetalleAyuda(results.getString("DETALLE_AYUDA"));
	            	objDatosGeneradosAyuMemoriaIiceDTO.setCodUsuario(results.getString("CODIGO_USUARIO"));
	            	objDatosGeneradosAyuMemoriaIiceDTO.setNombreUsuario(results.getString("NOMBRE_USUARIO"));
	            	objDatosGeneradosAyuMemoriaIiceDTO.setEstadoExpediente(results.getString("ESTADO_EXPEDIENTE"));
	            	objDatosGeneradosAyuMemoriaIiceDTO.setFechaHora(results.getString("FECHA_HORA"));
	            	objDatosGeneradosAyuMemoriaIiceDTO.setPerfilUsuario(results.getString("PERFIL_USUARIO"));
	                 
	            } catch(Throwable t){
	            	LOG.info("message: " + t.getMessage());
	            	t.printStackTrace();            	 
	            	
	            }
	            listDatosGeneradosAyuMemoriaIiceDTO.add(objDatosGeneradosAyuMemoriaIiceDTO);

	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("SQLException:getMessage" + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("Exception:getMessage" + e.getMessage());
		}

		return listDatosGeneradosAyuMemoriaIiceDTO;
	}
	
	public List<DatosDetalleHistoricoIiceDTO> generarDetalleHistorialIICE(ArrayList pParameters) throws DataAccessException {		
		List<DatosDetalleHistoricoIiceDTO> listDatosDetalleHistIiceDTO=null;
		DatosDetalleHistoricoIiceDTO objDatosDetalleHistIiceDTO=null;
		
		LOG.info("1.- this.getEntityName(): " + this.getEntityName());
		CallableStatement stmt=getSQLStoredProcedureHistAntiguo(this.getDetalleHistorialIICE(),pParameters);
		LOG.info("2.- despues de CallableStatement");
		try{
			LOG.info("3.- try");
			stmt.execute();			
			LOG.info("4.- Execute");
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        LOG.info("5.- results");
	        listDatosDetalleHistIiceDTO=new ArrayList<DatosDetalleHistoricoIiceDTO>();
	        LOG.info("antes del while");
	        while (results.next()) {
	        	
	        	if(!results.isFirst()){
	        		objDatosDetalleHistIiceDTO=new DatosDetalleHistoricoIiceDTO();
		            try{
		            	
		            	objDatosDetalleHistIiceDTO.setNroHistorial(results.getString("CORRELATIVO_HISTORIAL"));
		            	objDatosDetalleHistIiceDTO.setCodUsuario(results.getString("CODIGO_USUARIO"));
		            	objDatosDetalleHistIiceDTO.setNombreUsuario(results.getString("NOMBRE_USUARIO"));
		            	objDatosDetalleHistIiceDTO.setEstadoExpediente(results.getString("ESTADO_EXPEDIENTE"));
		            	objDatosDetalleHistIiceDTO.setFechaHoraEnvio(results.getString("FECHA_HORA_ENVIO"));
		            	objDatosDetalleHistIiceDTO.setFechaHoraAtencion(results.getString("FECHA_HORA_ATENCION"));
		            	objDatosDetalleHistIiceDTO.setPerfilUsuario(results.getString("PERFIL_USUARIO"));
		            	objDatosDetalleHistIiceDTO.setNroExpediente(results.getString("CORRELATIVO_EXPEDIENTE"));
		            	objDatosDetalleHistIiceDTO.setTerminal(results.getString("TERMINAL"));
		            	objDatosDetalleHistIiceDTO.setNombrePerfil(results.getString("PERFIL_NOMBRE"));
		            	objDatosDetalleHistIiceDTO.setNombreEmpleado(results.getString("EMP_NOMBRE"));
		            	objDatosDetalleHistIiceDTO.setApePatEmpleado(results.getString("EMP_APELLIDOP"));
		            	objDatosDetalleHistIiceDTO.setApeMatEmpleado(results.getString("EMP_APELLIDOM"));
		            	objDatosDetalleHistIiceDTO.setFechaHoraTrabajo(results.getString("FECHA_HORA_TRABAJO"));
		            	objDatosDetalleHistIiceDTO.setFlagDevolucion(results.getString("FLAG_DEVOLUCION"));
		            	objDatosDetalleHistIiceDTO.setCodEstado(results.getString("CODIGO_ESTADO"));
		                 
		            } catch(Throwable t){
		            	LOG.info("message: " + t.getMessage());
		            	t.printStackTrace();            	 
		            	
		            }
		            listDatosDetalleHistIiceDTO.add(objDatosDetalleHistIiceDTO);
	        	}
	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("SQLException:getMessage" + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("Exception:getMessage" + e.getMessage());
		}

		return listDatosDetalleHistIiceDTO;
	}
	
	public List<DatosDetalleObservacionesIiceDTO> generarDetalleObservacionesIICE(ArrayList pParameters) throws DataAccessException {		
		List<DatosDetalleObservacionesIiceDTO> listDatosDetalleObservIiceDTO=null;
		DatosDetalleObservacionesIiceDTO objDatosDetalleObservIiceDTO=null;
		
		LOG.info("1.- this.getEntityName(): " + this.getEntityName());
		CallableStatement stmt=getSQLStoredProcedureHistAntiguo(this.getDetalleObservacionesIICE(),pParameters);
		LOG.info("2.- despues de CallableStatement");
		try{
			LOG.info("3.- try");
			stmt.execute();			
			LOG.info("4.- Execute");
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        LOG.info("5.- results");
	        listDatosDetalleObservIiceDTO=new ArrayList<DatosDetalleObservacionesIiceDTO>();
	        LOG.info("antes del while");
	        while (results.next()) {
	        	
	        	objDatosDetalleObservIiceDTO=new DatosDetalleObservacionesIiceDTO();
	            try{
	            		            	
	            	objDatosDetalleObservIiceDTO.setNroObservacion(results.getString("CORRELATIVO_OBSERVACION"));
	            	objDatosDetalleObservIiceDTO.setCodUsuario(results.getString("CODIGO_USUARIO"));
	            	objDatosDetalleObservIiceDTO.setNombreUsuario(results.getString("NOMBRE_USUARIO"));
	            	objDatosDetalleObservIiceDTO.setEstadoExpediente(results.getString("ESTADO_EXPEDIENTE"));
	            	objDatosDetalleObservIiceDTO.setFechaHora(results.getString("FECHA_HORA"));
	            	objDatosDetalleObservIiceDTO.setTerminal(results.getString("TERMINAL"));
	            	objDatosDetalleObservIiceDTO.setDetalleObservacion(results.getString("DETALLE_OBSERVACION"));
	            	objDatosDetalleObservIiceDTO.setNroExpediente(results.getString("CORRELATIVO_EXPEDIENTE"));
	            	objDatosDetalleObservIiceDTO.setTipo(results.getString("TIPO"));
	            	objDatosDetalleObservIiceDTO.setNombrePerfil(results.getString("NOMBRE_PERFIL"));
	            	objDatosDetalleObservIiceDTO.setNombreEmpleado(results.getString("NOMBRE_EM"));
	            	objDatosDetalleObservIiceDTO.setApePatEmpleado(results.getString("EMP_APELLIDOP"));
	            	objDatosDetalleObservIiceDTO.setApeMatEmpleado(results.getString("EMP_APELLIDOM"));
	            	            	
	                 
	            } catch(Throwable t){
	            	LOG.info("message: " + t.getMessage());
	            	t.printStackTrace();            	 
	            	
	            }
	            listDatosDetalleObservIiceDTO.add(objDatosDetalleObservIiceDTO);

	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("SQLException:getMessage" + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("Exception:getMessage" + e.getMessage());
		}

		return listDatosDetalleObservIiceDTO;
	}
	
	public List<DatosDetalleLogIiceDTO> generarDetalleLogIICE(ArrayList pParameters) throws DataAccessException {		
		List<DatosDetalleLogIiceDTO> listDatosDetalleLogIiceDTO=null;
		DatosDetalleLogIiceDTO objDatosDetalleLogIiceDTO=null;
		
		LOG.info("1.- this.getEntityName(): " + this.getEntityName());
		CallableStatement stmt=getSQLStoredProcedureHistAntiguo(this.getDetalleLogIICE(),pParameters);
		LOG.info("2.- despues de CallableStatement");
		try{
			LOG.info("3.- try");
			stmt.execute();			
			LOG.info("4.- Execute");
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        LOG.info("5.- results");
	        listDatosDetalleLogIiceDTO=new ArrayList<DatosDetalleLogIiceDTO>();
	        LOG.info("antes del while");
	        while (results.next()) {
	        	
	        	objDatosDetalleLogIiceDTO=new DatosDetalleLogIiceDTO();
	            try{
	            		            	
	            	objDatosDetalleLogIiceDTO.setNroLog(results.getString("CORRELATIVO_LOG"));
	            	objDatosDetalleLogIiceDTO.setCodUsuario(results.getString("CODIGO_USUARIO"));
	            	objDatosDetalleLogIiceDTO.setNombreUsuario(results.getString("NOMBRE_USUARIO"));
	            	objDatosDetalleLogIiceDTO.setPerfilUsuario(results.getString("PERFIL_USUARIO"));
	            	objDatosDetalleLogIiceDTO.setEstadoExpediente(results.getString("ESTADO_EXPEDIENTE"));
	            	objDatosDetalleLogIiceDTO.setFechaHoraEnvio(results.getString("FECHA_HORA_ENVIO"));
	            	objDatosDetalleLogIiceDTO.setFechaHoraAtencion(results.getString("FECHA_HORA_ATENCION"));
	            	objDatosDetalleLogIiceDTO.setEventoRealizado(results.getString("EVENTO_REALIZADO"));
	            	objDatosDetalleLogIiceDTO.setNroExpediente(results.getString("CORRELATIVO_EXPEDIENTE"));
	            	objDatosDetalleLogIiceDTO.setTerminal(results.getString("TERMINAL"));
	            	objDatosDetalleLogIiceDTO.setNomrePerfil(results.getString("NOMBRE_PERFIL"));
	            		            	            	
	                 
	            } catch(Throwable t){
	            	LOG.info("message: " + t.getMessage());
	            	t.printStackTrace();            	 
	            	
	            }
	            listDatosDetalleLogIiceDTO.add(objDatosDetalleLogIiceDTO);

	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("SQLException:getMessage" + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("Exception:getMessage" + e.getMessage());
		}

		return listDatosDetalleLogIiceDTO;
	}

	public List<DatosDocumentosExpIiceDTO> generarDocumentosPorExpIICE(ArrayList pParameters) throws DataAccessException {		
		List<DatosDocumentosExpIiceDTO> listDocPorExpIiceDTO=null;
		DatosDocumentosExpIiceDTO objDocPorExpIiceDTO=null;
		
		LOG.info("1.- this.getEntityName(): " + this.getEntityName());
		CallableStatement stmt=getSQLStoredProcedureHistAntiguo(this.getDocumentoPorExpIICE(),pParameters);
		LOG.info("2.- despues de CallableStatement");
		try{
			LOG.info("3.- try");
			stmt.execute();			
			LOG.info("4.- Execute");
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        LOG.info("5.- results");
	        listDocPorExpIiceDTO=new ArrayList<DatosDocumentosExpIiceDTO>();
	        LOG.info("antes del while");
	        while (results.next()) {
	        	
	        	objDocPorExpIiceDTO=new DatosDocumentosExpIiceDTO();
	            try{
	            		            	
	            	objDocPorExpIiceDTO.setCorrelativoDocExp(results.getString("DE_CORDOCEXP"));
	            	objDocPorExpIiceDTO.setCorrelativoDoc(results.getString("D_CORDOC"));
	            	objDocPorExpIiceDTO.setCorrelativoTipoDoc(results.getString("D_CORTIPODOC"));
	            	objDocPorExpIiceDTO.setFechaCreacion(results.getString("D_FEC_CREA"));
	            	objDocPorExpIiceDTO.setFechaModificacion(results.getString("D_FEC_MOD"));
	            	objDocPorExpIiceDTO.setFechaVencimiento(results.getString("D_FEC_VENC"));
	            	objDocPorExpIiceDTO.setTipoDocumentoCodigo(results.getString("TD_CODIGO"));
	            	objDocPorExpIiceDTO.setTipoDocumentoDescripcion(results.getString("TD_DESC"));
	            	objDocPorExpIiceDTO.setTipoDocumentoNombre(results.getString("TD_NOMBRE"));
	            	objDocPorExpIiceDTO.setUsuarioExpediente(results.getString("DE_USUARIO"));
	            	objDocPorExpIiceDTO.setPerfilUsuarioExpediente(results.getString("DE_PERFIL"));
	            	objDocPorExpIiceDTO.setCorrelativoEstadoExpediente(results.getString("DE_ESTADO"));
	            	objDocPorExpIiceDTO.setFlagAdjunto(results.getString("FLAG_ADJ"));
	            		            	            	
	                 
	            } catch(Throwable t){
	            	LOG.info("message: " + t.getMessage());
	            	t.printStackTrace();            	 
	            	
	            }
	            listDocPorExpIiceDTO.add(objDocPorExpIiceDTO);

	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("SQLException:getMessage" + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("Exception:getMessage" + e.getMessage());
		}

		return listDocPorExpIiceDTO;
	}


	@Override
	protected <T extends AbstractDTO> List<T> processResult(ResultSet rs)
			throws DataAccessException {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	@Override
	protected String processResultReturnString(ResultSet rs)
			throws DataAccessException {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	@Override
    protected String getEntityName() {
        return "CONELE.PG_CE_HISTOR_IICE.SP_CE_HISTORICO_UNIFICADO";
    }
	
    protected String getEntityNamePorId() {
        return "CONELE.PG_CE_HISTOR_IICE.SP_CE_HISTORICO_UNIF_POR_ID";
    }
   
    protected String getAyudaMemoriaIICE() {
        return "CONELE.PG_CE_HISTOR_IICE.SP_CE_AYUDA_MEMORIA_IICE";
    }
    
    protected String getDetalleHistorialIICE() {
        return "CONELE.PG_CE_HISTOR_IICE.SP_CE_DETALLE_HISTORIAL_IICE";
    }
    
    protected String getDetalleObservacionesIICE() {
        return "CONELE.PG_CE_HISTOR_IICE.SP_CE_DET_OBSERVACIONES_IICE";
    }
    
    protected String getDetalleLogIICE() {
        return "CONELE.PG_CE_HISTOR_IICE.SP_CE_DETALLE_LOG_IICE";
    }
    
    protected String getDocumentoPorExpIICE() {
        return "CONELE.PG_CE_HISTOR_IICE.SP_CE_DOCUMENTO_X_EXP";
    }
}

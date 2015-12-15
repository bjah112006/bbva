package com.ibm.bbva.tabla.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.DatosGeneradosHisDTO;

public class DatosGeneradosHisDAO extends BaseDAO{
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosGeneradosHisDAO.class);

	private static DatosGeneradosHisDAO instance = null;
	
	private DatosGeneradosHisDAO() {
	}
	
	public static DatosGeneradosHisDAO getInstance() {
		if ( instance==null )
			 instance = new DatosGeneradosHisDAO();
		return instance;
	}
	
	public List<DatosGeneradosHisDTO> generarDatosHistorico(ArrayList pParameters) throws DataAccessException {		
		List<DatosGeneradosHisDTO> listDatosGeneradosHisDTO=null;
		DatosGeneradosHisDTO objDatosGeneradosHisDTO=null;
		LOG.info("1.- this.getEntityName(): " + this.getEntityName());
		CallableStatement stmt=getSQLStoredProcedureGenerico(this.getEntityName(),pParameters);
		LOG.info("2.- despues de CallableStatement");
		try{
			LOG.info("3.- try");
			stmt.execute();			
			LOG.info("4.- Execute");
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        LOG.info("5.- results");
	        listDatosGeneradosHisDTO=new ArrayList<DatosGeneradosHisDTO>();
	        LOG.info("antes del while");
	        while (results.next()) {
	        	
	            objDatosGeneradosHisDTO=new DatosGeneradosHisDTO();
	            try{
	                //objDatosGeneradosConsDTO.setNumeroExpediente(results.getString("NUMERO_EXPEDIENTE"));
	            	objDatosGeneradosHisDTO.setNroExpediente(results.getString("NRO_EXPEDIENTE"));
	            	objDatosGeneradosHisDTO.setNumeroRegistro(results.getString("NUMERO_REGISTRO"));
	            	objDatosGeneradosHisDTO.setUsuarioActual(results.getString("USUARIO_ACTUAL"));
	            	objDatosGeneradosHisDTO.setEstadoExpediente(results.getString("ESTADO_EXPEDIENTE"));
	            	objDatosGeneradosHisDTO.setCodigoEstado(results.getString("CODIGO_ESTADO"));
	            	objDatosGeneradosHisDTO.setPerfil(results.getString("PERFIL"));
	            	objDatosGeneradosHisDTO.setTarea(results.getString("TAREA"));
	            	objDatosGeneradosHisDTO.setAccion(results.getString("ACCION"));
	            	objDatosGeneradosHisDTO.setCodigoProducto(results.getString("CODIGO_PRODUCTO"));
	            	objDatosGeneradosHisDTO.setNombreProducto(results.getString("NOMBRE_PRODUCTO"));
	            	objDatosGeneradosHisDTO.setNombreTipoOferta(results.getString("NOMBRE_TIPO_OFERTA"));
	            	objDatosGeneradosHisDTO.setCodigoSubProducto(results.getString("CODIGO_SUB_PRODUCTO"));
	            	objDatosGeneradosHisDTO.setNombreSubProducto(results.getString("NOMBRE_SUB_PRODUCTO"));
	            	objDatosGeneradosHisDTO.setCodigoOficina(results.getString("CODIGO_OFICINA"));
	            	objDatosGeneradosHisDTO.setNombreOficina(results.getString("NOMBRE_OFICINA"));
	            	objDatosGeneradosHisDTO.setCodigoTerritorio(results.getString("CODIGO_TERRITORIO"));
	            	objDatosGeneradosHisDTO.setNombreTerritorio(results.getString("NOMBRE_TERRITORIO"));
	            	
	            	objDatosGeneradosHisDTO.setCodigoOficinaGestora(results.getString("CODIGO_OFICINA_GESTORA"));
	            	objDatosGeneradosHisDTO.setNombreOficinaGestora(results.getString("NOMBRE_OFICINA_GESTORA"));
	            	objDatosGeneradosHisDTO.setCodigoTerritorioGestora(results.getString("CODIGO_TERRITORIO_GESTORA"));
	            	objDatosGeneradosHisDTO.setNombreTerritorioGestora(results.getString("NOMBRE_TERRITORIO_GESTORA"));
	            	
	            	objDatosGeneradosHisDTO.setFechaHoraLlegada(results.getString("FECHA_HORA_LLEGADA"));
	            	objDatosGeneradosHisDTO.setFechaHoraInicioTrabajo(results.getString("FECHA_HORA_INICIO_TRABAJO"));
	            	objDatosGeneradosHisDTO.setFechaHoraEnvio(results.getString("FECHA_HORA_ENVIO"));
	            	objDatosGeneradosHisDTO.setTiempoEjecucionTe(results.getString("TIEMPO_EJECUCION_TE"));
	            	objDatosGeneradosHisDTO.setTiempoColaTc(results.getString("TIEMPO_COLA_TC"));
	            	objDatosGeneradosHisDTO.setTiempoProcesoTp(results.getString("TIEMPO_PROCESO_TP"));
	            	objDatosGeneradosHisDTO.setCumplioAns(results.getString("CUMPLIO_ANS"));
	            	objDatosGeneradosHisDTO.setFlagDevolucion(results.getString("FLAG_DEVOLUCION"));
	            	objDatosGeneradosHisDTO.setFlagRetraer(results.getString("FLAG_RETRAER"));
	            	objDatosGeneradosHisDTO.setTerminal(results.getString("TERMINAL"));
	            	objDatosGeneradosHisDTO.setObservacion(results.getString("OBSERVACION"));
	            	objDatosGeneradosHisDTO.setMotivoRechazo(results.getString("MOTIVO_RECHAZO"));
	            	objDatosGeneradosHisDTO.setComentario(results.getString("COMENTARIO"));
	            	objDatosGeneradosHisDTO.setAns(results.getString("ANS"));
	                
	            } catch(Throwable t){
	            	LOG.info("message: " + t.getMessage());
	            	t.printStackTrace();            	 
	            	
	            }
	            listDatosGeneradosHisDTO.add(objDatosGeneradosHisDTO);

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

		return listDatosGeneradosHisDTO;
	}
	
	public  Map<String, Object[]> generarDatosHistoricoMap(ArrayList pParameters) throws DataAccessException {		
		Map<String, Object[]> mapDatosGeneradosHisDTO = null;
		LOG.info("1.- this.getEntityName(): " + this.getEntityName());
		CallableStatement stmt=getSQLStoredProcedureGenerico(this.getEntityName(),pParameters);
		LOG.info("2.- despues de CallableStatement");
		try{
			LOG.info("3.- try");
			stmt.execute();			
			LOG.info("4.- Execute");
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        LOG.info("5.- results");
	        mapDatosGeneradosHisDTO = new HashMap<String, Object[]>();
	        int i=0;
	        LOG.info("antes del while");
	        while (results.next()) {
	        	
	            try{
	            	
	            	mapDatosGeneradosHisDTO.put(i+"",new Object[]{results.getString("NRO_EXPEDIENTE"),
	            												results.getString("NUMERO_REGISTRO"),
	            												results.getString("USUARIO_ACTUAL"),
	            												results.getString("ESTADO_EXPEDIENTE"),
	            												results.getString("CODIGO_ESTADO"),
	            												results.getString("PERFIL"),
	            												results.getString("TAREA"),
	            												results.getString("ACCION"),
	            												results.getString("CODIGO_PRODUCTO"),
	            												results.getString("NOMBRE_PRODUCTO"),
	            												results.getString("NOMBRE_TIPO_OFERTA"),
	            												results.getString("CODIGO_SUB_PRODUCTO"),
	            												results.getString("NOMBRE_SUB_PRODUCTO"),
	            												results.getString("CODIGO_OFICINA"),
	            												results.getString("NOMBRE_OFICINA"),
	            												results.getString("CODIGO_TERRITORIO"),
	            												results.getString("NOMBRE_TERRITORIO"),
	            												results.getString("CODIGO_OFICINA_GESTORA"),
	            												results.getString("NOMBRE_OFICINA_GESTORA"),
	            												results.getString("CODIGO_TERRITORIO_GESTORA"),
	            												results.getString("NOMBRE_TERRITORIO_GESTORA"),
	            												results.getString("FECHA_HORA_LLEGADA"),
	            												results.getString("FECHA_HORA_INICIO_TRABAJO"),
	            												results.getString("FECHA_HORA_ENVIO"),
	            												results.getString("TIEMPO_EJECUCION_TE"),
	            												results.getString("TIEMPO_COLA_TC"),
	            												results.getString("TIEMPO_PROCESO_TP"),
	            												results.getString("CUMPLIO_ANS"),
	            												results.getString("FLAG_DEVOLUCION"),
	            												results.getString("FLAG_RETRAER"),
	            												results.getString("TERMINAL"),
	            												results.getString("OBSERVACION"),
	            												results.getString("MOTIVO_RECHAZO"),
	            												results.getString("COMENTARIO"),
	            												results.getString("ANS")});
	            	
	                 
	            } catch(Throwable t){
	            	LOG.info("message: " + t.getMessage());
	            	t.printStackTrace();            	 
	            	
	            }
	            i ++;

	        }
	        LOG.info("REPORTE HISTORICO...TOTAL REGISTROS DATA RECUPERADA:: " + i);
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("SQLException:getMessage" + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			LOG.info("Exception:getMessage" + e.getMessage());
		}
		
		return mapDatosGeneradosHisDTO;
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
        return "CONELE.PG_CE_REPORTE_TC.SP_SEL_REPORTETC";
    }	
}

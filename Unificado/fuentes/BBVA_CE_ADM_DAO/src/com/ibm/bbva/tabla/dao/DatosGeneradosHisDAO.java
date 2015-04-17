package com.ibm.bbva.tabla.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.DatosGeneradosHisDTO;

public class DatosGeneradosHisDAO extends BaseDAO{

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
		System.out.println("1.- this.getEntityName(): " + this.getEntityName());
		CallableStatement stmt=getSQLStoredProcedureGenerico(this.getEntityName(),pParameters);
		System.out.println("2.- despues de CallableStatement");
		try{
			System.out.println("3.- try");
			stmt.execute();			
			System.out.println("4.- Execute");
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        System.out.println("5.- results");
	        listDatosGeneradosHisDTO=new ArrayList<DatosGeneradosHisDTO>();
	        System.out.println("antes del while");
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
	            	System.out.println("message: " + t.getMessage());
	            	t.printStackTrace();            	 
	            	
	            }
	            listDatosGeneradosHisDTO.add(objDatosGeneradosHisDTO);

	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			System.out.println("SQLException:getMessage" + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
			System.out.println("Exception:getMessage" + e.getMessage());
		}

		return listDatosGeneradosHisDTO;
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

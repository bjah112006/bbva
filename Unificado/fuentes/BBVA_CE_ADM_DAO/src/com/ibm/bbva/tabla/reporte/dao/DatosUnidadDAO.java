package com.ibm.bbva.tabla.reporte.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.reporte.dto.DatosUnidadDTO;

public class DatosUnidadDAO extends BaseDAO{

	private static DatosUnidadDAO instance = null;
	
	private DatosUnidadDAO() {
	}
	
	public static DatosUnidadDAO getInstance() {
		if ( instance==null )
			instance = new DatosUnidadDAO();
		return instance;
	}
	
	public List<DatosUnidadDTO> generarDatosUnidad(ArrayList pParameters) throws DataAccessException {		
		List<DatosUnidadDTO> listDatosUnidadDTO=null;
		DatosUnidadDTO objDatosUnidadDTO=null;
		CallableStatement stmt=getSQLStoredProcedureGenerico(this.getEntityName(),pParameters);
		
		try{
			stmt.execute();			
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        listDatosUnidadDTO=new ArrayList<DatosUnidadDTO>();

	        while (results.next()) {
	        	
	            objDatosUnidadDTO=new DatosUnidadDTO();
	            try{
	            	objDatosUnidadDTO.setTipo(results.getString("CONCEPTO"));
	            	objDatosUnidadDTO.setFlujo(results.getString("FLUJO"));
	            	objDatosUnidadDTO.setAceptado(results.getFloat("SICUMPLE"));
	            	objDatosUnidadDTO.setTotal(results.getFloat("TOTAL"));

	            } catch(Throwable t){
	            	
	            }
	            listDatosUnidadDTO.add(objDatosUnidadDTO);

	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
		}

		return listDatosUnidadDTO;
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
		return "CONELE.PG_CE_TOE_UNIDAD.SP_SEL_REPORTE_TOE_UNIDAD";
	}




}

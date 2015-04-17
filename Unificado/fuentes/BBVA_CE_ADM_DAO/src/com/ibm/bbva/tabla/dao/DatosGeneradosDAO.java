package com.ibm.bbva.tabla.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.DatosGeneradosDTO;
import com.ibm.bbva.tabla.dto.PosibleValorDTO;

public class DatosGeneradosDAO extends BaseDAO{

	private static DatosGeneradosDAO instance = null;
	
	private DatosGeneradosDAO() {
	}
	
	public static DatosGeneradosDAO getInstance() {
		if ( instance==null )
			instance = new DatosGeneradosDAO();
		return instance;
	}
	
	
	public List<DatosGeneradosDTO> generarDatosGenerico(ArrayList pParameters) 
			throws DataAccessException {		
		List<DatosGeneradosDTO> listDatosGeneradosDTO=null;
		DatosGeneradosDTO objDatosGeneradosDTO=null;
		CallableStatement stmt=getSQLStoredProcedureGenerico(this.getEntityName(),pParameters);
		
		try{
			stmt.execute();			
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        listDatosGeneradosDTO=new ArrayList<DatosGeneradosDTO>();

	        while (results.next()) {
	        	
	            objDatosGeneradosDTO=new DatosGeneradosDTO();
	            try{
	            	objDatosGeneradosDTO.setValor(results.getString("COLUMNA"));
	            	objDatosGeneradosDTO.setTipoConcepto(results.getString("CONCEPTO"));
	            	objDatosGeneradosDTO.setTc(results.getString("TC"));
	                objDatosGeneradosDTO.setTe(results.getString("TE"));
	                objDatosGeneradosDTO.setFlujo(results.getString("FLUJO"));

	            } catch(Throwable e){
	            	BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
	            }
	            listDatosGeneradosDTO.add(objDatosGeneradosDTO);

	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
		}

		return listDatosGeneradosDTO;
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
		return "PG_CE_REPORTE.SP_SEL_REPORTETOE";
	}




}

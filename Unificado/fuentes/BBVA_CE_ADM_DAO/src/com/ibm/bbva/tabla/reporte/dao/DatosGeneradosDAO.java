package com.ibm.bbva.tabla.reporte.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.openjpa.lib.log.Log;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.PosibleValorDTO;
import com.ibm.bbva.tabla.reporte.dto.DatosGeneradosDTO;

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
		System.out.println("Termino ejecucion de procedimiento");
		try{
			stmt.execute();			
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        listDatosGeneradosDTO=new ArrayList<DatosGeneradosDTO>();
	        System.out.println("Se inicia recorrido de valores");
	        while (results.next()) {
	        	
	            objDatosGeneradosDTO=new DatosGeneradosDTO();
	            try{
	            	objDatosGeneradosDTO.setValor(results.getString("COLUMNA"));
	            	objDatosGeneradosDTO.setTipoConcepto(results.getString("CONCEPTO"));
	            	objDatosGeneradosDTO.setTc(results.getString("TC")==null?"":results.getString("TC"));
	                objDatosGeneradosDTO.setTe(results.getString("TE")==null?"":results.getString("TE"));
	                objDatosGeneradosDTO.setFlujo(results.getString("FLUJO")==null?"":results.getString("FLUJO"));

	            } catch(Throwable e){
	            	BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
	            }
	            if(objDatosGeneradosDTO!=null)
		            System.out.println("item : "+objDatosGeneradosDTO.getValor()+" - "+objDatosGeneradosDTO.getTipoConcepto()+" - "+objDatosGeneradosDTO.getTc()+" - "+
		            		objDatosGeneradosDTO.getTe()+ " - "+objDatosGeneradosDTO.getFlujo());
	            
	            listDatosGeneradosDTO.add(objDatosGeneradosDTO);

	        }

	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			System.out.println("SQLException "+e.getMessage());
			
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
		} catch (Exception e){
			System.out.println("SQLException "+e.getMessage());
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

	/*@Override
	protected String getEntityName() {
		return "PG_CE_REPORTE.SP_SEL_REPORTETOE";
	}*/

	@Override
    protected String getEntityName() {
        return "CONELE.PG_CE_REPORTE.SP_SEL_REPORTETOE";
    }

}

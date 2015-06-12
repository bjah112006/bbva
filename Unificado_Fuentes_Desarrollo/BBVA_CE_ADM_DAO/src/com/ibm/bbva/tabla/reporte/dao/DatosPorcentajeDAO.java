package com.ibm.bbva.tabla.reporte.dao;

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
import com.ibm.bbva.tabla.reporte.dto.DatosPorcentajeDTO;

public class DatosPorcentajeDAO extends BaseDAO{
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosPorcentajeDAO.class);

	private static DatosPorcentajeDAO instance = null;
	
	private DatosPorcentajeDAO() {
	}
	
	public static DatosPorcentajeDAO getInstance() {
		if ( instance==null )
			instance = new DatosPorcentajeDAO();
		return instance;
	}
	
	public List<DatosPorcentajeDTO> generarDatosPorcentaje(ArrayList pParameters) throws DataAccessException {		
		List<DatosPorcentajeDTO> listDatosPorcentajeDTO=null;
		DatosPorcentajeDTO objDatosPorcentajeDTO=null;
		

		CallableStatement stmt=getSQLStoredProcedureGenerico(this.getEntityName(),pParameters);
		LOG.info("Termino ejecucion de procedimiento porcentaje");
		try{
			stmt.execute();		
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        listDatosPorcentajeDTO=new ArrayList<DatosPorcentajeDTO>();
	        
	        LOG.info("Se inicia recorrido de valores porcentaje");
	        while (results.next()) {
	        	
	            objDatosPorcentajeDTO=new DatosPorcentajeDTO();
	            try{
	            	String sTipoReport=pParameters.get(8).toString(); // TIPO REPORTE
	            	LOG.info("sTipoReport = "+sTipoReport);
	            	
	            	if(sTipoReport.trim().equals("0")){
	            		objDatosPorcentajeDTO.setValorRol(results.getString("PERFIL"));
		            	objDatosPorcentajeDTO.setValorFlujo(results.getString("FLUJO"));
		            	objDatosPorcentajeDTO.setValorPorcentaje(results.getString("PORCENTAJECPM"));
	            	}else{
	            		objDatosPorcentajeDTO.setValorRol(results.getString("PERFIL"));
		            	objDatosPorcentajeDTO.setValorPorcentaje(results.getString("PORCENTAJECPM"));	            		
	            	}            	

		            if(objDatosPorcentajeDTO!=null)
			            LOG.info("item : "+objDatosPorcentajeDTO.getValorRol()+" - "+objDatosPorcentajeDTO.getValorFlujo()+" - "+objDatosPorcentajeDTO.getValorPorcentaje());
		            else
		            	LOG.info("Objeto vacío");
		            
	            } catch(Throwable t){
	            	
	            }
	            listDatosPorcentajeDTO.add(objDatosPorcentajeDTO);
	            LOG.info("tamaño listDatosPorcentajeDTO = "+listDatosPorcentajeDTO.size());
	        }
	        
	        results.close();
	        stmt.close();	
	        
	        
		}catch (SQLException e) {
			LOG.info("SQLException "+e.getMessage());
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
		} catch (Exception e){
			LOG.info("SQLException "+e.getMessage());
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
		}

		return listDatosPorcentajeDTO;
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
		return "CONELE.PG_CE_TOE_PORCENTAJE.SP_SEL_TOE_PORCENTAJE";
	}




}

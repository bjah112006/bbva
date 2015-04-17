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
import com.ibm.bbva.tabla.dto.DatosPorcentajeDTO;

public class DatosPorcentajeDAO extends BaseDAO{

	private static DatosPorcentajeDAO instance = null;
	
	private DatosPorcentajeDAO() {
	}
	
	public static DatosPorcentajeDAO getInstance() {
		if (instance == null)
			instance = new DatosPorcentajeDAO();
		return instance;
	}

	public List<DatosPorcentajeDTO> generarDatosPorcentaje(ArrayList pParameters)
			throws DataAccessException {
		List<DatosPorcentajeDTO> listDatosPorcentajeDTO = null;
		DatosPorcentajeDTO objDatosPorcentajeDTO = null;

		CallableStatement stmt = getSQLStoredProcedureGenerico(
				this.getEntityName(), pParameters);

		try {
			stmt.execute();
			ResultSet results = (ResultSet) stmt.getObject(1);
			listDatosPorcentajeDTO = new ArrayList<DatosPorcentajeDTO>();

			while (results.next()) {

				objDatosPorcentajeDTO = new DatosPorcentajeDTO();
				try {
					objDatosPorcentajeDTO.setValorRol(results
							.getString("PERFIL"));
					objDatosPorcentajeDTO.setValorPorcentaje(results
							.getString("PORCENTAJECPM"));

				} catch (Throwable t) {

				}
				listDatosPorcentajeDTO.add(objDatosPorcentajeDTO);

			}

			results.close();
			stmt.close();

		} catch (SQLException e) {
			BaseLogger
					.log(this.getClass(),
							"executeSQLStoredProcedure",
							BaseLogger.Level.ERROR,
							"Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): "
									+ e.getMessage());
		} catch (Exception e) {
			BaseLogger
					.log(this.getClass(),
							"executeSQLStoredProcedure",
							BaseLogger.Level.ERROR,
							"Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): "
									+ e.getMessage());
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
		return "CONELE.CONELE.PG_CE_TOE_PORCENTAJE.SP_SEL_TOE_PORCENTAJE";
	}

}

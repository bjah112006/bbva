package com.ibm.as.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.GenericException;
import com.ibm.as.core.util.log.BaseLogger;

/**
 * @author root
 *
 */
public abstract class GenericDAO {
	
	public <T extends AbstractDTO> List<T> executeQuery(String query) throws DataAccessException {
		return executeQuery( getPreparedStatement(query) );
	}
	
	/**
	 * 
	 * @param <T>
	 * @param ps
	 * @return
	 * @throws DataAccessException
	 */
	public <T extends AbstractDTO> List<T> executeQuery(PreparedStatement ps) throws DataAccessException {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			return processResult(rs);
		}catch (Exception e) {
			throw new DataAccessException(e);
		}finally {		
			try {
				if (rs!=null){
					rs.close();
				}
			}catch (Exception e) {
				BaseLogger.log(this.getClass(), "executeQuery", BaseLogger.Level.ERROR, "Problema Cerrando el resultSet" + e.getMessage());
			}
			this.closeConnectionFromStatement(ps);
		}
	}
	
	/**
	 * 
	 * @param <T>
	 * @param ps
	 * @return
	 * @throws DataAccessException
	 */
	public String executeQueryReturnString(PreparedStatement ps) throws DataAccessException {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			return processResultReturnString(rs);
		}catch (Exception e) {
			throw new DataAccessException(e);
		}finally {		
			try {
				if (rs!=null){
					rs.close();
				}
			}catch (Exception e) {
				BaseLogger.log(this.getClass(), "executeQuery", BaseLogger.Level.ERROR, "Problema Cerrando el resultSet" + e.getMessage());
			}
			this.closeConnectionFromStatement(ps);
		}
	}
	
	
	/**
	 * 
	 * @param <T>
	 * @param ps
	 * @return
	 * @throws DataAccessException
	 */
	public int executeQueryInt(PreparedStatement ps) throws DataAccessException {
		ResultSet rs = null;
		int answer = 0;
		try {
			rs = ps.executeQuery();
			while ( rs.next() ) {
				answer = rs.getInt("PESOTOTAL")==0 && rs.wasNull()? null : rs.getInt("PESOTOTAL");
			}
			
			return answer;
		}catch (Exception e) {
			throw new DataAccessException(e);
		}finally {		
			try {
				if (rs!=null){
					rs.close();
				}
			}catch (Exception e) {
				BaseLogger.log(this.getClass(), "executeQuery", BaseLogger.Level.ERROR, "Problema Cerrando el resultSet" + e.getMessage());
			}
			this.closeConnectionFromStatement(ps);
		}
	}
	
	
	/**
	 * @param <T>: Lista de los DTO<T> seg�n se haya ejecutado en la consulta.
	 * @param idQuery: Identificador interno del query que ser�a ejecutado. 
	 * Este indicador se propaga el m�todo processResult(...) que genera los DTO seg�n la consulta.
	 * Si no es requerido puede ser enviado como nulo (null).
	 * @param ps: Sentencia a ejecutar.
	 * @return
	 * @throws DataAccessException
	 */
	public <T extends AbstractDTO> List<T> executeQuery(Integer idQuery, PreparedStatement ps) throws DataAccessException {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			return processResult(idQuery, rs);
		}catch (Exception e) {
			throw new DataAccessException(e);
		}finally {
			try {
				if (rs!=null){
					rs.close();
				}
			}catch (Exception e) {
				BaseLogger.log(this.getClass(), "executeQuery", BaseLogger.Level.ERROR, "Problema Cerrando el resultSet" + e.getMessage());
			}
			this.closeConnectionFromStatement(ps);
		}
	}
	
	public <T extends AbstractDTO> List<T> executeQueryBasic(PreparedStatement ps) throws DataAccessException {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			return processResult(rs);
		}catch (Exception e) {
			throw new DataAccessException(e);
		}
//		finally {		
//			try {
//				if (rs!=null){
//					rs.close();
//				}
//			}catch (Exception e) {
//				BaseLogger.log(this.getClass(), "executeQuery", BaseLogger.Level.ERROR, "Problema Cerrando el resultSet" + e.getMessage());
//			}
//			this.closeConnectionFromStatement(ps);
//		}
	}
	
	public PreparedStatement getPreparedStatement(String query) throws DataAccessException {
		return getPreparedStatement(query, null);
	}

	public PreparedStatement getPreparedStatement(String query, String[] columnNames) throws DataAccessException {
		Connection jdbcConnection = null;
		PreparedStatement ps = null;

		try {
			jdbcConnection = getConnection();
		} catch (GenericException e) {
			BaseLogger.log(this.getClass(), "getPreparedStatement", BaseLogger.Level.ERROR, "Error GenericException obteniendo la conexion: " + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "getPreparedStatement", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion: " + e.getMessage());
		}
		
		if( jdbcConnection != null ){
			if ( columnNames==null ){
				try {
					ps = jdbcConnection.prepareStatement(query);
				} catch (SQLException e) {
					BaseLogger.log(this.getClass(), "getPreparedStatement", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para prepareStatement(query): " + e.getMessage());
				} catch (Exception e){
					BaseLogger.log(this.getClass(), "getPreparedStatement", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para prepareStatement(query): " + e.getMessage());
				}
			}else{
				try {
					ps = jdbcConnection.prepareStatement(query, columnNames);
				} catch (SQLException e) {
					BaseLogger.log(this.getClass(), "getPreparedStatement", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para prepareStatement(query, columnNames): " + e.getMessage());
				} catch (Exception e){
					BaseLogger.log(this.getClass(), "getPreparedStatement", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para prepareStatement(query, columnNames): " + e.getMessage());
				}
			}
		}
		return ps;
	}

	public void executeUpdate(PreparedStatement ps) throws DataAccessException {
		try {
			ps.executeUpdate();
		}catch (Exception e) {
			throw new DataAccessException(e);
		}finally{
			this.closeConnectionFromStatement(ps);
		}
	}

	public long executeUpdateAndReturnKeyAsLong(PreparedStatement ps) throws DataAccessException{
		long key = 0;
		try {
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				key =  rs.getLong(1);
			}			
		}catch (Exception e) {
			throw new DataAccessException(e);
		}finally{
			this.closeConnectionFromStatement(ps);
		}
		return key;
	}

	public int executeUpdateAndReturnKeyAsInt(PreparedStatement ps) throws DataAccessException{
		int key = 0;
		try {
			//BaseLogger.log(this.getClass(), "executeUpdateAndReturnKeyAsInt", BaseLogger.Level.DEBUG, "Antes de executeUpdate");
			ps.executeUpdate();
			//BaseLogger.log(this.getClass(), "executeUpdateAndReturnKeyAsInt", BaseLogger.Level.DEBUG, "Despues de executeUpdate con ps = " + ps);
			ResultSet rs = ps.getGeneratedKeys();
			//BaseLogger.log(this.getClass(), "executeUpdateAndReturnKeyAsInt", BaseLogger.Level.DEBUG, "Despues de getGeneratedKeys con rs = " + rs);
			if (rs.next()) {
				key =  rs.getInt(1);
			}			
		}catch (Exception e) {
			throw new DataAccessException(e);
		}finally{
			this.closeConnectionFromStatement(ps);			
		}
		return key;
	}

	public String executeUpdateAndReturnKeyAsString(PreparedStatement ps) throws DataAccessException{
		String key = null;
		try {
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				key =  new String(rs.getString(1));
			}			
		}catch (Exception e) {
			throw new DataAccessException(e);
		}finally{
			this.closeConnectionFromStatement(ps);
		}
		return key;	
	}

	protected abstract Connection getConnection() throws GenericException;
	
	protected abstract void refreshConnection() throws GenericException;

	protected abstract <T extends AbstractDTO> List<T> processResult(ResultSet rs) throws DataAccessException;
	
	protected abstract String processResultReturnString(ResultSet rs) throws DataAccessException;
	

	protected <T extends AbstractDTO> List<T> processResult(Integer idQuery, ResultSet rs) throws DataAccessException{
		return processResult(rs);
	}
	
	protected abstract String getSchema();

	protected abstract String getEntityName();

	protected String getEntityQName() {
		return ((getSchema()==null || getSchema().length()==0)? 
							"" : getSchema()+".")+
				getEntityName();
	}
	
	protected String getQName(String objectName) {
		return ((getSchema()==null || getSchema().length()==0)? 
							"" : getSchema()+".")+
					objectName;
	}
	
	public void commitChanges() throws DataAccessException {
		try {
			System.out.println(this.getClass().toString() + ".commitChanges() -> before commit.");
			this.getConnection().commit();
			System.out.println(this.getClass().toString() + ".commitChanges() -> after commit.");
		}catch (SQLException e) {
			throw new DataAccessException(e);
		}catch (GenericException e) {
			throw new DataAccessException(e);
		}
	}
	
	/*****************************************************************
	 * Se adiciona la sentencia para cerrar la conexi�n utilizada. 
	 * Se libera el Statement utilizado y la conexi�n asociada a 
	 * este statement.
	 * AUTOR: Hern�n Dario F�quene M
	 * FECHA: 2010/04/27
	 *****************************************************************/
	public void closeConnectionFromStatement(Statement ps){
		if (ps != null){
			Connection conn = null;
			try{
				conn = ps.getConnection();
			}catch(SQLException e){
				BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.DEBUG, "Error obteniendo la conexi�n del Statement");
			}
			
			try{
				//BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.DEBUG, "Iniciando Cierre del Statement");
				ps.close();
				//BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.DEBUG, "Fin del Cierre del Statement");
			}catch(SQLException e){
				BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.ERROR, "Problema Cerrando el Statement" + e.getMessage());
			}
			
			if (conn != null){
				try{
					//BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.DEBUG, "Iniciando Cierre de la conexi�n");
					conn.close();
					//BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.DEBUG, "Fin del Cierre de la conexi�n");
				}catch(SQLException e){
					BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.ERROR, "Problema Cerrando la conexi�n" + e.getMessage());
				}
			}
		}
	}
	
	public Long nextVal (String nombSeq) throws DataAccessException {
		try {
			PreparedStatement statement = getPreparedStatement("select "+nombSeq+".nextval from dual");
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw new DataAccessException (e);
		}
		return null;
	}
	
	public Integer nextValInteger (String nombSeq) throws DataAccessException {
		try {
			PreparedStatement statement = getPreparedStatement("select "+nombSeq+".nextval from dual");
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new DataAccessException (e);
		}
		return null;
	}
}

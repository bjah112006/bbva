package com.ibm.bbva.tabla.core.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.GenericException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.bbva.tabla.core.constante.Constante;

/**
 * @author root
 *
 */
public abstract class GenericDAO {
	
	Connection jdbcConnectionCS = null;
	
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
	 * @param <T>: Lista de los DTO<T> según se haya ejecutado en la consulta.
	 * @param idQuery: Identificador interno del query que sería ejecutado. 
	 * Este indicador se propaga el método processResult(...) que genera los DTO según la consulta.
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
			
			//System.out.println("jdbcConnection : "+jdbcConnection);
			
			jdbcConnection.setAutoCommit(true);
			//jdbcConnection = getNaturalConnection();
			//jdbcConnection = getDataSourceConnection();
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

	public Connection getNaturalConnection(){
		Connection result = null;
		String DB_CONN_STRING = "jdbc:oracle:thin:@//:1521/xe";
	    //Provided by your driver documentation. In this case, a MySql driver is used : 
	    String DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
	    String USER_NAME = "APP_CONELE";
	    String PASSWORD = "p4ssw0rd";
	    
	    try {
	        Class.forName(DRIVER_CLASS_NAME).newInstance();
	     }
	     catch (Exception ex){
	        ex.printStackTrace();
	     }

	     try {
	       result = DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
	     }
	     catch (SQLException e){
	    	 e.printStackTrace();
	     }
	     return result;	   

	}
	
	
	
	public Connection getDataSourceConnection(){
	    Connection result = null;	     	    
	    try {
	    	InitialContext ic = new InitialContext();	
	    	DataSource dsConnect = (DataSource)ic.lookup("jdbc/BBVA"); 
	    	System.out.println("dsConnect : "+dsConnect);
			result = dsConnect.getConnection();
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
		}				
		return result;
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
	
	protected String getQJoinTables(String table1, String table2, String column1, String column2) {
		String joinTables;
		if(getSchema()==null || getSchema().length()==0){
			joinTables = "";
		}else{
			joinTables = getSchema()+"."+table1+" INNER JOIN "+getSchema()+"."+table2+" ON ("+getSchema()+"."+table1+"."+column1+" = "+getSchema()+"."+table2+"."+column2+")";
		}
		return joinTables;
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
	 * Se adiciona la sentencia para cerrar la conexión utilizada. 
	 * Se libera el Statement utilizado y la conexión asociada a 
	 * este statement.
	 * AUTOR: Hernán Dario Fúquene M
	 * FECHA: 2010/04/27
	 *****************************************************************/
	public void closeConnectionFromStatement(Statement ps){
		if (ps != null){
			Connection conn = null;
			try{
				conn = ps.getConnection();
			}catch(SQLException e){
				BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.DEBUG, "Error obteniendo la conexión del Statement");
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
					//BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.DEBUG, "Iniciando Cierre de la conexión");
					conn.close();
					//BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.DEBUG, "Fin del Cierre de la conexión");
				}catch(SQLException e){
					BaseLogger.log(this.getClass(), "closeConnectionFromStatement", BaseLogger.Level.ERROR, "Problema Cerrando la conexión" + e.getMessage());
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
    

    public CallableStatement getSQLStoredProcedureGenerico(String pStoredProcedure,ArrayList pParameters)
		    		throws DataAccessException {

		CallableStatement stmt=null;
		int numeroParametro = 1;
		String paramStore ="?,";
		
		try {
			if(jdbcConnectionCS==null || jdbcConnectionCS.isClosed()){
				System.out.println("jdbcConnection es nulo");
				try {
					System.out.println("getConnection");
					jdbcConnectionCS = getConnection();
					System.out.println("setAutoCommit");
					jdbcConnectionCS.setAutoCommit(true);
			
				} catch (Exception e){
					System.out.println("executeSQLStoredProcedure Error Exception obteniendo la conexion: " + e.getMessage());
					//BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion: " + e.getMessage());
				}			
			}
		} catch (SQLException e1) {
			// TODO Bloque catch generado automáticamente
			e1.printStackTrace();
		}
				
		if( jdbcConnectionCS != null ){
			try {
				for(int i = 0; i < pParameters.size(); i++){
					paramStore = paramStore + "?,";
				}
				paramStore = paramStore.substring(0, paramStore.length()-1);
				stmt = jdbcConnectionCS.prepareCall("{ call "+pStoredProcedure+"("+paramStore+")}");
				System.out.println("stmt: " + "{ call "+pStoredProcedure+"("+paramStore+")}");
				//stmt = jdbcConnectionCS.prepareCall("{ call "+pStoredProcedure+"(?,?,?,?,?,?,?,?,?,?)}");
				stmt.registerOutParameter(1, Constante.ORACLE_TYPES_CURSOR.getValueInt());
				numeroParametro = 2;
				
				for (int i = 0; i < pParameters.size(); i++) {
					System.out.println("pParameters.get(i):" + pParameters.get(i));
					if (pParameters.get(i) instanceof String){
					        stmt.setString(numeroParametro, (String) pParameters.get(i));
					}
					    
				    if (pParameters.get(i) instanceof Long){
				        stmt.setLong(numeroParametro, Long.
				                parseLong(((Long) pParameters.get(i)).toString()));
				    }
				    
	                if (pParameters.get(i) instanceof Integer){
	                    stmt.setInt(numeroParametro, Integer
                                .parseInt(((Integer) pParameters.get(i)).toString()));	                	
	                }		
	                
				    if (pParameters.get(i) instanceof Date){
				    	Date date=(Date)pParameters.get(i);
				    	java.sql.Date mydate=new java.sql.Date(Long.
				                parseLong(((Long) date.getTime()).toString()));
				        stmt.setDate(numeroParametro, mydate);
				        System.out.println("getTime:" + ((Long) date.getTime()).toString());
				        System.out.println("mydate:" + mydate);
				    }
				    
				    numeroParametro += 1;
				    
				    
				}
				
				System.out.println("fin pasar parametros");
			
				//jdbcConnectionCS.close();
				
			}  	catch (SQLException e) {
				BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException , executeSQLStoredProcedure(query): " + e.getMessage());
			} catch (Exception e){
				BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception , executeSQLStoredProcedure(query): " + e.getMessage());
			}
		}

		return stmt;
		
		}
 
	
}
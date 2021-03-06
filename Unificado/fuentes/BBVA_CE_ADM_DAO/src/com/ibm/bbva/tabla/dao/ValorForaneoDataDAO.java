package com.ibm.bbva.tabla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;


import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.ValorForaneoDataDTO;
import com.ibm.bbva.tabla.service.Constantes;
import com.ibm.as.core.exception.DataAccessException;

public class ValorForaneoDataDAO extends BaseDAO {

	private static ValorForaneoDataDAO instance = null;
	
	public static ValorForaneoDataDAO getInstance() {
		if ( instance==null )
			instance = new ValorForaneoDataDAO();
		return instance;
	}
	@Override
	protected String getEntityName() {
		return null;
	}

	public List<ValorForaneoDataDTO> getValoresForaneos(
			String nombreTabla,String nombreColumna,
			String nombreValor)
			throws DataAccessException {

		String where = "";
		if (nombreTabla.equals("TBL_CE_IBM_PERFIL_CE")) {
			where = " FLAG_PROCESO='1'";
		}		
		if (nombreTabla.equals("TBL_CE_IBM_TAREA")) {
			where = " ID <> 26";
		}
		
		String query = "SELECT " + nombreColumna+","+nombreValor+ " FROM " +getSchema()+"."+nombreTabla+(where.length()==0? "" : " WHERE "+where);
		//String query = "SELECT " + nombreColumna+","+nombreValor+ " FROM " +getSchema()+"."+nombreTabla;
		
		PreparedStatement ps = getPreparedStatement(query);
		
		return executeQuery(ps);
		
		
	}
	
	public List<ValorForaneoDataDTO> getValoresForaneosConcat(
			String nombreTabla,String nombreColumna,
			String nombreValor)
			throws DataAccessException {

		if (nombreColumna.contains("-")){
			String campo1 = nombreColumna.substring(0, nombreColumna.indexOf("-"));
			String campo2 = nombreColumna.substring(nombreColumna.indexOf("-")+1,nombreColumna.length());
			nombreColumna = "CONCAT(CONCAT("+campo1+",' - '),"+campo2+") AS " +campo2;
		}
		System.out.println("nombreColumna: " + nombreColumna);
		String query = "SELECT " + nombreColumna+","+nombreValor+ " FROM " +getSchema()+"."+nombreTabla;
		System.out.println("query: " + query);
		PreparedStatement ps = getPreparedStatement(query);
		
		return executeQuery(ps);
		
		
	}
	
	public ValorForaneoDataDTO getValorById(
	String nombreTabla,String nombreColumna,	
	String nombreValor,
	Integer valor)
	throws DataAccessException{
		
		List<Object> filterAtts = new ArrayList<Object>();
		String query = "SELECT " + nombreColumna+","+nombreValor+" FROM " +getSchema()+"."+nombreTabla+
		" WHERE " +nombreValor+"=?";

		filterAtts.add(valor);
		PreparedStatement ps = getPreparedStatement(query);
		
		try {
			for (int i=0; i<filterAtts.size(); ++i) {
				ps.setObject(i+1, filterAtts.get(i));				
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return (ValorForaneoDataDTO) executeQuery(ps).get(0);
		
	}
	
	public ValorForaneoDataDTO getValorByIdConcat(
			String nombreTabla,String nombreColumna,	
			String nombreValor,
			Integer valor)
			throws DataAccessException{
				if (nombreColumna.contains("-")){
					String campo1 = nombreColumna.substring(0, nombreColumna.indexOf("-"));
					String campo2 = nombreColumna.substring(nombreColumna.indexOf("-")+1,nombreColumna.length());
					nombreColumna = "CONCAT(CONCAT("+campo1+",' - '),"+campo2+") AS " +campo2;
				}
				List<Object> filterAtts = new ArrayList<Object>();
				String query = "SELECT " + nombreColumna+","+nombreValor+" FROM " +getSchema()+"."+nombreTabla+
				" WHERE " +nombreValor+"=?";
				System.out.print("query" + query);
				filterAtts.add(valor);
				PreparedStatement ps = getPreparedStatement(query);
				
				try {
					for (int i=0; i<filterAtts.size(); ++i) {
						ps.setObject(i+1, filterAtts.get(i));				
					}
				}
				catch (SQLException e) {
					throw new DataAccessException(e);
				}
				return (ValorForaneoDataDTO) executeQuery(ps).get(0);
				
			}
	
	@Override
	protected List<ValorForaneoDataDTO> processResult(ResultSet rs) throws DataAccessException{
		List<ValorForaneoDataDTO> list = new ArrayList<ValorForaneoDataDTO>();
	try{
		while (rs.next()) {
			
				ValorForaneoDataDTO dto=new ValorForaneoDataDTO();
				dto.setNombre(rs.getString(1));
				dto.setValor(rs.getInt(2));
				list.add(dto);
			}
	} catch (SQLException e) {
	throw new DataAccessException(e);
	}
		return list;
	}
	
	public String getSchema()
	{
		return Constantes.SCHEMA_TABLAS_MANTENIMIENTO;
	}

	@Override
	protected String processResultReturnString(ResultSet rs)
			throws DataAccessException {
		// TODO Apéndice de método generado automáticamente
		return "";
	}
}

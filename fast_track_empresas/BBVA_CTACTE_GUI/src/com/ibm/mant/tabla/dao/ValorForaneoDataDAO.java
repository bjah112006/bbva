package com.ibm.mant.tabla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.mant.tabla.core.dao.GenericDAO;
import com.ibm.mant.tabla.dto.ValorForaneoDataDTO;

public class ValorForaneoDataDAO extends GenericDAO {

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

		
		String query = "SELECT " + nombreColumna+","+nombreValor+ " FROM " +getSchema()+"."+nombreTabla;
		
		
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
		return ConstantesAdmin.NOMBRE_ESQUEMA_MANTENIMIENTO;
	}

	@Override
	protected String processResultReturnString(ResultSet rs)
			throws DataAccessException {
		// TODO Apéndice de método generado automáticamente
		return "";
	}
}

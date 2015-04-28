package com.ibm.mant.tabla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.ObjectNotFoundException;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.mant.tabla.core.dao.GenericDAO;
import com.ibm.mant.tabla.dto.ValorForaneoDTO;

public class ValorForaneoDAO extends GenericDAO{

	private static ValorForaneoDAO instance = null;
	
	public static ValorForaneoDAO getInstance() {
		if ( instance==null )
			instance = new ValorForaneoDAO();
		return instance;
	}
	
	protected String getEntityName() {
		//return "TBL_CE_IBM_VALORES_FORANEOS";
		return ConstantesAdmin.NOMBRETABLA_VALORFORANEO_MANTENIMIENTO;
	}

	

	
	public ValorForaneoDTO findById(Integer id) throws DataAccessException, ObjectNotFoundException {
		ValorForaneoDTO dto = new ValorForaneoDTO();
		dto.setIdColumna(id);

		List<ValorForaneoDTO> result = this.read(dto);

		if ( result.isEmpty() )
			throw new ObjectNotFoundException();

		return result.get(0);
	}
	
	private List<ValorForaneoDTO> read(ValorForaneoDTO filter) throws DataAccessException {
		String where = "";
		List<Object> filterAtts = new ArrayList<Object>();

		if ( filter.getId()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id=?";
			filterAtts.add( filter.getId() );
		}
		if ( filter.getIdColumna()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Id_Columna=?";
			filterAtts.add( filter.getIdColumna());
		}
		
		if ( filter.getNombreTabla()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre_Tabla=?";
			filterAtts.add( filter.getNombreTabla() );
		}
		if ( filter.getNombreColumna()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre_Columna=?";
			filterAtts.add( filter.getNombreColumna() );
		}
		if ( filter.getValorColumna()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Valor_Columna=?";
			filterAtts.add( filter.getValorColumna() );
		}
		
		String query = "SELECT id, Id_Columna, Nombre_Tabla, Nombre_Columna, Valor_Columna FROM "+getEntityQName()+(where.length()==0? "" : " WHERE "+where);
		PreparedStatement ps = getPreparedStatement(query);

		try {
			for (int i=0; i<filterAtts.size(); ++i) {
				ps.setObject(i+1, filterAtts.get(i));
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return executeQuery( ps );
	}
	
	protected List<ValorForaneoDTO> processResult(ResultSet rs)
			throws DataAccessException {
		
		List<ValorForaneoDTO> list = new ArrayList<ValorForaneoDTO>();
		try
		{
			while ( rs.next() ) {
				ValorForaneoDTO dto = new ValorForaneoDTO();
				dto.setId( rs.getInt("id")==0 && rs.wasNull()? null : rs.getInt("id") );
				dto.setIdColumna(rs.getInt("id_columna")==0 && rs.wasNull()? null : rs.getInt("id_columna") );
				dto.setNombreTabla( rs.getString("nombre_tabla"));
				dto.setNombreColumna(rs.getString("nombre_columna"));
				dto.setValorColumna( rs.getString("valor_columna"));
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

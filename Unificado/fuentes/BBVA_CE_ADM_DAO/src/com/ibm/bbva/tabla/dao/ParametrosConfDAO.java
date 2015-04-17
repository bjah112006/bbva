package com.ibm.bbva.tabla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.ObjectNotFoundException;
import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.ParametrosConfDTO;
import com.ibm.bbva.tabla.service.Constantes;

public class ParametrosConfDAO extends BaseDAO {

	private static ParametrosConfDAO instance = null;

	private ParametrosConfDAO() {
	}

	public static ParametrosConfDAO getInstance() {
		if ( instance==null )
			instance = new ParametrosConfDAO();
		return instance;
	}

	public List<ParametrosConfDTO> read(ParametrosConfDTO filter) throws DataAccessException {
		String where = "";
		List<Object> filterAtts = new ArrayList<Object>();

		if ( filter.getId()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id=?";
			filterAtts.add( filter.getId() );
		}
		if ( filter.getCodigoAplicativo()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Codigo_Aplicativo=?";
			filterAtts.add( filter.getCodigoAplicativo() );
		}
		if ( filter.getNombreVariable()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre_Variable=?";
			filterAtts.add( filter.getNombreVariable() );
		}
		
		if ( filter.getValorVariable()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Valor_Variable=?";
			filterAtts.add( filter.getValorVariable() );
		}
		
		String query = "SELECT id, Codigo_Aplicativo, Nombre_Variable, Valor_Variable FROM "+getEntityQName()+(where.length()==0? "" : " WHERE "+where);

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
	
	public ParametrosConfDTO findByVariable(Integer codigoAplicativo, String nombreVariable) throws DataAccessException, ObjectNotFoundException {
		ParametrosConfDTO dto = new ParametrosConfDTO();
		dto.setCodigoAplicativo(codigoAplicativo);
		dto.setNombreVariable(nombreVariable);

		List<ParametrosConfDTO> result = this.read(dto);

		if ( result.isEmpty() )
			throw new ObjectNotFoundException();

		return result.get(0);
	}
		
	@SuppressWarnings("unchecked")
	protected List<ParametrosConfDTO> processResult(ResultSet rs) throws DataAccessException {

		List<ParametrosConfDTO> list = new ArrayList<ParametrosConfDTO>();

		try {
			while ( rs.next() ) {
				ParametrosConfDTO dto = new ParametrosConfDTO();
				dto.setId( rs.getInt("id")==0 && rs.wasNull()? null : rs.getInt("id") );
				dto.setCodigoAplicativo(rs.getInt("Codigo_Aplicativo") );
				dto.setNombreVariable( rs.getString("Nombre_Variable") );				
				dto.setValorVariable( rs.getString("Valor_Variable"));				
				
				list.add( dto );
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return list;
	}

	protected String getEntityName() {
		return "TBL_CE_IBM_PARAMETROS_CONF";
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

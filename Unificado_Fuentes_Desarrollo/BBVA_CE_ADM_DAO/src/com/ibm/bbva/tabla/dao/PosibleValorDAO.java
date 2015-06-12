package com.ibm.bbva.tabla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.PosibleValorDTO;
import com.ibm.bbva.tabla.service.Constantes;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.ObjectNotFoundException;



public class PosibleValorDAO extends BaseDAO {

	private static PosibleValorDAO instance = null;

	private PosibleValorDAO() {
	}

	public static PosibleValorDAO getInstance() {
		if ( instance==null )
			instance = new PosibleValorDAO();
		return instance;
	}

	public List<PosibleValorDTO> read(PosibleValorDTO filter) throws DataAccessException {
		String where = "";
		List<Object> filterAtts = new ArrayList<Object>();

		if ( filter.getId()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id=?";
			filterAtts.add( filter.getId() );
		}
		if ( filter.getValor()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Valor=?";
			filterAtts.add( filter.getValor() );
		}
		if ( filter.getIdColumna()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id_Columna=?";
			filterAtts.add( filter.getIdColumna() );
		}
		if ( filter.getEtiqueta()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Etiqueta=?";
			filterAtts.add( filter.getEtiqueta() );
		}
		String query = "SELECT id, Valor, id_Columna, Etiqueta FROM "+getEntityQName()+(where.length()==0? "" : " WHERE "+where);

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
	public PosibleValorDTO findById(Integer id) throws DataAccessException, ObjectNotFoundException {
		PosibleValorDTO dto = new PosibleValorDTO();
		dto.setId( id );

		List<PosibleValorDTO> result = this.read(dto);

		if ( result.isEmpty() )
			throw new ObjectNotFoundException();

		return result.get(0);
	}

	public void update(PosibleValorDTO dto) throws DataAccessException {
		PosibleValorDTO keyDTO = new PosibleValorDTO();
		keyDTO.setId( dto.getId() );

		update(dto, keyDTO);
	}

	public void update(PosibleValorDTO dto, PosibleValorDTO filter) throws DataAccessException {
		String where = "";
		List<Object> filterAtts = new ArrayList<Object>();

		if ( filter!=null ) {
			if ( filter.getId()!=null ) {
				where += (where.length()==0? "" : " AND ")+"id=?";
				filterAtts.add( filter.getId() );
			}
			if ( filter.getValor()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Valor=?";
				filterAtts.add( filter.getValor() );
			}
			if ( filter.getIdColumna()!=null ) {
				where += (where.length()==0? "" : " AND ")+"id_Columna=?";
				filterAtts.add( filter.getIdColumna() );
			}
			if ( filter.getEtiqueta()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Etiqueta=?";
				filterAtts.add( filter.getEtiqueta() );
			}
		}

		String query = "UPDATE "+getEntityQName()+" SET id=?, Valor=?, id_Columna=?, Etiqueta=?"+(where.length()==0? "" : " WHERE "+where );

		PreparedStatement ps = getPreparedStatement(query);

		try {
			ps.setInt(1, dto.getId() );
			ps.setString(2, dto.getValor() );
			ps.setInt(3, dto.getIdColumna() );
			ps.setString(4, dto.getEtiqueta() );
			for (int i=0; i<filterAtts.size(); ++i) {
				ps.setObject(5+i, filterAtts.get(i));
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		executeUpdate(ps);
	}

	public PosibleValorDTO create(PosibleValorDTO dto) throws DataAccessException {

		String query = "INSERT INTO "+getEntityQName()+"(id, Valor, id_Columna, Etiqueta) VALUES ("+getQName("seq_Posible_Valor")+".nextval, ?, ?, ?)";

		PreparedStatement ps = getPreparedStatement(query, new String[] {"id"});

		try {
			ps.setString(1, dto.getValor() );
			ps.setInt(2, dto.getIdColumna() );
			ps.setString(3, dto.getEtiqueta() );

			int key = executeUpdateAndReturnKeyAsInt(ps);
			dto.setId( key );
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return dto;
	}

	public void delete(PosibleValorDTO dto) throws DataAccessException {
		String where = "";
		List<Object> filterAtts = new ArrayList<Object>();

		if ( dto.getId()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id=?";
			filterAtts.add( dto.getId() );
		}
		if ( dto.getValor()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Valor=?";
			filterAtts.add( dto.getValor() );
		}
		if ( dto.getIdColumna()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id_Columna=?";
			filterAtts.add( dto.getIdColumna() );
		}
		if ( dto.getEtiqueta()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Etiqueta=?";
			filterAtts.add( dto.getEtiqueta() );
		}

		String query = "DELETE FROM "+getEntityQName()+(where.length()==0? "" : " WHERE "+where);

		PreparedStatement ps = getPreparedStatement(query);

		try {
			for (int i=0; i<filterAtts.size(); ++i) {
				ps.setObject(i+1, filterAtts.get(i));
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		executeUpdate(ps);
	}

	@SuppressWarnings("unchecked")
	protected List<PosibleValorDTO> processResult(ResultSet rs) throws DataAccessException {

		List<PosibleValorDTO> list = new ArrayList<PosibleValorDTO>();

		try {
			while ( rs.next() ) {
				PosibleValorDTO dto = new PosibleValorDTO();
				dto.setId( rs.getInt("id")==0 && rs.wasNull()? null : rs.getInt("id") );
				dto.setValor( rs.getString("Valor") );
				dto.setIdColumna( rs.getInt("id_Columna")==0 && rs.wasNull()? null : rs.getInt("id_Columna") );
				dto.setEtiqueta( rs.getString("Etiqueta") );

				list.add( dto );
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return list;
	}

	protected String getEntityName() {
		return "TBL_CE_IBM_POSIBLE_VALOR";
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

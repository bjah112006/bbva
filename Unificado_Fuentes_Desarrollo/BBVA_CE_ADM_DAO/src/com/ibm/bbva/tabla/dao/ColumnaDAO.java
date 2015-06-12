package com.ibm.bbva.tabla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.ColumnaDTO;
import com.ibm.bbva.tabla.service.Constantes;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.ObjectNotFoundException;



public class ColumnaDAO extends BaseDAO {

	private static ColumnaDAO instance = null;

	private ColumnaDAO() {
	}

	public static ColumnaDAO getInstance() {
		if ( instance==null )
			instance = new ColumnaDAO();
		return instance;
	}

	public List<ColumnaDTO> read(ColumnaDTO filter) throws DataAccessException {
		String where = "";
		List<Object> filterAtts = new ArrayList<Object>();

		if ( filter.getId()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id=?";
			filterAtts.add( filter.getId() );
		}
		if ( filter.getNombre()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre=?";
			filterAtts.add( filter.getNombre() );
		}
		if ( filter.isEsLlavePrimaria()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Es_Llave_Primaria=?";
			filterAtts.add( (filter.isEsLlavePrimaria()? "1" : "0") );
		}
		if ( filter.getIdTabla()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id_Tabla=?";
			filterAtts.add( filter.getIdTabla() );
		}
		if ( filter.isEsRequerido()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Es_Requerido=?";
			filterAtts.add( (filter.isEsRequerido()? "1" : "0") );
		}
		if ( filter.getLongitudMaxima()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Longitud_Maxima=?";
			filterAtts.add( filter.getLongitudMaxima() );
		}
		if ( filter.getNombreMostrar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre_Mostrar=?";
			filterAtts.add( filter.getNombreMostrar() );
		}
		if ( filter.getTipoDato()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Tipo_Dato=?";
			filterAtts.add( filter.getTipoDato() );
		}
		
		if ( filter.isEsLlaveForanea()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Es_Llave_Foranea=?";
			filterAtts.add( (filter.isEsLlaveForanea()? "1" : "0") );
		}

		if ( filter.getBusqueda()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Busqueda=?";
			filterAtts.add(filter.getBusqueda());
		}

		if ( filter.getPlantilla()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Plantilla=?";
			filterAtts.add(filter.getPlantilla());
		}

		if ( filter.getOrdenColumna()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Orden_Columna=?";
			filterAtts.add(filter.getOrdenColumna());
		}
		
		if ( filter.getFlagDependencia()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Flag_Dependencia=?";
			filterAtts.add(filter.getFlagDependencia());
		}
		
		String query = "SELECT id, Nombre, Es_Llave_Primaria, id_Tabla, Es_Requerido, Longitud_Maxima, Nombre_Mostrar, Tipo_Dato, Es_Llave_Foranea, Busqueda, Plantilla, Orden_Columna, Flag_Dependencia FROM "+getEntityQName()+(where.length()==0? "" : " WHERE "+where);
		query = query + " ORDER BY ORDEN_COLUMNA ASC"; 
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
	public ColumnaDTO findById(Integer id) throws DataAccessException, ObjectNotFoundException {
		ColumnaDTO dto = new ColumnaDTO();
		dto.setId( id );

		List<ColumnaDTO> result = this.read(dto);

		if ( result.isEmpty() )
			throw new ObjectNotFoundException();

		return result.get(0);
	}

	public void update(ColumnaDTO dto) throws DataAccessException {
		ColumnaDTO keyDTO = new ColumnaDTO();
		keyDTO.setId( dto.getId() );

		update(dto, keyDTO);
	}

	public void update(ColumnaDTO dto, ColumnaDTO filter) throws DataAccessException {
		String where = "";
		List<Object> filterAtts = new ArrayList<Object>();

		if ( filter!=null ) {
			if ( filter.getId()!=null ) {
				where += (where.length()==0? "" : " AND ")+"id=?";
				filterAtts.add( filter.getId() );
			}
			if ( filter.getNombre()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Nombre=?";
				filterAtts.add( filter.getNombre() );
			}
			if ( filter.isEsLlavePrimaria()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Es_Llave_Primaria=?";
				filterAtts.add( (filter.isEsLlavePrimaria()? "1" : "0") );
			}
			if ( filter.getIdTabla()!=null ) {
				where += (where.length()==0? "" : " AND ")+"id_Tabla=?";
				filterAtts.add( filter.getIdTabla() );
			}
			if ( filter.isEsRequerido()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Es_Requerido=?";
				filterAtts.add( (filter.isEsRequerido()? "1" : "0") );
			}
			if ( filter.getLongitudMaxima()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Longitud_Maxima=?";
				filterAtts.add( filter.getLongitudMaxima() );
			}
			if ( filter.getNombreMostrar()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Nombre_Mostrar=?";
				filterAtts.add( filter.getNombreMostrar() );
			}
			if ( filter.getTipoDato()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Tipo_Dato=?";
				filterAtts.add( filter.getTipoDato() );
			}
			
			if ( filter.isEsLlaveForanea()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Es_Llave_Foranea=?";
				filterAtts.add( (filter.isEsLlaveForanea()? "1" : "0") );
			}
			
			if ( filter.getBusqueda()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Busqueda=?";
				filterAtts.add( (filter.getBusqueda()) );
			}			
			
			if ( filter.getPlantilla()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Plantilla=?";
				filterAtts.add( (filter.getPlantilla()) );
			}	
			
			if ( filter.getOrdenColumna()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Orden_Columna=?";
				filterAtts.add( (filter.getOrdenColumna()) );
			}
			
			if ( filter.getFlagDependencia()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Flag_Dependencia=?";
				filterAtts.add( (filter.getFlagDependencia()) );
			}			
		}

		String query = "UPDATE "+getEntityQName()+" SET id=?, Nombre=?, Es_Llave_Primaria=?, id_Tabla=?, Es_Requerido=?, Longitud_Maxima=?, Nombre_Mostrar=?, Tipo_Dato=?, Es_Llave_Foranea=?, Busqueda=?, Plantilla=?, Orden_Columna=?, Flag_Dependencia=? "+(where.length()==0? "" : " WHERE "+where );

		PreparedStatement ps = getPreparedStatement(query);

		try {
			ps.setInt(1, dto.getId() );
			ps.setString(2, dto.getNombre() );
			ps.setString(3, dto.isEsLlavePrimaria()==null? null : (dto.isEsLlavePrimaria()? "1" : "0") );
			ps.setInt(4, dto.getIdTabla() );
			ps.setString(5, dto.isEsRequerido()==null? null : (dto.isEsRequerido()? "1" : "0") );
			ps.setInt(6, dto.getLongitudMaxima() );
			ps.setString(7, dto.getNombreMostrar() );
			ps.setString(8, dto.getTipoDato() );
			ps.setString(9, dto.getBusqueda() );
			ps.setString(10, dto.getPlantilla() );
			ps.setString(11, dto.getOrdenColumna() );
			ps.setString(12, dto.getFlagDependencia() );
			for (int i=0; i<filterAtts.size(); ++i) {
				ps.setObject(10+i, filterAtts.get(i));
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		executeUpdate(ps);
	}

	public ColumnaDTO create(ColumnaDTO dto) throws DataAccessException {

		String query = "INSERT INTO "+getEntityQName()+"(id, Nombre, Es_Llave_Primaria, id_Tabla, Es_Requerido, Longitud_Maxima, Nombre_Mostrar, Tipo_Dato, Busqueda, Plantilla, Orden_Columna, Flag_Dependencia ) VALUES ("+getQName("seq_Columna")+".nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, )";

		PreparedStatement ps = getPreparedStatement(query, new String[] {"id"});

		try {
			ps.setString(1, dto.getNombre() );
			ps.setString(2, dto.isEsLlavePrimaria()==null? null : (dto.isEsLlavePrimaria()? "1" : "0") );
			ps.setInt(3, dto.getIdTabla() );
			ps.setString(4, dto.isEsRequerido()==null? null : (dto.isEsRequerido()? "1" : "0") );
			ps.setInt(5, dto.getLongitudMaxima() );
			ps.setString(6, dto.getNombreMostrar() );
			ps.setString(7, dto.getTipoDato() );
			ps.setString(8, dto.getBusqueda() );
			ps.setString(9, dto.getPlantilla() );
			ps.setString(10, dto.getOrdenColumna() );
			ps.setString(11, dto.getFlagDependencia() );

			int key = executeUpdateAndReturnKeyAsInt(ps);
			dto.setId( key );
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return dto;
	}

	public void delete(ColumnaDTO dto) throws DataAccessException {
		String where = "";
		List<Object> filterAtts = new ArrayList<Object>();

		if ( dto.getId()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id=?";
			filterAtts.add( dto.getId() );
		}
		if ( dto.getNombre()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre=?";
			filterAtts.add( dto.getNombre() );
		}
		if ( dto.isEsLlavePrimaria()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Es_Llave_Primaria=?";
			filterAtts.add( (dto.isEsLlavePrimaria()? "1" : "0") );
		}
		if ( dto.getIdTabla()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id_Tabla=?";
			filterAtts.add( dto.getIdTabla() );
		}
		if ( dto.isEsRequerido()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Es_Requerido=?";
			filterAtts.add( (dto.isEsRequerido()? "1" : "0") );
		}
		if ( dto.getLongitudMaxima()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Longitud_Maxima=?";
			filterAtts.add( dto.getLongitudMaxima() );
		}
		if ( dto.getNombreMostrar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre_Mostrar=?";
			filterAtts.add( dto.getNombreMostrar() );
		}
		if ( dto.getTipoDato()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Tipo_Dato=?";
			filterAtts.add( dto.getTipoDato() );
		}
		
		if ( dto.isEsLlaveForanea()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Es_Llave_Foranea=?";
			filterAtts.add( (dto.isEsLlaveForanea()? "1" : "0") );
		}

		if ( dto.getBusqueda()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Busqueda=?";
			filterAtts.add(dto.getBusqueda());
		}

		if ( dto.getPlantilla()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Plantilla=?";
			filterAtts.add(dto.getPlantilla());
		}
		
		if ( dto.getOrdenColumna()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Orden_Columna=?";
			filterAtts.add(dto.getOrdenColumna());
		}

		if ( dto.getFlagDependencia()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Flag_Dependencia=?";
			filterAtts.add(dto.getFlagDependencia());
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
	protected List<ColumnaDTO> processResult(ResultSet rs) throws DataAccessException {

		List<ColumnaDTO> list = new ArrayList<ColumnaDTO>();

		try {
			while ( rs.next() ) {
				ColumnaDTO dto = new ColumnaDTO();
				dto.setId( rs.getInt("id")==0 && rs.wasNull()? null : rs.getInt("id") );
				dto.setNombre( rs.getString("Nombre") );
				dto.setEsLlavePrimaria( rs.getString("Es_Llave_Primaria")==null? null : (rs.getString("Es_Llave_Primaria").equals("1")? Boolean.TRUE : Boolean.FALSE) );
				dto.setIdTabla( rs.getInt("id_Tabla")==0 && rs.wasNull()? null : rs.getInt("id_Tabla") );
				dto.setEsRequerido( rs.getString("Es_Requerido")==null? null : (rs.getString("Es_Requerido").equals("1")? Boolean.TRUE : Boolean.FALSE) );
				dto.setLongitudMaxima( rs.getInt("Longitud_Maxima")==0 && rs.wasNull()? null : rs.getInt("Longitud_Maxima") );
				dto.setNombreMostrar( rs.getString("Nombre_Mostrar") );
				dto.setTipoDato( rs.getString("Tipo_Dato") );
				dto.setEsLlaveForanea(rs.getString("Es_Llave_Foranea")==null? null : (rs.getString("Es_Llave_Foranea").equals("1")? Boolean.TRUE : Boolean.FALSE));
				dto.setBusqueda( rs.getString("Busqueda") );
				dto.setPlantilla( rs.getString("Plantilla") );
				dto.setOrdenColumna( rs.getString("Orden_Columna") );
				dto.setFlagDependencia( rs.getString("Flag_Dependencia") );
				list.add( dto );
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return list;
	}

	protected String getEntityName() {
		return "TBL_CE_IBM_COLUMNA";
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

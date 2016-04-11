package com.ibm.mant.tabla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.ObjectNotFoundException;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.mant.tabla.core.dao.GenericDAO;
import com.ibm.mant.tabla.dto.TablaDTO;
//import com.ibm.mant.tabla.service.selectTable;



public class TablaDAO extends GenericDAO {

	private static final Logger LOG = LoggerFactory.getLogger(TablaDAO.class);
	
	private static TablaDAO instance = null;
	//private selectTable selTabla=new selectTable();
	
	private TablaDAO() {
	}

	public static TablaDAO getInstance() {
		if ( instance==null )
			instance = new TablaDAO();
		return instance;
	}

	public List<TablaDTO> read(TablaDTO filter) throws DataAccessException {
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
		if ( filter.getNombreMostrar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre_Mostrar=?";
			filterAtts.add( filter.getNombreMostrar() );
		}
		
		if ( filter.getTipo()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Tipo=?";
			filterAtts.add( filter.getTipo() );
		}

		if ( filter.getBotonAgregar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Boton_Agregar=?";
			filterAtts.add( filter.getBotonAgregar() );
		}
		
		if ( filter.getBotonActualizar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Boton_Actualizar=?";
			filterAtts.add( filter.getBotonActualizar() );
		}		

		if ( filter.getBotonEliminar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Boton_Eliminar=?";
			filterAtts.add( filter.getBotonEliminar() );
		}
		if ( filter.getOrden()!=null ) {
			where += (where.length()==0? "" : " AND ")+"orden=?";
			filterAtts.add( filter.getOrden() );
		}
		String query = "SELECT id, Nombre, Nombre_Mostrar, tipo, boton_agregar, boton_actualizar, boton_eliminar,orden FROM "+getEntityQName()+(where.length()==0? "" : " WHERE "+where) + " ORDER BY Nombre_Mostrar";
		
		LOG.info("Llamar a PreparedStatement en metodo read");
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
	
	public TablaDTO findById(Integer id) throws DataAccessException, ObjectNotFoundException {
		TablaDTO dto = new TablaDTO();
		dto.setId( id );

		List<TablaDTO> result = this.read(dto);

		if ( result.isEmpty() )
			throw new ObjectNotFoundException();

		return result.get(0);
	}

	public void update(TablaDTO dto) throws DataAccessException {
		TablaDTO keyDTO = new TablaDTO();
		keyDTO.setId( dto.getId() );

		update(dto, keyDTO);
	}

	public void update(TablaDTO dto, TablaDTO filter) throws DataAccessException {
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
			if ( filter.getNombreMostrar()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Nombre_Mostrar=?";
				filterAtts.add( filter.getNombreMostrar() );
			}
			if ( filter.getTipo()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Tipo=?";
				filterAtts.add( filter.getTipo() );
			}	
			if ( filter.getBotonAgregar()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Boton_Agregar=?";
				filterAtts.add( filter.getBotonAgregar() );
			}
			
			if ( filter.getBotonActualizar()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Boton_Actualizar=?";
				filterAtts.add( filter.getBotonActualizar() );
			}		

			if ( filter.getBotonEliminar()!=null ) {
				where += (where.length()==0? "" : " AND ")+"Boton_Eliminar=?";
				filterAtts.add( filter.getBotonEliminar() );
			}			
			
		}

		String query = "UPDATE "+getEntityQName()+" SET id=?, Nombre=?, Nombre_Mostrar=?, Tipo=?, boton_agregar=?, boton_actualizar=?, boton_eliminar=? "+(where.length()==0? "" : " WHERE "+where );

		PreparedStatement ps = getPreparedStatement(query);

		try {
			ps.setInt(1, dto.getId() );
			ps.setString(2, dto.getNombre() );
			ps.setString(3, dto.getNombreMostrar() );
			ps.setString(4, dto.getTipo() );			
			ps.setString(5, dto.getBotonAgregar() );
			ps.setString(6, dto.getBotonActualizar() );
			ps.setString(7, dto.getBotonEliminar() );
			for (int i=0; i<filterAtts.size(); ++i) {
				ps.setObject(8+i, filterAtts.get(i));
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		executeUpdate(ps);
	}

	public TablaDTO create(TablaDTO dto) throws DataAccessException {

		String query = "INSERT INTO "+getEntityQName()+"(id, Nombre, Nombre_Mostrar, Tipo, boton_agregar, boton_actualizar, boton_eliminar ) VALUES ("+getQName("seq_TABLA")+".nextval, ?, ?, ?, ?, ?, ? )";

		PreparedStatement ps = getPreparedStatement(query, new String[] {"id"});

		try {
			ps.setString(1, dto.getNombre());
			ps.setString(2, dto.getNombreMostrar());
			ps.setString(3, dto.getTipo());			
			ps.setString(4, dto.getBotonAgregar());
			ps.setString(5, dto.getBotonActualizar());
			ps.setString(6, dto.getBotonEliminar());

			int key = executeUpdateAndReturnKeyAsInt(ps);
			
			dto.setId( key );
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return dto;
	}

	public void delete(TablaDTO dto) throws DataAccessException {
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
		if ( dto.getNombreMostrar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre_Mostrar=?";
			filterAtts.add( dto.getNombreMostrar() );
		}
		if ( dto.getTipo()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Tipo=?";
			filterAtts.add( dto.getTipo() );
		}
		if ( dto.getBotonAgregar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Boton_Agregar=?";
			filterAtts.add( dto.getBotonAgregar() );
		}
		
		if ( dto.getBotonActualizar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Boton_Actualizar=?";
			filterAtts.add( dto.getBotonActualizar() );
		}		

		if ( dto.getBotonEliminar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Boton_Eliminar=?";
			filterAtts.add( dto.getBotonEliminar() );
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
	protected List<TablaDTO> processResult(ResultSet rs) throws DataAccessException {

		List<TablaDTO> list = new ArrayList<TablaDTO>();

		try {
			while ( rs.next() ) {
				TablaDTO dto = new TablaDTO();
				dto.setId( rs.getInt("id")==0 && rs.wasNull()? null : rs.getInt("id") );
				dto.setNombre( rs.getString("Nombre") );
				dto.setNombreMostrar( rs.getString("Nombre_Mostrar") );				
				dto.setTipo( rs.getString("Tipo"));				
				dto.setBotonAgregar( rs.getString("boton_agregar"));
				dto.setBotonActualizar( rs.getString("boton_actualizar"));
				dto.setBotonEliminar( rs.getString("boton_eliminar"));
				dto.setOrden(rs.getString("orden"));

				list.add( dto );
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return list;
	}

	protected String getEntityName() {
		//return "TBL_CE_IBM_TABLA";
		return ConstantesAdmin.NOMBRETABLA_TABLA_MANTENIMIENTO;
		
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

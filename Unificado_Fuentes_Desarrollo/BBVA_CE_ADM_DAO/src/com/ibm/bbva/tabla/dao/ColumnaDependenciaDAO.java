package com.ibm.bbva.tabla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.ObjectNotFoundException;
import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.ColumnaDependenciaDTO;
import com.ibm.bbva.tabla.service.Constantes;

public class ColumnaDependenciaDAO extends BaseDAO{
	
	private static final Logger LOG = LoggerFactory.getLogger(ColumnaDependenciaDAO.class);

	private static ColumnaDependenciaDAO instance = null;
	
	public static ColumnaDependenciaDAO getInstance() {
		if ( instance==null )
			instance = new ColumnaDependenciaDAO();
		return instance;
	}
	
	protected String getEntityName() {
		return "TBL_CE_IBM_COLUMNA_DEPENDENCIA";
	}
	
	public ColumnaDependenciaDTO findById(Integer id) throws DataAccessException, ObjectNotFoundException {
		ColumnaDependenciaDTO dto = new ColumnaDependenciaDTO();
		dto.setIdColumnaPadre(id);

		List<ColumnaDependenciaDTO> result = this.read(dto);

		if ( result.isEmpty() )
			//throw new ObjectNotFoundException();
			return null;

		return result.get(0);
	}

	public ColumnaDependenciaDTO buscarPorDependencia(Integer id, String flagDependenciaCruzada) throws DataAccessException, ObjectNotFoundException {
		ColumnaDependenciaDTO dto = new ColumnaDependenciaDTO();
		dto.setIdColumnaPadre(id);
		dto.setFlagDependenciaCruzada(flagDependenciaCruzada);

		List<ColumnaDependenciaDTO> result = this.read(dto);

		if ( result.isEmpty() )
			//throw new ObjectNotFoundException();
			return null;

		return result.get(0);
	}
	
	public List<ColumnaDependenciaDTO> buscarPorDependenciaL(Integer id, String flagDependenciaCruzada, Integer idPadre) throws DataAccessException, ObjectNotFoundException {
		ColumnaDependenciaDTO dto = new ColumnaDependenciaDTO();
		dto.setIdColumnaPadre(id);
		dto.setFlagDependenciaCruzada(flagDependenciaCruzada);
		dto.setIdPadre(idPadre);

		List<ColumnaDependenciaDTO> result = this.read(dto);

		if ( result.isEmpty() )
			//throw new ObjectNotFoundException();
			return null;

		return result;
	}	
	
	
	public ColumnaDependenciaDTO findIdColumnaHijo(Integer id) throws DataAccessException, ObjectNotFoundException {
		ColumnaDependenciaDTO dto = new ColumnaDependenciaDTO();
		dto.setIdColumnaHijo(id);

		List<ColumnaDependenciaDTO> result = this.read(dto);

		if ( result.isEmpty() )
			//throw new ObjectNotFoundException();
			return null;

		return result.get(0);
	}
	
	private List<ColumnaDependenciaDTO> read(ColumnaDependenciaDTO filter) throws DataAccessException {
		String where = "";
		List<Object> filterAtts = new ArrayList<Object>();

		if ( filter.getId()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id=?";
			filterAtts.add( filter.getId() );
		}
		if ( filter.getIdColumnaPadre()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Id_Columna_Padre=?";
			filterAtts.add( filter.getIdColumnaPadre());
		}
		
		if ( filter.getIdTablaHijo()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Id_Tabla_Hijo=?";
			filterAtts.add( filter.getIdTablaHijo() );
		}
		
		if ( filter.getIdColumnaHijo()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Id_Columna_Hijo=?";
			filterAtts.add( filter.getIdColumnaHijo());
		}
		
		if ( filter.getNombreColumna()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre_Columna=?";
			filterAtts.add( filter.getNombreColumna() );
		}
		if ( filter.getValorColumna()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Valor_Columna=?";
			filterAtts.add( filter.getValorColumna() );
		}
		
		if ( filter.getNombreColumnaBsq()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre_Columna_Bsq=?";
			filterAtts.add( filter.getNombreColumnaBsq() );
		}

		if ( filter.getFlagDependenciaCruzada()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Flag_Dependencia_Cruzada=?";
			filterAtts.add( filter.getFlagDependenciaCruzada() );
		}

		if ( filter.getNombreColumnaDependencia()!=null ) {
			where += (where.length()==0? "" : " AND ")+"Nombre_Columna_Dependencia=?";
			filterAtts.add( filter.getNombreColumnaDependencia() );
		}
		
		if ( filter.getIdPadre()!=null ) {
			where += (where.length()==0? "" : " AND ")+"id_Padre=?";
			filterAtts.add( filter.getIdPadre() );
		}
		
		String query = "SELECT id, Id_Columna_Padre, id_Tabla_Hijo, id_Columna_Hijo, Nombre_Columna, Valor_Columna, Nombre_Columna_Bsq, Flag_Dependencia_Cruzada, Nombre_Columna_Dependencia, id_Padre FROM "+getEntityQName()+(where.length()==0? "" : " WHERE "+where);

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
	
	protected List<ColumnaDependenciaDTO> processResult(ResultSet rs)
			throws DataAccessException {
		
		List<ColumnaDependenciaDTO> list = new ArrayList<ColumnaDependenciaDTO>();
		try
		{
			while ( rs.next() ) {
				ColumnaDependenciaDTO dto = new ColumnaDependenciaDTO();
				dto.setId( rs.getInt("id")==0 && rs.wasNull()? null : rs.getInt("id") );
				dto.setIdColumnaPadre(rs.getInt("id_columna_padre")==0 && rs.wasNull()? null : rs.getInt("id_columna_padre") );
				dto.setIdTablaHijo( rs.getInt("id_tabla_hijo")==0 && rs.wasNull()? null : rs.getInt("id_tabla_hijo") );
				dto.setIdColumnaHijo(rs.getInt("id_columna_hijo")==0 && rs.wasNull()? null : rs.getInt("id_columna_hijo") );
				dto.setNombreColumna(rs.getString("nombre_columna"));
				dto.setValorColumna( rs.getString("valor_columna"));
				dto.setNombreColumnaBsq(rs.getString("nombre_columna_bsq"));
				dto.setFlagDependenciaCruzada(rs.getString("flag_dependencia_cruzada"));
				dto.setNombreColumnaDependencia(rs.getString("nombre_columna_dependencia"));
				dto.setIdPadre(rs.getInt("id_Padre"));
				list.add(dto);
			}
		} catch (SQLException e) {
		throw new DataAccessException(e);
		}
	return list;
	}
	
	public List<ColumnaDependenciaDTO> getColumnaDependenciaConcat(
			String nombreTabla,String nombreColumna,
			String nombreValor)
			throws DataAccessException {

		if (nombreColumna.contains("-")){
			String campo1 = nombreColumna.substring(0, nombreColumna.indexOf("-"));
			String campo2 = nombreColumna.substring(nombreColumna.indexOf("-")+1,nombreColumna.length());
			nombreColumna = "CONCAT(CONCAT("+campo1+",' - '),"+campo2+") AS " +campo2;
		}
		LOG.info("nombreColumna: " + nombreColumna);
		String query = "SELECT " + nombreColumna+","+nombreValor+ " FROM " +getSchema()+"."+nombreTabla;
		LOG.info("query: " + query);
		PreparedStatement ps = getPreparedStatement(query);
		
		return executeQuery(ps);		
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

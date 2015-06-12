package com.ibm.bbva.tabla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.helpers.TipoDato;
import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.ColumnaDTO;
import com.ibm.bbva.tabla.dto.RegistroTablaDTO;
import com.ibm.bbva.tabla.service.Constantes;

public class RegistroTablaDAO extends BaseDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(RegistroTablaDAO.class);

	private static RegistroTablaDAO instance = null;
	private Collection<ColumnaDTO> columnas;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	protected String getEntityName() {
		return null;
	}

	public static RegistroTablaDAO getInstance() {
		if (instance == null)
			instance = new RegistroTablaDAO();
		return instance;
	}

	public Collection<RegistroTablaDTO> getRegistrosTablaByNombreTabla(
			String nombreTabla, Collection<ColumnaDTO> columnas)
			throws DataAccessException {
		this.columnas = columnas;

		String query = "SELECT " + getQName(nombreTabla) + ".* FROM " + getQName(nombreTabla);
		PreparedStatement ps = getPreparedStatement(query);
		
		List<AbstractDTO> result = this.executeQuery(ps);
		Collection<RegistroTablaDTO> registros = null;

		if (result != null && result.size() > 0) {
			registros = new ArrayList<RegistroTablaDTO>();
			for (AbstractDTO dto : result) {
				registros.add((RegistroTablaDTO) dto);
			}
		}
		return registros;
	}

	public Collection<RegistroTablaDTO> buscar(
			String nombreTabla, Collection<ColumnaDTO> columnas, RegistroTablaDTO registroTablaDTO)
			throws DataAccessException {
		this.columnas = columnas;
        String where = "";

		if (registroTablaDTO != null && registroTablaDTO.getDatosRegistro() != null) {			
			if (columnas != null && columnas.size() > 0 && registroTablaDTO.getDatosRegistro() != null) {
				Map<String, Object> registro = registroTablaDTO.getDatosRegistro();
				for (ColumnaDTO columna : columnas) {
					String field = columna.getNombre();
					Object value = registro.get(field);

					if (columna.getBusqueda().equals("1") || columna.getFlagDependencia().equals("1")){
						if (columna.getTipoDato() != null && 
				 		   (TipoDato.STRING.toString().equals(columna.getTipoDato().toUpperCase()) || 
				 			TipoDato.BOOLEAN.toString().equals(columna.getTipoDato().toUpperCase()) ||
				 		    TipoDato.LISTA.toString().equals(columna.getTipoDato().toUpperCase()))) {								
								if (value != null && !value.equals("")){
									where += (where.length()==0?"" : " AND ")+field + " = '" + value + "'";	
								}								
							} else {
								if (columna.getTipoDato() != null &&
									(TipoDato.DATE.toString().equals(columna.getTipoDato().toUpperCase()) ||
									 TipoDato.INTEGER.toString().equals(columna.getTipoDato().toUpperCase()))) {								
									 if (value != null) {									
									     where += (where.length()==0?"" : " AND ")+field + " = " +  value;
								}
							}
						}
					}
				}
			}
		}	
		
		String query = "SELECT " + getQName(nombreTabla) + ".* FROM " + getQName(nombreTabla)+(where.length()==0? "" : " WHERE "+where);
		LOG.info("buscar - query : "+query);
		PreparedStatement ps = getPreparedStatement(query);
		
		List<AbstractDTO> result = this.executeQuery(ps);
		Collection<RegistroTablaDTO> registros = null;

		if (result != null && result.size() > 0) {
			registros = new ArrayList<RegistroTablaDTO>();
			for (AbstractDTO dto : result) {
				registros.add((RegistroTablaDTO) dto);
			}
		}
		return registros;
	}

	public Collection<RegistroTablaDTO> buscarRegistroTabla(
			String nombreTabla, Collection<ColumnaDTO> columnas, RegistroTablaDTO registroTablaDTO)
			throws DataAccessException {
		this.columnas = columnas;
        String where = "";

		if (registroTablaDTO != null && registroTablaDTO.getDatosRegistro() != null) {			
			if (columnas != null && columnas.size() > 0 && registroTablaDTO.getDatosRegistro() != null) {
				Map<String, Object> registro = registroTablaDTO.getDatosRegistro();
				for (ColumnaDTO columna : columnas) {
					String field = columna.getNombre();
					Object value = registro.get(field);
					
					if (columna.getTipoDato() != null && 
			 		   (TipoDato.STRING.toString().equals(columna.getTipoDato().toUpperCase()) || 
			 			TipoDato.BOOLEAN.toString().equals(columna.getTipoDato().toUpperCase()) ||
			 		    TipoDato.LISTA.toString().equals(columna.getTipoDato().toUpperCase()))) {								
							if (value != null && !value.equals("")){
								where += (where.length()==0?"" : " AND ")+field + " = '" + value + "'";	
							}								
						} else {
							if (columna.getTipoDato() != null &&
								(TipoDato.DATE.toString().equals(columna.getTipoDato().toUpperCase()) ||
								 TipoDato.INTEGER.toString().equals(columna.getTipoDato().toUpperCase()))) {								
								 if (value != null) {									
								     where += (where.length()==0?"" : " AND ")+field + " = " +  value;
							}
						}
					}
				}
			}
		}	
		
		LOG.info("nombreTabla:"+nombreTabla);
	
		String query = "";
		boolean bJoin = false;		
		String table1 = ""; String table2 = ""; String column1 = ""; String column2 = "";
		
		if (nombreTabla.equals("TBL_CE_IBM_TAREA_PERFIL")) {
			table1 = "TBL_CE_IBM_TAREA_PERFIL";
			table2 = "TBL_CE_IBM_PERFIL_CE";
			column1 = "ID_PERFIL_FK";
			column2 = "ID";		
			
			where += (where.length()==0?"" : " AND ")+" TBL_CE_IBM_PERFIL_CE.FLAG_PROCESO = '1' ";			
			bJoin = true;
		}
		
		if (nombreTabla.equals("TBL_CE_IBM_PRODUCTO_TAREA")) {
			where += (where.length()==0?"" : " AND ")+"ID_TAREA_FK <> 26";
		}
		
		//String query = "SELECT " +getQName(nombreTabla) + ".* FROM " + getQName(nombreTabla)+(where.length()==0? "" : " WHERE "+where);
		if(bJoin){
			query = "SELECT " +getQName(table1) + ".* FROM " + getQJoinTables(table1,table2,column1,column2)+(where.length()==0? "" : " WHERE "+where);
		}else{
			query = "SELECT " +getQName(nombreTabla) + ".* FROM " + getQName(nombreTabla)+(where.length()==0? "" : " WHERE "+where);
		}
		
		LOG.info("buscarRegistroTabla - query : "+query);
		PreparedStatement ps = getPreparedStatement(query);
		
		List<AbstractDTO> result = this.executeQuery(ps);
		Collection<RegistroTablaDTO> registros = null;

		if (result != null && result.size() > 0) {
			registros = new ArrayList<RegistroTablaDTO>();
			for (AbstractDTO dto : result) {
				registros.add((RegistroTablaDTO) dto);
			}
		}
		return registros;
	}
	
	public Collection<RegistroTablaDTO> buscarRegistrosTabla(
			String nombreTabla, Collection<ColumnaDTO> columnas, RegistroTablaDTO registroTablaDTO)
			throws DataAccessException {
		this.columnas = columnas;
        String where = "";

		if (registroTablaDTO != null && registroTablaDTO.getDatosRegistros() != null) {			
			if (columnas != null && columnas.size() > 0 && registroTablaDTO.getDatosRegistros() != null) {
				Map<String, ArrayList<Object>> registro = registroTablaDTO.getDatosRegistros();
				for (ColumnaDTO columna : columnas) {
					String field = columna.getNombre();
					Object value = null;
					if (registro.get(field)!=null) {
					    value = registro.get(field);
					}
					
					if (columna.getTipoDato() != null && 
			 		   (TipoDato.STRING.toString().equals(columna.getTipoDato().toUpperCase()) ||
			 			TipoDato.BOOLEAN.toString().equals(columna.getTipoDato().toUpperCase()) ||
			 		    TipoDato.LISTA.toString().equals(columna.getTipoDato().toUpperCase()))) {								
							if (value != null && !value.equals("")){
								where += (where.length()==0?"" : " AND ")+field + " in '" + value + "'";	
							}								
					} else {
						if (columna.getTipoDato() != null &&
							(TipoDato.DATE.toString().equals(columna.getTipoDato().toUpperCase()) ||
							 TipoDato.INTEGER.toString().equals(columna.getTipoDato().toUpperCase()))) {
							 if (value != null) {
								 // el HashSet elimina los valores repetidos porque no se pueden usar más de 1000 valores en el IN
								 String whereIn = value.toString().replace("[","").replace("]","").replace(" ","");
								 String[] arrWhereIn = whereIn.split(",");
								 HashSet hs = new HashSet(Arrays.asList(arrWhereIn));
								 
							     where += (where.length()==0?"" : " AND ")+field + " in " +  hs.toString().replace("[","(").replace("]",")");
							 }
						}
					}
				}
			}
		}	
		
		String query = "SELECT " +getQName(nombreTabla) + ".* FROM " + getQName(nombreTabla)+(where.length()==0? "" : " WHERE "+where);
		LOG.info("buscarRegistrosTabla - query : "+query);
		PreparedStatement ps = getPreparedStatement(query);
		
		List<AbstractDTO> result = this.executeQuery(ps);
		Collection<RegistroTablaDTO> registros = null;

		if (result != null && result.size() > 0) {
			registros = new ArrayList<RegistroTablaDTO>();
			for (AbstractDTO dto : result) {
				registros.add((RegistroTablaDTO) dto);
			}
		}
		return registros;
	}
	
	public RegistroTablaDTO buscarPorId(
			String nombreTabla, Collection<ColumnaDTO> columnas, RegistroTablaDTO registroTablaDTO)
			throws DataAccessException {
		this.columnas = columnas;
        String where = "";

		if (registroTablaDTO != null && registroTablaDTO.getDatosRegistro() != null) {			
			if (columnas != null && columnas.size() > 0 && registroTablaDTO.getDatosRegistro() != null) {
				Map<String, Object> registro = registroTablaDTO.getDatosRegistro();
				for (ColumnaDTO columna : columnas) {
					String field = columna.getNombre();
					Object value = registro.get(field);

					//if (columna.getBusqueda().equals("1") || columna.getFlagDependencia().equals("1")){
						if (columna.getTipoDato() != null && 
				 		   (TipoDato.STRING.toString().equals(columna.getTipoDato().toUpperCase()) || 
				 			TipoDato.BOOLEAN.toString().equals(columna.getTipoDato().toUpperCase()) ||	   
				 		    TipoDato.LISTA.toString().equals(columna.getTipoDato().toUpperCase()))) {								
								if (value != null && !value.equals("")){
									where += (where.length()==0?"" : " AND ")+field + " = '" + value + "'";	
								}								
							} else {
								if (columna.getTipoDato() != null &&
								   (TipoDato.DATE.toString().equals(columna.getTipoDato().toUpperCase()) ||
								   TipoDato.INTEGER.toString().equals(columna.getTipoDato().toUpperCase()))) {								
									if (value != null) {									
									   where += (where.length()==0?"" : " AND ")+field + " = " +  value;
								}
							}
						}
					//}
				}
			}
		}	
		
		String query = "SELECT " + getQName(nombreTabla) + ".* FROM " + getQName(nombreTabla)+(where.length()==0? "" : " WHERE "+where);
		LOG.info("buscarPorId - query : "+query);
		PreparedStatement ps = getPreparedStatement(query);
		
		List<AbstractDTO> result = this.executeQuery(ps);
		RegistroTablaDTO registros = null;

		if (result != null && result.size() > 0) {
			registros = new RegistroTablaDTO();
			for (AbstractDTO dto : result) {
				registros = (RegistroTablaDTO) dto;
			}
		}
		
		return registros;
	}	
	
	public void update(String nombreTabla, Collection<ColumnaDTO> columnas,
			RegistroTablaDTO registroTablaDTO) throws DataAccessException {

		String where = "";
		String set = "";
		Collection<Date> camposFecha = null;

		if (registroTablaDTO != null
				&& registroTablaDTO.getDatosRegistro() != null) {
			if (columnas != null && columnas.size() > 0
					&& registroTablaDTO.getDatosRegistro() != null) {
				Map<String, Object> registro = registroTablaDTO
						.getDatosRegistro();
				for (ColumnaDTO columna : columnas) {
					String field = columna.getNombre();
					Object value = registro.get(field);

					if (columna.isEsLlavePrimaria()) {
						where += (where.length()==0? "" : " AND ")+ field + " = " + value;
						//where += field + " = " + value;
					}
					set += set.length() == 0 ? "" : ",";
					set += field + " = ";

					if (columna.getTipoDato() != null
							&& (TipoDato.STRING.toString().equals(
									columna.getTipoDato().toUpperCase()) || TipoDato.LISTA
									.toString()
									.equals(columna.getTipoDato().toUpperCase()))) {
						if (value == null){
							set += null;
						}else{
							set += "'" + value + "'";	
						}
						
					} else {
						if (columna.getTipoDato() != null
								&& (TipoDato.DATE.toString().equals(columna
										.getTipoDato().toUpperCase()))) {
							set += " ? ";
							if (camposFecha == null) {
								camposFecha = new ArrayList<Date>();
							}
							try {
								if (value == null){
							       camposFecha.add(null);								   
								}else{
								   camposFecha.add(sdf.parse((String) value));
								}
							} catch (ParseException e) {
								LOG.error(e.getMessage(), e);
								camposFecha.add(null);
							}
						} else {
							set += value;
						}
					}

				}
			} else {
				throw new DataAccessException(
						"No se puede insertar registros en la tabla ya que la estructura de esta no ha sido definida correctamente.");
			}
		} else {
			throw new DataAccessException(
					"No se puede insertar registros en la tabla ya que la estructura de esta no ha sido definida correctamente.");
		}

		String query = "UPDATE " + getQName(nombreTabla)
				+ " SET " + set + " "
				+ (where.length() == 0 ? "" : " WHERE " + where);

		PreparedStatement ps = getPreparedStatement(query);

		try {
			if (camposFecha != null && camposFecha.size() > 0) {
				int i = 0;
				for (Date fecha : camposFecha) {
					ps.setTimestamp(++i,
							fecha != null ? new java.sql.Timestamp(fecha
									.getTime()) : null);
				}
			}
			
			executeUpdate(ps);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			/******
			 * Se incluye el llamado a un método que cerraría la conexión activa basandose en el statement
			 * AUTOR: Hernán Fúquene
			 * FECHA: 2010/04/27
			 ******/
			closeConnectionFromStatement(ps);
			/******************/
		}
	}

	/*
	 * Solución temporal al Error de PROTOCOL VIOLATION.
	 * Eliminar este método si se encuentra una solución definitiva. 
	 */
	private String consultarSecuencia(String nombreTabla) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//String query = "SELECT " + getQName("\"seq_"+nombreTabla.toUpperCase()+"\"")+".nextval from DUAL" ;
			String query = "SELECT " + getQName("SEQ_"+nombreTabla.toUpperCase().substring(4))+".nextval from DUAL" ;

			ps = getPreparedStatement(query);
			rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					return rs.getObject(1).toString();
				}
			}
		} catch (DataAccessException e) {
			LOG.error(e.getMessage(), e);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
		}finally {
			try {
				if (ps!=null)
					ps.close();
			}
			catch (Exception e) {
			}
			try {
				if (rs!=null)
					rs.close();
			}
			catch (Exception e) {
			}
			/******
			 * Se incluye el llamado a un método que cerraría la conexión activa basandose en el statement
			 * AUTOR: Hernán Fúquene
			 * FECHA: 2010/04/27
			 ******/
			closeConnectionFromStatement(ps);
			/******************/
		}
		return null;
	}
	
	/*
	 * Solución temporal al Error de PROTOCOL VIOLATION.
	 * Eliminar este método si se encuentra una solución definitiva y descomentariar el antiguo metodo create(abajo). 
	 */
	public RegistroTablaDTO create(String nombreTabla,
			Collection<ColumnaDTO> columnas, RegistroTablaDTO registroTablaDTO)
			throws DataAccessException {

		String fields = "";
		String values = "";
		
		String nombreSecuencia = getQName("SEQ_" + nombreTabla.substring(4)) + ".nextval";
		String nombreCampoPK = null;
		Collection<Date> camposFecha = null;

		nombreSecuencia = this.consultarSecuencia(nombreTabla);
		/*
		 * Se consulta el próximo valor de la secuencia.
		 */
		
		if (registroTablaDTO != null
				&& registroTablaDTO.getDatosRegistro() != null) {
			if (columnas != null && columnas.size() > 0
					&& registroTablaDTO.getDatosRegistro() != null) {
				Map<String, Object> registro = registroTablaDTO
						.getDatosRegistro();
				for (ColumnaDTO columna : columnas) {
					String field = columna.getNombre();
					Object value = registro.get(field);

					if (value != null || columna.isEsLlavePrimaria()) {
						fields += fields.length() == 0 ? "" : ",";
						values += values.length() == 0 ? "" : ",";

						fields += field;
						if (columna.isEsLlavePrimaria() && !columna.isEsLlaveForanea()) {
							nombreCampoPK = columna.getNombre();
							if(nombreTabla.equalsIgnoreCase("TBL_CE_IBM_GUIA_DOCUMENT_CC")){
								values += value;
							}else{
								values += nombreSecuencia;
							}
							
						} else {
							if (columna.getTipoDato() != null
									&& (TipoDato.STRING.toString()
											.equals(
													columna.getTipoDato()
															.toUpperCase()) || TipoDato.LISTA
											.toString().equals(
													columna.getTipoDato()
															.toUpperCase()))) {								
								if (value == null){
									values += null;
								}else{
									values += "'" + value + "'";	
								}								
							} else {
								if (columna.getTipoDato() != null
										&& (TipoDato.DATE.toString()
												.equals(columna.getTipoDato()
														.toUpperCase()))) {
									values += " ? ";
									if (camposFecha == null) {
										camposFecha = new ArrayList<Date>();
									}
									try {
										if (value == null) {
											camposFecha.add(null);
										}else{
										    camposFecha.add(sdf.parse((String) value));
										}
									} catch (ParseException e) {
										LOG.error(e.getMessage(), e);
										camposFecha.add(null);
									}
								} else {
									values += value;
								}

							}
						}
					}
				}
			} else {
				throw new DataAccessException(
						"No se puede insertar registros en la tabla ya que la estructura de esta no ha sido definida correctamente.");
			}
		} else {
			throw new DataAccessException(
					"No se puede insertar registros en la tabla ya que la estructura de esta no ha sido definida correctamente.");
		}

		String query = "INSERT INTO " + getQName(nombreTabla)
				+ " (" + fields + ") VALUES (" + values + ")";

		PreparedStatement ps = getPreparedStatement(query);//,new String[] { "id" });

		try {
			if (camposFecha != null && camposFecha.size() > 0) {
				int i = 0;
				for (Date fecha : camposFecha) {
					ps.setTimestamp(++i,
							fecha != null ? new java.sql.Timestamp(fecha
									.getTime()) : null);
				}
			}

			executeUpdate(ps);
			registroTablaDTO.getDatosRegistro().put(nombreCampoPK, Integer.parseInt(nombreSecuencia));
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			throw new DataAccessException(ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			/******
			 * Se incluye el llamado a un método que cerraría la conexión activa basandose en el statement
			 * AUTOR: Hernán Fúquene
			 * FECHA: 2010/04/27
			 ******/
			closeConnectionFromStatement(ps);
			/******************/
		}

		return registroTablaDTO;
	}
	
	
	/*
	 * Descomentariar este método si se haya la solución definitiva al error de PROTOCOL VIOLATION
	 */
	/*public RegistroTablaDTO create(String nombreTabla,
			Collection<ColumnaDTO> columnas, RegistroTablaDTO registroTablaDTO)
			throws DataAccessException {

		String fields = "";
		String values = "";
		String nombreSecuencia = getQName("seq_" + nombreTabla) + ".nextval";
		String nombreCampoPK = null;
		Collection<Date> camposFecha = null;

		if (registroTablaDTO != null
				&& registroTablaDTO.getDatosRegistro() != null) {
			if (columnas != null && columnas.size() > 0
					&& registroTablaDTO.getDatosRegistro() != null) {
				Map<String, Object> registro = registroTablaDTO
						.getDatosRegistro();
				for (ColumnaDTO columna : columnas) {
					String field = columna.getNombre();
					Object value = registro.get(field);

					if (value != null || columna.isEsLlavePrimaria()) {
						fields += fields.length() == 0 ? "" : ",";
						values += values.length() == 0 ? "" : ",";

						fields += field;
						if (columna.isEsLlavePrimaria()) {
							nombreCampoPK = columna.getNombre();
							values += nombreSecuencia;
						} else {
							if (columna.getTipoDato() != null
									&& (TipoDato.STRING.toString()
											.equals(
													columna.getTipoDato()
															.toUpperCase()) || TipoDato.LISTA
											.toString().equals(
													columna.getTipoDato()
															.toUpperCase()))) {
								values += "'" + value + "'";
							} else {
								if (columna.getTipoDato() != null
										&& (TipoDato.DATE.toString()
												.equals(columna.getTipoDato()
														.toUpperCase()))) {
									values += " ? ";
									if (camposFecha == null) {
										camposFecha = new ArrayList<Date>();
									}
									try {
										camposFecha.add(sdf
												.parse((String) value));
									} catch (ParseException e) {
										LOG.error(e.getMessage(), e);
										camposFecha.add(null);
									}
								} else {
									values += value;
								}

							}
						}
					}
				}
			} else {
				throw new DataAccessException(
						"No se puede insertar registros en la tabla ya que la estructura de esta no ha sido definida correctamente.");
			}
		} else {
			throw new DataAccessException(
					"No se puede insertar registros en la tabla ya que la estructura de esta no ha sido definida correctamente.");
		}

		String query = "INSERT INTO " + getQName(nombreTabla)
				+ " (" + fields + ") VALUES (" + values + ")";

		PreparedStatement ps = getPreparedStatement(query,
				new String[] { "id" });

		try {
			if (camposFecha != null && camposFecha.size() > 0) {
				int i = 0;
				for (Date fecha : camposFecha) {
					ps.setTimestamp(++i,
							fecha != null ? new java.sql.Timestamp(fecha
									.getTime()) : null);
				}
			}

			executeUpdate(ps);
			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {
				Integer id = rs.getInt(1);
				registroTablaDTO.getDatosRegistro().put(nombreCampoPK, id);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
		}

		return registroTablaDTO;
	}*/


	public void delete(String nombreTabla, RegistroTablaDTO registroTablaDTO)
			throws DataAccessException {
		//StringBuilder where = new StringBuilder();
		String where = "";
		if (registroTablaDTO != null
				&& registroTablaDTO.getDatosRegistro() != null) {
			if (columnas != null && columnas.size() > 0
					&& registroTablaDTO.getDatosRegistro() != null) {
				Map<String, Object> registro = registroTablaDTO
						.getDatosRegistro();
				for (ColumnaDTO columna : columnas) {
					String field = columna.getNombre();
					Object value = registro.get(field);

					if (columna.isEsLlavePrimaria()) {
						//where += field + " = " + value;
						//where.append(field).append(" = ").append(value);
						where += (where.length()==0? "" : " AND ")+ field + " = " + value;
					}
				}
			} else {
				throw new DataAccessException(
						"No se puede insertar registros en la tabla ya que la estructura de esta no ha sido definida correctamente.");
			}
		} else {
			throw new DataAccessException(
					"No se puede insertar registros en la tabla ya que la estructura de esta no ha sido definida correctamente.");
		}
		String query = "DELETE FROM " + getQName(nombreTabla)
				+ (where.length() == 0 ? "" : " WHERE " + where);
		PreparedStatement ps = getPreparedStatement(query);
		executeUpdate(ps);
	}

	@Override
	protected List<AbstractDTO> processResult(ResultSet rs)
			throws DataAccessException {
		List<AbstractDTO> result = null;
		try {
			while (rs.next()) {
				RegistroTablaDTO dto = new RegistroTablaDTO();
				Map<String, Object> registro = new HashMap<String, Object>();
				if (columnas != null && columnas.size() > 0) {
					for (ColumnaDTO columna : columnas) {
						if (columna != null && columna.getNombre() != null) {
							String nombreColumna = columna.getNombre();
							Object valorColumna = rs.getObject(columna
									.getNombre());
							registro.put(nombreColumna, valorColumna);
						}
					}
				}
				dto.setDatosRegistro(registro);
				if (result == null) {
					result = new ArrayList<AbstractDTO>();
				}
				result.add(dto);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return result;
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

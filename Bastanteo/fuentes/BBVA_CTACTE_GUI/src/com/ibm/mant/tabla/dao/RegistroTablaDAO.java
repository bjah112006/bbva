package com.ibm.mant.tabla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.helpers.TipoDato;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.mant.tabla.core.dao.GenericDAO;
import com.ibm.mant.tabla.dto.ColumnaDTO;
import com.ibm.mant.tabla.dto.RegistroTablaDTO;

public class RegistroTablaDAO extends GenericDAO {
	
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
	
	public String getRegistroTablaById(String nombreTabla, String nombreColumna,String nombreColumnaId,Integer id) throws DataAccessException {
		String xmlDATA ="";
		RegistroTablaDTO registroTablaDTO = new RegistroTablaDTO();
		String query = "SELECT " + nombreColumna + " FROM " + getQName(nombreTabla) + " WHERE " + nombreColumnaId + " = " + id;
		LOG.debug("query getRegistroTablaById: " + query);
		PreparedStatement ps = getPreparedStatement(query);
		LOG.info("query preparada");
		List<AbstractDTO> result = this.executeQuery(ps);
		LOG.info("Ejecutada");
		if (result != null && result.size() > 0) {
			for (AbstractDTO dto : result) {
				registroTablaDTO = (RegistroTablaDTO) dto;
				LOG.info("agregado");
			}
		}
		
		Map<String, Object> registro = registroTablaDTO.getDatosRegistro();
		LOG.info("registro.toString(): " + registro.toString());
		Object value = registro.get("TX_MENSAJE_ENTRADA");
		LOG.info("value: " + value);
		xmlDATA = value.toString();
		return xmlDATA;
	}

	public Collection<RegistroTablaDTO> getRegistrosTablaByNombreTabla(
			String nombreTabla, Collection<ColumnaDTO> columnas, String columOrdenNom, String tipoOrden)
			throws DataAccessException {
		this.columnas = columnas;
		String ORDER="";

		LOG.info("##############REGISTROS##############");
		
		if(columOrdenNom!=null ){
			ORDER=" ORDER BY "+columOrdenNom;
			
			if(tipoOrden!=null)
				ORDER=ORDER+" "+tipoOrden;
		}
		
		String campos ="";
		for (ColumnaDTO columna : columnas) {
			String field = columna.getNombre();
			
			if (columna.getTipoDato().equalsIgnoreCase("TIMESTAMP")){
				field = "TO_CHAR(" + field + ") AS " + field;
			}
			if (columna.getTipoDato().equalsIgnoreCase("BLOB")){
				field = "UTL_RAW.CAST_TO_VARCHAR2(" + field + ") AS " + field;
			}
			campos = campos + field + ",";
		}
		campos = campos.substring(0, campos.length()-1);
		
		LOG.info("##############ORDER = "+ORDER);
		//String query = "SELECT " + nombreTabla + ".* FROM " + getQName(nombreTabla);
		//String query = "SELECT " + nombreTabla + ".* FROM " + getQName(nombreTabla)+" "+ORDER;
		String query = "SELECT " + campos + " FROM " + getQName(nombreTabla)+" "+ORDER;
				
		LOG.debug(""+query);
		
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
					
					if (columna.getBusqueda().equals("1")){
						if (columna.getTipoDato() != null && 
				 		   (TipoDato.STRING.toString().equals(columna.getTipoDato().toUpperCase()) || 
				 			TipoDato.ALFANUMERICO.toString().equals(columna.getTipoDato().toUpperCase()) || 
				 		    TipoDato.LISTA.toString().equals(columna.getTipoDato().toUpperCase()))) {								
								if (value != null){
									where += (where.length()==0?"" : " AND ")+field + " = '" + value + "'";	
								}								
							} else {
								if (columna.getTipoDato() != null &&
								   (TipoDato.DATE.toString().equals(columna.getTipoDato().toUpperCase()))) {									
									if (value != null) {									
									   where += (where.length()==0?"" : " AND ")+field + value;
								}
							}
						}
					}
				}
			}
		}	
		
		String query = "SELECT " + nombreTabla + ".* FROM " + getQName(nombreTabla)+(where.length()==0? "" : " WHERE "+where);
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
									columna.getTipoDato().toUpperCase()) || 
								TipoDato.ALFANUMERICO.toString().equals(
									columna.getTipoDato().toUpperCase()) || 
								TipoDato.LISTA
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
						} else if (columna.getTipoDato() != null
								&& (TipoDato.BLOB.toString().equals(columna
										.getTipoDato().toUpperCase()))) {
							if (value == null) {
								set += null;
							} else {
								set += "UTL_RAW.CAST_TO_RAW('" + value + "')";	
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
		
		LOG.debug("queryTablaUpd : "+query);

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
			String query = "SELECT " + getQName("SEQ_CE_"+nombreTabla.toUpperCase().substring(7))+".nextval from DUAL" ;
			LOG.debug("QUERY:" + query);
			ps = getPreparedStatement(query);
			rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					LOG.info("SECUENCIA QUE TOCA = "+rs.getObject(1).toString());
					return rs.getObject(1).toString();
				}
			}
		} catch (DataAccessException e) {
			LOG.error("DataAccessException e: " + e.getMessage(), e);
		} catch (SQLException e) {
			LOG.error("SQLException e: " + e.getMessage(), e);
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
		LOG.info("secuencia: EN RegistroTablaDAO " + nombreSecuencia);
		String nombreCampoPK = null;
		Collection<Date> camposFecha = null;

		nombreSecuencia = this.consultarSecuencia(nombreTabla);
		LOG.info("nombreSecuencia===== " + nombreSecuencia);
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
								values += "'" + value + "'"; // este es el caso de COD_TIPO_PJ para la tabla TBL_CE_IBM_GUIA_DOCUMENT_CC
							}else{
								values += nombreSecuencia;
							}
							
						} else {
							if (columna.getTipoDato() != null
									&& (TipoDato.STRING.toString()
											.equals(
													columna.getTipoDato()
															.toUpperCase()) || 
										TipoDato.ALFANUMERICO.toString()
											.equals(
													columna.getTipoDato()
															.toUpperCase()) ||															
										TipoDato.LISTA
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
								} else if (columna.getTipoDato() != null
										&& (TipoDato.BLOB.toString().equals(columna
												.getTipoDato().toUpperCase()))) {
									if (value == null) {
										values += null;
									} else {
										values += "UTL_RAW.CAST_TO_RAW('" + value + "')";	
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

		LOG.info("QUERY INSERT ----------> " + query);
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
			LOG.error("SQLException e: " + e.getMessage(), e);
			throw new DataAccessException(e);
		} catch (Exception ex) {
			LOG.error("Exception e: " + ex.getMessage(), ex);
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
										e.printStackTrace();
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
		LOG.debug("DELETE QUERY= "+query);
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
		return ConstantesAdmin.NOMBRE_ESQUEMA_MANTENIMIENTO;
	}

	@Override
	protected String processResultReturnString(ResultSet rs)
			throws DataAccessException {
		// TODO Apéndice de método generado automáticamente
		return "";
	}
}

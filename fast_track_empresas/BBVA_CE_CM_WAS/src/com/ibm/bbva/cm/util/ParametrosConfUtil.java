package com.ibm.bbva.cm.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ParametrosConfUtil {

	private static volatile ParametrosConfUtil instance = null;
	private final String DATASOURCE_JNDI_NAME = "jdbc/BBVA";
	private final int CODIGO_APLICATIVO_WEBCM = 5;
	private final String CODIGO_MODULO_CONF = "CONF";
	private HashMap<String, String> propiedades = null;
	private HashMap<String, String> propiedadesVA = null;

	protected ParametrosConfUtil() {
		propiedades = new HashMap();
		propiedadesVA = new HashMap();
		Connection connection = getConnection();
		if (connection != null) {
			PreparedStatement statement = null;
			ResultSet rs = null;
			PreparedStatement statement1 = null;
			ResultSet rs1 = null;
			try {
				String sql = "SELECT NOMBRE_VARIABLE, VALOR_VARIABLE FROM CONELE.TBL_CE_IBM_PARAMETROS_CONF WHERE CODIGO_APLICATIVO = ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, CODIGO_APLICATIVO_WEBCM);
				rs = statement.executeQuery();

				while (rs.next()) {
					propiedades.put(rs.getString("NOMBRE_VARIABLE"),
							rs.getString("VALOR_VARIABLE"));
				}
				
				// Las propiedades del visor avanzado están en la tabla de cuenta corriente
				String sql1 = "SELECT NOMBRE_VARIABLE, VALOR_VARIABLE FROM CONELE.TBL_CE_IBM_PARAMETROS_CONF_CC WHERE CODIGO_MODULO = ?";
				statement1 = connection.prepareStatement(sql1);
				statement1.setString(1, CODIGO_MODULO_CONF);
				rs1 = statement1.executeQuery();

				while (rs1.next()) {
					propiedadesVA.put(rs1.getString("NOMBRE_VARIABLE"),
							rs1.getString("VALOR_VARIABLE"));
				}
				rs1.close();
			} catch (SQLException e) {
				System.err.println("*******************************************");
				System.err.println("ERROR AL CARGAR PARAMETROS DE CONFIGURACION");
				System.err.println("*******************************************");
				e.printStackTrace();
			} finally {
				try { if (rs != null) rs.close(); } catch (Exception e) {};
				try { if (statement != null) statement.close(); } catch (Exception e) {};
				try { if (rs1 != null) rs1.close(); } catch (Exception e) {};
				try { if (statement1 != null) statement1.close(); } catch (Exception e) {};
				try {
					if (!connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static ParametrosConfUtil getInstance() {
		if (instance == null) {
			synchronized (ParametrosConfUtil.class) {
				if (instance == null) {
					instance = new ParametrosConfUtil();
				}
			}
		}
		return instance;
	}
	
	public String getValue(String nombre) {
		return propiedades.get(nombre);
	}
	
	public String getValueVA(String nombre) {
		return propiedadesVA.get(nombre);
	}

	private Connection getConnection() {
		Connection connection = null;
		try {
			InitialContext context = new InitialContext();
			DataSource dataSource = (DataSource) context
					.lookup(DATASOURCE_JNDI_NAME);
			connection = dataSource.getConnection();
		} catch (NamingException e) {
			System.err.println("*******************************************");
			System.err.println("ERROR AL CARGAR PARAMETROS DE CONFIGURACION");
			System.err.println("*******************************************");
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("*******************************************");
			System.err.println("ERROR AL CARGAR PARAMETROS DE CONFIGURACION");
			System.err.println("*******************************************");
			e.printStackTrace();
		}
		return connection;
	}
}

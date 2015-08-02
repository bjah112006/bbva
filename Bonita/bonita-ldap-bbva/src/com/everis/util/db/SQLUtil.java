package com.everis.util.db;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.beanutils.MethodUtils;

import com.everis.core.exception.BussinesException;
import com.everis.util.DBUtilSpring;
import com.everis.util.db.mapper.RowMapper;
import com.everis.util.db.mapper.impl.StringMapperImpl;


public class SQLUtil<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private Logger LOGGER;
    private String jndiName;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private boolean opened = false;

    public SQLUtil() {
        super();
    }

    public SQLUtil(String jndiName) {
        super();
        this.jndiName = jndiName;
    }

    public Logger getTrace() {
        return LOGGER;
    }

    public void setTrace(Logger trace) {
        this.LOGGER = trace;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public void openConnection() throws BussinesException {
        if(!opened) {
            LOGGER.log(Level.WARNING, "SQLUtil:openConnection:Conexion abierta externamente");
            open(true);
            opened = true;
        }
    }
    
    public void closeConnection() throws BussinesException {
        if(opened) {
            LOGGER.log(Level.WARNING, "SQLUtil:closeConnection:Conexion abierta externamente");
            close(opened);
            opened = false;
        }
    }
    public boolean isOpened() {
        return opened;
    }

    private void close(Object o, String message) throws BussinesException {
        boolean isClose = false;
        if(o != null) {
            isClose = isClose || (o instanceof Connection);
            isClose = isClose || (o instanceof Statement);
            isClose = isClose || (o instanceof PreparedStatement);
            isClose = isClose || (o instanceof ResultSet);
    
            if (isClose) {
                try {
                    MethodUtils.invokeExactMethod(o, "close", new Object[] {});
                } catch (Exception e) {
                   throw new BussinesException(message, e);
                }
            }
        }
    }

    private Connection createConnection(String jndiName) throws SQLException {
        DataSource dataSource = DBUtilSpring.getInstance().getDataSource(jndiName);
        return dataSource.getConnection();
    }

    private void validQuery(String query) throws SQLException {
        LOGGER.log(Level.FINE, MessageFormat.format("SQLUtil:executeQuery:[{0}]", query));
        if (query.isEmpty()) {
            throw new SQLException("Query empty");
        }
    }

    public List<Metadata> getMetadata(String query) throws BussinesException {
        List<Metadata> cols;
        ResultSet resultSet = null;
        try {
            validQuery(query);
            String queryTop = "SELECT * FROM (" + query + ") WHERE ROWNUM = 1";
            open();
            resultSet = getResultSet(queryTop);
            cols = getMetadata(resultSet);
        } catch (Exception ex) {
            throw new BussinesException(ex);
        } finally {
            close(resultSet, "resultSet");
            close();
        }

        return cols;
    }

    private List<Metadata> getMetadata(ResultSet result) {
        List<Metadata> cols = new ArrayList<Metadata>();
        int i = 0;
        Metadata o = null;
        if (result != null) {
            try {
                ResultSetMetaData metaData = result.getMetaData();
                for (i = 1; i <= metaData.getColumnCount(); i++) {
                    o = new Metadata();
                    o.setColumnName(metaData.getColumnName(i));
                    o.setColumnType(metaData.getColumnType(i));
                    o.setPrecision(metaData.getPrecision(i));
                    o.setScale(metaData.getScale(i));
                    cols.add(o);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "[SQLUtil:getMetadata] initialContext SQLException", e);
            }
        }

        return cols;
    }

    public boolean execute(String query) throws BussinesException {
        Statement statement = null;
        boolean execute = false;

        try {
            validQuery(query);
            open();
            statement = connection.createStatement();
            statement.execute(query);
            execute = true;
        } catch (Exception ex) {
            throw new BussinesException(ex);
        } finally {
            close(statement, "Statement execute");
            close();
        }

        return execute;
    }

    public ResultSet getResultSet(String query) throws BussinesException {
        return getResultSet(query, false);
    }
    
    public ResultSet getResultSet(String query, boolean forUpdate) throws BussinesException {
        ResultSet rs;
        try {
            validQuery(query);
            if(!forUpdate) {
                preparedStatement = connection.prepareStatement(query);
            } else {
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            }
            rs = preparedStatement.executeQuery();
        } catch (Exception ex) {
            throw new BussinesException(ex);
        }

        return rs;
    }

    public int executeUpdate(String query, Object[] parameters) throws BussinesException {
        int result = -1;
        PreparedStatement statement = null;

        try {
            validQuery(query);
            open();
            statement = connection.prepareStatement(query);

            if (parameters != null) {
                for (int i = 1; i <= parameters.length; i++) {
                    if(parameters[i - 1] instanceof byte[]) {
                        statement.setBinaryStream(i, new ByteArrayInputStream((byte[]) parameters[i - 1]));
                    } else {
                        statement.setObject(i, parameters[i - 1]);
                    }
                    LOGGER.log(Level.FINE, "Index: " + i + ", Value: " + parameters[i - 1].toString());
                }
            }

            result = statement.executeUpdate();

        } catch (Exception ex) {
            throw new BussinesException(ex);
        } finally {
            close(statement, "Statement executeQuery");
            close();
        }

        return result;
    }
    
    @SuppressWarnings("resource")
    private List<T> executeQuery(String query, Object[] parameters, RowMapper<T> mapper, boolean onlyFirst) throws BussinesException {
        List<T> result = new ArrayList<T>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            validQuery(query);
            open();
            statement = connection.prepareStatement(query);

            if (parameters != null) {
                for (int i = 1; i <= parameters.length; i++) {
                    statement.setObject(i, parameters[i - 1]);
                    LOGGER.log(Level.FINE, "Index: " + i + ", Value: " + parameters[i - 1].toString());
                }
            }

            resultSet = statement.executeQuery();
            List<Metadata> metadata = getMetadata(resultSet);

            while (resultSet.next()) {
                result.add(mapper.proccess(resultSet, metadata));
                if (onlyFirst && result.size() > 1) {
                    result = null;
                    throw new SQLException("Se encontro mas de un resultado");
                }
            }

        } catch (Exception ex) {
            throw new BussinesException(ex);
        } finally {
            close(resultSet, "ResultSet executeQuery");
            close(statement, "Statement executeQuery");
            close();
        }

        return result;
    }

    public List<T> executeQuery(String query, Object[] parameters, RowMapper<T> mapper) throws BussinesException {
        return executeQuery(query, parameters, mapper, false);
    }

    public List<T> executeQuery(String query, RowMapper<T> mapper) throws BussinesException {
        return executeQuery(query, null, mapper, false);
    }

    public T executeQueryUniqueResult(String query, RowMapper<T> mapper) throws BussinesException {
        List<T> results = executeQuery(query, null, mapper, true);
        return results.isEmpty() ? null : results.get(0);
    }

    public T executeQueryUniqueResult(String query, Object[] parameters, RowMapper<T> mapper) throws BussinesException {
        List<T> results = executeQuery(query, parameters, mapper, true);
        return results.isEmpty() ? null : results.get(0);
    }

    public String executeQueryScalarString(String query) throws BussinesException {
        SQLUtil<String> sqlUtil = new SQLUtil<String>(this.jndiName);
        RowMapper<String> rowMapper = new StringMapperImpl();
        return sqlUtil.executeQueryUniqueResult(query, rowMapper);
    }

    public String executeQueryScalarString(String query, Object[] params) throws BussinesException {
        SQLUtil<String> sqlUtil = new SQLUtil<String>(this.jndiName);
        RowMapper<String> rowMapper = new StringMapperImpl();
        return sqlUtil.executeQueryUniqueResult(query, params, rowMapper);
    }
    
    private void open() throws BussinesException {
        open(!opened);
    }
    
    private void open(boolean opened) throws BussinesException {
        if(!opened) {
            LOGGER.log(Level.WARNING, "SQLUtil:open:Conexion abierta externamente");
            return;
        }
        
        LOGGER.log(Level.WARNING, "SQLUtil:open:" + jndiName);
        try {
            connection = createConnection(jndiName);
        } catch (SQLException e) {
            throw new BussinesException(e);
        }
    }

    private void close() throws BussinesException {
        close(!opened);
    }
    
    private void close(boolean opened) throws BussinesException {
        if(!opened) {
            LOGGER.log(Level.WARNING, "SQLUtil:close:Conexion abierta externamente");
            return;
        }
        
        LOGGER.log(Level.WARNING, "SQLUtil:close:" + jndiName);
        close(preparedStatement, "preparedStatement");
        close(connection, "closeConnection");
    }
    
    public boolean commit() {
        LOGGER.log(Level.WARNING, "SQLUtil:commit:" + jndiName);
        boolean result = true;
        try {
            connection.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "", e);
            result = false;
        }
        return result;
    }

    public boolean rollback() {
        LOGGER.log(Level.WARNING, "SQLUtil:rollback:" + jndiName);
        boolean result = true;
        try {
            connection.rollback();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "", e);
            result = false;
        }
        return result;
    }

    public boolean preparedBatch(String query) {
        boolean result = false;

        try {
            validQuery(query);
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query);
            result = true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "SQLUtil:preparedBatch", ex);
        }

        return result;
    }

    public SQLUtil<T> addBatch(List<Object> objects) throws SQLException {
        int i = 1;
        for (Object o : objects) {
            preparedStatement.setObject(i, o);
            i++;
        }
        preparedStatement.addBatch();
        return this;
    }

    public int[] executeBatch() throws SQLException {
        return preparedStatement.executeBatch();
    }
}

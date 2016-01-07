package com.pe.bbva.pyme.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pe.bbva.pyme.dao.DocumentoDAO;
import com.pe.bbva.pyme.model.Documento;

@Repository("documentoDAO")
public class SimpleDocumentoDAO implements DocumentoDAO {

    private static final Logger LOG = Logger.getLogger(SimpleDocumentoDAO.class);
    
    @Resource(name = "dataSource")
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    
    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setFetchSize(10);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Documento> listarDocumentos(boolean hasContent, Date creationDate) {
        // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = creationDate == null ? new Date() : creationDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();
        
        String sql = "select a.tenantid, a.id, a.author, a.creationdate, a.hascontent, a.filename, a.mimetype, a.url, a.content, b.processinstanceid from public.document a inner join public.document_mapping b on a.id=b.documentid where hascontent=? and date_trunc('day', to_timestamp(creationdate / 1000)) = ?" +
                " union all " +
                "select a.tenantid, a.id, a.author, a.creationdate, a.hascontent, a.filename, a.mimetype, a.url, a.content, b.processinstanceid from public.document a inner join public.arch_document_mapping b on a.id=b.documentid where hascontent=? and date_trunc('day', to_timestamp(creationdate / 1000)) = ?";
        return jdbcTemplate.query(sql, new Object[]{hasContent, date, hasContent, date}, new RowMapper<Documento>() {

            @Override
            public Documento mapRow(ResultSet resultset, int rowNum) throws SQLException {
                Documento documento = new Documento();
                
                documento.setTenantId(resultset.getLong("TENANTID"));
                documento.setId(resultset.getLong("ID"));
                documento.setAuthor(resultset.getLong("AUTHOR"));
                documento.setCreationDate(resultset.getLong("CREATIONDATE"));
                documento.setHasContent(resultset.getBoolean("HASCONTENT"));
                documento.setFilename(resultset.getString("FILENAME"));
                documento.setMimetype(resultset.getString("MIMETYPE"));
                documento.setUrl(resultset.getString("URL"));
                documento.setContent(resultset.getBytes("CONTENT"));
                documento.setProcessInstanceId(resultset.getLong("PROCESSINSTANCEID"));

                return documento;
            }
        });
    }

    @Override
    @Transactional(readOnly = false)
    public void actualizarDocumento(final List<Documento> documentos) {
        int[] result = jdbcTemplate.batchUpdate("update public.document set hascontent = false, mimetype = null, url = ?, content = null where tenantid=? and id=?", new BatchPreparedStatementSetter() {
            
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Documento doc = documentos.get(i);
                ps.setString(1, doc.getUrl());
                ps.setLong(2, doc.getTenantId());
                ps.setLong(3, doc.getId());
            }
            
            @Override
            public int getBatchSize() {
                return documentos.size();
            }
        });
        
        for(int i : result) {
            LOG.info("Result: " + i);
        }
    }

}

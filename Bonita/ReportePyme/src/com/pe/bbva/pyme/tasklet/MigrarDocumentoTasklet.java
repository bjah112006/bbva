package com.pe.bbva.pyme.tasklet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.DocumentoRequest;
import pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.DocumentoResponse;
import pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.EbsContentManagerSB12Client;

import com.pe.bbva.pyme.dao.DocumentoDAO;
import com.pe.bbva.pyme.model.Documento;
import com.pe.bbva.pyme.utils.DBUtil;

public class MigrarDocumentoTasklet implements Tasklet, InitializingBean {
    
    private static final Logger LOG = Logger.getLogger(MigrarDocumentoTasklet.class);
    private static final int BATCH_SIZE = 100;
    private DataSource dataSource;
    private DocumentoDAO documentoDAO;
    private EbsContentManagerSB12Client clienteContentWS;
    private String endpoint;

    @Override
    public void afterPropertiesSet() throws Exception {
        clienteContentWS = new EbsContentManagerSB12Client();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public DocumentoDAO getDocumentoDAO() {
        return documentoDAO;
    }

    public void setDocumentoDAO(DocumentoDAO documentoDAO) {
        this.documentoDAO = documentoDAO;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        endpoint = DBUtil.obtenerParametroDetalle(dataSource.getConnection(), "18", "001");
        RepeatStatus status = RepeatStatus.FINISHED;
        StepExecution execution = chunkContext.getStepContext().getStepExecution();
        ExitStatus exitStatus = ExitStatus.COMPLETED;
        List<Documento> documentos = documentoDAO.listarDocumentos(true, execution.getJobParameters().getDate("fechaProceso"));
        List<Documento> migrados = new ArrayList<Documento>();
        Documento doc;
        DocumentoRequest docRequest;
        DocumentoResponse docResponse;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for(int i = 0; i < documentos.size(); i++) {
            contribution.incrementReadCount();
            doc = documentos.get(i);
            docRequest = new DocumentoRequest("WFFP", "SYS", "001", doc.getId().toString());
            docRequest.put("nombreArchivo", doc.getFilename())
                      .put("mimetype", doc.getMimetype())
                      .put("mandatorio", "1")
                      .put("origen", "FNE")
                      .put("fechaCreacion", sdf.format(new Date(doc.getCreationDate())));
            
            try {
                docResponse = clienteContentWS.procesar(docRequest, doc.getContent(), endpoint);
                if(docResponse != null) {
                    doc.setUrl(docResponse.get("documento_url"));
                    migrados.add(doc);
                }
            } catch(Exception e) {
                LOG.error(doc + "\n", e);
            }
            
            if(migrados.size() == BATCH_SIZE) {
                documentoDAO.actualizarDocumento(migrados);
                migrados = new ArrayList<Documento>();
                contribution.incrementWriteCount(BATCH_SIZE);
                execution.incrementCommitCount();
                LOG.info("COMMIT: " + BATCH_SIZE);
            }
        }
        if(!migrados.isEmpty()) {
            documentoDAO.actualizarDocumento(migrados);
            contribution.incrementWriteCount(migrados.size());
            execution.incrementCommitCount();
            LOG.info("COMMIT: " + migrados.size());
        }
        
        contribution.setExitStatus(exitStatus);
        execution.setExitStatus(exitStatus);
        execution.getJobExecution().setExitStatus(exitStatus);
        
        return status;
    }

}

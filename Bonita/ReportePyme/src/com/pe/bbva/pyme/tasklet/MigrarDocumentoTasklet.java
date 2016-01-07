package com.pe.bbva.pyme.tasklet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private String activarWebService;
    private String directorioTemporal;
    private String url;

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

    private String completarSeparador(String directorio) {
        String result = directorio.replace("\\", "/");
        if (!result.endsWith("/")) {
            result += "/";
        }

        return result;
    }
    
    private boolean escribirArchivo(String archivo, byte[] valor) {
        boolean result = false;
        File file = new File(archivo);
        BufferedOutputStream output = null;

        try {
            if (file.exists()) {
                if (!file.isDirectory()) {
                    throw new IOException(file.getAbsolutePath() + " es un directorio.");
                }
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists() && !parentFile.mkdirs()) {
                    throw new IOException("Creacion de directorios " + parentFile.getPath() + " fallida");
                }
            }

            ByteArrayInputStream in = new ByteArrayInputStream(valor);
            output = new BufferedOutputStream(new FileOutputStream(file));
            int read = 0;
            int i = 0;
            byte[] buffer = new byte[1024];
            while ((read = in.read(buffer)) >= 0) {
                output.write(buffer, 0, read);
                i++;
                if ((i % 4) == 0) {
                    output.flush();
                }
            }
            output.flush();
            result = true;
        } catch (Exception e) {
            LOG.error("ArchivoUtil:escribirArchivo", e);
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                LOG.error("Archivo:cerrar:" + file.getPath(), e);
            }
        }

        return result;
    }
    
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        endpoint = DBUtil.obtenerParametroDetalle(dataSource.getConnection(), "18", "001");
        activarWebService = DBUtil.obtenerParametroDetalle(dataSource.getConnection(), "18", "003");
        directorioTemporal = completarSeparador(DBUtil.obtenerParametroDetalle(dataSource.getConnection(), "18", "005"));
        url = completarSeparador(DBUtil.obtenerParametroDetalle(dataSource.getConnection(), "18", "006"));

        RepeatStatus status = RepeatStatus.FINISHED;
        StepExecution execution = chunkContext.getStepContext().getStepExecution();
        ExitStatus exitStatus = ExitStatus.COMPLETED;
        List<Documento> documentos = documentoDAO.listarDocumentos(true, execution.getJobParameters().getDate("fechaProceso"));
        List<Documento> migrados = new ArrayList<Documento>();
        Documento doc;
        Map<String, String> cliente;
        DocumentoRequest docRequest;
        DocumentoResponse docResponse;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfDir = new SimpleDateFormat("yyyy/MM/dd/");
        String archivo;
        
        for(int i = 0; i < documentos.size(); i++) {
            contribution.incrementReadCount();
            doc = documentos.get(i);
            LOG.error(doc);
            cliente = DBUtil.obtenerDatosCliente(doc.getProcessInstanceId());
            
            if("1".equalsIgnoreCase(activarWebService)) {
                docRequest = new DocumentoRequest("WFFP", "SYS", "001", doc.getId().toString());
                docRequest.put("nombreArchivo", doc.getFilename())
                          .put("mimetype", doc.getMimetype())
                          .put("mandatorio", "1")
                          .put("origen", "FNE")
                          .put("fechaCreacion", sdf.format(new Date(doc.getCreationDate())))
                          .put("id", doc.getId().toString())
                          .put("numExpediente", doc.getProcessInstanceId().toString())
                          .put("tipo", "-")
                          .put("tipoDOI", "R")
                          .put("numDOI", cliente.get("numDOI"))
                          .put("codigoCliente", cliente.get("codigoCliente"));
                try {
                    docResponse = clienteContentWS.procesar(docRequest, doc.getContent(), endpoint);
                    if(docResponse != null) {
                        doc.setUrl(docResponse.get("documento_url"));
                        migrados.add(doc);
                    }
                } catch(Exception e) {
                    LOG.error("Error: \n", e);
                }
            } else {
                archivo = directorioTemporal + sdfDir.format(new Date(doc.getCreationDate())) + doc.getProcessInstanceId() + "/" + doc.getFilename();
                if(escribirArchivo(archivo, doc.getContent())) {
                    doc.setUrl(url + archivo);
                    migrados.add(doc);
                }
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

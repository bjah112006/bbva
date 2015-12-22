package com.pe.bbva.pyme.batch.service.impl;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Service;

import com.indra.mail.MailException;
import com.indra.mail.MailExceptionCodes;
import com.indra.mail.Message;
import com.indra.mail.MessageBody;
import com.indra.mail.MessageHeader;
import com.indra.mail.filter.FilterChainMail;
import com.indra.mail.manager.MailManager;
import com.indra.util.TrazaUtil;
import com.pe.bbva.pyme.batch.service.NotificacionService;

@Service("notificacionService")
public class NotificacionServiceImpl implements NotificacionService, Serializable {

    private static final Logger LOGGER = Logger.getLogger(NotificacionServiceImpl.class);
    private static final long serialVersionUID = 1L;
    private MailManager mailManager;
    
    public NotificacionServiceImpl() {
        mailManager = new MailManager();
        mailManager.setFilterChainMail(new FilterChainMail());
    }

    @Override
    public boolean notificar(JobExecution jobExecution) {
        boolean result = true;
        
        try {
            LOGGER.info("JobInstance.id: " + jobExecution.getJobInstance().getId());
            LOGGER.info("JobId: " + jobExecution.getJobId());
            LOGGER.info("Id: " + jobExecution.getId());

            if (!"COMPLETED".equalsIgnoreCase(jobExecution.getExitStatus().getExitCode())) {
                MessageHeader header = new MessageHeader();
                /*
                header.setHost(params[0].getValor());
                header.setPort(params[1].getValor());
                header.setUserFrom(params[2].getValor());
                header.setEmailFrom(params[2].getValor());
                header.setListTO(params[4].getValor());
                
                if (params[3] != null) {
                    header.setPasswordFrom(params[3].getValor());
                } else {
                    header.setPasswordFrom("");
                }
                */
                MessageBody body = new MessageBody();
                StringBuilder sb = new StringBuilder();

                header.setSubject("Migraci\u00F3n de Documentos - Notificaci\u00F3n de Error: " + jobExecution.getJobInstance().getJobName());
                sb.append("Ocurri\u00F3 un error al ejecutar el proceso:<br><br>");

                if (jobExecution.getFailureExceptions().isEmpty()) {
                    for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
                        for (Throwable e : stepExecution.getFailureExceptions()) {
                            sb.append(TrazaUtil.mostrarMensajeHTMLSoloCabecera(new Exception(e)));
                            sb.append("<br>");
                        }
                    }
                } else {
                    for (Throwable e : jobExecution.getFailureExceptions()) {
                        sb.append(TrazaUtil.mostrarMensajeHTMLSoloCabecera(new Exception(e)));
                        sb.append("<br>");
                    }
                }
                
                sb.append("Por favor no responder a este correo.");
                body.setMessage(sb.toString());
                LOGGER.info(body.getMessage());

                Message message = new Message();
                message.setHeader(header);
                message.setBody(body);

                mailManager.getFilterChainMail().send(message);
            }
        } catch (MailException e) {
            if(MailExceptionCodes.NOT_SEND.compareTo(e.getError()) != 0) {
                LOGGER.error("No se pudo enviar", e);
            }
            result = false;
        } catch (Exception e) {
            LOGGER.error("Error al obtener los parametros de configuraci\u00F3n", e);
            result = false;
        }

        return result;
    }
}

package com.indra.mail;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class Mail {

  
    private static final Logger LOGGER = Logger.getLogger(Mail.class);
    private Message message;
    private MimeMessage mime;

    public Mail() {
        this(null);
    }

    public Mail(Message message) {
        this.message = message;
    }

    public void send() throws MailException {
        if (mime != null) {
            if (message == null) {
                throw new MailException(new IllegalArgumentException("Message Null"), MailExceptionCodes.INVALID_PARAMETER, this.message);
            }

            try {
                mime.setContent(message.getBody().getMimeMultipart());
                Transport.send(mime);
            } catch (MessagingException e) {
                LOGGER.info(message.getBody().getMessage());
                throw new MailException(e, MailExceptionCodes.NOT_SEND, this.message);
            }
        } else {
            throw new MailException(new IllegalArgumentException("Mime Null"), MailExceptionCodes.INVALID_ADDRESS, this.message);
        }
    }

    public Message getMessage() {
        return message;
    }

    private Session createSession() {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", message.getHeader().getHost());
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", message.getHeader().getPort());
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.debug", LOGGER.isDebugEnabled() ? "true" : "false");

        return Session.getInstance(props, new MailAuthenticator(message));
    }

    private void setEmailFrom(MimeMessage mimeMessage, Message message) throws  MessagingException {
        if (message.getHeader().getEmailFrom() != null) {
            mimeMessage.setFrom(new InternetAddress(message.getHeader().getEmailFrom()));
        }
    }

    private void setRecipientTypeTO(MimeMessage mimeMessage, Message message) throws  MessagingException {
        if (message.getHeader().getListTO() != null && message.getHeader().getListTO().length() > 0) {
            mimeMessage.addRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(message.getHeader().getListTO()));
        }
    }

    private void setRecipientTypeCC(MimeMessage mimeMessage, Message message) throws MessagingException {
        if (message.getHeader().getListCC() != null && message.getHeader().getListCC().length() > 0) {
            mimeMessage.addRecipients(javax.mail.Message.RecipientType.CC, message.getHeader().getListCC());
        }
    }

    private void setRecipientTypeBCC(MimeMessage mimeMessage, Message message) throws MessagingException {
        if (message.getHeader().getListCO() != null && message.getHeader().getListCO().length() > 0) {
            mimeMessage.addRecipients(javax.mail.Message.RecipientType.BCC, message.getHeader().getListTO());
        }
    }

    private void setSubject(MimeMessage mimeMessage, Message message) throws MessagingException {
        if (message.getHeader().getSubject() != null && message.getHeader().getSubject().length() > 0) {
            mimeMessage.setSubject(message.getHeader().getSubject(), "utf-8");
        }
    }

    private void printParameters() {
        if(message != null && message.getHeader() != null) {
            LOGGER.debug("To: " + message.getHeader().getListTO());
            LOGGER.debug("CC: " + message.getHeader().getListCC());
            LOGGER.debug("CO: " + message.getHeader().getListCO());
            LOGGER.debug("Subject: " + message.getHeader().getSubject());
            LOGGER.debug("Host: " + message.getHeader().getHost());
            LOGGER.debug("Port: " + message.getHeader().getPort());
        }
    }

    public void setMessage(Message message) {
        if(message == null) {
            return;
        }

        this.message = message;
        printParameters();

        Session session = createSession();
        this.mime = new MimeMessage(session);

        try {
            setEmailFrom(this.mime, this.message);
            setRecipientTypeTO(this.mime, this.message);
            setRecipientTypeCC(this.mime, this.message);
            setRecipientTypeBCC(this.mime, this.message);
            setSubject(this.mime, this.message);
        } catch (AddressException e) {
            LOGGER.error("Direccion Invalida", e);
        } catch (MessagingException e) {
            LOGGER.error("Mensaje con error", e);
        }
    }
}

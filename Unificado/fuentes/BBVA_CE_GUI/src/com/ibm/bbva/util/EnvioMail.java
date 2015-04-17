package com.ibm.bbva.util;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ibm.bbva.service.Constantes;
import com.ibm.bbva.session.ParametrosConfBeanLocal;

import java.util.Properties;

public class EnvioMail extends Authenticator{
	
	private static final Logger LOG = LoggerFactory.getLogger(EnvioMail.class);
	private String remitenteMail = null;
	private String password="";
	
	private ParametrosConfBeanLocal parametrosConfBean;
	
	public void enviarMail(String asunto, InternetAddress[] listAddresses, String to, String mensaje) {
		
		LOG.info("---------- Inicio Método enviarMail ------------");
		String hostServerMail = null;
		String puertoServerMail = null;
		String remitenteMail = null;
				
		try{
			LOG.info("Consultar en TBL_CE_IBM_PARAMETROS_CONF: Host, Puerto, Remitente");
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			hostServerMail = parametrosConfBean.buscarPorVariable(Constantes.COD_ENVIOMAIL_HOST, Constantes.NOMBRE_VARIABLE_MAILHOST).getValorVariable();
			LOG.info("hostServerMail"+hostServerMail);
			puertoServerMail = parametrosConfBean.buscarPorVariable(Constantes.COD_ENVIOMAIL_PUERTO, Constantes.NOMBRE_VARIABLE_MAILPUERTO).getValorVariable();
			LOG.info("puertoServerMail"+puertoServerMail);
			remitenteMail = parametrosConfBean.buscarPorVariable(Constantes.COD_ENVIOMAIL_REMITENTE, Constantes.NOMBRE_VARIABLE_MAILREMITENTE).getValorVariable();
			LOG.info("remitenteMail "+ remitenteMail);
			
		}catch (NamingException e){
			e.printStackTrace();
			LOG.error("Error al enviar el Mail..faltan parametros", e);
		}
		
		LOG.info("Host: " + hostServerMail);
		LOG.info("Puerto: " + puertoServerMail);
		LOG.info("Remitente: " + remitenteMail);
		LOG.info("Asunto: " + asunto);
		LOG.info("Destinatario: " + to);
		LOG.info("Cuerpo: " + mensaje);
		
		
		if(listAddresses != null){
			for(int i = 0; i < listAddresses.length; i++)     
				LOG.info("Email: " + listAddresses[i]);
		}else{
			LOG.info("Email: " + to);
		}
				
		try{
			LOG.info("Enviar Mail...");
			Properties properties = new Properties();
			LOG.info("Instancio properties");
			//properties.setProperty("mail.smtp.host", hostServerMail);
			//properties.setProperty("mail.smtp.port", puertoServerMail);
			Session session = Session.getDefaultInstance(properties);
			session.getProperties().setProperty("mail.smtp.host", hostServerMail);
			LOG.info("mail.smtp.host" + session.getProperties().getProperty("mail.smtp.host", hostServerMail));
			session.getProperties().setProperty("mail.smtp.port", puertoServerMail);
			LOG.info("mail.smtp.port" + session.getProperties().getProperty("mail.smtp.port", puertoServerMail));
			
			// compose the message
			MimeMessage message = new MimeMessage(session);			
			message.setFrom(new InternetAddress(remitenteMail));
			
			if(listAddresses != null){
				message.addRecipients(Message.RecipientType.TO, listAddresses);
			}else{
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			}
			
			message.setSubject(asunto);
			
			message.setContent(mensaje, "text/html");
			
			
			// Send message
			Transport.send(message);
			LOG.info("Se envió el Mail satisfactoriamente.");
			
			
		}catch(MessagingException e){
			LOG.error("Error al enviar el Mail..", e);
			
		}		
		LOG.info("---------- Fin Método enviarMail ------------");
		
	}

	public String getRemitenteMail() {
		return remitenteMail;
	}

	public void setRemitenteMail(String remitenteMail) {
		this.remitenteMail = remitenteMail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	 
	 
	 
}

package com.indra.mail.handler;

import java.io.Serializable;

import com.indra.mail.MailException;
import com.indra.mail.Message;

public interface HandlerMail extends Serializable {

    void init();

    void error(MailException exception, Message message);

    void end();
}

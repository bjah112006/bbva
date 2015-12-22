package com.indra.mail.filter;

import com.indra.mail.MailException;
import com.indra.mail.Message;

public interface FilterMail {

    void sendBefore(Message message) throws MailException;

    void sendAfter(Message message) throws MailException;

    void sendError(Message message, Exception e) throws MailException;
}

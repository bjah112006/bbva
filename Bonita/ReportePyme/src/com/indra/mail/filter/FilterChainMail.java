package com.indra.mail.filter;

import com.indra.mail.Mail;
import com.indra.mail.MailException;
import com.indra.mail.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterChainMail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Mail target;
    private List<FilterMail> filters;

    public FilterChainMail() {
        filters = new ArrayList<FilterMail>();
        target = new Mail();
    }

    public void send(Message message) throws MailException {
        for (FilterMail filter : filters) {
            filter.sendBefore(message);
        }

        target.setMessage(message);
        try {
            target.send();
        } catch (Exception e) {
            for (FilterMail filter : filters) {
                filter.sendError(message, e);
            }
        }

        for (FilterMail filter : filters) {
            filter.sendAfter(message);
        }
    }
}

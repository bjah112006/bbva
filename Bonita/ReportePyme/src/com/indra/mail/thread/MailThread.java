package com.indra.mail.thread;

import org.apache.log4j.Logger;

import com.indra.mail.MailException;
import com.indra.mail.Message;
import com.indra.mail.manager.MailManager;

public class MailThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(MailThread.class);
    private MailManager manager;

    public MailThread(MailManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void run() {

        if (manager.getHandler() != null) {
            manager.getHandler().init();
        }

        Message ms = null;
        try {
            for (Message m : manager.getMessages()) {
                ms = m;
                manager.getFilterChainMail().send(m);
            }
        } catch (MailException e) {
            LOGGER.error("No se pudo enviar", e);
            if (manager.getHandler() != null) {
                manager.getHandler().error(e, ms);
            }
        }

        if (manager.getHandler() != null) {
            manager.getHandler().end();
        }
    }
}

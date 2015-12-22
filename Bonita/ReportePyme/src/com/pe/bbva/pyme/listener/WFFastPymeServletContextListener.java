package com.pe.bbva.pyme.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WFFastPymeServletContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(WFFastPymeServletContextListener.class);
    private static WebApplicationContext springContext;

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        springContext = null;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("Iniciando el listener...");
        springContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
    }

    public static ApplicationContext getApplicationContext() {
        return springContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) springContext.getBean(beanName);
    }

}

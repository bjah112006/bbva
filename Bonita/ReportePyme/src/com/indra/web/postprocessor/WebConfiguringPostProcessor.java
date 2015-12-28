package com.indra.web.postprocessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class WebConfiguringPostProcessor implements BeanPostProcessor {
    
    private static final Logger LOG = Logger.getLogger(WebConfiguringPostProcessor.class);
    private Properties props;
    
    public WebConfiguringPostProcessor() {
        try {
            LOG.info("btm.root ======>" + System.getProperty("btm.root"));
            LOG.info("bonita.home ===>" + System.getProperty("bonita.home"));
            props = new Properties();
            props.load(new FileInputStream(System.getProperty("btm.root").replace("\\", "/") + "/conf/bitronix-resources.properties"));
        } catch (IOException e) {
            LOG.error("WebConfiguringPostProcessor", e);
        }
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) {
        if (bean instanceof HttpMessageConverter<?>) {
            if (bean instanceof MappingJacksonHttpMessageConverter) {
                ((MappingJacksonHttpMessageConverter) bean).setPrettyPrint(true);
            } else if (bean instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) bean).setPrettyPrint(true);
            }
        } else if(bean instanceof DriverManagerDataSource) {
            String serverName = props.getProperty("resource.ds1.driverProperties.serverName");
            String portNumber = props.getProperty("resource.ds1.driverProperties.portNumber");
            String databaseName = props.getProperty("resource.ds1.driverProperties.databaseName");
            String user = props.getProperty("resource.ds1.driverProperties.user");
            String password = props.getProperty("resource.ds1.driverProperties.password");
            
            DriverManagerDataSource dataSource = (DriverManagerDataSource) bean;
            dataSource.setUrl("jdbc:postgresql://" + serverName + ":" + portNumber + "/" + databaseName);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
        }
        
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}

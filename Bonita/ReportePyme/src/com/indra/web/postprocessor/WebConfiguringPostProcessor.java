package com.indra.web.postprocessor;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

public class WebConfiguringPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) {
        if (bean instanceof HttpMessageConverter<?>) {
            if (bean instanceof MappingJacksonHttpMessageConverter) {
                ((MappingJacksonHttpMessageConverter) bean).setPrettyPrint(true);
            } else if (bean instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) bean).setPrettyPrint(true);
            }
        }
        
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}

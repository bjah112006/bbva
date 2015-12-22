package com.pe.bbva.pyme.tasklet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pe.bbva.pyme.batch.service.JobInstanceService;

public class TestTasklet {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        JobInstanceService jobInstanceService = (JobInstanceService) applicationContext.getBean("jobInstanceService");
        jobInstanceService.execute("migrarDocumentosJob");
        ((ClassPathXmlApplicationContext) applicationContext).close();
    }
}

package com.pe.bbva.pyme.controller;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.indra.util.TrazaUtil;
import com.pe.bbva.pyme.batch.service.JobInstanceService;
import com.pe.bbva.pyme.controller.model.Resultado;
import com.pe.bbva.pyme.controller.model.SchedulerModel;
import com.pe.bbva.pyme.quartz.ExecuteJob;
import com.pe.bbva.pyme.quartz.domain.JobConfig;
import com.pe.bbva.pyme.quartz.factory.QuartzFactory;

import flexjson.JSONSerializer;

@Controller("schedulerController")
@Scope("prototype")
@RequestMapping(value = "scheduler")
public class SchedulerController implements Serializable {

    private static final Logger LOG = Logger.getLogger(SchedulerController.class);
    private static final long serialVersionUID = 1L;

    @Resource(name = "quartzFactory")
    private QuartzFactory quartzFactory;

    @Resource(name = "jobInstanceService")
    private JobInstanceService jobInstanceService;

    @Resource(name = "jobExplorer")
    private JobExplorer jobExplorer;
    
    @RequestMapping(value = "index")
    public String index(ModelMap model) {
        model.addAttribute("schedulerClass", "ui-state-active-bbva");
        model.addAttribute("jobClass", "");
        return "scheduler/index";
    }

    @RequestMapping(value = "listar", method = RequestMethod.POST)
    public @ResponseBody String listar() {
        String result = "";

        try {
            SchedulerModel schedulerModel = new SchedulerModel();
            schedulerModel.setTipoResultado(Resultado.EXITO);
            schedulerModel.setTriggerInstances(jobInstanceService.listTrigger());
            result = this.renderModelJson(schedulerModel);
        } catch (Exception e) {
            result = this.renderErrorSistema(e);
        }

        return result;
    }

    @RequestMapping(value = "rescheduler/{jobName}", method = RequestMethod.POST)
    public @ResponseBody String rescheduler(@PathVariable("jobName") String jobName, HttpServletRequest request) {
        String result = "{\"rescheduler\": \"OK\"}"; 
        String cron = request.getParameter("cron");
        try {
            if(jobName.equalsIgnoreCase("migrarDocumentosJob")) {
                JobConfig jobConfig = new JobConfig();
                jobConfig.setJobName(jobName);
                jobConfig.setGroupName("FastPyme");
                jobConfig.setCronExpresion(cron);
                
                quartzFactory.rescheduler(jobConfig, ExecuteJob.class);
            } else {
                result = "{\"rescheduler\": \"KO\", \"message\": \"Job invalid\"}";
            }
        } catch(Exception e) {
            result = "{\"rescheduler\": \"KO\", \"message\": \"" + exceptionToString(e) + "\"}";
        }
        
        return result;
    }

    @RequestMapping(value = "execute/{jobName}", method = RequestMethod.POST)
    public @ResponseBody String execute(@PathVariable("jobName") final String jobName, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String result = "{\"execute\": \"OK\"}"; 
        final Date date;
        try {
            date = sdf.parse(request.getParameter("date"));
            if(jobName.equalsIgnoreCase("migrarDocumentosJob")) {
                Thread thread = new Thread(new Runnable() {
                    
                    @Override
                    public void run() {
                        jobInstanceService.execute(jobName, date);
                    }
                });
                
                thread.start();
                result = "{\"execute\": \"OK\", \"message\": \"Job started\"}";
            } else {
                result = "{\"execute\": \"KO\", \"message\": \"Job invalid\"}";
            }
        } catch(Exception e) {
            result = "{\"execute\": \"KO\", \"message\": \"" + exceptionToString(e) + "\"}";
        }
        
        return result;
    }
    
    @RequestMapping(value = "detail/{jobName}", method = RequestMethod.POST)
    public @ResponseBody String detail(ModelMap model, @PathVariable("jobName") String jobName) {
        String result; 
        
        try {
            SchedulerModel schedulerModel = new SchedulerModel();
            Long id = jobInstanceService.obtainLastExecution(jobName);
            JobInstance jobInstance = jobExplorer.getJobInstance(id);
        
            schedulerModel.setTipoResultado(Resultado.EXITO);
            if(id != null) {
                schedulerModel.setRunningJobInstances(jobExplorer.getJobExecutions(jobInstance));
            }
            schedulerModel.setJobInstance(jobInstance);
            schedulerModel.setTriggerInstances(jobInstanceService.listTrigger());
            result = this.renderModelJson(schedulerModel);
        } catch(Exception e) {
            result = this.renderErrorSistema(e);
        }
        
        return result;
    }
    
    private String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter(0);
        PrintWriter out = new PrintWriter(sw);
        e.printStackTrace(out);
        LOG.error("", e);
        return sw.toString();
    }
    
    private String renderModelJson(Object object) {
        return renderModelJsonDeepExclude(object, new String[] { "*.class" });
    }

    private String renderModelJsonDeepExclude(Object object, String[] exclude) {
        String json = "";
        try {
            json = new JSONSerializer().exclude(exclude).deepSerialize(object);
        } catch (Exception e) {
            LOG.error("GenericController:renderModelJsonDeepExclude", e);
            StringBuilder sb = new StringBuilder();
            sb.append("{\"mensaje\": \"");
            sb.append(TrazaUtil.mostrarMensajeHTML(e));
            sb.append("\", \"tipoResultado\": \"ERROR_SISTEMA\"}");
            json = sb.toString();
        }
        LOG.debug(json);
        return json;
    }
    
    public String renderErrorSistema(Exception ex) {
        LOG.error("renderErrorSistema", ex);
        SchedulerModel baseModel = new SchedulerModel();
        baseModel.setMensaje(TrazaUtil.mostrarMensajeHTML(ex));
        baseModel.setTipoResultado(Resultado.ERROR_SISTEMA);
        return this.renderModelJson(baseModel);
    }
}
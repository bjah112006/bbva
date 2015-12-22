package com.pe.bbva.pyme.quartz.dao.impl;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pe.bbva.pyme.quartz.dao.TriggerDAO;
import com.pe.bbva.pyme.quartz.domain.Trigger;

/**
 * Clase para acceder a los datos de los trabajos configurados mediante el uso
 * de la vista QRTZ_VW_TRIGGERS
 * 
 * @author jquedena
 * 
 */
@Repository("triggerDAO")
public class TriggerDAOImpl implements Serializable, TriggerDAO {

    private static final long serialVersionUID = 1L;
    // private static final Logger LOG = Logger.getLogger(TriggerDAOImpl.class);
    
    @Resource(name = "dataSource")
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    
    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Transactional(readOnly = true)
    @Override
    public long obtainLastExecution(String jobName) {
        String sql = "select max(job_instance_id) job_instance_id from batch.batch_job_instance where job_name = ?";
        return jdbcTemplate.queryForLong(sql, jobName);
    }
    
    /**
     * Lista los trabajos configurados mediante el uso de la vista
     * QRTZ_VW_TRIGGERS
     * 
     * @return La lista de trabajos configurados
     */
    @Transactional(readOnly = true)
    @Override
    public List<Trigger> listar() {
        StringBuilder sb = new StringBuilder();
        sb.append("select x.trigger_name ");
        sb.append(", x.trigger_group ");
        sb.append(", x.job_name ");
        sb.append(", x.job_group ");
        sb.append(", x.next_fire_time ");
        sb.append(", x.trigger_state ");
        sb.append(", x.trigger_type ");
        sb.append(", x.start_time ");
        sb.append(", x.end_time ");
        sb.append(", z.exit_code ");
        sb.append(" from public.qrtz_triggers x"); 
        sb.append(" left join(");
        sb.append(" select a.job_name, max(a.job_instance_id) job_instance_id from batch.batch_job_instance a");
        sb.append(" group by a.job_name");
        sb.append(") y on x.job_name=y.job_name");
        sb.append(" left join batch.batch_job_execution z on y.job_instance_id=z.job_instance_id");
        sb.append(" order by x.trigger_group, x.trigger_name");
        return jdbcTemplate.query(sb.toString(), new RowMapper<Trigger>() {

            @Override
            public Trigger mapRow(ResultSet resultset, int rowNum) throws SQLException {
                Trigger trigger = new Trigger();
                
                trigger
                    .setTriggerName(resultset.getString("TRIGGER_NAME"))
                    .setTriggerGroup(resultset.getString("TRIGGER_GROUP"))
                    .setJobName(resultset.getString("JOB_NAME"))
                    .setJobGroup(resultset.getString("JOB_GROUP"))
                    .setNextFireTime(resultset.getLong("NEXT_FIRE_TIME"))
                    .setTriggerState(resultset.getString("TRIGGER_STATE"))
                    .setTriggerType(resultset.getString("TRIGGER_TYPE"))
                    .setStartTime(resultset.getLong("START_TIME"))
                    .setEndTime(resultset.getLong("END_TIME"))
                    .setExitCode(resultset.getString("EXIT_CODE"));

                return trigger;
            }
        });
    }
}

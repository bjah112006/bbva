--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: batch; Type: SCHEMA; Schema: -; Owner: bonita_db_user_db_user
--

CREATE SCHEMA batch;


ALTER SCHEMA batch OWNER TO bonita_db_user;

SET search_path = batch, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: batch_job_execution; Type: TABLE; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

CREATE TABLE batch_job_execution (
    job_execution_id bigint NOT NULL,
    version bigint,
    job_instance_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status character varying(10),
    exit_code character varying(100),
    exit_message character varying(2500),
    last_updated timestamp without time zone
);


ALTER TABLE batch.batch_job_execution OWNER TO bonita_db_user;

--
-- Name: batch_job_execution_context; Type: TABLE; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

CREATE TABLE batch_job_execution_context (
    job_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text
);


ALTER TABLE batch.batch_job_execution_context OWNER TO bonita_db_user;

--
-- Name: batch_job_execution_params; Type: TABLE; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

CREATE TABLE batch_job_execution_params (
    job_execution_id bigint NOT NULL,
    type_cd character varying(6) NOT NULL,
    key_name character varying(100) NOT NULL,
    string_val character varying(250),
    date_val timestamp without time zone,
    long_val bigint,
    double_val double precision,
    identifying character(1) NOT NULL
);


ALTER TABLE batch.batch_job_execution_params OWNER TO bonita_db_user;

--
-- Name: batch_job_execution_seq; Type: SEQUENCE; Schema: batch; Owner: bonita_db_user
--

CREATE SEQUENCE batch_job_execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE batch.batch_job_execution_seq OWNER TO bonita_db_user;

--
-- Name: batch_job_instance; Type: TABLE; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

CREATE TABLE batch_job_instance (
    job_instance_id bigint NOT NULL,
    version bigint,
    job_name character varying(100) NOT NULL,
    job_key character varying(32) NOT NULL
);


ALTER TABLE batch.batch_job_instance OWNER TO bonita_db_user;

--
-- Name: batch_job_seq; Type: SEQUENCE; Schema: batch; Owner: bonita_db_user
--

CREATE SEQUENCE batch_job_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE batch.batch_job_seq OWNER TO bonita_db_user;

--
-- Name: batch_step_execution; Type: TABLE; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

CREATE TABLE batch_step_execution (
    step_execution_id bigint NOT NULL,
    version bigint NOT NULL,
    step_name character varying(100) NOT NULL,
    job_execution_id bigint NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone,
    status character varying(10),
    commit_count bigint,
    read_count bigint,
    filter_count bigint,
    write_count bigint,
    read_skip_count bigint,
    write_skip_count bigint,
    process_skip_count bigint,
    rollback_count bigint,
    exit_code character varying(100),
    exit_message character varying(2500),
    last_updated timestamp without time zone
);


ALTER TABLE batch.batch_step_execution OWNER TO bonita_db_user;

--
-- Name: batch_step_execution_context; Type: TABLE; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

CREATE TABLE batch_step_execution_context (
    step_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text
);


ALTER TABLE batch.batch_step_execution_context OWNER TO bonita_db_user;

--
-- Name: batch_step_execution_seq; Type: SEQUENCE; Schema: batch; Owner: bonita_db_user
--

CREATE SEQUENCE batch_step_execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE batch.batch_step_execution_seq OWNER TO bonita_db_user;

--
-- Name: batch_job_execution_context_pkey; Type: CONSTRAINT; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

ALTER TABLE ONLY batch_job_execution_context
    ADD CONSTRAINT batch_job_execution_context_pkey PRIMARY KEY (job_execution_id);


--
-- Name: batch_job_execution_pkey; Type: CONSTRAINT; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

ALTER TABLE ONLY batch_job_execution
    ADD CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id);


--
-- Name: batch_job_instance_pkey; Type: CONSTRAINT; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

ALTER TABLE ONLY batch_job_instance
    ADD CONSTRAINT batch_job_instance_pkey PRIMARY KEY (job_instance_id);


--
-- Name: batch_step_execution_context_pkey; Type: CONSTRAINT; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

ALTER TABLE ONLY batch_step_execution_context
    ADD CONSTRAINT batch_step_execution_context_pkey PRIMARY KEY (step_execution_id);


--
-- Name: batch_step_execution_pkey; Type: CONSTRAINT; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

ALTER TABLE ONLY batch_step_execution
    ADD CONSTRAINT batch_step_execution_pkey PRIMARY KEY (step_execution_id);


--
-- Name: job_inst_un; Type: CONSTRAINT; Schema: batch; Owner: bonita_db_user; Tablespace: 
--

ALTER TABLE ONLY batch_job_instance
    ADD CONSTRAINT job_inst_un UNIQUE (job_name, job_key);


--
-- Name: job_exec_ctx_fk; Type: FK CONSTRAINT; Schema: batch; Owner: bonita_db_user
--

ALTER TABLE ONLY batch_job_execution_context
    ADD CONSTRAINT job_exec_ctx_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id);


--
-- Name: job_exec_params_fk; Type: FK CONSTRAINT; Schema: batch; Owner: bonita_db_user
--

ALTER TABLE ONLY batch_job_execution_params
    ADD CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id);


--
-- Name: job_exec_step_fk; Type: FK CONSTRAINT; Schema: batch; Owner: bonita_db_user
--

ALTER TABLE ONLY batch_step_execution
    ADD CONSTRAINT job_exec_step_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id);


--
-- Name: job_inst_exec_fk; Type: FK CONSTRAINT; Schema: batch; Owner: bonita_db_user
--

ALTER TABLE ONLY batch_job_execution
    ADD CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id) REFERENCES batch_job_instance(job_instance_id);


--
-- Name: step_exec_ctx_fk; Type: FK CONSTRAINT; Schema: batch; Owner: bonita_db_user
--

ALTER TABLE ONLY batch_step_execution_context
    ADD CONSTRAINT step_exec_ctx_fk FOREIGN KEY (step_execution_id) REFERENCES batch_step_execution(step_execution_id);


--
-- PostgreSQL database dump complete
--

INSERT INTO tbl_pyme_parameter (id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (18, '001', '', 'Endpoint ContentWS' , 'http://118.180.188.27:9080/ContentManagerEARWeb/ContentManagerServiceSB11', 1);
INSERT INTO tbl_pyme_parameter (id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (18, '002', '', 'migrarDocumentosJob', '0 18 10 1 * ? *', 1);
INSERT INTO tbl_pyme_parameter (id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (18, '003', '', 'activarWebService'  , '0', 1);
INSERT INTO tbl_pyme_parameter (id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (18, '004', '', 'activarJob'         , '0', 1);
INSERT INTO tbl_pyme_parameter (id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (18, '005', '', 'directorioTemporal' , '/mnt/bonitafiles', 1);
INSERT INTO tbl_pyme_parameter (id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (18, '006', '', 'url' , 'https://extranetperu.grupobbva.pe/ReportePyme/download?url=', 1);

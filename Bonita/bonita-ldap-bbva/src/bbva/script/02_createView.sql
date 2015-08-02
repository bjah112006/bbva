-- drop extension tablefunc;
create extension tablefunc schema public version "1.0";

-- drop schema if exists fastpyme;
create schema fastpyme;

-- drop view if exists fastpyme.data_instance_detail;
create view fastpyme.data_instance_detail as
select containerid, containertype, tenantid, name,
case discriminant
when 'SDateDataInstanceImpl' then longvalue::text
else clobvalue end "value"
from public.data_instance
order by containerid, tenantid;

-- drop view if exists fastpyme.task_pending;
create view fastpyme.task_pending as
select 
  a.name
, a.version 
, b.tenantid
, b.rootprocessinstanceid
, b.startdate
, c.actorid
, d.name actor
, c.flownodedefinitionid
, c.name taskname
, c.stateid
, c.statename
, c.assigneeid
from public.process_definition a
inner join public.process_instance b on a.tenantid=b.tenantid and a.processid=b.processdefinitionid
inner join public.flownode_instance c on b.tenantid=c.tenantid and b.rootprocessinstanceid=c.rootcontainerid
inner join public.actor d on c.tenantid=d.tenantid and c.actorid=d.id;

create view fastpyme.data_instance as
select * from public.crosstab(
  'select containerid, containertype, tenantid, name, "value" from fastpyme.data_instance_detail order by containerid, tenantid',
  'select distinct name from fastpyme.data_instance_detail order by 1'
) as (
       containerid bigint,
       containertype character varying(60),
       tenantid bigint,
       fechaRegistro text,
       fechaResolucion text,
       motivoTramite text,
       nombreSolicitante text,
       nroDocumentoSolicitante text,
       resultadoEvaluacion text,
       tipoDocumentos text,
       tipoDocumentoSolicitante text
);

select * from fastpyme.task_pending a
inner join fastpyme.data_instance b on a.tenantid=b.tenantid and a.rootprocessinstanceid=b.containerid;

/*
select * from fastpyme.data_instance;

select * from public.data_instance;

select * from public.actor;

select * from public.actormember;

select * from public.user_;

select * from public.user_membership;

select * from public.arch_flownode_instance;

select b.name, a.* from public.flownode_instance a
inner join public.actor b on a.actorid=b.id and a.tenantid=b.tenantid;

select * from public.crosstab
(
  'select containerid, containertype, tenantid, name,
case discriminant
when ''SDateDataInstanceImpl'' then longvalue::text
else clobvalue end "value"
from public.data_instance ORDER BY containerid, tenantid',
  'SELECT DISTINCT name FROM public.data_instance ORDER BY 1'
)
AS
(
       containerid bigint,
       containertype character varying(60),
       tenantid bigint,
       fechaRegistro text,
       fechaResolucion text,
       motivoTramite text,
       nombreSolicitante text,
       nroDocumentoSolicitante text,
       resultadoEvaluacion text,
       tipoDocumentos text,
       tipoDocumentoSolicitante text
);

-- select * from public.process_definition;
-- select * from public.process_instance;
*/
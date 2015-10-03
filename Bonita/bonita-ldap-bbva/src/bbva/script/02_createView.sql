-- drop extension tablefunc;
-- create extension tablefunc schema public version "1.0";

drop schema if exists fastpyme cascade;
create schema fastpyme authorization bonita;
--ALTER SCHEMA fastpyme OWNER TO bonita;
    
-- drop view if exists fastpyme.data_instance_detail;
create view fastpyme.data_instance_detail as
select containerid, containertype, tenantid, name,
case discriminant
when 'SDateDataInstanceImpl' then longvalue::text
else clobvalue end "value"
from public.data_instance
order by containerid, tenantid;

-- drop view if exists fastpyme.data_instance_detail;
create view fastpyme.arch_data_instance_detail as
select containerid, containertype, tenantid, name,
case discriminant
when 'SDateDataInstanceImpl' then longvalue::text
else clobvalue end "value"
from public.arch_data_instance
order by containerid, tenantid;

-- drop view if exists fastpyme.task_pending;
create or replace view fastpyme.task_pending as
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
, e.username
, e.firstname
, e.lastname
, case d.name
  when 'GBN' then 'OFICINA'
  when 'ERC' then 'RIESGO'
  when 'JGR' then 'RIESGO'
  when 'ERM' then 'RIESGO'
  when 'GMC' then 'MESA DE CONTROL'
  when 'CPM' then 'CPM'
  else '' end estacion
, '{"url": "/portal/homepage#?id=' || b.rootprocessinstanceid|| '&_p=casemoredetails&_pf=1", "value": "' || b.rootprocessinstanceid || '"}' url
from public.process_definition a
inner join public.process_instance b on a.tenantid=b.tenantid and a.processid=b.processdefinitionid
inner join public.flownode_instance c on b.tenantid=c.tenantid and b.rootprocessinstanceid=c.rootcontainerid
inner join public.actor d on c.tenantid=d.tenantid and c.actorid=d.id
left join public.user_ e on c.tenantid=e.tenantid and c.assigneeid=e.id;

-- drop view if exists fastpyme.arch_task_pending;
create or replace view fastpyme.arch_task_pending as
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
, e.username
, e.firstname
, e.lastname
, case d.name
  when 'GBN' then 'OFICINA'
  when 'ERC' then 'RIESGO'
  when 'JGR' then 'RIESGO'
  when 'ERM' then 'RIESGO'
  when 'GMC' then 'MESA DE CONTROL'
  when 'CPM' then 'CPM'
  else '' end estacion
, '{"url": "/portal/homepage#?id=' || b.id || '&_p=archivedcasemoredetailsadmin&_pf=1", "value": "' || b.rootprocessinstanceid || '"}' url
from public.process_definition a
inner join public.arch_process_instance b on a.tenantid=b.tenantid and a.processid=b.processdefinitionid
inner join (
	select rootprocessinstanceid, max(id) id from public.arch_process_instance where stateid=6 group by rootprocessinstanceid
) bx on b.id=bx.id
inner join public.arch_flownode_instance c on b.tenantid=c.tenantid and b.sourceobjectid=c.sourceobjectid -- b.rootprocessinstanceid=c.rootcontainerid
inner join public.actor d on c.tenantid=d.tenantid and c.actorid=d.id
left join public.user_ e on c.tenantid=e.tenantid and c.assigneeid=e.id
where b.stateid=6 and c.stateid not in(4, 32);

-- select * from arch_flownode_instance
-- select * from flownode_instance
-- select stateid, state_name from public.arch_process_instance

/*
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
*/

-- drop view if exists fastpyme.data_instance;
create or replace view fastpyme.data_instance as
select containerid
, containertype
, tenantid
, max(case name when 'tipo_doi_cliente' then value else '' end) tipo_doi_cliente
, max(case name when 'num_doi_cliente' then value else '' end) num_doi_cliente
, max(case name when 'nombre_cliente' then value else '' end) nombre_cliente
, max(case name when 'estado_solicitud' then value else '' end) estado_solicitud
, max(case name when 'oferta_aprobada' then value else '' end) oferta_aprobada
, max(case name when 'ofi_registro' then value else '' end) ofi_registro
, max(case name when 'num_rvgl' then value else '' end) num_rvgl
, max(case name when 'producto' then value else '' end) producto
, max(case name when 'campania' then value else '' end) campania
, max(case name when 'clte_clasificacion' then value else '' end) clte_clasificacion
, max(case name when 'num_tramite' then value else '' end) num_tramite
, max(case name when 'usu_registrante' then value else '' end) usu_registrante
, max(case name when 'ambito_registrante' then value else '' end) ambito_registro
, max(case name when 'codigo_centro_negocio' then value else '' end) codigo_centro_negocio
, max(case name when 'codigo_cliente' then value else '' end) codigo_cliente
from fastpyme.data_instance_detail
where containertype='PROCESS_INSTANCE'
group by containerid, containertype, tenantid;

-- drop view if exists fastpyme.arch_data_instance;
create or replace view fastpyme.arch_data_instance as
select containerid
, containertype
, tenantid
, max(case name when 'tipo_doi_cliente' then value else '' end) tipo_doi_cliente
, max(case name when 'num_doi_cliente' then value else '' end) num_doi_cliente
, max(case name when 'nombre_cliente' then value else '' end) nombre_cliente
, max(case name when 'estado_solicitud' then value else '' end) estado_solicitud
, max(case name when 'oferta_aprobada' then value else '' end) oferta_aprobada
, max(case name when 'ofi_registro' then value else '' end) ofi_registro
, max(case name when 'num_rvgl' then value else '' end) num_rvgl
, max(case name when 'producto' then value else '' end) producto
, max(case name when 'campania' then value else '' end) campania
, max(case name when 'clte_clasificacion' then value else '' end) clte_clasificacion
, max(case name when 'num_tramite' then value else '' end) num_tramite
, max(case name when 'usu_registrante' then value else '' end) usu_registrante
, max(case name when 'ambito_registrante' then value else '' end) ambito_registro
, max(case name when 'codigo_centro_negocio' then value else '' end) codigo_centro_negocio
, max(case name when 'codigo_cliente' then value else '' end) codigo_cliente
from fastpyme.arch_data_instance_detail
where containertype='PROCESS_INSTANCE'
group by containerid, containertype, tenantid;

-- drop view if exists fastpyme.instance;
create or replace view fastpyme.instance as
select
      a.name
    , a.version 
    , a.tenantid
    , a.rootprocessinstanceid
    , a.startdate
    , a.actorid
    , a.actor
    , a.flownodedefinitionid
    , a.taskname
    , a.stateid
    , a.statename
    , a.assigneeid
    , a.username
    , a.firstname
    , a.lastname
    , case when a.estacion != 'OFICINA' then a.estacion else (case when length(b.usu_registrante) = 0 then 'OFICINA' else 'FUVEX' end) end estacion
    , b.tipo_doi_cliente
    , b.num_doi_cliente
    , b.nombre_cliente
    , b.estado_solicitud
    , b.oferta_aprobada
    , b.ofi_registro
    , b.num_rvgl
    , b.producto
    , b.campania
    , b.clte_clasificacion
    , b.num_tramite
    , b.usu_registrante
    , b.ambito_registro
    , a.url
    , b.codigo_centro_negocio
    , b.codigo_cliente
from fastpyme.task_pending a
inner join fastpyme.data_instance b on 
a.tenantid=b.tenantid and 
a.rootprocessinstanceid=b.containerid;

-- drop view if exists fastpyme.arch_instance;
create or replace view fastpyme.arch_instance as
select
      a.name
    , a.version 
    , a.tenantid
    , a.rootprocessinstanceid
    , a.startdate
    , a.actorid
    , a.actor
    , a.flownodedefinitionid
    , a.taskname
    , a.stateid
    , a.statename
    , a.assigneeid
    , a.username
    , a.firstname
    , a.lastname
    , case when a.estacion != 'OFICINA' then a.estacion else (case when length(b.usu_registrante) = 0 then 'OFICINA' else 'FUVEX' end) end estacion
    , b.tipo_doi_cliente
    , b.num_doi_cliente
    , b.nombre_cliente
    , b.estado_solicitud
    , b.oferta_aprobada
    , b.ofi_registro
    , b.num_rvgl
    , b.producto
    , b.campania
    , b.clte_clasificacion
    , b.num_tramite
    , b.usu_registrante
    , b.ambito_registro
    , a.url
    , b.codigo_centro_negocio
    , b.codigo_cliente
from fastpyme.arch_task_pending a
inner join fastpyme.arch_data_instance b on 
a.tenantid=b.tenantid and 
a.rootprocessinstanceid=b.containerid;

create view fastpyme.file_instance as
select 
	to_char('1970-01-01 00:00:00 GMT'::timestamp + ((c.creationdate/1000)::text)::interval, 'yyyy/MM/dd hh:mm:ss') as fecha_documento 	
	, a.tenantid
	, a.id as instance
	, a.name as name_instance
	, a.processdefinitionid as definition_id
	, b.id as document_mapping_id
	, b.documentid
	, b.name as filename_mapping
	, b.description as descripcion
	, c.filename
	, c.mimetype
	, c.url
from public.process_instance a  
inner join public.document_mapping b on a.id = b.PROCESSINSTANCEID  
inner join public.document c on b.documentid = c.id;


alter table fastpyme.arch_data_instance owner to bonita;
alter table fastpyme.arch_data_instance_detail owner to bonita;
alter table fastpyme.arch_task_pending owner to bonita;
alter table fastpyme.arch_instance owner to bonita;

alter table fastpyme.data_instance owner to bonita;
alter table fastpyme.data_instance_detail owner to bonita;
alter table fastpyme.task_pending owner to bonita;
alter table fastpyme.instance owner to bonita;

-- INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (10, '010', '', '|B23|B21|', 'Cargos que pertenecen a la oficina', 1);

/*
select *
from fastpyme.task_pending a
inner join fastpyme.data_instance b on a.tenantid=b.tenantid and a.rootprocessinstanceid=b.containerid and estacion='OFICINA';

select *
from fastpyme.arch_task_pending a
inner join fastpyme.arch_data_instance b on a.tenantid=b.tenantid and a.rootprocessinstanceid=b.containerid
where a.rootprocessinstanceid=10
order by a.tenantid, a.rootprocessinstanceid, actor;

select row_number() over(order by tenantid, rootprocessinstanceid, actor), *
from fastpyme.arch_instance where rootprocessinstanceid=10;

select row_number() over(order by tenantid, rootprocessinstanceid, actor), *
from fastpyme.instance where rootprocessinstanceid=10;

inner join public.user_ f on b.tenantid=f.tenantid and b.startedby=f.if
left join custom_usr_inf_val g on f.tenantid=g.tenantid and f.id=g.userid and g.definitionid=3

-- nro
rootprocessinstanceid
num_rvgl
num_doi_cliente
num_tramite

    	//TODO: CPM
    	public static final String DESEMBOLSADO = "DESEMBOLSADO";
    	public static final String EN_ESPERA_TRAMITE = "EN ESPERA TRAMITE";
    	public static final String APROBADO_CON_MODIFICACION = "APROBADO CON MODIFICACION";
    	public static final String APROBADO_SIN_MODIFICACION = "APROBADO SIN MODIFICACION";
    	
    	//TODO: RIESGOS
    	public static final String ENVIADO_A_EVALUACION = "ENVIADO A EVALUACION";
    	public static final String ENVIADO_ASIGNACION_EVALUADOR = "ENVIADO ASIGNACION EVALUADOR";
    	public static final String ENVIADO_A_VISITA_CAMPO = "ENVIADO A VISITA CAMPO";
    	public static final String TRANSFERIDO = "TRANSFERIDO";
    	public static final String VISITA_DE_CAMPO_REALIZADO = "VISITA DE CAMPO REALIZADO";
    	public static final String AUTORIZACION_ESCALADA = "AUTORIZACION ESCALADA";
    	
    	//TODO: MESA CONTROL
    	public static final String ENVIADO_A_MESA_CONTROL = "ENVIADO A MESA CONTROL";
    	
    	//TODO: OFICINA/FUVEX
    	public static final String DEVUELTO_POR_MESA_CONTROL = "DEVUELTO POR MESA CONTROL";
    	public static final String APROBADO_CON_MODIFICACION_POR_CONFIRMAR = "APROBADO CON MODIFICACION POR CONFIRMAR";
    	public static final String REQUISITO_OBSERVADO = "REQUISITO OBSERVADO";
*/

/*
select * from fastpyme.data_instance;

select * from public.data_instance;

select * from public.actor;

select * from public.actormember;

select * from public.user_;

select * from public.user_membership;

select * from public.arch_flownode_instance where rootcontainerid=10 and stateid=2;

select * from public.arch_process_instance where rootprocessinstanceid=10;

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
-- select startedby from public.process_instance;
*/

/*
select * from user_ a
inner join custom_usr_inf_val b on a.id=b.userid -- and definitionid=3
where a.id in(
select startedby from public.process_instance
);
 
select  * from public.custom_usr_inf_val
select  * from public.custom_usr_inf_def

select * from public.user_
select * from public.arch_data_instance
select * from public.arch_process_instance

"P007395"
"P012866"
"XP88810"
"P017336"
"P018795"

" 6282-ADECCO"
"0712 - MALL AV.PZA S.ANITA"
"0242 - DAMERO GAMARRA"
"0396 - COVIDA"
"0486 - San Isidro"

*/

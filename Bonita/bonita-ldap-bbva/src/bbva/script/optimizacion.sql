-- Index: idx3_arch_data_instance
-- DROP INDEX idx3_arch_data_instance;
CREATE INDEX idx3_arch_data_instance ON arch_data_instance USING btree (name COLLATE pg_catalog."default");

-- Index: idx4_arch_data_instance
-- DROP INDEX idx4_arch_data_instance;
CREATE INDEX idx4_arch_data_instance ON arch_data_instance USING btree (containerid, archivedate, containertype COLLATE pg_catalog."default", tenantid, name COLLATE pg_catalog."default");

-- Index: idx4_arch_process_instance
-- DROP INDEX idx4_arch_process_instance;
CREATE INDEX idx4_arch_process_instance ON arch_process_instance USING btree (id);

CREATE OR REPLACE FUNCTION fastpyme.string_to_rows(text, text) RETURNS SETOF TEXT AS $$ 
  DECLARE
    elems text[];      
  BEGIN
    elems := string_to_array($1, $2);
    FOR i IN array_lower(elems, 1) .. array_upper(elems, 1) LOOP
      RETURN NEXT elems[i];
    END LOOP;
    RETURN;
  END
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION fastpyme.fn_instance(
    IN p_name character varying,
    IN p_value character varying,
    IN p_id numeric,
    IN p_estacion character varying,
    IN p_username character varying,
    IN p_oficina character varying,
    IN p_ambito character varying,
    IN p_rownum numeric,
    IN p_posicion character varying
)
  RETURNS TABLE(
	name character varying
	, version character varying
	, tenantid bigint
	, rootprocessinstanceid bigint
	, startdate bigint
	, actorid bigint
	, actor character varying
	, flownodedefinitionid bigint
	, taskname character varying
	, stateid integer
	, statename character varying
	, assigneeid bigint
	, username character varying
	, firstname character varying
	, lastname character varying
	, estacion text
	, tipo_doi_cliente text
	, num_doi_cliente text
	, nombre_cliente text
	, estado_solicitud text
	, oferta_aprobada text
	, ofi_registro text
	, num_rvgl text
	, producto text
	, campania text
	, clte_clasificacion text
	, num_tramite text
	, usu_registrante text
	, ambito_registro text
	, url text
	, codigo_centro_negocio text
	, codigo_cliente text
	, rownum bigint
	, type_instance text) AS
$BODY$
begin
    return query
		with data_instance_filter as (
			select distinct ax0.containerid
			from public.data_instance ax0
			where (p_id = -1 or ax0.containerid=p_id) or (ax0.name=p_name and ax0.clobvalue=p_value)
		), data_instance_pivot as (
			select ax3.containerid
				, ax3.containertype
				, ax3.tenantid
				, max(case ax3.name when 'tipo_doi_cliente' then value else '' end) tipo_doi_cliente
				, max(case ax3.name when 'num_doi_cliente' then value else '' end) num_doi_cliente
				, max(case ax3.name when 'nombre_cliente' then value else '' end) nombre_cliente
				, max(case ax3.name when 'estado_solicitud' then value else '' end) estado_solicitud
				, max(case ax3.name when 'oferta_aprobada' then value else '' end) oferta_aprobada
				, max(case ax3.name when 'ofi_registro' then value else '' end) ofi_registro
				, max(case ax3.name when 'num_rvgl' then value else '' end) num_rvgl
				, max(case ax3.name when 'producto' then value else '' end) producto
				, max(case ax3.name when 'campania' then value else '' end) campania
				, max(case ax3.name when 'clte_clasificacion' then value else '' end) clte_clasificacion
				, max(case ax3.name when 'num_tramite' then value else '' end) num_tramite
				, max(case ax3.name when 'usu_registrante' then value else '' end) usu_registrante
				, max(case ax3.name when 'ambito_registrante' then value else '' end) ambito_registro
				, max(case ax3.name when 'codigo_centro_negocio' then value else '' end) codigo_centro_negocio
				, max(case ax3.name when 'codigo_cliente' then value else '' end) codigo_cliente
			from (
				select ax2.containerid, ax2.containertype, ax2.tenantid, ax2.name, 
				case discriminant
					when 'SDateDataInstanceImpl' then longvalue::text
					else clobvalue end "value", -1 archivedate
				from public.data_instance ax2, data_instance_filter bx2
				where ax2.containerid=bx2.containerid and
					ax2.containertype='PROCESS_INSTANCE' and
					ax2.name in(
						  'tipo_doi_cliente'
						, 'num_doi_cliente'
						, 'nombre_cliente'
						, 'estado_solicitud'
						, 'oferta_aprobada'
						, 'ofi_registro'
						, 'num_rvgl'
						, 'producto'
						, 'campania'
						, 'clte_clasificacion'
						, 'num_tramite'
						, 'usu_registrante'
						, 'ambito_registrante'
						, 'codigo_centro_negocio'
						, 'codigo_cliente'
					)
			) ax3
			group by ax3.containerid, ax3.containertype, ax3.tenantid
		), instance as (
			select
				  gx4.name
				, gx4.version 
				, bx4.tenantid
				, bx4.rootprocessinstanceid
				, bx4.startdate
				, dx4.actorid
				, ex4.name actor
				, dx4.flownodedefinitionid
				, dx4.name taskname
				, dx4.stateid
				, dx4.statename
				, dx4.assigneeid
				, fx4.username
				, fx4.firstname
				, fx4.lastname
				, case ex4.name
					  when 'GBN' then 
						case when length(ax4.usu_registrante) = 0 then 'OFICINA' else 'FUVEX' end
					  when 'ERC' then 'RIESGO'
					  when 'JGR' then 'RIESGO'
					  when 'ERM' then 'RIESGO'
					  when 'GMC' then 'MESA DE CONTROL'
					  when 'CPM' then 'CPM'
					  else '' end estacion
				, ax4.tipo_doi_cliente
				, ax4.num_doi_cliente
				, ax4.nombre_cliente
				, ax4.estado_solicitud
				, ax4.oferta_aprobada
				, ax4.ofi_registro
				, ax4.num_rvgl
				, ax4.producto
				, ax4.campania
				, ax4.clte_clasificacion
				, ax4.num_tramite
				, ax4.usu_registrante
				, ax4.ambito_registro
				, '{"url": "/portal/homepage#?id=' || bx4.rootprocessinstanceid || '&_p=casemoredetails&_pf=1", "value": "' || bx4.rootprocessinstanceid || '"}' url
				, ax4.codigo_centro_negocio
				, ax4.codigo_cliente
				, row_number() over(order by bx4.rootprocessinstanceid) row_num
				, 'P'::text type_instance
			from data_instance_pivot ax4
				inner join public.process_instance bx4 on bx4.tenantid=ax4.tenantid and bx4.rootprocessinstanceid=ax4.containerid and bx.name='FAST NEGOCIOS'
				inner join public.flownode_instance dx4 on dx4.tenantid=bx4.tenantid and dx4.rootcontainerid=bx4.rootprocessinstanceid and dx4.stateid in( 4, 3 )
				inner join public.process_definition gx4 on bx4.processdefinitionid=gx4.processid and gx4.name='FAST NEGOCIOS'
				left join public.actor ex4 on dx4.tenantid=ex4.tenantid and dx4.actorid=ex4.id
				left join public.user_ fx4 on dx4.tenantid=fx4.tenantid and dx4.assigneeid=fx4.id
			where
				(p_oficina = '-1' or ax4.ofi_registro in( select string_to_rows from fastpyme.string_to_rows(p_oficina, '|') )) and
				(p_ambito = '-1' or ax4.ambito_registro in( select string_to_rows from fastpyme.string_to_rows(p_ambito, '|') )) and
				(p_estacion = '-1' or case ex4.name
					when 'GBN' then 
						case when length(ax4.usu_registrante) = 0 then 'OFICINA' else 'FUVEX' end
					when 'ERC' then 'RIESGO'
					when 'JGR' then 'RIESGO'
					when 'ERM' then 'RIESGO'
					when 'GMC' then 'MESA DE CONTROL'
					when 'CPM' then 'CPM'
					else '' end = p_estacion) and
				(p_username = '-1' or fx4.username = p_username)
		)
		select * from instance where (p_posicion = 'S' and row_num > p_rownum) or (p_posicion = 'A' and row_num <= p_rownum) limit 10;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 10;

CREATE OR REPLACE FUNCTION fastpyme.fn_arch_instance(
    IN p_name character varying,
    IN p_value character varying,
    IN p_id numeric,
    IN p_estacion character varying,
    IN p_username character varying,
    IN p_oficina character varying,
    IN p_ambito character varying,
    IN p_rownum numeric,
    IN p_posicion character varying)
  RETURNS TABLE(
	name character varying
	, version character varying
	, tenantid bigint
	, rootprocessinstanceid bigint
	, startdate bigint
	, actorid bigint
	, actor character varying
	, flownodedefinitionid bigint
	, taskname character varying
	, stateid integer
	, statename character varying
	, assigneeid bigint
	, username character varying
	, firstname character varying
	, lastname character varying
	, estacion text
	, tipo_doi_cliente text
	, num_doi_cliente text
	, nombre_cliente text
	, estado_solicitud text
	, oferta_aprobada text
	, ofi_registro text
	, num_rvgl text
	, producto text
	, campania text
	, clte_clasificacion text
	, num_tramite text
	, usu_registrante text
	, ambito_registro text
	, url text
	, codigo_centro_negocio text
	, codigo_cliente text
	, rownum bigint
	, type_instance text) AS
$BODY$
begin
    return query
		with arch_data_instance_filter as (
			select distinct ax0.containerid
			from public.arch_data_instance ax0
			where (p_id = -1 or ax0.containerid=p_id) or (ax0.name=p_name and ax0.clobvalue=p_value)
		), arch_data_instance_archive as (
			select ax1.containerid, ax1.containertype, ax1.tenantid, ax1.name, max(ax1.archivedate) archivedate
			from public.arch_data_instance ax1 inner join arch_data_instance_filter bx1 on ax1.containerid=bx1.containerid
			where ax1.containertype='PROCESS_INSTANCE' and
				ax1.name in(
					  'tipo_doi_cliente'
					, 'num_doi_cliente'
					, 'nombre_cliente'
					, 'estado_solicitud'
					, 'oferta_aprobada'
					, 'ofi_registro'
					, 'num_rvgl'
					, 'producto'
					, 'campania'
					, 'clte_clasificacion'
					, 'num_tramite'
					, 'usu_registrante'
					, 'ambito_registrante'
					, 'codigo_centro_negocio'
					, 'codigo_cliente'
				)
			group by ax1.containerid, ax1.containertype, ax1.tenantid, ax1.name
		), arch_data_instance_pivot as (
			select ax3.containerid
				, ax3.containertype
				, ax3.tenantid
				, max(case ax3.name when 'tipo_doi_cliente' then value else '' end) tipo_doi_cliente
				, max(case ax3.name when 'num_doi_cliente' then value else '' end) num_doi_cliente
				, max(case ax3.name when 'nombre_cliente' then value else '' end) nombre_cliente
				, max(case ax3.name when 'estado_solicitud' then value else '' end) estado_solicitud
				, max(case ax3.name when 'oferta_aprobada' then value else '' end) oferta_aprobada
				, max(case ax3.name when 'ofi_registro' then value else '' end) ofi_registro
				, max(case ax3.name when 'num_rvgl' then value else '' end) num_rvgl
				, max(case ax3.name when 'producto' then value else '' end) producto
				, max(case ax3.name when 'campania' then value else '' end) campania
				, max(case ax3.name when 'clte_clasificacion' then value else '' end) clte_clasificacion
				, max(case ax3.name when 'num_tramite' then value else '' end) num_tramite
				, max(case ax3.name when 'usu_registrante' then value else '' end) usu_registrante
				, max(case ax3.name when 'ambito_registrante' then value else '' end) ambito_registro
				, max(case ax3.name when 'codigo_centro_negocio' then value else '' end) codigo_centro_negocio
				, max(case ax3.name when 'codigo_cliente' then value else '' end) codigo_cliente
			from (
				select ax2.containerid, ax2.containertype, ax2.tenantid, ax2.name, 
				case discriminant
					when 'SDateDataInstanceImpl' then longvalue::text
					else clobvalue end "value", ax2.archivedate
				from public.arch_data_instance ax2, arch_data_instance_archive bx2
				where ax2.containerid=bx2.containerid
					and ax2.archivedate=bx2.archivedate
					and ax2.containertype=bx2.containertype
					and ax2.tenantid=bx2.tenantid
					and ax2.name=bx2.name
			) ax3
			group by ax3.containerid, ax3.containertype, ax3.tenantid
		), arch_instance as (
			select
				  gx4.name
				, gx4.version 
				, bx4.tenantid
				, bx4.rootprocessinstanceid
				, bx4.startdate
				, dx4.actorid
				, ex4.name actor
				, dx4.flownodedefinitionid
				, dx4.name taskname
				, dx4.stateid
				, dx4.statename
				, dx4.assigneeid
				, fx4.username
				, fx4.firstname
				, fx4.lastname
				, case ex4.name
					  when 'GBN' then 
						case when length(ax4.usu_registrante) = 0 then 'OFICINA' else 'FUVEX' end
					  when 'ERC' then 'RIESGO'
					  when 'JGR' then 'RIESGO'
					  when 'ERM' then 'RIESGO'
					  when 'GMC' then 'MESA DE CONTROL'
					  when 'CPM' then 'CPM'
					  else '' end estacion
				, ax4.tipo_doi_cliente
				, ax4.num_doi_cliente
				, ax4.nombre_cliente
				, ax4.estado_solicitud
				, ax4.oferta_aprobada
				, ax4.ofi_registro
				, ax4.num_rvgl
				, ax4.producto
				, ax4.campania
				, ax4.clte_clasificacion
				, ax4.num_tramite
				, ax4.usu_registrante
				, ax4.ambito_registro
				, '{"url": "/portal/homepage#?id=' || bx4.rootprocessinstanceid || '&_p=archivedcasemoredetails&_pf=1", "value": "' || bx4.rootprocessinstanceid || '"}' url
				, ax4.codigo_centro_negocio
				, ax4.codigo_cliente
				, row_number() over(order by bx4.rootprocessinstanceid) row_num
				, 'A'::text type_instance
			from arch_data_instance_pivot ax4
				inner join public.arch_process_instance bx4 on bx4.tenantid=ax4.tenantid and bx4.rootprocessinstanceid=ax4.containerid
				inner join (
					select ws1.tenantid, ws1.rootprocessinstanceid, max(ws1.id) id from public.arch_process_instance ws1 where ws1.stateid=6 group by ws1.tenantid, ws1.rootprocessinstanceid
				) cx4 on cx4.id=bx4.id
				inner join public.arch_flownode_instance dx4 on dx4.tenantid=cx4.tenantid and dx4.rootcontainerid=cx4.rootprocessinstanceid
				inner join (
					select ws2.tenantid, ws2.rootcontainerid, max(ws2.archivedate) archivedate from public.arch_flownode_instance ws2 where ws2.kind='user' and ws2.stateid not in(4, 32) group by ws2.tenantid, ws2.rootcontainerid
				) cx on dx4.rootcontainerid=cx.rootcontainerid and dx4.archivedate=cx.archivedate
				inner join public.process_definition gx4 on bx4.processdefinitionid=gx4.processid and gx4.name='FAST NEGOCIOS'
				left join public.actor ex4 on dx4.tenantid=ex4.tenantid and dx4.actorid=ex4.id
				left join public.user_ fx4 on dx4.tenantid=fx4.tenantid and dx4.assigneeid=fx4.id
			where
				(p_estacion = '-1' or case ex4.name
					when 'GBN' then 
						case when length(ax4.usu_registrante) = 0 then 'OFICINA' else 'FUVEX' end
					when 'ERC' then 'RIESGO'
					when 'JGR' then 'RIESGO'
					when 'ERM' then 'RIESGO'
					when 'GMC' then 'MESA DE CONTROL'
					when 'CPM' then 'CPM'
					else '' end = p_estacion) and
				(p_username = '-1' or fx4.username = p_username)
		)
		select * from arch_instance where (p_posicion = 'S' and row_num > p_rownum) or (p_posicion = 'A' and row_num <= p_rownum) limit 10;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 10;

CREATE OR REPLACE FUNCTION fastpyme.fn_count_instance(
    IN p_name character varying,
    IN p_value character varying,
    IN p_id numeric,
    IN p_estacion character varying,
    IN p_username character varying,
    IN p_oficina character varying,
    IN p_ambito character varying,
    IN p_rownum numeric,
    IN p_posicion character varying
)
  RETURNS TABLE(rows_count bigint) AS
$BODY$
begin
    return query 
		with data_instance_filter as (
			select distinct ax0.containerid
			from public.data_instance ax0
			where (p_id = -1 or ax0.containerid=p_id) or (ax0.name=p_name and ax0.clobvalue=p_value)
		), data_instance_pivot as (
			select ax3.containerid
				, ax3.containertype
				, ax3.tenantid
				, max(case ax3.name when 'ofi_registro' then value else '' end) ofi_registro
				, max(case ax3.name when 'usu_registrante' then value else '' end) usu_registrante
				, max(case ax3.name when 'ambito_registrante' then value else '' end) ambito_registro
				, max(case ax3.name when 'codigo_centro_negocio' then value else '' end) codigo_centro_negocio
			from (
				select ax2.containerid, ax2.containertype, ax2.tenantid, ax2.name, 
				case discriminant
					when 'SDateDataInstanceImpl' then longvalue::text
					else clobvalue end "value", -1 archivedate
				from public.data_instance ax2, data_instance_filter bx2
				where ax2.containerid=bx2.containerid and
					ax2.containertype='PROCESS_INSTANCE' and
					ax2.name in(
						  'ofi_registro'
						, 'usu_registrante'
						, 'ambito_registrante'
						, 'codigo_centro_negocio'
					)
			) ax3
			group by ax3.containerid, ax3.containertype, ax3.tenantid
		)
		select count(*) rows_count
		from data_instance_pivot ax4
			inner join public.process_instance bx4 on bx4.tenantid=ax4.tenantid and bx4.rootprocessinstanceid=ax4.containerid
			inner join public.flownode_instance dx4 on dx4.tenantid=bx4.tenantid and dx4.rootcontainerid=bx4.rootprocessinstanceid and dx4.stateid in( 4, 3 )
			inner join public.process_definition gx4 on bx4.processdefinitionid=gx4.processid and gx4.name='FAST NEGOCIOS'
			left join public.actor ex4 on dx4.tenantid=ex4.tenantid and dx4.actorid=ex4.id
			left join public.user_ fx4 on dx4.tenantid=fx4.tenantid and dx4.assigneeid=fx4.id
		where
			(p_oficina = '-1' or ax4.ofi_registro in( select string_to_rows from fastpyme.string_to_rows(p_oficina, '|') )) and
			(p_ambito = '-1' or ax4.ambito_registro in( select string_to_rows from fastpyme.string_to_rows(p_ambito, '|') )) and
			(p_estacion = '-1' or case ex4.name
				when 'GBN' then 
					case when length(ax4.usu_registrante) = 0 then 'OFICINA' else 'FUVEX' end
				when 'ERC' then 'RIESGO'
				when 'JGR' then 'RIESGO'
				when 'ERM' then 'RIESGO'
				when 'GMC' then 'MESA DE CONTROL'
				when 'CPM' then 'CPM'
				else '' end = p_estacion) and
			(p_username = '-1' or fx4.username = p_username);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION fastpyme.fn_count_arch_instance(
    IN p_name character varying,
    IN p_value character varying,
    IN p_id numeric,
    IN p_estacion character varying,
    IN p_username character varying,
    IN p_oficina character varying,
    IN p_ambito character varying,
    IN p_rownum numeric,
    IN p_posicion character varying)
  RETURNS TABLE(rows_count bigint) AS
$BODY$
begin
    return query
		with arch_data_instance_filter as (
			select distinct ax0.containerid
			from public.arch_data_instance ax0
			where (p_id = -1 or ax0.containerid=p_id) or (ax0.name=p_name and ax0.clobvalue=p_value)
		), arch_data_instance_archive as (
			select ax1.containerid, ax1.containertype, ax1.tenantid, ax1.name, max(ax1.archivedate) archivedate
			from public.arch_data_instance ax1 inner join arch_data_instance_filter bx1 on ax1.containerid=bx1.containerid
			where ax1.containertype='PROCESS_INSTANCE' and
				ax1.name in(
					  'ofi_registro'
					, 'usu_registrante'
					, 'ambito_registrante'
					, 'codigo_centro_negocio'
				)
			group by ax1.containerid, ax1.containertype, ax1.tenantid, ax1.name
		), arch_data_instance_pivot as (
			select ax3.containerid
				, ax3.containertype
				, ax3.tenantid
				, max(case ax3.name when 'ofi_registro' then value else '' end) ofi_registro
				, max(case ax3.name when 'usu_registrante' then value else '' end) usu_registrante
				, max(case ax3.name when 'ambito_registrante' then value else '' end) ambito_registro
				, max(case ax3.name when 'codigo_centro_negocio' then value else '' end) codigo_centro_negocio
			from (
				select ax2.containerid, ax2.containertype, ax2.tenantid, ax2.name, 
				case discriminant
					when 'SDateDataInstanceImpl' then longvalue::text
					else clobvalue end "value", ax2.archivedate
				from public.arch_data_instance ax2, arch_data_instance_archive bx2
				where ax2.containerid=bx2.containerid
					and ax2.archivedate=bx2.archivedate
					and ax2.containertype=bx2.containertype
					and ax2.tenantid=bx2.tenantid
					and ax2.name=bx2.name
			) ax3
			group by ax3.containerid, ax3.containertype, ax3.tenantid
		)
		select count(*) rows_count
		from arch_data_instance_pivot ax4
			inner join public.arch_process_instance bx4 on bx4.tenantid=ax4.tenantid and bx4.rootprocessinstanceid=ax4.containerid
			inner join (
				select ws1.tenantid, ws1.rootprocessinstanceid, max(ws1.id) id from public.arch_process_instance ws1 where ws1.stateid=6 group by ws1.tenantid, ws1.rootprocessinstanceid
			) cx4 on cx4.id=bx4.id
			inner join public.arch_flownode_instance dx4 on dx4.tenantid=cx4.tenantid and dx4.rootcontainerid=cx4.rootprocessinstanceid
			inner join (
				select ws2.tenantid, ws2.rootcontainerid, max(ws2.archivedate) archivedate from public.arch_flownode_instance ws2 where ws2.kind='user' and ws2.stateid not in(4, 32) group by ws2.tenantid, ws2.rootcontainerid
			) cx on dx4.rootcontainerid=cx.rootcontainerid and dx4.archivedate=cx.archivedate
			inner join public.process_definition gx4 on bx4.processdefinitionid=gx4.processid and gx4.name='FAST NEGOCIOS'
			left join public.actor ex4 on dx4.tenantid=ex4.tenantid and dx4.actorid=ex4.id
			left join public.user_ fx4 on dx4.tenantid=fx4.tenantid and dx4.assigneeid=fx4.id
		where
			(p_estacion = '-1' or case ex4.name
				when 'GBN' then 
					case when length(ax4.usu_registrante) = 0 then 'OFICINA' else 'FUVEX' end
				when 'ERC' then 'RIESGO'
				when 'JGR' then 'RIESGO'
				when 'ERM' then 'RIESGO'
				when 'GMC' then 'MESA DE CONTROL'
				when 'CPM' then 'CPM'
				else '' end = p_estacion) and
			(p_username = '-1' or fx4.username = p_username);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
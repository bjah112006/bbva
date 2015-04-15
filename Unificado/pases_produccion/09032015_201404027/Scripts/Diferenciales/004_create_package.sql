spool 003_create_package.log replace;
/*
	Reporte HISTORICO-CONSOLIDADO
*/

create or replace PACKAGE          "CONELE"."PG_CE_REPORTE_TC" AS 
	TYPE TYP_CURSOR IS REF CURSOR;
	unique_name         exception;
	not_found_pk_fk     exception;
	repeat_primary_key  exception;
	deleting_not_found  exception;
	updating_not_found  exception;

	PROCEDURE SP_SEL_REPORTETC
	(
    LIST_REPORTE		  OUT TYP_CURSOR,
    FECHA_INICIO			IN DATE,
    FECHA_FINAL       IN DATE,
    PRODUCTO          IN NUMBER,
    PERFIL            IN NUMBER,
    TIPO_OFERTA       IN NUMBER,
    ESTADO            IN NUMBER,
    TERRITORIO        IN NUMBER,
    OFICINA           IN NUMBER,
    TIPO_BUSQ         IN NUMBER
	);
END PG_CE_REPORTE_TC;



/*
	Reporte TOE
*/

/
create or replace PACKAGE          "CONELE"."PG_CE_REPORTE" AS 
	TYPE TYP_CURSOR IS REF CURSOR;
	unique_name         exception;
	not_found_pk_fk     exception;
	repeat_primary_key  exception;
	deleting_not_found  exception;
	updating_not_found  exception;

	PROCEDURE SP_SEL_REPORTETOE
	(	
    LIST_REPORTE		  OUT TYP_CURSOR,
	  FECHA_INICIO			IN DATE,
    FECHA_FINAL       IN DATE,
    PRODUCTO          IN NUMBER,
    PERFIL            IN NUMBER,
    TIPO_OFERTA       IN NUMBER,
    ESTADO            IN NUMBER,
    TERRITORIO        IN NUMBER,
    OFICINA           IN NUMBER,
    TIPO_BUSQ         IN NUMBER,
    TIPO_FLUJO         IN NUMBER
	);


END PG_CE_REPORTE;

spool off;
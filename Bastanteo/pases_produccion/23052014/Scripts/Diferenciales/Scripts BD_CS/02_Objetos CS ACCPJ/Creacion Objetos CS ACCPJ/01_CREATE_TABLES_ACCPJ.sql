--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_AUDIT_BASTANTEO_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_AUDIT_BASTANTEO_CC" 
   (	"ID" NUMBER, 
	"RESULTADO_BASTANTEO" VARCHAR2(30 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_AUDIT_CLIENTE_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_AUDIT_CLIENTE_CC" 
   (	"ID" NUMBER, 
	"CODIGO_CENTRAL" VARCHAR2(8 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_AUDIT_CRITERIOS_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_AUDIT_CRITERIOS_CC" 
   (	"ID" NUMBER, 
	"TIPO_CRITERIO" VARCHAR2(30 BYTE), 
	"INDICADOR" NUMBER
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_AYUDA_MEMORIA_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_AYUDA_MEMORIA_CC" 
   (	"ID" NUMBER(10,0), 
	"COMENTARIO" VARCHAR2(250 BYTE), 
	"ID_EXPEDIENTE_FK" NUMBER(10,0), 
	"FECHA_REGISTRO" TIMESTAMP (6), 
	"ID_EMPLEADO_FK" NUMBER(10,0), 
	"ID_PERFIL_FK" NUMBER(5,0)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_CAMPANIA_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_CAMPANIA_CC" 
   (	"ID" NUMBER, 
	"DESCRIPCION" VARCHAR2(50 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_CARTERIZACION
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_CARTERIZACION" 
   (	"ID_PRODUCTO_FK" NUMBER(5,0), 
	"ID_TERRITORIO_FK" NUMBER(5,0), 
	"ID_EMPLEADO_FK" NUMBER(10,0), 
	"CODIGO" VARCHAR2(10 BYTE), 
	"DESCRIPCION" CHAR(50 BYTE)
   )TABLESPACE "USERS" ;

--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_CLIENTE_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_CLIENTE_CC" 
   (	"ID" NUMBER(10,0), 
	"RAZON_SOCIAL" VARCHAR2(60 BYTE), 
	"FECHA_CONSTITUCION" DATE, 
	"NUMERO_DOI" VARCHAR2(11 BYTE), 
	"TIPO_DOI" VARCHAR2(1 BYTE), 
	"CODIGO_CENTRAL" VARCHAR2(8 BYTE), 
	"NUMERO_CELULAR_1" VARCHAR2(14 BYTE), 
	"NUMERO_CELULAR_2" VARCHAR2(14 BYTE), 
	"TIPO_DOI_DES" VARCHAR2(30 BYTE), 
	"SECTOR_COD" VARCHAR2(3 BYTE), 
	"SECTOR_DES" VARCHAR2(60 BYTE), 
	"ACTIVIDAD_ECONOMICA_COD" VARCHAR2(11 BYTE), 
	"ACTIVIDAD_ECONOMICA_DES" VARCHAR2(60 BYTE), 
	"DIRECCION" VARCHAR2(60 BYTE), 
	"UBICACION" VARCHAR2(60 BYTE), 
	"DEPARTAMENTO_COD" VARCHAR2(2 BYTE), 
	"DEPARTAMENTO_DES" VARCHAR2(30 BYTE), 
	"PROVINCIA_COD" VARCHAR2(2 BYTE), 
	"PROVINCIA_DES" VARCHAR2(30 BYTE), 
	"DISTRITO_COD" VARCHAR2(3 BYTE), 
	"DISTRITO_DES" VARCHAR2(30 BYTE), 
	"CORREO_ELECTRONICO_1" VARCHAR2(50 BYTE), 
	"CORREO_ELECTRONICO_2" VARCHAR2(50 BYTE)
   ) TABLESPACE "CONELE_DAT";

   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_CLIENTE_CC"."ID" IS 'Correlativo autogenerado por un SEQUENCE';
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_COBRO_COMISION_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_COBRO_COMISION_CC" 
   (	"ID" NUMBER(10,0), 
	"DIAS_REINTENTO" NUMBER(10,0), 
	"HORA_EJECUCION_1" VARCHAR2(8 BYTE), 
	"HORA_EJECUCION_2" VARCHAR2(8 BYTE), 
	"HORA_EJECUCION_3" VARCHAR2(8 BYTE), 
	"HORA_EJECUCION_4" VARCHAR2(8 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_CUENTA_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_CUENTA_CC" 
   (	"ID" NUMBER(10,0), 
	"ID_CLIENTE_CC" NUMBER(10,0), 
	"PRODUCTO_COD" VARCHAR2(5 BYTE), 
	"PRODUCTO_DES" VARCHAR2(30 BYTE), 
	"SUBPRODUCTO_COD" VARCHAR2(15 BYTE), 
	"SUBPRODUCTO_DES" VARCHAR2(40 BYTE), 
	"NUMERO_CONTRATO" VARCHAR2(25 BYTE), 
	"MONEDA_COD" VARCHAR2(5 BYTE), 
	"MONEDA_DES" VARCHAR2(30 BYTE), 
	"FECHA_CREACION" DATE, 
	"SITUACION_CUENTA" VARCHAR2(10 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_DOCUMENTO_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_CC" 
   (	"ID" NUMBER, 
	"DESCRIPCION" VARCHAR2(200 BYTE), 
	"TIPO_DOCUMENTO" VARCHAR2(20 BYTE), 
	"CODIGO_DOCUMENTO" VARCHAR2(5 BYTE), 
	"TIPO_VISOR" CHAR(1 BYTE), 
	"VIGENCIA_DIAS" NUMBER, 
	"FLAG_VIGENCIA" CHAR(1 BYTE), 
	"PESO_PROM_HOJA_KB" NUMBER
   ) TABLESPACE "CONELE_DAT";

   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_CC"."ID" IS '';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_CC"."TIPO_DOCUMENTO" IS 'por ejemplo: legal';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_CC"."TIPO_VISOR" IS 'TIPO VISOR:          A=Avanzado          B=Basico';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_CC"."FLAG_VIGENCIA" IS 'Para indicar si el documento tiene vigencia: 1: Si tiene 0: No';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_CC"."PESO_PROM_HOJA_KB" IS 'Peso Promedio x Hoja en KB que tiene el documento';
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_DOCUMENTO_EXP_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC" 
   (	"ID_DOCUMENTO_CC_FK" NUMBER, 
	"ID_EXPEDIENTE_CC_FK" NUMBER(10,0), 
	"FLAG_ESCANEADO" CHAR(1 BYTE), 
	"FECHA_ESCANEO" TIMESTAMP (6), 
	"FECHA_VIGENCIA" DATE, 
	"FLAG_RECHAZADO" CHAR(1 BYTE), 
	"ID_MOTIVO_CC_FK" NUMBER, 
	"NOMBRE_ARCHIVO" VARCHAR2(20 BYTE), 
	"FLAG_CM" CHAR(1 BYTE), 
	"ID_CM" NUMBER, 
	"ID_CM_COPIA" NUMBER, 
	"FLAG_ALTERNO" CHAR(1 BYTE), 
	"FLAG_OBLIGATORIO" CHAR(1 BYTE), 
	"ID" NUMBER, 
	"DOC_PESO_PROM_KB" NUMBER(5,0), 
	"FLAG_REQ_ESCANEO" CHAR(1 BYTE) DEFAULT 0, 
	"PID_CM" VARCHAR2(100 BYTE)
   ) TABLESPACE "CONELE_DAT";

   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC"."FLAG_ESCANEADO" IS 'Si el documento fue escaneado';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC"."FLAG_RECHAZADO" IS '1: Rechazado 0:Aprobado';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC"."FLAG_CM" IS 'Si el documento esta en CM 1: Si 0: NO';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC"."ID_CM" IS 'ID DEL DOCUMENTO EN CONTENT MANAGER';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC"."ID_CM_COPIA" IS 'ID DE LA COPIA DEL DOCUMENTO EN CM';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC"."FLAG_ALTERNO" IS 'Registro que pertenece a un documento de tipo 1: Instrucciones 2: Dictamen';
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_DOCUMENTO_HIST_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC" 
   (	"ID_DOCUMENTO_FK" NUMBER, 
	"ID_EXPEDIENTE_FK" NUMBER(10,0), 
	"FLAG_ESCANEADO" CHAR(1 BYTE), 
	"FECHA_ESCANEO" TIMESTAMP (6), 
	"FECHA_VIGENCIA" DATE, 
	"NOMBRE_ARCHIVO" VARCHAR2(14 BYTE), 
	"FLAG_CM" CHAR(1 BYTE), 
	"ID_CM" NUMBER, 
	"FLAG_ALTERNO" CHAR(1 BYTE), 
	"FLAG_OBLIGATORIO" CHAR(1 BYTE), 
	"ID" NUMBER, 
	"ID_CLIENTE_FK" NUMBER(10,0), 
	"FLAG_MIGRACION" CHAR(1 BYTE)
   ) TABLESPACE "CONELE_DAT";

   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC"."FLAG_CM" IS 'Si el documento esta en CM 1: Si 0: NO';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC"."ID_CM" IS 'ID DEL DOCUMENTO EN CM';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC"."FLAG_ALTERNO" IS 'Registro que pertenece a un documento de tipo 1: Instrucciones 2: Dictamen';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC"."FLAG_MIGRACION" IS '1: Si documento proviene de migracion histórica 0:Caso contrario';
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_EMPLEADO
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" 
   (	"ID" NUMBER(10,0), 
	"CODIGO" VARCHAR2(8 BYTE), 
	"NOMBRES" VARCHAR2(60 BYTE), 
	"APEPAT" VARCHAR2(50 BYTE), 
	"APEMAT" VARCHAR2(50 BYTE), 
	"ID_PERFIL_FK" NUMBER(5,0), 
	"FECING" DATE, 
	"FECEGR" DATE, 
	"ID_OFICINA_FK" NUMBER(5,0), 
	"NOMBRES_COMPLETOS" VARCHAR2(110 BYTE), 
	"ID_NIVEL_FK" NUMBER(5,0), 
	"ID_TIPO_CATEGORIA_FK" NUMBER(5,0), 
	"CORREO" VARCHAR2(50 BYTE), 
	"FLAG_ACTIVO" CHAR(1 BYTE), 
	"FLAG_ABOGADO" CHAR(1 BYTE), 
	"ID_ESTUDIO_ABOG_FK" NUMBER(10,0)
   )TABLESPACE "USERS" ;

   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_EMPLEADO"."ID_NIVEL_FK" IS 'Nivel utilizado para Delegacion Riesgos';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_EMPLEADO"."ID_TIPO_CATEGORIA_FK" IS 'Tipo Categoria utilizado en Delegacion Oficina';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_EMPLEADO"."FLAG_ABOGADO" IS 'Flag que indica si el usuario es un abogado externo al Banco';

--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_ESCANEO_WEB_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_ESCANEO_WEB_CC" 
   (	"ID" NUMBER, 
	"ID_EMPRESA" VARCHAR2(20 BYTE), 
	"ID_SISTEMA" VARCHAR2(20 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_ESTADO_EXP_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_ESTADO_EXP_CC" 
   (	"ID" NUMBER, 
	"DESCRIPCION" VARCHAR2(50 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_ESTADO_TAREA_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_ESTADO_TAREA_CC" 
   (	"ID" NUMBER(5,0), 
	"DESCRIPCION" VARCHAR2(100 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_ESTUDIO_ABOGADO_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_ESTUDIO_ABOGADO_CC" 
   (	"ID" NUMBER(10,0), 
	"DESCRIPCION" VARCHAR2(30 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_EXP_CUENTA_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_EXP_CUENTA_CC" 
   (	"ID" NUMBER, 
	"ID_EXPEDIENTE_FK" NUMBER, 
	"ID_CUENTA_FK" NUMBER
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_EXPEDIENTE_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" 
   (	"ID" NUMBER(10,0), 
	"RESULTADO" VARCHAR2(30 BYTE), 
	"ID_EMPLEADO_FK" NUMBER(10,0), 
	"SUBPRODUCTO" VARCHAR2(40 BYTE), 
	"FECHA_REGISTRO" TIMESTAMP (6), 
	"FLAG_CORREO" CHAR(1 BYTE), 
	"FLAG_SMSTEXTO" CHAR(1 BYTE), 
	"FLAG_EXONERACION_COMISION" CHAR(1 BYTE), 
	"FLAG_EJECUTO_COBRO_COMISION" CHAR(1 BYTE), 
	"CUENTA_COBRO_COMISION" VARCHAR2(30 BYTE), 
	"NUMERO_CUENTA_COBRO" VARCHAR2(25 BYTE), 
	"ESTADO_CUENTA" VARCHAR2(20 BYTE), 
	"ID_CAMPANIA_FK" NUMBER, 
	"ID_MOTIVO_FK" NUMBER, 
	"ID_CLIENTE_FK" NUMBER(10,0), 
	"ID_OFICINA_FK" NUMBER(5,0), 
	"ID_PRODUCTO_FK" NUMBER(5,0), 
	"ID_OPERACION_FK" NUMBER, 
	"OBSERVACIONES" VARCHAR2(500 BYTE), 
	"FLAG_FACULTADES_ESPECIALES" CHAR(1 BYTE), 
	"ID_ESTADO_FK" NUMBER(5,0), 
	"FECHA_ENVIO" TIMESTAMP (6), 
	"FECHA_PROGRAMADA" TIMESTAMP (6), 
	"FECHA_FIN" TIMESTAMP (6), 
	"FLAG_CONFORMIDAD_BASTANTEO" CHAR(1 BYTE), 
	"RESULTADO_BASTANTEO" VARCHAR2(30 BYTE), 
	"OBSERVACIONES_BASTANTEO" VARCHAR2(500 BYTE), 
	"FLAG_MODIFICATORIA" CHAR(1 BYTE), 
	"COD_TIPO_PJ" VARCHAR2(2 BYTE), 
	"DES_TIPO_PJ" VARCHAR2(30 BYTE), 
	"INSTRUCCIONES_BASTANTEO" BLOB, 
	"DICTAMEN_BASTANTEO" BLOB, 
	"RESULTADO_REVOCATORIA" BLOB, 
	"ACCION" VARCHAR2(100 BYTE), 
	"NUM_TERMINAL" VARCHAR2(20 BYTE), 
	"FLAG_INDICADOR_BASTANTEO" CHAR(1 BYTE), 
	"DESCRIPCION_MENSAJE_BASTANTEO" VARCHAR2(60 CHAR)
   ) TABLESPACE "USERS";

   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC"."FLAG_EXONERACION_COMISION" IS '1: Exonerado 0: No exonerado';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC"."ID_MOTIVO_FK" IS '';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC"."FLAG_INDICADOR_BASTANTEO" IS '1: Si se ejecuto con exito SFP 0: caso contrario';
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_EXPEDIENTE_TAREA_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_TAREA_CC" 
   (	"ID_EXPEDIENTE_FK" NUMBER(10,0), 
	"ID_TAREA_FK" NUMBER(10,0), 
	"ID" NUMBER(5,0), 
	"ID_ESTADO_TAREA_FK" NUMBER(5,0)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_EXPTAR_PROCESO_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_EXPTAR_PROCESO_CC" 
   (	"ID_EXPEDIENTE_FK" NUMBER(10,0), 
	"ID" NUMBER(10,0), 
	"ID_EMPLEADO_FK" NUMBER(10,0), 
	"ID_TAREA_FK" NUMBER(5,0)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_GUIA_DOCUMENT_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" 
   (	"ID_OPERACION_FK" NUMBER, 
	"ID_DOCUMENTO_FK" NUMBER, 
	"FLAG_OBLIGATORIO" CHAR(1 BYTE) DEFAULT 0, 
	"FLAG_REEMPLAZABLE" CHAR(1 BYTE), 
	"FLAG_CADUCA" CHAR(1 BYTE), 
	"FLAG_REQ_ESCANEO" CHAR(1 BYTE), 
	"ID_PRODUCTO_FK" NUMBER(5,0), 
	"COD_TIPO_PJ" CHAR(2 BYTE) DEFAULT NULL, 
	"DES_TIPO_PJ" VARCHAR2(30 BYTE)
   ) TABLESPACE "CONELE_DAT";

   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC"."FLAG_OBLIGATORIO" IS '1: Obligatorio 0: Opcional';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC"."FLAG_REEMPLAZABLE" IS '1: Reemplazable 0: No reemplazable';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC"."FLAG_CADUCA" IS '1: Caduca 0: No caduca';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC"."FLAG_REQ_ESCANEO" IS '1: Requiere escaneo 0: No requiere escaneo';
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_HISTORIAL_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" 
   (	"ID" NUMBER(10,0), 
	"ID_EXPEDIENTE_CC_FK" NUMBER(10,0), 
	"RESULTADO" VARCHAR2(30 BYTE), 
	"ID_EMPLEADO_FK" NUMBER(10,0), 
	"SUBPRODUCTO" VARCHAR2(40 BYTE), 
	"FECHA_REGISTRO" TIMESTAMP (6), 
	"FLAG_CORREO" CHAR(1 BYTE), 
	"FLAG_SMSTEXTO" CHAR(1 BYTE), 
	"FLAG_EXONERACION_COMISION" CHAR(1 BYTE), 
	"FLAG_EJECUTO_COBRO_COMISION" CHAR(1 BYTE), 
	"CUENTA_COBRO_COMISION" VARCHAR2(30 BYTE), 
	"NUMERO_CUENTA_COBRO" VARCHAR2(25 BYTE), 
	"ESTADO_CUENTA" VARCHAR2(20 BYTE), 
	"ID_CAMPANIA_FK" NUMBER, 
	"ID_MOTIVO_FK" NUMBER, 
	"ID_CLIENTE_FK" NUMBER(10,0), 
	"ID_OFICINA_FK" NUMBER(5,0), 
	"ID_PRODUCTO_FK" NUMBER(5,0), 
	"OBSERVACIONES" VARCHAR2(500 BYTE), 
	"FLAG_FACULTADES_ESPECIALES" CHAR(1 BYTE), 
	"ID_ESTADO_FK" NUMBER(5,0), 
	"ID_TAREA_FK" NUMBER(5,0), 
	"FECHA_ENVIO" TIMESTAMP (6), 
	"FECHA_PROGRAMADA" TIMESTAMP (6), 
	"FECHA_FIN" TIMESTAMP (6), 
	"FLAG_CONFORMIDAD_BASTANTEO" CHAR(1 BYTE), 
	"RESULTADO_BASTANTEO" VARCHAR2(30 BYTE), 
	"OBSERVACIONES_BASTANTEO" VARCHAR2(500 BYTE), 
	"FLAG_MODIFICATORIA" CHAR(1 BYTE), 
	"COD_TIPO_PJ" VARCHAR2(2 BYTE), 
	"DES_TIPO_PJ" VARCHAR2(30 BYTE), 
	"DICTAMEN_BASTANTEO" BLOB, 
	"INSTRUCCIONES_BASTANTEO" BLOB, 
	"RESULTADO_REVOCATORIA" BLOB, 
	"ID_OPERACION_FK" NUMBER, 
	"NUM_TERMINAL" VARCHAR2(20 BYTE)
   ) TABLESPACE "USERS";

   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_HISTORIAL_CC"."ID_ESTADO_FK" IS 'Estado del Expediente';
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_LISTA_CERRADA_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_LISTA_CERRADA_CC" 
   (	"CODIGO_CENTRAL" VARCHAR2(20 BYTE), 
	"FLAG_ACTIVO" CHAR(1 BYTE), 
	"ID" NUMBER
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_LOG_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_LOG_CC" 
   (	"ID" NUMBER(10,0), 
	"DESCRIPCION" VARCHAR2(400 BYTE), 
	"NUM_TERMINAL" VARCHAR2(20 BYTE), 
	"ID_EMPLEADO_FK" NUMBER(10,0), 
	"ID_ESTADO_FK" NUMBER(5,0), 
	"EXPEDIENTE_FK" NUMBER(10,0), 
	"FEC_REGISTRO" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_MOTIVO_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_MOTIVO_CC" 
   (	"ID" NUMBER, 
	"DESCRIPCION" VARCHAR2(200 BYTE), 
	"TIPO_MOTIVO" VARCHAR2(100 BYTE), 
	"ID_TAREA_FK" NUMBER, 
	"FLAG_ACTIVO" CHAR(1 BYTE), 
	"CODIGO_MOTIVO" VARCHAR2(5 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_OPERACION_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_OPERACION_CC" 
   (	"ID" NUMBER, 
	"DESCRIPCION" VARCHAR2(50 BYTE), 
	"CODIGO_OPERACION" VARCHAR2(5 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_PARAMETROS_CONF_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_PARAMETROS_CONF_CC" 
   (	"ID" NUMBER, 
	"CODIGO_MODULO" VARCHAR2(20 BYTE), 
	"NOMBRE_VARIABLE" VARCHAR2(50 BYTE), 
	"VALOR_VARIABLE" VARCHAR2(150 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_PARA_VISTA_UNICA_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_PARA_VISTA_UNICA_CC" 
   (	"ID" NUMBER, 
	"ID_DOCUMENTO_CC_FK" NUMBER, 
	"FLAG_SOLO_ULTIMA_VERSION" VARCHAR2(1 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_PARTICIPE_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_PARTICIPE_CC" 
   (	"ID_CLIENTE_FK" NUMBER(10,0), 
	"ID" NUMBER(10,0), 
	"CODIGO_CENTRAL" VARCHAR2(15 BYTE), 
	"TIPO_DOI" VARCHAR2(1 BYTE), 
	"NUMERO_DOI" VARCHAR2(11 BYTE), 
	"NOMBRE" VARCHAR2(20 BYTE), 
	"APELLIDO_PATERNO" VARCHAR2(20 BYTE), 
	"APELLIDO_MATERNO" VARCHAR2(20 BYTE), 
	"NUMERO_CUENTA" VARCHAR2(15 BYTE), 
	"FLAG_FIRMA_ASOCIADA" CHAR(1 BYTE), 
	"FLAG_FIRMA_ACTIVA" CHAR(1 BYTE), 
	"FLAG_INDICADOR_FIRMA" CHAR(1 BYTE), 
	"TIPO_DOI_DES" VARCHAR2(20 BYTE), 
	"DIRECCION" VARCHAR2(60 BYTE), 
	"UBICACION" VARCHAR2(60 BYTE), 
	"DEPARTAMENTO_COD" VARCHAR2(2 BYTE), 
	"DEPARTAMENTO_DES" VARCHAR2(30 BYTE), 
	"PROVINCIA_COD" VARCHAR2(2 BYTE), 
	"PROVINCIA_DES" VARCHAR2(30 BYTE), 
	"DISTRITO_COD" VARCHAR2(3 BYTE), 
	"DISTRITO_DES" VARCHAR2(30 BYTE), 
	"INDICADOR_FIRMA" VARCHAR2(1 BYTE), 
	"FECHA_SERIALIZACION" DATE, 
	"NIVEL_INTERVENCION" VARCHAR2(1 BYTE), 
	"SECUENCIA_INTERVENCION" VARCHAR2(2 BYTE), 
	"ID_CUENTA_FK" NUMBER
   ) TABLESPACE "CONELE_DAT";

   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_PARTICIPE_CC"."FLAG_FIRMA_ASOCIADA" IS '1: firma asociada 0: firma no asociada';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_PARTICIPE_CC"."FLAG_FIRMA_ACTIVA" IS '1: firma activa 0: firma no activa';
   COMMENT ON COLUMN "CONELE"."TBL_CE_IBM_PARTICIPE_CC"."FLAG_INDICADOR_FIRMA" IS 'Se obtiene de Consulta Cliente PJ - HOST e indica si tiene firma o no';
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_PERFIL
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_PERFIL" 
   (	"ID" NUMBER(5,0), 
	"CODIGO" VARCHAR2(5 BYTE), 
	"DESCRIPCION" VARCHAR2(60 BYTE), 
	"ID_PERFIL_FK" NUMBER(5,0), 
	"LISTA_CORREO_JEFES" VARCHAR2(150 BYTE), 
	"FLAG_REGISTRA_EXP" VARCHAR2(1 BYTE), 
	"FLAG_ASIGNACION" VARCHAR2(1 BYTE), 
	"FLAG_ADMINISTRACION" VARCHAR2(1 BYTE), 
	"FLAG_REGISTRA_AYU" VARCHAR2(1 BYTE)
   ) TABLESPACE "CONELE_DAT";

--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_PERFIL_BALANCEO_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_PERFIL_BALANCEO_CC" 
   (	"ID" NUMBER, 
	"ID_PERFIL_FK" NUMBER, 
	"ID_TIPO_BALANCEO_FK" NUMBER
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_PESO_EXP_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_PESO_EXP_CC" 
   (	"ID" NUMBER, 
	"PESO_MB_DESDE" NUMBER, 
	"PESO_MB_HASTA" NUMBER, 
	"PESO" NUMBER
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_PESO_PARTICIPE_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_PESO_PARTICIPE_CC" 
   (	"ID" NUMBER, 
	"NUMERO_HASTA" NUMBER, 
	"PESO" NUMBER, 
	"NUMERO_DESDE" NUMBER
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_PLAZO_SUBSANA_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_PLAZO_SUBSANA_CC" 
   (	"ID" NUMBER, 
	"PLAZO_DIAS" NUMBER
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_PRODUCTO
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_PRODUCTO" 
   (	"ID" NUMBER(5,0), 
	"CODIGO" VARCHAR2(5 BYTE), 
	"DESCRIPCION" VARCHAR2(50 BYTE), 
	"PESO" NUMBER(5,0)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_PRODUCTO_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_PRODUCTO_CC" 
   (	"ID" NUMBER(5,0), 
	"CODIGO" VARCHAR2(5 BYTE), 
	"DESCRIPCION" VARCHAR2(50 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_TAREA_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_TAREA_CC" 
   (	"ID" NUMBER(5,0), 
	"DESCRIPCION" VARCHAR2(100 BYTE), 
	"VERDE_MINUTOS" NUMBER(7,0), 
	"AMARILLO_MINUTOS" NUMBER(7,0), 
	"NOMBRE_PAGINA" VARCHAR2(70 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_TAREA_PERFIL_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_TAREA_PERFIL_CC" 
   (	"ID" NUMBER(10,0), 
	"ID_TAREA_CC_FK" NUMBER(5,0), 
	"ID_PERFIL_FK" NUMBER(5,0)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_TAREAS_REASIG_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_TAREAS_REASIG_CC" 
   (	"ID_EXPEDIENTE_FK" NUMBER(10,0), 
	"ID" NUMBER(10,0), 
	"ID_EMPLEADO_DE_FK" NUMBER(10,0), 
	"ID_EMPLEADO_A_FK" NUMBER(10,0), 
	"ID_TAREA_FK" NUMBER(5,0), 
	"ID_MOTIVO_FK" NUMBER(5,0)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Table TBL_CE_IBM_TIPO_BALANCEO_CC
--------------------------------------------------------

  CREATE TABLE "CONELE"."TBL_CE_IBM_TIPO_BALANCEO_CC" 
   (	"ID" NUMBER, 
	"DESCRIPCION" VARCHAR2(50 BYTE)
   ) TABLESPACE "CONELE_DAT";
--------------------------------------------------------
--  DDL for Index OPERACION_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."OPERACION_CC_PK" ON "CONELE"."TBL_CE_IBM_OPERACION_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_COBRO_COMISION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_COBRO_COMISION_PK" ON "CONELE"."TBL_CE_IBM_COBRO_COMISION_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_ESCANEO_WEB_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_ESCANEO_WEB_PK" ON "CONELE"."TBL_CE_IBM_ESCANEO_WEB_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index PESO_EXPEDIENTE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."PESO_EXPEDIENTE_PK" ON "CONELE"."TBL_CE_IBM_PESO_EXP_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_ESTUDIO_ABOGAD_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_ESTUDIO_ABOGAD_PK" ON "CONELE"."TBL_CE_IBM_ESTUDIO_ABOGADO_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_EXPEDIENTE_TAR_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_EXPEDIENTE_TAR_PK" ON "CONELE"."TBL_CE_IBM_EXPEDIENTE_TAREA_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index PRODUCTO_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."PRODUCTO_CC_PK" ON "CONELE"."TBL_CE_IBM_PRODUCTO_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;

--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_GUIA_DOCUMENT__PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT__PK" ON "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" ("ID_OPERACION_FK", "ID_DOCUMENTO_FK", "COD_TIPO_PJ") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index CLIENTE_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."CLIENTE_CC_PK" ON "CONELE"."TBL_CE_IBM_CLIENTE_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_TAREA_PERFIL_C_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_TAREA_PERFIL_C_PK" ON "CONELE"."TBL_CE_IBM_TAREA_PERFIL_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_PESO_PARTICIPE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_PESO_PARTICIPE_PK" ON "CONELE"."TBL_CE_IBM_PESO_PARTICIPE_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_EXPTAR_PROC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_EXPTAR_PROC_PK" ON "CONELE"."TBL_CE_IBM_EXPTAR_PROCESO_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index CARTERIZACION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."CARTERIZACION_PK" ON "CONELE"."TBL_CE_IBM_CARTERIZACION" ("ID_PRODUCTO_FK", "ID_TERRITORIO_FK", "ID_EMPLEADO_FK") 
   TABLESPACE "CONELE_IDX" ;

--------------------------------------------------------
--  DDL for Index HISTORIAL_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."HISTORIAL_CC_PK" ON "CONELE"."TBL_CE_IBM_HISTORIAL_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_TAREAS_REASIG__PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_TAREAS_REASIG__PK" ON "CONELE"."TBL_CE_IBM_TAREAS_REASIG_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index DOCUMENTO_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."DOCUMENTO_CC_PK" ON "CONELE"."TBL_CE_IBM_DOCUMENTO_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_PERFIL_BALANCE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_PERFIL_BALANCE_PK" ON "CONELE"."TBL_CE_IBM_PERFIL_BALANCEO_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index ESTADO_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."ESTADO_CC_PK" ON "CONELE"."TBL_CE_IBM_ESTADO_TAREA_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index DOCUMENTO_HISTORIAL_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."DOCUMENTO_HISTORIAL_PK" ON "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index LISTA_CERRADA_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."LISTA_CERRADA_CC_PK" ON "CONELE"."TBL_CE_IBM_LISTA_CERRADA_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_CLIENTE_AUDIT__PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_CLIENTE_AUDIT__PK" ON "CONELE"."TBL_CE_IBM_AUDIT_CLIENTE_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index CAMPANIA_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."CAMPANIA_CC_PK" ON "CONELE"."TBL_CE_IBM_CAMPANIA_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_CUENTA_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_CUENTA_CC_PK" ON "CONELE"."TBL_CE_IBM_CUENTA_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index EMPLEADO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."EMPLEADO_PK" ON "CONELE"."TBL_CE_IBM_EMPLEADO" ("ID")
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_PARTICIPE_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_PARTICIPE_CC_PK" ON "CONELE"."TBL_CE_IBM_PARTICIPE_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_EXP_CUENTA_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_EXP_CUENTA_PK" ON "CONELE"."TBL_CE_IBM_EXP_CUENTA_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_PARAMETROS_CON_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_PARAM_CON_CC_PK" ON "CONELE"."TBL_CE_IBM_PARAMETROS_CONF_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_LOG_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_LOG_CC_PK" ON "CONELE"."TBL_CE_IBM_LOG_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index EXPEDIENTE_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."EXPEDIENTE_CC_PK" ON "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_PLAZO_SUBSANAC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_PLAZO_SUBSANAC_PK" ON "CONELE"."TBL_CE_IBM_PLAZO_SUBSANA_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index PERFIL_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."PERFIL_PK" ON "CONELE"."TBL_CE_IBM_PERFIL" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_AYUDA_MEMORIA__PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_AYUDA_MEMORIA__PK" ON "CONELE"."TBL_CE_IBM_AYUDA_MEMORIA_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index DOCUMENTO_EXP__PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."DOCUMENTO_EXP__PK" ON "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index MOTIVO_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."MOTIVO_CC_PK" ON "CONELE"."TBL_CE_IBM_MOTIVO_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_TIPO_BALANCEO__PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_TIPO_BALANCEO__PK" ON "CONELE"."TBL_CE_IBM_TIPO_BALANCEO_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_ESTADO_EXP_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_ESTADO_EXP_PK" ON "CONELE"."TBL_CE_IBM_ESTADO_EXP_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_PARAM__PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_PARAM__PK" ON "CONELE"."TBL_CE_IBM_PARA_VISTA_UNICA_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_TAREA_CC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_TAREA_CC_PK" ON "CONELE"."TBL_CE_IBM_TAREA_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_CE_IBM_AUDIT_CRITERIO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_CE_IBM_AUDIT_CRITERIO_PK" ON "CONELE"."TBL_CE_IBM_AUDIT_CRITERIOS_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index TBL_ESTADO_EXP_AUDIT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."TBL_ESTADO_EXP_AUDIT_PK" ON "CONELE"."TBL_CE_IBM_AUDIT_BASTANTEO_CC" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  DDL for Index PRODUCTO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONELE"."PRODUCTO_PK" ON "CONELE"."TBL_CE_IBM_PRODUCTO" ("ID") 
   TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_PESO_EXP_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PESO_EXP_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PESO_EXP_CC" ADD CONSTRAINT "PESO_EXPEDIENTE_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_GUIA_DOCUMENT_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" ADD CONSTRAINT "TBL_CE_IBM_GUIA_DOCUMENT__PK" PRIMARY KEY ("ID_OPERACION_FK", "ID_DOCUMENTO_FK", "COD_TIPO_PJ")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" MODIFY ("DES_TIPO_PJ" NOT NULL DISABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" MODIFY ("FLAG_OBLIGATORIO" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" MODIFY ("COD_TIPO_PJ" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" MODIFY ("ID_DOCUMENTO_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" MODIFY ("ID_OPERACION_FK" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_AUDIT_CLIENTE_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_AUDIT_CLIENTE_CC" ADD CONSTRAINT "TBL_CE_IBM_CLIENTE_AUDIT__PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_AUDIT_CLIENTE_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_PLAZO_SUBSANA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PLAZO_SUBSANA_CC" ADD CONSTRAINT "TBL_CE_IBM_PLAZO_SUBSANAC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_PLAZO_SUBSANA_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_PRODUCTO
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PRODUCTO" MODIFY ("PESO" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PRODUCTO" MODIFY ("DESCRIPCION" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PRODUCTO" MODIFY ("CODIGO" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PRODUCTO" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PRODUCTO" ADD CONSTRAINT "PRODUCTO_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_PARTICIPE_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PARTICIPE_CC" ADD CONSTRAINT "TBL_CE_IBM_PARTICIPE_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_PARTICIPE_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_DOCUMENTO_HIST_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC" MODIFY ("ID_EXPEDIENTE_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC" MODIFY ("ID_DOCUMENTO_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC" ADD CONSTRAINT "DOCUMENTO_HISTORIAL_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_HISTORIAL_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" ADD CONSTRAINT "HISTORIAL_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_COBRO_COMISION_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_COBRO_COMISION_CC" ADD CONSTRAINT "TBL_CE_IBM_COBRO_COMISION_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_COBRO_COMISION_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_MOTIVO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_MOTIVO_CC" MODIFY ("DESCRIPCION" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_MOTIVO_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_MOTIVO_CC" ADD CONSTRAINT "MOTIVO_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_OPERACION_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_OPERACION_CC" MODIFY ("DESCRIPCION" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_OPERACION_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_OPERACION_CC" ADD CONSTRAINT "OPERACION_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_PARA_VISTA_UNICA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PARA_VISTA_UNICA_CC" ADD CONSTRAINT "TBL_CE_IBM_PARAM__PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_PARA_VISTA_UNICA_CC" MODIFY ("ID_DOCUMENTO_CC_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PARA_VISTA_UNICA_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_LISTA_CERRADA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_LISTA_CERRADA_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_LISTA_CERRADA_CC" MODIFY ("CODIGO_CENTRAL" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_LISTA_CERRADA_CC" ADD CONSTRAINT "LISTA_CERRADA_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_TIPO_BALANCEO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_TIPO_BALANCEO_CC" ADD CONSTRAINT "TBL_CE_IBM_TIPO_BALANCEO__PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_TIPO_BALANCEO_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_EMPLEADO
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" MODIFY ("NOMBRES_COMPLETOS" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" MODIFY ("ID_OFICINA_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" MODIFY ("ID_PERFIL_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" MODIFY ("APEMAT" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" MODIFY ("APEPAT" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" MODIFY ("NOMBRES" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" MODIFY ("CODIGO" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" ADD CONSTRAINT "EMPLEADO_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;

--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_EXPTAR_PROCESO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPTAR_PROCESO_CC" ADD CONSTRAINT "TBL_CE_IBM_EXPTAR_PROC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPTAR_PROCESO_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_TAREA_PERFIL_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREA_PERFIL_CC" ADD CONSTRAINT "TBL_CE_IBM_TAREA_PERFIL_C_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREA_PERFIL_CC" MODIFY ("ID_PERFIL_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREA_PERFIL_CC" MODIFY ("ID_TAREA_CC_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREA_PERFIL_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_PERFIL
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PERFIL" MODIFY ("DESCRIPCION" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PERFIL" MODIFY ("CODIGO" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PERFIL" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PERFIL" ADD CONSTRAINT "PERFIL_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;

--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_AUDIT_BASTANTEO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_AUDIT_BASTANTEO_CC" ADD CONSTRAINT "TBL_ESTADO_EXP_AUDIT_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_AUDIT_BASTANTEO_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_CUENTA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_CUENTA_CC" ADD CONSTRAINT "TBL_CE_IBM_CUENTA_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_CUENTA_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_DOCUMENTO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_CC" MODIFY ("CODIGO_DOCUMENTO" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_CC" ADD CONSTRAINT "DOCUMENTO_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_ESTADO_EXP_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_ESTADO_EXP_CC" ADD CONSTRAINT "TBL_CE_IBM_ESTADO_EXP_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_ESTADO_EXP_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_TAREAS_REASIG_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREAS_REASIG_CC" ADD CONSTRAINT "TBL_CE_IBM_TAREAS_REASIG__PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREAS_REASIG_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_LOG_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_LOG_CC" ADD CONSTRAINT "TBL_CE_IBM_LOG_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_LOG_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_PERFIL_BALANCEO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PERFIL_BALANCEO_CC" ADD CONSTRAINT "TBL_CE_IBM_PERFIL_BALANCE_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_PERFIL_BALANCEO_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_CAMPANIA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_CAMPANIA_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_CAMPANIA_CC" ADD CONSTRAINT "CAMPANIA_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_EXP_CUENTA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_EXP_CUENTA_CC" ADD CONSTRAINT "TBL_CE_IBM_EXP_CUENTA_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXP_CUENTA_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_PESO_PARTICIPE_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PESO_PARTICIPE_CC" ADD CONSTRAINT "TBL_CE_IBM_PESO_PARTICIPE_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_PESO_PARTICIPE_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_EXPEDIENTE_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ADD CONSTRAINT "EXPEDIENTE_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_EXPEDIENTE_TAREA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_TAREA_CC" ADD CONSTRAINT "TBL_CE_IBM_EXPEDIENTE_TAR_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_TAREA_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_TAREA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREA_CC" ADD CONSTRAINT "TBL_CE_IBM_TAREA_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREA_CC" MODIFY ("DESCRIPCION" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREA_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_DOCUMENTO_EXP_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC" MODIFY ("ID_EXPEDIENTE_CC_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC" MODIFY ("ID_DOCUMENTO_CC_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC" ADD CONSTRAINT "DOCUMENTO_EXP__PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_ESTADO_TAREA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_ESTADO_TAREA_CC" MODIFY ("DESCRIPCION" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_ESTADO_TAREA_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_ESTADO_TAREA_CC" ADD CONSTRAINT "ESTADO_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_ESTUDIO_ABOGADO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_ESTUDIO_ABOGADO_CC" ADD CONSTRAINT "TBL_CE_IBM_ESTUDIO_ABOGAD_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_ESTUDIO_ABOGADO_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_AYUDA_MEMORIA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_AYUDA_MEMORIA_CC" ADD CONSTRAINT "TBL_CE_IBM_AYUDA_MEMORIA__PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_AYUDA_MEMORIA_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_CLIENTE_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_CLIENTE_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_CLIENTE_CC" ADD CONSTRAINT "CLIENTE_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;

--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_PRODUCTO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PRODUCTO_CC" MODIFY ("DESCRIPCION" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PRODUCTO_CC" MODIFY ("CODIGO" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PRODUCTO_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PRODUCTO_CC" ADD CONSTRAINT "PRODUCTO_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_CARTERIZACION
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_CARTERIZACION" MODIFY ("ID_EMPLEADO_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_CARTERIZACION" MODIFY ("ID_TERRITORIO_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_CARTERIZACION" MODIFY ("ID_PRODUCTO_FK" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_CARTERIZACION" ADD CONSTRAINT "CARTERIZACION_PK" PRIMARY KEY ("ID_PRODUCTO_FK", "ID_TERRITORIO_FK", "ID_EMPLEADO_FK")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;

--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_PARAMETROS_CONF_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PARAMETROS_CONF_CC" ADD CONSTRAINT "TBL_CE_IBM_PARAM_CON_CC_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_PARAMETROS_CONF_CC" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PARAMETROS_CONF_CC" MODIFY ("CODIGO_MODULO" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PARAMETROS_CONF_CC" MODIFY ("NOMBRE_VARIABLE" NOT NULL ENABLE);
  ALTER TABLE "CONELE"."TBL_CE_IBM_PARAMETROS_CONF_CC" MODIFY ("VALOR_VARIABLE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_AUDIT_CRITERIOS_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_AUDIT_CRITERIOS_CC" ADD CONSTRAINT "TBL_CE_IBM_AUDIT_CRITERIO_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_AUDIT_CRITERIOS_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_ESCANEO_WEB_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_ESCANEO_WEB_CC" ADD CONSTRAINT "TBL_CE_IBM_ESCANEO_WEB_PK" PRIMARY KEY ("ID")
  USING INDEX  TABLESPACE "CONELE_IDX" ENABLE;
  ALTER TABLE "CONELE"."TBL_CE_IBM_ESCANEO_WEB_CC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_AYUDA_MEMORIA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_AYUDA_MEMORIA_CC" ADD CONSTRAINT "TBL_CE_IBM_AYUDA_MEM_EMP_CC_FK" FOREIGN KEY ("ID_EMPLEADO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EMPLEADO" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_AYUDA_MEMORIA_CC" ADD CONSTRAINT "TBL_CE_IBM_AYUDA_MEM_EXP_CC_FK" FOREIGN KEY ("ID_EXPEDIENTE_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_AYUDA_MEMORIA_CC" ADD CONSTRAINT "TBL_CE_IBM_AYUDA_MEM_PERFIL_FK" FOREIGN KEY ("ID_PERFIL_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_PERFIL" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_CARTERIZACION
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_CARTERIZACION" ADD CONSTRAINT "CARTERIZACION_EMPLEADO_FK" FOREIGN KEY ("ID_EMPLEADO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EMPLEADO" ("ID") ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_CARTERIZACION" ADD CONSTRAINT "CARTERIZACION_PRODUCTO_FK" FOREIGN KEY ("ID_PRODUCTO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_PRODUCTO" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_CARTERIZACION" ADD CONSTRAINT "CARTERIZACION_TERRITORIO_FK" FOREIGN KEY ("ID_TERRITORIO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_TERRITORIO" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_CUENTA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_CUENTA_CC" ADD CONSTRAINT "TBL_CE_IBM_CLIENTE_CC_FK1" FOREIGN KEY ("ID_CLIENTE_CC")
	  REFERENCES "CONELE"."TBL_CE_IBM_CLIENTE_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_DOCUMENTO_EXP_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC" ADD CONSTRAINT "DOCUMENTO_EXP_DOCUMENTO_FK" FOREIGN KEY ("ID_DOCUMENTO_CC_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_DOCUMENTO_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_EXP_CC" ADD CONSTRAINT "DOCUMENTO_EXP_MOTIVO_CC_FK" FOREIGN KEY ("ID_MOTIVO_CC_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_MOTIVO_CC" ("ID") ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_DOCUMENTO_HIST_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC" ADD CONSTRAINT "DOCUMENTO_HIST_CLIENTE_CC_FK" FOREIGN KEY ("ID_CLIENTE_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_CLIENTE_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC" ADD CONSTRAINT "DOCUMENTO_HIST_DOCUMENTO_FK" FOREIGN KEY ("ID_DOCUMENTO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_DOCUMENTO_CC" ("ID") ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_DOCUMENTO_HIST_CC" ADD CONSTRAINT "DOCUMENTO_HIST_EXP_CC_FK" FOREIGN KEY ("ID_EXPEDIENTE_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ("ID") ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_EMPLEADO
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" ADD CONSTRAINT "EMPLEADO_ESTUDIO_CC_FK" FOREIGN KEY ("ID_ESTUDIO_ABOG_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_ESTUDIO_ABOGADO_CC" ("ID") ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" ADD CONSTRAINT "EMPLEADO_OFICINA_FK" FOREIGN KEY ("ID_OFICINA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_OFICINA" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EMPLEADO" ADD CONSTRAINT "EMPLEADO_PERFIL_FK" FOREIGN KEY ("ID_PERFIL_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_PERFIL" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_EXP_CUENTA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_EXP_CUENTA_CC" ADD CONSTRAINT "TBL_CE_IBM_EXP_CUENTA_CC_FK2" FOREIGN KEY ("ID_CUENTA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_CUENTA_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_EXPEDIENTE_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ADD CONSTRAINT "EXPEDIENTE_CC_CAMPANIA_CC_FK" FOREIGN KEY ("ID_CAMPANIA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_CAMPANIA_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ADD CONSTRAINT "EXPEDIENTE_CC_CLIENTE_CC_FK" FOREIGN KEY ("ID_CLIENTE_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_CLIENTE_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ADD CONSTRAINT "EXPEDIENTE_CC_EMPLEADO_FK" FOREIGN KEY ("ID_EMPLEADO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EMPLEADO" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ADD CONSTRAINT "EXPEDIENTE_CC_ESTADOEXP_CC_FK" FOREIGN KEY ("ID_ESTADO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_ESTADO_EXP_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ADD CONSTRAINT "EXPEDIENTE_CC_MOTIVO_CC_FK" FOREIGN KEY ("ID_MOTIVO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_MOTIVO_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ADD CONSTRAINT "EXPEDIENTE_CC_OFICINA_FK" FOREIGN KEY ("ID_OFICINA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_OFICINA" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ADD CONSTRAINT "EXPEDIENTE_CC_OPERACION_CC_FK" FOREIGN KEY ("ID_OPERACION_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_OPERACION_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ADD CONSTRAINT "EXPEDIENTE_CC_PRODUCTO_CC_FK" FOREIGN KEY ("ID_PRODUCTO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_PRODUCTO_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_EXPEDIENTE_TAREA_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_TAREA_CC" ADD CONSTRAINT "TBL_CE_IBM_EXPTAR_ESTTAR_FK" FOREIGN KEY ("ID_ESTADO_TAREA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_ESTADO_TAREA_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPEDIENTE_TAREA_CC" ADD CONSTRAINT "TBL_CE_IBM_EXPTAR_TAR_CC_FK" FOREIGN KEY ("ID_TAREA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_TAREA_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_EXPTAR_PROCESO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPTAR_PROCESO_CC" ADD CONSTRAINT "TBL_CE_IBM_EXPTARPROC_EMP_FK" FOREIGN KEY ("ID_EMPLEADO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EMPLEADO" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_EXPTAR_PROCESO_CC" ADD CONSTRAINT "TBL_CE_IBM_EXPTARPROC_TAR_FK" FOREIGN KEY ("ID_TAREA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_TAREA_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_GUIA_DOCUMENT_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" ADD CONSTRAINT "GUIA_DOCUMENT_DOCUMENT_CC_FK" FOREIGN KEY ("ID_DOCUMENTO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_DOCUMENTO_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" ADD CONSTRAINT "GUIA_DOCUMENT_OPERACION_CC_FK" FOREIGN KEY ("ID_OPERACION_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_OPERACION_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_GUIA_DOCUMENT_CC" ADD CONSTRAINT "GUIA_DOCUMENT_PRODUCTO_CC_FK" FOREIGN KEY ("ID_PRODUCTO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_PRODUCTO_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_HISTORIAL_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" ADD CONSTRAINT "HISTORIAL_CC_CAMPANIA_CC_FK" FOREIGN KEY ("ID_CAMPANIA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_CAMPANIA_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" ADD CONSTRAINT "HISTORIAL_CC_CLIENTE_CC_FK" FOREIGN KEY ("ID_CLIENTE_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_CLIENTE_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" ADD CONSTRAINT "HISTORIAL_CC_EMPLEADO_FK" FOREIGN KEY ("ID_EMPLEADO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EMPLEADO" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" ADD CONSTRAINT "HISTORIAL_CC_ESTADO_CC_FK" FOREIGN KEY ("ID_ESTADO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_ESTADO_EXP_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" ADD CONSTRAINT "HISTORIAL_CC_MOTIVO_CC_FK" FOREIGN KEY ("ID_MOTIVO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_MOTIVO_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" ADD CONSTRAINT "HISTORIAL_CC_OPERACION_FK" FOREIGN KEY ("ID_OPERACION_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_OPERACION_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" ADD CONSTRAINT "HISTORIAL_CC_PRODUCTO_CC_FK" FOREIGN KEY ("ID_PRODUCTO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_PRODUCTO_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_HISTORIAL_CC" ADD CONSTRAINT "HISTORIAL_CC_TAREA_CC_FK" FOREIGN KEY ("ID_TAREA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_TAREA_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_LOG_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_LOG_CC" ADD CONSTRAINT "TBL_CE_IBM_LOG_CC_EMPLEADO_FK" FOREIGN KEY ("ID_EMPLEADO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EMPLEADO" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_LOG_CC" ADD CONSTRAINT "TBL_CE_IBM_LOG_CC_EST_EXP_FK" FOREIGN KEY ("ID_ESTADO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_ESTADO_EXP_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_MOTIVO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_MOTIVO_CC" ADD CONSTRAINT "TBL_CE_IBM_MOTIVO_CC_TBL__FK1" FOREIGN KEY ("ID_TAREA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_TAREA_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_PARTICIPE_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PARTICIPE_CC" ADD CONSTRAINT "TBL_CE_IBM_PARTICIPE_CC_T_FK1" FOREIGN KEY ("ID_CUENTA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_CUENTA_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_PERFIL
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PERFIL" ADD CONSTRAINT "PERFIL_PERFIL_FK" FOREIGN KEY ("ID_PERFIL_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_PERFIL" ("ID") ON DELETE CASCADE  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_PERFIL_BALANCEO_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_PERFIL_BALANCEO_CC" ADD CONSTRAINT "TBL_CE_IBM_PERFIL_BALANCE_FK1" FOREIGN KEY ("ID_PERFIL_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_PERFIL" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_PERFIL_BALANCEO_CC" ADD CONSTRAINT "TBL_CE_IBM_PERFIL_BALANCE_FK2" FOREIGN KEY ("ID_TIPO_BALANCEO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_TIPO_BALANCEO_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_TAREA_PERFIL_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREA_PERFIL_CC" ADD CONSTRAINT "TAREAPERFIL_PERFIL_FK" FOREIGN KEY ("ID_PERFIL_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_PERFIL" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREA_PERFIL_CC" ADD CONSTRAINT "TAREAXPERFIL_TAREA_CC_FK" FOREIGN KEY ("ID_TAREA_CC_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_TAREA_CC" ("ID")  ;
--------------------------------------------------------
--  Ref Constraints for Table TBL_CE_IBM_TAREAS_REASIG_CC
--------------------------------------------------------

  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREAS_REASIG_CC" ADD CONSTRAINT "TBL_CE_IBM_REATAR_EMP_A_FK" FOREIGN KEY ("ID_EMPLEADO_A_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EMPLEADO" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREAS_REASIG_CC" ADD CONSTRAINT "TBL_CE_IBM_REATAR_EMP_DE_FK" FOREIGN KEY ("ID_EMPLEADO_DE_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EMPLEADO" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREAS_REASIG_CC" ADD CONSTRAINT "TBL_CE_IBM_REATAR_MOTIVO_FK" FOREIGN KEY ("ID_MOTIVO_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_MOTIVO_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREAS_REASIG_CC" ADD CONSTRAINT "TBL_CE_IBM_REATAR_TAREA_FK" FOREIGN KEY ("ID_TAREA_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_TAREA_CC" ("ID")  ;
  ALTER TABLE "CONELE"."TBL_CE_IBM_TAREAS_REASIG_CC" ADD CONSTRAINT "TBL_CE_IBM_TARREASIG_EXP_CC_FK" FOREIGN KEY ("ID_EXPEDIENTE_FK")
	  REFERENCES "CONELE"."TBL_CE_IBM_EXPEDIENTE_CC" ("ID")  ;
--------------------------------------------------------
--  DDL for View VIEW_CE_IBM_NUMERO_EXPEDIENTES
--------------------------------------------------------

  CREATE OR REPLACE VIEW "CONELE"."VIEW_CE_IBM_NUMERO_EXPEDIENTES" AS 
  SELECT CAR.ID_PRODUCTO_FK AS ID_PRODUCTO, CAR.ID_TERRITORIO_FK AS ID_TERRITORIO, TAPER.ID_TAREA_CC_FK AS ID_TAREA, CAR.ID_EMPLEADO_FK AS ID_EMPLEADO, EMP.CODIGO AS COD_EMPLEADO, EMP.NOMBRES || ' ' || EMP.APEPAT || ' ' || EMP.APEMAT AS NOM_EMPLEADO, PER.ID AS ID_PERFIL, PER.DESCRIPCION AS DES_PERFIL, COUNT(EXP.ID) AS NUM_EXPEDIENTES
FROM 
CONELE.TBL_CE_IBM_CARTERIZACION CAR 
INNER JOIN (CONELE.TBL_CE_IBM_EMPLEADO EMP 
    INNER JOIN (CONELE.TBL_CE_IBM_PERFIL PER 
        INNER JOIN CONELE.TBL_CE_IBM_TAREA_PERFIL_CC TAPER 
        ON PER.ID = TAPER.ID_PERFIL_FK)
    ON EMP.ID_PERFIL_FK = PER.ID)
    LEFT OUTER JOIN CONELE.TBL_CE_IBM_EXPTAR_PROCESO_CC EXP 
    ON  EMP.ID = EXP.ID_EMPLEADO_FK
ON CAR.ID_EMPLEADO_FK = EMP.ID
WHERE
EMP.FLAG_ACTIVO = '1'
GROUP BY CAR.ID_PRODUCTO_FK, CAR.ID_TERRITORIO_FK, TAPER.ID_TAREA_CC_FK, CAR.ID_EMPLEADO_FK, EMP.CODIGO, EMP.NOMBRES, EMP.APEPAT, EMP.APEMAT, PER.ID, PER.DESCRIPCION
ORDER BY NUM_EXPEDIENTES ASC;
--------------------------------------------------------
--  DDL for View VIEW_CE_IBM_NUMERO_PARTICIPES
--------------------------------------------------------

  CREATE OR REPLACE VIEW "CONELE"."VIEW_CE_IBM_NUMERO_PARTICIPES" AS 
  SELECT CAR.ID_PRODUCTO_FK AS ID_PRODUCTO, CAR.ID_TERRITORIO_FK AS ID_TERRITORIO, TAPER.ID_TAREA_CC_FK AS ID_TAREA, CAR.ID_EMPLEADO_FK AS ID_EMPLEADO, EMP.CODIGO AS COD_EMPLEADO, EMP.NOMBRES || ' ' || EMP.APEPAT || ' ' || EMP.APEMAT AS NOM_EMPLEADO, PER.ID AS ID_PERFIL, PER.DESCRIPCION AS DES_PERFIL, EXP.id_expediente_fk AS ID_EXPEDIENTE, COUNT(PAR.ID) AS NUM_PARTICIPES
FROM 
CONELE.TBL_CE_IBM_CARTERIZACION CAR 
INNER JOIN (CONELE.TBL_CE_IBM_EMPLEADO EMP 
    INNER JOIN (CONELE.TBL_CE_IBM_PERFIL PER 
        INNER JOIN CONELE.TBL_CE_IBM_TAREA_PERFIL_CC TAPER 
        ON PER.ID = TAPER.ID_PERFIL_FK)
    ON EMP.ID_PERFIL_FK = PER.ID)
    INNER JOIN (CONELE.TBL_CE_IBM_EXPTAR_PROCESO_CC EXP 
        INNER JOIN (CONELE.TBL_CE_IBM_EXP_CUENTA_CC EXPCTA
          INNER JOIN TBL_CE_IBM_PARTICIPE_CC PAR
          ON EXPCTA.ID_CUENTA_FK = PAR.ID_CUENTA_FK)
        ON EXP.id_expediente_fk = EXPCTA.ID_EXPEDIENTE_FK)
    ON  EMP.ID = EXP.ID_EMPLEADO_FK
ON CAR.ID_EMPLEADO_FK = EMP.ID
WHERE
EMP.FLAG_ACTIVO = '1'
GROUP BY CAR.ID_PRODUCTO_FK, CAR.ID_TERRITORIO_FK, TAPER.ID_TAREA_CC_FK, CAR.ID_EMPLEADO_FK, EMP.CODIGO, EMP.NOMBRES, EMP.APEPAT, EMP.APEMAT, PER.ID, PER.DESCRIPCION, EXP.id_expediente_fk
ORDER BY NUM_PARTICIPES ASC;

--------------------------------------------------------
--  DDL for View VIEW_CE_IBM_PESO_PARTICIPE_EXP
--------------------------------------------------------

  CREATE OR REPLACE VIEW "CONELE"."VIEW_CE_IBM_PESO_PARTICIPE_EXP"  AS 
  SELECT VNUMPAR.ID_PRODUCTO, VNUMPAR.ID_TERRITORIO, VNUMPAR.ID_TAREA, VNUMPAR.ID_EMPLEADO, VNUMPAR.COD_EMPLEADO, VNUMPAR.NOM_EMPLEADO, VNUMPAR.ID_PERFIL, VNUMPAR.DES_PERFIL, SUM(PESPAR.PESO) AS SUM_PESOPARTICIPE
FROM CONELE.VIEW_CE_IBM_NUMERO_PARTICIPES VNUMPAR, CONELE.TBL_CE_IBM_PESO_PARTICIPE_CC PESPAR
WHERE
NUM_PARTICIPES >= PESPAR.NUMERO_DESDE AND 
NUM_PARTICIPES <= PESPAR.NUMERO_HASTA
GROUP BY ID_PRODUCTO, ID_TERRITORIO, ID_TAREA, ID_EMPLEADO, COD_EMPLEADO, NOM_EMPLEADO, ID_PERFIL, DES_PERFIL
ORDER BY SUM_PESOPARTICIPE ASC;
--------------------------------------------------------
--  DDL for View VIEW_CE_IBM_SUMA_PESODOC_EXP
--------------------------------------------------------

  CREATE OR REPLACE VIEW "CONELE"."VIEW_CE_IBM_SUMA_PESODOC_EXP" AS 
  SELECT CAR.ID_PRODUCTO_FK AS ID_PRODUCTO, CAR.ID_TERRITORIO_FK AS ID_TERRITORIO, TAPER.ID_TAREA_CC_FK AS ID_TAREA, CAR.ID_EMPLEADO_FK AS ID_EMPLEADO, EMP.CODIGO AS COD_EMPLEADO, EMP.NOMBRES || ' ' || EMP.APEPAT || ' ' || EMP.APEMAT AS NOM_EMPLEADO, PER.ID AS ID_PERFIL, PER.DESCRIPCION AS DES_PERFIL, EXP.id_expediente_fk AS ID_EXPEDIENTE, SUM(nvl(DOCEXP.DOC_PESO_PROM_KB,0)) as SUMA_PESOS_DOC_KB
FROM 
CONELE.TBL_CE_IBM_CARTERIZACION CAR 
INNER JOIN (CONELE.TBL_CE_IBM_EMPLEADO EMP 
    INNER JOIN (CONELE.TBL_CE_IBM_PERFIL PER 
        INNER JOIN CONELE.TBL_CE_IBM_TAREA_PERFIL_CC TAPER 
        ON PER.ID = TAPER.ID_PERFIL_FK)
    ON EMP.ID_PERFIL_FK = PER.ID)
    INNER JOIN (CONELE.TBL_CE_IBM_EXPTAR_PROCESO_CC EXP 
        INNER JOIN CONELE.TBL_CE_IBM_DOCUMENTO_EXP_CC DOCEXP
        ON EXP.id_expediente_fk = DOCEXP.ID_EXPEDIENTE_CC_FK)
    ON  EMP.ID = EXP.ID_EMPLEADO_FK
ON CAR.ID_EMPLEADO_FK = EMP.ID
WHERE
EMP.FLAG_ACTIVO = '1'
GROUP BY CAR.ID_PRODUCTO_FK, CAR.ID_TERRITORIO_FK, TAPER.ID_TAREA_CC_FK, CAR.ID_EMPLEADO_FK, EMP.CODIGO, EMP.FLAG_ACTIVO, EMP.NOMBRES, EMP.APEPAT, EMP.APEMAT, PER.ID, PER.DESCRIPCION, EXP.id_expediente_fk
ORDER BY SUMA_PESOS_DOC_KB ASC;
--------------------------------------------------------
--  DDL for View VIEW_CE_IBM_PESO_EXP_EMP
--------------------------------------------------------

  CREATE OR REPLACE VIEW "CONELE"."VIEW_CE_IBM_PESO_EXP_EMP" AS 
  SELECT VSUMDOCEXP.ID_PRODUCTO, VSUMDOCEXP.ID_TERRITORIO, VSUMDOCEXP.ID_TAREA, VSUMDOCEXP.ID_EMPLEADO, VSUMDOCEXP.COD_EMPLEADO, VSUMDOCEXP.NOM_EMPLEADO, VSUMDOCEXP.ID_PERFIL, VSUMDOCEXP.DES_PERFIL, SUM(PESEXP.PESO) AS SUM_PESO_DOC_EXPEDIENTE
FROM CONELE.VIEW_CE_IBM_SUMA_PESODOC_EXP VSUMDOCEXP, CONELE.TBL_CE_IBM_PESO_EXP_CC PESEXP
WHERE
SUMA_PESOS_DOC_KB/1024 > PESEXP.PESO_MB_DESDE AND 
SUMA_PESOS_DOC_KB/1024 <= PESEXP.PESO_MB_HASTA
GROUP BY ID_PRODUCTO, ID_TERRITORIO, ID_TAREA, ID_EMPLEADO, COD_EMPLEADO, NOM_EMPLEADO, ID_PERFIL, DES_PERFIL
ORDER BY SUM_PESO_DOC_EXPEDIENTE ASC;
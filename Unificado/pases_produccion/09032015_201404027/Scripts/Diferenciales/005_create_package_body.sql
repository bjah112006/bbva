spool 005_create_package_body.log replace;
/*
	Reporte HISTORICO-CONSOLIDADO
*/

create or replace
PACKAGE BODY                  "CONELE"."PG_CE_REPORTE_TC" AS 
 
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
	)
	AS
  C_SQL_EXEC_REP_TC VARCHAR(32000);
  C_SQL_WHERE_REP_TC VARCHAR(32000);
  C_SQL_ORDER_REP_TC VARCHAR(32000);
  
  BEGIN
  

    C_SQL_WHERE_REP_TC := ' WHERE 1=1 ';  
    
    IF TIPO_BUSQ = 1 THEN/*REPORTE CONSOLIDADO*/
    
      C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND  EXP.ID_PRODUCTO_FK IS NOT NULL  ';
      C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND  EXPTC.ID_TIPO_OFERTA_FK IS NOT NULL  ';
      
      IF PRODUCTO > -1 THEN
         C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( EXP.ID_PRODUCTO_FK = '||PRODUCTO||') ';    
      END IF;
      IF TIPO_OFERTA > -1 THEN
         C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( EXPTC.ID_TIPO_OFERTA_FK = '||TIPO_OFERTA||') ';
      END IF;   
      IF ESTADO > -1 THEN
      --   C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( EXP.ID_ESTADO_FK = '||ESTADO||') ';
	 C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( EXP.ID_ESTADO_FK IN (SELECT ID_ESTADO_FK FROM CONELE.TBL_CE_IBM_ESTADO_TAREA_CE where ID_ESTADO_CE_FK IN (SELECT ID FROM TBL_CE_IBM_ESTADO_CE WHERE ID = '||ESTADO||')) ) ';

      END IF;   
      
    ELSE/*REPORTE HISTORICO*/
    
      IF PRODUCTO > -1 THEN
         C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( HIS.ID_PRODUCTO_FK = '||PRODUCTO||') ';    
      END IF;
      IF TIPO_OFERTA > -1 THEN
         C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( HIS.ID_TIPO_OFERTA_FK = '||TIPO_OFERTA||') ';
      END IF;  
      IF ESTADO > -1 THEN
     --    C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( HIS.ID_ESTADO_FK = '||ESTADO||') ';
       C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( HIS.ID_ESTADO_FK IN (SELECT ID_ESTADO_FK FROM CONELE.TBL_CE_IBM_ESTADO_TAREA_CE where ID_ESTADO_CE_FK IN (SELECT ID FROM TBL_CE_IBM_ESTADO_CE WHERE ID = '||ESTADO||')) ) ';
      END IF; 
      C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC || ' AND HIS.ID_ESTADO_FK NOT IN (62) '; 
      
    END IF;
    
    IF PERFIL > -1 THEN
       C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( EMP.ID_PERFIL_FK = '||PERFIL||') ';
    END IF;

    IF TERRITORIO > -1 THEN
       C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( OFC.ID_TERRITORIO_FK = '||TERRITORIO||') ';
    END IF;
    
    IF OFICINA > -1 THEN
       C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||' AND ( EMP.ID_OFICINA_FK = '||OFICINA||') ';
    END IF;  
    
  
    
    /*REPORTE CONSOLIDADO*/
    IF TIPO_BUSQ = 1 THEN 
      C_SQL_ORDER_REP_TC := 'ORDER BY 1 ';  
    -- SINTAXIS SQL WHERE
      DBMS_OUTPUT.PUT_LINE('TIPO_BUSQ 1');
      --C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||'  AND (   to_char(CAST(EXP.FEC_REGISTRO AS DATE),'||CHR(39)||'DD/MM/YY'||CHR(39)||') >= TO_DATE('||CHR(39)||FECHA_INICIO||CHR(39)||','||CHR(39)||'DD/MM/YY'||CHR(39)||' ) AND   to_char(CAST(EXP.FEC_REGISTRO AS DATE),'||CHR(39)||'DD/MM/YY'||CHR(39)||')    <= TO_DATE('||CHR(39)||FECHA_FINAL||CHR(39)||','||CHR(39)||'DD/MM/YY'||CHR(39)||' ) )';
      C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||'  AND EXP.FEC_REGISTRO IS NOT NULL  AND (   TO_DATE(CAST(EXP.FEC_REGISTRO AS DATE),'||CHR(39)||'DD/MM/YY'||CHR(39)||') >= TO_DATE('||CHR(39)||FECHA_INICIO||CHR(39)||','||CHR(39)||'DD/MM/YY'||CHR(39)||' ) AND   TO_DATE(CAST(EXP.FEC_REGISTRO AS DATE),'||CHR(39)||'DD/MM/YY'||CHR(39)||')    <= TO_DATE('||CHR(39)||FECHA_FINAL||CHR(39)||','||CHR(39)||'DD/MM/YY'||CHR(39)||' ) )';
      
      C_SQL_EXEC_REP_TC :=  'SELECT DISTINCT NVL(EXPTC.ID_EXP_FK,'||CHR(39)||''||CHR(39)||') NUMERO_EXPEDIENTE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(CLTE.ID_SEGMENTO_FK,'||CHR(39)||''||CHR(39)||') CODIGO_SEGMENTO_CLIENTE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(SEG.DESCRIPCION,'||CHR(39)||''||CHR(39)||') DESCRIPCION_SEGMENTO_CLIENTE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXP.ID_ESTADO_FK,'||CHR(39)||''||CHR(39)||') CORRELATIVO_ESTADO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EST.DESCRIPCION,'||CHR(39)||''||CHR(39)||') NOMBRE_ESTADO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(PROD.DESCRIPCION,'||CHR(39)||''||CHR(39)||') NOMBRE_PRODUCTO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EMP.CODIGO,'||CHR(39)||''||CHR(39)||') CODIGO_USUARIO_CREACION,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'TRIM(TRIM(EMP.NOMBRES)||'||CHR(39)||' '||CHR(39)||'||TRIM(EMP.APEPAT)||'||CHR(39)||' '||CHR(39)||'||TRIM(EMP.APEMAT)) NOMBRE_USUARIO_CREACION,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' TO_CHAR(EXP.FEC_REGISTRO,'||CHR(39)||'DD-MM-YYYY HH:MI:SS AM'||CHR(39)||') FECHA_CREACION,';
	    C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EMP.ID_OFICINA_FK,'||CHR(39)||''||CHR(39)||') CORRELATIVO_OFICINA,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(OFC.CODIGO,'||CHR(39)||''||CHR(39)||') CODIGO_OFICINA,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(OFC.DESCRIPCION,'||CHR(39)||''||CHR(39)||') NOMBRE_OFICINA,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.ID_GARANTIA_FK,'||CHR(39)||''||CHR(39)||') CODIGO_GARANTIA,';      
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(GARAN.DESCRIPCION,'||CHR(39)||''||CHR(39)||') DESCRIPCION_GARANTIA,';      
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.ID_SUBPRODUCTO_FK,'||CHR(39)||''||CHR(39)||') CORRELATIVO_SUBPRODUCTO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(SUB_PROD.DESCRIPCION,'||CHR(39)||''||CHR(39)||') NOMBRE_SUBPRODUCTO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXP.ID_CLIENTE_NATURAL_FK,'||CHR(39)||''||CHR(39)||') CORRELATIVO_CLIENTE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(CLTE.APE_PAT,'||CHR(39)||''||CHR(39)||') APELLIDO_PATERNO_CLIENTE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(CLTE.APE_MAT,'||CHR(39)||''||CHR(39)||') APELLIDO_MATERNO_CLIENTE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(CLTE.NOMBRE,'||CHR(39)||''||CHR(39)||') NOMBRES_CLIENTE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(TIPDOC.DESCRIPCION,'||CHR(39)||''||CHR(39)||') TIPO_DOCUMENTO_IDENTIDAD,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'TO_CHAR(NVL(CLTE.NUM_DOI,'||CHR(39)||''||CHR(39)||')) NUMERO_DOCUMENTO_IDENTIDAD,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(TIPCLTE.DESCRIPCION,'||CHR(39)||''||CHR(39)||') TIPO_CLIENTE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(CLTE.ING_NETO_MENSUAL,'||CHR(39)||''||CHR(39)||') INGRESO_NETO_MENSUAL,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(ESTCIV.DESCRIPCION,'||CHR(39)||''||CHR(39)||') ESTADO_CIVIL,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' CASE CLTE.PER_EXP_PUB';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'0'||CHR(39)||' THEN '||CHR(39)||'NO'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' END PERSONA_EXPUESTA_PUBLICA,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' CASE CLTE.PAGO_HAB ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'0'||CHR(39)||' THEN '||CHR(39)||'NO'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' END PAGO_HABIENTE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' CASE CLTE.SUBROG ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'0'||CHR(39)||' THEN '||CHR(39)||'NO'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' END SUBROGADO,';      
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(TIPOFE.DESCRIPCION,'||CHR(39)||''||CHR(39)||') TIPO_OFERTA,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(TIPMONSOL.DESCRIPCION,'||CHR(39)||''||CHR(39)||') MONEDA_IMPORTE_SOLICITADO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.LINEA_CRED_SOL,'||CHR(39)||''||CHR(39)||') IMPORTE_SOLICITADO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(TIPMONAPR.DESCRIPCION,'||CHR(39)||''||CHR(39)||') MONEDA_IMPORTE_APROBADO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.LINEA_CRED_APROB,'||CHR(39)||''||CHR(39)||') IMPORTE_APROBADO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.PLAZO_SOL,'||CHR(39)||''||CHR(39)||') PLAZO_SOLICITADO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.PLAZO_SOL_APROB,'||CHR(39)||'0'||CHR(39)||') PLAZO_APROBADO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'(CASE NVL(EXPTC.TIPO_RESOLUCION,'||CHR(39)||''||CHR(39)||') WHEN '||CHR(39)||'C'||CHR(39)||' THEN '||CHR(39)||'Resolución con modificación'||CHR(39)||' 
                                                WHEN '||CHR(39)||'S'||CHR(39)||' THEN '||CHR(39)||'Resolución sin modificación'||CHR(39)||' END) TIPO_RESOLUCION , ' ;
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'CAST(NVL(EXPTC.COD_PRE_EVAL,'||CHR(39)||''||CHR(39)||') AS VARCHAR(20)) CODIGO_PREEVALUADOR,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.RVGL,'||CHR(39)||''||CHR(39)||') CODIGO_RVGL,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.LINEA_CONSUMO,'||CHR(39)||''||CHR(39)||') LINEA_CONSUMO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.RIESGO_CLIENTE,'||CHR(39)||''||CHR(39)||') RIESGO_CLIENTE_GRUPAL,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.PORCENTAJE_ENDEUDAMIENTO,'||CHR(39)||''||CHR(39)||') PORCENTAJE_ENDEUDAMIENTO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'CAST(NVL(EXPTC.NRO_CONTRATO,'||CHR(39)||''||CHR(39)||') AS VARCHAR(20)) CODIGO_CONTRATO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(TIPBURO.DESCRIPCION,'||CHR(39)||''||CHR(39)||') GRUPO_BURO,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.CLASIFICACION_SBS,'||CHR(39)||''||CHR(39)||') CLASIFICACION_SBS_TITULAR,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.CLASIFICACION_BANCO,'||CHR(39)||''||CHR(39)||') CLASIFICACION_BANCO_TITULAR,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.SBS_CONYUGE,'||CHR(39)||''||CHR(39)||') CLASIFICACION_SBS_CONYUGE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL((select cb.descripcion from CONELE.tbl_ce_ibm_clasif_banco cb where cb.id=EXPTC.BANCO_CONYUGE),'||CHR(39)||''||CHR(39)||') CLASIFICACION_BANCO_CONYUGE,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'(CASE TO_CHAR(NVL(EXPTC.ID_TIPO_SCORING_FK,'||CHR(39)||''||CHR(39)||')) WHEN '||CHR(39)|| 1 ||CHR(39)||' THEN '||CHR(39)||'APROBADO'||CHR(39)||'  
                                                 WHEN '||CHR(39)|| 2 ||CHR(39)||' THEN '||CHR(39)||'RECHAZADO'||CHR(39)||' WHEN '||CHR(39)||3||CHR(39)||' THEN '||CHR(39)||'OBSERVADO'||CHR(39)||' ELSE '||CHR(39)||''||CHR(39)||' END) SCORING,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'(CASE TO_CHAR(NVL(EXPTC.TASA_ESP,'||CHR(39)||''||CHR(39)||')) WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39)||' ELSE 
                                                 '||CHR(39)||'NO'||CHR(39)||' END) TASA_ESPECIAL,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'(CASE NVL(EXPTC.VERIF_DOM,'||CHR(39)||''||CHR(39)||') WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39)||' ELSE 
                                                 '||CHR(39)||'NO'||CHR(39)||' END) FLAG_VERIF_DOMICILIARIA,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'( SELECT NVL( (SELECT CASE NVL(X.FLAG_VERIF,'||CHR(39)||'-1'||CHR(39)||')';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'0'||CHR(39)||' THEN '||CHR(39)||'APROBADO'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'OBSERVADO'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'-1'||CHR(39)||' THEN '||CHR(39)||'PENDIENTE'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' END ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' FROM CONELE.TBL_CE_IBM_VERIFICACION_EXP X, CONELE.TBL_CE_IBM_TIPO_VERIFICACION Y';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHERE (Y.ID = X.ID_TIPO_VERIFICACION_FK)';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' AND (X.ID_EXPEDIENTE_FK = EXPTC.ID_EXP_FK)';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' AND (Y.ID = 2) AND (EXPTC.VERIF_DOM = '||CHR(39)||'1'||CHR(39)||') AND ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' (X.id IN (SELECT MAX(ve.id) FROM CONELE.TBL_CE_IBM_VERIFICACION_EXP VE';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHERE ve.id_expediente_fk=X.ID_EXPEDIENTE_FK AND ve.id_tipo_verificacion_fk=2)) ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' ) ,'||CHR(39)||'PENDIENTE'||CHR(39)||') FROM DUAL WHERE EXPTC.VERIF_DOM = '||CHR(39)||'1'||CHR(39)||'  ) ESTADO_VERIF_DOMICILIARIA,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'(CASE NVL(EXPTC.VERIF_LAB,'||CHR(39)||''||CHR(39)||') WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39)||' ELSE 
                                                 '||CHR(39)||'NO'||CHR(39)||' END) FLAG_VERIF_LABORAL,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'( SELECT NVL(  (SELECT CASE NVL(X.FLAG_VERIF,'||CHR(39)||'-1'||CHR(39)||')';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'               WHEN '||CHR(39)||'0'||CHR(39)||' THEN '||CHR(39)||'APROBADO'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'               WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'OBSERVADO'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'               WHEN '||CHR(39)||'-1'||CHR(39)||' THEN '||CHR(39)||'PENDIENTE'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'        END ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'   FROM CONELE.TBL_CE_IBM_VERIFICACION_EXP X, CONELE.TBL_CE_IBM_TIPO_VERIFICACION Y';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'  WHERE (Y.ID = X.ID_TIPO_VERIFICACION_FK)';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'    AND (X.ID_EXPEDIENTE_FK = EXPTC.ID_EXP_FK)';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'    AND (Y.ID = 1) AND (EXPTC.VERIF_LAB = '||CHR(39)||'1'||CHR(39)||') AND ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' (X.id IN (SELECT MAX(ve.id) FROM CONELE.TBL_CE_IBM_VERIFICACION_EXP VE';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHERE ve.id_expediente_fk=X.ID_EXPEDIENTE_FK AND ve.id_tipo_verificacion_fk=1)) ';      
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' ),'||CHR(39)||'PENDIENTE'||CHR(39)||') FROM DUAL WHERE EXPTC.VERIF_LAB = '||CHR(39)||'1'||CHR(39)||'  ) ESTADO_VERIF_LABORAL,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'(CASE NVL(EXPTC.VERIF_DPS,'||CHR(39)||''||CHR(39)||') WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39)||'  
                                                 ELSE '||CHR(39)||'NO'||CHR(39)||' END) FLAG_DPS,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'(SELECT NVL( (SELECT CASE NVL(X.FLAG_VERIF,'||CHR(39)||'-1'||CHR(39)||')';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'        	WHEN '||CHR(39)||'0'||CHR(39)||' THEN '||CHR(39)||'APROBADO'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'               WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'OBSERVADO'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'               WHEN '||CHR(39)||'-1'||CHR(39)||' THEN '||CHR(39)||'PENDIENTE'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'        END ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'   FROM CONELE.TBL_CE_IBM_VERIFICACION_EXP X, CONELE.TBL_CE_IBM_TIPO_VERIFICACION Y ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'  WHERE (Y.ID = X.ID_TIPO_VERIFICACION_FK) ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'    AND (X.ID_EXPEDIENTE_FK = EXPTC.ID_EXP_FK)';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'    AND (Y.ID = 4) AND (EXPTC.VERIF_DPS = '||CHR(39)||'1'||CHR(39)||') AND ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' (X.id IN (SELECT MAX(ve.id) FROM CONELE.TBL_CE_IBM_VERIFICACION_EXP VE';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' WHERE ve.id_expediente_fk=X.ID_EXPEDIENTE_FK AND ve.id_tipo_verificacion_fk=4)) '; 
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' ),'||CHR(39)||'PENDIENTE'||CHR(39)||') FROM DUAL WHERE EXPTC.VERIF_DPS = '||CHR(39)||'1'||CHR(39)||'  ) ESTADO_DPS,';
      
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'CASE EXPTC.FLAG_SOL_TASA_ESP';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'	WHEN '||CHR(39)||'0'||CHR(39)||' THEN '||CHR(39)||'NO'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'       WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39);
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'END MODIFICAR_TASA,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'(CASE NVL(EXPTC.FLAG_MODIF_SCORE,'||CHR(39)||''||CHR(39)||') WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39)||' ELSE 
                                                 '||CHR(39)||'NO'||CHR(39)||' END) MODIFICAR_SCORING,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'(CASE NVL(EXPTC.FLAG_DELEGACION,'||CHR(39)||''||CHR(39)||') WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39)||' ELSE 
                                                 '||CHR(39)||'NO'||CHR(39)||' END) INDICADOR_DELEGACION,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'(CASE NVL(EXPTC.FLAG_EXC_DELEGACION,'||CHR(39)||''||CHR(39)||') WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39)||' ELSE 
                                                 '||CHR(39)||'NO'||CHR(39)||' END) INDICADOR_EXCLUSION_DELEGACION,';
  
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' NVL((select nvl(mp.peso,0)*(NVL((select prod.peso from CONELE.tbl_ce_ibm_producto_ce prod where prod.id=EXP.ID_PRODUCTO_FK),0))';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' from CONELE.tbl_ce_ibm_monto_peso mp where mp.id_producto_fk=EXP.ID_PRODUCTO_FK and  ';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' (mp.monto_minimo<=(CASE EXPTC.ID_TIPMONSOL_FK WHEN 2 THEN EXPTC.LINEA_CRED_SOL*NVL(EXPTC.TIPO_CAMBIO_EXP,0) ELSE EXPTC.LINEA_CRED_SOL END) AND 
                                                  mp.monto_maximo>=(CASE EXPTC.ID_TIPMONSOL_FK WHEN 2 THEN EXPTC.LINEA_CRED_SOL*NVL(EXPTC.TIPO_CAMBIO_EXP,0) ELSE EXPTC.LINEA_CRED_SOL END))),0) NIVEL_COMPLEJIDAD , ';
                                                  
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL(EXPTC.NRO_DEVOLUCIONES,'||CHR(39)||0||CHR(39)||') NRO_DEVOLUCIONES,';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||'NVL((SELECT E.CODIGO FROM CONELE.TBL_CE_IBM_EMPLEADO_CE E WHERE E.ID=EXP.ID_EMPLEADO_FK),'||CHR(39)||0||CHR(39)||') CODIGO_USUARIO_ACTUAL ';
      
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' FROM CONELE.TBL_CE_IBM_EXPEDIENTE_TC EXPTC INNER JOIN CONELE.TBL_CE_IBM_EXPEDIENTE EXP ON EXPTC.ID_EXP_FK=EXP.ID '; 
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_CLIENTE_NATURAL CLTE ON CLTE.ID=EXP.ID_CLIENTE_NATURAL_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_SEGMENTO SEG ON SEG.ID = CLTE.ID_SEGMENTO_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_ESTADO EST ON EST.ID=EXP.ID_ESTADO_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_PRODUCTO_CE PROD ON PROD.ID=EXP.ID_PRODUCTO_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_EMPLEADO_CE EMP ON EMP.ID=EXPTC.ID_EMP_RESP_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_OFICINA_CE OFC ON OFC.ID = EMP.ID_OFICINA_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_TERRITORIO_CE TERR ON TERR.ID = OFC.ID_TERRITORIO_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_SUBPRODUCTO SUB_PROD ON SUB_PROD.ID = EXPTC.ID_SUBPRODUCTO_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_TIPO_DOI TIPDOC ON TIPDOC.ID = CLTE.ID_TIPO_DOI_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_TIPO_CLIENTE TIPCLTE ON TIPCLTE.ID = CLTE.ID_TIPO_CLIENTE_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_ESTADO_CIVIL ESTCIV ON ESTCIV.ID = CLTE.ID_ESTADO_CIVIL_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_TIPO_OFERTA TIPOFE ON TIPOFE.ID = EXPTC.ID_TIPO_OFERTA_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_TIPO_MONEDA TIPMONSOL ON TIPMONSOL.ID = EXPTC.ID_TIPMONSOL_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_TIPO_MONEDA TIPMONAPR ON TIPMONAPR.ID = EXPTC.ID_TIPMONAPROB_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_TIPO_BURO TIPBURO ON TIPBURO.ID = EXPTC.ID_TIPO_BURO_FK';
      C_SQL_EXEC_REP_TC :=  C_SQL_EXEC_REP_TC ||' LEFT JOIN TBL_CE_IBM_GARANTIAS GARAN ON GARAN.CODIGO=EXPTC.ID_GARANTIA_FK';  
      
      
    ELSE
      /*REPORTE HISTORICO*/
    -- SINTAXIS SQL WHERE
      C_SQL_ORDER_REP_TC := 'ORDER BY 1,2';  
      C_SQL_WHERE_REP_TC := C_SQL_WHERE_REP_TC ||'  AND HIS.FECHA_T3 IS NOT NULL AND (   TO_DATE( CAST(HIS.FECHA_T3 AS DATE),'||CHR(39)||'DD/MM/YY'||CHR(39)||' )   >= TO_DATE('||CHR(39)||FECHA_INICIO||CHR(39)||','||CHR(39)||'DD/MM/YY'||CHR(39)||' ) AND  TO_DATE(  CAST(HIS.FECHA_T3 AS DATE),'||CHR(39)||'DD/MM/YY'||CHR(39)||' )  <= TO_DATE('||CHR(39)||FECHA_FINAL||CHR(39)||','||CHR(39)||'DD/MM/YY'||CHR(39)||' ) )';
      
      
      C_SQL_EXEC_REP_TC := 'SELECT NVL(HIS.ID_EXPEDIENTE_FK,'||CHR(39)||''||CHR(39)||') NRO_EXPEDIENTE,';     
      --C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.ID,'||CHR(39)||''||CHR(39)||') NUMERO_REGISTRO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(EMP.CODIGO,'||CHR(39)||''||CHR(39)||') NUMERO_REGISTRO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'TRIM(EMP.NOMBRES)||'||CHR(39)||' '||CHR(39)||'||TRIM(EMP.APEPAT)||'||CHR(39)||' '||CHR(39)||'||TRIM(EMP.APEMAT) USUARIO_ACTUAL,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(EST.DESCRIPCION,'||CHR(39)||''||CHR(39)||') ESTADO_EXPEDIENTE,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(EST.CODIGO,'||CHR(39)||''||CHR(39)||') CODIGO_ESTADO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(PERFIL.DESCRIPCION,'||CHR(39)||''||CHR(39)||') PERFIL,';
            C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' NVL(TARE.DESCRIPCION,'||CHR(39)||'1.-Registrar Expediente'||CHR(39)||') TAREA,';
                  C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.ACCION,'||CHR(39)||''||CHR(39)||') ACCION,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.ID_PRODUCTO_FK,'||CHR(39)||''||CHR(39)||') CODIGO_PRODUCTO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(PROD.DESCRIPCION,'||CHR(39)||''||CHR(39)||') NOMBRE_PRODUCTO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(TIPOF.DESCRIPCION,'||CHR(39)||''||CHR(39)||') NOMBRE_TIPO_OFERTA,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.ID_SUBPRODUCTO_FK,'||CHR(39)||''||CHR(39)||') CODIGO_SUB_PRODUCTO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(SUBPROD.DESCRIPCION,'||CHR(39)||''||CHR(39)||') NOMBRE_SUB_PRODUCTO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(OFC.CODIGO,'||CHR(39)||''||CHR(39)||') CODIGO_OFICINA,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(OFC.DESCRIPCION,'||CHR(39)||''||CHR(39)||') NOMBRE_OFICINA,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(TERR.CODIGO,'||CHR(39)||''||CHR(39)||') CODIGO_TERRITORIO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(TERR.DESCRIPCION,'||CHR(39)||''||CHR(39)||') NOMBRE_TERRITORIO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.FECHA_T1,'||CHR(39)||''||CHR(39)||') FECHA_HORA_LLEGADA,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.FECHA_T2,'||CHR(39)||''||CHR(39)||') FECHA_HORA_INICIO_TRABAJO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.FECHA_T3,'||CHR(39)||''||CHR(39)||') FECHA_HORA_ENVIO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'(SELECT SUM(NVL(HIS.tiempo_ejecucion,0)+NVL(HIS.tiempo_ejecucionr,0)) FROM CONELE.TBL_CE_IBM_HISTORIAL H WHERE H.ID=HIS.ID) TIEMPO_EJECUCION_TE, ';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'(SELECT SUM(NVL(HIS.tiempo_cola,0)+NVL(HIS.tiempo_colar,0)+NVL(HIS.tiempo_cola_retraer,0)) FROM CONELE.TBL_CE_IBM_HISTORIAL H WHERE H.ID=HIS.ID ) TIEMPO_COLA_TC, ';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'(SELECT SUM(NVL(HIS.tiempo_ejecucion,0)+NVL(HIS.tiempo_ejecucionr,0)) + SUM(NVL(HIS.tiempo_cola,0)+NVL(HIS.tiempo_colar,0)+NVL(HIS.tiempo_cola_retraer,0)) FROM CONELE.TBL_CE_IBM_HISTORIAL H WHERE H.ID=HIS.ID )  TIEMPO_PROCESO_TP,';
      
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' CASE ';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' WHEN ((SELECT SUM(NVL(HIS.tiempo_ejecucion,0)+NVL(HIS.tiempo_ejecucionr,0)) + SUM(NVL(HIS.tiempo_cola,0)+NVL(HIS.tiempo_colar,0)+NVL(HIS.tiempo_cola_retraer,0))  
                                                  FROM CONELE.TBL_CE_IBM_HISTORIAL H WHERE H.ID=HIS.ID) <= 
                                                  (NVL(HIS.ANS,0))
                                                  )  THEN '||CHR(39)||'SI'||CHR(39);
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' ELSE '||CHR(39)||'NO'||CHR(39);
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' END CUMPLIO_ANS,';	
      
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' CASE NVL(HIS.FLAG_DEVOLUCION,'||CHR(39)||''||CHR(39)||')';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'1'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39);
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' ELSE '||CHR(39)||'NO'||CHR(39);
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' END FLAG_DEVOLUCION,';
      
      
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' CASE NVL(HIS.FLAG_RETRAER,'||CHR(39)||''||CHR(39)||')';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'N'||CHR(39)||' THEN '||CHR(39)||'NO'||CHR(39);
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||'S'||CHR(39)||' THEN '||CHR(39)||'SI'||CHR(39);
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' WHEN '||CHR(39)||''||CHR(39)||' THEN '||CHR(39)||'NO'||CHR(39);
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' ELSE '||CHR(39)||'NO'||CHR(39);
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' END FLAG_RETRAER,';
      
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.FLAG_RETRAER,'||CHR(39)||''||CHR(39)||') FLAG_RETRAER,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.NUM_TERMINAL,'||CHR(39)||''||CHR(39)||') TERMINAL,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.COMENTARIO_RECHAZO,'||CHR(39)||''||CHR(39)||') OBSERVACION,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(DEV.DESCRIPCION,'||CHR(39)||''||CHR(39)||') MOTIVO_RECHAZO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'HIS.COMENTARIO COMENTARIO,';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'NVL(HIS.ANS,'||CHR(39)||''||CHR(39)||') ANS';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||' FROM CONELE.TBL_CE_IBM_HISTORIAL HIS LEFT JOIN TBL_CE_IBM_ESTADO EST ON EST.ID = HIS.ID_ESTADO_FK';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'                               LEFT JOIN TBL_CE_IBM_TAREA TARE ON TARE.ID = HIS.ID_TAREA_FK';      
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'                               LEFT JOIN TBL_CE_IBM_PRODUCTO_CE PROD ON PROD.ID = HIS.ID_PRODUCTO_FK';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'                               LEFT JOIN TBL_CE_IBM_TIPO_OFERTA TIPOF ON TIPOF.ID = HIS.ID_TIPO_OFERTA_FK';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'                               INNER JOIN TBL_CE_IBM_CLIENTE_NATURAL CLI ON CLI.ID = HIS.ID_CLIENTE_NATURAL_FK';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'                               LEFT JOIN TBL_CE_IBM_MOTIVO_DEVOLUCION DEV ON DEV.ID = HIS.ID_MOTIVO_DEVOLUCION_FK';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'                               LEFT JOIN TBL_CE_IBM_SUBPRODUCTO SUBPROD ON SUBPROD.ID = HIS.ID_SUBPRODUCTO_FK';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'                               LEFT JOIN TBL_CE_IBM_EMPLEADO_CE EMP ON EMP.ID = HIS.ID_EMPLEADO_FK';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'                               LEFT JOIN TBL_CE_IBM_OFICINA_CE OFC ON  OFC.ID = EMP.ID_OFICINA_FK';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'                               LEFT JOIN TBL_CE_IBM_TERRITORIO_CE TERR ON TERR.ID = OFC.ID_TERRITORIO_FK';
      C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC ||'                               INNER JOIN TBL_CE_IBM_PERFIL_CE PERFIL ON PERFIL.ID = EMP.ID_PERFIL_FK';

    END IF;
    
    /*MANDAMOS A IMPRIMIR EL REPORTE*/
    C_SQL_EXEC_REP_TC := C_SQL_EXEC_REP_TC || C_SQL_WHERE_REP_TC || C_SQL_ORDER_REP_TC;
    DBMS_OUTPUT.PUT_LINE(C_SQL_EXEC_REP_TC);
    
    OPEN LIST_REPORTE FOR C_SQL_EXEC_REP_TC; 
    
    EXCEPTION
    WHEN OTHERS THEN raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
   
	END SP_SEL_REPORTETC;
	
END;




/*
	Reporte TOE
*/
/
create or replace
PACKAGE BODY          "CONELE"."PG_CE_REPORTE" IS

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
    TIPO_FLUJO        IN NUMBER
	)
	AS
  
  C_SQL_EXECUTE_FLUJO_REPROCESO VARCHAR(32767);
  C_SQL_EXECUTE_FLUJO_REGULAR VARCHAR(32767);
  C_SQL_TIEMP_OBJ VARCHAR(20000);
  C_SQL_TIEMP_PRE VARCHAR(20000);
  C_SQL_TIEMP_REAL VARCHAR(20000);
  
  C_SQL_WHERE_COLUM VARCHAR(32767);
  
  C_SQL_WHERE_VAL_FLUJO VARCHAR(500);
  C_SQL_WHERE_VAL_OFICINA VARCHAR(500);
  C_SQL_WHERE_FECHA VARCHAR(700);
  C_SQL_WHERE_VAL_PRODUCTO VARCHAR(200);
  C_SQL_WHERE_VAL_TIPO_OFERTA VARCHAR(200);
  C_SQL_WHERE_VAL_ESTADO VARCHAR(200);
  C_SQL_WHERE_VAL_EST_CONSUL VARCHAR(200);
  C_SQL_WHERE_VAL_EST_CONSULTA VARCHAR(200);
  C_SQL_WHERE_VAL_EXPCERRADOS VARCHAR(200);  
  
  C_SQL_TIPO_FLUJO VARCHAR(10);
  C_SQL_TIPO_ROL VARCHAR(400);

  CONT_EXECUTE NUMBER; 
  
  CONT NUMBER;

  Cursor C1 Is
  select trim(valor) valor, id 
  from CONELE.tbl_ce_ibm_posible_valor where id_columna=236 order by id;

  Cursor C2 Is
  select trim(valor)  valor  
  from CONELE.tbl_ce_ibm_posible_valor where id_columna=235 order by id;

	BEGIN  

  C_SQL_EXECUTE_FLUJO_REPROCESO :='';
  C_SQL_EXECUTE_FLUJO_REGULAR :='';
  C_SQL_WHERE_VAL_OFICINA := '';
  C_SQL_WHERE_VAL_PRODUCTO := '';
  C_SQL_WHERE_VAL_TIPO_OFERTA := ''; 
  C_SQL_WHERE_VAL_EST_CONSULTA := ''; 
  C_SQL_WHERE_VAL_EST_CONSUL := ''; 
  C_SQL_WHERE_VAL_EXPCERRADOS := ' h.id_expediente_fk in (select he.id_expediente_fk from TBL_CE_IBM_HISTORIAL he 
                                  where he.id_estado_fk in (62) group by he.id_expediente_fk)';  
  CONT_EXECUTE :=1;
  /*
  C_SQL_WHERE_FECHA := ' h.fecha_t1 IS NOT NULL  AND (   TO_DATE(CAST(h.fecha_t1 AS DATE),'||CHR(39)||'DD/MM/YY'||CHR(39)||') >= TO_DATE('||CHR(39)||FECHA_INICIO||CHR(39)||','||CHR(39)||'DD/MM/YY'||CHR(39)||' ) AND  
  TO_DATE(CAST(h.fecha_t1 AS DATE),'||CHR(39)||'DD/MM/YY'||CHR(39)||')    <= TO_DATE('||CHR(39)||FECHA_FINAL||CHR(39)||','||CHR(39)||'DD/MM/YY'||CHR(39)||' ) ) AND ';
  */
  
  C_SQL_WHERE_FECHA := ' h.fecha_t3 IS NOT NULL  AND (   TO_DATE(CAST(h.fecha_t3 AS DATE),'||CHR(39)||'DD/MM/YY'||CHR(39)||') >= TO_DATE('||CHR(39)||FECHA_INICIO||CHR(39)||','||CHR(39)||'DD/MM/YY'||CHR(39)||' ) AND  
  TO_DATE(CAST(h.fecha_t3 AS DATE),'||CHR(39)||'DD/MM/YY'||CHR(39)||')    <= TO_DATE('||CHR(39)||FECHA_FINAL||CHR(39)||','||CHR(39)||'DD/MM/YY'||CHR(39)||' ) ) AND ';
  
    IF  (PRODUCTO > -1) THEN
      C_SQL_WHERE_VAL_PRODUCTO := ' h.id_producto_fk = '|| PRODUCTO || ' AND ';
    ELSE
      C_SQL_WHERE_VAL_PRODUCTO :='';
    END IF;   

    IF  (TIPO_OFERTA > -1) THEN
      C_SQL_WHERE_VAL_TIPO_OFERTA := ' h.ID_TIPO_OFERTA_FK='|| TIPO_OFERTA || ' AND ';
    ELSE
      C_SQL_WHERE_VAL_TIPO_OFERTA :='';
    END IF; 
    
    /* FILTROS PARA REPORTE ESPECIFICO*/
    IF  (PERFIL > -1) THEN
      C_SQL_WHERE_VAL_ESTADO := ' h.id_perfil_fk='|| PERFIL ||' AND ';
      C_SQL_WHERE_VAL_EST_CONSUL := 'tp.id_perfil_fk='|| PERFIL ||' AND ';
    ELSE
      C_SQL_WHERE_VAL_ESTADO :='';
      C_SQL_WHERE_VAL_EST_CONSUL :='';
    END IF; 
    /*
      tp.id_perfil_fk='|| PERFIL ||' and  et.id_estado_ce_fk='|| ESTADO ||' and     
    */
    IF  (PERFIL > -1 and ESTADO > -1) THEN
      C_SQL_WHERE_VAL_ESTADO := C_SQL_WHERE_VAL_ESTADO || ' h.id_estado_fk = '|| ESTADO || ' AND ';
      C_SQL_WHERE_VAL_EST_CONSUL := C_SQL_WHERE_VAL_EST_CONSUL || ' et.id_estado_ce_fk='|| ESTADO ||' AND ';
      C_SQL_WHERE_VAL_EST_CONSULTA := ' h.id_estado_fk IN (SELECT et.ID_ESTADO_FK FROM TBL_CE_IBM_ESTADO_TAREA_CE et where et.ID_ESTADO_CE_FK= '||ESTADO||') AND ';
    END IF; 
    /*
      h.id_estado_fk IN (SELECT et.ID_ESTADO_FK FROM TBL_CE_IBM_ESTADO_TAREA_CE et where et.ID_ESTADO_CE_FK= '||ESTADO||') AND
    */
    
    IF  (OFICINA > -1 and TERRITORIO >-1) THEN
     C_SQL_WHERE_VAL_OFICINA := ' h.ID_OFICINA_FK='|| OFICINA || ' and h.ID_OFICINA_FK in (select ter.id from CONELE.TBL_CE_IBM_OFICINA_CE ter where ter.ID_TERRITORIO_FK='|| TERRITORIO || ') AND ';
    END IF;
    
    
    
 /* BUSQUEDA GENERAL */
    IF(TIPO_BUSQ=0) THEN

    
    For Itera1Flujo In C2
    Loop   
    
      CONT :=1;
      C_SQL_TIPO_FLUJO := LTRIM(RTRIM(Itera1Flujo.valor));
      C_SQL_WHERE_VAL_FLUJO :='';
      
     --DBMS_OUTPUT.PUT_LINE('**********************************Itera1Flujo = ' ||C_SQL_TIPO_FLUJO);
      IF(C_SQL_TIPO_FLUJO ='P') THEN
        C_SQL_WHERE_VAL_FLUJO :='1'; /* FLUJO REPROCESO */
      ELSE
        C_SQL_WHERE_VAL_FLUJO :='0'; /* FLUJO REGULAR */
      END IF;
     
      C_SQL_WHERE_VAL_FLUJO :=' trim(NVL(h.FLAG_DEVOLUCION, '||chr(39)|| '0'|| chr(39)||')) LIKE trim('||chr(39)|| C_SQL_WHERE_VAL_FLUJO|| chr(39)||') AND ';
      
      For Itera1 In C1
          Loop 
          C_SQL_TIPO_ROL :='';
         
         IF(Itera1.id=24) THEN/* CONTROLLER SIN VERIFICACION */
            C_SQL_TIPO_ROL:=' (CASE WHEN (nvl(h.verif_dom,0)=0 AND nvl(h.verif_dps,0)=0 AND nvl(h.verif_lab,0)=0) 
                                 THEN 1 ELSE 0 END) = 1 AND ';        
         END IF;                    
         
         IF(Itera1.id=25) THEN/* CONTROLLER CON VERIFICACION */
            C_SQL_TIPO_ROL:=' (CASE WHEN (nvl(h.verif_dom,0)=1 OR nvl(h.verif_dps,0)=1 OR nvl(h.verif_lab,0)=1) 
                                 THEN 1 ELSE 0 END) = 1 AND ';
         END IF;

                C_SQL_TIEMP_OBJ := ' SELECT    '||chr(39)|| Itera1.valor || chr(39)||' columna,
            '||chr(39)|| 'T. OBJETIVO'|| chr(39)||' concepto,
            ROUND((sum(h.tiempo_obj_tc)/count(h.tiempo_obj_tc)),2) tc, ROUND((sum(h.tiempo_obj_te)/count(h.tiempo_obj_te)),2) te,
            '||chr(39)|| C_SQL_TIPO_FLUJO ||chr(39)||' flujo
            FROM CONELE.tbl_ce_ibm_toe_perfil_estado h 
            where h.nom_columna like '||chr(39)|| Itera1.valor || chr(39)||' and 
            '|| C_SQL_WHERE_VAL_TIPO_OFERTA || C_SQL_WHERE_VAL_PRODUCTO || ' trim(h.TIPO_FLUJO) like trim('||chr(39)||  C_SQL_TIPO_FLUJO || chr(39)||') and 
            h.id_tarea_fk in (select rpt.id_tarea_fk from CONELE.tbl_ce_ibm_rol_perfil_tarea rpt 
            where rpt.id_posible_valor_fk='|| Itera1.id || ')
            group by h.nom_columna ';  
  
                C_SQL_TIEMP_PRE := ' SELECT    '||chr(39)|| Itera1.valor || chr(39)||' columna,
            '||chr(39)|| 'T. PRELIMINAR' ||chr(39)||' concepto,
            ROUND((sum(h.tiempo_pre_tc)/count(h.tiempo_pre_tc)),2) tc, ROUND((sum(h.tiempo_pre_te)/count(h.tiempo_pre_te)),2) te,
            '||chr(39)|| C_SQL_TIPO_FLUJO ||chr(39)||' flujo
            FROM CONELE.tbl_ce_ibm_toe_perfil_estado h 
            where h.nom_columna like '||chr(39)|| Itera1.valor || chr(39)||' and 
            '|| C_SQL_WHERE_VAL_TIPO_OFERTA || C_SQL_WHERE_VAL_PRODUCTO ||  ' trim(h.TIPO_FLUJO) like trim('||chr(39)||  C_SQL_TIPO_FLUJO || chr(39)||') and 
            h.id_tarea_fk in (select rpt.id_tarea_fk from CONELE.tbl_ce_ibm_rol_perfil_tarea rpt 
            where rpt.id_posible_valor_fk='|| Itera1.id || ')
            group by h.nom_columna ';       
            
             C_SQL_TIEMP_REAL := ' SELECT '||chr(39)|| Itera1.valor ||chr(39)||' columna, 
            '||chr(39)|| 'T. REAL' ||chr(39)||' concepto,
            ROUND(MEDIAN(h.tiempo_cola+h.tiempo_colar+h.tiempo_cola_retraer),2) tc, 
            ROUND(MEDIAN(h.tiempo_ejecucion+h.tiempo_ejecucionr),2) te ,            
            '||chr(39)|| C_SQL_TIPO_FLUJO ||chr(39)||' flujo
            FROM CONELE.TBL_CE_IBM_HISTORIAL h
            WHERE  
            '|| C_SQL_WHERE_VAL_TIPO_OFERTA || C_SQL_WHERE_VAL_FLUJO || C_SQL_WHERE_FECHA || C_SQL_WHERE_VAL_PRODUCTO || '  
            nvl(h.ID_TAREA_FK,1) in ( select rpt.id_tarea_fk from CONELE.tbl_ce_ibm_rol_perfil_tarea rpt where rpt.id_posible_valor_fk='|| Itera1.id || ') AND 
            h.nom_columna like '||chr(39)|| Itera1.valor ||chr(39)||' AND 
            h.id_estado_fk in (SELECT ehist.id_estado_fk  FROM CONELE.tbl_ce_ibm_estado_tarea_ce ehist 
            where ehist.id_tarea_fk in ( select rpt.id_tarea_fk from CONELE.tbl_ce_ibm_rol_perfil_tarea rpt where rpt.id_posible_valor_fk='|| Itera1.id || ')) AND '|| C_SQL_TIPO_ROL || C_SQL_WHERE_VAL_EXPCERRADOS;
 
          /**
            CONT_EXECUTE=1 -->  FLUJO REPROCESO
            CONT_EXECUTE=2 -->  FLUJO REGULAR
            */
            IF CONT=1 THEN 
              IF CONT_EXECUTE=1 THEN
                C_SQL_EXECUTE_FLUJO_REPROCESO := C_SQL_TIEMP_OBJ || ' UNION ALL ' || C_SQL_TIEMP_PRE || ' UNION ALL ' || C_SQL_TIEMP_REAL ;
              ELSE
                C_SQL_EXECUTE_FLUJO_REGULAR := C_SQL_TIEMP_OBJ || ' UNION ALL ' || C_SQL_TIEMP_PRE || ' UNION ALL ' || C_SQL_TIEMP_REAL ;
              END IF;
            ELSE 
              IF CONT_EXECUTE=1 THEN
                C_SQL_EXECUTE_FLUJO_REPROCESO := C_SQL_EXECUTE_FLUJO_REPROCESO || ' UNION ALL ' ||  C_SQL_TIEMP_OBJ || ' UNION ALL ' || C_SQL_TIEMP_PRE || ' UNION ALL ' || C_SQL_TIEMP_REAL  ;
              ELSE
                C_SQL_EXECUTE_FLUJO_REGULAR := C_SQL_EXECUTE_FLUJO_REGULAR || ' UNION ALL ' ||  C_SQL_TIEMP_OBJ || ' UNION ALL ' || C_SQL_TIEMP_PRE || ' UNION ALL ' || C_SQL_TIEMP_REAL  ;
              END IF;
            END IF;
            
             CONT :=CONT+1;
            
          End Loop;
          
          CONT_EXECUTE := 2;
          
         

    End Loop;
          
   ELSE  /* BUSQUEDA ESPECIFICA */

                C_SQL_TIEMP_OBJ := ' SELECT    h.ID_PERFIL_FK columna,
            '||chr(39)|| 'T. OBJETIVO'|| chr(39)||' concepto,
            ROUND((sum(h.tiempo_obj_tc)/count(h.tiempo_obj_tc)),2) tc, ROUND((sum(h.tiempo_obj_te)/count(h.tiempo_obj_te)),2) te
            FROM CONELE.tbl_ce_ibm_toe_perfil_estado h 
            where '|| C_SQL_WHERE_VAL_ESTADO || C_SQL_WHERE_VAL_PRODUCTO || C_SQL_WHERE_VAL_TIPO_OFERTA || ' 
            h.nom_columna in ( select rptt.valor from TBL_CE_IBM_ROL_PERFIL_TAREA rptt where rptt.id_perfil_fk='|| PERFIL ||' and 
            rptt.id_tarea_fk in (select tp.id_tarea_fk from TBL_CE_IBM_TAREA_PERFIL tp INNER JOIN TBL_CE_IBM_ESTADO_TAREA_CE et ON tp.id_tarea_fk= et.id_tarea_fk 
              where '|| C_SQL_WHERE_VAL_EST_CONSUL ||' 
              et.id_tarea_fk in (select ptr.id_tarea_fk from TBL_CE_IBM_ROL_PERFIL_TAREA ptr where ptr.id_perfil_fk='|| PERFIL ||'))) AND
            h.id_tarea_fk in (select tp.id_tarea_fk from TBL_CE_IBM_TAREA_PERFIL tp INNER JOIN TBL_CE_IBM_ESTADO_TAREA_CE et ON tp.id_tarea_fk= et.id_tarea_fk 
              where '|| C_SQL_WHERE_VAL_EST_CONSUL ||'  
              et.id_tarea_fk in (select ptr.id_tarea_fk from TBL_CE_IBM_ROL_PERFIL_TAREA ptr where ptr.id_perfil_fk='|| PERFIL ||')) 
            group by h.ID_PERFIL_FK ';  
       
                C_SQL_TIEMP_PRE := ' SELECT    h.ID_PERFIL_FK columna,
            '||chr(39)|| 'T. PRELIMINAR'|| chr(39)||' concepto,
            ROUND((sum(h.tiempo_pre_tc)/count(h.tiempo_pre_tc)),2) tc, ROUND((sum(h.tiempo_pre_te)/count(h.tiempo_pre_te)),2) te
            FROM CONELE.tbl_ce_ibm_toe_perfil_estado h 
            where '|| C_SQL_WHERE_VAL_ESTADO || C_SQL_WHERE_VAL_PRODUCTO || C_SQL_WHERE_VAL_TIPO_OFERTA || ' 
            h.nom_columna in ( select rptt.valor from TBL_CE_IBM_ROL_PERFIL_TAREA rptt where rptt.id_perfil_fk='|| PERFIL ||' and 
            rptt.id_tarea_fk in (select tp.id_tarea_fk from TBL_CE_IBM_TAREA_PERFIL tp INNER JOIN TBL_CE_IBM_ESTADO_TAREA_CE et ON tp.id_tarea_fk= et.id_tarea_fk 
              where '|| C_SQL_WHERE_VAL_EST_CONSUL ||' 
              et.id_tarea_fk in (select ptr.id_tarea_fk from TBL_CE_IBM_ROL_PERFIL_TAREA ptr where ptr.id_perfil_fk='|| PERFIL ||'))) AND 
            h.id_tarea_fk in (select tp.id_tarea_fk from TBL_CE_IBM_TAREA_PERFIL tp INNER JOIN TBL_CE_IBM_ESTADO_TAREA_CE et ON tp.id_tarea_fk= et.id_tarea_fk 
              where '|| C_SQL_WHERE_VAL_EST_CONSUL ||' 
              et.id_tarea_fk in (select ptr.id_tarea_fk from TBL_CE_IBM_ROL_PERFIL_TAREA ptr where ptr.id_perfil_fk='|| PERFIL ||'))               
            group by h.ID_PERFIL_FK ';               
                      /*
      tp.id_perfil_fk='|| PERFIL ||' and  et.id_estado_ce_fk='|| ESTADO ||' and     
    */
             C_SQL_TIEMP_REAL := ' SELECT h.ID_PERFIL_FK columna, 
            '||chr(39)|| 'T. REAL' ||chr(39)||' concepto,
            ROUND(MEDIAN(h.tiempo_cola+h.tiempo_colar+h.tiempo_cola_retraer),2) tc, 
            ROUND(MEDIAN(h.tiempo_ejecucion+h.tiempo_ejecucionr),2) te 
            FROM CONELE.TBL_CE_IBM_HISTORIAL h
            WHERE '|| C_SQL_WHERE_VAL_TIPO_OFERTA || C_SQL_WHERE_FECHA || C_SQL_WHERE_VAL_PRODUCTO || ' 
            h.nom_columna in (select rptt.valor from TBL_CE_IBM_ROL_PERFIL_TAREA rptt where rptt.id_perfil_fk='|| PERFIL ||' and 
              rptt.id_tarea_fk in (select tp.id_tarea_fk from TBL_CE_IBM_TAREA_PERFIL tp INNER JOIN TBL_CE_IBM_ESTADO_TAREA_CE et ON tp.id_tarea_fk= et.id_tarea_fk 
              where '|| C_SQL_WHERE_VAL_EST_CONSUL ||' 
              et.id_tarea_fk in (select ptr.id_tarea_fk from TBL_CE_IBM_ROL_PERFIL_TAREA ptr where ptr.id_perfil_fk='|| PERFIL ||'))) AND 
            nvl(h.id_tarea_fk,1) in (select tp.id_tarea_fk from TBL_CE_IBM_TAREA_PERFIL tp INNER JOIN TBL_CE_IBM_ESTADO_TAREA_CE et ON tp.id_tarea_fk= et.id_tarea_fk 
              where '|| C_SQL_WHERE_VAL_EST_CONSUL ||' 
              et.id_tarea_fk in (select ptr.id_tarea_fk from TBL_CE_IBM_ROL_PERFIL_TAREA ptr where ptr.id_perfil_fk='|| PERFIL ||')) AND 
              '|| C_SQL_WHERE_VAL_EST_CONSULTA || C_SQL_WHERE_VAL_EXPCERRADOS ||'
            group by h.ID_PERFIL_FK';
                       

                    
            C_SQL_EXECUTE_FLUJO_REGULAR := C_SQL_TIEMP_OBJ || ' UNION ALL ' || C_SQL_TIEMP_PRE || ' UNION ALL ' || C_SQL_TIEMP_REAL; 
          
   END IF;

  IF(TIPO_BUSQ=0) THEN
 -- DBMS_OUTPUT.PUT_LINE(C_SQL_EXECUTE_FLUJO_REPROCESO);
   -- OPEN LIST_REPORTE FOR C_SQL_EXECUTE_FLUJO_REPROCESO || ' UNION ALL ' || C_SQL_EXECUTE_FLUJO_REGULAR; 
    IF(TIPO_FLUJO=1) THEN
    --DBMS_OUTPUT.PUT_LINE('Flujo 1');
      OPEN LIST_REPORTE FOR C_SQL_EXECUTE_FLUJO_REPROCESO;
    ELSE
      IF(TIPO_FLUJO=2) THEN
      --DBMS_OUTPUT.PUT_LINE('Flujo 2');
        OPEN LIST_REPORTE FOR C_SQL_EXECUTE_FLUJO_REGULAR; 
      END IF;
    END IF; 
    
  ELSE
  --DBMS_OUTPUT.PUT_LINE(C_SQL_EXECUTE_FLUJO_REGULAR);
   OPEN LIST_REPORTE FOR C_SQL_EXECUTE_FLUJO_REGULAR;
  END IF;
		
   EXCEPTION
   WHEN OTHERS THEN
   raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
 
	END SP_SEL_REPORTETOE;

END;

spool off;
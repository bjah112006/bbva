spool 001_SCRIPT_BD_PAQUETE1_cambiar_valores.log

ALTER TABLE CONELE.TBL_CE_IBM_EMPLEADO ADD (
    CODCARGO VARCHAR2(4),
    NOMCARGO VARCHAR2(45),
    FECHA_VIGENCIA DATE
);

ALTER TABLE CONELE.TBL_CE_IBM_PERFIL ADD (
    FLAG_MOSTRAR_INICIALES VARCHAR2(1) DEFAULT 0 NOT NULL
);

ALTER TABLE CONELE.TBL_CE_IBM_PARAMETROS_CONF_CC MODIFY (
    NOMBRE_VARIABLE VARCHAR2(60)
);

ALTER TABLE CONELE.TBL_CE_IBM_EXPEDIENTE_CC MODIFY OBSERVACIONES VARCHAR2(3000);

ALTER TABLE CONELE.TBL_CE_IBM_HISTORIAL_CC MODIFY OBSERVACIONES VARCHAR2(3000);

DROP SEQUENCE "CONELE"."SEQ_CE_IBM_EMPLEADO";
CREATE SEQUENCE "CONELE"."SEQ_CE_IBM_EMPLEADO" MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 3000 NOCACHE NOORDER NOCYCLE;
GRANT SELECT ON CONELE.SEQ_CE_IBM_EMPLEADO TO APP_CONELE;

CREATE TABLE "CONELE"."TBL_CE_IBM_MENSAJE_CC"  (
    "ID" NUMBER NOT NULL ENABLE, 
    "DESCRIPCION" VARCHAR2(150 BYTE) NOT NULL ENABLE, 
    "CONTENIDO" BLOB, 
    CONSTRAINT "TBL_CE_IBM_MENSAJE_CC_PK" PRIMARY KEY ("ID") USING INDEX  TABLESPACE "CONELE_IDX"  
) TABLESPACE "CONELE_DAT";
GRANT SELECT, INSERT, UPDATE, DELETE ON CONELE.TBL_CE_IBM_MENSAJE_CC TO APP_CONELE;

ALTER TABLE CONELE.TBL_CE_IBM_EXPEDIENTE_CC ADD (
  ID_EXP_ULT_BASTANTEO NUMBER(10,0)
);

ALTER TABLE CONELE.TBL_CE_IBM_EXPEDIENTE_CC
ADD CONSTRAINT EXPEDIENTE_CC_ULT_BASTANTEO_FK
FOREIGN KEY (ID_EXP_ULT_BASTANTEO)
REFERENCES CONELE.TBL_CE_IBM_EXPEDIENTE_CC(ID);

ALTER TABLE CONELE.TBL_CE_IBM_CLIENTE_CC ADD (
  FLAG_EXP_MIGRADO VARCHAR2(1)
);

spool off
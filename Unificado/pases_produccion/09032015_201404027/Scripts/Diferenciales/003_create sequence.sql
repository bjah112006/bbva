spool 003_create_sequence.log replace;
--------------------------------------------------------
--  DDL for Sequence SEQ_CE_IBM_MENSAJE_CE
--------------------------------------------------------

   CREATE SEQUENCE  CONELE.SEQ_CE_IBM_MENSAJE_CE  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;
   
   
   
   --------------------------------------------------------
--  DDL for Sequence SEQ_CE_IBM_MENSAJE_CE
--------------------------------------------------------

  GRANT SELECT ON CONELE.SEQ_CE_IBM_MENSAJE_CE TO APP_CONELE; 
   
   GRANT SELECT, INSERT, UPDATE, DELETE ON CONELE.TBL_CE_IBM_MENSAJE_CE TO APP_CONELE;
   
   ---------------------------------------------22012015---------------------------------------
   --------------------------------------------------------
--  DDL for Sequence SEQ_CE_IBM_LOG_ERRORES
--------------------------------------------------------
   CREATE SEQUENCE CONELE.SEQ_CE_IBM_LOG_ERRORES MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;
   
--------------------------------------------------------
--  DDL for Sequence SEQ_CE_IBM_EMPLEADO_AD_CE
--------------------------------------------------------
   CREATE SEQUENCE CONELE.SEQ_CE_IBM_EMPLEADO_AD_CE MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE ;
   
--------------------------------------------------------
--  DDL for Sequence SEQ_CE_IBM_HISTORIAL_LOG MINVALUE
--------------------------------------------------------
   CREATE SEQUENCE CONELE.SEQ_CE_IBM_HISTORIAL_LOG MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE ;
----------------------------------22/01/2015---------------------------------------
DROP SEQUENCE "CONELE"."SEQ_CE_IBM_HORARIO_OFICINA_CE";
CREATE SEQUENCE  "CONELE"."SEQ_CE_IBM_HORARIO_OFICINA_CE"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 22 NOCACHE  NOORDER  NOCYCLE ;


DROP SEQUENCE "CONELE"."SEQ_CE_IBM_HORARIO_CE";
CREATE SEQUENCE  "CONELE"."SEQ_CE_IBM_HORARIO_CE"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 4718 NOCACHE  NOORDER  NOCYCLE ;

spool off;
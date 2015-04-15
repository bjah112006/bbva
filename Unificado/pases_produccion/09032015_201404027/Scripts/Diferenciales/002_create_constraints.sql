spool 002_create_constraints.log replace;
--------------------------------------------------------
-- ALTER TABLE PARA TBL_CE_IBM_MENSAJE_CE
--------------------------------------------------------
--  DDL for Index MENSAJE_CE_PK
--------------------------------------------------------
CREATE UNIQUE INDEX CONELE.MENSAJE_CE_PK ON CONELE.TBL_CE_IBM_MENSAJE_CE ("ID") 
  TABLESPACE "CONELE_IDX" ;
--------------------------------------------------------
--  Constraints for Table TBL_CE_IBM_MENSAJE_CE
--------------------------------------------------------

  ALTER TABLE CONELE.TBL_CE_IBM_MENSAJE_CE ADD CONSTRAINT "MENSAJE_CE_PK" PRIMARY KEY ("ID")
  USING INDEX TABLESPACE "CONELE_IDX"  ENABLE;
 
 spool off;
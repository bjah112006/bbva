spool 11_updateBlob_RULE.log

DECLARE
  TMP_BLOB BLOB := EMPTY_BLOB();
  SRC_CHUNK_01 RAW(32767);
BEGIN
  SRC_CHUNK_01 := UTL_RAW.CAST_TO_RAW('package packws.cargaterritorio

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.bbva.batch.util.DeciderParam;

global java.util.Map status

rule "101 No Generar IICE"
dialect "java" 
salience 101
no-loop true
when
    param: DeciderParam(name == ''flagIICE'', value == ''SI'')
then
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DeciderParam.class);
    logger.info("FAILED");
    status.put("status", "FAILED");
end');

  UPDATE CONELE.MNTR_PARAMETRO SET BINARIO = EMPTY_BLOB() WHERE ID = 15;

  SELECT BINARIO INTO TMP_BLOB
  FROM CONELE.MNTR_PARAMETRO
  WHERE ID = 15 FOR UPDATE;
  
  DBMS_LOB.WRITEAPPEND(TMP_BLOB, UTL_RAW.LENGTH(SRC_CHUNK_01), SRC_CHUNK_01);

  COMMIT;
END;
/

spool off
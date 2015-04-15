spool 17_updateBlob_RULE_COMMAND.log

DECLARE
  TMP_BLOB BLOB := EMPTY_BLOB();
  SRC_CHUNK_01 RAW(32767);
BEGIN
  SRC_CHUNK_01 := UTL_RAW.CAST_TO_RAW('package packws.cargaterritorio

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.io.File;

import org.springframework.batch.core.StepExecution;

import com.everis.util.FechaUtil;

import com.bbva.batch.util.DeciderParam;

global java.util.Map status

rule "101 Transferencia de archivos"
dialect "java" 
salience 101
no-loop true
when
    params: Map()
then
    String existStatus = "COMPLETED";
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DeciderParam.class);
    
    try {
	    String comando = params.get("PB_COMANDO_TRANSFERENCIA").toString();
		String rutaArchivo = params.get("PB_RUTA_ARCHIVO").toString();
		String nombreArchivo = params.get("PB_NOMBRE_ARCHIVO").toString();
		String formatoFechaSufijo = params.get("PB_FORMATO_FECHA_SUFIJO").toString();
		String nroMesAnteriores = params.get("PB_MESES_ANTERIORES").toString();

		if (!rutaArchivo.endsWith("/")) {
			rutaArchivo += "/";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(rutaArchivo);
		sb.append(nombreArchivo);
		sb.append(FechaUtil.formatFecha(new Date(), formatoFechaSufijo));
		sb.append(".txt");
		
		String filename = sb.toString();
		comando = comando.replace("@@@", filename);
		logger.info("comando="+comando);
		Runtime.getRuntime().exec(comando);
    } catch(Exception e) {
        ((StepExecution) status.get("execution")).addFailureException(e);
        existStatus = "FAILED";
    }

	logger.info(existStatus);
    status.put("status", existStatus);
end');

  UPDATE CONELE.MNTR_PARAMETRO SET BINARIO = EMPTY_BLOB() WHERE ID = 53;

  SELECT BINARIO INTO TMP_BLOB
  FROM CONELE.MNTR_PARAMETRO
  WHERE ID = 53 FOR UPDATE;
  
  DBMS_LOB.WRITEAPPEND(TMP_BLOB, UTL_RAW.LENGTH(SRC_CHUNK_01), SRC_CHUNK_01);

  COMMIT;
END;
/

spool off

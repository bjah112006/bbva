spool 15_updateBlob_DepurarArchivo.log

DECLARE
  TMP_BLOB BLOB := EMPTY_BLOB();
  SRC_CHUNK_01 RAW(32767);
BEGIN
  SRC_CHUNK_01 := UTL_RAW.CAST_TO_RAW('[{"class":"com.bbva.batch.util.DeciderParam","name":"PB_RUTA_ARCHIVO","query":"SELECT VALOR_VARIABLE FROM CONELE.TBL_CE_IBM_PARAMETROS_CONF WHERE CODIGO_APLICATIVO=1000 AND NOMBRE_VARIABLE=\u0027PB_RUTA_ARCHIVO\u0027","value":null},{"class":"com.bbva.batch.util.DeciderParam","name":"PB_NOMBRE_ARCHIVO","query":"SELECT VALOR_VARIABLE FROM CONELE.TBL_CE_IBM_PARAMETROS_CONF WHERE CODIGO_APLICATIVO=1000 AND NOMBRE_VARIABLE=\u0027PB_NOMBRE_ARCHIVO\u0027","value":null},{"class":"com.bbva.batch.util.DeciderParam","name":"PB_FORMATO_FECHA_SUFIJO","query":"SELECT VALOR_VARIABLE FROM CONELE.TBL_CE_IBM_PARAMETROS_CONF WHERE CODIGO_APLICATIVO=1000 AND NOMBRE_VARIABLE=\u0027PB_FORMATO_FECHA_SUFIJO\u0027","value":null},{"class":"com.bbva.batch.util.DeciderParam","name":"PB_DIAS_ESPERA","query":"SELECT VALOR_VARIABLE FROM CONELE.TBL_CE_IBM_PARAMETROS_CONF WHERE CODIGO_APLICATIVO=1000 AND NOMBRE_VARIABLE=\u0027PB_DIAS_ESPERA\u0027","value":null}]');
  UPDATE CONELE.MNTR_PARAMETRO SET BINARIO = EMPTY_BLOB() WHERE ID = 49;
  SELECT BINARIO INTO TMP_BLOB
  FROM CONELE.MNTR_PARAMETRO
  WHERE ID = 49 FOR UPDATE;
  DBMS_LOB.WRITEAPPEND(TMP_BLOB, UTL_RAW.LENGTH(SRC_CHUNK_01), SRC_CHUNK_01);
  COMMIT;

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

rule "101 Depuracion de archivos"
dialect "java"
salience 101
no-loop true
when
    params: Map()
then
    String existStatus = "COMPLETED";
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DeciderParam.class);
   
    try {
        String rutaArchivo = params.get("PB_RUTA_ARCHIVO").toString();
        String nombreArchivo = params.get("PB_NOMBRE_ARCHIVO").toString();
        String formatoFechaSufijo = params.get("PB_FORMATO_FECHA_SUFIJO").toString();
        String nroDiasEspera = params.get("PB_DIAS_ESPERA").toString();
       
        File directorio = new File(rutaArchivo);
        String filenameSufix;
        Integer diasEspera = Integer.parseInt(nroDiasEspera);
        Date date;
        Date hoy = new Date();
           
        if(directorio.isDirectory()) {
            for(File f : directorio.listFiles()) {
                filenameSufix = f.getName().replaceAll(nombreArchivo, "");
                date = FechaUtil.parseFecha(filenameSufix, formatoFechaSufijo);
                if(date != null) {
                    logger.info("==>" + f.getName() + " -> Diff: [" + FechaUtil.diff(date, hoy) + "]::[" + diasEspera + "]");
                }
                if(date != null ' || chr(38) || chr(38) || ' FechaUtil.diff(date, hoy) >= diasEspera) {
                    logger.info("Archivo eliminado: " + f.getName() + " -> Diff: [" + FechaUtil.diff(date, hoy) + "]");
                    if(!f.delete()) {
                        logger.error("El archivo " + f.getName() + " no se pudo eliminar");
                    }
                }
            }
        }
    } catch(Exception e) {
        ((StepExecution) status.get("execution")).addFailureException(e);
        existStatus = "FAILED";
    }

    logger.info(existStatus);
    status.put("status", existStatus);
end');
  UPDATE CONELE.MNTR_PARAMETRO SET BINARIO = EMPTY_BLOB() WHERE ID = 50;
  SELECT BINARIO INTO TMP_BLOB
  FROM CONELE.MNTR_PARAMETRO
  WHERE ID = 50 FOR UPDATE;
  DBMS_LOB.WRITEAPPEND(TMP_BLOB, UTL_RAW.LENGTH(SRC_CHUNK_01), SRC_CHUNK_01);
  COMMIT;
END;
/

spool off

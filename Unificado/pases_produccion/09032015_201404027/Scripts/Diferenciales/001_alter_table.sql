spool 001_alter_table.log replace;

ALTER TABLE CONELE.TBL_CE_IBM_PRODUCTO_TAREA 
ADD (
FLAG_VALIDACION Varchar2(1) 
);

-------------------------13012015-------------------------------

  CREATE TABLE CONELE.TBL_CE_IBM_MENSAJE_CE 
   ("ID" NUMBER(10,0), 	
	"DESCRIPCION" VARCHAR2(4000 BYTE), 
	"CONTENIDO" BLOB
   )TABLESPACE "CONELE_DAT" ;
-------------------------------

--------------------------22012015------------------
--------------------------------------------------------
--  CREATE TABLE TBL_CE_IBM_LOG_ERRORES
--------------------------------------------------------
	CREATE TABLE CONELE.TBL_CE_IBM_LOG_ERRORES
  (
    id                    		NUMBER(10,0),
    id_expediente_fk        	NUMBER(10,0),
    id_estado_anterior_fk 		NUMBER(5,0),
    id_perfil_fk          		NUMBER(5,0),
    id_tarea_fk                	NUMBER(5,0),
    id_empleado_fk            	NUMBER(10,0),
    fecha_incidente 	  		TIMESTAMP (6),
    nro_reintentos 	  			NUMBER(5,0),
    tipo_error 		  			VARCHAR2(20 BYTE),
    descripcion_error      		VARCHAR2(200 BYTE),
    cant_documentos 	  		NUMBER(5,0),
    fecha_restauracion  		TIMESTAMP (6),
    fecha_cancelacion			TIMESTAMP (6),
    CONSTRAINT LOG_ERRORES_PK PRIMARY KEY (id) USING INDEX TABLESPACE CONELE_IDX,
    CONSTRAINT LOG_ERRORES_EXPEDIENTE_FK FOREIGN KEY (id_expediente_fk) REFERENCES CONELE.TBL_CE_IBM_EXPEDIENTE (id)  ,
    CONSTRAINT LOG_ERRORES_ESTADO_CE_FK FOREIGN KEY (id_estado_anterior_fk) REFERENCES CONELE.TBL_CE_IBM_ESTADO_CE (id) ,
    CONSTRAINT LOG_ERRORES_PERFIL_CE_FK FOREIGN KEY (id_perfil_fk) REFERENCES CONELE.TBL_CE_IBM_PERFIL_CE (id)  ,
    CONSTRAINT LOG_ERRORES_TAREA_FK FOREIGN KEY (id_tarea_fk) REFERENCES CONELE.TBL_CE_IBM_TAREA (id) ,
	CONSTRAINT LOG_ERRORES_EMPLEADO_CE_FK FOREIGN KEY (id_empleado_fk) REFERENCES CONELE.TBL_CE_IBM_EMPLEADO_CE (id) 
  )
	TABLESPACE CONELE_DAT;

--------------------------------------------------------
--  CREATE TABLE TBL_CE_IBM_EMPLEADO_AD_CE
--------------------------------------------------------	
-- Se Elimina por duplicidad
/*	CREATE TABLE CONELE.TBL_CE_IBM_EMPLEADO_AD_CE
  (
    ID           			NUMBER(10,0),
    CODIGO       			VARCHAR2(8 BYTE),
    NOMBRES      			VARCHAR2(60 BYTE),
    APEPAT       			VARCHAR2(50 BYTE),
    APEMAT       			VARCHAR2(50 BYTE),
    ID_PERFIL_FK 			NUMBER(5,0),
    FECING 					DATE,
    FECEGR 					DATE,
    ID_OFICINA_FK        	NUMBER(5,0),
    NOMBRES_COMPLETOS    	VARCHAR2(110 BYTE),
    ID_NIVEL_FK          	NUMBER(5,0),
    ID_TIPO_CATEGORIA_FK 	NUMBER(5,0),
    CORREO               	VARCHAR2(50 BYTE),
    FLAG_ACTIVO          	CHAR(1 BYTE),
    CONSTRAINT EMPLEADO_AD_CE_PK PRIMARY KEY (ID) USING INDEX TABLESPACE CONELE_IDX
  )
	TABLESPACE CONELE_DAT;
*/

--------------------------------------------------------
--  CREATE TABLE TBL_CE_IBM_HISTORIAL_LOG
--------------------------------------------------------
	CREATE TABLE CONELE.TBL_CE_IBM_HISTORIAL_LOG
  (
    ID                        		NUMBER(10,0),
    ID_EXPEDIENTE_FK          		NUMBER(10,0),
    ID_CLIENTE_NATURAL_FK     		NUMBER(10,0),
    ID_EMPLEADO_FK            		NUMBER(10,0),
    ID_ESTADO_FK             		NUMBER(5,0),
    ID_ESTADOENVWORKFLOWTC_FK 		NUMBER(5,0),
    ID_ESTADOTIPRESOL_FK      		NUMBER(5,0),
    ID_MOTIVO_DEVOLUCION_FK   		NUMBER(5,0),
    ID_PRODUCTO_FK            		NUMBER(5,0),
    ID_SUBPRODUCTO_FK         		NUMBER(5,0),
    ID_TIPO_BURO_FK           		NUMBER(5,0),
    ID_TIPO_ENVIO_FK          		NUMBER(5,0),
    ID_TIPMONSOL_FK           		NUMBER(5,0),
    ID_TIPMONAPROB_FK         		NUMBER(5,0),
    ID_TIPO_OFERTA_FK         		NUMBER(5,0),
    ID_TIPO_SCORING_FK        		NUMBER(5,0),
    ID_TAREA_FK               		NUMBER(5,0),
    ID_EMP_RESP_FK            		NUMBER(10,0),
    COD_PRE_EVAL              		VARCHAR2(16 BYTE),
    LINEA_CRED_SOL 					FLOAT(126),
    SOL_TASA_ESP    				CHAR(1 BYTE),
    VERIF_LAB       				CHAR(1 BYTE),
    VERIF_DOM       				CHAR(1 BYTE),
    ZONA_PEL        				CHAR(1 BYTE),
    FLAG_COMENTARIO 				CHAR(1 BYTE),
    COMENTARIO      				VARCHAR2(300 BYTE),
    LINEA_CONSUMO 					FLOAT(126),
    PORCENTAJE_ENDEUDAMIENTO 		FLOAT(126),
    RIESGO_CLIENTE 					FLOAT(126),
    CLASIFICACION_SBS 				FLOAT(126),
    SBS_CONYUGE 					FLOAT(126),
    RVGL                  			VARCHAR2(50 BYTE),
    NRO_CTA               			VARCHAR2(23 BYTE),
    FLAG_DELEGACION       			CHAR(1 BYTE),
    COMENTARIO_RECHAZO    			VARCHAR2(250 BYTE),
    COMENTARIO_DELEGACION 			VARCHAR2(100 BYTE),
    LINEA_CRED_APROB 				FLOAT(126),
    FLAG_SOL_TASA_ESP 				CHAR(1 BYTE),
    TASA_ESP 						FLOAT(126),
    FLAG_MODIF_SCORE    			CHAR(1 BYTE),
    FLAG_EXC_DELEGACION 			CHAR(1 BYTE),
    COMENTARIO_AYU_MEM  			VARCHAR2(100 BYTE),
    NUM_TERMINAL        			VARCHAR2(20 BYTE),
    FECHA_ENVIO 					TIMESTAMP (6),
    FECHA_PROGRAMADA 				TIMESTAMP (6),
    FECHA_FIN 						TIMESTAMP (6),
    ACCION       					VARCHAR2(100 BYTE),
    NRO_CONTRATO 					VARCHAR2(21 BYTE),
    FEC_REGISTRO 					TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP,
    FECHA_T1 						TIMESTAMP (6),
    FECHA_T2 						TIMESTAMP (6),
    FECHA_T3 						TIMESTAMP (6),
    ID_CLIENTE_NATURAL_CONYUGE_FK 	NUMBER(10,0),
    FLAG_DEVOLUCION               	CHAR(1 BYTE),
    FLAG_RETRAER                  	CHAR(1 BYTE),
    TIPO_RESOLUCION               	CHAR(1 BYTE),
    TIEMPO_COLA 					FLOAT(126),
    TIEMPO_EJECUCION 				FLOAT(126),
    FECHA_T1R 						TIMESTAMP (6),
    FECHA_T2R 						TIMESTAMP (6),
    FECHA_T3R 						TIMESTAMP (6),
    TIEMPO_COLAR 					FLOAT(126),
    TIEMPO_EJECUCIONR 				FLOAT(126),
    TIEMPO_COLA_RETRAER 			FLOAT(126),
    VERIF_DPS           			CHAR(1 BYTE),
    PLAZO_APROB         			VARCHAR2(10 BYTE),
    ID_OFICINA_FK       			NUMBER(5,0),
    PLAZO_SOL           			VARCHAR2(10 BYTE),
    PLAZO_SOL_APROB     			VARCHAR2(10 BYTE),
    CLASIFICACION_BANCO 			NUMBER(5,0),
    BANCO_CONYUGE       			NUMBER(5,0),
    TIPO_CAMBIO_EXP     			NUMBER(5,2),
    FECHA_TIPOCAMBIO_EXP 			DATE,
    ID_PERFIL_FK         			NUMBER(5,0),
    NOM_COLUMNA          			VARCHAR2(50 BYTE),
    TIEMPO_OBJ_TC        			NUMBER(5,2),
    TIEMPO_OBJ_TE        			NUMBER(5,2),
    TIEMPO_PRE_TC       	 		NUMBER(5,2),
    TIEMPO_PRE_TE        			NUMBER(5,2),
    ID_ESTADO_LLEGADA_FK 			NUMBER(5,0),
    ANS                 			NUMBER(5,2),
	MOTIVO_DEVOLUCION_CANCELACION   VARCHAR2(200 BYTE),
    CONSTRAINT HISTORIAL_LOG__PK PRIMARY KEY (ID) USING INDEX TABLESPACE CONELE_IDX
  )
	TABLESPACE CONELE_DAT;
	
--------------------------------------------------------
--  CREATE TABLE TBL_CE_IBM_TAREA_AD
--------------------------------------------------------
	CREATE TABLE CONELE.TBL_CE_IBM_TAREA_AD
  (
    ID          	NUMBER(5,0) DEFAULT NULL,
    CODIGO      	VARCHAR2(50 BYTE),
    DESCRIPCION 	VARCHAR2(100 BYTE),
    PAGINA      	VARCHAR2(50 BYTE),
    CONSTRAINT TAREA_AD_PK PRIMARY KEY (ID) USING INDEX TABLESPACE CONELE_IDX
  )
	TABLESPACE CONELE_DAT;
	
spool off;
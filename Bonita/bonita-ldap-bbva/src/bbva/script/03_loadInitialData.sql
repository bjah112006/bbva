update tbl_pyme_parameter set id_reference='LIM' where id_table=4 and id_column in('001','002','003','004');
update tbl_pyme_parameter set id_reference='PRV' where id_table=4 and id_column in('006','007','008','005','009');
select * from tbl_pyme_parameter where id_table=4;


SELECT * FROM TBL_PYME_PARAMETER WHERE ID_TABLE=4 AND ID_REFERENCE='LIM' ORDER BY VAL_COLUMN1;


SELECT 

CASE TASKNAME
WHEN 'Asignar Evaluación' THEN 
WHEN 'Asignar Evaluacion' THEN 
WHEN 'Evaluar Riesgo Mesa' THEN 
WHEN 'Autorizar Aprobación' THEN
WHEN 'Autorizar Aprobacion' THEN
WHEN 'Evaluar Riesgo Campo' THEN


FROM FASTPYME.INSTANCE WHERE ESTACION='RIESGO';

SELECT TRIM(OFI_REGISTRO) FROM FASTPYME.INSTANCE WHERE ESTACION='RIESGO';


SELECT DISTINCT TASKNAME FROM FASTPYME.INSTANCE WHERE ESTACION='RIESGO';

SELECT NAME FROM DATA_INSTANCE GROUP BY NAME ORDER BY 1;

SELECT * FROM DATA_INSTANCE WHERE NAME='acn_riesgo';
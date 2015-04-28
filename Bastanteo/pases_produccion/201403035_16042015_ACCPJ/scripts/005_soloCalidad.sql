spool 005_soloCalidad.log

update conele.tbl_ce_ibm_parametros_conf_cc set valor_variable='https://de-espper.peru.igrupobbva/calidad18/BBVA_CTACTE_GUI')
where id=61;
commit;

spool off
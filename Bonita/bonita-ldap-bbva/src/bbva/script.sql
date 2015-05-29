CREATE TABLE BBVA_PERFIL (
	ID_ROLE BIGINT,
	ID_GROUP BIGINT,
	PUESTO VARCHAR_IGNORECASE
);

INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='FUVEX'  ), (SELECT ID FROM ROLE WHERE NAME='ABN'), 'KGO');
INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='FUVEX'  ), (SELECT ID FROM ROLE WHERE NAME='ABN'), 'Y16');
INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='FUVEX'  ), (SELECT ID FROM ROLE WHERE NAME='JCN'), 'KDZ');
INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='OFICINA'), (SELECT ID FROM ROLE WHERE NAME='EBN'), 'B25');
INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='OFICINA'), (SELECT ID FROM ROLE WHERE NAME='EBN'), 'B41');
INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='RIESGOS'), (SELECT ID FROM ROLE WHERE NAME='JER'), 'ANE');
INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='RIESGOS'), (SELECT ID FROM ROLE WHERE NAME='JGR'), 'ANF');
INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='RIESGOS'), (SELECT ID FROM ROLE WHERE NAME='GMC'), 'ATB');
INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='RIESGOS'), (SELECT ID FROM ROLE WHERE NAME='AR' ), 'ANG');
INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='RIESGOS'), (SELECT ID FROM ROLE WHERE NAME='AR' ), 'AY6');
INSERT INTO BBVA_PERFIL(ID_GROUP, ID_ROLE, PUESTO) VALUES((SELECT ID FROM GROUP_ WHERE NAME='CPM'    ), (SELECT ID FROM ROLE WHERE NAME='CPM'), '');
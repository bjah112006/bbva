update tbl_pyme_parameter set id_reference='LIM' where id_table=4 and id_column in('001','002','003','004');
update tbl_pyme_parameter set id_reference='PRV' where id_table=4 and id_column in('006','007','008','005','009');
select * from tbl_pyme_parameter where id_table=4;

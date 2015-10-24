INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '001', '500', 'Número Solicitud'   , 'nroSolicitud'              , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '002', '250', 'Tipo DOI'           , 'tipo_doi_cliente'          , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '003', '500', 'Número DOI'         , 'num_doi_cliente'           , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '004', '800', 'Nombre Cliente'     , 'nombre_cliente'            , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '005', '600', 'Nombre tarea'       , 'nombreTarea'               , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '006', '400', 'Estado'             , 'estado'                    , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '007', '480', 'Tipo Oferta'        , 'oferta_aprobada'           , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '008', '600', 'Oficina'            , 'ofi_registro'              , 1); 
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '009', '500', 'Fecha Llegada'      , 'producto'                  , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '010', '500', 'Fecha Envío'        , 'campania'                  , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '011', '250', 'Rol'                , 'rolEjecutorTarea'          , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '012', '250', 'Usuario'            , 'usuarioEjecutorTarea'      , 1); 
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '013', '250', 'RVGL'               , 'num_rvgl'                  , 1); 
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '014', '800', 'Número Contrato'    , 'aux_num_contrato'          , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '015', '500', 'Número Garantía'    , 'aux_num_garantia'          , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '016', '800', 'Dictamen'           , 'aux_dictamen'              , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '017', '400', 'Producto'           , 'aux_producto'              , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '018', '400', 'Campañia'           , 'aux_campania'              , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '019', '400', 'Clasf. Cliente'     , 'aux_clte_clasificacion'    , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '020', '400', 'ABN Registrante'    , 'usu_registrante'           , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '021', '400', 'Num. Preimpreso'    , 'num_tramite'               , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '022', '400', 'Causal Obs. GMC'    , 'aux_causal_devolucion_gmc' , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '023', '400', 'Causal Cancelacion' , 'aux_causal_clte_rechaza'   , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '024', '400', 'Moneda'             , 'aux_moneda'                , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '025', '400', 'Monto'              , 'aux_monto'                 , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '026', '400', 'Plazo'              , 'aux_plazo'                 , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '027', '400', 'Tasa'               , 'aux_tasa'                  , 1);
INSERT INTO tbl_pyme_parameter(id_table, id_column, id_reference, val_column1, val_column2, flg_state) VALUES (17, '028', '400', 'Centro Negocio'     , 'centro_negocio_origen'     , 1);


'aux_estado_solicitud'      
'aux_tipo_oferta'           
'aux_num_rvgl'              
''          
''          
              
   
 
   

cell0.setCellValue(solicitud.getNroSolicitud());
cell1.setCellValue(solicitud.getTipoDOICliente());
cell2.setCellValue(solicitud.getNroDOICliente());
cell3.setCellValue(solicitud.getNombreCliente());
cell4.setCellValue(solicitud.getNombreTarea());
cell5.setCellValue(solicitud.getEstado());
cell6.setCellValue(solicitud.getTipoOferta());
cell7.setCellValue(solicitud.getOficinaSolicitud());
cell8.setCellValue(solicitud.getFechaLlegada());
cell9.setCellValue(solicitud.getFechaEnvio());
cell10.setCellValue(solicitud.getRolEjecutorTarea());
cell11.setCellValue(solicitud.getUsuarioEjecutorTarea());
cell12.setCellValue(solicitud.getNroRVGL());
cell13.setCellValue(solicitud.getNroContrato());
cell14.setCellValue(solicitud.getNroGarantia());
cell15.setCellValue(solicitud.getDictamen());
cell16.setCellValue(solicitud.getProducto());
cell17.setCellValue(solicitud.getCampania());
cell18.setCellValue(solicitud.getClasificacion_clte());
cell19.setCellValue(solicitud.getAbn_registante());
cell20.setCellValue(solicitud.getNum_preimpreso());
cell21.setCellValue(solicitud.getCausal_devol_gmc());
cell22.setCellValue(solicitud.getCausal_clte_cancela());
cell23.setCellValue(solicitud.getMoneda());
cell24.setCellValue(solicitud.getMonto());
cell25.setCellValue(solicitud.getPlazo());
cell26.setCellValue(solicitud.getTasa());
cell27.setCellValue(solicitud.getCentro_negocio_riesgos());

package com.pe.bbva.pyme.dao;

import java.util.Date;
import java.util.List;

import com.pe.bbva.pyme.model.Documento;

public interface DocumentoDAO {

    List<Documento> listarDocumentos(boolean hashContent, Date creationDate);
    void actualizarDocumento(List<Documento> documentos);
}

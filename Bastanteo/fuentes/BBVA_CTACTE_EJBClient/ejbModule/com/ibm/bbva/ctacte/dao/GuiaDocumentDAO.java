package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.bean.GuiaDocument;

public interface GuiaDocumentDAO {
	
	public List<GuiaDocument> findByDocumento(GuiaDocument guiaDocumento);
	public List<GuiaDocument> findByTipoPJOperacionCasoNegocio(String codTipoPj, Integer operacion, Integer casoNegocio);

}

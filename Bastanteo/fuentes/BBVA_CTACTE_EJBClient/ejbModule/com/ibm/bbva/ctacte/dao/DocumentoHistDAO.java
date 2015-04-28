package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.bean.DocumentoHist;

public interface DocumentoHistDAO {

	public List<DocumentoHist> getListDocHistoricobyClienteDocumento(int idCliente,int idDocumento);
	
}

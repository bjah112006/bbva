package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Documento;

public interface DocumentoDAO extends IGenericDAO<Documento, Integer> {
	
	public Documento findByCodigo(String codigo);
	public List<Documento> getListaDocumentos(int idExpediente);
	public List<Documento> getListaDocumentoCU15(int idExpediente);
	
}

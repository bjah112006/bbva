package com.ibm.bbva.cm.service;

import com.ibm.bbva.cm.bean.Documento;

public interface ContentService {
	
	String insert(Documento documento);
	String insert_PID(Documento documento);
	String insertAll(Documento[] documentos);
	String delete(Documento documento);
	String deleteAll(Documento[] documentos);
	Documento find(Documento documento);
	Documento[] findAll(Documento documento);
	String update(Documento documento);
	Documento findAsImage(Documento documento,String mimeType);
	String actualizarTipoDoc(Integer id , String tipoDoc);
	
}

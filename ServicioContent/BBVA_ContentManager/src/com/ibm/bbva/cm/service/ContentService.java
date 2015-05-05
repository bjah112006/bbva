package com.ibm.bbva.cm.service;

import java.util.List;

import com.ibm.bbva.cm.bean.Documento;
import com.ibm.bbva.cm.exception.ICMException;

public interface ContentService {
	
	void insertAll(List<Documento> documentos) throws ICMException;
	void deleteAll(List<Documento> documentos) throws ICMException;
	Documento[] findAll(Documento documento);
	void update(Documento documento) throws ICMException;	
}

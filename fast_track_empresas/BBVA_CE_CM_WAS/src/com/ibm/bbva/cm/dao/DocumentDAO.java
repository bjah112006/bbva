package com.ibm.bbva.cm.dao;

import java.util.List;

import com.ibm.bbva.cm.bean.DocumentoPid;

public interface DocumentDAO {

	public DocumentoPid retrieve(DocumentoPid filter);
	public void store(DocumentoPid document);
	public void store(List<DocumentoPid> documents);
	public String store_PID(DocumentoPid document);
	public void remove(DocumentoPid document);
	public void remove(List<DocumentoPid> documents);
	public List<DocumentoPid> getDocuments(DocumentoPid filter);
}

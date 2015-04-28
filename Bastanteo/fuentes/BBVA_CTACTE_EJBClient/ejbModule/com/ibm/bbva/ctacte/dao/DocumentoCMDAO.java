package com.ibm.bbva.ctacte.dao;

import com.ibm.bbva.ctacte.bean.DocumentoCM;

public interface DocumentoCMDAO {
	
	public DocumentoCM getDocumentoCM(int idExpediente, int idDocumento);
	public DocumentoCM getDocumentoCMxCodigo(int idExpediente, String codigoDocumento);

}

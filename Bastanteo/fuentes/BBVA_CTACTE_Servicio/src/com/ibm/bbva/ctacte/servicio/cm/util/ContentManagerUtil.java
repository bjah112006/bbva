package com.ibm.bbva.ctacte.servicio.cm.util;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.servicio.cm.bean.DocumentoExpediente;

public class ContentManagerUtil {

	public DocumentoExpediente copiarDocumentoExpediente(DocumentoExp documentoExp){
		DocumentoExpediente documentoExpediente = new DocumentoExpediente();
		
		if (documentoExp.getDocPesoPromKB()!=null)documentoExpediente.setDocPesoPromKB(documentoExp.getDocPesoPromKB());
		if (documentoExp.getFechaEscaneo()!=null)documentoExpediente.setFechaEscaneo(documentoExp.getFechaEscaneo());
		if (documentoExp.getFechaVigencia()!=null)documentoExpediente.setFechaVigencia(documentoExp.getFechaVigencia());
		if (documentoExp.getFlagAlterno()!=null)documentoExpediente.setFlagAlterno(documentoExp.getFlagAlterno().toString());
		if (documentoExp.getFlagCm()!=null)documentoExpediente.setFlagCm(documentoExp.getFlagCm().toString());
		if (documentoExp.getFlagEscaneado()!=null)documentoExpediente.setFlagEscaneado(documentoExp.getFlagEscaneado().toString());
		if (documentoExp.getFlagObligatorio()!=null)documentoExpediente.setFlagObligatorio(documentoExp.getFlagObligatorio().toString());
		if (documentoExp.getFlagRechazado()!=null)documentoExpediente.setFlagRechazado(documentoExp.getFlagRechazado().toString());
		if (documentoExp.getFlagReqEscaneo()!=null)documentoExpediente.setFlagReqEscaneo(documentoExp.getFlagReqEscaneo().toString());
		if (documentoExp.getId()!=null)documentoExpediente.setId(documentoExp.getId());
		if (documentoExp.getIdCm()!=null)documentoExpediente.setIdCm(documentoExp.getIdCm());
		if (documentoExp.getIdCmCopia()!=null)documentoExpediente.setIdCmCopia(documentoExp.getIdCmCopia());
		if (documentoExp.getDocPesoPromKB()!=null)documentoExpediente.setIdDocumento(documentoExp.getDocumento().getId());
		if (documentoExp.getDocPesoPromKB()!=null)documentoExpediente.setIdExpediente(documentoExp.getExpediente().getId());
		if (documentoExp.getMotivo()!=null) if (documentoExp.getMotivo().getId()!=null) documentoExpediente.setIdMotivo(documentoExp.getMotivo().getId());
		if (documentoExp.getNombreArchivo()!=null)documentoExpediente.setNombreArchivo(documentoExp.getNombreArchivo());
		if (documentoExp.getPidCM()!=null)documentoExpediente.setPidCM(documentoExp.getPidCM());		
		
		return documentoExpediente;
		
	}
}

package com.ibm.bbva.session;

import java.math.BigDecimal;
import java.util.List;

import com.ibm.bbva.entities.DocumentoExpTc;

public interface DocumentoExpTcBeanLocal {
	public  DocumentoExpTc buscarPorId(long id);
	public List<DocumentoExpTc> buscarPorExpediente(long id);
	public DocumentoExpTc create(DocumentoExpTc documentoExpTc);
	public void edit(DocumentoExpTc documentoExpTc);
	public void remove(DocumentoExpTc documentoExpTc);
	public DocumentoExpTc consultarDocumentoExpediente(long idExpediente, String codigoTipoDoc);
	public DocumentoExpTc consultarDocumentoExpedienteCM(long idExpediente, String codigoTipoDoc);
	public List<DocumentoExpTc> consultarDocumentosExpediente(long idExpediente, String codigoTipoDoc);
	public int actualizarDocumentoExpediente(DocumentoExpTc objDocumentoExpTc);
	public List<DocumentoExpTc> buscarPorExpedienteFlagCM(long id, String flagCm);
	DocumentoExpTc findByTipoDocisNullCM(long idExpediente, long idTipoDocumento);
	public DocumentoExpTc consultarUltimoDocumentoExpediente(long idExpediente, String codigoTipoDoc);
	public DocumentoExpTc consultarDocumentoExpediente(long idExpediente, BigDecimal idCm);
	public List<DocumentoExpTc> buscarPorExpedienteFlagEscaneado(long id, String flagEscaneado);
	public List<DocumentoExpTc> buscarDocumentosReutilizables(long idTipoDoi, String numeroDoi);
	public int actualizarDocumentosNoObservados(long idExpediente, String iDsCM);
	
	public int actualizarDocumentoObservadoExpediente(long idDocumentoExpTc);
	public int removeDocumentoxpediente(long idExpediente, long idTipoDocumento);
	public int observarDocumentoExpediente(long idCM);
	public int actualizarDocumentosObservadosExpediente(String idDocumentosExpTc);
}

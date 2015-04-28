package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.DocumentoExp;

public interface DocumentoExpDAO extends IGenericDAO<DocumentoExp, Integer> {

	public List<DocumentoExp> findByExpdiente(int idExpediente);
	public List<DocumentoExp> findByDocumentosLegales(int idExpediente,String documentosLegales);
	public List<DocumentoExp> findByDocumentosLegalesRechazados(int idExpediente,String documentosLegales);
	public List<DocumentoExp> findByExpdienteEscaneado(int idExpediente, String flagEscaneado, String codDOI, String codFRF);
	public List<DocumentoExp> findByExpdienteRechazado(int idExpediente,String codDOI, String codFRF);
	public List<DocumentoExp> findByExpdienteCartaRevocatoria(int idExpediente, String codCartaRevocatoria);
	public DocumentoExp findByDocExp(Integer idDoc, Integer idExp);
	public DocumentoExp findByCodDocExp(String codDoc, Integer idExp);
	public DocumentoExp findByCodDocCodCent(String codDoc, String codigoCentral);
	public DocumentoExp findByIdCm(Integer idCm);
	public int delDocumentoExpediente(int codExpediente);
	public List<DocumentoExp> getListaDocumentos(Integer idExpediente);
	public String validarFlagEnvioContent(Integer idExpediente);
	
}

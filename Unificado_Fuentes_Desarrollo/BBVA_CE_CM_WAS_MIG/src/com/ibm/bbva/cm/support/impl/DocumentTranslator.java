package com.ibm.bbva.cm.support.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.ibm.bbva.cm.bean.DocumentoPid;
import com.ibm.bbva.cm.support.DomainTranslator;

public class DocumentTranslator implements DomainTranslator {
	private static final String FALSE = "0";
	private static final String TRUE = "1";
	
	private static final String ATTRIBUTE_ID = "CE_MIG_Id";
	private static final String ATTRIBUTE_TYPE = "CE_MIG_Type";
	private static final String ATTRIBUTE_MANDATORY = "CE_MIG_Mandatary";
	private static final String ATTRIBUTE_EXPIRATION_DATE = "CE_MIG_ExpirationDate";
	
	//Propiedades agregadas a partir de los requerimientos funcionales
	
//	private static final String ATTRIBUTE_TIPO_DOI = "CE_TipoDoi";
//	private static final String ATTRIBUTE_NUMERO_DOI = "CE_NumeroDoi";
//	private static final String ATTRIBUTE_COD_CLIENTE = "CE_CodCliente";
//	private static final String ATTRIBUTE_FECHA_CREACION = "CE_FecCreacion";
//	private static final String ATTRIBUTE_NOMBRE_ARCHIVO = "CE_NomArchivo";
//	private static final String ATTRIBUTE_ORIGEN = "CE_Origen";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bbvabancocontinental.vv.framework.icm.core.translator.DomainTranslator#translateToDomain
	 * (com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public Object translateToDomain(ICMDocument icmDocument) {
		DocumentoPid document = new DocumentoPid();

		Integer id = (Integer) icmDocument.getAttributes().get(ATTRIBUTE_ID);
		String type = (String) icmDocument.getAttributes().get(ATTRIBUTE_TYPE);
		
		//Nuevos
//		String tipoDoi = (String) icmDocument.getAttributes().get(ATTRIBUTE_TIPO_DOI);
//		String numDoi = (String) icmDocument.getAttributes().get(ATTRIBUTE_NUMERO_DOI);
//		String codCli = (String) icmDocument.getAttributes().get(ATTRIBUTE_COD_CLIENTE);
//		
//		//Adiconales por Cta Cte
//		String nomArchivo = (String) icmDocument.getAttributes().get(ATTRIBUTE_NOMBRE_ARCHIVO);
//		String origen = (String) icmDocument.getAttributes().get(ATTRIBUTE_ORIGEN);
//		
//		Timestamp timestampCC = (java.sql.Timestamp) icmDocument.getAttributes().get(ATTRIBUTE_FECHA_CREACION);
//		Calendar fechaCreacion = null;
//		if (timestampCC != null) {
//			fechaCreacion = DateHelper.toCalendar(timestampCC);
//		}
		
		
		Timestamp timestamp = (java.sql.Timestamp) icmDocument.getAttributes().get(ATTRIBUTE_EXPIRATION_DATE);
		Calendar expirationDate = null;
		if (timestamp != null) {
			expirationDate = DateHelper.toCalendar(timestamp);
		}
		
		Boolean mandatory = TRUE.equals(icmDocument.getAttributes().get(ATTRIBUTE_MANDATORY)) ? Boolean.TRUE : Boolean.FALSE;

		document.setContenido(icmDocument.getContent());
		document.setFechaExpiracion(expirationDate);
		document.setId(id);
		document.setTipo(type);
		document.setMandatorio(mandatory);
		//Nuevas
//		document.setCodCliente(codCli);
//		document.setTipoDoi(tipoDoi);
//		document.setNumDoi(numDoi);
//		
//		document.setNombreArchivo(nomArchivo);
//		document.setOrigen(origen);
//		document.setFechaCreacion(fechaCreacion);
		
		document.setUrlContent(icmDocument.getUrlContent());
		document.setPid(icmDocument.getPid());
		return document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seews.synopsis.framework.icm.core.translator.DomainTranslator#
	 * translateToICMDocument(java.lang.Object)
	 */
	public ICMDocument translateToICMDocument(Object domain) {
		DocumentoPid document = (DocumentoPid)domain;
		Map<String, Object> attributes = new HashMap<String, Object>();

		if (document.getId() != null) {
			attributes.put(ATTRIBUTE_ID, document.getId());
		}
		if (document.getTipo() != null) {
			attributes.put(ATTRIBUTE_TYPE, document.getTipo());
		}
		if (document.getMandatorio() != null) {
			attributes.put(ATTRIBUTE_MANDATORY, document.getMandatorio() ? TRUE : FALSE);
		}
		if (document.getFechaExpiracion() != null) {
			attributes.put(ATTRIBUTE_EXPIRATION_DATE, DateHelper.toTimestamp(document.getFechaExpiracion()));
		}
		//Nuevas
//		if (document.getCodCliente() != null) {
//			attributes.put(ATTRIBUTE_COD_CLIENTE, document.getCodCliente());
//		}
//		if (document.getTipoDoi() != null) {
//			attributes.put(ATTRIBUTE_TIPO_DOI, document.getTipoDoi());
//		}
//		if (document.getNumDoi() != null) {
//			attributes.put(ATTRIBUTE_NUMERO_DOI, document.getNumDoi());
//		}
//		
//		if (document.getNombreArchivo() != null) {
//			attributes.put(ATTRIBUTE_NOMBRE_ARCHIVO, document.getNombreArchivo());
//		}
//		
//		if (document.getOrigen() != null) {
//			attributes.put(ATTRIBUTE_ORIGEN, document.getOrigen());
//		}
//		
//		if (document.getFechaCreacion() != null) {
//			attributes.put(ATTRIBUTE_FECHA_CREACION, DateHelper.toTimestamp(document.getFechaCreacion()));
//		}
		
		ICMDocument icmDocument = new ICMDocument();
		icmDocument.setAttributes(attributes);

		return icmDocument;
	}

}

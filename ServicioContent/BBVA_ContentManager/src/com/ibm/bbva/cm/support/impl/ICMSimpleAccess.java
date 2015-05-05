package com.ibm.bbva.cm.support.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.exception.ICMDocumentNotFoundException;
import com.ibm.bbva.cm.exception.ICMException;
import com.ibm.bbva.cm.support.ICMAccess;
import com.ibm.mm.sdk.common.DKConstant;
import com.ibm.mm.sdk.common.DKConstantICM;
import com.ibm.mm.sdk.common.DKDDO;
import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.common.DKLobICM;
import com.ibm.mm.sdk.common.DKNVPair;
import com.ibm.mm.sdk.common.DKParts;
import com.ibm.mm.sdk.common.DKResults;
import com.ibm.mm.sdk.common.DKRetrieveOptionsICM;
import com.ibm.mm.sdk.common.DKUsageError;
import com.ibm.mm.sdk.common.dkIterator;
import com.ibm.mm.sdk.server.DKDatastoreICM;

public class ICMSimpleAccess implements ICMAccess {
	private static final Logger logger = LoggerFactory.getLogger(ICMSimpleAccess.class);

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.support.ICMAccess#store(com.ibm.mm.sdk.server.DKDatastoreICM, com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public void store(DKDatastoreICM dsICM, ICMDocument icmDocument) throws DKUsageError, DKException, Exception {
		DKDDO ddoDocument = dsICM.createDDO(icmDocument.getItemType(), DKConstant.DK_CM_DOCUMENT);
		for (Map.Entry<String, Object> entry : icmDocument.getAttributes().entrySet()) {
			logger.debug("attribute: [" + entry.getKey() + "," + entry.getValue() + "]");
			ddoDocument.setData(ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, entry.getKey()), entry.getValue());
		}
		DKLobICM base = (DKLobICM) dsICM.createDDO("ICMBASE", DKConstantICM.DK_ICM_SEMANTIC_TYPE_BASE);
		base.setMimeType(icmDocument.getMimeType());
		base.setContent(icmDocument.getContent());
		short dataid = ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, DKConstant.DK_CM_DKPARTS);
		assertDocumentModelItem(dataid);
		DKParts dkParts = (DKParts) ddoDocument.getData(dataid);
		dkParts.addElement(base);
		ddoDocument.add();
		logger.info("document " + icmDocument.getItemType() + " with PID: " + ddoDocument.getPidObject().pidString() + " stored successfully");
	}
	
	
	public String store_PID(DKDatastoreICM dsICM, ICMDocument icmDocument) throws DKUsageError, DKException, Exception {
		DKDDO ddoDocument = dsICM.createDDO(icmDocument.getItemType(), DKConstant.DK_CM_DOCUMENT);
		for (Map.Entry<String, Object> entry : icmDocument.getAttributes().entrySet()) {
			logger.debug("attribute: [" + entry.getKey() + "," + entry.getValue() + "]");
			ddoDocument.setData(ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, entry.getKey()), entry.getValue());
		}
		DKLobICM base = (DKLobICM) dsICM.createDDO("ICMBASE", DKConstantICM.DK_ICM_SEMANTIC_TYPE_BASE);
		base.setMimeType(icmDocument.getMimeType());
		base.setContent(icmDocument.getContent());
		short dataid = ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, DKConstant.DK_CM_DKPARTS);
		assertDocumentModelItem(dataid);
		DKParts dkParts = (DKParts) ddoDocument.getData(dataid);
		dkParts.addElement(base);
		ddoDocument.add();
		logger.info("document " + icmDocument.getItemType() + " with PID: " + ddoDocument.getPidObject().pidString() + " stored successfully");
		return ddoDocument.getPidObject().pidString();
	}

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.support.ICMAccess#remove(com.ibm.mm.sdk.server.DKDatastoreICM, com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public void remove(DKDatastoreICM dsICM, ICMDocument icmDocument) throws DKUsageError, DKException, Exception {
		String query = buildQuery(icmDocument.getItemType(), icmDocument.getAttributes());
		logger.debug("query:  " + query);

		DKDDO ddoDocument = findDDODocument(dsICM, query);
		ddoDocument.del();

		logger.info("document " + icmDocument.getItemType() + " deleted successfully");
	}

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.support.ICMAccess#retrieve(com.ibm.mm.sdk.server.DKDatastoreICM, com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public ICMDocument retrieve(DKDatastoreICM dsICM, ICMDocument filter) throws DKUsageError, DKException, Exception {
		ICMDocument icmDocument = null;
		String query = buildQuery(filter.getItemType(), filter.getAttributes());
		logger.debug("query:  " + query);

		DKDDO ddoDocument = findDDODocument(dsICM, query);
		
		short dataid = ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, DKConstant.DK_CM_DKPARTS);
		logger.debug("Buscando PID : "+ddoDocument.getPidObject().pidString());
		logger.debug("dataid : "+dataid);
		assertDocumentModelItem(dataid);
				
		DKParts dkParts = (DKParts) ddoDocument.getData(dataid);
		dkIterator dkPartsIterator = dkParts.createIterator();
		assertPartsCardinalityOne(dkParts.cardinality());
		
		DKDDO part = (DKDDO) dkPartsIterator.next();
		assertDKLobICMPart(part);
		
		
		
		DKLobICM base = (DKLobICM) part;
		DKRetrieveOptionsICM dkBaseRetrieveOptions = DKRetrieveOptionsICM.createInstance(dsICM);
		dkBaseRetrieveOptions.resourceContent(true);
		String urls[] = base.getContentURLs(DKConstant.DK_CM_VERSION_LATEST, -1, -1, DKConstantICM.DK_ICM_GETINITIALRMURL);
		
		base.retrieve(dkBaseRetrieveOptions.dkNVPair());
		logger.debug("content retrieved: " + base.getContent());
		
		icmDocument = new ICMDocument();
		
		icmDocument.setAttributes(getAttributes(ddoDocument));
		icmDocument.setContent(base.getContent());
		icmDocument.setItemType(filter.getItemType());
		String strurl = urls[0];
		InetAddress addr = InetAddress.getLocalHost();
        if (strurl.indexOf(addr.getHostName())!=-1){		
	        logger.info("Host: " + addr.getHostName());
	        logger.info("IP: " + addr.getHostAddress());
	        //strurl = strurl.replaceFirst(addr.getHostName()+".pe.ibm.com",addr.getHostAddress());
        }
        logger.info("URL: " + strurl);
		icmDocument.setUrlContent(strurl);
		icmDocument.setPid(ddoDocument.getPidObject().pidString());
		logger.info("document " + icmDocument.getItemType() + " retrieved successfully");

		return icmDocument;
	}
	
	public List<ICMDocument> getDocuments(DKDatastoreICM dsICM, ICMDocument filter) throws DKUsageError, DKException, Exception {
		ICMDocument icmDocument = null;
		List<ICMDocument> lista = new ArrayList<ICMDocument>();
		String query = buildQuery(filter.getItemType(), filter.getAttributes());
		logger.debug("query:  " + query);
		
		List<DKDDO> documentos = findDocuments(dsICM, query);
		logger.debug("documentos cantidad:  " + documentos.size());
		for(DKDDO ddoDocument : documentos){
			short dataid = ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, DKConstant.DK_CM_DKPARTS);
			logger.debug("dataid : "+dataid);
			assertDocumentModelItem(dataid);

			DKParts dkParts = (DKParts) ddoDocument.getData(dataid);
			dkIterator dkPartsIterator = dkParts.createIterator();
			//assertPartsCardinalityOne(dkParts.cardinality());

			DKDDO part = (DKDDO) dkPartsIterator.next();
			assertDKLobICMPart(part);

			DKLobICM base = (DKLobICM) part;
			//DKRetrieveOptionsICM dkBaseRetrieveOptions = DKRetrieveOptionsICM.createInstance(dsICM);
			//dkBaseRetrieveOptions.resourceContent(true);
			String urls[] = base.getContentURLs(DKConstant.DK_CM_VERSION_LATEST, -1, -1, DKConstantICM.DK_ICM_GETINITIALRMURL);
			
			//base.retrieve(dkBaseRetrieveOptions.dkNVPair());
			//logger.debug("content retrieved: " + base.getContent());

			icmDocument = new ICMDocument();
			icmDocument.setAttributes(getAttributes(ddoDocument));
			//icmDocument.setContent(base.getContent());
			icmDocument.setContent(null);
			icmDocument.setItemType(filter.getItemType());
			String strurl = urls[0];
			InetAddress addr = InetAddress.getLocalHost();
			if (strurl.indexOf(addr.getHostName())!=-1){		
		        //strurl = strurl.replaceFirst(addr.getHostName(),addr.getHostAddress());
	        }
	        icmDocument.setUrlContent(strurl);
	        icmDocument.setPid(ddoDocument.getPidObject().pidString());
			lista.add(icmDocument);
		}
		
		return lista;
		
	}

	private Map<String, Object> getAttributes(DKDDO ddo) throws DKUsageError {
		Map<String, Object> attributes = new HashMap<String, Object>();

		for (short dataid = 1; dataid <= ddo.dataCount(); dataid++) {
			String key = ddo.getDataName(dataid);
			Object value = ddo.getData(dataid);
			attributes.put(key, value);
		}

		return attributes;
	}

	/**
	 * Busca un documento DDO usando un query XPath.
	 * 
	 * @param dsICM
	 *            conexion al ICM
	 * @param query
	 *            query XPath
	 * @return documento DDO
	 * @throws DKUsageError
	 * @throws DKException
	 * @throws Exception
	 */
	protected DKDDO findDDODocument(DKDatastoreICM dsICM, String query) throws DKUsageError, DKException, Exception {
		DKRetrieveOptionsICM dkRetrieveOptions = DKRetrieveOptionsICM.createInstance(dsICM);
		dkRetrieveOptions.baseAttributes(true);
		dkRetrieveOptions.partsList(true);
		dkRetrieveOptions.partsAttributes(true);

		DKNVPair options[] = new DKNVPair[3];
		options[0] = new DKNVPair(DKConstant.DK_CM_PARM_MAX_RESULTS, "1");
		options[1] = new DKNVPair(DKConstant.DK_CM_PARM_RETRIEVE, dkRetrieveOptions);
		options[2] = new DKNVPair(DKConstant.DK_CM_PARM_END, null);

		DKResults results = (DKResults) dsICM.evaluate(query, DKConstantICM.DK_CM_XQPE_QL_TYPE, options);
		dkIterator dkResultsIterator = results.createIterator();
		logger.debug("results cardinality:  " + results.cardinality());

		assertDocumentsCardinalityOne(results.cardinality());

		return (DKDDO) dkResultsIterator.next();
	}
	
	protected List<DKDDO> findDocuments(DKDatastoreICM dsICM, String query) throws DKUsageError, DKException, Exception {
		DKRetrieveOptionsICM dkRetrieveOptions = DKRetrieveOptionsICM.createInstance(dsICM);
		dkRetrieveOptions.baseAttributes(true);
		dkRetrieveOptions.partsList(true);
		dkRetrieveOptions.partsAttributes(true);
		
		DKNVPair options[] = new DKNVPair[3];
		options[0] = new DKNVPair(DKConstant.DK_CM_PARM_MAX_RESULTS, "0");
		options[1] = new DKNVPair(DKConstant.DK_CM_PARM_RETRIEVE, dkRetrieveOptions);
		options[2] = new DKNVPair(DKConstant.DK_CM_PARM_END, null);
		
		logger.info("query: {}, DKConstantICM.DK_CM_XQPE_QL_TYPE: {}, options[0]: {}, options[1]: {}, options[2]: {}", query, DKConstantICM.DK_CM_XQPE_QL_TYPE, options[0], options[1], options[2]);
		DKResults results = (DKResults) dsICM.evaluate(query, DKConstantICM.DK_CM_XQPE_QL_TYPE, options);
		dkIterator dkResultsIterator = results.createIterator();
		List<DKDDO> resultado =new ArrayList<DKDDO>();
		
		while(dkResultsIterator.more()){
			resultado.add((DKDDO) dkResultsIterator.next());
		}
		
		return resultado;
	}

	/**
	 * Verifica que la parte DKDDO sea una instancia de DKLobICM.
	 * 
	 * @param part
	 *            parte de documento DDO
	 */
	protected void assertDKLobICMPart(DKDDO part) {
		if (!(part instanceof DKLobICM)) {
			throw new ICMException(
					"No DKLobICM part Found!  DDO is either not an Item of a Document Model classified Item Type or Document has not been explicitly retrieved.");
		}
	}

	/**
	 * Verifica que la busqueda de como resultado solo un documento DDO.
	 * 
	 * @param cardinality
	 *            cardinalidad del resultado de la busqueda
	 */
	protected void assertDocumentsCardinalityOne(int cardinality) {
		if (cardinality == 0) {
			throw new ICMDocumentNotFoundException("No document found");
		} else if (cardinality > 1) {
			throw new ICMException("Too many documents found");
		}
	}

	/**
	 * Verifica que la busqueda de como resultado solo una parte DKDDO
	 * 
	 * @param cardinality
	 *            cardinalidad del resultado de la busqueda
	 */
	protected void assertPartsCardinalityOne(int cardinality) {
		if (cardinality == 0) {
			throw new ICMException("No parts found");
		} else if (cardinality > 1) {
			throw new ICMException("Too many parts found");
		}
	}

	/**
	 * Verifica que el documento DDO corresponda al modelo de documentos del ICM.
	 * 
	 * @param dataid
	 *            identificador del modelo del documento DDO
	 * @throws Exception
	 */
	protected void assertDocumentModelItem(short dataid) throws Exception {
		if (dataid == 0) {
			throw new ICMException(
					"No DKParts Attribute Found!  DDO is either not an Item of a Document Model classified Item Type or Document has not been explicitly retrieved.");
		}
	}

	/**
	 * Construye un query XPath para consultar por documentos DDO.
	 * 
	 * @param itemType
	 *            tipo del item a buscar
	 * @param attributes
	 *            atributos del item a buscar
	 * @return query XPath
	 */
	protected String buildQuery(String itemType, Map<String, Object> attributes) {
		List<String> entries = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			entries.add("@" + entry.getKey() + "=" + getFormattedValue(entry.getValue()));
		}

		StrBuilder builder = new StrBuilder();
		builder.append("/");
		builder.append(itemType);
		builder.append("[");
		builder.appendWithSeparators(entries, " and ");
		builder.append("]");

		return builder.toString();
	}

	private String getFormattedValue(Object value) {
		if (value instanceof Number) {
			return value.toString();
		} else {
			return "\"" + value + "\"";
		}
	}

}
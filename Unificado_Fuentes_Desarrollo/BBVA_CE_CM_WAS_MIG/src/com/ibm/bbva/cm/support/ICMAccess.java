package com.ibm.bbva.cm.support;

import java.util.List;

import com.ibm.bbva.cm.support.impl.ICMDocument;
import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.common.DKUsageError;
import com.ibm.mm.sdk.server.DKDatastoreICM;

public interface ICMAccess {

	public void store(DKDatastoreICM dsICM, ICMDocument icmDocument) throws DKUsageError, DKException, Exception;
	
	public String store_PID(DKDatastoreICM dsICM, ICMDocument icmDocument) throws DKUsageError, DKException, Exception;
	
	public void remove(DKDatastoreICM dsICM, ICMDocument icmDocument) throws DKUsageError, DKException, Exception;
	
	public ICMDocument retrieve(DKDatastoreICM dsICM, ICMDocument filter) throws DKUsageError, DKException, Exception;
	
	public List<ICMDocument> getDocuments(DKDatastoreICM dsICM, ICMDocument filter) throws DKUsageError, DKException, Exception;
}

package com.ibm.bbva.cm.support;

import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.common.DKUsageError;

public interface ICMCheckedCallback {

	public Object doInCallback() throws DKUsageError, DKException, Exception;
}

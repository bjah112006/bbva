package com.ibm.bbva.cm.support;

import com.ibm.mm.sdk.server.DKDatastoreICM;

public interface ICMCallback {

	public Object doInCallback(DKDatastoreICM dsICM);
}

package com.ibm.bbva.cm.servicepid;

import com.ibm.bbva.cm.bean.DocumentoPid;

public interface ContentServicePid {
	DocumentoPid[] findAll(DocumentoPid documento);
}

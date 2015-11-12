package com.ibm.bbva.cm.support;

import com.ibm.bbva.cm.support.impl.ICMDocument;


public interface DomainTranslator {

public Object translateToDomain(ICMDocument icmDocument);
	
	public ICMDocument translateToICMDocument(Object domain);
}

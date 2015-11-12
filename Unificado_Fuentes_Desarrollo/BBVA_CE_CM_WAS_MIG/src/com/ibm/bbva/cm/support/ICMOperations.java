package com.ibm.bbva.cm.support;

import java.util.List;

import com.ibm.bbva.cm.support.impl.ICMDocument;

public interface ICMOperations {

	public void store(ICMDocument icmDocument);
	
	public void store(List<ICMDocument> icmDocuments);
	
	public ICMDocument retrieve(ICMDocument filter);
	
	public void remove(ICMDocument icmDocument);
	
	public void remove(List<ICMDocument> icmDocuments);
}

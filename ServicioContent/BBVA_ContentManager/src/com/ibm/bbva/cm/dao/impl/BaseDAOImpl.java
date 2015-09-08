package com.ibm.bbva.cm.dao.impl;

import com.ibm.bbva.cm.support.DomainTranslator;
import com.ibm.bbva.cm.support.impl.DocumentTranslator;
import com.ibm.bbva.cm.support.impl.ICMTemplate;

public class BaseDAOImpl {

	// se inyectan por Spring
	private ICMTemplate icmTemplate;
	private DomainTranslator translator;
	private String mimeType;
	private String itemType;
	private String mimeType_Tiff;

	public BaseDAOImpl() {
		translator = new DocumentTranslator();
		icmTemplate = new ICMTemplate();
		mimeType = "application/pdf";
		mimeType_Tiff = "image/tiff";
		itemType = "FG_Document";
	}

	public ICMTemplate getIcmTemplate() {
		return icmTemplate;
	}

	public void setIcmTemplate(ICMTemplate icmTemplate) {
		this.icmTemplate = icmTemplate;
	}

	public DomainTranslator getTranslator() {
		return translator;
	}

	public void setTranslator(DomainTranslator translator) {
		this.translator = translator;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getMimeType_Tiff() {
		return mimeType_Tiff;
	}

	public void setMimeType_Tiff(String mimeType_Tiff) {
		this.mimeType_Tiff = mimeType_Tiff;
	}

}

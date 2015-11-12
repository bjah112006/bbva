package com.ibm.bbva.cm.support.impl;

import java.util.Map;

public class ICMDocument {

	private byte[] content;

	private String itemType;

	private Map<String, Object> attributes;

	private String mimeType;
	
	private String mimeType_Tiff;

	private String urlContent;
	
	private String pid;
	
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getMimeType_Tiff() {
		return mimeType_Tiff;
	}

	public void setMimeType_Tiff(String mimeType_Tiff) {
		this.mimeType_Tiff = mimeType_Tiff;
	}

	public String getUrlContent() {
		return urlContent;
	}

	public void setUrlContent(String urlContent) {
		this.urlContent = urlContent;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	
	
}

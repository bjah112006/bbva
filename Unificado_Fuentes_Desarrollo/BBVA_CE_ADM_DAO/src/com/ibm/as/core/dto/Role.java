package com.ibm.as.core.dto;

public class Role extends AbstractDTO {

	private static final long serialVersionUID = 1008934795028887165L;

	private String id = null;
	private String name = null;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

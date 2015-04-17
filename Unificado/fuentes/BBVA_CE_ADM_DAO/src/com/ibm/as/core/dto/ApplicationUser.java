package com.ibm.as.core.dto;

import java.util.ArrayList;
import java.util.List;

public class ApplicationUser extends AbstractDTO {

	private static final long serialVersionUID = 4595709132391102037L;

	public static final String OBJECT_ID = ApplicationUser.class.getName()+"@"+serialVersionUID;

	private String userId = null;
	private String name = null;
	private String lastName = null;
	private String email = null;
	private boolean active = false;

	private List<Role> roles = new ArrayList<Role>();
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}

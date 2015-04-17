package com.ibm.as.core.dto;

import java.util.Collections;
import java.util.List;

public class ApplicationResource extends AbstractDTO implements Comparable<ApplicationResource> {

	private static final long serialVersionUID = -701538954612070251L;

	public static final String OBJECT_ID = ApplicationResource.class.getName()+"@"+serialVersionUID;

	private String id = null;
	private String name = null;

	private List<ApplicationResource> children = null;

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

	public List<ApplicationResource> getChildren() {
		return children;
	}

	public void setChildren(List<ApplicationResource> children) {
		this.children = children;
	}

	public ApplicationResource containsChild(ApplicationResource filter) {
		if ( children==null )
			return null;

		ApplicationResource child = null;
		int idx = 0;
		
		if ( (idx=Collections.binarySearch(children, filter))>=0 )
			return children.get(idx);

		for ( ApplicationResource res : children ) {
			if ( (child=res.containsChild(filter))!=null )
				break;
		}

		return child;
	}

	public int compareTo(ApplicationResource r) {
		return this.getId().compareTo( r.getId() );
	}
}

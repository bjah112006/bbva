/**
 * 
 */
package com.ibm.as.core.ejb;


public abstract class AbstractSessionBean implements javax.ejb.SessionBean {

	/**
	 * 
	 */
	static final long serialVersionUID = 3206093459760846163L;

	private javax.ejb.SessionContext sessionCtx;

	/**
	 * getSessionContext
	 */
	public javax.ejb.SessionContext getSessionContext() {
		return sessionCtx;
	}

	/**
	 * setSessionContext
	 */
	public final void setSessionContext(javax.ejb.SessionContext ctx) {
		sessionCtx = ctx;
	}

	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws javax.ejb.CreateException {
	}

	/**
	 * ejbActivate
	 */
	public final void ejbActivate() {
	}

	/**
	 * ejbPassivate
	 */
	public final void ejbPassivate() {
	}

	/**
	 * ejbRemove
	 */
	public void ejbRemove() {

	}

}

package com.ibm.jsf2.custom.context;

import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.event.PhaseId;

public class CustomPartialViewContext extends PartialViewContextWrapper {

	private PartialViewContext wrapped;
	private PartialResponseWriter writer;

	public CustomPartialViewContext(PartialViewContext wrapped) {
		this.wrapped = wrapped;
		this.writer = new CustomPartialResponseWriter(wrapped.getPartialResponseWriter());
	}

	@Override
	public PartialResponseWriter getPartialResponseWriter() {
		return writer;
	}

	@Override
	public PartialViewContext getWrapped() {
		return wrapped;
	}

	@Override
	public void setPartialRequest(boolean isPartialRequest) {
		wrapped.setPartialRequest(isPartialRequest);
	}

}

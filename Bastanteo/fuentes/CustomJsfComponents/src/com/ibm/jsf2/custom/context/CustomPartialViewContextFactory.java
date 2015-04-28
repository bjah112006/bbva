package com.ibm.jsf2.custom.context;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;

import org.apache.myfaces.context.PartialViewContextFactoryImpl;

public class CustomPartialViewContextFactory extends PartialViewContextFactoryImpl {

	private PartialViewContextFactory wrapped;

    public CustomPartialViewContextFactory(PartialViewContextFactory wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public PartialViewContext getPartialViewContext(FacesContext context) {
        return new CustomPartialViewContext(wrapped.getPartialViewContext(context));
    }

}
